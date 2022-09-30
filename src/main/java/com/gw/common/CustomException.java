package com.gw.common;

/**
 * @Description 自定义业务异常类
 * @Author ygw
 * @Date 2022/9/16 16:28
 * @Version 1.0
 */
public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }

}
