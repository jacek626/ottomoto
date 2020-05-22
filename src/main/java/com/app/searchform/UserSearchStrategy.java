package com.app.searchform;

import com.app.entity.User;
import com.app.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class UserSearchStrategy implements SearchStrategy<User> {

    private final UserRepository userRepository;

    public UserSearchStrategy(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> loadData(PageRequest pageRequest, Predicate predicate) {
        return userRepository.findAll(predicate, pageRequest);
    }
}
