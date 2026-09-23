package com.financial.sharing.budgetPlanning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.budgetPlanning.dto.BudgetReportQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetReport;
import com.financial.sharing.budgetPlanning.mapper.BudgetReportMapper;
import com.financial.sharing.budgetPlanning.service.BudgetReportService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 预算报表Service实现类
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Slf4j
@Service
public class BudgetReportServiceImpl extends ServiceImpl<BudgetReportMapper, TblBudgetReport> 
        implements BudgetReportService {

    @Autowired
    private BudgetReportMapper budgetReportMapper;

    @Override
    public PageInfo<TblBudgetReport> getReportList(BudgetReportQueryParam param) {
        if (param.getPageNum() != null && param.getPageSize() != null) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        List<TblBudgetReport> list = budgetReportMapper.selectReportList(param);
        return new PageInfo<>(list);
    }

    @Override
    public TblBudgetReport getReportById(String reportId) {
        if (!StringUtils.hasText(reportId)) {
            return null;
        }
        return this.getById(reportId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createReport(TblBudgetReport report) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查报表编码是否重复
            LambdaQueryWrapper<TblBudgetReport> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TblBudgetReport::getOrgId, report.getOrgId())
                    .eq(TblBudgetReport::getReportCode, report.getReportCode());
            
            long count = this.count(queryWrapper);
            if (count > 0) {
                result.put("success", false);
                result.put("message", "报表编码已存在");
                return result;
            }

            // 设置创建时间
            report.setCreateTime(new Date());
            report.setUpdateTime(new Date());

            // 保存
            boolean success = this.save(report);
            
            result.put("success", success);
            result.put("message", success ? "创建成功" : "创建失败");
            result.put("data", report);
            
        } catch (Exception e) {
            log.error("创建报表配置失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "创建失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateReport(TblBudgetReport report) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查报表是否存在
            TblBudgetReport existReport = this.getById(report.getReportId());
            if (existReport == null) {
                result.put("success", false);
                result.put("message", "报表配置不存在");
                return result;
            }

            // 设置更新时间
            report.setUpdateTime(new Date());

            // 更新
            boolean success = this.updateById(report);
            
            result.put("success", success);
            result.put("message", success ? "更新成功" : "更新失败");
            result.put("data", report);
            
        } catch (Exception e) {
            log.error("更新报表配置失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "更新失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> deleteReports(List<String> reportIds) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (reportIds == null || reportIds.isEmpty()) {
                result.put("success", false);
                result.put("message", "请选择要删除的报表");
                return result;
            }

            // 批量删除
            boolean success = this.removeByIds(reportIds);
            
            result.put("success", success);
            result.put("message", success ? "删除成功" : "删除失败");
            
        } catch (Exception e) {
            log.error("删除报表配置失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "删除失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public PageInfo<Map<String, Object>> queryBudgetDetailData(BudgetReportQueryParam param) {
        if (param.getPageNum() != null && param.getPageSize() != null) {
            PageHelper.startPage(param.getPageNum(), param.getPageSize());
        }
        List<Map<String, Object>> list = budgetReportMapper.selectBudgetDetailData(param);
        return new PageInfo<>(list);
    }

    @Override
    public List<Map<String, Object>> queryBudgetSummaryData(BudgetReportQueryParam param) {
        return budgetReportMapper.selectBudgetSummaryData(param);
    }

    @Override
    public List<Map<String, Object>> queryBudgetCompareData(BudgetReportQueryParam param) {
        return budgetReportMapper.selectBudgetCompareData(param);
    }

    @Override
    public List<Map<String, Object>> queryBudgetTrendData(BudgetReportQueryParam param) {
        return budgetReportMapper.selectBudgetTrendData(param);
    }

    @Override
    public Map<String, Object> exportReportData(BudgetReportQueryParam param) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<Map<String, Object>> data;
            
            // 根据报表类型查询数据
            switch (param.getReportType()) {
                case "DETAIL":
                    data = budgetReportMapper.selectBudgetDetailData(param);
                    break;
                case "SUMMARY":
                    data = budgetReportMapper.selectBudgetSummaryData(param);
                    break;
                case "COMPARE":
                    data = budgetReportMapper.selectBudgetCompareData(param);
                    break;
                case "TREND":
                    data = budgetReportMapper.selectBudgetTrendData(param);
                    break;
                default:
                    data = new ArrayList<>();
            }
            
            result.put("success", true);
            result.put("data", data);
            result.put("message", "导出成功");
            
        } catch (Exception e) {
            log.error("导出报表数据失败: {}", e.getMessage(), e);
            result.put("success", false);
            result.put("message", "导出失败: " + e.getMessage());
        }
        
        return result;
    }
}

