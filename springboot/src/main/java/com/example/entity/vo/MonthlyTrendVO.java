package com.example.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * 鏈堝害瓒嬪娍
 */
@Data
public class MonthlyTrendVO {
    private List<String> months;    // 鏈堜唤鍒楄〃
    private List<Double> revenues;  // 鏀跺叆
    private List<Integer> newUsers; // 鏂板鐢ㄦ埛
}

