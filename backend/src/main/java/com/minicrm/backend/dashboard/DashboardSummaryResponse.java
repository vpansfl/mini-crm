package com.minicrm.backend.dashboard;

import com.minicrm.backend.campaign.CampaignChannel;
import com.minicrm.backend.campaign.CampaignStatus;
import java.math.BigDecimal;
import java.util.List;

// 비율은 모두 전체 발송 수 대비 (0~1)
public record DashboardSummaryResponse(
		long totalCustomers,
		long totalSegments,
		long totalCampaigns,
		long activeCampaigns,
		long totalSent,
		long totalOpen,
		long totalClicks,
		long totalConversions,
		BigDecimal totalRevenue,
		double openRate,
		double clickRate,
		double conversionRate,
		List<RecentCampaign> recentCampaigns,
		List<SegmentSize> segmentSizes) {

	public record RecentCampaign(
			Long id,
			String name,
			CampaignChannel channel,
			CampaignStatus status,
			String segmentName,
			long targetCount,
			int sentCount,
			int openCount,
			int clickCount,
			int conversionCount,
			BigDecimal revenue) {
	}

	public record SegmentSize(
			Long id,
			String name,
			long customerCount) {
	}

}
