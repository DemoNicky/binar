package com.binarfood.binarapp.DTO;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserLoginDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

}
