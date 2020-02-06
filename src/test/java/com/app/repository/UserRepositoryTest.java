package com.app.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.app.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void pageableTest() {
		
	}

}
