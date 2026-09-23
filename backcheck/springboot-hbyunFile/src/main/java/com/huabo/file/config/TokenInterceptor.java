package com.huabo.file.config;

import com.google.common.base.Strings;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.file.exception.FileException;
import com.huabo.file.util.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token 校验
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<TblStaffUtil> loginStaff = new ThreadLocal<>();
    
    @Resource
    private UserProvider userProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String TOKEN = "token";
        String token = request.getHeader(TOKEN);
        if (Strings.isNullOrEmpty(token)) {
            throw new FileException(ErrorCodeEnum.USER_ERROR);
        }
        TblStaffUtil loginStaff = null;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            log.error("token:[{}]解析失败", token, e);
        }
        if (loginStaff == null) {
            throw new FileException(ErrorCodeEnum.USER_ERROR);
        } else {
            setTblStaffUtil(loginStaff);
        }
        return true;
    }

    public static void setTblStaffUtil(TblStaffUtil staffUtil){
        loginStaff.set(staffUtil);
    }
    public static TblStaffUtil getTblStaffUtil(){
        return loginStaff.get();
    }

    public static void removeTblStaffUtil(){
        loginStaff.remove();
    }
}
