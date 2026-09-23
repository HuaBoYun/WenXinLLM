package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjQuestionaffirmEntity;

public class TblNbsjQuestionaffirmMapperSqlConfig {
	
	public String insertEntity(TblNbsjQuestionaffirmEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_QUESTIONAFFIRM(AFFIRMID,AFFIRMTIME");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getAffirmtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		
		if(plan.getTblNbsjQuestion().getQuestionId() != null && !"".equals(plan.getTblNbsjQuestion().getQuestionId())) {
			colSb.append(",QUESTIONID");
			valSb.append(",'"+plan.getTblNbsjQuestion().getQuestionId()+"'");
		}
		if(plan.getDescribe() != null && !"".equals(plan.getDescribe())) {
			colSb.append(",DESCRIBE");
			valSb.append(",'"+plan.getDescribe()+"'");
		}
		if(plan.getFactid() != null && !"".equals(plan.getFactid())) {
			colSb.append(",FACTID");
			valSb.append(",'"+plan.getFactid()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}

}
