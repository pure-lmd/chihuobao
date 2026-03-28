package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 鎿嶄綔category鐩稿叧鏁版嵁鎺ュ彛
*/
public interface CategoryMapper extends BaseMapper<Category> {

    /**
      * 鏂板
    */
    int insertCategory(Category category);

    /**
      * 鍒犻櫎
    */
    @Delete("delete from category where id = #{id}")
    int deleteCategoryById(Integer id);

    /**
      * 淇敼
    */
    int updateCategoryById(Category category);

    /**
      * 鏍规嵁ID鏌ヨ
    */
    @Select("select * from category where id = #{id}")
    Category selectCategoryById(Integer id);

    /**
      * 鏌ヨ鎵€鏈?
    */
    List<Category> selectCategoryAll(Category category);

}

