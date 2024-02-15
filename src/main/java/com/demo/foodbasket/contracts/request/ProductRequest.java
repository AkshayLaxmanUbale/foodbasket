package com.demo.foodbasket.contracts.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "ProductName is required.")
    private String productName;

    private String description;

    @NotBlank(message = "availableUnits is required.")
    private int availableUnits;

    @NotBlank(message = "PricePerUnit is required.")
    private double pricePerUnit;

    @NotBlank(message = "UserId is required.")
    private int userId;
}
