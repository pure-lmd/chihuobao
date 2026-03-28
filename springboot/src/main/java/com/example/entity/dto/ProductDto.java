package com.example.entity.dto;

import com.example.entity.Product;
import lombok.Data;

@Data
public class ProductDto extends Product {

    private String shopName;

    private String categoryName;
}

