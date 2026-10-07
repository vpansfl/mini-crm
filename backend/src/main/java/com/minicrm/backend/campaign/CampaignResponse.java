package com.minicrm.backend.campaign;

import java.time.LocalDateTime;

// targetCount: 대상 세그먼트의 현재 고객 수, metrics: 성과 (시뮬레이션 전에는 null)
public record CampaignResponse(
		Long id,
		String name,
		Long segmentId,
		String segmentName,
		CampaignChannel channel,
		String message,
		CampaignStatus status,
		LocalDateTime startAt,
		LocalDateTime endAt,
		long targetCount,
		CampaignMetricsResponse metrics,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {

	public static CampaignResponse from(Campaign campaign, long targetCount, CampaignMetrics metrics) {
		return new CampaignResponse(
				campaign.getId(),
				campaign.getName(),
				campaign.getSegment().getId(),
				campaign.getSegment().getName(),
				campaign.getChannel(),
				campaign.getMessage(),
				campaign.getStatus(),
				campaign.getStartAt(),
				campaign.getEndAt(),
				targetCount,
				metrics != null ? CampaignMetricsResponse.from(metrics) : null,
				campaign.getCreatedAt(),
				campaign.getUpdatedAt());
	}

}
