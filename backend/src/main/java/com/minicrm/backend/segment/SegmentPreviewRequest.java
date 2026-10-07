package com.minicrm.backend.segment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 세그먼트 저장 전 조건에 맞는 고객 수 미리보기 요청
public record SegmentPreviewRequest(
		@NotNull(message = "조건 필드는 필수입니다.") SegmentField ruleField,
		@NotNull(message = "조건 연산자는 필수입니다.") SegmentOperator ruleOperator,
		@Size(max = 255) String ruleValue) {

	public SegmentRule toRule() {
		return new SegmentRule(ruleField, ruleOperator, ruleValue);
	}

}
