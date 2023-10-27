package com.binarfood.binarfoodapp.Service;

import com.binarfood.binarfoodapp.Entity.Role;

import java.util.List;

public interface RoleService {
    String saveRole(String roleName);

    List<Role> findAllRole();
}
