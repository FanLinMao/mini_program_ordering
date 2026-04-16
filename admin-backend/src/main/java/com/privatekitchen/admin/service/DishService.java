package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.Dish;

import java.util.List;

public interface DishService {

    List<Dish> listDishes();

    Dish saveDish(Dish dish);

    Dish updateDish(Long id, Dish dish);

    void deleteDish(Long id);
}
