package com.gw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.dto.DishDto;
import com.gw.dto.SetmealDto;
import com.gw.mapper.SetmealMapper;
import com.gw.pojo.*;
import com.gw.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/16 16:01
 * @Version 1.0
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private DishFlavorService dishFlavorService;


    @Autowired
    private DishService dishService;

    @Override
    @Transactional
    public Page<SetmealDto> pageWithSetmeal(int page, int pageSize, String name) {

        Page<Setmeal> res = new Page<>(page, pageSize);
        Page<SetmealDto> tar = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        page(res, queryWrapper);
        BeanUtils.copyProperties(res, tar, "records");

        List<Setmeal> list = list(queryWrapper);
        List<SetmealDto> ans = list.stream().map((item) -> {
            Category categoryName = categoryService.getById(item.getCategoryId());
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(categoryName.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        tar.setRecords(ans);

        return tar;
    }

    @Override
    public void updateStatus(int status, List<Long> ids) {


        List<Setmeal> setmeals = listByIds(ids);
        List<Setmeal> list = setmeals.stream().map((item) -> {
            item.setStatus(Integer.valueOf(status));
            return item;
        }).collect(Collectors.toList());
        updateBatchById(list);

    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {

        LambdaQueryWrapper<SetmealDish> queryWrapperDish = new LambdaQueryWrapper<>();
        for (Long id : ids) {
            queryWrapperDish.eq(SetmealDish::getSetmealId, id);
            setmealDishService.remove(queryWrapperDish);
        }
        removeByIds(ids);

    }

    /**
     * 一定一定要注意起名字的问题，这里的名字与service里一个函数重名
     * 导致调另一个函数时一直递归，栈溢出
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateById(SetmealDto setmealDto) {
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getId, setmealDto.getId());
        this.update(setmealDto, queryWrapper1);
//        ArrayList<Long> ids = new ArrayList<>();
//        ids.add(setmealDto.getId());
//        deleteByIds(ids);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        List<SetmealDish> setmealDishList = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishList);
//        save(setmealDto);

    }



}
