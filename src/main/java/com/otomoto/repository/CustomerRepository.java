package com.otomoto.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.otomoto.entity.Customer;
import com.otomoto.entity.Person;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	List<Person> findByLogin(String login);
	List<Person> countByEmail(String email);
}
