package com.gw.common;

/**
 * @ClassName BaseContext
 * @Description 基于ThreadLocal封装的工具类
 * @Author ygw
 * @Date 2022/9/16 11:39
 * @Version 1.0
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
