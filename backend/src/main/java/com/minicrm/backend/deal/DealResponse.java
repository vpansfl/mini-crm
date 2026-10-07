package com.minicrm.backend.deal;

import com.minicrm.backend.customer.Customer;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record DealResponse(
		Long id,
		String hubspotId,
		String dealName,
		BigDecimal amount,
		String pipeline,
		String dealStage,
		Instant closeDate,
		Long companyId,
		String companyName,
		List<Long> customerIds,
		Instant hubspotCreatedAt,
		Instant hubspotUpdatedAt,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {

	public static DealResponse from(Deal deal) {
		return new DealResponse(
				deal.getId(),
				deal.getHubspotId(),
				deal.getDealName(),
				deal.getAmount(),
				deal.getPipeline(),
				deal.getDealStage(),
				deal.getCloseDate(),
				deal.getCompany() != null ? deal.getCompany().getId() : null,
				deal.getCompany() != null ? deal.getCompany().getName() : null,
				deal.getCustomers().stream().map(Customer::getId).sorted().toList(),
				deal.getHubspotCreatedAt(),
				deal.getHubspotUpdatedAt(),
				deal.getCreatedAt(),
				deal.getUpdatedAt());
	}

}
