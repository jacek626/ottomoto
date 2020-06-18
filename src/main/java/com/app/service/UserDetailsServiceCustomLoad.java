package com.app.service;

import com.app.entity.Privilege;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
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
                    "No user found with username: " + login);
        }

        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.getActive(),
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                getAuthorities(user.getRole()));
    }

    private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }

        return authorities;
    }

    private final Collection<? extends GrantedAuthority> getAuthorities(final Role role) {
        return getGrantedAuthorities(getPrivileges(role));
    }

    private final List<String> getPrivileges(final Role role) {
        final List<String> privileges = new ArrayList<>();

        for (final Privilege item : role.getPrivileges()) {
            privileges.add(item.getName());
        }

        privileges.add(role.getName());

        return privileges;
    }

}
