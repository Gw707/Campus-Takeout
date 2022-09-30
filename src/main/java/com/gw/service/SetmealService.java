package com.gw.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gw.dto.DishDto;
import com.gw.dto.SetmealDto;
import com.gw.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    public Page<SetmealDto> pageWithSetmeal(int page, int pageSize, String name);

    public void updateStatus(int status, List<Long> ids);

    public void deleteByIds(List<Long> ids);

    public void updateById(SetmealDto setmealDto);

}
