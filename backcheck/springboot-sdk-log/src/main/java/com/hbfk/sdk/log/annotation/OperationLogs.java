package com.hbfk.sdk.log.annotation;

import java.lang.annotation.*;

/**
* @Description: OperationLog数组
* @Author: 61
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OperationLogs {
    OperationLog[] value();
}
