package com.settlerofcatan.gateway.repositories;

import com.settlerofcatan.gateway.entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

    boolean existsByName(String name);
}
