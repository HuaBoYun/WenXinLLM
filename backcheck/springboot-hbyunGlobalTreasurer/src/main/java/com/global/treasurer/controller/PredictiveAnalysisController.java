package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.annotation.FlexibleRequestBody;
import com.global.treasurer.entity.PredictiveAnalysis;
import com.global.treasurer.service.IPredictiveAnalysisService;
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
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 预测分析控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@RestController
@RequestMapping({"/financial/decision/predictive", "/xjgl/decision/predictive", "/centralaudit/decision/predictive", "/decision/predictive", "/predictiveAnalysis"})
@Api(tags = "预测分析管理")
public class PredictiveAnalysisController {
    private static final Logger log = LoggerFactory.getLogger(PredictiveAnalysisController.class);

    @Resource
    private IPredictiveAnalysisService predictiveAnalysisService;

    @Resource
    private UserProvider userProvider;

    @GetMapping({"/list", "/page"})
    @ApiOperation("获取预测分析列表(GET)")
    public String getAnalysisListGet(
            @ApiParam(value = "页码", example = "1") @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam(value = "分析名称") @RequestParam(required = false) String analysisName,
            @ApiParam(value = "预测类型") @RequestParam(required = false) String predictionType,
            @ApiParam(value = "分析状态") @RequestParam(required = false) String analysisStatus,
            HttpServletResponse response) {

        try {
            log.info("========== 预测分析查询接口(GET) ==========");
            log.info("接收参数 - pageNo: {}, pageSize: {}", pageNum, pageSize);
            log.info("接收参数 - analysisName: {}, predictionType: {}, analysisStatus: {}",
                    analysisName, predictionType, analysisStatus);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> queryParams = new HashMap<>();
            if (analysisName != null && !analysisName.trim().isEmpty()) {
                queryParams.put("analysisName", analysisName);
            }
            if (predictionType != null && !predictionType.trim().isEmpty()) {
                queryParams.put("analysisType", predictionType);
            }
            if (analysisStatus != null && !analysisStatus.trim().isEmpty()) {
                queryParams.put("analysisStatus", analysisStatus);
            }

            log.info("构建的queryParams: {}", queryParams);

            // 分页查询
            Page<PredictiveAnalysis> page = new Page<>(pageNum, pageSize);
            IPage<PredictiveAnalysis> result = predictiveAnalysisService.selectPage(page, queryParams);

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
            log.error("获取预测分析列表失败", e);
            return JsonBean.error("获取预测分析列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取预测分析详情")
    public String getAnalysisDetail(
            @ApiParam(value = "分析ID", required = true) @RequestParam Long analysisId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            PredictiveAnalysis analysis = predictiveAnalysisService.getById(analysisId);
            if (analysis == null) {
                return JsonBean.error("预测分析不存在");
            }

            return new JsonBean(1, "查询成功", analysis).toString();

        } catch (Exception e) {
            log.error("获取预测分析详情失败，analysisId: {}", analysisId, e);
            return JsonBean.error("获取预测分析详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/{analysisId}")
    @ApiOperation("获取预测分析详情(RESTful)")
    public String getAnalysisById(
            @ApiParam(value = "分析ID", required = true) @PathVariable Long analysisId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            PredictiveAnalysis analysis = predictiveAnalysisService.getById(analysisId);
            if (analysis == null) {
                return JsonBean.error("预测分析不存在");
            }

            return new JsonBean(1, "查询成功", analysis).toString();

        } catch (Exception e) {
            log.error("获取预测分析详情失败，analysisId: {}", analysisId, e);
            return JsonBean.error("获取预测分析详情失败: " + e.getMessage());
        }
    }

    @PostMapping(value = "/create", consumes = "application/json")
    @ApiOperation("创建预测分析")
    public String createAnalysis(@RequestBody PredictiveAnalysis analysis, HttpServletResponse response) {

        try {
            log.info("[创建预测分析] 接收到请求: analysisNo={}, analysisName={}, predictionType={}, orgId={}",
                analysis.getAnalysisNo(), analysis.getAnalysisName(), analysis.getPredictionType(), analysis.getOrgId());

            // 权限验证
            if (!validateUser()) {
                log.warn("[创建预测分析] 用户验证失败");
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(analysis.getAnalysisNo())) {
                return JsonBean.error("分析编号不能为空");
            }
            if (!StringUtils.hasText(analysis.getAnalysisName())) {
                return JsonBean.error("分析名称不能为空");
            }
            if (!StringUtils.hasText(analysis.getPredictionType())) {
                return JsonBean.error("预测类型不能为空");
            }

            // 设置默认值
            if (analysis.getAnalysisStatus() == null) {
                analysis.setAnalysisStatus("PENDING");
            }
            analysis.setCreateTime(java.time.LocalDateTime.now());
            analysis.setUpdateTime(java.time.LocalDateTime.now());
            analysis.setDelFlag("0");

            // 设置创建人
            try {
                TblStaffUtil user = userProvider.get();
                if (user != null) {
                    analysis.setCreateBy(getStaffId(user));
                    analysis.setUpdateBy(getStaffId(user));
                    if (user.getCurrentOrg() != null) {
                        analysis.setOrgId(getOrgId(user.getCurrentOrg()));
                    }
                } else {
                    log.warn("用户信息为空，使用默认值");
                    analysis.setCreateBy(1L);
                    analysis.setUpdateBy(1L);
                    if (analysis.getOrgId() == null) {
                        analysis.setOrgId(1L);
                    }
                }
            } catch (Exception e) {
                // 开发环境：忽略用户信息获取失败，使用默认值
                log.warn("获取用户信息失败，使用默认值: {}", e.getMessage());
                analysis.setCreateBy(1L);
                analysis.setUpdateBy(1L);
                if (analysis.getOrgId() == null) {
                    analysis.setOrgId(1L);
                }
            }

            // 确保 orgId 不为空（二次保险）
            if (analysis.getOrgId() == null) {
                log.warn("[创建预测分析] orgId为空，设置为默认值1");
                analysis.setOrgId(1L);
            }

            log.info("[创建预测分析] 准备保存数据，最终数据: {}", analysis);

            boolean success = predictiveAnalysisService.save(analysis);

            log.info("[创建预测分析] 保存结果: success={}, analysisId={}", success, analysis.getAnalysisId());

            if (success) {
                return JsonBean.success("创建成功");
            } else {
                log.error("[创建预测分析] 保存失败，原因未知");
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("[创建预测分析] 系统异常，analysisNo: {}", analysis.getAnalysisNo(), e);
            return JsonBean.error("创建预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新预测分析")
    public String updateAnalysis(@FlexibleRequestBody PredictiveAnalysis analysis, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (analysis.getAnalysisId() == null) {
                return JsonBean.error("分析ID不能为空");
            }

            // 检查是否存在
            PredictiveAnalysis existing = predictiveAnalysisService.getById(analysis.getAnalysisId());
            if (existing == null) {
                return JsonBean.error("预测分析不存在");
            }

            analysis.setUpdateTime(java.time.LocalDateTime.now());

            // 设置更新人
            try {
                TblStaffUtil user = userProvider.get();
                if (user != null) {
                    analysis.setUpdateBy(getStaffId(user));
                }
            } catch (Exception e) {
                // 开发环境：忽略用户信息获取失败，使用默认值
                log.debug("获取用户信息失败，使用默认值: {}", e.getMessage());
                analysis.setUpdateBy(1L);
            }

            boolean success = predictiveAnalysisService.updateById(analysis);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新预测分析失败", e);
            return JsonBean.error("更新预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除预测分析")
    public String deleteAnalysis(
            @ApiParam(value = "分析ID", required = true) @RequestParam Long analysisId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            PredictiveAnalysis existing = predictiveAnalysisService.getById(analysisId);
            if (existing == null) {
                return JsonBean.error("预测分析不存在");
            }

            // 逻辑删除
            existing.setDelFlag("1");
            existing.setUpdateTime(java.time.LocalDateTime.now());

            boolean success = predictiveAnalysisService.updateById(existing);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除预测分析失败，analysisId: {}", analysisId, e);
            return JsonBean.error("删除预测分析失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{analysisId}")
    @ApiOperation("删除预测分析(RESTful)")
    public String deleteAnalysisRestful(
            @ApiParam(value = "分析ID", required = true) @PathVariable Long analysisId,
            HttpServletResponse response) {

        try {
            log.info("========== 删除预测分析(RESTful) ==========");
            log.info("接收参数 - 分析ID: {}", analysisId);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            PredictiveAnalysis existing = predictiveAnalysisService.getById(analysisId);
            if (existing == null) {
                log.warn("预测分析不存在，analysisId: {}", analysisId);
                return JsonBean.error("预测分析不存在");
            }

            // 逻辑删除
            existing.setDelFlag("1");
            existing.setUpdateTime(java.time.LocalDateTime.now());

            boolean success = predictiveAnalysisService.updateById(existing);
            if (success) {
                log.info("预测分析删除成功，analysisId: {}", analysisId);
                return JsonBean.success("删除成功");
            } else {
                log.error("预测分析删除失败，analysisId: {}", analysisId);
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除预测分析失败，analysisId: {}", analysisId, e);
            return JsonBean.error("删除预测分析失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除预测分析")
    public String batchDeleteAnalysis(
            @ApiParam(value = "分析ID数组", required = true) @RequestParam(value = "analysisIds", required = false) List<Long> analysisIds,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (analysisIds == null || analysisIds.isEmpty()) {
                return JsonBean.error("分析ID列表不能为空");
            }

            int successCount = 0;
            int failCount = 0;

            for (Long analysisId : analysisIds) {
                try {
                    // 检查是否存在
                    PredictiveAnalysis existing = predictiveAnalysisService.getById(analysisId);
                    if (existing != null) {
                        // 逻辑删除
                        existing.setDelFlag("1");
                        existing.setUpdateTime(java.time.LocalDateTime.now());
                        boolean success = predictiveAnalysisService.updateById(existing);
                        if (success) {
                            successCount++;
                        } else {
                            failCount++;
                        }
                    } else {
                        failCount++;
                    }
                } catch (Exception e) {
                    log.error("删除预测分析失败，analysisId: {}", analysisId, e);
                    failCount++;
                }
            }

            java.util.Map<String, Object> result = new java.util.HashMap<>();
            result.put("total", analysisIds.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);

            if (failCount == 0) {
                return new JsonBean(1, "批量删除成功", result).toString();
            } else if (successCount == 0) {
                return JsonBean.error("批量删除全部失败");
            } else {
                return new JsonBean(1, "批量删除部分成功", result).toString();
            }

        } catch (Exception e) {
            log.error("批量删除预测分析失败", e);
            return JsonBean.error("批量删除预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/execute")
    @ApiOperation("执行预测分析")
    public String executeAnalysis(
            @ApiParam(value = "分析ID", required = true) @RequestParam Long analysisId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 获取当前用户ID
            TblStaffUtil user = userProvider.get();
            Long executeUserId = user != null ? getStaffId(user) : null;

            boolean success = predictiveAnalysisService.executeAnalysis(analysisId, executeUserId);
            if (success) {
                return JsonBean.success("预测分析执行成功");
            } else {
                return JsonBean.error("预测分析执行失败");
            }

        } catch (Exception e) {
            log.error("执行预测分析失败，analysisId: {}", analysisId, e);
            return JsonBean.error("执行预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/{analysisId}/execute")
    @ApiOperation("执行预测分析(RESTful)")
    public String executeAnalysisRestful(
            @ApiParam(value = "分析ID", required = true) @PathVariable Long analysisId,
            @ApiParam(value = "执行用户ID") @RequestParam(required = false) Long executeUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入executeUser，则从当前用户获取
            Long executeUserId = executeUser;
            if (executeUserId == null) {
                TblStaffUtil user = userProvider.get();
                executeUserId = user != null ? getStaffId(user) : null;
            }

            boolean success = predictiveAnalysisService.executeAnalysis(analysisId, executeUserId);
            if (success) {
                // 返回预测结果
                PredictiveAnalysis analysis = predictiveAnalysisService.getById(analysisId);
                java.util.Map<String, Object> result = new java.util.HashMap<>();
                result.put("analysisId", analysisId);
                result.put("status", analysis.getAnalysisStatus());
                result.put("predictionResult", analysis.getPredictionResult());
                return new JsonBean(1, "预测分析执行成功", result).toString();
            } else {
                return JsonBean.error("预测分析执行失败");
            }

        } catch (Exception e) {
            log.error("执行预测分析失败，analysisId: {}", analysisId, e);
            return JsonBean.error("执行预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/cancel")
    @ApiOperation("取消预测分析")
    public String cancelAnalysis(
            @ApiParam(value = "分析ID", required = true) @RequestParam Long analysisId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 获取当前用户ID
            TblStaffUtil user = userProvider.get();
            Long cancelUserId = user != null ? getStaffId(user) : null;

            boolean success = predictiveAnalysisService.cancelAnalysis(analysisId, cancelUserId);
            if (success) {
                return JsonBean.success("预测分析取消成功");
            } else {
                return JsonBean.error("预测分析取消失败");
            }

        } catch (Exception e) {
            log.error("取消预测分析失败，analysisId: {}", analysisId, e);
            return JsonBean.error("取消预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/{analysisId}/cancel")
    @ApiOperation("取消预测分析(RESTful)")
    public String cancelAnalysisRestful(
            @ApiParam(value = "分析ID", required = true) @PathVariable Long analysisId,
            @ApiParam(value = "取消用户ID") @RequestParam(required = false) Long cancelUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入cancelUser，则从当前用户获取
            Long cancelUserId = cancelUser;
            if (cancelUserId == null) {
                TblStaffUtil user = userProvider.get();
                cancelUserId = user != null ? getStaffId(user) : null;
            }

            boolean success = predictiveAnalysisService.cancelAnalysis(analysisId, cancelUserId);
            if (success) {
                return JsonBean.success("预测分析取消成功");
            } else {
                return JsonBean.error("预测分析取消失败");
            }

        } catch (Exception e) {
            log.error("取消预测分析失败，analysisId: {}", analysisId, e);
            return JsonBean.error("取消预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/retry")
    @ApiOperation("重试失败的预测分析")
    public String retryAnalysis(
            @ApiParam(value = "分析ID", required = true) @RequestParam Long analysisId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 获取当前用户ID
            TblStaffUtil user = userProvider.get();
            Long retryUserId = user != null ? getStaffId(user) : null;

            boolean success = predictiveAnalysisService.retryAnalysis(analysisId, retryUserId);
            if (success) {
                return JsonBean.success("预测分析重试成功");
            } else {
                return JsonBean.error("预测分析重试失败");
            }

        } catch (Exception e) {
            log.error("重试预测分析失败，analysisId: {}", analysisId, e);
            return JsonBean.error("重试预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/{analysisId}/retry")
    @ApiOperation("重试失败的预测分析(RESTful)")
    public String retryAnalysisRestful(
            @ApiParam(value = "分析ID", required = true) @PathVariable Long analysisId,
            @ApiParam(value = "重试用户ID") @RequestParam(required = false) Long retryUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入retryUser，则从当前用户获取
            Long retryUserId = retryUser;
            if (retryUserId == null) {
                TblStaffUtil user = userProvider.get();
                retryUserId = user != null ? getStaffId(user) : null;
            }

            boolean success = predictiveAnalysisService.retryAnalysis(analysisId, retryUserId);
            if (success) {
                return JsonBean.success("预测分析重试成功");
            } else {
                return JsonBean.error("预测分析重试失败");
            }

        } catch (Exception e) {
            log.error("重试预测分析失败，analysisId: {}", analysisId, e);
            return JsonBean.error("重试预测分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateActualResult")
    @ApiOperation("更新实际结果")
    public String updateActualResult(
            @ApiParam(value = "分析ID", required = true) @RequestParam Long analysisId,
            @ApiParam(value = "实际结果(JSON格式)", required = true) @RequestParam String actualResult,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 获取当前用户ID
            TblStaffUtil user = userProvider.get();
            Long updateUserId = user != null ? getStaffId(user) : null;

            boolean success = predictiveAnalysisService.updateActualResult(analysisId, actualResult, updateUserId);
            if (success) {
                return JsonBean.success("更新实际结果成功");
            } else {
                return JsonBean.error("更新实际结果失败");
            }

        } catch (Exception e) {
            log.error("更新实际结果失败，analysisId: {}", analysisId, e);
            return JsonBean.error("更新实际结果失败: " + e.getMessage());
        }
    }

    @PutMapping("/{analysisId}/actual-result")
    @ApiOperation("更新实际结果(RESTful)")
    public String updateActualResultRestful(
            @ApiParam(value = "分析ID", required = true) @PathVariable Long analysisId,
            @ApiParam(value = "实际结果(JSON格式)", required = true) @RequestParam String actualResult,
            @ApiParam(value = "更新用户ID") @RequestParam(required = false) Long updateUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入updateUser，则从当前用户获取
            Long updateUserId = updateUser;
            if (updateUserId == null) {
                TblStaffUtil user = userProvider.get();
                updateUserId = user != null ? getStaffId(user) : null;
            }

            boolean success = predictiveAnalysisService.updateActualResult(analysisId, actualResult, updateUserId);
            if (success) {
                return JsonBean.success("更新实际结果成功");
            } else {
                return JsonBean.error("更新实际结果失败");
            }

        } catch (Exception e) {
            log.error("更新实际结果失败，analysisId: {}", analysisId, e);
            return JsonBean.error("更新实际结果失败: " + e.getMessage());
        }
    }

    @PostMapping("/calculateAccuracy")
    @ApiOperation("计算预测准确率")
    public String calculateAccuracy(
            @ApiParam(value = "分析ID", required = true) @RequestParam Long analysisId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 获取当前用户ID
            TblStaffUtil user = userProvider.get();
            Long calculateUserId = user != null ? getStaffId(user) : null;

            boolean success = predictiveAnalysisService.calculateAccuracy(analysisId, calculateUserId);
            if (success) {
                return JsonBean.success("计算准确率成功");
            } else {
                return JsonBean.error("计算准确率失败");
            }

        } catch (Exception e) {
            log.error("计算准确率失败，analysisId: {}", analysisId, e);
            return JsonBean.error("计算准确率失败: " + e.getMessage());
        }
    }

    @PostMapping("/{analysisId}/calculate-accuracy")
    @ApiOperation("计算预测准确率(RESTful)")
    public String calculateAccuracyRestful(
            @ApiParam(value = "分析ID", required = true) @PathVariable Long analysisId,
            @ApiParam(value = "计算用户ID") @RequestParam(required = false) Long calculateUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入calculateUser，则从当前用户获取
            Long calculateUserId = calculateUser;
            if (calculateUserId == null) {
                TblStaffUtil user = userProvider.get();
                calculateUserId = user != null ? getStaffId(user) : null;
            }

            boolean success = predictiveAnalysisService.calculateAccuracy(analysisId, calculateUserId);
            if (success) {
                // 返回准确率数据
                PredictiveAnalysis analysis = predictiveAnalysisService.getById(analysisId);
                java.util.Map<String, Object> result = new java.util.HashMap<>();
                result.put("analysisId", analysisId);
                result.put("accuracy", analysis.getAccuracy());
                result.put("predictionResult", analysis.getPredictionResult());
                result.put("actualResult", analysis.getActualResult());
                return new JsonBean(1, "计算准确率成功", result).toString();
            } else {
                return JsonBean.error("计算准确率失败");
            }

        } catch (Exception e) {
            log.error("计算准确率失败，analysisId: {}", analysisId, e);
            return JsonBean.error("计算准确率失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出预测分析")
    public void exportAnalysis(
            @ApiParam(value = "分析编号") @RequestParam(required = false) String analysisNo,
            @ApiParam(value = "分析名称") @RequestParam(required = false) String analysisName,
            @ApiParam(value = "分析类型") @RequestParam(required = false) String analysisType,
            @ApiParam(value = "分析状态") @RequestParam(required = false) String analysisStatus,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 构建查询参数
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("analysisNo", analysisNo);
            params.put("analysisName", analysisName);
            params.put("analysisType", analysisType);
            params.put("analysisStatus", analysisStatus);

            // 查询所有数据(不分页)
            com.baomidou.mybatisplus.core.metadata.IPage<PredictiveAnalysis> iPage = predictiveAnalysisService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10000),
                    params
            );

            java.util.List<PredictiveAnalysis> analysisList = iPage.getRecords();

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("预测分析_" + System.currentTimeMillis(), "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建Excel工作簿
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("预测分析");

            // 创建标题行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"分析编号", "分析名称", "分析类型", "分析状态", "预测目标", "预测周期",
                               "准确率", "开始时间", "结束时间", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            int rowNum = 1;
            for (PredictiveAnalysis analysis : analysisList) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(analysis.getAnalysisNo() != null ? analysis.getAnalysisNo() : "");
                row.createCell(1).setCellValue(analysis.getAnalysisName() != null ? analysis.getAnalysisName() : "");
                row.createCell(2).setCellValue(analysis.getAnalysisType() != null ? analysis.getAnalysisType() : "");
                row.createCell(3).setCellValue(analysis.getAnalysisStatus() != null ? analysis.getAnalysisStatus() : "");
                row.createCell(4).setCellValue(analysis.getPredictionTarget() != null ? analysis.getPredictionTarget() : "");
                row.createCell(5).setCellValue(analysis.getPredictionPeriod() != null ? analysis.getPredictionPeriod() : "");
                row.createCell(6).setCellValue(analysis.getAccuracy() != null ? analysis.getAccuracy().toString() : "");
                row.createCell(7).setCellValue(analysis.getStartTime() != null ? analysis.getStartTime().toString() : "");
                row.createCell(8).setCellValue(analysis.getEndTime() != null ? analysis.getEndTime().toString() : "");
                row.createCell(9).setCellValue(analysis.getCreateTime() != null ? analysis.getCreateTime().toString() : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出预测分析成功，共{}条记录", analysisList.size());

        } catch (Exception e) {
            log.error("导出预测分析失败", e);
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
}
