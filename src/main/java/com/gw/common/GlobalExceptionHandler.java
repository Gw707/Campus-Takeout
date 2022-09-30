package com.gw.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 对sql异常的处理，重复插入相同的用户
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Status<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        String msg = null;
        if(ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            msg = s[2] + "已存在，添加失败";
        }else{
            msg = "未知错误";
        }
        return Status.error(msg);
    }

    /**
     * 删除异常，菜品分类中还包含菜品就进行了删除
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Status<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return Status.error(ex.getMessage());
    }
}
