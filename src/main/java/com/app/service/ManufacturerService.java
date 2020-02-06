package com.app.service;

import java.util.Map;

import com.app.entity.Manufacturer;
import com.app.utils.Result;

public interface ManufacturerService {
	Result saveManufacturer(Manufacturer manufacturer);
	void deleteManufacturer(Manufacturer manufacturer);
}
