package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblAssessTarget;
import com.huabo.system.mapper.TblAssessTargetMapper;
import com.huabo.system.service.TblAssessTargetService;

@Service
public class TblAssessTargetServiceImpl implements TblAssessTargetService {

    @Resource
    private TblAssessTargetMapper tblAssessTargetMapper;

    @Override
    public void MyMark(BigDecimal staffid, String assid, String assName, PageInfo<TblAssessTarget> pageInfo) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        
        Page<TblAssessTarget> page = new Page<TblAssessTarget>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
        IPage<TblAssessTarget> pageList = tblAssessTargetMapper.MyMark(staffid, assid, assName, page);
        
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
        resultMap.put("data", pageInfo);
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
    }

}
