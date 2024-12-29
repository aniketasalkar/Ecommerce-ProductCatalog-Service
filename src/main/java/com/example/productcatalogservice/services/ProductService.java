package com.example.productcatalogservice.services;

import com.example.productcatalogservice.clients.KafkaProducerClient;
import com.example.productcatalogservice.dtos.InventoryDto;
import com.example.productcatalogservice.dtos.ProductPatchRequestDto;
import com.example.productcatalogservice.dtos.ProductRequestDto;
import com.example.productcatalogservice.exceptions.AlreadyExistsException;
import com.example.productcatalogservice.exceptions.EmptyDataException;
import com.example.productcatalogservice.exceptions.NotFoundException;
import com.example.productcatalogservice.exceptions.UnauthorizedException;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.models.ProductSpecification;
import com.example.productcatalogservice.models.Status;
import com.example.productcatalogservice.repository.CategoryRepository;
import com.example.productcatalogservice.repository.ProductRepository;
import com.example.productcatalogservice.utils.InventoryEventsGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService implements IProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    private InventoryEventsGenerator inventoryEventsGenerator;

    @Transactional
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

        Product storedProduct = productRepository.save(product);

        if (storedProduct.getId() == null) {
            log.error("Error occurred while saving product: ");
            throw new RuntimeException("Error occurred while saving product: " + product.getName() + " to database");
        }

        log.info("Product saved to database");
        inventoryEventsGenerator.generateInventoryEvents(storedProduct);

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

    @Transactional
    @Override
    public List<Product> addproductsBulk(List<ProductRequestDto> productRequestDtos) {
        if (productRequestDtos.isEmpty()) {
            throw new EmptyDataException("Product request is empty");
        }

        List<Category> categoriesToBeCreated = new ArrayList<>();

        Set<ProductRequestDto> productDtos = productRequestDtos.stream().collect(Collectors.toSet());
        List<Product> products = new ArrayList<>();
        for (ProductRequestDto productRequestDto : productDtos) {
            Product product = from(productRequestDto);
            product.setProductSpecification(ProductSpecification.ALL);
            product.setStatus(Status.ACTIVE);

            Date currentTime = new Date();
            product.setCreatedAt(currentTime);
            product.setUpdatedAt(currentTime);

            products.add(product);

            // Category Operations
            categoriesToBeCreated.add(product.getCategory());
        }


        List<Product> newProducts = null;
        try {
            categoryService.bulkAddCategories(categoriesToBeCreated);
            for (Product product : products) {
                product.setCategory(categoryRepository.findByName(product.getCategory().getName()).get());
            }
            newProducts = productRepository.saveAll(products);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            System.out.println("Product Exception");
        }

        if (newProducts.size() == 0) {
            log.error("Error occurred while saving products");
            throw new RuntimeException("Error occurred while saving products to database");
        }

        log.info("Products saved to database");
        for (Product product : newProducts) {
            inventoryEventsGenerator.generateInventoryEvents(product);
        }

        return newProducts;
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
