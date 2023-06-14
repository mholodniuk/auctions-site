package com.pwr.auctionsite.security;

import com.pwr.auctionsite.security.model.CustomUser;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationContext authenticationContext;

    public User getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(User.class)
                .orElse(null);
    }

    public CustomUser getUser() {
        return authenticationContext.getAuthenticatedUser(CustomUser.class)
                .orElse(null);
    }

    public Set<String> getAuthenticatedUserRole() {
        return AuthorityUtils.authorityListToSet(getAuthenticatedUser().getAuthorities());
    }

    public void logout() {
        authenticationContext.logout();
    }
}