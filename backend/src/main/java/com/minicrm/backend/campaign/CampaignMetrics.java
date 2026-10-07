package com.minicrm.backend.campaign;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

// 캠페인 성과. 캠페인당 하나만 유지한다. (campaign_id unique)
@Entity
@Table(name = "campaign_metrics")
public class CampaignMetrics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "campaign_id", nullable = false, unique = true)
	private Campaign campaign;

	@Column(name = "sent_count", nullable = false)
	private int sentCount;

	@Column(name = "open_count", nullable = false)
	private int openCount;

	@Column(name = "click_count", nullable = false)
	private int clickCount;

	@Column(name = "conversion_count", nullable = false)
	private int conversionCount;

	@Column(name = "revenue", nullable = false, precision = 19, scale = 2)
	private BigDecimal revenue = BigDecimal.ZERO;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected CampaignMetrics() {
	}

	public CampaignMetrics(Campaign campaign) {
		this.campaign = campaign;
	}

	public void update(int sentCount, int openCount, int clickCount, int conversionCount, BigDecimal revenue) {
		this.sentCount = sentCount;
		this.openCount = openCount;
		this.clickCount = clickCount;
		this.conversionCount = conversionCount;
		this.revenue = revenue;
	}

	public Long getId() {
		return id;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public int getSentCount() {
		return sentCount;
	}

	public int getOpenCount() {
		return openCount;
	}

	public int getClickCount() {
		return clickCount;
	}

	public int getConversionCount() {
		return conversionCount;
	}

	public BigDecimal getRevenue() {
		return revenue;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

}
