package com.app.utils;

import com.app.entity.Announcement;
import com.google.common.collect.Lists;

import java.util.List;

public class BreadCrumb {

    public static List<BreadCrumbElement> create(Announcement announcement) {
        List<BreadCrumbElement> breadCrumbElements = Lists.newArrayList();

        if(announcement.getVehicleModel().getVehicleType() != null)
            breadCrumbElements.add(new BreadCrumbElement(announcement.getVehicleModel().getVehicleType().getLabel(), "vehicleType=" + announcement.getVehicleModel().getVehicleType()));

        if(announcement.getVehicleModel().getManufacturer() != null)
            breadCrumbElements.add(new BreadCrumbElement(announcement.getVehicleModel().getManufacturer().getName(), "manufacturer=" + announcement.getVehicleModel().getManufacturer()));

        if(announcement.getVehicleModel() != null)
            breadCrumbElements.add(new BreadCrumbElement(announcement.getVehicleModel().getName(), "vehicleModel=" + announcement.getVehicleModel().getId()));

        return breadCrumbElements;
    }
}
