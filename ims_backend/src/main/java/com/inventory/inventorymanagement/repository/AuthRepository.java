package com.electrowaveselectronics.inventorymanagement.repository;

import com.electrowaveselectronics.inventorymanagement.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthRepository extends JpaRepository<Auth, Integer> {

    Auth findByUsername(String username);

    @Query("SELECT a.username from Auth a where a.cookie = ?1")
    String findByToken(String token);

}
