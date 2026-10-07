package com.minicrm.backend.campaign;

import com.minicrm.backend.exception.ResourceNotFoundException;

public class CampaignNotFoundException extends ResourceNotFoundException {

	public CampaignNotFoundException(Long id) {
		super("Campaign Not Found", "캠페인을 찾을 수 없습니다. id=" + id);
	}

}
