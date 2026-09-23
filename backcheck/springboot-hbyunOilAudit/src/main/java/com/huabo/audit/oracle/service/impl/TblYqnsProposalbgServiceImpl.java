package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.huabo.audit.service.impl.ReservePropertyService;
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
import com.huabo.audit.oracle.entity.TblYqnsProposalbg;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsProposalbgMapper;
import com.huabo.audit.oracle.service.TblYqnsProposalbgService;
import com.huabo.audit.oracle.vo.XmdqVo;
import com.huabo.audit.util.PageResult;


@Service
public class TblYqnsProposalbgServiceImpl implements TblYqnsProposalbgService {
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblYqnsProposalbgMapper tblYqnsProposalbgMapper;

 	@Resource
	private ReservePropertyService reservePropertyService;
 	
 	@Resource
    private UserProvider userProvider;
	
	
	@Override
	public JsonBean saveOrupdate(String token, TblYqnsProposalbg ry, String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        ry.setCreatedate(new Date());
        ry.setCreatestaffid(staff.getStaffid());
        ry.setCreatename(staff.getRealname());
        ry.setStatus(0);
        if(ry!=null && ry.getBgid()!=null) {
        	tblYqnsProposalbgMapper.updateById(ry);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String aid : stins) {
        			tblYqnsProposalbgMapper.insertAttInfoAtt(ry.getBgid(), aid);;
				}
        	}
        }else {
        	ry.setBgid(RandomUtil.uuBigDecimalId());
        	tblYqnsProposalbgMapper.insert(ry);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsProposalbgMapper.insertAttInfoAtt(ry.getBgid(), attid);;
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
        TblYqnsProposalbg ry = tblYqnsProposalbgMapper.selectById(ryid);
        resultMap.put("data", ry);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(ry);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findattlistByid(String token, BigDecimal ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        } 
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        List<TblAttachment> list = tblAttachmentMapper.selectAttListBybgid(ryid);
        resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsProposalbg> queryWrapper = new QueryWrapper<>();
		
		if (vo.getDocument()!=null && vo.getDocument().length()>0) {
			queryWrapper.like("code", vo.getDocument());
		}
		
		if (vo.getTitle()!=null && vo.getTitle().length()>0) {
			queryWrapper.like("bgname", vo.getTitle());
		}
		
		if(vo.getCheckRight() == null) {
		 if (StringUtils.isNotBlank(staff.getDeptIds())) {
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	        }else {
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("CREATESTAFFID", staff.getStaffid()));
	        }
		}
		com.github.pagehelper.PageInfo<TblYqnsProposalbg> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsProposalbgMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsProposalbg> build = new PageResult<TblYqnsProposalbg>().build(info);

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
        tblYqnsProposalbgMapper.deleteAttInfoAttByxm(ryid);
        tblYqnsProposalbgMapper.deleteoneById(ryid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean deleteatt(String token, String attid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsProposalbgMapper.deleteAttInfoAtt(attid);
        return ResponseFormat.retParam(1,200,null);
	}

	
	
}
