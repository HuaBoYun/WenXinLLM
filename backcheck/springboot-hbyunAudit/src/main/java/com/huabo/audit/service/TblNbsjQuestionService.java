package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.vo.AuditQuestionVo;
import com.huabo.audit.oracle.vo.TblNbsjQuestionVo;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-13
*/
public interface TblNbsjQuestionService  {

	com.github.pagehelper.PageInfo<TblNbsjQuestionEntity> auditQuestionList(AuditQuestionVo auditQuestion) throws Exception;

	TblNbsjQuestionEntity findNbsjQuestionBySheetId(String sheetid);

	List<Object[]> findQuestionObjects(String questions);

//	PageBean findQuestion(String recstatus, String status, String pmids, String businessAffiliation,
//			String riskAttrbution, Integer projectId, Integer pageNumber, int pageSize);

	void delete(String id);

//	PageBean findQuestionByJt(String recstatus, String status, String pmids, String businessAffiliation,
//			String riskAttrbution, String string, TblNbsjProjectEntity project, Integer pageNumber, int pageSize);

	List<TblNbsjQuestionEntity> findQuestion(String factid);

//	PageBean findQuestionByStatus(Integer projectId, String pmids, String riskAttrbution, String businessAffiliation,
//			int i, int pageSize, String factid, String sheetidsstr);
	
	
	JsonBean questionStorePageList(String token, Integer pageNumber, Integer pageSize, TblNbsjQuestionVo tblNbsjQuestionVo, BigDecimal projectid) throws Exception;
	
	 List<TblNbsjSheetEntity>  getQuestionStorePageList(TblStaffUtil loginStaff,TblNbsjQuestionVo tblNbsjQuestionVo,BigDecimal projectid) throws Exception;

	JsonBean questionStorePageAdd(TblNbsjQuestionEntity tnq, String token)throws Exception;
    
    JsonBean questionStorePageDelete(BigDecimal questionid, String token) throws Exception;
    
    JsonBean questionStorePageFQZG(BigDecimal questionid, String token) throws Exception;

	JsonBean confirmationQuestionStorePageList(String token, Integer pageNumber, Integer pageSize,
			TblNbsjQuestionVo tblNbsjQuestionVo, BigDecimal projectid) throws Exception;

	JsonBean confirmationQuestionStoreLink(String token, BigDecimal factid) throws Exception;
	
	JsonBean questionStorePageISfalse(BigDecimal questionid, String token,Integer ifsbg) throws Exception;
	
}
