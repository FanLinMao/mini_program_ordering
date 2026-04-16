package com.privatekitchen.admin.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.CategoryDao;
import com.privatekitchen.admin.dao.DishDao;
import com.privatekitchen.admin.entity.Category;
import com.privatekitchen.admin.entity.Dish;
import com.privatekitchen.admin.miniapp.service.MiniappMenuService;
import com.privatekitchen.admin.miniapp.vo.MiniappCategoryVO;
import com.privatekitchen.admin.miniapp.vo.MiniappDishVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MiniappMenuServiceImpl implements MiniappMenuService {

    private final CategoryDao categoryDao;
    private final DishDao dishDao;

    public MiniappMenuServiceImpl(CategoryDao categoryDao, DishDao dishDao) {
        this.categoryDao = categoryDao;
        this.dishDao = dishDao;
    }

    @Override
    public List<MiniappCategoryVO> listMenuCategories() {
        List<Category> categories = categoryDao.selectList(new LambdaQueryWrapper<Category>()
                .eq(Category::getDelFlag, 0)
                .eq(Category::getStatus, 1)
                .orderByAsc(Category::getSort)
                .orderByAsc(Category::getId));

        List<Dish> dishes = dishDao.selectList(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getDelFlag, 0)
                .eq(Dish::getStatus, 1)
                .orderByAsc(Dish::getSort)
                .orderByAsc(Dish::getId));

        Map<Long, List<MiniappDishVO>> dishMap = dishes.stream()
                .collect(Collectors.groupingBy(
                        Dish::getCategoryId,
                        Collectors.mapping(this::toDishVO, Collectors.toList())
                ));

        return categories.stream()
                .map(category -> toCategoryVO(category, dishMap.get(category.getId())))
                .collect(Collectors.toList());
    }

    private MiniappCategoryVO toCategoryVO(Category category, List<MiniappDishVO> dishes) {
        MiniappCategoryVO result = new MiniappCategoryVO();
        result.setId(String.valueOf(category.getId()));
        result.setName(category.getName());
        result.setSort(category.getSort());
        if (dishes != null) {
            result.setDishes(dishes);
        }
        return result;
    }

    private MiniappDishVO toDishVO(Dish dish) {
        MiniappDishVO result = new MiniappDishVO();
        result.setId(String.valueOf(dish.getId()));
        result.setCategoryId(String.valueOf(dish.getCategoryId()));
        result.setName(dish.getName());
        result.setPrice(dish.getPrice());
        result.setImage(dish.getImage());
        result.setMonthlySales(dish.getMonthlySales());
        result.setPraise(dish.getPraise());
        result.setExtra(dish.getExtra());
        return result;
    }
}
