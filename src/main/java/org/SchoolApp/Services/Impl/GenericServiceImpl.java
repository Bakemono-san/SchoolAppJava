package org.SchoolApp.Services.Impl;


import org.SchoolApp.Datas.Repository.SoftDeleteRepository;
import org.SchoolApp.Services.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

    protected SoftDeleteRepository<T, ID> repository;

    public GenericServiceImpl(SoftDeleteRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }



    @Override
    @Transactional
    public void delete(ID id) {
        repository.softDelete((Long) id);
    }
}
