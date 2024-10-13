package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.CategoryRequestDto;
import com.example.productcatalogservice.dtos.CategoryResponseDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productCategoryService")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/category")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        try {
            Category category = categoryService.createCategory(from(categoryRequestDto));

            return new ResponseEntity<>(from(category), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        try {
            List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();
            List<Category> allCategories = categoryService.getAllCategories();
            for (Category category : allCategories) {
                categoryResponseDtos.add(from(category));
            }

            return new ResponseEntity<>(categoryResponseDtos, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @GetMapping("/category/{name}")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);

            return new ResponseEntity<>(from(category), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PatchMapping("/category/{name}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable String name, @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        try {
            Category category = categoryService.updateCategory(name, from(categoryRequestDto));

            return new ResponseEntity<>(from(category), HttpStatus.OK);
        }  catch (Exception exception) {
            throw exception;
        }
    }

    private Category from(CategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());

        return category;
    }

    private CategoryResponseDto from(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setName(category.getName());
        categoryResponseDto.setDescription(category.getDescription());
        categoryResponseDto.setId(category.getId());

        return categoryResponseDto;
    }
}
