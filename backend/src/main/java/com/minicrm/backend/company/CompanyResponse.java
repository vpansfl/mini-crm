package com.minicrm.backend.company;

import java.time.Instant;
import java.time.LocalDateTime;

public record CompanyResponse(
		Long id,
		String hubspotId,
		String name,
		String domain,
		String industry,
		String phone,
		String city,
		String state,
		String country,
		Instant hubspotCreatedAt,
		Instant hubspotUpdatedAt,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {

	public static CompanyResponse from(Company company) {
		return new CompanyResponse(
				company.getId(),
				company.getHubspotId(),
				company.getName(),
				company.getDomain(),
				company.getIndustry(),
				company.getPhone(),
				company.getCity(),
				company.getState(),
				company.getCountry(),
				company.getHubspotCreatedAt(),
				company.getHubspotUpdatedAt(),
				company.getCreatedAt(),
				company.getUpdatedAt());
	}

}
