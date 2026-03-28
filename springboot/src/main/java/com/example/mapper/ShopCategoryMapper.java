package com.example.mapper;

import com.example.entity.ShopCategory;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface ShopCategoryMapper {
    @Select("select * from shop_category where status = 1 order by sort")
    List<ShopCategory> selectAll();
}
