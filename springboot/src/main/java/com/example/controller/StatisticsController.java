package com.example.controller;

import com.example.common.Result;
import com.example.service.StatisticsService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 缁熻鍓嶇鎿嶄綔鎺ュ彛
 */
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    /**
     * 鑾峰彇缁熻鍗＄墖鏁版嵁
     */
    @GetMapping("/summary")
    public Result getSummary() {
        return Result.success(statisticsService.getSummary());
    }

    /**
     * 鑾峰彇杩?鏃ヨ鍗曡秼鍔?     */
    @GetMapping("/order-trend")
    public Result getOrderTrend() {
        return Result.success(statisticsService.getOrderTrend());
    }

    /**
     * 鑾峰彇璁㈠崟鐘舵€佸垎甯?     */
    @GetMapping("/order-status")
    public Result getOrderStatus() {
        return Result.success(statisticsService.getOrderStatusDistribution());
    }

    /**
     * 鑾峰彇鐑攢鑿滃搧TOP10
     */
    @GetMapping("/hot-products")
    public Result getHotProducts() {
        return Result.success(statisticsService.getHotProducts());
    }

    /**
     * 鑾峰彇鍒嗙被閿€閲忓崰姣?     */
    @GetMapping("/category-sales")
    public Result getCategorySales() {
        return Result.success(statisticsService.getCategorySales());
    }

    /**
     * 鑾峰彇鏈堝害鏀跺叆瓒嬪娍
     */
    @GetMapping("/monthly-revenue")
    public Result getMonthlyRevenue() {
        return Result.success(statisticsService.getMonthlyRevenue());
    }

    /**
     * 鑾峰彇鐢ㄦ埛澧為暱瓒嬪娍
     */
    @GetMapping("/user-growth")
    public Result getUserGrowth() {
        return Result.success(statisticsService.getUserGrowth());
    }
}

