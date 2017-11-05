package com.settlerofcatan.gateway.service;

import com.settlerofcatan.gateway.dto.ApplicationUserRoleDTO;
import com.settlerofcatan.gateway.entity.ApplicationUserRole;
import com.settlerofcatan.gateway.exception.RoleAlreadyExistsException;
import com.settlerofcatan.gateway.exception.RoleNotFoundException;
import com.settlerofcatan.gateway.mapper.RoleMapper;
import com.settlerofcatan.gateway.repository.ApplicationUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ApplicationRoleService {
    private RoleMapper roleMapper;
    private ApplicationUserRoleRepository applicationUserRoleRepository;

    @Autowired
    public ApplicationRoleService(RoleMapper roleMapper, ApplicationUserRoleRepository applicationUserRoleRepository) {
        this.roleMapper = roleMapper;
        this.applicationUserRoleRepository = applicationUserRoleRepository;
    }

    public ApplicationUserRoleDTO getRoleById(Long id) throws RoleNotFoundException {
        Optional<ApplicationUserRole> roleOptional = applicationUserRoleRepository.findById(id);

        if (roleOptional.isPresent()) {
            return roleMapper.toRoleDTO(roleOptional.get());
        }

        throw new RoleNotFoundException(id);
    }

    public List<ApplicationUserRoleDTO> getRoles() {
        return StreamSupport.stream(applicationUserRoleRepository.findAll().spliterator(), false).map(role -> roleMapper.toRoleDTO(role)).collect(Collectors.toList());
    }

    public void updateRole(ApplicationUserRoleDTO userRoleDTO) throws RoleNotFoundException {
        boolean roleExists = applicationUserRoleRepository.existsById(userRoleDTO.getId());

        if (roleExists) {
            applicationUserRoleRepository.save(roleMapper.toRole(userRoleDTO));
        }

        throw new RoleNotFoundException(userRoleDTO.getId());
    }

    public void addRole(ApplicationUserRoleDTO userRoleDTO) throws RoleAlreadyExistsException {
        boolean roleExists = applicationUserRoleRepository.existsByName(userRoleDTO.getName());

        if (roleExists) {
            throw new RoleAlreadyExistsException(userRoleDTO.getName().name());
        }

        ApplicationUserRole applicationUserRole = applicationUserRoleRepository.save(roleMapper.toRole(userRoleDTO));
        userRoleDTO.setId(applicationUserRole.getId());
    }

    public void deleteById(Long id) {
        boolean roleExists = applicationUserRoleRepository.existsById(id);

        if (roleExists) {
            applicationUserRoleRepository.deleteById(id);
        }

        throw new RoleNotFoundException(id);
    }
}

