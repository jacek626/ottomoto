package com.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Manufacturer;
import com.app.repository.ManufacturerRepository;
import com.app.service.ManufacturerService;
import com.app.utils.Result;
import com.app.validator.ManufacturerValidator;

@Service("manufacturerService")
public class ManufacturerServiceImpl implements ManufacturerService {
	
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	
	@Autowired
	private ManufacturerValidator manufacturerValidator;
	
	public Result saveManufacturer(Manufacturer manufacturer) {
		Result result = manufacturerValidator.checkBeforeSave(manufacturer);
		
		if(result.isSuccess())
			manufacturerRepository.save(manufacturer);
		
		return result;
	}
	
	public void deleteManufacturer(Manufacturer manufacturer) {
		manufacturerRepository.delete(manufacturer);
	}
	

}
