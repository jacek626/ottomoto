package com.otomoto.repository;

import org.springframework.data.repository.CrudRepository;

import com.otomoto.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByLogin(String login);
	Integer countByEmail(String email);
}
	