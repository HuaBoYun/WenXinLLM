package com.huabo.audit.util;

import com.huabo.audit.common.ResultCode;
import com.huabo.audit.exception.CommercialException;
import org.apache.commons.lang.StringUtils;

/**
 * @author lyz
 * @description
 */
public class TokenUtils {
    /**
    * @description 校验token
    * @author   lyz
    * @date 2022/4/15 11:36
    */
    public static void validToken(String token) {
        //检查token
        if(StringUtils.isBlank(token)){
            throw new CommercialException(ResultCode.BIZ_ERROR,"token 无效！");
        }
    }

}
