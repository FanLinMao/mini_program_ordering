package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.SysUserLoginLogDao;
import com.privatekitchen.admin.entity.SysUserLoginLog;
import com.privatekitchen.admin.service.SysUserLoginLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysUserLoginLogServiceImpl implements SysUserLoginLogService {

    private final SysUserLoginLogDao sysUserLoginLogDao;

    public SysUserLoginLogServiceImpl(SysUserLoginLogDao sysUserLoginLogDao) {
        this.sysUserLoginLogDao = sysUserLoginLogDao;
    }

    @Override
    public void recordLog(String username, String displayName, String actionType, Integer successFlag,
                          String ipAddress, String userAgent, String remark) {
        SysUserLoginLog log = new SysUserLoginLog();
        log.setUsername(username);
        log.setDisplayName(displayName);
        log.setActionType(actionType);
        log.setSuccessFlag(successFlag == null ? 1 : successFlag);
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        log.setRemark(remark);
        log.setDelFlag(0);
        log.setCreateTime(LocalDateTime.now());
        log.setCreateBy(username == null || username.isBlank() ? "system" : username);
        log.setUpdateTime(LocalDateTime.now());
        log.setUpdateBy(username == null || username.isBlank() ? "system" : username);
        sysUserLoginLogDao.insert(log);
    }

    @Override
    public List<SysUserLoginLog> listByUsername(String username) {
        return sysUserLoginLogDao.selectList(new LambdaQueryWrapper<SysUserLoginLog>()
                .eq(SysUserLoginLog::getDelFlag, 0)
                .eq(SysUserLoginLog::getUsername, username)
                .orderByDesc(SysUserLoginLog::getCreateTime)
                .last("LIMIT 20"));
    }
}
