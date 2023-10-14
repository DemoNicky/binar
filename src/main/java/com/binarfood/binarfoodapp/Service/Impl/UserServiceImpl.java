package com.binarfood.binarfoodapp.Service.Impl;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.Role;
import com.binarfood.binarfoodapp.Entity.User;
import com.binarfood.binarfoodapp.Repository.UserRepository;
import com.binarfood.binarfoodapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserRegisterResponseDTO register(UserRegisterRequsetDTO requset) {
        UserRegisterResponseDTO userRegisterResponseDTO = new UserRegisterResponseDTO();
        if (userRepository.existsByEmail(requset.getEmail())){
            userRegisterResponseDTO.setMessage("Fail to register");
            userRegisterResponseDTO.setError("Email is already exists");
            return userRegisterResponseDTO;
        }else if (userRepository.existsByUsername(requset.getUsername())){
            userRegisterResponseDTO.setMessage("Fail to register");
            userRegisterResponseDTO.setError("username is already exists");
            return userRegisterResponseDTO;
        }
        User user = new User();
        user.setUserCode(getKode());
        user.setUsername(requset.getUsername());
        user.setEmail(requset.getEmail());
        user.setPassword(requset.getPassword());
        user.setRole(Role.USER);
        user.setDeleted(false);
        userRepository.save(user);
        userRegisterResponseDTO.setMessage("Success to register");
        return userRegisterResponseDTO;
    }

    @Override
    public ResponseHandling<List<UserResponseDTO>> getUser(String name) {
        Optional<List<User>> users = userRepository.findUser(name);
        ResponseHandling<List<UserResponseDTO>> response = new ResponseHandling<>();
        List<User> users1 = users.get();
        if (!users.isPresent() || users1.isEmpty()){
            response.setMessage("cant get user");
            response.setErrors("user with username "+name+ " not found");
            return response;
        }
        List<UserResponseDTO> userResponse = users1.stream().map((p)->{
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setUsername(p.getUsername());
            userResponseDTO.setEmail(p.getEmail());
            userResponseDTO.setRole(p.getRole().toString());
            return userResponseDTO;
        }).collect(Collectors.toList());
        response.setData(userResponse);
        response.setMessage("success get user");
        return response;
    }

    @Override
    public ResponseHandling<UserResponseDTO> updateUser(String code, UserRequestUpdateDTO request) {
        Optional<User> user = userRepository.findByUserCode(code);
        ResponseHandling<UserResponseDTO> response = new ResponseHandling<>();
        if (!user.isPresent()){
            response.setMessage("cant find user");
            response.setErrors("user with code "+code+" Not found");
            return response;
        }
        User user1 = user.get();
        user1.setUsername(request.getUsername());
        user1.setEmail(request.getEmail());
        user1.setPassword(request.getPassword());
        userRepository.save(user1);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user1.getUsername());
        userResponseDTO.setEmail(user1.getEmail());
        userResponseDTO.setRole(user1.getRole().toString());
        response.setData(userResponseDTO);
        response.setMessage("success update data");
        return response;
    }

    @Override
    public UserResponseDeleteDTO deleteData(String code) {
        Optional<User> user = userRepository.findByUserCode(code);
        UserResponseDeleteDTO response = new UserResponseDeleteDTO();
        if (!user.isPresent()){
            response.setMessage("fail to delete user");
            response.setError("user data with code " +code+ " not found");
            return response;
        }
        User user1 = user.get();
        user1.setDeleted(true);
        userRepository.save(user1);
        response.setMessage("success to delete user data");
        return response;
    }

    private String getKode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 5);
        return kode;
    }
}
