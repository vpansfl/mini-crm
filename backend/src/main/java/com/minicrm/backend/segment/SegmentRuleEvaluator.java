package com.minicrm.backend.segment;

import com.minicrm.backend.customer.Customer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;
import java.util.function.Predicate;
import org.springframework.stereotype.Component;

// 세그먼트 조건을 고객 한 명에 적용한다. DB/외부 의존이 없어 단위 테스트가 쉽다.
// - 문자열 비교는 대소문자를 구분하지 않는다.
// - NOT_CONTAINS는 값이 없는 고객도 포함한다.
// - CREATED_AT은 HubSpot 생성일(createdate)을 우선 사용하고, 없으면 CRM 저장 시각을 사용한다.
@Component
public class SegmentRuleEvaluator {

	private final ZoneId zoneId;

	public SegmentRuleEvaluator() {
		this(ZoneId.systemDefault());
	}

	SegmentRuleEvaluator(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public Predicate<Customer> toPredicate(SegmentRule rule) {
		return switch (rule.field().valueType()) {
			case TEXT -> customer -> matchesText(textValue(customer, rule.field()), rule);
			case DATE -> {
				LocalDate expected = rule.value() != null ? rule.dateValue() : null;
				yield customer -> matchesDate(dateValue(customer, rule.field()), rule.operator(), expected);
			}
		};
	}

	private boolean matchesText(String actual, SegmentRule rule) {
		boolean present = actual != null && !actual.isBlank();
		if (rule.operator() == SegmentOperator.EXISTS) {
			return present;
		}
		String expected = rule.value().toLowerCase(Locale.ROOT);
		String normalized = present ? actual.trim().toLowerCase(Locale.ROOT) : null;
		return switch (rule.operator()) {
			case EQ -> expected.equals(normalized);
			case CONTAINS -> normalized != null && normalized.contains(expected);
			case NOT_CONTAINS -> normalized == null || !normalized.contains(expected);
			default -> throw new IllegalStateException("지원하지 않는 문자열 연산자: " + rule.operator());
		};
	}

	private boolean matchesDate(LocalDate actual, SegmentOperator operator, LocalDate expected) {
		if (operator == SegmentOperator.EXISTS) {
			return actual != null;
		}
		if (actual == null) {
			return false;
		}
		int compared = actual.compareTo(expected);
		return switch (operator) {
			case EQ -> compared == 0;
			case GT -> compared > 0;
			case GTE -> compared >= 0;
			case LT -> compared < 0;
			case LTE -> compared <= 0;
			default -> throw new IllegalStateException("지원하지 않는 날짜 연산자: " + operator);
		};
	}

	private String textValue(Customer customer, SegmentField field) {
		return switch (field) {
			case EMAIL -> customer.getEmail();
			case COMPANY_NAME -> customer.getCompanyName();
			case CITY -> customer.getCity();
			case COUNTRY -> customer.getCountry();
			case LIFECYCLE_STAGE -> customer.getLifecycleStage();
			case LEAD_STATUS -> customer.getLeadStatus();
			default -> throw new IllegalStateException("문자열 필드가 아닙니다: " + field);
		};
	}

	private LocalDate dateValue(Customer customer, SegmentField field) {
		if (field != SegmentField.CREATED_AT) {
			throw new IllegalStateException("날짜 필드가 아닙니다: " + field);
		}
		if (customer.getHubspotCreatedAt() != null) {
			return LocalDate.ofInstant(customer.getHubspotCreatedAt(), zoneId);
		}
		return customer.getCreatedAt() != null ? customer.getCreatedAt().toLocalDate() : null;
	}

}
