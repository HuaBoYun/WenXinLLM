package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
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
import com.huabo.audit.oracle.entity.TblYqnsPaper;
import com.huabo.audit.oracle.entity.TblYqnsPaperPx;
import com.huabo.audit.oracle.entity.TblYqnsPaperTb;
import com.huabo.audit.oracle.entity.TblYqnsRule;
import com.huabo.audit.oracle.entity.TblYqnsRuletb;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsPaperMapper;
import com.huabo.audit.oracle.mapper.TblYqnsPaperPxMapper;
import com.huabo.audit.oracle.mapper.TblYqnsPaperTbMapper;
import com.huabo.audit.oracle.mapper.TblYqnsRuletbMapper;
import com.huabo.audit.oracle.service.TblYqnsPaperService;
import com.huabo.audit.oracle.vo.XmdqVo;
import com.huabo.audit.util.PageResult;


@Service
public class TblYqnsPaperServiceImpl implements TblYqnsPaperService {
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblYqnsPaperMapper tblYqnsPaperMapper;

	@Resource
	private TblYqnsPaperPxMapper tblYqnsPaperPxMapper;
	
	@Resource
	private TblYqnsRuletbMapper tblYqnsRuletbMapper;
	
	
	@Resource
	private TblYqnsPaperTbMapper tblYqnsPaperTbMapper;
    @Autowired
    private ReservePropertyService reservePropertyService;
    
    @Resource
    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsPaper ry, String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        ry.setCreatedate(new Date());
        ry.setCreatestaffid(staff.getStaffid());
        ry.setStatus(0);
        if(ry!=null && ry.getPerid()!=null) {
        	tblYqnsPaperMapper.updateById(ry);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String aid : stins) {
        			tblYqnsPaperMapper.insertAttInfoAtt(ry.getPerid(), aid);;
				}
        	}
        }else {
        	ry.setPerid(RandomUtil.uuBigDecimalId());
        	tblYqnsPaperMapper.insert(ry);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsPaperMapper.insertAttInfoAtt(ry.getPerid(), attid);;
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
        TblYqnsPaper ry = tblYqnsPaperMapper.selectById(ryid);

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
        List<TblAttachment> list = tblAttachmentMapper.selectAttListBylwid(ryid);
        resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsPaper> queryWrapper = new QueryWrapper<>();
		
		if (vo.getXmname()!=null && vo.getXmname().length()>0) {
			queryWrapper.like("papername", vo.getXmname());
		}
		 if (StringUtils.isNotBlank(staff.getDeptIds())) {
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	        }else {
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("ZXRSTAFFID", staff.getStaffid()));
	        }
		 
		com.github.pagehelper.PageInfo<TblYqnsPaper> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsPaperMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsPaper> build = new PageResult<TblYqnsPaper>().build(info);

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
        tblYqnsPaperMapper.deleteAttInfoAttByxm(ryid);
        tblYqnsPaperMapper.deleteoneById(ryid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean deleteatt(String token, String attid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsPaperMapper.deleteAttInfoAtt(attid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean lwsb(String token, String ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        if(ryid!=null && ryid.length()>0) {
        	String[] ids = ryid.split(",");
        	for (String id : ids) {
        		 tblYqnsPaperMapper.sblw(id);
			}
        }
        return ResponseFormat.retParam(1,200,null);
	}
	
	
	@Override
	public JsonBean thlw(String token, String ryid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        if(ryid!=null && ryid.length()>0) {
        	String[] ids = ryid.split(",");
        	for (String id : ids) {
        		 tblYqnsPaperMapper.thlw(id);
			}
        }
        return ResponseFormat.retParam(1,200,null);
	}

 
	@Override
	public JsonBean findAlltzList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsPaper> queryWrapper = new QueryWrapper<>();
		
		if (vo.getXmname()!=null && vo.getXmname().length()>0) {
			queryWrapper.like("papername", vo.getXmname());
		}
		if (StringUtils.isNotBlank(staff.getDeptIds())) {
	        queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	    }else {
	        queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("ZXRSTAFFID", staff.getStaffid()));
	    }
		 
		 queryWrapper.and(q -> q.eq("STATUS", "1"));
		 queryWrapper.orderByAsc("CODE");
		com.github.pagehelper.PageInfo<TblYqnsPaper> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsPaperMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsPaper> build = new PageResult<TblYqnsPaper>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());

		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	@Override
	public JsonBean lwpxxg(String token, BigDecimal ryid,Integer code) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsPaperMapper.pxxg(ryid, code);
        return ResponseFormat.retParam(1,200,null);
	}
	
	
	
	
	@Override
	public JsonBean deletebyglid(String token, BigDecimal perid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        if(perid!=null) {
        	tblYqnsPaperMapper.deletebyglid(perid);
        	return ResponseFormat.retParam(1,200,null);
        }else {
        	return ResponseFormat.retParam(0,"未选择数据",null);
        }
        }
       
		
	@Override
	public JsonBean findbytbnr(String token, BigDecimal tbid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        if(tbid!=null) {
        	List<TblYqnsPaper> list = tblYqnsPaperMapper.selectByListId(tbid);

			//构建预留字段返回
			reservePropertyService.buildReserveProperty(list);

     		resultMap.put("data", list);
        }else {
        	resultMap.put("data", "");
        }
       
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllxztbList(String token, Integer pageNumber, Integer pageSize, TblYqnsPaper vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsPaper> queryWrapper = new QueryWrapper<>();
		
		if (vo.getPapername()!=null && vo.getPapername().length()>0) {
			queryWrapper.like("papername", vo.getPapername());
		}
		 if (StringUtils.isNotBlank(staff.getDeptIds())) {
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	        }else {
	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("CREATESTAFFID", staff.getStaffid()));
	        }
		 queryWrapper.and(q -> q.eq("STATUS", "1"));
		 queryWrapper.and(q -> q.notInSql("PERID", "SELECT PERID FROM TBL_YQNS_PAPERTB_GL"));
		 queryWrapper.orderByAsc("CODE");
		com.github.pagehelper.PageInfo<TblYqnsPaper> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsPaperMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsPaper> build = new PageResult<TblYqnsPaper>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());

		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	@Override
	public JsonBean findbyfslist(String token, BigDecimal perid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        if(perid!=null) {
        	List<TblYqnsPaperPx> list = tblYqnsPaperPxMapper.selectByListId(perid);

			//构建预留字段返回
//			reservePropertyService.buildReserveProperty(list);

     		resultMap.put("data", list);
        }else {
        	resultMap.put("data", "");
        }
       
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	
	@Override
	public JsonBean findAllzpxList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) { 
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsPaper> queryWrapper = new QueryWrapper<>(); 
		 
		if (vo.getXmname()!=null && vo.getXmname().length()>0) {
			queryWrapper.like("papername", vo.getXmname());
		}
//		 if (StringUtils.isNotBlank(staff.getDeptIds())) {
//	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("PWSTAFFIDS", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
//	        }else {
//	        	queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("PWSTAFFIDS", staff.getStaffid()));
//	        }
		 queryWrapper.and(q -> q.eq("STATUS", "1")); 
		 queryWrapper.and(q -> q.inSql("PERID", "SELECT PERID FROM TBL_YQNS_PAPERTB_GL where TBID in (SELECT TBID from TBL_YQNS_PAPERTB WHERE STATUS=6)"));
		 queryWrapper.orderByDesc("total");
		com.github.pagehelper.PageInfo<TblYqnsPaper> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsPaperMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsPaper> build = new PageResult<TblYqnsPaper>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());

		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public JsonBean selectByListmc(String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        List<TblYqnsPaper> list = tblYqnsPaperMapper.selectByListmc();
       
      
        int currentYear = Year.now().getValue();
        List<TblYqnsRuletb> yzyear = tblYqnsRuletbMapper.yzyear(currentYear+"");
        int num=0;
        if(yzyear!=null && yzyear.size()>0) {
        	TblYqnsRuletb tb=yzyear.get(0);
        	double bl = tblYqnsRuletbMapper.selectBydj(tb.getTbid(), "一等奖");
        	double sl=list.size()*bl;
        	System.out.println("一等奖数量："+sl);
        	num=(int) Math.round(sl);
        	System.out.println(num);
        	if(num==0) {num=1;}
        	List<TblYqnsPaper> hjlist =new ArrayList<>();
			for (int i = 0; i < num; i++) {
				TblYqnsPaper tblYqnsPaper = list.get(i);
				//构建预留字段返回
				reservePropertyService.buildReserveProperty(tblYqnsPaper);
				hjlist.add(tblYqnsPaper);
			}
			resultMap.put("list1", hjlist);
			
			
			int qs=num;
			bl = tblYqnsRuletbMapper.selectBydj(tb.getTbid(), "二等奖");
        	sl=list.size()*bl;
        	System.out.println("二等奖数量："+sl);
        	num=(int) Math.round(sl)+qs;
        	System.out.println(num);
        	if(num==0) {num=1;}
        	 hjlist =new ArrayList<>();
			for (int i = qs; i < num; i++) {
				TblYqnsPaper tblYqnsPaper = list.get(i);
				//构建预留字段返回
				reservePropertyService.buildReserveProperty(tblYqnsPaper);
				hjlist.add(tblYqnsPaper);
			}
			resultMap.put("list2", hjlist);
			
			qs=num;
			bl = tblYqnsRuletbMapper.selectBydj(tb.getTbid(), "三等奖");
        	sl=list.size()*bl;
        	System.out.println("三等奖数量："+sl);
        	num=(int) Math.round(sl)+qs;
        	System.out.println(num);
        	if(num==0) {num=1;}
        	 hjlist =new ArrayList<>();
			for (int i = qs; i < num; i++) {
				TblYqnsPaper tblYqnsPaper = list.get(i);
				//构建预留字段返回
				reservePropertyService.buildReserveProperty(tblYqnsPaper);
				hjlist.add(tblYqnsPaper);
			}
			resultMap.put("list3", hjlist);
        }
        
		return ResponseFormat.retParam(1,200,resultMap);
	}
	
	
	@Override
	public List<TblYqnsPaper> findAllexportList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		
		QueryWrapper<TblYqnsPaper> queryWrapper = new QueryWrapper<>();
		
		if (vo.getXmname()!=null && vo.getXmname().length()>0) {
			queryWrapper.like("papername", vo.getXmname());
		}
		if (StringUtils.isNotBlank(staff.getDeptIds())) {
	        queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("CREATESTAFFID", staff.getStaffid()).or().inSql("CREATESTAFFID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
	    }else {
	        queryWrapper.and(q -> q.eq("CREATESTAFFID", staff.getStaffid()).or().like("ZXRSTAFFID", staff.getStaffid()));
	    }
		 
		 queryWrapper.and(q -> q.eq("STATUS", "1"));
		 queryWrapper.orderByAsc("CODE");
		com.github.pagehelper.PageInfo<TblYqnsPaper> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsPaperMapper.selectList(queryWrapper));
		return info.getList();
	}

	
	@Override
	public JsonBean fssaveOrupdate(String token, TblYqnsPaperPx ry) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
     
        if(ry!=null && ry.getPxid()!=null) {
        	tblYqnsPaperPxMapper.updateById(ry);
        }else {
        	ry.setPxid(RandomUtil.uuBigDecimalId());
        	tblYqnsPaperPxMapper.insert(ry);
        }
        if(ry.getPerid()!=null) {
        	 Integer hzfs = tblYqnsPaperPxMapper.hzfs(ry.getPerid());
        	 if(hzfs==null) {
        		 hzfs=0;
        	 }
             tblYqnsPaperMapper.pxhj(ry.getPerid(), hzfs);
             
             
             List<TblYqnsPaper> list = tblYqnsPaperMapper.selectByListmcnot();
             if(list!=null &&  list.size()>0) {
            	 for (int i = 0; i < list.size(); i++) {
            		 TblYqnsPaper per=list.get(i);
            		 tblYqnsPaperMapper.xgmc(per.getPerid(), i+1);
				}
             }
             
              
        }
       
        resultMap.put("data", ry);
		return ResponseFormat.retParam(1,200,resultMap);
	}
 
	
	
	@Override
	public JsonBean yzpf(String token, BigDecimal tbid) throws Exception {
		TblYqnsPaperTb tb = tblYqnsPaperTbMapper.selectById(tbid);
		List<TblYqnsPaper> list = tblYqnsPaperMapper.selectByListId(tbid);
		if(tb!=null && list!=null && list.size()>0) {
			 int length = tb.getPwstaffids().split(",").length;
			 for (TblYqnsPaper paper : list) {
				 List<TblYqnsPaperPx> pfs = tblYqnsPaperPxMapper.selectByListId(paper.getPerid());
				 if(pfs!=null&& pfs.size()!=length) {
					 return ResponseFormat.retParam(0,"评分未完成，不能提交审批",null);
				 }
			}
				return ResponseFormat.retParam(1,"验证通过",null);
		}
		return ResponseFormat.retParam(0,"评分未完成，不能提交审批",null);
	}
	
	
}
