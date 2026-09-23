package com.huabo.audit.oracle.mapper;

import java.util.Date;

import com.huabo.audit.oracle.entity.TblTemplateDu;

public class TblTemplateDuMapperSqlConfig  {

	public String insertEntity(TblTemplateDu temp) {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_TEMPLATE_UD(tempId,type,title,html,pre");
		StringBuffer valSb = new StringBuffer(" VALUES ( HIBERNATE_SEQUENCE.nextval,'"+temp.getType()+"','"+temp.getTitle()+"','"+temp.getHtml()+"','pre1.png'");
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	

}
