package com.example.entity;


import java.time.LocalDateTime;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * 鍟嗗搧鍒嗙被琛?
*/
@Data
public class Category {

    /** ID */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 鎵€灞炲簵閾篒D */
    private Integer shopId;

    /** 鍒嗙被鍚嶇О */
    private String name;

    /** 鎺掑簭鏉冮噸 */
    private Integer sort;

    /** 鐘舵€? 0-绂佺敤, 1-鍚敤 */
    private Integer status;

    /** 鍒涘缓鏃堕棿 */
    private LocalDateTime createTime;

    /** 淇敼鏃堕棿 */
    private LocalDateTime updateTime;


}

