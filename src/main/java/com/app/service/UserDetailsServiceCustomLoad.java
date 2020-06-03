package com.app.service;

import com.app.entity.User;
import com.app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceCustomLoad implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceCustomLoad(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(login);
	        if (user == null) {
	            throw new UsernameNotFoundException(
	              "No user found with username: "+ login);
	        }
	        boolean enabled = true;
	        boolean accountNonExpired = true;
	        boolean credentialsNonExpired = true;
	        boolean accountNonLocked = true;
	        
	        
	        return  new org.springframework.security.core.userdetails.User
	          (user.getEmail(), 
	          user.getPassword().toLowerCase(), enabled, accountNonExpired, 
	          credentialsNonExpired, accountNonLocked,null );
	}

}
