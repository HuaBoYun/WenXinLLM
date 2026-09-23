package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjBugEntity;
import com.huabo.audit.oracle.vo.TblNbsjBugVo;
import com.huabo.audit.util.R;
import org.apache.poi.ss.usermodel.Sheet;

public interface TblNbsjBugService {
	public void add(TblNbsjBugEntity tblBug);
    @SuppressWarnings("rawtypes")
	public List findAll(String type);
    public List<TblNbsjBugEntity> findBySql(String sql);
    public void saveInnerId(String bugId, String innerId);
    public List<TblNbsjBugEntity> findByAll(String orgid,String type);

    public void saveOuterId(String outerId, String innerId);

    void delete(String id);

    TblNbsjBugEntity findById(BigDecimal bigDecimal);
    @SuppressWarnings("rawtypes")
	public List findByneed(String orgid,String orgtype);

    @SuppressWarnings("rawtypes")
	public Set getInnerRules();

    @SuppressWarnings("rawtypes")
	public Set getOuterRules();
//    public PageBean findALLHy(TblNbsjBugEntity bug, String startdate, String enddate,Integer startIndex, Integer pageSize,String orgid,String orgtype) ;
    void update(TblNbsjBugEntity tblBug);
    @SuppressWarnings("rawtypes")
	public List findByneed(String number,String fstart,String fend,String uname);
    @SuppressWarnings("rawtypes")
	List search(String plancode, String searchbegintime, String searchendtime, String plantype, String state, String planname);
//    public PageBean findALLZ(TblNbsjBugEntity bug,Integer startIndex, Integer pageSize,String orgid,String orgtype);
    @SuppressWarnings("rawtypes")
	List getAllReform();
//    public PageBean findALL(TblNbsjBugEntity bug,String startdate,String enddate,Integer startIndex, Integer pageSize,String orgid,String  orgtype,String buglevel,String type);

    //导出
    public List<Object[]> qxglExport(String orgid,String type);
    
    public TblNbsjBugEntity findByCode(String code, String type);
    
    public TblNbsjBugEntity findByCode(String code, String type,String orgid);
    /**
     * 使用当前缺陷等级的缺陷
     * @param orgid
     * @param orgtype
     * @return
     */
	public List<TblNbsjBugEntity> findByCriterionId(BigDecimal criterionId);


    /**
     * 缺陷管理-附件列表
     * @param token
     * @param bugId
     * @return
     */
    JsonBean defectFileList(String token,BigDecimal bugId)throws Exception;


	
	//==
	JsonBean bugPageList(String token, Integer pageNumber, Integer pageSize,TblNbsjBugVo tblNbsjBugVo,BigDecimal orgid) throws Exception;
	
	JsonBean bugAdd(TblNbsjBugEntity bug, String token,String attids)throws Exception;
    
    JsonBean bugDelete(BigDecimal bugid, String token) throws Exception;
    
    JsonBean findBugDetail(String token, BigDecimal bugid) throws Exception;

    /**
     * 缺陷管理-导出
     * @param token
     * @param orgId
     * @return
     * @throws Exception
     */
    JsonBean defect_file_export(String token, BigDecimal orgId,HttpServletResponse response) throws Exception;
    
    JsonBean innerCommonQxwtList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid) throws Exception;
    
	JsonBean outerCommonQxwtList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid) throws Exception;
	
	JsonBean bugInnrulidsAdd(String token, BigDecimal bugid, String innrulids) throws Exception;
	
	JsonBean bugOutrulidsAdd(String token, BigDecimal bugid, String outrulids) throws Exception;
	
	JsonBean innerCommonLinkList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid) throws Exception;
    
	JsonBean outerCommonLinkList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid) throws Exception;
	
	JsonBean defectQxwtAdd(String token, BigDecimal bugid, String bugids) throws Exception;
	
	JsonBean defectLinkList(String token, Integer pageNumber, Integer pageSize, BigDecimal bugid) throws Exception;
	
	JsonBean bugInnrulidsDelete(String token, BigDecimal bugid, String innrulids) throws Exception;
	
	JsonBean bugOutrulidsDelete(String token, BigDecimal bugid, String outrulids) throws Exception;
	
	JsonBean bugFatherDelete(String token, BigDecimal bugid, String bugids) throws Exception;
	
	JsonBean findBugCriterion(String token,String bugtype) throws Exception;
	
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;
	
	JsonBean dgbugAdd(TblNbsjBugEntity bug, String token,String attids)throws Exception;
	
	
	List<TblNbsjBugEntity>  exportList(String token,  TblNbsjBugVo tblNbsjBugVo,BigDecimal orgid)
			throws Exception;

	void resolveSheet(Sheet sheet, String token) throws Exception;

}
