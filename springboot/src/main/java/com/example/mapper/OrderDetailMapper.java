package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.OrderDetail;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 鎿嶄綔orderdetail鐩稿叧鏁版嵁鎺ュ彛
*/
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
      * 鏂板
    */
    int insertOrderDetail(OrderDetail orderDetail);

    /**
      * 鍒犻櫎
    */
    @Delete("delete from order_detail where id = #{id}")
    int deleteOrderDetailById(Integer id);

    /**
      * 淇敼
    */
    int updateOrderDetailById(OrderDetail orderDetail);

    /**
      * 鏍规嵁ID鏌ヨ
    */
    @Select("select * from order_detail where id = #{id}")
    OrderDetail selectOrderDetailById(Integer id);

    /**
      * 鏌ヨ鎵€鏈?
    */
    List<OrderDetail> selectOrderDetailAll(OrderDetail orderDetail);

}

