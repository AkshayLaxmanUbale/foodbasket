package com.demo.foodbasket.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.foodbasket.entities.UserEntity;

@Repository
public interface UserRespository extends JpaRepository<UserEntity, Integer> {
    @Cacheable(value = "users")
    UserEntity findByUsername(String username);
}
