package com.huabo.audit.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.ExportUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.PageInfo;
import com.hbfk.util.RandowUtil;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblFlowardRecord;
import com.huabo.audit.oracle.entity.TblOrgNoId;
import com.huabo.audit.oracle.entity.TblRectificationIssues;
import com.huabo.audit.oracle.entity.TblRelationSheet;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.entity.TblZgzzIssuesilist;
import com.huabo.audit.oracle.entity.TblZgzzRctevaluation;
import com.huabo.audit.oracle.entity.TblZgzzRectificationplan;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblFlowardRecordMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjWbProjectMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblRectificationIssuesMapper;
import com.huabo.audit.oracle.mapper.TblRelationSheetMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.mapper.TblZgzzIssuesilistMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRctevaluationMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationimplMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationplanMapper;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import com.huabo.audit.oracle.vo.TblZgzzProjectVo;
import com.huabo.audit.oracle.vo.TblZgzzRctevaluationVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationimplVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;
import com.huabo.audit.service.TblOrganizaService;
import com.huabo.audit.service.TblZgzzRectificationplanService;
import com.huabo.audit.vo.param.MaxNumberParam;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.service.impl
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:02
 */
@Service
public class TblZgzzRectificationplanServiceimpl implements TblZgzzRectificationplanService {

	@Resource
	private TblZgzzIssuesilistMapper tblZgzzIssuesilistMapper;
	
	@Resource
	private TblNbsjWbProjectMapper tblNbsjWbProjectMapper;

	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Resource
	private TblZgzzRectificationplanMapper tblZgzzRectificationplanMapper;
	
	@Resource
	private TblRectificationIssuesMapper tblRectificationIssuesMapper;
	
	@Resource
	private TblRelationSheetMapper tblRelationSheetMapper;
	
	@Resource
	private TblZgzzRectificationimplMapper tblZgzzRectificationimplMapper;
	
	@Resource
	private TblZgzzRctevaluationMapper tblZgzzRctevaluationMapper;
	
	@Resource
	private TblOrganizationMapper organizationMapper;
	
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Resource
    private UserProvider userProvider;
	
	
	@Resource
	private TblStaffMapper  tblStaffMapper;
	
	@Resource
	private TblFlowardRecordMapper tblFlowardRecordMapper;
	
	@Override
	public JsonBean getSolutionProjectList(String token, TblZgzzProjectVo project, Integer planType) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		PageInfo<TblZgzzProjectVo> pageInfo = new PageInfo<TblZgzzProjectVo>();
		pageInfo.setPageSize(project.getPageSize());
		pageInfo.setCurrentPage(project.getPageNum());
		pageInfo.setCondition(project);
		
		com.github.pagehelper.PageInfo<TblZgzzProjectVo> page = null;
       
		
		//根据不同的方案类别 获取所属的对应的项目信息
		switch (planType) {
		case 1:
			//获取审计项目信息
			page = PageMethod.startPage(project.getPageNum(), project.getPageSize()).doSelectPageInfo(() -> this.tblNbsjProjectMapper.selectPageInfoListByRectification(project));
			break;
		case 2:
			//获取内控项目信息
			page = PageMethod.startPage(project.getPageNum(), project.getPageSize()).doSelectPageInfo(() -> this.tblZgzzRectificationplanMapper.selectPageInfoNkProjectList(project));
			break;
		case 3:
		case 4:
			//获取外部项目信息
			page = PageMethod.startPage(project.getPageNum(), project.getPageSize()).doSelectPageInfo(() -> this.tblNbsjWbProjectMapper.selectPageInfoListByRectification(project,planType));
			break;
		default:
			return ResponseFormat.retParam(0,"方案类别错误，无法获取项目信息",null);
		}
		
		pageInfo.setTlist(page.getList());
	    pageInfo.setTotalRecord((int)page.getTotal());
		return ResponseFormat.retParam(1,200,pageInfo);
	}


	@Override
	public JsonBean saveRectificationPlan(String token, TblZgzzRectificationplan rectification) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//1. 判断方案编号是否重复 , 以公司为单位
		rectification.setLinkOrgId(loginStaff.getCurrentOrg().getOrgid());
		Integer count = this.tblZgzzRectificationplanMapper.checkRepeatPlanCode(rectification);
		if(count.compareTo(0)> 0) {
			return ResponseFormat.retParam(0,"方案编号重复",null);
		}
		//2.判断方案名称是否重复，以公司为单位
		count = this.tblZgzzRectificationplanMapper.checkRepeatPlanName(rectification);
		if(count.compareTo(0)> 0) {
			return ResponseFormat.retParam(0,"方案名称重复",null);
		}
		
		//根据主键ID判断新增还是修改
		if(rectification.getPlanId() != null) {
			TblZgzzRectificationplanVo vo = tblZgzzRectificationplanMapper.selectEntityById(rectification.getPlanId().toString());
			if(vo.getZgfs()!=null && vo.getZgfs().equals("0") && rectification.getStatus()==7) {
				rectification.setStatus(9);
			}
			
			
			//主键不为空 进行修改
			rectification.setUpdateStaff(loginStaff.getStaffid());
			rectification.setUpdateTime(new Date());
			this.tblZgzzRectificationplanMapper.updateByPrimaryKeySelective(rectification);
		}else {
			if(rectification.getZgfs()!=null && rectification.getZgfs().equals("0")) {
				rectification.setResponse(loginStaff.getStaffid());
				rectification.setHandlerId(loginStaff.getStaffid());
			}
			
			//主键为空 进行新增
			rectification.setLinkOrgId(loginStaff.getCurrentOrg().getOrgid());
			rectification.setLinkDeptId(loginStaff.getLinkDetp().getOrgid());
			rectification.setCreateStaff(loginStaff.getStaffid());
			rectification.setCreateTime(new Date());
			rectification.setStatus(0);
			rectification.setPlanId(RandomUtil.uuBigDecimalId());
			this.tblZgzzRectificationplanMapper.insertSelective(rectification);
		}
		
		//循环保存附件关系
		if(rectification.getAttIds() != null) {
			for (String attId : rectification.getAttIds()) {
				this.tblZgzzRectificationplanMapper.insertAttFileRelation(rectification.getPlanId().toString(),attId);
			}
		}
		
		//循环保存整改清单与整改列表问题
		if(rectification.getRelaList() != null && rectification.getRelaList().size() > 0) {
			for (TblRectificationIssues issues : rectification.getRelaList()) {
				if(issues.getRelaId() != null) {
					this.tblRectificationIssuesMapper.updateByPrimaryKeySelective(issues);
				}else {
					issues.setRelaId(RandomUtil.uuBigDecimalId());
					issues.setPlanId(rectification.getPlanId().toString());
					issues.setStatus(0);
					issues.setVersion(1);
					issues.setCreateTime(new Date());
					this.tblRectificationIssuesMapper.insertSelective(issues);
				}
				if(issues.getAttIds() != null) {
					for (String attid : issues.getAttIds()) {
						this.tblRectificationIssuesMapper.saveFileRelation(attid,issues.getRelaId().toString());
					}
				}
				//修改关联的整改清单问题 状态
				this.tblZgzzIssuesilistMapper.updateStatusBySavePlan(issues.getIssuesId(),7);
			}
		}
		return ResponseFormat.retParam(1,200,rectification);
	}
	
	@Override
	public JsonBean getRectificationPlanDetail(String token, String planId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		TblZgzzRectificationplanVo rectification = this.tblZgzzRectificationplanMapper.selectEntityById(planId);
		
		//查询相关的整改清单
		TblRectificationIssuesVo issues = new TblRectificationIssuesVo();
		issues.setPlanId(planId);
		List<TblRectificationIssuesVo> issuesList = this.tblRectificationIssuesMapper.selectListByExample(issues);
		
		rectification.setIssuesList(issuesList);
		
		//查询整改方案 相关的附件列表信息
		List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListbyRectification(planId);
		rectification.setAttList(attList);
		
		return ResponseFormat.retParam(1,200,rectification);
	}


	@Override
	public JsonBean removeRectificationFile(String token, String planId, String attId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		// 删除整改方案与此附件的关系表中的数据
		this.tblZgzzRectificationplanMapper.deleteRectificationFileRela(planId,attId);
		return ResponseFormat.retParam(1,200,null);
	}


	@Override
	public JsonBean removeRectificationIssues(String token, String planId, String issuesId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//查询获取所有整改方案与整改清单的关系主键
		List<String> relaIdList = this.tblRectificationIssuesMapper.selectIdByIssuesIdPlanId(planId,issuesId);
		
		
		for (String relaId : relaIdList) {
			//删除关系表在组织用户关系表中 协办部门的关系
			this.tblRelationSheetMapper.deleteRelation(relaId,TblRectificationIssues.FORMTYPE,TblRectificationIssues.FORMCOL,TblRelationSheet.ORGTYPE);
		}
		
		
		// 删除整改方案与此附件的关系表中的数据
		this.tblZgzzRectificationplanMapper.deleteRectificationIssuesRela(planId,issuesId);
		
		//将关联的整改清单 状态还原，
		this.tblZgzzIssuesilistMapper.deleteRelationPlanRecoverStatus(issuesId);
		return ResponseFormat.retParam(1,200,null);
	}


	@Override
	public JsonBean removeRectification(String token, String planId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//查询整改方案 相关的附件列表信息
		List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListbyRectification(planId);
		
		//循环删除ftp上的文件以及数据库的主体信息
		if(attList != null) {
			for (TblAttachment att : attList) {
				FtpUtil.removeFile(att.getAttpath());
				this.tblAttachmentMapper.deleteEntity(att.getAttid());
			}
		}
		
		//删除整改方案与附件的中间关系表数据
		this.tblZgzzRectificationplanMapper.deleteRectificationFileRelaByRid(planId);
		
		//删除整改方案清单中间关系表关联的组织用户数据
		this.tblZgzzRectificationplanMapper.deleteRelationSheet(planId,TblRectificationIssues.FORMTYPE,TblRectificationIssues.FORMCOL,TblRelationSheet.ORGTYPE);
		
		//还原 该方案关联的整改清单状态
		this.tblZgzzIssuesilistMapper.deleteRelationPlanRecoverListStatus(planId);
		
		//删除整改方案与整改清单中间关系表数据
		this.tblZgzzRectificationplanMapper.deleteRectificationIssuesRelaByRid(planId);
		
		//删除整改方案实体信息；
		this.tblZgzzRectificationplanMapper.deleteByPrimaryKey(planId);
		
		return ResponseFormat.retParam(1,200,null);
	}


	@Override
	public JsonBean getRectificationStatus(String token, String planId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		Integer status = this.tblZgzzRectificationplanMapper.selectStatusById(planId);
		
		//校验方案状态
		if(status.compareTo(0) > 0 && status.compareTo(6) < 0) {
			return ResponseFormat.retParam(0,"方案审批中",null);
		}else if(status.compareTo(6) == 0) {
			return ResponseFormat.retParam(0,"方案审批完成",null);
		}else if(status.compareTo(7) == 0) {
			return ResponseFormat.retParam(0,"方案已下发",null);
		}else if(status.compareTo(8) == 0) {
			return ResponseFormat.retParam(0,"方案已分派",null);
		}
		
		return ResponseFormat.retParam(1,200,null);
	}


	@Override
	public JsonBean getRectificationPlanList(TblStaffUtil loginStaff, TblZgzzRectificationplanVo plan) throws Exception {
		
		//封装分页查询工具类
		PageInfo<TblZgzzRectificationplanVo> pageInfo = new PageInfo<TblZgzzRectificationplanVo>();
		pageInfo.setCurrentPage(plan.getPageNumber());
		pageInfo.setPageSize(plan.getPageSize());
		
		//判断如果是整改评价查询 则将添加查询条件
		//获取所有整改落实信息 审批完成的整改方案
		if(plan.getSelectType() != null) {
			if(plan.getSelectType().compareTo(3) == 0) {
				plan.setStatus(9); //查询开始整改的方案
				plan.setResponse(loginStaff.getStaffid());//设置整改方案的查询条件整改责任人 意为整改分派查询 查询自己的分派方案
				pageInfo.setSqlStr(new StringBuffer(" AND TZR.PLANID IN (SELECT PLANID FROM TBL_RECTIFICATION_ISSUES WHERE PLANID = TZR.PLANID AND IMPLEMENTER IS NOT NULL )").toString());
			}else {
				plan.setCreateStaff(loginStaff.getStaffid());
			}
			plan.setDeptIds(loginStaff.getDeptIds());
			if(plan.getSelectType().compareTo(2) == 0) {
				pageInfo.setSqlStr(new StringBuffer(" AND TZR.STATUS IN (7,9) ").toString());
				plan.setResponse(loginStaff.getStaffid());
			}
		}
		plan.setCreateStaff(loginStaff.getStaffid());
		plan.setUseSecrect(loginStaff.getCurrentOrg().getUseSecrect());
		plan.setSecrectStaff(loginStaff.getStaffid());
		plan.setSecrectScopeIds(loginStaff.getSecrectScopeIds());
		
		pageInfo.setCondition(plan);
		
		com.github.pagehelper.PageInfo<TblZgzzRectificationplanVo> page = PageMethod.startPage(plan.getPageNumber(), plan.getPageSize()).doSelectPageInfo(() -> this.tblZgzzRectificationplanMapper.selectPageInfoList(plan,pageInfo.getSqlStr(),loginStaff));
        
		//分页查询
		List<TblZgzzRectificationplanVo> planList = page.getList();
		
		//判断为整改分派查询
		if(plan.getSelectType() != null && (plan.getSelectType().compareTo(2) == 0 || plan.getSelectType().compareTo(4) == 0)) {
			//整改分派查询 获取整改分派，总分派记录数，已分派记录数，未分派记录数
			TblRectificationIssuesVo issues = null;
			List<TblRectificationIssuesVo> issuesList = null;
			List<TblRectificationIssuesVo> allocatedList = null;//已分派记录数集合
			Integer totalRecord = 0;//总记录数
			Integer allocatedRecord = 0; //已分派记录数
			for (TblZgzzRectificationplanVo vo : planList) {
				issues = new TblRectificationIssuesVo();
				issues.setPlanId(vo.getPlanId());
				issuesList = this.tblRectificationIssuesMapper.selectListByExample(issues);
				totalRecord = issuesList.size();
				allocatedList = issuesList.stream().filter(item -> item.getImplementer() != null).collect(Collectors.toList());
				allocatedRecord = allocatedList.size();
				vo.setTotalRecord(totalRecord);
				vo.setAllocatedRecord(allocatedRecord);
				vo.setUnassignedRecord(totalRecord - allocatedRecord);
			}
		}else if(plan.getSelectType() != null && plan.getSelectType().compareTo(3) == 0) {
			// 判断为整改评价查询，获取总评价记录数，已评价记录数，未评价记录数
			TblRectificationIssuesVo issues = null;
			List<TblRectificationIssuesVo> issuesList = null;
			Integer totalRecord = 0;//总评价记录数
			Integer allocatedRecord = 0; //已评价记录数
			for (TblZgzzRectificationplanVo vo : planList) {
				issues = new TblRectificationIssuesVo();
				issues.setPlanId(vo.getPlanId());
				issuesList = this.tblRectificationIssuesMapper.selectListByExample(issues);
				totalRecord = issuesList.size();
				allocatedRecord = this.tblZgzzRctevaluationMapper.selectAllocatedList(vo.getPlanId());
				vo.setTotalRecord(totalRecord); //总评价记录数
				vo.setAllocatedRecord(allocatedRecord);//已评价记录数
				vo.setUnassignedRecord(totalRecord - allocatedRecord);//未评价记录数
			}
		}
		//封装分页实体返回数据
		pageInfo.setTlist(planList);
		pageInfo.setTotalRecord((int)page.getTotal());
		
		return ResponseFormat.retParam(1,200,pageInfo);
	}

	@Override
	public JsonBean getIssuesDetailByPlan(String token, String relaId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//查询 方案与清单关系表中填写的数据
		TblRectificationIssuesVo rela = this.tblRectificationIssuesMapper.selectEntityById(relaId);
		
		rela.setAttList(this.tblAttachmentMapper.selectAttListbyRelaId(rela.getRelaId()));
		
		if(StringUtils.isNotBlank(rela.getIssuesId())) {
			//查询获取 整改清单的详情数据
			TblZgzzIssuesilistVo issues = this.tblZgzzIssuesilistMapper.selectEntityById(rela.getIssuesId());
			List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListbyIssuesId(issues.getIssuesId());
	        issues.setAttList(attList);
			rela.setIssues(issues);
		}
		
		if(StringUtils.isNotBlank(rela.getIssuesId())) {
			//查询获取 整改方案的详情数据
			TblZgzzRectificationplanVo plan = this.tblZgzzRectificationplanMapper.selectEntityById(rela.getPlanId());
			rela.setPlan(plan);
		}
		
		//从业务表单组织用户关系表中 获取关联的承办部门信息；
		List<String> orgIdList = this.tblRelationSheetMapper.selectObjectIdListByForm(rela.getRelaId(),TblRectificationIssues.FORMTYPE,TblRectificationIssues.FORMCOL,TblRelationSheet.ORGTYPE);
		if(orgIdList != null && orgIdList.size() > 0) {
			String orgIds = orgIdList.stream().collect(Collectors.joining(","));
			//通过组织主键拼接获取所有的组织名称
			List<String> orgNameList = this.tblRelationSheetMapper.selectOrgNameListByOrgIds(orgIds);
			rela.setCborgIds(orgIds);
			rela.setCborgNames(orgNameList.stream().collect(Collectors.joining(",")));
		}
		
		//查找我的整改落实信息
		List<TblZgzzRectificationimplVo> implList = this.tblZgzzRectificationimplMapper.selectEntityByRelaId(rela.getRelaId());
		
		if(implList != null && implList.size() > 0) {
			TblZgzzRectificationimplVo impl = implList.get(0);
			//查询整改方案 相关的附件列表信息
			List<TblAttachment> implAttList = this.tblAttachmentMapper.selectAttListbyRectificationImpl(impl.getImplId());
			impl.setAttList(implAttList);
			rela.setReimpl(impl);
			
			//查询获取整改评价的数据
			TblZgzzRctevaluationVo eval = this.tblZgzzRctevaluationMapper.selectEntityByImplId(impl.getImplId());
			if(eval != null) {
				List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListByZgzzEvaluation(eval.getEvalId());
				eval.setAttList(attList);
				rela.setValua(eval);
			}
		}
		
		return ResponseFormat.retParam(1,200,rela);
	}


	@Override
	public JsonBean saveIssuesRelaPlan(String token, TblRectificationIssues rela, String[] orgIds) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//保存中间表实体数据
		this.tblRectificationIssuesMapper.updateByPrimaryKeySelective(rela);
		
		//删除之前保存组织用户中间关系表的数据；
		this.tblRelationSheetMapper.deleteRelation(rela.getRelaId().toString(), TblRectificationIssues.FORMTYPE, TblRectificationIssues.FORMCOL, TblRelationSheet.ORGTYPE);
		
		//保存与组织的中间关系表数据
		if(orgIds != null) {
			TblRelationSheet sheet = null;
			int i = 1 ; //排序
			for (String orgId : orgIds) {
				sheet = new TblRelationSheet();
				sheet.setFid(new BigDecimal(RandowUtil.uuId()));
				sheet.setFormid(rela.getRelaId().toString());
				sheet.setFormtype(TblRectificationIssues.FORMTYPE);
				sheet.setFormcol(TblRectificationIssues.FORMCOL);
				sheet.setObjectid(new BigDecimal(orgId));
				sheet.setObjtype(TblRelationSheet.ORGTYPE);
				sheet.setSort(i);
				this.tblRelationSheetMapper.insertSelective(sheet);
			}
		}
		return ResponseFormat.retParam(1,200,rela);
	}


	@Override
	public JsonBean getRectificationAllocationIssuesList(String token, TblRectificationIssuesVo issues) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//封装分页查询实体
		PageInfo<TblRectificationIssuesVo> pageInfo = new PageInfo<TblRectificationIssuesVo>();
		
		pageInfo.setPageSize(issues.getPageSize());
		pageInfo.setCurrentPage(issues.getPageNum());
		pageInfo.setCondition(issues);
		
		//查询获取分页返回数据
		com.github.pagehelper.PageInfo<TblRectificationIssuesVo> page = PageMethod.startPage(issues.getPageNum(), issues.getPageSize()).doSelectPageInfo(() -> this.tblRectificationIssuesMapper.selectPageInfoListByRectificaionAllocation(issues));
		
		pageInfo.setTlist(page.getList());
		pageInfo.setTotalRecord((int) page.getTotal());
		
		return ResponseFormat.retParam(1,200,pageInfo);
	}


	@Override
	public JsonBean saveIssuesRelaImpementer(String token, BigDecimal impementerId, String relaId)
			throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		
		
		
		//保存整改清单与整改方案中间表的整改落实人信息
		TblRectificationIssues reiss = new TblRectificationIssues();
		reiss.setImplementer(impementerId);
		reiss.setRelaId(new BigDecimal(relaId));
		this.tblRectificationIssuesMapper.updateByPrimaryKeySelective(reiss);
		
		//获取原来的整改落实人员信息
		if(impementerId!=null) {
			TblStaff staff = tblStaffMapper.getById(impementerId.toString());
			TblFlowardRecord record=new TblFlowardRecord();
			record.setRecordid(RandomUtil.uuBigDecimalId());
			record.setCreatedate(new Date());
			record.setZfrename(loginStaff.getRealname());
			record.setZfstaffid(loginStaff.getStaffid().toString());
			record.setJsstaffid(staff.getStaffid().toString());
			record.setJsrename(staff.getRealname());
			record.setFormid(new BigDecimal(relaId));
			tblFlowardRecordMapper.insertSelective(record);
		}
		
		//如果最后一个整改落实人已分派 则修改整改方案状态 为已分派完成  开始整改
		
		//获取当前关联的整改方案主键
		reiss = this.tblRectificationIssuesMapper.selectByPrimaryKey(new BigDecimal(relaId));
		
		//获取 当前整改方案下 未分派的整改清单数量
		Integer count = this.tblRectificationIssuesMapper.selectUnassignedRecordCount(reiss.getPlanId());
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		if(count.compareTo(0) == 0) {
			//该方案下所有整改清单均已分派完 整改落实人 修改整改方案状态
			TblZgzzRectificationplan plan = new TblZgzzRectificationplan();
			plan.setPlanId(new BigDecimal(reiss.getPlanId()));
			plan.setStatus(9);
			this.tblZgzzRectificationplanMapper.updateByPrimaryKeySelective(plan);
			TblRectificationIssuesVo issues = new TblRectificationIssuesVo();
			issues.setPlanId(reiss.getPlanId());
			List<TblRectificationIssuesVo> issuesList = this.tblRectificationIssuesMapper.selectListByExample(issues);
			resultMap.put("isSend", 1);//通知前端调用设置模块 业务单据下发事项保存接口 formId - relaId 业务表单Id、reciver - implementer 接收人 、 issuesName - distributionTitle 下发事项标题 ，formType=2
			resultMap.put("issuesList", issuesList);//通知事项集合
		}else {
			resultMap.put("isSend", 0);//未全部分派 不需要调用业务单据下发事项保存接口
		}
		
		return ResponseFormat.retParam(1,200,null);
	}


	@Override
	public JsonBean getMyRectificationList(String token, TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		plan.setStatus(9);//查找开始整改方案的问题清单
		reiss.setImplementer(loginStaff.getStaffid());//查找属于当前登录人的整改清单
		
		//封装分页查询实体
		PageInfo<TblRectificationIssuesVo> pageInfo = new PageInfo<TblRectificationIssuesVo>();
		pageInfo.setPageSize(reiss.getPageSize());
		pageInfo.setCurrentPage(reiss.getPageNum());
		reiss.setPlan(plan);
		reiss.setIssues(issues);
		pageInfo.setCondition(reiss);
		
		
		com.github.pagehelper.PageInfo<TblRectificationIssuesVo> page = PageMethod.startPage(reiss.getPageNum(), reiss.getPageSize()).doSelectPageInfo(() -> this.tblRectificationIssuesMapper.selectMyRectificationPageInfoList(reiss));
        
		//查询分页数据
		List<TblRectificationIssuesVo> reisList = page.getList();
		
		//循环查找出来的 整改清单  将协办部门信息放入
		List<String> orgIdList = null;
		String orgIds = null;
		List<String> orgNameList = null;
		for (TblRectificationIssuesVo reis : reisList) {
			orgIdList = this.tblRelationSheetMapper.selectObjectIdListByForm(reis.getRelaId(),TblRectificationIssues.FORMTYPE,TblRectificationIssues.FORMCOL,TblRelationSheet.ORGTYPE);
			if(orgIdList != null && orgIdList.size() > 0) {
				orgIds = orgIdList.stream().collect(Collectors.joining(","));
				//通过组织主键拼接获取所有的组织名称
				orgNameList = this.tblRelationSheetMapper.selectOrgNameListByOrgIds(orgIds);
				reis.setCborgIds(orgIds);
				reis.setCborgNames(orgNameList.stream().collect(Collectors.joining(",")));
			}
		}
		
		//查询总记录数
		pageInfo.setTotalRecord((int)page.getTotal());
		pageInfo.setTlist(reisList);
		
		return ResponseFormat.retParam(1,200,pageInfo);
	}


	@Override
	public JsonBean getUnresolvedIssuesList(String token, TblRectificationIssuesVo issues) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//封装分页查询实体
		PageInfo<TblRectificationIssuesVo> pageInfo = new PageInfo<TblRectificationIssuesVo>();
		issues.getIssues().setCreateStaff(loginStaff.getStaffid());
		issues.getIssues().setDeptIds(loginStaff.getDeptIds());
		pageInfo.setPageSize(issues.getPageSize());
		pageInfo.setCurrentPage(issues.getPageNumber());
		pageInfo.setCondition(issues);
		
		com.github.pagehelper.PageInfo<TblRectificationIssuesVo> page = PageMethod.startPage(issues.getPageNumber(), issues.getPageSize()).doSelectPageInfo(() -> this.tblRectificationIssuesMapper.selectPageInfoListByRectificaionAllocation(issues));
        
		//查询获取分页返回数据
		pageInfo.setTlist(page.getList());
		pageInfo.setTotalRecord((int)page.getTotal());
		
		return ResponseFormat.retParam(1,200,pageInfo);
	}


	@Override
	public JsonBean getRectificationIssuesLedgetList(String token, TblRectificationIssuesVo issues, Integer isAll) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
				
		PageInfo<TblRectificationIssuesVo> pageInfo = new PageInfo<TblRectificationIssuesVo>();
		issues.getIssues().setCreateStaff(loginStaff.getStaffid());
		issues.getIssues().setDeptIds(loginStaff.getDeptIds());
		pageInfo.setPageSize(issues.getPageSize());
		pageInfo.setCurrentPage(issues.getPageNum());
		pageInfo.setCondition(issues);
		
		pageInfo.setTlist(this.tblRectificationIssuesMapper.selectRectificationIssuesLedgetPageInfoList(pageInfo,isAll));
		pageInfo.setTotalRecord(this.tblRectificationIssuesMapper.selectRectificationIssuesLedgetPageInfoCount(pageInfo,isAll));
		return ResponseFormat.retParam(1,200,pageInfo);
	}


	@Override
	public JsonBean exportIssuesLedgetList(String token, TblRectificationIssuesVo issues, HttpServletResponse response,
			Integer isAll) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		issues.getIssues().setCreateStaff(loginStaff.getStaffid());
		issues.getIssues().setDeptIds(loginStaff.getDeptIds());
		
		return null;
	}


	@Override
	public JsonBean exportRectificationPlanLedger(TblStaffUtil loginStaff, TblZgzzRectificationplanVo plan,
			HttpServletResponse response) throws Exception {
				//验证用户登录是否失效
				//设置默认导出开始时间和结束时间 默认为当前年份1月1日起 在至 当前时间；
//				if(plan.getCreateTimeStart() == null) {
//			        Calendar calendar = Calendar.getInstance();
//			        int year = calendar.get(Calendar.YEAR);
//			        String startDate = year+"-01-01";
//			        plan.setCreateTimeStart(DateUtil.formatDate(startDate, DateUtil.DATE_SMALL_STR));
//				}
//				if(plan.getCreateTimeEnd() == null) {
//					plan.setCreateTimeEnd(new Date());
//				}
				
				//1.根据输入条件获取所选择的整改台账列表 为空默认所有方案导出
				List<TblZgzzRectificationplanVo> planList = this.tblZgzzRectificationplanMapper.selectAllListByExportt(plan);
				
		        
		        if(planList == null) {
		        	return ResponseFormat.retParam(0, 50007, null);
		        }
		        
		        List<Object[]> objList = new ArrayList<Object[]>(0);
		        Object[] objs = null;
//		        String[] titles = {"整改方案编号", "整改方案名称 ","方案类别","项目名称","创建人","截止时间","整改经办人","整改责任人",  "方案状态","备注", "创建时间"};
		        String[] titles = {"整改方案编号", "整改方案名称 ","方案类别","项目名称","创建人","截止时间","整改经办人","整改责任人",  "方案状态","备注"};
		        for (TblZgzzRectificationplanVo vo : planList) {
					objs = new Object[10];
					objs[0] = vo.getPlanCode();
					objs[1] = vo.getPlanName();
					objs[2] = vo.getPlanTypeStr(vo.getPlanType());
					
					if(vo.getPlanType()==1) {
						//审计
						objs[3] = vo.getSjname();
					}
					if(vo.getPlanType()==2) {
						//内控
						objs[3] = vo.getNkname();
					}
					if(vo.getPlanType()==3) {
						//外部
						objs[3] = vo.getWbname();
					}
					objs[4] = vo.getCreateStaffName();
					objs[5] = DateUtil.parseDate(vo.getDeadlineTime(), DateUtil.DATE_SMALL_STR);
					objs[6] = vo.getHandlerName();
					objs[7] = vo.getZrrRealName();
					objs[8] = vo.getStatuseStr(vo.getStatus());
					objs[9] = vo.getPlanMemo();
//					objs[10] = DateUtil.parseDate(vo.getCreateTime(), DateUtil.DATE_SMALL_STR);
					objList.add(objs);
				}
		        
		        //导出设置 excel表头
		        
		        response.setContentType("application/binary;charset=UTF-8");
				response.setHeader("Content-disposition", "attachment; filename=" + new String("整改方案台账".getBytes(), "iso-8859-1") + ".xls");
				ImportOrExportExcelUtil.exportExcel(titles, objList, response.getOutputStream(), null);
				return ResponseFormat.retParam(1, 200, null);
	}
	
	@Override
	public JsonBean getRectificationPlanListByReportType(TblStaffUtil loginStaff, TblZgzzRectificationplanVo plan,
			Integer reporttype) throws Exception {
		
		//封装分页查询工具类
		PageInfo<TblZgzzRectificationplanVo> pageInfo = new PageInfo<TblZgzzRectificationplanVo>();
		pageInfo.setCurrentPage(plan.getPageNumber());
		pageInfo.setPageSize(plan.getPageSize());
		
		plan.setUseSecrect(loginStaff.getCurrentOrg().getUseSecrect());
		plan.setSecrectStaff(loginStaff.getStaffid());
		plan.setSecrectScopeIds(loginStaff.getSecrectScopeIds());
		
		//判断是否是审计经办人；
		boolean flag = JudgeRoleRight.judgeRoleRight("审计经办人", loginStaff.getRoleNames());
		
		/**
		 * 	落实人：整改报告可以选择自己落实的报告
			整改责任人：整改报告选择方案报告，可以选择自己负责的方案，选择落实报告，可以选择自己分派的方案所对应的问题。
			审计部创建人：可以选择自己创建的方案创建方案报告，也可以选择自己创建方案对应的落实问题，创建落实报告。
			审计经办人：部门超大权限，无论是自己创建的还是别人创建的，所有方案都可以选择，可根据需要创建方案报告和落实报告
			
			sql 语句直接在service层处理
		 */
		if(reporttype.compareTo(2) == 0) {
			//整改落实报告
			if(!flag) {
				pageInfo.setSqlStr(new StringBuffer(" AND ( TZR.PLANID IN (SELECT PLANID FROM TBL_RECTIFICATION_ISSUES WHERE IMPLEMENTER =  ").append(loginStaff.getStaffid()).append(") OR TZR.RESPONSE = ")
						.append(loginStaff.getStaffid()).append(" OR TZR.CREATESTAFF = ").append(loginStaff.getStaffid()).append(" ) ").toString());
				
			}else {
				//s审计经办人直接查询所有
				/*plan.setCreateStaff(loginStaff.getStaffid());
				String deptIds = this.getAllDeptIdStrsByOrgId(loginStaff.getCurrentOrg().getOrgid().toString(),null);
				plan.setDeptIds(deptIds);*/
			}
		}else {
			//整改方案报告
			if(!flag) {
				pageInfo.setSqlStr(new StringBuffer(" AND ( TZR.RESPONSE = ").append(loginStaff.getStaffid()).append(" OR TZR.CREATESTAFF = ").append(loginStaff.getStaffid()).append(" ) ").toString());
			}
		}
		
		pageInfo.setCondition(plan);
		//分页查询
		com.github.pagehelper.PageInfo<TblZgzzRectificationplanVo> page = PageMethod.startPage(plan.getPageNumber(), plan.getPageSize()).doSelectPageInfo(() -> this.tblZgzzRectificationplanMapper.selectPageInfoList(plan,pageInfo.getSqlStr(),loginStaff));
        
		
		List<TblZgzzRectificationplanVo> planList = page.getList();
		List<TblRectificationIssuesVo> issuesList = null;
		TblRectificationIssuesVo issues = null;
		for (TblZgzzRectificationplanVo planVo : planList) {
			//获取整改落实清单数据
			issues = new TblRectificationIssuesVo();
			issues.setPlanId(planVo.getPlanId());
			//如果不是 审计经办人角色，并且不是整改责任人 并且不是 整改方案创建人  则只能查看落实给自己的落实信息
			if(reporttype.compareTo(2) == 0 && !flag  && loginStaff.getStaffid().compareTo(planVo.getCreateStaff()) != 0 && planVo.getResponse() != null && loginStaff.getStaffid().compareTo(planVo.getResponse()) != 0  ) {
				//整改落实报告只查看落实人自己的
				issues.setImplementer(loginStaff.getStaffid());
			}
			issues.setHandlerId(loginStaff.getStaffid());//整改报告关联问题清单的创建人，先用经办人代替
			
			issuesList = this.tblRectificationIssuesMapper.selectListByReport(issues);
			if(issuesList != null && issuesList.size() > 0) {
				planVo.setIssuesList(issuesList);
			}
		}
		
		//封装分页实体返回数据
		pageInfo.setTlist(planList);
		pageInfo.setTotalRecord((int)page.getTotal());
		
		return ResponseFormat.retParam(1,200,pageInfo);	
	}
	
	@Override
	public JsonBean getRectificationValuation(String token, TblRectificationIssuesVo reiss, TblZgzzIssuesilistVo issues,
			TblZgzzRectificationplanVo plan) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//plan.setStatus(9);//查找开始整改方案的问题清单
		plan.setStatusStrs("9,10,11");
		plan.setCreateStaff(loginStaff.getStaffid());//查找属于当前登录人的整改清单
		
		//封装分页查询实体
		PageInfo<TblRectificationIssuesVo> pageInfo = new PageInfo<TblRectificationIssuesVo>();
		pageInfo.setPageSize(reiss.getPageSize());
		pageInfo.setCurrentPage(reiss.getPageNumber());
		reiss.setPlan(plan);
		reiss.setIssues(issues);
		
		TblZgzzRectificationimplVo impl = new TblZgzzRectificationimplVo();
		impl.setStatus(6);
		reiss.setReimpl(impl);
		pageInfo.setCondition(reiss);
		
		//查询分页数据
		com.github.pagehelper.PageInfo<TblRectificationIssuesVo> page = PageMethod.startPage(reiss.getPageNumber(), reiss.getPageSize()).doSelectPageInfo(() -> this.tblRectificationIssuesMapper.selectMyRectificationPageInfoList(reiss));
		List<TblRectificationIssuesVo> reisList = page.getList();
		
		//循环查找出来的 整改清单  将协办部门信息放入
		List<String> orgIdList = null;
		String orgIds = null;
		List<String> orgNameList = null;
		for (TblRectificationIssuesVo reis : reisList) {
			orgIdList = this.tblRelationSheetMapper.selectObjectIdListByForm(reis.getRelaId(),TblRectificationIssues.FORMTYPE,TblRectificationIssues.FORMCOL,TblRelationSheet.ORGTYPE);
			if(orgIdList != null && orgIdList.size() > 0) {
				orgIds = orgIdList.stream().collect(Collectors.joining(","));
				//通过组织主键拼接获取所有的组织名称
				orgNameList = this.tblRelationSheetMapper.selectOrgNameListByOrgIds(orgIds);
				reis.setCborgIds(orgIds);
				reis.setCborgNames(orgNameList.stream().collect(Collectors.joining(",")));
			}
		}
		
		//查询总记录数
		pageInfo.setTotalRecord((int)page.getTotal());
		pageInfo.setTlist(reisList);
		return ResponseFormat.retParam(1,200,pageInfo);
	}
	
	@Override
	public JsonBean modifyRectificationPlanStatus(String token, TblZgzzRectificationplan rectification)
			throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		//主键不为空 进行修改
		this.tblZgzzRectificationplanMapper.updateByPrimaryKeySelective(rectification);
		
		//整改方案关闭，则将正在整改的清单状态还原为未整改
		if(rectification.getStatus() == 11) {
			this.tblZgzzIssuesilistMapper.updateStatusByClosePlan(rectification.getPlanId(),0);
		}
		
		return ResponseFormat.retParam(1,200,rectification);
	}
	
	@Override
	public JsonBean getIssuesAllDetailInfoByPlanIssuesId(String token, String planId, String issuesId)
			throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		String relaId = this.tblRectificationIssuesMapper.selectRelaIdByPlanIdIssuesId(planId,issuesId);
		
		//查询 方案与清单关系表中填写的数据
		TblRectificationIssuesVo rela = this.tblRectificationIssuesMapper.selectEntityById(relaId);
		List<TblAttachment> relaAttList = this.tblAttachmentMapper.selectAttListbyRelaId(relaId);
		rela.setAttList(relaAttList);
		
		if(StringUtils.isNotBlank(rela.getIssuesId())) {
			//查询获取 整改清单的详情数据
			TblZgzzIssuesilistVo issues = this.tblZgzzIssuesilistMapper.selectEntityById(rela.getIssuesId());
			List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListbyIssuesId(issues.getIssuesId());
		    issues.setAttList(attList);
		    List<TblRectificationIssuesVo> relaList = this.tblRectificationIssuesMapper.selectListByIssuesId(issues.getIssuesId(),relaId);
		    issues.setRelaList(relaList);
			rela.setIssues(issues);
		}
		
		if(StringUtils.isNotBlank(rela.getPlanId())) {
			//查询获取 整改方案的详情数据
			TblZgzzRectificationplanVo plan = this.tblZgzzRectificationplanMapper.selectEntityById(rela.getPlanId());
			rela.setPlan(plan);
		}
		
		//从业务表单组织用户关系表中 获取关联的承办部门信息；
		List<String> orgIdList = this.tblRelationSheetMapper.selectObjectIdListByForm(rela.getRelaId(),TblRectificationIssues.FORMTYPE,TblRectificationIssues.FORMCOL,TblRelationSheet.ORGTYPE);
		if(orgIdList != null && orgIdList.size() > 0) {
			String orgIds = orgIdList.stream().collect(Collectors.joining(","));
			//通过组织主键拼接获取所有的组织名称
			List<String> orgNameList = this.tblRelationSheetMapper.selectOrgNameListByOrgIds(orgIds);
			rela.setCborgIds(orgIds);
			rela.setCborgNames(orgNameList.stream().collect(Collectors.joining(",")));
		}
		
		//查找我的整改落实信息
		List<TblZgzzRectificationimplVo> implList = this.tblZgzzRectificationimplMapper.selectEntityByRelaId(rela.getRelaId());
		
		if(implList != null && implList.size() > 0) {
			TblZgzzRectificationimplVo impl = implList.get(0);
			//查询整改方案 相关的附件列表信息
			List<TblAttachment> implAttList = this.tblAttachmentMapper.selectAttListbyRectificationImpl(impl.getImplId());
			impl.setAttList(implAttList);
			rela.setReimpl(impl);
			
			//查询获取整改评价的数据
			TblZgzzRctevaluationVo eval = this.tblZgzzRctevaluationMapper.selectEntityByImplId(impl.getImplId());
			if(eval != null) {
				List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListByZgzzEvaluation(eval.getEvalId());
				eval.setAttList(attList);
				rela.setValua(eval);
			}
		}
		
		return ResponseFormat.retParam(1,200,rela);
	}


	@Override
	public JsonBean completeRectificationEval(String token, String planId) throws Exception {
		//验证用户登录是否失效
				TblStaffUtil loginStaff = userProvider.get();
				if(loginStaff == null) {
					return ResponseFormat.retParam(0,20006,null);
				}
				
				TblZgzzRectificationplanVo rectification = this.tblZgzzRectificationplanMapper.selectEntityById(planId);
				
				if(rectification == null) {
					return ResponseFormat.retParam(0,50001,null);
				}
				
				// 判断为整改评价查询，获取总评价记录数，已评价记录数，未评价记录数
				TblRectificationIssuesVo issues = null;
				issues = new TblRectificationIssuesVo();
				issues.setPlanId(planId);
				List<TblRectificationIssuesVo> issuesList = this.tblRectificationIssuesMapper.selectListByExample(issues);
				Integer totalRecord = issuesList.size(); //总评价记录数
				Integer allocatedRecord = this.tblZgzzRctevaluationMapper.selectAllocatedList(planId); //已评价记录数
				
				if(totalRecord.compareTo(allocatedRecord) != 0) {
					return ResponseFormat.retParam(0,"评价未全部完成，不能完成整改",null);
				}
				
				TblZgzzRectificationplan plan = new TblZgzzRectificationplan();
				plan.setPlanId(new BigDecimal(planId));
				plan.setStatus(10);
				
				this.tblZgzzRectificationplanMapper.updateByPrimaryKeySelective(plan);
				
				return ResponseFormat.retParam(1,200,null);
	}


	@Override
	public JsonBean saveIssuesRelaPlan(String token, TblRectificationIssues rela, String[] orgIds, String[] attIds) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//保存中间表实体数据
		this.tblRectificationIssuesMapper.updateByPrimaryKeySelective(rela);
		
		//删除之前保存组织用户中间关系表的数据；
		this.tblRelationSheetMapper.deleteRelation(rela.getRelaId().toString(), TblRectificationIssues.FORMTYPE, TblRectificationIssues.FORMCOL, TblRelationSheet.ORGTYPE);
		
		//保存与组织的中间关系表数据
		if(orgIds != null) {
			TblRelationSheet sheet = null;
			int i = 1 ; //排序
			for (String orgId : orgIds) {
				sheet = new TblRelationSheet();
				sheet.setFid(RandomUtil.uuBigDecimalId());
				sheet.setFormid(rela.getRelaId().toString());
				sheet.setFormtype(TblRectificationIssues.FORMTYPE);
				sheet.setFormcol(TblRectificationIssues.FORMCOL);
				sheet.setObjectid(new BigDecimal(orgId));
				sheet.setObjtype(TblRelationSheet.ORGTYPE);
				sheet.setSort(i);
				this.tblRelationSheetMapper.insertSelective(sheet);
			}
		}
		
		if(attIds != null) {
			for (String attid : attIds) {
				this.tblRectificationIssuesMapper.saveFileRelation(attid,rela.getRelaId().toString());
			}
		}
		
		
		return ResponseFormat.retParam(1,200,rela);
	}


	@Override
	public JsonBean removePlanIssuesFile(String token, String relaId, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        //附件表不删除保留  中间关系表删除连接关系
        this.tblRectificationIssuesMapper.deleteFileRelationByAttId(relaId,attId);
        
        return ResponseFormat.retParam(1, 200, null);
	}
	
	
	@Override
	public JsonBean exportMyRectificationList(TblStaffUtil loginStaff, TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan,
			HttpServletResponse response, TblZgzzRectificationimplVo reimpl) throws Exception {
				
		 //1.根据输入条件获取所选择的整改台账列表 为空默认所有方案导出
		 reiss.setDeptIds(loginStaff.getDeptIds());
	     reiss.setImplementer(loginStaff.getStaffid());//查找属于当前登录人的整改清单
		
		//封装分页查询实体
		reiss.setPlan(plan);
		reiss.setIssues(issues);
		reiss.setReimpl(reimpl);
		
		List<TblRectificationIssuesVo> reisList =  this.tblRectificationIssuesMapper.selectMyRectificationExportList(reiss);
        
	    if(reisList == null) {
	        return ResponseFormat.retParam(0, 50007, null);
	    }
	    
	    TblZgzzRectificationimplVo implVo = null;
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] objs = null;
		String titleone = "整改清单";
		String titletwo = "截至"+DateUtil.parseDate(new Date(), "yyyy年MM月dd日");
		String[] titlethree = {"序号", "单位名称 ","问题来源","审计报告出具年份","问题类别","问题在审计报告中的序号及表述","整改责任清单","整改目标清单","具体责任单位细化的整改要求","具体责任单位整改责任人","具体责任单位整改情况"};
		String[] titlefour = {"一级单位", "具体责任单位 ","一级标题","二级标题","三级标题",  "在审计报告中的表述","具体责任单位","具体问题表述","问题金额（万元）","负责监督管理责任的主管部门","整改类型","法规政策依据","整改要求","整改时限","整改完成标准","细化的整改措施","对应的完成时间","整改第一责任人","协助整改工作的领导","牵头整改部门责任人及联系电话","配合整改部门责任人及联系电话","审计部门责任人及联系电话","已采取的整改措施","项目数(个)","问题整改金额（万元）","其中","其他","土地、森林等面积（公顷）","矿产资源、产能等（万吨）","单位（个）","家庭（户）","人数（人）","住房（套）","追责问题情况","完善制度情况","未整改到位问题原因及下一步计划","是否已完成整改","是否销号"};
		String[] titlefive = {"追缴资金（万元）", "归还原渠道（万元） ","统筹盘活（万元）","加快拨付（万元）","退抵税费或补缴补发（万元）","调整账表（万元）","终止或调整金融业务服务(万元)","补办手续、重签协议、停止收费等加强管理（万元）",  "方式","金额（万元）","情形","人数","数量（个）","（分修订、制定，文件名称）"};
		Integer no = 1;
		for (TblRectificationIssuesVo vo : reisList) {
			implVo = vo.getReimpl();
			if(implVo == null) {
				implVo = new TblZgzzRectificationimplVo();
			}
			objs = new Object[52];
			objs[0] = no;
			objs[1] = implVo.getOneorgname()==null?"":implVo.getOneorgname();
			objs[2] = vo.getIssues().getAuditObjectName()==null?"":vo.getIssues().getAuditObjectName();
			objs[3] = implVo.getProblemsrc()==null?"":implVo.getProblemsrc();
			objs[4] = implVo.getReportyear()==null?"":implVo.getReportyear();
			objs[5] = implVo.getProblemtype()==null?"":implVo.getProblemtype();
			objs[6] = implVo.getOnetitle()==null?"":implVo.getOnetitle();
			objs[7] = implVo.getTwotitle()==null?"":implVo.getTwotitle();
			objs[8] = implVo.getThreetitle()==null?"":implVo.getThreetitle();
			objs[9] = implVo.getReportexpression()==null?"":implVo.getReportexpression();
			objs[10] = vo.getIssues().getAuditObjectName()==null?"":vo.getIssues().getAuditObjectName();
			objs[11] = implVo.getQueexpression()==null?"":implVo.getQueexpression();
			objs[12] = implVo.getQuemoney()==null?"":implVo.getQuemoney();
			objs[13] = implVo.getSupervision()==null?"":implVo.getSupervision();
			objs[14] = implVo.getRecttype()==null?"":implVo.getRecttype();
			objs[15] = implVo.getLegalbasis()==null?"":implVo.getLegalbasis();
			objs[16] = implVo.getRectdemand()==null?"":implVo.getRectdemand();
			objs[17] = implVo.getRecttimelimit()==null?"":implVo.getRecttimelimit();
			objs[18] = implVo.getSituationoverView()==null?"":implVo.getSituationoverView();
			objs[19] = implVo.getRectificationMeasures()==null?"":implVo.getRectificationMeasures();
			objs[20] = implVo.getDeadline()==null?"":DateUtil.parseDate(implVo.getDeadline(), DateUtil.DATE_SMALL_STR);
			objs[21] = implVo.getFirstresponstaffname()==null?"":implVo.getFirstresponstaffname();
			objs[22] = implVo.getAssistleader()==null?"":implVo.getAssistleader();
			objs[23] = implVo.getMaindeptheadtel()==null?"":implVo.getMaindeptheadtel();
			objs[24] = implVo.getAssistdeptheadtel()==null?"":implVo.getAssistdeptheadtel();
			objs[25] = implVo.getAuditdeptheadtel()==null?"":implVo.getAuditdeptheadtel();
			objs[26] = implVo.getAchivement()==null?"":implVo.getAchivement();
			objs[27] = implVo.getPjcnt()==null?"":implVo.getPjcnt();
			objs[28] = implVo.getRectmoney()==null?"":implVo.getRectmoney();
			objs[29] = implVo.getRecoverymoney()==null?"":implVo.getRecoverymoney();
			objs[30] = implVo.getBackmoney()==null?"":implVo.getBackmoney();
			objs[31] = implVo.getOverallmoney()==null?"":implVo.getOverallmoney();
			objs[32] = implVo.getAcceleratemoney()==null?"":implVo.getAcceleratemoney();
			objs[33] = implVo.getRetrievemoney()==null?"":implVo.getRetrievemoney();
			objs[34] = implVo.getAdjustmoney()==null?"":implVo.getAdjustmoney();
			objs[35] = implVo.getStopmoney()==null?"":implVo.getStopmoney();
			objs[36] = implVo.getReissuemoney()==null?"":implVo.getReissuemoney();
			objs[37] = implVo.getOtherway()==null?"":implVo.getOtherway();
			objs[38] = implVo.getOthermoney()==null?"":implVo.getOthermoney();
			objs[39] = implVo.getLandarea()==null?"":implVo.getLandarea();
			objs[40] = implVo.getMinerals()==null?"":implVo.getMinerals();
			objs[41] = implVo.getOrgcnt()==null?"":implVo.getOrgcnt();
			objs[42] = implVo.getFamilycnt()==null?"":implVo.getFamilycnt();
			objs[43] = implVo.getPersoncnt()==null?"":implVo.getPersoncnt();
			objs[44] = implVo.getHousecnt()==null?"":implVo.getHousecnt();
			objs[45] = implVo.getAccountabilityinfo()==null?"":implVo.getAccountabilityinfo();
			objs[46] = implVo.getAccountabilitycnt()==null?"":implVo.getAccountabilitycnt();
			objs[47] = implVo.getInstitutioncnt()==null?"":implVo.getInstitutioncnt();
			objs[48] = implVo.getInstitutioninfo()==null?"":implVo.getInstitutioninfo();
			objs[49] = implVo.getNextMeasures()==null?"":implVo.getNextMeasures();
			objs[50] = implVo.getConclusion()==null?"":implVo.getConclusion();
			if(implVo.getIsxh()!=null && implVo.getIsxh().equals("1")) {
				objs[51] ="是";
			}else {
				objs[51] ="否";
			}
			
			objList.add(objs);
			no++;
		}
		//导出设置 excel表头
		response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + new String("整改清单".getBytes(), "iso-8859-1") + ".xlsx");
		ImportOrExportExcelUtil.exportZglsZgqdExcel(titleone,titletwo,titlethree,titlefour,titlefive, objList, response.getOutputStream(), null);
		
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean exportMyRectificationIssuesList(TblStaffUtil loginStaff, TblRectificationIssuesVo reiss,
			TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan, HttpServletResponse response, TblZgzzRectificationimplVo reimpl)
			throws Exception {
		 reiss.setDeptIds(loginStaff.getDeptIds());
	     reiss.setImplementer(loginStaff.getStaffid());//查找属于当前登录人的整改清单
		
		//封装分页查询实体
		reiss.setPlan(plan);
		reiss.setIssues(issues);
		reiss.setReimpl(reimpl);
		
		List<TblRectificationIssuesVo> reisList =  this.tblRectificationIssuesMapper.selectMyRectificationExportList(reiss);
        
	    if(reisList == null) {
	        return ResponseFormat.retParam(0, 50007, null);
	    }
	    
	    TblZgzzRectificationimplVo implVo = null;
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] objs = null;
		String titleone = "问题清单";
		String titletwo = "截至"+DateUtil.parseDate(new Date(), "yyyy年MM月dd日");
		String[] titlethree = {"序号", "单位名称 ","问题来源","审计报告出具年份","问题类别","问题在审计报告中的序号及表述","整改责任清单","是否已整改完成"};
		String[] titlefour = {"一级单位", "具体责任单位 ","一级标题","二级标题","三级标题",  "在审计报告中的表述","具体责任单位","具体问题表述","问题金额（万元）","负责监督管理责任的主管部门"};
		Integer no = 1;
		for (TblRectificationIssuesVo vo : reisList) {
			implVo = vo.getReimpl();
			if(implVo == null) {
				implVo = new TblZgzzRectificationimplVo();
			}
			objs = new Object[15];
			objs[0] = no;
			objs[1] = implVo.getOneorgname()==null?"":implVo.getOneorgname();
			objs[2] = vo.getIssues().getAuditObjectName()==null?"":vo.getIssues().getAuditObjectName();
			objs[3] = implVo.getProblemsrc()==null?"":implVo.getProblemsrc();
			objs[4] = implVo.getReportyear()==null?"":implVo.getReportyear();
			objs[5] = implVo.getProblemtype()==null?"":implVo.getProblemtype();
			objs[6] = implVo.getOnetitle()==null?"":implVo.getOnetitle();
			objs[7] = implVo.getTwotitle()==null?"":implVo.getTwotitle();
			objs[8] = implVo.getThreetitle()==null?"":implVo.getThreetitle();
			objs[9] = implVo.getReportexpression()==null?"":implVo.getReportexpression();
			objs[10] = vo.getIssues().getAuditObjectName()==null?"":vo.getIssues().getAuditObjectName();
			objs[11] = implVo.getQueexpression()==null?"":implVo.getQueexpression();
			objs[12] = implVo.getQuemoney()==null?"":implVo.getQuemoney();
			objs[13] = implVo.getSupervision()==null?"":implVo.getSupervision();
			objs[14] = implVo.getConclusion()==null?"":implVo.getConclusion();
			objList.add(objs);
			no++;
		}
		//导出设置 excel表头
		response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + new String("问题清单".getBytes(), "iso-8859-1") + ".xlsx");
		ImportOrExportExcelUtil.exportZglsZgqdExcel(titleone,titletwo,titlethree,titlefour, objList, response.getOutputStream(), null);
		
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean exportMyRectificationResponseList(TblStaffUtil loginStaff, TblRectificationIssuesVo reiss,
			TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan, HttpServletResponse response,TblZgzzRectificationimplVo reimpl)
			throws Exception {
		//1.根据输入条件获取所选择的整改台账列表 为空默认所有方案导出
		reiss.setDeptIds(loginStaff.getDeptIds());
	    reiss.setImplementer(loginStaff.getStaffid());//查找属于当前登录人的整改清单
		
		//封装分页查询实体
		reiss.setPlan(plan);
		reiss.setIssues(issues);
		reiss.setReimpl(reimpl);
			
		
		List<TblRectificationIssuesVo> reisList =  this.tblRectificationIssuesMapper.selectMyRectificationExportList(reiss);
        
	    if(reisList == null) {
	        return ResponseFormat.retParam(0, 50007, null);
	    }
	    
	    TblZgzzRectificationimplVo implVo = null;
		List<Object[]> objList = new ArrayList<Object[]>(0);
		Object[] objs = null;
		String titleone = "责任清单";
		String titletwo = "截至"+DateUtil.parseDate(new Date(), "yyyy年MM月dd日");
		String[] titlethree = {"序号", "单位名称 ","问题来源","审计报告出具年份","问题类别","问题在审计报告中的序号及表述","整改责任清单","整改目标清单","具体责任单位细化的整改要求","具体责任单位整改责任人","是否已完成整改"};
		String[] titlefour = {"一级单位", "具体责任单位 ","具体责任单位","具体问题表述","问题金额（万元）","负责监督管理责任的主管部门","整改类型","法规政策依据","整改要求","整改时限","整改完成标准","细化的整改措施","对应的完成时间","整改第一责任人","协助整改工作的领导","牵头整改部门责任人及联系电话","配合整改部门责任人及联系电话","审计部门责任人及联系电话"};
		
		Integer no = 1;
		for (TblRectificationIssuesVo vo : reisList) {
			implVo = vo.getReimpl();
			if(implVo == null) {
				implVo = new TblZgzzRectificationimplVo();
			}
			objs = new Object[24];
			objs[0] = no;
			objs[1] = implVo.getOneorgname()==null?"":implVo.getOneorgname();
			objs[2] = vo.getIssues().getAuditObjectName()==null?"":vo.getIssues().getAuditObjectName();
			objs[3] = implVo.getProblemsrc()==null?"":implVo.getProblemsrc();
			objs[4] = implVo.getReportyear()==null?"":implVo.getReportyear();
			objs[5] = implVo.getProblemtype()==null?"":implVo.getProblemtype();
			objs[6] = implVo.getReportexpression()==null?"":implVo.getReportexpression();
			objs[7] = vo.getIssues().getAuditObjectName()==null?"":vo.getIssues().getAuditObjectName();
			objs[8] = implVo.getQueexpression()==null?"":implVo.getQueexpression();
			objs[9] = implVo.getQuemoney()==null?"":implVo.getQuemoney();
			objs[10] = implVo.getSupervision()==null?"":implVo.getSupervision();
			objs[11] = implVo.getRecttype()==null?"":implVo.getRecttype();
			objs[12] = implVo.getLegalbasis()==null?"":implVo.getLegalbasis();
			objs[13] = implVo.getRectdemand()==null?"":implVo.getRectdemand();
			objs[14] = implVo.getRecttimelimit()==null?"":implVo.getRecttimelimit();
			objs[15] = implVo.getSituationoverView()==null?"":implVo.getSituationoverView();
			objs[16] = implVo.getRectificationMeasures()==null?"":implVo.getRectificationMeasures();
			objs[17] = implVo.getDeadline()==null?"":DateUtil.parseDate(implVo.getDeadline(), DateUtil.DATE_SMALL_STR);
			objs[18] = implVo.getFirstresponstaffname()==null?"":implVo.getFirstresponstaffname();
			objs[19] = implVo.getAssistleader()==null?"":implVo.getAssistleader();
			objs[20] = implVo.getMaindeptheadtel()==null?"":implVo.getMaindeptheadtel();
			objs[21] = implVo.getAssistdeptheadtel()==null?"":implVo.getAssistdeptheadtel();
			objs[22] = implVo.getAuditdeptheadtel()==null?"":implVo.getAuditdeptheadtel();
			objs[23] = implVo.getConclusion()==null?"":implVo.getConclusion();
			objList.add(objs);
			no++;
		}
		//导出设置 excel表头
		response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Content-disposition", "attachment; filename=" + new String("责任清单".getBytes(), "iso-8859-1") + ".xlsx");
		ImportOrExportExcelUtil.exportZglsZerqdExcel(titleone,titletwo,titlethree,titlefour, objList, response.getOutputStream(), null);
		
		return ResponseFormat.retParam(1, 200, null);
	}


	@Override
	public JsonBean getRecitfiacationImpleLedger(TblStaffUtil loginStaff, TblRectificationIssuesVo reiss,
			TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan, HttpServletResponse response,TblZgzzRectificationimplVo reimpl)
			throws Exception {
		
        PageInfo<TblZgzzIssuesilistVo> pageInfo = new PageInfo<TblZgzzIssuesilistVo>();
        pageInfo.setPageSize(issues.getPageSize());
        pageInfo.setCurrentPage(issues.getPageNum());
        pageInfo.setCondition(issues);
        
        reiss.setDeptIds(loginStaff.getDeptIds());
        reiss.setImplementer(loginStaff.getStaffid());//查找属于当前登录人的整改清单
      		
      	//封装分页查询实体
      	reiss.setPlan(plan);
      	reiss.setIssues(issues);
        reiss.setReimpl(reimpl);
      	
        com.github.pagehelper.PageInfo<TblZgzzIssuesilistVo> page = PageMethod.startPage(issues.getPageNum(), issues.getPageSize()).doSelectPageInfo(() ->  this.tblRectificationIssuesMapper.selectMyRectificationExportList(reiss));
        
        pageInfo.setTlist(page.getList());
        pageInfo.setTotalRecord((int)page.getTotal());
		
		return ResponseFormat.retParam(1, 200, pageInfo);
	}


	@Override
	public String findFlowNextId(String tblName, String column, String orgCol, BigDecimal orgid, Integer NoId,
			String chChoiceCol, String choiceVal, String bjf) throws Exception {
		// TODO Auto-generated method stub
        Integer isUse = organizationMapper.selectUniqueColumn(orgid);
        if (isUse == 0) {
            return "-1";//该组织没有使用自定义编码
        } else {
            String orgNumber = organizationMapper.selectOrgNumber(orgid);
            Map<String, Object> orgNo = tblRectificationIssuesMapper.getcodeRule(orgid,NoId);
            if (orgNo == null) {
                return "-1";
            } else {
                String noSql = null;
                String result = null;
                String jgf = String.valueOf(orgNo.get("CODE"));
//                jgf = jgf + "-"+orgNumber;
                System.out.println("-------------------------------------------------------------");
                System.out.println(jgf);
                String noCode = String.valueOf(orgNo.get("CODE"));
                String sep = String.valueOf(orgNo.get("SEP"));
                TblOrgNoId ton = new TblOrgNoId();
                ton.setNocode(noCode);
                ton.setNoNumber(Integer.valueOf(String.valueOf(orgNo.get("NUMBER1"))));
                if (sep != null) {
                    ton.setNoSepartor(sep);
                }
                if (jgf.indexOf("_") != -1) {
                    jgf = jgf.replace("_", "/_");
                }
                MaxNumberParam param=new MaxNumberParam();
                param.setColumn(column);
                param.setTblName(tblName);
                param.setOrgCol(orgCol);
                param.setOrgid(orgid);
                param.setNoCode(noCode);
                param.setSep(sep);
                List<BigDecimal> addList = new ArrayList<>();
//                List<BigDecimal> orglist=tblOrganizaService.getOrgIdListAutoNumber();
//                List<BigDecimal> orglist2=tblOrganizaService.getOrgIdLis(orgid);
//                addList.addAll(orglist);
//                addList.addAll(orglist2);
//               List<String> orgList = addList.stream()
//                        .map(BigDecimal::toString)
//                        .collect(Collectors.toList());
//                param.setOrgList(orgList);
                //判断用户有没有使用分隔符
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                jgf = jgf + "-"+orgid+"-"+year;
                System.out.println(jgf);

                if (sep != null) {
                    if (jgf.indexOf("_") != -1) {
                        if ("_".equals(sep)) {
                            jgf += "/" +sep;
                        } else {
                            jgf +=sep;
                        }
                        jgf += "%' escape '/";
                    } else {
                        if ("_".equals(sep)) {
                            jgf += "/" + sep + "%' escape '/";
                        } else {
                            jgf +=sep + "%";
                        }
                    }
                    param.setJgf(jgf);
                } else {
                    if (jgf.indexOf("_") != -1) {
                        jgf +="%' escape '/";
                    } else {
                        jgf +="%";
                    }
                  param.setJgf(jgf);
                }
                if (chChoiceCol != null) {
                    if ("包含".equals(bjf)) {
                        bjf = "LIKE";
                        choiceVal = "'%" + choiceVal + "%'";
                    } else if ("等于".equals(bjf)) {
                        bjf = "=";
                        choiceVal = "'" + choiceVal + "'";
                    }
                    param.setBjf(bjf) ;
                }
                result=  tblRectificationIssuesMapper.getMaxNumberFor(param);
                if  (result == null) {
                    result = "0";
                }
                Integer num = Integer.parseInt(result)+1;
                if(num<10){
                    jgf =  jgf.substring(0,jgf.length()-1)+"0"+num;
                }else {
                    jgf =  jgf.substring(0,jgf.length()-1)+num;
                }
                System.out.println("jgf:"+jgf);
//                return getNewCode(result, ton);
                return jgf;
            }
        }
	}
	
	
	
	
	
	
	@Override
	public JsonBean getMyhxzgList(String token, TblRectificationIssuesVo reiss,TblZgzzIssuesilistVo issues, TblZgzzRectificationplanVo plan) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		plan.setStatus(9);//查找开始整改方案的问题清单
		reiss.setImplementer(loginStaff.getStaffid());//查找属于当前登录人的整改清单
		
		//封装分页查询实体
		PageInfo<TblRectificationIssuesVo> pageInfo = new PageInfo<TblRectificationIssuesVo>();
		pageInfo.setPageSize(reiss.getPageSize());
		pageInfo.setCurrentPage(reiss.getPageNum());
		reiss.setPlan(plan);
		reiss.setIssues(issues);
		pageInfo.setCondition(reiss);
		
		
		com.github.pagehelper.PageInfo<TblRectificationIssuesVo> page = PageMethod.startPage(reiss.getPageNum(), reiss.getPageSize()).doSelectPageInfo(() -> this.tblRectificationIssuesMapper.selectHxzgPageInfoList(reiss));
        
		//查询分页数据
		List<TblRectificationIssuesVo> reisList = page.getList();
		
		//循环查找出来的 整改清单  将协办部门信息放入
		List<String> orgIdList = null;
		String orgIds = null;
		List<String> orgNameList = null;
		for (TblRectificationIssuesVo reis : reisList) {
			orgIdList = this.tblRelationSheetMapper.selectObjectIdListByForm(reis.getRelaId(),TblRectificationIssues.FORMTYPE,TblRectificationIssues.FORMCOL,TblRelationSheet.ORGTYPE);
			if(orgIdList != null && orgIdList.size() > 0) {
				orgIds = orgIdList.stream().collect(Collectors.joining(","));
				//通过组织主键拼接获取所有的组织名称
				orgNameList = this.tblRelationSheetMapper.selectOrgNameListByOrgIds(orgIds);
				reis.setCborgIds(orgIds);
				reis.setCborgNames(orgNameList.stream().collect(Collectors.joining(",")));
			}
		}
		
		//查询总记录数
		pageInfo.setTotalRecord((int)page.getTotal());
		pageInfo.setTlist(reisList);
		
		return ResponseFormat.retParam(1,200,pageInfo);
	}


	@Override
	public JsonBean getbyUserlist(String token, String relaId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		return ResponseFormat.retParam(1,200,tblFlowardRecordMapper.findbyFromid(relaId));
	}

}
