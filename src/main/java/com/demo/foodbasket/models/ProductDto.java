package com.demo.foodbasket.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private String ProductId;

    private String productName;

    private String description;

    private int availableUnits;

    private double pricePerUnit;

    private int userId;
}
