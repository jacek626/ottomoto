package com.app.utils;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
import com.app.enums.BooleanValuesForDropDown;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Deprecated
public class AnnouncementQueryPreparer {

	class SearchModel {
		List<Predicate> predicates;

	}

	public String prepareQueryAndSearchArguments(Announcement announcement, List<Predicate> predicates) {
		//StringBuilder stringBuilder = new StringBuilder(searchArguments);
		StringBuilder stringBuilder = new StringBuilder("&");
		//predicates = new ArrayList<>();

		// refleksyjne przegladanie metod
/*		Class type = clazz.getDeclaredField(criteria.getKey()).getType();
// don't forget to catch exception if field doesn't exist ..
		switch(type.getSimpleName()) {
			case "String":
				StringExpression exp =  entityPath.getString(...);
		}*/

		PathBuilder<Announcement> entityPath = new PathBuilder<Announcement>(Announcement.class, "announcement");

		if(announcement.getManufacturerId() != null)
		{
			predicates.add(QAnnouncement.announcement.vehicleModel.manufacturer.id.eq(announcement.getManufacturerId()));
			stringBuilder.append("manufacturerId=").append(announcement.getManufacturerId()).append("&");
		}

		if(announcement.getVehicleType() != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.vehicleType.eq(announcement.getVehicleType()));
			stringBuilder.append("vehicleType=").append(announcement.getVehicleType()).append("&");
		}
		if(announcement.getVehicleModel() != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.id.eq(announcement.getVehicleModel().getId()));
			stringBuilder.append("vehicleModel=").append(announcement.getVehicleModel().getId()).append("&");
		}
		
		if(announcement.getFuelType() != null) {
			predicates.add(QAnnouncement.announcement.fuelType.eq(announcement.getFuelType()));
			stringBuilder.append("fuelType=").append(announcement.getFuelType()).append("&");
		}
		
		if(announcement.getVehicleSubtype() != null) {
			predicates.add(QAnnouncement.announcement.vehicleSubtype.eq(announcement.getVehicleSubtype()));
			stringBuilder.append("vehicleSubtype=").append(announcement.getVehicleSubtype()).append("&");
		}

		//predicates.add(entityPath.getBoolean("accidents").eq(announcement.getSearchFields().getAccidents().getQueryValue()));
	//	predicates.add(QAnnouncement.announcement.accidents.eq(announcement.getSearchFields().getAccidents().getQueryValue()));
		
		if(announcement.getSearchFields().getAccidents() != null && announcement.getSearchFields().getAccidents() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.accidents.eq(announcement.getSearchFields().getAccidents() == BooleanValuesForDropDown.YES));
			stringBuilder.append("searchFields.accidents=").append(announcement.getSearchFields().getAccidents() == BooleanValuesForDropDown.YES).append("&");
		}

		//predicates.add(entityPath.getBoolean("firstOwner").eq(announcement.getSearchFields().getFirstOwner().getQueryValue()));
	//	predicates.add(QAnnouncement.announcement.firstOwner.eq(announcement.getSearchFields().getFirstOwner().getQueryValue()));

		if(announcement.getSearchFields().getFirstOwner() != null && announcement.getSearchFields().getFirstOwner() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.firstOwner.eq(announcement.getSearchFields().getFirstOwner() == BooleanValuesForDropDown.YES));
			stringBuilder.append("searchFields.firstOwner=").append(announcement.getSearchFields().getFirstOwner() == BooleanValuesForDropDown.YES).append("&");
		}

	//	predicates.add(entityPath.getBoolean("damaged").eq(announcement.getSearchFields().getDamaged().getQueryValue()));


		if(announcement.getSearchFields().getDamaged() != null && announcement.getSearchFields().getDamaged() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.damaged.eq(announcement.getSearchFields().getDamaged() == BooleanValuesForDropDown.YES));
			stringBuilder.append("searchFields.damaged=").append(announcement.getSearchFields().getDamaged() == BooleanValuesForDropDown.YES).append("&");
		}

	//	predicates.add(QAnnouncement.announcement.netPrice.eq(announcement.getSearchFields().getNetPrice().getQueryValue()));
		
		if(announcement.getSearchFields().getNetPrice() != null && announcement.getSearchFields().getNetPrice() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.netPrice.eq(announcement.getSearchFields().getNetPrice() == BooleanValuesForDropDown.YES));
			stringBuilder.append("searchFields.netPrice=").append(announcement.getSearchFields().getNetPrice() == BooleanValuesForDropDown.YES).append("&");
		}


		//predicates.add(entityPath.getNumber( "productionYear" , Short.class ).be(announcement.getSearchFields().getProductionYearFrom(), announcement.getSearchFields().getProductionYearTo()));
		//entityPath.getNumber( "productionYear" , Short.class ).between((e) -> {}, (d) -> {});
		if(announcement.getSearchFields().getProductionYearFrom() != null) {
			predicates.add(QAnnouncement.announcement.productionYear.goe(announcement.getSearchFields().getProductionYearFrom()));
			stringBuilder.append("searchFields.productionYearFrom=").append(announcement.getSearchFields().getProductionYearFrom()).append("&");
		}
		if(announcement.getSearchFields().getProductionYearTo() != null) {
			predicates.add(QAnnouncement.announcement.productionYear.lt(announcement.getSearchFields().getProductionYearTo()));
			stringBuilder.append("searchFields.productionYearTo=").append(announcement.getSearchFields().getProductionYearTo()).append("&");
		}

		//predicates.add(entityPath.getNumber("netPrice").between(announcement.getSearchFields().getNetPrice().getQueryValue()));

		//predicates.add(QAnnouncement.announcement.mileage.between(announcement.getSearchFields().getMileageFrom(), announcement.getSearchFields().getMileageTo()));

		if(announcement.getSearchFields().getMileageFrom() != null) {
			predicates.add(QAnnouncement.announcement.mileage.goe(announcement.getSearchFields().getMileageFrom()));
				stringBuilder.append("searchFields.productionYearFrom=").append(announcement.getSearchFields().getProductionYearFrom()).append("&");
		}
		if(announcement.getSearchFields().getMileageTo() != null) {
			predicates.add(QAnnouncement.announcement.mileage.lt(announcement.getSearchFields().getMileageTo()));
				stringBuilder.append("searchFields.productionYearTo=").append(announcement.getSearchFields().getProductionYearTo()).append("&");
		}
		
		if(announcement.getSearchFields().getEngineCapacityFrom() != null) {
			predicates.add(QAnnouncement.announcement.engineCapacity.goe(announcement.getSearchFields().getEngineCapacityFrom()));
				stringBuilder.append("searchFields.productionYearFrom=").append(announcement.getSearchFields().getProductionYearFrom()).append("&");
		}
		if(announcement.getSearchFields().getEngineCapacityTo() != null) {
			predicates.add(QAnnouncement.announcement.engineCapacity.lt(announcement.getSearchFields().getEngineCapacityTo()));
				stringBuilder.append("searchFields.productionYearTo=").append(announcement.getSearchFields().getProductionYearTo()).append("&");
		}
		
		
		if(announcement.getSearchFields().getEnginePowerFrom() != null) {
			predicates.add(QAnnouncement.announcement.enginePower.goe(announcement.getSearchFields().getEnginePowerFrom()));
				stringBuilder.append("searchFields.productionYearFrom=").append(announcement.getSearchFields().getProductionYearFrom()).append("&");
		}
		if(announcement.getSearchFields().getEnginePowerTo() != null) {
			predicates.add(QAnnouncement.announcement.enginePower.lt(announcement.getSearchFields().getEnginePowerTo()));
				stringBuilder.append("searchFields.productionYearTo=").append(announcement.getSearchFields().getProductionYearTo()).append("&");
		}

		if(announcement.getSearchFields().getColorList().size() > 0) {
			predicates.add(QAnnouncement.announcement.carColor.in(announcement.getSearchFields().getColorList()));
			stringBuilder.append("searchFields.colorList=").append(announcement.getSearchFields().getColorList().stream().map(Object::toString).collect(Collectors.joining())).append("&");
		}
		
		if(announcement.getSearchFields().getPriceFrom() != null) {
			BigDecimal priceFrom = new BigDecimal(announcement.getSearchFields().getPriceFrom());
			predicates.add(QAnnouncement.announcement.price.goe(priceFrom));
			stringBuilder.append("searchFields.priceFrom=").append(priceFrom).append("&");
		}
		if(announcement.getSearchFields().getPriceTo() != null) {
			BigDecimal priceTo = new BigDecimal(announcement.getSearchFields().getPriceTo());
			predicates.add(QAnnouncement.announcement.price.loe(priceTo));
			stringBuilder.append("searchFields.priceTo=").append(priceTo).append("&");
		}
		
		if(announcement.getSearchFields().getDoorsFrom() != null) {
			predicates.add(QAnnouncement.announcement.doors.goe(announcement.getSearchFields().getDoorsFrom()));
			stringBuilder.append("productionYearFrom=").append(announcement.getSearchFields().getProductionYearFrom()).append("&");
		}
		if(announcement.getSearchFields().getDoorsTo() != null) {
			predicates.add(QAnnouncement.announcement.doors.lt(announcement.getSearchFields().getDoorsTo()));
			stringBuilder.append("productionYearTo=").append(announcement.getSearchFields().getProductionYearTo()).append("&");
		}

		if(announcement.getUser() != null) {
			predicates.add(QAnnouncement.announcement.user.id.eq(announcement.getUser().getId()));
			stringBuilder.append("user=").append(announcement.getUser().getId()).append("&");
		}

		return stringBuilder.toString();
	}


}
