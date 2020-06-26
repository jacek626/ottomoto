package com.app.dto;

import com.app.entity.VehicleModel;
import com.app.enums.*;
import com.app.utils.AnnouncementSearchFields;
import com.querydsl.core.BooleanBuilder;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class AnnouncementDto {
    private Long id;
    @Builder.Default
    private String title = "Default title";
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
    @Builder.Default
    private Integer mileage = 0;
    @Size(max = 20)
    private String vin;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private FuelType fuelType = FuelType.PETROL;
    @NotNull
    @Min(0)
    @Digits(integer = 9, fraction = 2)
    private BigDecimal price;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Currency currency = Currency.PLN;
    @NotNull
    @Builder.Default
    private Boolean netPrice = false;
    @NotNull
    @Builder.Default
    private Boolean priceNegotiate = true;
    @NotNull
    private VehicleSubtype vehicleSubtype;
    @NotNull
    @Builder.Default
    private CarColor carColor = CarColor.WHITE;
    @Singular
    private List<PictureDto> pictures = new ArrayList<>();
    @NotNull
    private UserDto userDto;
    private List<PictureDto> imagesToDelete;
    private Date creationDate;
    @Builder.Default
    private Boolean active = true;
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
    private StringBuilder urlParams;
    @Builder.Default
    private AnnouncementSearchFields searchFields = new AnnouncementSearchFields();
    @Getter(AccessLevel.NONE)
    private BooleanBuilder predicates;

    public AnnouncementDto() {
    }

    public void preparePictures() {
        Map<Boolean, List<PictureDto>> picturesToSaveAndDeleteInSeparateLists = getPictures().stream().collect(Collectors.partitioningBy(PictureDto::isPictureToDelete));
        List<PictureDto> imagesToDelete = picturesToSaveAndDeleteInSeparateLists.get(Boolean.TRUE);
        List<PictureDto> imagesToSave = picturesToSaveAndDeleteInSeparateLists.get(Boolean.FALSE);
        setPictures(imagesToSave);
        setImagesToDelete(imagesToDelete);
    }

}
