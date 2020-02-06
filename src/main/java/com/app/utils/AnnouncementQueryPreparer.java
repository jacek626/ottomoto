package com.app.utils;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
import com.app.entity.QPicture;
import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.VehicleType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Component
public class AnnouncementQueryPreparer {

	public String prepareQueryAndSearchArguments(Announcement announcement, String searchArguments, List<Predicate> predicates) {
		StringBuilder stringBuilder = new StringBuilder(searchArguments);
		if(StringUtils.isNotBlank(announcement.getSearchFields().getPriceFrom())) {
			BigDecimal priceFrom = new BigDecimal(announcement.getSearchFields().getPriceFromWithoutSpaces());
			predicates.add(QAnnouncement.announcement.price.goe(priceFrom));
			stringBuilder.append("priceFrom=" + priceFrom + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getPriceTo())) { 
			BigDecimal priceTo = new BigDecimal(announcement.getSearchFields().getPriceToWithoutSpaces());
			predicates.add(QAnnouncement.announcement.price.loe(priceTo));
			stringBuilder.append("priceTo=" + priceTo + "&");
		}
		if(announcement.getManufacturer() != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.manufacturer.name.contains(announcement.getManufacturer().getName()));
			stringBuilder.append("manufacturer=" + announcement.getManufacturer().getId() + "&");
		}
		
		if(announcement.getVehicleType() != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.vehicleType.eq(announcement.getVehicleType()));
			stringBuilder.append("VehicleType=" + announcement.getVehicleType() + "&");
		}
		if(announcement.getVehicleModel() != null) {
			predicates.add(QAnnouncement.announcement.vehicleModel.id.eq(announcement.getVehicleModel().getId()));
			stringBuilder.append("vehicleModel=" + announcement.getVehicleModel().getName() + "&");
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
			stringBuilder.append("accidents=" + (announcement.getSearchFields().getAccidents() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getFirstOwner() != null && announcement.getSearchFields().getFirstOwner() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.firstOwner.eq(announcement.getSearchFields().getFirstOwner() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("firstOwner=" + (announcement.getSearchFields().getFirstOwner() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getDamaged() != null && announcement.getSearchFields().getDamaged() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.damaged.eq(announcement.getSearchFields().getDamaged() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("damaged=" + (announcement.getSearchFields().getDamaged() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getNetPrice() != null && announcement.getSearchFields().getNetPrice() != BooleanValuesForDropDown.ALL) {
			predicates.add(QAnnouncement.announcement.netPrice.eq(announcement.getSearchFields().getNetPrice() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("netPrice=" + (announcement.getSearchFields().getNetPrice() == BooleanValuesForDropDown .YES ? true : false) + "&");
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
			stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(announcement.getSearchFields().getProductionYearToAsOpt().isPresent()) {
			predicates.add(QAnnouncement.announcement.productionYear.lt(announcement.getSearchFields().getProductionYearTo()));
			stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getMileageFrom())) {
			predicates.add(QAnnouncement.announcement.mileage.goe(announcement.getSearchFields().getMileageFromAsInteger()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getMileageTo())) {
			predicates.add(QAnnouncement.announcement.mileage.lt(announcement.getSearchFields().getMileageToAsInteger()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEngineCapacityFrom())) {
			predicates.add(QAnnouncement.announcement.engineCapacity.goe(announcement.getSearchFields().getEngineCapacityFromAsInteger()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEngineCapacityTo())) {
			predicates.add(QAnnouncement.announcement.engineCapacity.lt(announcement.getSearchFields().getEngineCapacityToAsInteger()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEnginePowerFrom())) {
			predicates.add(QAnnouncement.announcement.enginePower.goe(announcement.getSearchFields().getEnginePowerFromAsInteger()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEnginePowerTo())) {
			predicates.add(QAnnouncement.announcement.enginePower.lt(announcement.getSearchFields().getEnginePowerToAsInteger()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(announcement.getSearchFields().getColorList().size() > 0) {
			predicates.add(QAnnouncement.announcement.carColor.in(announcement.getSearchFields().getColorList()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(announcement.getSearchFields().getFuelTypeList().size() > 0) {
			predicates.add(QAnnouncement.announcement.fuelType.in(announcement.getSearchFields().getFuelTypeList()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		
		if(announcement.getVehicleType() != VehicleType.CAR)
			return stringBuilder.toString();
		
		
		if(announcement.getSearchFields().getDoorsFrom() != null) {
			predicates.add(QAnnouncement.announcement.doors.goe(announcement.getSearchFields().getDoorsFrom()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(announcement.getSearchFields().getDoorsTo() != null) {
			predicates.add(QAnnouncement.announcement.doors.lt(announcement.getSearchFields().getDoorsTo()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		// booleanBuilder.and(QOrder.order.validationStatus.eq(ValidationStatus.ACCOUNTED))
		
		//queryBuilder.and(QPicture.picture.announcement.id.isNotNull().and(QPicture.picture.announcement.id.eq(-1L)));
		//	queryBuilder.and(QPicture.picture.announcement.id.isNotNull().and(QPicture.picture.announcement.id.eq(announcement.getId())));
		
		//	queryBuilder.and(QAnnouncement.announcement.pictures.contains(QPicture.picture.isNotNull());
		
		return stringBuilder.toString();
	}
	
	public String prepareQueryAndSearchArgumentsOld(Announcement announcement, String searchArguments, BooleanBuilder queryBuilder) {
		StringBuilder stringBuilder = new StringBuilder(searchArguments);
		if(StringUtils.isNotBlank(announcement.getSearchFields().getPriceFrom())) {
			BigDecimal priceFrom = new BigDecimal(announcement.getSearchFields().getPriceFromWithoutSpaces());
			queryBuilder.and(QAnnouncement.announcement.price.goe(priceFrom));
			stringBuilder.append("priceFrom=" + priceFrom + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getPriceTo())) { 
			BigDecimal priceTo = new BigDecimal(announcement.getSearchFields().getPriceToWithoutSpaces());
			queryBuilder.and(QAnnouncement.announcement.price.loe(priceTo));
			stringBuilder.append("priceTo=" + priceTo + "&");
		}
		if(announcement.getManufacturer() != null) {
			queryBuilder.and(QAnnouncement.announcement.vehicleModel.manufacturer.name.contains(announcement.getManufacturer().getName()));
			stringBuilder.append("manufacturer=" + announcement.getManufacturer().getId() + "&");
		}
		
		if(announcement.getVehicleType() != null) {
			queryBuilder.and(QAnnouncement.announcement.vehicleModel.vehicleType.eq(announcement.getVehicleType()));
			stringBuilder.append("VehicleType=" + announcement.getVehicleType() + "&");
		}
		if(announcement.getVehicleModel() != null) {
			queryBuilder.and(QAnnouncement.announcement.vehicleModel.id.eq(announcement.getVehicleModel().getId()));
			stringBuilder.append("vehicleModel=" + announcement.getVehicleModel().getName() + "&");
		}
		
		if(announcement.getFuelType() != null) {
			queryBuilder.and(QAnnouncement.announcement.fuelType.eq(announcement.getFuelType()));
			stringBuilder.append("fuelType=" + announcement.getFuelType() + "&");
		}
		
		if(announcement.getVehicleSubtype() != null) {
			queryBuilder.and(QAnnouncement.announcement.vehicleSubtype.eq(announcement.getVehicleSubtype()));
			stringBuilder.append("vehicleSubtype=" + announcement.getVehicleSubtype() + "&");
		}
		
		
		if(announcement.getSearchFields().getAccidents() != null && announcement.getSearchFields().getAccidents() != BooleanValuesForDropDown.ALL) {
			queryBuilder.and(QAnnouncement.announcement.accidents.eq(announcement.getSearchFields().getAccidents() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("accidents=" + (announcement.getSearchFields().getAccidents() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getFirstOwner() != null && announcement.getSearchFields().getFirstOwner() != BooleanValuesForDropDown.ALL) {
			queryBuilder.and(QAnnouncement.announcement.firstOwner.eq(announcement.getSearchFields().getFirstOwner() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("firstOwner=" + (announcement.getSearchFields().getFirstOwner() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getDamaged() != null && announcement.getSearchFields().getDamaged() != BooleanValuesForDropDown.ALL) {
			queryBuilder.and(QAnnouncement.announcement.damaged.eq(announcement.getSearchFields().getDamaged() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("damaged=" + (announcement.getSearchFields().getDamaged() == BooleanValuesForDropDown .YES ? true : false) + "&");
		}
		
		if(announcement.getSearchFields().getNetPrice() != null && announcement.getSearchFields().getNetPrice() != BooleanValuesForDropDown.ALL) {
			queryBuilder.and(QAnnouncement.announcement.netPrice.eq(announcement.getSearchFields().getNetPrice() == BooleanValuesForDropDown .YES ? true : false));
			stringBuilder.append("netPrice=" + (announcement.getSearchFields().getNetPrice() == BooleanValuesForDropDown .YES ? true : false) + "&");
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
			queryBuilder.and(QAnnouncement.announcement.productionYear.goe(announcement.getSearchFields().getProductionYearFrom()));
			stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(announcement.getSearchFields().getProductionYearToAsOpt().isPresent()) {
			queryBuilder.and(QAnnouncement.announcement.productionYear.lt(announcement.getSearchFields().getProductionYearTo()));
			stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getMileageFrom())) {
			queryBuilder.and(QAnnouncement.announcement.mileage.goe(announcement.getSearchFields().getMileageFromAsInteger()));
		//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getMileageTo())) {
			queryBuilder.and(QAnnouncement.announcement.mileage.lt(announcement.getSearchFields().getMileageToAsInteger()));
		//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEngineCapacityFrom())) {
			queryBuilder.and(QAnnouncement.announcement.engineCapacity.goe(announcement.getSearchFields().getEngineCapacityFromAsInteger()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEngineCapacityTo())) {
			queryBuilder.and(QAnnouncement.announcement.engineCapacity.lt(announcement.getSearchFields().getEngineCapacityToAsInteger()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEnginePowerFrom())) {
			queryBuilder.and(QAnnouncement.announcement.enginePower.goe(announcement.getSearchFields().getEnginePowerFromAsInteger()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(StringUtils.isNotBlank(announcement.getSearchFields().getEnginePowerTo())) {
			queryBuilder.and(QAnnouncement.announcement.enginePower.lt(announcement.getSearchFields().getEnginePowerToAsInteger()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(announcement.getSearchFields().getColorList().size() > 0) {
			queryBuilder.and(QAnnouncement.announcement.carColor.in(announcement.getSearchFields().getColorList()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		if(announcement.getSearchFields().getFuelTypeList().size() > 0) {
			queryBuilder.and(QAnnouncement.announcement.fuelType.in(announcement.getSearchFields().getFuelTypeList()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		
		if(announcement.getVehicleType() != VehicleType.CAR)
			return stringBuilder.toString();
		
		
		if(announcement.getSearchFields().getDoorsFrom() != null) {
			queryBuilder.and(QAnnouncement.announcement.doors.goe(announcement.getSearchFields().getDoorsFrom()));
			//	stringBuilder.append("productionYearFrom=" + announcement.getSearchFields().getProductionYearFrom() + "&");
		}
		if(announcement.getSearchFields().getDoorsTo() != null) {
			queryBuilder.and(QAnnouncement.announcement.doors.lt(announcement.getSearchFields().getDoorsTo()));
			//	stringBuilder.append("productionYearTo=" + announcement.getSearchFields().getProductionYearTo() + "&");
		}
		
		// booleanBuilder.and(QOrder.order.validationStatus.eq(ValidationStatus.ACCOUNTED))
		
	//queryBuilder.and(QPicture.picture.announcement.id.isNotNull().and(QPicture.picture.announcement.id.eq(-1L)));
		//	queryBuilder.and(QPicture.picture.announcement.id.isNotNull().and(QPicture.picture.announcement.id.eq(announcement.getId())));

		//	queryBuilder.and(QAnnouncement.announcement.pictures.contains(QPicture.picture.isNotNull());
		
		return stringBuilder.toString();
	}
}
