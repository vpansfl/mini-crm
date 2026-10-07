package com.minicrm.backend.hubspot;

import java.time.Instant;

// HubSpot Contact 중 CRM에서 사용하는 값만 담는다.
public record HubSpotContact(
		String id,
		String firstName,
		String lastName,
		String email,
		String phone,
		String mobilePhone,
		String company,
		String jobTitle,
		String city,
		String state,
		String country,
		String lifecycleStage,
		String leadStatus,
		String associatedCompanyId,
		Instant createdAt,
		Instant updatedAt) {
}
