package com.privatekitchen.admin.service;

import com.privatekitchen.admin.dto.SysLoginRequest;
import com.privatekitchen.admin.dto.SysLogoutRequest;
import com.privatekitchen.admin.entity.SysUser;
import com.privatekitchen.admin.entity.SysUserLoginLog;
import com.privatekitchen.admin.vo.SysLoginUserVO;

import java.util.List;

public interface SysUserService {

    SysLoginUserVO login(SysLoginRequest request);

    boolean logout(SysLogoutRequest request);

    SysUser getCurrentUser(String username);

    SysUser updateCurrentUser(String username, SysUser user);

    boolean changePassword(String username, String oldPassword, String newPassword);

    List<SysUserLoginLog> listCurrentUserLogs(String username);

    List<SysUser> listUsers();

    SysUser createUser(SysUser user);

    SysUser updateUser(Long id, SysUser user);

    void deleteUser(Long id);
}
