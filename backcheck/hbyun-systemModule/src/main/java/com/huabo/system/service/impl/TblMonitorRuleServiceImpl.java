package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorRule;
import com.huabo.system.mapper.TblMonitorRuleMapper;
import com.huabo.system.service.TblMonitorRuleService;

@Service
public class TblMonitorRuleServiceImpl implements TblMonitorRuleService{

    @Resource
    private TblMonitorRuleMapper tblMonitorRuleMapper;

    @Override
    public Map<String, Object> findTblMonitorRuleByUser(BigDecimal staffid, PageInfo<TblMonitorRule> pageInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Page<TblMonitorRule> page = new Page<TblMonitorRule>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblMonitorRule> pageList = this.tblMonitorRuleMapper.findTblMonitorRuleByUser(page, staffid);
        
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public TblMonitorRule findOne(BigDecimal ruleid) {
        return tblMonitorRuleMapper.findRuleid(ruleid);
    }

    @Override
    public void findAll(String solutionid, PageInfo<TblMonitorRule> pageInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Page<TblMonitorRule> page = new Page<TblMonitorRule>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblMonitorRule> pageList = this.tblMonitorRuleMapper.findAll(solutionid, page);
        
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
    }

}
