package com.binarfood.binarfoodapp.ServiceTest;

import com.binarfood.binarfoodapp.Entity.Role;
import com.binarfood.binarfoodapp.Repository.RoleRepository;
import com.binarfood.binarfoodapp.Service.Impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@SpringBootTest
public class RoleTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveRolePositive() {
        Role role = new Role();
        role.setRoleName("Admin");

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        String result = roleService.saveRole("Admin");
        assertEquals("Berhasil", result);
    }

    @Test
    public void findAllRole_Success() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ADMIN"));
        roles.add(new Role("USER"));

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.findAllRole();
        assertEquals(2, result.size());
    }

    @Test
    public void findAllRole_Empty() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Role> result = roleService.findAllRole();
        assertEquals(0, result.size());
    }

}
