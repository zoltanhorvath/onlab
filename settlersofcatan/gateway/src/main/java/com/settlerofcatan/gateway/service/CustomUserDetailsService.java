package com.settlerofcatan.gateway.service;

import com.settlerofcatan.gateway.entity.ApplicationUser;
import com.settlerofcatan.gateway.exception.UserNotFoundException;
import com.settlerofcatan.gateway.repository.ApplicationUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private ApplicationUserRepository applicationUserRepository;


    public CustomUserDetailsService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        Optional<ApplicationUser> applicationUserOptional = applicationUserRepository.findByEmail(email);

        if (applicationUserOptional.isPresent()) {
            ApplicationUser applicationUser = applicationUserOptional.get();
            return new User(applicationUser.getEmail(), applicationUser.getPassword(), new ArrayList<>());
        }

        throw new UserNotFoundException(email);
    }
}
