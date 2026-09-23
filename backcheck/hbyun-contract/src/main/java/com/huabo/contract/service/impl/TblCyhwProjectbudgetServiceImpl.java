package com.huabo.contract.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import cn.hutool.core.collection.CollectionUtil;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.service.TblStaffService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblContractAppendixsigning;
import com.huabo.contract.entity.TblCounterpartBankinfo;
import com.huabo.contract.entity.TblCyhwBudetgoods;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblFlow;
import com.huabo.contract.entity.TblOppblackRecord;
import com.huabo.contract.entity.TblOppblackRemove;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.entity.TblStaff;
import com.huabo.contract.mapper.TblAttachmentMapper;
import com.huabo.contract.mapper.TblContractAppendixsigningMapper;
import com.huabo.contract.mapper.TblCounterpartBankinfoMapper;
import com.huabo.contract.mapper.TblCyhwBudetgoodsMapper;
import com.huabo.contract.mapper.TblCyhwProjectbudgetMapper;
import com.huabo.contract.mapper.TblCyhwUnitMapper;
import com.huabo.contract.mapper.TblFlowMapper;
import com.huabo.contract.mapper.TblOppblackRecordMapper;
import com.huabo.contract.mapper.TblOppblackRemoveMapper;
import com.huabo.contract.mapper.TblOrganizationMapper;
import com.huabo.contract.service.TblCyhwProjectbudgetService;
import com.huabo.contract.util.DateUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service("tblCyhwProjectbudgetServiceImpl")
@Slf4j
public class TblCyhwProjectbudgetServiceImpl implements TblCyhwProjectbudgetService {

	@Resource
	private TblCyhwProjectbudgetMapper tblCyhwProjectbudgetMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Autowired
	private UserProvider userProvider;
	
	@Resource
	private TblCounterpartBankinfoMapper tblCounterpartBankinfoMapper;
	
	@Resource
	private TblCyhwUnitMapper tblCyhwUnitMapper;
	
	@Resource
	private TblCyhwBudetgoodsMapper tblCyhwBudetgoodsMapper;
	
	@Resource
	private TblContractAppendixsigningMapper tblContractAppendixsigningMapper;
	
	@Resource
	private TblFlowMapper tblFlowMapper;
	
	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
	private TblOppblackRecordMapper tblOppblackRecordMapper;
	
	@Resource
	private TblOppblackRemoveMapper tblOppblackRemoveMapper;
	@Resource
	private TblStaffService tblStaffService;
	@Resource
	private TblOrganizaService tblOrganizaService;
	
		//相对方维护
	@Override
	public Map<String,Object> findBudgetListByStaffOrg(String token, String flowId, String staffId, TblCyhwProjectbudget budget,Integer currentPage, Integer pageSize)
			throws Exception {
		// 创建一个空的Map来存储结果
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
	
		try {
			// 解析用户令牌，获取用户信息
			TblStaffUtil staff = userProvider.get();
			// 如果用户已失效，设置返回结果
			if(staff == null) {
				// 如果用户已失效，设置返回结果
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
	
			// 创建一个分页信息对象
			
			//budget.setFlowid(flow.getFlowid());
			budget.setOrgid(staff.getCurrentOrg().getOrgid());
			budget.setRecordtype(TblCyhwProjectbudget.FLOWNUMBER);
			
			
			IPage<TblCyhwProjectbudget> page = new Page<TblCyhwProjectbudget>(currentPage, pageSize);
			IPage<TblCyhwProjectbudget> pageList = tblCyhwProjectbudgetMapper.selectListByPageInfo(page,staff,0,budget);
			/*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
				pageList.getRecords().forEach(item->{
					if (Objects.nonNull(item.getStaffid1())) {
						item.setRealname1(tblStaffService.getStaffName(item.getStaffid1()));
					}
					if (Objects.nonNull(item.getStaffid2())) {
						item.setRealname2(tblStaffService.getStaffName(item.getStaffid2()));
					}
					if (Objects.nonNull(item.getStaffid3())) {
						item.setRealname3(tblStaffService.getStaffName(item.getStaffid3()));
					}
					if (Objects.nonNull(item.getStaffid4())) {
						item.setRealname4(tblStaffService.getStaffName(item.getStaffid4()));
					}
					if (Objects.nonNull(item.getStaffid5())) {
						item.setRealname5(tblStaffService.getStaffName(item.getStaffid5()));
					}
					if (StringUtils.isNotBlank(item.getStaffids1())) {
						item.setRealnames1(tblStaffService.getStaffNames(item.getStaffids1()));
					}
					if (StringUtils.isNotBlank(item.getStaffids2())) {
						item.setRealnames2(tblStaffService.getStaffNames(item.getStaffids2()));
					}
					if (StringUtils.isNotBlank(item.getStaffids3())) {
						item.setRealnames3(tblStaffService.getStaffNames(item.getStaffids3()));
					}
					if (StringUtils.isNotBlank(item.getStaffids4())) {
						item.setRealnames4(tblStaffService.getStaffNames(item.getStaffids4()));
					}
					if (StringUtils.isNotBlank(item.getStaffids5())) {
						item.setRealnames5(tblStaffService.getStaffNames(item.getStaffids5()));
					}
					if (Objects.nonNull(item.getOrgid1())) {
						item.setOrgname1(tblOrganizaService.getOrgName(item.getOrgid1()));
					}
					if (Objects.nonNull(item.getOrgid2())) {
						item.setOrgname2(tblOrganizaService.getOrgName(item.getOrgid2()));
					}
					if (Objects.nonNull(item.getOrgid3())) {
						item.setOrgname3(tblOrganizaService.getOrgName(item.getOrgid3()));
					}
					if (Objects.nonNull(item.getOrgid4())) {
						item.setOrgname4(tblOrganizaService.getOrgName(item.getOrgid4()));
					}
					if (Objects.nonNull(item.getOrgid5())) {
						item.setOrgname5(tblOrganizaService.getOrgName(item.getOrgid5()));
					}
					if (StringUtils.isNotBlank(item.getOrgids1())) {
						item.setOrgnames1(tblOrganizaService.getOrgNames(item.getOrgids1()));
					}
					if (StringUtils.isNotBlank(item.getOrgids2())) {
						item.setOrgnames2(tblOrganizaService.getOrgNames(item.getOrgids2()));
					}
					if (StringUtils.isNotBlank(item.getOrgids3())) {
						item.setOrgnames3(tblOrganizaService.getOrgNames(item.getOrgids3()));
					}
					if (StringUtils.isNotBlank(item.getOrgids4())) {
						item.setOrgnames4(tblOrganizaService.getOrgNames(item.getOrgids4()));
					}
					if (StringUtils.isNotBlank(item.getOrgids5())) {
						item.setOrgnames5(tblOrganizaService.getOrgNames(item.getOrgids5()));
					}
				});
			}*/
			PageInfo<TblCyhwProjectbudget> pageInfo = new PageInfo<TblCyhwProjectbudget>();
			pageInfo.setTlist(pageList.getRecords());
			pageInfo.setTotalRecord((int) pageList.getTotal());
			
			pageInfo.setPageSize(pageSize);
			pageInfo.setCurrentPage(currentPage);
			pageInfo.setCondition(budget);
			resultMap.put("code", "1");
			resultMap.put("msg", "访问接口成功");
			resultMap.put("data", pageInfo);
			// 判断当前登录用户是否为“合同黑名单管理员”
			if(JudgeRoleRight.judgeRoleRight("合同黑名单管理员",staff.getRoleNames())){
				resultMap.put("isBlackAdmin", 0);//当前登录用户是黑名单管理员
	        }else {
	        	resultMap.put("isBlackAdmin", 1);//当前登录用户不是黑名单管理员
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> SaveOppositeParty(TblCyhwProjectbudget tcpb, String attids, String staffId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		// 解析用户令牌，获取用户信息
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		// 获取流程信息
		/*TblFlow flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwProjectbudget.FLOWNUMBER,staff.getCurrentOrg().getOrgid());
		if(flow == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "流程未定义！");
			return resultMap;
		}*/
		// 设置实体类的相关信息
		tcpb.setRecordtype(TblCyhwProjectbudget.FLOWNUMBER);
		tcpb.setCreateuser(staff.getStaffid());
		tcpb.setOrgid(staff.getCurrentOrg().getOrgid());
		tcpb.setCreatetime(new Date());
		// 检查相对方名称是否重复
		Integer count = this.tblCyhwProjectbudgetMapper.selectRepearBudgetName(tcpb.getBudgetname(), staff.getCurrentOrg().getOrgid(), null);
		if (count > 0) {
			// 如果相对方名称重复，设置返回结果
			resultMap.put("code", "0");
			resultMap.put("msg", "相对方名称重复！");
			return resultMap;
		}
		// 插入相对方信息
		tcpb.setInspectionstatus(0);
		tcpb.setBudgetid(RandomUtil.uuBigDecimalId());
		tcpb.setCerType(0);
		this.tblCyhwProjectbudgetMapper.insert(tcpb);
		// 如果有附件ID，插入附件关系
		if (attids != null && !"".equals(attids)) {
			String[] ids = attids.split(",");
			for (String id : ids) {
				this.tblCyhwProjectbudgetMapper.insertAttmentRelation(id, tcpb.getBudgetid());
			}
		}
		// 设置返回结果
		resultMap.put("code", "1");
		resultMap.put("msg", "相对方保存成功！");
		resultMap.put("data", tcpb);
		
		return resultMap;
	}

	public void removeOppsiteFile(String attid) throws Exception {
		// 删除附件关系记录
		this.tblAttachmentMapper.deleteOppsiteFileRelatioin(attid);
		// 删除附件信息记录
		this.tblAttachmentMapper.deleteFileInfoById(attid);
	}
	
	public Integer findStatueById(BigDecimal budgetId) throws Exception {
		// 根据预算ID查询状态
		return this.tblCyhwProjectbudgetMapper.selectStatusByBudgetId(budgetId);
	}

	@Override
	public Map<String, Object> findOppsiteAllInfoById(BigDecimal budgetId) throws Exception {
		// 创建一个空的Map来存储结果
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
		try {
			// 解析用户令牌，获取用户信息
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				// 如果用户已失效，设置返回结果
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			// 获取流程信息
			//TblFlow flow = tblFlowMapper.findFlowInfoById(flowId);
			// 根据预算ID查询项目预算信息
			TblCyhwProjectbudget item = this.tblCyhwProjectbudgetMapper.selectAllInfoById(budgetId);
			if (Objects.nonNull(item)){
				if (Objects.nonNull(item.getStaffid1())) {
					item.setRealname1(tblStaffService.getStaffName(item.getStaffid1()));
				}
				if (Objects.nonNull(item.getStaffid2())) {
					item.setRealname2(tblStaffService.getStaffName(item.getStaffid2()));
				}
				if (Objects.nonNull(item.getStaffid3())) {
					item.setRealname3(tblStaffService.getStaffName(item.getStaffid3()));
				}
				if (Objects.nonNull(item.getStaffid4())) {
					item.setRealname4(tblStaffService.getStaffName(item.getStaffid4()));
				}
				if (Objects.nonNull(item.getStaffid5())) {
					item.setRealname5(tblStaffService.getStaffName(item.getStaffid5()));
				}
				if (StringUtils.isNotBlank(item.getStaffids1())) {
					item.setRealnames1(tblStaffService.getStaffNames(item.getStaffids1()));
				}
				if (StringUtils.isNotBlank(item.getStaffids2())) {
					item.setRealnames2(tblStaffService.getStaffNames(item.getStaffids2()));
				}
				if (StringUtils.isNotBlank(item.getStaffids3())) {
					item.setRealnames3(tblStaffService.getStaffNames(item.getStaffids3()));
				}
				if (StringUtils.isNotBlank(item.getStaffids4())) {
					item.setRealnames4(tblStaffService.getStaffNames(item.getStaffids4()));
				}
				if (StringUtils.isNotBlank(item.getStaffids5())) {
					item.setRealnames5(tblStaffService.getStaffNames(item.getStaffids5()));
				}
				if (Objects.nonNull(item.getOrgid1())) {
					item.setOrgname1(tblOrganizaService.getOrgName(item.getOrgid1()));
				}
				if (Objects.nonNull(item.getOrgid2())) {
					item.setOrgname2(tblOrganizaService.getOrgName(item.getOrgid2()));
				}
				if (Objects.nonNull(item.getOrgid3())) {
					item.setOrgname3(tblOrganizaService.getOrgName(item.getOrgid3()));
				}
				if (Objects.nonNull(item.getOrgid4())) {
					item.setOrgname4(tblOrganizaService.getOrgName(item.getOrgid4()));
				}
				if (Objects.nonNull(item.getOrgid5())) {
					item.setOrgname5(tblOrganizaService.getOrgName(item.getOrgid5()));
				}
				if (StringUtils.isNotBlank(item.getOrgids1())) {
					item.setOrgnames1(tblOrganizaService.getOrgNames(item.getOrgids1()));
				}
				if (StringUtils.isNotBlank(item.getOrgids2())) {
					item.setOrgnames2(tblOrganizaService.getOrgNames(item.getOrgids2()));
				}
				if (StringUtils.isNotBlank(item.getOrgids3())) {
					item.setOrgnames3(tblOrganizaService.getOrgNames(item.getOrgids3()));
				}
				if (StringUtils.isNotBlank(item.getOrgids4())) {
					item.setOrgnames4(tblOrganizaService.getOrgNames(item.getOrgids4()));
				}
				if (StringUtils.isNotBlank(item.getOrgids5())) {
					item.setOrgnames5(tblOrganizaService.getOrgNames(item.getOrgids5()));
				}
			}
			// 根据预算ID查询附件列表
			List<TblAttachment> attList = this.tblCyhwProjectbudgetMapper.selectAllATT(budgetId);
			// 根据预算ID查询银行信息列表
			List<TblCounterpartBankinfo> bankInfoList = this.tblCounterpartBankinfoMapper.selectBankInfoByBugetId(budgetId);
			//查找加入黑名单相关信息
			TblOppblackRecord br = this.tblOppblackRecordMapper.selectByOppId(item.getBudgetid());
			
			if(br != null) {
				item.setBlackRecord(br);
				
				//查找取消黑名单记录信息
				TblOppblackRemove removeB = this.tblOppblackRemoveMapper.selectUniqueByBrIdOppId(br.getBrid(),item.getBudgetid());
				item.setRemoveRecord(removeB);
			}
			
			// 设置项目预算信息的流程信息
			//budget.setTblFlow(flow);
			// 将附件列表和银行信息列表添加到dataMap中
			dataMap.put("attList", attList);
			dataMap.put("bankInfoList", bankInfoList);
			dataMap.put("budget", item);
			// 设置返回结果
			resultMap.put("code", "1");
			resultMap.put("msg", "数据查询成功！");
			resultMap.put("data", dataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
	
	@Override
	public JsonBean getOppoRelaInfo(String removeid, String blackid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(StringUtils.isBlank(blackid) && StringUtils.isBlank(removeid)) {
			return ResponseFormat.retParam(0, 10004, null);
		}
		
		BigDecimal budgetId = null;
		TblOppblackRecord br = null;
		TblOppblackRemove rb = null;
		
		if(StringUtils.isNotBlank(blackid)) {
			br = this.tblOppblackRecordMapper.selectById(blackid);
			if(br == null) {
				return ResponseFormat.retParam(0, 50001, null);
			}
			budgetId = br.getOppoid();
			rb = this.tblOppblackRemoveMapper.selectUniqueByBrIdOppId(br.getBrid(),budgetId);
		}
		
		if(StringUtils.isNotBlank(removeid)) {
			rb = this.tblOppblackRemoveMapper.selectById(removeid);
			if(rb == null) {
				return ResponseFormat.retParam(0, 50001, null);
			}
			br = this.tblOppblackRecordMapper.selectById(rb.getBrid());
			budgetId = br.getOppoid();
		}
		if(budgetId == null) {
			return ResponseFormat.retParam(0, 50001, null);
		}
		
		
		TblCyhwProjectbudget budget = this.tblCyhwProjectbudgetMapper.selectAllInfoById(budgetId);
		// 根据预算ID查询附件列表
		List<TblAttachment> attList = this.tblCyhwProjectbudgetMapper.selectAllATT(budgetId);
		// 根据预算ID查询银行信息列表
		List<TblCounterpartBankinfo> bankInfoList = this.tblCounterpartBankinfoMapper.selectBankInfoByBugetId(budgetId);
		
		budget.setBlackRecord(br);
		budget.setRemoveRecord(rb);
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
		dataMap.put("attList", attList);
		dataMap.put("bankInfoList", bankInfoList);
		dataMap.put("budget", budget);
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	
	
	
	public Map<String, Object> mengerOppsitePartyById(TblCyhwProjectbudget tcpb, String attids) throws Exception {
		// 创建一个空的Map来存储结果
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			// 如果用户已失效，设置返回结果
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		
		// 检查相对方名称是否重复
		Integer count = this.tblCyhwProjectbudgetMapper.selectRepearBudgetName(tcpb.getBudgetname(), staff.getCurrentOrg().getOrgid(), tcpb.getBudgetid());
		if (count > 0) {
			// 如果相对方名称重复，设置返回结果
			resultMap.put("code", "0");
			resultMap.put("msg", "相对方名称重复！");
			return resultMap;
		}
		// 设置审核状态为6（已提交）
		//tcpb.setInspectionstatus(6);
		// 如果有附件ID，插入附件关系
		this.tblCyhwProjectbudgetMapper.updateById(tcpb);
		if (attids != null && !"".equals(attids)) {
			String[] ids = attids.split(",");
			for (String id : ids) {
				this.tblCyhwProjectbudgetMapper.insertAttmentRelation(id, tcpb.getBudgetid());
			}
		}
		resultMap.put("code", "1");
		resultMap.put("msg", "相对方修改成功！");
		resultMap.put("data", tcpb);
		return resultMap;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Map<String, Object> removeOppsitePartyInfo(String budgetId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		// 解析用户令牌，获取用户信息
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		// 获取新的预算ID
		BigDecimal newBudgetId;
		if (budgetId.indexOf(",") != -1) {
			newBudgetId = new BigDecimal(budgetId.split(",")[1]);
		} else {
			newBudgetId = new BigDecimal(budgetId);
		}

		//查询相对方信息
		TblCyhwProjectbudget bybudgetId = tblCyhwProjectbudgetMapper.findBybudgetId(newBudgetId);
		if (Objects.isNull(bybudgetId)) {
			resultMap.put("code", "0");
			resultMap.put("msg", "未查询到相对方信息！");
			return resultMap;
		}
		//0 null未审批，3 撤销
		if (!bybudgetId.getInspectionstatus().equals(0) && bybudgetId.getInspectionstatus() !=null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "仅能删除未审批的相对方信息！");
			return resultMap;
		}

		//this.tblCyhwProjectbudgetMapper.deleteOppsiteFileInfoByBudtetId(newBudgetId);
		// 删除相对方银行信息
		this.tblCounterpartBankinfoMapper.deleteByBankInfo(newBudgetId);
		// 删除关联文件信息
		this.tblCyhwProjectbudgetMapper.deleteRelaFileInfo(newBudgetId);
		// 删除相对方信息
		this.tblCyhwProjectbudgetMapper.deleteOppsiteInfoByBudtetId(newBudgetId);
		// 获取流程信息
		/*TblFlow flow = tblFlowMapper.findFlowInfoById(flowId);
		// 构造跳转URL
		String url = "redirect:" + (flow.getFlowmappingurl() != null && !"".equals(flow.getFlowmappingurl()) ? flow.getFlowmappingurl() : "/nbkz/cwgl/zcgl_main") + "?flowid=" + flowId + "&flowname=" + flow.getFlownumber();
		resultMap.put("data", url);*/
		resultMap.put("code", "1");
		resultMap.put("msg", "相对方删除成功！");
		return resultMap;
	}

	@Override
	public Map<String, Object> removeOppsitePartyInfoNoLc(String budgetId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		BigDecimal newBudgetId;
		if (budgetId.indexOf(",") != -1) {
			newBudgetId =  new BigDecimal(budgetId.split(",")[1]);
		} else {
			newBudgetId = new BigDecimal(budgetId);
		}
		//删除之前先判断这个相对方是否已经被合同选择过了
		//判断是否为法务人员
		int count=0;
		
		//法务人员删除相对方：合同引用过的相对方都不能进行删除
		if(staff.getCurrentOrg().getOrgname().indexOf("法务")!=-1){
			count=tblCyhwProjectbudgetMapper.checkOppositeDelete(newBudgetId.toString());
		}else{//合同订立删除相对方：合同引用过的相对方都不能进行删除 只能删除自己创建的
			TblCyhwProjectbudget tbl=tblCyhwProjectbudgetMapper.selectAllInfoById(newBudgetId);
		if(!tbl.getCreateStaff().getStaffid().toString().equals(staff.getStaffid().toString())){
				resultMap.put("code", "0");
				resultMap.put("msg", "不能删除非本人创建的相对方!");
				return resultMap;
			}
			count=tblCyhwProjectbudgetMapper.checkOppositeDelete(newBudgetId.toString());
		}
		if(count>0){
			resultMap.put("code", "0");
			resultMap.put("msg", "该相对方已关联合同!");
			return resultMap;
		}
		this.tblCounterpartBankinfoMapper.deleteByBankInfo(newBudgetId);
		this.tblCyhwProjectbudgetMapper.deleteRelaFileInfo(newBudgetId);
		this.tblCyhwProjectbudgetMapper.deleteOppsiteInfoByBudtetId(newBudgetId);
		
		resultMap.put("code", "1");
		resultMap.put("msg", "相对方删除成功！");
		
		return resultMap;
	}

	
	public Map<String, Object> saveOppsitePartyBlack(BigDecimal budgetId,Integer blackType,
			String datetext, String backreason, String brid) throws Exception {
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
			
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			
			Integer version = this.tblOppblackRecordMapper.selectAddBlackVersionById(budgetId);
			
			TblOppblackRecord br = new TblOppblackRecord();
			
			if(StringUtils.isNotBlank(datetext)) {
				br.setBlackdeadtime(DateUtil.formatDate(datetext, DateUtil.DATE_SMALL_STR));
			}
			
			br.setOppoid(budgetId);
			br.setBackreason(backreason);
			br.setObrtype(blackType);
			
			if(StringUtils.isBlank(brid)) {
				//主键为空新增
				brid = RandomUtil.uuStringId();
				br.setBrid(brid);
				br.setCreatestaff(staff.getStaffid());
				br.setCreatetime(new Date());
				br.setLinkdeptid(staff.getLinkDetp().getOrgid());
				br.setLinkorgid(staff.getCurrentOrg().getOrgid());
				br.setAprstatus(0);
				br.setVersion(version+1);
				this.tblOppblackRecordMapper.insert(br);
			}else {
				//主键不为空修改
				br.setBrid(brid);
				br.setModifystaff(staff.getStaffid());
				br.setModifytime(new Date());
				this.tblOppblackRecordMapper.updateById(br);
			}
			resultMap.put("code", "1");
			resultMap.put("msg", "保存成功");
			resultMap.put("brid", brid);
			return resultMap;
	}
	
	@Override
	public Map<String, Object> finOppsiteWarningList(Integer pageNumber, TblCyhwProjectbudget tcpb, Integer isFlowdb,
			String flowNumber, Integer view, Integer pageSize) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		
		tcpb.setOrgid(staff.getCurrentOrg().getOrgid());
		tcpb.setRecordtype(flowNumber);
		
		IPage<TblCyhwProjectbudget> page = new Page<TblCyhwProjectbudget>(pageNumber, pageSize);
        IPage<TblCyhwProjectbudget> pageList = tblCyhwProjectbudgetMapper.selectListByPageInfoFlowid(page,tcpb,staff, 0, null);
		
		
		//关联合同数量+履约数量
		List<TblCyhwProjectbudget> tlist = pageList.getRecords();
		List<TblCyhwProjectbudget> budgetlist = new ArrayList<TblCyhwProjectbudget>();
		/**
		 * 获取今年年份
		 */
		Calendar cal=Calendar.getInstance();       
		String dateStr = cal.get(Calendar.YEAR)+"-01-01";       
		Integer findListByXdf = 0;
		Integer findListByXdf2 = 0;
		Integer nowyearcount = 0;
		
		TblOppblackRecord record = null;
		long curretDate = new Date().getTime();
		for (TblCyhwProjectbudget tblCyhwProjectrBudget : tlist) {
			findListByXdf = this.tblCyhwUnitMapper.selectCyhwUnitCountByBudgetId(tblCyhwProjectrBudget.getBudgetid().toString());
			findListByXdf2 = this.tblCyhwUnitMapper.selectContractCountWyByBudgetId(tblCyhwProjectrBudget.getBudgetid().toString());//相对方违约数量
			//Hu改
			nowyearcount=this.tblCyhwUnitMapper.selectNowyearcount1(tblCyhwProjectrBudget.getBudgetid().toString(),dateStr);
			tblCyhwProjectrBudget.setNowyearCount(nowyearcount);
			
			tblCyhwProjectrBudget.setUnitCount(findListByXdf);
			tblCyhwProjectrBudget.setPerformanceCount(findListByXdf2);//相对方违约数量
			
			//判断是否在黑名单
			record = tblCyhwProjectrBudget.getBlackRecord();
			if(record != null) {
				
				if(record.getObrtype() == 2|| (record.getObrtype() == 1 && record.getBlackdeadtime().getTime() >= curretDate )) {
					tblCyhwProjectrBudget.setIsblack(1);
				}else {
					tblCyhwProjectrBudget.setIsblack(0);
				}
			}

			if (Objects.nonNull(tblCyhwProjectrBudget.getStaffid1())) {
				tblCyhwProjectrBudget.setRealname1(tblStaffService.getStaffName(tblCyhwProjectrBudget.getStaffid1()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getStaffid2())) {
				tblCyhwProjectrBudget.setRealname2(tblStaffService.getStaffName(tblCyhwProjectrBudget.getStaffid2()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getStaffid3())) {
				tblCyhwProjectrBudget.setRealname3(tblStaffService.getStaffName(tblCyhwProjectrBudget.getStaffid3()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getStaffid4())) {
				tblCyhwProjectrBudget.setRealname4(tblStaffService.getStaffName(tblCyhwProjectrBudget.getStaffid4()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getStaffid5())) {
				tblCyhwProjectrBudget.setRealname5(tblStaffService.getStaffName(tblCyhwProjectrBudget.getStaffid5()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getStaffids1())) {
				tblCyhwProjectrBudget.setRealnames1(tblStaffService.getStaffNames(tblCyhwProjectrBudget.getStaffids1()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getStaffids2())) {
				tblCyhwProjectrBudget.setRealnames2(tblStaffService.getStaffNames(tblCyhwProjectrBudget.getStaffids2()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getStaffids3())) {
				tblCyhwProjectrBudget.setRealnames3(tblStaffService.getStaffNames(tblCyhwProjectrBudget.getStaffids3()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getStaffids4())) {
				tblCyhwProjectrBudget.setRealnames4(tblStaffService.getStaffNames(tblCyhwProjectrBudget.getStaffids4()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getStaffids5())) {
				tblCyhwProjectrBudget.setRealnames5(tblStaffService.getStaffNames(tblCyhwProjectrBudget.getStaffids5()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getOrgid1())) {
				tblCyhwProjectrBudget.setOrgname1(tblOrganizaService.getOrgName(tblCyhwProjectrBudget.getOrgid1()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getOrgid2())) {
				tblCyhwProjectrBudget.setOrgname2(tblOrganizaService.getOrgName(tblCyhwProjectrBudget.getOrgid2()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getOrgid3())) {
				tblCyhwProjectrBudget.setOrgname3(tblOrganizaService.getOrgName(tblCyhwProjectrBudget.getOrgid3()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getOrgid4())) {
				tblCyhwProjectrBudget.setOrgname4(tblOrganizaService.getOrgName(tblCyhwProjectrBudget.getOrgid4()));
			}
			if (Objects.nonNull(tblCyhwProjectrBudget.getOrgid5())) {
				tblCyhwProjectrBudget.setOrgname5(tblOrganizaService.getOrgName(tblCyhwProjectrBudget.getOrgid5()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getOrgids1())) {
				tblCyhwProjectrBudget.setOrgnames1(tblOrganizaService.getOrgNames(tblCyhwProjectrBudget.getOrgids1()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getOrgids2())) {
				tblCyhwProjectrBudget.setOrgnames2(tblOrganizaService.getOrgNames(tblCyhwProjectrBudget.getOrgids2()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getOrgids3())) {
				tblCyhwProjectrBudget.setOrgnames3(tblOrganizaService.getOrgNames(tblCyhwProjectrBudget.getOrgids3()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getOrgids4())) {
				tblCyhwProjectrBudget.setOrgnames4(tblOrganizaService.getOrgNames(tblCyhwProjectrBudget.getOrgids4()));
			}
			if (StringUtils.isNotBlank(tblCyhwProjectrBudget.getOrgids5())) {
				tblCyhwProjectrBudget.setOrgnames5(tblOrganizaService.getOrgNames(tblCyhwProjectrBudget.getOrgids5()));
			}
			
			budgetlist.add(tblCyhwProjectrBudget);
		}
		
		PageInfo<TblCyhwProjectbudget> pageInfo = new PageInfo<TblCyhwProjectbudget>();
		pageInfo.setPageSize(pageSize);
		pageInfo.setCurrentPage(pageNumber);
		
		pageInfo.setCondition(tcpb);
		pageInfo.setTlist(budgetlist);
		pageInfo.setTotalRecord((int) pageList.getTotal());
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
		resultMap.put("data", pageInfo);
		resultMap.put("code", "1");
		resultMap.put("msg", "接口访问成功！");
		resultMap.put("dataMap", dataMap);
		return resultMap;
	}

	@Override
	public String projectrBudgetDetail(BigDecimal budgetid) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblCyhwProjectbudget tcpb = tblCyhwProjectbudgetMapper.getEntity(budgetid);
			List<TblCyhwBudetgoods> goodsList = this.tblCyhwBudetgoodsMapper.findkFirstLevel();
			List<TblCyhwBudetgoods> goodsListCh = null;

			if(tcpb.getRecordparent() != null){
				TblCyhwUnit tcu = tblCyhwUnitMapper.selectChangeContractInfo(tcpb.getRecordparent());
				resultMap.put("tcu", tcu);
			}
			//相对方外键
			if(tcpb.getRecordconcat() != null){
				TblCyhwProjectbudget tcpb1 = tblCyhwProjectbudgetMapper.getEntity(tcpb.getRecordconcat());
				resultMap.put("tcpb1", tcpb1);
			}
			if(tcpb.getGoodstype() != null){
				resultMap.put("choice",0);
				goodsListCh = tblCyhwBudetgoodsMapper.findkListByParentId(tcpb.getGoodstype());
			}else{
				resultMap.put("choice",1);
			}
			//String searchUrl = this.tblFlowService.findMappingUrl(flowid);
			//resultMap.put("searchUrl",searchUrl);
			resultMap.put("tcpb", tcpb);
			//String flowname = request.getParameter("flowname");
			//resultMap.put("flowname",flowname);
			resultMap.put("goodsList", goodsList);
			resultMap.put("goodsListCh", goodsListCh);
			//resultMap.put("flowid",flowid);
		} catch (Exception e) {
			log.error("异常信息：", e);
		}
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		return jsonObjectMV.toString();
	}

	@Override
	public Map<String, Object> findOppsiteBlackList(Integer pageNumber, TblCyhwProjectbudget tcpb, String flowNumber,
			Integer pageSize, String staffId, String choose) throws Exception {
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "用户已失效！");
			return resultMap;
		}
		// 判断用户是否有权限
		boolean taskCount = true;
		/**
		 * 相对方管理
		 */
		// 设置查询条件
		tcpb.setRecordtype(TblCyhwProjectbudget.FLOWNUMBER);
		tcpb.setOrgid(staff.getCurrentOrg().getOrgid());
		if (!"".equals(choose) && choose != null) {
			tcpb.setBudgetname(choose);
		}
		//tcpb.setBlacktype(3);
		//tcpb.setBlackAprStatus(6);
		
		if (JudgeRoleRight.judgeRoleRight("合同管理员", staff.getRoleNames())) {
				taskCount = false;
		}
		// 查询相对方黑名单列表
		
		IPage<TblCyhwProjectbudget> page = new Page<TblCyhwProjectbudget>(pageNumber, pageSize);
        IPage<TblCyhwProjectbudget> pageList = tblCyhwProjectbudgetMapper.selectOppsiteBlackList(page,tcpb,staff, taskCount);
        /*if (CollectionUtil.isNotEmpty(pageList.getRecords())){
			pageList.getRecords().forEach(item->{
				if (Objects.nonNull(item.getStaffid1())) {
					item.setRealname1(tblStaffService.getStaffName(item.getStaffid1()));
				}
				if (Objects.nonNull(item.getStaffid2())) {
					item.setRealname2(tblStaffService.getStaffName(item.getStaffid2()));
				}
				if (Objects.nonNull(item.getStaffid3())) {
					item.setRealname3(tblStaffService.getStaffName(item.getStaffid3()));
				}
				if (Objects.nonNull(item.getStaffid4())) {
					item.setRealname4(tblStaffService.getStaffName(item.getStaffid4()));
				}
				if (Objects.nonNull(item.getStaffid5())) {
					item.setRealname5(tblStaffService.getStaffName(item.getStaffid5()));
				}
				if (StringUtils.isNotBlank(item.getStaffids1())) {
					item.setRealnames1(tblStaffService.getStaffNames(item.getStaffids1()));
				}
				if (StringUtils.isNotBlank(item.getStaffids2())) {
					item.setRealnames2(tblStaffService.getStaffNames(item.getStaffids2()));
				}
				if (StringUtils.isNotBlank(item.getStaffids3())) {
					item.setRealnames3(tblStaffService.getStaffNames(item.getStaffids3()));
				}
				if (StringUtils.isNotBlank(item.getStaffids4())) {
					item.setRealnames4(tblStaffService.getStaffNames(item.getStaffids4()));
				}
				if (StringUtils.isNotBlank(item.getStaffids5())) {
					item.setRealnames5(tblStaffService.getStaffNames(item.getStaffids5()));
				}
				if (Objects.nonNull(item.getOrgid1())) {
					item.setOrgname1(tblOrganizaService.getOrgName(item.getOrgid1()));
				}
				if (Objects.nonNull(item.getOrgid2())) {
					item.setOrgname2(tblOrganizaService.getOrgName(item.getOrgid2()));
				}
				if (Objects.nonNull(item.getOrgid3())) {
					item.setOrgname3(tblOrganizaService.getOrgName(item.getOrgid3()));
				}
				if (Objects.nonNull(item.getOrgid4())) {
					item.setOrgname4(tblOrganizaService.getOrgName(item.getOrgid4()));
				}
				if (Objects.nonNull(item.getOrgid5())) {
					item.setOrgname5(tblOrganizaService.getOrgName(item.getOrgid5()));
				}
				if (StringUtils.isNotBlank(item.getOrgids1())) {
					item.setOrgnames1(tblOrganizaService.getOrgNames(item.getOrgids1()));
				}
				if (StringUtils.isNotBlank(item.getOrgids2())) {
					item.setOrgnames2(tblOrganizaService.getOrgNames(item.getOrgids2()));
				}
				if (StringUtils.isNotBlank(item.getOrgids3())) {
					item.setOrgnames3(tblOrganizaService.getOrgNames(item.getOrgids3()));
				}
				if (StringUtils.isNotBlank(item.getOrgids4())) {
					item.setOrgnames4(tblOrganizaService.getOrgNames(item.getOrgids4()));
				}
				if (StringUtils.isNotBlank(item.getOrgids5())) {
					item.setOrgnames5(tblOrganizaService.getOrgNames(item.getOrgids5()));
				}
			});
		}*/
		
		PageInfo<TblCyhwProjectbudget> pageInfo = new PageInfo<TblCyhwProjectbudget>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		pageInfo.setCondition(tcpb);
		
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int) pageList.getTotal());
		// 设置返回结果
		resultMap.put("data", pageInfo);
		resultMap.put("code", "1");
		resultMap.put("msg", "数据访问成功");
		
		return resultMap;
	}

	@Override
	public Map<String, Object> addOppsitePartyBlackList(BigDecimal budgetid) throws Exception {
		Map<String,Object> resultMap  = new HashMap<String, Object>(0);
		
		TblCyhwProjectbudget tcpb = tblCyhwProjectbudgetMapper.getEntity(budgetid);
		
		String searchUrl = "/nbkz/cwgl/zcgl_main";
		// 将查询到的数据放入resultMap
		resultMap.put("searchUrl",searchUrl);
		//resultMap.put("/nbkz/contract/addOppsitePartyBlackList");
		resultMap.put("data", tcpb);
		//String flowname = request.getParameter("flowname");
		return resultMap;
	}

	@Override
	public JsonBean removeOppsitePartyBlack(BigDecimal budgetid, String brid, String rmid, String remreason, String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblOppblackRemove rb = new TblOppblackRemove();
		
		if(StringUtils.isNotBlank(rmid)) {
			//主键不为空修改
			rb.setRbid(rmid);
			rb.setModifystaff(staff.getStaffid());
			rb.setModifytime(new Date());
			rb.setRemreason(remreason);
			this.tblOppblackRemoveMapper.updateById(rb);
		}else {
			//主键为空新增
			rb.setRbid(RandomUtil.uuStringId());
			rb.setCreatestaff(staff.getStaffid());
			rb.setCreatetime(new Date());
			rb.setLinkdeptid(staff.getLinkDetp().getOrgid());
			rb.setLinkorgid(staff.getCurrentOrg().getOrgid());
			rb.setOppoid(budgetid);
			rb.setBrid(brid);
			rb.setRemreason(remreason);
			rb.setRmstatus(YMUrlStatic.STATE_YWC);
			this.tblOppblackRemoveMapper.insert(rb);
			
			if(rb.getRmstatus() == 6) {
				this.tblCyhwProjectbudgetMapper.executeSql(budgetid, null);
			}
			
		}
		 return ResponseFormat.retParam(1, 200, rb);
	}

	@Override
	public List<TblCyhwProjectbudget> blacklistExport(BigDecimal orgid, TblCyhwProjectbudget tcbp, TblStaffUtil staff) throws Exception {
		boolean taskCount = true;
		if (JudgeRoleRight.judgeRoleRight("合同管理员", staff.getRoleNames())) {
			taskCount = false;
		}
		
		List<TblCyhwProjectbudget> objList = this.tblCyhwProjectbudgetMapper.selectBlackList(orgid,staff,taskCount,tcbp);
		return objList;
	}

	@Override
	public void invoiceCounterpartInfoListByPageInfo(PageInfo<TblCyhwProjectbudget> pageInfo, TblCyhwProjectbudget tcpb)
			throws Exception {
		IPage<TblCyhwProjectbudget> page = new Page<TblCyhwProjectbudget>(pageInfo.getCurrentPage(), pageInfo.getPageSize());
        IPage<TblCyhwProjectbudget> pageList = tblCyhwProjectbudgetMapper.findCollectionChoiceContractPid(page,tcpb);
		
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
	}

	@Override
	public List<TblCyhwProjectbudget> getContractBudgetList(BigDecimal contractId)
			throws Exception {
        return tblCyhwProjectbudgetMapper.getContractBudgetList(contractId);
	}

	@Override
	public Map<String, Object> projectrBudgetToModify(BigDecimal flowId, BigDecimal budgetId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
		try {
			TblCyhwProjectbudget tcpb = tblCyhwProjectbudgetMapper.findBybudgetId(budgetId);
			List<TblAttachment> attList = tblAttachmentMapper.findAttachmentListByBudgetId(budgetId);
			
			if (tcpb.getRecordparent() != null) {
				TblCyhwUnit item = tblCyhwUnitMapper.getEntity(tcpb.getRecordparent());
				if (Objects.nonNull(item.getStaffid1())) {
					item.setRealname1(tblStaffService.getStaffName(item.getStaffid1()));
				}
				if (Objects.nonNull(item.getStaffid2())) {
					item.setRealname2(tblStaffService.getStaffName(item.getStaffid2()));
				}
				if (Objects.nonNull(item.getStaffid3())) {
					item.setRealname3(tblStaffService.getStaffName(item.getStaffid3()));
				}
				if (Objects.nonNull(item.getStaffid4())) {
					item.setRealname4(tblStaffService.getStaffName(item.getStaffid4()));
				}
				if (Objects.nonNull(item.getStaffid5())) {
					item.setRealname5(tblStaffService.getStaffName(item.getStaffid5()));
				}
				if (StringUtils.isNotBlank(item.getStaffids1())) {
					item.setRealnames1(tblStaffService.getStaffNames(item.getStaffids1()));
				}
				if (StringUtils.isNotBlank(item.getStaffids2())) {
					item.setRealnames2(tblStaffService.getStaffNames(item.getStaffids2()));
				}
				if (StringUtils.isNotBlank(item.getStaffids3())) {
					item.setRealnames3(tblStaffService.getStaffNames(item.getStaffids3()));
				}
				if (StringUtils.isNotBlank(item.getStaffids4())) {
					item.setRealnames4(tblStaffService.getStaffNames(item.getStaffids4()));
				}
				if (StringUtils.isNotBlank(item.getStaffids5())) {
					item.setRealnames5(tblStaffService.getStaffNames(item.getStaffids5()));
				}
				if (Objects.nonNull(item.getOrgid1())) {
					item.setOrgname1(tblOrganizaService.getOrgName(item.getOrgid1()));
				}
				if (Objects.nonNull(item.getOrgid2())) {
					item.setOrgname2(tblOrganizaService.getOrgName(item.getOrgid2()));
				}
				if (Objects.nonNull(item.getOrgid3())) {
					item.setOrgname3(tblOrganizaService.getOrgName(item.getOrgid3()));
				}
				if (Objects.nonNull(item.getOrgid4())) {
					item.setOrgname4(tblOrganizaService.getOrgName(item.getOrgid4()));
				}
				if (Objects.nonNull(item.getOrgid5())) {
					item.setOrgname5(tblOrganizaService.getOrgName(item.getOrgid5()));
				}
				if (StringUtils.isNotBlank(item.getOrgids1())) {
					item.setOrgnames1(tblOrganizaService.getOrgNames(item.getOrgids1()));
				}
				if (StringUtils.isNotBlank(item.getOrgids2())) {
					item.setOrgnames2(tblOrganizaService.getOrgNames(item.getOrgids2()));
				}
				if (StringUtils.isNotBlank(item.getOrgids3())) {
					item.setOrgnames3(tblOrganizaService.getOrgNames(item.getOrgids3()));
				}
				if (StringUtils.isNotBlank(item.getOrgids4())) {
					item.setOrgnames4(tblOrganizaService.getOrgNames(item.getOrgids4()));
				}
				if (StringUtils.isNotBlank(item.getOrgids5())) {
					item.setOrgnames5(tblOrganizaService.getOrgNames(item.getOrgids5()));
				}
				dataMap.put("tcu", item);
				List<TblContractAppendixsigning> signingList = this.tblContractAppendixsigningMapper.findFileListByContractId(item.getContractid());
				dataMap.put("signingList", signingList);
			}
			//相对方外键
			if (tcpb.getRecordconcat() != null) {
				TblCyhwProjectbudget tcpb1 = tblCyhwProjectbudgetMapper.getEntity(tcpb.getRecordparent());
				dataMap.put("tcpb1", tcpb1);
			}
			dataMap.put("attList", attList);
			dataMap.put("tcpb", tcpb);
			dataMap.put("flowId", flowId);
			resultMap.put("code", "1");
			resultMap.put("msg", "成功！");
			resultMap.put("data", dataMap);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> projectrBudgettoAdd(BigDecimal flowId, BigDecimal contractId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		Map<String, Object> dataMap = new HashMap<String, Object>(0);
		try {
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMSSS");
			SimpleDateFormat nyr = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			java.util.Date date = cal.getTime();
			String myTime = sdFormat.format(date);
			if (flowId != null && flowId.toString().trim().length() > 0) {
				TblFlow flow = tblFlowMapper.findById(flowId.toString());
				dataMap.put("code", flow.getFlownumber() + "_" + myTime);
				if ("HTGL003".equals(flow.getFlownumber()) && contractId != null) {
					TblCyhwUnit unit = this.tblCyhwUnitMapper.getEntity(contractId);
					dataMap.put("unit", unit);
				}
				dataMap.put("searchUrl", ((flow.getFlowmappingurl() == null) ? "/nbkz/cwgl/zcgl_main" : flow.getFlowmappingurl()));
			}
			List<TblCyhwBudetgoods> goodsList = tblCyhwBudetgoodsMapper.findkFirstLevel();
			String createDate = DateUtils.getNowTime("yyyy-MM-dd");
			dataMap.put("createDate", createDate);
			dataMap.put("goodsList", goodsList);
			resultMap.put("code", "1");
			resultMap.put("msg", "接口访问成功！");
			resultMap.put("dataMap", dataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> insertOrUpdateBybudget(TblCyhwProjectbudget tcpb, String token, String attids,
			String flowId, BigDecimal sealorgid, BigDecimal singingId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			
			/*BigDecimal contractId = tcpb.getRecordparent();
			TblCyhwUnit unit = this.tblCyhwUnitMapper.getEntity(contractId);
			if("2".equals(unit.getContractstatus()+"")) {
				resultMap.put("code", "0");
				resultMap.put("msg", "请在“我的待办”处理需调整的用印信息！");
			}*/
			
			/*TblFlow flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwProjectbudget.HTYY,staff.getCurrentOrg().getOrgid());
		    if(flow == null) {
		    	resultMap.put("code", "0");
		    	resultMap.put("msg", "流程未定义！");
		    	return resultMap;
		    }*/
			tcpb.setRecordtype(TblCyhwProjectbudget.HTYY);
			tcpb.setCreateuser(staff.getStaffid());
			tcpb.setOrgid(staff.getCurrentOrg().getOrgid());
			//tcpb.setFlowid(flow.getFlowid());
		
			//tcpb.setInspectionstatus(6);
			if (tcpb.getBudgetid() != null) {
				this.tblCyhwProjectbudgetMapper.updateOppositeInfoById(tcpb,sealorgid);
			} else {
				tcpb.setBudgetid(RandomUtil.uuBigDecimalId());
				this.tblCyhwProjectbudgetMapper.insertOppositeParty(tcpb,sealorgid);
			}
			
			if (attids != null && !"".equals(attids)) {
				String[] ids = attids.split(",");
				for (String id : ids) {
					this.tblCyhwProjectbudgetMapper.insertAttmentRelation(id, tcpb.getBudgetid());
				}
			}
			resultMap.put("code", "1");
			resultMap.put("msg", "保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> findbudgteInfoById(BigDecimal budgetId, String token) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}
			Map<String, Object> dataMap = new HashMap<String, Object>(0);
			
			TblCyhwProjectbudget budget = this.tblCyhwProjectbudgetMapper.selectAllInfoById(budgetId);
			dataMap.put("budget", budget);
			resultMap.put("code", "1");
			resultMap.put("msg", "保存成功！");
			resultMap.put("data", dataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> loadAddOppositePartyInfo(String flowId, Integer contractId, String choiceSearch,
			String token, String staffId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		Map<String, Object> dateMap = new HashMap<String, Object>(0);
		// 解析用户令牌，获取用户信息
		try {
			TblStaffUtil staff = userProvider.get();
			if (staff == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "用户已失效！");
				return resultMap;
			}

			// 获取流程信息
			/*TblFlow flow = tblFlowMapper.findFlowInfoByNumberOrgId(TblCyhwProjectbudget.FLOWNUMBER,staff.getCurrentOrg().getOrgid());
			if(flow == null) {
				resultMap.put("code", "0");
				resultMap.put("msg", "流程未定义！");
				return resultMap;
			}
			if ("HTGL003".equals(flow.getFlownumber()) && contractId != null) {
				//TblCyhwUnit unit = this.tblCyhwUnitService.getEntity(contractId);
				//mv.addObject("unit", unit);
			}*/
			// 获取用户所在组织的编码
			String writen = this.tblOrganizationMapper.selectWrittenDeptByOrgId(staff.getLinkOrg().getOrgid());
			// 拼接相对方编号
			String counterpartno = writen+"-XDF-";
			if(StringUtils.isBlank(writen)) {
				counterpartno = staff.getLinkOrg().getOrgid()+"-XDF-";
			}
			
			// 获取自动编号
			Integer autoNum = this.tblCyhwProjectbudgetMapper.findAutoNumber(counterpartno+"%",staff.getCurrentOrg().getOrgid());
			if (autoNum != null) {
				autoNum++;
			}else {
				autoNum = 1;
			}
			// 拼接完整的相对方编号
			counterpartno += autoNum;
			// 设置日期相关的信息
			dateMap.put("counterpartno", counterpartno);
			dateMap.put("code", TblCyhwProjectbudget.FLOWNUMBER + "_" +  DateUtil.parseDate(new Date(), "yyyyMMddHHmmss"));
			String createDate = DateUtil.getNowTime("yyyy-MM-dd");
			dateMap.put("createDate", createDate);
			dateMap.put("staffInfo", staff);

			resultMap.put("code", "1");
			resultMap.put("msg", "访问接口成功");
			resultMap.put("data", dateMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

}
