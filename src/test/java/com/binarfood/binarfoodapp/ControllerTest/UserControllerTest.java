package com.binarfood.binarfoodapp.ControllerTest;

import com.binarfood.binarfoodapp.Controller.UserController;
import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.User;
import com.binarfood.binarfoodapp.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserControllerTest {


    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_login_success() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("username", "password");
        JwtResponse jwtResponse = new JwtResponse("jwtToken");
        ResponseHandling<JwtResponse> response = new ResponseHandling<>();
        response.setData(jwtResponse);
        when(userService.createJwtToken(jwtRequest)).thenReturn(response);

        ResponseEntity<ResponseHandling<JwtResponse>> result = userController.login(jwtRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(jwtResponse, result.getBody().getData());
    }

    @Test
    public void test_login_failed() throws Exception {
        JwtRequest jwtRequest = new JwtRequest("username", "password");
        ResponseHandling<JwtResponse> response = new ResponseHandling<>();
        when(userService.createJwtToken(jwtRequest)).thenReturn(response);

        ResponseEntity<ResponseHandling<JwtResponse>> result = userController.login(jwtRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertNull(result.getBody().getData());
    }

    @Test
    public void testRegisterUserSuccess() {
        UserRegisterRequsetDTO request = new UserRegisterRequsetDTO();
        request.setUsername("username");
        request.setEmail("email");
        request.setPassword("password");

        UserRegisterResponseDTO response = new UserRegisterResponseDTO();
        response.setMessage("Success to register");

        when(userService.register(request)).thenReturn(response);

        ResponseEntity<UserRegisterResponseDTO> result = userController.registerUser(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Success to register", result.getBody().getMessage());
    }

    @Test
    public void testRegisterUserFailure() {
        UserRegisterRequsetDTO request = new UserRegisterRequsetDTO();
        request.setUsername("username");
        request.setEmail("email");
        request.setPassword("password");

        UserRegisterResponseDTO response = new UserRegisterResponseDTO();
        response.setError("Email is already exists");

        when(userService.register(request)).thenReturn(response);

        ResponseEntity<UserRegisterResponseDTO> result = userController.registerUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Email is already exists", result.getBody().getError());
    }

    @Test
    void testGetUserByUsername_PositiveCase() {
        String username = "john.doe";
        List<UserResponseDTO> userResponseDTOList = new ArrayList<>();
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername("John Doe");
        userResponseDTO.setEmail("john@example.com");
        userResponseDTO.setRole("USER");
        userResponseDTOList.add(userResponseDTO);
        when(userService.getUser(username)).thenReturn(
                new ResponseHandling<>(userResponseDTOList, "Success", null)
        );

        ResponseEntity<ResponseHandling<List<UserResponseDTO>>> response = userController.getUserByUsername(username);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseHandling<List<UserResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Success", responseBody.getMessage());
        assertEquals(userResponseDTOList, responseBody.getData());
    }

    @Test
    void testGetUserByUsername_NegativeCase() {
        String username = "nonexistentuser";
        when(userService.getUser(username)).thenReturn(
                new ResponseHandling<>(null, "User not found", "User with username nonexistentuser not found")
        );

        ResponseEntity<ResponseHandling<List<UserResponseDTO>>> response = userController.getUserByUsername(username);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseHandling<List<UserResponseDTO>> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("User not found", responseBody.getMessage());
        assertEquals("User with username nonexistentuser not found", responseBody.getErrors());
    }

    @Test
    public void testUpdateUser_Success() {
        UserRequestUpdateDTO request = new UserRequestUpdateDTO();
        request.setUsername("newUsername");
        request.setEmail("newEmail");
        request.setPassword("newPassword");

        UserResponseDTO userResponse = new UserResponseDTO();
        userResponse.setUsername(request.getUsername());
        userResponse.setEmail(request.getEmail());
        userResponse.setRole("USER");

        Mockito.when(userService.updateUser(Mockito.anyString(), Mockito.any(UserRequestUpdateDTO.class)))
                .thenReturn(new ResponseHandling<>(userResponse, "Success message", null));

        ResponseEntity<ResponseHandling<UserResponseDTO>> response = userController.updateUser("usercode123", request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseHandling<UserResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Success message", responseBody.getMessage());
        assertNotNull(responseBody.getData());
        assertEquals("newUsername", responseBody.getData().getUsername());
        assertEquals("newEmail", responseBody.getData().getEmail());
        assertEquals("USER", responseBody.getData().getRole());
    }

    @Test
    public void testUpdateUser_NotFound() {
        UserRequestUpdateDTO request = new UserRequestUpdateDTO();
        request.setUsername("newUsername");
        request.setEmail("newEmail");
        request.setPassword("newPassword");

        Mockito.when(userService.updateUser(Mockito.anyString(), Mockito.any(UserRequestUpdateDTO.class)))
                .thenReturn(new ResponseHandling<>(null, null, "User not found"));

        ResponseEntity<ResponseHandling<UserResponseDTO>> response = userController.updateUser("nonExistentUser", request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ResponseHandling<UserResponseDTO> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("User not found", responseBody.getErrors());
    }

    @Test
    public void testDeleteData_Success() {
        UserResponseDeleteDTO userResponseDeleteDTO = new UserResponseDeleteDTO();
        userResponseDeleteDTO.setMessage("Success message");
        userResponseDeleteDTO.setError(null);
        Mockito.when(userService.deleteData(Mockito.anyString()))
                .thenReturn(userResponseDeleteDTO);

        ResponseEntity<UserResponseDeleteDTO> response = userController.deleteData("usercode123");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserResponseDeleteDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Success message", responseBody.getMessage());
    }

    @Test
    public void testDeleteData_NotFound() {
        UserResponseDeleteDTO userResponseDeleteDTO = new UserResponseDeleteDTO();
        userResponseDeleteDTO.setMessage(null);
        userResponseDeleteDTO.setError("User not found");

        Mockito.when(userService.deleteData(Mockito.anyString()))
                .thenReturn(userResponseDeleteDTO);

        ResponseEntity<UserResponseDeleteDTO> response = userController.deleteData("nonExistentUser");

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        UserResponseDeleteDTO responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("User not found", responseBody.getError());
    }

}

