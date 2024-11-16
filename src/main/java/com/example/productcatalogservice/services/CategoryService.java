package com.example.productcatalogservice.services;

import com.example.productcatalogservice.exceptions.AlreadyExistsException;
import com.example.productcatalogservice.exceptions.EmptyDataException;
import com.example.productcatalogservice.exceptions.NotFoundException;
import com.example.productcatalogservice.exceptions.UnauthorizedException;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Status;
import com.example.productcatalogservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new AlreadyExistsException("Category with name " + category.getName() + " already exists");
        }

        Date currentDate = new Date();
        category.setCreatedAt(currentDate);
        category.setUpdatedAt(currentDate);
        category.setStatus(Status.ACTIVE);

        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(String categoryName, Category category) {
        if (!categoryRepository.findByName(categoryName).isPresent()) {
            throw new NotFoundException("Category with name " + categoryName + " does not exist");
        }
//        if (categoryName != category.getName()) {
//            if (categoryRepository.findByName(categoryName).isPresent()) {
//                throw new AlreadyExistsException("Category with name " + category.getName() + " already exists");
//            }
//        }

        Field[] fields = category.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
//                if (field.getName().equals("name")) {
//                    if (categoryName == category.getName()) {
//                        continue;
//                    }
//                }
                Object value = field.get(category);
                Field modifiersField = field.getClass().getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.set(category, value);
            } catch (NoSuchFieldException e) {
                throw new NotFoundException("Field not found");
            } catch (IllegalAccessException exception) {
                throw new UnauthorizedException("Field not accessible");
            }
        }
        Date currentDate = new Date();
        category.setUpdatedAt(currentDate);

        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryByName(String name) {

        return categoryRepository.findByName(name).orElseThrow(() -> new NotFoundException("Category with name " + name + " not found"));
    }

    @Override
    public List<Category> bulkAddCategories(List<Category> categories) {
        if (categories.isEmpty()) {
            throw new EmptyDataException("Category list is empty");
        }
        List<Category> createdCategories = new ArrayList<>();
        for (Category category : categories) {
            createdCategories.add(createCategory(category));
        }

        return createdCategories;
    }
}
