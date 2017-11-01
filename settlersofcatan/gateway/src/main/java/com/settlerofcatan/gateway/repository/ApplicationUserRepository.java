package com.settlerofcatan.gateway.repository;

import com.settlerofcatan.gateway.entity.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Long> {

    Optional<ApplicationUser> findByEmail(String email);
    boolean existsByEmail(String email);

}
