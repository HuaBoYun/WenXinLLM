package com.huabo.audit.service;

import com.huabo.audit.oracle.entity.TblNbsjQuestionaffirmEntity;

public interface TblNbsjQuestionaffirmService {
	public void saveTblNbsjQuestionaffirm(TblNbsjQuestionaffirmEntity questionaffirm)throws Exception;
	
	public void updateTblNbsjQuestionaffirm(TblNbsjQuestionaffirmEntity questionaffirm);
	
	public void deleteTblNbsjQuestionaffirm(TblNbsjQuestionaffirmEntity questionaffirm);
	
	public void deleteTblNbsjQuestionaffirmBySql(String sheetid,Integer projectid,Integer factid) throws Exception;
	
	/**
	 * @author tyb
	 * 2016-5-16下午3:59:20
	 * 根据审计发现和事实确认书查询
	 */
	public TblNbsjQuestionaffirmEntity finNbsjQuestionaffirmByQuestionidAndFactid(String factid,String questionid);
	public void deleteTblNbsjQuestionaffirm(String questionId,String factid);
}
