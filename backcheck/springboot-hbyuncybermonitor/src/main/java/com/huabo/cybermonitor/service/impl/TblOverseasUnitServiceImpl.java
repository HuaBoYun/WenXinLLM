package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblOverseasUnit;
import com.huabo.cybermonitor.mapper.TblOverseasUnitMapper;
import com.huabo.cybermonitor.service.ITblOverseasUnitService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblOverseasUnitQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblOverseasUnitServiceImpl extends ServiceImpl<TblOverseasUnitMapper, TblOverseasUnit>
        implements ITblOverseasUnitService {

    @Override
    public PageResult<TblOverseasUnit> selectByPage(TblOverseasUnitQueryVO queryVO) {
        LambdaQueryWrapper<TblOverseasUnit> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getUnitName())) {
            wrapper.like(TblOverseasUnit::getUnitName, queryVO.getUnitName());
        }
        if (StringUtils.isNotEmpty(queryVO.getCountry())) {
            wrapper.eq(TblOverseasUnit::getCountry, queryVO.getCountry());
        }
        if (StringUtils.isNotEmpty(queryVO.getOperationStatus())) {
            wrapper.eq(TblOverseasUnit::getOperationStatus, queryVO.getOperationStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblOverseasUnit::getUnitName, queryVO.getKeyword())
                    .or().like(TblOverseasUnit::getCountry, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblOverseasUnit::getCreateTime);

        Page<TblOverseasUnit> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblOverseasUnit> result = this.page(page, wrapper);

        PageResult<TblOverseasUnit> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblOverseasUnit record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblOverseasUnit record) {
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
        long totalCount = this.count();
        stats.put("totalCount", totalCount);
        stats.put("total", totalCount);

        // 正常运营的境外单位视为"已完成监管覆盖"
        LambdaQueryWrapper<TblOverseasUnit> normalWrapper = new LambdaQueryWrapper<>();
        normalWrapper.eq(TblOverseasUnit::getOperationStatus, "NORMAL");
        long normalCount = this.count(normalWrapper);
        stats.put("completed", normalCount);

        // 异常状态的境外单位
        LambdaQueryWrapper<TblOverseasUnit> abnormalWrapper = new LambdaQueryWrapper<>();
        abnormalWrapper.eq(TblOverseasUnit::getOperationStatus, "ABNORMAL");
        long abnormalCount = this.count(abnormalWrapper);
        stats.put("abnormal", abnormalCount);

        // 风险单位 - WARNING + ABNORMAL
        LambdaQueryWrapper<TblOverseasUnit> riskWrapper = new LambdaQueryWrapper<>();
        riskWrapper.in(TblOverseasUnit::getOperationStatus, "WARNING", "ABNORMAL");
        long riskCount = this.count(riskWrapper);
        stats.put("riskCount", riskCount);

        return stats;
    }
}

