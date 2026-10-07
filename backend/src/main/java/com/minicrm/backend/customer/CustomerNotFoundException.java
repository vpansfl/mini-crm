package com.minicrm.backend.customer;

import com.minicrm.backend.exception.ResourceNotFoundException;

public class CustomerNotFoundException extends ResourceNotFoundException {

	public CustomerNotFoundException(Long id) {
		super("Customer Not Found", "고객을 찾을 수 없습니다. id=" + id);
	}

}
