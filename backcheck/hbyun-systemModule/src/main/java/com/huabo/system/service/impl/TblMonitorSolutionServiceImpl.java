package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorSolution;
import com.huabo.system.mapper.TblMonitorSolutionMapper;
import com.huabo.system.service.TblMonitorSolutionService;

@Service
public class TblMonitorSolutionServiceImpl implements TblMonitorSolutionService {

    @Resource
    private TblMonitorSolutionMapper tblMonitorSolutionMapper;

    @Override
    public Map<String, Object> tblMonitorSolutionService(BigDecimal staffid, PageInfo<TblMonitorSolution> pageInfo, String type) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            Page<TblMonitorSolution> page = new Page<TblMonitorSolution>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
            page.setOptimizeCountSql(false); // 禁用自动优化
            IPage<TblMonitorSolution> pageList = this.tblMonitorSolutionMapper.tblMonitorSolutionService(page, staffid, type);
            pageInfo.setTlist(pageList.getRecords());
            pageInfo.setTotalRecord((int)pageList.getTotal());
            resultMap.put("code", 1);
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", pageInfo);
            return resultMap;
    }


    @Override
    public TblMonitorSolution findOne(String solutionid) {
        return tblMonitorSolutionMapper.selectSolutionid(solutionid);
    }


}
