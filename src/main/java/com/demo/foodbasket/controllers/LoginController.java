package com.demo.foodbasket.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.foodbasket.contracts.request.AuthRequest;
import com.demo.foodbasket.contracts.request.SignUpRequest;
import com.demo.foodbasket.contracts.response.AuthResponse;
import com.demo.foodbasket.contracts.response.UserResponse;
import com.demo.foodbasket.models.AccountStatusDto;
import com.demo.foodbasket.models.UserDto;
import com.demo.foodbasket.services.JwtService;
import com.demo.foodbasket.services.JwtUserDetailsService;
import com.demo.foodbasket.services.UserService;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUserDetailsService userDetailsService;

    @Autowired 
    UserService userService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired 
    JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) throws Exception {
        
        authenticate(authRequest);

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(AuthResponse.builder().username(userDetails.getUsername()).token(token).build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {       
        UserDto userDetails = modelMapper.map(signUpRequest, UserDto.class); 
        UserDto userDto = userService.createUser(userDetails);
        return ResponseEntity.ok(modelMapper.map(userDto, UserResponse.class));
    }
    
    @GetMapping("/user/verify/{username}")
    public String verifyUsername(@PathVariable String username) {
        UserDto existingUser = userService.getUserByUsername(username);
        UserDto userDetailsToUpdate = UserDto.builder()
                .status(AccountStatusDto.UNDERREVIEW)
                .build();

        userService.updateUser(existingUser.getId(), userDetailsToUpdate);
        return "Email Verification Successfull!";
    }
    

    private void authenticate(AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (DisabledException ex) {
            throw new Exception("User Disabled!!", ex);
        } catch (BadCredentialsException ex) {
            throw new Exception("Bad Credentials!!", ex);
        }
    }
}
