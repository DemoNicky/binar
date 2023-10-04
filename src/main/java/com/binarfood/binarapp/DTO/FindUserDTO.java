package com.binarfood.binarapp.DTO;

import com.binarfood.binarapp.Entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Builder
public class FindUserDTO {

    private String username;

    private String email;

    private String role;
}
