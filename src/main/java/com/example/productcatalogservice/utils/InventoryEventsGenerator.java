package com.example.productcatalogservice.utils;

import com.example.productcatalogservice.clients.KafkaProducerClient;
import com.example.productcatalogservice.dtos.InventoryDto;
import com.example.productcatalogservice.models.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryEventsGenerator {

    @Autowired
    KafkaProducerClient kafkaProducerClient;

    @Autowired
    ObjectMapper objectMapper;

    private String initializeInventoryTopic = "initialize-inventory";

    public void generateInventoryEvents(Product storedProduct) {
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setProductId(storedProduct.getId());
        inventoryDto.setProductName(storedProduct.getName());
        inventoryDto.setQuantity(0);
        try {
            kafkaProducerClient.sendMessage(initializeInventoryTopic, objectMapper.writeValueAsString(inventoryDto));
            log.info("Kafka event generated for product: {}", storedProduct.getId());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
