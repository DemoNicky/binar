package com.binarfood.binarapp.Service;

import com.binarfood.binarapp.DTO.FindUserDTO;
import com.binarfood.binarapp.DTO.UserLoginDTO;
import com.binarfood.binarapp.Entity.User;

import java.util.Optional;

public interface UserService {
    String register(String username, String email, String pass);

    Optional<User> login(UserLoginDTO userLoginDTO);

    Boolean createUser(String id);

    FindUserDTO findUser(String username);

    Boolean deleteUser(String id);
}
