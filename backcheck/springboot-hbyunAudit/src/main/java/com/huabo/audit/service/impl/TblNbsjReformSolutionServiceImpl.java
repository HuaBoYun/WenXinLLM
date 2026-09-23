package com.huabo.audit.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjRefopm;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjRefopmMapper;
import com.huabo.audit.oracle.mapper.TblNbsjReformSolutionMapper;
import com.huabo.audit.service.TblNbsjReformSolutionService;

import cn.hutool.json.JSONObject;

/**
 *  整改方案
 * @author T
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjReformSolutionServiceImpl implements TblNbsjReformSolutionService {
	
	@Resource
	private  TblNbsjReformSolutionMapper solutionMapper;
	@Resource
	private TblNbsjRefopmMapper reMapper;
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	@Resource
	private TblNbsjProjectMapper tblnbsjprojectMapper;
	
	@Resource
    private UserProvider userProvider;
	
	
	@Override
	public JsonBean saveOrUpdate(TblNbsjReformSolution solution, String token,String reformid,String attIds,String projectid,String endDate) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(endDate!=null) {
			solution.setEnddate(sdf.parse(endDate));
		}
		if(projectid!=null) {
			TblNbsjProject pro = tblnbsjprojectMapper.getById(projectid);
			solution.setPmid(pro.getPmId());
			solution.setProjectid(new BigDecimal(projectid));
		}
		
		if(solution!=null && solution.getRunstatus()==null) {
			solution.setRunstatus(0);
		}
		
		
		if(solution.getSolutionid()!=null) {
			this.solutionMapper.updateEntity(solution);
		}else {
			solution.setCreatedate(new Date());
			solution.setStaffid(loginStaff.getStaffid());
			solution.setOrgid(loginStaff.getLinkDetp().getOrgid());
			solution.setSolutionid(RandomUtil.uuBigDecimalId());
			solutionMapper.insert(solution);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(reformid!=null && reformid.trim().length()>0) {
			String[] list=reformid.split(",");
			for (int i = 0; i < list.length; i++) {
				TblNbsjRefopm reform = new TblNbsjRefopm();
				if (list[i].indexOf("__b") >= 0) {
					List<TblNbsjRefopm> relist = reMapper.selectNbsjReformWtList(solution.getSolutionid(),  list[i].replace("__b", ""),null);
					if(relist!=null && relist.size()>0) {
						continue;
					}
					reform.setBugid(new BigDecimal(list[i].replace("__b", "")));
				}else {
					List<TblNbsjRefopm> relist = reMapper.selectNbsjReformWtList(solution.getSolutionid(), null, list[i].replace("__p", ""));
					if(relist!=null && relist.size()>0) {
						continue;
					}
					
					if(!"".equals(list[i].replace("__p", ""))) {
						reform.setQuestionid(new BigDecimal(list[i].replace("__p", "")));
					}
				}
				reform.setProjectid(new BigDecimal(projectid));
				reform.setStatus(TblNbsjRefopm.ZGZ);
				reform.setCreateid(loginStaff.getStaffid());
				reform.setSolutionid(solution.getSolutionid());
				reform.setCreatedate(new Date());
				reform.setReformid(RandomUtil.uuBigDecimalId());
				reMapper.insert(reform);
			}
		}
		
		
		
		if(attIds != null && !"".equals(attIds)) {
			String[] attId = attIds.split(",");
			for (String aid : attId) {
				solutionMapper.insertAttInfoAtt(solution.getSolutionid(), aid);
			}
		}
		resultMap.put("solution",solution);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean delete(String solutionid, String token) throws Exception {
		if(solutionid!=null) {
			List<TblNbsjRefopm> relist = reMapper.selectNbsjReformBySolutinFa(new BigDecimal(solutionid), null);
			if(relist!=null && relist.size()>0) {
				for (TblNbsjRefopm tblNbsjRefopm : relist) {
					reMapper.deleteAttInfoAttByref(tblNbsjRefopm.getReformid().toString());
					reMapper.deleteNbsjRefopmById(tblNbsjRefopm.getReformid());
				}
			}
			solutionMapper.deleteAttInfoAttBySolutionid(solutionid);
			solutionMapper.deleteSolutionById(new BigDecimal(solutionid));
			return ResponseFormat.retParam(1,200,null);
		}
		
		return ResponseFormat.retParam(0,10002,null);
	}
	@Override
	public JsonBean findBySolutionid(String solutionid) throws Exception {
		if(solutionid!=null) {
			TblNbsjReformSolution solution = solutionMapper.selectNbsjReformSolutionListByID(new BigDecimal(solutionid));
			List<TblNbsjRefopm> relist = reMapper.selectNbsjReformBySolutinFa(solution.getSolutionid(), null);
			solution.setTblReforms(relist);
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			resultMap.put("solution",solution);
			return ResponseFormat.retParam(1,200,resultMap);
		}
		return ResponseFormat.retParam(0,10002,null);
	}

	@Override
	public JsonBean findAllcxs( String code, String name, Integer pageNumber, Integer pageSize,String token,String type,BigDecimal createstaffid,Integer runstatus) throws Exception {
		TblNbsjReformSolution solution=new TblNbsjReformSolution();
		solution.setSolutioncode(code);
		solution.setSolutionname(name);
		if(null != createstaffid) {
			TblStaff csf = new TblStaff();
			csf.setStaffid(createstaffid);
			solution.setCreateStaff(csf);
		}
		
		solution.setRunstatus(runstatus);
		
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(type!=null && type.equals("3")) {
			TblStaff csf = new TblStaff();
			csf.setStaffid(loginStaff.getStaffid());
			solution.setCreateStaff(csf);
		} 
		String orgid=loginStaff.getCurrentOrg().getOrgid().toString();
		if(type!=null &&( type.equals("1") || type.equals("2"))) {
			orgid=loginStaff.getStaffid().toString();
		}
		
		if(orgid!=null) {
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
	    	
	    	PageInfo<TblNbsjReformSolution> pageInfo = new PageInfo<TblNbsjReformSolution>();
	    	
	    	pageInfo.setPageSize(pageSize);
	    	pageInfo.setCurrentPage(pageNumber);
	    	
	    	//查询关联方案的整改内容
	    	List<TblNbsjReformSolution> list = solutionMapper.selectNbsjReformSolutionByPageInfo(pageInfo, new BigDecimal(orgid), solution,type);
	    	if(list!=null && list.size()>0) {
	    		for (TblNbsjReformSolution tblNbsjReformSolution : list) {
	    			List<TblNbsjRefopm> relist = reMapper.selectNbsjReformBySolutinFa(tblNbsjReformSolution.getSolutionid(), null);
	    			tblNbsjReformSolution.setTblReforms(relist);
				}
	    	}
	    	pageInfo.setTlist(list);
	    	pageInfo.setTotalRecord(solutionMapper.selectNbsjReformSolutionByCount( new BigDecimal(orgid), solution,type));
	    	pageInfo.getTotalPage();
	    	resultMap.put("pageInfo", pageInfo);
	    	return ResponseFormat.retParam(1,200,resultMap);
		}
		return ResponseFormat.retParam(0,10002,null);
	}
	
	
	@Override
	public JsonBean getAttListBySolutionId(String token, BigDecimal solutionid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListBySolutionId(solutionid);
		resultMap.put("attList", attList);
		return ResponseFormat.retParam(1,200,attList);
	}

	
	
	@Override
	public JsonBean delAttListBySolutionId(String token, BigDecimal attid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(attid!=null) {
			solutionMapper.deleteAttInfoAttid(attid.toString());
			this.tblAttachmentMapper.deleteEntity(attid);
			return ResponseFormat.retParam(1,200,null);
		}
		return ResponseFormat.retParam(0,10002,null);
	}
	
	
	
	
	@Override
	public JsonBean xfry(TblNbsjReformSolution solution,String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(solution.getSolutionid()!=null) {
			this.solutionMapper.updateEntity(solution);
			return ResponseFormat.retParam(1,200,null);
		}
		
		
		return ResponseFormat.retParam(0,10002,null);
	}
	
	
	
	
	@Override
	public List<JSONObject> findAlls2(BigDecimal solutionid) {
		List<JSONObject> list=null;
		try {
			list = solutionMapper.findAlls2(solutionid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	// 将字Clob转成String类型
	public String ClobToString(Clob sc) throws SQLException, IOException {
		String reString = "";
		Reader is = sc.getCharacterStream();// 得到流
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		return reString;
	}

	
	
}
