package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblMilitaryTask;
import com.huabo.cybermonitor.mapper.TblMilitaryTaskMapper;
import com.huabo.cybermonitor.service.ITblMilitaryTaskService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblMilitaryTaskQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblMilitaryTaskServiceImpl extends ServiceImpl<TblMilitaryTaskMapper, TblMilitaryTask>
        implements ITblMilitaryTaskService {

    @Override
    public PageResult<TblMilitaryTask> selectByPage(TblMilitaryTaskQueryVO queryVO) {
        LambdaQueryWrapper<TblMilitaryTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblMilitaryTask::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblMilitaryTask::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getTaskCode())) {
            wrapper.eq(TblMilitaryTask::getTaskCode, queryVO.getTaskCode());
        }
        if (StringUtils.isNotEmpty(queryVO.getTaskType())) {
            wrapper.eq(TblMilitaryTask::getTaskType, queryVO.getTaskType());
        }
        if (StringUtils.isNotEmpty(queryVO.getSecurityLevel())) {
            wrapper.eq(TblMilitaryTask::getSecurityLevel, queryVO.getSecurityLevel());
        }
        if (StringUtils.isNotEmpty(queryVO.getTaskStatus())) {
            wrapper.eq(TblMilitaryTask::getTaskStatus, queryVO.getTaskStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblMilitaryTask::getTaskName, queryVO.getKeyword())
                    .or().like(TblMilitaryTask::getTaskCode, queryVO.getKeyword())
                    .or().like(TblMilitaryTask::getCompanyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblMilitaryTask::getCreateTime);

        Page<TblMilitaryTask> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblMilitaryTask> result = this.page(page, wrapper);

        PageResult<TblMilitaryTask> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblMilitaryTask record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblMilitaryTask record) {
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
        LambdaQueryWrapper<TblMilitaryTask> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            wrapper.eq(TblMilitaryTask::getCompanyId, companyId);
        }
        stats.put("totalCount", this.count(wrapper));
        return stats;
    }
}

