package com.example.productcatalogservice.dtos;

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
    private double price;

    private String imageUrl;

    @NonNull
    private CategoryRequestDto category;
//    private ProductSpecification productSpecification;
}
