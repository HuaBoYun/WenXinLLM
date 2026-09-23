package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblHomePageModel;
import com.huabo.system.mapper.TblHomePageModelMapper;
import com.huabo.system.service.TblHomePageModelService;

@Service
public class TblHomePageModelServiceImpl implements TblHomePageModelService {

    @Resource
    private TblHomePageModelMapper tblHomePageModelMapper;

    @Override
    public Map<String, Object> homePageModels(PageInfo<TblHomePageModel> pageInfo, BigDecimal staffid) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		Page<TblHomePageModel> page = new Page<TblHomePageModel>(pageInfo.getCurrentPage(),pageInfo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblHomePageModel> pageList = this.tblHomePageModelMapper.homePageModels(page, staffid);
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
		resultMap.put("code", "1");
		resultMap.put("msg", "访问接口成功");
		resultMap.put("data", pageInfo);
		return resultMap;
    }

}
