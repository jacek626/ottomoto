package com.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, QuerydslPredicateExecutor<User> {
	User findByLogin(String login);
	int countByLogin(String login);
	User findByEmail(String login);
	int countByEmail(String email);
	int countByEmailAndIdNot(String email, long id);
	
	
	
	Page<User> findAll(Pageable pageable);
	Page<User> findByLoginContainingIgnoreCase(String login, Pageable pageable);
	// email, firstName lastname 
	Page<User> findByLoginContainingIgnoreCaseAndEmailAndFirstNameAndLastName(String login, String email, String firstName, String lastName, Pageable pageable);
	
	
	
	
	
	//@Query(value = "select new com.datum.fnd.domain.Node(c.idNode, c.name,c.address, c.description, c.point) from Node c")
	//List<User> getNodesByPage(Pageable pageable);
}
	