package com.gw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.common.CustomException;
import com.gw.mapper.CategoryMapper;
import com.gw.pojo.Category;
import com.gw.pojo.Dish;
import com.gw.pojo.Setmeal;
import com.gw.service.CategoryService;
import com.gw.service.DishService;
import com.gw.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName CategoryServiceImpl
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/16 14:57
 * @Version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    /**
     * 注意：删除属于一个事务，关于删除的一些约束不应该放在Controller层
     * @param id
     */

    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果关联了菜品，则抛出一个业务异常
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(queryWrapper);
        if(count > 0){
            //抛出一个业务异常类
            throw new CustomException("当前分类下包含了菜品，不能进行删除");

        }

        //查询当前分类是否关联了套餐，如果关联了则抛出异常
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(queryWrapper1);
        if(count1 > 0){
            //抛出异常
            throw new CustomException("当前分类下关联了套餐， 不能进行删除");
        }

        //如果都没有关联，则执行正常的删除
        super.removeById(id);
    }
}
