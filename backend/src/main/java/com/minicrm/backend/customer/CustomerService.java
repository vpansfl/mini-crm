package com.minicrm.backend.customer;

import com.minicrm.backend.common.SyncResult;
import com.minicrm.backend.company.Company;
import com.minicrm.backend.company.CompanyNotFoundException;
import com.minicrm.backend.company.CompanyRepository;
import com.minicrm.backend.hubspot.HubSpotContact;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final CompanyRepository companyRepository;

	public CustomerService(CustomerRepository customerRepository, CompanyRepository companyRepository) {
		this.customerRepository = customerRepository;
		this.companyRepository = companyRepository;
	}

	public List<CustomerResponse> getCustomers() {
		return customerRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
				.stream()
				.map(CustomerResponse::from)
				.toList();
	}

	public CustomerResponse getCustomer(Long id) {
		return customerRepository.findById(id)
				.map(CustomerResponse::from)
				.orElseThrow(() -> new CustomerNotFoundException(id));
	}

	public List<CustomerResponse> getCustomersByCompany(Long companyId) {
		if (!companyRepository.existsById(companyId)) {
			throw new CompanyNotFoundException(companyId);
		}
		return customerRepository.findAllByCompany_IdOrderByIdDesc(companyId)
				.stream()
				.map(CustomerResponse::from)
				.toList();
	}

	// hubspotId 기준으로 기존 고객은 update, 없으면 insert 한다. 전체가 하나의 트랜잭션으로 처리된다.
	// 대표 회사(associatedcompanyid)는 이미 동기화된 회사가 있을 때만 연결한다.
	@Transactional
	public SyncResult upsertFromHubSpot(List<HubSpotContact> contacts) {
		Map<String, Customer> existing = customerRepository
				.findAllByHubspotIdIn(contacts.stream().map(HubSpotContact::id).toList())
				.stream()
				.collect(Collectors.toMap(Customer::getHubspotId, Function.identity()));
		Map<String, Company> companies = companyRepository
				.findAllByHubspotIdIn(contacts.stream()
						.map(HubSpotContact::associatedCompanyId)
						.filter(Objects::nonNull)
						.distinct()
						.toList())
				.stream()
				.collect(Collectors.toMap(Company::getHubspotId, Function.identity()));

		int created = 0;
		int updated = 0;
		int unchanged = 0;
		for (HubSpotContact contact : contacts) {
			Company company = contact.associatedCompanyId() != null ? companies.get(contact.associatedCompanyId())
					: null;
			Customer customer = existing.get(contact.id());
			if (customer == null) {
				customer = new Customer(contact.id());
				customer.applyHubSpot(contact, company);
				customerRepository.save(customer);
				existing.put(contact.id(), customer);
				created++;
			}
			else if (customer.applyHubSpot(contact, company)) {
				updated++;
			}
			else {
				unchanged++;
			}
		}
		return new SyncResult(contacts.size(), created, updated, unchanged);
	}

}
