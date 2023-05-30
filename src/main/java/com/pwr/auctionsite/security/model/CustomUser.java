package com.pwr.auctionsite.security.model;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@ToString
public class CustomUser extends User {
    private final Long id;
    private final boolean isBlocked;

    public CustomUser(String username,
                      String password,
                      boolean isBlocked,
                      Collection<? extends GrantedAuthority> authorities,
                      Long id) {
        super(username, password, authorities);
        this.id = id;
        this.isBlocked = isBlocked;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean isEnabled() {
        return !isBlocked;
    }
}
