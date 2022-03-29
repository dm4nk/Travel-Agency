package com.sharashkina_kontora.travel_agency.service;

import java.util.List;
import java.util.Optional;

public interface BasicService<T> {
    List<T> findAll();

    Optional<T> findById(Integer id);

    T save(T t);

    void delete(T t);
}
