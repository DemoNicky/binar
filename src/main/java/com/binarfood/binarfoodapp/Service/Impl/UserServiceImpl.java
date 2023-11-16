package com.binarfood.binarfoodapp.Service.Impl;

import com.binarfood.binarfoodapp.DTO.*;
import com.binarfood.binarfoodapp.Entity.Role;
import com.binarfood.binarfoodapp.Entity.User;
import com.binarfood.binarfoodapp.Repository.RoleRepository;
import com.binarfood.binarfoodapp.Repository.UserRepository;
import com.binarfood.binarfoodapp.Service.UserService;
import com.binarfood.binarfoodapp.Util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseHandling<JwtResponse> createJwtToken(JwtRequest jwtRequest) throws Exception {
        ResponseHandling<JwtResponse> response = new ResponseHandling<>();
        try {
            String username = jwtRequest.getUsername();
            String password = jwtRequest.getPassword();

            authenticate(username, password);

            final UserDetails userDetails = loadUserByUsername(username);

            String newToken = jwtUtil.generateToken(userDetails);

            User user = userRepository.findByUsername(username).get();

            response.setData(new JwtResponse(user.getUserCode(), user.getUsername(), newToken));
            response.setMessage("Authentication successful");
        }catch (Exception e){
            response.setErrors("Failed to login");
            response.setMessage("Authentication failed: " + e.getMessage());
        }
        return response;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).get();
        if (user != null){
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(),getAuthorities(user)
            );
        }else {
            throw new UsernameNotFoundException("Username is not found");
        }
    }

    private Set getAuthorities(User user){
        Set authorities = new HashSet();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        return authorities;
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (DisabledException e){
            throw new Exception("User is Disable");
        }catch (BadCredentialsException e){
            System.out.println(e);
            throw new Exception("Bad Credential from user");
        }
    }

    @Transactional
    @Override
    public UserRegisterResponseDTO register(UserRegisterRequsetDTO requset) {
        UserRegisterResponseDTO response = new UserRegisterResponseDTO();

        try {
            if (userRepository.existsByEmail(requset.getEmail())) {
                response.setMessage("Fail to register");
                response.setError("Email is already exists");
                return response;
            } else if (userRepository.existsByUsername(requset.getUsername())) {
                response.setMessage("Fail to register");
                response.setError("Username is already exists");
                return response;
            }

            User user = new User();
            user.setUserCode(getKode());
            user.setUsername(requset.getUsername());
            user.setEmail(requset.getEmail());
            String encryptedPassword = getEncryptPass(requset);
            user.setPassword(encryptedPassword);
            Role role = roleRepository.findByRoleName("USER");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRole(roles);
            user.setDeleted(false);
            userRepository.save(user);

            response.setMessage("Success to register");
        } catch (Exception e) {
            log.error("Error during user registration", e);
            response.setMessage("Fail to register");
            response.setError("An error occurred during registration");
        }

        return response;
    }

    private String getEncryptPass(UserRegisterRequsetDTO requset) {
        String pass = passwordEncoder.encode(requset.getPassword());
        return pass;
    }

    @Override
    public ResponseHandling<List<UserResponseDTO>> getUser(String name) {
        Optional<List<User>> users = userRepository.findUser(name);
        ResponseHandling<List<UserResponseDTO>> response = new ResponseHandling<>();
        List<User> users1 = users.get();
        if (!users.isPresent() || users1.isEmpty()){
            response.setMessage("cant get user");
            response.setErrors("user with username "+name+ " not found");
            return response;
        }
        List<UserResponseDTO> userResponse = users1.stream().map((p)->{
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            userResponseDTO.setUsername(p.getUsername());
            userResponseDTO.setEmail(p.getEmail());
            userResponseDTO.setRole(p.getRole().toString());
            return userResponseDTO;
        }).collect(Collectors.toList());
        response.setData(userResponse);
        response.setMessage("success get user");
        return response;
    }

    @Override
    public ResponseHandling<UserResponseDTO> updateUser(String code, UserRequestUpdateDTO request) {
        Optional<User> user = userRepository.findByUserCode(code);
        ResponseHandling<UserResponseDTO> response = new ResponseHandling<>();
        if (!user.isPresent()){
            response.setMessage("cant find user");
            response.setErrors("user with code "+code+" Not found");
            return response;
        }
        User user1 = user.get();
        user1.setUsername(request.getUsername());
        user1.setEmail(request.getEmail());
        user1.setPassword(request.getPassword());
        userRepository.save(user1);

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user1.getUsername());
        userResponseDTO.setEmail(user1.getEmail());
        userResponseDTO.setRole(user1.getRole().toString());
        response.setData(userResponseDTO);
        response.setMessage("success update data");
        return response;
    }

    @Override
    public UserResponseDeleteDTO deleteData(String code) {
        Optional<User> user = userRepository.findByUserCode(code);
        UserResponseDeleteDTO response = new UserResponseDeleteDTO();
        if (!user.isPresent()){
            response.setMessage("fail to delete user");
            response.setError("user data with code " +code+ " not found");
            return response;
        }
        User user1 = user.get();
        user1.setDeleted(true);
        userRepository.save(user1);
        response.setMessage("success to delete user data");
        return response;
    }

    private String getKode() {
        UUID uuid = UUID.randomUUID();
        String kode = uuid.toString().substring(0, 5);
        return kode;
    }



}
