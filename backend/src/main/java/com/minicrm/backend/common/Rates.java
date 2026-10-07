package com.minicrm.backend.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

// 성과 비율 계산. 0~1 사이 값(소수 넷째 자리 반올림)을 반환하고, 분모가 0이면 0을 반환한다.
public final class Rates {

	private Rates() {
	}

	public static double of(long part, long whole) {
		if (whole <= 0) {
			return 0.0;
		}
		return BigDecimal.valueOf(part)
				.divide(BigDecimal.valueOf(whole), 4, RoundingMode.HALF_UP)
				.doubleValue();
	}

}
