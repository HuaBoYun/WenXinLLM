package com.huabo.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.dto.TblYqnsProjectWeerklyOperationDto;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.ImplementPlanTeamEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditOverseeRecordsEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsProjectWeerklyEntity;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.mapper.EnginAuditProjectMapper;
import com.huabo.audit.oracle.mapper.FundAuditProjectMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.ProjectWeeklyMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.service.ProjectWeerklyService;
import com.huabo.audit.util.PageInfoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Service
public class ProjectWeerklyServiceImpl implements ProjectWeerklyService {

    @Autowired
    private ProjectWeeklyMapper projectWeeklyMapper;

    @Autowired
    private ImplementPlanMapper implementPlanMapper;
    
    @Resource
    private FundAuditProjectMapper fundAuditProjectMapper;
    
    
    @Resource
    private EnginAuditProjectMapper enginAuditProjectMapper;

	@Resource
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;
	
	 @Resource
	 private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;
    

    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize,TblYqnsProjectWeerklyEntity week,String ids) throws Exception {

        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        PageInfo<TblYqnsProjectWeerklyEntity> pageInfo;
        com.huabo.audit.util.PageInfo<TblYqnsProjectWeerklyEntity> info = new com.huabo.audit.util.PageInfo<>();
        QueryWrapper<TblYqnsProjectWeerklyEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
            queryWrapper.and(q -> q.eq("CREATORID", staff.getStaffid()).or().inSql("CREATORID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
            queryWrapper.and(q -> q.eq("CREATORID", staff.getStaffid()));
        }
//        if(StringUtils.isNotBlank(entity.getProjectName())) {
//        	query.like(TblYqnsAuditOverseeRecordsEntity::getProjectName, entity.getProjectName());
//        }
        if (StringUtils.isNotBlank(ids)) {
        	queryWrapper.and(q -> q.in("ID", ids));
        }
        //倒序
        queryWrapper.orderByDesc(true, "WEEKDATE");
        pageInfo = PageMethod.startPage(pageNumber, pageSize)
                .doSelectPageInfo(() -> projectWeeklyMapper.selectList(queryWrapper));
        for (TblYqnsProjectWeerklyEntity projectWeerklyEntity : pageInfo.getList()) {
            projectWeerklyEntity.setImplementPlanEntities(implementPlanMapper.selectById(projectWeerklyEntity.getImplementId().toString()));
            ImplementPlanEntity implementPlanEntities = projectWeerklyEntity.getImplementPlanEntities();
            if(implementPlanEntities!=null && implementPlanEntities.getZykstype()!=null && !implementPlanEntities.getZykstype().equals("基建")) {
	        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
	                    .eq("DELETED", 0)
	                    .eq("ID", implementPlanEntities.getXmapbid()));
	        	if(fund!=null) {
	        		TblYqnsXmdq xmqd=new TblYqnsXmdq();
		        	xmqd.setGljhxmid(fund.getGljhxmid());
		        	xmqd.setPlanid(fund.getPlanid());
		        	xmqd.setPlanname(fund.getPlanname());
		        	xmqd.setGljhxmlx(fund.getGljhxmlx());
		        	xmqd.setZsstaffid(fund.getApproverId());
		        	xmqd.setZsname(fund.getApprover());
		        	xmqd.setSiteEndTime(fund.getXcendtime());
		        	xmqd.setXmname(fund.getName());
		        	xmqd.setAssistApprover(fund.getAssistApprover());
		         	xmqd.setAssistApproverId(fund.getAssistApproverId());
		         	xmqd.setFzzStafffId(fund.getFzzStafffId());
		         	xmqd.setFzzName(fund.getFzzName());
		         	xmqd.setSsorgname(fund.getExePhraseUnit());
		         	xmqd.setSsorgid(fund.getExePhraseUnitId());
		         	xmqd.setXcendtime(fund.getXcendtime());
		         	xmqd.setXcsrarttime(fund.getXcsrarttime());
		         	xmqd.setGroupLeader(fund.getGroupLeader());
		         	xmqd.setAuditGroup(fund.getAuditGroup());
		         	implementPlanEntities.setXmqd(xmqd);
		         	
	        	}
	        	
	        }
	        if(implementPlanEntities!=null && implementPlanEntities.getZykstype()!=null && implementPlanEntities.getZykstype().equals("基建")) {
	        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
	                     .eq("DELETED", 0)
	                     .eq("ID", implementPlanEntities.getXmapbid()));
	        	 if(endin!=null) {
	        		 TblYqnsXmdq xmqd=new TblYqnsXmdq();
			         	xmqd.setGljhxmid(endin.getGljhxmid());
			         	xmqd.setPlanid(endin.getPlanid()); 
			         	xmqd.setPlanname(endin.getPlanname());
			         	xmqd.setGljhxmlx(endin.getGljhxmlx());
			         	xmqd.setZsstaffid(endin.getApproverId());
			         	xmqd.setZsname(endin.getApprover());
			         	xmqd.setSiteEndTime(endin.getXcendtime());
			         	xmqd.setXmname(endin.getName());
			         	xmqd.setAssistApprover(endin.getAssistApprover());
			         	xmqd.setAssistApproverId(endin.getAssistApproverId());
			         	xmqd.setFzzStafffId(endin.getFzzStafffId());
			         	xmqd.setFzzName(endin.getFzzName());
			         	xmqd.setSsorgname(endin.getExePhraseUnit());
			         	xmqd.setSsorgid(endin.getExePhraseUnitId());
			         	xmqd.setXcendtime(endin.getXcendtime());
			         	xmqd.setXcsrarttime(endin.getXcsrarttime());
			         	xmqd.setGroupLeader(endin.getGroupLeader());
			         	xmqd.setAuditGroup(endin.getAuditGroup());
			         	implementPlanEntities.setXmqd(xmqd);
	        	 }
	        	 
	        }
	        
//	        if(projectWeerklyEntity.getImplementId()!=null) {
//	        	List<ImplementPlanTeamEntity> list = implementPlanMapper.selectTeamsById(projectWeerklyEntity.getImplementId());
//	        	if(list!=null && list.size()>0) {
//	        		 implementPlanEntities.setTeams(list);
//	        	}
//	        	
//	        }
	        
           
        }

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getList());

        // 构建返回值条件
        info.setCurrentPage(pageInfo.getPageNum());
        info.setPageSize(pageInfo.getPageSize());

        info.setTotalRecord((int) pageInfo.getTotal());
        info.setTlist(pageInfo.getList());
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("pageInfo", info);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    
    //获取当前实施项目；
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        }
        return implementPlanMapper.selectById(projectId.toString());
    }
    
    @Override
    public JsonBean saveEntity(String token, TblYqnsProjectWeerklyEntity weerklyEntity) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        
        if(weerklyEntity.getImplementId()==null) {
        	 //==查询当前实施的项目！
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp == null) {
                return ResponseFormat.retParam(0,30003,null);
            }
        	weerklyEntity.setImplementId(tnp.getId());
        }
        
        weerklyEntity.setCreatorId(staff.getStaffid());
        weerklyEntity.setCreatorName(staff.getRealname());
        weerklyEntity.setId(RandomUtil.uuBigDecimalId());
        projectWeeklyMapper.insert(weerklyEntity);
        return ResponseFormat.retParam(1,200,weerklyEntity);
    }

    @Override
    public JsonBean updateEntity(String token, TblYqnsProjectWeerklyEntity weerklyEntity) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        projectWeeklyMapper.updateById(weerklyEntity);
        return ResponseFormat.retParam(1,200,weerklyEntity);
    }

    @Override
    public JsonBean deleteone(String token, BigDecimal id) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        projectWeeklyMapper.deleteById(id);
        return ResponseFormat.retParam(1,200,null);
    }


    @Override
    public JsonBean getone(String token, BigDecimal id) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        TblYqnsProjectWeerklyEntity weerklyEntity = projectWeeklyMapper.selectById(id);

        weerklyEntity.setImplementPlanEntities(implementPlanMapper.selectById(weerklyEntity.getImplementId().toString()));
        ImplementPlanEntity implementPlanEntities = weerklyEntity.getImplementPlanEntities();
        if(implementPlanEntities!=null && implementPlanEntities.getZykstype()!=null && !implementPlanEntities.getZykstype().equals("基建")) {
        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
                    .eq("DELETED", 0)
                    .eq("ID", implementPlanEntities.getXmapbid()));
        	if(fund!=null) {
        		TblYqnsXmdq xmqd=new TblYqnsXmdq();
	        	xmqd.setGljhxmid(fund.getGljhxmid());
	        	xmqd.setPlanid(fund.getPlanid());
	        	xmqd.setPlanname(fund.getPlanname());
	        	xmqd.setGljhxmlx(fund.getGljhxmlx());
	        	xmqd.setZsstaffid(fund.getApproverId());
	        	xmqd.setZsname(fund.getApprover());
	        	xmqd.setSiteEndTime(fund.getXcendtime());
	        	xmqd.setXmname(fund.getName());
	        	xmqd.setAssistApprover(fund.getAssistApprover());
	         	xmqd.setAssistApproverId(fund.getAssistApproverId());
	         	xmqd.setFzzStafffId(fund.getFzzStafffId());
	         	xmqd.setFzzName(fund.getFzzName());
	         	xmqd.setSsorgname(fund.getExePhraseUnit());
	         	xmqd.setSsorgid(fund.getExePhraseUnitId());
	         	xmqd.setXcendtime(fund.getXcendtime());
	         	xmqd.setXcsrarttime(fund.getXcsrarttime());
	         	xmqd.setGroupLeader(fund.getGroupLeader());
	         	xmqd.setAuditGroup(fund.getAuditGroup());
	         	implementPlanEntities.setXmqd(xmqd);

        	}

        }
        if(implementPlanEntities!=null && implementPlanEntities.getZykstype()!=null && implementPlanEntities.getZykstype().equals("基建")) {
        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
                     .eq("DELETED", 0)
                     .eq("ID", implementPlanEntities.getXmapbid()));
        	 if(endin!=null) {
        		 TblYqnsXmdq xmqd=new TblYqnsXmdq();
		         	xmqd.setGljhxmid(endin.getGljhxmid());
		         	xmqd.setPlanid(endin.getPlanid());
		         	xmqd.setPlanname(endin.getPlanname());
		         	xmqd.setGljhxmlx(endin.getGljhxmlx());
		         	xmqd.setZsstaffid(endin.getApproverId());
		         	xmqd.setZsname(endin.getApprover());
		         	xmqd.setSiteEndTime(endin.getXcendtime());
		         	xmqd.setXmname(endin.getName());
		         	xmqd.setAssistApprover(endin.getAssistApprover());
		         	xmqd.setAssistApproverId(endin.getAssistApproverId());
		         	xmqd.setFzzStafffId(endin.getFzzStafffId());
		         	xmqd.setFzzName(endin.getFzzName());
		         	xmqd.setSsorgname(endin.getExePhraseUnit());
		         	xmqd.setSsorgid(endin.getExePhraseUnitId());
		         	xmqd.setXcendtime(endin.getXcendtime());
		         	xmqd.setXcsrarttime(endin.getXcsrarttime());
		         	xmqd.setGroupLeader(endin.getGroupLeader());
		         	xmqd.setAuditGroup(endin.getAuditGroup());
		         	implementPlanEntities.setXmqd(xmqd);
        	 }

        }
        implementPlanEntities.setTeams(implementPlanMapper.selectTeamsById(weerklyEntity.getImplementId()));

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(weerklyEntity);

        return ResponseFormat.retParam(1,200,weerklyEntity);
    }


    @Override
    public List<TblYqnsProjectWeerklyEntity> findAlldc(String token, Integer pageNumber, Integer pageSize,TblYqnsProjectWeerklyEntity week,String ids) throws Exception {

        TblStaffUtil staff = userProvider.get();
        QueryWrapper<TblYqnsProjectWeerklyEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(staff.getDeptIds())) {
            queryWrapper.and(q -> q.eq("CREATORID", staff.getStaffid()).or().inSql("CREATORID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
        }else {
            queryWrapper.and(q -> q.eq("CREATORID", staff.getStaffid()));
        }
//        if(StringUtils.isNotBlank(entity.getProjectName())) {
//        	query.like(TblYqnsAuditOverseeRecordsEntity::getProjectName, entity.getProjectName());
//        }
        if (StringUtils.isNotBlank(ids)) {
        	queryWrapper.and(q -> q.inSql("ID", ids));
        }
        //倒序
        queryWrapper.orderByDesc(true, "CREATORDATE");

        List<TblYqnsProjectWeerklyEntity> list= projectWeeklyMapper.selectList(queryWrapper);

//        pageInfo = PageMethod.startPage(pageNumber, pageSize)
//                .doSelectPageInfo(() -> projectWeeklyMapper.selectList(queryWrapper));
        for (TblYqnsProjectWeerklyEntity projectWeerklyEntity : list) {
            projectWeerklyEntity.setImplementPlanEntities(implementPlanMapper.selectById(projectWeerklyEntity.getImplementId().toString()));
            ImplementPlanEntity implementPlanEntities = projectWeerklyEntity.getImplementPlanEntities();
            if(implementPlanEntities!=null && implementPlanEntities.getZykstype()!=null && !implementPlanEntities.getZykstype().equals("基建")) {
	        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
	                    .eq("DELETED", 0)
	                    .eq("ID", implementPlanEntities.getXmapbid()));
	        	if(fund!=null) {
	        		TblYqnsXmdq xmqd=new TblYqnsXmdq();
		        	xmqd.setGljhxmid(fund.getGljhxmid());
		        	xmqd.setPlanid(fund.getPlanid());
		        	xmqd.setPlanname(fund.getPlanname());
		        	xmqd.setGljhxmlx(fund.getGljhxmlx());
		        	xmqd.setZsstaffid(fund.getApproverId());
		        	xmqd.setZsname(fund.getApprover());
		        	xmqd.setSiteEndTime(fund.getXcendtime());
		        	xmqd.setXmname(fund.getName());
		        	xmqd.setAssistApprover(fund.getAssistApprover());
		         	xmqd.setAssistApproverId(fund.getAssistApproverId());
		         	xmqd.setFzzStafffId(fund.getFzzStafffId());
		         	xmqd.setFzzName(fund.getFzzName());
		         	xmqd.setSsorgname(fund.getExePhraseUnit());
		         	xmqd.setSsorgid(fund.getExePhraseUnitId());
		         	xmqd.setXcendtime(fund.getXcendtime());
		         	xmqd.setXcsrarttime(fund.getXcsrarttime());
		         	xmqd.setGroupLeader(fund.getGroupLeader());
		         	xmqd.setAuditGroup(fund.getAuditGroup());
		         	implementPlanEntities.setXmqd(xmqd);

	        	}

	        }
	        if(implementPlanEntities!=null && implementPlanEntities.getZykstype()!=null && implementPlanEntities.getZykstype().equals("基建")) {
	        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
	                     .eq("DELETED", 0)
	                     .eq("ID", implementPlanEntities.getXmapbid()));
	        	 if(endin!=null) {
	        		 TblYqnsXmdq xmqd=new TblYqnsXmdq();
			         	xmqd.setGljhxmid(endin.getGljhxmid());
			         	xmqd.setPlanid(endin.getPlanid());
			         	xmqd.setPlanname(endin.getPlanname());
			         	xmqd.setGljhxmlx(endin.getGljhxmlx());
			         	xmqd.setZsstaffid(endin.getApproverId());
			         	xmqd.setZsname(endin.getApprover());
			         	xmqd.setSiteEndTime(endin.getXcendtime());
			         	xmqd.setXmname(endin.getName());
			         	xmqd.setAssistApprover(endin.getAssistApprover());
			         	xmqd.setAssistApproverId(endin.getAssistApproverId());
			         	xmqd.setFzzStafffId(endin.getFzzStafffId());
			         	xmqd.setFzzName(endin.getFzzName());
			         	xmqd.setSsorgname(endin.getExePhraseUnit());
			         	xmqd.setSsorgid(endin.getExePhraseUnitId());
			         	xmqd.setXcendtime(endin.getXcendtime());
			         	xmqd.setXcsrarttime(endin.getXcsrarttime());
			         	xmqd.setGroupLeader(endin.getGroupLeader());
			         	xmqd.setAuditGroup(endin.getAuditGroup());
			         	implementPlanEntities.setXmqd(xmqd);
	        	 }

	        }
            implementPlanEntities.setTeams(implementPlanMapper.selectTeamsById(projectWeerklyEntity.getImplementId()));
        }
        return list;
    }


	/**
	 * 审计项目运行情况汇总
	 * @param token
	 * @param week
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Override
	public JsonBean findAuditProjectOperationStatusAllList(String token, Integer pageNumber, Integer pageSize,TblYqnsProjectWeerklyEntity week,String ids) throws Exception {


		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		PageInfo<TblYqnsProjectWeerklyEntity> pageInfo;
		QueryWrapper<TblYqnsProjectWeerklyEntity> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(staff.getDeptIds())) {
			queryWrapper.and(q -> q.eq("CREATORID", staff.getStaffid()).or().inSql("CREATORID", "SELECT DISTINCT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ("+staff.getDeptIds()+") AND ORGID = "+staff.getCurrentOrg().getOrgid()));
		}else {
			queryWrapper.and(q -> q.eq("CREATORID", staff.getStaffid()));
		}
//        if(StringUtils.isNotBlank(entity.getProjectName())) {
//        	query.like(TblYqnsAuditOverseeRecordsEntity::getProjectName, entity.getProjectName());
//        }
		if (StringUtils.isNotBlank(ids)) {
			queryWrapper.and(q -> q.in("ID", ids));
		}
		//倒序
		queryWrapper.orderByDesc(true, "CREATORDATE");
		pageInfo = PageMethod.startPage(1, 5000000)
				.doSelectPageInfo(() -> projectWeeklyMapper.selectList(queryWrapper));
		for (TblYqnsProjectWeerklyEntity projectWeerklyEntity : pageInfo.getList()) {
			projectWeerklyEntity.setImplementPlanEntities(implementPlanMapper.selectById(projectWeerklyEntity.getImplementId().toString()));
			ImplementPlanEntity implementPlanEntities = projectWeerklyEntity.getImplementPlanEntities();
			if(implementPlanEntities!=null && implementPlanEntities.getZykstype()!=null && !implementPlanEntities.getZykstype().equals("基建")) {
				TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
						.eq("DELETED", 0)
						.eq("ID", implementPlanEntities.getXmapbid()));
				if(fund!=null) {
					TblYqnsXmdq xmqd=new TblYqnsXmdq();
					xmqd.setGljhxmid(fund.getGljhxmid());
					xmqd.setPlanid(fund.getPlanid());
					xmqd.setPlanname(fund.getPlanname());
					xmqd.setGljhxmlx(fund.getGljhxmlx());
					xmqd.setZsstaffid(fund.getApproverId());
					xmqd.setZsname(fund.getApprover());
					xmqd.setSiteEndTime(fund.getXcendtime());
					xmqd.setXmname(fund.getName());
					xmqd.setAssistApprover(fund.getAssistApprover());
					xmqd.setAssistApproverId(fund.getAssistApproverId());
					xmqd.setFzzStafffId(fund.getFzzStafffId());
					xmqd.setFzzName(fund.getFzzName());
					xmqd.setSsorgname(fund.getExePhraseUnit());
					xmqd.setSsorgid(fund.getExePhraseUnitId());
					xmqd.setXcendtime(fund.getXcendtime());
					xmqd.setXcsrarttime(fund.getXcsrarttime());
					xmqd.setGroupLeader(fund.getGroupLeader());
					xmqd.setAuditGroup(fund.getAuditGroup());
					implementPlanEntities.setXmqd(xmqd);

				}

			}
			if(implementPlanEntities!=null && implementPlanEntities.getZykstype()!=null && implementPlanEntities.getZykstype().equals("基建")) {
				TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
						.eq("DELETED", 0)
						.eq("ID", implementPlanEntities.getXmapbid()));
				if(endin!=null) {
					TblYqnsXmdq xmqd=new TblYqnsXmdq();
					xmqd.setGljhxmid(endin.getGljhxmid());
					xmqd.setPlanid(endin.getPlanid());
					xmqd.setPlanname(endin.getPlanname());
					xmqd.setGljhxmlx(endin.getGljhxmlx());
					xmqd.setZsstaffid(endin.getApproverId());
					xmqd.setZsname(endin.getApprover());
					xmqd.setSiteEndTime(endin.getXcendtime());
					xmqd.setXmname(endin.getName());
					xmqd.setAssistApprover(endin.getAssistApprover());
					xmqd.setAssistApproverId(endin.getAssistApproverId());
					xmqd.setFzzStafffId(endin.getFzzStafffId());
					xmqd.setFzzName(endin.getFzzName());
					xmqd.setSsorgname(endin.getExePhraseUnit());
					xmqd.setSsorgid(endin.getExePhraseUnitId());
					xmqd.setXcendtime(endin.getXcendtime());
					xmqd.setXcsrarttime(endin.getXcsrarttime());
					xmqd.setGroupLeader(endin.getGroupLeader());
					xmqd.setAuditGroup(endin.getAuditGroup());
					implementPlanEntities.setXmqd(xmqd);
				}

			}

//	        if(projectWeerklyEntity.getImplementId()!=null) {
//	        	List<ImplementPlanTeamEntity> list = implementPlanMapper.selectTeamsById(projectWeerklyEntity.getImplementId());
//	        	if(list!=null && list.size()>0) {
//	        		 implementPlanEntities.setTeams(list);
//	        	}
//
//	        }


		}
		com.huabo.audit.util.PageInfo<TblYqnsProjectWeerklyOperationDto> info = new com.huabo.audit.util.PageInfo<>();
		// 构建返回值条件
		info.setCurrentPage(pageInfo.getPageNum());
		info.setPageSize(pageInfo.getPageSize());
		List<TblYqnsProjectWeerklyOperationDto> toList =	groupAndSortEntitiesByImplementPlan(pageInfo.getList());
		info.setTlist(toList);
		info.setTotalRecord(toList.size());
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("pageInfo", info);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	/**
	 * 按 ImplementPlan 分组并排序 Entity 列表
	 *
	 * @param entities 包含 Entity 的列表
	 * @return List<PlanWithReports> 分组后的结果
	 */
	public static List<TblYqnsProjectWeerklyOperationDto> groupAndSortEntitiesByImplementPlan(List<TblYqnsProjectWeerklyEntity> entities) {
		// 按 ImplementPlan 分组
		Map<ImplementPlanEntity, List<TblYqnsProjectWeerklyEntity>> grouped = entities.stream()
				.filter(entity -> entity.getImplementPlanEntities() != null) // 过滤掉 null 的记录
				.collect(Collectors.groupingBy(TblYqnsProjectWeerklyEntity::getImplementPlanEntities));
		// 构建 PlanWithReports 列表
		return grouped.entrySet().stream()
				.map(entry -> {
					ImplementPlanEntity plan = entry.getKey();
					List<TblYqnsProjectWeerklyEntity> sortedEntities = entry.getValue();
					sortedEntities.sort(Comparator.comparing(TblYqnsProjectWeerklyEntity::getWeekDate)); // 按 weekDate 排序
					TblYqnsProjectWeerklyOperationDto planWithReports = new TblYqnsProjectWeerklyOperationDto();
					planWithReports.setImplementPlanEntity(plan);
					planWithReports.setWeerklyList(sortedEntities);

					return planWithReports;
				})
				.collect(Collectors.toList());
	}
    
}
