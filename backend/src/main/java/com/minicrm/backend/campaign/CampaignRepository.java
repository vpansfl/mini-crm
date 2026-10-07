package com.minicrm.backend.campaign;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

// 응답에 세그먼트 이름이 포함되므로 EntityGraph로 함께 조회한다.
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

	@EntityGraph(attributePaths = "segment")
	List<Campaign> findAllByOrderByIdDesc();

	@EntityGraph(attributePaths = "segment")
	List<Campaign> findTop10ByOrderByIdDesc();

	@EntityGraph(attributePaths = "segment")
	Optional<Campaign> findWithSegmentById(Long id);

	boolean existsBySegment_Id(Long segmentId);

	long countByStatus(CampaignStatus status);

}
