package com.demo.foodbasket.contracts.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotBlank(message = "username is mandatory.")
    private String username;

    @NotBlank(message = "password is mandatory.")
    private String password;
}
