package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblFinanceStatement;
import com.huabo.cybermonitor.mapper.TblFinanceStatementMapper;
import com.huabo.cybermonitor.service.ITblFinanceStatementService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblFinanceStatementQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblFinanceStatementServiceImpl extends ServiceImpl<TblFinanceStatementMapper, TblFinanceStatement>
        implements ITblFinanceStatementService {

    @Override
    public PageResult<TblFinanceStatement> selectByPage(TblFinanceStatementQueryVO queryVO) {
        LambdaQueryWrapper<TblFinanceStatement> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblFinanceStatement::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblFinanceStatement::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getReportPeriod())) {
            wrapper.eq(TblFinanceStatement::getReportPeriod, queryVO.getReportPeriod());
        }
        if (StringUtils.isNotEmpty(queryVO.getReportType())) {
            wrapper.eq(TblFinanceStatement::getReportType, queryVO.getReportType());
        }
        if (StringUtils.isNotEmpty(queryVO.getIsConsolidated())) {
            wrapper.eq(TblFinanceStatement::getIsConsolidated, queryVO.getIsConsolidated());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblFinanceStatement::getCompanyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblFinanceStatement::getCreateTime);

        Page<TblFinanceStatement> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblFinanceStatement> result = this.page(page, wrapper);

        PageResult<TblFinanceStatement> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblFinanceStatement record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblFinanceStatement record) {
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
        LambdaQueryWrapper<TblFinanceStatement> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            wrapper.eq(TblFinanceStatement::getCompanyId, companyId);
        }
        stats.put("totalCount", this.count(wrapper));
        return stats;
    }
}

