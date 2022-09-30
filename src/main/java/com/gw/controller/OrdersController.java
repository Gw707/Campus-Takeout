package com.gw.controller;

import com.gw.common.Status;
import com.gw.pojo.Orders;
import com.gw.service.OrderDetailService;
import com.gw.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    @PostMapping("/submit")
    public Status<String> submit(@RequestBody Orders orders, HttpSession session){
        Long userId = (Long) session.getAttribute("user");

        return Status.success("提交成功");
    }

}
