package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.GzctInvestWarning;
import com.huabo.cybermonitor.mapper.GzctInvestWarningMapper;
import com.huabo.cybermonitor.service.IGzctInvestWarningService;
import com.huabo.cybermonitor.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class GzctInvestWarningServiceImpl extends ServiceImpl<GzctInvestWarningMapper, GzctInvestWarning>
        implements IGzctInvestWarningService {

    @Override
    public PageResult<GzctInvestWarning> selectByPage(Map<String, Object> params) {
        int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        LambdaQueryWrapper<GzctInvestWarning> wrapper = new LambdaQueryWrapper<>();
        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) wrapper.eq(GzctInvestWarning::getStatus, status);
        String level = (String) params.get("level");
        if (StringUtils.isNotEmpty(level)) wrapper.eq(GzctInvestWarning::getLevel, level);
        String warningType = (String) params.get("warningType");
        if (StringUtils.isNotEmpty(warningType)) wrapper.eq(GzctInvestWarning::getWarningType, warningType);
        String companyId = (String) params.get("companyId");
        if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctInvestWarning::getCompanyId, companyId);
        wrapper.orderByDesc(GzctInvestWarning::getTriggerTime);
        Page<GzctInvestWarning> page = new Page<>(pageNum, pageSize);
        Page<GzctInvestWarning> result = this.page(page, wrapper);
        PageResult<GzctInvestWarning> pr = new PageResult<>();
        pr.setTotalRecord((int) result.getTotal());
        pr.setCurrentPage((int) result.getCurrent());
        pr.setPageNumber((int) result.getCurrent());
        pr.setTotalPage((int) result.getPages());
        pr.setPageSize((int) result.getSize());
        pr.setTlist(result.getRecords());
        return pr;
    }

    @Override
    public boolean dispatch(String warningId) {
        GzctInvestWarning warning = this.getById(warningId);
        if (warning != null) {
            warning.setStatus("HANDLING");
            warning.setUpdateTime(LocalDateTime.now());
            return this.updateById(warning);
        }
        return false;
    }

    @Override
    public boolean handle(String warningId, String handleResult, String handleUser) {
        GzctInvestWarning warning = this.getById(warningId);
        if (warning != null) {
            warning.setStatus("COMPLETED");
            warning.setHandleResult(handleResult);
            warning.setHandleUser(handleUser);
            warning.setHandleTime(LocalDateTime.now());
            warning.setUpdateTime(LocalDateTime.now());
            return this.updateById(warning);
        }
        return false;
    }
}
