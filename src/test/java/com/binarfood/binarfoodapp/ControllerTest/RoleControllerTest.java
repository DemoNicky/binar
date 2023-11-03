package com.binarfood.binarfoodapp.ControllerTest;

import com.binarfood.binarfoodapp.Controller.RoleController;
import com.binarfood.binarfoodapp.Entity.Role;
import com.binarfood.binarfoodapp.Service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RoleControllerTest {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRole_Success() {
        Mockito.when(roleService.saveRole(Mockito.anyString()))
                .thenReturn("Role created successfully");

        ResponseEntity<String> response = roleController.createRole("ROLE_USER");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Role created successfully", response.getBody());
    }

    @Test
    public void testCreateRole_DuplicateRole() {
        Mockito.when(roleService.saveRole(Mockito.anyString()))
                .thenReturn("Role 'ROLE_USER' already exists");

        ResponseEntity<String> response = roleController.createRole("ROLE_USER");

        assertNotNull(response);
        assertEquals("Role 'ROLE_USER' already exists", response.getBody());
    }

    @Test
    public void testFindAllRoles_Success() {
        List<Role> roles = Arrays.asList(new Role("ROLE_USER"), new Role("ROLE_ADMIN"));
        Mockito.when(roleService.findAllRole()).thenReturn(roles);

        ResponseEntity<List<Role>> response = roleController.findAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Role> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    public void testFindAllRoles_EmptyList() {
        Mockito.when(roleService.findAllRole()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Role>> response = roleController.findAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Role> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

}
