package com.minicrm.backend.campaign;

import com.minicrm.backend.common.Rates;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// 비율은 모두 발송 수 대비 (0~1)
public record CampaignMetricsResponse(
		Long campaignId,
		int sentCount,
		int openCount,
		int clickCount,
		int conversionCount,
		BigDecimal revenue,
		double openRate,
		double clickRate,
		double conversionRate,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {

	public static CampaignMetricsResponse from(CampaignMetrics metrics) {
		return new CampaignMetricsResponse(
				metrics.getCampaign().getId(),
				metrics.getSentCount(),
				metrics.getOpenCount(),
				metrics.getClickCount(),
				metrics.getConversionCount(),
				metrics.getRevenue(),
				Rates.of(metrics.getOpenCount(), metrics.getSentCount()),
				Rates.of(metrics.getClickCount(), metrics.getSentCount()),
				Rates.of(metrics.getConversionCount(), metrics.getSentCount()),
				metrics.getCreatedAt(),
				metrics.getUpdatedAt());
	}

}
