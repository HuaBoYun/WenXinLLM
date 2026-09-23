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

import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjMb;
import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblNbsjMbMapper;
import com.huabo.audit.service.TblNbsjMbService;


/**
 *  审计模板
 * @author T
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TblNbsjMbServiceImpl implements TblNbsjMbService {
	
	@Resource
	private TblNbsjMbMapper tblNbsjMbMapper;
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrUpdate(TblNbsjMb mb, String token, BigDecimal mbid, String attids) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		mb.setUpdatedtime(new Date());
		mb.setStaffid(user.getStaffid());
		mb.setOrgid(user.getCurrentOrg().getOrgid());

		if(mbid!=null) {
			tblNbsjMbMapper.updateById(mb);
		}else {
			mb.setCreatedtime(new Date());
			mb.setMbid(RandomUtil.uuBigDecimalId());
			tblNbsjMbMapper.insert(mb);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(attids != null && !"".equals(attids)) {
			String[] attId = attids.split(",");
			for (String aid : attId) {
				tblNbsjMbMapper.insertAttInfoAtt(mb.getMbid(), aid);
			}
		}
		resultMap.put("mb", mb);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delete(BigDecimal mbid, String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(mbid==null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		Integer integer = tblNbsjMbMapper.selectNbsjMbByyy(user.getCurrentOrg().getOrgid(), mbid);
		if(integer<=0) {
			tblNbsjMbMapper.deleteAttInfoAttBymbid(mbid);
			tblNbsjMbMapper.deleteMbById(mbid);
			return ResponseFormat.retParam(1,200,null);
		}else {
			return ResponseFormat.retParam(0,"模板库已被引用，不能删除",null);
		}
		
	}

	@Override
	public JsonBean findByMbid(BigDecimal mbid, String token) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("mb", tblNbsjMbMapper.selectNbsjMbByID(mbid));
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAll(String code, String name, Integer startIndex, Integer pageSize, String token,String auditype) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		TblNbsjMb mb=new TblNbsjMb();
		mb.setAudittype(auditype);
		mb.setMbcode(code);
		mb.setMbname(name);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
    	PageInfo<TblNbsjMb> pageInfo = new PageInfo<TblNbsjMb>();
    	pageInfo.setPageSize(pageSize);
    	pageInfo.setCurrentPage(startIndex);
    	
    	com.github.pagehelper.PageInfo<TblNbsjMb> pageHelper = PageMethod.startPage(startIndex, pageSize)
				.doSelectPageInfo(() -> tblNbsjMbMapper.selectNbsjMbByPageInfo(pageInfo, user, mb));
    	
    	pageInfo.setTlist(pageHelper.getList());
    	pageInfo.setTotalRecord((int)pageHelper.getTotal());
    	resultMap.put("pageInfo", pageInfo);
    	return  ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getAttListByMbId(String token, BigDecimal mbid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(mbid==null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("attList", tblAttachmentMapper.selectAttListByMbId(mbid));
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
		tblNbsjMbMapper.deleteAttInfoAttid(attid);
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
		resultMap.put("mblist", tblNbsjMbMapper.selectNbsjMbByDateperidPageInfo(dataperid));
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean delAttListBymbId(String token, BigDecimal mbid) throws Exception {
		TblStaffUtil user = userProvider.get();
		if(user == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		if(mbid==null) {
			return ResponseFormat.retParam(0,50001,null);
		}
		tblNbsjMbMapper.delAttListBymbId(mbid);
		return  ResponseFormat.retParam(1,200,null);
	}

	
}
