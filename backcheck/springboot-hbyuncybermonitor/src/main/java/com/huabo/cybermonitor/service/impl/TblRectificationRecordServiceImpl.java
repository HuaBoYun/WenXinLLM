package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblRectificationRecord;
import com.huabo.cybermonitor.mapper.TblRectificationRecordMapper;
import com.huabo.cybermonitor.service.ITblRectificationRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblRectificationRecordQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TblRectificationRecordServiceImpl extends ServiceImpl<TblRectificationRecordMapper, TblRectificationRecord>
        implements ITblRectificationRecordService {

    @Override
    public PageResult<TblRectificationRecord> selectByPage(TblRectificationRecordQueryVO queryVO) {
        LambdaQueryWrapper<TblRectificationRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getTaskId())) {
            wrapper.eq(TblRectificationRecord::getTaskId, queryVO.getTaskId());
        }
        if (StringUtils.isNotEmpty(queryVO.getRectStatus())) {
            wrapper.eq(TblRectificationRecord::getRectStatus, queryVO.getRectStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getResponsibleUser())) {
            wrapper.eq(TblRectificationRecord::getResponsibleUser, queryVO.getResponsibleUser());
        }
        if (StringUtils.isNotEmpty(queryVO.getVerifyResult())) {
            wrapper.eq(TblRectificationRecord::getVerifyResult, queryVO.getVerifyResult());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblRectificationRecord::getRequirement, queryVO.getKeyword())
                    .or().like(TblRectificationRecord::getProgressDesc, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblRectificationRecord::getCreateTime);

        Page<TblRectificationRecord> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblRectificationRecord> result = this.page(page, wrapper);

        PageResult<TblRectificationRecord> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblRectificationRecord record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblRectificationRecord record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }
}

