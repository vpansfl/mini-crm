package com.minicrm.backend.sync;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HubSpotSyncController {

	private final HubSpotSyncService hubSpotSyncService;

	public HubSpotSyncController(HubSpotSyncService hubSpotSyncService) {
		this.hubSpotSyncService = hubSpotSyncService;
	}

	@PostMapping("/api/hubspot/sync")
	public HubSpotSyncResponse syncAll() {
		return hubSpotSyncService.syncAll();
	}

}
