package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.SysUserLoginLog;

import java.util.List;

public interface SysUserLoginLogService {

    void recordLog(String username, String displayName, String actionType, Integer successFlag,
                   String ipAddress, String userAgent, String remark);

    List<SysUserLoginLog> listByUsername(String username);
}
