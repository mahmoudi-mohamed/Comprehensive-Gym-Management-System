package com.project.bodify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.project.bodify.model.Role;
import com.project.bodify.model.User;

import java.util.List;
import java.util.Optional;
@Repository

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
 boolean existsByEmail(String email);
 List<User> findByRole(Role role);
 List<String> findTokenFcmByRole(Role role);
 Optional<User> findByPasswordResetToken(String token); // Add this method
 
}