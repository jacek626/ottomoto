package com.otomoto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.otomoto.entity.Admin;
import com.otomoto.entity.Person;

public interface AdminRepository extends CrudRepository<Admin, Long> {
	
	List<Admin> findByLogin(String login);

}
