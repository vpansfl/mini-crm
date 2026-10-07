package com.minicrm.backend.company;

import com.minicrm.backend.common.SyncResult;
import com.minicrm.backend.hubspot.HubSpotCompany;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CompanyService {

	private final CompanyRepository companyRepository;

	public CompanyService(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	public List<CompanyResponse> getCompanies() {
		return companyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
				.stream()
				.map(CompanyResponse::from)
				.toList();
	}

	public CompanyResponse getCompany(Long id) {
		return companyRepository.findById(id)
				.map(CompanyResponse::from)
				.orElseThrow(() -> new CompanyNotFoundException(id));
	}

	// hubspotId 기준으로 기존 회사는 update, 없으면 insert 한다. 전체가 하나의 트랜잭션으로 처리된다.
	@Transactional
	public SyncResult upsertFromHubSpot(List<HubSpotCompany> companies) {
		Map<String, Company> existing = companyRepository
				.findAllByHubspotIdIn(companies.stream().map(HubSpotCompany::id).toList())
				.stream()
				.collect(Collectors.toMap(Company::getHubspotId, Function.identity()));

		int created = 0;
		int updated = 0;
		int unchanged = 0;
		for (HubSpotCompany source : companies) {
			Company company = existing.get(source.id());
			if (company == null) {
				company = new Company(source.id());
				company.applyHubSpot(source);
				companyRepository.save(company);
				existing.put(source.id(), company);
				created++;
			}
			else if (company.applyHubSpot(source)) {
				updated++;
			}
			else {
				unchanged++;
			}
		}
		return new SyncResult(companies.size(), created, updated, unchanged);
	}

}
