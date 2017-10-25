package com.settlerofcatan.gateway.controllers;

import com.settlerofcatan.gateway.dtos.UserDTO;
import com.settlerofcatan.gateway.entities.User;
import com.settlerofcatan.gateway.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public List<UserDTO> getUsers(){
        List<UserDTO> userDTOS = StreamSupport.stream(userRepository.findAll().spliterator(), false).map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
        return userDTOS;
    }
}
