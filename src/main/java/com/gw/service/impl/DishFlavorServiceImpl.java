package com.gw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.mapper.DishFlavorMapper;
import com.gw.mapper.DishMapper;
import com.gw.pojo.DishFlavor;
import com.gw.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/17 11:02
 * @Version 1.0
 */

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
