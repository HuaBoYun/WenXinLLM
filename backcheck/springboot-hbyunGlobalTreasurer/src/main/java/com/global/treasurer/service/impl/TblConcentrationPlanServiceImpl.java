package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblConcentrationPlan;
import com.global.treasurer.entity.TblConcentrationStrategy;
import com.global.treasurer.mapper.TblConcentrationPlanMapper;
import com.global.treasurer.service.TblConcentrationPlanService;
import com.global.treasurer.service.TblConcentrationStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.util.Date;
import java.util.List;

/**
 * 归集计划Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblConcentrationPlanServiceImpl implements TblConcentrationPlanService {
    private static final Logger log = LoggerFactory.getLogger(TblConcentrationPlanServiceImpl.class);

    @Resource
    private TblConcentrationPlanMapper tblConcentrationPlanMapper;

    @Resource
    private TblConcentrationStrategyService tblConcentrationStrategyService;

    @Override
    public PageInfo<TblConcentrationPlan> getPlanPage(Integer pageNum, Integer pageSize,
                                                      String planName, String planType, String planStatus, Long strategyId,
                                                      String executionDateStart, String executionDateEnd) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblConcentrationPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(planName), TblConcentrationPlan::getPlanName, planName)
               .eq(StringUtils.isNotBlank(planStatus), TblConcentrationPlan::getPlanStatus, planStatus)
               .eq(strategyId != null, TblConcentrationPlan::getStrategyId, strategyId);

        // 日期范围查询 - 结束日期设置为当天 23:59:59 以包含当天所有数据
        if (StringUtils.isNotBlank(executionDateStart)) {
            wrapper.ge(TblConcentrationPlan::getExecutionTime, parseDate(executionDateStart));
        }
        if (StringUtils.isNotBlank(executionDateEnd)) {
            wrapper.le(TblConcentrationPlan::getExecutionTime, parseDateWithEndOfDay(executionDateEnd));
        }

        wrapper.orderByDesc(TblConcentrationPlan::getCreateTime);
        List<TblConcentrationPlan> list = tblConcentrationPlanMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    /**
     * 解析日期字符串
     */
    private Date parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            log.error("解析日期失败: {}", dateStr, e);
            return null;
        }
    }

    /**
     * 解析日期并设置为当天结束时间 23:59:59
     */
    private Date parseDateWithEndOfDay(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(date);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            cal.set(java.util.Calendar.MILLISECOND, 999);
            return cal.getTime();
        } catch (Exception e) {
            log.error("解析日期失败: {}", dateStr, e);
            return null;
        }
    }

    @Override
    public TblConcentrationPlan getPlanById(Long planId) {
        return tblConcentrationPlanMapper.selectById(planId);
    }

    @Override
    public TblConcentrationPlan savePlan(TblConcentrationPlan plan) {
        plan.setCreateTime(new Date());
        if (plan.getPlanStatus() == null) {
            plan.setPlanStatus("PENDING");
        }
        if (plan.getActualAmount() == null) {
            plan.setActualAmount(new java.math.BigDecimal("0"));
        }
        if (plan.getExecutionProgress() == null) {
            plan.setExecutionProgress(0);
        }
        // 根据 strategyId 查询策略名称
        if (plan.getStrategyId() != null) {
            try {
                TblConcentrationStrategy strategy = tblConcentrationStrategyService.getStrategyById(plan.getStrategyId());
                if (strategy != null) {
                    plan.setStrategyName(strategy.getStrategyName());
                } else {
                    log.warn("未找到策略ID={}对应的策略", plan.getStrategyId());
                }
            } catch (Exception e) {
                log.error("查询策略名称失败, strategyId={}", plan.getStrategyId(), e);
            }
        }
        tblConcentrationPlanMapper.insert(plan);
        return plan;
    }

    @Override
    public void updatePlan(TblConcentrationPlan plan) {
        plan.setUpdateTime(new Date());
        // 如果 strategyId 有值，重新查询策略名称
        if (plan.getStrategyId() != null) {
            try {
                TblConcentrationStrategy strategy = tblConcentrationStrategyService.getStrategyById(plan.getStrategyId());
                if (strategy != null) {
                    plan.setStrategyName(strategy.getStrategyName());
                } else {
                    log.warn("未找到策略ID={}对应的策略，strategyName将为空", plan.getStrategyId());
                    plan.setStrategyName(null);
                }
            } catch (Exception e) {
                log.error("查询策略名称失败, strategyId={}", plan.getStrategyId(), e);
            }
        }
        tblConcentrationPlanMapper.updateById(plan);
    }

    @Override
    public void deletePlan(Long planId) {
        tblConcentrationPlanMapper.deleteById(planId);
    }

    @Override
    public String executePlan(Long planId) {
        TblConcentrationPlan plan = tblConcentrationPlanMapper.selectById(planId);
        if (plan == null) {
            throw new RuntimeException("计划不存在，planId=" + planId);
        }
        plan.setPlanStatus("EXECUTING");
        plan.setExecutionTime(new Date());
        plan.setUpdateTime(new Date());
        tblConcentrationPlanMapper.updateById(plan);
        log.info("执行归集计划: {}", plan.getPlanName());
        return "EX" + System.currentTimeMillis();
    }

    @Override
    public String batchExecutePlan(List<Long> planIds) {
        if (planIds == null || planIds.isEmpty()) {
            throw new RuntimeException("计划ID列表不能为空");
        }
        StringBuilder result = new StringBuilder();
        for (Long planId : planIds) {
            String execId = executePlan(planId);
            result.append(execId).append(",");
        }
        return result.toString();
    }

    @Override
    public void pausePlan(Long planId) {
        TblConcentrationPlan plan = new TblConcentrationPlan();
        plan.setPlanId(planId);
        plan.setPlanStatus("PAUSED");
        plan.setUpdateTime(new Date());
        tblConcentrationPlanMapper.updateById(plan);
    }

    @Override
    public void resumePlan(Long planId) {
        TblConcentrationPlan plan = new TblConcentrationPlan();
        plan.setPlanId(planId);
        plan.setPlanStatus("PENDING");
        plan.setUpdateTime(new Date());
        tblConcentrationPlanMapper.updateById(plan);
    }

    @Override
    public void cancelPlan(Long planId) {
        TblConcentrationPlan plan = new TblConcentrationPlan();
        plan.setPlanId(planId);
        plan.setPlanStatus("CANCELLED");
        plan.setUpdateTime(new Date());
        tblConcentrationPlanMapper.updateById(plan);
    }

    @Override
    public List<TblConcentrationPlan> getPendingPlans() {
        LambdaQueryWrapper<TblConcentrationPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblConcentrationPlan::getPlanStatus, "PENDING");
        return tblConcentrationPlanMapper.selectList(wrapper);
    }

    @Override
    public void batchDeletePlan(List<Long> planIds) {
        if (planIds != null && !planIds.isEmpty()) {
            for (Long planId : planIds) {
                deletePlan(planId);
            }
        }
    }

    @Override
    public void exportPlan(String planName, String planStatus, Long strategyId,
                       ServletOutputStream outputStream) throws Exception {
        LambdaQueryWrapper<TblConcentrationPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(planName), TblConcentrationPlan::getPlanName, planName)
               .eq(StringUtils.isNotBlank(planStatus), TblConcentrationPlan::getPlanStatus, planStatus)
               .eq(strategyId != null, TblConcentrationPlan::getStrategyId, strategyId);
        List<TblConcentrationPlan> list = tblConcentrationPlanMapper.selectList(wrapper);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("归集计划");

        // 创建标题行
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("计划ID");
        headerRow.createCell(1).setCellValue("计划名称");
        headerRow.createCell(2).setCellValue("策略ID");
        headerRow.createCell(3).setCellValue("状态");
        headerRow.createCell(4).setCellValue("创建时间");

        // 填充数据
        int rowNum = 1;
        for (TblConcentrationPlan plan : list) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(plan.getPlanId());
            row.createCell(1).setCellValue(plan.getPlanName());
            row.createCell(2).setCellValue(plan.getStrategyId());
            row.createCell(3).setCellValue(plan.getPlanStatus());
            row.createCell(4).setCellValue(plan.getCreateTime());
        }

        workbook.write(outputStream);
        workbook.close();
    }
}
