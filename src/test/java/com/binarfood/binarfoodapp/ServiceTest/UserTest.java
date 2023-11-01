package com.binarfood.binarfoodapp.ServiceTest;
import com.binarfood.binarfoodapp.Controller.UserController;
import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.Role;
import com.binarfood.binarfoodapp.Entity.User;
import com.binarfood.binarfoodapp.Repository.RoleRepository;
import com.binarfood.binarfoodapp.Repository.UserRepository;
import com.binarfood.binarfoodapp.Service.Impl.UserServiceImpl;
import com.binarfood.binarfoodapp.Util.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRegisterRequsetDTO request;
    private Role role;
    private User user;

    @BeforeEach
    public void setUp() {
        request = new UserRegisterRequsetDTO();
        request.setUsername("newUser");
        request.setEmail("newuser@example.com");
        request.setPassword("password");

        role = new Role();
        role.setRoleName("USER");

        user = new User();
        user.setUsername(request.getUsername());

        when(roleRepository.findByRoleName("USER")).thenReturn(role);

        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    public void tearDown() {
        request = null;
        role = null;
        user = null;
    }


    /**
     * Register Test
     */

    @Test
    public void testRegister_Positive() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserRegisterResponseDTO response = userService.register(request);

        assertEquals("Success to register", response.getMessage());
        assertNull(response.getError());
    }

    @Test
    public void testRegister_Negative_EmailExists() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        UserRegisterResponseDTO response = userService.register(request);

        assertEquals("Fail to register", response.getMessage());
        assertEquals("Email is already exists", response.getError());
    }

    @Test
    public void testRegister_Negative_UsernameExists() {
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        UserRegisterResponseDTO response = userService.register(request);

        assertEquals("Fail to register", response.getMessage());
        assertEquals("username is already exists", response.getError());
    }

    /**
     * login test
     */

    @Test
    public void testCreateJwtToken_Negative_DisabledUser() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setDeleted(false);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        Mockito.doThrow(new DisabledException("User is Disable")).when(authenticationManager).authenticate(any());

        ResponseHandling<JwtResponse> response = userService.createJwtToken(new JwtRequest("testUser", "testPassword"));

        assertEquals("Failed to login", response.getErrors());
        assertEquals("Authentication failed: User is Disable", response.getMessage());
    }

    @Test
    public void testCreateJwtToken_Negative_BadCredentialsException() throws Exception {
        when(userRepository.findByUsername("testUser")).thenReturn(null);

        Mockito.doThrow(new BadCredentialsException("Bad Credential from user")).when(authenticationManager).authenticate(any());

        ResponseHandling<JwtResponse> response = userService.createJwtToken(new JwtRequest("testUser", "testPassword"));

        assertEquals("Failed to login", response.getErrors());
        assertEquals("Authentication failed: Bad Credential from user", response.getMessage());
    }

    /**
     * get user Test
     */

    @Test
    public void testGetUserPositive() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User("1", "user1", "user1@example.com", "password", false, new HashSet<>()));
        mockUsers.add(new User("2", "user2", "user2@example.com", "password", false, new HashSet<>()));
        when(userRepository.findUser("user1")).thenReturn(Optional.of(mockUsers));
        ResponseHandling<List<UserResponseDTO>> response = userService.getUser("user1");
        assertEquals("success get user", response.getMessage());
        assertTrue(response.getData() != null && !response.getData().isEmpty());
    }

//    @Test
//    public void testGetUserNegative() {
//        when(userRepository.findUser("nonexistent_user")).thenReturn(Optional.empty());
//        ResponseHandling<List<UserResponseDTO>> response = userService.getUser("nonexistent_user");
//        assertEquals("cant get user", response.getMessage());
//        assertTrue(response.getErrors() != null && !response.getErrors().isEmpty());
//    }

    @Test
    public void testUpdateUserPositive() {
        User mockUser = new User("1", "user1", "user1@example.com", "password", false, new HashSet<>());
        when(userRepository.findByUserCode("user1")).thenReturn(Optional.of(mockUser));

        UserRequestUpdateDTO updateDTO = new UserRequestUpdateDTO();
        updateDTO.setUsername("newUsername");
        updateDTO.setEmail("newEmail");
        updateDTO.setPassword("newPassword");

        ResponseHandling<UserResponseDTO> response = userService.updateUser("user1", updateDTO);

        assertEquals("success update data", response.getMessage());
        UserResponseDTO updatedUser = response.getData();
        assertNotNull(updatedUser);
        assertEquals("newUsername", updatedUser.getUsername());
        assertEquals("newEmail", updatedUser.getEmail());
    }

    @Test
    public void testUpdateUserNegative() {
        when(userRepository.findByUserCode("nonexistent_user")).thenReturn(Optional.empty());

        UserRequestUpdateDTO updateDTO = new UserRequestUpdateDTO();
        updateDTO.setUsername("newUsername");
        updateDTO.setEmail("newEmail");
        updateDTO.setPassword("newPassword");
        ResponseHandling<UserResponseDTO> response = userService.updateUser("nonexistent_user", updateDTO);
        assertEquals("cant find user", response.getMessage());
        assertTrue(response.getErrors() != null && !response.getErrors().isEmpty());
    }

    @Test
    public void testDeleteDataPositive() {
        User mockUser = new User("1", "user1", "user1@example.com", "password", false, new HashSet<>());
        when(userRepository.findByUserCode("user1")).thenReturn(Optional.of(mockUser));
        UserResponseDeleteDTO response = userService.deleteData("user1");

        assertEquals("success to delete user data", response.getMessage());
    }

    @Test
    public void testDeleteDataNegative() {
        when(userRepository.findByUserCode("nonexistent_user")).thenReturn(Optional.empty());
        UserResponseDeleteDTO response = userService.deleteData("nonexistent_user");
        assertEquals("fail to delete user", response.getMessage());
        assertNotNull(response.getError());
    }

}
