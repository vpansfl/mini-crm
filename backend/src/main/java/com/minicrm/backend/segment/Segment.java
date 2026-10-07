package com.minicrm.backend.segment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "segments")
public class Segment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", nullable = false, length = 100)
	@Size(max = 100)
	private String name;

	@Column(name = "description", length = 500)
	@Size(max = 500)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "rule_field", nullable = false, length = 32)
	private SegmentField ruleField;

	@Enumerated(EnumType.STRING)
	@Column(name = "rule_operator", nullable = false, length = 16)
	private SegmentOperator ruleOperator;

	@Column(name = "rule_value", length = 255)
	@Size(max = 255)
	private String ruleValue;

	// segment_members를 마지막으로 계산한 시각
	@Column(name = "refreshed_at")
	private LocalDateTime refreshedAt;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected Segment() {
	}

	public Segment(String name, String description, SegmentRule rule) {
		update(name, description, rule);
	}

	public void update(String name, String description, SegmentRule rule) {
		this.name = name;
		this.description = description;
		this.ruleField = rule.field();
		this.ruleOperator = rule.operator();
		this.ruleValue = rule.value();
	}

	public void markRefreshed(LocalDateTime refreshedAt) {
		this.refreshedAt = refreshedAt;
	}

	public SegmentRule rule() {
		return new SegmentRule(ruleField, ruleOperator, ruleValue);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public SegmentField getRuleField() {
		return ruleField;
	}

	public SegmentOperator getRuleOperator() {
		return ruleOperator;
	}

	public String getRuleValue() {
		return ruleValue;
	}

	public LocalDateTime getRefreshedAt() {
		return refreshedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}
