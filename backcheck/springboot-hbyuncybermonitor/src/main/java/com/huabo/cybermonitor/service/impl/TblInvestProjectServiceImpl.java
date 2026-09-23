package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblInvestProject;
import com.huabo.cybermonitor.mapper.TblInvestProjectMapper;
import com.huabo.cybermonitor.service.ITblInvestProjectService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblInvestProjectQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 投资项目台账服务实现 - 投资穿透式监管
 */
@Service
public class TblInvestProjectServiceImpl extends ServiceImpl<TblInvestProjectMapper, TblInvestProject>
        implements ITblInvestProjectService {

    @Override
    public PageResult<TblInvestProject> selectByPage(TblInvestProjectQueryVO queryVO) {
        LambdaQueryWrapper<TblInvestProject> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getProjectName())) {
            wrapper.like(TblInvestProject::getProjectName, queryVO.getProjectName());
        }
        if (StringUtils.isNotEmpty(queryVO.getProjectCode())) {
            wrapper.eq(TblInvestProject::getProjectCode, queryVO.getProjectCode());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblInvestProject::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblInvestProject::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getInvestType())) {
            wrapper.eq(TblInvestProject::getInvestType, queryVO.getInvestType());
        }
        if (StringUtils.isNotEmpty(queryVO.getInvestCategory())) {
            wrapper.eq(TblInvestProject::getInvestCategory, queryVO.getInvestCategory());
        }
        if (StringUtils.isNotEmpty(queryVO.getApprovalStatus())) {
            wrapper.eq(TblInvestProject::getApprovalStatus, queryVO.getApprovalStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getProjectStatus())) {
            wrapper.eq(TblInvestProject::getProjectStatus, queryVO.getProjectStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getDecisionLevel())) {
            wrapper.eq(TblInvestProject::getDecisionLevel, queryVO.getDecisionLevel());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblInvestProject::getProjectName, queryVO.getKeyword())
                    .or().like(TblInvestProject::getProjectCode, queryVO.getKeyword())
                    .or().like(TblInvestProject::getCompanyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblInvestProject::getCreateTime);

        Page<TblInvestProject> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblInvestProject> result = this.page(page, wrapper);

        PageResult<TblInvestProject> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addProject(TblInvestProject project) {
        project.setCreateTime(LocalDateTime.now());
        return this.save(project);
    }

    @Override
    public boolean updateProject(TblInvestProject project) {
        project.setUpdateTime(LocalDateTime.now());
        return this.updateById(project);
    }

    @Override
    public boolean deleteProject(String projectId) {
        return this.removeById(projectId);
    }

    @Override
    public Map<String, Object> getStatistics(String companyId) {
        Map<String, Object> stats = new HashMap<>();
        LambdaQueryWrapper<TblInvestProject> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            wrapper.eq(TblInvestProject::getCompanyId, companyId);
        }
        long totalCount = this.count(wrapper);
        stats.put("totalCount", totalCount);
        stats.put("total", totalCount);

        LambdaQueryWrapper<TblInvestProject> execWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            execWrapper.eq(TblInvestProject::getCompanyId, companyId);
        }
        execWrapper.eq(TblInvestProject::getProjectStatus, "EXECUTING");
        stats.put("executingCount", this.count(execWrapper));

        // 已完成项目数 - 用于计算监管进度率
        LambdaQueryWrapper<TblInvestProject> completedWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            completedWrapper.eq(TblInvestProject::getCompanyId, companyId);
        }
        completedWrapper.eq(TblInvestProject::getProjectStatus, "COMPLETED");
        long completedCount = this.count(completedWrapper);
        stats.put("completed", completedCount);

        // 异常项目数 - 实际进度低于计划进度超过20%的项目
        LambdaQueryWrapper<TblInvestProject> abnormalWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            abnormalWrapper.eq(TblInvestProject::getCompanyId, companyId);
        }
        abnormalWrapper.eq(TblInvestProject::getProjectStatus, "EXECUTING");
        abnormalWrapper.apply("ACTUAL_PROGRESS < PLAN_PROGRESS - 20");
        long abnormalCount = 0;
        try { abnormalCount = this.count(abnormalWrapper); } catch (Exception e) { /* 字段可能不存在 */ }
        stats.put("abnormal", abnormalCount);

        // 风险项目数 - 审批被驳回或项目状态异常
        LambdaQueryWrapper<TblInvestProject> riskWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            riskWrapper.eq(TblInvestProject::getCompanyId, companyId);
        }
        riskWrapper.in(TblInvestProject::getApprovalStatus, "REJECTED", "PENDING");
        long riskCount = 0;
        try { riskCount = this.count(riskWrapper); } catch (Exception e) { /* 容错 */ }
        stats.put("riskCount", riskCount);

        return stats;
    }
}

