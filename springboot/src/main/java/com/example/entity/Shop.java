package com.example.entity;


import java.time.LocalDateTime;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 搴楅摵琛?
*/
@Data
public class Shop {

    /** ID */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 搴楅摵鍚嶇О */
    private String name;

    /** 搴楅摵鍦板潃 */
    private String address;

    /** 鑱旂郴鐢佃瘽 */
    private String phone;

    /** 搴楅摵灏侀潰鍥剧墖 */
    private String coverUrl;

    /** 搴楅摵绠€浠?*/
    private String description;

    /** 璧烽€佷环 */
    private Double minPrice;

    /** 閰嶉€佽垂 */
    private Double deliveryFee;

    /** 缁煎悎璇勫垎 */
    private Double rating;

    /** 钀ヤ笟鐘舵€? 0-浼戞伅, 1-钀ヤ笟 */
    private Integer status;

    /** 商家分类ID */
    private Integer categoryId;

    /** 搴椾富ID锛屽叧鑱攗ser琛?*/
    private Integer ownerId;

    /** 鍒涘缓鏃堕棿 */
    private LocalDateTime createTime;

    /** 淇敼鏃堕棿 */
    private LocalDateTime updateTime;


}

