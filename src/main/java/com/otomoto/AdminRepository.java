package com.otomoto;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.otomoto.entity.Admin;
import com.otomoto.entity.Person;

public interface AdminRepository extends CrudRepository<Admin, Long> {
	
	List<Admin> findByLogin(String login);
	List<Admin> countByEmail(String email);
	
	// @Query("SELECT COUNT(u) FROM User u WHERE u.name=?1")
   // Long aMethodNameOrSomething(String name);

}
