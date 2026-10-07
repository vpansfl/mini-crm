package com.minicrm.backend.campaign;

import com.minicrm.backend.exception.ResourceNotFoundException;

public class CampaignMetricsNotFoundException extends ResourceNotFoundException {

	public CampaignMetricsNotFoundException(Long campaignId) {
		super("Campaign Metrics Not Found",
				"캠페인 성과 데이터가 없습니다. 성과 시뮬레이션을 먼저 실행하세요. campaignId=" + campaignId);
	}

}
