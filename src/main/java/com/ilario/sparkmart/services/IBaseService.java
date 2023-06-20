package com.ilario.sparkmart.services;

import org.springframework.data.domain.Page;

public interface IBaseService<T, ID> {
    T getById(ID id);
    void saveToDB(T entity);
    Page<T> getAll(int page, int pageSize, String sortBy, String sortDir, String keyword);
    void update(ID id, T entity);
    void delete(ID id);
}
