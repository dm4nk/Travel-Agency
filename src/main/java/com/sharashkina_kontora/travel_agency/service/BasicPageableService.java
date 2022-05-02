package com.sharashkina_kontora.travel_agency.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasicPageableService<T> extends BasicService<T> {
    Page<T> findAll(Pageable pageable);
}
