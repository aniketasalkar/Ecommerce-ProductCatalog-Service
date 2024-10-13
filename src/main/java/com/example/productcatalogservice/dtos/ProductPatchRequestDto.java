package com.example.productcatalogservice.dtos;

import com.example.productcatalogservice.models.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductPatchRequestDto {
    @NotBlank(message = "Name Required")
    private String name;

    @NotEmpty(message = "Description Required")
    private String description;

    @NotEmpty(message = "Price Required")
    private double price;

    private String imageUrl;

    @NotEmpty(message = "Category Required")
    private Category category;
}
