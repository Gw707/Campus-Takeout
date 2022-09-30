package com.gw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.common.Status;
import com.gw.dto.DishDto;
import com.gw.mapper.DishMapper;
import com.gw.pojo.Category;
import com.gw.pojo.Dish;
import com.gw.pojo.DishFlavor;
import com.gw.service.CategoryService;
import com.gw.service.DishFlavorService;
import com.gw.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/16 16:00
 * @Version 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品，同时插入口味数据
     * 涉及到对两张表的操作，封装在一个service中
     * 由于用到两张表，需要加入事务支持@Transactional
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {

        this.save(dishDto);
        Long dishId = dishDto.getId();
        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(dishId);
        }
        dishFlavorService.saveBatch(dishDto.getFlavors());

    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        updateById(dishDto);

        //更新flavor表
        //清理当前dishId对应的flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //插入dishId对应的新flavor
        List<DishFlavor> flavors = dishDto.getFlavors();
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    @Transactional
    public void deleteWithFlavor(List<Long> ids) {
        removeByIds(ids);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();

        for (Long id : ids) {
            queryWrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(queryWrapper);
        }
    }

    @Override
    @Transactional
    public Page pageWithFlavor(int page, int pageSize, String name) {
        /**
         * 分页操作
         */
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> pageDto = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        /**
         * 这里写的有问题！！！！！
         * 没有问题，这是service层，本身就有page方法
         */
        page(dishPage, queryWrapper);

        /**
         * category的name未知，通过dto拓展属性进行连表
         * 进行对象拷贝, 但是records属性不用拷贝
         */
        BeanUtils.copyProperties(dishPage, pageDto, "records");

        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map((dish)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());
            //log.info(dishDto.toString());
            return dishDto;
        }).collect(Collectors.toList());

        pageDto.setRecords(list);
        return pageDto;
    }


}
