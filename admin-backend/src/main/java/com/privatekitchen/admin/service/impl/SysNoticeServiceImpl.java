package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.SysNoticeDao;
import com.privatekitchen.admin.entity.SysNotice;
import com.privatekitchen.admin.service.SysNoticeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    private final SysNoticeDao sysNoticeDao;

    public SysNoticeServiceImpl(SysNoticeDao sysNoticeDao) {
        this.sysNoticeDao = sysNoticeDao;
    }

    @Override
    public List<SysNotice> listNotices() {
        return sysNoticeDao.selectList(new LambdaQueryWrapper<SysNotice>()
                .eq(SysNotice::getDelFlag, 0)
                .orderByAsc(SysNotice::getSort)
                .orderByDesc(SysNotice::getPublishTime)
                .orderByDesc(SysNotice::getCreateTime));
    }

    @Override
    public SysNotice createNotice(SysNotice notice) {
        LocalDateTime now = LocalDateTime.now();
        notice.setStatus(notice.getStatus() == null ? 1 : notice.getStatus());
        notice.setSort(notice.getSort() == null ? 0 : notice.getSort());
        if (notice.getPublishTime() == null && Integer.valueOf(1).equals(notice.getStatus())) {
            notice.setPublishTime(now);
        }
        notice.setDelFlag(0);
        notice.setCreateTime(now);
        notice.setCreateBy("admin");
        notice.setUpdateTime(now);
        notice.setUpdateBy("admin");
        sysNoticeDao.insert(notice);
        return notice;
    }

    @Override
    public SysNotice updateNotice(Long id, SysNotice notice) {
        SysNotice existing = sysNoticeDao.selectById(id);
        if (existing == null || Integer.valueOf(1).equals(existing.getDelFlag())) {
            return null;
        }
        existing.setTitle(notice.getTitle());
        existing.setContent(notice.getContent());
        existing.setStatus(notice.getStatus() == null ? existing.getStatus() : notice.getStatus());
        existing.setSort(notice.getSort() == null ? existing.getSort() : notice.getSort());
        existing.setPublishTime(notice.getPublishTime() != null ? notice.getPublishTime() : existing.getPublishTime());
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        sysNoticeDao.updateById(existing);
        return existing;
    }

    @Override
    public void deleteNotice(Long id) {
        SysNotice existing = sysNoticeDao.selectById(id);
        if (existing == null || Integer.valueOf(1).equals(existing.getDelFlag())) {
            return;
        }
        existing.setDelFlag(1);
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        sysNoticeDao.updateById(existing);
    }
}
