package com.example.controller;

import com.example.common.Result;
import com.example.entity.OrderDetail;
import com.example.service.OrderDetailService;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 璁㈠崟鏄庣粏琛ㄥ墠绔搷浣滄帴鍙?
 **/
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Resource
    private OrderDetailService orderDetailService;

    /**
     * 鏂板
     */
    @PostMapping("/addOrderDetail")
    public Result addOrderDetail(@RequestBody OrderDetail orderDetail) {
        orderDetailService.addOrderDetail(orderDetail);
        return Result.success();
    }

    /**
     * 鍒犻櫎
     */
    @DeleteMapping("/deleteOrderDetailById/{id}")
    public Result deleteOrderDetailById(@PathVariable Integer id) {
        orderDetailService.deleteOrderDetailById(id);
        return Result.success();
    }

    /**
     * 淇敼
     */
    @PutMapping("/updateOrderDetailById")
    public Result updateOrderDetailById(@RequestBody OrderDetail orderDetail) {
        orderDetailService.updateOrderDetailById(orderDetail);
        return Result.success();
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    @GetMapping("/selectOrderDetailById/{id}")
    public Result selectOrderDetailById(@PathVariable Integer id) {
        OrderDetail orderDetail = orderDetailService.selectOrderDetailById(id);
        return Result.success(orderDetail);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    @GetMapping("/selectOrderDetailAll")
    public Result selectOrderDetailAll(OrderDetail orderDetail) {
        List<OrderDetail> list = orderDetailService.selectOrderDetailAll(orderDetail);
        return Result.success(list);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    @GetMapping("/selectOrderDetailPage")
    public Result selectOrderDetailPage(OrderDetail orderDetail,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<OrderDetail> page = orderDetailService.selectOrderDetailPage(orderDetail, pageNum, pageSize);
        return Result.success(page);
    }

}

