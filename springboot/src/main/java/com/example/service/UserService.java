package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public void addUser(User user) {
        User dbUser = userMapper.selectUserByUsername(user.getUsername());
        if (ObjectUtil.isNotNull(dbUser)) {
            throw new ServiceException("用户已存在");
        }
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword("123456");
        }
        // 密码MD5加密存储
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        if (ObjectUtil.isEmpty(user.getRole())) {
            user.setRole("USER");
        }
        userMapper.insertUser(user);
    }

    public void deleteUserById(Integer id) {
        userMapper.deleteUserById(id);
    }

    public void updateUserById(User user) {
        userMapper.updateUserById(user);
    }

    public User selectUserById(Integer id) {
        return userMapper.selectUserById(id);
    }

    public List<User> selectUserAll(User user) {
        return userMapper.selectUserAll(user);
    }

    public PageInfo<User> selectUserPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectUserAll(user);
        return PageInfo.of(list);
    }

    public Account login(Account account) {
        Account dbUser = userMapper.selectUserByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new ServiceException("用户不存在");
        }
        String inputPwdMd5 = DigestUtils.md5DigestAsHex(account.getPassword().getBytes());
        if (!inputPwdMd5.equals(dbUser.getPassword())) {
            throw new ServiceException("账号或密码错误");
        }
        return dbUser;
    }

    public void updatePassword(Account account) {
        User dbUser = userMapper.selectUserByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new ServiceException("用户不存在");
        }
        String oldPwdMd5 = DigestUtils.md5DigestAsHex(account.getPassword().getBytes());
        if (!oldPwdMd5.equals(dbUser.getPassword())) {
            throw new ServiceException("原密码错误");
        }
        String newPwdMd5 = DigestUtils.md5DigestAsHex(account.getNewPassword().getBytes());
        dbUser.setPassword(newPwdMd5);
        userMapper.updateUserById(dbUser);
    }
}
