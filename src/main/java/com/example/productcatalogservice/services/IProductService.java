package com.example.productcatalogservice.services;

import com.example.productcatalogservice.dtos.ProductPatchRequestDto;
import com.example.productcatalogservice.dtos.ProductRequestDto;
import com.example.productcatalogservice.models.Product;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductRequestDto productRequestDto);
    Product getProduct(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, ProductPatchRequestDto productPatchRequestDto);
    void deleteProduct(Long id);
    List<Product> addproductsBulk(List<ProductRequestDto> productRequestDtos);
}
