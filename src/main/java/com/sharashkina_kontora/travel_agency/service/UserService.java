package com.sharashkina_kontora.travel_agency.service;

import com.sharashkina_kontora.travel_agency.domain.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends BasicService<User>, UserDetailsService {
}
