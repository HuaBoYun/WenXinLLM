package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblAuthorizationRecord;
import com.huabo.system.entity.TblSecrectLevel;
import com.huabo.system.mapper.TblAuthorizationRecordMapper;
import com.huabo.system.mapper.TblSecrectLevelMapper;
import com.huabo.system.service.SecrectLevelService;

@Service
public class SecrectLevelServiceImpl implements SecrectLevelService {
	@Resource
    private TblSecrectLevelMapper tblSecrectLevelMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Resource
	private TblAuthorizationRecordMapper tblAuthorizationRecordMapper;

	@Override
	public JsonBean saveInfo(String token, TblSecrectLevel secrect) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		secrect.setLevelId(RandomUtil.uuBigDecimalId());
		secrect.setCreateStaffId(loginStaff.getStaffid());
		secrect.setCreateStaffName(loginStaff.getRealname());
		secrect.setCreateTime(new Date());
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			String secrectType = null;
			switch (secrect.getLevelType()) {
			case 1:
				secrectType = "功能模块";
				break;
			case 2:
				secrectType = "业务单据";
				break;
			case 3:
				secrectType = "人员";
				break;
			default:
				secrectType = "附件";
				break;
			}
			//新增确认记录需要发起流程
			TblAuthorizationRecord confirm = new TblAuthorizationRecord();
			confirm.setRecordId(RandomUtil.uuStringId());
			confirm.setCreationTime(new Date());
			confirm.setCreator(loginStaff.getStaffid());
			confirm.setCreatorName(loginStaff.getRealname());
			confirm.setOperationData(JSONObject.toJSONString(secrect));
			confirm.setOperationMemo("新增密级信息，密级名称："+secrect.getLevelName()+"、密级类型："+secrectType);
			confirm.setOperationType(TblAuthorizationRecord.OPERATIONINSERT);
			confirm.setStatus(0);
			confirm.setTargetId(secrect.getLevelId().toString());
			confirm.setRecordText("新增密级"+secrect.getLevelName());
			confirm.setTargetType(TblAuthorizationRecord.TARGETTYPESECRECT);
			this.tblAuthorizationRecordMapper.insert(confirm);
			return ResponseFormat.retParam(1, 200, confirm);
		}else {
			this.tblSecrectLevelMapper.insert(secrect);
			return ResponseFormat.retParam(1, 200, secrect);
		}
	}

	@Override
	public JsonBean modifyInfo(String token, TblSecrectLevel secrect) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(secrect.getLevelId() == null || StringUtils.isBlank(secrect.getLevelName())) {
			return ResponseFormat.retParam(0, 10004, null);
		}
		
		secrect.setModifyStaffId(loginStaff.getStaffid());
		secrect.setModifyStaffName(loginStaff.getRealname());
		secrect.setModifyTime(new Date());
		
		
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			String secrectType = null;
			switch (secrect.getLevelType()) {
			case 1:
				secrectType = "功能模块";
				break;
			case 2:
				secrectType = "业务单据";
				break;
			case 3:
				secrectType = "人员";
				break;
			default:
				secrectType = "附件";
				break;
			}
			
			TblSecrectLevel presecrect = this.tblSecrectLevelMapper.selectById(secrect.getLevelId());
			TblAuthorizationRecord confirm = this.tblAuthorizationRecordMapper.selectSpzRecordInfoByTargetId(secrect.getLevelId().toString());
			String memo = "";
        	if(!presecrect.getLevelName().equals(secrect.getLevelName())) {
        		memo = "修改密级名称："+presecrect.getLevelName()+"——>"+secrect.getLevelName()+"；";
        	}
        	if(confirm != null) {
        		//修改审批中的确认数据
        		confirm.setModifiedTime(new Date());
        		confirm.setModifier(loginStaff.getStaffid());
        		confirm.setModifyerName(loginStaff.getRealname());
        		confirm.setOperationData(JSONObject.toJSONString(secrect));
        		confirm.setOperationMemo("修改密级信息，"+memo+"密级类型："+secrectType+"；");
        		confirm.setRecordText("修改密级"+secrect.getLevelName());
        		this.tblAuthorizationRecordMapper.updateById(confirm);
        	}else {
        		//新增确认记录需要发起流程
        		confirm = new TblAuthorizationRecord();
        		confirm.setRecordId(RandomUtil.uuStringId());
        		confirm.setCreationTime(new Date());
        		confirm.setCreator(loginStaff.getStaffid());
        		confirm.setCreatorName(loginStaff.getRealname());
           	 	confirm.setOperationData(JSONObject.toJSONString(secrect));
           	 	confirm.setOperationMemo("修该密级信息，"+memo+"密级类型："+secrectType+"；");
           	 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONMODIFY);
           	 	confirm.setStatus(0);
           	 	confirm.setTargetId(secrect.getLevelId().toString());
           	 	confirm.setRecordText("修改密级"+secrect.getLevelName());
           	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPESECRECT);
           	 	this.tblAuthorizationRecordMapper.insert(confirm);
        	}
        	return ResponseFormat.retParam(1, 200, confirm);
		}else {
			this.tblSecrectLevelMapper.updateById(secrect);
			return ResponseFormat.retParam(1, 200, secrect);
		}
		
	}

	@Override
	public JsonBean removeInfo(String token, BigDecimal levelId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			TblSecrectLevel secrect = this.tblSecrectLevelMapper.selectById(levelId);
			TblAuthorizationRecord confirm = new TblAuthorizationRecord();
    		confirm.setRecordId(RandomUtil.uuStringId());
    		confirm.setCreationTime(new Date());
    		confirm.setCreator(loginStaff.getStaffid());
    		confirm.setCreatorName(loginStaff.getRealname());
       	 	confirm.setOperationData(JSONObject.toJSONString(secrect));
       	 	confirm.setOperationMemo("删除密级信息，"+secrect.getLevelName());
       	 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONREMOVE);
       	 	confirm.setStatus(0);
       	 	confirm.setTargetId(secrect.getLevelId().toString());
       	 	confirm.setRecordText("删除密级"+secrect.getLevelName());
       	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPESECRECT);
       	 	this.tblAuthorizationRecordMapper.insert(confirm);
       	 	return ResponseFormat.retParam(1, 200, confirm);
		}else {
			this.tblSecrectLevelMapper.deleteById(levelId);
			return ResponseFormat.retParam(1, 200, null);
		}
		
	}

	@Override
	public JsonBean getDetail(String token, BigDecimal levelId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		TblSecrectLevel secrect = this.tblSecrectLevelMapper.selectById(levelId);
		
		return ResponseFormat.retParam(1, 200, secrect);
	}

	@Override
	public JsonBean getPageInfo(String token, TblSecrectLevel secrect, Integer pageNumber, Integer pageSize)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<TblSecrectLevel> wrapper = new QueryWrapper<TblSecrectLevel>();
		
		wrapper.like(StringUtils.isNotBlank(secrect.getLevelName()), "LEVELNAME", secrect.getLevelName());
		wrapper.eq(secrect.getLevelType()!=null, "LEVELTYPE", secrect.getLevelType());
		wrapper.like(StringUtils.isNotBlank(secrect.getCreateStaffName()), "CREATESTAFFNAME", secrect.getCreateStaffName());
		
		wrapper.orderByAsc("LEVELTYPE,LEVELID");
		Page<TblSecrectLevel> page = new Page<TblSecrectLevel>(pageNumber,pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblSecrectLevel> pages = this.tblSecrectLevelMapper.selectPage(page, wrapper);
		
		return ResponseFormat.retParam(1, 200, pages);
	}

	@Override
	public JsonBean getScopeSecrectListByType(String token, Integer levelType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		
		//校验当前密级类型获取其所下属的密级类型
		Integer cutype = 0;
		boolean employeeflag = false;
		switch (levelType) {
		case 1:
			cutype = 2;
			break;
		case 2:
			employeeflag = true;
			cutype = 4;
			break;
		default:
		case 3:
			cutype = 2;
			break;
		}
		
		QueryWrapper<TblSecrectLevel> wrapper = new QueryWrapper<TblSecrectLevel>();
		wrapper.eq("LEVELTYPE", cutype);
		List<TblSecrectLevel> menuList = this.tblSecrectLevelMapper.selectList(wrapper);
		resultMap.put("menuList", menuList);
		if(employeeflag) {
			wrapper.clear();
			wrapper.eq("LEVELTYPE", 3);
			List<TblSecrectLevel> employeeList = this.tblSecrectLevelMapper.selectList(wrapper);
			resultMap.put("employeeList", employeeList);
		}
		
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean getSecrectListByType(String token, Integer levelType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<TblSecrectLevel> wrapper = new QueryWrapper<TblSecrectLevel>();
		wrapper.eq("LEVELTYPE", levelType);
		List<TblSecrectLevel> menuList = this.tblSecrectLevelMapper.selectList(wrapper);
		
		return ResponseFormat.retParam(1, 200, menuList);
	}

	@Override
	public JsonBean getSecrectListForRight(String token, BigDecimal rightId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<TblSecrectLevel> wrapper = new QueryWrapper<TblSecrectLevel>();
		
		/*wrapper.inSql("LEVELID", "SELECT SECRECTLEVELID FROM TBL_SYSTEM_RIGHT WHERE ID = "+rightId);
		
		TblSecrectLevel menu = this.tblSecrectLevelMapper.selectOne(wrapper);
		
		if(menu == null) {
			return ResponseFormat.retParam(0, "该模块未配置密级", null);
		}*/
		if(StringUtils.isBlank(loginStaff.getSecrectScopeIds())) {
			return ResponseFormat.retParam(0, "该用户未配置密级", null);
		}
		wrapper.clear();
		wrapper.inSql("LEVELID", loginStaff.getSecrectScopeIds());
		wrapper.orderByAsc("LEVEL");
		List<TblSecrectLevel> menuList = this.tblSecrectLevelMapper.selectList(wrapper);
		
		return ResponseFormat.retParam(1, 200, menuList);
	}

	@Override
	public JsonBean getSecrectListByManage(String token, BigDecimal levelId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblSecrectLevel menu = this.tblSecrectLevelMapper.selectById(levelId);
		
		if(menu == null) {
			return ResponseFormat.retParam(0, "该模块未配置密级", null);
		}
		
		QueryWrapper<TblSecrectLevel> wrapper = new QueryWrapper<TblSecrectLevel>();
		List<TblSecrectLevel> menuList = null;
		if(StringUtils.isNotBlank(menu.getSecrectMenuScope())) {
			wrapper.inSql("LEVELID", menu.getSecrectMenuScope());
			menuList = this.tblSecrectLevelMapper.selectList(wrapper);
		}
		
		
		Map<String, Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("menuList", menuList);
		resultMap.put("menu", menu);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean getSecrectListByLoginUser(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		
		QueryWrapper<TblSecrectLevel> wrapper = new QueryWrapper<TblSecrectLevel>();
		
		/*wrapper.inSql("LEVELID", "SELECT SECRECTLEVELID FROM TBL_SYSTEM_RIGHT WHERE ID = "+rightId);
		
		TblSecrectLevel menu = this.tblSecrectLevelMapper.selectOne(wrapper);
		
		if(menu == null) {
			return ResponseFormat.retParam(0, "该模块未配置密级", null);
		}*/
		if(StringUtils.isBlank(loginStaff.getSecrectScopeIds())) {
			return ResponseFormat.retParam(0, "该用户未配置密级", null);
		}
		wrapper.clear();
		wrapper.inSql("LEVELID", loginStaff.getSecrectScopeIds());
		List<TblSecrectLevel> menuList = this.tblSecrectLevelMapper.selectList(wrapper);
		
		return ResponseFormat.retParam(1, 200, menuList);
	}

}
