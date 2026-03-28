package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Product;
import com.example.entity.dto.ProductDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 鎿嶄綔product鐩稿叧鏁版嵁鎺ュ彛
*/
public interface ProductMapper extends BaseMapper<Product> {

    /**
      * 鏂板
    */
    int insertProduct(Product product);

    /**
      * 鍒犻櫎
    */
    @Delete("delete from product where id = #{id}")
    int deleteProductById(Integer id);

    /**
      * 淇敼
    */
    int updateProductById(Product product);

    /**
      * 鏍规嵁ID鏌ヨ
    */
    @Select("select * from product where id = #{id}")
    Product selectProductById(Integer id);

    /**
      * 鏌ヨ鎵€鏈?
    */
    List<ProductDto> selectProductAll(Product product);

    /**
     * 扣减库存并增加销量
     */
    @Update("update product set stock = stock - #{quantity}, sales = sales + #{quantity}, update_time = now() where id = #{id} and stock >= #{quantity}")
    int reduceStock(@Param("id") Integer id, @Param("quantity") Integer quantity);

    /**
     * 库存为0时自动下架
     */
    @Update("update product set status = 0, update_time = now() where id = #{id} and stock <= 0")
    int offShelfIfNoStock(@Param("id") Integer id);

}
