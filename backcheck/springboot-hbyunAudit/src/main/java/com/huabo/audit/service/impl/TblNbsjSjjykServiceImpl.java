package com.huabo.audit.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjSjjyk;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjSjjykMapper;
import com.huabo.audit.service.TblNbsjSjjykService;


/**
 *  审计模板
 * @author T
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjSjjykServiceImpl implements TblNbsjSjjykService {
	
	@Resource
	private TblNbsjSjjykMapper tblNbsjSjjykMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrUpdate(TblNbsjSjjyk jyk, String token, BigDecimal jykid, String attids) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		jyk.setUpdatedtime(new Date());
		jyk.setStaffid(user.getStaffid());
		jyk.setOrgid(user.getCurrentOrg().getOrgid());
		if(jykid!=null) {
			//tblNbsjSjjykMapper.updateEntity(jyk);
			TblNbsjSjjyk sjjyk = tblNbsjSjjykMapper.selectNbsjJykByID(jykid);
			
			jyk.setCreatedtime(sjjyk.getCreatedtime());
			tblNbsjSjjykMapper.updateByPrimaryKeySelective(jyk);
		}else {
			jyk.setCreatedtime(new Date());
			//tblNbsjSjjykMapper.insertEntity(jyk);
			jyk.setJykid(RandomUtil.uuBigDecimalId());
			tblNbsjSjjykMapper.insertSelective(jyk);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(attids != null && !"".equals(attids)) {
			String[] attId = attids.split(",");
			for (String aid : attId) {
				tblNbsjSjjykMapper.insertAttInfoAtt(jyk.getJykid(), aid);
			}
		}
		resultMap.put("jyk", jyk);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delete(BigDecimal jykid, String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(jykid==null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		Integer integer = tblNbsjSjjykMapper.selectNbsjjykByyy(user.getCurrentOrg().getOrgid(), jykid);
		if(integer<=0) {
			tblNbsjSjjykMapper.deleteAttInfoAttByjykid(jykid);
			tblNbsjSjjykMapper.deleteJykById(jykid);
			return ResponseFormat.retParam(1,200,null);
		}else {
			return ResponseFormat.retParam(0,"模板库已被引用，不能删除",null);
		}
		
	}

	@Override
	public JsonBean findByjykid(BigDecimal jykid, String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("jyk", tblNbsjSjjykMapper.selectNbsjJykByID(jykid));
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAll(String code, String tatle,Integer startIndex,Integer pageSize, String token,String experiencetype,String overview) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjSjjyk jyk=new TblNbsjSjjyk();
		jyk.setCode(code);
		jyk.setTatle(tatle);
		jyk.setExperiencetype(experiencetype);
		jyk.setOverview(overview);
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	PageInfo<TblNbsjSjjyk> pageInfo = new PageInfo<TblNbsjSjjyk>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(startIndex);
    	
    	pageInfo.setTlist(tblNbsjSjjykMapper.selectNbsjjykByPageInfo(pageInfo, user, jyk));
    	pageInfo.setTotalRecord(tblNbsjSjjykMapper.selectNbsjjykByPageInfoCount(user, jyk));
    	resultMap.put("pageInfo", pageInfo);
    	return  ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getAttListByjykid(String token, BigDecimal jykid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(jykid==null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("attList", tblAttachmentMapper.selectAttListByJykId(jykid));
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delAttListByattId(String token, BigDecimal attid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(attid==null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		tblNbsjSjjykMapper.deleteAttInfoAttid(attid);
		tblAttachmentMapper.deleteEntity(attid);
		return  ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findAllByDatapreID(String token, BigDecimal dataperid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("jyklist", tblNbsjSjjykMapper.selectNbsjjykByDateperidPageInfo(dataperid));
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delAttListByjykId(String token, BigDecimal jykid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(jykid==null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		tblNbsjSjjykMapper.deletetjykdid(jykid);
		return  ResponseFormat.retParam(1,200,null);
	}

	
}