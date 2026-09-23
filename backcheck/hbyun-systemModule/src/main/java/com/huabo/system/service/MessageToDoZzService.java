package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemSheetTable;


public interface MessageToDoZzService {


	public JSONObject returnObject(TblSystemSheetTable sheet,BigDecimal fromId,TblStaffUtil loginStaff,String type,String processId)throws Exception;
	
	public JSONObject returnObjectMessage(TblSystemSheetTable sheet,BigDecimal fromId,TblStaffUtil loginStaff)throws Exception;


}
