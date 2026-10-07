package com.minicrm.backend.segment;

import com.minicrm.backend.campaign.CampaignRepository;
import com.minicrm.backend.customer.Customer;
import com.minicrm.backend.customer.CustomerRepository;
import com.minicrm.backend.customer.CustomerResponse;
import com.minicrm.backend.exception.ConflictException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 세그먼트 CRUD와 멤버(segment_members) 계산을 담당한다.
// 멤버는 저장 시점의 customers 데이터로 계산되며, 고객 데이터가 바뀌면 refresh로 다시 계산한다.
@Service
@Transactional(readOnly = true)
public class SegmentService {

	private final SegmentRepository segmentRepository;
	private final SegmentMemberRepository segmentMemberRepository;
	private final CustomerRepository customerRepository;
	private final CampaignRepository campaignRepository;
	private final SegmentRuleEvaluator ruleEvaluator;

	public SegmentService(SegmentRepository segmentRepository, SegmentMemberRepository segmentMemberRepository,
			CustomerRepository customerRepository, CampaignRepository campaignRepository,
			SegmentRuleEvaluator ruleEvaluator) {
		this.segmentRepository = segmentRepository;
		this.segmentMemberRepository = segmentMemberRepository;
		this.customerRepository = customerRepository;
		this.campaignRepository = campaignRepository;
		this.ruleEvaluator = ruleEvaluator;
	}

	public List<SegmentResponse> getSegments() {
		Map<Long, Long> counts = getMemberCounts();
		return segmentRepository.findAllByOrderByIdDesc()
				.stream()
				.map(segment -> SegmentResponse.from(segment, counts.getOrDefault(segment.getId(), 0L)))
				.toList();
	}

	public SegmentResponse getSegment(Long id) {
		Segment segment = findSegment(id);
		return SegmentResponse.from(segment, getMemberCount(id));
	}

	public List<CustomerResponse> getSegmentCustomers(Long id) {
		findSegment(id);
		return segmentMemberRepository.findCustomersBySegmentId(id)
				.stream()
				.map(CustomerResponse::from)
				.toList();
	}

	public SegmentPreviewResponse preview(SegmentRule rule) {
		return new SegmentPreviewResponse(findMatchingCustomers(rule, customerRepository.findAll()).size());
	}

	@Transactional
	public SegmentResponse createSegment(SegmentRequest request) {
		Segment segment = segmentRepository.save(
				new Segment(request.name().trim(), normalize(request.description()), request.toRule()));
		long count = refreshMembers(segment, customerRepository.findAll());
		return SegmentResponse.from(segment, count);
	}

	@Transactional
	public SegmentResponse updateSegment(Long id, SegmentRequest request) {
		Segment segment = findSegment(id);
		segment.update(request.name().trim(), normalize(request.description()), request.toRule());
		long count = refreshMembers(segment, customerRepository.findAll());
		// flush해서 @UpdateTimestamp 값이 응답에 반영되도록 한다.
		return SegmentResponse.from(segmentRepository.saveAndFlush(segment), count);
	}

	@Transactional
	public void deleteSegment(Long id) {
		Segment segment = findSegment(id);
		if (campaignRepository.existsBySegment_Id(id)) {
			throw new ConflictException("이 세그먼트를 대상으로 하는 캠페인이 있어 삭제할 수 없습니다. 캠페인을 먼저 삭제하세요.");
		}
		segmentMemberRepository.deleteBySegmentId(id);
		segmentRepository.delete(segment);
	}

	@Transactional
	public SegmentResponse refreshSegment(Long id) {
		Segment segment = findSegment(id);
		long count = refreshMembers(segment, customerRepository.findAll());
		return SegmentResponse.from(segment, count);
	}

	// 고객 동기화 후 모든 세그먼트를 다시 계산한다.
	@Transactional
	public void refreshAllSegments() {
		List<Customer> customers = customerRepository.findAll();
		segmentRepository.findAll().forEach(segment -> refreshMembers(segment, customers));
	}

	public long getMemberCount(Long segmentId) {
		return segmentMemberRepository.countBySegment_Id(segmentId);
	}

	public Map<Long, Long> getMemberCounts() {
		return segmentMemberRepository.countGroupBySegment()
				.stream()
				.collect(Collectors.toMap(SegmentMemberRepository.SegmentMemberCount::getSegmentId,
						SegmentMemberRepository.SegmentMemberCount::getMemberCount));
	}

	private long refreshMembers(Segment segment, List<Customer> customers) {
		List<Customer> matched = findMatchingCustomers(segment.rule(), customers);
		segmentMemberRepository.deleteBySegmentId(segment.getId());
		segmentMemberRepository.saveAll(matched.stream().map(customer -> new SegmentMember(segment, customer)).toList());
		segment.markRefreshed(LocalDateTime.now());
		return matched.size();
	}

	private List<Customer> findMatchingCustomers(SegmentRule rule, List<Customer> customers) {
		return customers.stream().filter(ruleEvaluator.toPredicate(rule)).toList();
	}

	private Segment findSegment(Long id) {
		return segmentRepository.findById(id).orElseThrow(() -> new SegmentNotFoundException(id));
	}

	private static String normalize(String value) {
		return (value == null || value.isBlank()) ? null : value.trim();
	}

}
