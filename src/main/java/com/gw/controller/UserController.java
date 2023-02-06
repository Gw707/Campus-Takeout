package com.gw.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gw.common.Status;
import com.gw.pojo.User;
import com.gw.service.UserService;
import com.gw.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author ygw
 * @Date 2022/9/28 16:27
 * @Version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     *这个方法此处未进行使用，
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public Status<String> getCode(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        String code = String.valueOf( ValidateCodeUtils.generateValidateCode(4));
        log.info("获取的验证码为：{}", code);
        if(!StringUtils.isEmpty(phone)){

            session.setAttribute("code", code);

            return Status.success("发送成功");
        }
        return Status.error("发送失败");
    }

    /**
     * 前端传入手机号和验证码进行验证登录
     *
     * 从前端传入的验证码，和session中的验证码进行对比
     * 如果一致则登录成功
     *
     * 如果手机号为新用户自动完成注册
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public Status<String> login(@RequestBody Map map, HttpSession session){
        log.info("获取的电话号码为：{}", map.get("phone"));
        log.info("获取的电话号码为：{}", map.get("code"));
        /**
         * 真实场景下map中放的是phone和code，code需要和session中的code进行验证
         */
        Object code = session.getAttribute("code");
        if(!code.equals(map.get("code"))) return Status.error("验证码错误");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, map.get("phone"));
        List<User> list = userService.list(queryWrapper);

        if(list.isEmpty()){
            User user = new User();
            user.setPhone((String) map.get("phone"));
            user.setStatus(Integer.valueOf(1));
            userService.save(user);
            session.setAttribute("user", user.getId());

        }else{
            session.setAttribute("user", list.get(0).getId());
        }

        return Status.success("登录成功");
    }

    @PostMapping("/loginout")
    public Status<String> logout(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().removeAttribute("user");
        return Status.success("退出成功");
    }



}
