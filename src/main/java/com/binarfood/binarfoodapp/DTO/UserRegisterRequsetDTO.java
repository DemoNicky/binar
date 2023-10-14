package com.binarfood.binarfoodapp.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserRegisterRequsetDTO {

    @NotBlank(message = "username cant be null!!!")
    private String username;

    @NotBlank(message = "email cant be null!!!")
    private String email;

    @NotBlank(message = "password cant be null!!!")
    private String password;

}
