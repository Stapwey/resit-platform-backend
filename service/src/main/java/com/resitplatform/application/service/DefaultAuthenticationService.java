
package com.resitplatform.application.service;

import com.resitplatform.domain.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

    @SuppressWarnings("unchecked")
    @Override
    public String currentUsername() {
        Optional<User> principal = (Optional<User>) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.map(User::getUsername).orElse(null);
    }

}
