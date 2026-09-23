package com.huabo.audit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjType;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTypeMapper;
import com.huabo.audit.service.TblNbsjTypeService;
@Service
public class TblNbsjTypeServiceImpl implements TblNbsjTypeService {

	@Resource
	private TblNbsjTypeMapper tblNbsjTypeMapper;
	
	@Resource
	private TblNbsjProjectMapper  tblNbsjProjectMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean getNbsjTypeListForMerge(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		List<TblNbsjType> typeList = this.tblNbsjTypeMapper.selectNbsjTypeListForMerge(loginStaff.getCurrentOrg().getOrgid());
		resultMap.put("typeList", typeList);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean saveNbsjType(TblNbsjType type, String token) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		try {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
      			return ResponseFormat.retParam(0,20006,null);
		}
		if (type != null && type.getTypeId() == null) {
			List<TblNbsjType> findByOrgidAndType = tblNbsjTypeMapper.findByOrgidAndType(loginStaff.getCurrentOrg().getOrgid(), type.getAuditType());
			if(findByOrgidAndType!=null && findByOrgidAndType.size()>0) {
	               resultMap.put("msg", "数据已存在！");
	               return ResponseFormat.retParam(1,200,resultMap);
			}
			// 新建
			type.setVersion("1");
			if(type.getStatus()==null) {
				type.setStatus(2);
			}
			type.setOrgid(loginStaff.getCurrentOrg().getOrgid().toString());
			type.setTypeId(RandomUtil.uuBigDecimalId());
//			tblNbsjTypeMapper.insert(type);
			tblNbsjTypeMapper.insertTblNbsjType(type.getAuditType(), type.getVersion(), type.getOrgid(), type.getStatus(),type.getAuditCode());
		} else {
			List<TblNbsjType> findByOrgidAndType = tblNbsjTypeMapper.findByOrgidAndId(loginStaff.getCurrentOrg().getOrgid(),type.getAuditType(),type.getTypeId());
			if(findByOrgidAndType!=null && findByOrgidAndType.size()>0) {
	               return ResponseFormat.retParam(1,"审计类型不能重复",null);
			}
			Integer varsion = Integer.parseInt(type.getVersion())+1;
			type.setVersion(varsion.toString());
			type.setOrgid(loginStaff.getCurrentOrg().getOrgid().toString());
			tblNbsjTypeMapper.updateTblNbsjType(type.getTypeId(),type.getAuditType(), type.getVersion(), type.getOrgid(), type.getStatus(),type.getAuditCode());
		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public  JsonBean  SelectNbsjType(String type, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjType nbsjtype=tblNbsjTypeMapper.selectNbsjType(type);
         resultMap.put("date", nbsjtype);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delNbsjType(String typeid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
   			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjType nbsjtype=tblNbsjTypeMapper.selectNbsjType(typeid);
		List<TblNbsjProject> sjlxList = tblNbsjProjectMapper.findByNbsjLx(nbsjtype.getAuditType(),loginStaff.getCurrentOrg().getOrgid().toString());
        if(sjlxList.size()>0){
     		return ResponseFormat.retParam(0,20007,null);
        }else{
        	  tblNbsjTypeMapper.delTblNbsjType(typeid);
        }
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean getNbsjTypeListPage(String token, Integer pageNumber, Integer pageSize,String auditType) throws Exception {
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
    	PageInfo<TblNbsjType> pageInfo = new PageInfo<TblNbsjType>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(this.tblNbsjTypeMapper.selectNbsjTempleteListByPageInfo(pageInfo,loginStaff.getCurrentOrg().getOrgid(),auditType));
    	pageInfo.setTotalRecord(this.tblNbsjTypeMapper.selectNbsjTempleteListCountByPageInfo(pageInfo,loginStaff.getCurrentOrg().getOrgid(),auditType));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getNbsjTypeAllList(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	List<TblNbsjType> nbsjTypeList = this.tblNbsjTypeMapper.selectNbsjTypeAllList(loginStaff.getCurrentOrg().getOrgid());
    	return ResponseFormat.retParam(1,200,nbsjTypeList);
	}

}
