package com.demo.foodbasket.contracts.request;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "firstname is required.")
    private String firstname;

    @NotBlank(message = "lastname is required.")
    private String lastname;

    @NotNull(message = "dateofbirth is required.")
    private Date dateOfBirth;

    @NotBlank(message = "username is required.")
    private String username;

    @NotBlank(message = "password is required.")
    private String password;
}
