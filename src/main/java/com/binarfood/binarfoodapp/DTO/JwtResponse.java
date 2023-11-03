package com.binarfood.binarfoodapp.DTO;

import com.binarfood.binarfoodapp.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String userCode;

    private String username;

    private String token;

    public JwtResponse(String s) {
    }
}
