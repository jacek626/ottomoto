package com.app.common.utils;

import com.app.announcement.entity.Announcement;
import com.app.announcement.types.VehicleSubtype;
import com.app.announcement.types.VehicleType;
import com.app.manufacturer.entity.Manufacturer;
import com.app.user.entity.User;
import com.app.vehiclemodel.entity.VehicleModel;

import java.math.BigDecimal;

public class TestUtils {

    public static Announcement prepareAnnouncement() {
        User user = User.builder().login("user").password("testPass").passwordConfirm("testPass").email("user@test.com").active(true).build();
        Manufacturer manufacturer = Manufacturer.builder().name("manufacturer").build();
        manufacturer.getVehicleModel().add(VehicleModel.builder().name("vehicleModel").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build());

        Announcement announcement = Announcement.builder().user(user).vehicleModel(manufacturer.getVehicleModel().get(0)).vehicleSubtype(VehicleSubtype.COMPACT).productionYear(2_000).price(BigDecimal.valueOf(180_000)).build();
        announcement.setVehicleSubtype(VehicleSubtype.CABRIO);
        announcement.getUser().setId(-1L);
        announcement.getVehicleModel().setId(-5L);
        announcement.setVehicleType(VehicleType.CAR);
        announcement.setManufacturerId(-3L);
        announcement.setMileage(100);
        announcement.setEngineCapacity(2000);
        announcement.setEnginePower(100);

        return announcement;

    }
}
