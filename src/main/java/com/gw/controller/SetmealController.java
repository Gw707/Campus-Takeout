package com.gw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.Status;
import com.gw.dto.DishDto;
import com.gw.dto.SetmealDto;
import com.gw.pojo.*;
import com.gw.service.CategoryService;
import com.gw.service.DishService;
import com.gw.service.SetmealDishService;
import com.gw.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 套餐管理
 * @Author ygw
 * @Date 2022/9/22 16:20
 * @Version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Status<String> save(@RequestBody SetmealDto setmealDto){

        /**
         * mybatis-plus 会自动注入id，再将数据放至数据库中
         */
//        log.info("setmeal的id为：{}",setmealDto.getId());
        setmealService.save(setmealDto);
//        log.info("setmeal的id为：{}",setmealDto.getId());
        for (SetmealDish setmealDish : setmealDto.getSetmealDishes()) {
//            log.info("setmeal的id为：{}",setmealDto.getId());
            setmealDish.setSetmealId(setmealDto.getId());
        }

        setmealDishService.saveBatch(setmealDto.getSetmealDishes());

        return Status.success("保存成功");
    }

    /**
     * 获取修改时的数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Status<SetmealDto> getById(@PathVariable Long id){

        Setmeal setmeal = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal, setmealDto);

        Category category = categoryService.getById(setmealDto.getCategoryId());
        setmealDto.setCategoryName(category.getName());

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);


        return Status.success(setmealDto);
    }

    /**
     * 保存对套餐的修改
     * 先删除原有的套餐信息，再添加一个新的套餐信息
     */
    @PutMapping
    public Status<String> updateById(@RequestBody SetmealDto setmealDto){

        /**
         * 注意使用dto接收前端数据时，toString方法显示出来的继承的结果为空
         * 但实际上是有的，直接用就行了
         */

        setmealService.updateById(setmealDto);


        return Status.success("修改成功");
    }


    /**
     * 分页查询
     */

    @GetMapping("/page")
    public Status<Page> page(int page, int pageSize, String name){
        Page<SetmealDto> setmealPage = setmealService.pageWithSetmeal(page, pageSize, name);
        return Status.success(setmealPage);
    }

    @PostMapping("/status/{status}")
    public Status<String> updateStatus(@PathVariable int status, @RequestParam("ids") List<Long> ids){

        for (Long id : ids) {
            log.info("id为{}",id);
        }

        setmealService.updateStatus(status, ids);
        return Status.success("修改成功");
    }

    /**
     * setmeal在删除时，还需要删除在setmeal_dish中的内容
     * @param ids
     * @return
     */
    @DeleteMapping
    public Status<String> deleteByIds(@RequestParam List<Long> ids){

        setmealService.deleteByIds(ids);

        return Status.success("删除成功");
    }

    @GetMapping("/list")
    public Status<List<Setmeal>> getByCategory(@RequestParam("categoryId") Long categoryId,
                                               @RequestParam("status") int status){


        if(status == 0) return Status.error("该品种商品已停售");

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId);
        List<Setmeal> setmeals = setmealService.list(queryWrapper);
        return Status.success(setmeals);

    }

}
