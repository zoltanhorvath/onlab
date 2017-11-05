package com.settlerofcatan.gateway.repository;

import com.settlerofcatan.gateway.entity.ApplicationUserRole;
import jwt.RoleType;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRoleRepository extends CrudRepository<ApplicationUserRole, Long> {

    boolean existsByName(RoleType name);
}
