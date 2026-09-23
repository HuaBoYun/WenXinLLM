package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.GzctInvestPostEval;
import com.huabo.cybermonitor.mapper.GzctInvestPostEvalMapper;
import com.huabo.cybermonitor.service.IGzctInvestPostEvalService;
import com.huabo.cybermonitor.util.PageResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class GzctInvestPostEvalServiceImpl extends ServiceImpl<GzctInvestPostEvalMapper, GzctInvestPostEval>
        implements IGzctInvestPostEvalService {

    @Override
    public PageResult<GzctInvestPostEval> selectByPage(Map<String, Object> params) {
        int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        LambdaQueryWrapper<GzctInvestPostEval> wrapper = new LambdaQueryWrapper<>();
        String evalStatus = (String) params.get("evalStatus");
        if (StringUtils.isNotEmpty(evalStatus)) wrapper.eq(GzctInvestPostEval::getEvalStatus, evalStatus);
        String projectName = (String) params.get("projectName");
        if (StringUtils.isNotEmpty(projectName)) wrapper.like(GzctInvestPostEval::getProjectName, projectName);
        wrapper.orderByDesc(GzctInvestPostEval::getCreateTime);
        Page<GzctInvestPostEval> page = new Page<>(pageNum, pageSize);
        Page<GzctInvestPostEval> result = this.page(page, wrapper);
        PageResult<GzctInvestPostEval> pr = new PageResult<>();
        pr.setTotalRecord((int) result.getTotal());
        pr.setCurrentPage((int) result.getCurrent());
        pr.setPageNumber((int) result.getCurrent());
        pr.setTotalPage((int) result.getPages());
        pr.setPageSize((int) result.getSize());
        pr.setTlist(result.getRecords());
        return pr;
    }

    @Override
    public boolean startEval(GzctInvestPostEval eval) {
        eval.setEvalStatus("IN_PROGRESS");
        eval.setCreateTime(LocalDateTime.now());
        eval.setUpdateTime(LocalDateTime.now());
        return this.save(eval);
    }

    @Override
    public boolean updateEval(GzctInvestPostEval eval) {
        eval.setUpdateTime(LocalDateTime.now());
        return this.updateById(eval);
    }

    @Override
    public boolean deleteEval(String evalId) {
        return this.removeById(evalId);
    }
}
