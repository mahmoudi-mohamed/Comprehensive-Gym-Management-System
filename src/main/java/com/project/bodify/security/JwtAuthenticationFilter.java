package com.project.bodify.security;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.bodify.service.JwtService;
import com.project.bodify.service.impl.UserServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  private final JwtService jwtServiceImpl;
  private final UserServiceImpl userServiceImpl;

  @Override
  protected void doFilterInternal(javax.servlet.http.HttpServletRequest request,
        javax.servlet.http.HttpServletResponse response, 
        javax.servlet.FilterChain filterChain)
        throws javax.servlet.ServletException, IOException {
      final String authHeader = request.getHeader("Authorization");
      final String jwt;
      final String userEmail;
      if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
          filterChain.doFilter(request, response);
          return;
      }
      jwt = authHeader.substring(7);
      log.debug("JWT - {}", jwt.toString());
      
      try {
          userEmail = jwtServiceImpl.extractUserName(jwt);
          if (StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
              UserDetails userDetails = userServiceImpl.userDetailsService().loadUserByUsername(userEmail);
              if (jwtServiceImpl.isTokenValid(jwt, userDetails)) {
                log.debug("User - {}", userDetails);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
              }
          }
      } catch (ExpiredJwtException e) {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT expired");
          return;
      }
      
      filterChain.doFilter(request, response);
  }
}
