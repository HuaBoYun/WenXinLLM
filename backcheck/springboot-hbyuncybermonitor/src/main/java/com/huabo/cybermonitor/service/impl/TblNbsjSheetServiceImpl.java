package com.huabo.cybermonitor.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.enmus.ProcessEnum;
import com.huabo.cybermonitor.entity.TblNbsjProject;
import com.huabo.cybermonitor.entity.TblNbsjSheetEntity;
import com.huabo.cybermonitor.entity.TblNbsjSheetReportEntity;
import com.huabo.cybermonitor.entity.TblOrganization;
import com.huabo.cybermonitor.mapper.TblNbkzRiskMapper;
import com.huabo.cybermonitor.mapper.TblNbsjProjectMapper;
import com.huabo.cybermonitor.mapper.TblNbsjSheetMapper;
import com.huabo.cybermonitor.service.ActivityPluginsService;
import com.huabo.cybermonitor.service.TblNbsjProjectService;
import com.huabo.cybermonitor.service.TblNbsjSheetService;
import com.huabo.cybermonitor.vo.TBlNbsjSheetVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
* 描述:实现类
* @author: ziyao
* @date: 2022-04-20
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjSheetServiceImpl extends ServiceImpl<TblNbsjSheetMapper, TblNbsjSheetEntity> implements TblNbsjSheetService {
    @Autowired
    private TblNbsjSheetMapper tblNbsjSheetMapper;
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
	@Resource
	private TblNbkzRiskMapper tblNbkzRiskMapper;
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	@Autowired
	private ActivityPluginsService activityPluginsService;
	
	@Resource
	private UserProvider userProvider;


	@Override
	public JsonBean dgglPageList(String token, Integer pageNumber, Integer pageSize, TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	//==查询当前实施的项目！
		TblNbsjProject tnp = this.tblNbsjProjectService.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
		if(tnp == null) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		Integer projectId = tnp.getProjectId();
		if(null == projectId) {
			return ResponseFormat.retParam(0,30003,resultMap);
		}
		tBlNbsjSheetVo.setProjectid(projectId);
		TblNbsjProject project = tblNbsjProjectMapper.selectPJById(projectId);
		resultMap.put("project", project);
    	PageInfo<TblNbsjSheetEntity> pageInfo = new PageInfo<TblNbsjSheetEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjSheetMapper.selectListByPageInfo(pageInfo,tBlNbsjSheetVo));
    	pageInfo.setTotalRecord(this.tblNbsjSheetMapper.selectCountByPageInfo(pageInfo,tBlNbsjSheetVo));
    	pageInfo.getTotalPage();
    	String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
    	resultMap.put("identifier", identifier);
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean dgAllPageList(String token, Integer pageNumber, Integer pageSize,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}

		if(pageNumber == null) {
			pageNumber = 1;
		}
		if(pageSize==null) {
			pageSize=15;
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);

		PageInfo<TblNbsjSheetEntity> pageInfo = new PageInfo<TblNbsjSheetEntity>();
//    	tblNbsjSheet.setAuditorgid(loginStaff.getCurrentOrg().getOrgid());
//    	pageInfo.setCondition(tblNbsjSheet);
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setTlist(this.tblNbsjSheetMapper.selectListByPageInfo(pageInfo,tBlNbsjSheetVo));
		pageInfo.setTotalRecord(this.tblNbsjSheetMapper.selectCountByPageInfo(pageInfo,tBlNbsjSheetVo));
		pageInfo.getTotalPage();
		String identifier = activityPluginsService.getoNState(ProcessEnum.SJ_JHGL.name());
		resultMap.put("identifier", identifier);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean findNbsjSheetDetail(String token, Integer sheetid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		TblNbsjSheetEntity plan = this.tblNbsjSheetMapper.selectById(sheetid);
		resultMap.put("sheet", plan);
		TblNbsjProject project = tblNbsjProjectMapper.selectPJById(plan.getProjectId());
		resultMap.put("project", project);
		
		//==
		List<TblNbsjSheetReportEntity> listSP = this.tblNbsjSheetMapper.selectListSheetReport(sheetid);
		if (null != listSP) {
			for (TblNbsjSheetReportEntity sp : listSP) {
				String orgids = sp.getSjdeptIds();
				
				String sysOrgName = "";
				List<TblOrganization> listOrg = this.tblNbkzRiskMapper.selectListOrgInId(orgids);
				if(null != listOrg) {
					for (int i = 0; i < listOrg.size(); i++) {
						TblOrganization org = listOrg.get(i);
						String orgname = org.getOrgname();
						sysOrgName += orgname;
						if((listOrg.size()-1) > i) {
							sysOrgName += ",";
						}
					}
				}
				sp.setSjdeptNames(sysOrgName);
			}
		}
		resultMap.put("listSP", listSP);
		
		return ResponseFormat.retParam(1,200,resultMap);
	}


}
