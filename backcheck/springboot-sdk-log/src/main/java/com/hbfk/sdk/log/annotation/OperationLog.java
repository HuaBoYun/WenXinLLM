package com.hbfk.sdk.log.annotation;

import com.hbfk.sdk.log.enums.OperationType;

import java.lang.annotation.*;

/**
* @Description: 操作日志注解
 * 默认变量
 * _RET:     返回值。   eg: {_RET{str}}
 * _ERR_MSG: 异常信息。 eg: {_ERR_MSG{str}}
 * 支持spEL的字段：success、fail、busNo、extra
* @Author: 61
*/
@Repeatable(OperationLogs.class)
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OperationLog {
    /**
     * @return 方法执行成功后的日志模版
     */
    String success();
    /**
     * @return 方法执行失败后的日志模版 默认异常信息
     */
    String fail() default "{_SUB200{#_ERR_MSG}}";
    /**
     * @return 业务类型。模块自定义
     */
    String busType();
    /**
     * @return 操作类型。见枚举类OperationType
     */
    OperationType operationType() default OperationType.DEFAULT;
    /**
     * @return 子类型：同一个操作可以区分用户操作说明、数据操作说明。
     */
    String subType() default "";
    /**
     * @return 业务表示唯一编号（业务中唯一）
     */
    String busNo() default "";
    /**
     * @return 扩展信息
     */
    String extra() default "";

}
