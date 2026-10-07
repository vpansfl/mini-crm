package com.minicrm.backend.hubspot;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class RestClientHubSpotCrmClient implements HubSpotCrmClient {

	private static final Logger log = LoggerFactory.getLogger(RestClientHubSpotCrmClient.class);

	private static final ObjectSpec CONTACTS = new ObjectSpec("contacts", "crm.objects.contacts.read",
			List.of("firstname", "lastname", "email", "phone", "mobilephone", "company", "jobtitle", "city", "state",
					"country", "lifecyclestage", "hs_lead_status", "associatedcompanyid", "createdate",
					"lastmodifieddate"),
			List.of());

	private static final ObjectSpec COMPANIES = new ObjectSpec("companies", "crm.objects.companies.read",
			List.of("name", "domain", "industry", "phone", "city", "state", "country", "createdate",
					"hs_lastmodifieddate"),
			List.of());

	private static final ObjectSpec DEALS = new ObjectSpec("deals", "crm.objects.deals.read",
			List.of("dealname", "amount", "pipeline", "dealstage", "closedate", "createdate", "hs_lastmodifieddate"),
			List.of("companies", "contacts"));

	// Deal의 대표(primary) Company 연관 타입
	private static final String PRIMARY_DEAL_TO_COMPANY = "deal_to_company";

	private final RestClient hubSpotRestClient;
	private final HubSpotProperties properties;

	public RestClientHubSpotCrmClient(RestClient hubSpotRestClient, HubSpotProperties properties) {
		this.hubSpotRestClient = hubSpotRestClient;
		this.properties = properties;
	}

	@Override
	public List<HubSpotContact> fetchAllContacts() {
		return fetchAll(CONTACTS, result -> {
			Map<String, String> props = propertiesOf(result);
			return new HubSpotContact(
					result.id(),
					text(props, "firstname"),
					text(props, "lastname"),
					text(props, "email"),
					text(props, "phone"),
					text(props, "mobilephone"),
					text(props, "company"),
					text(props, "jobtitle"),
					text(props, "city"),
					text(props, "state"),
					text(props, "country"),
					text(props, "lifecyclestage"),
					text(props, "hs_lead_status"),
					text(props, "associatedcompanyid"),
					instant(props, "createdate"),
					instant(props, "lastmodifieddate"));
		});
	}

	@Override
	public List<HubSpotCompany> fetchAllCompanies() {
		return fetchAll(COMPANIES, result -> {
			Map<String, String> props = propertiesOf(result);
			return new HubSpotCompany(
					result.id(),
					text(props, "name"),
					text(props, "domain"),
					text(props, "industry"),
					text(props, "phone"),
					text(props, "city"),
					text(props, "state"),
					text(props, "country"),
					instant(props, "createdate"),
					instant(props, "hs_lastmodifieddate"));
		});
	}

	@Override
	public List<HubSpotDeal> fetchAllDeals() {
		return fetchAll(DEALS, result -> {
			Map<String, String> props = propertiesOf(result);
			return new HubSpotDeal(
					result.id(),
					text(props, "dealname"),
					decimal(props, "amount"),
					text(props, "pipeline"),
					text(props, "dealstage"),
					instant(props, "closedate"),
					instant(props, "createdate"),
					instant(props, "hs_lastmodifieddate"),
					primaryCompanyId(associationsOf(result, "companies")),
					associationsOf(result, "contacts").stream()
							.map(HubSpotObjectsPage.Association::id)
							.distinct()
							.toList());
		});
	}

	private <T> List<T> fetchAll(ObjectSpec spec, Function<HubSpotObjectsPage.Result, T> mapper) {
		if (!properties.hasAccessToken()) {
			throw new HubSpotApiException(HttpStatus.SERVICE_UNAVAILABLE,
					"HubSpot 액세스 토큰이 설정되지 않았습니다. HUBSPOT_ACCESS_TOKEN 환경변수를 설정한 뒤 서버를 다시 시작하세요.");
		}

		List<T> records = new ArrayList<>();
		String after = null;
		int pageCount = 0;
		do {
			HubSpotObjectsPage page = fetchPage(spec, after);
			pageCount++;
			if (page.results() != null) {
				page.results().stream().map(mapper).forEach(records::add);
			}
			after = page.nextAfter();
		} while (after != null);

		log.info("HubSpot {} 조회 완료: pages={}, records={}", spec.objectType(), pageCount, records.size());
		return records;
	}

	private HubSpotObjectsPage fetchPage(ObjectSpec spec, String after) {
		try {
			HubSpotObjectsPage page = hubSpotRestClient.get()
					.uri(uriBuilder -> {
						uriBuilder.path("/crm/v3/objects/" + spec.objectType())
								.queryParam("limit", properties.pageSize())
								.queryParam("properties", String.join(",", spec.properties()))
								.queryParam("archived", false);
						if (!spec.associations().isEmpty()) {
							uriBuilder.queryParam("associations", String.join(",", spec.associations()));
						}
						if (after != null) {
							uriBuilder.queryParam("after", after);
						}
						return uriBuilder.build();
					})
					.headers(headers -> headers.setBearerAuth(properties.accessToken()))
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.body(HubSpotObjectsPage.class);
			if (page == null) {
				throw new HubSpotApiException(HttpStatus.BAD_GATEWAY, "HubSpot API 응답이 비어 있습니다.");
			}
			return page;
		}
		catch (RestClientResponseException ex) {
			// HubSpot 오류 응답 본문에는 토큰이 포함되지 않는다. 요청 헤더는 로그에 남기지 않는다.
			log.warn("HubSpot API 호출 실패: object={}, status={}, body={}", spec.objectType(),
					ex.getStatusCode().value(), ex.getResponseBodyAsString());
			throw toApiException(spec, ex.getStatusCode());
		}
		catch (ResourceAccessException ex) {
			log.warn("HubSpot API 연결 실패: {}", ex.getMessage());
			throw new HubSpotApiException(HttpStatus.GATEWAY_TIMEOUT,
					"HubSpot API에 연결할 수 없거나 응답 시간이 초과되었습니다. 잠시 후 다시 시도하세요.", ex);
		}
		catch (RestClientException ex) {
			log.warn("HubSpot API 응답 처리 실패: {}", ex.getMessage());
			throw new HubSpotApiException(HttpStatus.BAD_GATEWAY, "HubSpot API 응답을 처리할 수 없습니다.", ex);
		}
	}

	private HubSpotApiException toApiException(ObjectSpec spec, HttpStatusCode status) {
		int code = status.value();
		if (code == 401) {
			return new HubSpotApiException(HttpStatus.BAD_GATEWAY,
					"HubSpot 인증에 실패했습니다. HUBSPOT_ACCESS_TOKEN 값이 올바른지 확인하세요.");
		}
		if (code == 403) {
			return new HubSpotApiException(HttpStatus.BAD_GATEWAY,
					"HubSpot " + spec.objectType() + " 조회 권한이 부족합니다. " + spec.requiredScope()
							+ " 권한이 있는지 확인하세요.");
		}
		if (code == 429) {
			return new HubSpotApiException(HttpStatus.SERVICE_UNAVAILABLE,
					"HubSpot API 호출 한도를 초과했습니다. 잠시 후 다시 시도하세요.");
		}
		if (status.is5xxServerError()) {
			return new HubSpotApiException(HttpStatus.BAD_GATEWAY,
					"HubSpot 서버에서 오류가 발생했습니다. (HubSpot status=" + code + ")");
		}
		return new HubSpotApiException(HttpStatus.BAD_GATEWAY,
				"HubSpot API 요청이 거부되었습니다. (HubSpot status=" + code + ")");
	}

	private static Map<String, String> propertiesOf(HubSpotObjectsPage.Result result) {
		return result.properties() != null ? result.properties() : Map.of();
	}

	private static List<HubSpotObjectsPage.Association> associationsOf(HubSpotObjectsPage.Result result,
			String toObjectType) {
		if (result.associations() == null) {
			return List.of();
		}
		HubSpotObjectsPage.AssociationList list = result.associations().get(toObjectType);
		if (list == null || list.results() == null) {
			return List.of();
		}
		return list.results();
	}

	// primary 연관을 우선하고, 없으면 첫 번째 Company를 사용한다.
	private static String primaryCompanyId(List<HubSpotObjectsPage.Association> companies) {
		return companies.stream()
				.filter(association -> PRIMARY_DEAL_TO_COMPANY.equals(association.type()))
				.findFirst()
				.or(() -> companies.stream().findFirst())
				.map(HubSpotObjectsPage.Association::id)
				.orElse(null);
	}

	private static String text(Map<String, String> props, String name) {
		String value = props.get(name);
		return (value == null || value.isBlank()) ? null : value.trim();
	}

	// HubSpot 날짜/시간 속성은 ISO-8601 UTC 문자열이다. (예: 2024-01-15T08:30:00.000Z)
	private static Instant instant(Map<String, String> props, String name) {
		String value = text(props, name);
		if (value == null) {
			return null;
		}
		try {
			return Instant.parse(value);
		}
		catch (DateTimeParseException ex) {
			log.warn("HubSpot 날짜 속성을 해석할 수 없습니다: property={}, value={}", name, value);
			return null;
		}
	}

	// DB 컬럼(scale=2)과 같은 scale로 맞춰 동기화 시 불필요한 변경 감지를 막는다.
	private static BigDecimal decimal(Map<String, String> props, String name) {
		String value = text(props, name);
		if (value == null) {
			return null;
		}
		try {
			return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
		}
		catch (NumberFormatException ex) {
			log.warn("HubSpot 숫자 속성을 해석할 수 없습니다: property={}, value={}", name, value);
			return null;
		}
	}

	private record ObjectSpec(String objectType, String requiredScope, List<String> properties,
			List<String> associations) {
	}

}
