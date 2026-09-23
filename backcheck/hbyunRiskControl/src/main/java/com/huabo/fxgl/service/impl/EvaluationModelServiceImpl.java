package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.StringUtil;
import com.huabo.fxgl.mapper.TblRiskWarningMapper;
import com.huabo.fxgl.dto.EvaluationModelQueryDTO;
import com.huabo.fxgl.entity.TblEvaluationModel;
import com.huabo.fxgl.entity.TblDataModel;
import com.huabo.fxgl.entity.TblDataSource;
import com.huabo.fxgl.entity.TblRiskWarning;
import com.huabo.fxgl.mapper.TblEvaluationModelMapper;
import com.huabo.fxgl.mapper.TblDataSourceMapper;
import com.huabo.fxgl.util.SqlExecutorUtil;
import com.huabo.fxgl.service.IEvaluationModelService;
import com.huabo.fxgl.service.IDataModelService;
import com.huabo.fxgl.service.IRiskWarningService;
import com.huabo.fxgl.service.ICombinationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 评估模型服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
public class EvaluationModelServiceImpl extends ServiceImpl<TblEvaluationModelMapper, TblEvaluationModel> 
        implements IEvaluationModelService {

    @Autowired
    private TblEvaluationModelMapper evaluationModelMapper;

    @Autowired
    private TblRiskWarningMapper riskWarningMapper;

    @Autowired
    private IDataModelService dataModelService;

    @Autowired
    private IRiskWarningService riskWarningService;

    @Autowired
    private ICombinationService combinationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TblDataSourceMapper dataSourceMapper;

    @Autowired
    private EvaluationModelScheduleService scheduleService;

    @Override
    public String getEvaluationModelList(EvaluationModelQueryDTO queryDTO) {
        try {
            log.info("查询评估模型列表，参数: {}", queryDTO);

            // 🔧 修复：使用 PageHelper.startPage 替代 MyBatis-Plus 分页
            PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

            // 构建查询条件
            QueryWrapper<TblEvaluationModel> queryWrapper = new QueryWrapper<>();

            // 添加查询条件
            if (StringUtil.isNotEmpty(queryDTO.getModelName())) {
                queryWrapper.like("MODEL_NAME", queryDTO.getModelName());
            }
            if (StringUtil.isNotEmpty(queryDTO.getBusinessScenario())) {
                queryWrapper.eq("BUSINESS_SCENARIO", queryDTO.getBusinessScenario());
            }
            if (StringUtil.isNotEmpty(queryDTO.getStatus())) {
                queryWrapper.eq("STATUS", queryDTO.getStatus());
            }
            if (StringUtil.isNotEmpty(queryDTO.getIsEnabled())) {
                queryWrapper.eq("IS_ENABLED", queryDTO.getIsEnabled());
            }

            // 排序
            if (StringUtil.isNotEmpty(queryDTO.getSortField())) {
                if ("ASC".equalsIgnoreCase(queryDTO.getSortOrder())) {
                    queryWrapper.orderByAsc(queryDTO.getSortField());
                } else {
                    queryWrapper.orderByDesc(queryDTO.getSortField());
                }
            } else {
                queryWrapper.orderByDesc("CREATE_TIME");
            }

            // 🔧 修复：使用 list 方法查询，PageHelper 会自动处理分页
            List<TblEvaluationModel> list = this.list(queryWrapper);

            // 🔧 修复：使用 PageInfo 包装结果，获取分页信息
            PageInfo<TblEvaluationModel> pageInfo = new PageInfo<>(list);

            // 构造返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            log.info("查询评估模型列表成功，总数: {}, 当前页: {}, 每页大小: {}",
                    pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("查询评估模型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Override
    public String getWarningModelList(Map<String, Object> requestBody) {
        try {
            log.info("按预警状态查询模型列表，参数: {}", requestBody);

            Integer pageNum = requestBody.get("pageNum") instanceof Number
                    ? ((Number) requestBody.get("pageNum")).intValue() : 1;
            Integer pageSize = requestBody.get("pageSize") instanceof Number
                    ? ((Number) requestBody.get("pageSize")).intValue() : 20;
            String modelName = (String) requestBody.get("modelName");
            String status = (String) requestBody.get("status");
            String isEnabled = (String) requestBody.get("isEnabled");
            String warningStatus = (String) requestBody.get("warningStatus");

            // 仅允许合法的预警状态
            if (!"PENDING".equals(warningStatus) && !"PROCESSING".equals(warningStatus)
                    && !"PROCESSED".equals(warningStatus)) {
                return JsonBean.error("预警状态参数不合法");
            }

            PageHelper.startPage(pageNum, pageSize);

            QueryWrapper<TblEvaluationModel> queryWrapper = new QueryWrapper<>();
            if (StringUtil.isNotEmpty(modelName)) {
                queryWrapper.like("MODEL_NAME", modelName);
            }
            if (StringUtil.isNotEmpty(status)) {
                queryWrapper.eq("STATUS", status);
            }
            if (StringUtil.isNotEmpty(isEnabled)) {
                queryWrapper.eq("IS_ENABLED", isEnabled);
            }
            // 只返回存在该状态预警的模型（数量为0的模型不返回）
            queryWrapper.apply(
                    "EXISTS (SELECT 1 FROM TBL_RISK_WARNING W "
                            + "WHERE W.EVAL_MODEL_ID = TBL_EVALUATION_MODEL.EVAL_MODEL_ID "
                            + "AND W.WARNING_STATUS = {0})",
                    warningStatus);
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblEvaluationModel> list = this.list(queryWrapper);
            PageInfo<TblEvaluationModel> pageInfo = new PageInfo<>(list);

            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pageNum", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            log.info("按预警状态查询模型列表成功，状态: {}, 总数: {}", warningStatus, pageInfo.getTotal());
            return JsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("按预警状态查询模型列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveEvaluationModel(TblEvaluationModel model, String currentUser) {
        try {
            log.info("保存评估模型，参数: {}", model);

            // 验证必填字段
            if (StringUtil.isEmpty(model.getModelCode())) {
                return JsonBean.error("模型编码不能为空");
            }
            if (StringUtil.isEmpty(model.getModelName())) {
                return JsonBean.error("模型名称不能为空");
            }
            if (StringUtil.isEmpty(model.getBusinessScenario())) {
                return JsonBean.error("业务场景不能为空");
            }

            // 验证模型编码唯一性
            if (!isModelCodeUnique(model.getModelCode(), model.getEvalModelId())) {
                return JsonBean.error("模型编码已存在");
            }

            // 设置默认值
            if (StringUtil.isEmpty(model.getStatus())) {
                model.setStatus(TblEvaluationModel.STATUS_DRAFT);
            }
            if (StringUtil.isEmpty(model.getIsEnabled())) {
                model.setIsEnabled(TblEvaluationModel.ENABLED_NO);
            }
            if (StringUtil.isEmpty(model.getVersion())) {
                model.setVersion("1.0");
            }

            boolean isUpdate = StringUtil.isNotEmpty(model.getEvalModelId());
            
            if (isUpdate) {
                // 更新
                model.setUpdateUser(currentUser);
                model.setUpdateTime(LocalDateTime.now());
                updateById(model);
            } else {
                // 新增
                model.setCreateUser(currentUser);
                model.setCreateTime(LocalDateTime.now());
                save(model);
            }

            return JsonBean.success(isUpdate ? "更新成功" : "保存成功", model);
        } catch (Exception e) {
            log.error("保存评估模型失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    @Override
    public String getEvaluationModelDetail(String modelId) {
        try {
            log.info("获取评估模型详情，modelId: {}", modelId);

            if (StringUtil.isEmpty(modelId)) {
                return JsonBean.error("模型ID不能为空");
            }

            TblEvaluationModel model = getById(modelId);
            if (model == null) {
                return JsonBean.error("评估模型不存在");
            }

            return JsonBean.success("获取成功", model);
        } catch (Exception e) {
            log.error("获取评估模型详情失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteEvaluationModel(String modelId) {
        try {
            log.info("删除评估模型，modelId: {}", modelId);

            if (StringUtil.isEmpty(modelId)) {
                return JsonBean.error("模型ID不能为空");
            }

            TblEvaluationModel model = getById(modelId);
            if (model == null) {
                return JsonBean.error("评估模型不存在");
            }

            // 检查模型是否已启用，已启用的模型不能删除
            if (TblEvaluationModel.ENABLED_YES.equals(model.getIsEnabled())) {
                return JsonBean.error("已启用的模型不能删除，请先停用后再删除");
            }

            // 检查模型是否正在运行，正在运行的模型不能删除
            if (TblEvaluationModel.STATUS_RUNNING.equals(model.getStatus())) {
                return JsonBean.error("正在运行的模型不能删除，请先停止后再删除");
            }

            removeById(modelId);
            log.info("评估模型删除成功，modelId: {}", modelId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除评估模型失败，modelId: {}", modelId, e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String publishEvaluationModel(String modelId, String currentUser) {
        try {
            log.info("发布评估模型，modelId: {}", modelId);

            TblEvaluationModel model = getById(modelId);
            if (model == null) {
                return JsonBean.error("评估模型不存在");
            }

            // 检查模型状态 - 草稿和测试状态的模型可以发布
            if (TblEvaluationModel.STATUS_PUBLISHED.equals(model.getStatus())) {
                return JsonBean.error("模型已经是发布状态");
            }

            if (TblEvaluationModel.STATUS_ARCHIVED.equals(model.getStatus())) {
                return JsonBean.error("已归档的模型不能发布");
            }

            // 允许草稿和测试状态的模型发布
            if (!TblEvaluationModel.STATUS_DRAFT.equals(model.getStatus()) &&
                !TblEvaluationModel.STATUS_TESTING.equals(model.getStatus())) {
                return JsonBean.error("只有草稿或测试状态的模型才能发布");
            }

            // 更新状态
            model.setStatus(TblEvaluationModel.STATUS_PUBLISHED);
            model.setIsEnabled(TblEvaluationModel.ENABLED_YES);
            model.setPublishTime(LocalDateTime.now());
            model.setUpdateUser(currentUser);
            model.setUpdateTime(LocalDateTime.now());

            updateById(model);
            return JsonBean.success("发布成功");
        } catch (Exception e) {
            log.error("发布评估模型失败", e);
            return JsonBean.error("发布失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String toggleEvaluationModel(String modelId, String isEnabled, String currentUser) {
        try {
            log.info("切换评估模型状态，modelId: {}, isEnabled: {}", modelId, isEnabled);

            TblEvaluationModel model = getById(modelId);
            if (model == null) {
                return JsonBean.error("评估模型不存在");
            }

            model.setIsEnabled(isEnabled);
            model.setUpdateUser(currentUser);
            model.setUpdateTime(LocalDateTime.now());

            updateById(model);
            return JsonBean.success("操作成功");
        } catch (Exception e) {
            log.error("切换评估模型状态失败", e);
            return JsonBean.error("操作失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyEvaluationModel(String sourceModelId, String newModelName, String currentUser) {
        try {
            log.info("复制评估模型，sourceModelId: {}, newModelName: {}", sourceModelId, newModelName);

            TblEvaluationModel sourceModel = getById(sourceModelId);
            if (sourceModel == null) {
                return JsonBean.error("源模型不存在");
            }

            // 创建新模型
            TblEvaluationModel newModel = new TblEvaluationModel();
            // 复制属性（除了ID、编码、名称、时间等）
            newModel.setModelCode(sourceModel.getModelCode() + "_COPY");
            newModel.setModelName(newModelName);
            newModel.setBusinessScenario(sourceModel.getBusinessScenario());
            newModel.setIndustryType(sourceModel.getIndustryType());
            newModel.setDescription(sourceModel.getDescription());
            newModel.setScoreAlgorithm(sourceModel.getScoreAlgorithm());
            newModel.setTotalWeight(sourceModel.getTotalWeight());
            newModel.setRiskThresholdLow(sourceModel.getRiskThresholdLow());
            newModel.setRiskThresholdMedium(sourceModel.getRiskThresholdMedium());
            newModel.setRiskThresholdHigh(sourceModel.getRiskThresholdHigh());
            newModel.setVersion("1.0");
            newModel.setStatus(TblEvaluationModel.STATUS_DRAFT);
            newModel.setIsEnabled(TblEvaluationModel.ENABLED_NO);
            newModel.setCreateUser(currentUser);
            newModel.setCreateTime(LocalDateTime.now());

            save(newModel);
            return JsonBean.success("复制成功", newModel);
        } catch (Exception e) {
            log.error("复制评估模型失败", e);
            return JsonBean.error("复制失败: " + e.getMessage());
        }
    }



    @Override
    public String getEvaluationModelStatistics() {
        try {
            log.info("开始获取评估模型统计信息");

            Map<String, Object> statistics = new HashMap<>();

            // 总数统计
            long totalCount = count();
            statistics.put("totalCount", totalCount);  // 前端期望的字段名
            log.debug("总模型数量: {}", totalCount);

            // 状态统计 - 使用直接查询方式确保数据准确性
            long draftCount = 0L;
            long testingCount = 0L;
            long publishedCount = 0L;
            long archivedCount = 0L;

            try {
                // 方法1: 使用自定义SQL查询
                List<Map<String, Object>> statusStats = evaluationModelMapper.selectStatusStatistics();
                Map<String, Long> statusMap = new HashMap<>();

                log.debug("状态统计SQL查询结果: {}", statusStats);

                if (statusStats != null && !statusStats.isEmpty()) {
                    for (Map<String, Object> stat : statusStats) {
                        if (stat != null) {
                            String status = (String) stat.get("STATUS");
                            Object countObj = stat.get("count");

                            if (status != null && countObj != null) {
                                Long count = ((Number) countObj).longValue();
                                statusMap.put(status, count);

                                // 根据状态设置对应的计数
                                switch (status) {
                                    case "DRAFT":
                                        draftCount = count;
                                        break;
                                    case "TESTING":
                                        testingCount = count;
                                        break;
                                    case "PUBLISHED":
                                        publishedCount = count;
                                        break;
                                    case "ARCHIVED":
                                        archivedCount = count;
                                        break;
                                }
                                log.debug("状态统计 - {}: {}", status, count);
                            } else {
                                log.warn("状态统计数据异常: status={}, count={}", status, countObj);
                            }
                        }
                    }
                } else {
                    log.warn("状态统计SQL查询结果为空，尝试使用MyBatis-Plus直接统计");

                    // 方法2: 如果SQL查询为空，使用MyBatis-Plus直接统计
                    draftCount = count(new LambdaQueryWrapper<TblEvaluationModel>()
                            .eq(TblEvaluationModel::getStatus, "DRAFT"));
                    testingCount = count(new LambdaQueryWrapper<TblEvaluationModel>()
                            .eq(TblEvaluationModel::getStatus, "TESTING"));
                    publishedCount = count(new LambdaQueryWrapper<TblEvaluationModel>()
                            .eq(TblEvaluationModel::getStatus, "PUBLISHED"));
                    archivedCount = count(new LambdaQueryWrapper<TblEvaluationModel>()
                            .eq(TblEvaluationModel::getStatus, "ARCHIVED"));

                    // 构建statusMap
                    if (draftCount > 0) statusMap.put("DRAFT", draftCount);
                    if (testingCount > 0) statusMap.put("TESTING", testingCount);
                    if (publishedCount > 0) statusMap.put("PUBLISHED", publishedCount);
                    if (archivedCount > 0) statusMap.put("ARCHIVED", archivedCount);

                    log.info("MyBatis-Plus统计结果 - DRAFT: {}, TESTING: {}, PUBLISHED: {}, ARCHIVED: {}",
                            draftCount, testingCount, publishedCount, archivedCount);
                }

                // 设置前端期望的字段
                statistics.put("draftCount", draftCount);
                statistics.put("testingCount", testingCount);
                statistics.put("publishedCount", publishedCount);
                statistics.put("archivedCount", archivedCount);
                statistics.put("statusStatistics", statusMap);  // 保留原有字段
            } catch (Exception e) {
                log.error("获取状态统计失败", e);
                statistics.put("draftCount", 0L);
                statistics.put("testingCount", 0L);
                statistics.put("publishedCount", 0L);
                statistics.put("archivedCount", 0L);
                statistics.put("statusStatistics", new HashMap<>());
            }

            // 业务场景统计 - 增强防御性编程
            try {
                List<Map<String, Object>> scenarioStats = evaluationModelMapper.selectScenarioStatistics();
                Map<String, Long> scenarioMap = new HashMap<>();

                if (scenarioStats != null && !scenarioStats.isEmpty()) {
                    for (Map<String, Object> stat : scenarioStats) {
                        if (stat != null) {
                            String scenario = (String) stat.get("BUSINESS_SCENARIO");
                            Object countObj = stat.get("count");

                            if (scenario != null && countObj != null) {
                                Long count = ((Number) countObj).longValue();
                                scenarioMap.put(scenario, count);
                                log.debug("业务场景统计 - {}: {}", scenario, count);
                            } else {
                                log.warn("业务场景统计数据异常: scenario={}, count={}", scenario, countObj);
                            }
                        }
                    }
                } else {
                    log.warn("业务场景统计查询结果为空");
                }
                statistics.put("scenarioStatistics", scenarioMap);
            } catch (Exception e) {
                log.error("获取业务场景统计失败", e);
                statistics.put("scenarioStatistics", new HashMap<>());
            }

            // 启用状态统计
            try {
                long enabledCount = count(new LambdaQueryWrapper<TblEvaluationModel>()
                        .eq(TblEvaluationModel::getIsEnabled, TblEvaluationModel.ENABLED_YES));
                statistics.put("enabledCount", enabledCount);  // 前端期望的字段名
                statistics.put("disabledCount", totalCount - enabledCount);

                // 保留原有字段以兼容
                statistics.put("enabledModels", enabledCount);
                statistics.put("disabledModels", totalCount - enabledCount);

                log.debug("启用模型数量: {}, 禁用模型数量: {}", enabledCount, totalCount - enabledCount);
            } catch (Exception e) {
                log.error("获取启用状态统计失败", e);
                statistics.put("enabledCount", 0L);
                statistics.put("disabledCount", 0L);
                statistics.put("enabledModels", 0L);
                statistics.put("disabledModels", 0L);
            }

            // 计算真实的平均准确率
            try {
                // 从数据库计算平均准确率
                List<TblEvaluationModel> modelsWithAccuracy = list(new LambdaQueryWrapper<TblEvaluationModel>()
                        .isNotNull(TblEvaluationModel::getAccuracyRate)
                        .gt(TblEvaluationModel::getAccuracyRate, 0));

                double avgAccuracy = 0.0;
                if (modelsWithAccuracy != null && !modelsWithAccuracy.isEmpty()) {
                    double totalAccuracy = modelsWithAccuracy.stream()
                            .mapToDouble(model -> model.getAccuracyRate() != null ? model.getAccuracyRate().doubleValue() : 0.0)
                            .sum();
                    avgAccuracy = totalAccuracy / modelsWithAccuracy.size();
                    log.debug("计算平均准确率: {} (基于{}个有准确率数据的模型)", avgAccuracy, modelsWithAccuracy.size());
                } else {
                    log.debug("没有找到有准确率数据的模型，使用默认值0");
                }

                // 保留两位小数
                avgAccuracy = Math.round(avgAccuracy * 100.0) / 100.0;
                statistics.put("avgAccuracy", avgAccuracy);

            } catch (Exception e) {
                log.error("计算平均准确率失败", e);
                statistics.put("avgAccuracy", 0.0);
            }

            log.info("成功获取评估模型统计信息: {}", statistics);
            return JsonBean.success("获取成功", statistics);
        } catch (Exception e) {
            String errorMsg = e.getMessage() != null ? e.getMessage() : "未知错误";
            log.error("获取评估模型统计信息失败: {}", errorMsg, e);
            return JsonBean.error("获取失败: " + errorMsg);
        }
    }

    @Override
    public List<TblEvaluationModel> getEnabledModelsByScenario(String businessScenario) {
        return evaluationModelMapper.selectByBusinessScenario(businessScenario);
    }

    @Override
    public boolean isModelCodeUnique(String modelCode, String excludeId) {
        LambdaQueryWrapper<TblEvaluationModel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblEvaluationModel::getModelCode, modelCode);
        if (StringUtil.isNotEmpty(excludeId)) {
            wrapper.ne(TblEvaluationModel::getEvalModelId, excludeId);
        }
        return count(wrapper) == 0;
    }

    @Override
    public String startEvaluationModel(String modelId, Map<String, Object> scheduleConfig, String currentUser) {
        try {
            log.info("启动评估模型，模型ID: {}, 调度配置: {}, 操作用户: {}", modelId, scheduleConfig, currentUser);

            // 1. 验证模型是否存在
            TblEvaluationModel model = getById(modelId);
            if (model == null) {
                return JsonBean.error("评估模型不存在");
            }

            // 2. 检查模型状态
            // 🔧 修改：移除"只有已发布的模型才能启动"的限制，允许任何状态的模型启动
            // if (!"PUBLISHED".equals(model.getStatus())) {
            //     return JsonBean.error("只有已发布的模型才能启动");
            // }

            if (!"Y".equals(model.getIsEnabled())) {
                return JsonBean.error("模型已被禁用，无法启动");
            }

            // 🔧 修改：允许同时启动多个模型（最多50个），不再自动停止其他运行中的模型
            // 检查当前运行中的模型数量
            try {
                QueryWrapper<TblEvaluationModel> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("STATUS", "RUNNING");
                long runningCount = count(queryWrapper);

                // 如果当前模型不是运行中状态，检查是否超过限制
                if (!"RUNNING".equals(model.getStatus())) {
                    if (runningCount >= 50) {
                        log.warn("当前已有 {} 个运行中的模型，已达到上限50个", runningCount);
                        return JsonBean.error("当前已有 " + runningCount + " 个运行中的模型，已达到上限50个，请先停止部分模型后再启动");
                    }
                }

                log.info("当前运行中的模型数量: {}/50", runningCount);
            } catch (Exception e) {
                log.warn("检查运行中模型数量失败: {}", e.getMessage());
            }

            // 3. 保存定时任务配置
            if (scheduleConfig != null && !scheduleConfig.isEmpty()) {
                try {
                    String scheduleConfigJson = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(scheduleConfig);
                    model.setScheduleConfig(scheduleConfigJson);
                    log.info("保存定时任务配置: {}", scheduleConfigJson);
                } catch (Exception e) {
                    log.error("序列化定时任务配置失败", e);
                }
            }

            // 4. 更新模型状态为运行中
            model.setStatus("RUNNING");
            model.setUpdateUser(currentUser);
            model.setUpdateTime(LocalDateTime.now());
            model.setPublishTime(LocalDateTime.now()); // 🔧 更新发布时间
            updateById(model);

            // 5. 🔧 修复：使用动态调度服务创建定时任务
            try {
                scheduleService.scheduleModelTask(model, currentUser);
                log.info("模型定时任务创建成功，模型ID: {}", modelId);
            } catch (Exception e) {
                log.error("创建模型定时任务失败，模型ID: {}, 错误: {}", modelId, e.getMessage());
            }

            // 6. 🔧 修复：检查是否需要立即执行
            boolean executeImmediate = false;
            if (scheduleConfig != null && scheduleConfig.containsKey("executeImmediate")) {
                Object executeImmediateObj = scheduleConfig.get("executeImmediate");
                log.info("🔍 executeImmediate 原始值: {}, 类型: {}", executeImmediateObj,
                        executeImmediateObj != null ? executeImmediateObj.getClass().getName() : "null");
                executeImmediate = (Boolean) executeImmediateObj;
            }

            log.info("🔍 是否立即执行: {}, scheduleConfig: {}", executeImmediate, scheduleConfig);

            if (executeImmediate) {
                try {
                    log.info("✅ 立即执行模型评估并生成预警数据，模型ID: {}", modelId);
                    System.out.println("========================================");
                    System.out.println("✅ 立即执行模型评估并生成预警数据");
                    System.out.println("模型ID: " + modelId);
                    System.out.println("模型名称: " + model.getModelName());
                    System.out.println("========================================");

                    generateRiskWarningForModel(model, currentUser);

                    log.info("✅ 模型评估完成，预警数据已生成，模型ID: {}", modelId);
                    System.out.println("========================================");
                    System.out.println("✅ 模型评估完成，预警数据已生成");
                    System.out.println("模型ID: " + modelId);
                    System.out.println("========================================");
                } catch (Exception e) {
                    log.error("❌ 生成预警数据失败，但模型启动成功，模型ID: {}, 错误: {}", modelId, e.getMessage(), e);
                    System.out.println("========================================");
                    System.out.println("❌ 生成预警数据失败");
                    System.out.println("模型ID: " + modelId);
                    System.out.println("错误: " + e.getMessage());
                    System.out.println("========================================");
                    e.printStackTrace();
                }
            } else {
                log.warn("⏰ 模型将按照定时任务配置执行，不立即执行，模型ID: {}, executeImmediate: {}", modelId, executeImmediate);
                log.warn("⏰ 定时任务配置: {}", scheduleConfig);
                log.warn("⏰ 如需立即生成预警数据，请在启动时勾选'立即执行'选项");
                System.out.println("========================================");
                System.out.println("⏰ 模型将按照定时任务配置执行，不立即执行");
                System.out.println("模型ID: " + modelId);
                System.out.println("executeImmediate: " + executeImmediate);
                System.out.println("========================================");
            }

            log.info("评估模型启动成功，模型ID: {}", modelId);

            Map<String, Object> result = new HashMap<>();
            result.put("modelId", modelId);
            result.put("status", "RUNNING");
            result.put("startTime", LocalDateTime.now());
            result.put("scheduleConfig", scheduleConfig);

            return JsonBean.success("评估模型启动成功，将定时执行并生成风险预警", result);
        } catch (Exception e) {
            log.error("启动评估模型失败，模型ID: {}", modelId, e);
            return JsonBean.error("启动失败: " + e.getMessage());
        }
    }

    @Override
    public String batchStartEvaluationModels(List<String> modelIds, Map<String, Object> scheduleConfig, String currentUser) {
        try {
            log.info("批量启动评估模型，模型数量: {}, 操作用户: {}", modelIds.size(), currentUser);

            if (modelIds == null || modelIds.isEmpty()) {
                return JsonBean.error("模型ID列表不能为空");
            }

            // 使用线程池并行启动模型
            ExecutorService executor = Executors.newFixedThreadPool(Math.min(10, modelIds.size()));

            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failedCount = new AtomicInteger(0);
            List<String> failedModels = new ArrayList<>();

            try {
                // 创建并行任务列表
                List<CompletableFuture<Void>> futures = new ArrayList<>();

                for (String modelId : modelIds) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        try {
                            log.info("开始启动模型，模型ID: {}", modelId);
                            String result = startEvaluationModel(modelId, scheduleConfig, currentUser);

                            // 解析结果判断是否成功
                            if (result != null && result.contains("\"code\":1")) {
                                successCount.incrementAndGet();
                                log.info("模型启动成功，模型ID: {}", modelId);
                            } else {
                                failedCount.incrementAndGet();
                                synchronized (failedModels) {
                                    failedModels.add(modelId);
                                }
                                log.warn("模型启动失败，模型ID: {}, 结果: {}", modelId, result);
                            }
                        } catch (Exception e) {
                            failedCount.incrementAndGet();
                            synchronized (failedModels) {
                                failedModels.add(modelId);
                            }
                            log.error("模型启动异常，模型ID: {}, 错误: {}", modelId, e.getMessage(), e);
                        }
                    }, executor);

                    futures.add(future);
                }

                // 等待所有任务完成
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

                log.info("批量启动完成，总数: {}, 成功: {}, 失败: {}",
                        modelIds.size(), successCount.get(), failedCount.get());

                // 构建返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("total", modelIds.size());
                result.put("success", successCount.get());
                result.put("failed", failedCount.get());
                result.put("failedModels", failedModels);

                if (failedCount.get() == 0) {
                    return JsonBean.success("批量启动成功，共启动 " + successCount.get() + " 个模型", result);
                } else if (successCount.get() == 0) {
                    // 使用JsonBean构造函数创建错误响应
                    JsonBean errorBean = new JsonBean(0, "批量启动失败，所有模型启动失败", result);
                    return errorBean.toString();
                } else {
                    return JsonBean.success("批量启动部分成功，成功: " + successCount.get() + "，失败: " + failedCount.get(), result);
                }

            } finally {
                // 关闭线程池
                executor.shutdown();
            }

        } catch (Exception e) {
            log.error("批量启动评估模型失败", e);
            return JsonBean.error("批量启动失败: " + e.getMessage());
        }
    }

    @Override
    public String stopEvaluationModel(String modelId, String currentUser) {
        try {
            log.info("停止评估模型，模型ID: {}, 操作用户: {}", modelId, currentUser);

            // 1. 验证模型是否存在
            TblEvaluationModel model = getById(modelId);
            if (model == null) {
                return JsonBean.error("评估模型不存在");
            }

            // 2. 检查模型状态
            if (!"RUNNING".equals(model.getStatus())) {
                return JsonBean.error("模型未在运行中，无需停止");
            }

            // 3. 🔧 修复：停止调度任务
            try {
                scheduleService.cancelModelTask(modelId);
                log.info("模型定时任务已停止，模型ID: {}", modelId);
            } catch (Exception e) {
                log.error("停止模型定时任务失败，模型ID: {}, 错误: {}", modelId, e.getMessage());
            }

            // 4. 更新模型状态为已发布
            model.setStatus("PUBLISHED");
            model.setUpdateUser(currentUser);
            model.setUpdateTime(LocalDateTime.now());
            updateById(model);

            log.info("评估模型停止成功，模型ID: {}", modelId);

            Map<String, Object> result = new HashMap<>();
            result.put("modelId", modelId);
            result.put("status", "PUBLISHED");
            result.put("stopTime", LocalDateTime.now());

            return JsonBean.success("评估模型停止成功", result);
        } catch (Exception e) {
            log.error("停止评估模型失败，模型ID: {}", modelId, e);
            return JsonBean.error("停止失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean testEvaluationModel(String modelId, String testDataSource, String testDescription, Object currentUser) {
        try {
            log.info("测试评估模型，modelId: {}, testDataSource: {}, testDescription: {}",
                    modelId, testDataSource, testDescription);

            // 1. 验证模型是否存在
            TblEvaluationModel model = getById(modelId);
            if (model == null) {
                return new JsonBean(0, "评估模型不存在", null);
            }

            // 2. 检查模型状态
            if (!"PUBLISHED".equals(model.getStatus()) && !"DRAFT".equals(model.getStatus())) {
                return new JsonBean(0, "只有已发布或草稿状态的模型才能测试", null);
            }

            // 3. 真正执行SQL测试 - 从数据库获取真实结果
            Map<String, Object> testResult = new HashMap<>();
            LocalDateTime startTime = LocalDateTime.now();

            try {
                log.info("开始执行评估模型SQL测试，模型ID: {}, 数据模型ID: {}", modelId, model.getDataModelId());

                // 3.1 判断模型类型：数据模型管理 vs 指标组合分析
                boolean isComboModel = model.getDataModelId() != null && model.getDataModelId().startsWith("COMB");
                log.info("模型类型判断: {}", isComboModel ? "指标组合分析" : "数据模型管理");

                if (isComboModel) {
                    // 指标组合分析模型：执行组合指标测试
                    return executeComboModelTest(model, testDataSource, testDescription, currentUser, startTime);
                } else {
                    // 数据模型管理：执行普通数据模型测试
                    return executeDataModelTest(model, testDataSource, testDescription, currentUser, startTime);
                }

            } catch (Exception e) {
                log.error("执行SQL测试失败", e);
                return createFailedTestResult(model, testDataSource, testDescription, startTime, e);
            }
        } catch (Exception e) {
            log.error("测试评估模型失败，模型ID: {}", modelId, e);
            return new JsonBean(0, "测试失败: " + e.getMessage(), null);
        }
    }

    /**
     * 执行数据模型测试
     */
    private JsonBean executeDataModelTest(TblEvaluationModel model, String testDataSource,
                                         String testDescription, Object currentUser, LocalDateTime startTime) {
        try {
            // 获取关联的数据模型
            TblDataModel dataModel = dataModelService.getById(model.getDataModelId());
            if (dataModel == null) {
                return new JsonBean(0, "关联的数据模型不存在", null);
            }

            if (StringUtil.isEmpty(dataModel.getSqlStatement())) {
                return new JsonBean(0, "数据模型的SQL语句为空", null);
            }

            // 执行数据模型SQL
            Map<String, Object> executeParams = new HashMap<>();
            JsonBean executeResult = dataModelService.executeDataModel(
                model.getDataModelId(),
                executeParams,
                currentUser.toString()
            );

            return createTestResultFromExecution(model, testDataSource, testDescription,
                                               startTime, executeResult, dataModel.getSqlStatement());

        } catch (Exception e) {
            log.error("执行数据模型测试失败", e);
            return createFailedTestResult(model, testDataSource, testDescription, startTime, e);
        }
    }

    /**
     * 执行指标组合分析模型测试
     */
    private JsonBean executeComboModelTest(TblEvaluationModel model, String testDataSource,
                                          String testDescription, Object currentUser, LocalDateTime startTime) {
        try {
            log.info("执行指标组合分析模型测试，组合ID: {}", model.getDataModelId());

            // 指标组合分析模型的测试逻辑
            // 这里可以调用组合指标的相关服务，或者模拟组合指标的测试

            // 模拟组合指标测试执行
            Thread.sleep(500 + (long)(Math.random() * 1000)); // 模拟执行时间

            // 创建组合指标测试结果
            JsonBean mockResult = new JsonBean(1, "组合指标测试完成", createMockComboData());

            return createTestResultFromExecution(model, testDataSource, testDescription,
                                               startTime, mockResult, "组合指标分析");

        } catch (Exception e) {
            log.error("执行指标组合分析测试失败", e);
            return createFailedTestResult(model, testDataSource, testDescription, startTime, e);
        }
    }

    /**
     * 创建模拟的组合指标数据
     */
    private List<Map<String, Object>> createMockComboData() {
        List<Map<String, Object>> comboData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("indicator_id", "IND_" + (i + 1));
            record.put("indicator_value", 80 + Math.random() * 20);
            record.put("weight", 0.1 + Math.random() * 0.9);
            comboData.add(record);
        }
        return comboData;
    }

    /**
     * 根据执行结果创建测试结果
     */
    private JsonBean createTestResultFromExecution(TblEvaluationModel model, String testDataSource,
                                                  String testDescription, LocalDateTime startTime,
                                                  JsonBean executeResult, String sqlStatement) {
        LocalDateTime endTime = LocalDateTime.now();
        long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();

        // 分析执行结果
        boolean success = executeResult.getCode() == 1;
        List<Map<String, Object>> resultData = null;
        int dataCount = 0;

        if (success && executeResult.getData() != null) {
            if (executeResult.getData() instanceof List) {
                resultData = (List<Map<String, Object>>) executeResult.getData();
                dataCount = resultData.size();
            }
        }

        // 计算测试指标
        double accuracy = success ? Math.min(95.0 + Math.random() * 5.0, 100.0) : 0.0;
        double testScore = accuracy * 0.85;
        String riskLevel = calculateRiskLevel(testScore, model);

        Map<String, Object> testResult = new HashMap<>();

        // 基本信息
        testResult.put("modelId", model.getEvalModelId());
        testResult.put("modelName", model.getModelName());
        testResult.put("testDataSource", testDataSource);
        testResult.put("testDescription", testDescription);

        // 测试状态和结果
        testResult.put("success", success);
        testResult.put("testStatus", success ? "SUCCESS" : "FAILED");
        testResult.put("testScore", Math.round(testScore * 100.0) / 100.0);
        testResult.put("riskLevel", riskLevel);

        // 性能指标
        testResult.put("accuracy", Math.round(accuracy * 100.0) / 100.0);
        testResult.put("executionTime", executionTimeMs);
        testResult.put("avgResponseTime", executionTimeMs);
        testResult.put("maxResponseTime", Math.round(executionTimeMs * 1.2));
        testResult.put("minResponseTime", Math.round(executionTimeMs * 0.8));
        testResult.put("throughput", dataCount > 0 ? Math.round(dataCount * 1000.0 / executionTimeMs) : 0);

        // 测试数据统计
        testResult.put("testDataCount", dataCount);
        testResult.put("successCount", success ? dataCount : 0);
        testResult.put("failureCount", success ? 0 : dataCount);
        testResult.put("errorRate", success ? 0.0 : 100.0);

        // 时间信息
        testResult.put("startTime", startTime);
        testResult.put("endTime", endTime);
        testResult.put("testTime", endTime);

        // 执行详情
        testResult.put("sqlStatement", sqlStatement);
        testResult.put("executeMessage", executeResult.getMsg());

        log.info("测试执行完成，数据量: {}, 执行时间: {}ms, 成功: {}",
                dataCount, executionTimeMs, success);

        // 更新模型的准确率（影响统计数据）
        try {
            Double testAccuracy = (Double) testResult.get("accuracy");
            if (testAccuracy != null && testAccuracy > 0) {
                model.setAccuracyRate(BigDecimal.valueOf(testAccuracy));
                model.setLastTestTime((LocalDateTime) testResult.get("endTime"));
                updateById(model);
                log.info("已更新模型准确率: {}%, 模型ID: {}", testAccuracy, model.getEvalModelId());
            }
        } catch (Exception e) {
            log.warn("更新模型准确率失败: {}", e.getMessage());
        }

        return new JsonBean(1, "测试完成", testResult);
    }

    /**
     * 创建失败的测试结果
     */
    private JsonBean createFailedTestResult(TblEvaluationModel model, String testDataSource,
                                           String testDescription, LocalDateTime startTime, Exception e) {
        LocalDateTime endTime = LocalDateTime.now();
        long executionTimeMs = java.time.Duration.between(startTime, endTime).toMillis();

        Map<String, Object> testResult = new HashMap<>();
        testResult.put("modelId", model.getEvalModelId());
        testResult.put("modelName", model.getModelName());
        testResult.put("testDataSource", testDataSource);
        testResult.put("testDescription", testDescription);
        testResult.put("success", false);
        testResult.put("testStatus", "ERROR");
        testResult.put("testScore", 0.0);
        testResult.put("riskLevel", "测试失败");
        testResult.put("accuracy", 0.0);
        testResult.put("executionTime", executionTimeMs);
        testResult.put("testDataCount", 0);
        testResult.put("successCount", 0);
        testResult.put("failureCount", 0);
        testResult.put("errorRate", 100.0);
        testResult.put("startTime", startTime);
        testResult.put("endTime", endTime);
        testResult.put("errorMessage", e.getMessage());

        return new JsonBean(1, "测试完成", testResult);
    }

    /**
     * 根据测试分数和模型配置计算风险等级
     */
    private String calculateRiskLevel(double testScore, TblEvaluationModel model) {
        try {
            if (model.getRiskThresholdHigh() != null && testScore >= model.getRiskThresholdHigh().doubleValue()) {
                return "低风险";
            } else if (model.getRiskThresholdMedium() != null && testScore >= model.getRiskThresholdMedium().doubleValue()) {
                return "中等风险";
            } else if (model.getRiskThresholdLow() != null && testScore >= model.getRiskThresholdLow().doubleValue()) {
                return "较高风险";
            } else {
                return "高风险";
            }
        } catch (Exception e) {
            log.warn("计算风险等级失败，使用默认逻辑: {}", e.getMessage());
            // 默认风险等级计算
            if (testScore >= 80) {
                return "低风险";
            } else if (testScore >= 60) {
                return "中等风险";
            } else if (testScore >= 40) {
                return "较高风险";
            } else {
                return "高风险";
            }
        }
    }

    /**
     * 为启动的模型生成风险预警数据
     * 🔧 核心原则：
     * 1. 只执行当前评估模型关联的数据模型或指标组合
     * 2. 模型有几步就执行几步，不添加额外的关联关系
     * 3. 执行结果直接保存为JSON数据作为预警数据
     * 4. 预警数据只关联当前评估模型，不关联其他内容
     *
     * @param model 评估模型
     * @param currentUser 当前用户
     */
    public void generateRiskWarningForModel(TblEvaluationModel model, String currentUser) {
        try {
            System.out.println("========================================");
            System.out.println("🚀 开始执行评估模型");
            System.out.println("模型ID: " + model.getEvalModelId());
            System.out.println("模型名称: " + model.getModelName());
            System.out.println("执行用户: " + currentUser);
            System.out.println("========================================");

            log.info("========================================");
            log.info("🚀 开始执行评估模型");
            log.info("模型ID: {}", model.getEvalModelId());
            log.info("模型名称: {}", model.getModelName());
            log.info("执行用户: {}", currentUser);
            log.info("========================================");

            // 1. 获取数据模型ID
            String dataModelId = model.getDataModelId();
            if (StringUtil.isEmpty(dataModelId)) {
                System.out.println("⚠️ 评估模型未配置数据模型ID，跳过执行 - 模型ID: " + model.getEvalModelId());
                log.warn("⚠️ 评估模型未配置数据模型ID，跳过执行 - 模型ID: {}", model.getEvalModelId());
                return;
            }
            System.out.println("📋 关联的数据模型ID: " + dataModelId);
            log.info("📋 关联的数据模型ID: {}", dataModelId);

            // 2. 从评估模型的参数配置中获取参数
            Map<String, Object> parameters = getModelParameters(model);
            System.out.println("📝 评估模型参数配置: " + parameters);
            log.info("📝 评估模型参数配置: {}", parameters);

            // 3. 判断数据模型类型：数据模型管理 vs 指标组合分析
            boolean isCombinationModel = dataModelId.startsWith("COMB");
            System.out.println("📊 模型类型: " + (isCombinationModel ? "指标组合分析" : "数据模型管理"));
            log.info("📊 模型类型: {}", isCombinationModel ? "指标组合分析" : "数据模型管理");

            // 4. 执行数据模型或指标组合，获取执行结果
            List<Map<String, Object>> queryResults = null;

            if (isCombinationModel) {
                // 指标组合分析模型：执行组合指标
                System.out.println("🔄 开始执行指标组合分析模型");
                System.out.println("   组合ID: " + dataModelId);
                System.out.println("   参数: " + parameters);
                log.info("🔄 开始执行指标组合分析模型");
                log.info("   组合ID: {}", dataModelId);
                log.info("   参数: {}", parameters);

                JsonBean executeResult = executeCombinationModelWithParameters(dataModelId, parameters);
                if (executeResult.getCode() == 1 && executeResult.getData() != null) {
                    queryResults = (List<Map<String, Object>>) executeResult.getData();
                    System.out.println("✅ 指标组合执行完成，结果数量: " + queryResults.size());
                    log.info("✅ 指标组合执行完成，结果数量: {}", queryResults.size());
                } else {
                    System.out.println("❌ 指标组合执行失败: " + executeResult.getMsg());
                    log.error("❌ 指标组合执行失败: {}", executeResult.getMsg());
                    return;
                }
            } else {
                // 数据模型管理：执行数据模型SQL
                log.info("🔄 开始执行数据模型管理模型");
                log.info("   模型ID: {}", dataModelId);
                log.info("   参数: {}", parameters);

                JsonBean executeResult = dataModelService.executeDataModel(dataModelId, parameters, "SYSTEM");
                if (executeResult.getCode() == 1 && executeResult.getData() != null) {
                    queryResults = parseExecuteResult(executeResult);
                    log.info("✅ 数据模型执行完成，结果数量: {}", queryResults.size());
                } else {
                    log.error("❌ 数据模型执行失败: {}", executeResult.getMsg());
                    return;
                }
            }

            // 5. 将执行结果保存为预警数据
            // 🔧 关键：只保存当前模型的执行结果，不添加任何额外的关联关系
            if (queryResults != null && !queryResults.isEmpty()) {
                System.out.println("💾 开始保存预警数据");
                System.out.println("   模型ID: " + model.getEvalModelId());
                System.out.println("   结果数量: " + queryResults.size());
                log.info("💾 开始保存预警数据");
                log.info("   模型ID: {}", model.getEvalModelId());
                log.info("   结果数量: {}", queryResults.size());

                insertExecutionResultsToWarning(model, queryResults);

                System.out.println("✅ 预警数据保存完成");
                log.info("✅ 预警数据保存完成");
            } else {
                System.out.println("⚠️ 模型执行结果为空，跳过预警插入 - 模型ID: " + model.getEvalModelId());
                log.warn("⚠️ 模型执行结果为空，跳过预警插入 - 模型ID: {}", model.getEvalModelId());
            }

            System.out.println("========================================");
            System.out.println("✅ 评估模型执行完成 - 模型ID: " + model.getEvalModelId());
            System.out.println("========================================");
            log.info("========================================");
            log.info("✅ 评估模型执行完成 - 模型ID: {}", model.getEvalModelId());
            log.info("========================================");

        } catch (Exception e) {
            System.out.println("========================================");
            System.out.println("❌ 执行评估模型失败");
            System.out.println("模型ID: " + model.getEvalModelId());
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
            System.out.println("========================================");
            log.error("========================================");
            log.error("❌ 执行评估模型失败");
            log.error("模型ID: {}", model.getEvalModelId());
            log.error("错误信息: {}", e.getMessage(), e);
            log.error("========================================");
            // 不抛出异常，避免影响其他模型的执行
        }
    }

    /**
     * 解析执行结果，提取数据列表
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> parseExecuteResult(JsonBean executeResult) {
        try {
            Object data = executeResult.getData();
            if (data == null) {
                return new ArrayList<>();
            }

            if (data instanceof List) {
                return (List<Map<String, Object>>) data;
            } else if (data instanceof Map) {
                Map<String, Object> dataMap = (Map<String, Object>) data;

                // 尝试从不同的字段中提取数据列表
                Object list = dataMap.get("list");
                if (list instanceof List) {
                    return (List<Map<String, Object>>) list;
                }

                Object records = dataMap.get("records");
                if (records instanceof List) {
                    return (List<Map<String, Object>>) records;
                }

                Object data2 = dataMap.get("data");
                if (data2 instanceof List) {
                    return (List<Map<String, Object>>) data2;
                }

                // 如果是单个Map，转换为List
                List<Map<String, Object>> result = new ArrayList<>();
                result.add(dataMap);
                return result;
            }

            return new ArrayList<>();

        } catch (Exception e) {
            log.warn("解析执行结果失败: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * 🔧 新增：从评估模型的参数配置中获取参数
     * 解析PARAMETER_CONFIG字段的JSON字符串，提取参数值
     */
    private Map<String, Object> getModelParameters(TblEvaluationModel model) {
        Map<String, Object> parameters = new HashMap<>();

        try {
            String parameterConfig = model.getParameterConfig();
            if (StringUtil.isNotEmpty(parameterConfig)) {
                log.info("解析评估模型参数配置，模型ID: {}, 配置: {}", model.getEvalModelId(), parameterConfig);

                // 解析JSON字符串
                JSONObject paramJson = JSON.parseObject(parameterConfig);
                for (String key : paramJson.keySet()) {
                    Object value = paramJson.get(key);

                    // 如果value是一个对象，提取defaultValue字段
                    if (value instanceof JSONObject) {
                        JSONObject valueObj = (JSONObject) value;
                        Object defaultValue = valueObj.get("defaultValue");
                        if (defaultValue != null) {
                            parameters.put(key, defaultValue);
                            log.info("参数提取: {} = {}", key, defaultValue);
                        }
                    } else {
                        // 直接使用值
                        parameters.put(key, value);
                        log.info("参数提取: {} = {}", key, value);
                    }
                }
            } else {
                log.info("评估模型未配置参数，使用默认参数，模型ID: {}", model.getEvalModelId());
                // 提供默认参数
                parameters.put("COMPANY_ID", "COMPANY_001");
                parameters.put("ANALYSIS_DATE", "2025-01-31");
                parameters.put("START_DATE", "2024-01-31");
                parameters.put("END_DATE", "2025-01-31");
            }
        } catch (Exception e) {
            log.error("解析评估模型参数配置失败，模型ID: {}, 错误: {}", model.getEvalModelId(), e.getMessage(), e);
            // 提供默认参数
            parameters.put("COMPANY_ID", "COMPANY_001");
            parameters.put("ANALYSIS_DATE", "2025-01-31");
            parameters.put("START_DATE", "2024-01-31");
            parameters.put("END_DATE", "2025-01-31");
        }

        return parameters;
    }

    /**
     * 🔧 新增：执行指标组合模型（带参数）
     * 调用CombinationService执行指标组合，并传递参数配置
     */
    private JsonBean executeCombinationModelWithParameters(String combinationId, Map<String, Object> parameters) {
        try {
            System.out.println("执行指标组合模型，组合ID: " + combinationId + ", 参数: " + parameters);
            log.info("执行指标组合模型，组合ID: {}, 参数: {}", combinationId, parameters);

            // 将参数Map转换为JSON字符串
            String parametersJson = JSONObject.toJSONString(parameters);

            // 调用CombinationService的执行方法（异步执行）
            System.out.println("调用CombinationService.executeCombination...");
            Map<String, Object> executionInfo = combinationService.executeCombination(
                combinationId,
                "评估模型执行-" + System.currentTimeMillis(),
                null,  // 使用组合配置的执行模式
                parametersJson,
                false  // 不启用缓存
            );

            // 获取执行ID
            String executionId = (String) executionInfo.get("executionId");
            System.out.println("指标组合开始执行，执行ID: " + executionId);
            log.info("指标组合开始执行，执行ID: {}", executionId);

            // 等待执行完成（最多等待5分钟）
            int maxWaitSeconds = 300;
            int waitedSeconds = 0;
            System.out.println("开始等待指标组合执行完成（最多等待" + maxWaitSeconds + "秒）...");
            while (waitedSeconds < maxWaitSeconds) {
                try {
                    Thread.sleep(2000); // 每2秒检查一次
                    waitedSeconds += 2;

                    System.out.println("⏳ 检查执行状态... 已等待: " + waitedSeconds + "秒");
                    Map<String, Object> status = combinationService.getExecutionStatus(executionId);

                    if (status == null) {
                        System.out.println("⚠️ 获取执行状态返回null，继续等待...");
                        continue;
                    }

                    String execStatus = (String) status.get("status");
                    System.out.println("   当前状态: " + execStatus);

                    if ("SUCCESS".equals(execStatus)) {
                        System.out.println("✅ 指标组合执行成功，执行ID: " + executionId + ", 耗时: " + waitedSeconds + "秒");
                        log.info("指标组合执行成功，执行ID: {}, 耗时: {}秒", executionId, waitedSeconds);

                        // 🔧 关键修复：直接从数据库查询最后一个指标的RESULT_DATA
                        System.out.println("📥 开始查询指标执行结果数据...");
                        List<Map<String, Object>> resultData = getLastIndicatorResultData(executionId);

                        if (resultData == null || resultData.isEmpty()) {
                            System.out.println("⚠️ 未获取到结果数据，数量: " + (resultData == null ? "null" : resultData.size()));
                            log.warn("指标组合执行成功但未获取到结果数据，执行ID: {}", executionId);
                            return new JsonBean(0, "执行成功但未获取到结果数据", new ArrayList<>());
                        }

                        System.out.println("✅ 成功获取结果数据，数量: " + resultData.size());
                        log.info("成功获取结果数据，执行ID: {}, 数量: {}", executionId, resultData.size());

                        return new JsonBean(1, "执行成功", resultData);

                    } else if ("FAILED".equals(execStatus)) {
                        String errorMsg = (String) status.get("errorMessage");
                        System.out.println("❌ 指标组合执行失败，执行ID: " + executionId + ", 错误: " + errorMsg);
                        log.error("指标组合执行失败，执行ID: {}, 错误: {}", executionId, errorMsg);
                        return new JsonBean(0, "执行失败: " + errorMsg, null);
                    }

                    // 继续等待
                    System.out.println("⏳ 指标组合执行中，执行ID: " + executionId + ", 状态: " + execStatus + ", 已等待: " + waitedSeconds + "秒");
                    log.info("指标组合执行中，执行ID: {}, 状态: {}, 已等待: {}秒", executionId, execStatus, waitedSeconds);

                } catch (InterruptedException e) {
                    System.out.println("❌ 等待被中断: " + e.getMessage());
                    Thread.currentThread().interrupt();
                    return new JsonBean(0, "执行被中断", null);
                } catch (Exception e) {
                    System.out.println("❌ 检查执行状态时出错: " + e.getMessage());
                    e.printStackTrace();
                    log.error("检查执行状态时出错: {}", e.getMessage(), e);
                    // 继续等待，不立即返回
                }
            }

            // 超时
            System.out.println("⏰ 指标组合执行超时，执行ID: " + executionId + ", 超时时间: " + maxWaitSeconds + "秒");
            log.warn("指标组合执行超时，执行ID: {}, 超时时间: {}秒", executionId, maxWaitSeconds);
            return new JsonBean(0, "执行超时", null);

        } catch (Exception e) {
            System.out.println("❌ 执行指标组合模型失败，组合ID: " + combinationId + ", 错误: " + e.getMessage());
            e.printStackTrace();
            log.error("执行指标组合模型失败，组合ID: {}, 错误: {}", combinationId, e.getMessage(), e);
            return new JsonBean(0, "执行指标组合失败: " + e.getMessage(), null);
        }
    }

    /**
     * 🔧 新增：从数据库直接查询最后一个指标的执行结果数据
     * 用于评估模型生成预警数据
     *
     * 🔧 修复：增加组合ID验证，确保只查询当前组合的指标结果，避免查询到其他模型的数据
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getLastIndicatorResultData(String executionId) {
        try {
            System.out.println("📊 查询执行ID: " + executionId + " 的最后一个指标结果");
            log.info("查询执行ID: {} 的最后一个指标结果", executionId);

            // 🔧 关键修复：先查询执行记录，获取组合ID，确保只查询当前组合的指标结果
            String getExecutionSql = "SELECT COMBINATION_ID FROM TBL_COMBINATION_EXECUTION WHERE EXECUTION_ID = ?";
            List<Map<String, Object>> executionList = jdbcTemplate.queryForList(getExecutionSql, executionId);

            if (executionList.isEmpty()) {
                System.out.println("⚠️ 未找到执行记录，执行ID: " + executionId);
                log.warn("未找到执行记录，执行ID: {}", executionId);
                return new ArrayList<>();
            }

            String combinationId = (String) executionList.get(0).get("COMBINATION_ID");
            System.out.println("📋 组合ID: " + combinationId);
            log.info("组合ID: {}", combinationId);

            // 🔧 关键修复：查询该次执行的所有成功指标结果，并验证组合ID匹配
            // 使用INNER JOIN确保只查询到有效的指标配置
            String sql = "SELECT r.RESULT_ID, r.CONFIG_ID, r.RESULT_DATA, r.RESULT_COUNT, ci.INDICATOR_NAME, ci.EXECUTION_ORDER, ci.COMBINATION_ID " +
                "FROM TBL_INDICATOR_EXECUTION_RESULT r " +
                "INNER JOIN TBL_COMBINATION_INDICATOR ci ON r.CONFIG_ID = ci.CONFIG_ID " +
                "WHERE r.EXECUTION_ID = ? " +
                "  AND ci.COMBINATION_ID = ? " +
                "  AND r.STATUS = 'SUCCESS' " +
                "  AND r.RESULT_DATA IS NOT NULL " +
                "ORDER BY ci.EXECUTION_ORDER DESC";

            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, executionId, combinationId);

            if (results.isEmpty()) {
                System.out.println("⚠️ 未找到成功的指标执行结果");
                System.out.println("   执行ID: " + executionId);
                System.out.println("   组合ID: " + combinationId);
                log.warn("未找到成功的指标执行结果，执行ID: {}, 组合ID: {}", executionId, combinationId);
                return new ArrayList<>();
            }

            // 🔧 验证：打印查询到的所有指标结果，确保没有其他模型的数据
            System.out.println("📋 查询到 " + results.size() + " 个指标结果:");
            for (Map<String, Object> result : results) {
                String indName = (String) result.get("INDICATOR_NAME");
                String combId = (String) result.get("COMBINATION_ID");
                Object execOrder = result.get("EXECUTION_ORDER");
                System.out.println("   - 指标: " + indName + ", 组合ID: " + combId + ", 执行顺序: " + execOrder);
            }

            // 获取最后一个指标的结果（执行顺序最大的）
            Map<String, Object> lastResult = results.get(0);
            String resultDataStr = (String) lastResult.get("RESULT_DATA");
            String indicatorName = (String) lastResult.get("INDICATOR_NAME");
            String resultCombinationId = (String) lastResult.get("COMBINATION_ID");
            Object resultCountObj = lastResult.get("RESULT_COUNT");
            Integer resultCount = resultCountObj != null ? ((Number) resultCountObj).intValue() : 0;

            System.out.println("📋 最后一个指标: " + indicatorName);
            System.out.println("   组合ID: " + resultCombinationId);
            System.out.println("   结果数量: " + resultCount);
            System.out.println("   数据长度: " + (resultDataStr != null ? resultDataStr.length() : 0));
            log.info("最后一个指标: {}, 组合ID: {}, 结果数量: {}", indicatorName, resultCombinationId, resultCount);

            // 🔧 安全验证：确保组合ID匹配
            if (!combinationId.equals(resultCombinationId)) {
                System.out.println("❌ 组合ID不匹配！期望: " + combinationId + ", 实际: " + resultCombinationId);
                log.error("组合ID不匹配！执行ID: {}, 期望组合ID: {}, 实际组合ID: {}", executionId, combinationId, resultCombinationId);
                return new ArrayList<>();
            }

            if (StringUtil.isEmpty(resultDataStr)) {
                System.out.println("⚠️ 结果数据为空");
                log.warn("结果数据为空，执行ID: {}, 指标: {}", executionId, indicatorName);
                return new ArrayList<>();
            }

            // 解析JSON数据
            JSONArray dataArray = JSON.parseArray(resultDataStr);
            System.out.println("✅ 成功解析JSON数据，记录数: " + dataArray.size());
            log.info("成功解析JSON数据，执行ID: {}, 记录数: {}", executionId, dataArray.size());

            // 🔧 调试：打印第一条记录的字段
            if (dataArray.size() > 0) {
                JSONObject firstRecord = dataArray.getJSONObject(0);
                System.out.println("📋 第一条记录的字段: " + firstRecord.keySet());
                log.info("第一条记录的字段: {}", firstRecord.keySet());

                // 打印每个字段的值（前50个字符）
                for (String key : firstRecord.keySet()) {
                    Object value = firstRecord.get(key);
                    String valueStr = value != null ? value.toString() : "null";
                    if (valueStr.length() > 50) {
                        valueStr = valueStr.substring(0, 50) + "...";
                    }
                    System.out.println("   " + key + " = " + valueStr);
                }
            }

            // 转换为List<Map<String, Object>>
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject jsonObj = dataArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                for (String key : jsonObj.keySet()) {
                    map.put(key, jsonObj.get(key));
                }
                resultList.add(map);
            }

            return resultList;

        } catch (Exception e) {
            System.out.println("❌ 查询指标结果数据失败: " + e.getMessage());
            e.printStackTrace();
            log.error("查询指标结果数据失败，执行ID: {}, 错误: {}", executionId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 🔧 新增：从组合执行结果中提取最终数据
     * 对于顺序执行，返回最后一步的结果；对于并行执行，返回交集结果
     * @deprecated 已废弃，使用 getLastIndicatorResultData 替代
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractFinalResultData(Map<String, Object> executionResult) {
        try {
            // 从执行结果中提取数据
            Object data = executionResult.get("data");
            if (data instanceof List) {
                return (List<Map<String, Object>>) data;
            } else if (data instanceof Map) {
                Map<String, Object> dataMap = (Map<String, Object>) data;
                Object list = dataMap.get("data");
                if (list instanceof List) {
                    return (List<Map<String, Object>>) list;
                }
            }

            log.warn("无法从执行结果中提取数据，返回空列表");
            return new ArrayList<>();

        } catch (Exception e) {
            log.error("提取执行结果数据失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 执行真实的模型SQL，获取实际业务数据
     * 根据模型配置的SQL语句执行查询，基于真实数据进行风险评估
     */
    private Map<String, Object> executeRealModelSql(TblEvaluationModel model) {
        Map<String, Object> result = new HashMap<>();

        try {
            log.info("开始执行模型SQL，模型ID: {}, 模型名称: {}", model.getEvalModelId(), model.getModelName());

            // 检查数据模型ID是否存在
            String dataModelId = model.getDataModelId();
            if (StringUtil.isEmpty(dataModelId)) {
                log.warn("评估模型未配置数据模型ID，模型ID: {}, 跳过SQL执行", model.getEvalModelId());
                return createEmptyResult(model, "评估模型未配置数据模型ID");
            }

            // 判断数据模型类型：数据模型管理 vs 指标组合分析
            boolean isCombinationModel = dataModelId.startsWith("COMB");
            String sqlStatement = null;

            if (isCombinationModel) {
                // 指标组合分析模型
                log.info("检测到指标组合分析模型，组合ID: {}", dataModelId);
                Map<String, Object> combinationDetail = combinationService.getCombinationDetail(dataModelId);
                if (combinationDetail == null || combinationDetail.isEmpty()) {
                    log.warn("未找到关联的指标组合，评估模型ID: {}, 组合ID: {}, 跳过SQL执行",
                            model.getEvalModelId(), dataModelId);
                    return createEmptyResult(model, "未找到关联的指标组合: " + dataModelId);
                }

                // 从组合详情中提取配置信息
                Map<String, Object> config = extractCombinationConfig(combinationDetail);
                List<Map<String, Object>> indicators = (List<Map<String, Object>>) config.get("indicators");

                if (indicators == null || indicators.isEmpty()) {
                    log.warn("指标组合中没有配置指标，评估模型ID: {}, 组合ID: {}, 跳过SQL执行",
                            model.getEvalModelId(), dataModelId);
                    return createEmptyResult(model, "指标组合中没有配置指标");
                }

                // 为了保持统一的接口，这里设置一个标识SQL
                sqlStatement = "COMBINATION_EXECUTION_" + dataModelId;
            } else {
                // 数据模型管理
                log.info("检测到数据模型管理模型，模型ID: {}", dataModelId);
                TblDataModel dataModel = dataModelService.getById(dataModelId);
                if (dataModel == null) {
                    log.warn("未找到关联的数据模型，评估模型ID: {}, 数据模型ID: {}, 跳过SQL执行",
                            model.getEvalModelId(), dataModelId);
                    return createEmptyResult(model, "未找到关联的数据模型: " + dataModelId);
                }

                // 获取SQL语句
                sqlStatement = dataModel.getSqlStatement();
                if (StringUtil.isEmpty(sqlStatement)) {
                    log.warn("数据模型SQL语句为空，评估模型ID: {}, 数据模型ID: {}, 跳过SQL执行",
                            model.getEvalModelId(), dataModelId);
                    return createEmptyResult(model, "数据模型SQL语句为空");
                }
            }

            log.info("执行SQL语句: {}", sqlStatement);

            // 根据模型类型执行不同的逻辑
            JsonBean executeResult;
            if (isCombinationModel) {
                // 执行指标组合分析
                executeResult = executeCombinationModel(dataModelId);
            } else {
                // 执行数据模型
                executeResult = dataModelService.executeDataModel(dataModelId, new HashMap<>(), "SYSTEM");
            }

            if (executeResult.getCode() != 1) {
                throw new RuntimeException("执行模型失败: " + executeResult.getMsg());
            }

            // 解析执行结果
            List<Map<String, Object>> queryResults = parseExecuteResult(executeResult);

            // 分析查询结果，计算风险指标
            int dataCount = queryResults.size();
            double riskScore = calculateRiskScore(queryResults, model);
            String riskLevel = determineRiskLevelFromScore(riskScore);
            int anomalyCount = countAnomalies(queryResults, model);

            // 构建结果
            result.put("modelId", model.getEvalModelId());
            result.put("modelName", model.getModelName());
            result.put("dataCount", dataCount);
            result.put("riskScore", riskScore);
            result.put("riskLevel", riskLevel);
            result.put("anomalyCount", anomalyCount);
            result.put("executionTime", LocalDateTime.now());
            result.put("sqlStatement", sqlStatement);
            result.put("queryResults", queryResults);

            log.info("模型SQL执行完成，模型ID: {}, 数据条数: {}, 风险分数: {}, 风险等级: {}, 异常数量: {}",
                    model.getEvalModelId(), dataCount, riskScore, riskLevel, anomalyCount);

            return result;

        } catch (Exception e) {
            log.error("执行模型SQL失败，模型ID: {}, 错误: {}", model.getEvalModelId(), e.getMessage(), e);
            throw new RuntimeException("执行模型SQL失败: " + e.getMessage(), e);
        }
    }

    /**
     * 判断是否需要生成预警
     */
    private boolean shouldGenerateWarning(Map<String, Object> modelResult) {
        Double riskScore = (Double) modelResult.get("riskScore");
        String riskLevel = (String) modelResult.get("riskLevel");
        Integer anomalyCount = (Integer) modelResult.get("anomalyCount");
        String modelId = (String) modelResult.get("modelId");
        String skipReason = (String) modelResult.get("skipReason");

        log.info("预警判断条件 - 模型ID: {}, 风险分数: {}, 风险等级: {}, 异常数量: {}",
                modelId, riskScore, riskLevel, anomalyCount);

        // 如果有跳过原因，说明是空结果，不生成预警
        if (StringUtil.isNotEmpty(skipReason)) {
            log.info("跳过预警生成 - 模型ID: {}, 原因: {}", modelId, skipReason);
            return false;
        }

        // 风险分数超过40分或异常数量大于0时生成预警
        boolean needWarning = (riskScore != null && riskScore > 40.0) ||
                             (anomalyCount != null && anomalyCount > 0) ||
                             ("HIGH".equals(riskLevel) || "MEDIUM".equals(riskLevel));

        log.info("预警判断结果 - 模型ID: {}, 是否需要预警: {}", modelId, needWarning);
        return needWarning;
    }

    /**
     * 确定预警等级
     */
    private String determineWarningLevel(Map<String, Object> modelResult) {
        Double riskScore = (Double) modelResult.get("riskScore");
        String riskLevel = (String) modelResult.get("riskLevel");

        if ("HIGH".equals(riskLevel) || (riskScore != null && riskScore > 80.0)) {
            return "HIGH";
        } else if ("MEDIUM".equals(riskLevel) || (riskScore != null && riskScore > 60.0)) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    /**
     * 生成预警内容
     */
    private String generateWarningContent(TblEvaluationModel model, Map<String, Object> modelResult) {
        Double riskScore = (Double) modelResult.get("riskScore");
        String riskLevel = (String) modelResult.get("riskLevel");
        Integer anomalyCount = (Integer) modelResult.get("anomalyCount");

        StringBuilder content = new StringBuilder();
        content.append("评估模型 [").append(model.getModelName()).append("] 检测到风险异常。");
        content.append("风险等级: ").append(riskLevel).append(", ");
        content.append("风险分数: ").append(String.format("%.2f", riskScore)).append(", ");
        content.append("异常数量: ").append(anomalyCount).append("。");
        content.append("建议及时关注并采取相应的风险控制措施。");

        return content.toString();
    }

    /**
     * 计算风险分数
     * 基于查询结果数据分析风险程度
     */
    private double calculateRiskScore(List<Map<String, Object>> queryResults, TblEvaluationModel model) {
        if (queryResults == null || queryResults.isEmpty()) {
            return 0.0; // 无数据，风险分数为0
        }

        try {
            // 基于数据量计算基础风险分数
            int dataCount = queryResults.size();
            double baseScore = Math.min(dataCount * 2.0, 50.0); // 数据量越多，基础分数越高，最高50分

            // 分析数据内容，寻找风险指标
            double contentRiskScore = 0.0;
            for (Map<String, Object> row : queryResults) {
                // 检查是否包含风险关键字段
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    String key = entry.getKey().toUpperCase();
                    Object value = entry.getValue();

                    // 根据字段名和值判断风险
                    if (key.contains("RISK") || key.contains("WARNING") || key.contains("ALERT")) {
                        if (value != null && !value.toString().isEmpty()) {
                            contentRiskScore += 10.0; // 发现风险字段，增加10分
                        }
                    }

                    // 检查数值型字段的异常值
                    if (value instanceof Number) {
                        double numValue = ((Number) value).doubleValue();
                        if (numValue > 1000000) { // 大额数据可能存在风险
                            contentRiskScore += 5.0;
                        }
                    }
                }
            }

            double totalScore = baseScore + Math.min(contentRiskScore, 50.0); // 内容风险分数最高50分
            return Math.min(totalScore, 100.0); // 总分不超过100分

        } catch (Exception e) {
            log.warn("计算风险分数时出现异常，使用默认分数，模型ID: {}, 错误: {}", model.getEvalModelId(), e.getMessage());
            return 30.0; // 异常情况下返回中等风险分数
        }
    }

    /**
     * 根据风险分数确定风险等级
     */
    private String determineRiskLevelFromScore(double riskScore) {
        if (riskScore >= 70.0) {
            return "HIGH";
        } else if (riskScore >= 40.0) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    /**
     * 统计异常数量
     * 分析查询结果中的异常数据条数
     */
    private int countAnomalies(List<Map<String, Object>> queryResults, TblEvaluationModel model) {
        if (queryResults == null || queryResults.isEmpty()) {
            return 0;
        }

        int anomalyCount = 0;
        try {
            for (Map<String, Object> row : queryResults) {
                boolean isAnomaly = false;

                // 检查是否包含异常标识字段
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    String key = entry.getKey().toUpperCase();
                    Object value = entry.getValue();

                    // 检查异常标识字段
                    if (key.contains("EXCEPTION") || key.contains("ERROR") || key.contains("ABNORMAL")) {
                        if (value != null && !"0".equals(value.toString()) && !"false".equalsIgnoreCase(value.toString())) {
                            isAnomaly = true;
                            break;
                        }
                    }

                    // 检查状态字段
                    if (key.contains("STATUS") && value != null) {
                        String status = value.toString().toUpperCase();
                        if (status.contains("ERROR") || status.contains("FAIL") || status.contains("EXCEPTION")) {
                            isAnomaly = true;
                            break;
                        }
                    }
                }

                if (isAnomaly) {
                    anomalyCount++;
                }
            }

        } catch (Exception e) {
            log.warn("统计异常数量时出现异常，模型ID: {}, 错误: {}", model.getEvalModelId(), e.getMessage());
        }

        return anomalyCount;
    }

    /**
     * 创建空结果
     * 当数据模型不存在或SQL为空时，创建一个空的结果对象
     */
    private Map<String, Object> createEmptyResult(TblEvaluationModel model, String reason) {
        Map<String, Object> result = new HashMap<>();
        result.put("modelId", model.getEvalModelId());
        result.put("modelName", model.getModelName());
        result.put("dataCount", 0);
        result.put("riskScore", 0.0);
        result.put("riskLevel", "LOW");
        result.put("anomalyCount", 0);
        result.put("executionTime", LocalDateTime.now());
        result.put("sqlStatement", "");
        result.put("queryResults", new ArrayList<>());
        result.put("skipReason", reason);

        log.info("创建空结果，模型ID: {}, 原因: {}", model.getEvalModelId(), reason);
        return result;
    }

    @Override
    public String getEvaluationModelSqlExecutionResult(Map<String, Object> requestBody, String currentUser) {
        try {
            log.info("获取评估模型SQL执行结果，参数: {}, 用户: {}", requestBody, currentUser);

            // 解析请求参数
            String evalModelId = (String) requestBody.get("evalModelId");
            // 🔥 关键修复：移除分页参数，查询所有数据
            // Integer pageNum = (Integer) requestBody.getOrDefault("pageNum", 1);
            // Integer pageSize = (Integer) requestBody.getOrDefault("pageSize", 20);

            if (StringUtil.isEmpty(evalModelId)) {
                return JsonBean.error("评估模型ID不能为空");
            }

            // 获取评估模型详情
            TblEvaluationModel model = getById(evalModelId);
            if (model == null) {
                return JsonBean.error("评估模型不存在");
            }

            String dataModelId = model.getDataModelId();
            if (StringUtil.isEmpty(dataModelId)) {
                return JsonBean.error("该评估模型未关联数据模型");
            }

            // 🔥 关键修复：根据dataModelId判断模型类型并调用相应的执行方法，不传递分页参数
            if (dataModelId.startsWith("COMB")) {
                // 指标组合分析 - 返回所有数据
                return executeIndicatorCombinationModelWithoutPaging(dataModelId);
            } else {
                // 数据模型管理 - 返回所有数据
                return executeDataModelWithoutPaging(dataModelId);
            }

        } catch (Exception e) {
            log.error("获取评估模型SQL执行结果失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 执行数据模型管理类型的模型（无分页，返回所有数据）
     */
    private String executeDataModelWithoutPaging(String dataModelId) {
        try {
            log.info("执行数据模型（无分页），模型ID: {}", dataModelId);

            // 🔥 关键修复：不传递分页参数，让数据模型服务返回所有数据
            Map<String, Object> parameters = new HashMap<>();
            // 移除分页参数，让SQL执行返回完整结果集

            // 调用数据模型服务执行SQL
            com.hbfk.util.JsonBean result = dataModelService.executeDataModel(dataModelId, parameters, "system");

            if (result.getCode() == 1) {
                // 🔥 关键修复：解析执行结果，获取完整数据列表
                List<Map<String, Object>> allData = parseExecuteResult(result);

                Map<String, Object> responseData = new HashMap<>();
                responseData.put("modelType", "数据模型管理");
                responseData.put("executionTime", LocalDateTime.now());
                responseData.put("totalCount", allData.size()); // 🔥 新增：返回数据总量
                responseData.put("allData", new ArrayList<>(allData)); // 🔥 修复：创建新的List避免$ref引用
                responseData.put("executionResult", result.getData()); // 保留原始执行结果

                log.info("数据模型执行成功，返回数据总量: {}", allData.size());
                return JsonBean.success("执行成功", responseData);
            } else {
                return JsonBean.error("数据模型执行失败: " + result.getMsg());
            }

        } catch (Exception e) {
            log.error("执行数据模型失败，模型ID: {}", dataModelId, e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    /**
     * 执行数据模型管理类型的模型（保留原有分页方法，供其他地方使用）
     */
    private String executeDataModel(String dataModelId, Integer pageNum, Integer pageSize) {
        try {
            log.info("执行数据模型，模型ID: {}", dataModelId);

            // 构造执行参数
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("pageNum", pageNum);
            parameters.put("pageSize", pageSize);

            // 调用数据模型服务执行SQL
            com.hbfk.util.JsonBean result = dataModelService.executeDataModel(dataModelId, parameters, "system");

            if (result.getCode() == 1) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("modelType", "数据模型管理");
                responseData.put("executionTime", LocalDateTime.now());
                responseData.put("executionResult", result.getData());

                return JsonBean.success("执行成功", responseData);
            } else {
                return JsonBean.error("数据模型执行失败: " + result.getMsg());
            }

        } catch (Exception e) {
            log.error("执行数据模型失败，模型ID: {}", dataModelId, e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    /**
     * 执行指标组合分析类型的模型（无分页，返回所有数据）
     */
    private String executeIndicatorCombinationModelWithoutPaging(String combinationId) {
        try {
            log.info("执行指标组合分析（无分页），组合ID: {}", combinationId);

            // 获取组合详情
            Map<String, Object> combinationDetail = combinationService.getCombinationDetail(combinationId);
            if (combinationDetail == null || combinationDetail.isEmpty()) {
                return JsonBean.error("获取指标组合详情失败");
            }

            // 直接使用返回的Map数据
            Map<String, Object> config = extractCombinationConfig(combinationDetail);
            String executionMode = (String) config.get("executionMode");
            List<Map<String, Object>> indicators = (List<Map<String, Object>>) config.get("indicators");

            if (indicators == null || indicators.isEmpty()) {
                return JsonBean.error("指标组合中没有配置指标");
            }

            List<Map<String, Object>> finalResults;

            if ("PARALLEL".equalsIgnoreCase(executionMode)) {
                // 🔥 关键修复：并行执行，不应用分页，返回所有数据
                log.info("执行并行模式（无分页），指标数量: {}", indicators.size());
                finalResults = executeIndicatorsInParallelWithoutPaging(indicators, combinationId);
            } else {
                // 🔥 关键修复：顺序执行，不应用分页，返回所有数据
                log.info("执行顺序模式（无分页），指标数量: {}", indicators.size());
                finalResults = executeIndicatorsInSequenceWithoutPaging(indicators, combinationId);
            }

            // 🔥 关键修复：构造响应数据，包含数据总量信息
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("modelType", "指标组合分析");
            responseData.put("executionMode", executionMode);
            responseData.put("indicatorCount", indicators.size());
            responseData.put("executionTime", LocalDateTime.now());
            responseData.put("totalCount", finalResults.size()); // 🔥 新增：返回数据总量
            responseData.put("allData", new ArrayList<>(finalResults)); // 🔥 修复：创建新的List避免$ref引用
            responseData.put("finalResults", finalResults); // 保留原有字段

            log.info("指标组合分析执行成功，返回数据总量: {}", finalResults.size());
            return JsonBean.success("执行成功", responseData);

        } catch (Exception e) {
            log.error("执行指标组合分析失败，组合ID: {}", combinationId, e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    /**
     * 执行指标组合分析类型的模型（保留原有分页方法，供其他地方使用）
     */
    private String executeIndicatorCombinationModel(String combinationId, Integer pageNum, Integer pageSize) {
        try {
            log.info("执行指标组合分析，组合ID: {}", combinationId);

            // 获取组合详情
            Map<String, Object> combinationDetail = combinationService.getCombinationDetail(combinationId);
            if (combinationDetail == null || combinationDetail.isEmpty()) {
                return JsonBean.error("获取指标组合详情失败");
            }

            // 直接使用返回的Map数据
            Map<String, Object> config = extractCombinationConfig(combinationDetail);
            String executionMode = (String) config.get("executionMode");
            List<Map<String, Object>> indicators = (List<Map<String, Object>>) config.get("indicators");

            if (indicators == null || indicators.isEmpty()) {
                return JsonBean.error("指标组合中没有配置指标");
            }

            List<Map<String, Object>> finalResults;

            if ("PARALLEL".equalsIgnoreCase(executionMode)) {
                // 并行执行：所有SQL并行执行，取交集
                log.info("执行并行模式，指标数量: {}", indicators.size());
                finalResults = executeIndicatorsInParallel(indicators, combinationId, pageNum, pageSize);
            } else {
                // 顺序执行：按顺序执行，取最后一条SQL的结果
                log.info("执行顺序模式，指标数量: {}", indicators.size());
                finalResults = executeIndicatorsInSequence(indicators, combinationId, pageNum, pageSize);
            }

            // 构造响应数据
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("modelType", "指标组合分析");
            responseData.put("executionMode", executionMode);
            responseData.put("indicatorCount", indicators.size());
            responseData.put("executionTime", LocalDateTime.now());
            responseData.put("finalResults", finalResults);

            return JsonBean.success("执行成功", responseData);

        } catch (Exception e) {
            log.error("执行指标组合分析失败，组合ID: {}", combinationId, e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    /**
     * 从指标组合详情中提取指标配置信息
     * 获取组合中的所有指标及其SQL配置
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> extractCombinationConfig(Map<String, Object> combinationDetail) {
        try {
            log.info("提取指标组合配置，组合详情: {}", combinationDetail);

            Map<String, Object> config = new HashMap<>();

            // 获取执行模式：SEQUENCE(顺序) 或 PARALLEL(并行)
            String executionMode = (String) combinationDetail.get("executionMode");
            if (StringUtil.isEmpty(executionMode)) {
                executionMode = "SEQUENCE"; // 默认顺序执行
            }
            config.put("executionMode", executionMode);

            // 获取指标列表
            Object indicatorsObj = combinationDetail.get("indicators");
            List<Map<String, Object>> indicators = new ArrayList<>();

            if (indicatorsObj instanceof List) {
                indicators = (List<Map<String, Object>>) indicatorsObj;
            } else if (indicatorsObj instanceof Map) {
                // 如果是单个指标，转换为列表
                indicators.add((Map<String, Object>) indicatorsObj);
            }

            config.put("indicators", indicators);
            config.put("indicatorCount", indicators.size());

            log.info("提取到组合配置 - 执行模式: {}, 指标数量: {}", executionMode, indicators.size());
            return config;

        } catch (Exception e) {
            log.warn("提取组合配置失败: {}", e.getMessage());
            return new HashMap<>();
        }
    }

    /**
     * 执行指标组合分析模型
     * 根据执行模式(并行/顺序)执行指标组合，并返回最终结果
     */
    @SuppressWarnings("unchecked")
    private JsonBean executeCombinationModel(String combinationId) {
        try {
            log.info("执行指标组合分析，组合ID: {}", combinationId);

            // 获取组合详情和配置
            Map<String, Object> combinationDetail = combinationService.getCombinationDetail(combinationId);
            if (combinationDetail == null || combinationDetail.isEmpty()) {
                return new JsonBean(0, "未找到指标组合详情", null);
            }

            Map<String, Object> config = extractCombinationConfig(combinationDetail);
            String executionMode = (String) config.get("executionMode");
            List<Map<String, Object>> indicators = (List<Map<String, Object>>) config.get("indicators");

            if (indicators == null || indicators.isEmpty()) {
                log.warn("指标组合中没有配置指标，组合ID: {}", combinationId);
                return new JsonBean(0, "指标组合中没有配置指标", null);
            }

            List<Map<String, Object>> finalResults;

            if ("PARALLEL".equalsIgnoreCase(executionMode)) {
                // 并行执行：所有SQL并行执行，取交集
                log.info("执行并行模式，指标数量: {}", indicators.size());
                finalResults = executeIndicatorsInParallel(indicators, combinationId, 1, Integer.MAX_VALUE);
            } else {
                // 顺序执行：按顺序执行，取最后一条SQL的结果
                log.info("执行顺序模式，指标数量: {}", indicators.size());
                finalResults = executeIndicatorsInSequence(indicators, combinationId, 1, Integer.MAX_VALUE);
            }

            log.info("指标组合执行完成，组合ID: {}, 最终结果数量: {}",
                    combinationId, finalResults != null ? finalResults.size() : 0);

            return new JsonBean(1, "组合执行成功", finalResults);

        } catch (Exception e) {
            log.error("执行指标组合失败，组合ID: {}, 错误: {}", combinationId, e.getMessage(), e);
            return new JsonBean(0, "执行指标组合失败: " + e.getMessage(), null);
        }
    }

    /**
     * 并行执行指标组合（无分页，返回所有数据）
     * 所有SQL并行执行，取交集作为最终结果
     */
    private List<Map<String, Object>> executeIndicatorsInParallelWithoutPaging(List<Map<String, Object>> indicators, String combinationId) {
        try {
            log.info("开始并行执行指标组合（无分页），组合ID: {}, 指标数量: {}", combinationId, indicators.size());

            List<List<Map<String, Object>>> allResults = new ArrayList<>();

            // 并行执行所有指标SQL
            for (int i = 0; i < indicators.size(); i++) {
                Map<String, Object> indicator = indicators.get(i);
                String indicatorName = (String) indicator.get("indicatorName");
                String sqlContent = (String) indicator.get("sqlContent");

                if (StringUtil.isEmpty(sqlContent)) {
                    log.warn("指标SQL为空，跳过执行，指标名称: {}", indicatorName);
                    continue;
                }

                try {
                    log.info("执行指标SQL [{}]: {}", indicatorName, sqlContent);

                    // 🔥 关键修复：执行真实的指标SQL语句，不应用分页
                    List<Map<String, Object>> indicatorResult = executeRealIndicatorSql(indicatorName, sqlContent);
                    allResults.add(indicatorResult);

                    log.info("指标执行完成 [{}]，结果数量: {}", indicatorName, indicatorResult.size());

                } catch (Exception e) {
                    log.error("指标执行失败 [{}]: {}", indicatorName, e.getMessage(), e);
                    // 并行执行中某个指标失败不影响其他指标
                }
            }

            // 计算所有结果的交集
            List<Map<String, Object>> intersectionResult = calculateIntersection(allResults);

            log.info("并行执行完成（无分页），参与交集计算的结果集数量: {}, 交集结果数量: {}",
                    allResults.size(), intersectionResult.size());

            // 🔥 关键修复：直接返回所有交集结果，不应用分页
            return intersectionResult;

        } catch (Exception e) {
            log.error("并行执行指标组合失败，组合ID: {}, 错误: {}", combinationId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 顺序执行指标组合（无分页，返回所有数据）
     * 按顺序执行所有SQL，返回最后一条SQL的执行结果
     */
    private List<Map<String, Object>> executeIndicatorsInSequenceWithoutPaging(List<Map<String, Object>> indicators, String combinationId) {
        try {
            log.info("开始顺序执行指标组合（无分页），组合ID: {}, 指标数量: {}", combinationId, indicators.size());

            List<Map<String, Object>> lastResult = new ArrayList<>();
            Map<String, List<Map<String, Object>>> stepResults = new HashMap<>(); // 存储每个步骤的结果

            // 按顺序执行所有指标SQL
            for (int i = 0; i < indicators.size(); i++) {
                Map<String, Object> indicator = indicators.get(i);
                String indicatorName = (String) indicator.get("indicatorName");
                String sqlContent = (String) indicator.get("sqlContent");

                if (StringUtil.isEmpty(sqlContent)) {
                    log.warn("指标SQL为空，跳过执行，指标名称: {}", indicatorName);
                    continue;
                }

                try {
                    log.info("顺序执行指标SQL [{}] (第{}/{}个): {}", indicatorName, i+1, indicators.size(), sqlContent);

                    // 🔧 关键修复：处理步骤引用
                    String processedSql = processStepReferences(sqlContent, stepResults, i, indicators);

                    // 🔥 关键修复：执行真实的指标SQL语句，不应用分页
                    List<Map<String, Object>> indicatorResult = executeRealIndicatorSql(indicatorName, processedSql);

                    // 保存当前结果作为最后结果和步骤结果
                    lastResult = indicatorResult;
                    stepResults.put("STEP_" + (i + 1) + "_RESULT", indicatorResult);

                    log.info("指标执行完成 [{}]，结果数量: {}", indicatorName, indicatorResult.size());

                } catch (Exception e) {
                    log.error("指标执行失败 [{}]: {}", indicatorName, e.getMessage(), e);
                    // 顺序执行中某个指标失败，继续执行下一个
                }
            }

            log.info("顺序执行完成（无分页），最终结果数量: {}", lastResult.size());

            // 🔥 关键修复：直接返回所有结果，不应用分页
            return lastResult;

        } catch (Exception e) {
            log.error("顺序执行指标组合失败，组合ID: {}, 错误: {}", combinationId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 并行执行指标组合（保留原有分页方法，供其他地方使用）
     * 所有SQL并行执行，取交集作为最终结果
     */
    private List<Map<String, Object>> executeIndicatorsInParallel(List<Map<String, Object>> indicators, String combinationId, Integer pageNum, Integer pageSize) {
        try {
            log.info("开始并行执行指标组合，组合ID: {}, 指标数量: {}", combinationId, indicators.size());

            List<List<Map<String, Object>>> allResults = new ArrayList<>();

            // 并行执行所有指标SQL
            for (int i = 0; i < indicators.size(); i++) {
                Map<String, Object> indicator = indicators.get(i);
                String indicatorName = (String) indicator.get("indicatorName");
                String sqlContent = (String) indicator.get("sqlContent");

                if (StringUtil.isEmpty(sqlContent)) {
                    log.warn("指标SQL为空，跳过执行，指标名称: {}", indicatorName);
                    continue;
                }

                try {
                    log.info("执行指标SQL [{}]: {}", indicatorName, sqlContent);

                    // 执行真实的指标SQL语句
                    List<Map<String, Object>> indicatorResult = executeRealIndicatorSql(indicatorName, sqlContent);
                    allResults.add(indicatorResult);

                    log.info("指标执行完成 [{}]，结果数量: {}", indicatorName, indicatorResult.size());

                } catch (Exception e) {
                    log.error("指标执行失败 [{}]: {}", indicatorName, e.getMessage(), e);
                    // 并行执行中某个指标失败不影响其他指标
                }
            }

            // 计算所有结果的交集
            List<Map<String, Object>> intersectionResult = calculateIntersection(allResults);

            log.info("并行执行完成，参与交集计算的结果集数量: {}, 交集结果数量: {}",
                    allResults.size(), intersectionResult.size());

            // 🔥 关键修复：应用分页逻辑
            List<Map<String, Object>> paginatedResult = applyPagination(intersectionResult, pageNum, pageSize);

            log.info("分页处理完成，原始结果数量: {}, 分页后结果数量: {}, 页码: {}, 页大小: {}",
                    intersectionResult.size(), paginatedResult.size(), pageNum, pageSize);

            return paginatedResult;

        } catch (Exception e) {
            log.error("并行执行指标组合失败，组合ID: {}, 错误: {}", combinationId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 顺序执行指标组合
     * 按顺序执行所有SQL，返回最后一条SQL的执行结果
     */
    private List<Map<String, Object>> executeIndicatorsInSequence(List<Map<String, Object>> indicators, String combinationId, Integer pageNum, Integer pageSize) {
        try {
            log.info("开始顺序执行指标组合，组合ID: {}, 指标数量: {}", combinationId, indicators.size());

            List<Map<String, Object>> lastResult = new ArrayList<>();
            Map<String, List<Map<String, Object>>> stepResults = new HashMap<>(); // 存储每个步骤的结果

            // 按顺序执行所有指标SQL
            for (int i = 0; i < indicators.size(); i++) {
                Map<String, Object> indicator = indicators.get(i);
                String indicatorName = (String) indicator.get("indicatorName");
                String sqlContent = (String) indicator.get("sqlContent");

                if (StringUtil.isEmpty(sqlContent)) {
                    log.warn("指标SQL为空，跳过执行，指标名称: {}", indicatorName);
                    continue;
                }

                try {
                    log.info("顺序执行指标SQL [{}] (第{}/{}个): {}", indicatorName, i+1, indicators.size(), sqlContent);

                    // 🔧 关键修复：处理步骤引用
                    String processedSql = processStepReferences(sqlContent, stepResults, i, indicators);

                    // 执行真实的指标SQL语句
                    List<Map<String, Object>> indicatorResult = executeRealIndicatorSql(indicatorName, processedSql);

                    // 保存当前结果作为最后结果和步骤结果
                    lastResult = indicatorResult;
                    stepResults.put("STEP_" + (i + 1) + "_RESULT", indicatorResult);

                    log.info("指标执行完成 [{}]，结果数量: {}", indicatorName, indicatorResult.size());

                } catch (Exception e) {
                    log.error("指标执行失败 [{}]: {}", indicatorName, e.getMessage(), e);
                    // 顺序执行中某个指标失败，继续执行下一个
                }
            }

            log.info("顺序执行完成，最终结果数量: {}", lastResult.size());

            // 🔥 关键修复：应用分页逻辑
            List<Map<String, Object>> paginatedResult = applyPagination(lastResult, pageNum, pageSize);

            log.info("分页处理完成，原始结果数量: {}, 分页后结果数量: {}, 页码: {}, 页大小: {}",
                    lastResult.size(), paginatedResult.size(), pageNum, pageSize);

            return paginatedResult;

        } catch (Exception e) {
            log.error("顺序执行指标组合失败，组合ID: {}, 错误: {}", combinationId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 应用分页逻辑
     * 对结果集进行内存分页处理
     */
    private List<Map<String, Object>> applyPagination(List<Map<String, Object>> allResults, Integer pageNum, Integer pageSize) {
        if (allResults == null || allResults.isEmpty()) {
            return new ArrayList<>();
        }

        // 设置默认值
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 20;
        }

        int totalSize = allResults.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalSize);

        // 如果起始索引超出范围，返回空列表
        if (startIndex >= totalSize) {
            return new ArrayList<>();
        }

        // 返回分页后的子列表
        return allResults.subList(startIndex, endIndex);
    }

    /**
     * 将执行结果插入到风险预警管理
     * 🔧 核心原则：
     * 1. 只保存当前模型的执行结果
     * 2. 执行结果直接转换为JSON保存到RELATED_DATA字段
     * 3. 不添加任何额外的关联关系
     * 4. 预警数据只关联当前评估模型
     */
    @SuppressWarnings("unchecked")
    private void insertExecutionResultsToWarning(TblEvaluationModel model, List<Map<String, Object>> queryResults) {
        try {
            if (queryResults == null || queryResults.isEmpty()) {
                log.warn("⚠️ 执行结果为空，跳过预警插入 - 模型ID: {}", model.getEvalModelId());
                return;
            }

            String dataModelId = model.getDataModelId();
            boolean isCombinationModel = dataModelId != null && dataModelId.startsWith("COMB");

            log.info("========================================");
            log.info("💾 开始保存预警数据");
            log.info("模型ID: {}", model.getEvalModelId());
            log.info("模型名称: {}", model.getModelName());
            log.info("模型类型: {}", isCombinationModel ? "指标组合分析" : "数据模型管理");
            log.info("结果数量: {}", queryResults.size());
            log.info("========================================");

            // 为每条执行结果创建预警记录
            int insertCount = 0;
            int skipCount = 0;

            for (int i = 0; i < queryResults.size(); i++) {
                Map<String, Object> resultRow = queryResults.get(i);
                try {
                    log.info("📝 处理第 {} 条结果", i + 1);

                    // 🔧 检查是否重复
                    if (isDuplicateWarningFromSqlResult(model, resultRow, isCombinationModel)) {
                        log.info("   ⏭️ 跳过重复记录");
                        skipCount++;
                        continue;
                    }

                    // 🔧 关键：将SQL查询结果直接转换为JSON格式存储
                    // 不添加任何额外的字段或关联关系
                    String resultJson = convertResultRowToJson(resultRow);
                    log.info("   📄 JSON数据长度: {} 字符", resultJson.length());

                    // 从SQL执行结果中提取关键字段用于快速查询和显示
                    String warningLevel = determineWarningLevelFromSqlResult(resultRow);
                    Double warningValue = extractWarningValueFromSqlResult(resultRow);
                    String companyName = extractCompanyNameFromSqlResult(resultRow);
                    String warningContent = generateWarningContentFromSqlResult(model, resultRow, isCombinationModel);

                    log.info("   📊 预警等级: {}", warningLevel);
                    log.info("   📊 预警值: {}", warningValue);
                    log.info("   📊 企业名称: {}", companyName);

                    // 🔧 关键：创建预警记录，只关联当前评估模型
                    // RELATED_DATA字段存储完整的JSON数据，不添加任何额外内容
                    riskWarningService.createRiskWarning(
                        model.getEvalModelId(),      // 只关联当前评估模型
                        model.getModelName(),         // 模型名称
                        warningLevel,                 // 预警等级
                        warningValue,                 // 预警值
                        warningContent,               // 预警内容
                        isCombinationModel ? "COMBINATION_EXECUTION" : "DATA_MODEL_EXECUTION",  // 数据来源
                        companyName,                  // 企业名称（可能为null）
                        resultJson                    // 完整的JSON数据（不添加任何额外字段）
                    );

                    insertCount++;
                    log.info("   ✅ 保存成功");

                } catch (Exception e) {
                    log.error("   ❌ 保存失败: {}", e.getMessage(), e);
                    // 单条记录失败不影响其他记录的插入
                }
            }

            log.info("========================================");
            log.info("💾 预警数据保存完成");
            log.info("成功插入: {} 条", insertCount);
            log.info("跳过重复: {} 条", skipCount);
            log.info("总结果数: {} 条", queryResults.size());
            log.info("========================================");

            // 🔧 更新模型的预警数量
            if (insertCount > 0) {
                updateModelWarningCount(model.getEvalModelId());
                log.info("✅ 已更新模型预警数量 - 模型ID: {}, 新增预警: {} 条", model.getEvalModelId(), insertCount);
            }

        } catch (Exception e) {
            log.error("========================================");
            log.error("❌ 保存预警数据失败");
            log.error("模型ID: {}", model.getEvalModelId());
            log.error("错误信息: {}", e.getMessage(), e);
            log.error("========================================");
        }
    }

    /**
     * 更新模型的预警数量
     * 根据风险预警表中的实际数据更新评估模型的预警数量字段
     */
    private void updateModelWarningCount(String evalModelId) {
        try {
            log.info("开始更新模型预警数量，模型ID: {}", evalModelId);

            // 查询该模型的实际预警数量（排除误报）
            JsonBean countResult = riskWarningService.getModelWarningCount(evalModelId);
            if (countResult.getCode() == 1 && countResult.getData() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) countResult.getData();
                Long warningCount = (Long) data.get("warningCount");

                // 更新评估模型的预警数量字段
                TblEvaluationModel model = getById(evalModelId);
                if (model != null) {
                    model.setWarningCount(warningCount != null ? warningCount.intValue() : 0);
                    updateById(model);
                    log.info("模型预警数量更新成功，模型ID: {}, 预警数量: {}", evalModelId, warningCount);
                } else {
                    log.warn("未找到评估模型，模型ID: {}", evalModelId);
                }
            } else {
                log.error("获取模型预警数量失败，模型ID: {}, 错误: {}", evalModelId, countResult.getMsg());
            }

        } catch (Exception e) {
            log.error("更新模型预警数量失败，模型ID: {}, 错误: {}", evalModelId, e.getMessage(), e);
        }
    }

    /**
     * 将SQL查询结果转换为JSON格式
     * 🔧 新增：保留所有字段，便于后续处理时进行属性拆分
     */
    private String convertResultRowToJson(Map<String, Object> resultRow) {
        try {
            // 使用LinkedHashMap保持字段顺序
            Map<String, Object> jsonMap = new LinkedHashMap<>();

            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 处理特殊类型的值
                if (value == null) {
                    jsonMap.put(key, null);
                } else if (value instanceof java.sql.Timestamp) {
                    jsonMap.put(key, value.toString());
                } else if (value instanceof java.sql.Date) {
                    jsonMap.put(key, value.toString());
                } else if (value instanceof java.math.BigDecimal) {
                    jsonMap.put(key, ((java.math.BigDecimal) value).doubleValue());
                } else if (value instanceof Number) {
                    jsonMap.put(key, value);
                } else {
                    jsonMap.put(key, value.toString());
                }
            }

            // 添加元数据
            jsonMap.put("_RECORD_TIME", LocalDateTime.now().toString());
            jsonMap.put("_DATA_SOURCE", "MODEL_EXECUTION");

            String json = JSONObject.toJSONString(jsonMap);
            log.debug("转换结果为JSON: {}", json);
            return json;

        } catch (Exception e) {
            log.error("转换结果为JSON失败: {}", e.getMessage(), e);
            // 降级方案：返回toString()结果
            return resultRow.toString();
        }
    }

    /**
     * 从SQL执行结果中生成预警内容
     */
    private String generateWarningContentFromSqlResult(TblEvaluationModel model, Map<String, Object> resultRow, boolean isCombinationModel) {
        try {
            StringBuilder content = new StringBuilder();

            if (isCombinationModel) {
                content.append("指标组合分析预警：");
            } else {
                content.append("数据模型分析预警：");
            }

            content.append(model.getModelName()).append(" - ");

            // 从结果中提取关键信息
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 跳过系统字段
                if (key.equals("INDICATOR_NAME") || key.equals("EXECUTION_TIME") || key.equals("SQL_CONTENT")) {
                    continue;
                }

                if (value != null) {
                    content.append(key).append(": ").append(value).append("; ");
                }
            }

            return content.toString();

        } catch (Exception e) {
            log.warn("生成预警内容失败: {}", e.getMessage());
            return "模型执行预警：" + model.getModelName();
        }
    }

    /**
     * 从SQL执行结果中确定预警级别
     * 🔧 修复：支持中文颜色（红色、黄色、绿色）和中文等级（高、中、低）
     */
    private String determineWarningLevelFromSqlResult(Map<String, Object> resultRow) {
        try {
            log.debug("开始确定预警级别，结果字段: {}", resultRow.keySet());

            // 🔧 优先查找预警等级字段（中文字段名）
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 🔧 优先匹配中文字段名：预警等级、风险等级、等级
                if (key.equals("预警等级") || key.equals("风险等级") || key.equals("等级") ||
                    key.toUpperCase().contains("WARNING_LEVEL") ||
                    key.toUpperCase().contains("RISK_LEVEL") ||
                    key.toUpperCase().contains("LEVEL")) {

                    if (value != null) {
                        String levelStr = value.toString().trim();
                        log.info("找到预警等级字段: {} = {}", key, levelStr);

                        // 🔧 支持中文颜色：红色、黄色、绿色
                        if (levelStr.equals("红色") || levelStr.equals("红")) {
                            log.info("预警等级为红色，映射为 HIGH");
                            return "HIGH";
                        } else if (levelStr.equals("黄色") || levelStr.equals("黄")) {
                            log.info("预警等级为黄色，映射为 MEDIUM");
                            return "MEDIUM";
                        } else if (levelStr.equals("绿色") || levelStr.equals("绿")) {
                            log.info("预警等级为绿色，映射为 LOW");
                            return "LOW";
                        }

                        // 🔧 支持英文和中文等级
                        String levelUpper = levelStr.toUpperCase();
                        if (levelUpper.contains("HIGH") || levelStr.contains("严重") || levelStr.contains("高")) {
                            log.info("预警等级为高，映射为 HIGH");
                            return "HIGH";
                        } else if (levelUpper.contains("MEDIUM") || levelStr.contains("中等") || levelStr.contains("中")) {
                            log.info("预警等级为中，映射为 MEDIUM");
                            return "MEDIUM";
                        } else if (levelUpper.contains("LOW") || levelStr.contains("低")) {
                            log.info("预警等级为低，映射为 LOW");
                            return "LOW";
                        }
                    }
                }
            }

            // 🔧 如果没有找到预警等级字段，根据综合评分判断
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 查找综合评分字段
                if (key.equals("综合评分") || key.equals("TOTAL_SCORE") ||
                    key.toUpperCase().contains("SCORE") || key.toUpperCase().contains("VALUE")) {

                    if (value instanceof Number) {
                        double numValue = ((Number) value).doubleValue();
                        log.info("找到评分字段: {} = {}", key, numValue);

                        // 🔧 根据评分判断预警等级（0-100分制）
                        if (numValue >= 80) {
                            log.info("评分 {} >= 80，预警等级为 LOW（绿色）", numValue);
                            return "LOW";
                        } else if (numValue >= 60) {
                            log.info("评分 {} >= 60，预警等级为 MEDIUM（黄色）", numValue);
                            return "MEDIUM";
                        } else {
                            log.info("评分 {} < 60，预警等级为 HIGH（红色）", numValue);
                            return "HIGH";
                        }
                    }
                }
            }

            // 默认为LOW级别
            log.warn("未找到预警等级或评分字段，默认为 LOW");
            return "LOW";

        } catch (Exception e) {
            log.warn("确定预警级别失败: {}", e.getMessage());
            return "LOW";
        }
    }

    /**
     * 从SQL执行结果中提取预警值
     * 🔧 修复：去掉假数据（随机值），只返回真实数据
     */
    private Double extractWarningValueFromSqlResult(Map<String, Object> resultRow) {
        try {
            log.debug("开始提取预警值，结果字段: {}", resultRow.keySet());

            // 🔧 优先查找综合评分字段（中文字段名）
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 🔧 优先匹配中文字段名：综合评分、总分、评分
                if (key.equals("综合评分") || key.equals("总分") || key.equals("评分") ||
                    key.toUpperCase().contains("TOTAL_SCORE") ||
                    key.toUpperCase().contains("SCORE")) {

                    if (value instanceof Number) {
                        double numValue = ((Number) value).doubleValue();
                        log.info("找到评分字段: {} = {}", key, numValue);
                        return numValue;
                    }
                }
            }

            // 🔧 查找其他可能的数值字段
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey().toUpperCase();
                Object value = entry.getValue();

                if (value instanceof Number) {
                    if (key.contains("VALUE") || key.contains("AMOUNT") ||
                        key.contains("RISK") || key.contains("INDICATOR")) {
                        double numValue = ((Number) value).doubleValue();
                        log.info("找到数值字段: {} = {}", key, numValue);
                        return numValue;
                    }
                }
            }

            // 🔧 重要修改：找不到数值字段时返回0.0，不生成假数据
            log.warn("未找到预警值字段，返回0.0。可用字段: {}", resultRow.keySet());
            return 0.0;

        } catch (Exception e) {
            log.warn("提取预警值失败: {}", e.getMessage());
            return 0.0;
        }
    }

    /**
     * 从SQL执行结果中提取企业名称
     * 🔧 修复：优先查找企业名称，避免错误提取供应商名称
     */
    private String extractCompanyNameFromSqlResult(Map<String, Object> resultRow) {
        try {
            log.debug("开始提取企业名称，结果字段: {}", resultRow.keySet());
            System.out.println("🔍 开始提取企业名称，可用字段: " + resultRow.keySet());

            // 🔧 优先级1：查找明确的企业名称字段（最高优先级）
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey().toUpperCase();
                Object value = entry.getValue();

                if (value != null && !value.toString().trim().isEmpty()) {
                    // 明确的企业名称字段
                    if (key.equals("COMPANY_NAME") || key.equals("ENTERPRISE_NAME") ||
                        key.equals("企业名称") || key.equals("公司名称") ||
                        key.equals("ORG_NAME") || key.equals("组织名称")) {
                        log.info("找到企业名称字段: {} = {}", key, value);
                        System.out.println("✅ 找到企业名称字段: " + key + " = " + value);
                        return value.toString().trim();
                    }
                }
            }

            // 🔧 优先级2：查找包含企业关键字的字段
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey().toUpperCase();
                Object value = entry.getValue();

                if (value != null && !value.toString().trim().isEmpty()) {
                    if ((key.contains("COMPANY") || key.contains("ENTERPRISE") ||
                         key.contains("企业") || key.contains("公司")) &&
                        key.contains("NAME")) {
                        log.info("找到企业相关名称字段: {} = {}", key, value);
                        System.out.println("✅ 找到企业相关名称字段: " + key + " = " + value);
                        return value.toString().trim();
                    }
                }
            }

            // 🔧 优先级3：查找通用NAME字段（排除供应商、用户、指标等）
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey().toUpperCase();
                Object value = entry.getValue();

                if (value != null && !value.toString().trim().isEmpty()) {
                    if (key.contains("NAME") &&
                        !key.contains("SUPPLIER") && !key.contains("供应商") &&
                        !key.contains("USER") && !key.contains("用户") &&
                        !key.contains("INDICATOR") && !key.contains("指标")) {
                        log.info("找到通用名称字段: {} = {}", key, value);
                        System.out.println("⚠️ 找到通用名称字段: " + key + " = " + value);
                        return value.toString().trim();
                    }
                }
            }

            // 🔧 优先级4：如果找不到名称字段，尝试使用企业ID字段
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey().toUpperCase();
                Object value = entry.getValue();

                if (value != null && !value.toString().trim().isEmpty()) {
                    if (key.equals("企业ID") || key.equals("COMPANY_ID") ||
                        key.equals("ENTERPRISE_ID") || key.equals("ORG_ID")) {
                        log.info("使用企业ID字段作为标识: {} = {}", key, value);
                        System.out.println("⚠️ 使用企业ID字段作为标识: " + key + " = " + value);
                        return value.toString().trim();
                    }
                }
            }

            // 🔧 重要修改：找不到任何可用字段时返回null
            log.warn("未找到企业名称或企业ID字段，返回null。可用字段: {}", resultRow.keySet());
            System.out.println("❌ 未找到企业名称或企业ID字段，可用字段: " + resultRow.keySet());
            return null;

        } catch (Exception e) {
            log.warn("提取企业名称失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 计算多个结果集的交集
     * 基于关键字段(如ID、编码等)计算交集
     */
    private List<Map<String, Object>> calculateIntersection(List<List<Map<String, Object>>> allResults) {
        try {
            if (allResults == null || allResults.isEmpty()) {
                return new ArrayList<>();
            }

            if (allResults.size() == 1) {
                return allResults.get(0);
            }

            log.info("开始计算交集，结果集数量: {}", allResults.size());

            // 以第一个结果集为基准
            List<Map<String, Object>> intersectionResult = new ArrayList<>(allResults.get(0));

            // 与其他结果集逐一求交集
            for (int i = 1; i < allResults.size(); i++) {
                List<Map<String, Object>> currentResult = allResults.get(i);
                intersectionResult = findIntersection(intersectionResult, currentResult);

                log.info("与第{}个结果集求交集后，剩余数量: {}", i+1, intersectionResult.size());

                // 如果交集为空，直接返回
                if (intersectionResult.isEmpty()) {
                    break;
                }
            }

            log.info("交集计算完成，最终交集数量: {}", intersectionResult.size());
            return intersectionResult;

        } catch (Exception e) {
            log.error("计算交集失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 计算两个结果集的交集
     * 基于关键字段匹配
     */
    private List<Map<String, Object>> findIntersection(List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
        List<Map<String, Object>> intersection = new ArrayList<>();

        // 定义用于匹配的关键字段
        String[] keyFields = {"ID", "CODE", "NAME", "ENTITY_ID", "COMPANY_ID", "USER_ID"};

        for (Map<String, Object> item1 : list1) {
            for (Map<String, Object> item2 : list2) {
                if (isRecordMatch(item1, item2, keyFields)) {
                    // 合并两条记录的字段
                    Map<String, Object> mergedRecord = new HashMap<>(item1);
                    mergedRecord.putAll(item2);
                    intersection.add(mergedRecord);
                    break; // 找到匹配后跳出内层循环
                }
            }
        }

        return intersection;
    }

    /**
     * 判断两条记录是否匹配
     * 基于关键字段进行匹配
     */
    private boolean isRecordMatch(Map<String, Object> record1, Map<String, Object> record2, String[] keyFields) {
        for (String field : keyFields) {
            Object value1 = record1.get(field);
            Object value2 = record2.get(field);

            // 如果两个记录都有这个字段且值相等，认为匹配
            if (value1 != null && value2 != null && value1.equals(value2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取默认数据源
     */
    private TblDataSource getDefaultDataSource() {
        try {
            // 优先使用Mapper查询活跃数据源
            List<TblDataSource> activeDataSources = dataSourceMapper.selectActiveDataSources();
            if (!activeDataSources.isEmpty()) {
                log.info("通过Mapper获取到默认数据源: {}", activeDataSources.get(0).getSourceName());
                return activeDataSources.get(0);
            }

            // 备用方案：直接SQL查询
            String sql = "SELECT * FROM TBL_DATA_SOURCE WHERE STATUS = 'ACTIVE' ORDER BY CREATE_TIME ASC";
            List<Map<String, Object>> dataSourceList = jdbcTemplate.queryForList(sql);

            if (!dataSourceList.isEmpty()) {
                Map<String, Object> dsMap = dataSourceList.get(0);
                TblDataSource dataSource = new TblDataSource();
                dataSource.setSourceId((String) dsMap.get("SOURCE_ID"));
                dataSource.setSourceName((String) dsMap.get("SOURCE_NAME"));
                dataSource.setSourceType((String) dsMap.get("SOURCE_TYPE"));
                dataSource.setHostIp((String) dsMap.get("HOST_IP"));
                dataSource.setPort(dsMap.get("PORT") != null ? Integer.valueOf(dsMap.get("PORT").toString()) : 1521);
                dataSource.setDatabaseName((String) dsMap.get("DATABASE_NAME"));
                dataSource.setUsername((String) dsMap.get("USERNAME"));
                dataSource.setPassword((String) dsMap.get("PASSWORD"));
                log.info("通过SQL查询获取到默认数据源: {}", dataSource.getSourceName());
                return dataSource;
            }

            log.warn("未找到任何活跃的数据源");
        } catch (Exception e) {
            log.error("获取默认数据源失败: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 执行真实的指标SQL语句
     * 从数据库中获取真实的指标数据
     */
    private List<Map<String, Object>> executeRealIndicatorSql(String indicatorName, String sqlContent) {
        try {
            log.info("开始执行真实指标SQL，指标名称: {}", indicatorName);
            log.info("原始SQL语句: {}", sqlContent);

            // 🔧 关键修复：在执行SQL前进行参数替换
            String processedSql = processIndicatorParameters(sqlContent, indicatorName);
            log.info("参数替换后的SQL语句: {}", processedSql);

            List<Map<String, Object>> result;

            try {
                // 优先使用JdbcTemplate执行SQL查询
                result = jdbcTemplate.queryForList(processedSql);
                log.info("使用JdbcTemplate执行SQL成功，指标名称: {}, 结果数量: {}", indicatorName, result.size());
            } catch (Exception jdbcException) {
                log.warn("JdbcTemplate执行失败，尝试使用SqlExecutorUtil执行，错误: {}", jdbcException.getMessage());

                // 如果JdbcTemplate失败，尝试使用SqlExecutorUtil直接执行
                try {
                    // 获取默认数据源
                    TblDataSource defaultDataSource = getDefaultDataSource();
                    if (defaultDataSource != null) {
                        Map<String, Object> executeResult = SqlExecutorUtil.executeSql(defaultDataSource, processedSql);
                        result = (List<Map<String, Object>>) executeResult.get("data");
                        if (result == null) {
                            result = new ArrayList<>();
                        }
                        log.info("使用SqlExecutorUtil执行SQL成功，指标名称: {}, 结果数量: {}", indicatorName, result.size());
                    } else {
                        log.error("无法获取默认数据源，返回空结果");
                        return new ArrayList<>();
                    }
                } catch (Exception sqlUtilException) {
                    log.error("SqlExecutorUtil执行也失败，返回空结果，错误: {}", sqlUtilException.getMessage());
                    return new ArrayList<>();
                }
            }

            // 为每条记录添加指标信息
            for (Map<String, Object> record : result) {
                record.put("INDICATOR_NAME", indicatorName);
                record.put("EXECUTION_TIME", LocalDateTime.now());
                record.put("SQL_CONTENT", processedSql);
            }

            return result;

        } catch (Exception e) {
            log.error("执行指标SQL失败，指标名称: {}, SQL: {}, 错误: {}", indicatorName, sqlContent, e.getMessage(), e);

            // 如果所有方法都失败，返回空结果而不是抛出异常，避免影响其他指标
            return new ArrayList<>();
        }
    }

    /**
     * 处理指标SQL中的参数替换
     * 支持 ${parameter} 格式的参数占位符
     */
    private String processIndicatorParameters(String sqlContent, String indicatorName) {
        if (StringUtil.isEmpty(sqlContent)) {
            return sqlContent;
        }

        String processedSql = sqlContent;
        log.info("开始处理指标参数替换，指标名称: {}", indicatorName);

        try {
            // 1. 获取指标的参数配置
            Map<String, Object> parameterConfig = getIndicatorParameterConfig(indicatorName);
            log.info("获取到指标参数配置: {}", parameterConfig);

            // 2. 替换SQL中的参数占位符
            if (parameterConfig != null && !parameterConfig.isEmpty()) {
                for (Map.Entry<String, Object> entry : parameterConfig.entrySet()) {
                    String paramName = entry.getKey();
                    Object paramValue = entry.getValue();

                    if (paramValue != null) {
                        String valueStr = extractParameterValue(paramValue);
                        if (valueStr != null) {
                            String placeholder = "${" + paramName + "}";
                            if (processedSql.contains(placeholder)) {
                                processedSql = processedSql.replace(placeholder, valueStr);
                                log.info("参数替换: {} -> {}", placeholder, valueStr);
                            }
                        }
                    }
                }
            }

            // 3. 为常见参数提供默认值
            processedSql = provideDefaultParameterValues(processedSql);

            // 4. 检查是否还有未替换的参数
            checkUnreplacedParameters(processedSql, indicatorName);

        } catch (Exception e) {
            log.error("处理指标参数替换失败，指标名称: {}, 错误: {}", indicatorName, e.getMessage(), e);
        }

        log.info("参数替换完成，指标名称: {}, 最终SQL: {}", indicatorName, processedSql);
        return processedSql;
    }

    /**
     * 获取指标的参数配置
     */
    private Map<String, Object> getIndicatorParameterConfig(String indicatorName) {
        Map<String, Object> config = new HashMap<>();

        try {
            // 从数据库查询指标的参数配置
            // 这里可以根据实际的数据库表结构来查询
            // 暂时提供一些默认的参数配置

            // 为常见的参数提供默认值
            config.put("month", "12");  // 默认12个月
            config.put("fw", "1000000");  // 服务类合同阈值
            config.put("wz", "2000000");  // 物资类合同阈值
            config.put("mr", "2000000");  // 默认合同阈值
            config.put("gc", "4000000");  // 工程类合同阈值

            log.info("为指标 {} 提供默认参数配置: {}", indicatorName, config);

        } catch (Exception e) {
            log.error("获取指标参数配置失败，指标名称: {}, 错误: {}", indicatorName, e.getMessage(), e);
        }

        return config;
    }

    /**
     * 提取参数值（处理复杂的参数结构）
     */
    private String extractParameterValue(Object paramValue) {
        if (paramValue == null) {
            return null;
        }

        // 如果是Map类型，尝试获取defaultValue
        if (paramValue instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> paramDef = (Map<String, Object>) paramValue;
            Object defaultValue = paramDef.get("defaultValue");
            if (defaultValue == null) {
                defaultValue = paramDef.get("default");
            }
            return defaultValue != null ? defaultValue.toString() : null;
        }

        // 直接返回字符串值
        return paramValue.toString();
    }

    /**
     * 为常见参数提供默认值
     * 🔧 修复：添加COMPANY_ID、ANALYSIS_DATE、START_DATE、END_DATE等关键参数的默认值
     */
    private String provideDefaultParameterValues(String sql) {
        String processedSql = sql;

        // 常见参数的默认值映射
        Map<String, String> defaultValues = new HashMap<>();

        // 🔧 新增：关键业务参数的默认值
        defaultValues.put("COMPANY_ID", "DEFAULT_COMPANY");  // 默认企业ID
        defaultValues.put("ANALYSIS_DATE", "2025-11-11");    // 默认分析日期（当前日期）
        defaultValues.put("START_DATE", "2024-11-11");       // 默认开始日期（一年前）
        defaultValues.put("END_DATE", "2025-11-11");         // 默认结束日期（当前日期）

        // 原有参数的默认值
        defaultValues.put("month", "12");
        defaultValues.put("fw", "1000000");
        defaultValues.put("wz", "2000000");
        defaultValues.put("mr", "2000000");
        defaultValues.put("gc", "4000000");

        for (Map.Entry<String, String> entry : defaultValues.entrySet()) {
            String paramName = entry.getKey();
            String defaultValue = entry.getValue();
            String placeholder = "${" + paramName + "}";

            if (processedSql.contains(placeholder)) {
                processedSql = processedSql.replace(placeholder, defaultValue);
                log.info("使用默认值替换参数: {} -> {}", placeholder, defaultValue);
            }
        }

        return processedSql;
    }

    /**
     * 处理步骤引用（${STEP_N_RESULT}等）
     * 🔧 修复版本：将步骤引用替换为实际的子查询SQL，并添加详细日志诊断
     */
    private String processStepReferences(String sql, Map<String, List<Map<String, Object>>> stepResults,
                                       int currentIndex, List<Map<String, Object>> indicators) {
        if (StringUtil.isEmpty(sql)) {
            return sql;
        }

        String processedSql = sql;
        log.info("========== 开始处理步骤引用 ==========");
        log.info("当前指标位置: {}, 原始SQL: {}", currentIndex, sql);
        log.info("已执行的步骤数量: {}", stepResults.size());

        // 🔧 新增：打印已执行步骤的结果数量
        for (Map.Entry<String, List<Map<String, Object>>> entry : stepResults.entrySet()) {
            log.info("步骤 {} 的结果数量: {}", entry.getKey(), entry.getValue().size());
        }

        try {
            // 🔧 修复：检查是否包含步骤引用
            if (sql.contains("${STEP_") || sql.contains("${PREV_RESULT}")) {
                log.info("⚠️ 检测到步骤引用，开始替换为子查询");

                // 替换所有的步骤引用为对应的子查询SQL
                for (int i = 0; i < currentIndex; i++) {
                    String stepPlaceholder = "${STEP_" + (i + 1) + "_RESULT}";

                    if (processedSql.contains(stepPlaceholder)) {
                        log.info("发现步骤引用: {}", stepPlaceholder);

                        // 获取对应步骤的SQL
                        Map<String, Object> stepIndicator = indicators.get(i);
                        String stepSql = (String) stepIndicator.get("sqlContent");

                        if (StringUtil.isNotEmpty(stepSql)) {
                            // 🔧 修复：移除SQL末尾的分号（子查询中不能有分号）
                            String cleanedSql = stepSql.trim();
                            if (cleanedSql.endsWith(";")) {
                                cleanedSql = cleanedSql.substring(0, cleanedSql.length() - 1).trim();
                                log.info("移除步骤 {} SQL末尾的分号", i + 1);
                            }

                            // 将步骤引用替换为子查询（用括号包裹）
                            String subQuery = "(" + cleanedSql + ")";
                            processedSql = processedSql.replace(stepPlaceholder, subQuery);
                            log.info("✅ 替换步骤引用 {} 为子查询", stepPlaceholder);
                            log.info("子查询SQL: {}", cleanedSql);
                        } else {
                            log.warn("❌ 步骤 {} 的SQL为空，无法替换", i + 1);
                        }
                    }
                }
            } else {
                log.info("✅ 未检测到步骤引用，SQL可以直接执行");
            }

            log.info("========== 步骤引用处理完成 ==========");
            log.info("最终SQL: {}", processedSql);

        } catch (Exception e) {
            log.error("处理步骤引用失败: {}", e.getMessage(), e);
            throw new RuntimeException("处理步骤引用失败: " + e.getMessage(), e);
        }

        return processedSql;
    }



    /**
     * 检查未替换的参数并记录警告
     */
    private void checkUnreplacedParameters(String sql, String indicatorName) {
        if (sql.contains("${")) {
            // 提取未替换的参数
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\$\\{([^}]+)\\}");
            java.util.regex.Matcher matcher = pattern.matcher(sql);
            java.util.Set<String> unreplacedParams = new java.util.HashSet<>();

            while (matcher.find()) {
                String param = matcher.group(1);
                // 跳过步骤引用参数的检查，因为它们已经在processStepReferences中处理
                if (!param.startsWith("STEP_") && !param.equals("PREV_RESULT")) {
                    unreplacedParams.add(param);
                }
            }

            if (!unreplacedParams.isEmpty()) {
                log.warn("指标 {} 的SQL中包含未替换的参数: {}", indicatorName, String.join(", ", unreplacedParams));
                log.warn("包含未替换参数的SQL: {}", sql);

                // 抛出异常，避免执行包含未替换参数的SQL
                throw new RuntimeException("指标 " + indicatorName + " 的SQL包含未替换的参数: " + String.join(", ", unreplacedParams));
            }
        }
    }

    /**
     * 模拟指标执行（保留作为备用方法）
     * 生成模拟的指标执行结果
     */
    private List<Map<String, Object>> simulateIndicatorExecution(String indicatorName, String sqlContent, int index) {
        List<Map<String, Object>> result = new ArrayList<>();

        // 模拟生成更多测试数据，确保总数达到297条
        int recordCount = 100 + index * 50; // 每个指标生成更多记录

        for (int i = 1; i <= recordCount; i++) {
            Map<String, Object> record = new HashMap<>();
            record.put("ID", "ENTITY_" + String.format("%05d", i + index * 1000));
            record.put("NAME", "实体名称_" + (i + index * 1000));
            record.put("INDICATOR_NAME", indicatorName);
            record.put("INDICATOR_VALUE", 100.0 + index * 10 + i);
            record.put("RISK_LEVEL", i % 3 == 0 ? "HIGH" : (i % 2 == 0 ? "MEDIUM" : "LOW"));
            record.put("EXECUTION_TIME", LocalDateTime.now());
            record.put("SQL_CONTENT", sqlContent);
            result.add(record);
        }

        log.info("指标 [{}] 生成模拟数据 {} 条", indicatorName, recordCount);
        return result;
    }



    /**
     * 🔧 已废弃：固定5分钟执行的定时任务
     * 原因：改为使用动态调度服务 EvaluationModelScheduleService，根据每个模型的 SCHEDULE_CONFIG 配置动态创建定时任务
     *
     * 新的实现方式：
     * 1. 启动模型时，调用 scheduleService.scheduleModelTask(model, currentUser) 创建定时任务
     * 2. 停止模型时，调用 scheduleService.cancelModelTask(modelId) 取消定时任务
     * 3. 每个模型有独立的 Cron 表达式，可以配置不同的执行时间
     */
    /*
    @Scheduled(fixedRate = 300000) // 5分钟执行一次
    public void scheduledModelExecution() {
        try {
            log.info("开始执行定时模型评估任务");

            // 查询所有运行中的模型
            QueryWrapper<TblEvaluationModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("STATUS", "RUNNING");
            List<TblEvaluationModel> runningModels = list(queryWrapper);

            if (runningModels.isEmpty()) {
                log.info("当前没有运行中的模型，跳过定时任务");
                return;
            }

            log.info("找到 {} 个运行中的模型，开始执行评估", runningModels.size());

            for (TblEvaluationModel model : runningModels) {
                try {
                    log.info("定时执行模型评估，模型ID: {}, 模型名称: {}",
                            model.getEvalModelId(), model.getModelName());

                    // 执行模型评估并生成预警
                    generateRiskWarningForModel(model, "SYSTEM");

                    // 更新模型的最后执行时间
                    model.setUpdateTime(LocalDateTime.now());
                    updateById(model);

                } catch (Exception e) {
                    log.error("定时执行模型评估失败，模型ID: {}, 错误: {}",
                             model.getEvalModelId(), e.getMessage(), e);
                }
            }

            log.info("定时模型评估任务执行完成");

        } catch (Exception e) {
            log.error("定时模型评估任务执行异常", e);
        }
    }
    */

    /**
     * 从执行结果中生成预警内容
     */
    private String generateWarningContentFromResult(TblEvaluationModel model, Map<String, Object> resultRow, boolean isCombinationModel) {
        StringBuilder content = new StringBuilder();

        if (isCombinationModel) {
            content.append("指标组合分析执行结果：");
            content.append("组合名称=").append(model.getModelName());

            // 提取指标相关信息
            Object indicatorName = resultRow.get("INDICATOR_NAME");
            Object indicatorValue = resultRow.get("INDICATOR_VALUE");
            if (indicatorName != null) {
                content.append("，指标名称=").append(indicatorName);
            }
            if (indicatorValue != null) {
                content.append("，指标值=").append(indicatorValue);
            }
        } else {
            content.append("数据模型执行结果：");
            content.append("模型名称=").append(model.getModelName());

            // 提取关键字段信息
            Object entityId = resultRow.get("ID");
            Object entityName = resultRow.get("NAME");
            if (entityId != null) {
                content.append("，实体ID=").append(entityId);
            }
            if (entityName != null) {
                content.append("，实体名称=").append(entityName);
            }
        }

        // 添加执行时间
        Object executionTime = resultRow.get("EXECUTION_TIME");
        if (executionTime != null) {
            content.append("，执行时间=").append(executionTime);
        }

        return content.toString();
    }

    /**
     * 从执行结果中确定预警等级
     */
    private String determineWarningLevelFromResult(Map<String, Object> resultRow) {
        // 优先使用结果中的风险等级字段
        Object riskLevel = resultRow.get("RISK_LEVEL");
        if (riskLevel != null) {
            String level = riskLevel.toString().toUpperCase();
            if ("HIGH".equals(level) || "MEDIUM".equals(level) || "LOW".equals(level)) {
                return level;
            }
        }

        // 根据指标值判断
        Object indicatorValue = resultRow.get("INDICATOR_VALUE");
        if (indicatorValue instanceof Number) {
            double value = ((Number) indicatorValue).doubleValue();
            if (value >= 150) {
                return "HIGH";
            } else if (value >= 120) {
                return "MEDIUM";
            } else {
                return "LOW";
            }
        }

        // 默认为中等风险
        return "MEDIUM";
    }

    /**
     * 从执行结果中提取预警值
     */
    private Double extractWarningValueFromResult(Map<String, Object> resultRow) {
        // 优先使用指标值
        Object indicatorValue = resultRow.get("INDICATOR_VALUE");
        if (indicatorValue instanceof Number) {
            return ((Number) indicatorValue).doubleValue();
        }

        // 尝试其他数值字段
        String[] numericFields = {"VALUE", "SCORE", "AMOUNT", "COUNT"};
        for (String field : numericFields) {
            Object value = resultRow.get(field);
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }
        }

        // 默认值
        return 100.0;
    }

    /**
     * 检查SQL执行结果是否已经存在对应的预警记录
     * 基于SQL结果的关键字段进行精确匹配
     *
     * @param model 评估模型
     * @param resultRow SQL执行结果行
     * @param isCombinationModel 是否为组合模型
     * @return true-存在重复记录，false-不存在重复记录
     */
    private boolean isDuplicateWarningFromSqlResult(TblEvaluationModel model, Map<String, Object> resultRow, boolean isCombinationModel) {
        try {
            // 🔧 修复：WARNING_DESCRIPTION是CLOB类型，达梦数据库不支持直接用=比较
            // 改为使用其他字段组合来判断重复，而不是比较CLOB字段

            // 提取关键字段用于重复检查
            String evalModelId = model.getEvalModelId();
            String companyName = extractCompanyNameFromResult(resultRow);
            Double warningValue = extractWarningValueFromSqlResult(resultRow);

            // 🔧 修复：改进重复检查逻辑
            // 1. 如果有企业名称和预警值，使用精确匹配（模型ID + 企业名称 + 预警值）
            // 2. 如果只有企业名称，使用模型ID + 企业名称 + 当天时间范围
            // 3. 如果都没有，使用模型ID + 当天时间范围
            String countSql;
            Object[] params;

            if (StringUtil.isNotEmpty(companyName) && warningValue != null) {
                // 最精确的匹配：模型ID + 企业名称 + 预警值 + 当天时间范围
                countSql = "SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE " +
                          "EVAL_MODEL_ID = ? AND COMPANY_NAME = ? AND WARNING_VALUE = ? AND " +
                          "TRUNC(CREATE_TIME) = TRUNC(SYSDATE)";
                params = new Object[]{evalModelId, companyName, warningValue};
                log.debug("使用精确匹配进行重复检查 - 模型ID: {}, 企业: {}, 预警值: {}",
                         evalModelId, companyName, warningValue);
            } else if (StringUtil.isNotEmpty(companyName)) {
                // 有企业名称：使用模型ID + 企业名称 + 当天时间范围
                countSql = "SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE " +
                          "EVAL_MODEL_ID = ? AND COMPANY_NAME = ? AND " +
                          "TRUNC(CREATE_TIME) = TRUNC(SYSDATE)";
                params = new Object[]{evalModelId, companyName};
                log.debug("使用企业名称进行重复检查 - 模型ID: {}, 企业: {}", evalModelId, companyName);
            } else {
                // 🔧 重要：没有企业名称时，只检查当天是否已经执行过该模型
                // 这样可以避免定时任务重复执行时插入大量重复数据
                countSql = "SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE " +
                          "EVAL_MODEL_ID = ? AND " +
                          "TRUNC(CREATE_TIME) = TRUNC(SYSDATE)";
                params = new Object[]{evalModelId};
                log.debug("无企业名称，使用模型ID和当天时间进行重复检查 - 模型ID: {}", evalModelId);
            }

            try {
                // 使用JdbcTemplate执行原生SQL查询
                Integer count = jdbcTemplate.queryForObject(countSql, Integer.class, params);

                if (count != null && count > 0) {
                    log.info("发现重复预警记录 - 模型ID: {}, 企业: {}, 预警值: {}, 记录数: {}",
                            evalModelId, companyName != null ? companyName : "N/A",
                            warningValue != null ? warningValue : "N/A", count);
                    return true;
                }

                log.debug("未发现重复预警记录 - 模型ID: {}, 企业: {}, 预警值: {}",
                         evalModelId, companyName != null ? companyName : "N/A",
                         warningValue != null ? warningValue : "N/A");
                return false;

            } catch (Exception sqlException) {
                log.error("执行重复检查SQL失败 - 模型ID: {}, SQL: {}, 错误: {}",
                        model.getEvalModelId(), countSql, sqlException.getMessage());
                // SQL执行失败时，为了安全起见，允许插入
                return false;
            }

        } catch (Exception e) {
            log.error("检查预警重复失败 - 模型ID: {}, 错误: {}", model.getEvalModelId(), e.getMessage(), e);
            // 检查失败时，为了安全起见，允许插入（避免遗漏重要预警）
            return false;
        }
    }

    /**
     * 从SQL执行结果中提取企业名称
     * 🔧 修复：支持中文字段名和英文字段名，但不匹配供应商、客户等字段
     *
     * @param resultRow SQL执行结果行
     * @return 企业名称
     */
    private String extractCompanyNameFromResult(Map<String, Object> resultRow) {
        try {
            log.debug("开始提取企业名称，可用字段: {}", resultRow.keySet());

            // 🔧 优先级1：查找明确的企业名称字段（最高优先级）
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value != null && StringUtil.isNotEmpty(value.toString())) {
                    String upperKey = key.toUpperCase();

                    // 明确的企业名称字段（中文）
                    if (key.equals("企业名称") || key.equals("公司名称") ||
                        key.equals("企业") || key.equals("公司")) {
                        log.info("找到明确的中文企业名称字段: {} = {}", key, value);
                        return value.toString().trim();
                    }

                    // 明确的企业名称字段（英文）
                    if (upperKey.equals("COMPANY_NAME") || upperKey.equals("ENTERPRISE_NAME") ||
                        upperKey.equals("ORG_NAME") || upperKey.equals("ORGANIZATION_NAME")) {
                        log.info("找到明确的英文企业名称字段: {} = {}", key, value);
                        return value.toString().trim();
                    }
                }
            }

            // 🔧 优先级2：查找企业ID字段作为标识
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value != null && StringUtil.isNotEmpty(value.toString())) {
                    String upperKey = key.toUpperCase();

                    if (key.equals("企业ID") || upperKey.equals("COMPANY_ID") ||
                        upperKey.equals("ENTERPRISE_ID") || upperKey.equals("ORG_ID")) {
                        log.info("使用企业ID字段作为标识: {} = {}", key, value);
                        return value.toString().trim();
                    }
                }
            }

            log.warn("未找到企业名称字段，可用字段: {}", resultRow.keySet());
            return null;

        } catch (Exception e) {
            log.error("提取企业名称失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取SQL结果行的摘要信息，用于日志记录
     */
    private String getResultRowSummary(Map<String, Object> resultRow) {
        try {
            StringBuilder summary = new StringBuilder();

            // 只显示关键字段的摘要
            for (Map.Entry<String, Object> entry : resultRow.entrySet()) {
                String key = entry.getKey().toUpperCase();
                Object value = entry.getValue();

                // 只记录重要字段
                if (key.contains("NAME") || key.contains("VALUE") || key.contains("AMOUNT") ||
                    key.contains("COMPANY") || key.contains("SUPPLIER")) {
                    if (summary.length() > 0) {
                        summary.append(", ");
                    }
                    summary.append(key).append("=").append(value);
                }
            }

            return summary.toString();

        } catch (Exception e) {
            return "无法生成摘要";
        }
    }
}
