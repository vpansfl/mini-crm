package com.minicrm.backend.hubspot;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

// HubSpot Deal 중 CRM에서 사용하는 값과 연관된 Company/Contact ID를 담는다.
public record HubSpotDeal(
		String id,
		String dealName,
		BigDecimal amount,
		String pipeline,
		String dealStage,
		Instant closeDate,
		Instant createdAt,
		Instant updatedAt,
		String companyId,
		List<String> contactIds) {
}
