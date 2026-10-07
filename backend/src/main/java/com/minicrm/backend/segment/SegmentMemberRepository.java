package com.minicrm.backend.segment;

import com.minicrm.backend.customer.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SegmentMemberRepository extends JpaRepository<SegmentMember, Long> {

	long countBySegment_Id(Long segmentId);

	@Query("select m.segment.id as segmentId, count(m) as memberCount from SegmentMember m group by m.segment.id")
	List<SegmentMemberCount> countGroupBySegment();

	@Query("select m.customer from SegmentMember m where m.segment.id = :segmentId order by m.customer.id desc")
	List<Customer> findCustomersBySegmentId(@Param("segmentId") Long segmentId);

	@Modifying(flushAutomatically = true)
	@Query("delete from SegmentMember m where m.segment.id = :segmentId")
	int deleteBySegmentId(@Param("segmentId") Long segmentId);

	interface SegmentMemberCount {

		Long getSegmentId();

		Long getMemberCount();

	}

}
