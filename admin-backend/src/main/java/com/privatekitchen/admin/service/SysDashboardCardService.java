package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.SysDashboardCard;

import java.util.List;

public interface SysDashboardCardService {

    List<SysDashboardCard> listCards();

    List<SysDashboardCard> listEnabledCards();

    SysDashboardCard createCard(SysDashboardCard card);

    SysDashboardCard updateCard(Long id, SysDashboardCard card);

    void deleteCard(Long id);
}
