package com.example.application.views.main.data;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

public interface SupportProfileRepository extends CrudRepository<SupportProfile, Long>{
    public Set<SupportProfile> findAll();
}
