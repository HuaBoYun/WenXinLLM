package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblInvestigationTask;
import com.huabo.cybermonitor.mapper.TblInvestigationTaskMapper;
import com.huabo.cybermonitor.service.ITblInvestigationTaskService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblInvestigationTaskQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblInvestigationTaskServiceImpl extends ServiceImpl<TblInvestigationTaskMapper, TblInvestigationTask>
        implements ITblInvestigationTaskService {

    @Override
    public PageResult<TblInvestigationTask> selectByPage(TblInvestigationTaskQueryVO queryVO) {
        LambdaQueryWrapper<TblInvestigationTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblInvestigationTask::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblInvestigationTask::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getDomainType())) {
            wrapper.eq(TblInvestigationTask::getDomainType, queryVO.getDomainType());
        }
        if (StringUtils.isNotEmpty(queryVO.getTaskStatus())) {
            wrapper.eq(TblInvestigationTask::getTaskStatus, queryVO.getTaskStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getAssignUser())) {
            wrapper.eq(TblInvestigationTask::getAssignUser, queryVO.getAssignUser());
        }
        if (StringUtils.isNotEmpty(queryVO.getConclusion())) {
            wrapper.eq(TblInvestigationTask::getConclusion, queryVO.getConclusion());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblInvestigationTask::getTaskTitle, queryVO.getKeyword())
                    .or().like(TblInvestigationTask::getTaskCode, queryVO.getKeyword())
                    .or().like(TblInvestigationTask::getCompanyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblInvestigationTask::getCreateTime);

        Page<TblInvestigationTask> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblInvestigationTask> result = this.page(page, wrapper);

        PageResult<TblInvestigationTask> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addTask(TblInvestigationTask task) {
        task.setCreateTime(LocalDateTime.now());
        return this.save(task);
    }

    @Override
    public boolean updateTask(TblInvestigationTask task) {
        task.setUpdateTime(LocalDateTime.now());
        return this.updateById(task);
    }

    @Override
    public boolean deleteTask(String id) {
        return this.removeById(id);
    }

    @Override
    public Map<String, Object> getStatistics(String companyId) {
        Map<String, Object> stats = new HashMap<>();
        LambdaQueryWrapper<TblInvestigationTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            wrapper.eq(TblInvestigationTask::getCompanyId, companyId);
        }
        stats.put("totalCount", this.count(wrapper));

        LambdaQueryWrapper<TblInvestigationTask> pendingWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            pendingWrapper.eq(TblInvestigationTask::getCompanyId, companyId);
        }
        pendingWrapper.eq(TblInvestigationTask::getTaskStatus, "PENDING");
        stats.put("pendingCount", this.count(pendingWrapper));
        return stats;
    }
}

