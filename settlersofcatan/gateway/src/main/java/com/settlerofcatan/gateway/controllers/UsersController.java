package com.settlerofcatan.gateway.controllers;

import com.settlerofcatan.gateway.dtos.RoleDTO;
import com.settlerofcatan.gateway.dtos.UserDTO;
import com.settlerofcatan.gateway.entities.Role;
import com.settlerofcatan.gateway.entities.User;
import com.settlerofcatan.gateway.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping("users")
public class UsersController {

    private UserRepository userRepository;


    private ModelMapper modelMapper;

    @Autowired
    public UsersController(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<UserDTO> getUsers() {
        List<UserDTO> userDTOS = StreamSupport.stream(userRepository.findAll().spliterator(), false).
                map(user -> {
                    RoleDTO roleDTO = modelMapper.map(user, RoleDTO.class);
                    UserDTO userDTO = modelMapper.map(user, UserDTO.class);
                    userDTO.setRoleDTO(roleDTO);
                    return userDTO;
                }).collect(toList());
        return userDTOS;
    }

    @PostMapping()
    public ResponseEntity addUser(@RequestBody final UserDTO userDTO) {

        boolean userExistsWithEmail = userRepository.existsByEmail(userDTO.getEmail());

        if (userExistsWithEmail) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userAlreadyExists(userDTO.getEmail()));
        } else {
            User user = userRepository.save(modelMapper.map(userDTO, User.class));
            userDTO.setId(user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(user, UserDTO.class));
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity getUserById(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
        } else {
            return ResponseEntity.badRequest().body(userDoesntExist(id));
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUserById(@RequestParam Long id) {
        boolean userExists = userRepository.existsById(id);

        if (userExists) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(userDeleted(id));
        } else {
            return ResponseEntity.badRequest().body(userDoesntExist(id));
        }
    }

    @PutMapping()
    public ResponseEntity updateUser(@RequestBody UserDTO userDTO) {
        boolean userExists = userRepository.existsById(userDTO.getId());

        if (userExists) {
            userRepository.save(modelMapper.map(userDTO, User.class));
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.badRequest().body(userDoesntExist(userDTO.getId()));
        }
    }

    private String userAlreadyExists(String email) {
        return "User with '" + email + "' email address already exists.";
    }

    private String userDoesntExist(Long id) {
        return "User with id: '" + id + "' doesn't exist.";
    }

    private String userDeleted(Long id) {
        return "User with id: '" + id + "' successfully deleted.";
    }
}
