package com.example.leasing_spring;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeasingRepository extends JpaRepository<LeasingEntity, Long> {
    @Query("SELECT p FROM LeasingEntity p WHERE CONCAT(p.firstName, ' ', p.lastName, ' ', p.number, ' ', p.score) LIKE %:keyword%")
    List<LeasingEntity> search(@Param("keyword") String keyword);
}
