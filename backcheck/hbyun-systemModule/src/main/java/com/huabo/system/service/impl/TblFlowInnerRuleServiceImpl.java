package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblFlowInnerRule;
import com.huabo.system.entity.TblInnerrule;
import com.huabo.system.entity.TblOuterrule;
import com.huabo.system.mapper.TblFlowInnerRuleMapper;
import com.huabo.system.mapper.TblInnerruleMapper;
import com.huabo.system.mapper.TblOuterruleMapper;
import com.huabo.system.service.TblFlowInnerRuleService;
import com.huabo.system.service.TblOrganizaService;

@Service
public class TblFlowInnerRuleServiceImpl implements TblFlowInnerRuleService {

    @Resource
    private TblFlowInnerRuleMapper tblFlowInnerRuleMapper;
    @Resource
    private TblInnerruleMapper tblInnerruleMapper;
    @Resource
    private TblOuterruleMapper tblOuterruleMapper;
    
    @Resource
    private UserProvider userProvider;
    
    @Resource
    private TblOrganizaService tblOrganizaService;

    @Override
    public void saveTblFlowInnerRule(TblFlowInnerRule inner) {
        tblFlowInnerRuleMapper.insertRule(inner);
    }

    @Override
    public Map<String, Object> findByOrgidAndFlowidobj(String flowid, Integer pageNumber, Integer pageSize, String token) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
                PageInfo<TblInnerrule> pageInfo = new PageInfo<TblInnerrule>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                
                BigDecimal rootOrgId = this.tblOrganizaService.getRootCompanyByOrgId(user.getCurrentOrg().getOrgid());
                String allOrgIds = this.tblOrganizaService.getAllChildrenOrgIdStrs(rootOrgId.toString(),null);
                
                Page<TblInnerrule> page = new Page<TblInnerrule>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblInnerrule> pageList = tblInnerruleMapper.findInnerruleByFolwid(page, allOrgIds, flowid);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int)pageList.getTotal());
                resultMap.put("data", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public List<TblFlowInnerRule> isIfFlowInner(String flowid, String[] innerid) {
        List<TblFlowInnerRule> list = new ArrayList<>();
        for (String i : innerid) {
            TblFlowInnerRule innerRule = this.tblFlowInnerRuleMapper.selectFlowInner(flowid, i);
            list.add(innerRule);
        }
        return list;
    }

    @Override
    public void deleteTblFlowInnerRule(TblFlowInnerRule inner) {
        this.tblFlowInnerRuleMapper.delteTblFlowInnerRule(inner);
    }

    @Override
    public Map<String, Object> findOutRuleByOrgidAndFlowid(String name, String status, String flowid, Integer pageNumber, Integer pageSize, String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            TblStaffUtil user = userProvider.get();
                PageInfo<TblOuterrule> pageInfo = new PageInfo<TblOuterrule>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                
                Page<TblOuterrule> page = new Page<TblOuterrule>(pageNumber,pageSize);
                page.setOptimizeCountSql(false); // 禁用自动优化
                IPage<TblOuterrule> pageList = tblOuterruleMapper.findInnerruleByFolwid(page, user.getCurrentOrg().getOrgid().toString(), name, status, flowid);
                
                pageInfo.setTlist(pageList.getRecords());
                pageInfo.setTotalRecord((int) pageList.getTotal());

                resultMap.put("data", pageInfo);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    @Override
    public void delteTblFlowInnerRule(TblFlowInnerRule inner) {
    	this.tblFlowInnerRuleMapper.delteTblFlowInnerRule(inner);	
    }
}
