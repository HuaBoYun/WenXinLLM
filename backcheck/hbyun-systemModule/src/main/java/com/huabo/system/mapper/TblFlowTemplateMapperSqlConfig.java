package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblFlowTemplate;

public class TblFlowTemplateMapperSqlConfig {
	
	public String selectListByPageInfo(IPage<TblFlowTemplate> page, TblFlowTemplate temp) throws Exception {
		String sql = "SELECT * FROM TBL_FLOW_TEMPLATE WHERE LINKSTAFF = "+temp.getLinkStaff()+" AND FLOWID = '"+temp.getFlowId()+"'";
		
		/*if(temp.getTaskNodeId() != null && !"".equals(temp.getTaskNodeId())) {
			sql += " AND TASKNODEID = '"+temp.getTaskNodeId()+"'";
		}*/
		
		if(temp.getTempTitle() != null && !"".equals(temp.getTempTitle())) {
			sql += " AND TEMPTITLE LIKE '%"+temp.getTempTitle()+"%'";
		}
		
		if(temp.getTempMemo() != null && !"".equals(temp.getTempMemo())) {
			sql += " AND TEMPMEMO LIKE '%"+temp.getTempMemo()+"%'";
		}
		
		sql += " ORDER BY CREATETIME DESC";
		return sql;
	}
	
}
