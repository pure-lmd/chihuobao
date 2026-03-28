package com.example.entity;


import java.time.LocalDateTime;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 璁㈠崟鏄庣粏琛?
*/
@Data
public class OrderDetail {

    /** ID */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 璁㈠崟ID */
    private Integer orderId;

    /** 鍟嗗搧ID */
    private Integer productId;

    /** 鍟嗗搧鍚嶇О蹇収 */
    private String productName;

    /** 鍟嗗搧蹇収鍥剧墖 */
    private String productImage;

    /** 璐拱鍗曚环蹇収 */
    private Double price;

    /** 璐拱鏁伴噺 */
    private Integer quantity;

    /** 鎬讳环鏍?鍗曚环*鏁伴噺) */
    private Double totalFee;

    /** 鍒涘缓鏃堕棿 */
    private LocalDateTime createTime;

    /** 淇敼鏃堕棿 */
    private LocalDateTime updateTime;


}

