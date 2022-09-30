package com.gw.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName MyMetaObjectHandler
 * @Description TODO
 * @Author 86188
 * @Date 2022/9/16 10:59
 * @Version 1.0
 */

/**
 * 在这个类中指定mybatis-plus自动填充的策略
 * 注：需要实现MetaObjectHandler接口
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * insert时自动填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insertfill");
        /**
         * 对元数据进行修改、添加信息
         */
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        /**
         * 需要通过ThreadLocal获取当前操作对象的id，ThreadLocal对仅对当前线程/请求有效，线程间隔离
         * ThreadLocal中提供了set、get方法，我们可以在doFilter方法中将id进行set，在此处get
         * 请求(线程)执行顺序：filter->controller->update、insert->metaObject->...
         */
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

        log.info(metaObject.toString());
    }

    /**
     * update时自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("updatefill");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
