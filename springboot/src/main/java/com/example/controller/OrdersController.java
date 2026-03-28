package com.example.controller;

import com.example.common.Result;
import com.example.entity.Orders;
import com.example.service.OrdersService;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 璁㈠崟涓昏〃鍓嶇鎿嶄綔鎺ュ彛
 **/
@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    /**
     * 鏂板
     */
    @PostMapping("/addOrders")
    public Result addOrders(@RequestBody Orders orders) {
        ordersService.addOrders(orders);
        return Result.success();
    }

    /**
     * 鍒犻櫎
     */
    @DeleteMapping("/deleteOrdersById/{id}")
    public Result deleteOrdersById(@PathVariable Integer id) {
        ordersService.deleteOrdersById(id);
        return Result.success();
    }

    /**
     * 淇敼
     */
    @PutMapping("/updateOrdersById")
    public Result updateOrdersById(@RequestBody Orders orders) {
        ordersService.updateOrdersById(orders);
        return Result.success();
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    @GetMapping("/selectOrdersById/{id}")
    public Result selectOrdersById(@PathVariable Integer id) {
        Orders orders = ordersService.selectOrdersById(id);
        return Result.success(orders);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    @GetMapping("/selectOrdersAll")
    public Result selectOrdersAll(Orders orders) {
        List<Orders> list = ordersService.selectOrdersAll(orders);
        return Result.success(list);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    @GetMapping("/selectOrdersPage")
    public Result selectOrdersPage(Orders orders,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Orders> page = ordersService.selectOrdersPage(orders, pageNum, pageSize);
        return Result.success(page);
    }

}

