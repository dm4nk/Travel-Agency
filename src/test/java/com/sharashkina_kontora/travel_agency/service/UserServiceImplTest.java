package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Role;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    RoleService roleService;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    User user;
    Role role;

    @BeforeEach
    void setUp() {
        role = Role.builder()
                .id(2L)
                .name("admin")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("Dima")
                .lastName("Prokopovich")
                .email("dim.xx2011@yandex.ru")
                .phoneNumber("89277583192")
                .birthday(LocalDate.now())
                .password("123456")
                .role(role)
                .build();
    }

    @Test
    void findAll() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertArrayEquals(result.toArray(), users.toArray());
    }

    @Test
    void findById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L).orElse(null);

        assertEquals(result, user);
    }

    @Test
    void save() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertEquals(result, user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void delete() {
        userService.delete(user);

        verify(userRepository, times(1)).delete(user);
    }
}