package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.service.impl.ReservePropertyService;
import org.apache.commons.collections.CollectionUtils;
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
import com.huabo.audit.oracle.entity.TblYqnsRzmxtb;
import com.huabo.audit.oracle.mapper.TblYqnsRzmxtbMapper;
import com.huabo.audit.oracle.service.TblYqnsRzmxtbService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;


@Service
public class TblYqnsRzmxtbServiceImpl implements TblYqnsRzmxtbService {

	@Resource
	private TblYqnsRzmxtbMapper tblYqnsRzmxtbMapper;
	@Autowired
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean saveOrupdate(String token, TblYqnsRzmxtb jd, String glids) throws Exception {
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
        	tblYqnsRzmxtbMapper.updateById(jd);
        	if(glids!=null && glids.length()>0) {
        		tblYqnsRzmxtbMapper.deleteglnr(jd.getTbid());
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsRzmxtbMapper.insertglnr(jd.getTbid(),  glid);
				}
        	}
        	
        }else {
        	jd.setTbid(RandomUtil.uuBigDecimalId());
        	tblYqnsRzmxtbMapper.insert(jd);
        	if(glids!=null && glids.length()>0) {
        		tblYqnsRzmxtbMapper.deleteglnr(jd.getTbid());
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsRzmxtbMapper.insertglnr(jd.getTbid(),  glid);
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
        TblYqnsRzmxtb tb = tblYqnsRzmxtbMapper.selectById(jdid);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(tb);

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
        QueryWrapper<TblYqnsRzmxtb> queryWrapper = new QueryWrapper<>();
        if(vo.getTbname()!=null && vo.getTbname().length()>0) {
        	queryWrapper.like("tbname", vo.getTbname());
        }
        
        if(vo.getTbrgid()!=null &&  vo.getTbrgid().length()>0) {
        	queryWrapper.eq("tbrgid", vo.getTbrgid());
        } 
        
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()));
        }
        
        if(StringUtils.isNotEmpty(vo.getYear())) {
        	LocalDate startlocalDate = LocalDate.parse(vo.getYear()+"-01-01", DateTimeFormatter.ISO_DATE);
            LocalDate endlocalDate = LocalDate.parse(vo.getYear()+"-12-31", DateTimeFormatter.ISO_DATE);
            if (StringUtils.isNotBlank(vo.getYear())) {
            	queryWrapper.lambda().ge(TblYqnsRzmxtb::getCreatedate, startlocalDate);
            	queryWrapper.lambda().lt(TblYqnsRzmxtb::getCreatedate, endlocalDate);
            }
        }
        
        //倒序 
        queryWrapper.orderByDesc(true, "CREATEDATE");
        com.github.pagehelper.PageInfo<TblYqnsRzmxtb> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsRzmxtbMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsRzmxtb> build = new PageResult<TblYqnsRzmxtb>().build(info);

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
        tblYqnsRzmxtbMapper.deleteglnr(jdid);
        tblYqnsRzmxtbMapper.deleteoneById(jdid);
        return ResponseFormat.retParam(1,200,null);
	}

	
}
