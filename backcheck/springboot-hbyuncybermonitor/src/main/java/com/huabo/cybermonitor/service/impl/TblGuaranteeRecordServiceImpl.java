package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblGuaranteeRecord;
import com.huabo.cybermonitor.mapper.TblGuaranteeRecordMapper;
import com.huabo.cybermonitor.service.ITblGuaranteeRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblGuaranteeRecordQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TblGuaranteeRecordServiceImpl extends ServiceImpl<TblGuaranteeRecordMapper, TblGuaranteeRecord>
        implements ITblGuaranteeRecordService {

    @Override
    public PageResult<TblGuaranteeRecord> selectByPage(TblGuaranteeRecordQueryVO queryVO) {
        LambdaQueryWrapper<TblGuaranteeRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblGuaranteeRecord::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblGuaranteeRecord::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getGuaranteedName())) {
            wrapper.like(TblGuaranteeRecord::getGuaranteedName, queryVO.getGuaranteedName());
        }
        if (StringUtils.isNotEmpty(queryVO.getGuaranteeType())) {
            wrapper.eq(TblGuaranteeRecord::getGuaranteeType, queryVO.getGuaranteeType());
        }
        if (StringUtils.isNotEmpty(queryVO.getGuaranteeStatus())) {
            wrapper.eq(TblGuaranteeRecord::getGuaranteeStatus, queryVO.getGuaranteeStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getRiskLevel())) {
            wrapper.eq(TblGuaranteeRecord::getRiskLevel, queryVO.getRiskLevel());
        }
        if (StringUtils.isNotEmpty(queryVO.getApprovalStatus())) {
            wrapper.eq(TblGuaranteeRecord::getApprovalStatus, queryVO.getApprovalStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblGuaranteeRecord::getCompanyName, queryVO.getKeyword())
                    .or().like(TblGuaranteeRecord::getGuaranteeName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblGuaranteeRecord::getCreateTime);

        Page<TblGuaranteeRecord> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblGuaranteeRecord> result = this.page(page, wrapper);

        PageResult<TblGuaranteeRecord> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblGuaranteeRecord record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblGuaranteeRecord record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }
}

