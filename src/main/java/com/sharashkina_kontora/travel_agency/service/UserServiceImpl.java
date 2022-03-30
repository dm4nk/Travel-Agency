package com.sharashkina_kontora.travel_agency.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserService> findAll() {
        return null;
    }

    @Override
    public Optional<UserService> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public UserService save(UserService userService) {
        return null;
    }

    @Override
    public void delete(UserService userService) {

    }
}
