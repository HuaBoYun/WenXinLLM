package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.RandowUtil;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblNbsjWbProject;
import com.huabo.audit.oracle.entity.TblTesttaskProblemFind;
import com.huabo.audit.oracle.entity.TblZgzzIssuesilist;
import com.huabo.audit.oracle.entity.TblZgzzReport;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjSheetMapper;
import com.huabo.audit.oracle.mapper.TblNbsjWbProjectMapper;
import com.huabo.audit.oracle.mapper.TblRectificationIssuesMapper;
import com.huabo.audit.oracle.mapper.TblTesttaskProblemFindMapper;
import com.huabo.audit.oracle.mapper.TblZgzzIssuesilistMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationimplMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationplanMapper;
import com.huabo.audit.oracle.mapper.TblZgzzReportMapper;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationimplVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;
import com.huabo.audit.oracle.vo.TblZgzzReportVo;
import com.huabo.audit.service.TblNbsjSheetService;
import com.huabo.audit.service.TblZgzzIssuesilistService;
import com.huabo.audit.service.TblZgzzReportService;

@Service
public class TblZgzzReportServiceimpl implements TblZgzzReportService {
	
	@Resource
	private TblZgzzIssuesilistMapper tblZgzzIssuesilistMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblZgzzReportMapper tblZgzzReportMapper;
	
	@Resource
	private TblZgzzRectificationplanMapper tblZgzzRectificationplanMapper;
	
	@Resource
	private TblRectificationIssuesMapper tblRectificationIssuesMapper;
	
	@Resource
	private TblZgzzRectificationimplMapper tblZgzzRectificationimplMapper;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveReport(String token, TblZgzzReport report, String planStrs, String[] attIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        //1.判断ID是否为空，为空新增，不为空修改
        if(StringUtils.isNotBlank(report.getReportid())) {
        	this.tblZgzzReportMapper.updateByPrimaryKeySelective(report);
        }else {
        	report.setLinkorg(loginStaff.getCurrentOrg().getOrgid());
        	report.setCreatedate(new Date());
        	report.setStatus(0);
        	report.setReportid(RandomUtil.uuStringId());
        	report.setCreatestaff(loginStaff.getStaffid());
        	report.setLinkdept(loginStaff.getLinkDetp().getOrgid());
        	this.tblZgzzReportMapper.insertSelective(report);
        }
        
        //2.保存整改落实和整改方案等关系
        //2.1删除之前保存整改方案和整改落实信息
        this.tblZgzzReportMapper.removeIssuesRelation(report.getReportid());
        this.tblZgzzReportMapper.removerectificationPlanRelation(report.getReportid());
        
        //2.2 方案、落实 不为空 保存相关信息；
        if(StringUtils.isNotBlank(planStrs)) {
        	JSONArray array = JSONArray.parseArray(planStrs);
        	JSONObject obj = null;
        	String planId = null;
        	String issuesIdStrs = null;
        	String[] issuesIds = null;
        	for (int i = 0; i < array.size() ; i++ ) {
				obj = array.getJSONObject(i);
				planId = obj.getString("planId");
				issuesIdStrs = obj.getString("issuesId");
				issuesIds = issuesIdStrs.split(",");
				this.tblZgzzReportMapper.saveRectificationPlanRelation(report.getReportid(),planId);
				for (String issId : issuesIds) {
					this.tblZgzzReportMapper.saveIssuesRelation(report.getReportid(),planId,issId,loginStaff.getStaffid());
				}
			}
        }
        
        //3.保存附件关系
        if(attIds != null) {
        	for (String attId : attIds) {
				this.tblZgzzReportMapper.saveFileRelation(attId,report.getReportid());
			}
        }
        
        return ResponseFormat.retParam(1, 200, report);
	}

	@Override
	public JsonBean removeReport(String token, String reportid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
		//1.获取实体判断状态
        TblZgzzReportVo report = this.tblZgzzReportMapper.selectEntityById(reportid);
        
        if(report.getStatus().compareTo(6) == 0 ) {
        	return ResponseFormat.retParam(0, "报告审批已完成", null);
        }
        
        if(report.getStatus().compareTo(0) > 0 ) {
        	return ResponseFormat.retParam(0, "报告审批中，无法删除", null);
        }
        //2删除之前保存整改方案和整改落实信息
        this.tblZgzzReportMapper.removeIssuesRelation(report.getReportid());
        this.tblZgzzReportMapper.removerectificationPlanRelation(report.getReportid());
        
        //3.删除附件关系表中的数据
        this.tblZgzzReportMapper.deleteFileRelation(reportid);
        
        //4.删除实体数据
        this.tblZgzzReportMapper.deleteByPrimaryKey(report.getReportid());
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean removeReportFile(String token, String reportid, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        //附件表不删除保留  中间关系表删除连接关系
        this.tblZgzzReportMapper.deleteFileRelationByAttId(reportid,attId);
        
        return ResponseFormat.retParam(1, 200, null);
		
	}

	@Override
	public JsonBean getReportDetail(String token, String reportid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        TblZgzzReportVo report = this.tblZgzzReportMapper.selectEntityById(reportid);
        
        //查询所有关联的整改方案
        List<TblZgzzRectificationplanVo> planList = this.tblZgzzRectificationplanMapper.selectPlanListByReportId(report.getReportid());
        String planIdStrs = "";
        String planStrs = "";
        
        for (TblZgzzRectificationplanVo plan : planList) {
			planIdStrs += plan.getPlanId()+",";
        	planStrs += plan.getPlanName()+",";
		}
        
        if(planIdStrs.length() > 0) {
        	planIdStrs = planIdStrs.substring(0, planIdStrs.length()-1);
        	planStrs = planStrs.substring(0, planStrs.length()-1);
        }
        
        report.setPlanIdStrs(planIdStrs);
        report.setPlanStrs(planStrs);
        
        //查询所有整改清单的信息
        List<TblZgzzIssuesilistVo> issuesList = this.tblZgzzIssuesilistMapper.selectAllListByReportId(reportid);

        TblZgzzIssuesilistVo tzo = null;
        for (int i = 0; i < issuesList.size(); i++) {
        	tzo = issuesList.get(i);
        	String planid = tzo.getPlanId();
        	String issuesid = tzo.getIssuesId();
        	String relaId = this.tblRectificationIssuesMapper.selectRelaIdByPlanIdIssuesId(planid,issuesid);
        	//整改方案报告-整改清单-整改方案
        	TblRectificationIssuesVo rela = this.tblRectificationIssuesMapper.selectEntityById(relaId);
        	if(null!= rela) {
        		tzo.setRectificationPlan(rela.getRectificationPlan());
        	}
        	//整改落实报告-整改清单-整改方案
        	List<TblZgzzRectificationimplVo> implList = this.tblZgzzRectificationimplMapper.selectEntityByRelaId(relaId);
        	if(implList != null && implList.size() > 0) {
        		tzo.setSituationoverView(implList.get(0).getSituationoverView());
        	}
		}
        
        report.setIssuesList(issuesList);
        
        //查询所有附件信息
        List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListbyZgzzReportId(reportid);
        report.setAttList(attList);
        
        return ResponseFormat.retParam(1, 200, report);
	}

	@Override
	public JsonBean getReportList(String token, TblZgzzReportVo report) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        PageInfo<TblZgzzReportVo> pageInfo = new PageInfo<TblZgzzReportVo>();
        pageInfo.setPageSize(report.getPageSize());
        pageInfo.setCurrentPage(report.getPageNumber());
       
        report.setCreatestaff(loginStaff.getStaffid());
        report.setDeptIds(loginStaff.getDeptIds());
        
        report.setUseSecrect(loginStaff.getCurrentOrg().getUseSecrect());
        report.setSecrectStaff(loginStaff.getStaffid());
        report.setSecrectScopeIds(loginStaff.getSecrectScopeIds());
        
        pageInfo.setCondition(report);
        
        com.github.pagehelper.PageInfo<TblZgzzReportVo> page = PageMethod.startPage(report.getPageNumber(), report.getPageSize()).doSelectPageInfo(() -> this.tblZgzzReportMapper.selectListByPageInfo(report,loginStaff));
        
        
        
        pageInfo.setTlist(page.getList());
		pageInfo.setTotalRecord((int)page.getTotal());
		
		return ResponseFormat.retParam(1, 200, pageInfo);
	}
	
	//核实报告上传会议文件列表
    @Override
    public JsonBean getReportMeetFileList(String token, String reportid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", tblZgzzReportMapper.selectReportMeetFileList(reportid));
        return ResponseFormat.retParam(1, 200, resultMap);
    }
	
    //核实报告上传会议文件
    @Override
    public JsonBean uploadattbyid(String reportid, String attId) throws Exception {
    	tblZgzzReportMapper.insertMeetAttInfo(reportid, attId);
        TblAttachment att = this.tblZgzzReportMapper.selectAttEntityById(attId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", att);
        return ResponseFormat.retParam(1, 200, resultMap);
    }
    //核实报告删除会议文件
    @Override
    public JsonBean delattbyid(String attid, String token) throws Exception {
    	 TblStaffUtil loginStaff = userProvider.get();
    	 if (loginStaff == null) {
             return ResponseFormat.retParam(0, 20006, null);
         }
    	
    	 tblZgzzReportMapper.deleteMeetFileInfoByAttId(attid);
        this.tblZgzzReportMapper.deleteEntity(new BigDecimal(attid));
        return ResponseFormat.retParam(1, 200, null);
    }

}
