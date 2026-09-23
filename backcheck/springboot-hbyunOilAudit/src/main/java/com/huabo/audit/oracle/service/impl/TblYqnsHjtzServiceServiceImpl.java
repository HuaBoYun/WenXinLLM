package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.huabo.audit.service.impl.ReservePropertyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.huabo.audit.oracle.entity.TblYqnsHjtz;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsHjtzMapper;
import com.huabo.audit.oracle.service.TblYqnsHjtzService;
import com.huabo.audit.oracle.vo.XmdqVo;
import com.huabo.audit.util.PageResult;

@Service
public class TblYqnsHjtzServiceServiceImpl implements TblYqnsHjtzService {
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblYqnsHjtzMapper tblYqnsHjtzMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsHjtz ry, String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        ry.setCreatedate(new Date());
        ry.setCreatestaffid(staff.getStaffid());
        ry.setStatus(0);
        if(ry!=null && ry.getRyid()!=null) {
        	tblYqnsHjtzMapper.updateById(ry);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String aid : stins) {
        			tblYqnsHjtzMapper.insertAttInfoAtt(ry.getRyid(), aid);;
				}
        	}
        }else {
        	ry.setRyid(RandomUtil.uuBigDecimalId());
        	tblYqnsHjtzMapper.insert(ry);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsHjtzMapper.insertAttInfoAtt(ry.getRyid(), attid);;
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
		TblYqnsHjtz ry = tblYqnsHjtzMapper.selectById(ryid);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(ry);

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
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
        List<TblAttachment> list = tblAttachmentMapper.selectAttListByyjtzid(ryid);
        resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsHjtz> queryWrapper = new QueryWrapper<>();
		
		if (vo.getTitle()!=null && vo.getTitle().length()>0) {
			queryWrapper.like("title", vo.getTitle());
		}
		if (StringUtils.isNotBlank(staff.getDeptIds())) {
	    	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("XFRYIDS", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	    }else {
	        queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("XFRYIDS", staff.getStaffid()));
	    }
		queryWrapper.orderByDesc("RYID");
		com.github.pagehelper.PageInfo<TblYqnsHjtz> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsHjtzMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsHjtz> build = new PageResult<TblYqnsHjtz>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());

		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsHjtzMapper.deleteAttInfoAttByxm(ryid);
        tblYqnsHjtzMapper.deleteoneById(ryid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean deleteatt(String token, String attid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsHjtzMapper.deleteAttInfoAtt(attid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean xfry(String token,String xfryids, String xfrynames, BigDecimal ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsHjtzMapper.xfry(xfryids, xfrynames, ryid);
        return ResponseFormat.retParam(1,200,null);
	}
 
	
}
