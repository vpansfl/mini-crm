package com.minicrm.backend.campaign;

import com.minicrm.backend.segment.Segment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "campaigns")
public class Campaign {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 100)
	@Size(max = 100)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "segment_id", nullable = false)
	private Segment segment;

	@Enumerated(EnumType.STRING)
	@Column(name = "channel", nullable = false, length = 16)
	private CampaignChannel channel;

	@Column(name = "message", nullable = false, length = 2000)
	@Size(max = 2000)
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private CampaignStatus status;

	@Column(name = "start_at")
	private LocalDateTime startAt;

	@Column(name = "end_at")
	private LocalDateTime endAt;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected Campaign() {
	}

	public Campaign(String name, Segment segment, CampaignChannel channel, String message, CampaignStatus status,
			LocalDateTime startAt, LocalDateTime endAt) {
		update(name, segment, channel, message, status, startAt, endAt);
	}

	public void update(String name, Segment segment, CampaignChannel channel, String message, CampaignStatus status,
			LocalDateTime startAt, LocalDateTime endAt) {
		this.name = name;
		this.segment = segment;
		this.channel = channel;
		this.message = message;
		this.status = status;
		this.startAt = startAt;
		this.endAt = endAt;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Segment getSegment() {
		return segment;
	}

	public CampaignChannel getChannel() {
		return channel;
	}

	public String getMessage() {
		return message;
	}

	public CampaignStatus getStatus() {
		return status;
	}

	public LocalDateTime getStartAt() {
		return startAt;
	}

	public LocalDateTime getEndAt() {
		return endAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}
