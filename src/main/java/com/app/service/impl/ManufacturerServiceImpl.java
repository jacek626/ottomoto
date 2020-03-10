package com.app.service.impl;

import com.app.entity.Manufacturer;
import com.app.repository.ManufacturerRepository;
import com.app.service.ManufacturerService;
import com.app.utils.Result;
import com.app.validator.ManufacturerValidator;
import org.springframework.stereotype.Service;

@Service("manufacturerService")
public class ManufacturerServiceImpl implements ManufacturerService {
	
	private final ManufacturerRepository manufacturerRepository;
	
	private final ManufacturerValidator manufacturerValidator;

	public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, ManufacturerValidator manufacturerValidator) {
		this.manufacturerRepository = manufacturerRepository;
		this.manufacturerValidator = manufacturerValidator;
	}

	public Result saveManufacturer(Manufacturer manufacturer) {
		Result result = manufacturerValidator.checkBeforeSave(manufacturer);
		
		if(result.isSuccess())
			manufacturerRepository.save(manufacturer);
		
		return result;
	}
	
	public Result deleteManufacturer(Manufacturer manufacturer) {
		Result result = manufacturerValidator.checkBeforeDelete(manufacturer);

		if (result.isSuccess())
			manufacturerRepository.delete(manufacturer);

		return result;
	}
	

}
