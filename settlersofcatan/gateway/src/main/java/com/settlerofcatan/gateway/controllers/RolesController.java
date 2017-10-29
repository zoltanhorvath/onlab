package com.settlerofcatan.gateway.controllers;

import com.settlerofcatan.gateway.dtos.RoleDTO;
import com.settlerofcatan.gateway.entities.Role;
import com.settlerofcatan.gateway.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController()
@RequestMapping("roles")
public class RolesController {

    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    @Autowired
    public RolesController(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<RoleDTO> getRoles() {
        List<RoleDTO> roles = StreamSupport.stream(roleRepository.findAll().spliterator(), false).map(role -> modelMapper.map(role, RoleDTO.class)).collect(Collectors.toList());
        return roles;
    }

    @GetMapping("/{id}")
    public ResponseEntity getRoleById(@RequestParam Long id) {
        Optional<Role> role = roleRepository.findById(id);

        if (role.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(role, RoleDTO.class));
        } else {
            return ResponseEntity.badRequest().body(roleDoesntExist(id));
        }
    }

    @PutMapping
    public ResponseEntity updateRole(@RequestBody RoleDTO roleDTO) {
        boolean roleExists = roleRepository.existsById(roleDTO.getId());

        if (roleExists) {
            roleRepository.save(modelMapper.map(roleDTO, Role.class));
            return ResponseEntity.ok(roleDTO);
        }else{
            return ResponseEntity.badRequest().body(roleDoesntExist(roleDTO.getId()));
        }
    }

    @PostMapping()
    public ResponseEntity addRole(@RequestBody RoleDTO roleDTO) {
        boolean roleExists = roleRepository.existsByName(roleDTO.getName());

        if (roleExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleAlreadyExists(roleDTO.getName()));
        } else {
            Role role = roleRepository.save(modelMapper.map(roleDTO, Role.class));
            return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(role, RoleDTO.class));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRoleById(@RequestParam Long id) {
        boolean roleExists = roleRepository.existsById(id);

        if (roleExists) {
            roleRepository.deleteById(id);
            return ResponseEntity.ok(roleDeleted(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(roleDoesntExist(id));
        }
    }

    private String roleDoesntExist(Long id) {
        return "Role with id: '" + id + "' doesn't exist.";
    }

    private String roleDeleted(Long id) {
        return "Role with id: '" + id + "' successfully deleted.";
    }

    private String roleAlreadyExists(String name) {
        return "Role with name: '" + name + "' already exists.";
    }
}
