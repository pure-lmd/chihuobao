package com.example.service;

import com.example.entity.OrderDetail;
import com.example.mapper.OrderDetailMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.List;
import java.time.LocalDateTime;

/**
 * 璁㈠崟鏄庣粏琛ㄤ笟鍔″鐞?
 **/
@Service
public class OrderDetailService {

    @Resource
    private OrderDetailMapper orderDetailMapper;

    /**
     * 鏂板
     */
    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetail.setCreateTime(LocalDateTime.now());
        orderDetail.setUpdateTime(LocalDateTime.now());
        orderDetailMapper.insertOrderDetail(orderDetail);
    }

    /**
     * 鍒犻櫎
     */
    public void deleteOrderDetailById(Integer id) {
        orderDetailMapper.deleteOrderDetailById(id);
    }

    /**
     * 淇敼
     */
    public void updateOrderDetailById(OrderDetail orderDetail) {
        orderDetail.setUpdateTime(LocalDateTime.now());
        orderDetailMapper.updateOrderDetailById(orderDetail);
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    public OrderDetail selectOrderDetailById(Integer id) {
        return orderDetailMapper.selectOrderDetailById(id);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    public List<OrderDetail> selectOrderDetailAll(OrderDetail orderDetail) {
        return orderDetailMapper.selectOrderDetailAll(orderDetail);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    public PageInfo<OrderDetail> selectOrderDetailPage(OrderDetail orderDetail, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OrderDetail> list = orderDetailMapper.selectOrderDetailAll(orderDetail);
        return PageInfo.of(list);
    }

}

