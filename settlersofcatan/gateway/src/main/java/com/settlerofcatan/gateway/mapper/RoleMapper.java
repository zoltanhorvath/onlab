package com.settlerofcatan.gateway.mapper;

import com.settlerofcatan.gateway.dto.ApplicationUserRoleDTO;
import com.settlerofcatan.gateway.entity.ApplicationUserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    ApplicationUserRoleDTO toRoleDTO(ApplicationUserRole applicationUserRole);
    ApplicationUserRole toRole(ApplicationUserRoleDTO applicationUserRoleDTO);
}
