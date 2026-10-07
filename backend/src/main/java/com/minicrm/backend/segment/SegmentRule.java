package com.minicrm.backend.segment;

import com.minicrm.backend.exception.InvalidRequestException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// 단일 조건(필드 + 연산자 + 값). 생성 시 필드/연산자 조합과 값 형식을 검증하고 값을 정규화한다.
public record SegmentRule(SegmentField field, SegmentOperator operator, String value) {

	public SegmentRule {
		if (field == null || operator == null) {
			throw new InvalidRequestException("조건 필드와 연산자는 필수입니다.");
		}
		if (!field.supports(operator)) {
			throw new InvalidRequestException(field + " 필드에는 " + operator + " 연산자를 사용할 수 없습니다.");
		}
		if (!operator.requiresValue()) {
			value = null;
		}
		else {
			value = value != null ? value.trim() : "";
			if (value.isEmpty()) {
				throw new InvalidRequestException(operator + " 연산자에는 조건 값이 필요합니다.");
			}
			if (field.valueType() == SegmentField.ValueType.DATE) {
				parseDate(value);
			}
		}
	}

	public LocalDate dateValue() {
		return parseDate(value);
	}

	private static LocalDate parseDate(String value) {
		try {
			return LocalDate.parse(value);
		}
		catch (DateTimeParseException ex) {
			throw new InvalidRequestException("날짜 조건 값은 yyyy-MM-dd 형식이어야 합니다. value=" + value);
		}
	}

}
