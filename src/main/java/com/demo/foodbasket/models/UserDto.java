package com.demo.foodbasket.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String username;
    private String password;
    private RoleDto role;
    private AccountStatusDto status;
}
