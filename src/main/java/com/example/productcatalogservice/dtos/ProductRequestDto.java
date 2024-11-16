package com.example.productcatalogservice.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDto {
    @NotBlank(message = "Name Required")
    private String name;

    @NotBlank(message = "Description Required")
    private String description;

    @NonNull
    @Min(value = 0, message = "Price of the product should be positive")
    private double price;

    private String imageUrl;

    @NonNull
    private CategoryRequestDto category;
//    private ProductSpecification productSpecification;
}
