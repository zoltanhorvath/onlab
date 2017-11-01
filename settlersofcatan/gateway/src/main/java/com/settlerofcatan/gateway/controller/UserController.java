package com.settlerofcatan.gateway.controller;

import com.settlerofcatan.gateway.dto.ApplicationUserDTO;
import com.settlerofcatan.gateway.entity.ApplicationUser;
import com.settlerofcatan.gateway.mapper.UserMapper;
import com.settlerofcatan.gateway.repository.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping("api/users")
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserMapper userMapper;

    @Autowired
    public UserController(ApplicationUserRepository applicationUserRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping()
    public List<ApplicationUserDTO> getUsers() {
        List<ApplicationUserDTO> applicationUserDTOS = StreamSupport.stream(applicationUserRepository.findAll().spliterator(), false).
                map(user -> userMapper.toUserDTO(user)).collect(toList());
        return applicationUserDTOS;
    }

    @PostMapping(value = "sign-up")
    public ResponseEntity addUser(@RequestBody final ApplicationUserDTO applicationUserDTO) {

        boolean userExistsWithEmail = applicationUserRepository.existsByEmail(applicationUserDTO.getEmail());

        if (userExistsWithEmail) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userAlreadyExists(applicationUserDTO.getEmail()));
        } else {
            ApplicationUser applicationUser = userMapper.toUser(applicationUserDTO);
            applicationUser.setPassword(bCryptPasswordEncoder.encode(applicationUser.getPassword()));
            applicationUserRepository.save(applicationUser);
            applicationUserDTO.setId(applicationUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(applicationUserDTO);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getUserById(@RequestParam Long id) {
        Optional<ApplicationUser> userOptional = applicationUserRepository.findById(id);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userMapper.toUserDTO(userOptional.get()));
        } else {
            return ResponseEntity.badRequest().body(userDoesntExist(id));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUserById(@RequestParam Long id) {
        boolean userExists = applicationUserRepository.existsById(id);

        if (userExists) {
            applicationUserRepository.deleteById(id);
            return ResponseEntity.ok(userDeleted(id));
        } else {
            return ResponseEntity.badRequest().body(userDoesntExist(id));
        }
    }

    @PutMapping()
    public ResponseEntity updateUser(@RequestBody ApplicationUserDTO applicationUserDTO) {
        boolean userExists = applicationUserRepository.existsById(applicationUserDTO.getId());

        if (userExists) {
            applicationUserRepository.save(userMapper.toUser(applicationUserDTO));
            return ResponseEntity.ok(applicationUserDTO);
        } else {
            return ResponseEntity.badRequest().body(userDoesntExist(applicationUserDTO.getId()));
        }
    }

    private String userAlreadyExists(String email) {
        return "ApplicationUser with '" + email + "' email address already exists.";
    }

    private String userDoesntExist(Long id) {
        return "ApplicationUser with id: '" + id + "' doesn't exist.";
    }

    private String userDeleted(Long id) {
        return "ApplicationUser with id: '" + id + "' successfully deleted.";
    }
}
