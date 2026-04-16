package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.SysNotice;

import java.util.List;

public interface SysNoticeService {

    List<SysNotice> listNotices();

    SysNotice createNotice(SysNotice notice);

    SysNotice updateNotice(Long id, SysNotice notice);

    void deleteNotice(Long id);
}
