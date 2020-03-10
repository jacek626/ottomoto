package com.app.service;

import com.app.entity.Manufacturer;
import com.app.utils.Result;

public interface ManufacturerService {
	Result saveManufacturer(Manufacturer manufacturer);
	Result deleteManufacturer(Manufacturer manufacturer);
}
