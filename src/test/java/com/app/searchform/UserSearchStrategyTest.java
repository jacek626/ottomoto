package com.app.searchform;

import com.app.common.utils.search.PaginationDetails;
import com.app.user.entity.QUser;
import com.app.user.entity.User;
import com.app.user.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserSearchStrategyTest {

    private final static PaginationDetails paginationDetails = PaginationDetails.builder().page(1).size(10).orderBy("id").sort("ASC").build();

    @Autowired
    @Spy
    private ModelMapper modelMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserSearchStrategy userSearchStrategy;

    @Test
    public void checkThatSearchStrategyReturnAllRequiredElements() {
        //given
        var user = User.builder().build();
        when(userRepository.findAll(any(Predicate.class), any(PageRequest.class))).thenReturn(new PageImpl<>(Lists.newArrayList(user), paginationDetails.convertToPageRequest(), 10));

        //when
        Map<String, Object> users = userSearchStrategy.prepareSearchForm(user, paginationDetails);

        //then
        assertThat(users).containsKey("pageSizes");
        assertThat(users).containsKey("pages");
        assertThat(users).containsKey("size");
        assertThat(users).containsKey("size");
        assertThat(users).containsKey("pageNumbers");
        assertThat(users).containsKey("page");
        assertThat(users).containsKey("sort");
    }

    @Test
    public void shouldPrepareDataForPaginationElements() {
        //given
        var user = User.builder().id(-10L).firstName("Jan").city("Lublin").lastName("Kowalski").login("loginJan").email("jan@kowalski.pl").build();

        //when
        Predicate predicate = user.preparePredicates();

        //then
        assertThat(predicate.toString()).contains(QUser.user.firstName.containsIgnoreCase(user.getFirstName()).toString());
        assertThat(predicate.toString()).contains(QUser.user.lastName.containsIgnoreCase(user.getLastName()).toString());
        assertThat(predicate.toString()).contains(QUser.user.email.containsIgnoreCase(user.getEmail()).toString());
        assertThat(predicate.toString()).contains(QUser.user.city.containsIgnoreCase(user.getCity()).toString());
        assertThat(predicate.toString()).contains(QUser.user.login.containsIgnoreCase(user.getLogin()).toString());
    }

    @Test
    public void shouldUrlParams() {
        //given
        var user = User.builder().id(-10L).firstName("Jan").city("Lublin").lastName("Kowalski").login("loginJan").email("jan@kowalski.pl").build();

        //when
        String urlParams = user.prepareUrlParams();

        //then
        assertThat(urlParams).contains("firstName=" + user.getFirstName());
        assertThat(urlParams).contains("lastName=" + user.getLastName());
        assertThat(urlParams).contains("email=" + user.getEmail());
        assertThat(urlParams).contains("city=" + user.getCity());
        assertThat(urlParams).contains("login=" + user.getLogin());
    }


}
