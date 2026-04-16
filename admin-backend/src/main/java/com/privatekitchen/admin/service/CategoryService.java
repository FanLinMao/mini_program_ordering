package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> listCategories();

    Category saveCategory(Category category);

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);
}
