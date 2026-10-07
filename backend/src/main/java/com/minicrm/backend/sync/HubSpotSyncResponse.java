package com.minicrm.backend.sync;

import com.minicrm.backend.common.SyncResult;

public record HubSpotSyncResponse(
		SyncResult companies,
		SyncResult customers,
		SyncResult deals) {
}
