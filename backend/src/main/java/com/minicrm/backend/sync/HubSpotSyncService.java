package com.minicrm.backend.sync;

import com.minicrm.backend.company.CompanyService;
import com.minicrm.backend.customer.CustomerService;
import com.minicrm.backend.deal.DealService;
import com.minicrm.backend.hubspot.HubSpotCompany;
import com.minicrm.backend.hubspot.HubSpotContact;
import com.minicrm.backend.hubspot.HubSpotCrmClient;
import com.minicrm.backend.hubspot.HubSpotDeal;
import com.minicrm.backend.segment.SegmentService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// Company → Contact → Deal 순서로 전체 동기화한다. (참조 대상이 먼저 저장되어야 관계를 연결할 수 있다)
// HubSpot 데이터를 모두 조회한 뒤 저장하므로, 조회 중 오류가 나면 DB는 변경되지 않는다.
// 저장은 객체 종류별로 각각의 트랜잭션에서 처리된다.
@Service
public class HubSpotSyncService {

	private static final Logger log = LoggerFactory.getLogger(HubSpotSyncService.class);

	private final HubSpotCrmClient hubSpotCrmClient;
	private final CompanyService companyService;
	private final CustomerService customerService;
	private final DealService dealService;
	private final SegmentService segmentService;

	public HubSpotSyncService(HubSpotCrmClient hubSpotCrmClient, CompanyService companyService,
			CustomerService customerService, DealService dealService, SegmentService segmentService) {
		this.hubSpotCrmClient = hubSpotCrmClient;
		this.companyService = companyService;
		this.customerService = customerService;
		this.dealService = dealService;
		this.segmentService = segmentService;
	}

	public HubSpotSyncResponse syncAll() {
		List<HubSpotCompany> companies = hubSpotCrmClient.fetchAllCompanies();
		List<HubSpotContact> contacts = hubSpotCrmClient.fetchAllContacts();
		List<HubSpotDeal> deals = hubSpotCrmClient.fetchAllDeals();

		HubSpotSyncResponse result = new HubSpotSyncResponse(
				companyService.upsertFromHubSpot(companies),
				customerService.upsertFromHubSpot(contacts),
				dealService.upsertFromHubSpot(deals));
		segmentService.refreshAllSegments();
		log.info("HubSpot 전체 동기화 완료: {}", result);
		return result;
	}

}
