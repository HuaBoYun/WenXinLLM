package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblFinancingRecord;
import com.huabo.cybermonitor.mapper.TblFinancingRecordMapper;
import com.huabo.cybermonitor.service.ITblFinancingRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblFinancingRecordQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblFinancingRecordServiceImpl extends ServiceImpl<TblFinancingRecordMapper, TblFinancingRecord>
        implements ITblFinancingRecordService {

    @Override
    public PageResult<TblFinancingRecord> selectByPage(TblFinancingRecordQueryVO queryVO) {
        LambdaQueryWrapper<TblFinancingRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblFinancingRecord::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblFinancingRecord::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getFinancingType())) {
            wrapper.eq(TblFinancingRecord::getFinancingType, queryVO.getFinancingType());
        }
        if (StringUtils.isNotEmpty(queryVO.getFinancingStatus())) {
            wrapper.eq(TblFinancingRecord::getFinancingStatus, queryVO.getFinancingStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getRiskLevel())) {
            wrapper.eq(TblFinancingRecord::getRiskLevel, queryVO.getRiskLevel());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblFinancingRecord::getCompanyName, queryVO.getKeyword())
                    .or().like(TblFinancingRecord::getFinancingType, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblFinancingRecord::getCreateTime);

        Page<TblFinancingRecord> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblFinancingRecord> result = this.page(page, wrapper);

        PageResult<TblFinancingRecord> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblFinancingRecord record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblFinancingRecord record) {
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
        LambdaQueryWrapper<TblFinancingRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            wrapper.eq(TblFinancingRecord::getCompanyId, companyId);
        }
        List<TblFinancingRecord> records = this.list(wrapper);
        stats.put("totalCount", records.size());

        // 融资总额
        BigDecimal totalAmount = records.stream()
            .map(r -> r.getFinancingAmount() != null ? r.getFinancingAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalAmount", totalAmount);

        // 平均利率
        double avgRate = records.stream()
            .filter(r -> r.getInterestRate() != null)
            .mapToDouble(r -> r.getInterestRate().doubleValue())
            .average().orElse(0.0);
        stats.put("avgRate", Math.round(avgRate * 100.0) / 100.0);

        // 90天内到期金额
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ninetyDaysLater = now.plusDays(90);
        BigDecimal nearExpiryAmount = records.stream()
            .filter(r -> r.getMaturityDate() != null)
            .filter(r -> {
                LocalDateTime maturity = r.getMaturityDate().atStartOfDay();
                return maturity.isAfter(now) && maturity.isBefore(ninetyDaysLater);
            })
            .map(r -> r.getOutstandingAmount() != null ? r.getOutstandingAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("nearExpiryAmount", nearExpiryAmount);

        // 逾期金额
        BigDecimal overdueAmount = records.stream()
            .filter(r -> "OVERDUE".equals(r.getFinancingStatus()))
            .map(r -> r.getOutstandingAmount() != null ? r.getOutstandingAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("overdueAmount", overdueAmount);

        // 融资类型分布
        Map<String, Long> typeDistribution = new HashMap<>();
        records.stream()
            .filter(r -> r.getFinancingType() != null)
            .forEach(r -> typeDistribution.merge(r.getFinancingType(), 1L, Long::sum));
        stats.put("typeDistribution", typeDistribution);

        // 期限分布 [1年以内, 1-3年, 3-5年, 5年以上] 金额(亿元)
        BigDecimal[] termDist = new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
        for (TblFinancingRecord r : records) {
            if (r.getStartDate() != null && r.getMaturityDate() != null && r.getFinancingAmount() != null) {
                long months = java.time.temporal.ChronoUnit.MONTHS.between(r.getStartDate().atStartOfDay(), r.getMaturityDate().atStartOfDay());
                BigDecimal amt = r.getFinancingAmount().divide(BigDecimal.valueOf(10000), 2, java.math.RoundingMode.HALF_UP);
                if (months <= 12) termDist[0] = termDist[0].add(amt);
                else if (months <= 36) termDist[1] = termDist[1].add(amt);
                else if (months <= 60) termDist[2] = termDist[2].add(amt);
                else termDist[3] = termDist[3].add(amt);
            }
        }
        stats.put("termDistribution", new BigDecimal[]{termDist[0], termDist[1], termDist[2], termDist[3]});

        return stats;
    }
}

