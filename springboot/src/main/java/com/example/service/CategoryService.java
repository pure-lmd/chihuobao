package com.example.service;

import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.List;
import java.time.LocalDateTime;

/**
 * 鍟嗗搧鍒嗙被琛ㄤ笟鍔″鐞?
 **/
@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 鏂板
     */
    public void addCategory(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insertCategory(category);
    }

    /**
     * 鍒犻櫎
     */
    public void deleteCategoryById(Integer id) {
        categoryMapper.deleteCategoryById(id);
    }

    /**
     * 淇敼
     */
    public void updateCategoryById(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.updateCategoryById(category);
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    public Category selectCategoryById(Integer id) {
        return categoryMapper.selectCategoryById(id);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    public List<Category> selectCategoryAll(Category category) {
        return categoryMapper.selectCategoryAll(category);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    public PageInfo<Category> selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Category> list = categoryMapper.selectCategoryAll(category);
        return PageInfo.of(list);
    }

}

