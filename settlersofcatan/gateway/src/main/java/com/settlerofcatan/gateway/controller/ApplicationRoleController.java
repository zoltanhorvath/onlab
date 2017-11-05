package com.settlerofcatan.gateway.controller;

import com.settlerofcatan.gateway.dto.ApplicationUserRoleDTO;
import com.settlerofcatan.gateway.exception.RoleAlreadyExistsException;
import com.settlerofcatan.gateway.exception.RoleNotFoundException;
import com.settlerofcatan.gateway.service.ApplicationRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/roles")
public class ApplicationRoleController {

    private ApplicationRoleService applicationRoleService;

    @Autowired
    public ApplicationRoleController(ApplicationRoleService applicationRoleService) {
        this.applicationRoleService = applicationRoleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getRoleById(@RequestParam Long id) {
        try {
            ApplicationUserRoleDTO userRoleDTO = applicationRoleService.getRoleById(id);
            return ResponseEntity.status(HttpStatus.OK).body(userRoleDTO);
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping()
    public List<ApplicationUserRoleDTO> getRoles() {
        return applicationRoleService.getRoles();
    }

    @PutMapping
    public ResponseEntity updateRole(@RequestBody ApplicationUserRoleDTO userRoleDTO) {
        try {
            applicationRoleService.updateRole(userRoleDTO);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity addRole(@RequestBody ApplicationUserRoleDTO userRoleDTO) {
        try {
            applicationRoleService.addRole(userRoleDTO);
            return ResponseEntity.status(HttpStatus.OK).body(userRoleDTO);
        } catch (RoleAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRoleById(@RequestParam Long id) {
        try {
            applicationRoleService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RoleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
