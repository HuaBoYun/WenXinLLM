package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblSalaryTotal;
import com.huabo.cybermonitor.mapper.TblSalaryTotalMapper;
import com.huabo.cybermonitor.service.ITblSalaryTotalService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblSalaryTotalQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblSalaryTotalServiceImpl extends ServiceImpl<TblSalaryTotalMapper, TblSalaryTotal>
        implements ITblSalaryTotalService {

    @Override
    public PageResult<TblSalaryTotal> selectByPage(TblSalaryTotalQueryVO queryVO) {
        LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblSalaryTotal::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblSalaryTotal::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getReportYear())) {
            wrapper.eq(TblSalaryTotal::getReportYear, queryVO.getReportYear());
        }
        if (StringUtils.isNotEmpty(queryVO.getReportMonth())) {
            wrapper.eq(TblSalaryTotal::getReportMonth, queryVO.getReportMonth());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblSalaryTotal::getCompanyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblSalaryTotal::getCreateTime);

        Page<TblSalaryTotal> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblSalaryTotal> result = this.page(page, wrapper);

        PageResult<TblSalaryTotal> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblSalaryTotal record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblSalaryTotal record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }

    @Override
    public Map<String, Object> getStatistics(String companyId) {
        Map<String, Object> stats = new HashMap<>();
        LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            wrapper.eq(TblSalaryTotal::getCompanyId, companyId);
        }
        long totalCount = this.count(wrapper);
        stats.put("totalCount", totalCount);
        stats.put("total", totalCount);

        // 已填报的记录数（actualTotal不为空）
        LambdaQueryWrapper<TblSalaryTotal> completedWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            completedWrapper.eq(TblSalaryTotal::getCompanyId, companyId);
        }
        completedWrapper.isNotNull(TblSalaryTotal::getActualTotal);
        long completedCount = this.count(completedWrapper);
        stats.put("completed", completedCount);

        // 异常：工资增长率超过利润增长率（违反"两个不高于"原则）
        LambdaQueryWrapper<TblSalaryTotal> abnormalWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            abnormalWrapper.eq(TblSalaryTotal::getCompanyId, companyId);
        }
        abnormalWrapper.isNotNull(TblSalaryTotal::getWageGrowthRate);
        abnormalWrapper.isNotNull(TblSalaryTotal::getProfitGrowthRate);
        abnormalWrapper.apply("WAGE_GROWTH_RATE > PROFIT_GROWTH_RATE");
        long abnormalCount = 0;
        try { abnormalCount = this.count(abnormalWrapper); } catch (Exception e) { /* 容错 */ }
        stats.put("abnormal", abnormalCount);

        // 风险：人工成本率超过40%
        LambdaQueryWrapper<TblSalaryTotal> riskWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            riskWrapper.eq(TblSalaryTotal::getCompanyId, companyId);
        }
        riskWrapper.isNotNull(TblSalaryTotal::getLaborCostRate);
        riskWrapper.apply("LABOR_COST_RATE > 40");
        long riskCount = 0;
        try { riskCount = this.count(riskWrapper); } catch (Exception e) { /* 容错 */ }
        stats.put("riskCount", riskCount);

        return stats;
    }
}

