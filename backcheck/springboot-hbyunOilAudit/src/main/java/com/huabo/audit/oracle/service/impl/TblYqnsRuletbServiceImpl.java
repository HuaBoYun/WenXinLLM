package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsRule;
import com.huabo.audit.oracle.entity.TblYqnsRuletb;
import com.huabo.audit.oracle.mapper.TblYqnsRuletbMapper;
import com.huabo.audit.oracle.service.TblYqnsRuletbService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;


@Service
public class TblYqnsRuletbServiceImpl implements TblYqnsRuletbService {

	@Resource
	private TblYqnsRuletbMapper tblYqnsRuletbMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean saveOrupdate(String token, TblYqnsRuletb jd, String glids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        jd.setCreatedate(new Date());
        jd.setCreatestaffid(staff.getStaffid());
        jd.setCreatename(staff.getRealname());
        jd.setStatus(0); 
        if(jd!=null && jd.getTbid()!=null) {
        	tblYqnsRuletbMapper.updateById(jd);
        	if(glids!=null && glids.length()>0) {
        		List<TblYqnsRule> list = JSONArray.parseArray(glids,TblYqnsRule.class);
        		tblYqnsRuletbMapper.deleteglnr(jd.getTbid());
        		for (TblYqnsRule rule : list) {
        			tblYqnsRuletbMapper.insertglnr(jd.getTbid(), RandomUtil.uuBigDecimalId(), rule.getHjbl(), rule.getHjtype());
				}
        	} 
        	
        }else {
        	jd.setTbid(RandomUtil.uuBigDecimalId());
        	tblYqnsRuletbMapper.insert(jd);
        	if(glids!=null && glids.length()>0) {
        		List<TblYqnsRule> list = JSONArray.parseArray(glids,TblYqnsRule.class);
        		for (TblYqnsRule rule : list) {
        			tblYqnsRuletbMapper.insertglnr(jd.getTbid(), RandomUtil.uuBigDecimalId(), rule.getHjbl(), rule.getHjtype());
				}
        	}
        }
        resultMap.put("data", jd);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findByid(String token, BigDecimal jdid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsRuletb tb = tblYqnsRuletbMapper.selectById(jdid);
        List<TblYqnsRule> list = tblYqnsRuletbMapper.selectBytbId(jdid);
        tb.setList(list);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", tb);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblYqnsRuletb> queryWrapper = new QueryWrapper<>();
        if(vo.getYear()!=null && vo.getYear().length()>0) {
        	queryWrapper.like("ruleyear", vo.getYear());
        }
        
        if(vo.getTbrgid()!=null &&  vo.getTbrgid().length()>0) {
        	queryWrapper.eq("tbrgid", vo.getTbrgid());
        } 
        
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()));
        }
        //倒序 
        queryWrapper.orderByDesc(true, "CREATEDATE");
        com.github.pagehelper.PageInfo<TblYqnsRuletb> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsRuletbMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsRuletb> build = new PageResult<TblYqnsRuletb>().build(info);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal jdid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsRuletbMapper.deleteglnr(jdid);
        tblYqnsRuletbMapper.deleteoneById(jdid);
        return ResponseFormat.retParam(1,200,null);
	}
	
	
	@Override
	public JsonBean yzYear(String token, String year) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsRuletb> list = tblYqnsRuletbMapper.yzyear(year);
        if(list!=null && list.size()>0) {
        	return ResponseFormat.retParam(0, "已有当前"+year+"年的评分规则，无法新建", null);
        }
        return ResponseFormat.retParam(1,200,null);
	}
	
}
