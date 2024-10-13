package com.example.productcatalogservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryPatchRequestDto {
    @NotBlank(message = "Category name should not be empty.")
    private String name;

    @NotBlank(message = "Description name should not be empty")
    @Size(min = 10, max = 250, message = "Minimum length should be 10")
    private String description;
}
