package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.SysDashboardCardDao;
import com.privatekitchen.admin.entity.SysDashboardCard;
import com.privatekitchen.admin.service.SysDashboardCardService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SysDashboardCardServiceImpl implements SysDashboardCardService {

    private final SysDashboardCardDao sysDashboardCardDao;

    public SysDashboardCardServiceImpl(SysDashboardCardDao sysDashboardCardDao) {
        this.sysDashboardCardDao = sysDashboardCardDao;
    }

    @Override
    public List<SysDashboardCard> listCards() {
        return sysDashboardCardDao.selectList(baseQuery());
    }

    @Override
    public List<SysDashboardCard> listEnabledCards() {
        return sysDashboardCardDao.selectList(baseQuery().eq(SysDashboardCard::getEnabled, 1));
    }

    @Override
    public SysDashboardCard createCard(SysDashboardCard card) {
        LocalDateTime now = LocalDateTime.now();
        card.setEnabled(card.getEnabled() == null ? 1 : card.getEnabled());
        card.setSort(card.getSort() == null ? 0 : card.getSort());
        card.setDelFlag(0);
        card.setCreateTime(now);
        card.setCreateBy("admin");
        card.setUpdateTime(now);
        card.setUpdateBy("admin");
        sysDashboardCardDao.insert(card);
        return card;
    }

    @Override
    public SysDashboardCard updateCard(Long id, SysDashboardCard card) {
        SysDashboardCard existing = sysDashboardCardDao.selectById(id);
        if (existing == null || Integer.valueOf(1).equals(existing.getDelFlag())) {
            return null;
        }
        existing.setCardKey(card.getCardKey());
        existing.setTitle(card.getTitle());
        existing.setCardValue(card.getCardValue());
        existing.setExtraText(card.getExtraText());
        existing.setEnabled(card.getEnabled() == null ? existing.getEnabled() : card.getEnabled());
        existing.setSort(card.getSort() == null ? existing.getSort() : card.getSort());
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        sysDashboardCardDao.updateById(existing);
        return existing;
    }

    @Override
    public void deleteCard(Long id) {
        SysDashboardCard existing = sysDashboardCardDao.selectById(id);
        if (existing == null || Integer.valueOf(1).equals(existing.getDelFlag())) {
            return;
        }
        existing.setDelFlag(1);
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        sysDashboardCardDao.updateById(existing);
    }

    private LambdaQueryWrapper<SysDashboardCard> baseQuery() {
        return new LambdaQueryWrapper<SysDashboardCard>()
                .eq(SysDashboardCard::getDelFlag, 0)
                .orderByAsc(SysDashboardCard::getSort)
                .orderByDesc(SysDashboardCard::getCreateTime);
    }
}
