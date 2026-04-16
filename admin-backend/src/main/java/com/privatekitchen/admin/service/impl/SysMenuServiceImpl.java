package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.SysMenuDao;
import com.privatekitchen.admin.entity.SysMenu;
import com.privatekitchen.admin.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuDao sysMenuDao;

    public SysMenuServiceImpl(SysMenuDao sysMenuDao) {
        this.sysMenuDao = sysMenuDao;
    }

    @Override
    public List<SysMenu> listMenuTree() {
        return buildTree(sysMenuDao.selectList(baseQuery()));
    }

    @Override
    public List<SysMenu> listSidebarMenus() {
        List<SysMenu> allMenus = sysMenuDao.selectList(baseQuery());
        return filterEnabledMenus(buildTree(allMenus));
    }

    @Override
    public SysMenu createMenu(SysMenu menu) {
        LocalDateTime now = LocalDateTime.now();
        menu.setParentId(menu.getParentId() == null ? 0L : menu.getParentId());
        menu.setType(defaultText(menu.getType(), menu.getParentId() == 0 ? "group" : "menu"));
        menu.setStatus(menu.getStatus() == null ? 1 : menu.getStatus());
        menu.setSort(menu.getSort() == null ? 0 : menu.getSort());
        menu.setDelFlag(0);
        menu.setCreateTime(now);
        menu.setCreateBy("admin");
        menu.setUpdateTime(now);
        menu.setUpdateBy("admin");
        sysMenuDao.insert(menu);
        return menu;
    }

    @Override
    public SysMenu updateMenu(Long id, SysMenu menu) {
        SysMenu existing = sysMenuDao.selectById(id);
        if (existing == null || Integer.valueOf(1).equals(existing.getDelFlag())) {
            return null;
        }
        existing.setParentId(menu.getParentId() == null ? existing.getParentId() : menu.getParentId());
        existing.setName(menu.getName());
        existing.setTitle(menu.getTitle());
        existing.setPath(menu.getPath());
        existing.setComponent(menu.getComponent());
        existing.setIcon(menu.getIcon());
        existing.setType(defaultText(menu.getType(), existing.getType()));
        existing.setStatus(menu.getStatus() == null ? existing.getStatus() : menu.getStatus());
        existing.setSort(menu.getSort() == null ? existing.getSort() : menu.getSort());
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        sysMenuDao.updateById(existing);
        return existing;
    }

    @Override
    public void deleteMenu(Long id) {
        List<SysMenu> menus = sysMenuDao.selectList(baseQuery());
        List<Long> deleteIds = new ArrayList<>();
        collectChildrenIds(id, menus, deleteIds);
        deleteIds.add(id);

        LocalDateTime now = LocalDateTime.now();
        for (Long deleteId : deleteIds) {
            SysMenu existing = sysMenuDao.selectById(deleteId);
            if (existing != null && !Integer.valueOf(1).equals(existing.getDelFlag())) {
                existing.setDelFlag(1);
                existing.setUpdateTime(now);
                existing.setUpdateBy("admin");
                sysMenuDao.updateById(existing);
            }
        }
    }

    private LambdaQueryWrapper<SysMenu> baseQuery() {
        return new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getDelFlag, 0)
                .orderByAsc(SysMenu::getSort)
                .orderByAsc(SysMenu::getCreateTime);
    }

    private List<SysMenu> buildTree(List<SysMenu> allMenus) {
        Map<Long, SysMenu> menuMap = allMenus.stream()
                .peek(menu -> menu.setChildren(new ArrayList<>()))
                .collect(Collectors.toMap(SysMenu::getId, Function.identity()));

        List<SysMenu> roots = new ArrayList<>();
        for (SysMenu menu : allMenus) {
            Long parentId = menu.getParentId() == null ? 0L : menu.getParentId();
            if (parentId == 0L || !menuMap.containsKey(parentId)) {
                roots.add(menu);
            } else {
                menuMap.get(parentId).getChildren().add(menu);
            }
        }

        sortTree(roots);
        return roots;
    }

    private void sortTree(List<SysMenu> menus) {
        menus.sort(Comparator.comparing(SysMenu::getSort, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(SysMenu::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder())));
        for (SysMenu menu : menus) {
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                sortTree(menu.getChildren());
            }
        }
    }

    private List<SysMenu> filterEnabledMenus(List<SysMenu> menus) {
        List<SysMenu> enabledMenus = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (!Integer.valueOf(1).equals(menu.getStatus())) {
                continue;
            }
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                menu.setChildren(filterEnabledMenus(menu.getChildren()));
            }
            enabledMenus.add(menu);
        }
        return enabledMenus;
    }

    private void collectChildrenIds(Long parentId, List<SysMenu> menus, List<Long> deleteIds) {
        for (SysMenu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                deleteIds.add(menu.getId());
                collectChildrenIds(menu.getId(), menus, deleteIds);
            }
        }
    }

    private String defaultText(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
