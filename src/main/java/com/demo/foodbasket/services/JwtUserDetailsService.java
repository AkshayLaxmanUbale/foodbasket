package com.demo.foodbasket.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.foodbasket.models.UserDto;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userService.getUserByUsername(username);
        List<String> roles = new ArrayList<>();
        roles.add("USER");

        if (user != null) {
            return User.builder()
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .roles(roles.toArray(new String[0]))
                    .build();
        }

        throw new UsernameNotFoundException("Invalid User Details!!");
    }

}
