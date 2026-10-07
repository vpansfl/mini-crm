package com.minicrm.backend.customer;

import java.time.Instant;
import java.time.LocalDateTime;

// 기존 필드는 그대로 두고 새 필드를 뒤에 추가한다. (기존 프론트 호환)
public record CustomerResponse(
		Long id,
		String hubspotId,
		String firstName,
		String lastName,
		String email,
		String companyName,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		String phone,
		String mobilePhone,
		String jobTitle,
		String city,
		String state,
		String country,
		String lifecycleStage,
		String leadStatus,
		Long companyId,
		Instant hubspotCreatedAt,
		Instant hubspotUpdatedAt) {

	public static CustomerResponse from(Customer customer) {
		return new CustomerResponse(
				customer.getId(),
				customer.getHubspotId(),
				customer.getFirstName(),
				customer.getLastName(),
				customer.getEmail(),
				customer.getCompanyName(),
				customer.getCreatedAt(),
				customer.getUpdatedAt(),
				customer.getPhone(),
				customer.getMobilePhone(),
				customer.getJobTitle(),
				customer.getCity(),
				customer.getState(),
				customer.getCountry(),
				customer.getLifecycleStage(),
				customer.getLeadStatus(),
				customer.getCompany() != null ? customer.getCompany().getId() : null,
				customer.getHubspotCreatedAt(),
				customer.getHubspotUpdatedAt());
	}

}
