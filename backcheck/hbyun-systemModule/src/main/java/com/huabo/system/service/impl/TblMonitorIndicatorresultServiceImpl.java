package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorIndicatorresult;
import com.huabo.system.mapper.TblMonitorIndicatorresultMapper;
import com.huabo.system.service.TblMonitorIndicatorresultService;

@Service
public class TblMonitorIndicatorresultServiceImpl implements TblMonitorIndicatorresultService {

    @Resource
    private TblMonitorIndicatorresultMapper tblMonitorIndicatorresultMapper;

    @Override
    public Map<String, Object> getResultListJKZX(PageInfo<TblMonitorIndicatorresult> pageInfo, BigDecimal indicatorid, BigDecimal solutionresultid) {
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
		 
		 Page<TblMonitorIndicatorresult> page = new Page<TblMonitorIndicatorresult>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		 page.setOptimizeCountSql(false); // 禁用自动优化
		 IPage<TblMonitorIndicatorresult> pageList =  tblMonitorIndicatorresultMapper.getResultListJKZX(page, indicatorid, solutionresultid);
		 
		 pageInfo.setTlist(pageList.getRecords());
		 pageInfo.setTotalRecord((int) pageList.getTotal());
		 resultMap.put("code", "1");
		 resultMap.put("msg", "访问接口成功");
		 resultMap.put("data", pageInfo);
		 return resultMap;
    }
}
