package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingRiskDTO;
import com.global.treasurer.dto.FinancingRiskQueryDTO;
import com.global.treasurer.entity.TblFinancingMonitoring;
import com.global.treasurer.mapper.FinancingRiskMapper;
import com.global.treasurer.service.FinancingRiskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 融资风险监控服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Service
public class FinancingRiskServiceImpl implements FinancingRiskService {
    private static final Logger log = LoggerFactory.getLogger(FinancingRiskServiceImpl.class);

    @Resource
    private FinancingRiskMapper financingRiskMapper;

    @Override
    public PageInfo<TblFinancingMonitoring> getRiskList(FinancingRiskQueryDTO queryDTO) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("alertType", queryDTO.getAlertType());
            params.put("alertLevel", queryDTO.getAlertLevel());
            params.put("alertStatus", queryDTO.getAlertStatus());
            params.put("companyId", queryDTO.getCompanyId());
            params.put("relatedFinancingId", queryDTO.getRelatedFinancingId());
            params.put("alertStartDate", queryDTO.getAlertStartDate());
            params.put("alertEndDate", queryDTO.getAlertEndDate());
            params.put("handlerId", queryDTO.getHandlerId());
            params.put("keyword", queryDTO.getKeyword());

            // 分页查询
            PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
            List<TblFinancingMonitoring> list = financingRiskMapper.selectRiskList(params);
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("查询融资风险列表失败", e);
            throw new RuntimeException("查询融资风险列表失败: " + e.getMessage());
        }
    }

    @Override
    public TblFinancingMonitoring getRiskById(Long monitoringId) {
        try {
            return financingRiskMapper.selectRiskById(monitoringId);
        } catch (Exception e) {
            log.error("查询融资风险详情失败, monitoringId={}", monitoringId, e);
            throw new RuntimeException("查询融资风险详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingMonitoring createRisk(FinancingRiskDTO dto) {
        try {
            TblFinancingMonitoring monitoring = new TblFinancingMonitoring();
            BeanUtils.copyProperties(dto, monitoring);

            // 设置默认值
            if (monitoring.getAlertStatus() == null) {
                monitoring.setAlertStatus("PENDING");
            }
            if (monitoring.getAlertDate() == null) {
                monitoring.setAlertDate(new Date());
            }
            monitoring.setCreatedTime(new Date());
            monitoring.setUpdatedTime(new Date());
            monitoring.setDeleteFlag(0);

            financingRiskMapper.insert(monitoring);
            log.info("创建融资风险记录成功, monitoringId={}", monitoring.getMonitoringId());
            return monitoring;
        } catch (Exception e) {
            log.error("创建融资风险记录失败", e);
            throw new RuntimeException("创建融资风险记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingMonitoring updateRisk(FinancingRiskDTO dto) {
        try {
            if (dto.getMonitoringId() == null) {
                throw new IllegalArgumentException("监控ID不能为空");
            }

            TblFinancingMonitoring monitoring = financingRiskMapper.selectRiskById(dto.getMonitoringId());
            if (monitoring == null) {
                throw new IllegalArgumentException("风险记录不存在");
            }

            // 更新字段
            if (StringUtils.hasText(dto.getAlertType())) {
                monitoring.setAlertType(dto.getAlertType());
            }
            if (StringUtils.hasText(dto.getAlertLevel())) {
                monitoring.setAlertLevel(dto.getAlertLevel());
            }
            if (StringUtils.hasText(dto.getAlertMessage())) {
                monitoring.setAlertMessage(dto.getAlertMessage());
            }
            if (dto.getRelatedFinancingId() != null) {
                monitoring.setRelatedFinancingId(dto.getRelatedFinancingId());
            }
            if (StringUtils.hasText(dto.getAlertStatus())) {
                monitoring.setAlertStatus(dto.getAlertStatus());
            }
            if (dto.getAlertDate() != null) {
                monitoring.setAlertDate(dto.getAlertDate());
            }
            if (dto.getHandlerId() != null) {
                monitoring.setHandlerId(dto.getHandlerId());
            }
            if (StringUtils.hasText(dto.getHandlerName())) {
                monitoring.setHandlerName(dto.getHandlerName());
            }
            if (dto.getHandleDate() != null) {
                monitoring.setHandleDate(dto.getHandleDate());
            }
            if (StringUtils.hasText(dto.getHandleOpinion())) {
                monitoring.setHandleOpinion(dto.getHandleOpinion());
            }
            if (dto.getCompanyId() != null) {
                monitoring.setCompanyId(dto.getCompanyId());
            }
            if (StringUtils.hasText(dto.getCompanyName())) {
                monitoring.setCompanyName(dto.getCompanyName());
            }
            if (StringUtils.hasText(dto.getRemark())) {
                monitoring.setRemark(dto.getRemark());
            }

            monitoring.setUpdatedTime(new Date());
            financingRiskMapper.updateById(monitoring);

            log.info("更新融资风险记录成功, monitoringId={}", monitoring.getMonitoringId());
            return monitoring;
        } catch (Exception e) {
            log.error("更新融资风险记录失败", e);
            throw new RuntimeException("更新融资风险记录失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> assessRisk(Long relatedFinancingId) {
        try {
            // 模拟风险评估逻辑
            Map<String, Object> assessment = new HashMap<>();
            assessment.put("financingId", relatedFinancingId);
            assessment.put("riskScore", 75);
            assessment.put("riskLevel", "MEDIUM");
            assessment.put("riskFactors", Arrays.asList("利率波动", "期限匹配", "担保方式"));
            assessment.put("suggestions", Arrays.asList("关注利率变化", "优化期限结构", "完善担保措施"));
            assessment.put("assessmentDate", new Date());

            log.info("风险评估完成, financingId={}", relatedFinancingId);
            return assessment;
        } catch (Exception e) {
            log.error("风险评估失败, financingId={}", relatedFinancingId, e);
            throw new RuntimeException("风险评估失败: " + e.getMessage());
        }
    }

    @Override
    public List<TblFinancingMonitoring> alertRisk(Long relatedFinancingId) {
        try {
            // 模拟风险预警逻辑
            List<TblFinancingMonitoring> alerts = new ArrayList<>();

            // 模拟生成预警
            TblFinancingMonitoring alert1 = new TblFinancingMonitoring();
            alert1.setAlertType("INTEREST_RATE");
            alert1.setAlertLevel("HIGH");
            alert1.setAlertMessage("市场利率上升，浮动利率贷款成本可能增加");
            alert1.setRelatedFinancingId(relatedFinancingId);
            alert1.setAlertStatus("PENDING");
            alert1.setAlertDate(new Date());
            alerts.add(alert1);

            TblFinancingMonitoring alert2 = new TblFinancingMonitoring();
            alert2.setAlertType("EXPIRY");
            alert2.setAlertLevel("MEDIUM");
            alert2.setAlertMessage("贷款即将到期，需提前安排还款资金");
            alert2.setRelatedFinancingId(relatedFinancingId);
            alert2.setAlertStatus("PENDING");
            alert2.setAlertDate(new Date());
            alerts.add(alert2);

            log.info("风险预警完成, financingId={}, 预警数量={}", relatedFinancingId, alerts.size());
            return alerts;
        } catch (Exception e) {
            log.error("风险预警失败, financingId={}", relatedFinancingId, e);
            throw new RuntimeException("风险预警失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRiskTrend(Long companyId, Integer days) {
        try {
            // 模拟风险趋势统计
            Map<String, Object> trend = new HashMap<>();
            List<Map<String, Object>> dataList = new ArrayList<>();

            // 生成最近N天的数据
            Calendar calendar = Calendar.getInstance();
            for (int i = days - 1; i >= 0; i--) {
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, -i);

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", calendar.getTime());
                dayData.put("highRisk", Math.random() * 10);
                dayData.put("mediumRisk", Math.random() * 20);
                dayData.put("lowRisk", Math.random() * 30);
                dataList.add(dayData);
            }

            trend.put("companyId", companyId);
            trend.put("days", days);
            trend.put("trendData", dataList);
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalHighRisk", (int) (Math.random() * 50));
            summary.put("totalMediumRisk", (int) (Math.random() * 100));
            summary.put("totalLowRisk", (int) (Math.random() * 150));
            trend.put("summary", summary);

            log.info("获取风险趋势统计成功, companyId={}, days={}", companyId, days);
            return trend;
        } catch (Exception e) {
            log.error("获取风险趋势统计失败, companyId={}, days={}", companyId, days, e);
            throw new RuntimeException("获取风险趋势统计失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean processRisk(Long monitoringId, String handlerName, String handleOpinion, String alertStatus) {
        try {
            TblFinancingMonitoring monitoring = financingRiskMapper.selectRiskById(monitoringId);
            if (monitoring == null) {
                throw new IllegalArgumentException("风险记录不存在");
            }

            // 支持 HANDLED（已处理）和 CLOSED（已关闭）两种状态
            String status = (alertStatus != null && !alertStatus.isEmpty()) ? alertStatus : "HANDLED";
            monitoring.setAlertStatus(status);
            monitoring.setHandlerName(handlerName);
            monitoring.setHandleDate(new Date());
            monitoring.setHandleOpinion(handleOpinion);
            monitoring.setUpdatedTime(new Date());

            int rows = financingRiskMapper.updateById(monitoring);
            log.info("处理融资风险成功, monitoringId={}, handlerName={}, alertStatus={}", monitoringId, handlerName, status);
            return rows > 0;
        } catch (Exception e) {
            log.error("处理融资风险失败, monitoringId={}", monitoringId, e);
            throw new RuntimeException("处理融资风险失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRisk(Long monitoringId) {
        try {
            financingRiskMapper.deleteById(monitoringId);
            log.info("删除融资风险记录成功, monitoringId={}", monitoringId);
        } catch (Exception e) {
            log.error("删除融资风险记录失败, monitoringId={}", monitoringId, e);
            throw new RuntimeException("删除融资风险记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRisks(List<Long> monitoringIds) {
        try {
            if (monitoringIds == null || monitoringIds.isEmpty()) {
                throw new IllegalArgumentException("监控ID列表不能为空");
            }
            financingRiskMapper.batchDeleteByIds(monitoringIds);
            log.info("批量删除融资风险记录成功, 数量={}", monitoringIds.size());
        } catch (Exception e) {
            log.error("批量删除融资风险记录失败", e);
            throw new RuntimeException("批量删除融资风险记录失败: " + e.getMessage());
        }
    }
}
