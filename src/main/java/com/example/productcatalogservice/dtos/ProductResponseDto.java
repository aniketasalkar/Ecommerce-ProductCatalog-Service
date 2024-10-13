package com.example.productcatalogservice.dtos;

import com.example.productcatalogservice.models.ProductSpecification;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private CategoryResponseDto category;
    private ProductSpecification productSpecification;
}
