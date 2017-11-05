package com.settlerofcatan.gateway.service;

import com.settlerofcatan.gateway.dto.ApplicationUserDTO;
import com.settlerofcatan.gateway.entity.ApplicationUser;
import com.settlerofcatan.gateway.exception.UserAlreadyExistsException;
import com.settlerofcatan.gateway.exception.UserNotFoundException;
import com.settlerofcatan.gateway.mapper.UserMapper;
import com.settlerofcatan.gateway.repository.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
public class ApplicationUserService {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserService.class);
    private ApplicationUserRepository applicationUserRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(ApplicationUserDTO userDTO) throws UserAlreadyExistsException {
        boolean userExistsWithEmail = applicationUserRepository.existsByEmail(userDTO.getEmail());

        if (userExistsWithEmail) {
            throw new UserAlreadyExistsException(userDTO.getEmail());
        }

        ApplicationUser applicationUser = userMapper.toUser(userDTO);
        applicationUser.setPassword(passwordEncoder.encode(applicationUser.getPassword()));
        applicationUserRepository.save(applicationUser);
        userDTO.setId(applicationUser.getId());

        log.debug("User created: " + userDTO);
    }

    public List<ApplicationUserDTO> listUsers() {
        return StreamSupport.stream(applicationUserRepository.findAll().spliterator(), false).
                map(user -> userMapper.toUserDTO(user)).collect(toList());
    }

    public ApplicationUserDTO getUserById(Long id) throws UserNotFoundException {
        Optional<ApplicationUser> applicationUserOption = applicationUserRepository.findById(id);

        if (applicationUserOption.isPresent()) {
            log.debug("User found in DB with id= " + id);
            return userMapper.toUserDTO(applicationUserOption.get());
        }

        throw new UserNotFoundException(id);
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        boolean userExists = applicationUserRepository.existsById(id);

        if (userExists) {
            applicationUserRepository.deleteById(id);
            log.debug("User found in DB and deleted.");
        }

        throw new UserNotFoundException(id);
    }


    public void updateUser(ApplicationUserDTO userDTO) throws UserNotFoundException {
        boolean userExists = applicationUserRepository.existsById(userDTO.getId());

        if (userExists) {
            applicationUserRepository.save(userMapper.toUser(userDTO));
            log.debug("User updated with id = " + userDTO.getId());
        }

        throw new UserNotFoundException(userDTO.getId());
    }
}
