package com.financial.sharing.controller;

import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.HashMap;

/**
 * 权限测试控制器 - 用于诊断权限验证问题
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/test/auth")
public class TestAuthController {

    @Resource
    private UserProvider userProvider;

    /**
     * 测试权限验证
     */
    @GetMapping("/check")
    public String checkAuth() {
        try {
            log.info("开始测试权限验证...");

            TblStaffUtil loginStaff = userProvider.get();
            log.info("获取到用户信息: {}", loginStaff);

            if (loginStaff == null) {
                log.error("用户信息为null");
                JsonBean json = new JsonBean();
                json.setCode(0);
                json.setMsg("用户信息为null");
                return JsonMapper.nonNullMapper().toJson(json);
            }

            if (loginStaff.getLinkDetp() == null) {
                log.error("用户部门信息为null");
                JsonBean json = new JsonBean();
                json.setCode(0);
                json.setMsg("用户部门信息为null");
                return JsonMapper.nonNullMapper().toJson(json);
            }

            if (loginStaff.getCurrentOrg() == null) {
                log.error("用户组织信息为null");
                JsonBean json = new JsonBean();
                json.setCode(0);
                json.setMsg("用户组织信息为null");
                return JsonMapper.nonNullMapper().toJson(json);
            }

            // 成功获取用户信息
            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            Long userId = loginStaff.getStaffid().longValue();
            String userName = loginStaff.getUsername();

            log.info("权限验证成功 - 用户ID: {}, 用户名: {}, 租户ID: {}",
                    userId, userName, tenantId);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("userName", userName);
            data.put("tenantId", tenantId);

            JsonBean successJson = new JsonBean();
            successJson.setCode(1);
            successJson.setMsg("权限验证成功");
            successJson.setData(data);
            return JsonMapper.nonNullMapper().toJson(successJson);

        } catch (Exception e) {
            log.error("权限验证失败", e);
            JsonBean errorJson = new JsonBean();
            errorJson.setCode(0);
            errorJson.setMsg("权限验证失败: " + e.getMessage());
            return JsonMapper.nonNullMapper().toJson(errorJson);
        }
    }

    /**
     * 测试不带权限验证的接口
     */
    @GetMapping("/simple")
    public String simpleTest() {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg("简单测试成功");
        return JsonMapper.nonNullMapper().toJson(json);
    }
}