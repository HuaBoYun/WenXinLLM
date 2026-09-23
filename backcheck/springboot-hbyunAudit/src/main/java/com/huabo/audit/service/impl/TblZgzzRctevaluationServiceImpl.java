package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.RandowUtil;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblRectificationIssues;
import com.huabo.audit.oracle.entity.TblRelationSheet;
import com.huabo.audit.oracle.entity.TblZgzzRctevaluation;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
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
import com.huabo.audit.service.TblZgzzRctevaluationService;

@Service
public class TblZgzzRctevaluationServiceImpl implements TblZgzzRctevaluationService {
	@Resource
	private TblZgzzRctevaluationMapper tblZgzzRctevaluationMapper;
	
	@Resource
	private TblZgzzIssuesilistMapper tblZgzzIssuesilistMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblZgzzRectificationplanMapper tblZgzzRectificationplanMapper;

	@Resource
	private TblRectificationIssuesMapper tblRectificationIssuesMapper;
	
	@Resource
	private TblRelationSheetMapper tblRelationSheetMapper;
	
	@Resource
	private TblZgzzRectificationimplMapper tblZgzzRectificationimplMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean saveRectificationImpl(String token, TblZgzzRctevaluation valua, String[] attIds)
			throws Exception {
		//验证用户登录是否失效
				TblStaffUtil loginStaff = userProvider.get();
				if(loginStaff == null) {
					return ResponseFormat.retParam(0,20006,null);
				}
				
				//如果主键不为空则修改 ，为空则新增
				if(valua.getEvalId() != null) {
					valua.setUpdateStaff(loginStaff.getStaffid());
					valua.setUpdateTime(new Date());
					valua.setEvaluator(loginStaff.getStaffid());
					this.tblZgzzRctevaluationMapper.updateByPrimaryKeySelective(valua);
				}else {
					valua.setCreateTime(new Date());
					valua.setCreateStaff(loginStaff.getStaffid());
					valua.setLinkDept(loginStaff.getLinkDetp().getOrgid());
					valua.setLinkOrg(loginStaff.getCurrentOrg().getOrgid());
					valua.setEvaluator(loginStaff.getStaffid());
					valua.setEvaluaTime(new Date());
					valua.setStatus(0);
					valua.setEvalId(RandomUtil.uuBigDecimalId());
					this.tblZgzzRctevaluationMapper.insertSelective(valua);
				}
				
				//获取评价关联的整改清单主键
				String issuesId = this.tblZgzzIssuesilistMapper.selectIssuesIdByImpld(valua.getImplId());
				
				if(valua.getResultStatus() != null) {
					//判断评价整改是否到位
					if(valua.getResultStatus().compareTo(2) <= 0) {
						//未整改到位 修改整改清单状态为 未销号问题
						this.tblZgzzIssuesilistMapper.updateStatusBySavePlan(issuesId,9);
					}else {
						//已整改到位  修改整改清单状态 为  已整改完成
						this.tblZgzzIssuesilistMapper.updateStatusBySavePlan(issuesId,8);
					}
				}
				
				
				//循环保存附件信息
				if(attIds != null) {
					for (String attId : attIds) {
						this.tblZgzzRctevaluationMapper.insertAttRelation(valua.getEvalId().toString(),attId);
					}
				}
				
				return ResponseFormat.retParam(1,200,valua);
	}

	@Override
	public JsonBean removeRectiValuaAttRela(String token, String attId, String evalId) throws Exception {
		//验证用户登录是否失效
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
		this.tblZgzzRctevaluationMapper.deleteAttRelation(attId,evalId);
		
		return ResponseFormat.retParam(1,200,null);
	}
	
	@Override
	public JsonBean getZgzzeEvaluationDetail(TblStaffUtil loginStaff, String evalId) throws Exception {
		
		//获取整改评价信息
		TblZgzzRctevaluationVo eval = this.tblZgzzRctevaluationMapper.selectEntityById(evalId);
		
		List<TblAttachment> attList = this.tblAttachmentMapper.selectAttListByZgzzEvaluation(evalId);
		
		eval.setAttList(attList);
		
		//获取整改方案与整改清单关系表的数据
		String relaId = this.tblRectificationIssuesMapper.selectIdByImplId(eval.getImplId());
		
		//查询 方案与清单关系表中填写的数据
		TblRectificationIssuesVo rela = this.tblRectificationIssuesMapper.selectEntityById(relaId);
		
		rela.setValua(eval);
		
		if(StringUtils.isNotBlank(rela.getIssuesId())) {
			//查询获取 整改清单的详情数据
			TblZgzzIssuesilistVo issues = this.tblZgzzIssuesilistMapper.selectEntityById(rela.getIssuesId());
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
		}
		return ResponseFormat.retParam(1,200,rela);
	}

}
