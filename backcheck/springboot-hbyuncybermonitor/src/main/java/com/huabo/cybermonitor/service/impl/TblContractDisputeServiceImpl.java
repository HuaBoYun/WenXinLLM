package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblContractDispute;
import com.huabo.cybermonitor.mapper.TblContractDisputeMapper;
import com.huabo.cybermonitor.service.ITblContractDisputeService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblContractDisputeQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TblContractDisputeServiceImpl extends ServiceImpl<TblContractDisputeMapper, TblContractDispute>
        implements ITblContractDisputeService {

    @Override
    public PageResult<TblContractDispute> selectByPage(TblContractDisputeQueryVO queryVO) {
        LambdaQueryWrapper<TblContractDispute> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getContractId())) {
            wrapper.eq(TblContractDispute::getContractId, queryVO.getContractId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblContractDispute::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getDisputeType())) {
            wrapper.eq(TblContractDispute::getDisputeType, queryVO.getDisputeType());
        }
        if (StringUtils.isNotEmpty(queryVO.getCaseStatus())) {
            wrapper.eq(TblContractDispute::getCaseStatus, queryVO.getCaseStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getCounterpartyName())) {
            wrapper.like(TblContractDispute::getCounterpartyName, queryVO.getCounterpartyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblContractDispute::getCounterpartyName, queryVO.getKeyword())
                    .or().like(TblContractDispute::getDisputeReason, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblContractDispute::getCreateTime);

        Page<TblContractDispute> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblContractDispute> result = this.page(page, wrapper);

        PageResult<TblContractDispute> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblContractDispute record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblContractDispute record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }
}

