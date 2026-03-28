package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {

    @Select("SELECT COUNT(*) FROM user")
    Long countUsers();

    @Select("SELECT COUNT(*) FROM shop WHERE status = 1")
    Long countShops();

    @Select("SELECT COUNT(*) FROM orders")
    Long countOrders();

    @Select("SELECT COALESCE(SUM(pay_amount), 0) FROM orders WHERE status != 5")
    Double sumTotalRevenue();

    @Select("SELECT DATE(create_time) as date, COUNT(*) as cnt, COALESCE(SUM(pay_amount), 0) as amount FROM orders WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) GROUP BY DATE(create_time) ORDER BY date")
    List<Map<String, Object>> getOrderTrend();

    @Select("SELECT status as name, COUNT(*) as value FROM orders GROUP BY status")
    List<Map<String, Object>> getOrderStatusDistribution();

    @Select("SELECT name, COALESCE(sales, 0) as sales FROM product ORDER BY sales DESC LIMIT 10")
    List<Map<String, Object>> getHotProducts();

    @Select("SELECT c.name, COALESCE(SUM(p.sales), 0) as sales FROM category c LEFT JOIN product p ON c.id = p.category_id GROUP BY c.id, c.name HAVING sales > 0 ORDER BY sales DESC LIMIT 10")
    List<Map<String, Object>> getCategorySales();

    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m') as month, COALESCE(SUM(pay_amount), 0) as revenue FROM orders WHERE status != 5 GROUP BY DATE_FORMAT(create_time, '%Y-%m') ORDER BY month")
    List<Map<String, Object>> getMonthlyRevenue();

    @Select("SELECT COALESCE(DATE_FORMAT(create_time, '%Y-%m'), DATE_FORMAT(CURDATE(), '%Y-%m')) as month, COUNT(*) as count FROM user GROUP BY COALESCE(DATE_FORMAT(create_time, '%Y-%m'), DATE_FORMAT(CURDATE(), '%Y-%m')) ORDER BY month")
    List<Map<String, Object>> getUserGrowth();
}

