package com.samsonan.counters.web;

import org.springframework.security.core.context.SecurityContextHolder;

import com.samsonan.counters.domain.Counter;
import com.samsonan.counters.domain.User;

public class BaseController {

    protected String getCurrentUserLogin() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    protected boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(a -> a.getAuthority()).anyMatch(s -> s.equals(User.ROLE_ADMIN));
    }    
    
    /**
     * If the current user has permissions on given counter
     */
    protected boolean isHasPermission(Counter counter) {
        return isAdmin() || getCurrentUserLogin().equals(counter.getOwner());
    }
    
}
