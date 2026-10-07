package com.minicrm.backend.segment;

import java.time.LocalDateTime;

public record SegmentResponse(
		Long id,
		String name,
		String description,
		SegmentField ruleField,
		SegmentOperator ruleOperator,
		String ruleValue,
		long customerCount,
		LocalDateTime refreshedAt,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {

	public static SegmentResponse from(Segment segment, long customerCount) {
		return new SegmentResponse(
				segment.getId(),
				segment.getName(),
				segment.getDescription(),
				segment.getRuleField(),
				segment.getRuleOperator(),
				segment.getRuleValue(),
				customerCount,
				segment.getRefreshedAt(),
				segment.getCreatedAt(),
				segment.getUpdatedAt());
	}

}
