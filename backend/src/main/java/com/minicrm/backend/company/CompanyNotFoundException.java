package com.minicrm.backend.company;

import com.minicrm.backend.exception.ResourceNotFoundException;

public class CompanyNotFoundException extends ResourceNotFoundException {

	public CompanyNotFoundException(Long id) {
		super("Company Not Found", "회사를 찾을 수 없습니다. id=" + id);
	}

}
