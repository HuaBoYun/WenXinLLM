package com.huabo.audit.service.impl;

import java.math.BigDecimal;
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
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjProjectTeamEntity;
import com.huabo.audit.oracle.entity.TblNbsjTeamstaffEntity;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectteamMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTeamstaffMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjProjectteamService;

/**
 * 描述:实现类
 * 
 * @author: ziyao
 * @date: 2022-04-19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjProjectteamServiceImpl implements TblNbsjProjectteamService {
	@Resource
	private TblStaffMapper tblStaffMapper;
	
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	@Resource
	private TblNbsjProjectteamMapper tblNbsjProjectteamMapper;
	
	@Resource
	private TblNbsjTeamstaffMapper tblNbsjTeamstaffMapper;
	
	@Resource
	public TblNbsjProjectService tblnbsjProjectService;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean findProjectteamList(String token, BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		//
		if(null == projectid) {
			TblNbsjProject tnp = this.tblnbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
			if(tnp == null) {
				return ResponseFormat.retParam(0,10001,resultMap);
			}
			projectid = tnp.getProjectId();
		}
		
		List<TblNbsjProjectTeamEntity> listTeam = this.tblNbsjProjectteamMapper.selectListTeamByProjectId(projectid);
		
		List<TblNbsjTeamstaffEntity> listTeamZY = this.tblNbsjProjectteamMapper.selectListTeamZuYuanByProjectId(projectid);
		
		TblNbsjProjectTeamEntity team = null;
		for (int i = 0; i < listTeam.size(); i++) {
			team = listTeam.get(i);

			BigDecimal teamid = team.getTeamId();
			 
			setLeaderInfo(teamid,listTeamZY,team);
			team.setTeamStaffs(listTeamZY);
			team.setZyNames(getZyNamesByTeamid(teamid, listTeamZY));
			team.setZystaffids(getZyStaffidsByTeamid(teamid, listTeamZY));
		}
		
		resultMap.put("listTeam", listTeam);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	private void setLeaderInfo(BigDecimal teamid,List<TblNbsjTeamstaffEntity> listTeamZY,TblNbsjProjectTeamEntity team) {
		
		TblNbsjTeamstaffEntity ts = null;
		for (int i = 0; i < listTeamZY.size(); i++) {
			ts = listTeamZY.get(i);
			BigDecimal zyTeamid = ts.getTeamid();
			if(zyTeamid == teamid) {
				if(ts.getStafftype() == 0) {
					listTeamZY.remove(i);
					
					team.setLeaderid(ts.getStaffid());
					if(null != ts.getStaff()) {
						team.setLeaderName(ts.getStaff().getRealname());
					}
					
					return;
				}
			}
		}
		return;
	}
	
	private String getZyNamesByTeamid(BigDecimal teamid,List<TblNbsjTeamstaffEntity> listTeamZY) {
		String zyNames = "";
		
		TblNbsjTeamstaffEntity ts = null;
		int cnt = 0;
		for (int i = 0; i < listTeamZY.size(); i++) {
			ts = listTeamZY.get(i);
			BigDecimal zyTeamid = ts.getTeamid();
			if(zyTeamid == teamid) {
				if(ts.getStafftype() == 1) {
					
					if(cnt>0) {
						zyNames+=",";
					}
					zyNames+=ts.getStaff().getRealname();
					cnt = cnt+1;
					
				}
			}
		}
		return zyNames;
	}
	
	private String getZyStaffidsByTeamid(BigDecimal teamid,List<TblNbsjTeamstaffEntity> listTeamZY) {
		String zyNames = "";
		
		TblNbsjTeamstaffEntity ts = null;
		
		int cnt = 0;
		
		for (int i = 0; i < listTeamZY.size(); i++) {
			ts = listTeamZY.get(i);
			BigDecimal zyTeamid = ts.getTeamid();
			if(zyTeamid == teamid) {
				if(ts.getStafftype() == 1) {
					
					if(cnt>0) {
						zyNames+=",";
					}
					zyNames+=ts.getStaffid();//.getStaff().getStaffid();
					cnt = cnt+1;
				}
			}
		}
		return zyNames;
	}
	
	
	@Override
	public JsonBean findProjectteamUsrList(String token, BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
//		List<TblNbsjProjectTeamEntity> listTeam = this.tblNbsjProjectteamMapper.selectListTeamByProjectId(projectid);
		
		List<TblNbsjTeamstaffEntity> listTeamZY = this.tblNbsjProjectteamMapper.selectListTeamZuYuanByProjectId(projectid);
		
		resultMap.put("listTeamZY", listTeamZY);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean findProjectUsrList(String token, BigDecimal projectid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		List<TblNbsjTeamstaffEntity> listTeamZY = this.tblNbsjProjectteamMapper.selectListTeamZuYuanByProjectId(projectid);
		//一级复合二级复合人员增加项目主审
		TblNbsjProject project=this.tblNbsjProjectMapper.selectPJById(projectid);
		TblNbsjTeamstaffEntity e=new TblNbsjTeamstaffEntity();
		e.setStafftype(3);
		e.setStaff(tblStaffMapper.selectById(project.getPmId()));
		e.setCreateTime(project.getCreateTime());
		e.setStaffid(project.getCreateStaffId());
		e.setCreateUserName(project.getCreateUserName());
		listTeamZY.add(e);
		resultMap.put("listTeamZY", listTeamZY);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getPjteamInfoById(String token, BigDecimal teamId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjProjectTeamEntity team = this.tblNbsjProjectteamMapper.selectTeamByTeamId(teamId);
		
		List<TblNbsjTeamstaffEntity> listTeamZY = this.tblNbsjProjectteamMapper.selectListTeamZuYuanByTeamId(teamId);

		BigDecimal teamid = team.getTeamId();
		 
		setLeaderInfo(teamid,listTeamZY,team);
		team.setTeamStaffs(listTeamZY);
		team.setZyNames(getZyNamesByTeamid(teamid, listTeamZY));
		
		resultMap.put("team", team);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean pjItemAdd(String token, TblNbsjProjectTeamEntity team)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
//		Integer count = this.tblNbsjProjectMapper.selectPlanCodeByOrgid(pj);
//		if(count > 0) {
//			return ResponseFormat.retParam(0,202,null);
//		}
    	
		team.setCreateUserId(loginStaff.getStaffid());
		team.setCreateTime(new Date());
		//根据主键是否为空判断新增还是修改 ，主键为空新增、不为空修改；
		
		if(team.getTeamId() != null) {
			//修改；
			this.tblNbsjProjectteamMapper.updateEntity(team);
			
			//先清除
			tblNbsjTeamstaffMapper.deleteZYByTeamId(team.getTeamId());
			
			//保存组长
			saveZY(team.getTeamId(), team.getLeaderid(), 0);
			//保存组员
			if (team.getZystaffids() != null && !"".equals(team.getZystaffids())) {
				String[] staffids = team.getZystaffids().split(",");
				for (int i = 0; i < staffids.length; i++) {
					String staffid = staffids[i];
					saveZY(team.getTeamId(), new BigDecimal(staffid), 1);
				}
			}
		}else {
			if(team.getLeaderid()!=null&&team.getLeaderid().compareTo(new BigDecimal("0"))!=0){
			team.setTeamId(RandomUtil.uuBigDecimalId());
			//新增；
			this.tblNbsjProjectteamMapper.insertEntity(team);
			
			//保存组长
			saveZY(team.getTeamId(), team.getLeaderid(), 0);
			//保存组员
			if (team.getZystaffids() != null && !"".equals(team.getZystaffids())) {
				String[] staffids = team.getZystaffids().split(",");
				for (int i = 0; i < staffids.length; i++) {
					String staffid = staffids[i];
					saveZY(team.getTeamId(), new BigDecimal(staffid), 1);
				}
			}
			
			//==关联信息TBL_NBSJ_PRO_TEAM
			BigDecimal teamId = team.getTeamId();
			this.tblNbsjProjectteamMapper.insertProTeam(teamId, team.getProjectid());
			}
			
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("team",team);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	private void saveZY(BigDecimal teamid,BigDecimal staffid,Integer stafftype) throws Exception {
		TblNbsjTeamstaffEntity teamstaff = new TblNbsjTeamstaffEntity();
		teamstaff.setTeamid(teamid);
		teamstaff.setStaffid(staffid);
		teamstaff.setStafftype(stafftype);
		teamstaff.setId(RandomUtil.uuBigDecimalId());
		tblNbsjTeamstaffMapper.insertEntity(teamstaff);
	}

	@Override
	public JsonBean pjItemDelete(String token, BigDecimal teamId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		this.tblNbsjProjectteamMapper.deleteTSByTeamId(teamId);
		this.tblNbsjProjectteamMapper.deletePTByTeamId(teamId);
		this.tblNbsjProjectteamMapper.deletePJEByTeamId(teamId);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
}
