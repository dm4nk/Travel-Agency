package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Role;
import com.sharashkina_kontora.travel_agency.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleServiceImpl roleService;

    Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(1L)
                .name("admin")
                .build();
    }

    @Test
    void findById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role result = roleService.findById(1L).orElse(null);

        assertEquals(result, role);
    }

    @Test
    void save() {
        when(roleRepository.save(role)).thenReturn(role);

        Role result = roleService.save(role);

        assertEquals(result, role);
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void delete() {
        roleService.delete(role);

        verify(roleRepository, times(1)).delete(role);
    }
}