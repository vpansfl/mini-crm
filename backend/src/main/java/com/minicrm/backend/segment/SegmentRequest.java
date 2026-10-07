package com.minicrm.backend.segment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 세그먼트 생성/수정 요청
public record SegmentRequest(
		@NotBlank(message = "세그먼트 이름은 필수입니다.") @Size(max = 100) String name,
		@Size(max = 500) String description,
		@NotNull(message = "조건 필드는 필수입니다.") SegmentField ruleField,
		@NotNull(message = "조건 연산자는 필수입니다.") SegmentOperator ruleOperator,
		@Size(max = 255) String ruleValue) {

	public SegmentRule toRule() {
		return new SegmentRule(ruleField, ruleOperator, ruleValue);
	}

}
