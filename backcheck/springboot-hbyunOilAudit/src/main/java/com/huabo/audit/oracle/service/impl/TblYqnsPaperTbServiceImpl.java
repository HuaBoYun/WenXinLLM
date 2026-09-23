package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.huabo.audit.service.impl.ReservePropertyService;
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
import com.huabo.audit.oracle.entity.TblYqnsPaperTb;
import com.huabo.audit.oracle.mapper.TblYqnsPaperTbMapper;
import com.huabo.audit.oracle.service.TblYqnsPaperTbService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;


@Service
public class TblYqnsPaperTbServiceImpl implements TblYqnsPaperTbService {

	@Resource
	private TblYqnsPaperTbMapper tblYqnsPaperTbMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsPaperTb jd, String glids) throws Exception {
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
        	tblYqnsPaperTbMapper.updateById(jd);
        	if(glids!=null && glids.length()>0) {
        		tblYqnsPaperTbMapper.deleteglnr(jd.getTbid());
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsPaperTbMapper.insertglnr(jd.getTbid(),  glid);
				}
        	}
        	
        }else {
        	jd.setTbid(RandomUtil.uuBigDecimalId());
        	tblYqnsPaperTbMapper.insert(jd);
        	if(glids!=null && glids.length()>0) {
        		tblYqnsPaperTbMapper.deleteglnr(jd.getTbid());
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsPaperTbMapper.insertglnr(jd.getTbid(),  glid);
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
        TblYqnsPaperTb tb = tblYqnsPaperTbMapper.selectById(jdid);

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
        QueryWrapper<TblYqnsPaperTb> queryWrapper = new QueryWrapper<>(); 
        if(vo.getJdname()!=null && vo.getJdname().length()>0) { 
        	queryWrapper.like("tbname", vo.getJdname());
        }
        
        if(vo.getTbrgid()!=null &&  vo.getTbrgid().length()>0) {
        	queryWrapper.eq("tbrgid", vo.getTbrgid());
        } 
        
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("ZZSTAFFIDS", staff.getStaffid()).or().like("FZSTAFFIDS", staff.getStaffid()).or().like("PWSTAFFIDS", staff.getStaffid()));
        }
         
        if(StringUtils.isNotEmpty(vo.getYear())) {
        	LocalDate startlocalDate = LocalDate.parse(vo.getYear()+"-01-01", DateTimeFormatter.ISO_DATE);
            LocalDate endlocalDate = LocalDate.parse(vo.getYear()+"-12-31", DateTimeFormatter.ISO_DATE);
            if (StringUtils.isNotBlank(vo.getYear())) {
            	queryWrapper.lambda().ge(TblYqnsPaperTb::getCreatedate, startlocalDate);
            	queryWrapper.lambda().lt(TblYqnsPaperTb::getCreatedate, endlocalDate);
            }
        }
        
        //倒序 
        queryWrapper.orderByDesc(true, "CREATEDATE");
        com.github.pagehelper.PageInfo<TblYqnsPaperTb> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsPaperTbMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsPaperTb> build = new PageResult<TblYqnsPaperTb>().build(info);

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
        tblYqnsPaperTbMapper.deleteglnr(jdid);
        tblYqnsPaperTbMapper.deleteoneById(jdid);
        return ResponseFormat.retParam(1,200,null);
	}
	
	
	
}
