package com.privatekitchen.admin.service;

import com.privatekitchen.admin.dto.SysLoginRequest;
import com.privatekitchen.admin.entity.SysUser;
import com.privatekitchen.admin.vo.SysLoginUserVO;

import java.util.List;

public interface SysUserService {

    SysLoginUserVO login(SysLoginRequest request);

    List<SysUser> listUsers();

    SysUser createUser(SysUser user);

    SysUser updateUser(Long id, SysUser user);

    void deleteUser(Long id);
}
