package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.Role;
import com.sharashkina_kontora.travel_agency.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Returns all existing roles
     * @return list of roles
     */
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    /**
     * Returns role by special id
     * @param id
     * @return role by special id
     */
    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * Method to create or update role or its characteristics
     * @param role
     * @return role that was created or changed
     */
    @Override
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    /**
     * Method to remove role from database
     * If at least one of users has such role, it cannot be removed
     * @param role
     */
    @Override
    @Transactional
    public void delete(Role role) {
        roleRepository.delete(role);
    }
}
