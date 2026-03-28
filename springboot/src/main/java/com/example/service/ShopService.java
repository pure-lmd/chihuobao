package com.example.service;

import com.example.entity.Shop;
import com.example.mapper.ShopMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.List;
import java.time.LocalDateTime;

/**
 * 搴楅摵琛ㄤ笟鍔″鐞?
 **/
@Service
public class ShopService {

    @Resource
    private ShopMapper shopMapper;

    /**
     * 鏂板
     */
    public void addShop(Shop shop) {
        shop.setCreateTime(LocalDateTime.now());
        shop.setUpdateTime(LocalDateTime.now());
        shopMapper.insertShop(shop);
    }

    /**
     * 鍒犻櫎
     */
    public void deleteShopById(Integer id) {
        shopMapper.deleteShopById(id);
    }

    /**
     * 淇敼
     */
    public void updateShopById(Shop shop) {
        shop.setUpdateTime(LocalDateTime.now());
        shopMapper.updateShopById(shop);
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    public Shop selectShopById(Integer id) {
        return shopMapper.selectShopById(id);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    public List<Shop> selectShopAll(Shop shop) {
        return shopMapper.selectShopAll(shop);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    public PageInfo<Shop> selectShopPage(Shop shop, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shop> list = shopMapper.selectShopAll(shop);
        return PageInfo.of(list);
    }

}

