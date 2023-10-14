package com.binarfood.binarfoodapp.Controller;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserRegisterResponseDTO>registerUser(@RequestBody UserRegisterRequsetDTO requset){
        UserRegisterResponseDTO response = userService.register(requset);
        if (response.getError()==null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<List<UserResponseDTO>>>getUserByUsername(@RequestParam String name){
        ResponseHandling<List<UserResponseDTO>> response = userService.getUser(name);
        if (response.getData() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(
            path = "/{usercode}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseHandling<UserResponseDTO>>updateUser(@PathVariable("usercode")String code, @RequestBody UserRequestUpdateDTO request){
        ResponseHandling<UserResponseDTO> response = userService.updateUser(code, request);
        if (response.getData()==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(
            path = "/{usercode}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserResponseDeleteDTO>deleteData(@PathVariable("usercode")String code){
        UserResponseDeleteDTO response = userService.deleteData(code);
        if (response.getError()==null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
