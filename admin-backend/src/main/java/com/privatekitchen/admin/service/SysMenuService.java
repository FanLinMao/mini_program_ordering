package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.SysMenu;

import java.util.List;

public interface SysMenuService {

    List<SysMenu> listMenuTree();

    List<SysMenu> listSidebarMenus();

    SysMenu createMenu(SysMenu menu);

    SysMenu updateMenu(Long id, SysMenu menu);

    void deleteMenu(Long id);
}
