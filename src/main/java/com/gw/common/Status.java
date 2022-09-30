package com.gw.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class Status<T>{

    private Integer code;

    private T data;

    private String msg;

    private Map<Object, Object> map = new HashMap();


    public static <T> Status<T> success(T data){

        Status<T> tStatus = new Status<T>();

        tStatus.setCode(1);
        tStatus.setData(data);

        return tStatus;

    }

    public static <T> Status<T> error(String msg){

        Status tStatus = new Status();

        tStatus.setCode(0);
        tStatus.setMsg(msg);

        return tStatus;

    }

    public Status<T> add(String key, String value){
        map.put(key ,value);
        return this;
    }





}
