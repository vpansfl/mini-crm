package com.minicrm.backend.hubspot;

import java.time.Instant;

// HubSpot Company 중 CRM에서 사용하는 값만 담는다.
public record HubSpotCompany(
		String id,
		String name,
		String domain,
		String industry,
		String phone,
		String city,
		String state,
		String country,
		Instant createdAt,
		Instant updatedAt) {
}
