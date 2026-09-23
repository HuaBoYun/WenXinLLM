package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingMonitoringDTO;
import com.global.treasurer.dto.FinancingMonitoringQueryDTO;
import com.global.treasurer.entity.TblFinancingMonitoring;
import com.global.treasurer.mapper.FinancingMonitoringMapper;
import com.global.treasurer.service.FinancingMonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 融资监控服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@Service
public class FinancingMonitoringServiceImpl implements FinancingMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(FinancingMonitoringServiceImpl.class);

    @Resource
    private FinancingMonitoringMapper financingMonitoringMapper;

    @Override
    public PageInfo<TblFinancingMonitoring> getFinancingMonitoringList(FinancingMonitoringQueryDTO queryDTO) {
        try {
            int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
            int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;
            PageHelper.startPage(pageNum, pageSize);
            List<TblFinancingMonitoring> list = financingMonitoringMapper.selectByQueryDTO(queryDTO);
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询融资监控列表失败", e);
            throw new RuntimeException("查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getMonitoringOverview() {
        try {
            return financingMonitoringMapper.selectMonitoringOverview();
        } catch (Exception e) {
            log.error("获取监控概览失败", e);
            throw new RuntimeException("获取概览失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMonitoringData(FinancingMonitoringDTO dto) {
        try {
            TblFinancingMonitoring entity = financingMonitoringMapper.selectById(dto.getMonitoringId());
            if (entity == null) {
                throw new RuntimeException("监控记录不存在");
            }

            // 更新字段
            if (dto.getAlertType() != null) {
                entity.setAlertType(dto.getAlertType());
            }
            if (dto.getAlertLevel() != null) {
                entity.setAlertLevel(dto.getAlertLevel());
            }
            if (dto.getAlertMessage() != null) {
                entity.setAlertMessage(dto.getAlertMessage());
            }
            if (dto.getAlertStatus() != null) {
                entity.setAlertStatus(dto.getAlertStatus());
            }
            if (dto.getRemark() != null) {
                entity.setRemark(dto.getRemark());
            }

            entity.setUpdatedTime(new Date());
            financingMonitoringMapper.updateById(entity);
        } catch (Exception e) {
            log.error("更新监控数据失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processAlert(Long monitoringId, String handleOpinion, Long handlerId, String handlerName) {
        try {
            TblFinancingMonitoring entity = financingMonitoringMapper.selectById(monitoringId);
            if (entity == null) {
                throw new RuntimeException("监控记录不存在");
            }

            entity.setAlertStatus("HANDLED");
            entity.setHandlerId(handlerId);
            entity.setHandlerName(handlerName);
            entity.setHandleDate(new Date());
            entity.setHandleOpinion(handleOpinion);
            entity.setUpdatedTime(new Date());

            financingMonitoringMapper.updateById(entity);
        } catch (Exception e) {
            log.error("处理预警失败", e);
            throw new RuntimeException("处理失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> exportMonitoringReport(FinancingMonitoringQueryDTO queryDTO) {
        try {
            // 查询所有符合条件的数据（不分页）
            queryDTO.setPageNum(null);
            queryDTO.setPageSize(null);
            List<TblFinancingMonitoring> list = financingMonitoringMapper.selectByQueryDTO(queryDTO);

            // 转换为导出格式
            List<Map<String, Object>> reportData = new ArrayList<>();
            for (TblFinancingMonitoring item : list) {
                Map<String, Object> row = new HashMap<>();
                row.put("预警类型", item.getAlertType());
                row.put("预警级别", item.getAlertLevel());
                row.put("预警内容", item.getAlertMessage());
                row.put("预警状态", item.getAlertStatus());
                row.put("预警日期", item.getAlertDate());
                row.put("公司名称", item.getCompanyName());
                row.put("处理人", item.getHandlerName());
                row.put("处理日期", item.getHandleDate());
                row.put("处理意见", item.getHandleOpinion());
                row.put("备注", item.getRemark());
                reportData.add(row);
            }
            return reportData;
        } catch (Exception e) {
            log.error("导出监控报告失败", e);
            throw new RuntimeException("导出失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getDashboardData() {
        try {
            return financingMonitoringMapper.selectDashboardData();
        } catch (Exception e) {
            log.error("获取仪表盘数据失败", e);
            throw new RuntimeException("获取失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getTrendsAnalysis(String period) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> trendsData = financingMonitoringMapper.selectTrendsData(period);

            // 按日期分组统计
            Map<String, Integer> dailyCount = new LinkedHashMap<>();
            Map<String, Integer> highAlertCount = new LinkedHashMap<>();
            Map<String, Integer> mediumAlertCount = new LinkedHashMap<>();
            Map<String, Integer> lowAlertCount = new LinkedHashMap<>();

            for (Map<String, Object> item : trendsData) {
                String dateKey = item.get("DATE_KEY").toString();
                String alertLevel = (String) item.get("ALERT_LEVEL");
                Integer count = Integer.parseInt(item.get("COUNT").toString());

                dailyCount.put(dateKey, dailyCount.getOrDefault(dateKey, 0) + count);

                if ("HIGH".equals(alertLevel)) {
                    highAlertCount.put(dateKey, highAlertCount.getOrDefault(dateKey, 0) + count);
                } else if ("MEDIUM".equals(alertLevel)) {
                    mediumAlertCount.put(dateKey, mediumAlertCount.getOrDefault(dateKey, 0) + count);
                } else if ("LOW".equals(alertLevel)) {
                    lowAlertCount.put(dateKey, lowAlertCount.getOrDefault(dateKey, 0) + count);
                }
            }

            result.put("dailyCount", dailyCount);
            result.put("highAlertCount", highAlertCount);
            result.put("mediumAlertCount", mediumAlertCount);
            result.put("lowAlertCount", lowAlertCount);
            result.put("trendsData", trendsData);

            return result;
        } catch (Exception e) {
            log.error("获取趋势分析失败", e);
            throw new RuntimeException("获取失败: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refreshMonitoringData() {
        try {
            List<TblFinancingMonitoring> needRefreshList = financingMonitoringMapper.selectNeedRefresh();

            for (TblFinancingMonitoring monitoring : needRefreshList) {
                // 这里可以根据实际业务逻辑刷新监控数据
                // 例如：重新计算预警级别、更新预警状态等
                monitoring.setUpdatedTime(new Date());
                financingMonitoringMapper.updateById(monitoring);
            }

            log.info("刷新监控数据完成，共刷新{}条记录", needRefreshList.size());
        } catch (Exception e) {
            log.error("刷新监控数据失败", e);
            throw new RuntimeException("刷新失败: " + e.getMessage(), e);
        }
    }

    @Override
    public TblFinancingMonitoring getFinancingMonitoringById(Long monitoringId) {
        return financingMonitoringMapper.selectById(monitoringId);
    }

    @Override
    public List<TblFinancingMonitoring> getAlertList(String alertLevel) {
        return financingMonitoringMapper.selectByAlertLevel(alertLevel);
    }

    @Override
    public Map<String, Object> getMonitoringStatistics() {
        try {
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> statistics = financingMonitoringMapper.selectMonitoringStatistics();
            result.put("statistics", statistics);
            return result;
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            throw new RuntimeException("获取失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getFinancingTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = financingMonitoringMapper.selectFinancingTypeDistribution();
            if (distribution == null || distribution.isEmpty()) {
                // 返回默认数据，避免前端图表显示异常
                List<Map<String, Object>> defaultData = new ArrayList<>();
                Map<String, Object> item = new HashMap<>();
                item.put("code", "UNKNOWN");
                item.put("name", "暂无数据");
                item.put("value", 0);
                defaultData.add(item);
                return defaultData;
            }
            return distribution;
        } catch (Exception e) {
            log.error("获取融资类型分布数据失败", e);
            throw new RuntimeException("获取失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getRiskLevelDistribution() {
        try {
            List<Map<String, Object>> distribution = financingMonitoringMapper.selectRiskLevelDistribution();
            if (distribution == null || distribution.isEmpty()) {
                // 返回默认数据，避免前端图表显示异常
                List<Map<String, Object>> defaultData = new ArrayList<>();
                Map<String, Object> item = new HashMap<>();
                item.put("code", "UNKNOWN");
                item.put("name", "暂无数据");
                item.put("value", 0);
                defaultData.add(item);
                return defaultData;
            }
            return distribution;
        } catch (Exception e) {
            log.error("获取风险等级分布数据失败", e);
            throw new RuntimeException("获取失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getComplianceDistribution() {
        try {
            List<Map<String, Object>> distribution = financingMonitoringMapper.selectComplianceDistribution();
            if (distribution == null || distribution.isEmpty()) {
                // 返回默认数据，避免前端图表显示异常
                List<Map<String, Object>> defaultData = new ArrayList<>();
                Map<String, Object> item = new HashMap<>();
                item.put("code", "UNKNOWN");
                item.put("name", "暂无数据");
                item.put("value", 0);
                defaultData.add(item);
                return defaultData;
            }
            return distribution;
        } catch (Exception e) {
            log.error("获取合规状态分布数据失败", e);
            throw new RuntimeException("获取失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getFinancingScaleTrend(Integer months) {
        try {
            if (months == null || months <= 0) {
                months = 6; // 默认查询6个月
            }
            List<Map<String, Object>> trendData = financingMonitoringMapper.selectFinancingScaleTrend(months);
            if (trendData == null || trendData.isEmpty()) {
                log.info("融资规模趋势数据为空，返回空列表");
                return new ArrayList<>();
            }
            return trendData;
        } catch (Exception e) {
            log.error("获取融资规模趋势数据失败", e);
            throw new RuntimeException("获取失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getFinancingStructureAnalysis() {
        try {
            List<Map<String, Object>> structureData = financingMonitoringMapper.selectFinancingStructure();
            if (structureData == null || structureData.isEmpty()) {
                log.info("融资结构数据为空，返回空列表");
                return new ArrayList<>();
            }
            return structureData;
        } catch (Exception e) {
            log.error("获取融资结构分析数据失败", e);
            throw new RuntimeException("获取失败: " + e.getMessage(), e);
        }
    }
}
