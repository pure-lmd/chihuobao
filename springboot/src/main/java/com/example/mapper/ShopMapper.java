package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Shop;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 鎿嶄綔shop鐩稿叧鏁版嵁鎺ュ彛
*/
public interface ShopMapper extends BaseMapper<Shop> {

    /**
      * 鏂板
    */
    int insertShop(Shop shop);

    /**
      * 鍒犻櫎
    */
    @Delete("delete from shop where id = #{id}")
    int deleteShopById(Integer id);

    /**
      * 淇敼
    */
    int updateShopById(Shop shop);

    /**
      * 鏍规嵁ID鏌ヨ
    */
    @Select("select * from shop where id = #{id}")
    Shop selectShopById(Integer id);

    /**
      * 鏌ヨ鎵€鏈?
    */
    List<Shop> selectShopAll(Shop shop);

}

