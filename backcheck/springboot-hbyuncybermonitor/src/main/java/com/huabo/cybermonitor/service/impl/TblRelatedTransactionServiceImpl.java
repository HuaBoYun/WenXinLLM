package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblRelatedTransaction;
import com.huabo.cybermonitor.mapper.TblRelatedTransactionMapper;
import com.huabo.cybermonitor.service.ITblRelatedTransactionService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblRelatedTransactionQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TblRelatedTransactionServiceImpl extends ServiceImpl<TblRelatedTransactionMapper, TblRelatedTransaction>
        implements ITblRelatedTransactionService {

    @Override
    public PageResult<TblRelatedTransaction> selectByPage(TblRelatedTransactionQueryVO queryVO) {
        LambdaQueryWrapper<TblRelatedTransaction> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblRelatedTransaction::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblRelatedTransaction::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getCounterpartyName())) {
            wrapper.like(TblRelatedTransaction::getCounterpartyName, queryVO.getCounterpartyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getRelationType())) {
            wrapper.eq(TblRelatedTransaction::getRelationType, queryVO.getRelationType());
        }
        if (StringUtils.isNotEmpty(queryVO.getTransactionType())) {
            wrapper.eq(TblRelatedTransaction::getTransactionType, queryVO.getTransactionType());
        }
        if (StringUtils.isNotEmpty(queryVO.getApprovalStatus())) {
            wrapper.eq(TblRelatedTransaction::getApprovalStatus, queryVO.getApprovalStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getReportPeriod())) {
            wrapper.eq(TblRelatedTransaction::getReportPeriod, queryVO.getReportPeriod());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblRelatedTransaction::getCompanyName, queryVO.getKeyword())
                    .or().like(TblRelatedTransaction::getCounterpartyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblRelatedTransaction::getCreateTime);

        Page<TblRelatedTransaction> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblRelatedTransaction> result = this.page(page, wrapper);

        PageResult<TblRelatedTransaction> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblRelatedTransaction record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblRelatedTransaction record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }
}

