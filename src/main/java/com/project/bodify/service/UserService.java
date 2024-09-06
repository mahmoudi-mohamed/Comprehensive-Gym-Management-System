package com.project.bodify.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.project.bodify.model.User;

public interface UserService extends UserDetailsService {
    User save(User newUser);
}
