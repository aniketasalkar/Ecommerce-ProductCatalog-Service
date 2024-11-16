package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.CategoryResponseDto;
import com.example.productcatalogservice.dtos.ProductPatchRequestDto;
import com.example.productcatalogservice.dtos.ProductRequestDto;
import com.example.productcatalogservice.dtos.ProductResponseDto;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productCategoryService")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        try {
            Product product = productService.createProduct(productRequestDto);

            return new ResponseEntity<>(from(product), HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProduct(id);

            return new ResponseEntity<>(from(product), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }

    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();

            List<ProductResponseDto> productResponseDtos = new ArrayList<>();
            for (Product product : products) {
                productResponseDtos.add(from(product));
            }

            return new ResponseEntity<>(productResponseDtos, HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }


    @PatchMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody ProductPatchRequestDto productPatchRequestDto) {
        try {
            Product product = productService.updateProduct(id, productPatchRequestDto);

            return new ResponseEntity<>(from(product), HttpStatus.OK);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @PostMapping("/products/bulk")
    public ResponseEntity<List<ProductResponseDto>> createProductsBulk(@RequestBody @Valid List<ProductRequestDto> productRequestDtoList) {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        try {
            List<Product> createdProducts = productService.addproductsBulk(productRequestDtoList);
            for (Product product : createdProducts) {
                productResponseDtoList.add(from(product));
            }

            return new ResponseEntity<>(productResponseDtoList, HttpStatus.CREATED);
        } catch (Exception exception) {
            throw exception;
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);

            return new ResponseEntity<>("Product deleted successfully.", HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            throw exception;
        }
    }

//    private Product from (ProductRequestDto productRequestDto) {
//        Product product = new Product();
//
//        product.setName(productRequestDto.getName());
//        product.setPrice(productRequestDto.getPrice());
//        product.setDescription(productRequestDto.getDescription());
//        product.setCategory(productRequestDto.getCategory());
//        product.setImageUrl(productRequestDto.getImageUrl());
//
//        return product;
//    }

    private ProductResponseDto from (Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();

        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setDescription(product.getDescription());
        productResponseDto.setImageUrl(product.getImageUrl());
        productResponseDto.setProductSpecification(product.getProductSpecification());
        if (product.getCategory() != null) {
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
            categoryResponseDto.setId(product.getCategory().getId());
            categoryResponseDto.setName(product.getCategory().getName());
            categoryResponseDto.setDescription(product.getCategory().getDescription());
            productResponseDto.setCategory(categoryResponseDto);
        }

        return productResponseDto;
    }

}
