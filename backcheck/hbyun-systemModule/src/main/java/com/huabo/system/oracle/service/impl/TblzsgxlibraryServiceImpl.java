package com.huabo.system.oracle.service.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.Tblzsgxlibrary;
import com.huabo.system.mapper.TblAttachmentMapper;
import com.huabo.system.mapper.TblzsgxlibraryMapper;
import com.huabo.system.oracle.service.TblzsgxlibraryService;

@Service
public class TblzsgxlibraryServiceImpl implements TblzsgxlibraryService {
	
	@Autowired
	private TblzsgxlibraryMapper tblzsgxlibraryMapper;
	
	@Autowired
	TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean getList(String token, String type,Tblzsgxlibrary library, Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageInfo<Tblzsgxlibrary> pageInfo = new PageInfo<Tblzsgxlibrary>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(pageNumber);
    	
    	IPage<Tblzsgxlibrary> page = new Page<Tblzsgxlibrary>(pageNumber,pageSize);
    	IPage<Tblzsgxlibrary> pageList = tblzsgxlibraryMapper.selectType(page, library, type, loginStaff.getCurrentOrg().getOrgid());
    	
    	pageInfo.setTlist(pageList.getRecords());
    	pageInfo.setTotalRecord((int)pageList.getTotal());
    	resultMap.put("pageInfo", pageInfo);
    	return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean saveOrUpdate(String token, Tblzsgxlibrary libary,String attids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(libary!=null && libary.getLibraryid()!=null) {
			tblzsgxlibraryMapper.updateById(libary);
		}else {
			libary.setCretetime(new Date());
			libary.setCreatestaffid(loginStaff.getStaffid());
			libary.setCreateorganid(loginStaff.getCurrentOrg().getOrgid());
			libary.setLibraryid(RandomUtil.uuBigDecimalId());
			tblzsgxlibraryMapper.insert(libary);
			
		}
		if(attids != null && !"".equals(attids)) {
			String[] attId = attids.split(",");
			for (String aid : attId) {
				tblzsgxlibraryMapper.insertAttInfoAtt(libary.getLibraryid(), aid);
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",libary);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findById(String token, BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Tblzsgxlibrary library = tblzsgxlibraryMapper.selectId(id);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",library);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delete(String token, BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		tblzsgxlibraryMapper.deleteattId(id);
		tblzsgxlibraryMapper.deleteId(id);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override 
	public JsonBean findByattId(String token, BigDecimal id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		List<TblAttachment> list = tblAttachmentMapper.selectTrainAttListBylibraryid(id);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data",list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteattid(String token, BigDecimal attid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		tblAttachmentMapper.deleteEntity(attid);
		return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean getBytypelist(String token, String type,String lrtype) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(type!=null && type.equals("1")) {
			List<Tblzsgxlibrary> list = tblzsgxlibraryMapper.selectbywj(loginStaff.getCurrentOrg().getOrgid(),lrtype);
			resultMap.put("data",list);
		}
		if(type!=null && type.equals("2")) {
			List<Tblzsgxlibrary> list =tblzsgxlibraryMapper.selectbyzt(loginStaff.getCurrentOrg().getOrgid(),lrtype);
			resultMap.put("data",list);
		}
		
		if(type!=null && type.equals("3")) {
			List<Tblzsgxlibrary> list =tblzsgxlibraryMapper.selectbyjg(loginStaff.getCurrentOrg().getOrgid(),lrtype);
			resultMap.put("data",list);
		}
		
		if(type!=null && type.equals("4")) {
			List<Tblzsgxlibrary> list =tblzsgxlibraryMapper.selectbysxx(loginStaff.getCurrentOrg().getOrgid(),lrtype);
			resultMap.put("data",list);
		}
		
		if(type!=null && type.equals("5")) {
			List<Tblzsgxlibrary> list =tblzsgxlibraryMapper.selectbyfglb(loginStaff.getCurrentOrg().getOrgid(),lrtype);
			resultMap.put("data",list);
		}
		if(type!=null && type.equals("6")) {
			List<Tblzsgxlibrary> list =tblzsgxlibraryMapper.selectbygbnf(loginStaff.getCurrentOrg().getOrgid(),lrtype);
			resultMap.put("data",list);
		}
		
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean addcxcount(String token, BigDecimal libraryid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		Tblzsgxlibrary library = tblzsgxlibraryMapper.selectId(libraryid);
		if(library!=null) {
			Integer coujnt=0;
			if(library.getCkcount()==null) {
				coujnt=1;
			}else {
				coujnt=library.getCkcount()+1;
			}
			library.setCkcount(coujnt);
			tblzsgxlibraryMapper.updateById(library);
		}
		resultMap.put("data",library);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean addxzcount(String token, BigDecimal libraryid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		Tblzsgxlibrary library = tblzsgxlibraryMapper.selectId(libraryid);
		if(library!=null) {
			Integer coujnt=0;
			if(library.getXzcount()==null) {
				coujnt=1;
			}else {
				coujnt=library.getXzcount()+1;
			}
			
			library.setXzcount(coujnt);
			tblzsgxlibraryMapper.updateById(library);
		}
		resultMap.put("data",library);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	 
	 

	 

}
