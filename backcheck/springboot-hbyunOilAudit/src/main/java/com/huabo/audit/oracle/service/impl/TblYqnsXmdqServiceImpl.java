package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.huabo.audit.service.impl.ReservePropertyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.InterimAuditDetailEntity;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.oracle.entity.ProjectProposalEvaluationEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhGL;
import com.huabo.audit.oracle.entity.TblYqnsOtherAudit;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.mapper.EnginAuditProjectMapper;
import com.huabo.audit.oracle.mapper.FundAuditProjectMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.InterimAuditDetailMapper;
import com.huabo.audit.oracle.mapper.LeaveAudit2LMapper;
import com.huabo.audit.oracle.mapper.ProjectProposalEvaluationMapper;
import com.huabo.audit.oracle.mapper.TblAttachmentMapper;
import com.huabo.audit.oracle.mapper.TblYqnsJhglJhGLMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOtherAuditMapper;
import com.huabo.audit.oracle.mapper.TblYqnsXmdqMapper;
import com.huabo.audit.oracle.service.TblYqnsXmdqService;
import com.huabo.audit.oracle.vo.XmdqVo;
import com.huabo.audit.util.PageInfoUtil;
import com.huabo.audit.util.PageResult;

import tk.mybatis.mapper.entity.Example;

@Service
public class TblYqnsXmdqServiceImpl implements TblYqnsXmdqService {
	
	@Resource
	private TblAttachmentMapper tblAttachmentMapper;
	
	@Resource
	private TblYqnsXmdqMapper tblYqnsXmdqMapper;
	
	@Autowired
    private ProjectProposalEvaluationMapper ProjectProposalEvaluationMapper;
	
	@Autowired
    private LeaveAudit2LMapper leaveAudit2LMapper;
	
	@Autowired
    private InterimAuditDetailMapper interimAuditDetailMapper;
	
	@Resource
	private TblYqnsJhglJhGLMapper tblYqnsJhglJhGLMapper;
	
	@Resource
    private TblYqnsOtherAuditMapper tblYqnsOtherAuditMapper;
	
	@Autowired
	private  ImplementPlanMapper implementPlanMapper;
	
	 @Resource
	 private FundAuditProjectMapper fundAuditProjectMapper;
	    
	    
	  @Resource
	  private EnginAuditProjectMapper enginAuditProjectMapper;
	  @Autowired
	  private ReservePropertyService reservePropertyService;
	  
	  @Resource
	    private UserProvider userProvider;

	@Override
	public JsonBean saveOrupdate(String token, TblYqnsXmdq xmqd, String attids) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        xmqd.setCreatedate(new Date());
    	xmqd.setCreatestaffid(staff.getStaffid());
    	xmqd.setStatus(0);
        if(xmqd!=null && xmqd.getXmdqid()!=null) {
        	tblYqnsXmdqMapper.updateById(xmqd);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String aid : stins) {
        			tblYqnsXmdqMapper.insertAttInfoAtt(xmqd.getXmdqid(), aid);;
				}
        	}
        }else {
        	xmqd.setXmdqid(RandomUtil.uuBigDecimalId());
        	tblYqnsXmdqMapper.insert(xmqd);
        	if(attids!=null && attids.length()>0) {
        		String[] stins = attids.split(",");
        		for (String attid : stins) {
        			tblYqnsXmdqMapper.insertAttInfoAtt(xmqd.getXmdqid(), attid);;
				}
        		
        	}
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
        TblYqnsXmdq xmdq = tblYqnsXmdqMapper.selectById(xmdqid);

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
        List<TblAttachment> list = tblAttachmentMapper.selectAttListByxmdqid(xmdqid);
        resultMap.put("data", list);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsXmdq> queryWrapper = new QueryWrapper<>();
		
		if (vo.getXmname()!=null && vo.getXmname().length()>0) {
			queryWrapper.like("xmname", vo.getXmname());
		}
		if (vo.getXmtype() != null && vo.getXmtype() .length()>0) {
			queryWrapper.eq("xmtype",vo.getXmtype());
		} 
		if (vo.getQdcode()!=null && vo.getQdcode().length()>0) {
			queryWrapper.like("qdcode", vo.getQdcode());
		}
		if(vo.getStaffId() != null) {
			queryWrapper.eq("CREATESTAFFID", vo.getStaffId());
			queryWrapper.eq("STATUS", 1);
		}
		if(vo.getXmnd() != null) {
			queryWrapper.inSql("PLANID", "SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+vo.getXmnd());
			queryWrapper.eq("STATUS", 1);
		}
		queryWrapper.orderByDesc("XMDQID");
		com.github.pagehelper.PageInfo<TblYqnsXmdq> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsXmdqMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsXmdq> build = new PageResult<TblYqnsXmdq>().build(info);

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
        tblYqnsXmdqMapper.deleteAttInfoAttByxm(xmdqid);
        tblYqnsXmdqMapper.deleteoneById(xmdqid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean deleteatt(String token, String attid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsXmdqMapper.deleteAttInfoAtt(attid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean qdproject(String token, BigDecimal xmdqid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsXmdq xmdq = tblYqnsXmdqMapper.selectById(xmdqid);
        xmdq.setStatus(1);
        tblYqnsXmdqMapper.updateById(xmdq);
        if(xmdq.getGljhxmid()!=null) {
        	 this.implementPlanMapper.updatePjStart(xmdq.getGljhxmid());
        }
       
        return ResponseFormat.retParam(1,200,null);
	}
	
	
	
	
	@Override
	public JsonBean findqdAllList(String token, Integer pageNumber, Integer pageSize, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		QueryWrapper<TblYqnsXmdq> queryWrapper = new QueryWrapper<>();
		 
		if (vo.getXmname()!=null && vo.getXmname().length()>0) {
			queryWrapper.like("xmname", vo.getXmname());
		} 
		if (vo.getXmtype() != null && vo.getXmtype() .length()>0) {
			queryWrapper.eq("xmtype",vo.getXmtype());
		}
		queryWrapper.eq("status","1");
		com.github.pagehelper.PageInfo<TblYqnsXmdq> info =  PageMethod.startPage(vo.getPageNum(), vo.getPageSize())
				.doSelectPageInfo(() -> tblYqnsXmdqMapper.selectList(queryWrapper));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsXmdq> build = new PageResult<TblYqnsXmdq>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean auditProjectPlanAnalysis(String token, Integer pageNumber, Integer pageSize, Integer xmnd)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		com.github.pagehelper.PageInfo<TblYqnsXmdq> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsXmdqMapper.selectAuditProjectPlanAnalysisList(xmnd));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsXmdq> build = new PageResult<TblYqnsXmdq>().build(info);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean findPlanAnalysisXmList(String token, Integer pageNumber, Integer pageSize, Integer xmnd,
			Integer dataType) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
		com.github.pagehelper.PageInfo<TblYqnsXmdq> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsXmdqMapper.selectPlanAnalysisXmList(xmnd,dataType));
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsXmdq> build = new PageResult<TblYqnsXmdq>().build(info);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(build.getTlist());
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getMx11(String token, Integer pageNumber, Integer pageSize, Integer xmnd, String projectType, String projectName) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Page<ProjectProposalEvaluationEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> ProjectProposalEvaluationMapper.findListByAnalysis(xmnd,projectType,projectName));
		PageInfo<ProjectProposalEvaluationEntity> pageInfo = new PageInfoUtil<ProjectProposalEvaluationEntity>().parsePageInfo(page);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("evaPage", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getMx21(String token, Integer pageNumber, Integer pageSize, Integer xmnd, String projectName)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        
        Page<LeaveAudit2LEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(() -> leaveAudit2LMapper.findListByAnalysis(xmnd,projectName));
        PageInfo<LeaveAudit2LEntity> pageInfo = new PageInfoUtil<LeaveAudit2LEntity>().parsePageInfo(page);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());
		resultMap.put("laTwoPage", pageInfo);
        return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getMx22(String token, Integer pageNumber, Integer pageSize, Integer xmnd, String projectName)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
        
        Page<InterimAuditDetailEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> interimAuditDetailMapper.findListByAnalysis(xmnd,projectName));
		PageInfo<InterimAuditDetailEntity> pageInfo = new PageInfoUtil<InterimAuditDetailEntity>().parsePageInfo(page);
		resultMap.put("rzList", pageInfo);
        return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getMxhzList(String token, Integer pageNumber, Integer pageSize, Integer xmnd, String glType,String projectName) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
	    Page<TblYqnsJhglJhGL> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> tblYqnsJhglJhGLMapper.findListByAnalysis(xmnd,glType,projectName));
		PageInfo<TblYqnsJhglJhGL> pageInfo = new PageInfoUtil<TblYqnsJhglJhGL>().parsePageInfo(page);
		resultMap.put("hzList", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getMxOtherAuditList(String token, Integer pageNumber, Integer pageSize, Integer xmnd,
			String auditItemName) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
		
		Page<TblYqnsOtherAudit> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> tblYqnsOtherAuditMapper.findListByAnalysis(xmnd,auditItemName));
        List<String> orgNameList = null;
        if(page.getTotal() != 0) { 
        	for (TblYqnsOtherAudit oau : page.getResult()) {
            	orgNameList = this.tblYqnsOtherAuditMapper.selectAuditOrgNameList(oau.getAuditOrgidStrs());
            	oau.setAuditOrgNameStrs(String.join(",", orgNameList));
    		}
        }
        PageInfo<TblYqnsOtherAudit> pageInfo = new PageInfoUtil<TblYqnsOtherAudit>().parsePageInfo(page);
		resultMap.put("oauList", pageInfo);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean getTipList(String token, XmdqVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
		
        List<TblYqnsXmdq> dataList = this.tblYqnsXmdqMapper.selectTipList(staff.getStaffid());

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(dataList);
		return ResponseFormat.retParam(1,200,dataList);
	}
	
	
	@Override
	public JsonBean tzproject(String token, BigDecimal xmdqid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsXmdq xmdq = tblYqnsXmdqMapper.selectById(xmdqid);
        xmdq.setStatus(2);
        tblYqnsXmdqMapper.updateById(xmdq);
        if(xmdq.getGljhxmid()!=null) {
        	 this.implementPlanMapper.tzstatus(xmdq.getGljhxmid());
        	 
        	 ImplementPlanEntity plan = implementPlanMapper.selectById(xmdq.getGljhxmid().toString());
 			if(plan!=null && plan.getXmapbid()!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
 	        	fundAuditProjectMapper.tzstatus(plan.getXmapbid().toString());
 	        	
 	        }
 	        if(plan!=null &&plan.getXmapbid()!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
 	        	enginAuditProjectMapper.tzstatus(plan.getXmapbid().toString());
 	        	 
 	        }
        	 
        	 
        }
       
        return ResponseFormat.retParam(1,200,null);
	}
	
}
