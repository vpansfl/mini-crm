package com.minicrm.backend.customer;

import com.minicrm.backend.common.SyncResult;
import com.minicrm.backend.hubspot.HubSpotContact;
import com.minicrm.backend.hubspot.HubSpotCrmClient;
import com.minicrm.backend.segment.SegmentService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// Contact만 동기화한다. (POST /api/customers/sync, 기존 API 유지용)
// Company/Deal까지 포함한 전체 동기화는 HubSpotSyncService를 사용한다.
// HubSpot 조회(외부 호출)와 DB 저장(트랜잭션)을 분리해, 외부 API를 기다리는 동안 DB 커넥션을 점유하지 않는다.
@Service
public class CustomerSyncService {

	private static final Logger log = LoggerFactory.getLogger(CustomerSyncService.class);

	private final HubSpotCrmClient hubSpotCrmClient;
	private final CustomerService customerService;
	private final SegmentService segmentService;

	public CustomerSyncService(HubSpotCrmClient hubSpotCrmClient, CustomerService customerService,
			SegmentService segmentService) {
		this.hubSpotCrmClient = hubSpotCrmClient;
		this.customerService = customerService;
		this.segmentService = segmentService;
	}

	public SyncResult syncFromHubSpot() {
		List<HubSpotContact> contacts = hubSpotCrmClient.fetchAllContacts();
		SyncResult result = customerService.upsertFromHubSpot(contacts);
		// 고객 데이터가 바뀌었으므로 세그먼트 멤버를 다시 계산한다.
		segmentService.refreshAllSegments();
		log.info("HubSpot 고객 동기화 완료: {}", result);
		return result;
	}

}
