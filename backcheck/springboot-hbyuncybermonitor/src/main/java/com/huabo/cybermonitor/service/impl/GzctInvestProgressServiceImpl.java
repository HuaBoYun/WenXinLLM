package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.GzctInvestProgress;
import com.huabo.cybermonitor.mapper.GzctInvestProgressMapper;
import com.huabo.cybermonitor.service.IGzctInvestProgressService;
import com.huabo.cybermonitor.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class GzctInvestProgressServiceImpl extends ServiceImpl<GzctInvestProgressMapper, GzctInvestProgress>
        implements IGzctInvestProgressService {

    @Override
    public PageResult<GzctInvestProgress> selectByPage(Map<String, Object> params) {
        int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        LambdaQueryWrapper<GzctInvestProgress> wrapper = new LambdaQueryWrapper<>();
        String companyId = (String) params.get("companyId");
        if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctInvestProgress::getCompanyId, companyId);
        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) wrapper.eq(GzctInvestProgress::getStatus, status);
        String projectId = (String) params.get("projectId");
        if (StringUtils.isNotEmpty(projectId)) wrapper.eq(GzctInvestProgress::getProjectId, projectId);
        wrapper.orderByDesc(GzctInvestProgress::getCreateTime);
        Page<GzctInvestProgress> page = new Page<>(pageNum, pageSize);
        Page<GzctInvestProgress> result = this.page(page, wrapper);
        PageResult<GzctInvestProgress> pr = new PageResult<>();
        pr.setTotalRecord((int) result.getTotal());
        pr.setCurrentPage((int) result.getCurrent());
        pr.setPageNumber((int) result.getCurrent());
        pr.setTotalPage((int) result.getPages());
        pr.setPageSize((int) result.getSize());
        pr.setTlist(result.getRecords());
        return pr;
    }

    @Override
    public boolean addProgress(GzctInvestProgress progress) {
        progress.setCreateTime(LocalDateTime.now());
        progress.setUpdateTime(LocalDateTime.now());
        return this.save(progress);
    }

    @Override
    public boolean updateProgress(GzctInvestProgress progress) {
        progress.setUpdateTime(LocalDateTime.now());
        return this.updateById(progress);
    }

    @Override
    public boolean deleteProgress(String id) {
        return this.removeById(id);
    }
}
