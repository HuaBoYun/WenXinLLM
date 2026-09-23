package com.huabo.know.config;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一鉴权
 *
 * @author zhihui
 * @date 2024/04/12
 */
@Slf4j
@Component
public class AuthInterceptor  implements HandlerInterceptor {
	
	@Resource
	private UserProvider userProvider;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查handler是否为Controller的方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            // 检查方法上是否有@Authenticated注解
            if (method.getMethodAnnotation(Authenticated.class)!=null) {
                return this.auth(request, response);
            }

            //检查类是否有@Authenticated注解
            Authenticated authenticated = AnnotatedElementUtils.findMergedAnnotation(((HandlerMethod) handler).getBeanType(), Authenticated.class);
            if (authenticated!=null) {
                return this.auth(request, response);
            }
        }
        // 如果不需要进一步处理，返回true；如果需要进一步处理（例如跳转到登录页面），返回false。
        return true;
    }

    private boolean auth(HttpServletRequest request,HttpServletResponse response) throws Exception {
        // 这里进行鉴权处理...
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            log.error("统一鉴权，未获取到token");
            this.respInfo(response, ResponseFormat.retParam(0,20006, null).toString());
            return false;
        }
        TblStaffUtil tblStaffUtil = userProvider.get();
        if (tblStaffUtil == null) {
            log.error("统一鉴权，未获取到用户信息，token：{}", token);
            this.respInfo(response, ResponseFormat.retParam(0,20006, null).toString());
            return false;
        }
        // 赋值用户信息
        request.setAttribute("staff", tblStaffUtil);
        return true;
    }

    private void respInfo(HttpServletResponse response,String text) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("统一鉴权返回错误异常");
        }
    }
}
