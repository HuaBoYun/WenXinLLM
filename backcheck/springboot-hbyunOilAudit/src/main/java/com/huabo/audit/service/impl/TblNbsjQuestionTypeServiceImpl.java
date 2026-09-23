package com.huabo.audit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.page.PageMethod;
import org.springframework.stereotype.Service;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjQuestionType;
import com.huabo.audit.oracle.entity.TblNbsjStatType;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjQuestionTypeMapper;
import com.huabo.audit.service.TblNbsjQuestionTypeService;

@Service
public class TblNbsjQuestionTypeServiceImpl implements TblNbsjQuestionTypeService{
	
	@Resource
	private TblNbsjQuestionTypeMapper tblNbsjQuestionTypeMapper;
	
	@Resource
	private TblNbsjProjectMapper tblNbsjProjectMapper;
	
	@Resource
    private UserProvider userProvider;
	

	@Override
	public JsonBean getNbsjQuestionTypeListPage(String token, Integer pageNumber, Integer pageSize) throws Exception {
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
    	PageInfo<TblNbsjQuestionType> pageInfo = new PageInfo<TblNbsjQuestionType>();

		com.github.pagehelper.PageInfo<TblNbsjQuestionType> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblNbsjQuestionTypeMapper.selectNbsjQuestionTypeListByPageInfo(pageInfo,loginStaff.getCurrentOrg().getOrgid());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord((( int)pageInfo2.getTotal()));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	@Override
	public JsonBean getNbsjQuestionTypeList(String token) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
    	Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	List<TblNbsjQuestionType> list=this.tblNbsjQuestionTypeMapper.selectNbsjQuestionTypeListCount(loginStaff.getCurrentOrg().getOrgid());
    	resultMap.put("data", list);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public  JsonBean  getNbsjQuestionType(String typeid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjQuestionType nbsjtype=tblNbsjQuestionTypeMapper.selectNbsjType(typeid);
         resultMap.put("date", nbsjtype);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean saveNbsjQuestionType(TblNbsjQuestionType type, String token) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
	      			return ResponseFormat.retParam(0,20006,null);
			}
			if (type != null && type.getTypeId() == null) {
				List<TblNbsjQuestionType> findByOrgidAndType = tblNbsjQuestionTypeMapper.findByOrgidAndType(loginStaff.getCurrentOrg().getOrgid().intValue(), type.getAuditType());
				if(findByOrgidAndType!=null && findByOrgidAndType.size()>0) {
		               resultMap.put("msg", "数据已存在！");
		               return ResponseFormat.retParam(1,200,resultMap);
				}
				// 新建
				type.setVersion(1);
				if(type.getStatus()==null) {
					type.setStatus(2);
				}
				type.setOrgid(loginStaff.getCurrentOrg().getOrgid().intValue());
				tblNbsjQuestionTypeMapper.insertTblNbsjType(type.getAuditType(), type.getVersion(), type.getOrgid(), type.getStatus());
			} else {
				List<TblNbsjQuestionType> findByOrgidAndType = tblNbsjQuestionTypeMapper.findByOrgidAndId(loginStaff.getCurrentOrg().getOrgid().intValue(),type.getAuditType(),type.getTypeId());
				if(findByOrgidAndType!=null && findByOrgidAndType.size()>0) {
		               return ResponseFormat.retParam(1,"审计问题类型不能重复",null);
				}
				Integer varsion = type.getVersion()+1;
				type.setVersion(varsion);
				type.setOrgid(loginStaff.getCurrentOrg().getOrgid().intValue());
				tblNbsjQuestionTypeMapper.updateTblNbsjType(type.getTypeId(),type.getAuditType(), type.getVersion(), type.getOrgid(), type.getStatus());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,null);
	}
	
	@Override
	public JsonBean delNbsjType(String typeid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
   			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjQuestionType nbsjtype=tblNbsjQuestionTypeMapper.selectNbsjType(typeid+"");
		List<TblNbsjProject> sjlxList = tblNbsjProjectMapper.findByNbsjLx(nbsjtype.getAuditType(),loginStaff.getCurrentOrg().getOrgid().toString());
        if(sjlxList.size()>0){
     		return ResponseFormat.retParam(0,20007,null);
        }else{
        	tblNbsjQuestionTypeMapper.delTblNbsjType(typeid);
        }
		return ResponseFormat.retParam(1,200,null);
	}
	
	
	//==统计类型维护
	@Override
	public JsonBean getNbsjStatTypeListPage(String token, Integer pageNumber, Integer pageSize) throws Exception {
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
    	PageInfo<TblNbsjStatType> pageInfo = new PageInfo<TblNbsjStatType>();

		com.github.pagehelper.PageInfo<TblNbsjStatType> pageInfo2 = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						this.tblNbsjQuestionTypeMapper.selectNbsjStatTypeListByPageInfo(pageInfo,loginStaff.getCurrentOrg().getOrgid());
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	pageInfo.setTlist(pageInfo2.getList());
    	pageInfo.setTotalRecord(((int) pageInfo2.getTotal()));
    	pageInfo.getTotalPage();
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public  JsonBean  getNbsjStatType(String typeid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblNbsjStatType nbsjtype=tblNbsjQuestionTypeMapper.selectNbsjStatType(typeid);
         resultMap.put("date", nbsjtype);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean saveNbsjStatType(TblNbsjStatType type, String token) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if(loginStaff == null) {
	      			return ResponseFormat.retParam(0,20006,null);
			}
			if (type != null && type.getTypeId() == null) {
				List<TblNbsjStatType> findByOrgidAndType = tblNbsjQuestionTypeMapper.findStatByOrgidAndType(loginStaff.getCurrentOrg().getOrgid().intValue(), type.getAuditType());
				if(findByOrgidAndType!=null && findByOrgidAndType.size()>0) {
		               resultMap.put("msg", "数据已存在！");
		               return ResponseFormat.retParam(1,200,resultMap);
				}
				// 新建
				type.setVersion(1);
				if(type.getStatus()==null) {
					type.setStatus(2);
				}
				type.setOrgid(loginStaff.getCurrentOrg().getOrgid().intValue());
				tblNbsjQuestionTypeMapper.insertTblNbsjStatType(type.getAuditType(), type.getVersion(), type.getOrgid(), type.getStatus());
			} else {
				List<TblNbsjStatType> findByOrgidAndType = tblNbsjQuestionTypeMapper.findStatByOrgidAndId(loginStaff.getCurrentOrg().getOrgid().intValue(),type.getAuditType(),type.getTypeId());
				if(findByOrgidAndType!=null && findByOrgidAndType.size()>0) {
		               return ResponseFormat.retParam(1,"统计类型不能重复",null);
				}
				Integer varsion = type.getVersion()+1;
				type.setVersion(varsion);
				type.setOrgid(loginStaff.getCurrentOrg().getOrgid().intValue());
				tblNbsjQuestionTypeMapper.updateTblNbsjStatType(type.getTypeId(),type.getAuditType(), type.getVersion(), type.getOrgid(), type.getStatus());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1,200,null);
	}
	
	@Override
	public JsonBean delNbsjStatType(String typeid, String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
   			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjStatType nbsjtype=tblNbsjQuestionTypeMapper.selectNbsjStatType(typeid+"");
		List<TblNbsjProject> sjlxList = tblNbsjProjectMapper.findByNbsjLx(nbsjtype.getAuditType(),loginStaff.getCurrentOrg().getOrgid().toString());
        if(sjlxList.size()>0){
     		return ResponseFormat.retParam(0,20007,null);
        }else{
        	tblNbsjQuestionTypeMapper.delTblNbsjStatType(typeid);
        }
		return ResponseFormat.retParam(1,200,null);
	}

}
