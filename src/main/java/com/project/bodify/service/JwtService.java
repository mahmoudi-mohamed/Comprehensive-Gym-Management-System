// JwtService.java
package com.project.bodify.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUserName(String token);
    
    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}
