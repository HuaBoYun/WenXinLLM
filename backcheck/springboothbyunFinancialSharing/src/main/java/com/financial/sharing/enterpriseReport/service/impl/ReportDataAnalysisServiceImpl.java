package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.enterpriseReport.dto.ReportDataQueryDTO;
import com.financial.sharing.enterpriseReport.entity.TblReportData;
import com.financial.sharing.enterpriseReport.mapper.ReportDataMapper;
import com.financial.sharing.enterpriseReport.service.ReportDataAnalysisService;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报表数据查询分析Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ReportDataAnalysisServiceImpl implements ReportDataAnalysisService {

    @Autowired
    private ReportDataMapper reportDataMapper;

    @Override
    public List<ReportDataQueryDTO> aggregateByIndicator(String taskId, String period) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        return reportDataMapper.aggregateByIndicator(tenantId, taskId, period);
    }

    @Override
    public List<ReportDataQueryDTO> aggregateByOrg(String taskId, String period) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        return reportDataMapper.aggregateByOrg(tenantId, taskId, period);
    }

    @Override
    public List<ReportDataQueryDTO> aggregateByPeriod(String taskId, String indicatorId, String orgId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        return reportDataMapper.aggregateByPeriod(tenantId, taskId, indicatorId, orgId);
    }

    @Override
    public ReportDataQueryDTO comparePeriod(String taskId, String indicatorId, String orgId,
                                           String currentPeriod, String comparePeriod) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        return reportDataMapper.comparePeriod(tenantId, taskId, indicatorId, orgId, 
                                             currentPeriod, comparePeriod);
    }

    @Override
    public List<ReportDataQueryDTO> multiDimensionQuery(Map<String, Object> params) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        // 构建查询条件
        QueryWrapper<TblReportData> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        
        if (params.containsKey("taskId") && params.get("taskId") != null) {
            wrapper.eq("TASK_ID", params.get("taskId"));
        }
        if (params.containsKey("templateId") && params.get("templateId") != null) {
            wrapper.eq("TEMPLATE_ID", params.get("templateId"));
        }
        if (params.containsKey("indicatorId") && params.get("indicatorId") != null) {
            wrapper.eq("INDICATOR_ID", params.get("indicatorId"));
        }
        if (params.containsKey("orgId") && params.get("orgId") != null) {
            wrapper.eq("ORG_ID", params.get("orgId"));
        }
        if (params.containsKey("period") && params.get("period") != null) {
            wrapper.eq("PERIOD", params.get("period"));
        }
        if (params.containsKey("dataSource") && params.get("dataSource") != null) {
            wrapper.eq("DATA_SOURCE", params.get("dataSource"));
        }
        
        wrapper.orderByDesc("CREATE_TIME");
        
        // 查询数据
        List<TblReportData> dataList = reportDataMapper.selectList(wrapper);
        
        // 转换为DTO
        return dataList.stream().map(data -> {
            ReportDataQueryDTO dto = new ReportDataQueryDTO();
            dto.setIndicatorId(data.getIndicatorId());
            dto.setIndicatorName(data.getIndicatorName());
            dto.setOrgId(data.getOrgId());
            dto.setOrgName(data.getOrgName());
            dto.setPeriod(data.getPeriod());
            dto.setTaskId(data.getTaskId());
            dto.setTaskName(data.getTaskName());
            
            // 转换数据值
            try {
                if (data.getDataValue() != null && !data.getDataValue().isEmpty()) {
                    dto.setDataValue(new BigDecimal(data.getDataValue()));
                }
            } catch (NumberFormatException e) {
                // 忽略非数字值
            }
            
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ReportDataQueryDTO> trendAnalysis(String taskId, String indicatorId, String orgId,
                                                  String startPeriod, String endPeriod) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        // 构建查询条件
        QueryWrapper<TblReportData> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        
        if (taskId != null && !taskId.isEmpty()) {
            wrapper.eq("TASK_ID", taskId);
        }
        if (indicatorId != null && !indicatorId.isEmpty()) {
            wrapper.eq("INDICATOR_ID", indicatorId);
        }
        if (orgId != null && !orgId.isEmpty()) {
            wrapper.eq("ORG_ID", orgId);
        }
        if (startPeriod != null && !startPeriod.isEmpty()) {
            wrapper.ge("PERIOD", startPeriod);
        }
        if (endPeriod != null && !endPeriod.isEmpty()) {
            wrapper.le("PERIOD", endPeriod);
        }
        
        wrapper.orderByAsc("PERIOD");
        
        // 查询数据
        List<TblReportData> dataList = reportDataMapper.selectList(wrapper);
        
        // 按期间分组并汇总
        Map<String, List<TblReportData>> periodMap = dataList.stream()
            .collect(Collectors.groupingBy(TblReportData::getPeriod));
        
        // 转换为DTO
        List<ReportDataQueryDTO> result = new ArrayList<>();
        for (Map.Entry<String, List<TblReportData>> entry : periodMap.entrySet()) {
            String period = entry.getKey();
            List<TblReportData> periodDataList = entry.getValue();

            ReportDataQueryDTO dto = new ReportDataQueryDTO();
            dto.setPeriod(period);
            dto.setDataCount(periodDataList.size());

            // 计算汇总值
            BigDecimal sum = BigDecimal.ZERO;
            BigDecimal max = null;
            BigDecimal min = null;
            int count = 0;

            for (TblReportData data : periodDataList) {
                try {
                    if (data.getDataValue() != null && !data.getDataValue().isEmpty()) {
                        BigDecimal value = new BigDecimal(data.getDataValue());
                        sum = sum.add(value);
                        count++;

                        if (max == null || value.compareTo(max) > 0) {
                            max = value;
                        }
                        if (min == null || value.compareTo(min) < 0) {
                            min = value;
                        }
                    }
                } catch (NumberFormatException e) {
                    // 忽略非数字值
                }
            }

            dto.setDataSum(sum);
            dto.setDataMax(max);
            dto.setDataMin(min);
            if (count > 0) {
                dto.setDataAvg(sum.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP));
            }

            result.add(dto);
        }

        return result;
    }
}


