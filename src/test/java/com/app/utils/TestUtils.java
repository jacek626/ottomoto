package com.app.utils;

import com.app.entity.Announcement;
import com.app.entity.Manufacturer;
import com.app.entity.User;
import com.app.entity.VehicleModel;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;

import java.math.BigDecimal;

public class TestUtils {

    public static  Announcement prepareAnnouncementWithAllNeededObjects() {
        User user = new User.UserBuilder("user","testPass","testPass", "user@test.com", true).build();
        Manufacturer manufacturer = new Manufacturer("manufacturer");
        manufacturer.getVehicleModel().add(new VehicleModel("vehicleModel" , manufacturer , VehicleType.CAR));

        Announcement announcement =  Announcement.builder().user(user).vehicleModel(manufacturer.getVehicleModel().get(0)).vehicleSubtype(VehicleSubtype.COMPACT).productionYear(2_000).price(BigDecimal.valueOf(180_000)).build();
        announcement.setVehicleSubtype(VehicleSubtype.CABRIO);
        announcement.getUser().setId(-1L);
        announcement.getVehicleModel().setId(-5L);
        announcement.setVehicleType(VehicleType.CAR);
        announcement.setManufacturerId(-3L);

        return announcement;

    }
}
