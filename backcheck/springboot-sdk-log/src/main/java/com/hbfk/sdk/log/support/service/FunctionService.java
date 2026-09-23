package com.hbfk.sdk.log.support.service;


import com.hbfk.sdk.log.support.service.impl.ParseFunctionFactory;

/**
* @Description: 函数处理
* @Author: 61
*/
public class FunctionService {

    private final ParseFunctionFactory parseFunctionFactory;

    public FunctionService(ParseFunctionFactory parseFunctionFactory) {
        this.parseFunctionFactory = parseFunctionFactory;
    }

    public String apply(String functionName, Object value) {
        IParseFunction function = parseFunctionFactory.getFunction(functionName);
        if (function == null) {
            return value.toString();
        }
        return function.apply(value);
    }

}
