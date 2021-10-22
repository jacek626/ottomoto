package com.app.security.repository;

import com.app.security.entity.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

	@Cacheable("roles")
	Role findByName(String name);

}
