package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.PredictiveAnalysis;
import com.global.treasurer.mapper.PredictiveAnalysisMapper;
import com.global.treasurer.service.IPredictiveAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 预测分析Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@Service
public class PredictiveAnalysisServiceImpl extends ServiceImpl<PredictiveAnalysisMapper, PredictiveAnalysis>
        implements IPredictiveAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(PredictiveAnalysisServiceImpl.class);

    @Override
    public IPage<PredictiveAnalysis> selectPage(IPage<PredictiveAnalysis> page, Map<String, Object> params) {
        log.info("========== 预测分析查询Service ==========");
        log.info("接收到的分页参数 - 当前页: {}, 每页大小: {}", page.getCurrent(), page.getSize());
        log.info("接收到的查询参数: {}", params);

        // 提取查询参数
        String analysisName = params.get("analysisName") != null ? params.get("analysisName").toString() : null;
        String analysisType = params.get("analysisType") != null ? params.get("analysisType").toString() : null;
        String analysisStatus = params.get("analysisStatus") != null ? params.get("analysisStatus").toString() : null;
        Long orgId = params.get("orgId") != null ? Long.parseLong(params.get("orgId").toString()) : null;

        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage((int)page.getCurrent(), (int)page.getSize());

        // 调用自定义的条件查询方法
        List<PredictiveAnalysis> list = baseMapper.selectByCondition(
                analysisName, analysisType, analysisStatus, orgId);

        log.info("查询结果记录数: {}", list.size());

        // 使用 PageInfo 获取分页信息
        PageInfo<PredictiveAnalysis> pageInfo = new PageInfo<>(list);

        // 构建返回的 IPage 对象
        Page<PredictiveAnalysis> resultPage = new Page<>(page.getCurrent(), page.getSize(), pageInfo.getTotal());
        resultPage.setRecords(list);
        resultPage.setPages(pageInfo.getPages());

        log.info("返回的分页信息 - 当前页: {}, 每页大小: {}, 总记录数: {}, 总页数: {}",
                resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), resultPage.getPages());

        return resultPage;
    }

    @Override
    public boolean executeAnalysis(Long analysisId, Long executeUser) {
        try {
            PredictiveAnalysis analysis = this.getById(analysisId);
            if (analysis == null) {
                log.error("预测分析不存在，analysisId: {}", analysisId);
                return false;
            }

            // 检查分析状态
            if ("RUNNING".equals(analysis.getAnalysisStatus())) {
                log.warn("预测分析正在运行中，analysisId: {}", analysisId);
                return false;
            }

            // 更新分析状态为运行中
            analysis.setAnalysisStatus("RUNNING");
            analysis.setUpdateBy(executeUser);
            analysis.setUpdateTime(LocalDateTime.now());
            boolean updated = this.updateById(analysis);

            if (!updated) {
                log.error("更新预测分析状态失败，analysisId: {}", analysisId);
                return false;
            }

            // 模拟执行预测分析
            log.info("开始执行预测分析，analysisId: {}, analysisName: {}", analysisId, analysis.getAnalysisName());

            // 模拟预测结果
            String predictionResult = "{\"predictedValue\": 1500000, \"trend\": \"UP\", \"confidence\": 0.95}";
            analysis.setPredictionResult(predictionResult);
            analysis.setAnalysisStatus("COMPLETED");
            analysis.setAccuracy(BigDecimal.valueOf(92.5));
            analysis.setAnalysisDate(LocalDate.now());
            analysis.setUpdateTime(LocalDateTime.now());

            updated = this.updateById(analysis);
            if (updated) {
                log.info("预测分析执行成功，analysisId: {}", analysisId);
            }

            return updated;
        } catch (Exception e) {
            log.error("执行预测分析失败，analysisId: {}", analysisId, e);

            // 更新分析状态为失败
            try {
                PredictiveAnalysis analysis = this.getById(analysisId);
                if (analysis != null) {
                    analysis.setAnalysisStatus("FAILED");
                    analysis.setErrorMessage(e.getMessage());
                    analysis.setUpdateTime(LocalDateTime.now());
                    this.updateById(analysis);
                }
            } catch (Exception ex) {
                log.error("更新预测分析失败状态异常", ex);
            }

            return false;
        }
    }

    @Override
    public boolean cancelAnalysis(Long analysisId, Long cancelUser) {
        try {
            PredictiveAnalysis analysis = this.getById(analysisId);
            if (analysis == null) {
                log.error("预测分析不存在，analysisId: {}", analysisId);
                return false;
            }

            // 检查分析状态
            if ("COMPLETED".equals(analysis.getAnalysisStatus())) {
                log.warn("预测分析已完成，无法取消，analysisId: {}", analysisId);
                return false;
            }

            analysis.setAnalysisStatus("CANCELLED");
            analysis.setUpdateBy(cancelUser);
            analysis.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(analysis);
            if (updated) {
                log.info("预测分析取消成功，analysisId: {}", analysisId);
            }

            return updated;
        } catch (Exception e) {
            log.error("取消预测分析失败，analysisId: {}", analysisId, e);
            return false;
        }
    }

    @Override
    public boolean retryAnalysis(Long analysisId, Long retryUser) {
        try {
            PredictiveAnalysis analysis = this.getById(analysisId);
            if (analysis == null) {
                log.error("预测分析不存在，analysisId: {}", analysisId);
                return false;
            }

            // 检查分析状态
            if (!"FAILED".equals(analysis.getAnalysisStatus())) {
                log.warn("预测分析状态不为失败，无法重试，analysisId: {}, analysisStatus: {}",
                         analysisId, analysis.getAnalysisStatus());
                return false;
            }

            // 重置分析状态
            analysis.setAnalysisStatus("PENDING");
            analysis.setErrorMessage(null);
            analysis.setPredictionResult(null);
            analysis.setUpdateBy(retryUser);
            analysis.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(analysis);
            if (updated) {
                log.info("预测分析重置成功，可以重新执行，analysisId: {}", analysisId);
                // 重新执行分析
                return executeAnalysis(analysisId, retryUser);
            }

            return false;
        } catch (Exception e) {
            log.error("重试预测分析失败，analysisId: {}", analysisId, e);
            return false;
        }
    }

    @Override
    public boolean updateActualResult(Long analysisId, String actualResult, Long updateUser) {
        try {
            PredictiveAnalysis analysis = this.getById(analysisId);
            if (analysis == null) {
                log.error("预测分析不存在，analysisId: {}", analysisId);
                return false;
            }

            analysis.setActualResult(actualResult);
            analysis.setUpdateBy(updateUser);
            analysis.setUpdateTime(LocalDateTime.now());

            boolean updated = this.updateById(analysis);
            if (updated) {
                log.info("更新预测分析实际结果成功，analysisId: {}", analysisId);
            }

            return updated;
        } catch (Exception e) {
            log.error("更新预测分析实际结果失败，analysisId: {}", analysisId, e);
            return false;
        }
    }

    @Override
    public boolean calculateAccuracy(Long analysisId, Long calculateUser) {
        try {
            PredictiveAnalysis analysis = this.getById(analysisId);
            if (analysis == null) {
                log.error("预测分析不存在，analysisId: {}", analysisId);
                return false;
            }

            // 检查是否已有预测结果和实际结果
            if (analysis.getPredictionResult() == null || analysis.getActualResult() == null) {
                log.warn("预测结果或实际结果为空，无法计算准确率，analysisId: {}", analysisId);
                return false;
            }

            // 模拟计算准确率
            BigDecimal accuracy = BigDecimal.valueOf(88.7);

            analysis.setAccuracy(accuracy);
            analysis.setUpdateBy(calculateUser);
            analysis.setUpdateTime(LocalDateTime.now());

            // 生成偏差分析结果
            String deviationAnalysis = "{\"meanAbsoluteError\": 0.12, \"meanSquaredError\": 0.015}";
            analysis.setDeviationAnalysis(deviationAnalysis);

            boolean updated = this.updateById(analysis);
            if (updated) {
                log.info("计算预测分析准确率成功，analysisId: {}, accuracy: {}%", analysisId, accuracy);
            }

            return updated;
        } catch (Exception e) {
            log.error("计算预测分析准确率失败，analysisId: {}", analysisId, e);
            return false;
        }
    }

    /**
     * 将驼峰字段名转换为数据库列名（下划线大写格式）
     * 例如: createTime -> CREATE_TIME, analysisId -> ANALYSIS_ID
     */
    private String convertToDbColumn(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return "CREATE_TIME"; // 默认排序字段
        }

        // 如果已经包含下划线，可能是大写字段名，直接返回
        if (fieldName.contains("_")) {
            return fieldName.toUpperCase();
        }

        // 驼峰转下划线大写
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < fieldName.length(); i++) {
            char c = fieldName.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append('_').append(c);
            } else {
                result.append(Character.toUpperCase(c));
            }
        }
        return result.toString();
    }
}
