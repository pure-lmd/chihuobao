package com.example.service;

import com.example.entity.Product;
import com.example.entity.dto.ProductDto;
import com.example.mapper.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.List;
import java.time.LocalDateTime;

/**
 * 鍟嗗搧琛ㄤ笟鍔″鐞?
 **/
@Service
public class ProductService {

    @Resource
    private ProductMapper productMapper;

    /**
     * 鏂板
     */
    public void addProduct(Product product) {
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.insertProduct(product);
    }

    /**
     * 鍒犻櫎
     */
    public void deleteProductById(Integer id) {
        productMapper.deleteProductById(id);
    }

    /**
     * 淇敼
     */
    public void updateProductById(Product product) {
        product.setUpdateTime(LocalDateTime.now());
        productMapper.updateProductById(product);
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    public Product selectProductById(Integer id) {
        return productMapper.selectProductById(id);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    public List<ProductDto> selectProductAll(Product product) {
        return productMapper.selectProductAll(product);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    public PageInfo<ProductDto> selectProductPage(Product product, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductDto> list = productMapper.selectProductAll(product);
        return PageInfo.of(list);
    }

}

