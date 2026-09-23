package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjOuterruleEntity;
import com.huabo.audit.util.R;

public interface TblNbsjOuterruleService {
	public void add(TblNbsjOuterruleEntity TblNbsjOuterrule) ;
	public void addList(List<TblNbsjOuterruleEntity> TblNbsjOuterrule) ;
	
    public List findAll();

	public TblNbsjOuterruleEntity findByid(String id) throws Exception;
	public void modify(TblNbsjOuterruleEntity TblNbsjOuterrule);
	public List search(String name ,String org ,String effectivelevel ,String timeliness,
    	String rulenumber,String status ,String pubstart ,String pubend ,String effstart ,
    	String effend ,String sort ,String sort_type);
	
	public List<TblNbsjOuterruleEntity> findBydefect(String conid);
	public List findBysql(String sql);
	
	public List<TblNbsjOuterruleEntity> findOuterRuleByFlowid(String flowid);
	
//	public PageBean findAll(Integer startIndex,Integer pageSize,TblNbsjOuterruleEntity TblNbsjOuterrule);
	
	public List<TblNbsjOuterruleEntity> findBycomid(String conid);
	public List<TblNbsjOuterruleEntity> findByRulecode(String code);
	public void addList(List<TblNbsjOuterruleEntity> TblNbsjOuterrule,BigDecimal orgid);
//	public PageBean findOuterRuleByFlowidPageBean(String flowid,Integer startIndex, Integer pageSize);
//	public PageBean findInnerRuleByBugPageBean(String orgid,String bugid,Integer startIndex, Integer pageSize);
//	public PageBean findOutRuleByOrgidAndFlowid(String orgid,String name,String status,String flowid,int startIndex, int pageSize);
//	
//	public PageBean findAllwnss(Integer startIndex, Integer pageSize,String wnss);
	
	
	public JsonBean findOuterRuleList(String token,Integer startIndex,Integer pageSize,TblNbsjOuterruleEntity TblNbsjOuterrule,String type) throws Exception;

	public JsonBean findById(String token,String outerid) throws Exception;
	
	public  Map<String,Object> delete(String token,String id) throws Exception;
	
	public JsonBean mergeOuterruleInfo(String token,TblNbsjOuterruleEntity outer,BigDecimal outerid,String attIds) throws Exception;
	
	public String SelectOuterruleAtt(String outer) throws Exception;
	
	R removeAttInfoByAttId(String token, String attId) throws Exception;

}
