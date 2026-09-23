package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblAuthorizationRecord;
import com.huabo.system.entity.TblContractTypeActivity;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblSecrectLevel;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemRight;
import com.huabo.system.mapper.TblAuthorizationRecordMapper;
import com.huabo.system.mapper.TblContractTypeActivityMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblSecrectLevelMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSystemRightMapper;
import com.huabo.system.mapper.TblSystemSheetTableMapper;
import com.huabo.system.service.TblAuthorizationRecordService;
import com.huabo.system.service.TblAutonoNumberService;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblRoleService;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.TblSystemRightService;
import com.huabo.system.service.TblUserOrgRelationService;
import com.huabo.system.service.YMBusinessService;
import com.huabo.system.vo.TblAuthorizationRecordVo;


@Service
public class TblAuthorizationRecordServiceImpl implements TblAuthorizationRecordService {
	
	@Resource
	private UserProvider userProvider;
	
	@Resource
    private TblAuthorizationRecordMapper tblAuthorizationRecordMapper;
	
	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
	private TblAutonoNumberService tblAutonoNumberService;
	
	@Resource
	private YMBusinessService ymBusinessService;
	
	@Resource
	private TblStaffMapper tblStaffMapper;
	
	@Resource
	private TblUserOrgRelationService tblUserOrgRelationService;
	
	@Resource
	private TblSystemRightMapper tblSystemRightMapper;
	
	@Resource
	private TblSecrectLevelMapper tblSecrectLevelMapper;
	
	@Resource
	private TblOrganizaService tblOrganizaService;
	
	@Resource
	private TblRoleService tblRoleService;
	
	@Resource
	private TblStaffService tblStaffService;
	
	@Resource
	private TblSystemRightService tblSystemRightService;
	
	@Resource
	private TblContractTypeActivityMapper tblContractTypeActivityMapper;
	
	@Resource
	private TblSystemSheetTableMapper tblSystemSheetTableMapper;

	@Override
	public void addEntity(TblAuthorizationRecord confirm) throws Exception {
		this.tblAuthorizationRecordMapper.insert(confirm);
	}

	@Override
	public TblAuthorizationRecord findSpzRecordInfoByTargetId(String targetId) throws Exception {
		return this.tblAuthorizationRecordMapper.selectSpzRecordInfoByTargetId(targetId);
	}

	@Override
	public void modifyEntity(TblAuthorizationRecord confirm) throws Exception {
		this.tblAuthorizationRecordMapper.updateById(confirm);
	}
	
	@Override
	public JsonBean verifyOperation(String targetId, String targetType,int operationType) throws Exception {
		Integer count = 0;
		if(TblAuthorizationRecord.TARGETTYPECONTRACTFLOW.equals(targetType)) {
			TblContractTypeActivity act = this.tblContractTypeActivityMapper.selectById(targetId);
			
			if(TblAuthorizationRecord.OPERATIONREMOVE == operationType) {
				count = this.tblAuthorizationRecordMapper.findSpzRecordCountByTargetId(targetId);
			}else {
				count = this.tblAuthorizationRecordMapper.findContractFLowSpzRecordCountByTargetId(targetId,act.getOrgId(),act.getTableId(),act.getTypeId());
			}
			
		}else {
			count = this.tblAuthorizationRecordMapper.findSpzRecordCountByTargetId(targetId);
		}
		
		return ResponseFormat.retParam(1, 200, count);
	}
	

	@Override
	public JsonBean findById(String recordId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		TblAuthorizationRecord record = this.tblAuthorizationRecordMapper.selectById(recordId);
		resultMap.put("primaryInfo", record);
		switch (record.getTargetType()) {
			case TblAuthorizationRecord.TARGETTYPECOMPANY:
			case TblAuthorizationRecord.TARGETTYPEDEPT:
				TblOrganization org = this.tblOrganizationMapper.selectByOrgId(new BigDecimal(record.getTargetId()));
				resultMap.put("beforeData", org);
			break;
			case TblAuthorizationRecord.TARGETTYPEUSER: 
				TblStaff prestaff = this.tblStaffService.findByStaffid(new BigDecimal(record.getTargetId()));
				if(record.getOperationType() == 1 && prestaff == null) {
					prestaff = this.tblStaffService.findByNewStaffid(record.getOperationData());
				}
				resultMap.put("beforeData", prestaff);
				break;
			case TblAuthorizationRecord.TARGETTYPERIGHT: 
				TblSystemRight right = this.tblSystemRightMapper.findById(new BigDecimal(record.getTargetId()));
				resultMap.put("beforeData", right);
				break;
			case TblAuthorizationRecord.TARGETTYPESECRECT: 
				TblSecrectLevel secrect = this.tblSecrectLevelMapper.selectById(record.getTargetId());
				resultMap.put("beforeData", secrect);
				break;
		}
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	@Override
	public JsonBean findPageList(TblAuthorizationRecordVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<TblAuthorizationRecord> wrapper = new QueryWrapper<TblAuthorizationRecord>();
		wrapper.select("RECORDID,RECORDTEXT,OPERATIONTYPE,TARGETTYPE,TARGETID,STATUS,CREATOR,MODIFIER,CREATORNAME,MODIFYERNAME,CREATIONTIME,MODIFIEDTIME");
		
		if(vo.getOperationType() != 0) {
			wrapper.eq("OPERATIONTYPE", vo.getOperationType());
		}
		if(StringUtils.isNotBlank(vo.getRecordText())) {
			wrapper.like("RECORDTEXT", vo.getRecordText());
		}
		if(vo.getStatus() != null) {
			wrapper.eq("STATUS", vo.getStatus());
		}
		if(StringUtils.isNotBlank(vo.getTargetId())) {
			wrapper.eq("TARGETID", vo.getTargetId());
		}
		if(StringUtils.isNotBlank(vo.getTargetType())) {
			wrapper.eq("TARGETTYPE", vo.getTargetType());
		}
		wrapper.orderByDesc("CREATIONTIME");
		Page<TblAuthorizationRecord> page = new Page<TblAuthorizationRecord>(vo.getPageNumber(), vo.getPageSize());
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblAuthorizationRecord> pageList = this.tblAuthorizationRecordMapper.selectPage(page, wrapper);
		return ResponseFormat.retParam(1, 200, pageList);
	}
	
	
	@Override
	public JsonBean modifyFlowEndModifyOperationData(String recordId, TblStaffUtil loginStaff) throws Exception {
		TblOrganization org = null;
		TblStaff staff = null;
		TblSystemRight right = null;
		TblSecrectLevel secrect = null;
		TblRole role = null;
		String dataStr = null;
		TblAuthorizationRecord record = this.tblAuthorizationRecordMapper.selectById(recordId);
		switch (record.getTargetType()) {
			case TblAuthorizationRecord.TARGETTYPECOMPANY:
				//维护公司信息；
				org = JSONObject.parseObject(record.getOperationData(), TblOrganization.class);
				switch (record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONINSERT:
						this.tblOrganizationMapper.insert(org);
						if (org.getIsautonumber() == 1) {
		                    tblAutonoNumberService.saveOrgNumber(org.getOrgid());
		                }
						if(YMUrlStatic.status == 0) {
		                 	//系统管理员登录  新增组织
		                 	this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.ORGINSERT);
		                }
						break;
					case TblAuthorizationRecord.OPERATIONMODIFY:
						this.tblOrganizationMapper.updateById(org);
						if (org.getIsautonumber() == 1) {
		                    tblAutonoNumberService.saveOrgNumber(org.getOrgid());
		                }
						 //公司数据同步至业务中台
		                if(YMUrlStatic.status == 0) {
		                	this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.ORGUPDATE);
		                }
						break;
					case TblAuthorizationRecord.OPERATIONREMOVE:
						this.tblOrganizationMapper.deleteById(org.getOrgid());
						break;
					case TblAuthorizationRecord.OPERATIONENABLE:
					case TblAuthorizationRecord.OPERATIONDEPRECATED:
						String targetIdString = org.getOrgid().toString();
						List<TblOrganization> childrenList = new ArrayList<TblOrganization>(0);
						this.tblOrganizationMapper.updateOrgInfoStatusByIds(targetIdString,org.getStatus());
						
						//获取子集数据并修改
						QueryWrapper<TblOrganization> wrapper = new QueryWrapper<TblOrganization>();
						if(org.getOrgtype() == null || org.getOrgtype() > 0) {
							wrapper.between("ORGTYPE", 1, 100);
						}else {
							wrapper.eq("ORGTYPE", 0);
						}
						wrapper.select("ORGID,ORGNAME,FATHERORGID,ORGANIZATIONTREES");
						wrapper.orderByAsc("ORGTYPE,ORDERID");
						List<TblOrganization> orgList = this.tblOrganizationMapper.selectList(wrapper);
						if(orgList != null && orgList.size() > 0) {
							childrenList = orgList.stream()
									.filter(item -> !targetIdString.equals(item.getOrgid().toString()))
									.filter(item -> StringUtils.isNotBlank(item.getOrganizationTrees()) && (item.getOrganizationTrees().equals(targetIdString) || 
											item.getOrganizationTrees().startsWith(targetIdString + ",") ||
											item.getOrganizationTrees().contains("," + targetIdString + ",") ||
											item.getOrganizationTrees().endsWith("," + targetIdString)))
									.collect(Collectors.toList());
						}
						for (TblOrganization chil : childrenList) {
							this.tblOrganizationMapper.updateOrgInfoStatusByIds(chil.getOrgid().toString(),org.getStatus());
						}
						break;
					default:
						break;
				}
				break;
			case TblAuthorizationRecord.TARGETTYPEDEPT:
				//维护部门信息
				org = JSONObject.parseObject(record.getOperationData(), TblOrganization.class);
				
				switch (record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONINSERT:
						this.tblOrganizationMapper.insert(org);
						if(YMUrlStatic.status == 0) {
		                	this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.DEPTINSERT);
		                	//当用户的直属主管分管领导在部门维护时，调用此方法 更新当前部门下所有用户的直属主管
		                	//this.orgUserInfoSynchronizationService.setUserOrgRelationPostUnique(organization);
		                }
		                if (org.getOrgtype() != null && org.getOrgtype() != 0) {
		                    TblOrganization neworg = tblOrganizaService.isCompanyAddWPZ(org);
		                }
						break;
					case TblAuthorizationRecord.OPERATIONMODIFY:
						this.tblOrganizationMapper.updateById(org);
						if(YMUrlStatic.status == 0) {
		                	this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.DEPTUPDATE);
		                	//当用户的直属主管分管领导在部门维护时，调用此方法 更新当前部门下所有用户的直属主管
		                	//this.orgUserInfoSynchronizationService.setUserOrgRelationPostUnique(organization);
		                }
		                if (org.getOrgtype() != null && org.getOrgtype() != 0) {
		                    TblOrganization neworg = tblOrganizaService.isCompanyAddWPZ(org);
		                }
						break;
					case TblAuthorizationRecord.OPERATIONREMOVE:
						this.tblOrganizationMapper.deleteById(org.getOrgid());
						break;
				}
				break;
			case TblAuthorizationRecord.TARGETTYPEUSER: 
				staff = JSONObject.parseObject(record.getOperationData(), TblStaff.class);
				
				switch (record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONINSERT:
						tblStaffMapper.insertUser(staff);
						this.tblUserOrgRelationService.removeRelationByStaffId(staff.getStaffid());
	                    this.tblUserOrgRelationService.InsertRealtionByStaffId(staff.getStaffid(),staff.getRelaList());
	        	        if(YMUrlStatic.status == 0) {
	        	        	this.ymBusinessService.dealUniqueStaffInfo(staff.getStaffid(),staff.getRelaList());
	        	        }
						break;
					case TblAuthorizationRecord.OPERATIONMODIFY:
						tblStaffMapper.updateStaff(staff);
						this.tblUserOrgRelationService.removeRelationByStaffId(staff.getStaffid());
	                    this.tblUserOrgRelationService.InsertRealtionByStaffId(staff.getStaffid(),staff.getRelaList());
	        	        if(YMUrlStatic.status == 0) {
	        	        	this.ymBusinessService.dealUniqueStaffInfo(staff.getStaffid(),staff.getRelaList());
	        	        }
						break;
					case TblAuthorizationRecord.OPERATIONRESETPWD:
						tblStaffMapper.updateStaff(staff);
						break;
					default:
						break;
				}
				break;
			case TblAuthorizationRecord.TARGETTYPERIGHT: 
				right = JSONObject.parseObject(record.getOperationData(), TblSystemRight.class);
				switch (record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONINSERT:
							this.tblSystemRightMapper.insertTblManageRight(right);
						break;
					case TblAuthorizationRecord.OPERATIONMODIFY:
							this.tblSystemRightMapper.updateTblSystemRight(right);
						break;
					case TblAuthorizationRecord.OPERATIONDEPRECATED:
					case TblAuthorizationRecord.OPERATIONENABLE:
							this.tblSystemRightMapper.updateSystemRightVisible(right.getId(), right.getVisible());
						break;
					case TblAuthorizationRecord.OPERATIONREMOVE:
							this.tblSystemRightMapper.deleteOrgRIght(right.getId());
							this.tblSystemRightMapper.deleteRoleRIght(right.getId());
							this.tblSystemRightMapper.deleteSystemRIght(right.getId());
						break;
					default:
						break;
				}
				break;
			case TblAuthorizationRecord.TARGETTYPESECRECT: 
				secrect = JSONObject.parseObject(record.getOperationData(), TblSecrectLevel.class);
				switch (record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONINSERT:
							this.tblSecrectLevelMapper.insert(secrect);
						break;
					case TblAuthorizationRecord.OPERATIONMODIFY:
							this.tblSecrectLevelMapper.updateById(secrect);
						break;
					case TblAuthorizationRecord.OPERATIONREMOVE:
							this.tblSecrectLevelMapper.deleteById(secrect.getLevelId());
						break;
					default:
						break;
				}
				break;
			case TblAuthorizationRecord.TARGETTYPEFLOW: 
				JSONObject dataJson = JSONObject.parseObject(record.getOperationData());
				switch (record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONDEPRECATED:
					case TblAuthorizationRecord.OPERATIONENABLE:
							this.tblSystemSheetTableMapper.removeYmWorkFormInfo(dataJson.getBigDecimal("tableId"),dataJson.getBigDecimal("orgid"));
							this.tblSystemSheetTableMapper.startYmWorkFormInfo(dataJson.getBigDecimal("tableId"),dataJson.getString("ymWorkForm"),dataJson.getBigDecimal("orgid"),dataJson.getInteger("qystatus"));
						break;
					case TblAuthorizationRecord.OPERATIONREMOVE:
						Map<String, String> headerMap = new HashMap<>();
						headerMap.put("Authorization",loginStaff.getYmToken()); 
						this.ymBusinessService.removeFlowInfo(dataJson.getString("ymWorkId"));
						//数据库中删除维护信息
						this.tblSystemSheetTableMapper.deleteTableYmFlowInfo(dataJson.getBigDecimal("tableId"),dataJson.getString("ymWorkId"),dataJson.getBigDecimal("orgid"));
						break;
					default:
						break;
				}
				break;
			case TblAuthorizationRecord.TARGETTYPECONTRACTFLOW:
				TblContractTypeActivity act = JSONObject.parseObject(record.getOperationData(),TblContractTypeActivity.class);
				switch (record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONDEPRECATED:
					case TblAuthorizationRecord.OPERATIONENABLE:
						this.tblContractTypeActivityMapper.removeYmWorkFormInfo(act);
						this.tblContractTypeActivityMapper.updateById(act);
						break;
					case TblAuthorizationRecord.OPERATIONREMOVE:
						Map<String, String> headerMap = new HashMap<>();
						headerMap.put("Authorization",loginStaff.getYmToken()); 
						//调用接口删除流程信息
						String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getWorkFlowInfo+"/"+act.getYmWorkFrom(),null,headerMap,loginStaff,HttpClient.HPPTDELETE,null);
						
						//数据库中删除维护信息
						this.tblContractTypeActivityMapper.deleteById(act.getActivityId());
						break;
					default:
						break;
				}
				break;
			case TblAuthorizationRecord.TARGETTYPEGRANT:
					switch(record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONGRANTUSER:
						dataStr = record.getOperationData();
						dataJson = JSONObject.parseObject(dataStr);
						this.tblRoleService.grantToUser(dataJson.getString("staffids"),record.getTargetId());
						break;
					case TblAuthorizationRecord.OPERATIONUNUSER:
						dataStr = record.getOperationData();
						dataJson = JSONObject.parseObject(dataStr);
						this.tblRoleService.unGrantFromUser(dataJson.getString("staffids"),record.getTargetId());
						break;
					case TblAuthorizationRecord.OPERATIONGRANTORG:
						dataStr = record.getOperationData();
						dataJson = JSONObject.parseObject(dataStr);
						this.tblRoleService.grantToOrg(dataJson.getString("orgids"),record.getTargetId());
						break;
					case TblAuthorizationRecord.OPERATIONUNORG:
						dataStr = record.getOperationData();
						dataJson = JSONObject.parseObject(dataStr);
						this.tblRoleService.unGrantFromOrg(dataJson.getString("orgids"),record.getTargetId());
						break;
					case TblAuthorizationRecord.OPERATIONGRANTMENU:
						dataStr = record.getOperationData();
						dataJson = JSONObject.parseObject(dataStr);
						this.tblSystemRightService.grantRoleRight(dataJson.getString("rightIds"),new BigDecimal(record.getTargetId()),dataJson.getString("moduleType"));
						break;
					case TblAuthorizationRecord.OPERATIONGRANTDATA:
						dataStr = record.getOperationData();
						dataJson = JSONObject.parseObject(dataStr);
						this.tblSystemRightService.grantRoleDataRight(new BigDecimal(record.getTargetId()),dataJson.getString("companyIds"));
						break;
					case TblAuthorizationRecord.OPERATIONGUNRANTDATA:
						dataStr = record.getOperationData();
						dataJson = JSONObject.parseObject(dataStr);
						this.tblSystemRightService.removeDataRight(new BigDecimal(record.getTargetId()),dataJson.getString("dataJson"));
						break;
					default:
						break;
				}
				break;
			case TblAuthorizationRecord.TARGETTYPEROLE:
				role = JSONObject.parseObject(record.getOperationData(), TblRole.class);
				switch(record.getOperationType()) {
					case TblAuthorizationRecord.OPERATIONINSERT:
						tblRoleService.save(role);
						if(YMUrlStatic.status == 0 && !TblRole.ADMINAME.equals(role.getRname())){
							this.ymBusinessService.dealUniqueRoleInfo(role.getRid());
						}
						break;
					case TblAuthorizationRecord.OPERATIONMODIFY:
						tblRoleService.update(role);
						if(YMUrlStatic.status == 0 && !TblRole.ADMINAME.equals(role.getRname())){
							this.ymBusinessService.dealUniqueRoleInfo(role.getRid());
						}
						break;
					case TblAuthorizationRecord.OPERATIONDEPRECATED:
					case TblAuthorizationRecord.OPERATIONENABLE:
						tblRoleService.update(role);
						break;
					case TblAuthorizationRecord.OPERATIONREMOVE:
						tblRoleService.delete(role);
	                	if(YMUrlStatic.status == 0 && role.getPkYmRoleId() == null && !"".equals(role.getPkYmRoleId())) {
	                    	this.ymBusinessService.removeRoleInfo(loginStaff,role.getPkYmRoleId());
	                    }
						break;
					default:
						break;
				}
			break;
			default:
			break;
		}
		return ResponseFormat.retParam(1, 200, null);
	}

}
