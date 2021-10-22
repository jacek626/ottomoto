package com.app.announcement.dto;

import com.app.common.enums.*;
import com.app.picture.dto.PictureDto;
import com.app.user.dto.UserDto;
import com.app.vehiclemodel.entity.VehicleModel;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
public class AnnouncementDto {
    private Long id;
    private String title;
    @Size(max = 3000)
    private String description;
    @NotNull
    private VehicleModel vehicleModel;
    @NotNull
    @Min(1900)
    @Max(2100)
    private Integer productionYear;
    @NotNull
    @Min(0)
    @Max(999_999_999)
    private Integer mileage = 0;
    @Size(max = 20)
    private String vin;
    @NotNull
    private FuelType fuelType;
    @NotNull
    @Min(0)
    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;
    @NotNull
    private Currency currency;
    @NotNull
    private Boolean netPrice;
    @NotNull
    private Boolean priceNegotiate;
    @NotNull
    private VehicleSubtype vehicleSubtype;
    @NotNull
    private CarColor carColor;
    @Singular
    private List<PictureDto> pictures = new ArrayList<>();
    @NotNull
    private UserDto user;
    private List<PictureDto> imagesToDelete;
    private Date creationDate;
    private Boolean active;
    @Min(0)
    @Max(value = 50_000, message = "{validation.rangeError}")
    @NotNull
    private Integer engineCapacity;
    @Max(value = 50000, message = "{validation.rangeError}")
    @NotNull
    private Integer enginePower;
    private Boolean accidents;
    private Boolean firstOwner;
    private Boolean damaged;
    private Byte doors;
    private Long manufacturerId;
    private String manufacturerName;
    private String miniatureRepositoryName;
    private VehicleType vehicleType;

    public void preparePictures() {
        Map<Boolean, List<PictureDto>> picturesToSaveAndDeleteInSeparateLists = getPictures().stream().collect(Collectors.partitioningBy(PictureDto::isPictureToDelete));
        List<PictureDto> imagesToDelete = picturesToSaveAndDeleteInSeparateLists.get(Boolean.TRUE);
        List<PictureDto> imagesToSave = picturesToSaveAndDeleteInSeparateLists.get(Boolean.FALSE);
        setPictures(imagesToSave);
        setImagesToDelete(imagesToDelete);
    }
}
