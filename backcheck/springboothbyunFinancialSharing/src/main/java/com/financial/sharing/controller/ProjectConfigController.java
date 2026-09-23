package com.financial.sharing.controller;

import com.financial.sharing.entity.TblProjectConfig;
import com.financial.sharing.entity.TblProjectMember;
import com.financial.sharing.entity.TblProjectBudget;
import com.financial.sharing.service.TblProjectConfigService;
import com.financial.sharing.service.TblProjectMemberService;
import com.financial.sharing.service.TblProjectBudgetService;
import com.financial.sharing.service.TblProjectSettlementService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.financial.sharing.util.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 项目配置管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "项目配置管理")
@RestController
@RequestMapping("/financial/project-configs")
@CrossOrigin
public class ProjectConfigController {

    @Autowired
    private TblProjectConfigService projectConfigService;

    @Autowired
    private TblProjectMemberService projectMemberService;

    @Autowired
    private TblProjectBudgetService projectBudgetService;

    @Autowired
    private TblProjectSettlementService projectSettlementService;

    @Autowired
    private UserProvider userProvider;

    @ApiOperation("查询项目配置列表")
    @GetMapping
    public String getProjectConfigList(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @ModelAttribute PageableParam pageableParam,
                                 @RequestParam(required = false) String orgId,
                                 @RequestParam(required = false) String projectName,
                                 @RequestParam(required = false) String projectType,
                                 @RequestParam(required = false) String projectStatus,
                                 @RequestParam(required = false) String projectManager) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return null;
            }

            Map<String, Object> params = new HashMap<>();
            params.put("pageNum", pageableParam.getPageNum());
            params.put("pageSize", pageableParam.getSize());
            // 只有非空字符串才添加到查询条件
            if (projectName != null && !projectName.trim().isEmpty()) {
                params.put("projectName", projectName);
            }
            if (projectStatus != null && !projectStatus.trim().isEmpty()) {
                params.put("projectStatus", projectStatus);
            }
            if (projectManager != null && !projectManager.trim().isEmpty()) {
                params.put("projectManager", projectManager);
            }

            PageResult<TblProjectConfig> pageResult = projectConfigService.getProjectConfigPage(params);

            // 转换返回格式
            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(pageResult);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询项目配置列表失败", e);
            return createErrorResponse(response, "查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存项目配置")
    @PostMapping
    public String saveProjectConfig(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @ModelAttribute TblProjectConfig projectConfig) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            // 处理前端字段映射
            if (projectConfig.getProjectManager() != null && !projectConfig.getProjectManager().trim().isEmpty()) {
                projectConfig.setProjectManagerName(projectConfig.getProjectManager());
            }

            if (projectConfig.getProjectId() == null || projectConfig.getProjectId().isEmpty()) {
                // 新增
                projectConfig.setProjectId(UUID.randomUUID().toString());
                projectConfig.setCreateTime(LocalDateTime.now());
                projectConfig.setIsEnabled(1);
                projectConfig.setCreateUser(loginStaff.getStaffid().toString());
            } else {
                // 更新
                projectConfig.setUpdateTime(LocalDateTime.now());
                projectConfig.setUpdateUser(loginStaff.getStaffid().toString());
            }

            projectConfigService.saveOrUpdate(projectConfig);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存成功");
            json.setData(projectConfig.getProjectId());
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存项目配置失败", e);
            return createErrorResponse(response, "保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除项目配置")
    @DeleteMapping("/{projectId}")
    public String deleteProjectConfig(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String projectId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            projectConfigService.removeById(projectId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("删除成功");
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("删除项目配置失败", e);
            return createErrorResponse(response, "删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除项目配置")
    @DeleteMapping("/batch")
    public String batchDeleteProjectConfigs(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestBody List<String> projectIds) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            int successCount = 0;
            int failureCount = 0;
            List<String> failedIds = new ArrayList<>();

            for (String projectId : projectIds) {
                try {
                    projectConfigService.removeById(projectId);
                    successCount++;
                } catch (Exception e) {
                    failureCount++;
                    failedIds.add(projectId);
                    log.error("删除项目失败: {}", projectId, e);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", projectIds.size());
            result.put("successCount", successCount);
            result.put("failureCount", failureCount);
            result.put("failedIds", failedIds);
            result.put("message", String.format("批量删除完成,成功%d个,失败%d个", successCount, failureCount));

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("批量删除完成");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("批量删除项目配置失败", e);
            return createErrorResponse(response, "批量删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取项目配置详情")
    @GetMapping("/{projectId}")
    public String getProjectConfigDetail(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable String projectId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            TblProjectConfig project = projectConfigService.getById(projectId);
            if (project == null) {
                JsonBean json = new JsonBean();
                json.setCode(0);
                json.setMsg("项目不存在");
                return JsonMapper.toJson(json);
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(project);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取项目配置详情失败", e);
            return createErrorResponse(response, "查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取项目成员列表")
    @GetMapping("/{projectId}/members")
    public String getProjectMembers(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String projectId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            List<TblProjectMember> members = projectMemberService.getByProjectId(projectId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(members);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取项目成员列表失败", e);
            return createErrorResponse(response, "查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存项目成员")
    @PostMapping("/{projectId}/members")
    public String saveProjectMembers(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String projectId,
                                     @RequestBody List<TblProjectMember> members) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            // 设置项目ID和创建时间
            for (TblProjectMember member : members) {
                if (member.getMemberId() == null || member.getMemberId().isEmpty()) {
                    member.setMemberId(UUID.randomUUID().toString());
                    member.setCreateTime(LocalDateTime.now());
                    member.setCreateUser(loginStaff.getStaffid().toString());
                }
                member.setProjectId(projectId);
                member.setIsEnabled(1);
            }

            projectMemberService.saveBatchMembers(members);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存项目成员成功");
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存项目成员失败", e);
            return createErrorResponse(response, "保存项目成员失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取项目预算明细")
    @GetMapping("/{projectId}/budget")
    public String getProjectBudget(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String projectId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            List<TblProjectBudget> budgets = projectBudgetService.getByProjectId(projectId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(budgets);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取项目预算明细失败", e);
            return createErrorResponse(response, "查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存项目预算明细")
    @PostMapping("/{projectId}/budget")
    public String saveProjectBudget(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String projectId,
                                     @RequestBody List<TblProjectBudget> budgets) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            // 设置项目ID和创建时间
            for (TblProjectBudget budget : budgets) {
                if (budget.getBudgetId() == null || budget.getBudgetId().isEmpty()) {
                    budget.setBudgetId(UUID.randomUUID().toString());
                    budget.setCreateTime(LocalDateTime.now());
                    budget.setCreateUser(loginStaff.getStaffid().toString());
                }
                budget.setProjectId(projectId);
                budget.setIsEnabled(1);
            }

            projectBudgetService.saveBatchBudgets(budgets);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("保存项目预算成功");
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存项目预算失败", e);
            return createErrorResponse(response, "保存项目预算失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新项目状态")
    @PutMapping("/{projectId}/status")
    public String updateProjectStatus(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @PathVariable String projectId,
                                        @RequestParam String projectStatus) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            projectConfigService.updateProjectStatus(projectId, projectStatus);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("项目状态更新成功");
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("更新项目状态失败", e);
            return createErrorResponse(response, "项目状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("项目结算")
    @PostMapping("/{projectId}/settlement")
    public String settleProject(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @PathVariable String projectId,
                                    @RequestBody Map<String, Object> settlementData) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            TblProjectConfig project = projectConfigService.getById(projectId);
            if (project == null) {
                return createErrorResponse(response, "项目不存在");
            }

            // 更新项目状态为已完成
            project.setProjectStatus("COMPLETED");
            project.setUpdateTime(LocalDateTime.now());
            project.setUpdateUser(loginStaff.getStaffid().toString());
            projectConfigService.updateById(project);

            Map<String, Object> result = new HashMap<>();
            result.put("projectId", projectId);
            result.put("settlementAmount", project.getBudgetAmount());
            result.put("settlementDate", new Date());
            result.put("settlementStatus", "COMPLETED");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("项目结算成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("项目结算失败", e);
            return createErrorResponse(response, "项目结算失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取项目统计信息")
    @GetMapping("/{projectId}/statistics")
    public String getProjectStatistics(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable String projectId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            Map<String, Object> statistics = projectConfigService.getProjectStatistics(projectId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(statistics);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取项目统计信息失败", e);
            return createErrorResponse(response, "查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("导入项目配置")
    @PostMapping("/import")
    public String importProjectConfigs(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestParam("file") MultipartFile file) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            // TODO: 实现真实的导入逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", 0);
            result.put("successCount", 0);
            result.put("failureCount", 0);
            result.put("message", "导入功能待实现");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("导入成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("导入项目配置失败", e);
            return createErrorResponse(response, "导入失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出项目配置")
    @GetMapping("/export")
    public String exportProjectConfigs(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestParam(required = false) String orgId,
                                         @RequestParam(required = false) String projectType) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null ||
                loginStaff.getLinkDetp() == null ||
                loginStaff.getCurrentOrg() == null) {
                return createErrorResponse(response, "用户已失效");
            }

            // TODO: 实现真实的导出逻辑
            Map<String, Object> result = new HashMap<>();
            result.put("fileName", "project_configs_" + System.currentTimeMillis() + ".xlsx");
            result.put("downloadUrl", "/download/project_configs.xlsx");
            result.put("totalCount", 0);
            result.put("message", "导出功能待实现");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("导出成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("导出项目配置失败", e);
            return createErrorResponse(response, "导出失败: " + e.getMessage());
        }
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(HttpServletResponse response, String message) {
        try {
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg(message);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("创建错误响应失败", e);
            return "{\"code\":0,\"msg\":\"系统错误\"}";
        }
    }
}
