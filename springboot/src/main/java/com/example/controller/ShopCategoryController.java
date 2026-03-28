package com.example.controller;

import com.example.common.Result;
import com.example.mapper.ShopCategoryMapper;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopCategory")
public class ShopCategoryController {
    @Resource
    private ShopCategoryMapper shopCategoryMapper;

    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.success(shopCategoryMapper.selectAll());
    }
}
