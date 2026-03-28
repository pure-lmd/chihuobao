package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Orders;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * 鎿嶄綔orders鐩稿叧鏁版嵁鎺ュ彛
*/
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
      * 鏂板
    */
    int insertOrders(Orders orders);

    /**
      * 鍒犻櫎
    */
    @Delete("delete from orders where id = #{id}")
    int deleteOrdersById(Integer id);

    /**
      * 淇敼
    */
    int updateOrdersById(Orders orders);

    /**
      * 鏍规嵁ID鏌ヨ
    */
    Orders selectOrdersById(Integer id);

    /**
      * 鏌ヨ鎵€鏈?
    */
    List<Orders> selectOrdersAll(Orders orders);

}

