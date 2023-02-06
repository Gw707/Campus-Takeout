package com.gw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gw.common.Status;
import com.gw.pojo.ShoppingCart;
import com.gw.pojo.User;
import com.gw.service.ShoppingCartService;
import com.gw.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/30 10:31
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Status<List<ShoppingCart>> list(HttpSession session){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, (Long) session.getAttribute("user"));
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);

        return Status.success(shoppingCarts);
    }

    /**
     * 点击add后显示出菜品的口味，之后再加入购物车
     * @return
     */
    @PostMapping("/add")
    public Status<String> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        log.info(shoppingCart.toString());
        Long userId = (Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);
        shoppingCart.setCreateTime(LocalDateTime.now());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getId, shoppingCart.getId());
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(queryWrapper);
        if(shoppingCart1 == null){
            shoppingCart.setNumber(Integer.valueOf(1));
            shoppingCartService.save(shoppingCart);
        }else{
            shoppingCart1.setNumber(shoppingCart1.getNumber() + Integer.valueOf(1));
            shoppingCartService.updateById(shoppingCart1);
        }

        return Status.success("添加成功");
    }

    @PostMapping("/sub")
    public Status<String> sub(@RequestBody Map map, HttpSession session){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, session.getAttribute("user"));
        if(map.get("setmealId") == null){
            queryWrapper.eq(ShoppingCart::getDishId, map.get("dishId"));
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId, map.get("setmealId"));
        }

        ShoppingCart shoppingCart = shoppingCartService.getOne(queryWrapper);

        Integer number = shoppingCart.getNumber();
        if(number.equals(Integer.valueOf(1))){
            shoppingCartService.removeById(shoppingCart.getId());
        }else{
            shoppingCart.setNumber(shoppingCart.getNumber() - Integer.valueOf(1));
            shoppingCartService.updateById(shoppingCart);
        }
        return Status.success("sub成功");
    }

    @DeleteMapping("/clean")
    public Status<String> clean(HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartService.remove(queryWrapper);

        return Status.success("清空成功");
    }

}
