package com.hbfk.sdk.log.support.parse;

import cn.hutool.core.util.StrUtil;
import com.hbfk.sdk.log.support.service.FunctionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* @Description: 函数解析
* @Author: 61
*/
@AllArgsConstructor
@Slf4j
public class LogFunctionParser {
    private FunctionService functionService;

    /**
    * @Description: 函数处理异常或函数不存在，返回原值
    * @Param: [value, functionName]
    * @return: java.lang.String
    * @Author: 61
    */
    public String getFunctionReturnValue(Object value, String functionName) {
        String ret = String.valueOf(value);
        if (StrUtil.isEmpty(functionName)) {
            return ret;
        }

        try {
            ret = functionService.apply(functionName, value);
        } catch (Exception e){
            log.error("functionName-[{}],value-[{}].处理异常:",functionName,value,e);
        }
        return ret;
    }

}
