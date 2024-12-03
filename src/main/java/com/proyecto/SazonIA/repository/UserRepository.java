
package com.proyecto.SazonIA.repository;

import org.springframework.stereotype.Repository;

import com.proyecto.SazonIA.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
