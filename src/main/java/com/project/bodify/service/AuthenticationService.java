package com.project.bodify.service;

import com.project.bodify.dto.JwtAuthenticationResponse;
import com.project.bodify.dto.SignInRequest;
import com.project.bodify.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);
    JwtAuthenticationResponse signin(SignInRequest request);
}
