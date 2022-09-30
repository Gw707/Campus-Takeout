package com.gw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gw.dto.DishDto;
import com.gw.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入口味数据
     * 涉及到对两张表的操作，封装在一个service中
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 更新菜品及口味信息
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);

    public void deleteWithFlavor(List<Long> ids);

    public Page pageWithFlavor(int page, int pageSize, String name);
}
