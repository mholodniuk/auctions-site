package com.pwr.auctionsite.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationContext authenticationContext;

    public User getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(User.class)
                .orElse(null);
    }

    public void logout() {
        authenticationContext.logout();
    }
}