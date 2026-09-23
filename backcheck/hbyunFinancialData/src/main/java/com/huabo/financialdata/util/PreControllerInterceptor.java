package com.huabo.financialdata.util;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 如果发现有问题，不能满足，先请先注释掉
 * token过滤器
 *
 * @author kangjx
 * @createTime 2022/8/14
 */
@Component
@Slf4j
public class PreControllerInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Resource
    private UserProvider userProvider;

    /**
     * token统一处理，受不少所有的操作全要传递token传递按照甲方处理，
     * 虽然理解不了,添加|修改、删除 这类还要传递token后台每个方法判断，
     * 实现理解不了，处理一下
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getServletPath();
        String method = request.getMethod();
        logger.info("访问地址", path);
        String token = request.getHeader("token");
        TblStaffUtil staff = userProvider.get();
//        if(this.isBoolean(method) && Objects.isNull(staff)){
//            this.printJson(response);
//            return false;
//        }
        return true;
    }

    /**
     * 判断类型,因老项目没有统一前置或者方法统一前置
     * 所以直接通过类型处理了。
     *
     * @param method 方式
     * @return
     */
    private boolean isBoolean(String method) {
        String[] types = {"post", "delete", "put"};
        for (String type : types) {
            if (type.equals(method) || type.toUpperCase().equals(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 前台信息传递
     *
     * @param response
     */
    public void printJson(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");

        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(new JsonBean(403, "没有token，请传递token", ""));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
