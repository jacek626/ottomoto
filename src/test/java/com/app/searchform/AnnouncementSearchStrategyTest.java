package com.app.searchform;

import com.app.entity.*;
import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.VehicleSubtype;
import com.app.enums.VehicleType;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleModelRepository;
import com.app.utils.PaginationDetails;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.PredicateOperation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnnouncementSearchStrategyTest {

    private final static PaginationDetails paginationDetails = PaginationDetails.builder().page(1).size(10).orderBy("id").sort("ASC").build();

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private VehicleModelRepository vehicleModelRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private AnnouncementSearchStrategy announcementSearchStrategy;


    @Test
    public void checkThatSearchStrategyReturnAllRequiredElements() {
        //given
        var announcement = Announcement.builder().build();

        when(announcementSearchStrategy.loadData(any(PageRequest.class), any(Predicate.class))).thenReturn(new PageImpl<>(Lists.newArrayList(announcement), paginationDetails.convertToPageRequest(), 10));
        //    when(announcementSearchStrategy.loadData(any(PageRequest.class), any(Predicate.class))).thenReturn(new PageImpl<>(Lists.newArrayList(TestUtils.prepareAnnouncement()), paginationDetails.convertToPageRequest(),10));

        //when
        Map<String, Object> announcements = announcementSearchStrategy.prepareSearchForm(announcement, paginationDetails);

        //then
        assertThat(announcements).containsKey("mileageList");
        assertThat(announcements).containsKey("pageSizes");
        assertThat(announcements).containsKey("pageNumbers");
        assertThat(announcements).containsKey("orderBy");
        assertThat(announcements).containsKey("engineCapacityList");
        assertThat(announcements).containsKey("sort");
        assertThat(announcements).containsKey("doorsList");
        assertThat(announcements).containsKey("pages");
        assertThat(announcements).containsKey("booleanValues");
        assertThat(announcements).containsKey("searchArguments");
        assertThat(announcements).containsKey("manufacturerList");
        assertThat(announcements).containsKey("vehicleSubtypeList");
        assertThat(announcements).containsKey("pricesList");
        assertThat(announcements).containsKey("page");
        assertThat(announcements).containsKey("enginePowerList");
    }

    @Test
    public void shouldPreparePredicatesInAnnouncement() {
        //given
        Manufacturer manufacturer = Manufacturer.builder().id(-1L).build();
        VehicleModel vehicleModel = VehicleModel.builder().id(-3L).name("vehicleModel").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build();

        Announcement announcement = Announcement.builder().
                vehicleSubtype(VehicleSubtype.COMPACT).
                productionYear(2_000).
                price(BigDecimal.valueOf(180_000)).
                vehicleSubtype(VehicleSubtype.CABRIO).
                user(User.builder().id(-1L).build()).
                vehicleModel(vehicleModel).
                vehicleType(VehicleType.CAR).
                build();

        //when
        Predicate predicate = announcement.preparePredicates();

        //then
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.user.id.eq(announcement.getUser().getId()).toString());
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.vehicleModel.id.eq(announcement.getVehicleModel().getId()).toString());
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.vehicleModel.vehicleType.eq(announcement.getVehicleType()).toString());
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.vehicleSubtype.eq(VehicleSubtype.CABRIO).toString());
    }

    @Test
    public void shouldPreparePredicatesWithProductionYear() {
        //given
        var announcement = new Announcement();
        announcement.getSearchFields().setProductionYearFrom(1990);
        announcement.getSearchFields().setProductionYearTo(2000);

        //when
        PredicateOperation predicateOperation = (PredicateOperation) ExpressionUtils.extract(announcement.preparePredicates());

        //then
        assertThat(predicateOperation.getArgs().size()).isEqualTo(2);
        assertThat(predicateOperation.getArgs()).contains(QAnnouncement.announcement.productionYear.goe(1990));
        assertThat(predicateOperation.getArgs()).contains(QAnnouncement.announcement.productionYear.lt(2000));
    }

    @Test
    public void shouldPreparePredicatesWithBooleanValues() {
        //given
        var announcement = new Announcement();
        announcement.getSearchFields().setAccidents(BooleanValuesForDropDown.YES);
        announcement.getSearchFields().setDamaged(BooleanValuesForDropDown.YES);
        announcement.getSearchFields().setFirstOwner(BooleanValuesForDropDown.NO);
        announcement.getSearchFields().setNetPrice(BooleanValuesForDropDown.NO);
        announcement.getSearchFields().setPriceNegotiate(BooleanValuesForDropDown.NO);

        //when
        Predicate predicate = announcement.preparePredicates();

        //then
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.accidents.eq(true).toString());
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.damaged.eq(true).toString());
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.firstOwner.eq(false).toString());
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.netPrice.eq(false).toString());
        assertThat(predicate.toString()).contains(QAnnouncement.announcement.priceNegotiate.eq(false).toString());
    }


    // spr urla
    //  Map<String, Object> prepareDataForHtmlElements(E entity);

}
