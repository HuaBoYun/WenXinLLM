package com.huabo.financialdata.config.exception;

import java.io.Serializable;

/**
 * 通用业务代码类
 *
 * @author lee
 * @version 1.0.0
 **/
public class BizCode implements Serializable {

    /**
     * 业务代码，如500
     */
    private int code;

    /**
     * 业务代码名称，如服务器内部错误
     */
    private String name;

    /**
     * 错误提示占位符数据
     */
    private String[] value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String[] value) {
        this.value = value;
    }

    public BizCode() {

    }

    public BizCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public BizCode(int code, String[] value) {
        this.code = code;
        this.value = value;
    }

    public BizCode(BizCodeEnumDefault bizCode) {
        this.code = bizCode.getValue();
        this.name = bizCode.getName();
    }

    /**
     * 通过枚举类生成一个BizCode对象
     *
     * @author wf.shu
     * @since 2019年3月14日 上午10:28:36
     */
    public static BizCode getBizCodeFromEnum(BizCodeEnumDefault bizCode) {
        BizCode code = new BizCode(bizCode);
        return code;
    }

}
