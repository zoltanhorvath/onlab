package com.settlerofcatan.gateway.mapper;

import com.settlerofcatan.gateway.dto.ApplicationUserDTO;
import com.settlerofcatan.gateway.entity.ApplicationUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ApplicationUser toUser(ApplicationUserDTO applicationUserDTO);
    ApplicationUserDTO toUserDTO(ApplicationUser applicationUser);
}
