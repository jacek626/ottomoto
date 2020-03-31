package com.app.searchForm;

import com.app.entity.Announcement;
import com.app.entity.QAnnouncement;
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
public class AnnouncementSearchFormStrategyTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private VehicleModelRepository vehicleModelRepository;

    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private AnnouncementSearchFormStrategy announcementSearchFormStrategy;

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
    public void shouldPreparePredicatesAndUrlParams_2() {
        //given
        Announcement announcement = TestUtils.prepareAnnouncementWithAllNeededObjects();
     //   announcement.getSearchFields().

        //when
      /*  PredicatesAndUrlParams predicatesAndUrlParams = announcementSearchFormStrategy.preparePredicatesAndUrlParams(announcement);

        //then
        assertThat(predicatesAndUrlParams.getPredicates().size()).isEqualTo(3);
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.user.id.eq(announcement.getUser().getId()));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.vehicleModel.id.eq(announcement.getVehicleModel().getId()));
        assertThat(predicatesAndUrlParams.getPredicates()).contains(QAnnouncement.announcement.vehicleSubtype.eq(VehicleSubtype.CABRIO));

        assertThat(predicatesAndUrlParams.getUrlParams()).contains("user=-1");
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("vehicleModel=-5");
        assertThat(predicatesAndUrlParams.getUrlParams()).contains("vehicleSubtype=CABRIO");
    */
    }
}
