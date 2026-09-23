package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjInnerrule;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjInnerruleMapper;
import com.huabo.audit.oracle.vo.TblNbsjInnerRuleVo;
import com.huabo.audit.service.TblNbsjInnerRuleService;
import com.huabo.audit.util.FiexibleNameAssignment;
import com.huabo.audit.util.PageInfo;
import com.huabo.audit.util.R;
import com.huabo.audit.vo.param.fieldOrgStaffId;
import com.huabo.audit.vo.param.fieldOrgStaffName;

import cn.hutool.core.util.StrUtil;
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjInnerRuleServiceImpl implements TblNbsjInnerRuleService  {
    @Autowired
    private TblNbsjInnerruleMapper tblNbsjInnerruleMapper;
    @Resource
	private TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private UserProvider userProvider;
    
	@Override
	public JsonBean selectInnerrulePageInfo(String token, Integer pageNumber, Integer pageSize,TblNbsjInnerRuleVo tblNbsjInnerRuleVo) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		tblNbsjInnerRuleVo.setCompanyid(loginStaff.getCurrentOrg().getOrgid().toString());
    	if(pageNumber == null) {
    		pageNumber = 1;
    	}
    	if(pageSize==null) {
    		pageSize=15;
    	}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	PageInfo<TblNbsjInnerrule> pageInfo = new PageInfo<TblNbsjInnerrule>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	List<TblNbsjInnerrule>  list=tblNbsjInnerruleMapper.selectInnerruleListView(pageInfo,tblNbsjInnerRuleVo,loginStaff);
    	 FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(list)){
			list.forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,entity ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
			
		}
    	pageInfo.setTlist(list);
    	pageInfo.setTotalRecord(tblNbsjInnerruleMapper.selectInnerruleCountView(pageInfo,tblNbsjInnerRuleVo,loginStaff));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
    	
	}

	@Override
	public JsonBean mergeInnerruleInfo(TblNbsjInnerrule tblNbsjInnerrule, String token, String attIds)  throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		tblNbsjInnerrule.setCompanyid(loginStaff.getCurrentOrg().getOrgid().toString());
 		tblNbsjInnerrule.setPublishdate(new Date());   //生效日期
		tblNbsjInnerrule.setOrgname(null);
		tblNbsjInnerrule.setCreatestaffid(loginStaff.getStaffid());
		try {
		if(tblNbsjInnerrule.getInnrulid()!= null) {
			//修改；
			tblNbsjInnerruleMapper.updateByPrimaryKeySelective(tblNbsjInnerrule);
			tblNbsjInnerruleMapper.deleteAttInfo(tblNbsjInnerrule.getInnrulid());
			if(StrUtil.isNotBlank(attIds)) {
				String[] split = attIds.split(",");
				for (String attid : split) {
					tblNbsjInnerruleMapper.insertAttInfo(tblNbsjInnerrule.getInnrulid(), attid);
				}
			}
		}else {
			//新增；
			tblNbsjInnerruleMapper.insertSelective(tblNbsjInnerrule);
			if(StrUtil.isNotBlank(attIds)) {
				String[] split = attIds.split(",");
				for (String attid : split) {
					tblNbsjInnerruleMapper.insertAttInfo(tblNbsjInnerrule.getInnrulid(), attid);
				}
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("tblNbsjInnerrule",tblNbsjInnerrule);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean getInnerRuleType(String token, String innruletype) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	
    	List<String> listType = this.tblNbsjInnerruleMapper.getInnerRuleType(innruletype);
    	
    	resultMap.put("listType", listType);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean selectInfo(String innerid) throws Exception {
		//查询
		TblNbsjInnerrule tblNbsjInnerrule = tblNbsjInnerruleMapper.findById(innerid);
		  FiexibleNameAssignment ment=new FiexibleNameAssignment();
      	//对灵活字段中的姓名名称及机构名称赋值
		  if(tblNbsjInnerrule!=null){
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(tblNbsjInnerrule,item); 
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,tblNbsjInnerrule ); 
		  }
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("tblNbsjInnerrule",tblNbsjInnerrule);
		List<TblAttachment> attList = tblAttachmentMapper.findtTblAttachmentByInner_SJ(new BigDecimal(innerid));
		resultMap.put("attList", attList);
		if(tblNbsjInnerrule==null) {
			return ResponseFormat.retParam(1,50001,resultMap);
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}
	public TblNbsjInnerrule getInfo(String innerid) throws Exception {
		//查询
		TblNbsjInnerrule tblNbsjInnerrule = tblNbsjInnerruleMapper.findById(innerid);
		return tblNbsjInnerrule;
	}

	@Override
	public JsonBean deleteInfo(String innerid) throws Exception {
		//查询
		TblNbsjInnerrule tblNbsjInnerrule = tblNbsjInnerruleMapper.findById(innerid);
		if(tblNbsjInnerrule==null) {
			return ResponseFormat.retParam(1,50001,null);
		}
		tblNbsjInnerruleMapper.deleteAttInfo(tblNbsjInnerrule.getInnrulid());
		tblNbsjInnerruleMapper.deleteById(innerid);
		return ResponseFormat.retParam(1,70003,null);
	}

	@Override
	public R removeAttInfoByAttId(String token, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return R.fail("用户已失效！");
		}
        return this.deleteRealtionAttInfo(attId);
	}
	private R deleteRealtionAttInfo(String attId) throws Exception {
		boolean b = false;
        TblAttachment att = this.tblAttachmentMapper.selectEntityById(new BigDecimal(attId));
        this.tblNbsjInnerruleMapper.deleteFileInfoByAttId(att.getAttid().toString());
        this.tblAttachmentMapper.deleteEntity(att.getAttid());
        
        return R.success();
	}

}
