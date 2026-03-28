package com.example.entity;


import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 璁㈠崟涓昏〃
*/
@Data
public class Orders {

    /** ID */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 璁㈠崟鏄庣粏鍒楄〃锛堥潪鏁版嵁搴撳瓧娈碉級 */
    @TableField(exist = false)
    private List<OrderDetail> orderDetails;

    /** 璁㈠崟缂栧彿(鍞竴) */
    private String orderNo;

    /** 涓嬪崟鐢ㄦ埛ID */
    private Integer userId;

    /** 搴楅摵ID */
    private Integer shopId;

    /** 搴楅摵鍚嶇О锛堝叧鑱旀煡璇㈠瓧娈碉級 */
    @TableField(exist = false)
    private String shopName;

    /** 璁㈠崟鎬婚噾棰?*/
    private Double totalAmount;

    /** 瀹炰粯閲戦 */
    private Double payAmount;

    /** 杩愯垂 */
    private Double deliveryFee;

    /** 璁㈠崟澶囨敞 */
    private String remark;

    /** 璁㈠崟鐘舵€? 1-寰呬粯娆? 2-寰呮帴鍗? 3-閰嶉€佷腑, 4-宸插畬鎴? 5-宸插彇娑?*/
    private Integer status;

    /** 鏀惰揣浜哄鍚?*/
    private String receiverName;

    /** 鏀惰揣浜虹數璇?*/
    private String receiverPhone;

    /** 鏀惰揣鍦板潃 */
    private String receiverAddress;

    /** 鏀粯鏃堕棿 */
    private LocalDateTime payTime;

    /** 鍙戣揣鏃堕棿 */
    private LocalDateTime deliveryTime;

    /** 瀹屾垚鏃堕棿 */
    private LocalDateTime finishTime;

    /** 鍒涘缓鏃堕棿 */
    private LocalDateTime createTime;

    /** 淇敼鏃堕棿 */
    private LocalDateTime updateTime;


}

