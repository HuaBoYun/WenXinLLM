package com.financial.sharing.service.impl;

import com.financial.sharing.service.SpecialCostService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.financial.sharing.vo.param.SpecialCostParam;
import com.financial.sharing.vo.result.SpecialCostResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class SpecialCostServiceImpl implements SpecialCostService {

    @Override
    public Map<String, Object> getSpecialCostStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProjectCost", new BigDecimal("12500000.00"));
        return stats;
    }

    @Override
    public PageResult<SpecialCostResult.ProjectCost> getProjectCostPage(PageableParam param) {
        PageResult<SpecialCostResult.ProjectCost> result = new PageResult<>();
        result.setTlist(new ArrayList<>());
        result.setTotalRecord(0);
        return result;
    }

    @Override
    public void saveOrUpdateProjectCost(SpecialCostParam.ProjectCostSave param) {
        log.info("保存或更新项目成本");
    }

    @Override
    public SpecialCostResult.ProjectCostDetail getProjectCostById(Long projectId) {
        return new SpecialCostResult.ProjectCostDetail();
    }

    @Override
    public void deleteProjectCost(Long projectId) {
        log.info("删除项目成本");
    }

    @Override
    public void batchDeleteProjectCost(List<Long> projectIds) {
        log.info("批量删除项目成本");
    }

    @Override
    public SpecialCostResult.ProjectCostAnalysis getProjectCostAnalysis(Long projectId) {
        return new SpecialCostResult.ProjectCostAnalysis();
    }

    @Override
    public PageResult<SpecialCostResult.ActivityCost> getActivityCostPage(PageableParam param) {
        PageResult<SpecialCostResult.ActivityCost> result = new PageResult<>();
        result.setTlist(new ArrayList<>());
        result.setTotalRecord(0);
        return result;
    }

    @Override
    public void saveOrUpdateActivityCost(SpecialCostParam.ActivityCostSave param) {
        log.info("保存或更新作业成本");
    }

    @Override
    public SpecialCostResult.ActivityCostDetail getActivityCostById(Long activityId) {
        return new SpecialCostResult.ActivityCostDetail();
    }

    @Override
    public void deleteActivityCost(Long activityId) {
        log.info("删除作业成本");
    }

    @Override
    public void processActivityCostAllocation(SpecialCostParam.ActivityAllocation param) {
        log.info("处理作业成本分配");
    }

    @Override
    public PageResult<SpecialCostResult.QualityCost> getQualityCostPage(PageableParam param) {
        PageResult<SpecialCostResult.QualityCost> result = new PageResult<>();
        result.setTlist(new ArrayList<>());
        result.setTotalRecord(0);
        return result;
    }

    @Override
    public void saveOrUpdateQualityCost(SpecialCostParam.QualityCostSave param) {
        log.info("保存或更新质量成本");
    }

    @Override
    public SpecialCostResult.QualityCostDetail getQualityCostById(Long qualityId) {
        return new SpecialCostResult.QualityCostDetail();
    }

    @Override
    public void deleteQualityCost(Long qualityId) {
        log.info("删除质量成本");
    }

    @Override
    public Map<String, Object> getQualityCostCategoryStats() {
        return new HashMap<>();
    }

    @Override
    public PageResult<SpecialCostResult.EnvironmentCost> getEnvironmentCostPage(PageableParam param) {
        PageResult<SpecialCostResult.EnvironmentCost> result = new PageResult<>();
        result.setTlist(new ArrayList<>());
        result.setTotalRecord(0);
        return result;
    }

    @Override
    public void saveOrUpdateEnvironmentCost(SpecialCostParam.EnvironmentCostSave param) {
        log.info("保存或更新环境成本");
    }

    @Override
    public SpecialCostResult.EnvironmentCostDetail getEnvironmentCostById(Long environmentId) {
        return new SpecialCostResult.EnvironmentCostDetail();
    }

    @Override
    public void deleteEnvironmentCost(Long environmentId) {
        log.info("删除环境成本");
    }

    @Override
    public PageResult<SpecialCostResult.RdCost> getRdCostPage(PageableParam param) {
        PageResult<SpecialCostResult.RdCost> result = new PageResult<>();
        result.setTlist(new ArrayList<>());
        result.setTotalRecord(0);
        return result;
    }

    @Override
    public void saveOrUpdateRdCost(SpecialCostParam.RdCostSave param) {
        log.info("保存或更新研发成本");
    }

    @Override
    public SpecialCostResult.RdCostDetail getRdCostById(Long rdId) {
        return new SpecialCostResult.RdCostDetail();
    }

    @Override
    public void deleteRdCost(Long rdId) {
        log.info("删除研发成本");
    }

    @Override
    public SpecialCostResult.ComprehensiveAnalysis getSpecialCostAnalysis(SpecialCostParam.AnalysisQuery param) {
        return new SpecialCostResult.ComprehensiveAnalysis();
    }

    @Override
    public SpecialCostResult.CompareAnalysis getSpecialCostCompareAnalysis(String startDate, String endDate, String compareType) {
        return new SpecialCostResult.CompareAnalysis();
    }
}
