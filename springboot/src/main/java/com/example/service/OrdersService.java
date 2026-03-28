package com.example.service;

import com.example.entity.OrderDetail;
import com.example.entity.Orders;
import com.example.mapper.OrderDetailMapper;
import com.example.mapper.OrdersMapper;
import com.example.mapper.ProductMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

/**
 * 璁㈠崟涓昏〃涓氬姟澶勭悊
 **/
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Resource
    private ProductMapper productMapper;

    /**
     * 鏂板锛堝悓鏃跺垱寤鸿鍗曞拰璁㈠崟鏄庣粏锛?
     */
    @Transactional
    public void addOrders(Orders orders) {
        orders.setCreateTime(LocalDateTime.now());
        orders.setUpdateTime(LocalDateTime.now());
        // 鎻掑叆璁㈠崟涓昏〃
        ordersMapper.insert(orders);

        // 鎻掑叆璁㈠崟鏄庣粏
        if (orders.getOrderDetails() != null && !orders.getOrderDetails().isEmpty()) {
            for (OrderDetail detail : orders.getOrderDetails()) {
                detail.setOrderId(orders.getId());
                detail.setCreateTime(LocalDateTime.now());
                detail.setUpdateTime(LocalDateTime.now());
                orderDetailMapper.insertOrderDetail(detail);
                // 扣减库存并增加销量
                productMapper.reduceStock(detail.getProductId(), detail.getQuantity());
                // 库存为0时自动下架
                productMapper.offShelfIfNoStock(detail.getProductId());
            }
        }
    }

    /**
     * 鍒犻櫎
     */
    public void deleteOrdersById(Integer id) {
        ordersMapper.deleteOrdersById(id);
    }

    /**
     * 淇敼
     */
    public void updateOrdersById(Orders orders) {
        orders.setUpdateTime(LocalDateTime.now());
        ordersMapper.updateOrdersById(orders);
    }

    /**
     * 鏍规嵁ID鏌ヨ锛堝寘鍚鍗曟槑缁嗭級
     */
    public Orders selectOrdersById(Integer id) {
        Orders orders = ordersMapper.selectOrdersById(id);
        if (orders != null) {
            // 鏌ヨ璁㈠崟鏄庣粏
            OrderDetail detailQuery = new OrderDetail();
            detailQuery.setOrderId(id);
            List<OrderDetail> details = orderDetailMapper.selectOrderDetailAll(detailQuery);
            // 从商品表获取最新图片
            for (OrderDetail detail : details) {
                if (detail.getProductId() != null) {
                    com.example.entity.Product product = productMapper.selectProductById(detail.getProductId());
                    if (product != null && product.getImageUrl() != null) {
                        detail.setProductImage(product.getImageUrl());
                    }
                }
            }
            orders.setOrderDetails(details);
        }
        return orders;
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    public List<Orders> selectOrdersAll(Orders orders) {
        return ordersMapper.selectOrdersAll(orders);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    public PageInfo<Orders> selectOrdersPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectOrdersAll(orders);
        return PageInfo.of(list);
    }

}
