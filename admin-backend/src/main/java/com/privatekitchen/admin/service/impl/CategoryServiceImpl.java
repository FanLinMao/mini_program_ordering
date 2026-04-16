package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.CategoryDao;
import com.privatekitchen.admin.entity.Category;
import com.privatekitchen.admin.service.CategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> listCategories() {
        return categoryDao.selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getDelFlag, 0)
                .orderByAsc(Category::getSort)
                .orderByAsc(Category::getId));
    }

    @Override
    public Category saveCategory(Category category) {
        LocalDateTime now = LocalDateTime.now();
        category.setDelFlag(0);
        category.setCreateTime(now);
        category.setUpdateTime(now);
        category.setCreateBy("admin");
        category.setUpdateBy("admin");
        categoryDao.insert(category);
        return category;
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existing = categoryDao.selectById(id);
        existing.setName(category.getName());
        existing.setSort(category.getSort());
        existing.setStatus(category.getStatus());
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        categoryDao.updateById(existing);
        return existing;
    }

    @Override
    public void deleteCategory(Long id) {
        Category existing = categoryDao.selectById(id);
        if (existing == null) {
            return;
        }
        existing.setDelFlag(1);
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        categoryDao.updateById(existing);
    }
}
