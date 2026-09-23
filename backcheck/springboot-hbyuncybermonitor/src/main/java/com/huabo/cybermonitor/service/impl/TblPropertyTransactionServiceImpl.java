package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblPropertyTransaction;
import com.huabo.cybermonitor.mapper.TblPropertyTransactionMapper;
import com.huabo.cybermonitor.service.ITblPropertyTransactionService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblPropertyTransactionQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TblPropertyTransactionServiceImpl extends ServiceImpl<TblPropertyTransactionMapper, TblPropertyTransaction>
        implements ITblPropertyTransactionService {

    @Override
    public PageResult<TblPropertyTransaction> selectByPage(TblPropertyTransactionQueryVO queryVO) {
        LambdaQueryWrapper<TblPropertyTransaction> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getPropertyId())) {
            wrapper.eq(TblPropertyTransaction::getPropertyId, queryVO.getPropertyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getTransactionType())) {
            wrapper.eq(TblPropertyTransaction::getTransactionType, queryVO.getTransactionType());
        }
        if (StringUtils.isNotEmpty(queryVO.getTransactionMethod())) {
            wrapper.eq(TblPropertyTransaction::getTransactionMethod, queryVO.getTransactionMethod());
        }
        if (StringUtils.isNotEmpty(queryVO.getApprovalStatus())) {
            wrapper.eq(TblPropertyTransaction::getApprovalStatus, queryVO.getApprovalStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblPropertyTransaction::getTransactionType, queryVO.getKeyword())
                    .or().like(TblPropertyTransaction::getTransactionMethod, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblPropertyTransaction::getCreateTime);

        Page<TblPropertyTransaction> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblPropertyTransaction> result = this.page(page, wrapper);

        PageResult<TblPropertyTransaction> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblPropertyTransaction record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblPropertyTransaction record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }
}

