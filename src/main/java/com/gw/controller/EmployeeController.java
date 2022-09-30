package com.gw.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gw.common.Status;
import com.gw.mapper.EmployeeMapper;
import com.gw.pojo.Employee;
import com.gw.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public Status<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee){

        System.out.println(employee);
        //1.将密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());

        Employee emp = employeeService.getOne(queryWrapper);

        //3.如果没有检查到查询则登陆失败

        if(emp == null){
            return Status.error("无此用户，登录失败");
        }

        //4.密码错误，登录失败
        if(!emp.getPassword().equals(password)){
            return Status.error("密码错误，无此用户");
        }

        //5.检验员工状态是否可登录
        if(emp.getStatus() == 0){
            return Status.error("账号已禁用");
        }

        //6.检验通过，可以登录
        //将员工id放至session中
        httpServletRequest.getSession().setAttribute("employee", emp.getId());

        return Status.success(emp);
    }

    @PostMapping("/logout")
    public Status<String> logout(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().removeAttribute("employee");
        return Status.success("退出成功");
    }

    @PostMapping
    public Status<String> save(@RequestBody Employee employee, HttpServletRequest request){

        log.info("获取的员工信息为{}", employee.toString());

        //设置初始密码，使用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        employee.setCreateUser((Long)request.getSession().getAttribute("employee"));
        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));

        employeeService.save(employee);

        return Status.success("新增员工成功");
    }

    @GetMapping("/{id}")
    public Status<Employee> getById(HttpServletRequest request,@PathVariable("id") Long empId){


        Employee employee = employeeService.getById(empId);

        if(employee != null){
            return Status.success(employee);
        }else{
            return Status.error("获取失败");
        }

    }
    /**
     * 分页查询起始页，页面大小，以及有可能的name查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Status<Page> page(int page, int pageSize, String name){
        log.info("page{}, pageSize{}, name{}",page, pageSize, name);

        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);

        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo, queryWrapper);

        return Status.success(pageInfo);
    }


    @PutMapping
    public Status<String> updateStatus(HttpServletRequest request, @RequestBody Employee employee){

        log.info(employee.toString());

        Long curId = (Long) request.getSession().getAttribute("employee");

//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(curId);

        employeeService.updateById(employee);

        log.info(employee.toString());
        return Status.success("修改成功！");
    }

}
