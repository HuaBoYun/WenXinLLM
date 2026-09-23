package com.hbfk.util;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;

import java.util.Objects;

/**
 * 常量类
 * 
 * @author 华博云
 * @since 2025-01-21
 */
public interface ConstClass {

    /**
     * 默认分页大小
     */
    public static final int DEFAULT_SIZE = 20;

    /**
     * 检查Token是否有效
     * 
     * @param token 用户Token
     * @return 是否有效
     */
    static boolean checkToken(String token) {
        try {
            TblStaffUtil staff = DealUserToken.parseUserToken(token);
            return Objects.isNull(staff) ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Token验证失败的响应
     *
     * @return JsonBean响应
     */
    static JsonBean tokenFailure() {
        return new JsonBean(200, "没有token,或者token错误.", "");
    }

    /**
     * 获取当前用户信息
     *
     * @param token 用户Token
     * @return 用户信息
     */
    static TblStaffUtil getCurrentUser(String token) {
        try {
            return DealUserToken.parseUserToken(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
