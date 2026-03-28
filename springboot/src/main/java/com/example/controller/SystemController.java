package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.service.UserService;
import javax.annotation.Resource;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


@RestController
public class SystemController {

    @Resource
    private UserService userService;


    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = userService.login(account);
        return Result.success(ac);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        userService.addUser(new com.example.entity.User() {{
            setUsername(account.getUsername());
            setPassword(account.getPassword());
            setName(account.getName());
            setRole("USER");
        }});
        return Result.success();
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
