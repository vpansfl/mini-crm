package com.minicrm.backend.segment;

public enum SegmentOperator {

	EQ(true),
	CONTAINS(true),
	NOT_CONTAINS(true),
	EXISTS(false),
	GT(true),
	GTE(true),
	LT(true),
	LTE(true);

	private final boolean requiresValue;

	SegmentOperator(boolean requiresValue) {
		this.requiresValue = requiresValue;
	}

	public boolean requiresValue() {
		return requiresValue;
	}

}
