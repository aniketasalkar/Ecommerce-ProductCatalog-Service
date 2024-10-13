package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.CategoryRequestDto;
import com.example.productcatalogservice.dtos.ProductPatchRequestDto;
import com.example.productcatalogservice.dtos.ProductRequestDto;
import com.example.productcatalogservice.exceptions.AlreadyExistsException;
import com.example.productcatalogservice.exceptions.NotFoundException;
import com.example.productcatalogservice.exceptions.UnauthorizedException;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.ProductSpecification;
import com.example.productcatalogservice.models.Status;
import com.example.productcatalogservice.repository.CategoryRepository;
import com.example.productcatalogservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

@Service
public class ProductService implements IProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Product createProduct(ProductRequestDto productRequestDto) {

        Product product = from(productRequestDto);

        if (productRepository.findByName(product.getName()).isPresent()) {
            throw new AlreadyExistsException("Product already exists");
        }

        if (categoryRepository.findByName(product.getCategory().getName()).isPresent()) {
            product.setCategory(categoryRepository.findByName(product.getCategory().getName()).get());
        } else {
            throw new NotFoundException("Category with the name " + product.getCategory().getName() + " not found");
        }

        Date currentTime = new Date();

        product.setProductSpecification(ProductSpecification.ALL);
        product.setCreatedAt(currentTime);
        product.setUpdatedAt(currentTime);
        product.setStatus(Status.ACTIVE);

        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Product not found"));

        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products;
    }

    @Override
    public Product updateProduct(Long id, ProductPatchRequestDto productPatchRequestDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));

        if (categoryRepository.findByName(product.getCategory().getName()).isPresent()) {
            product.setCategory(categoryRepository.findByName(product.getCategory().getName()).get());
        } else {
            throw new NotFoundException("Category with the name " + product.getCategory().getName() + " not found");
        }

        Field[] fields = productPatchRequestDto.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(productPatchRequestDto) == null) {
                    continue;
                }
                if (field.get(productPatchRequestDto) instanceof Category) {
                   continue;
                }
                Object value = field.get(productPatchRequestDto);
                Field productField = Product.class.getDeclaredField(field.getName());
                productField.setAccessible(true);
                productField.set(product, value);

            } catch (NoSuchFieldException e) {
                throw new NotFoundException("Field not found");
            } catch (IllegalAccessException exception) {
                throw new UnauthorizedException("Field not accessible");
            }
        }

        Date currentTime = new Date();
        product.setUpdatedAt(currentTime);

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.findById(id).isPresent()) {
            throw new NotFoundException("Product not found");
        }

        productRepository.deleteById(id);
    }

    private Product from (ProductRequestDto productRequestDto) {
        Product product = new Product();

        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        product.setDescription(productRequestDto.getDescription());
        product.setImageUrl(productRequestDto.getImageUrl());
        if (productRequestDto.getCategory() != null) {
            Category category = new Category();
            category.setName(productRequestDto.getCategory().getName());
            category.setDescription(productRequestDto.getCategory().getDescription());
            product.setCategory(category);
        }

        return product;
    }
}
