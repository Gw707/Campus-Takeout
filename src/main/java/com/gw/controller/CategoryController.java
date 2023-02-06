package com.gw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.Status;
import com.gw.pojo.Category;
import com.gw.service.CategoryService;
import com.gw.service.impl.CategoryServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName CategoryController
 * @Description 对菜品种类和套餐种类的管理
 * @Author ygw
 * @Date 2022/9/16 14:58
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Status<String> save(HttpServletRequest request, @RequestBody Category category){

        log.info(category.toString());

        categoryService.save(category);

        return Status.success("添加成功");
    }

    @PutMapping
    public Status<String> update(@RequestBody Category category){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, category.getId());

        categoryService.update(category, queryWrapper);

        return Status.success("修改成功");
    }

    @GetMapping("/page")
    public Status<Page> page(int page, int pageSize){

        log.info("page:{} pageSize:{}", page, pageSize);
        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getUpdateTime);

        categoryService.page(pageInfo, queryWrapper);

        return Status.success(pageInfo);
    }

    @DeleteMapping
    public Status<String> removeById(@RequestParam("ids") Long id){

        log.info(id.toString());

        categoryService.remove(id);

        return Status.success("删除成功");
    }

    @GetMapping("/list")
    public Status<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());

        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        list.toString();

        return Status.success(list);
    }

}
