package com.hbfk.sdk.log.support.service;

/**
* @Description: 函数解析接口
* @Author: 61
*/
public interface IParseFunction {

    /**
    * @Description: 函数名称
    * @Param: []
    * @return: java.lang.String
    * @Author: 61
    * @Date: 2023/4/4
    */
    String functionName();

    /**
    * @Description: 执行函数
    * @Param: [value]
    * @return: java.lang.String
    * @Author: 61
    */
    String apply(Object value);
}
