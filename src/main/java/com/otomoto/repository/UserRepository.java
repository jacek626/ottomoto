package com.otomoto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.otomoto.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	List<User> findByLogin(String login);
	Integer countByEmail(String email);
}
	