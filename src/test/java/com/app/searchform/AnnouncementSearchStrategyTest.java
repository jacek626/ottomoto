package com.app.searchform;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
import com.app.enums.BooleanValuesForDropDown;
import com.app.enums.VehicleSubtype;
import com.app.repository.AnnouncementRepository;
import com.app.repository.ManufacturerRepository;
import com.app.repository.VehicleModelRepository;
import com.app.utils.PredicatesAndUrlParams;
import com.app.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AnnouncementSearchStrategyTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private VehicleModelRepository vehicleModelRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private AnnouncementSearchStrategy announcementSearchFormStrategy;

    @Test
    public void shouldPreparePredicatesAndUrlParamsOnlyInAnnouncement() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncementWithAllNeededObjects();

        //when
        PredicatesAndUrlParams predicatesAndUrlParams = announcementSearchFormStrategy.preparePredicatesAndUrlParams(announcement);

        //then
        assertThat(predicatesAndUrlParams.getPredicates().size()).isEqualTo(5);
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.user.id.eq(announcement.getUser().getId()));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.vehicleModel.manufacturer.id.eq(announcement.getManufacturerId()));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.vehicleModel.id.eq(announcement.getVehicleModel().getId()));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.vehicleModel.vehicleType.eq(announcement.getVehicleType()));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.vehicleSubtype.eq(VehicleSubtype.CABRIO));

        assertThat(predicatesAndUrlParams.getUrlParams()).contains("user=" + announcement.getUser().getId());
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("manufacturerId=" + announcement.getManufacturerId());
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("vehicleModel=" + announcement.getVehicleModel().getId());
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("vehicleType="+announcement.getVehicleType().toString());
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("vehicleSubtype="+announcement.getVehicleSubtype().toString());
    }

    @Test
    public void shouldPreparePredicatesAndUrlParamsWithProductionYear() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncementWithAllNeededObjects();
        announcement.getSearchFields().setProductionYearFrom(1990);
        announcement.getSearchFields().setProductionYearTo(2000);

        //when
        PredicatesAndUrlParams predicatesAndUrlParams = announcementSearchFormStrategy.preparePredicatesAndUrlParams(announcement);

        //then
        assertThat(predicatesAndUrlParams.getPredicates().size()).isEqualTo(7);
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.productionYear.goe(1990));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.productionYear.lt(2000));

        assertThat(predicatesAndUrlParams.getUrlParams()).contains("searchFields.productionYearFrom=1990");
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("searchFields.productionYearTo=2000");

    }

    @Test
    public void shouldPreparePredicatesAndUrlParamsWithBooleanValues() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncementWithAllNeededObjects();
        announcement.getSearchFields().setAccidents(BooleanValuesForDropDown.YES);
        announcement.getSearchFields().setDamaged(BooleanValuesForDropDown.YES);
        announcement.getSearchFields().setFirstOwner(BooleanValuesForDropDown.NO);
        announcement.getSearchFields().setNetPrice(BooleanValuesForDropDown.NO);
        announcement.getSearchFields().setPriceNegotiate(BooleanValuesForDropDown.NO);

        //when
        PredicatesAndUrlParams predicatesAndUrlParams = announcementSearchFormStrategy.preparePredicatesAndUrlParams(announcement);

        //then
        assertThat(predicatesAndUrlParams.getPredicates().size()).isEqualTo(10);
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.accidents.eq(true));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.damaged.eq(true));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.firstOwner.eq(false));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.netPrice.eq(false));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.priceNegotiate.eq(false));

        assertThat(predicatesAndUrlParams.getUrlParams()).contains("searchFields.accidents=true");
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("searchFields.damaged=true");
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("searchFields.firstOwner=false");
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("searchFields.netPrice=false");
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("searchFields.priceNegotiate=false");

    }


}
