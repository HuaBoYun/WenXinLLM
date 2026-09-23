package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.entity.ArAgingSnapshotEntity;
import com.financial.sharing.oracle.entity.ArReceivableEntity;
import com.financial.sharing.oracle.mapper.ArAgingSnapshotMapper;
import com.financial.sharing.oracle.mapper.ArReceivableMapper;
import com.financial.sharing.service.ArAgingAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArAgingAnalysisQueryParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 账龄分析服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArAgingAnalysisServiceImpl implements ArAgingAnalysisService {

    @Resource
    private ArAgingSnapshotMapper agingSnapshotMapper;

    @Resource
    private ArReceivableMapper receivableMapper;

    @Override
    public MyJsonBean<PageResult> getAgingDetails(ArAgingAnalysisQueryParam param) {
        try {
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            List<ArAgingSnapshotEntity> list = agingSnapshotMapper.selectAgingDetails(param);
            PageInfo<ArAgingSnapshotEntity> pageInfo = new PageInfo<>(list);
            
            PageResult result = new PageResult();
            result.setTlist(pageInfo.getList());
            result.setTotalRecord((int) pageInfo.getTotal());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询账龄分析明细失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAgingRangeSummary(LocalDate analysisDate, Long tenantId) {
        try {
            List<Map<String, Object>> summary = agingSnapshotMapper.selectAgingRangeSummary(analysisDate, tenantId);
            return MyJsonBean.successData(summary);
        } catch (Exception e) {
            log.error("查询账龄区间统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAgingByCustomer(LocalDate analysisDate, Long tenantId) {
        try {
            List<Map<String, Object>> result = agingSnapshotMapper.selectAgingByCustomer(analysisDate, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("按客户统计账龄失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAgingByRiskLevel(LocalDate analysisDate, Long tenantId) {
        try {
            List<Map<String, Object>> result = agingSnapshotMapper.selectAgingByRiskLevel(analysisDate, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("按风险等级统计账龄失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean generateAgingSnapshot(LocalDate analysisDate, Long tenantId) {
        try {
            // 删除当天已有的快照
            agingSnapshotMapper.deleteByAnalysisDate(analysisDate, tenantId);

            // 查询所有待收应收单据
            ArAgingAnalysisQueryParam param = new ArAgingAnalysisQueryParam();
            param.setTenantId(tenantId);
            List<Map<String, Object>> receivables = agingSnapshotMapper.calculateAgingFromReceivables(analysisDate, tenantId);

            List<ArAgingSnapshotEntity> snapshots = new ArrayList<>();
            for (Map<String, Object> r : receivables) {
                ArAgingSnapshotEntity snapshot = new ArAgingSnapshotEntity();
                snapshot.setSnapshotId(UUID.randomUUID().toString().replace("-", ""));
                snapshot.setAnalysisDate(analysisDate);
                snapshot.setReceivableId((String) r.get("receivableId"));
                snapshot.setCustomerId((String) r.get("customerId"));
                snapshot.setReceivableAmount((BigDecimal) r.get("receivableAmount"));
                snapshot.setRemainingAmount((BigDecimal) r.get("remainingAmount"));
                
                // 计算账龄天数
                LocalDate dueDate = (LocalDate) r.get("dueDate");
                int agingDays = (int) ChronoUnit.DAYS.between(dueDate, analysisDate);
                snapshot.setAgingDays(agingDays);
                snapshot.setAgingRange(calculateAgingRange(agingDays));
                snapshot.setRiskLevel(calculateRiskLevel(agingDays));

                snapshot.setTenantId(tenantId);
                snapshot.setCreateTime(LocalDateTime.now());
                snapshots.add(snapshot);
            }
            if (!snapshots.isEmpty()) {
                agingSnapshotMapper.batchInsert(snapshots);
            }
            return MyJsonBean.successData("账龄快照生成成功，共" + snapshots.size() + "条记录");
        } catch (Exception e) {
            log.error("生成账龄快照失败", e);
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getLatestSnapshotDate(Long tenantId) {
        try {
            LocalDate latestDate = agingSnapshotMapper.selectLatestSnapshotDate(tenantId);
            return MyJsonBean.successData(latestDate);
        } catch (Exception e) {
            log.error("查询最新快照日期失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getAgingTrend(LocalDate startDate, LocalDate endDate, Long tenantId) {
        try {
            List<Map<String, Object>> trend = agingSnapshotMapper.selectAgingTrend(startDate, endDate, tenantId);
            return MyJsonBean.successData(trend);
        } catch (Exception e) {
            log.error("查询账龄趋势失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getTotalOverdueAmount(LocalDate analysisDate, Long tenantId) {
        try {
            BigDecimal overdueAmount = agingSnapshotMapper.selectTotalOverdueAmount(analysisDate, tenantId);
            return MyJsonBean.successData(overdueAmount);
        } catch (Exception e) {
            log.error("查询逾期金额统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportAgingReport(ArAgingAnalysisQueryParam param) {
        return MyJsonBean.successData("导出功能待实现");
    }

    /**
     * 计算账龄区间
     */
    private String calculateAgingRange(int agingDays) {
        if (agingDays <= 0) {
            return "未逾期";
        } else if (agingDays <= 30) {
            return "1-30天";
        } else if (agingDays <= 60) {
            return "31-60天";
        } else if (agingDays <= 90) {
            return "61-90天";
        } else if (agingDays <= 180) {
            return "91-180天";
        } else if (agingDays <= 365) {
            return "181-365天";
        } else {
            return "1年以上";
        }
    }

    /**
     * 计算风险等级
     */
    private Integer calculateRiskLevel(int agingDays) {
        if (agingDays <= 0) {
            return 1; // 低
        } else if (agingDays <= 60) {
            return 2; // 中
        } else if (agingDays <= 180) {
            return 3; // 高
        } else {
            return 4; // 极高
        }
    }
}
