package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.StepResult;
import com.huabo.audit.oracle.entity.TblAccBook;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblAccBookMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditStepMapper;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.service.TblNbsjAuditStepService;
import com.huabo.audit.util.JDBCProperties;
@Service
public class TblNbsjAuditStepServiceImpl  implements TblNbsjAuditStepService {
	
	@Autowired
	private TblNbsjAuditStepMapper stepmapper;

	@Autowired
	private TblAccBookMapper bookmapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean findByExper(String token, BigDecimal typeId,TblNbsjAuditStepEntity setp,Integer pageNumber,Integer pageSize) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
//		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		if(typeId!=null) {
//			List<TblNbsjAuditStepEntity> list = stepmapper.findByExperByExperId(typeId);
//			resultMap.put("data",list);
//		}else {
//			List<TblNbsjAuditStepEntity> list = stepmapper.findByExperByExperall(typeId, setp);
//			resultMap.put("data",list);
//		}
//		
		

		//封装分页查询实体
		PageInfo<TblNbsjAuditStepEntity> pageInfo = new PageInfo<TblNbsjAuditStepEntity>();
		
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber); 
		pageInfo.setCondition(setp);
		
		//查询获取分页返回数据
		com.github.pagehelper.PageInfo<TblNbsjAuditStepEntity> page = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> {
			try {
				this.stepmapper.findByExperByExperall(typeId, setp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		pageInfo.setTlist(page.getList());
		pageInfo.setTotalRecord((int) page.getTotal());
		
		
		return ResponseFormat.retParam(1,200,pageInfo);
	}
	
	
	@Override
	public JsonBean findByxfStep(String token, TblNbsjAuditStepEntity setp,Integer pageNumber,Integer pageSize) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
//		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		List<TblNbsjAuditStepEntity> list = stepmapper.findByStaffid(loginStaff.getStaffid());
//		resultMap.put("data",list);
		
		
		

		//封装分页查询实体
		PageInfo<TblNbsjAuditStepEntity> pageInfo = new PageInfo<TblNbsjAuditStepEntity>();
		
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setCondition(setp);
		
		//查询获取分页返回数据
		com.github.pagehelper.PageInfo<TblNbsjAuditStepEntity> page = PageMethod.startPage(pageNumber, pageSize).doSelectPageInfo(() -> {
			try {
				this.stepmapper.selectListxfPageInfo(pageInfo, setp, loginStaff.getStaffid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		pageInfo.setTlist(page.getList());
		pageInfo.setTotalRecord((int) page.getTotal());
		
		return ResponseFormat.retParam(1,200,pageInfo);
		
	}

	
	
	@Override
	public JsonBean findByxfUser(String token, BigDecimal stepId,String realname) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(stepId!=null) {
			List<TblStaff> list = stepmapper.findByxfList(stepId);
			if(realname!=null && realname.length()>0) {
				list = stepmapper.findByNamexfList(stepId, realname);
			}
			resultMap.put("data",list);
		}
		
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteByExperId(String token, BigDecimal stepId) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		stepmapper.deletebyid(stepId);
		return ResponseFormat.retParam(1,200,null);
	}
	
	
	@Override
	public JsonBean xgStatus(String token, BigDecimal stepId,Integer xgstatus) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		stepmapper.xgStatus(stepId, xgstatus);
		return ResponseFormat.retParam(1,200,null);
	}
	
	@Override
	public JsonBean saveXfry(String token, String stepIds,String staffids) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(stepIds!=null && stepIds.length()>0) {
			String[] list = stepIds.split(",");
			for (String stepId : list) {
				if(staffids!=null && staffids.trim().length()>0) {
					String[] split = staffids.split(",");
					for (String staffid : split) {
						stepmapper.saveXfry(stepId, staffid);
					}
				}
			}
			
			
		}
		
		return ResponseFormat.retParam(1,200,null);
	}
	
	@Override
	public JsonBean deleteXfry(String token, BigDecimal stepId,String staffids) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(staffids!=null && staffids.trim().length()>0) {
			stepmapper.deleteXfry(stepId, staffids);
		}
		
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean saveOrupdate(String token, TblNbsjAuditStepEntity auditStep)throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(auditStep.getStepid()!=null) {
			auditStep.setUpdatetime(new Date());
			stepmapper.updateByPrimaryKeySelective(auditStep);
		}else {
			auditStep.setOrgid(loginStaff.getCurrentOrg().getOrgid()); 
			auditStep.setCreatetime(new Date());
			auditStep.setStepid(RandomUtil.uuBigDecimalId());
			stepmapper.insertSelective(auditStep);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",auditStep);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getone(String token, BigDecimal stepId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjAuditStepEntity entity = stepmapper.findByExperBystepId(stepId);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",entity);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getList(String token, Integer pageNumber, Integer pageSize,String sql) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>();
    	
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
		
		pageInfo.setTlist(stepmapper.selectListPageInfo(pageInfo, sql));
		pageInfo.setTotalRecord(stepmapper.selectListPageInfocount(sql));
		pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	
	
	
	@Override
	public JsonBean zxsql(String token, BigDecimal stepId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjAuditStepEntity entity = stepmapper.findByExperBystepId(stepId);
		if(entity!=null && entity.getBookid()!=null) {
			TblAccBook accBook = bookmapper.findByBookIdOne(entity.getBookid());
			Long time = System.currentTimeMillis();
			StepResult result=new StepResult();
			result.setMemo(time+"");
			result.setSavetime(new Date());
			result.setStaffid(loginStaff.getStaffid());
			result.setStepid(entity.getStepid());
			stepmapper.insertEntity(result);
			
			List<String> params = JDBCProperties.getTbable(entity.getSqlstr(), accBook.getAcctid().toString());
			if(!JDBCProperties.isNotExistsTable("NBSJ_MX_"+entity.getStepid(), accBook.getAcctid().toString())) {
				JDBCProperties.executeSql("NBSJ_MX_"+entity.getStepid(), accBook.getAcctid().toString(), params);
			}
			List<Map<String, Object>> list = JDBCProperties.getData(entity.getSqlstr(), accBook.getAcctid().toString());
			JDBCProperties.insertData("NBSJ_MX_"+entity.getStepid(), accBook.getAcctid().toString(), list, loginStaff.getStaffid(), result.getResultid()+"_"+time);
			
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",entity);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean getxjjgList(String token, BigDecimal stepId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	List<StepResult> list = stepmapper.sjmxReulst(stepId);
    	resultMap.put("data", list); 
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getDatelistt(String token, BigDecimal resultid,Integer pageNumber,Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		StepResult result = stepmapper.onesjmxReulst(resultid);
		TblNbsjAuditStepEntity entity = stepmapper.findByExperBystepId(result.getStepid());
		TblAccBook accBook = bookmapper.findByBookIdOne(entity.getBookid());
		String tablename="NBSJ_MX_"+result.getStepid();
		String sql="select * from "+tablename+" where EXECTIME='"+resultid+"_"+result.getMemo()+"'";
		PageInfo<Map<String,Object>> pageInfo = new PageInfo<Map<String,Object>>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	return  JDBCProperties.GetGather(accBook.getAcctid().toString(), sql, pageInfo);
	} 

	

	
	public JsonBean findBycode(String token)throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		 Calendar cal = Calendar.getInstance();
		 int year = cal.get(Calendar.YEAR);
		String  code = stepmapper.findbycode(year+"");
		Integer integer = stepmapper.findbycodecount(year+"");
		Integer num=1;
		if(code!=null && code.length()>0) {
			code=code.substring((code.length()-2),(code.length()));
			System.out.println("----------"+code);
			System.out.println(Integer.parseInt(code));
			integer=Integer.parseInt(code);
		    num=Integer.parseInt(code)+1;
		}
		
		 if(integer<10) { 
			 code=year+"0"+num; 
		 }else {
			 code=year+""+num;
		  }
		 
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data", code);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
}
