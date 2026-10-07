package com.minicrm.backend.segment;

import java.util.EnumSet;
import java.util.Set;

// 세그먼트 조건으로 사용할 수 있는 고객 필드. 값의 종류(ValueType)에 따라 사용할 수 있는 연산자가 정해진다.
// 숫자 필드가 필요해지면 ValueType.NUMBER를 추가하고 SegmentRuleEvaluator에 비교 로직을 추가한다.
public enum SegmentField {

	EMAIL(ValueType.TEXT),
	COMPANY_NAME(ValueType.TEXT),
	CITY(ValueType.TEXT),
	COUNTRY(ValueType.TEXT),
	LIFECYCLE_STAGE(ValueType.TEXT),
	LEAD_STATUS(ValueType.TEXT),
	CREATED_AT(ValueType.DATE);

	public enum ValueType {

		TEXT(EnumSet.of(SegmentOperator.EQ, SegmentOperator.CONTAINS, SegmentOperator.NOT_CONTAINS,
				SegmentOperator.EXISTS)),
		DATE(EnumSet.of(SegmentOperator.EQ, SegmentOperator.GT, SegmentOperator.GTE, SegmentOperator.LT,
				SegmentOperator.LTE, SegmentOperator.EXISTS));

		private final Set<SegmentOperator> operators;

		ValueType(Set<SegmentOperator> operators) {
			this.operators = operators;
		}

		public Set<SegmentOperator> operators() {
			return operators;
		}

	}

	private final ValueType valueType;

	SegmentField(ValueType valueType) {
		this.valueType = valueType;
	}

	public ValueType valueType() {
		return valueType;
	}

	public boolean supports(SegmentOperator operator) {
		return valueType.operators().contains(operator);
	}

}
