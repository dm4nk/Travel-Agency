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

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns all existing users
     *
     * @return list of users
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Returns user by special id
     *
     * @param id
     * @return user by special id
     */
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Method to create or update user or its characteristics
     *
     * @param user
     * @return user that was created or changed
     */
    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Method to remove user from database
     * Before the user is deleted, we removed links to it from roles table
     *
     * @param user
     */
    @Override
    @Transactional
    public void delete(User user) {
        Role role = user.getRole();
        role.getUsers().remove(user);

        userRepository.delete(user);
    }
}
