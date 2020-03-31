package com.app.searchForm;

import com.app.entity.Announcement;
import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.PageSize;
import com.app.enums.SearchEngineDropDownValues;
import com.app.enums.VehicleSubtype;
import com.app.projection.ManufacturerProjection;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleModelRepository;
import com.app.utils.PredicatesAndUrlParams;
import com.querydsl.core.types.Predicate;
import org.apache.commons.collections.ListUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnnouncementSearchFormStrategy implements SearchFormStrategy<Announcement> {

    private final AnnouncementRepository announcementRepository;

    private final VehicleModelRepository vehicleModelRepository;

    private final ManufacturerRepository manufacturerRepository;

    public AnnouncementSearchFormStrategy(AnnouncementRepository announcementRepository, VehicleModelRepository vehicleModelRepository, ManufacturerRepository manufacturerRepository) {
        this.announcementRepository = announcementRepository;
        this.vehicleModelRepository = vehicleModelRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public PredicatesAndUrlParams preparePredicatesAndUrlParams(Announcement announcement) {
        Pair<List<Predicate>, String> predicatesAndUrlParamsFromAnnouncement = announcement.preparePredicatesAndUrlParams();
        Pair<List<Predicate>, String> predicatesAndUrlParamsFromSearchFields = announcement.getSearchFields().prepareQueryAndSearchArguments();

        List<Predicate> predicates = ListUtils.union(predicatesAndUrlParamsFromAnnouncement.getFirst(), predicatesAndUrlParamsFromSearchFields.getFirst());
        String urlParams = new StringBuilder().append("&").append(predicatesAndUrlParamsFromAnnouncement.getSecond()).append(predicatesAndUrlParamsFromSearchFields.getSecond()).toString();

        return PredicatesAndUrlParams.of(predicates, urlParams);
    }

    @Override
    public Page<Announcement> loadData(PageRequest pageRequest, List<Predicate>  predicates) {
        return announcementRepository.findByPredicatesAndLoadMainPicture(pageRequest, predicates);
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

    private Map<String,Object> prepareModelAttributesFromEnums() {
        Map<String, Object> model = new HashMap<>();

        model.put("pageSizes", PageSize.LIST);
        model.put("pricesList", SearchEngineDropDownValues.CAR_PRICES_LIST);
        model.put("mileageList", SearchEngineDropDownValues.MILEAGE_LIST);
        model.put("engineCapacityList", SearchEngineDropDownValues.ENGINE_CAPACITY_LIST);
        model.put("enginePowerList", SearchEngineDropDownValues.ENGINE_POWER_LIST);
        model.put("doorsList", SearchEngineDropDownValues.DOOR_LIST);
        model.put("booleanValues", BooleanValuesForDropDown.values());

        return model;
    }

    private Map<String,Object> prepareVehicleModelsIfManufacturerIsSet(Announcement announcement, List<ManufacturerProjection> manufacturerList) {
        Map<String,Object> model = new HashMap<>();

        if(announcement.getManufacturerId() != null) {
            announcement.setManufacturerName(manufacturerList.stream().filter(e -> e.getId() == announcement.getManufacturerId()).findAny().get().getName());
            model.put("vehicleModelList", vehicleModelRepository.findByManufacturerIdAndVehicleType(announcement.getManufacturerId(), announcement.getVehicleType()));
        }
        else if(manufacturerList.size() > 0) {
            model.put("vehicleModelList", vehicleModelRepository.findByManufacturerIdAndVehicleType(manufacturerList.get(0).getId(), announcement.getVehicleType()));
        }

        return model;
    }

}


