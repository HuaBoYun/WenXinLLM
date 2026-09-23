package com.huabo.audit.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huabo.audit.oracle.entity.TblNbsjQuestionaffirmEntity;
import com.huabo.audit.oracle.mapper.TblNbsjQuestionaffirmMapper;
import com.huabo.audit.service.TblNbsjQuestionaffirmService;
@Service
public class TblNbsjQuestionaffirmServiceImpl implements TblNbsjQuestionaffirmService {
	
	@Autowired
    private TblNbsjQuestionaffirmMapper tblNbsjQuestionaffirmMapper;
	
	
	@Override
	public void saveTblNbsjQuestionaffirm(TblNbsjQuestionaffirmEntity questionaffirm) throws Exception {
		
		questionaffirm.setAffirmtime(new Date());
		tblNbsjQuestionaffirmMapper.insertEntity(questionaffirm);

	}

	@Override
	public void updateTblNbsjQuestionaffirm(TblNbsjQuestionaffirmEntity questionaffirm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTblNbsjQuestionaffirm(TblNbsjQuestionaffirmEntity questionaffirm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTblNbsjQuestionaffirmBySql(String sheetid, Integer projectid, Integer factid) throws Exception {
		tblNbsjQuestionaffirmMapper.deleteTblNbsjQuestionaffirmBySql( sheetid, projectid, factid);

	}

	@Override
	public TblNbsjQuestionaffirmEntity finNbsjQuestionaffirmByQuestionidAndFactid(String factid, String questionid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTblNbsjQuestionaffirm(String questionId, String factid) {
		// TODO Auto-generated method stub

	}

}
