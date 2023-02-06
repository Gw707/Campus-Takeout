package com.gw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gw.mapper.UserMapper;
import com.gw.pojo.User;
import com.gw.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/28 16:26
 * @Version 1.0
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
