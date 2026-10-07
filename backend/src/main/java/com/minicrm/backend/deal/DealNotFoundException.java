package com.minicrm.backend.deal;

import com.minicrm.backend.exception.ResourceNotFoundException;

public class DealNotFoundException extends ResourceNotFoundException {

	public DealNotFoundException(Long id) {
		super("Deal Not Found", "거래를 찾을 수 없습니다. id=" + id);
	}

}
