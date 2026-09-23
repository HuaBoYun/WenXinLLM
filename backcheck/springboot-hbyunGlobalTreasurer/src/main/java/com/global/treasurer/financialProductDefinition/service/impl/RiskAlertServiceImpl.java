package com.global.treasurer.financialProductDefinition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.TblBillRiskAlert;
import com.global.treasurer.financialProductDefinition.mapper.RiskAlertMapper;
import com.global.treasurer.financialProductDefinition.service.RiskAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风险预警Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class RiskAlertServiceImpl extends ServiceImpl<RiskAlertMapper, TblBillRiskAlert>
        implements RiskAlertService {

    private static final Logger log = LoggerFactory.getLogger(RiskAlertServiceImpl.class);

    @Override
    public IPage<TblBillRiskAlert> getAlertPage(Integer pageNo, Integer pageSize,
                                                  String alertType, String alertLevel,
                                                  String alertStatus, String productName,
                                                  Long orgId) {
        log.info("查询风险预警列表: pageNo={}, pageSize={}, alertType={}, alertLevel={}, alertStatus={}, productName={}, orgId={}",
                 pageNo, pageSize, alertType, alertLevel, alertStatus, productName, orgId);

        // 创建分页对象
        Page<TblBillRiskAlert> page = new Page<>(pageNo, pageSize);

        // 构建查询条件
        QueryWrapper<TblBillRiskAlert> queryWrapper = new QueryWrapper<>();

        if (alertType != null && !alertType.isEmpty()) {
            queryWrapper.eq("ALERT_TYPE", alertType);
        }
        if (alertLevel != null && !alertLevel.isEmpty()) {
            queryWrapper.eq("ALERT_LEVEL", alertLevel);
        }
        if (alertStatus != null && !alertStatus.isEmpty()) {
            queryWrapper.eq("ALERT_STATUS", alertStatus);
        }
        if (productName != null && !productName.isEmpty()) {
            queryWrapper.like("PRODUCT_NAME", productName);
        }
        if (orgId != null) {
            queryWrapper.eq("ORG_ID", orgId);
        }

        // 按预警时间倒序
        queryWrapper.orderByDesc("ALERT_TIME");

        // 执行分页查询
        IPage<TblBillRiskAlert> result = this.page(page, queryWrapper);

        log.info("查询结果: total={}, records={}", result.getTotal(), result.getRecords().size());

        return result;
    }

    @Override
    public Map<String, Object> getStatistics() {
        log.info("获取预警统计信息");

        Map<String, Object> statistics = new HashMap<>();

        try {
            // 按状态统计
            List<Map<String, Object>> statusStats = baseMapper.countByStatus();
            Map<String, Integer> statusMap = new HashMap<>();
            for (Map<String, Object> item : statusStats) {
                String status = (String) item.get("status");
                Long count = (Long) item.get("count");
                statusMap.put(status, count.intValue());
            }
            statistics.put("byStatus", statusMap);

            // 按类型统计
            List<Map<String, Object>> typeStats = baseMapper.countByType();
            Map<String, Integer> typeMap = new HashMap<>();
            for (Map<String, Object> item : typeStats) {
                String type = (String) item.get("alertType");
                Long count = (Long) item.get("count");
                typeMap.put(type, count.intValue());
            }
            statistics.put("byType", typeMap);

            // 按级别统计
            List<Map<String, Object>> levelStats = baseMapper.countByLevel();
            Map<String, Integer> levelMap = new HashMap<>();
            for (Map<String, Object> item : levelStats) {
                String level = (String) item.get("alertLevel");
                Long count = (Long) item.get("count");
                levelMap.put(level, count.intValue());
            }
            statistics.put("byLevel", levelMap);

            // 总数统计
            Integer totalCount = this.count();
            statistics.put("totalCount", totalCount);

        } catch (Exception e) {
            log.error("获取预警统计信息失败", e);
            throw new RuntimeException("获取预警统计信息失败: " + e.getMessage(), e);
        }

        return statistics;
    }
}
