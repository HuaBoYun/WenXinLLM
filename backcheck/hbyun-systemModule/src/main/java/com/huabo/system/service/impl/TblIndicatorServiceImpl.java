package com.huabo.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblIndicator;
import com.huabo.system.mapper.TblIndicatorMapper;
import com.huabo.system.service.TblIndicatorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class TblIndicatorServiceImpl implements TblIndicatorService {

    @Resource
    private TblIndicatorMapper tblIndicatorMapper;

    @Override
    public Map<String, Object> findIndicatorByUseridAndSlouid(PageInfo<TblIndicator> pageInfo, BigDecimal staffid) {
    	Map<String, Object> resultMap = new HashMap<>(0);
    	Page<TblIndicator> page = new Page<TblIndicator>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
    	IPage<TblIndicator> pageList = this.tblIndicatorMapper.findIndicatorByUseridAndSlouid(page, staffid);
    	
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public Map<String, Object> findIndicatorByJKZX(String solutionid, PageInfo<TblIndicator> pageInfo) {
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
    	Page<TblIndicator> page = new Page<TblIndicator>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
    	IPage<TblIndicator> pageList = this.tblIndicatorMapper.findIndicatorByJKZX(solutionid, page);
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }

    @Override
    public TblIndicator findOne(String indicatorid) {
        return tblIndicatorMapper.findIndicatorid(indicatorid);
    }
}
