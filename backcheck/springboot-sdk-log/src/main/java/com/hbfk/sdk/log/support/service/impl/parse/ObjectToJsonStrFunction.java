package com.hbfk.sdk.log.support.service.impl.parse;


import cn.hutool.json.JSONUtil;
import com.hbfk.sdk.log.support.service.IParseFunction;
import org.springframework.stereotype.Component;

/**
* @Description: object转json字符串 解析函数实现。
 * 使用方式：{_DEFAULT{str or spEl对象表达式（#obj.id..）}}
* @Author: 61
*/
@Component
public class ObjectToJsonStrFunction implements IParseFunction {
    final String FUNCTION_NAME="_JSON";
    @Override
    public String functionName() {
        return FUNCTION_NAME;
    }

    @Override
    public String apply(Object value) {
        return JSONUtil.toJsonStr(value);
    }

}
