package com.huabo.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblCirculation;
import com.huabo.system.mapper.TblCirculationMapper;
import com.huabo.system.service.TblCirculationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TblCirculationServiceImpl implements TblCirculationService {

    @Resource
    private TblCirculationMapper tblCirculationMapper;

    @Override
    public Map<String, Object> findAll(BigDecimal staffid, TblCirculation tca, PageInfo<TblCirculation> pageInfo) {
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
    	Page<TblCirculation> page = new Page<TblCirculation>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
        page.setOptimizeCountSql(false); // 禁用自动优化
    	IPage<TblCirculation> pageList = tblCirculationMapper.findAll(page, staffid, tca);
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int) pageList.getTotal());
        resultMap.put("data", pageInfo);
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        return resultMap;
    }

    @Override
    public TblCirculation get(String cyid) {
        return tblCirculationMapper.selectByCyid(cyid);
    }

    @Override
    public List<TblCirculation> findStaffid(String taskid) {
        return tblCirculationMapper.findStaffid(taskid);

    }

    @Override
    public List<TblCirculation> findAllStaffid(String taskid) {
        return tblCirculationMapper.findAllStaffid(taskid);
    }

    @Override
    public List<TblCirculation> findAllString(String s) {
        return tblCirculationMapper.findAllString(s);
    }

    @Override
    public TblCirculation saveTblCirculation(String type, String number, String name, String url,
                                             BigDecimal loginUser) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delTblCirculation(TblCirculation c) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(TblCirculation c) {
        // TODO Auto-generated method stub

    }
}
