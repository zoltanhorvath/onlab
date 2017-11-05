package com.settlerofcatan.gateway.mapper;

import com.settlerofcatan.gateway.dto.ApplicationUserDTO;
import com.settlerofcatan.gateway.entity.ApplicationUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ApplicationUser toUser(ApplicationUserDTO applicationUserDTO);

    @Mapping(target = "password", ignore = true)
    ApplicationUserDTO toUserDTO(ApplicationUser applicationUser);
}
