package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblOaFlowMessage;

public class TblOaFlowMessageMapperSqlConifg {
	
	public String selectPageInfoList(IPage<TblOaFlowMessage> page, TblOaFlowMessage message) {
		String sql = "SELECT * FROM TBL_OAFLOW_MESSAGE WHERE FLOWMODULE = '"+message.getFlowModule()+"' AND FLOWTYPE LIKE '%"+message.getFlowType()+"%' ";
		
		if(message.getTitle() != null && !"".equals(message.getTitle())) {
			sql += " AND TITLE LIKE '%"+message.getTitle()+"%'";
		}
		sql += " ORDER BY ID DESC ";
		return sql;
	}
	
}
