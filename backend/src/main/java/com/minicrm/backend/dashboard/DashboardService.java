package com.minicrm.backend.dashboard;

import com.minicrm.backend.campaign.CampaignMetrics;
import com.minicrm.backend.campaign.CampaignMetricsRepository;
import com.minicrm.backend.campaign.CampaignMetricsResponse;
import com.minicrm.backend.campaign.CampaignRepository;
import com.minicrm.backend.campaign.CampaignResponse;
import com.minicrm.backend.campaign.CampaignService;
import com.minicrm.backend.campaign.CampaignStatus;
import com.minicrm.backend.common.Rates;
import com.minicrm.backend.customer.CustomerRepository;
import com.minicrm.backend.segment.SegmentRepository;
import com.minicrm.backend.segment.SegmentResponse;
import com.minicrm.backend.segment.SegmentService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 대시보드 집계. activeCampaigns는 RUNNING 상태의 캠페인 수다.
@Service
@Transactional(readOnly = true)
public class DashboardService {

	private final CustomerRepository customerRepository;
	private final SegmentRepository segmentRepository;
	private final CampaignRepository campaignRepository;
	private final CampaignMetricsRepository campaignMetricsRepository;
	private final SegmentService segmentService;
	private final CampaignService campaignService;

	public DashboardService(CustomerRepository customerRepository, SegmentRepository segmentRepository,
			CampaignRepository campaignRepository, CampaignMetricsRepository campaignMetricsRepository,
			SegmentService segmentService, CampaignService campaignService) {
		this.customerRepository = customerRepository;
		this.segmentRepository = segmentRepository;
		this.campaignRepository = campaignRepository;
		this.campaignMetricsRepository = campaignMetricsRepository;
		this.segmentService = segmentService;
		this.campaignService = campaignService;
	}

	public DashboardSummaryResponse getSummary() {
		List<CampaignMetrics> allMetrics = campaignMetricsRepository.findAll();
		long totalSent = allMetrics.stream().mapToLong(CampaignMetrics::getSentCount).sum();
		long totalOpen = allMetrics.stream().mapToLong(CampaignMetrics::getOpenCount).sum();
		long totalClicks = allMetrics.stream().mapToLong(CampaignMetrics::getClickCount).sum();
		long totalConversions = allMetrics.stream().mapToLong(CampaignMetrics::getConversionCount).sum();
		BigDecimal totalRevenue = allMetrics.stream()
				.map(CampaignMetrics::getRevenue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return new DashboardSummaryResponse(
				customerRepository.count(),
				segmentRepository.count(),
				campaignRepository.count(),
				campaignRepository.countByStatus(CampaignStatus.RUNNING),
				totalSent,
				totalOpen,
				totalClicks,
				totalConversions,
				totalRevenue,
				Rates.of(totalOpen, totalSent),
				Rates.of(totalClicks, totalSent),
				Rates.of(totalConversions, totalSent),
				campaignService.getRecentCampaigns().stream().map(DashboardService::toRecentCampaign).toList(),
				segmentService.getSegments().stream().map(DashboardService::toSegmentSize).toList());
	}

	private static DashboardSummaryResponse.RecentCampaign toRecentCampaign(CampaignResponse campaign) {
		CampaignMetricsResponse metrics = campaign.metrics();
		return new DashboardSummaryResponse.RecentCampaign(
				campaign.id(),
				campaign.name(),
				campaign.channel(),
				campaign.status(),
				campaign.segmentName(),
				campaign.targetCount(),
				metrics != null ? metrics.sentCount() : 0,
				metrics != null ? metrics.openCount() : 0,
				metrics != null ? metrics.clickCount() : 0,
				metrics != null ? metrics.conversionCount() : 0,
				metrics != null ? metrics.revenue() : BigDecimal.ZERO);
	}

	private static DashboardSummaryResponse.SegmentSize toSegmentSize(SegmentResponse segment) {
		return new DashboardSummaryResponse.SegmentSize(segment.id(), segment.name(), segment.customerCount());
	}

}
