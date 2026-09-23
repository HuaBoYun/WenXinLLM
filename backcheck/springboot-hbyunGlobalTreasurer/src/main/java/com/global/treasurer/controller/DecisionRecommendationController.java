package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.DecisionRecommendation;
import com.global.treasurer.service.IDecisionRecommendationService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 决策建议控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@RestController
@RequestMapping({"/qqsk/recommendation", "/financial/decision/recommendation", "/xjgl/decision/recommendation", "/centralaudit/decision/recommendation", "/decision/recommendation", "/recommendation"})
@Api(tags = "决策建议管理")
public class DecisionRecommendationController {
    private static final Logger log = LoggerFactory.getLogger(DecisionRecommendationController.class);

    @Resource
    private IDecisionRecommendationService decisionRecommendationService;

    @Resource
    private UserProvider userProvider;

    @GetMapping({"/list", "/page"})
    @ApiOperation("获取决策建议列表(GET)")
    public String getRecommendationListGet(
            @ApiParam(value = "页码", example = "1") @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam(value = "建议编号") @RequestParam(required = false) String recommendationNo,
            @ApiParam(value = "建议名称") @RequestParam(required = false) String recommendationName,
            @ApiParam(value = "建议类型") @RequestParam(required = false) String recommendationType,
            @ApiParam(value = "建议状态") @RequestParam(required = false) String recommendationStatus,
            @ApiParam(value = "优先级") @RequestParam(required = false) String priority,
            HttpServletResponse response) {

        try {
            log.info("========== 决策建议查询接口(GET) ==========");
            log.info("接收参数 - pageNo: {}, pageSize: {}", pageNum, pageSize);
            log.info("接收参数 - recommendationNo: {}, recommendationName: {}, recommendationType: {}, recommendationStatus: {}, priority: {}",
                    recommendationNo, recommendationName, recommendationType, recommendationStatus, priority);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> queryParams = new HashMap<>();
            if (recommendationNo != null && !recommendationNo.trim().isEmpty()) {
                queryParams.put("recommendationNo", recommendationNo);
            }
            if (recommendationName != null && !recommendationName.trim().isEmpty()) {
                queryParams.put("recommendationName", recommendationName);
            }
            if (recommendationType != null && !recommendationType.trim().isEmpty()) {
                queryParams.put("recommendationType", recommendationType);
            }
            if (recommendationStatus != null && !recommendationStatus.trim().isEmpty()) {
                queryParams.put("recommendationStatus", recommendationStatus);
            }
            if (priority != null && !priority.trim().isEmpty()) {
                queryParams.put("priority", priority);
            }

            log.info("构建的queryParams: {}", queryParams);

            // 分页查询
            Page<DecisionRecommendation> page = new Page<>(pageNum, pageSize);
            IPage<DecisionRecommendation> result = decisionRecommendationService.selectPage(page, queryParams);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询结果 - 当前页: {}, 每页大小: {}, 总记录数: {}, 当前页记录数: {}",
                    result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords().size());

            return new JsonBean(1, "查询成功", data).toString();

        } catch (Exception e) {
            log.error("获取决策建议列表失败", e);
            return JsonBean.error("获取决策建议列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取决策建议详情")
    public String getRecommendationDetail(
            @ApiParam(value = "建议ID", required = false) @RequestParam(required = false) Long recommendationId,
            HttpServletResponse response) {

        try {
            log.info("获取决策建议详情，收到的recommendationId参数: {}", recommendationId);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (recommendationId == null || recommendationId <= 0) {
                log.warn("无效的recommendationId: {}", recommendationId);
                return JsonBean.error("无效的建议ID，recommendationId参数未传递或格式错误");
            }

            DecisionRecommendation recommendation = decisionRecommendationService.getById(recommendationId);
            if (recommendation == null) {
                log.warn("决策建议不存在，recommendationId: {}", recommendationId);
                return JsonBean.error("决策建议不存在");
            }

            log.info("获取决策建议详情成功，recommendationId: {}", recommendationId);
            return new JsonBean(1, "查询成功", recommendation).toString();

        } catch (Exception e) {
            log.error("获取决策建议详情失败，recommendationId: {}", recommendationId, e);
            return JsonBean.error("获取决策建议详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/{recommendationId}")
    @ApiOperation("获取决策建议详情(RESTful)")
    public String getRecommendationDetailRestful(
            @ApiParam(value = "建议ID", required = true) @PathVariable Long recommendationId,
            HttpServletResponse response) {

        try {
            log.info("========== 获取决策建议详情(RESTful) ==========");
            log.info("接收参数 - 建议ID: {}", recommendationId);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (recommendationId <= 0) {
                log.warn("无效的recommendationId: {}", recommendationId);
                return JsonBean.error("无效的建议ID");
            }

            DecisionRecommendation recommendation = decisionRecommendationService.getById(recommendationId);
            if (recommendation == null) {
                log.warn("决策建议不存在，recommendationId: {}", recommendationId);
                return JsonBean.error("决策建议不存在");
            }

            log.info("获取决策建议详情成功，recommendationId: {}", recommendationId);
            return new JsonBean(1, "查询成功", recommendation).toString();

        } catch (Exception e) {
            log.error("获取决策建议详情失败，recommendationId: {}", recommendationId, e);
            return JsonBean.error("获取决策建议详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("创建决策建议")
    public String createRecommendation(@ModelAttribute DecisionRecommendation recommendation, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(recommendation.getRecommendationName())) {
                return JsonBean.error("建议名称不能为空");
            }
            if (!StringUtils.hasText(recommendation.getRecommendationType())) {
                return JsonBean.error("建议类型不能为空");
            }

            // 自动生成建议编号（如果为空）
            if (!StringUtils.hasText(recommendation.getRecommendationNo())) {
                String autoRecommendationNo = generateRecommendationNo();
                recommendation.setRecommendationNo(autoRecommendationNo);
            }

            // 设置默认值
            if (recommendation.getRecommendationStatus() == null) {
                recommendation.setRecommendationStatus("PENDING");
            }
            if (recommendation.getPriority() == null) {
                recommendation.setPriority("MEDIUM");
            }
            recommendation.setCreateTime(java.time.LocalDateTime.now());
            recommendation.setUpdateTime(java.time.LocalDateTime.now());
            recommendation.setDelFlag("0");

            // 设置创建人
            TblStaffUtil user = userProvider.get();
            if (user != null) {
                recommendation.setCreateBy(getStaffId(user));
                recommendation.setUpdateBy(getStaffId(user));
                if (user.getCurrentOrg() != null) {
                    recommendation.setOrgId(getOrgId(user.getCurrentOrg()));
                }
            }

            boolean success = decisionRecommendationService.save(recommendation);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建决策建议失败", e);
            return JsonBean.error("创建决策建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新决策建议")
    public String updateRecommendation(@ModelAttribute DecisionRecommendation recommendation, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (recommendation.getRecommendationId() == null) {
                return JsonBean.error("建议ID不能为空");
            }

            // 检查是否存在
            DecisionRecommendation existing = decisionRecommendationService.getById(recommendation.getRecommendationId());
            if (existing == null) {
                return JsonBean.error("决策建议不存在");
            }

            recommendation.setUpdateTime(java.time.LocalDateTime.now());

            // 设置更新人
            TblStaffUtil user = userProvider.get();
            if (user != null) {
                recommendation.setUpdateBy(getStaffId(user));
            }

            boolean success = decisionRecommendationService.updateById(recommendation);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新决策建议失败", e);
            return JsonBean.error("更新决策建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除决策建议")
    public String deleteRecommendation(
            @ApiParam(value = "建议ID", required = true) @RequestParam Long recommendationId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            DecisionRecommendation existing = decisionRecommendationService.getById(recommendationId);
            if (existing == null) {
                return JsonBean.error("决策建议不存在");
            }

            // 逻辑删除
            existing.setDelFlag("1");
            existing.setUpdateTime(java.time.LocalDateTime.now());

            boolean success = decisionRecommendationService.updateById(existing);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除决策建议失败，recommendationId: {}", recommendationId, e);
            return JsonBean.error("删除决策建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/review")
    @ApiOperation("审核决策建议")
    public String reviewRecommendation(
            @ApiParam(value = "建议ID", required = true) @RequestParam Long recommendationId,
            @ApiParam(value = "审核意见") @RequestParam(required = false) String reviewOpinion,
            @ApiParam(value = "审核人ID") @RequestParam(required = false) Long reviewUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入审核人ID，则使用当前登录用户
            if (reviewUser == null) {
                TblStaffUtil user = userProvider.get();
                reviewUser = user != null ? getStaffId(user) : null;
            }

            boolean success = decisionRecommendationService.reviewRecommendation(recommendationId, reviewOpinion, reviewUser);
            if (success) {
                return JsonBean.success("审核成功");
            } else {
                return JsonBean.error("审核失败");
            }

        } catch (Exception e) {
            log.error("审核决策建议失败，recommendationId: {}", recommendationId, e);
            return JsonBean.error("审核决策建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/{recommendationId}/review")
    @ApiOperation("审核决策建议(RESTful)")
    public String reviewRecommendationRestful(
            @ApiParam(value = "建议ID", required = true) @PathVariable Long recommendationId,
            @ApiParam(value = "审核意见") @RequestParam(required = false) String reviewOpinion,
            @ApiParam(value = "审核人ID") @RequestParam(required = false) Long reviewUser,
            HttpServletResponse response) {
        return reviewRecommendation(recommendationId, reviewOpinion, reviewUser, response);
    }

    @PostMapping("/approve")
    @ApiOperation("批准决策建议")
    public String approveRecommendation(
            @ApiParam(value = "建议ID", required = true) @RequestParam Long recommendationId,
            @ApiParam(value = "批准意见") @RequestParam(required = false) String approvalOpinion,
            @ApiParam(value = "批准人ID") @RequestParam(required = false) Long reviewUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入批准人ID，则使用当前登录用户
            Long approvalUserId = reviewUser;
            if (approvalUserId == null) {
                TblStaffUtil user = userProvider.get();
                approvalUserId = user != null ? getStaffId(user) : null;
            }

            boolean success = decisionRecommendationService.approveRecommendation(recommendationId, approvalOpinion, approvalUserId);
            if (success) {
                return JsonBean.success("批准成功");
            } else {
                return JsonBean.error("批准失败");
            }

        } catch (Exception e) {
            log.error("批准决策建议失败，recommendationId: {}", recommendationId, e);
            return JsonBean.error("批准决策建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/{recommendationId}/approve")
    @ApiOperation("批准决策建议(RESTful)")
    public String approveRecommendationRestful(
            @ApiParam(value = "建议ID", required = true) @PathVariable Long recommendationId,
            @ApiParam(value = "批准意见") @RequestParam(required = false) String reviewOpinion,
            @ApiParam(value = "批准人ID") @RequestParam(required = false) Long reviewUser,
            HttpServletResponse response) {
        return approveRecommendation(recommendationId, reviewOpinion, reviewUser, response);
    }

    @PostMapping("/reject")
    @ApiOperation("拒绝决策建议")
    public String rejectRecommendation(
            @ApiParam(value = "建议ID", required = true) @RequestParam Long recommendationId,
            @ApiParam(value = "拒绝理由") @RequestParam(required = false) String rejectOpinion,
            @ApiParam(value = "拒绝人ID") @RequestParam(required = false) Long reviewUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入拒绝人ID，则使用当前登录用户
            Long rejectUserId = reviewUser;
            if (rejectUserId == null) {
                TblStaffUtil user = userProvider.get();
                rejectUserId = user != null ? getStaffId(user) : null;
            }

            boolean success = decisionRecommendationService.rejectRecommendation(recommendationId, rejectOpinion, rejectUserId);
            if (success) {
                return JsonBean.success("拒绝成功");
            } else {
                return JsonBean.error("拒绝失败");
            }

        } catch (Exception e) {
            log.error("拒绝决策建议失败，recommendationId: {}", recommendationId, e);
            return JsonBean.error("拒绝决策建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/{recommendationId}/reject")
    @ApiOperation("拒绝决策建议(RESTful)")
    public String rejectRecommendationRestful(
            @ApiParam(value = "建议ID", required = true) @PathVariable Long recommendationId,
            @ApiParam(value = "拒绝理由") @RequestParam(required = false) String reviewOpinion,
            @ApiParam(value = "拒绝人ID") @RequestParam(required = false) Long reviewUser,
            HttpServletResponse response) {
        return rejectRecommendation(recommendationId, reviewOpinion, reviewUser, response);
    }

    @PostMapping("/implement")
    @ApiOperation("实施决策建议")
    public String implementRecommendation(
            @ApiParam(value = "建议ID", required = true) @RequestParam Long recommendationId,
            @ApiParam(value = "实施结果") @RequestParam(required = false) String implementationResult,
            @ApiParam(value = "实施人ID") @RequestParam(required = false) Long implementUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入实施人ID，则使用当前登录用户
            Long implementUserId = implementUser;
            if (implementUserId == null) {
                TblStaffUtil user = userProvider.get();
                implementUserId = user != null ? getStaffId(user) : null;
            }

            boolean success = decisionRecommendationService.implementRecommendation(recommendationId, implementationResult, implementUserId);
            if (success) {
                return JsonBean.success("实施成功");
            } else {
                return JsonBean.error("实施失败");
            }

        } catch (Exception e) {
            log.error("实施决策建议失败，recommendationId: {}", recommendationId, e);
            return JsonBean.error("实施决策建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/{recommendationId}/implement")
    @ApiOperation("实施决策建议(RESTful)")
    public String implementRecommendationRestful(
            @ApiParam(value = "建议ID", required = true) @PathVariable Long recommendationId,
            @ApiParam(value = "实施结果") @RequestParam(required = false) String implementationResult,
            @ApiParam(value = "实施人ID") @RequestParam(required = false) Long implementUser,
            HttpServletResponse response) {
        return implementRecommendation(recommendationId, implementationResult, implementUser, response);
    }

    @PostMapping("/batch-review")
    @ApiOperation("批量审核决策建议")
    public String batchReviewRecommendations(
            @ApiParam(value = "建议ID数组", required = true) @RequestParam(value = "recommendationIds", required = false) List<Long> recommendationIds,
            @ApiParam(value = "建议状态") @RequestParam(required = false) String recommendationStatus,
            @ApiParam(value = "审核人ID") @RequestParam(required = false) Long reviewUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入审核人ID，则使用当前登录用户
            if (reviewUser == null) {
                TblStaffUtil user = userProvider.get();
                reviewUser = user != null ? getStaffId(user) : null;
            }

            if (recommendationIds == null || recommendationIds.isEmpty()) {
                return JsonBean.error("建议ID数组不能为空");
            }

            int successCount = 0;
            int failCount = 0;

            for (Long recommendationId : recommendationIds) {
                try {
                    boolean success = decisionRecommendationService.reviewRecommendation(recommendationId, null, reviewUser);
                    if (success) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    log.error("批量审核决策建议失败，recommendationId: {}", recommendationId, e);
                    failCount++;
                }
            }

            java.util.Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("successCount", successCount);
            resultMap.put("failCount", failCount);
            return new JsonBean(1, "批量审核完成，成功" + successCount + "条，失败" + failCount + "条", resultMap).toString();

        } catch (Exception e) {
            log.error("批量审核决策建议失败", e);
            return JsonBean.error("批量审核决策建议失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-implement")
    @ApiOperation("批量实施决策建议")
    public String batchImplementRecommendations(
            @ApiParam(value = "建议ID数组", required = true) @RequestParam(value = "recommendationIds", required = false) List<Long> recommendationIds,
            @ApiParam(value = "实施人ID") @RequestParam(required = false) Long implementUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入实施人ID，则使用当前登录用户
            if (implementUser == null) {
                TblStaffUtil user = userProvider.get();
                implementUser = user != null ? getStaffId(user) : null;
            }

            if (recommendationIds == null || recommendationIds.isEmpty()) {
                return JsonBean.error("建议ID数组不能为空");
            }

            int successCount = 0;
            int failCount = 0;

            for (Long recommendationId : recommendationIds) {
                try {
                    boolean success = decisionRecommendationService.implementRecommendation(recommendationId, null, implementUser);
                    if (success) {
                        successCount++;
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    log.error("批量实施决策建议失败，recommendationId: {}", recommendationId, e);
                    failCount++;
                }
            }

            java.util.Map<String, Object> resultMap = new java.util.HashMap<>();
            resultMap.put("successCount", successCount);
            resultMap.put("failCount", failCount);
            return new JsonBean(1, "批量实施完成，成功" + successCount + "条，失败" + failCount + "条", resultMap).toString();

        } catch (Exception e) {
            log.error("批量实施决策建议失败", e);
            return JsonBean.error("批量实施决策建议失败: " + e.getMessage());
        }
    }

    @PutMapping("/{recommendationId}/implementation-result")
    @ApiOperation("更新实施结果")
    public String updateImplementationResult(
            @ApiParam(value = "建议ID", required = true) @PathVariable Long recommendationId,
            @ApiParam(value = "实施结果", required = true) @RequestParam String implementationResult,
            @ApiParam(value = "更新人ID") @RequestParam(required = false) Long updateUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查建议是否存在
            DecisionRecommendation existing = decisionRecommendationService.getById(recommendationId);
            if (existing == null) {
                return JsonBean.error("决策建议不存在");
            }

            // 如果没有传入更新人ID，则使用当前登录用户
            if (updateUser == null) {
                TblStaffUtil user = userProvider.get();
                updateUser = user != null ? getStaffId(user) : null;
            }

            // 更新实施结果
            existing.setImplementationResult(implementationResult);
            existing.setUpdateTime(java.time.LocalDateTime.now());
            existing.setUpdateBy(updateUser);

            boolean success = decisionRecommendationService.updateById(existing);
            if (success) {
                return JsonBean.success("更新实施结果成功");
            } else {
                return JsonBean.error("更新实施结果失败");
            }

        } catch (Exception e) {
            log.error("更新实施结果失败，recommendationId: {}", recommendationId, e);
            return JsonBean.error("更新实施结果失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出决策建议")
    public void exportRecommendations(
            @ApiParam(value = "建议编号") @RequestParam(required = false) String recommendationNo,
            @ApiParam(value = "建议标题") @RequestParam(required = false) String recommendationTitle,
            @ApiParam(value = "建议类型") @RequestParam(required = false) String recommendationType,
            @ApiParam(value = "建议状态") @RequestParam(required = false) String recommendationStatus,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 构建查询参数
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("recommendationNo", recommendationNo);
            params.put("recommendationTitle", recommendationTitle);
            params.put("recommendationType", recommendationType);
            params.put("recommendationStatus", recommendationStatus);

            // 查询所有数据(不分页)
            com.baomidou.mybatisplus.core.metadata.IPage<DecisionRecommendation> iPage = decisionRecommendationService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10000),
                    params
            );

            java.util.List<DecisionRecommendation> recommendationList = iPage.getRecords();

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("决策建议_" + System.currentTimeMillis(), "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建Excel工作簿
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("决策建议");

            // 创建标题行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"建议编号", "建议标题", "建议类型", "建议状态", "优先级", "风险等级",
                               "预期收益", "审核意见", "生成时间", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            int rowNum = 1;
            for (DecisionRecommendation recommendation : recommendationList) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(recommendation.getRecommendationNo() != null ? recommendation.getRecommendationNo() : "");
                row.createCell(1).setCellValue(recommendation.getRecommendationTitle() != null ? recommendation.getRecommendationTitle() : "");
                row.createCell(2).setCellValue(recommendation.getRecommendationType() != null ? recommendation.getRecommendationType() : "");
                row.createCell(3).setCellValue(recommendation.getRecommendationStatus() != null ? recommendation.getRecommendationStatus() : "");
                row.createCell(4).setCellValue(recommendation.getPriority() != null ? recommendation.getPriority() : "");
                row.createCell(5).setCellValue(recommendation.getRiskLevel() != null ? recommendation.getRiskLevel() : "");
                row.createCell(6).setCellValue(recommendation.getExpectedBenefit() != null ? recommendation.getExpectedBenefit().toString() : "");
                row.createCell(7).setCellValue(recommendation.getReviewOpinion() != null ? recommendation.getReviewOpinion() : "");
                row.createCell(8).setCellValue(recommendation.getGenerateTime() != null ? recommendation.getGenerateTime().toString() : "");
                row.createCell(9).setCellValue(recommendation.getCreateTime() != null ? recommendation.getCreateTime().toString() : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出决策建议成功，共{}条记录", recommendationList.size());

        } catch (Exception e) {
            log.error("导出决策建议失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("导出失败: " + e.getMessage());
            } catch (java.io.IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        }
    }

    /**
     * 验证用户权限
     */
    private boolean validateUser() {
        try {
            // 开发环境: 暂时绕过认证验证
            return true;
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return false;
        }
    }

    /**
     * 获取当前用户的StaffId（处理BigDecimal到Long的转换）
     */
    private Long getStaffId(TblStaffUtil user) {
        if (user == null || user.getStaffid() == null) {
            return null;
        }
        return user.getStaffid().longValue();
    }

    /**
     * 获取组织的OrgId（处理BigDecimal到Long的转换）
     */
    private Long getOrgId(com.hbfk.entity.TblOrganizationUtil org) {
        if (org == null || org.getOrgid() == null) {
            return null;
        }
        return org.getOrgid().longValue();
    }

    /**
     * 自动生成建议编号
     * 格式: DR + 年月日 + 3位序号
     * 例如: DR20250225001
     */
    private String generateRecommendationNo() {
        // 获取当前日期的年月日
        String dateStr = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 查询今天已有的建议编号数量
        QueryWrapper<DecisionRecommendation> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("RECOMMENDATION_NO", "DR" + dateStr);
        long todayCount = decisionRecommendationService.count(queryWrapper);

        // 生成3位序号，补0
        String sequence = String.format("%03d", todayCount + 1);

        // 组装建议编号
        return "DR" + dateStr + sequence;
    }
}
