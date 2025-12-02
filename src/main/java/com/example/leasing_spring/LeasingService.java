package com.example.leasing_spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeasingService {
    @Autowired
    private LeasingRepository repo;

    public List<LeasingEntity> listAll(String keyword) {
        if (keyword != null) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    public void save(LeasingEntity leasing) {
        repo.save(leasing);
    }

    public LeasingEntity get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
