package com.demo.foodbasket.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.foodbasket.services.JwtService;
import com.demo.foodbasket.services.JwtUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        try {
            String accessToken = jwtService.resolveToken(request);
            if(accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("token : " + accessToken);
            Claims claims = jwtService.getAllClaims(accessToken);

            if (claims != null & jwtService.validateToken(accessToken)) {
                String username = claims.getSubject();
                System.out.println("username : " + username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, "",
                        new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception ex) {
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details", ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            objectMapper.writeValue(response.getWriter(), errorDetails);
        }
        filterChain.doFilter(request, response);
    }

}
