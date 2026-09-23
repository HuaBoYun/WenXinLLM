package com.financial.sharing.util;

import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * 用户工具类 - 兼容层
 * 用于兼容旧代码中使用 com.hbyun.common.utils.UserUtils 的地方
 * 
 * @author Augment Agent
 * @date 2026-02-04
 */
@Component
public class UserUtils {
    
    @Autowired
    private UserProvider userProvider;
    
    private static UserUtils instance;
    
    @PostConstruct
    public void init() {
        instance = this;
    }
    
    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    public static TblStaffUtil getUser() {
        if (instance == null || instance.userProvider == null) {
            throw new RuntimeException("UserProvider未初始化");
        }
        try {
            return instance.userProvider.get();
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
    
    /**
     * 获取当前用户的组织ID（即租户ID）。
     * <p>
     * 不同登录流程下，TblStaffUtil 顶层 orgid 字段不一定有值，但嵌套对象
     * currentOrg/linkOrg/groupOrg 通常会被填充（参考 OperatorFilter 校验逻辑）。
     * 这里按优先级 fallback：顶层 → 当前公司 → 隶属公司 → 所属集团。
     *
     * @param user 用户对象，不能为 null
     * @return orgid（可能为 null）
     */
    private static BigDecimal resolveOrgid(TblStaffUtil user) {
        if (user.getOrgid() != null) {
            return user.getOrgid();
        }
        TblOrganizationUtil currentOrg = user.getCurrentOrg();
        if (currentOrg != null && currentOrg.getOrgid() != null) {
            return currentOrg.getOrgid();
        }
        TblOrganizationUtil linkOrg = user.getLinkOrg();
        if (linkOrg != null && linkOrg.getOrgid() != null) {
            return linkOrg.getOrgid();
        }
        TblOrganizationUtil groupOrg = user.getGroupOrg();
        if (groupOrg != null && groupOrg.getOrgid() != null) {
            return groupOrg.getOrgid();
        }
        return null;
    }

    /**
     * 获取当前用户ID
     * @return 用户ID
     */
    public static String getUserId() {
        TblStaffUtil user = getUser();
        return user != null && user.getStaffid() != null ? user.getStaffid().toString() : null;
    }

    /**
     * 获取当前用户ID（必填）。当用户未登录或 staffid 为空时抛出可读业务异常，
     * 取代各 service 里的 UserUtils.getUser().getStaffid().toString() 链式调用。
     */
    public static String requireUserId() {
        TblStaffUtil user = getUser();
        if (user == null || user.getStaffid() == null) {
            throw new RuntimeException("用户未登录或会话已失效，请重新登录");
        }
        return user.getStaffid().toString();
    }

    /**
     * 获取当前用户租户ID（实际上是组织ID）
     * @return 租户ID
     */
    public static String getTenantId() {
        TblStaffUtil user = getUser();
        if (user == null) {
            return null;
        }
        BigDecimal orgid = resolveOrgid(user);
        return orgid != null ? orgid.toString() : null;
    }

    /**
     * 获取当前用户租户ID（必填，String 形态）。当用户未登录或 orgid 为空时抛出
     * 可读业务异常，取代各 service 里的 UserUtils.getTenantId().toString() 与
     * UserUtils.getUser().getOrgid().toString() 链式调用。
     */
    public static String requireTenantId() {
        TblStaffUtil user = getUser();
        if (user == null) {
            throw new RuntimeException("用户未登录或会话已失效，请重新登录");
        }
        BigDecimal orgid = resolveOrgid(user);
        if (orgid == null) {
            throw new RuntimeException("用户未登录或会话已失效，请重新登录");
        }
        return orgid.toString();
    }

    /**
     * 获取当前用户租户ID并转换为 Long（实际上是组织ID）。
     * 当用户未登录或会话已失效（orgid 为空）时，抛出可读的业务异常，
     * 避免在调用方触发隐晦的 NumberFormatException / NullPointerException。
     *
     * @return 租户ID（Long）
     */
    public static Long getTenantIdAsLong() {
        TblStaffUtil user = getUser();
        if (user == null) {
            throw new RuntimeException("用户未登录或会话已失效，请重新登录");
        }
        BigDecimal orgid = resolveOrgid(user);
        if (orgid == null) {
            throw new RuntimeException("用户未登录或会话已失效，请重新登录");
        }
        return orgid.longValue();
    }

    // ====================================================================
    // 以下为语义对齐华博云命名规范（ORG_ID）的别名方法。
    // 内部实现复用上面的 resolveOrgid，无副作用，可放心替换调用。
    // ====================================================================

    /** 别名：返回当前登录用户的 orgid（String），未登录返回 null。 */
    public static String getOrgId() {
        return getTenantId();
    }

    /** 别名：返回当前登录用户的 orgid（String），未登录抛业务异常。 */
    public static String requireOrgId() {
        return requireTenantId();
    }

    /** 别名：返回当前登录用户的 orgid（Long），未登录抛业务异常。 */
    public static Long getOrgIdAsLong() {
        return getTenantIdAsLong();
    }
}

