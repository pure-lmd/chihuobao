package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 鐢ㄦ埛鍓嶇鎿嶄綔鎺ュ彛
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 鐧诲綍
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account loginUser = userService.login(account);
        return Result.success(loginUser);
    }

    /**
     * 鏂板
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user) {
        userService.addUser(user);
        return Result.success();
    }

    /**
     * 鍒犻櫎
     */
    @DeleteMapping("/deleteUserById/{id}")
    public Result deleteUserById(@PathVariable Integer id) {
        userService.deleteUserById(id);
        return Result.success();
    }

    /**
     * 淇敼
     */
    @PutMapping("/updateUserById")
    public Result updateUserById(@RequestBody User user) {
        userService.updateUserById(user);
        return Result.success();
    }

    /**
     * 鏍规嵁ID鏌ヨ
     */
    @GetMapping("/selectUserById/{id}")
    public Result selectUserById(@PathVariable Integer id) {
        User user = userService.selectUserById(id);
        return Result.success(user);
    }

    /**
     * 鏌ヨ鎵€鏈?
     */
    @GetMapping("/selectUserAll")
    public Result selectUserAll(User user) {
        List<User> list = userService.selectUserAll(user);
        return Result.success(list);
    }

    /**
     * 鍒嗛〉鏌ヨ
     */
    @GetMapping("/selectUserPage")
    public Result selectUserPage(User user,
                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<User> page = userService.selectUserPage(user, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        userService.updatePassword(account);
        return Result.success();
    }

}
