package com.example.controller;

import com.example.common.Result;
import com.example.entity.Category;
import com.example.service.CategoryService;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 鍟嗗搧鍒嗙被琛ㄥ墠绔搷浣滄帴鍙?
 **/
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 鏂板
     */
    @PostMapping("/addCategory")
    public Result addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return Result.success();
    }

    /**
     * 鍒犻櫎
     */
    @DeleteMapping("/deleteCategoryById/{id}")
    public Result deleteCategoryById(@PathVariable Integer id) {
        categoryService.deleteCategoryById(id);
        return Result.success();
    }

    /**
     * 淇敼
     */
    @PutMapping("/updateCategoryById")
    public Result updateCategoryById(@RequestBody Category category) {
        categoryService.updateCategoryById(category);
        return Result.success();
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    @GetMapping("/selectCategoryById/{id}")
    public Result selectCategoryById(@PathVariable Integer id) {
        Category category = categoryService.selectCategoryById(id);
        return Result.success(category);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    @GetMapping("/selectCategoryAll")
    public Result selectCategoryAll(Category category) {
        List<Category> list = categoryService.selectCategoryAll(category);
        return Result.success(list);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    @GetMapping("/selectCategoryPage")
    public Result selectCategoryPage(Category category,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Category> page = categoryService.selectCategoryPage(category, pageNum, pageSize);
        return Result.success(page);
    }

}

