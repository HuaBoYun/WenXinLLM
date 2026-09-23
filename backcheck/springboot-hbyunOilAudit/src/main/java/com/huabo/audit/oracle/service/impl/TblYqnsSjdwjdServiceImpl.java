package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.annotation.Resource;

import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
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
import com.huabo.audit.oracle.entity.TblYqnsSjdwjd;
import com.huabo.audit.oracle.mapper.TblYqnsSjdwjdMapper;
import com.huabo.audit.oracle.service.TblYqnsSjdwjdService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;


@Service
public class TblYqnsSjdwjdServiceImpl implements TblYqnsSjdwjdService {

	@Resource
	private TblYqnsSjdwjdMapper tblYqnsSjdwjdMapper;
	@Autowired
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean saveOrupdate(String token, TblYqnsSjdwjd jd, String glids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        jd.setCreatedate(new Date());
        jd.setCreatestaffid(staff.getStaffid());
        jd.setCreatename(staff.getRealname());
        jd.setStatus(0);
        if(jd!=null && jd.getJdid()!=null) {
        	tblYqnsSjdwjdMapper.updateById(jd);
        	if(glids!=null && glids.length()>0) {
        		tblYqnsSjdwjdMapper.deleteglnr(jd.getJdid());
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsSjdwjdMapper.insertglnr(jd.getJdid(),  glid);
				}
        	}
        	
        }else {
        	jd.setJdid(RandomUtil.uuBigDecimalId());
        	tblYqnsSjdwjdMapper.insert(jd);
        	if(glids!=null && glids.length()>0) {
        		tblYqnsSjdwjdMapper.deleteglnr(jd.getJdid());
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsSjdwjdMapper.insertglnr(jd.getJdid(),  glid);
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
        TblYqnsSjdwjd sjdwjd = tblYqnsSjdwjdMapper.selectById(jdid);
		//构建预留字段返回
		reservePropertyService.buildReserveProperty(sjdwjd);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", sjdwjd);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, SjdwjdVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblYqnsSjdwjd> queryWrapper = new QueryWrapper<>();
        if(vo.getJdname()!=null && vo.getJdname().length()>0) {
        	queryWrapper.like("jdname", vo.getJdname());
        }
        
        if(StringUtils.isNotBlank(vo.getQueryYear())) {
        	Date date = DateUtil.formatDate(vo.getQueryYear()+"-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
			System.out.println(date.toString());
			date = DateUtil.formatDate(vo.getQueryYear()+"-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			System.out.println(date.toString());
        	queryWrapper.ge("CREATEDATE", DateUtil.formatDate(vo.getQueryYear()+"-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss")).le("CREATEDATE",  DateUtil.formatDate(vo.getQueryYear()+"-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss"));
        }
        
        if(vo.getTbrgid()!=null &&  vo.getTbrgid().length()>0) {
        	queryWrapper.eq("tbrgid", vo.getTbrgid());
        } 
        
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("XFRYIDS", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("XFRYIDS", staff.getStaffid()));
        }
        //倒序 
        queryWrapper.orderByDesc(true, "CREATEDATE");
        com.github.pagehelper.PageInfo<TblYqnsSjdwjd> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsSjdwjdMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsSjdwjd> build = new PageResult<TblYqnsSjdwjd>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal jdid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsSjdwjdMapper.deleteglnr(jdid);
        tblYqnsSjdwjdMapper.deleteoneById(jdid);
        return ResponseFormat.retParam(1,200,null);
	}
	
}
