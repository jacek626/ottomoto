package com.app.searchform;

import com.app.user.dto.UserDto;
import com.app.user.entity.User;
import com.app.user.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class UserSearch implements SearchStrategy<User, UserDto> {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserSearch(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<User> loadData(PageRequest pageRequest, Predicate predicate) {
        return userRepository.findAll(predicate, pageRequest);
    }

    @Override
    public UserDto convertToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }
}
