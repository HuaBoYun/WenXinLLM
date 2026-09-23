package com.financial.sharing.groupControl.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.groupControl.dto.DimensionQueryParam;
import com.financial.sharing.groupControl.entity.TblDimensionInfo;
import com.financial.sharing.groupControl.service.DimensionService;
import com.hbfk.entity.TblStaffUtil;
import com.financial.sharing.util.JsonMapper;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维度信息Controller
 * 
 * @author 华博云开发团队
 * @since 2026-01-30
 */
@Slf4j
@RestController
@RequestMapping("/groupControl/dimension")
@Api(tags = "集团绩效平台-维度信息管理")
public class DimensionController {

    @Autowired
    private DimensionService dimensionService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 分页查询维度列表
     */
    @ApiOperation("分页查询维度列表")
    @PostMapping("/getList")
    public String getList(HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestBody DimensionQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 401);
                result.put("msg", "用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            // 设置租户ID
            if (!StringUtils.hasText(param.getTenantId())) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().toString());
            }

            // 查询数据
            Page<TblDimensionInfo> page = dimensionService.getList(param);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("msg", "查询成功");
            result.put("data", page);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("查询维度列表失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "查询失败: " + e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        }
    }

    /**
     * 查询维度详情
     */
    @ApiOperation("查询维度详情")
    @PostMapping("/detail")
    public String getDetail(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestBody Map<String, String> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 401);
                result.put("msg", "用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            String dimensionId = param.get("dimensionId");
            if (!StringUtils.hasText(dimensionId)) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "维度ID不能为空");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            TblDimensionInfo dimension = dimensionService.getDetail(dimensionId);

            Map<String, Object> result = new HashMap<>();
            result.put("code", 1);
            result.put("msg", "查询成功");
            result.put("data", dimension);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("查询维度详情失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "查询失败: " + e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        }
    }

    /**
     * 保存维度信息
     */
    @ApiOperation("保存维度信息")
    @PostMapping("/save")
    public String save(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestBody TblDimensionInfo dimension) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 401);
                result.put("msg", "用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            // 设置租户ID和操作人信息
            if (!StringUtils.hasText(dimension.getTenantId())) {
                dimension.setTenantId(loginStaff.getCurrentOrg().getOrgid().toString());
            }
            if (!StringUtils.hasText(dimension.getDimensionId())) {
                dimension.setCreateUser(loginStaff.getStaffid().toString());
            }
            dimension.setUpdateUser(loginStaff.getStaffid().toString());

            // 保存数据
            boolean success = dimensionService.save(dimension);

            Map<String, Object> result = new HashMap<>();
            result.put("code", success ? 1 : 0);
            result.put("msg", success ? "保存成功" : "保存失败");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("保存维度信息失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "保存失败: " + e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        }
    }

    /**
     * 删除维度
     */
    @ApiOperation("删除维度")
    @PostMapping("/delete")
    public String delete(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestBody Map<String, String> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 401);
                result.put("msg", "用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            String dimensionId = param.get("dimensionId");
            if (!StringUtils.hasText(dimensionId)) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "维度ID不能为空");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            boolean success = dimensionService.delete(dimensionId);

            Map<String, Object> result = new HashMap<>();
            result.put("code", success ? 1 : 0);
            result.put("msg", success ? "删除成功" : "删除失败");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("删除维度失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "删除失败: " + e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        }
    }

    /**
     * 批量删除维度
     */
    @ApiOperation("批量删除维度")
    @PostMapping("/batchDelete")
    public String batchDelete(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestBody Map<String, List<String>> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 401);
                result.put("msg", "用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            List<String> dimensionIds = param.get("dimensionIds");
            if (dimensionIds == null || dimensionIds.isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "请选择要删除的维度");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            boolean success = dimensionService.batchDelete(dimensionIds);

            Map<String, Object> result = new HashMap<>();
            result.put("code", success ? 1 : 0);
            result.put("msg", success ? "批量删除成功" : "批量删除失败");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("批量删除维度失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "批量删除失败: " + e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        }
    }

    /**
     * 更新维度状态
     */
    @ApiOperation("更新维度状态")
    @PostMapping("/updateStatus")
    public String updateStatus(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody Map<String, String> param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 401);
                result.put("msg", "用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            String dimensionId = param.get("dimensionId");
            String status = param.get("status");

            if (!StringUtils.hasText(dimensionId) || !StringUtils.hasText(status)) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 0);
                result.put("msg", "参数不完整");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return JsonMapper.toJson(result);
            }

            boolean success = dimensionService.updateStatus(dimensionId, status);

            Map<String, Object> result = new HashMap<>();
            result.put("code", success ? 1 : 0);
            result.put("msg", success ? "状态更新成功" : "状态更新失败");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        } catch (Exception e) {
            log.error("更新维度状态失败", e);
            Map<String, Object> result = new HashMap<>();
            result.put("code", 0);
            result.put("msg", "状态更新失败: " + e.getMessage());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(result);
        }
    }
}

