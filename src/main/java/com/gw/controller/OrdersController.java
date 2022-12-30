package com.gw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.BaseContext;
import com.gw.common.Status;
import com.gw.dto.OrdersDto;
import com.gw.pojo.AddressBook;
import com.gw.pojo.OrderDetail;
import com.gw.pojo.Orders;
import com.gw.pojo.User;
import com.gw.service.AddressBookService;
import com.gw.service.OrderDetailService;
import com.gw.service.OrdersService;
import com.gw.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/30 15:49
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private UserService userService;

    @PostMapping("/submit")
    public Status<String> submit(@RequestBody OrdersDto orders, HttpSession session){
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        Long orderId = System.currentTimeMillis();
        System.out.println(orders.toString());
        System.out.println(orders.getAddressBookId());
        for (OrderDetail orderDetail : orders.getOrderDetails()) {
            orderDetail.setOrderId(orderId);
            orderDetailService.save(orderDetail);
        }
        Orders order = new Orders();
        BeanUtils.copyProperties(orders, order);
        order.setAddress(addressBook.getDetail());
        order.setNumber(orderId.toString());  //orderId -- number
        order.setPhone(addressBook.getPhone());
        order.setOrderTime(LocalDateTime.now());
        order.setUserId(BaseContext.getCurrentId());
        User user = userService.getById(BaseContext.getCurrentId());
        order.setUserName(user.getName());
        order.setCheckoutTime(LocalDateTime.now());
        order.setAmount(BigDecimal.ONE);
        System.out.println(order.toString());


        ordersService.save(order);

        return Status.success("提交成功");
    }

    @GetMapping("/page")
    public Status<Page> OrderPage(int page, int pageSize, Long orderId){
        Page res = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if (orderId != null){
            queryWrapper.eq(Orders::getId, orderId);
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(res, queryWrapper);
        return Status.success(res);
    }
    @GetMapping("/userPage")
    public Status<Page> userPage(int page, int pageSize, Long orderId){
        Page res = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if (orderId != null){
            queryWrapper.eq(Orders::getId, orderId);
        }
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(res, queryWrapper);
        return Status.success(res);
    }

}
