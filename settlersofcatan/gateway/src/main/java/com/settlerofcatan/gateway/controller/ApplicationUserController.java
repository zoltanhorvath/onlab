package com.settlerofcatan.gateway.controller;

import com.settlerofcatan.gateway.dto.ApplicationUserDTO;
import com.settlerofcatan.gateway.exception.UserAlreadyExistsException;
import com.settlerofcatan.gateway.exception.UserNotFoundException;
import com.settlerofcatan.gateway.mapper.UserMapper;
import com.settlerofcatan.gateway.repository.ApplicationUserRepository;
import com.settlerofcatan.gateway.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/users")
public class ApplicationUserController {

    private ApplicationUserService applicationUserService;

    @Autowired
    public ApplicationUserController(ApplicationUserRepository applicationUserRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder, ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping(value = "sign-up")
    public ResponseEntity addUser(@RequestBody final ApplicationUserDTO userDTO) {
        try {
            applicationUserService.addUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
        } catch (UserAlreadyExistsException existsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(existsException.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping()
    public List<ApplicationUserDTO> getUsers() {
        return applicationUserService.listUsers();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getUserById(@RequestParam Long id) {
        try {
            ApplicationUserDTO userDTO = applicationUserService.getUserById(id);
            return ResponseEntity.ok(userDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUserById(@RequestParam Long id) {
        try {
            applicationUserService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping()
    public ResponseEntity updateUser(@RequestBody final ApplicationUserDTO userDTO) {
        try {
            applicationUserService.updateUser(userDTO);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
