package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.GzctInvestCompliance;
import com.huabo.cybermonitor.mapper.GzctInvestComplianceMapper;
import com.huabo.cybermonitor.service.IGzctInvestComplianceService;
import com.huabo.cybermonitor.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class GzctInvestComplianceServiceImpl extends ServiceImpl<GzctInvestComplianceMapper, GzctInvestCompliance>
        implements IGzctInvestComplianceService {

    @Override
    public PageResult<GzctInvestCompliance> selectByPage(Map<String, Object> params) {
        int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        LambdaQueryWrapper<GzctInvestCompliance> wrapper = new LambdaQueryWrapper<>();
        String complianceStatus = (String) params.get("complianceStatus");
        if (StringUtils.isNotEmpty(complianceStatus)) wrapper.eq(GzctInvestCompliance::getIsCompliant, complianceStatus);
        String approvalStatus = (String) params.get("approvalStatus");
        if (StringUtils.isNotEmpty(approvalStatus)) wrapper.eq(GzctInvestCompliance::getApprovalStatus, approvalStatus);
        String projectName = (String) params.get("projectName");
        if (StringUtils.isNotEmpty(projectName)) wrapper.like(GzctInvestCompliance::getProjectName, projectName);
        // 越权标记筛选：通过 issueDesc 包含"越权"关键字来判断
        String issueDesc = (String) params.get("issueDesc");
        if (StringUtils.isNotEmpty(issueDesc)) wrapper.like(GzctInvestCompliance::getIssueDesc, issueDesc);
        // 越权标记精确筛选：overrideFlag=Y 表示越权（issueDesc包含越权），N 表示正常（issueDesc不包含越权）
        String overrideFlag = (String) params.get("overrideFlag");
        if ("Y".equals(overrideFlag)) {
            wrapper.like(GzctInvestCompliance::getIssueDesc, "越权");
        } else if ("N".equals(overrideFlag)) {
            wrapper.and(w -> w.isNull(GzctInvestCompliance::getIssueDesc)
                    .or().notLike(GzctInvestCompliance::getIssueDesc, "越权"));
        }
        wrapper.orderByDesc(GzctInvestCompliance::getCreateTime);
        Page<GzctInvestCompliance> page = new Page<>(pageNum, pageSize);
        Page<GzctInvestCompliance> result = this.page(page, wrapper);
        PageResult<GzctInvestCompliance> pr = new PageResult<>();
        pr.setTotalRecord((int) result.getTotal());
        pr.setCurrentPage((int) result.getCurrent());
        pr.setPageNumber((int) result.getCurrent());
        pr.setTotalPage((int) result.getPages());
        pr.setPageSize((int) result.getSize());
        pr.setTlist(result.getRecords());
        return pr;
    }

    @Override
    public Map<String, Object> getChainDetail(String complianceId) {
        Map<String, Object> result = new HashMap<>();
        GzctInvestCompliance compliance = this.getById(complianceId);
        if (compliance != null) {
            result.put("complianceId", complianceId);
            result.put("projectName", compliance.getProjectName());
            result.put("decisionLevel", compliance.getDecisionLevel());
            result.put("isCompliant", compliance.getIsCompliant());
            result.put("issueDesc", compliance.getIssueDesc());
            result.put("approvalStatus", compliance.getApprovalStatus());
            result.put("rectStatus", compliance.getRectStatus());
        }
        return result;
    }

    @Override
    public boolean dispatch(String complianceId) {
        GzctInvestCompliance compliance = this.getById(complianceId);
        if (compliance != null) {
            compliance.setDispatchStatus("DISPATCHED");
            compliance.setUpdateTime(LocalDateTime.now());
            return this.updateById(compliance);
        }
        return false;
    }

    @Override
    public boolean addCompliance(GzctInvestCompliance compliance) {
        compliance.setCreateTime(LocalDateTime.now());
        compliance.setUpdateTime(LocalDateTime.now());
        return this.save(compliance);
    }

    @Override
    public boolean updateCompliance(GzctInvestCompliance compliance) {
        compliance.setUpdateTime(LocalDateTime.now());
        return this.updateById(compliance);
    }

    @Override
    public boolean deleteCompliance(String complianceId) {
        return this.removeById(complianceId);
    }
}
