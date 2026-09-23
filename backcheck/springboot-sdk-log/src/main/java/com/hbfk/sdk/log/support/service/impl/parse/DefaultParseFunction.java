package com.hbfk.sdk.log.support.service.impl.parse;


import com.hbfk.sdk.log.support.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
* @Description: 默认解析函数实现。自定义可以实现IParseFunction去实现自己的函数
 * 功能 前面拼接 _DEFAULT
 * 使用方式：{_DEFAULT{str or spEl对象表达式（#obj.id..）}}
* @Author: 61
*/
@Component
public class DefaultParseFunction implements IParseFunction {
    final String FUNCTION_NAME="_DEFAULT";
    @Override
    public String functionName() {
        return FUNCTION_NAME;
    }

    @Override
    public String apply(Object value) {
        return FUNCTION_NAME+":"+value;
    }
}
