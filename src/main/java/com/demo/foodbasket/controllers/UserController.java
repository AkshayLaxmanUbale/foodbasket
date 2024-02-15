package com.demo.foodbasket.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.foodbasket.contracts.response.UserResponse;
import com.demo.foodbasket.models.AccountStatusDto;
import com.demo.foodbasket.models.UserDto;
import com.demo.foodbasket.services.UserService;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @PutMapping("/{id}/updatestatus/{accountStatus}")
    public ResponseEntity<UserResponse> changeStatus(@PathVariable int id, @PathVariable String accountStatus) {
        UserDto userDetailsToUpdate = UserDto.builder()
                .status(mapper.map(accountStatus, AccountStatusDto.class))
                .build();

        UserDto updatedUser = userService.updateUser(id, userDetailsToUpdate);
        return ResponseEntity.ok(mapper.map(updatedUser, UserResponse.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Integer id) {
        UserDto user = userService.getUserById(id);
        if (user == null) {
            throw new EntityNotFoundException("User not found.");
        }
        return ResponseEntity.ok(mapper.map(user, UserResponse.class));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<UserDto> userDtos = userService.getAllUsers();
        return ResponseEntity.ok(userDtos.stream().map(x -> mapper.map(x, UserResponse.class)).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        userService.deleteUserById(id);

        return ResponseEntity.ok("User Deleted Successfully.");
    }

}
