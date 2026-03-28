package com.example.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderTrendVO {
    private List<String> dates;
    private List<Integer> orderCounts;
    private List<Double> amounts;
}
