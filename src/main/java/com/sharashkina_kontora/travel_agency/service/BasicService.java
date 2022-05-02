package com.sharashkina_kontora.travel_agency.service;

import java.util.List;
import java.util.Optional;

public interface BasicService<T> {
    List<T> findAll();

    Optional<T> findById(Long id);

    T save(T t) throws Exception;

    void delete(T t) throws Exception;
}
