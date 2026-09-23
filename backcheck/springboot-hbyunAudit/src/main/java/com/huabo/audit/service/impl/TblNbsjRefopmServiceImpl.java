package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;
import com.huabo.audit.oracle.entity.TblNbsjRefopm;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;
import com.huabo.audit.oracle.entity.TblNbsjStaffscoreDetails;
import com.huabo.audit.oracle.entity.TblNbsjZglsProblem;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuditplanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjQuestionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjRefopmMapper;
import com.huabo.audit.oracle.mapper.TblNbsjReformSolutionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjZglsProblemMapper;
import com.huabo.audit.service.TblNbsjRefopmService;
import com.huabo.audit.util.ModuleCodeDef;
/**
 *  整改任务
 * @author T
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjRefopmServiceImpl implements TblNbsjRefopmService {

	
	@Resource
	private TblNbsjRefopmMapper reMapper;
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	@Resource
	private  TblNbsjReformSolutionMapper solutionMapper;
	@Resource
	private TblNbsjZglsProblemMapper tblNbsjZglsProblemMapper;
	@Resource
	private TblNbsjQuestionMapper tblNbsjQuestionMapper;
	
	@Resource
    private TblNbsjAuditplanMapper tblNbsjAuditplanMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean saveOrupdate(TblNbsjRefopm refor,String token,BigDecimal solutionid)  throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjRefopm reforms=reMapper.selectNbsjReformById(refor.getReformid());
		if(reforms!=null && reforms.getStatus()==1 && reforms.getPeronincharge()==null){
			reforms.setLastreformstatus(1);
			reforms.setPeronincharge(refor.getPeronincharge());
			reMapper.updateEntity(reforms);
		}else {
			TblNbsjRefopm reform=new TblNbsjRefopm();
			reform.setLastreformstatus(1);
			reform.setPeronincharge(reforms.getPeronincharge());
			if(solutionid!=null) {
				reform.setSolutionid(solutionid);
			}else {
				TblNbsjReformSolution solution = solutionMapper.selectNbsjReformSolutiByI(reforms.getSolutionid());
				TblNbsjReformSolution solutionnew = solutionMapper.selectNbsjReformSolutionListByIDnew(reforms.getSolutionid());
				if(solutionnew==null) {
					//获取当前月
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());
					calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH) + 1;
					String montrStr = "";
					if(month<10) {
						montrStr = "0"+month;
					}else {
						montrStr = month+"";
					}
					
					//组合自动编号:年度-月-模块-自增数
					String solutioncode = year+"-"+montrStr+"-"+ModuleCodeDef.ZGFA+"-";
					
					//自增数查询
					String no = "0";
					try {
						no = this.tblNbsjAuditplanMapper.selectMaxSolutioncode(solutioncode);
					} catch (Exception e) {
						no = "0";
					}
					if(no != null) {
//						 no = no.replace(solutioncode, "");
					}else {
						no = "0";
					}
					solutioncode += (Integer.parseInt(no)+1);
					solutionnew=solution;
					solutionnew.setSolutioncode(solutioncode);
					solutionnew.setSolutionid(null);
					solutionnew.setRunstatus(2);
					solutionnew.setParentid(reforms.getSolutionid());
					solutionMapper.insertEntity(solutionnew);
				}
				reform.setWxhstatus(1);
				if(solutionnew!=null) {
					reform.setSolutionid(solutionnew.getSolutionid());
					reform.setProjectid(solutionnew.getProjectid());
				}
				reform.setReformmeasure(reforms.getNextmeasures());
				
			}
			
			reform.setStatus(1);
			String problemid = reforms.getProblemid();
			reform.setCreatedate(new Date());
			reform.setCreateid(loginStaff.getStaffid());
			if (problemid.indexOf("__b") >= 0) {
				reform.setBugid(new BigDecimal(problemid.replace("__b", "")));
			}
			if (problemid.indexOf("__p") >= 0) {
				reform.setQuestionid(new BigDecimal(problemid.replace("__p", "")));
			}
			reMapper.insertEntity(reform);
			
			reforms.setLastreformstatus(0);
			if(reforms.getStatus()==1){
				reforms.setStatus(0);
			}
			reMapper.updateEntity(reforms);
		}
		
		
		
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean delete(BigDecimal reformid)   throws Exception{
		if(reformid!=null) {
			reMapper.deleteNbsjRefopmById(reformid);
			return ResponseFormat.retParam(1,200,null);
		}
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean getByid(String reid)  throws Exception{
		if(reid!=null) {
			TblNbsjRefopm refopm = reMapper.selectNbsjReformById(new BigDecimal(reid));
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			resultMap.put("reform",refopm);
			return ResponseFormat.retParam(1,200,resultMap);
		}
		return ResponseFormat.retParam(0,10002,null);
	}


	@Override
	public JsonBean getAllReformNbsjByProject(TblNbsjRefopm re, Integer pageNumber, Integer pageSize, BigDecimal projectid,String selectidIdsstr)  throws Exception{
		
		if(projectid!=null) {
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
	    	PageInfo<TblNbsjRefopm> pageInfo = new PageInfo<TblNbsjRefopm>();
	    	
	    	pageInfo.setPageSize(pageSize);
	    	pageInfo.setCurrentPage(pageNumber);
			
			pageInfo.setTlist(reMapper.selectNbsjReformByPageInfo(pageInfo, projectid, re, selectidIdsstr));
			pageInfo.setTotalRecord(reMapper.selectNbsjReformByProjectCount(projectid, re, selectidIdsstr));
			pageInfo.getTotalPage();
	    	resultMap.put("pageInfo", pageInfo);
	    	return ResponseFormat.retParam(1,200,resultMap);
		}
		
		
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean findTblReformBySheetIdAndSoultionid(String code, BigDecimal soultionid, Integer pageNumber,Integer pageSize,TblNbsjRefopm re,String type,String token)  throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(soultionid!=null) {
			re.setPeronincharge(loginStaff.getStaffid());
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
	    	PageInfo<TblNbsjRefopm> pageInfo = new PageInfo<TblNbsjRefopm>();
	    	
	    	pageInfo.setPageSize(pageSize);
	    	pageInfo.setCurrentPage(pageNumber);
			
			pageInfo.setTlist(reMapper.selectNbsjReformBySolutionPageInfo(pageInfo, soultionid, re,type));
			pageInfo.setTotalRecord(reMapper.selectNbsjReformBySolutionCount(soultionid, re,type));
			pageInfo.getTotalPage();
	    	resultMap.put("pageInfo", pageInfo);
	    	return ResponseFormat.retParam(1,200,resultMap);
		}
		
		
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean update(TblNbsjRefopm refor, String token,String attids,String type) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(type!=null && type.equals("1")) {
			refor.setZgstatus(3);
		}
		if(refor!=null) {
			reMapper.updateEntity(refor);
			if(attids != null && !"".equals(attids)) {
				String[] attId = attids.split(",");
				for (String aid : attId) {
					reMapper.insertAttInfoAtt(refor.getReformid(), aid);
				}
			}
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			resultMap.put("refopm",refor);
	    	return ResponseFormat.retParam(1,200,null);
		}
		
		
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean tjyz(BigDecimal soultionid, String token) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(soultionid!=null) {
			List<TblNbsjRefopm> list = reMapper.selectNbsjReformBySolutionRwAll(soultionid);
			if(list!=null && list.size()>0) {
				return ResponseFormat.retParam(0,30004,null);
			}
			List<TblNbsjRefopm> listwc = reMapper.selectNbsjReformBySolutionRwwcAll(soultionid);
			if(listwc!=null && listwc.size()>0) {
				for (TblNbsjRefopm tblNbsjRefopm : listwc) {
					tblNbsjRefopm.setStatus(3);
					tblNbsjRefopm.setWxhstatus(null);
					reMapper.updateEntity(tblNbsjRefopm);
				}
			}
			
			 Integer wcczs = reMapper.selectNbsjReformBySolutionzsCount(soultionid);
			 Integer wczs = reMapper.selectNbsjReformBySolutionYwcsCount(soultionid);
			 if(wcczs==wczs) {
				 TblNbsjReformSolution solution = solutionMapper.selectNbsjReformSolutionListByID(soultionid);
				 solution.setRunstatus(4);
				// solution.setZgstatus(1);
				 solutionMapper.updateEntity(solution);
			 }
			 
			 return ResponseFormat.retParam(1,200,null);
			 
		}
		
		
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean update(TblNbsjRefopm refor,String token,BigDecimal solutionid,String attids,String type,String zgproblem)  throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		if(refor!=null) {
			TblNbsjReformSolution solution = solutionMapper.selectNbsjReformSolutionListByID(solutionid);
			if(type!=null && type.equals("1")) {
				refor.setZgstatus(3);
				solution.setZgstatus(1);
			}else if(type!=null && type.equals("2")) {
				refor.setStatus(2);
				solution.setRunstatus(2);
			}else {
				refor.setStatus(2);
				solution.setRunstatus(2);
			}
			solutionMapper.updateEntity(solution);
			reMapper.updateEntity(refor);
			TblNbsjRefopm refopm = reMapper.selectNbsjReformByIds(refor.getReformid());
			if(refopm.getQuestionid()!=null && zgproblem!=null) {
				TblNbsjQuestionEntity entity = tblNbsjQuestionMapper.getById(refor.getQuestionid().toString());
				List<TblNbsjZglsProblem> list = JSONArray.parseArray(zgproblem,TblNbsjZglsProblem.class);
				if(list!=null && list.size()>0) {
					for (TblNbsjZglsProblem tblNbsjZglsProblem : list) {
						tblNbsjZglsProblem.setCreateorg(loginStaff.getCurrentOrg().getOrgid());
						tblNbsjZglsProblem.setCreatestaff(loginStaff.getStaffid());
						tblNbsjZglsProblem.setCreatetiem(new Date());
						tblNbsjZglsProblem.setRectification(entity.getSheetId());
						tblNbsjZglsProblem.setReformid(refor.getReformid());
						tblNbsjZglsProblemMapper.insert(tblNbsjZglsProblem);
					}
				}
				
			}
			
			
			if(attids != null && !"".equals(attids)) {
				String[] attId = attids.split(",");
				for (String aid : attId) {
					reMapper.insertAttInfoAtt(refor.getReformid(), aid);
				}
			}
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			resultMap.put("refopm",refor);
	    	return ResponseFormat.retParam(1,200,null);
		}
		
		
		return ResponseFormat.retParam(0,10002,null);
	
	}
	
	
	
	@Override
	public JsonBean pjtjyz(BigDecimal soultionid, String token) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(soultionid!=null) {
			Integer zs = reMapper.selectNbsjReformBySolutionpjCount(soultionid);
			Integer wczs = reMapper.selectNbsjReformBySolutionpjwcCount(soultionid);
			TblNbsjReformSolution solution = solutionMapper.selectNbsjReformSolutionListByID(soultionid);
			if(zs==wczs) {
				 solution.setRunstatus(4);
				 solution.setZgstatus(3);
				 solutionMapper.updateEntity(solution);
			}else {
				 solution.setZgstatus(3);
				 solutionMapper.updateEntity(solution);
			}
			
			
			 return ResponseFormat.retParam(1,200,null);
			 
		}
		
		
		return ResponseFormat.retParam(0,10002,null);
	}
	
	@Override
	public JsonBean getAllReformNbsjBywxh(TblNbsjRefopm re,Integer pageNumber, Integer pageSize)  throws Exception{
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
	    	PageInfo<TblNbsjRefopm> pageInfo = new PageInfo<TblNbsjRefopm>();
	    	pageInfo.setPageSize(pageSize);
	    	pageInfo.setCurrentPage(pageNumber);
			pageInfo.setTlist(reMapper.selectNbsjReformBySolutionPageInfowxh(pageInfo, re));
			pageInfo.setTotalRecord(reMapper.selectNbsjReformBySolutionPageInfowxhcount(pageInfo, re));
			pageInfo.getTotalPage();
	    	resultMap.put("pageInfo", pageInfo);
	    	return ResponseFormat.retParam(1,200,resultMap);
		
	}
	
	
	
	@Override
	public JsonBean getResultall(BigDecimal soultionid,String token,String reformid) throws Exception{
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		if(reformid!=null) {
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
	    	
			if (reformid.indexOf("__b") >= 0) {
				List<TblNbsjRefopm> relist = reMapper.selectNbsjReformWtList(soultionid,  reformid.replace("__b", ""),null);
				if(relist!=null && relist.size()>0) {
					resultMap.put("list", relist);
				}
			}else {
				List<TblNbsjRefopm> relist = reMapper.selectNbsjReformWtList(soultionid, null, reformid.replace("__p", ""));
				if(relist!=null && relist.size()>0) {
					resultMap.put("list", relist);
				}
			}
	    	
	    	return ResponseFormat.retParam(1,200,resultMap);
		}
		
		
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean selectNbsjReformByAll(BigDecimal auditorg, String code, String projectname, Integer pageNumber,Integer pageSize,String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	PageInfo<TblNbsjRefopm> pageInfo = new PageInfo<TblNbsjRefopm>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(reMapper.selectNbsjReformByAllPageInfo(pageInfo, auditorg, code, projectname));
		pageInfo.setTotalRecord(reMapper.selectNbsjReformByAllcount(auditorg, code, projectname));
		pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
		
	}
	
	@Override
	public JsonBean getAttListByReformId(String token, BigDecimal reformid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListByRefromId(reformid);
		resultMap.put("attList", attList);
		return ResponseFormat.retParam(1,200,attList);
	}
	
	@Override
	public JsonBean delAttListByReformId(String token, BigDecimal attid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(attid!=null) {
			reMapper.deleteAttInfoAtt(attid.toString());
			this.tblAttachmentMapper.deleteEntity(attid);
			return ResponseFormat.retParam(1,200,null);
		}
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean getzgproblemlist(String token, BigDecimal reformid,String datatype) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		List<TblNbsjZglsProblem> list = tblNbsjZglsProblemMapper.getzgproblemlist(reformid, datatype);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("tlist", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deletezgproblemlist(String token, BigDecimal problemid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		tblNbsjZglsProblemMapper.deleteById(problemid);
		return ResponseFormat.retParam(1,200,null);
	}

}
