package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;
import com.huabo.audit.oracle.entity.TblProjectDataPreEntity;
import com.huabo.audit.oracle.vo.DataProVo;
import com.huabo.audit.util.R;

public interface TblNbsjProjectDataProService {
	/**
	 * 项目资料查询列表 
	 * @return
	 */
//		public PageBean projectDataList(Integer pageNumber,int pageSize,TblProjectDataPreEntity dataPre,String orgid);
		/**
		 * 添加/修改
		 * @param id
		 */
		public void saveOrUpdate(TblProjectDataPreEntity dataPre);
		/**
		 * 删除
		 * @param id
		 */
		public void proDel(TblProjectDataPreEntity dataPre);
		
		public TblProjectDataPreEntity proDataById(BigDecimal id);
		
		public void addList(List<TblProjectDataPreEntity> tblProjectDataPres,String username,BigDecimal projectid,String projectname,String orgid) ;
		public Integer proDataById(String projectDataPreId);
		public Integer selectProjectNumber(String projectDataPreId,String projectId);
		public void updateTblProjectDataPre(TblProjectDataPreEntity dataPre);
		
		
		//==
		JsonBean dataproPageList(String token, Integer pageNumber, Integer pageSize,DataProVo dataProVo,Integer orgid,Integer projectId) throws Exception;
		
		JsonBean dataproAdd(TblNbsjProjectDataEntity pd, String token,String attids,String mbids,String sjjykids)throws Exception;
	    
	    JsonBean dataproDelete(Integer dataId, String token) throws Exception;
	    
	    JsonBean findDataProDetail(String token, Integer dataId) throws Exception;
	    
	    R removeAttInfoByAttId(String token, String attId) throws Exception;
	    
		JsonBean dataproAddFile(Integer dataId,BigDecimal attids)throws Exception;
		
		JsonBean  issueProject(String token,String staffId,String dataId,String projectId)throws Exception;
		
		public JsonBean savexmfj(String token,Integer dataId) throws Exception ;


}
