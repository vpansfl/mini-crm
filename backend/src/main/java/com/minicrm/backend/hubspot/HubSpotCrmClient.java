package com.minicrm.backend.hubspot;

import java.util.List;

// HubSpot CRM 객체 조회. 각 메서드는 페이지네이션을 따라가며 모든 (보관되지 않은) 레코드를 조회한다.
public interface HubSpotCrmClient {

	List<HubSpotContact> fetchAllContacts();

	List<HubSpotCompany> fetchAllCompanies();

	List<HubSpotDeal> fetchAllDeals();

}
