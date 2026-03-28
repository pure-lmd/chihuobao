package com.example.entity.vo;

import lombok.Data;

/**
 * 缁熻鍗＄墖鏁版嵁
 */
@Data
public class StatisticsVO {
    private Long userCount;        // 鐢ㄦ埛鎬绘暟
    private Long shopCount;        // 鍟嗗簵鏁伴噺
    private Long orderCount;       // 璁㈠崟鎬绘暟
    private Double totalRevenue;   // 鏀跺叆鎬婚
}

