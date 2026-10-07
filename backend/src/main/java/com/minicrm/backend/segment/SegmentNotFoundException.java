package com.minicrm.backend.segment;

import com.minicrm.backend.exception.ResourceNotFoundException;

public class SegmentNotFoundException extends ResourceNotFoundException {

	public SegmentNotFoundException(Long id) {
		super("Segment Not Found", "세그먼트를 찾을 수 없습니다. id=" + id);
	}

}
