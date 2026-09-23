package com.hbfk.sdk.log.support.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hbfk.sdk.log.support.service.IParseFunction;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* @Description: 函数解析工厂类
* @Author: 61
*/
public class ParseFunctionFactory {
    private Map<String, IParseFunction> allFunctionMap;

    public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
        if (CollectionUtils.isEmpty(parseFunctions)) {
            return;
        }
        allFunctionMap = new HashMap<>();
        for (IParseFunction p : parseFunctions) {
            if (StrUtil.isEmpty(p.functionName())) {
                continue;
            }
            allFunctionMap.put(p.functionName(), p);
        }
    }

    public IParseFunction getFunction(String functionName) {
        return allFunctionMap.get(functionName);
    }

}
