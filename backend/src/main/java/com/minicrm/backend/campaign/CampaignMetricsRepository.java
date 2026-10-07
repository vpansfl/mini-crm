package com.minicrm.backend.campaign;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignMetricsRepository extends JpaRepository<CampaignMetrics, Long> {

	Optional<CampaignMetrics> findByCampaign_Id(Long campaignId);

	List<CampaignMetrics> findAllByCampaign_IdIn(Collection<Long> campaignIds);

	@Modifying(flushAutomatically = true)
	@Query("delete from CampaignMetrics m where m.campaign.id = :campaignId")
	int deleteByCampaignId(@Param("campaignId") Long campaignId);

}
