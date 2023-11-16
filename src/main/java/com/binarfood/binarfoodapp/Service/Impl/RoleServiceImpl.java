package com.binarfood.binarfoodapp.Service.Impl;

import com.binarfood.binarfoodapp.Entity.Role;
import com.binarfood.binarfoodapp.Repository.RoleRepository;
import com.binarfood.binarfoodapp.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public String saveRole(String roleName) {

        Role role = new Role();
        role.setRoleName(roleName);
        roleRepository.save(role);
        return "Berhasil";
    }

    @Override
    public List<Role> findAllRole() {
        return roleRepository.findAll();
    }
}
