package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.RandowUtil;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblRectificationIssues;
import com.huabo.audit.oracle.entity.TblRelationSheet;
import com.huabo.audit.oracle.entity.TblZgzzRectificationimpl;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjWbProjectMapper;
import com.huabo.audit.oracle.mapper.TblRectificationIssuesMapper;
import com.huabo.audit.oracle.mapper.TblRelationSheetMapper;
import com.huabo.audit.oracle.mapper.TblZgzzIssuesilistMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRctevaluationMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationimplMapper;
import com.huabo.audit.oracle.mapper.TblZgzzRectificationplanMapper;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import com.huabo.audit.oracle.vo.TblZgzzRctevaluationVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationimplVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;
import com.huabo.audit.service.TblZgzzRectificationimplService;

/**
 * 描述:计划编号实现类
 * author: lyz
 * date: 2022-04-13
 */
@Service
public class TblZgzzRectificationimplServiceImpl implements TblZgzzRectificationimplService {

	@Resource
	private TblZgzzRectificationimplMapper tblZgzzRectificationimplMapper;
	
	@Resource
	private TblRectificationIssuesMapper tblRectificationIssuesMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblZgzzIssuesilistMapper tblZgzzIssuesilistMapper;
	
	@Resource
	private TblNbsjWbProjectMapper tblNbsjWbProjectMapper;
	
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Resource
	private TblZgzzRectificationplanMapper tblZgzzRectificationplanMapper;
	
	@Resource
	private TblRelationSheetMapper tblRelationSheetMapper;
	
	@Resource
	private TblZgzzRctevaluationMapper tblZgzzRctevaluationMapper;
	
	@Resource
    private UserProvider userProvider;
	
	
	@Override
	public JsonBean saveRectificationImpl(String token, TblZgzzRectificationimpl impl, String[] attIds)
			throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//判断impl主键是否为空 为空新增 不为空 修改
		if(impl.getImplId() != null) {
			//不为空 修改
			impl.setUpdateStaff(loginStaff.getStaffid());
			impl.setUpdateTime(new Date());
			this.tblZgzzRectificationimplMapper.updateByPrimaryKeySelective(impl);
		}else {
			//为空  新增
			impl.setImplId(RandomUtil.uuBigDecimalId());
			impl.setCreateTime(new Date());
			impl.setCreateStaff(loginStaff.getStaffid());
			impl.setLinkDept(loginStaff.getLinkDetp().getOrgid());
			impl.setLinkOrg(loginStaff.getCurrentOrg().getOrgid());
			impl.setStatus(0);
			this.tblZgzzRectificationimplMapper.insertSelective(impl);
			
		}
		
		//保存附件关系
		if(attIds != null) {
			for (String attId : attIds) {
				this.tblZgzzRectificationimplMapper.insertAttRelation(impl.getImplId().toString(),attId);
			}
		}
		return ResponseFormat.retParam(1,200,impl);
	}

	@Override
	public JsonBean removeRectificationImplAtt(String token, String implId, String attId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		this.tblZgzzRectificationimplMapper.deleteAttRelation(implId,attId);
		return ResponseFormat.retParam(1,200,null);
	}
	
	
	@Override
	public JsonBean getRectificationIssuesListByReportType(TblStaffUtil loginStaff, TblRectificationIssuesVo riv,
			Integer reporttype, String planIdStrs) throws Exception {
		
		/*PageInfo<TblRectificationIssuesVo> pageInfo = new PageInfo<TblRectificationIssuesVo>();
		pageInfo.setCurrentPage(riv.getPageNum());
		pageInfo.setPageSize(riv.getPageSize());*/
		boolean flag = JudgeRoleRight.judgeRoleRight("审计经办人", loginStaff.getRoleNames());
		
		String[] planIds = planIdStrs.split(",");
		
		TblZgzzRectificationplanVo plan = null;
		List<TblRectificationIssuesVo> triList = new ArrayList<TblRectificationIssuesVo>(0);
		riv.setHandlerId(loginStaff.getStaffid());
		for (String planId : planIds) {
			plan = this.tblZgzzRectificationplanMapper.selectEntityById(planId);
			if(reporttype.compareTo(2) == 0 && !flag && loginStaff.getStaffid().compareTo(plan.getResponse()) != 0 && loginStaff.getStaffid().compareTo(plan.getCreateStaff()) != 0  ) {
				//整改落实报告只查看落实人自己的
				riv.setImplementer(loginStaff.getStaffid());
			}
			riv.setPlanId(planId);
			triList.addAll(this.tblRectificationIssuesMapper.selectListByReport(riv));
			
			
		}
		/*pageInfo.setCondition(riv);
		planIdStrs = "'"+planIdStrs.replace(",", "','")+"'";
		pageInfo.setTlist(this.tblRectificationIssuesMapper.selectPageInfoListByReport(pageInfo,planIdStrs));
		pageInfo.setTotalRecord(this.tblRectificationIssuesMapper.selectPageInfoCountByReport(pageInfo,planIdStrs));*/
		
		return ResponseFormat.retParam(1,200,triList);
	}

	@Override
	public JsonBean getRectificationImplDetailInfo(String token, String implId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		//查找我的整改落实信息
		TblZgzzRectificationimplVo impl = this.tblZgzzRectificationimplMapper.selectEntityByImplId(implId);
		//查询整改方案 相关的附件列表信息
		List<TblAttachment> implAttList = this.tblAttachmentMapper.selectAttListbyRectificationImpl(impl.getImplId());
		impl.setAttList(implAttList);
			
		//查询获取整改评价的数据
		TblZgzzRctevaluationVo eval = this.tblZgzzRctevaluationMapper.selectEntityByImplId(impl.getImplId());
		if(eval != null) {
			List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListByZgzzEvaluation(eval.getEvalId());
			eval.setAttList(attList);
		}
		
		//查询 方案与清单关系表中填写的数据
		TblRectificationIssuesVo rela = this.tblRectificationIssuesMapper.selectEntityById(impl.getRelaid());
		List<TblAttachment> relaAttList = this.tblAttachmentMapper.selectAttListbyRelaId(rela.getRelaId());
		rela.setAttList(relaAttList);
		rela.setReimpl(impl);
		rela.setValua(eval);
			
		if(StringUtils.isNotBlank(rela.getIssuesId())) {
			//查询获取 整改清单的详情数据
			TblZgzzIssuesilistVo issues = this.tblZgzzIssuesilistMapper.selectEntityById(rela.getIssuesId());
			List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListbyIssuesId(issues.getIssuesId());
		    issues.setAttList(attList);
		    List<TblRectificationIssuesVo> relaList = this.tblRectificationIssuesMapper.selectListByIssuesId(issues.getIssuesId(),rela.getRelaId());
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
		
		return ResponseFormat.retParam(1,200,rela);
	}
}
	
