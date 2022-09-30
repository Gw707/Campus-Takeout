package com.gw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.Status;
import com.gw.dto.DishDto;
import com.gw.pojo.Category;
import com.gw.pojo.Dish;
import com.gw.pojo.DishFlavor;
import com.gw.service.CategoryService;
import com.gw.service.DishFlavorService;
import com.gw.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/17 11:04
 * @Version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public Status<String> save(@RequestBody DishDto dishDto){
        //log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return Status.success("新增成功");
    }

    @PutMapping
    public Status<String> updateById(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);

        return Status.success("修改成功");
    }

    @DeleteMapping
    public Status<String> delete(@RequestParam("ids") List<Long> ids){

        dishService.deleteWithFlavor(ids);

        return Status.success("删除成功");
    }

    @GetMapping("/{id}")
    public Status<DishDto> getById(HttpServletRequest request, @PathVariable Long id){

        DishDto dishDto = new DishDto();

        Dish dish = dishService.getById(id);
        BeanUtils.copyProperties(dish, dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);

        return Status.success(dishDto);
    }


    /**
     * 设计多个service的操作时，最好在service层中自定义一个事务@Transactional进行封装
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Status<Page> page(int page, int pageSize, String name){

        Page pageDto = dishService.pageWithFlavor(page, pageSize, name);

        return Status.success(pageDto);
    }

    @PostMapping("/status/{sta}")
    public Status<String> updateStatus(@PathVariable int sta, @RequestParam("ids") List<Long> id){
        List<Dish> dishes = dishService.listByIds(id);
        for (Dish curDish : dishes) {
            curDish.setStatus(sta);
            dishService.updateById(curDish);
        }

        return Status.success("菜品状态修改成功");
    }


    /**
     * 根据种类查询具体的菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public Status<List<DishDto>> getDishByCategory(Dish dish){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);


        List<Dish> dishes = dishService.list(queryWrapper);
        List<DishDto> list = dishes.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(queryWrapper1);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());


        return Status.success(list);
    }

}
