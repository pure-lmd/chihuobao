package com.example.controller;

import com.example.common.Result;
import com.example.entity.Shop;
import com.example.service.ShopService;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 搴楅摵琛ㄥ墠绔搷浣滄帴鍙?
 **/
@RestController
@RequestMapping("/shop")
public class ShopController {

    @Resource
    private ShopService shopService;

    /**
     * 鏂板
     */
    @PostMapping("/addShop")
    public Result addShop(@RequestBody Shop shop) {
        shopService.addShop(shop);
        return Result.success();
    }

    /**
     * 鍒犻櫎
     */
    @DeleteMapping("/deleteShopById/{id}")
    public Result deleteShopById(@PathVariable Integer id) {
        shopService.deleteShopById(id);
        return Result.success();
    }

    /**
     * 淇敼
     */
    @PutMapping("/updateShopById")
    public Result updateShopById(@RequestBody Shop shop) {
        shopService.updateShopById(shop);
        return Result.success();
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    @GetMapping("/selectShopById/{id}")
    public Result selectShopById(@PathVariable Integer id) {
        Shop shop = shopService.selectShopById(id);
        return Result.success(shop);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    @GetMapping("/selectShopAll")
    public Result selectShopAll(Shop shop) {
        List<Shop> list = shopService.selectShopAll(shop);
        return Result.success(list);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    @GetMapping("/selectShopPage")
    public Result selectShopPage(Shop shop,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Shop> page = shopService.selectShopPage(shop, pageNum, pageSize);
        return Result.success(page);
    }

}

