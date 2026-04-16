package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.UserDao;
import com.privatekitchen.admin.entity.User;
import com.privatekitchen.admin.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> listUsers() {
        return userDao.selectList(new LambdaQueryWrapper<User>()
                .eq(User::getDelFlag, 0)
                .orderByDesc(User::getUpdateTime));
    }

    @Override
    public User saveUser(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setDelFlag(0);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setCreateBy("admin");
        user.setUpdateBy("admin");
        userDao.insert(user);
        return user;
    }

    @Override
    public User updateUser(Long id, User user) {
        User existing = userDao.selectById(id);
        existing.setOpenid(user.getOpenid());
        existing.setNickname(user.getNickname());
        existing.setPhone(user.getPhone());
        existing.setAvatar(user.getAvatar());
        existing.setPoints(user.getPoints());
        existing.setMemberLevel(user.getMemberLevel());
        existing.setStatus(user.getStatus());
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        userDao.updateById(existing);
        return existing;
    }

    @Override
    public void deleteUser(Long id) {
        User existing = userDao.selectById(id);
        if (existing == null) {
            return;
        }
        existing.setDelFlag(1);
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        userDao.updateById(existing);
    }
}
