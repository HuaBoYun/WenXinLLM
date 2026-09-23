package com.huabo.contract.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.TblProjectInfoRegister;
import com.huabo.contract.service.TblProjectInfoRegisterService;
import com.huabo.contract.vo.ProjectInfoRegisterQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目信息登记管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-25
 */
@Slf4j
@RestController
@RequestMapping("/project/info/register")
@Tag(name="项目信息登记管理",description="项目信息登记管理")
@Validated
public class ProjectInfoRegisterController {

    @Autowired
    private TblProjectInfoRegisterService projectInfoRegisterService;

    /**
     * 分页查询项目信息登记列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询项目信息登记列表", description = "支持多条件查询和分页")
    public String getProjectInfoRegisterList(@RequestBody ProjectInfoRegisterQueryParam param) {
        try {
            log.info("分页查询项目信息登记列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<TblProjectInfoRegister> pageInfo = projectInfoRegisterService.getProjectInfoRegisterList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询项目信息登记列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取项目信息详情
     *
     * @param projectId 项目ID
     * @return 项目信息详情
     */
    @GetMapping("/{projectId}")
    @Operation(summary = "获取项目信息详情", description = "根据项目ID获取详细信息")
    public String getProjectInfoRegisterById(@PathVariable String projectId) {
        try {
            log.info("获取项目信息详情，项目ID：{}", projectId);

            if (!StringUtils.hasText(projectId)) {
                return JsonBean.error("项目ID不能为空");
            }

            TblProjectInfoRegister projectInfo = projectInfoRegisterService.getProjectInfoRegisterById(projectId);
            if (projectInfo == null) {
                return JsonBean.error("项目信息不存在");
            }

            return JsonBean.success("查询成功", projectInfo);
        } catch (Exception e) {
            log.error("获取项目信息详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存项目信息登记（新增或修改）
     *
     * @param projectInfo 项目信息
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存项目信息登记", description = "新增或修改项目信息登记")
    public String saveProjectInfoRegister(@RequestBody TblProjectInfoRegister projectInfo) {
        try {
            log.info("保存项目信息登记，项目信息：{}", projectInfo);

            // 校验必填字段
            if (!StringUtils.hasText(projectInfo.getProjectName())) {
                return JsonBean.error("项目名称不能为空");
            }
            if (!StringUtils.hasText(projectInfo.getContractorFullName())) {
                return JsonBean.error("发包方全称不能为空");
            }

            // 校验登记编号唯一性
            if (StringUtils.hasText(projectInfo.getRegisterNo())) {
                boolean exists = projectInfoRegisterService.existsRegisterNo(
                    projectInfo.getRegisterNo(), projectInfo.getProjectId());
                if (exists) {
                    return JsonBean.error("登记编号已存在，请重新输入");
                }
            }

            // 检查项目重复
            List<TblProjectInfoRegister> duplicateProjects = projectInfoRegisterService.checkDuplicateProjects(
                projectInfo.getProjectName(), projectInfo.getContractorFullName(), projectInfo.getProjectId());
            if (!duplicateProjects.isEmpty()) {
                return JsonBean.error("相同项目名称和发包方的项目已存在，请确认是否重复登记");
            }

            boolean result = projectInfoRegisterService.saveProjectInfoRegister(projectInfo);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存项目信息登记失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 删除项目信息登记
     *
     * @param projectId 项目ID
     * @return 删除结果
     */
    @DeleteMapping("/{projectId}")
    @Operation(summary = "删除项目信息登记", description = "根据项目ID删除项目信息")
    public String deleteProjectInfoRegister(@PathVariable String projectId) {
        try {
            log.info("删除项目信息登记，项目ID：{}", projectId);

            if (!StringUtils.hasText(projectId)) {
                return JsonBean.error("项目ID不能为空");
            }

            boolean result = projectInfoRegisterService.deleteProjectInfoRegister(projectId);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除项目信息登记失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除项目信息登记
     *
     * @param projectIds 项目ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除项目信息登记", description = "根据项目ID列表批量删除项目信息")
    public String batchDeleteProjectInfoRegister(@RequestBody List<String> projectIds) {
        try {
            log.info("批量删除项目信息登记，项目ID列表：{}", projectIds);

            if (projectIds == null || projectIds.isEmpty()) {
                return JsonBean.error("项目ID列表不能为空");
            }

            boolean result = projectInfoRegisterService.batchDeleteProjectInfoRegister(projectIds);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除项目信息登记失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 更新项目状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateStatus")
    @Operation(summary = "更新项目状态", description = "更新项目的状态")
    public String updateProjectStatus(@RequestBody Map<String, Object> request) {
        try {
            String projectId = (String) request.get("projectId");
            Integer projectStatus = (Integer) request.get("projectStatus");

            log.info("更新项目状态，项目ID：{}，项目状态：{}", projectId, projectStatus);

            if (!StringUtils.hasText(projectId)) {
                return JsonBean.error("项目ID不能为空");
            }
            if (projectStatus == null) {
                return JsonBean.error("项目状态不能为空");
            }

            boolean result = projectInfoRegisterService.updateProjectStatus(projectId, projectStatus);
            if (result) {
                return JsonBean.success("状态更新成功");
            } else {
                return JsonBean.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新项目状态失败", e);
            return JsonBean.error("状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新项目状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/batchUpdateStatus")
    @Operation(summary = "批量更新项目状态", description = "批量更新项目的状态")
    public String batchUpdateProjectStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<String> projectIds = (List<String>) request.get("projectIds");
            Integer projectStatus = (Integer) request.get("projectStatus");

            log.info("批量更新项目状态，项目ID列表：{}，项目状态：{}", projectIds, projectStatus);

            if (projectIds == null || projectIds.isEmpty()) {
                return JsonBean.error("项目ID列表不能为空");
            }
            if (projectStatus == null) {
                return JsonBean.error("项目状态不能为空");
            }

            boolean result = projectInfoRegisterService.batchUpdateProjectStatus(projectIds, projectStatus);
            if (result) {
                return JsonBean.success("批量状态更新成功");
            } else {
                return JsonBean.error("批量状态更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新项目状态失败", e);
            return JsonBean.error("批量状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 更新报备状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateReportStatus")
    @Operation(summary = "更新报备状态", description = "更新项目的报备状态")
    public String updateReportStatus(@RequestBody Map<String, Object> request) {
        try {
            String projectId = (String) request.get("projectId");
            Integer reportStatus = (Integer) request.get("reportStatus");

            log.info("更新报备状态，项目ID：{}，报备状态：{}", projectId, reportStatus);

            if (!StringUtils.hasText(projectId)) {
                return JsonBean.error("项目ID不能为空");
            }
            if (reportStatus == null) {
                return JsonBean.error("报备状态不能为空");
            }

            boolean result = projectInfoRegisterService.updateReportStatus(projectId, reportStatus);
            if (result) {
                return JsonBean.success("报备状态更新成功");
            } else {
                return JsonBean.error("报备状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新报备状态失败", e);
            return JsonBean.error("报备状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取需要首谈报备的项目列表
     *
     * @return 需要首谈报备的项目列表
     */
    @GetMapping("/firstTalkReportProjects")
    @Operation(summary = "获取需要首谈报备的项目列表", description = "获取所有需要首谈报备但未报备的项目")
    public String getFirstTalkReportProjects() {
        try {
            log.info("获取需要首谈报备的项目列表");

            List<TblProjectInfoRegister> projects = projectInfoRegisterService.getFirstTalkReportProjects();
            return JsonBean.success("查询成功", projects);
        } catch (Exception e) {
            log.error("获取需要首谈报备的项目列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 检查项目重复
     *
     * @param request 检查请求
     * @return 重复项目列表
     */
    @PostMapping("/checkDuplicate")
    @Operation(summary = "检查项目重复", description = "检查是否存在相同项目名称和发包方的项目")
    public String checkDuplicateProjects(@RequestBody Map<String, Object> request) {
        try {
            String projectName = (String) request.get("projectName");
            String contractorFullName = (String) request.get("contractorFullName");
            String excludeProjectId = (String) request.get("excludeProjectId");

            log.info("检查项目重复，项目名称：{}，发包方：{}，排除项目ID：{}", projectName, contractorFullName, excludeProjectId);

            if (!StringUtils.hasText(projectName)) {
                return JsonBean.error("项目名称不能为空");
            }
            if (!StringUtils.hasText(contractorFullName)) {
                return JsonBean.error("发包方全称不能为空");
            }

            List<TblProjectInfoRegister> duplicateProjects = projectInfoRegisterService.checkDuplicateProjects(
                projectName, contractorFullName, excludeProjectId);

            return JsonBean.success("查询成功", duplicateProjects);
        } catch (Exception e) {
            log.error("检查项目重复失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 生成项目登记编号
     *
     * @return 登记编号
     */
    @GetMapping("/generateRegisterNo")
    @Operation(summary = "生成项目登记编号", description = "自动生成唯一的项目登记编号")
    public String generateRegisterNo() {
        try {
            log.info("生成项目登记编号");
            String registerNo = projectInfoRegisterService.generateRegisterNo();
            return JsonBean.success("生成成功", registerNo);
        } catch (Exception e) {
            log.error("生成项目登记编号失败", e);
            return JsonBean.error("生成失败：" + e.getMessage());
        }
    }



}
