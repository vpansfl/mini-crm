package com.minicrm.backend.hubspot;

import java.util.List;
import java.util.Map;

// GET /crm/v3/objects/{objectType} 응답 중 필요한 부분만 매핑한다. (contacts, companies, deals 공통)
record HubSpotObjectsPage(List<Result> results, Paging paging) {

	record Result(String id, Map<String, String> properties, Map<String, AssociationList> associations) {
	}

	record AssociationList(List<Association> results) {
	}

	record Association(String id, String type) {
	}

	record Paging(Next next) {
	}

	record Next(String after) {
	}

	String nextAfter() {
		if (paging == null || paging.next() == null) {
			return null;
		}
		return paging.next().after();
	}

}
