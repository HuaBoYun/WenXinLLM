package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.TblProcurementProject;
import com.huabo.cybermonitor.mapper.TblProcurementProjectMapper;
import com.huabo.cybermonitor.service.ITblProcurementProjectService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.TblProcurementProjectQueryVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblProcurementProjectServiceImpl extends ServiceImpl<TblProcurementProjectMapper, TblProcurementProject>
        implements ITblProcurementProjectService {

    @Override
    public PageResult<TblProcurementProject> selectByPage(TblProcurementProjectQueryVO queryVO) {
        LambdaQueryWrapper<TblProcurementProject> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(queryVO.getCompanyId())) {
            wrapper.eq(TblProcurementProject::getCompanyId, queryVO.getCompanyId());
        }
        if (StringUtils.isNotEmpty(queryVO.getCompanyName())) {
            wrapper.like(TblProcurementProject::getCompanyName, queryVO.getCompanyName());
        }
        if (StringUtils.isNotEmpty(queryVO.getProjectCode())) {
            wrapper.eq(TblProcurementProject::getProjectCode, queryVO.getProjectCode());
        }
        if (StringUtils.isNotEmpty(queryVO.getProcurementType())) {
            wrapper.eq(TblProcurementProject::getProcurementType, queryVO.getProcurementType());
        }
        if (StringUtils.isNotEmpty(queryVO.getProcurementMethod())) {
            wrapper.eq(TblProcurementProject::getProcurementMethod, queryVO.getProcurementMethod());
        }
        if (StringUtils.isNotEmpty(queryVO.getProjectStatus())) {
            wrapper.eq(TblProcurementProject::getProjectStatus, queryVO.getProjectStatus());
        }
        if (StringUtils.isNotEmpty(queryVO.getKeyword())) {
            wrapper.and(w -> w.like(TblProcurementProject::getProjectName, queryVO.getKeyword())
                    .or().like(TblProcurementProject::getProjectCode, queryVO.getKeyword())
                    .or().like(TblProcurementProject::getCompanyName, queryVO.getKeyword()));
        }
        wrapper.orderByDesc(TblProcurementProject::getCreateTime);

        Page<TblProcurementProject> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        Page<TblProcurementProject> result = this.page(page, wrapper);

        PageResult<TblProcurementProject> pageResult = new PageResult<>();
        pageResult.setTotalRecord((int) result.getTotal());
        pageResult.setCurrentPage((int) result.getCurrent());
        pageResult.setPageNumber((int) result.getCurrent());
        pageResult.setTotalPage((int) result.getPages());
        pageResult.setPageSize((int) result.getSize());
        pageResult.setTlist(result.getRecords());
        return pageResult;
    }

    @Override
    public boolean addRecord(TblProcurementProject record) {
        record.setCreateTime(LocalDateTime.now());
        return this.save(record);
    }

    @Override
    public boolean updateRecord(TblProcurementProject record) {
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
        LambdaQueryWrapper<TblProcurementProject> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            wrapper.eq(TblProcurementProject::getCompanyId, companyId);
        }
        long totalCount = this.count(wrapper);
        stats.put("totalCount", totalCount);
        stats.put("total", totalCount);

        // 已完成采购项目
        LambdaQueryWrapper<TblProcurementProject> completedWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            completedWrapper.eq(TblProcurementProject::getCompanyId, companyId);
        }
        completedWrapper.eq(TblProcurementProject::getProjectStatus, "COMPLETED");
        long completedCount = this.count(completedWrapper);
        stats.put("completed", completedCount);

        // 异常项目 - 延期交付的项目
        LambdaQueryWrapper<TblProcurementProject> abnormalWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            abnormalWrapper.eq(TblProcurementProject::getCompanyId, companyId);
        }
        abnormalWrapper.isNotNull(TblProcurementProject::getDeliveryDate);
        abnormalWrapper.apply("ACTUAL_DELIVERY > DELIVERY_DATE");
        long abnormalCount = 0;
        try { abnormalCount = this.count(abnormalWrapper); } catch (Exception e) { /* 容错 */ }
        stats.put("abnormal", abnormalCount);

        // 风险项目 - 关联方交易
        LambdaQueryWrapper<TblProcurementProject> riskWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(companyId)) {
            riskWrapper.eq(TblProcurementProject::getCompanyId, companyId);
        }
        riskWrapper.eq(TblProcurementProject::getIsRelatedParty, "Y");
        long riskCount = 0;
        try { riskCount = this.count(riskWrapper); } catch (Exception e) { /* 容错 */ }
        stats.put("riskCount", riskCount);

        return stats;
    }
}

