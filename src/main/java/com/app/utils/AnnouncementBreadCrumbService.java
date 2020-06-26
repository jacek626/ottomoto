package com.app.utils;

import com.app.dto.AnnouncementDto;
import com.app.entity.Announcement;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleModelRepository;
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

    public List<BreadCrumbElement> create(Announcement announcement) {
        List<BreadCrumbElement> breadCrumbElements = Lists.newArrayList();

        if (announcement.getVehicleModel().getVehicleType() != null)
            breadCrumbElements.add(new BreadCrumbElement(announcement.getVehicleModel().getVehicleType().getLabel(), "vehicleType=" + announcement.getManufacturerId()));

        if (announcement.getManufacturerId() != null)
            breadCrumbElements.add(new BreadCrumbElement(announcement.getVehicleModel().getManufacturer().getName(), "manufacturerId=" + announcement.getVehicleModel().getManufacturer().getId()));

        if (announcement.getVehicleModel() != null) {
            breadCrumbElements.add(new BreadCrumbElement(announcement.getVehicleModel().getName(), "vehicleModel.id=" + announcement.getVehicleModel().getId() + "&manufacturerId=" + announcement.getManufacturerId()));
        }

        return breadCrumbElements;
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
