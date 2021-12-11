package com.app.searchform;

import com.app.announcement.entity.Announcement;
import com.app.announcement.entity.QAnnouncement;
import com.app.announcement.repository.AnnouncementRepository;
import com.app.announcement.types.VehicleSubtype;
import com.app.announcement.types.VehicleType;
import com.app.common.types.BooleanValuesForDropDown;
import com.app.common.utils.mapper.AnnouncementMapper;
import com.app.common.utils.search.PaginationDetails;
import com.app.manufacturer.entity.Manufacturer;
import com.app.manufacturer.repository.ManufacturerRepository;
import com.app.user.entity.User;
import com.app.vehiclemodel.entity.VehicleModel;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnnouncementSearchTest {

    private final static PaginationDetails paginationDetails = PaginationDetails.builder().page(1).size(10).orderBy("id").sort("ASC").build();

    @Mock
    @SuppressWarnings("unused")
    private AnnouncementMapper announcementMapper;

    @Mock
    @SuppressWarnings("unused")
    private ManufacturerRepository manufacturerRepository;

    @Mock
    @SuppressWarnings("unused")
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private AnnouncementSearch announcementSearch;

    @Test
    public void checkThatSearchStrategyReturnAllRequiredElements() {
        //given
        var announcement = Announcement.builder().build();

        given(announcementRepository.findByPredicatesAndLoadMainPicture(any(PageRequest.class), any(Predicate.class)))
                .willReturn(new PageImpl<>(Lists.newArrayList(announcement), paginationDetails.convertToPageRequest(), 10));

        //when
        Map<String, Object> announcements = announcementSearch.prepareSearchForm(announcement, paginationDetails);

        //then
        assertThat(announcements).containsKey("mileages");
        assertThat(announcements).containsKey("pageSizes");
        assertThat(announcements).containsKey("pageNumbers");
        assertThat(announcements).containsKey("orderBy");
        assertThat(announcements).containsKey("engineCapacities");
        assertThat(announcements).containsKey("sort");
        assertThat(announcements).containsKey("doors");
        assertThat(announcements).containsKey("pages");
        assertThat(announcements).containsKey("booleanValues");
        assertThat(announcements).containsKey("searchArguments");
        assertThat(announcements).containsKey("manufacturerList");
        assertThat(announcements).containsKey("vehicleSubtypeList");
        assertThat(announcements).containsKey("prices");
        assertThat(announcements).containsKey("page");
        assertThat(announcements).containsKey("enginePowers");
    }

    @Test
    public void shouldPreparePredicatesInAnnouncement() {
        //given
        var manufacturer = Manufacturer.builder().id(-1L).build();
        var vehicleModel = VehicleModel.builder().id(-3L).name("vehicleModel").manufacturer(manufacturer).vehicleType(VehicleType.CAR).build();

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
}
