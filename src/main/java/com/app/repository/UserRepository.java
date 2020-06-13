package com.app.repository;

import com.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, QuerydslPredicateExecutor<User> {
    User findByLogin(String login);

    @Query("SELECT u.phoneNumber FROM User u where u.id = :id")
    Integer findPhoneNumberById(long id);

    @Query("SELECT u.password FROM User u where u.id = :id")
    String findPasswordById(Long id);

    @Query("SELECT u.id FROM User u where u.login = :login")
    long findIdByLogin(String login);

    int countByLogin(String login);

    User findByEmail(String login);

    int countByEmail(String email);

	int countByEmailAndIdNot(String email, long id);

	int countByLoginAndIdNot(String email, long id);

	Page<User> findAll(Pageable pageable);
	Page<User> findByLoginContainingIgnoreCase(String login, Pageable pageable);
	Page<User> findByLoginContainingIgnoreCaseAndEmailAndFirstNameAndLastName(String login, String email, String firstName, String lastName, Pageable pageable);
}
