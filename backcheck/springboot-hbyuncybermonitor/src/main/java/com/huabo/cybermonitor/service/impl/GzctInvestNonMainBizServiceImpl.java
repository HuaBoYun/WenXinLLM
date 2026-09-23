package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.GzctInvestNonMainBiz;
import com.huabo.cybermonitor.mapper.GzctInvestNonMainBizMapper;
import com.huabo.cybermonitor.service.IGzctInvestNonMainBizService;
import com.huabo.cybermonitor.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GzctInvestNonMainBizServiceImpl extends ServiceImpl<GzctInvestNonMainBizMapper, GzctInvestNonMainBiz>
        implements IGzctInvestNonMainBizService {

    @Override
    public PageResult<GzctInvestNonMainBiz> selectByPage(Map<String, Object> params) {
        int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        LambdaQueryWrapper<GzctInvestNonMainBiz> wrapper = new LambdaQueryWrapper<>();
        String companyId = (String) params.get("companyId");
        if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctInvestNonMainBiz::getCompanyId, companyId);
        wrapper.orderByDesc(GzctInvestNonMainBiz::getCreateTime);
        Page<GzctInvestNonMainBiz> page = new Page<>(pageNum, pageSize);
        Page<GzctInvestNonMainBiz> result = this.page(page, wrapper);
        PageResult<GzctInvestNonMainBiz> pr = new PageResult<>();
        pr.setTotalRecord((int) result.getTotal());
        pr.setCurrentPage((int) result.getCurrent());
        pr.setPageNumber((int) result.getCurrent());
        pr.setTotalPage((int) result.getPages());
        pr.setPageSize((int) result.getSize());
        pr.setTlist(result.getRecords());
        return pr;
    }

    @Override
    public Map<String, Object> getStats(String companyId) {
        Map<String, Object> stats = new HashMap<>();
        LambdaQueryWrapper<GzctInvestNonMainBiz> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctInvestNonMainBiz::getCompanyId, companyId);
        List<GzctInvestNonMainBiz> list = this.list(wrapper);
        stats.put("totalNonMainAmount", list.stream()
                .map(n -> n.getNonMainAmount() != null ? n.getNonMainAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal totalRatio = list.stream()
                .map(n -> n.getNonMainRatio() != null ? n.getNonMainRatio() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("avgNonMainRatio", list.size() > 0
                ? totalRatio.divide(BigDecimal.valueOf(list.size()), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);
        stats.put("alertCount", list.stream()
                .filter(n -> n.getNonMainRatio() != null && n.getNonMainRatio().compareTo(BigDecimal.valueOf(20)) > 0)
                .count());
        List<Map<String, Object>> companyStats = list.stream()
                .collect(Collectors.groupingBy(n -> n.getCompanyName() != null ? n.getCompanyName() : "未知"))
                .entrySet().stream().map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("company", e.getKey());
                    BigDecimal ratio = e.getValue().stream()
                            .map(n -> n.getNonMainRatio() != null ? n.getNonMainRatio() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .divide(BigDecimal.valueOf(e.getValue().size()), 1, RoundingMode.HALF_UP);
                    m.put("ratio", ratio);
                    m.put("nonMainAmount", e.getValue().stream()
                            .map(n -> n.getNonMainAmount() != null ? n.getNonMainAmount() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                    m.put("totalAmount", e.getValue().stream()
                            .map(n -> n.getInvestAmount() != null ? n.getInvestAmount() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                    return m;
                }).collect(Collectors.toList());
        stats.put("companyStats", companyStats);
        return stats;
    }

    @Override
    public List<Map<String, Object>> getTrend(String companyId, Integer year) {
        List<Map<String, Object>> trend = new ArrayList<>();
        // 以选中年份为中心，展示前后共5年的趋势
        int centerYear = year != null ? year : 2024;
        for (int yi = centerYear - 4; yi <= centerYear; yi++) {
            String y = String.valueOf(yi);
            Map<String, Object> item = new HashMap<>();
            item.put("year", y);
            LambdaQueryWrapper<GzctInvestNonMainBiz> yWrapper = new LambdaQueryWrapper<>();
            yWrapper.eq(GzctInvestNonMainBiz::getReportYear, y);
            if (StringUtils.isNotEmpty(companyId)) yWrapper.eq(GzctInvestNonMainBiz::getCompanyId, companyId);
            List<GzctInvestNonMainBiz> list = this.list(yWrapper);
            BigDecimal avgRatio = list.size() > 0
                    ? list.stream()
                        .map(n -> n.getNonMainRatio() != null ? n.getNonMainRatio() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(list.size()), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            item.put("nonMainRatio", avgRatio);
            item.put("nonMainAmount", list.stream()
                    .map(n -> n.getNonMainAmount() != null ? n.getNonMainAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            trend.add(item);
        }
        return trend;
    }

    @Override
    public boolean addRecord(GzctInvestNonMainBiz record) {
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(GzctInvestNonMainBiz record) {
        record.setUpdateTime(LocalDateTime.now());
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(String id) {
        return this.removeById(id);
    }
}
