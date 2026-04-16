package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.SysUserDao;
import com.privatekitchen.admin.dto.SysLoginRequest;
import com.privatekitchen.admin.entity.SysUser;
import com.privatekitchen.admin.service.SysUserService;
import com.privatekitchen.admin.vo.SysLoginUserVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {

    private final SysUserDao sysUserDao;

    public SysUserServiceImpl(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    @Override
    public SysLoginUserVO login(SysLoginRequest request) {
        if (request == null || isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            return null;
        }

        SysUser user = sysUserDao.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDelFlag, 0)
                .eq(SysUser::getUsername, request.getUsername())
                .last("LIMIT 1"));

        if (user == null || !Integer.valueOf(1).equals(user.getStatus())) {
            return null;
        }

        if (!request.getPassword().equals(user.getPassword())) {
            return null;
        }

        user.setUpdateTime(LocalDateTime.now());
        user.setUpdateBy(user.getUsername());
        sysUserDao.updateById(user);

        SysLoginUserVO result = new SysLoginUserVO();
        result.setId(user.getId());
        result.setUsername(user.getUsername());
        result.setDisplayName(defaultText(user.getDisplayName(), user.getUsername()));
        result.setAvatar(user.getAvatar());
        result.setRoleName(defaultText(user.getRoleName(), "超级管理员"));
        result.setStatus(user.getStatus());
        result.setToken("sys-token-" + user.getId());
        return result;
    }

    @Override
    public List<SysUser> listUsers() {
        return sysUserDao.selectList(baseQuery());
    }

    @Override
    public SysUser createUser(SysUser user) {
        LocalDateTime now = LocalDateTime.now();
        user.setStatus(user.getStatus() == null ? 1 : user.getStatus());
        user.setRoleName(defaultText(user.getRoleName(), "超级管理员"));
        user.setPassword(defaultText(user.getPassword(), "admin123"));
        user.setDelFlag(0);
        user.setCreateTime(now);
        user.setCreateBy("admin");
        user.setUpdateTime(now);
        user.setUpdateBy("admin");
        sysUserDao.insert(user);
        return user;
    }

    @Override
    public SysUser updateUser(Long id, SysUser user) {
        SysUser existing = sysUserDao.selectById(id);
        if (existing == null || Integer.valueOf(1).equals(existing.getDelFlag())) {
            return null;
        }
        existing.setUsername(defaultText(user.getUsername(), existing.getUsername()));
        existing.setPassword(defaultText(user.getPassword(), existing.getPassword()));
        existing.setDisplayName(defaultText(user.getDisplayName(), existing.getDisplayName()));
        existing.setPhone(user.getPhone());
        existing.setAvatar(user.getAvatar());
        existing.setRoleName(defaultText(user.getRoleName(), existing.getRoleName()));
        existing.setStatus(user.getStatus() == null ? existing.getStatus() : user.getStatus());
        existing.setRemark(user.getRemark());
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        sysUserDao.updateById(existing);
        return existing;
    }

    @Override
    public void deleteUser(Long id) {
        SysUser existing = sysUserDao.selectById(id);
        if (existing == null || Integer.valueOf(1).equals(existing.getDelFlag())) {
            return;
        }
        existing.setDelFlag(1);
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        sysUserDao.updateById(existing);
    }

    private LambdaQueryWrapper<SysUser> baseQuery() {
        return new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDelFlag, 0)
                .orderByDesc(SysUser::getCreateTime);
    }

    private String defaultText(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
