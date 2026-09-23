package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblContractRecord;
import com.huabo.cybermonitor.mapper.TblContractRecordMapper;
import com.huabo.cybermonitor.service.ITblContractRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblContractRecordQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblContractRecordServiceImpl extends ServiceImpl<TblContractRecordMapper, TblContractRecord>
        implements ITblContractRecordService {

    @Override
    public PageResult<TblContractRecord> selectByPage(TblContractRecordQueryVO queryVO) {
        LambdaQueryWrapper<TblContractRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblContractRecord::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblContractRecord::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getContractCode())) {
            wrapper.eq(TblContractRecord::getContractCode, queryVO.getContractCode());
        }
        if (StringUtils.isNotEmpty(queryVO.getContractName())) {
            wrapper.like(TblContractRecord::getContractName, queryVO.getContractName());
        }
        if (StringUtils.isNotEmpty(queryVO.getContractType())) {
            wrapper.eq(TblContractRecord::getContractType, queryVO.getContractType());
        }
        if (StringUtils.isNotEmpty(queryVO.getContractStatus())) {
            wrapper.eq(TblContractRecord::getContractStatus, queryVO.getContractStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getCounterpartyName())) {
            wrapper.like(TblContractRecord::getCounterpartyName, queryVO.getCounterpartyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getDataSource())) {
            wrapper.eq(TblContractRecord::getDataSource, queryVO.getDataSource());
        }
        if (StringUtils.isNotEmpty(queryVO.getIsMajor())) {
            wrapper.eq(TblContractRecord::getIsMajor, queryVO.getIsMajor());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblContractRecord::getContractName, queryVO.getKeyword())
                    .or().like(TblContractRecord::getContractCode, queryVO.getKeyword())
                    .or().like(TblContractRecord::getCompanyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblContractRecord::getCreateTime);

        Page<TblContractRecord> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblContractRecord> result = this.page(page, wrapper);

        PageResult<TblContractRecord> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblContractRecord record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblContractRecord record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }

    @Override
    public Map<String, Object> getStatisticsByOrgPattern(String orgPattern) {
        Map<String, Object> stats = new HashMap<>();
        LambdaQueryWrapper<TblContractRecord> wrapper = new LambdaQueryWrapper<>();
        if (orgPattern != null) {
            wrapper.like(TblContractRecord::getOrgPath, orgPattern);
        }
        List<TblContractRecord> records = this.list(wrapper);
        long totalCount = records.size();
        stats.put("totalCount", totalCount);
        // 前端 STAT_MAP 期望字段
        stats.put("totalContracts", totalCount);
        stats.put("contractCount", totalCount);
        java.math.BigDecimal totalAmount = records.stream()
            .map(r -> r.getContractAmount() != null ? r.getContractAmount() : java.math.BigDecimal.ZERO)
            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        stats.put("totalAmount", totalAmount);
        stats.put("contractAmount", totalAmount);
        long riskCount = records.stream().filter(r -> "OVERDUE".equals(r.getContractStatus()) || "TERMINATED".equals(r.getContractStatus())).count();
        stats.put("riskCount", riskCount);
        stats.put("warningCount", riskCount);
        return stats;
    }

    @Override
    public Map<String, Object> getStatistics(String companyId) {
        Map<String, Object> stats = new HashMap<>();
        LambdaQueryWrapper<TblContractRecord> wrapper = new LambdaQueryWrapper<>();
        if (companyId != null && !companyId.isEmpty()) {
            wrapper.eq(TblContractRecord::getCompanyId, companyId);
        }
        stats.put("totalCount", this.count(wrapper));
        return stats;
    }
}

