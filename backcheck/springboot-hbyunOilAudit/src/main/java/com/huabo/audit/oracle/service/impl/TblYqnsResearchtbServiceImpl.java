package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsResearch;
import com.huabo.audit.oracle.entity.TblYqnsResearchtb;
import com.huabo.audit.oracle.mapper.TblYqnsResearchMapper;
import com.huabo.audit.oracle.mapper.TblYqnsResearchtbMapper;
import com.huabo.audit.oracle.service.TblYqnsResearchtbService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.oracle.vo.XmdqVo;
import com.huabo.audit.util.PageResult;
import org.apache.commons.lang3.StringUtils;


@Service
public class TblYqnsResearchtbServiceImpl implements TblYqnsResearchtbService {

	@Resource
	private TblYqnsResearchtbMapper tblYqnsResearchtbMapper;
	
	@Resource
	private TblYqnsResearchMapper tblYqnsResearchMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsResearchtb jd, String glids) throws Exception {
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
        	tblYqnsResearchtbMapper.updateById(jd);
        	if(glids!=null && glids.length()>0) {
        		tblYqnsResearchtbMapper.deleteglnr(jd.getTbid());
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsResearchtbMapper.insertglnr(jd.getTbid(),  glid);
				}
        	}
        	
        }else {
        	jd.setTbid(RandomUtil.uuBigDecimalId());
        	tblYqnsResearchtbMapper.insert(jd);
        	if(glids!=null && glids.length()>0) {
        		tblYqnsResearchtbMapper.deleteglnr(jd.getTbid());
        		String[] glidss = glids.split(",");
        		for (String glid : glidss) {
        			tblYqnsResearchtbMapper.insertglnr(jd.getTbid(),  glid);
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
        TblYqnsResearchtb tb = tblYqnsResearchtbMapper.selectById(jdid);

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
        QueryWrapper<TblYqnsResearchtb> queryWrapper = new QueryWrapper<>();
        if(vo.getJdname()!=null && vo.getJdname().length()>0) {
        	queryWrapper.like("tbname", vo.getJdname());
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
            	queryWrapper.lambda().ge(TblYqnsResearchtb::getCreatedate, startlocalDate);
            	queryWrapper.lambda().lt(TblYqnsResearchtb::getCreatedate, endlocalDate);
            }
        }
        
        //倒序 
        queryWrapper.orderByDesc(true, "CREATEDATE");
        com.github.pagehelper.PageInfo<TblYqnsResearchtb> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsResearchtbMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsResearchtb> build = new PageResult<TblYqnsResearchtb>().build(info);

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
        tblYqnsResearchtbMapper.deleteglnr(jdid);
        tblYqnsResearchtbMapper.deleteoneById(jdid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean findbyzb(String token, BigDecimal tbid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        List<TblYqnsResearch> list = tblYqnsResearchMapper.selectListBytbid(tbid);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(list);
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deletezbone(String token, BigDecimal chid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsResearchMapper.deleteoneByzbId(chid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean saveOrupdatezb(String token, TblYqnsResearch jd) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        jd.setCreatedate(new Date());
        jd.setCreatestaffid(staff.getStaffid());
        jd.setCreatename(staff.getRealname());
        jd.setStatus(0); 
        if(jd!=null && jd.getChid()!=null) {
        	tblYqnsResearchMapper.updateById(jd);
        }else {
        	jd.setChid(RandomUtil.uuBigDecimalId());
        	tblYqnsResearchMapper.insert(jd);
        }
        resultMap.put("data", jd);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	@Override
	public JsonBean findByzbid(String token, BigDecimal chid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsResearch ch = tblYqnsResearchMapper.selectByzbId(chid);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(ch);

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", ch);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findbyztz(String token, XmdqVo vo,Integer pageNumber, Integer pageSize) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper<TblYqnsResearch> queryWrapper = new QueryWrapper<>();
        if(vo.getXmname()!=null && vo.getXmname().length()>0) {
        	queryWrapper.like("direction", vo.getXmname()); 
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
            	queryWrapper.lambda().ge(TblYqnsResearch::getCreatedate, startlocalDate);
            	queryWrapper.lambda().lt(TblYqnsResearch::getCreatedate, endlocalDate);
            }
        }
        queryWrapper.and(q -> q.inSql("chid", "SELECT CHID FROM TBL_YQNS_RESEARCHTB_GL where TBID in (SELECT TBID from TBL_YQNS_RESEARCHTB WHERE STATUS=6)"));
        //倒序 
        queryWrapper.orderByAsc("code");
        com.github.pagehelper.PageInfo<TblYqnsResearch> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsResearchMapper.selectList(queryWrapper));
        if(info!=null &&info.getList()!=null) {
        	for (TblYqnsResearch ch : info.getList()) {
        		List<TblYqnsResearchtb> tt = tblYqnsResearchtbMapper.selectBychId(ch.getChid());
        		if(tt!=null && tt.size()>0) {
        			ch.setTborgname(tt.get(0).getTbrgname());
        		}

				//构建预留字段返回
				reservePropertyService.buildReserveProperty(ch);
			}
        	
        }
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsResearch> build = new PageResult<TblYqnsResearch>().build(info);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public List<TblYqnsResearch> findexportlist(String token, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        QueryWrapper<TblYqnsResearch> queryWrapper = new QueryWrapper<>();
        if(vo.getXmname()!=null && vo.getXmname().length()>0) {
        	queryWrapper.like("direction", vo.getXmname());
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
            	queryWrapper.lambda().ge(TblYqnsResearch::getCreatedate, startlocalDate);
            	queryWrapper.lambda().lt(TblYqnsResearch::getCreatedate, endlocalDate);
            }
        }
        queryWrapper.and(q -> q.inSql("chid", "SELECT CHID FROM TBL_YQNS_RESEARCHTB_GL where TBID in (SELECT TBID from TBL_YQNS_RESEARCHTB WHERE STATUS=6)"));
        //倒序 
        queryWrapper.orderByAsc("code");
        com.github.pagehelper.PageInfo<TblYqnsResearch> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsResearchMapper.selectList(queryWrapper));
        if(info!=null &&info.getList()!=null) {
        	for (TblYqnsResearch ch : info.getList()) {
        		List<TblYqnsResearchtb> tt = tblYqnsResearchtbMapper.selectBychId(ch.getChid());
        		if(tt!=null && tt.size()>0) {
        			ch.setTborgname(tt.get(0).getTbrgname());
        		}
			}
        	
        }
        return info.getList();
	}
	
}
