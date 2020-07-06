package com.app.service;

import com.app.dto.AnnouncementDto;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleModelRepository;
import com.app.utils.BreadCrumbElement;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnnouncementBreadCrumbService {

    private final ManufacturerRepository manufacturerRepository;
    private final VehicleModelRepository vehicleModelRepository;

    public AnnouncementBreadCrumbService(ManufacturerRepository manufacturerRepository, VehicleModelRepository vehicleTypeRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.vehicleModelRepository = vehicleTypeRepository;
    }

    public List<BreadCrumbElement> create(AnnouncementDto announcement) {
        List<BreadCrumbElement> breadCrumbElements = Lists.newArrayList();

        if (announcement.getVehicleType() != null)
            breadCrumbElements.add(new BreadCrumbElement(announcement.getVehicleType().getLabel(), "vehicleType=" + announcement.getManufacturerId()));

        if (announcement.getManufacturerId() != null)
            breadCrumbElements.add(new BreadCrumbElement(manufacturerRepository.findNameById(announcement.getManufacturerId()), "manufacturerId=" + announcement.getManufacturerId()));

        if (announcement.getVehicleModel() != null) {
            breadCrumbElements.add(new BreadCrumbElement(vehicleModelRepository.findNameById(announcement.getVehicleModel().getId()), "vehicleModel.id=" + announcement.getVehicleModel().getId() + "&manufacturerId=" + announcement.getManufacturerId()));
        }

        return breadCrumbElements;
    }
}
