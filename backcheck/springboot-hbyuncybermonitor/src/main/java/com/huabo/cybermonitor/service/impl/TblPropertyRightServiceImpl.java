package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblPropertyRight;
import com.huabo.cybermonitor.mapper.TblPropertyRightMapper;
import com.huabo.cybermonitor.service.ITblPropertyRightService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblPropertyRightQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblPropertyRightServiceImpl extends ServiceImpl<TblPropertyRightMapper, TblPropertyRight>
        implements ITblPropertyRightService {

    @Override
    public PageResult<TblPropertyRight> selectByPage(TblPropertyRightQueryVO queryVO) {
        LambdaQueryWrapper<TblPropertyRight> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblPropertyRight::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblPropertyRight::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getParentCompanyId())) {
            wrapper.eq(TblPropertyRight::getParentCompanyId, queryVO.getParentCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getParentCompanyName())) {
            wrapper.like(TblPropertyRight::getParentCompanyName, queryVO.getParentCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getRightType())) {
            wrapper.eq(TblPropertyRight::getRightType, queryVO.getRightType());
        }
        if (StringUtils.isNotEmpty(queryVO.getRegistrationStatus())) {
            wrapper.eq(TblPropertyRight::getRegistrationStatus, queryVO.getRegistrationStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getPropertyStatus())) {
            wrapper.eq(TblPropertyRight::getPropertyStatus, queryVO.getPropertyStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getBusinessStatus())) {
            wrapper.eq(TblPropertyRight::getBusinessStatus, queryVO.getBusinessStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getRegistrationConsistency())) {
            wrapper.eq(TblPropertyRight::getRegistrationConsistency, queryVO.getRegistrationConsistency());
        }
        if (StringUtils.isNotEmpty(queryVO.getIsConsolidated())) {
            wrapper.eq(TblPropertyRight::getIsConsolidated, queryVO.getIsConsolidated());
        }
        if (StringUtils.isNotEmpty(queryVO.getIndustry())) {
            wrapper.eq(TblPropertyRight::getIndustry, queryVO.getIndustry());
        }
        if (StringUtils.isNotEmpty(queryVO.getRegion())) {
            wrapper.eq(TblPropertyRight::getRegion, queryVO.getRegion());
        }
        if (queryVO.getEquityLevel() != null) {
            wrapper.eq(TblPropertyRight::getEquityLevel, queryVO.getEquityLevel());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblPropertyRight::getCompanyName, queryVO.getKeyword())
                    .or().like(TblPropertyRight::getIndustry, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblPropertyRight::getCreateTime);

        Page<TblPropertyRight> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblPropertyRight> result = this.page(page, wrapper);

        PageResult<TblPropertyRight> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblPropertyRight record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblPropertyRight record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }

    @Override
    public Map<String, Object> getStatistics(String companyId, String companyName) {
        Map<String, Object> stats = new HashMap<>();
        // 抽离公共过滤条件构造，避免每个 wrapper 重复 5 行 if
        java.util.function.Supplier<LambdaQueryWrapper<TblPropertyRight>> baseWrapperFactory = () -> {
            LambdaQueryWrapper<TblPropertyRight> w = new LambdaQueryWrapper<>();
            // companyId 优先，companyName 作 fallback；两者可同时存在
            if (StringUtils.isNotEmpty(companyId)) {
                w.eq(TblPropertyRight::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(companyName)) {
                w.eq(TblPropertyRight::getCompanyName, companyName);
            }
            return w;
        };

        long totalCount = this.count(baseWrapperFactory.get());
        stats.put("totalCount", totalCount);
        stats.put("total", totalCount);

        // 正常经营数量
        long normalCount = this.count(baseWrapperFactory.get().eq(TblPropertyRight::getBusinessStatus, "NORMAL"));
        stats.put("normalCount", normalCount);
        stats.put("completed", normalCount);

        // 亏损企业数量
        long lossCount = this.count(baseWrapperFactory.get().eq(TblPropertyRight::getBusinessStatus, "LOSS"));
        stats.put("lossCount", lossCount);

        // 清算/注销数量
        long cancelledCount = this.count(baseWrapperFactory.get().in(TblPropertyRight::getBusinessStatus, "CANCELLED", "LIQUIDATION"));
        stats.put("cancelledCount", cancelledCount);

        // 工商比对差异数量
        long differentCount = this.count(baseWrapperFactory.get().eq(TblPropertyRight::getRegistrationConsistency, "DIFFERENT"));
        stats.put("differentCount", differentCount);
        stats.put("abnormal", differentCount);

        // 风险数 = 亏损 + 清算/注销
        stats.put("riskCount", lossCount + cancelledCount);

        return stats;
    }
}

