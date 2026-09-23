package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblHomePageModel;

public interface TblHomePageModelService {
	
    Map<String,Object> homePageModels(PageInfo<TblHomePageModel> pageInfo, BigDecimal staffid);
}
