package com.demo.foodbasket.contracts.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String username;
    private String role;
    private String status;
}
