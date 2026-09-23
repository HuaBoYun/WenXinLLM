package com.huabo.system.service;


import com.huabo.system.entity.TblYyUserOrder;
import com.hbfk.util.PageInfo;

import java.util.Map;

public interface TblYyUserOrderService {

	Map<String, Object> selectPageInfoList(String token, String staffId, Integer pageNumber, Integer pageSize,TblYyUserOrder yuo);

}
