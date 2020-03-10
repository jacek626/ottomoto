package com.app.utils;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
import com.app.enums.BooleanValuesForDropDown;
import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnnouncementQueryPreparer {

	public String prepareQueryAndSearchArguments(Announcement announcement, String searchArguments, List<Predicate> predicates) {
		StringBuilder stringBuilder = new StringBuilder(searchArguments);

		if(announcement.getManufacturerId() != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.manufacturer.id.eq(announcement.getManufacturerId()));
			stringBuilder.append("manufacturerId=" + announcement.getManufacturerId() + "&");
		}
		
		if(announcement.getVehicleType() != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.vehicleType.eq(announcement.getVehicleType()));
			stringBuilder.append("vehicleType=" + announcement.getVehicleType() + "&");
		}
		if(announcement.getVehicleModel() != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.id.eq(announcement.getVehicleModel().getId()));
			stringBuilder.append("vehicleModel=" + announcement.getVehicleModel().getId() + "&");
		}
		
		if(announcement.getFuelType() != null) {
			predicates.add(QAnnouncement.announcement.fuelType.eq(announcement.getFuelType()));
			stringBuilder.append("fuelType=" + announcement.getFuelType() + "&");
		}
		
		if(announcement.getVehicleSubtype() != null) {
			predicates.add(QAnnouncement.announcement.vehicleSubtype.eq(announcement.getVehicleSubtype()));
			stringBuilder.append("vehicleSubtype=" + announcement.getVehicleSubtype() + "&");
		}
		
		
		if(announcement.getSearchFields().getAccidents() != null && announcement.getSearchFields().getAccidents() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.accidents.eq(announcement.getSearchFields().getAccidents() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("searchFields.accidents=" + (announcement.getSearchFields().getAccidents() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getFirstOwner() != null && announcement.getSearchFields().getFirstOwner() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.firstOwner.eq(announcement.getSearchFields().getFirstOwner() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("searchFields.firstOwner=" + (announcement.getSearchFields().getFirstOwner() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getDamaged() != null && announcement.getSearchFields().getDamaged() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.damaged.eq(announcement.getSearchFields().getDamaged() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("searchFields.damaged=" + (announcement.getSearchFields().getDamaged() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getNetPrice() != null && announcement.getSearchFields().getNetPrice() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.netPrice.eq(announcement.getSearchFields().getNetPrice() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("searchFields.netPrice=" + (announcement.getSearchFields().getNetPrice() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		/*	if(announcement.getAccidents() != null) {
			queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getAccidents()));
			stringBuilder.append("accidents=" + announcement.getAccidents() + "&");
		}*/
		
		
		/*		if(announcement.getFirstOwner() != null) {
			queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getFirstOwner()));
			stringBuilder.append("firstOwner=" + announcement.getFirstOwner() + "&");
		}
		
		if(announcement.getDamaged() != null) {
		queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getDamaged()));
		stringBuilder.append("damaged=" + announcement.getDamaged() + "&");
		}
		
		if(announcement.getNetPrice() != null) {
		queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getNetPrice()));
		stringBuilder.append("netPrice=" + announcement.getNetPrice() + "&");
		}
		
		if(announcement.getPriceNegotiate() != null) {
		queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getPriceNegotiate()));
		stringBuilder.append("priceNegotiate=" + announcement.getPriceNegotiate() + "&");
		}*/
		
		/*	if(announcement.getSearchFields().getProductionYearFromAsOpt().isPresent() && announcement.getSearchFields().getProductionYearToAsOpt().isPresent()) {
			queryBuilder.and(QAnnouncement.announcement.productionYear.between(
					announcement.getSearchFields().getProductionYearFrom(), 
					announcement.getSearchFields().getProductionYearTo()));
			stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
			stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}*/
		if(announcement.getSearchFields().getProductionYearFromAsOpt().isPresent()) {
			predicates.add(QAnnouncement.announcement.productionYear.goe(announcement.getSearchFields().getProductionYearFrom()));
			stringBuilder.append("searchFields.productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(announcement.getSearchFields().getProductionYearToAsOpt().isPresent()) {
			predicates.add(QAnnouncement.announcement.productionYear.lt(announcement.getSearchFields().getProductionYearTo()));
			stringBuilder.append("searchFields.productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getMileageFrom())) {
			predicates.add(QAnnouncement.announcement.mileage.goe(announcement.getSearchFields().getMileageFromAsInteger()));
				stringBuilder.append("searchFields.productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getMileageTo())) {
			predicates.add(QAnnouncement.announcement.mileage.lt(announcement.getSearchFields().getMileageToAsInteger()));
				stringBuilder.append("searchFields.productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEngineCapacityFrom())) {
			predicates.add(QAnnouncement.announcement.engineCapacity.goe(announcement.getSearchFields().getEngineCapacityFromAsInteger()));
				stringBuilder.append("searchFields.productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEngineCapacityTo())) {
			predicates.add(QAnnouncement.announcement.engineCapacity.lt(announcement.getSearchFields().getEngineCapacityToAsInteger()));
				stringBuilder.append("searchFields.productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEnginePowerFrom())) {
			predicates.add(QAnnouncement.announcement.enginePower.goe(announcement.getSearchFields().getEnginePowerFromAsInteger()));
				stringBuilder.append("searchFields.productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEnginePowerTo())) {
			predicates.add(QAnnouncement.announcement.enginePower.lt(announcement.getSearchFields().getEnginePowerToAsInteger()));
				stringBuilder.append("searchFields.productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}

		if(announcement.getSearchFields().getColorList().size() > 0) {
			predicates.add(QAnnouncement.announcement.carColor.in(announcement.getSearchFields().getColorList()));
			stringBuilder.append("searchFields.colorList=" + announcement.getSearchFields().getColorList().stream().map(Object::toString).collect(Collectors.joining()) + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getPriceFrom())) {
			BigDecimal priceFrom = new BigDecimal(announcement.getSearchFields().getPriceFromWithoutSpaces());
			predicates.add(QAnnouncement.announcement.price.goe(priceFrom));
			stringBuilder.append("searchFields.priceFrom=" + priceFrom + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getPriceTo())) {
			BigDecimal priceTo = new BigDecimal(announcement.getSearchFields().getPriceToWithoutSpaces());
			predicates.add(QAnnouncement.announcement.price.loe(priceTo));
			stringBuilder.append("searchFields.priceTo=" + priceTo + "&");
		}
		
		
		//if(announcement.getVehicleType() != VehicleType.CAR)
		//	return stringBuilder.toString();
		

		if(announcement.getSearchFields().getDoorsFrom() != null) {
			predicates.add(QAnnouncement.announcement.doors.goe(announcement.getSearchFields().getDoorsFrom()));
			stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(announcement.getSearchFields().getDoorsTo() != null) {
			predicates.add(QAnnouncement.announcement.doors.lt(announcement.getSearchFields().getDoorsTo()));
			stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}

		if(announcement.getUser() != null) {
			predicates.add(QAnnouncement.announcement.user.id.eq(announcement.getUser().getId()));
			stringBuilder.append("user=" + announcement.getUser().getId() + "&");
		}

		// booleanBuilder.and(QOrder.order.validationStatus.eq(ValidationStatus.ACCOUNTED))
		
		//queryBuilder.and(QPicture.picture.announcement.id.isNotNull().and(QPicture.picture.announcement.id.eq(-1L)));
		//	queryBuilder.and(QPicture.picture.announcement.id.isNotNull().and(QPicture.picture.announcement.id.eq(announcement.getId())));
		
		//	queryBuilder.and(QAnnouncement.announcement.pictures.contains(QPicture.picture.isNotNull());
		
		return stringBuilder.toString();
	}


}
