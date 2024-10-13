package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(Category category);
    Category updateCategory(String categoryName, Category category);
    List<Category> getAllCategories();
    Category getCategoryByName(String name);
}
