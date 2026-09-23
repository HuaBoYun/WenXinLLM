package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.huabo.audit.service.impl.ReservePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsXmsqsq;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsXmsqsqMapper;
import com.huabo.audit.oracle.service.TblYqnsXmsqsqService;
import com.huabo.audit.oracle.vo.XmdqVo;
import com.huabo.audit.util.PageResult;


@Service
public class TblYqnsXmsqsqServiceImpl implements TblYqnsXmsqsqService {
	
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblYqnsXmsqsqMapper tblYqnsXmsqsqMapper;
	
	 @Autowired
	 private  ImplementPlanMapper implementPlanMapper;

	 @Autowired
	 private ReservePropertyService reservePropertyService;
	 
	 @Resource
	 private UserProvider userProvider;
	

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsXmsqsq xmqd, String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        xmqd.setCreatedate(new Date());
    	xmqd.setCreatestaffid(staff.getStaffid());
    	xmqd.setStatus(0);
        if(xmqd!=null && xmqd.getXmdqid()!=null) {
        	tblYqnsXmsqsqMapper.updateById(xmqd);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String aid : stins) {
        			tblYqnsXmsqsqMapper.insertAttInfoAtt(xmqd.getXmdqid(), aid);;
				}
        	}
        }else {
        	xmqd.setXmdqid(RandomUtil.uuBigDecimalId());
        	tblYqnsXmsqsqMapper.insert(xmqd);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsXmsqsqMapper.insertAttInfoAtt(xmqd.getXmdqid(), attid);;
				}
        		
        	}
        }
        if(xmqd.getProjectid()!=null) {
        	implementPlanMapper.updateyqTime(xmqd.getProjectid(),  DateUtil.parseDate(xmqd.getYqdate(), "yyyy-MM-dd"));
        }
        
        resultMap.put("data", xmqd);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findByid(String token, BigDecimal xmdqid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblYqnsXmsqsq xmdq = tblYqnsXmsqsqMapper.selectById(xmdqid);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(xmdq);
        resultMap.put("data", xmdq);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findattlistByid(String token, BigDecimal xmdqid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        } 
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        List<TblAttachment> list = tblAttachmentMapper.selectAttListByxmyqid(xmdqid);
        resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsXmsqsq> queryWrapper = new QueryWrapper<>();
		
		if (vo.getXmname()!=null && vo.getXmname().length()>0) {
			queryWrapper.like("xmname", vo.getXmname());
		}
		com.github.pagehelper.PageInfo<TblYqnsXmsqsq> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsXmsqsqMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsXmsqsq> build = new PageResult<TblYqnsXmsqsq>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal xmdqid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsXmsqsqMapper.deleteAttInfoAttByxm(xmdqid);
        tblYqnsXmsqsqMapper.deleteoneById(xmdqid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean deleteatt(String token, String attid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsXmsqsqMapper.deleteAttInfoAtt(attid);
        return ResponseFormat.retParam(1,200,null);
	}

	
}
