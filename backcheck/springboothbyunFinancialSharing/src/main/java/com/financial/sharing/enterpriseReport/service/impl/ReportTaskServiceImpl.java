package com.financial.sharing.enterpriseReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.enterpriseReport.dto.ReportTaskQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportTask;
import com.financial.sharing.enterpriseReport.mapper.ReportTaskMapper;
import com.financial.sharing.enterpriseReport.service.ReportTaskService;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 报表任务Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ReportTaskServiceImpl extends ServiceImpl<ReportTaskMapper, TblReportTask> 
        implements ReportTaskService {

    @Override
    public List<TblReportTask> getList(ReportTaskQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        return baseMapper.selectReportTaskList(
            tenantId,
            param.getGroupId(),
            param.getTaskName(),
            param.getPeriodType(),
            param.getStatus()
        );
    }

    @Override
    public TblReportTask getDetail(String taskId) {
        TblReportTask reportTask = this.getById(taskId);
        if (reportTask != null) {
            // 补充关联信息
            String tenantId = UserUtils.getUser().getOrgid().toString();
            List<TblReportTask> list = baseMapper.selectReportTaskList(
                tenantId, null, null, null, null
            );
            for (TblReportTask task : list) {
                if (task.getTaskId().equals(taskId)) {
                    reportTask.setGroupName(task.getGroupName());
                    break;
                }
            }
        }
        return reportTask;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveReportTask(TblReportTask reportTask) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        reportTask.setTenantId(tenantId);
        
        // 检查任务编码是否重复
        int count = baseMapper.checkTaskCodeExists(
            reportTask.getTaskCode(),
            tenantId,
            reportTask.getTaskId()
        );
        if (count > 0) {
            throw new RuntimeException("任务编码已存在");
        }
        
        if (StringUtils.isEmpty(reportTask.getTaskId())) {
            // 新增
            reportTask.setCreateUser(userId);
            reportTask.setCreateTime(now);
            reportTask.setUpdateUser(userId);
            reportTask.setUpdateTime(now);
            
            // 设置默认状态
            if (StringUtils.isEmpty(reportTask.getStatus())) {
                reportTask.setStatus("DRAFT");
            }
            
            // 设置默认值
            if (reportTask.getPeriodOffset() == null) {
                reportTask.setPeriodOffset(0);
            }
            if (StringUtils.isEmpty(reportTask.getIsHierarchical())) {
                reportTask.setIsHierarchical("N");
            }
            if (StringUtils.isEmpty(reportTask.getIsNodeCheck())) {
                reportTask.setIsNodeCheck("N");
            }
            if (StringUtils.isEmpty(reportTask.getIsArchiveControl())) {
                reportTask.setIsArchiveControl("N");
            }
            
            return this.save(reportTask);
        } else {
            // 修改
            reportTask.setUpdateUser(userId);
            reportTask.setUpdateTime(now);
            
            return this.updateById(reportTask);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteReportTask(String taskId) {
        TblReportTask reportTask = this.getById(taskId);
        if (reportTask == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 只能删除草稿状态的任务
        if (!"DRAFT".equals(reportTask.getStatus())) {
            throw new RuntimeException("只能删除草稿状态的任务");
        }
        
        // TODO: 检查是否有工作流关联
        // TODO: 检查是否有报表数据关联
        
        return this.removeById(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishTask(String taskId) {
        TblReportTask reportTask = this.getById(taskId);
        if (reportTask == null) {
            throw new RuntimeException("任务不存在");
        }
        
        if ("PUBLISHED".equals(reportTask.getStatus())) {
            throw new RuntimeException("任务已发布");
        }
        
        // 设置为已发布状态
        reportTask.setStatus("PUBLISHED");
        reportTask.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        reportTask.setUpdateTime(new Date());
        
        return this.updateById(reportTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawTask(String taskId) {
        TblReportTask reportTask = this.getById(taskId);
        if (reportTask == null) {
            throw new RuntimeException("任务不存在");
        }
        
        if (!"PUBLISHED".equals(reportTask.getStatus())) {
            throw new RuntimeException("只能撤回已发布的任务");
        }

        // TODO: 检查是否有进行中的工作流

        // 设置为草稿状态
        reportTask.setStatus("DRAFT");
        reportTask.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        reportTask.setUpdateTime(new Date());

        return this.updateById(reportTask);
    }

    @Override
    public List<TblReportTask> getListByGroupId(String groupId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();

        QueryWrapper<TblReportTask> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        wrapper.eq("GROUP_ID", groupId);
        wrapper.orderByDesc("CREATE_TIME");

        return this.list(wrapper);
    }
}


