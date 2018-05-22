package me.integrate.socialbank.auth;

import me.integrate.socialbank.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public AuthUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        me.integrate.socialbank.user.User userByEmail = userService.getUserByEmail(username);
        return new User(username, userByEmail.getPassword(), userByEmail.getName(), userByEmail.getEnabled(), new ArrayList<>());
    }

}
