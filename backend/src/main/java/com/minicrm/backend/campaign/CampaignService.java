package com.minicrm.backend.campaign;

import com.minicrm.backend.exception.InvalidRequestException;
import com.minicrm.backend.segment.Segment;
import com.minicrm.backend.segment.SegmentNotFoundException;
import com.minicrm.backend.segment.SegmentRepository;
import com.minicrm.backend.segment.SegmentService;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 캠페인 CRUD와 성과(시뮬레이션) 관리. 실제 메시지는 발송하지 않는다.
@Service
@Transactional(readOnly = true)
public class CampaignService {

	private final CampaignRepository campaignRepository;
	private final CampaignMetricsRepository campaignMetricsRepository;
	private final SegmentRepository segmentRepository;
	private final SegmentService segmentService;
	private final CampaignMetricsSimulator metricsSimulator;

	public CampaignService(CampaignRepository campaignRepository, CampaignMetricsRepository campaignMetricsRepository,
			SegmentRepository segmentRepository, SegmentService segmentService,
			CampaignMetricsSimulator metricsSimulator) {
		this.campaignRepository = campaignRepository;
		this.campaignMetricsRepository = campaignMetricsRepository;
		this.segmentRepository = segmentRepository;
		this.segmentService = segmentService;
		this.metricsSimulator = metricsSimulator;
	}

	public List<CampaignResponse> getCampaigns() {
		return toResponses(campaignRepository.findAllByOrderByIdDesc());
	}

	// 대시보드용 최근 캠페인
	public List<CampaignResponse> getRecentCampaigns() {
		return toResponses(campaignRepository.findTop10ByOrderByIdDesc());
	}

	public CampaignResponse getCampaign(Long id) {
		return toResponse(findCampaign(id));
	}

	public CampaignMetricsResponse getMetrics(Long id) {
		findCampaign(id);
		return campaignMetricsRepository.findByCampaign_Id(id)
				.map(CampaignMetricsResponse::from)
				.orElseThrow(() -> new CampaignMetricsNotFoundException(id));
	}

	@Transactional
	public CampaignResponse createCampaign(CampaignRequest request) {
		validateSchedule(request);
		Campaign campaign = new Campaign(
				request.name().trim(),
				findSegment(request.segmentId()),
				request.channel(),
				request.message().trim(),
				request.status() != null ? request.status() : CampaignStatus.DRAFT,
				request.startAt(),
				request.endAt());
		return toResponse(campaignRepository.save(campaign));
	}

	@Transactional
	public CampaignResponse updateCampaign(Long id, CampaignRequest request) {
		validateSchedule(request);
		Campaign campaign = findCampaign(id);
		campaign.update(
				request.name().trim(),
				findSegment(request.segmentId()),
				request.channel(),
				request.message().trim(),
				request.status() != null ? request.status() : campaign.getStatus(),
				request.startAt(),
				request.endAt());
		// flush해서 @UpdateTimestamp 값이 응답에 반영되도록 한다.
		return toResponse(campaignRepository.saveAndFlush(campaign));
	}

	@Transactional
	public void deleteCampaign(Long id) {
		Campaign campaign = findCampaign(id);
		campaignMetricsRepository.deleteByCampaignId(id);
		campaignRepository.delete(campaign);
	}

	// 대상 세그먼트의 현재 고객 수로 성과를 생성한다. 캠페인당 metrics는 하나만 유지하며, 다시 실행하면 갱신된다.
	@Transactional
	public CampaignMetricsResponse simulateResults(Long id) {
		Campaign campaign = findCampaign(id);
		long targetCount = segmentService.getMemberCount(campaign.getSegment().getId());
		CampaignMetricsSimulator.SimulatedMetrics simulated = metricsSimulator.simulate(campaign.getChannel(),
				targetCount);

		CampaignMetrics metrics = campaignMetricsRepository.findByCampaign_Id(id)
				.orElseGet(() -> new CampaignMetrics(campaign));
		metrics.update(simulated.sentCount(), simulated.openCount(), simulated.clickCount(),
				simulated.conversionCount(), simulated.revenue());
		// flush해서 @CreationTimestamp/@UpdateTimestamp 값이 응답에 반영되도록 한다.
		return CampaignMetricsResponse.from(campaignMetricsRepository.saveAndFlush(metrics));
	}

	private List<CampaignResponse> toResponses(List<Campaign> campaigns) {
		Map<Long, Long> counts = segmentService.getMemberCounts();
		Map<Long, CampaignMetrics> metrics = campaignMetricsRepository
				.findAllByCampaign_IdIn(campaigns.stream().map(Campaign::getId).toList())
				.stream()
				.collect(Collectors.toMap(m -> m.getCampaign().getId(), Function.identity()));
		return campaigns.stream()
				.map(campaign -> CampaignResponse.from(campaign,
						counts.getOrDefault(campaign.getSegment().getId(), 0L),
						metrics.get(campaign.getId())))
				.toList();
	}

	private CampaignResponse toResponse(Campaign campaign) {
		return CampaignResponse.from(campaign,
				segmentService.getMemberCount(campaign.getSegment().getId()),
				campaignMetricsRepository.findByCampaign_Id(campaign.getId()).orElse(null));
	}

	private Campaign findCampaign(Long id) {
		return campaignRepository.findWithSegmentById(id).orElseThrow(() -> new CampaignNotFoundException(id));
	}

	private Segment findSegment(Long segmentId) {
		return segmentRepository.findById(segmentId).orElseThrow(() -> new SegmentNotFoundException(segmentId));
	}

	private static void validateSchedule(CampaignRequest request) {
		if (request.startAt() != null && request.endAt() != null && request.endAt().isBefore(request.startAt())) {
			throw new InvalidRequestException("종료 일시는 시작 일시보다 빠를 수 없습니다.");
		}
	}

}
