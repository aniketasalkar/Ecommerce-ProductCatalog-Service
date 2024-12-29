package com.example.productcatalogservice.dtos;

import lombok.Data;

@Data
public class InventoryDto {
    private Long productId;

    private String productName;

//    private String sku;

    private Integer quantity;
}
