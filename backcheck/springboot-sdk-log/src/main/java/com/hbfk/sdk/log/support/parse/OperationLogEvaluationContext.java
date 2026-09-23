package com.hbfk.sdk.log.support.parse;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;

/**
* @Description: OperationLogEvaluation 上下文 spEl
* @Author: 61
*/
public class OperationLogEvaluationContext extends MethodBasedEvaluationContext {

    public OperationLogEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                         ParameterNameDiscoverer parameterNameDiscoverer, Object ret, String errorMsg) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
        setVariable("_RET", ret);
        setVariable("_ERR_MSG", errorMsg);
    }
}
