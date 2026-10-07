package com.minicrm.backend.common;

// HubSpot 동기화 결과. fetched = created + updated + unchanged
public record SyncResult(
		int fetched,
		int created,
		int updated,
		int unchanged) {
}
