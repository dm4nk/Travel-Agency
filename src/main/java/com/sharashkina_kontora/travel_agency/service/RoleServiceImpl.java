package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Role;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public List<Role> findAll() {
        return null;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Role save(Role role) {
        return null;
    }

    @Override
    public void delete(Role role) {

    }
}
