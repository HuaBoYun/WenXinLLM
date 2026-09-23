package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblFundAllocation;
import com.global.treasurer.mapper.TblFundAllocationMapper;
import com.global.treasurer.service.TblFundAllocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 资金下拨Service实现类
 * @author Claude
 * @date 2026-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblFundAllocationServiceImpl implements TblFundAllocationService {
    private static final Logger log = LoggerFactory.getLogger(TblFundAllocationServiceImpl.class);

    @Resource
    private TblFundAllocationMapper tblFundAllocationMapper;

    @Override
    public PageInfo<TblFundAllocation> getAllocationPage(Integer pageNum, Integer pageSize,
                                                         String allocationNo, String allocationStatus,
                                                         String startDate, String endDate) {
        PageHelper.startPage(pageNum, pageSize);
        LambdaQueryWrapper<TblFundAllocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(allocationNo), TblFundAllocation::getAllocationCode, allocationNo)
               .eq(StringUtils.isNotBlank(allocationStatus), TblFundAllocation::getAllocationStatus, allocationStatus)
               .orderByDesc(TblFundAllocation::getCreateTime);
        List<TblFundAllocation> list = tblFundAllocationMapper.selectList(wrapper);
        return new PageInfo<>(list);
    }

    @Override
    public TblFundAllocation getAllocationById(String allocationId) {
        return tblFundAllocationMapper.selectById(allocationId);
    }

    @Override
    public TblFundAllocation saveAllocation(TblFundAllocation allocation) {
        allocation.setAllocationId(null); // IDENTITY自增，不手动设置
        allocation.setAllocationCode("AL" + System.currentTimeMillis());
        allocation.setCreateTime(new Date());
        if (allocation.getAllocationStatus() == null) {
            allocation.setAllocationStatus("PENDING");
        }
        if (allocation.getApprovalStatus() == null) {
            allocation.setApprovalStatus("PENDING");
        }
        tblFundAllocationMapper.insertAllocation(allocation);
        return allocation;
    }

    @Override
    public void updateAllocation(TblFundAllocation allocation) {
        allocation.setUpdateTime(new Date());
        tblFundAllocationMapper.updateById(allocation);
    }

    @Override
    public void deleteAllocation(String allocationId) {
        tblFundAllocationMapper.deleteById(allocationId);
    }

    @Override
    public void cancelAllocation(String allocationId) {
        TblFundAllocation allocation = new TblFundAllocation();
        allocation.setAllocationId(Long.valueOf(allocationId));
        allocation.setAllocationStatus("CANCELLED");
        allocation.setUpdateTime(new Date());
        tblFundAllocationMapper.updateById(allocation);
    }

    @Override
    public String executeAllocation(String allocationId) {
        TblFundAllocation allocation = tblFundAllocationMapper.selectById(allocationId);
        if (allocation == null) {
            return null;
        }
        allocation.setAllocationStatus("EXECUTING");
        allocation.setExecutionTime(new Date());
        allocation.setUpdateTime(new Date());
        tblFundAllocationMapper.updateById(allocation);
        log.info("执行资金下拨: {}", allocation.getAllocationCode());
        return allocation.getAllocationCode();
    }

    @Override
    public String retryAllocation(String allocationId) {
        TblFundAllocation allocation = tblFundAllocationMapper.selectById(allocationId);
        if (allocation == null) {
            return null;
        }
        allocation.setAllocationStatus("EXECUTING");
        allocation.setUpdateTime(new Date());
        tblFundAllocationMapper.updateById(allocation);
        return allocation.getAllocationCode();
    }

    @Override
    public void approveAllocation(String allocationId, String approveResult, String approveRemark) {
        TblFundAllocation allocation = new TblFundAllocation();
        allocation.setAllocationId(Long.valueOf(allocationId));
        allocation.setApprovalStatus(approveResult);
        allocation.setApprovalOpinion(approveRemark);
        allocation.setApprovalTime(new Date());
        allocation.setUpdateTime(new Date());
        if ("APPROVED".equals(approveResult)) {
            allocation.setAllocationStatus("APPROVED");
        } else {
            allocation.setAllocationStatus("REJECTED");
        }
        tblFundAllocationMapper.updateById(allocation);
    }

    @Override
    public List<TblFundAllocation> getPendingApprovalList() {
        LambdaQueryWrapper<TblFundAllocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblFundAllocation::getAllocationStatus, "PENDING");
        return tblFundAllocationMapper.selectList(wrapper);
    }

    @Override
    public long count() {
        LambdaQueryWrapper<TblFundAllocation> wrapper = new LambdaQueryWrapper<>();
        return tblFundAllocationMapper.selectCount(wrapper);
    }

    @Override
    public long countByStatus(String status) {
        LambdaQueryWrapper<TblFundAllocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblFundAllocation::getAllocationStatus, status);
        return tblFundAllocationMapper.selectCount(wrapper);
    }

    @Override
    public long countTodayExecutions() {
        LambdaQueryWrapper<TblFundAllocation> wrapper = new LambdaQueryWrapper<>();
        // 简化实现：统计所有执行记录
        return tblFundAllocationMapper.selectCount(wrapper);
    }
}

