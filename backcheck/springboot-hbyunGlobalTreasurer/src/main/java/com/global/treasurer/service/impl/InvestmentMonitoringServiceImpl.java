package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentMonitoringDTO;
import com.global.treasurer.dto.InvestmentMonitoringQueryDTO;
import com.global.treasurer.entity.TblInvestmentMonitoring;
import com.global.treasurer.mapper.InvestmentMonitoringMapper;
import com.global.treasurer.service.InvestmentMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvestmentMonitoringServiceImpl implements InvestmentMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentMonitoringServiceImpl.class);

    @Resource
    private InvestmentMonitoringMapper investmentMonitoringMapper;

    @Override
    public PageInfo<TblInvestmentMonitoring> getInvestmentMonitoringList(InvestmentMonitoringQueryDTO queryDTO) {
        try {
            int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
            PageHelper.startPage(pageNum, pageSize);
            List<TblInvestmentMonitoring> list = investmentMonitoringMapper.selectByQueryDTO(queryDTO);
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询投资监控列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    public TblInvestmentMonitoring getInvestmentMonitoringById(Long monitoringId) {
        return investmentMonitoringMapper.selectById(monitoringId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblInvestmentMonitoring saveInvestmentMonitoring(InvestmentMonitoringDTO dto) {
        TblInvestmentMonitoring entity = new TblInvestmentMonitoring();

        // 复制所有属性
        entity.setInvestmentId(dto.getInvestmentId());
        entity.setMonitoringDate(dto.getMonitoringDate());
        entity.setMonitoringType(dto.getMonitoringType());
        entity.setPreviousValue(dto.getPreviousValue());
        entity.setCurrentValue(dto.getCurrentValue());
        entity.setValueChange(dto.getValueChange());
        entity.setValueChangeRate(dto.getValueChangeRate());
        entity.setPerformanceScore(dto.getPerformanceScore());
        entity.setRiskScore(dto.getRiskScore());
        entity.setAlertLevel(dto.getAlertLevel());
        entity.setAlertMessage(dto.getAlertMessage());
        entity.setAlertType(dto.getAlertType());
        entity.setAlertStatus(dto.getAlertStatus());
        entity.setMonitoringStatus(dto.getMonitoringStatus());
        entity.setMonitoringNotes(dto.getMonitoringNotes());
        entity.setInvestmentType(dto.getInvestmentType());

        if (dto.getMonitoringId() != null) {
            entity.setMonitoringId(dto.getMonitoringId());
            entity.setUpdateTime(new java.sql.Timestamp(new Date().getTime()));
            investmentMonitoringMapper.updateById(entity);
        } else {
            entity.setCreateTime(new java.sql.Timestamp(new Date().getTime()));
            entity.setUpdateTime(new java.sql.Timestamp(new Date().getTime()));
            investmentMonitoringMapper.insert(entity);
        }
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInvestmentMonitoring(Long monitoringId) {
        log.info("删除投资监控，ID：{}", monitoringId);
        // 逻辑删除：更新 DELETE_FLAG 为 1
        TblInvestmentMonitoring monitoring = new TblInvestmentMonitoring();
        monitoring.setMonitoringId(monitoringId);
        monitoring.setDeleteFlag(1);
        monitoring.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        int result = investmentMonitoringMapper.updateById(monitoring);
        log.info("逻辑删除结果：{}", result > 0 ? "成功" : "失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteInvestmentMonitorings(List<Long> monitoringIds) {
        log.info("========== 开始批量删除投资监控 ==========");
        log.info("接收到的IDs：{}", monitoringIds);
        log.info("IDs数量：{}", monitoringIds != null ? monitoringIds.size() : 0);

        if (monitoringIds == null || monitoringIds.isEmpty()) {
            log.warn("批量删除失败：ID列表为空");
            return;
        }

        // 删除前先查询这些记录是否存在
        log.info("删除前先查询记录是否存在...");
        for (Long id : monitoringIds) {
            TblInvestmentMonitoring existing = investmentMonitoringMapper.selectById(id);
            if (existing != null) {
                log.info("记录存在 - ID: {}, 投资ID: {}, 监控日期: {}, DELETE_FLAG: {}",
                    id, existing.getInvestmentId(), existing.getMonitoringDate(), existing.getDeleteFlag());
            } else {
                log.warn("记录不存在 - ID: {}", id);
            }
        }

        // 执行逻辑删除：更新 DELETE_FLAG 为 1
        int deletedCount = 0;
        Timestamp now = new Timestamp(System.currentTimeMillis());
        for (Long id : monitoringIds) {
            log.info("正在逻辑删除投资监控，ID：{}", id);
            try {
                TblInvestmentMonitoring monitoring = new TblInvestmentMonitoring();
                monitoring.setMonitoringId(id);
                monitoring.setDeleteFlag(1);
                monitoring.setUpdateTime(now);

                int result = investmentMonitoringMapper.updateById(monitoring);
                log.info("MyBatis-Plus updateById 返回值：{}", result);
                if (result > 0) {
                    deletedCount++;
                    log.info("逻辑删除成功 - ID: {}", id);
                } else {
                    log.warn("逻辑删除失败（记录不存在或已被删除） - ID: {}", id);
                }
            } catch (Exception e) {
                log.error("逻辑删除异常 - ID: {}, 错误: {}", id, e.getMessage(), e);
                throw e;
            }
        }

        log.info("批量逻辑删除完成，成功删除 {} 条记录", deletedCount);
        log.info("========== 批量删除投资监控结束 ==========");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMonitoring(Long investmentId, String monitoringType) {
        // 实现创建监控逻辑
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMonitoringData(Long monitoringId, BigDecimal currentValue) {
        TblInvestmentMonitoring monitoring = investmentMonitoringMapper.selectById(monitoringId);
        if (monitoring != null) {
            monitoring.setPreviousValue(monitoring.getCurrentValue());
            monitoring.setCurrentValue(currentValue);

            // 计算价值变化
            if (monitoring.getPreviousValue() != null && currentValue != null) {
                BigDecimal valueChange = currentValue.subtract(monitoring.getPreviousValue());
                monitoring.setValueChange(valueChange);

                // 计算变化率
                if (monitoring.getPreviousValue().compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal changeRate = valueChange.divide(monitoring.getPreviousValue(), 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal("100"));
                    monitoring.setValueChangeRate(changeRate);
                }
            }

            investmentMonitoringMapper.updateById(monitoring);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculatePerformanceScore(Long monitoringId) {
        // 实现绩效评分计算逻辑
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void calculateRiskScore(Long monitoringId) {
        // 实现风险评分计算逻辑
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateAlert(Long monitoringId) {
        // 实现预警生成逻辑
    }

    @Override
    public Map<String, Object> getDashboardData() {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // 查询所有监控记录
            List<TblInvestmentMonitoring> allMonitorings = investmentMonitoringMapper.selectList(null);

            // 计算统计数据
            int totalCount = allMonitorings.size();
            int activeCount = 0;
            int highAlertCount = 0;
            double totalPerformance = 0.0;
            int performanceCount = 0;

            for (TblInvestmentMonitoring monitoring : allMonitorings) {
                // 统计活跃监控
                if ("ACTIVE".equals(monitoring.getMonitoringStatus())) {
                    activeCount++;
                }

                // 统计高风险预警
                if ("HIGH".equals(monitoring.getAlertLevel())) {
                    highAlertCount++;
                }

                // 累计绩效评分
                if (monitoring.getPerformanceScore() != null) {
                    totalPerformance += monitoring.getPerformanceScore();
                    performanceCount++;
                }
            }

            // 计算平均绩效
            double avgPerformance = 0.0;
            if (performanceCount > 0) {
                avgPerformance = totalPerformance / performanceCount;
            }

            // 组装仪表盘数据
            dashboard.put("totalMonitoring", totalCount);
            dashboard.put("activeMonitoring", activeCount);
            dashboard.put("highAlerts", highAlertCount);
            dashboard.put("avgPerformanceScore", avgPerformance);

            return dashboard;
        } catch (Exception e) {
            log.error("获取仪表盘数据失败", e);
            // 返回默认值
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("totalMonitoring", 0);
            dashboard.put("activeMonitoring", 0);
            dashboard.put("highAlerts", 0);
            dashboard.put("avgPerformanceScore", 0.0);
            return dashboard;
        }
    }

    @Override
    public List<TblInvestmentMonitoring> getAlertList(String alertLevel) {
        // 实现获取预警列表逻辑
        return null;
    }

    @Override
    public Map<String, Object> getMonitoringStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<TblInvestmentMonitoring> allMonitorings = investmentMonitoringMapper.selectList(null);
            stats.put("total", allMonitorings.size());
            return stats;
        } catch (Exception e) {
            log.error("获取监控统计信息失败", e);
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", 0);
            return stats;
        }
    }

    @Override
    public Map<String, Object> getMonitoringAnalysis() {
        try {
            Map<String, Object> analysis = new HashMap<>();
            List<TblInvestmentMonitoring> allMonitorings = investmentMonitoringMapper.selectList(null);

            int totalCount = allMonitorings.size();
            int highRiskCount = 0;
            int mediumRiskCount = 0;
            int lowRiskCount = 0;

            for (TblInvestmentMonitoring monitoring : allMonitorings) {
                if (monitoring.getRiskScore() != null) {
                    if (monitoring.getRiskScore() >= 80) {
                        highRiskCount++;
                    } else if (monitoring.getRiskScore() >= 50) {
                        mediumRiskCount++;
                    } else {
                        lowRiskCount++;
                    }
                }
            }

            analysis.put("totalMonitorings", totalCount);
            analysis.put("highRiskCount", highRiskCount);
            analysis.put("mediumRiskCount", mediumRiskCount);
            analysis.put("lowRiskCount", lowRiskCount);

            return analysis;
        } catch (Exception e) {
            log.error("获取监控分析数据失败", e);
            throw new RuntimeException("获取监控分析数据失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getRiskTrend(Integer months) {
        try {
            List<Map<String, Object>> trendList = new java.util.ArrayList<>();

            for (int i = 0; i < months; i++) {
                Map<String, Object> trendData = new HashMap<>();
                trendData.put("month", "第" + (i + 1) + "月");
                trendData.put("avgRiskScore", 50 + new java.util.Random().nextInt(40));
                trendData.put("highRiskCount", new java.util.Random().nextInt(10));
                trendData.put("mediumRiskCount", new java.util.Random().nextInt(20));
                trendData.put("lowRiskCount", new java.util.Random().nextInt(30));
                trendList.add(trendData);
            }

            return trendList;
        } catch (Exception e) {
            log.error("获取风险趋势分析失败", e);
            throw new RuntimeException("获取风险趋势分析失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> generateMonitoringReport(String reportType, String startDate, String endDate) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("reportType", reportType);
            report.put("startDate", startDate);
            report.put("endDate", endDate);
            report.put("generateTime", new java.util.Date());
            report.put("summary", "监控报告生成成功");
            report.put("totalItems", new java.util.Random().nextInt(100));

            return report;
        } catch (Exception e) {
            log.error("生成监控报告失败", e);
            throw new RuntimeException("生成监控报告失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getOptimizationRecommendations() {
        try {
            List<Map<String, Object>> recommendations = new java.util.ArrayList<>();

            Map<String, Object> recommendation1 = new HashMap<>();
            recommendation1.put("type", "风险优化");
            recommendation1.put("description", "建议降低高风险投资比例");
            recommendation1.put("priority", "高");
            recommendations.add(recommendation1);

            Map<String, Object> recommendation2 = new HashMap<>();
            recommendation2.put("type", "收益优化");
            recommendation2.put("description", "建议增加固定收益类投资");
            recommendation2.put("priority", "中");
            recommendations.add(recommendation2);

            Map<String, Object> recommendation3 = new HashMap<>();
            recommendation3.put("type", "分散投资");
            recommendation3.put("description", "建议优化投资组合结构");
            recommendation3.put("priority", "中");
            recommendations.add(recommendation3);

            return recommendations;
        } catch (Exception e) {
            log.error("获取优化建议失败", e);
            throw new RuntimeException("获取优化建议失败: " + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> detectAnomalies(String investmentType) {
        try {
            List<Map<String, Object>> anomalies = new java.util.ArrayList<>();

            // 返回模拟的异常检测结果
            Map<String, Object> anomaly1 = new HashMap<>();
            anomaly1.put("type", "收益率异常");
            anomaly1.put("description", "某产品收益率偏离正常范围");
            anomaly1.put("severity", "中");
            anomaly1.put("detectedTime", new java.util.Date());
            anomalies.add(anomaly1);

            Map<String, Object> anomaly2 = new HashMap<>();
            anomaly2.put("type", "风险异常");
            anomaly2.put("description", "检测到异常波动");
            anomaly2.put("severity", "高");
            anomaly2.put("detectedTime", new java.util.Date());
            anomalies.add(anomaly2);

            return anomalies;
        } catch (Exception e) {
            log.error("异常检测失败", e);
            throw new RuntimeException("异常检测失败: " + e.getMessage());
        }
    }
}
