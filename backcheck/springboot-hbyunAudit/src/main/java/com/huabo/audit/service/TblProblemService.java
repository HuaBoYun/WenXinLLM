package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.entity.TblProblemEntity;

public interface TblProblemService {
	public void add(TblProblemEntity tblProblem) ;

    @SuppressWarnings("rawtypes")
	public List findAll();

    public TblProblemEntity findById(BigDecimal id);

    public void delete(BigDecimal id);

    public void modify(TblProblemEntity tblProblem);

    @SuppressWarnings("rawtypes")
	List search(String problemname, String businessname, String finder_disp1, String startdt, String enddt, String finder_disp2, String ocurrdt_min, String ocurrdt_max, String sort, String sort_type);

    @SuppressWarnings("rawtypes")
	List search(String problemnum, String proborgs, String problemname, String bussinessbelongto, String riskbelongsto, String probfrom, String discoveryperson, String personincharge, String sort, String sort_type, String flag);
    @SuppressWarnings("rawtypes")
	public List findByneed(String orgid,String orgtype);
    @SuppressWarnings("rawtypes")
	public List getAllReform();
//    public PageBean getAllReformPro(TblProblemEntity pro,Integer startIndex, Integer pageSize,String orgid,String orgtype);
//    public PageBean getAllReformZGGZ(TblProblemEntity pro,Integer startIndex, Integer pageSize);
    @SuppressWarnings("rawtypes")
	public List findByneed(String number,String fstart,String fend,String uname);
    
    public List<TblProblemEntity> findBysql(String sql);
//    public PageBean getAllReformGZ(TblProblemEntity pro,Integer startIndex, Integer pageSize,String uName);
//    public PageBean getAllReformGZCX(TblProblemEntity pro,Integer startIndex, Integer pageSize,String uName,String createid);
//    public PageBean findByPageBeanhy(TblProblemEntity pro, String fsstartDate,String fsendDate, String fxstartDate, String fxendDate,Integer startIndex, Integer pageSize,String orgid,String orgtype);
//    
//    public PageBean findByPageBean(TblProblemEntity pro,String fsstartDate,String fsendDate,String fxstartDate,String fxendDate,Integer startIndex,Integer pageSize,String orgid,String orgtype);
//    public PageBean findByPageBean(Integer startIndex, Integer pageSize,String orgid,String orgtype);
//    
//    public PageBean getAllReformProBySlouid(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype);
//    public PageBean getAllReformProBySlouids(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype);
//    
//    public PageBean getAllReformProBySlouidLs(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype, String realName) ;
//    public PageBean getAllReformProBySlouidCX(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype) ;
//    
//    
//    
//    public PageBean getAllReformNbsj(TblProblemEntity pro,Integer startIndex, Integer pageSize,String orgid,String orgtype,String projectid);
//    
//    
//    public PageBean getAllReformProBySlouidNbsj(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype);
//
//    public PageBean getAllReformProBySlouidNbsjLS(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype, String staffid);
//
//	public PageBean nbsjZggz_gz(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype);
//	
//	
//	public PageBean getAllReformProBySlouidNbsjCX(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype);
//	public PageBean getAllReformProBySlouidNbsjCXProject(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype);
//	
//	 public PageBean getAllReformPro(TblProblemEntity pro,Integer startIndex, Integer pageSize,String orgid,String orgtype,String type);
//
//	 
//	 
//	 public PageBean getAllReformNbsj(TblProblemEntity pro,Integer startIndex, Integer pageSize,String orgid,String orgtype,String projectid,String solutionid);
//	 public PageBean getAllReformNbsjByProject(TblProblemEntity pro,Integer startIndex, Integer pageSize,String orgid,String orgtype,String projectid,String solutionid,String selectidIdsstr);
//	 
//	 
//	 public PageBean getAllReformPro(TblProblemEntity pro,Integer startIndex, Integer pageSize,String orgid,String orgtype,String type,String solutionid);
	 public List<Object> getAllReformProBySlouidNbsjCXList(TblProblemEntity pro,String solutionid,String orgid,String orgtype);
//	 public PageBean getAllReformProBySlouidNbsjLSProject(TblProblemEntity pro,String solutionid,Integer startIndex, Integer pageSize,String orgid,String orgtype, String staffid);
}
