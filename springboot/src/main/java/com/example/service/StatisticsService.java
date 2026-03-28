package com.example.service;

import com.example.entity.vo.*;
import com.example.mapper.StatisticsMapper;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;

    public StatisticsVO getSummary() {
        StatisticsVO vo = new StatisticsVO();
        vo.setUserCount(statisticsMapper.countUsers());
        vo.setShopCount(statisticsMapper.countShops());
        vo.setOrderCount(statisticsMapper.countOrders());
        vo.setTotalRevenue(statisticsMapper.sumTotalRevenue());
        return vo;
    }

    public OrderTrendVO getOrderTrend() {
        List<Map<String, Object>> data = statisticsMapper.getOrderTrend();
        OrderTrendVO vo = new OrderTrendVO();

        List<String> dates = new ArrayList<>();
        List<Integer> orderCounts = new ArrayList<>();
        List<Double> amounts = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.format(formatter);
            dates.add(dateStr);
            orderCounts.add(0);
            amounts.add(0.0);
        }

        for (Map<String, Object> row : data) {
            Object dateObj = row.get("date");
            if (dateObj != null) {
                String dateStr = dateObj.toString();
                int index = dates.indexOf(dateStr);
                if (index >= 0) {
                    Object cntObj = row.get("cnt");
                    Object amountObj = row.get("amount");
                    if (cntObj != null) {
                        orderCounts.set(index, ((Number) cntObj).intValue());
                    }
                    if (amountObj != null) {
                        amounts.set(index, ((Number) amountObj).doubleValue());
                    }
                }
            }
        }

        vo.setDates(dates);
        vo.setOrderCounts(orderCounts);
        vo.setAmounts(amounts);
        return vo;
    }

    public List<OrderStatusVO> getOrderStatusDistribution() {
        List<Map<String, Object>> data = statisticsMapper.getOrderStatusDistribution();
        List<OrderStatusVO> result = new ArrayList<>();
        for (Map<String, Object> row : data) {
            OrderStatusVO vo = new OrderStatusVO();
            Object nameObj = row.get("name");
            vo.setName(getStatusName(nameObj != null ? ((Number) nameObj).intValue() : 0));
            Object valueObj = row.get("value");
            vo.setValue(valueObj != null ? ((Number) valueObj).intValue() : 0);
            result.add(vo);
        }
        return result;
    }

    private String getStatusName(int status) {
        switch (status) {
            case 1: return "待支付";
            case 2: return "待接单";
            case 3: return "配送中";
            case 4: return "已完成";
            case 5: return "已取消";
            default: return "未知";
        }
    }

    public List<HotProductVO> getHotProducts() {
        List<Map<String, Object>> data = statisticsMapper.getHotProducts();
        List<HotProductVO> result = new ArrayList<>();
        for (Map<String, Object> row : data) {
            HotProductVO vo = new HotProductVO();
            vo.setName((String) row.get("name"));
            Object salesObj = row.get("sales");
            vo.setSales(salesObj != null ? ((Number) salesObj).intValue() : 0);
            result.add(vo);
        }
        return result;
    }

    public List<CategorySalesVO> getCategorySales() {
        List<Map<String, Object>> data = statisticsMapper.getCategorySales();
        List<CategorySalesVO> result = new ArrayList<>();
        for (Map<String, Object> row : data) {
            CategorySalesVO vo = new CategorySalesVO();
            vo.setName((String) row.get("name"));
            Object salesObj = row.get("sales");
            vo.setValue(salesObj != null ? ((Number) salesObj).intValue() : 0);
            result.add(vo);
        }
        return result;
    }

    public MonthlyTrendVO getMonthlyRevenue() {
        List<Map<String, Object>> revenueData = statisticsMapper.getMonthlyRevenue();
        List<Map<String, Object>> userData = statisticsMapper.getUserGrowth();

        MonthlyTrendVO vo = new MonthlyTrendVO();
        List<String> months = new ArrayList<>();
        List<Double> revenues = new ArrayList<>();
        List<Integer> newUsers = new ArrayList<>();

        for (Map<String, Object> row : revenueData) {
            String month = (String) row.get("month");
            months.add(month);
            Object revenueObj = row.get("revenue");
            revenues.add(revenueObj != null ? ((Number) revenueObj).doubleValue() : 0.0);
        }

        for (int i = 0; i < months.size(); i++) {
            newUsers.add(0);
        }

        for (Map<String, Object> row : userData) {
            String month = (String) row.get("month");
            int index = months.indexOf(month);
            if (index >= 0) {
                Object countObj = row.get("count");
                newUsers.set(index, countObj != null ? ((Number) countObj).intValue() : 0);
            }
        }

        vo.setMonths(months);
        vo.setRevenues(revenues);
        vo.setNewUsers(newUsers);
        return vo;
    }

    public List<Map<String, Object>> getUserGrowth() {
        return statisticsMapper.getUserGrowth();
    }
}
