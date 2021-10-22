package com.app.searchform;

import com.app.announcement.dto.AnnouncementDto;
import com.app.announcement.entity.Announcement;
import com.app.announcement.repository.AnnouncementRepository;
import com.app.common.enums.BooleanValuesForDropDown;
import com.app.common.enums.PaginationPageSize;
import com.app.common.enums.SearchEngineDropDownValues;
import com.app.common.enums.VehicleSubtype;
import com.app.common.utils.mapper.AnnouncementMapper;
import com.app.manufacturer.repository.ManufacturerProjection;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.vehiclemodel.entity.VehicleModel;
import com.app.vehiclemodel.repository.VehicleModelRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnnouncementSearchStrategy implements SearchStrategy<Announcement, AnnouncementDto> {

    private final AnnouncementRepository announcementRepository;

    private final VehicleModelRepository vehicleModelRepository;

    private final ManufacturerRepository manufacturerRepository;

    private final AnnouncementMapper announcementMapper;


    public AnnouncementSearchStrategy(AnnouncementRepository announcementRepository, VehicleModelRepository vehicleModelRepository, ManufacturerRepository manufacturerRepository, AnnouncementMapper announcementMapper) {
        this.announcementRepository = announcementRepository;
        this.vehicleModelRepository = vehicleModelRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.announcementMapper = announcementMapper;
    }

    @Override
    public Page<Announcement> loadData(PageRequest pageRequest, Predicate predicate) {
        return announcementRepository.findByPredicatesAndLoadMainPicture(pageRequest, predicate);
    }

    @Override
    public Map<String, Object> prepareDataForHtmlElements(Announcement announcement) {
        Map<String, Object> model = new HashMap<>();

        model.putAll(prepareModelAttributesFromEnums());

        List<ManufacturerProjection> manufacturerList = manufacturerRepository.findByVehicleType(announcement.getVehicleType());
        model.put("manufacturerList", manufacturerList);
        model.putAll(prepareVehicleModelsIfManufacturerIsSet(announcement, manufacturerList));
        model.put("vehicleSubtypeList", VehicleSubtype.getVehicleSubtypesByVehicleType(announcement.getVehicleType()));

        return model;
    }

    @Override
    public AnnouncementDto convertToDto(Announcement entity) {
        return announcementMapper.convertToDto(entity);
    }

    private Map<String, Object> prepareVehicleModelsIfManufacturerIsSet(Announcement announcement, List<ManufacturerProjection> manufacturerList) {
        Map<String, Object> model = new HashMap<>();

        if (announcement.getManufacturerId() != null) {
            List<VehicleModel> vehicleModels = vehicleModelRepository.findByManufacturerIdAndVehicleType(announcement.getManufacturerId(), announcement.getVehicleType());
            model.put("vehicleModelList", vehicleModels);
        }

        return model;
    }

    private Map<String,Object> prepareModelAttributesFromEnums() {
        Map<String, Object> model = new HashMap<>();

        model.put("pageSizes", PaginationPageSize.LIST);
        model.put("prices", SearchEngineDropDownValues.CAR_PRICES);
        model.put("mileages", SearchEngineDropDownValues.MILEAGES);
        model.put("engineCapacities", SearchEngineDropDownValues.ENGINE_CAPACITIES);
        model.put("enginePowers", SearchEngineDropDownValues.ENGINE_POWERS);
        model.put("doors", SearchEngineDropDownValues.DOORS);
        model.put("booleanValues", BooleanValuesForDropDown.values());

        return model;
    }

}


