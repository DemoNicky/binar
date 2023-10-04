package com.binarfood.binarapp.Service.Impl;

import com.binarfood.binarapp.DTO.FindUserDTO;
import com.binarfood.binarapp.DTO.UserLoginDTO;
import com.binarfood.binarapp.Entity.Role;
import com.binarfood.binarapp.Entity.User;
import com.binarfood.binarapp.Repository.UserRepository;
import com.binarfood.binarapp.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public String register(String username, String email, String pass) {
        logger.info("Registering user: {}", username);

        User user = User.builder()
                .username(username)
                .email(email)
                .password(pass)
                .role(Role.USER)
                .build();

        if (userRepository.existsByUsername(username) || userRepository.existsByEmail(email)){
            logger.warn("Username/Email has already been used.");

            return "Username/Email telah di pakai";
        } else {
            userRepository.save(user);
            logger.info("User registered: {}", user);

            return "Data telah Disimpan Shilakan Login";
        }

    }

    @Override
    public Optional<User> login(UserLoginDTO userLoginDTO) {
        logger.info("User login: {}", userLoginDTO.getUsername());

        try {
            Optional<User> user = userRepository.findByUsername(userLoginDTO.getUsername());
            if (user.isPresent()){
                User user2 = user.get();
                if (user2.getPassword().equals(userLoginDTO.getPassword())){
                    logger.info("User logged in: {}", user2.getUsername());

                    return Optional.of(user2);
                }
            }
            logger.warn("Login failed for user: {}", userLoginDTO.getUsername());

            return Optional.empty();
        }catch (NullPointerException e){
            logger.error("Error while logging in: {}", e.getMessage());

            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Boolean createUser(String id) {
        logger.info("Creating admin user with ID: {}", id);

        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            User user1 = user.get();
            user1.setRole(Role.ADMIN);
            userRepository.save(user1);
            logger.info("Admin user created: {}", user1.getUsername());

            return true;
        }
        logger.warn("Failed to create admin user because user not found.");

        return false;
    }

    @Override
    public FindUserDTO findUser(String username) {
        logger.info("Finding user by username: {}", username);

        Optional<User> user = userRepository.findUser(username);
        if (user.isPresent()){
            User user1 = user.get();
            FindUserDTO findUserDTO = FindUserDTO.builder()
                    .username(user1.getUsername())
                    .email(user1.getEmail())
                    .role(user1.getRole().toString())
                    .build();
            logger.info("User found: {}", username);

            return findUserDTO;
        }else {
            logger.warn("User not found for username: {}", username);

            throw new NullPointerException("User not found for username: " + username);
        }
    }

    @Override
    public Boolean deleteUser(String id) {
        logger.info("Deleting user with ID: {}", id);

        User user = userRepository.findById(id).get();
        if (user.equals(null) || user == null){
            throw new NullPointerException("User tidak di temukan");
        }else {
            userRepository.deleteById(user.getId());
            logger.info("User deleted: {}", user.getUsername());

            return true;
        }
    }
}
