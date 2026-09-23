package com.hbfk.sdk.log.support.service.impl.parse;


import com.hbfk.sdk.log.support.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
* @Description: 取前200个字符。
 * 使用方式：{_SUB200{str or spEl对象表达式（#obj.id..）}}
* @Author: 61
*/
@Component
public class StrSubFunction implements IParseFunction {
    final String FUNCTION_NAME="_SUB200";
    @Override
    public String functionName() {
        return FUNCTION_NAME;
    }

    @Override
    public String apply(Object value) {
        String valueOf = String.valueOf(value);
        return (valueOf.length()>200) ? valueOf.substring(0,200):valueOf;
    }
}
