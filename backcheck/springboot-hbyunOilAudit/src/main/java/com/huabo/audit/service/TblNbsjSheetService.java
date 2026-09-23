package com.huabo.audit.service;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.dto.TblNbsjSheetDto;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.util.R;

/**
* 描述: Service
* @author: ziyao
* @date: 2022-04-20
*/
public interface TblNbsjSheetService extends IService<TblNbsjSheetEntity> {
    /**
     * 提交审批
     * @param token
     * @param sheetid
     * @return
     * @throws Exception
     */
	JsonBean submitArrpoval(String token, Integer sheetid,String examination) throws Exception ;
	/**
	 * 查看审批页面
	 * @param token
	 * @param sheetid
	 * @param taskId
	 * @param cyId
	 * @return
	 * @throws Exception
	 */
	JsonBean getApprovalInfo(String token, Integer sheetid,Integer cyId) throws Exception;
	/**
	 * 办理
	 * @param token
	 * @param cyId
	 * @param taskId
	 * @param transition
	 * @param optDesc
	 * @param sheetid
	 * @return
	 * @throws Exception
	 */
	JsonBean dealApporval(String token, Integer cyId, String taskId, String transition, String optDesc,
			String sheetid,String processDefinitionId , String processInstanceId) throws Exception;
	
	
	List<TblNbsjSheetEntity>  getReportSheet(String token)throws Exception;
//    /**
//    * 条件查询 封装QueryWrapper
//    * @param model
//    * @return
//    */
//    LambdaQueryWrapper<TblNbsjSheetEntity> onSelectWhere(TblNbsjSheetEntity model);
//
//    /**
//    *  封装保存方法
//    * @param model
//    * @return
//    */
//    boolean saveTblNbsjSheet(TblNbsjSheetEntity model);
//
//    /**
//    *  封装更新方法
//    * @param model
//    * @return
//    */
//    boolean updateTblNbsjSheet(TblNbsjSheetEntity model);
//
    List<TblNbsjSheetEntity> selectByProjectId(Integer projectId);
//
////	PageBean findByProjectIdAndUserId(TblNbsjSheetEntity sheet, Integer projectId, BigDecimal staffid,
////			Integer pageNumber, int pageSize);
////
////	PageBean findByProjectIdAndPmUserId(TblNbsjSheetEntity sheet, Integer projectId, Integer pageNumber, int pageSize);
////
////	PageBean findByOrgId(BigDecimal bigDecimal, Integer pageNumber, int pageSize, String sheetCode, String sheetName,
////			Integer projectId);
////
////	PageBean findByOrgIdBysql(BigDecimal bigDecimal, Integer pageNumber, int pageSize, String sheetCode,
////			String sheetName);
//
//	List<Object[]> OBJfindAllByProjectid(String string, BigDecimal staffid);
//
//	void merge(TblNbsjSheetEntity blNbsjSheet);
//
//	boolean getSheetByCode(Integer projectId, String sheetCode);
//
//	Integer findByCount(String targetId);
//
////	PageBean findDgByProjectIdAndStatus(Integer projectId, Integer pageNumber, int pageSize);
////
////	PageBean findAllSheetByOrgid(BigDecimal orgid, Integer pageNumber, int pageSize, TblNbsjSheetEntity sheet);
//
//	List<TblNbsjSheetEntity> findDgByQues(Integer projectId);
//
////	PageBean findNbsjSheetByFactBookId(String string, String string2, int i, int j);
//
//	List<TblNbsjSheetEntity> findDgByProjectId(Integer projectId, Object object);
	
	
	JsonBean projectStandardDgPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;
	
	JsonBean projectStandardDgAdd(TblNbsjSheetEntity sheet, String token,String attids,String srJson,String factIds,String bugIds)throws Exception;
    
    JsonBean projectStandardDgDelete(Integer sheetid, String token) throws Exception;
    
    JsonBean dgAllPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;
    
    JsonBean dgglPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;
    
    JsonBean findNbsjSheetDetail(String token, Integer sheetid) throws Exception;

    
    
    List<TblNbsjSheetEntity> OBJfindAllByProjectid(Integer projectid,String token) throws Exception;
    
	JsonBean sheetReportDel(String token, Integer reportid) throws Exception;
    
    
	R removeAttInfoByAttId(String token, String attId) throws Exception;

    JsonBean chooseProjectSheet(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;

    JsonBean ifPmOrLeader(String token) throws Exception;

    /*
     * 我的底稿修改及详情页面--关联缺陷列表
     * */
    JsonBean getNbsjBugList(String token, Integer sheetid) throws Exception;
    
    JsonBean dgglPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo,Integer operateid) throws Exception;
    
    JsonBean dgAllPageList(String token) throws Exception;
    
    public List<TblNbsjSheetEntity> OBJfindAllByProjectidstaff(Integer projectid, String token) throws Exception;
    
    
    public JsonBean findbywtsl(String token,String type,String projectid) throws Exception;
}
