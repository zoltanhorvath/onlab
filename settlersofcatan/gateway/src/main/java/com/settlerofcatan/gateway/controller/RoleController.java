package com.settlerofcatan.gateway.controller;

import com.settlerofcatan.gateway.dto.ApplicationUserRoleDTO;
import com.settlerofcatan.gateway.entity.ApplicationUserRole;
import com.settlerofcatan.gateway.mapper.RoleMapper;
import com.settlerofcatan.gateway.repository.ApplicationUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController()
@RequestMapping("api/roles")
public class RoleController {

    private ApplicationUserRoleRepository applicationUserRoleRepository;
    private RoleMapper roleMapper;

    @Autowired
    public RoleController(ApplicationUserRoleRepository applicationUserRoleRepository, RoleMapper roleMapper) {
        this.applicationUserRoleRepository = applicationUserRoleRepository;
        this.roleMapper = roleMapper;
    }

    @GetMapping()
    public List<ApplicationUserRoleDTO> getRoles() {
        List<ApplicationUserRoleDTO> roles = StreamSupport.stream(applicationUserRoleRepository.findAll().spliterator(), false).map(role -> roleMapper.toRoleDTO(role)).collect(Collectors.toList());
        return roles;
    }

    @GetMapping("/{id}")
    public ResponseEntity getRoleById(@RequestParam Long id) {
        Optional<ApplicationUserRole> roleOptional = applicationUserRoleRepository.findById(id);

        if (roleOptional.isPresent()) {
            return ResponseEntity.ok(roleMapper.toRoleDTO(roleOptional.get()));
        } else {
            return ResponseEntity.badRequest().body(roleDoesntExist(id));
        }
    }

    @PutMapping
    public ResponseEntity updateRole(@RequestBody ApplicationUserRoleDTO applicationUserRoleDTO) {
        boolean roleExists = applicationUserRoleRepository.existsById(applicationUserRoleDTO.getId());

        if (roleExists) {
            applicationUserRoleRepository.save(roleMapper.toRole(applicationUserRoleDTO));
            return ResponseEntity.ok(applicationUserRoleDTO);
        } else {
            return ResponseEntity.badRequest().body(roleDoesntExist(applicationUserRoleDTO.getId()));
        }
    }

    @PostMapping()
    public ResponseEntity addRole(@RequestBody ApplicationUserRoleDTO applicationUserRoleDTO) {
        boolean roleExists = applicationUserRoleRepository.existsByName(applicationUserRoleDTO.getName());

        if (roleExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleAlreadyExists(applicationUserRoleDTO.getName()));
        } else {
            ApplicationUserRole applicationUserRole = applicationUserRoleRepository.save(roleMapper.toRole(applicationUserRoleDTO));
            applicationUserRoleDTO.setId(applicationUserRole.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(applicationUserRoleDTO);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRoleById(@RequestParam Long id) {
        boolean roleExists = applicationUserRoleRepository.existsById(id);

        if (roleExists) {
            applicationUserRoleRepository.deleteById(id);
            return ResponseEntity.ok(roleDeleted(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(roleDoesntExist(id));
        }
    }

    private String roleDoesntExist(Long id) {
        return "ApplicationUserRole with id: '" + id + "' doesn't exist.";
    }

    private String roleDeleted(Long id) {
        return "ApplicationUserRole with id: '" + id + "' successfully deleted.";
    }

    private String roleAlreadyExists(String name) {
        return "ApplicationUserRole with name: '" + name + "' already exists.";
    }
}
