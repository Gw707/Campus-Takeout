package com.gw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.gw.pojo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/30 15:50
 * @Version 1.0
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
