package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblSupplier;
import com.huabo.cybermonitor.mapper.TblSupplierMapper;
import com.huabo.cybermonitor.service.ITblSupplierService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblSupplierQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TblSupplierServiceImpl extends ServiceImpl<TblSupplierMapper, TblSupplier>
        implements ITblSupplierService {

    @Override
    public PageResult<TblSupplier> selectByPage(TblSupplierQueryVO queryVO) {
        LambdaQueryWrapper<TblSupplier> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblSupplier::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getSupplierName())) {
            wrapper.like(TblSupplier::getSupplierName, queryVO.getSupplierName());
        }
        if (StringUtils.isNotEmpty(queryVO.getSupplierType())) {
            wrapper.eq(TblSupplier::getSupplierType, queryVO.getSupplierType());
        }
        if (StringUtils.isNotEmpty(queryVO.getQualificationLevel())) {
            wrapper.eq(TblSupplier::getQualificationLevel, queryVO.getQualificationLevel());
        }
        if (StringUtils.isNotEmpty(queryVO.getCooperationStatus())) {
            wrapper.eq(TblSupplier::getCooperationStatus, queryVO.getCooperationStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblSupplier::getSupplierName, queryVO.getKeyword())
                    .or().like(TblSupplier::getSupplierCode, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblSupplier::getCreateTime);

        Page<TblSupplier> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblSupplier> result = this.page(page, wrapper);

        PageResult<TblSupplier> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblSupplier record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblSupplier record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }
}

