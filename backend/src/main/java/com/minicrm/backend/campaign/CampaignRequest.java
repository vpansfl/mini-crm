package com.minicrm.backend.campaign;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

// 캠페인 생성/수정 요청. status를 생략하면 생성 시 DRAFT, 수정 시 기존 상태를 유지한다.
public record CampaignRequest(
		@NotBlank(message = "캠페인 이름은 필수입니다.") @Size(max = 100) String name,
		@NotNull(message = "대상 세그먼트는 필수입니다.") @Positive Long segmentId,
		@NotNull(message = "채널은 필수입니다.") CampaignChannel channel,
		@NotBlank(message = "메시지는 필수입니다.") @Size(max = 2000) String message,
		CampaignStatus status,
		LocalDateTime startAt,
		LocalDateTime endAt) {
}
