package com.binarfood.binarfoodapp.Controller;

import com.binarfood.binarfoodapp.Entity.Role;
import com.binarfood.binarfoodapp.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String>createRole(@RequestParam String roleName){
        String response = roleService.saveRole(roleName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Role>> findAll(){
        List<Role> roles = roleService.findAllRole();
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

}
