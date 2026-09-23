package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblAccountingPolicy;
import com.huabo.cybermonitor.mapper.TblAccountingPolicyMapper;
import com.huabo.cybermonitor.service.ITblAccountingPolicyService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblAccountingPolicyQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TblAccountingPolicyServiceImpl extends ServiceImpl<TblAccountingPolicyMapper, TblAccountingPolicy>
        implements ITblAccountingPolicyService {

    @Override
    public PageResult<TblAccountingPolicy> selectByPage(TblAccountingPolicyQueryVO queryVO) {
        LambdaQueryWrapper<TblAccountingPolicy> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblAccountingPolicy::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblAccountingPolicy::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getPolicyType())) {
            wrapper.eq(TblAccountingPolicy::getPolicyType, queryVO.getPolicyType());
        }
        if (StringUtils.isNotEmpty(queryVO.getReportYear())) {
            wrapper.eq(TblAccountingPolicy::getReportYear, queryVO.getReportYear());
        }
        if (StringUtils.isNotEmpty(queryVO.getIsConsistent())) {
            wrapper.eq(TblAccountingPolicy::getIsConsistent, queryVO.getIsConsistent());
        }
        if (StringUtils.isNotEmpty(queryVO.getStatus())) {
            wrapper.eq(TblAccountingPolicy::getStatus, queryVO.getStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblAccountingPolicy::getCompanyName, queryVO.getKeyword())
                    .or().like(TblAccountingPolicy::getPolicyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblAccountingPolicy::getCreateTime);

        Page<TblAccountingPolicy> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblAccountingPolicy> result = this.page(page, wrapper);

        PageResult<TblAccountingPolicy> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblAccountingPolicy record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblAccountingPolicy record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }
}

