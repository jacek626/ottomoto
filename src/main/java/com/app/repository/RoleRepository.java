package com.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	Role findByName(String name);

}
