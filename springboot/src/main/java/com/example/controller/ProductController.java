package com.example.controller;

import com.example.common.Result;
import com.example.entity.Product;
import com.example.entity.dto.ProductDto;
import com.example.service.ProductService;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 鍟嗗搧琛ㄥ墠绔搷浣滄帴鍙?
 **/
@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 鏂板
     */
    @PostMapping("/addProduct")
    public Result addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return Result.success();
    }

    /**
     * 鍒犻櫎
     */
    @DeleteMapping("/deleteProductById/{id}")
    public Result deleteProductById(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return Result.success();
    }

    /**
     * 淇敼
     */
    @PutMapping("/updateProductById")
    public Result updateProductById(@RequestBody Product product) {
        productService.updateProductById(product);
        return Result.success();
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    @GetMapping("/selectProductById/{id}")
    public Result selectProductById(@PathVariable Integer id) {
        Product product = productService.selectProductById(id);
        return Result.success(product);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    @GetMapping("/selectProductAll")
    public Result selectProductAll(Product product) {
        List<ProductDto> list = productService.selectProductAll(product);
        return Result.success(list);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    @GetMapping("/selectProductPage")
    public Result selectProductPage(Product product,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<ProductDto> page = productService.selectProductPage(product, pageNum, pageSize);
        return Result.success(page);
    }

}

