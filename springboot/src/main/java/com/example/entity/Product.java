package com.example.entity;


import java.time.LocalDateTime;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 鍟嗗搧琛?
*/
@Data
public class Product {

    /** ID */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 搴楅摵ID */
    private Integer shopId;

    /** 鍒嗙被ID */
    private Integer categoryId;

    /** 鍟嗗搧鍚嶇О */
    private String name;

    /** 浠锋牸 */
    private Double price;

    /** 搴撳瓨 */
    private Integer stock;

    /** 鍟嗗搧鎻忚堪 */
    private String description;

    /** 鍟嗗搧涓诲浘鍥剧墖 */
    private String imageUrl;

    /** 绱閿€閲?鍐椾綑瀛楁锛屼篃鍙笉瀛橀潬鑱氬悎) */
    private Integer sales;

    /** 鍞崠鐘舵€? 0-鍋滃敭, 1-鍚敭 */
    private Integer status;

    /** 鍒涘缓鏃堕棿 */
    private LocalDateTime createTime;

    /** 淇敼鏃堕棿 */
    private LocalDateTime updateTime;


}

