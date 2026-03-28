package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 鎿嶄綔user鐩稿叧鏁版嵁鎺ュ彛
*/
public interface UserMapper extends BaseMapper<User> {

    /**
      * 鏂板
    */
    int insertUser(User user);

    /**
      * 鍒犻櫎
    */
    @Delete("delete from user where id = #{id}")
    int deleteUserById(Integer id);

    /**
      * 淇敼
    */
    int updateUserById(User user);

    /**
      * 鏍规嵁ID鏌ヨ
    */
    @Select("select * from user where id = #{id}")
    User selectUserById(Integer id);

    /**
      * 鏌ヨ鎵€鏈?
    */
    List<User> selectUserAll(User user);

    @Select("select * from user where username = #{username}")
    User selectUserByUsername(String username);

}


