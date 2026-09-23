package com.huabo.financialdata.config.mvn;


import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 框架包 - 统一返回结果
 *
 * @author lee
 * @version 1.0.0
 **/
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = 909430343951285771L;

    public static final int SUCCESS = 1;
    public static final int FAIL = 0;
    public static final String SUCCESS_MESSAGE = "response_success";
    public static final String FAIL_MESSAGE = "response_fail";


    /**
     * 编码
     */
    protected int code = 0;

    /**
     * 数据
     */
    protected T data = null;

    /**
     * 提示信息
     */
    protected String msg = null;

    private ApiResponse() {
    }

    public ApiResponse(int code) {
        this.code = code;
    }

    public ApiResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ApiResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return SUCCESS == (this.getCode());
    }

    public static boolean isSuccess(ApiResponse apiResponse) {
        if (apiResponse == null) {
            return false;
        }

        return apiResponse.isSuccess();
    }

    /**
     * 成功返回值，默认使用（1，response_success，作为返回值）
     *
     * @return
     */
    public static ApiResponse success() {
        return new ApiResponse(SUCCESS, null, SUCCESS_MESSAGE);
    }

    /**
     * 成功返回值，默认使用（1，response_success，作为返回值）
     *
     * @param data 返回参数数据
     * @return
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse(SUCCESS, data, SUCCESS_MESSAGE);
    }

    /**
     * 成功返回值，默认使用（1,作为返回值）
     *
     * @param data
     * @param message 如果不穿参数，或者位空字符串泽使用，response_success作为默认的返回值
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        if (StringUtils.isEmpty(message)) {
            return new ApiResponse(SUCCESS, SUCCESS_MESSAGE);
        } else {
            return new ApiResponse(SUCCESS, data, message);
        }
    }


    /**
     * 错误返回值，默认使用（500000，response_fail，作为返回值）
     *
     * @param message 如果不传参数，或者位空字符串则使用，response_fail作为默认的返回值
     * @return
     */
    public static ApiResponse fail(String message) {
        if (StringUtils.isEmpty(message)) {
            return new ApiResponse(FAIL, null, FAIL_MESSAGE);
        } else {
            return new ApiResponse(FAIL, null, message);
        }
    }

    /**
     * 错误返回值，默认使用（500000，response_fail，作为返回值）
     *
     * @param message 如果不传参数，或者位空字符串则使用，response_fail作为默认的返回值
     * @return
     */
    public static <T> ApiResponse<T> fail(String message, T data) {
        if (StringUtils.isEmpty(message)) {
            return new ApiResponse(FAIL, data, FAIL_MESSAGE);
        } else {
            return new ApiResponse(FAIL, data, message);
        }
    }

    public static ApiResponse fail(Throwable t) {
        return fail(t.getMessage());
    }

    /**
     * 返回值，默认使用（200000，response_success，作为返回值）
     *
     * @param code，如果为空，则默认200000
     * @return
     */
    public static ApiResponse response(int code) {
        if (code == 0) {
            code = 200000;
        }
        return new ApiResponse(code, null, SUCCESS_MESSAGE);

    }

    /**
     * 返回值，默认使用（200000，response_success，作为返回值）
     *
     * @param code code不能为空
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> response(int code, T data) {
        if (code == 0) {
            code = 200000;
        }
        return new ApiResponse(code, data, SUCCESS_MESSAGE);
    }

    /**
     * 返回值，默认使用（200000，response_success，作为返回值）
     *
     * @param code    code不传默认200000
     * @param data
     * @param message message为空默认response_success
     * @param <T>
     * @return
     */
    public static <T> ApiResponse<T> response(int code, T data, String message) {
        if (code == 0) {
            code = 200000;
        }
        if (StringUtils.isEmpty(message)) {
            message = SUCCESS_MESSAGE;
        }
        return new ApiResponse(code, data, message);
    }
}
