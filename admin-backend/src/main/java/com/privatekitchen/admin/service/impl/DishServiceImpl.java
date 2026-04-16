package com.privatekitchen.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.privatekitchen.admin.dao.DishDao;
import com.privatekitchen.admin.entity.Dish;
import com.privatekitchen.admin.service.DishService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    private final DishDao dishDao;

    public DishServiceImpl(DishDao dishDao) {
        this.dishDao = dishDao;
    }

    @Override
    public List<Dish> listDishes() {
        return dishDao.selectList(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getDelFlag, 0)
                .orderByAsc(Dish::getSort)
                .orderByAsc(Dish::getId));
    }

    @Override
    public Dish saveDish(Dish dish) {
        LocalDateTime now = LocalDateTime.now();
        dish.setDelFlag(0);
        dish.setCreateTime(now);
        dish.setUpdateTime(now);
        dish.setCreateBy("admin");
        dish.setUpdateBy("admin");
        dishDao.insert(dish);
        return dish;
    }

    @Override
    public Dish updateDish(Long id, Dish dish) {
        Dish existing = dishDao.selectById(id);
        existing.setCategoryId(dish.getCategoryId());
        existing.setName(dish.getName());
        existing.setPrice(dish.getPrice());
        existing.setImage(dish.getImage());
        existing.setMonthlySales(dish.getMonthlySales());
        existing.setPraise(dish.getPraise());
        existing.setExtra(dish.getExtra());
        existing.setSort(dish.getSort());
        existing.setStatus(dish.getStatus());
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        dishDao.updateById(existing);
        return existing;
    }

    @Override
    public void deleteDish(Long id) {
        Dish existing = dishDao.selectById(id);
        if (existing == null) {
            return;
        }
        existing.setDelFlag(1);
        existing.setUpdateTime(LocalDateTime.now());
        existing.setUpdateBy("admin");
        dishDao.updateById(existing);
    }
}
