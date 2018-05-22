package me.integrate.socialbank.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class User extends org.springframework.security.core.userdetails.User {
    private final String name;
    private final boolean enabled;

    public User(String username, String password, String name, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.name = name;
        this.enabled = enabled;
    }

    String getName() {
        return name;
    }

    boolean getEnabled() {
        return enabled;
    }
}
