package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Role;
import com.sharashkina_kontora.travel_agency.domain.User;
import com.sharashkina_kontora.travel_agency.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(User user) {
        Role role = user.getRole();
        role.getUsers().remove(user);
        roleService.save(role);

        userRepository.delete(user);
    }
}
