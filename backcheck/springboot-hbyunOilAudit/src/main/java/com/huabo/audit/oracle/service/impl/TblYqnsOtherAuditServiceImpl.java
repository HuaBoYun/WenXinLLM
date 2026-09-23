package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsOtherAudit;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOtherAuditMapper;
import com.huabo.audit.oracle.service.TblYqnsOtherAuditService;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsProjectAuditTemplateServiceImpl
 * @PACKAGE_NAME: com.huabo.audit.oracle.service.impl
 * @date 2023/10/10 11:21.
 * @version: V1.0
 * @description: 央企内审-基础配置-工程审计模板 serviceImpl
 */
@Service
public class TblYqnsOtherAuditServiceImpl extends ServiceImpl<TblYqnsOtherAuditMapper, TblYqnsOtherAudit>
        implements TblYqnsOtherAuditService {
    @Resource
    private TblYqnsOtherAuditMapper tblYqnsOtherAuditMapper;
    
    @Resource
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private UserProvider userProvider;

    
    @Override
	public JsonBean getListForChoose(String token, BigDecimal draftPlanId, Integer chooseType, String auditIdStrs) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
    	
    	List<TblYqnsOtherAudit> oauList = this.tblYqnsOtherAuditMapper.selectListByDraftPlanIdForChoose(draftPlanId,chooseType,auditIdStrs);
        List<String> orgNameList = null;
        for (TblYqnsOtherAudit oau : oauList) {
        	orgNameList = this.tblYqnsOtherAuditMapper.selectAuditOrgNameList(oau.getAuditOrgidStrs());
        	oau.setAuditOrgNameStrs(String.join(",", orgNameList));
		}
        return ResponseFormat.retParam(1, 200, oauList);
	}
    
	@Override
	public JsonBean mengerEntity(String token, TblYqnsOtherAudit vo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
		if(vo.getAuditId() == null) {
			vo.setAuditId(RandomUtil.uuBigDecimalId());
			vo.setCreateStaffId(loginStaff.getStaffid());
			vo.setCreateTime(new Date());
			vo.setLinkDept(loginStaff.getLinkDetp().getOrgid());
			vo.setStatus(0);
			this.tblYqnsOtherAuditMapper.insert(vo);
		}else {
			vo.setModifyDate(new Date());
			vo.setModifyStaffid(loginStaff.getStaffid());
			this.tblYqnsOtherAuditMapper.updateById(vo);
		}
		
		if(vo.getAttIds() != null) {
			for (String attid : vo.getAttIds()) {
				this.tblYqnsOtherAuditMapper.insertAttRelation(vo.getAuditId(),attid);
			}
		}
		return ResponseFormat.retParam(1, 200, vo);
	}

	@Override
	public JsonBean getDetailById(String token, BigDecimal auditId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        TblYqnsOtherAudit vo = this.tblYqnsOtherAuditMapper.selectEntityById(auditId);
        
        if(vo == null) {
        	return ResponseFormat.retParam(0, 50001, null);
        }
        
        if(StringUtils.isNotBlank(vo.getAuditOrgidStrs())) {
        	List<String> orgNameList = this.tblYqnsOtherAuditMapper.selectAuditOrgNameList(vo.getAuditOrgidStrs());
        	vo.setAuditOrgNameStrs(String.join(",", orgNameList));
        }
        
        List<TblAttachment> attList = this.tblAttachmentMapper.selectOtherAuditAttInfoList(vo.getAuditId());
        vo.setAttachments(attList);
        
        return ResponseFormat.retParam(1, 200, vo);
	}

	@Override
	public JsonBean removeAttInfo(String token, BigDecimal attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        
        this.tblYqnsOtherAuditMapper.deletAttRealtionByAttId(attId);
        
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean removById(String token, BigDecimal auditId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblYqnsOtherAuditMapper.deletAttRealtionByAuditId(auditId);
		this.tblYqnsOtherAuditMapper.deleteById(auditId);
        return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean toPreAdd(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
        List<String> implTypeList = new ArrayList<String>(0);
        if(loginStaff.getCurrentOrg().getFatherorgid().compareTo(BigDecimal.valueOf(-1)) == 0) {
        	//总部类型
        	implTypeList.add("自审");
        	implTypeList.add("外包");
        	implTypeList.add("授权自审");
        	implTypeList.add("委托审计");
        	implTypeList.add("交叉审计");
        	implTypeList.add("重大专项审计");
        }else {
        	//下属单位类型
        	implTypeList.add("自审");
        	implTypeList.add("外包");
        	implTypeList.add("单位委托审计");
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("implTypeList", implTypeList);
        resultMap.put("projectInitUnitName", loginStaff.getCurrentOrg().getOrgname());
        resultMap.put("projectInitUnit", loginStaff.getCurrentOrg().getOrgid());
        resultMap.put("createStaffId", loginStaff.getStaffid());
        resultMap.put("createStaffName", loginStaff.getRealname());
        resultMap.put("createTime", DateUtil.parseDate(new Date(), DateUtil.DATE_SMALL_STR));
        return ResponseFormat.retParam(1, 200, resultMap);
	}


}
