package com.huabo.system.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorModel;
import com.huabo.system.mapper.TblMonitorModelMapper;
import com.huabo.system.service.TblMonitorModelService;

@Service
public class TblMonitorModelServiceImpl implements TblMonitorModelService {

    @Resource
    private TblMonitorModelMapper tblMonitorModelMapper;

    @Override
    public Map<String, Object> findByModelJKZX(String solutionid, PageInfo<TblMonitorModel> pageInfo) {
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
    	Page<TblMonitorModel> page = new Page<TblMonitorModel>();
        page.setOptimizeCountSql(false); // 禁用自动优化
    	IPage<TblMonitorModel> pageList = this.tblMonitorModelMapper.findByModelJKZX(solutionid, page);
        pageInfo.setTlist(pageList.getRecords());
        pageInfo.setTotalRecord((int)pageList.getTotal());
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }
}
