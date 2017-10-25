package com.settlerofcatan.gateway.repositories;

import com.settlerofcatan.gateway.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
