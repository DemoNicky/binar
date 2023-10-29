package com.binarfood.binarfoodapp.Service;

import com.binarfood.binarfoodapp.DTO.*;

import java.util.List;

public interface UserService {

    UserRegisterResponseDTO register(UserRegisterRequsetDTO requset);

    ResponseHandling<List<UserResponseDTO>> getUser(String name);

    ResponseHandling<UserResponseDTO> updateUser(String code, UserRequestUpdateDTO request);

    UserResponseDeleteDTO deleteData(String code);

    ResponseHandling<JwtResponse> createJwtToken(JwtRequest jwtRequest) throws Exception;
}
