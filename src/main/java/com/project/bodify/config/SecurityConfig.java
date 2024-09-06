package com.project.bodify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.bodify.security.JwtAuthenticationFilter;
import com.project.bodify.service.impl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserServiceImpl userServiceImpl;
  private final PasswordEncoder passwordEncoder;

  @Bean
  public AuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(userServiceImpl.userDetailsService());
      authProvider.setPasswordEncoder(passwordEncoder);
      return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
      return config.getAuthenticationManager();
  }
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    HttpSecurity filterBefore = http
    .csrf(csrf -> csrf 
      .disable()
    )
    .sessionManagement(session -> session
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    )
    .authorizeHttpRequests(authorize -> authorize
      .antMatchers(HttpMethod.POST, "/api/auth/signup", "/api/auth/signin","/api/file/**").permitAll()
      .antMatchers(HttpMethod.GET, "/api/file/**","/api/defauts/**","/api/tvjourproduction/**").permitAll()
    /*  .antMatchers("/admin/**").hasRole("ADMIN")
      .antMatchers("/admin/**").hasRole("USER")
      .antMatchers("/admin/**").hasRole("TECHNICIEN")
      .anyRequest().authenticated().and()*/
    )
    .authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
