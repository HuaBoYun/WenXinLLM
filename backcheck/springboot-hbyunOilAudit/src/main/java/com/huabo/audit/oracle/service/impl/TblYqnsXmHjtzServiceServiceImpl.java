package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsXmHjtz;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsXmHjtzMapper;
import com.huabo.audit.oracle.service.TblYqnsXmHjtzService;
import com.huabo.audit.oracle.vo.XmdqVo;
import com.huabo.audit.util.PageResult;

@Service
public class TblYqnsXmHjtzServiceServiceImpl implements TblYqnsXmHjtzService {
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblYqnsXmHjtzMapper tblYqnsXmHjtzMapper;
	
	@Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsXmHjtz ry, String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        ry.setCreatedate(new Date());
        ry.setCreatestaffid(staff.getStaffid());
        ry.setStatus(0);
        if(ry!=null && ry.getRyid()!=null) {
        	tblYqnsXmHjtzMapper.updateById(ry);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String aid : stins) {
        			tblYqnsXmHjtzMapper.insertAttInfoAtt(ry.getRyid(), aid);;
				}
        	}
        }else {
        	ry.setRyid(RandomUtil.uuBigDecimalId());
        	tblYqnsXmHjtzMapper.insert(ry);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsXmHjtzMapper.insertAttInfoAtt(ry.getRyid(), attid);;
				}
        		
        	}
        }
        
        resultMap.put("data", ry);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findByid(String token, BigDecimal ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsXmHjtz ry = tblYqnsXmHjtzMapper.selectById(ryid);
        resultMap.put("data", ry);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findattlistByid(String token, BigDecimal ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        } 
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        List<TblAttachment> list = tblAttachmentMapper.selectAttListBXmtzid(ryid);
        resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsXmHjtz> queryWrapper = new QueryWrapper<>();
		
		if (vo.getTitle()!=null && vo.getTitle().length()>0) {
			queryWrapper.like("title", vo.getTitle());
		}
		if (StringUtils.isNotBlank(staff.getDeptIds())) {
	    	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("XFRYIDS", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	    }else {
	        queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("XFRYIDS", staff.getStaffid()));
	    }
		queryWrapper.orderByDesc("RYID");
		com.github.pagehelper.PageInfo<TblYqnsXmHjtz> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsXmHjtzMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsXmHjtz> build = new PageResult<TblYqnsXmHjtz>().build(info);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsXmHjtzMapper.deleteAttInfoAttByxm(ryid);
        tblYqnsXmHjtzMapper.deleteoneById(ryid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean deleteatt(String token, String attid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsXmHjtzMapper.deleteAttInfoAtt(attid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean xfry(String token,String xfryids, String xfrynames, BigDecimal ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsXmHjtzMapper.xfry(xfryids, xfrynames, ryid);
        return ResponseFormat.retParam(1,200,null);
	}
 
	
}
