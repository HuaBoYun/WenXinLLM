package com.hbfk.sdk.log.annotation;


import com.hbfk.sdk.log.support.OperationLogConfigureSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
* @Description:
* @Author: 61
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OperationLogConfigureSelector.class)
public @interface EnableOperationLog {

    String module();

    boolean proxyTargetClass() default false;

    AdviceMode mode() default AdviceMode.PROXY;

}
