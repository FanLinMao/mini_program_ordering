package com.privatekitchen.admin.miniapp.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MiniappCategoryVO {

    private String id;

    private String name;

    private Integer sort;

    private List<MiniappDishVO> dishes = new ArrayList<>();
}
