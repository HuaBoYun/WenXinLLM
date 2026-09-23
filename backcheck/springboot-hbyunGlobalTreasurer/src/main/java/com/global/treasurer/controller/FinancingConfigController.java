package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingConfigDTO;
import com.global.treasurer.dto.FinancingConfigQueryDTO;
import com.global.treasurer.entity.TblFinancingBasicParams;
import com.global.treasurer.service.FinancingConfigService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 融资配置管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@Controller
@RequestMapping({"/rzgl/financing-config", "/financial/rzgl/financing-config", "/centralaudit/rzgl/financing-config"})
@Api(tags = "融资配置管理")
public class FinancingConfigController {
    private static final Logger log = LoggerFactory.getLogger(FinancingConfigController.class);

    @Resource
    private FinancingConfigService financingConfigService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询融资配置列表
     */
    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询融资配置列表")
    public String getConfigList(FinancingConfigQueryDTO queryDTO,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            PageInfo<TblFinancingBasicParams> pageInfo = financingConfigService.getConfigList(queryDTO);

            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());

            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询融资配置列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取配置详情
     */
    @GetMapping("/detail/{id}")
    @ResponseBody
    @ApiOperation("获取配置详情")
    public String getConfigById(@PathVariable Long id,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblFinancingBasicParams params = financingConfigService.getConfigById(id);
            return new JsonBean(1, "成功", params).toJson();
        } catch (Exception e) {
            log.error("获取配置详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建融资配置
     */
    @PostMapping("/create")
    @ResponseBody
    @ApiOperation("创建融资配置")
    public String createConfig(FinancingConfigDTO dto,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblFinancingBasicParams params = financingConfigService.createConfig(dto);
            return new JsonBean(1, "创建成功", params).toJson();
        } catch (Exception e) {
            log.error("创建融资配置失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新融资配置
     */
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新融资配置")
    public String updateConfig(FinancingConfigDTO dto,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblFinancingBasicParams params = financingConfigService.updateConfig(dto);
            return new JsonBean(1, "更新成功", params).toJson();
        } catch (Exception e) {
            log.error("更新融资配置失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除融资配置
     */
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation("删除融资配置")
    public String deleteConfig(@PathVariable Long id,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            boolean result = financingConfigService.deleteConfig(id);
            return new JsonBean(result ? 1 : 0, result ? "删除成功" : "删除失败", null).toJson();
        } catch (Exception e) {
            log.error("删除融资配置失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新配置状态
     */
    @PostMapping("/updateStatus")
    @ResponseBody
    @ApiOperation("更新配置状态")
    public String updateStatus(@RequestParam Long paramId,
                              @RequestParam String status,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            boolean result = financingConfigService.updateStatus(paramId, status);
            return new JsonBean(result ? 1 : 0, result ? "状态更新成功" : "状态更新失败", null).toJson();
        } catch (Exception e) {
            log.error("更新配置状态失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }
}
