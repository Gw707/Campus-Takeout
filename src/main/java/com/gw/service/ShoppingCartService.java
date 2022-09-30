package com.gw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gw.mapper.ShoppingCartMapper;
import com.gw.pojo.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    public List<ShoppingCart> list(Long userId);
}
