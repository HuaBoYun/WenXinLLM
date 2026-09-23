package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.enterpriseReport.dto.ReportWorkflowQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportWorkflow;
import com.financial.sharing.enterpriseReport.mapper.ReportWorkflowMapper;
import com.financial.sharing.enterpriseReport.service.ReportWorkflowService;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 报表工作流Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ReportWorkflowServiceImpl extends ServiceImpl<ReportWorkflowMapper, TblReportWorkflow> 
        implements ReportWorkflowService {

    @Override
    public List<TblReportWorkflow> getList(ReportWorkflowQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        return baseMapper.selectReportWorkflowList(
            tenantId,
            param.getTaskId(),
            param.getWorkflowName(),
            param.getWorkflowType(),
            param.getStatus()
        );
    }

    @Override
    public TblReportWorkflow getDetail(String workflowId) {
        TblReportWorkflow reportWorkflow = this.getById(workflowId);
        if (reportWorkflow != null) {
            // 补充关联信息
            String tenantId = UserUtils.getUser().getOrgid().toString();
            List<TblReportWorkflow> list = baseMapper.selectReportWorkflowList(
                tenantId, null, null, null, null
            );
            for (TblReportWorkflow workflow : list) {
                if (workflow.getWorkflowId().equals(workflowId)) {
                    reportWorkflow.setTaskName(workflow.getTaskName());
                    break;
                }
            }
        }
        return reportWorkflow;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveReportWorkflow(TblReportWorkflow reportWorkflow) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        reportWorkflow.setTenantId(tenantId);
        
        // 检查流程编码是否重复
        int count = baseMapper.checkWorkflowCodeExists(
            reportWorkflow.getWorkflowCode(),
            tenantId,
            reportWorkflow.getWorkflowId()
        );
        if (count > 0) {
            throw new RuntimeException("流程编码已存在");
        }
        
        if (StringUtils.isEmpty(reportWorkflow.getWorkflowId())) {
            // 新增
            reportWorkflow.setCreateUser(userId);
            reportWorkflow.setCreateTime(now);
            reportWorkflow.setUpdateUser(userId);
            reportWorkflow.setUpdateTime(now);
            
            // 设置默认状态
            if (StringUtils.isEmpty(reportWorkflow.getStatus())) {
                reportWorkflow.setStatus("ACTIVE");
            }
            
            // 设置默认排序号
            if (reportWorkflow.getSortNo() == null) {
                reportWorkflow.setSortNo(0);
            }
            
            return this.save(reportWorkflow);
        } else {
            // 修改
            reportWorkflow.setUpdateUser(userId);
            reportWorkflow.setUpdateTime(now);
            
            return this.updateById(reportWorkflow);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReportWorkflow(String workflowId) {
        TblReportWorkflow reportWorkflow = this.getById(workflowId);
        if (reportWorkflow == null) {
            throw new RuntimeException("工作流不存在");
        }
        
        // TODO: 检查是否有工作流实例关联
        
        return this.removeById(workflowId);
    }

    @Override
    public List<TblReportWorkflow> getListByTaskId(String taskId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        QueryWrapper<TblReportWorkflow> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        wrapper.eq("TASK_ID", taskId);
        wrapper.orderByAsc("SORT_NO");
        wrapper.orderByDesc("CREATE_TIME");
        
        return this.list(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateWorkflowStatus(String workflowId, String status) {
        TblReportWorkflow reportWorkflow = this.getById(workflowId);
        if (reportWorkflow == null) {
            throw new RuntimeException("工作流不存在");
        }
        
        reportWorkflow.setStatus(status);
        reportWorkflow.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        reportWorkflow.setUpdateTime(new Date());
        
        return this.updateById(reportWorkflow);
    }
}

