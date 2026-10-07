package com.minicrm.backend.deal;

import com.minicrm.backend.common.SyncResult;
import com.minicrm.backend.company.Company;
import com.minicrm.backend.company.CompanyNotFoundException;
import com.minicrm.backend.company.CompanyRepository;
import com.minicrm.backend.customer.Customer;
import com.minicrm.backend.customer.CustomerNotFoundException;
import com.minicrm.backend.customer.CustomerRepository;
import com.minicrm.backend.hubspot.HubSpotDeal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DealService {

	private final DealRepository dealRepository;
	private final CompanyRepository companyRepository;
	private final CustomerRepository customerRepository;

	public DealService(DealRepository dealRepository, CompanyRepository companyRepository,
			CustomerRepository customerRepository) {
		this.dealRepository = dealRepository;
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
	}

	public List<DealResponse> getDeals() {
		return dealRepository.findAllByOrderByIdDesc()
				.stream()
				.map(DealResponse::from)
				.toList();
	}

	public DealResponse getDeal(Long id) {
		return dealRepository.findWithDetailsById(id)
				.map(DealResponse::from)
				.orElseThrow(() -> new DealNotFoundException(id));
	}

	public List<DealResponse> getDealsByCompany(Long companyId) {
		if (!companyRepository.existsById(companyId)) {
			throw new CompanyNotFoundException(companyId);
		}
		return dealRepository.findAllByCompany_IdOrderByIdDesc(companyId)
				.stream()
				.map(DealResponse::from)
				.toList();
	}

	public List<DealResponse> getDealsByCustomer(Long customerId) {
		if (!customerRepository.existsById(customerId)) {
			throw new CustomerNotFoundException(customerId);
		}
		return dealRepository.findAllByCustomers_IdOrderByIdDesc(customerId)
				.stream()
				.map(DealResponse::from)
				.toList();
	}

	// hubspotId 기준으로 기존 거래는 update, 없으면 insert 한다. 전체가 하나의 트랜잭션으로 처리된다.
	// 회사/고객 연결은 이미 동기화된 레코드가 있을 때만 맺는다.
	@Transactional
	public SyncResult upsertFromHubSpot(List<HubSpotDeal> deals) {
		Map<String, Deal> existing = dealRepository
				.findAllByHubspotIdIn(deals.stream().map(HubSpotDeal::id).toList())
				.stream()
				.collect(Collectors.toMap(Deal::getHubspotId, Function.identity()));
		Map<String, Company> companies = companyRepository
				.findAllByHubspotIdIn(deals.stream()
						.map(HubSpotDeal::companyId)
						.filter(Objects::nonNull)
						.distinct()
						.toList())
				.stream()
				.collect(Collectors.toMap(Company::getHubspotId, Function.identity()));
		Map<String, Customer> customers = customerRepository
				.findAllByHubspotIdIn(deals.stream()
						.flatMap(deal -> deal.contactIds().stream())
						.distinct()
						.toList())
				.stream()
				.collect(Collectors.toMap(Customer::getHubspotId, Function.identity()));

		int created = 0;
		int updated = 0;
		int unchanged = 0;
		for (HubSpotDeal source : deals) {
			Company company = source.companyId() != null ? companies.get(source.companyId()) : null;
			Set<Customer> dealCustomers = source.contactIds().stream()
					.map(customers::get)
					.filter(Objects::nonNull)
					.collect(Collectors.toCollection(HashSet::new));

			Deal deal = existing.get(source.id());
			if (deal == null) {
				deal = new Deal(source.id());
				deal.applyHubSpot(source, company, dealCustomers);
				dealRepository.save(deal);
				existing.put(source.id(), deal);
				created++;
			}
			else if (deal.applyHubSpot(source, company, dealCustomers)) {
				updated++;
			}
			else {
				unchanged++;
			}
		}
		return new SyncResult(deals.size(), created, updated, unchanged);
	}

}
