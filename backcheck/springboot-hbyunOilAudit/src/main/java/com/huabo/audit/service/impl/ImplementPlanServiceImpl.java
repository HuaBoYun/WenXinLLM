package com.huabo.audit.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.ImplementPlanTeamEntity;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.oracle.entity.TblNbsjAuthorizationEntity;
import com.huabo.audit.oracle.entity.TblNbsjOperateEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjTeamstaffEntity;
import com.huabo.audit.oracle.entity.TblNbsjTempleteEntity;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.entity.TblTargetTypeEntity;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsOperate;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.mapper.EnginAuditProjectMapper;
import com.huabo.audit.oracle.mapper.FundAuditProjectMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblAduitProGramMapper;
import com.huabo.audit.oracle.mapper.TblNbsjAuthorizationMapper;
import com.huabo.audit.oracle.mapper.TblNbsjOperateMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTeamstaffMapper;
import com.huabo.audit.oracle.mapper.TblNbsjTempleteMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.mapper.TblTargetTypeMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOperateMapper;
import com.huabo.audit.service.ImplementPlanService;
import com.huabo.audit.service.TblAduitProGramService;
import com.huabo.audit.service.TblNbsjAuthorizationService;
import com.huabo.audit.service.TblTargetTypeService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.huabo.audit.util.PageInfoUtil;
import com.huabo.audit.vo.result.FlowTaskInfo;
import com.huabo.audit.vo.result.QualityParam;
/**
 * @author Rui
 * @ClassName  ImplementPlanServiceImpl
 * @Description
 * @DATE 2023/10/27
 */

@Service
public class ImplementPlanServiceImpl implements  ImplementPlanService {

    @Autowired
    private  ImplementPlanMapper implementPlanMapper;

    @Autowired
    private TblNbsjTempleteMapper tblNbsjTempleteMapper;
    
    
    @Resource
    private TblTargetTypeMapper tblTargetTypeMapper;
    
    @Resource
    private TblTargetTypeService tblTargetTypeService;
    
    @Resource
    private TblAduitProGramService tblAduitProGramService;
    
    @Resource
    private TblAduitProGramMapper tblAduitProGramMapper;
    
    
    @Resource
    private TblNbsjOperateMapper tblNbsjOperateMapper;
    
    @Resource
    private TblNbsjAuthorizationService tblNbsjAuthorizationService;
     
    @Resource 
    private TblNbsjAuthorizationMapper tblNbsjAuthorizationMapper;
    
    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;
    
    @Resource
    private TblNbsjTeamstaffMapper tblNbsjTeamstaffMapper;
    
    
    @Resource
    private FundAuditProjectMapper fundAuditProjectMapper;
    
    
    @Resource
    private EnginAuditProjectMapper enginAuditProjectMapper;
    
    
    @Resource
    private TblOrganizationMapper tblOrganizationMapper;
    
    @Resource
    private TblStaffMapper  tblStaffMapper;

	@Resource
	private ReservePropertyService reservePropertyService;
	
	@Resource
    private UserProvider userProvider;
	
	@Resource
	private TblYqnsOperateMapper tblYqnsOperateMapper;
    
    @Override
    public JsonBean findRwfpAll(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId, String projectName, String planStarttime, String planEndtime) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         ImplementPlanEntity implementPlanEntity = new  ImplementPlanEntity();

        if(projectOrderId == null){
            implementPlanEntity.setProjectOrderId(user.getStaffid());
        }

        if(StringUtil.isNotEmpty(projectName)){
            implementPlanEntity.setProjectName(projectName);
        }

        if(StringUtil.isNotEmpty(planStarttime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = simpleDateFormat.parse(planStarttime);
            implementPlanEntity.setPlanStarttime(new java.sql.Date(startTime.getTime()));
        }

        if(StringUtil.isNotEmpty(planEndtime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date endTime = simpleDateFormat.parse(planEndtime);
            implementPlanEntity.setPlanEndtime(new java.sql.Date(endTime.getTime()));
        }

        Page<ImplementPlanEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> implementPlanMapper.selectByrwfpEntity(implementPlanEntity));
		PageInfo< ImplementPlanEntity> pageInfo = new PageInfoUtil< ImplementPlanEntity>().parsePageInfo(page);

		if(pageInfo.getTlist()!=null && pageInfo.getTlist().size()>0) {
			for (ImplementPlanEntity plan : pageInfo.getTlist()) {
				if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
		        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
		                    .eq("DELETED", 0)
		                    .eq("ID", plan.getXmapbid()));
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
			         	xmqd.setGroupLeader(fund.getGroupLeader());
			         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
			        	plan.setXmqd(xmqd);
		        	}
		        	
		        }
		        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
		        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
		                     .eq("DELETED", 0)
		                     .eq("ID", plan.getXmapbid()));
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
				         	xmqd.setGroupLeader(endin.getGroupLeader());
				         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
				         	plan.setXmqd(xmqd);
		        	 }
		        	 
		        }
			}
			
		}

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1,200,pageInfo);
    }
    
    
    
    @Override
    public JsonBean findAll(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId, String projectName, String planStarttime, String planEndtime, BigDecimal staffId, Integer xmnd,String spzt,String xctype) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         ImplementPlanEntity implementPlanEntity = new  ImplementPlanEntity();

        

        if(projectOrderId != null){
            implementPlanEntity.setProjectOrderId(projectOrderId);
        }

        if(StringUtil.isNotEmpty(projectName)){
            implementPlanEntity.setProjectName(projectName);
        }
        
        if(StringUtil.isNotEmpty(xctype)){
            implementPlanEntity.setXctype(xctype);
        }
        
        if(StringUtil.isNotEmpty(spzt)){
            implementPlanEntity.setSpzt(spzt);
        }

        if(StringUtil.isNotEmpty(planStarttime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = simpleDateFormat.parse(planStarttime);
            implementPlanEntity.setPlanStarttime(new java.sql.Date(startTime.getTime())); 
        }

        if(StringUtil.isNotEmpty(planEndtime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date endTime = simpleDateFormat.parse(planEndtime);
            implementPlanEntity.setPlanEndtime(new java.sql.Date(endTime.getTime()));
        }
        Page<ImplementPlanEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> implementPlanMapper.selectByEntity(implementPlanEntity,staffId,xmnd,user));
        if(staffId==null) {
        	page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> implementPlanMapper.selectByEntity(implementPlanEntity,user.getStaffid(),xmnd,user));
        }
        
		PageInfo<ImplementPlanEntity> pageInfo = new PageInfoUtil< ImplementPlanEntity>().parsePageInfo(page);
		if(pageInfo.getTlist()!=null && pageInfo.getTlist().size()>0) {
			for (ImplementPlanEntity plan : pageInfo.getTlist()) {
				if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
		        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
		                    .eq("DELETED", 0)
		                    .eq("ID", plan.getXmapbid()));
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
			         	xmqd.setGroupLeader(fund.getGroupLeader());
			         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
			        	plan.setXmqd(xmqd);
		        	}
		        	
		        }
		        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
		        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
		                     .eq("DELETED", 0)
		                     .eq("ID", plan.getXmapbid()));
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
				         	xmqd.setGroupLeader(endin.getGroupLeader());
				         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
				         	plan.setXmqd(xmqd);
		        	 }
		        	 
		        }
			}
			
		}
		
		pageInfo.setCurrProjectId(tblNbsjStaffSelectMapper.selectProjectIdByStaffId(user.getStaffid()));

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1,200,pageInfo);
    }

    @Override
    public JsonBean findById(String id) throws Exception{

        ImplementPlanEntity plan = implementPlanMapper.selectById(id);

			if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
	        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
	                    .eq("DELETED", 0)
	                    .eq("ID", plan.getXmapbid()));
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
		         	xmqd.setGroupLeader(fund.getGroupLeader());
		         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
		        	plan.setXmqd(xmqd);
	        	}
	        	
	        }
	        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
	        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
	                     .eq("DELETED", 0)
	                     .eq("ID", plan.getXmapbid()));
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
			         	xmqd.setGroupLeader(endin.getGroupLeader());
			         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
			         	plan.setXmqd(xmqd);
	        	 }
	        	 
	        }

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(plan);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", plan);
        return ResponseFormat.retParam(1,200,resultMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean updateEntity( ImplementPlanEntity implementPlanEntity) throws Exception{
    	if(implementPlanEntity.getTempId()!=null){
    		
			// 如果审计模板再次修改
			//if(!tempid.equals(protempid)){
				TblNbsjTempleteEntity templete = tblNbsjTempleteMapper.findbyid(implementPlanEntity.getTempId()+"");//tblNbsjTempleteService.get(new BigDecimal(tempid));
				TblNbsjTempleteEntity temp = new TblNbsjTempleteEntity();
				temp.setTempType("3");
				temp.setCreateDate(templete.getCreateDate());
				temp.setTempleteCode(templete.getTempleteCode());
				temp.setTempleteDesc(templete.getTempleteDesc());
				Set<TblOrganization> orgs = templete.getOrganizations();
				if (orgs != null && orgs.size() > 0) {
					for (TblOrganization org : orgs) {
						temp.getOrganizations().add(org);
					}
				}
				temp.setStaffId(templete.getStaffId());
				temp.setTempleteType(templete.getTempleteType());
				temp.setUpdateStaffId(templete.getUpdateStaffId());
				temp.setUpdateDate(templete.getUpdateDate());
				temp.setStatus(templete.getStatus());
				temp.setTempleteName(templete.getTempleteName());
//				tblNbsjTempleteService.save(temp);
//				tblNbsjTempleteMapper.insertEntity(temp);
				tblNbsjTempleteMapper.insertEntity(temp);
				// if(!tempid.equals(tblnbsjProject.getTbltemplete().getTempleteId().toString())){
				List<TblTargetTypeEntity> list = tblTargetTypeService.findByAllMB(implementPlanEntity.getTempId()+"");
				if (list != null && list.size() > 0) {
					for (TblTargetTypeEntity tblTargetType : list) {
						TblTargetTypeEntity target = new TblTargetTypeEntity();
						target.setCreateTime(tblTargetType.getCreateTime());
						target.setNbsjTemplete(temp);
						target.setParentId(tblTargetType.getParentId());
						target.setStatus(TblTargetTypeEntity.TEMP_NUMBER);//TEMP_NUMBER 0 ;ZY_NUMBER 1
						target.setTargetDesc(tblTargetType.getTargetDesc());
						target.setTargetName(tblTargetType.getTargetName());
//						tblTargetTypeService.save(target);
//						tblTargetTypeMapper.insertEntity(target);
						tblTargetTypeMapper.insertEntity(target);
						List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
						if (grams != null && grams.size() > 0) {
							for (TblAduitProGramEntity gram : grams) {
								TblAduitProGramEntity prog = new TblAduitProGramEntity();
								prog.setBioData(gram.getBioData());
								prog.setTargetId(target.getTargetId());
								prog.setControl(gram.getControl());
								prog.setBusinessType(gram.getBusinessType());
								prog.setStatus(TblAduitProGramEntity.TEMP_NUMBER);
								prog.setCreateTime(gram.getCreateTime());
								prog.setNbsjTemplete(temp);
								prog.setTempId(temp.getTempleteId());
								prog.setRiskPoint(gram.getRiskPoint());
								prog.setRiskSource(gram.getRiskSource());
								prog.setSuditProcess(gram.getSuditProcess());
								prog.setUpdateTime(gram.getUpdateTime());
//								tblAduitProGramService.save(prog);
								//tblAduitProGramMapper.insertEntity(prog);
								prog.setProgramId(RandomUtil.uuBigDecimalId());
								tblAduitProGramMapper.insertEntity(prog);
							}
						}
						capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
					}
				}
				//删除之前分配的任务
				//删除任务
				
					 List<TblNbsjAuthorizationEntity> nbsjauthorizatoin = this.tblNbsjAuthorizationService.getByProjectId(implementPlanEntity.getId());
					 if(null!=nbsjauthorizatoin&&nbsjauthorizatoin.size()>0) {
						 for (TblNbsjAuthorizationEntity tblNbsjAuthorization : nbsjauthorizatoin) {
							 if(null!=nbsjauthorizatoin) {
								 List<TblNbsjOperateEntity> listoper = tblNbsjOperateMapper.findByAuthId(tblNbsjAuthorization.getAuthId().intValue());//tblNbsjOperateService.findByAuthId(tblNbsjAuthorization.getAuthId());
								 TblNbsjOperateEntity oper = null;
								 if(listoper!=null && listoper.size()>0){
									 oper = listoper.get(0);
									}
								 tblNbsjOperateMapper.deleteByOpid(oper.getOperateid().intValue());
//								 tblNbsjOperateService.delete(oper);
//								 this.tblNbsjAuthorizationService.delete(tblNbsjAuthorization);
								 this.tblNbsjAuthorizationMapper.deleteByAuthid(tblNbsjAuthorization.getAuthId().intValue());
							 }
						 }
					 }
					 implementPlanEntity.setTempId(temp.getTempleteId());
			//}
		
    	}
    	if(implementPlanEntity.getAuditOrgName()!=null && ( implementPlanEntity.getAuditOrgId()==null || implementPlanEntity.getAuditOrgId().equals(""))) {
        	Long orgid = tblOrganizationMapper.findNameByname(implementPlanEntity.getAuditOrgName());
        	if(orgid!=null) {
        		implementPlanEntity.setAuditOrgId(orgid.toString());
        	}
        }
    	implementPlanEntity.setUpdatedate(new Date());
        implementPlanMapper.updateEntity(implementPlanEntity);
        implementPlanMapper.deleteImplementPlanTeamByIds(implementPlanEntity.getId()+"");
        implementPlanMapper.deleteAttachmentByIds(implementPlanEntity.getId()+"");
        //处理关联表关系
        if(implementPlanEntity.getTeams()!= null && implementPlanEntity.getTeams().size() > 0){
            for (ImplementPlanTeamEntity team : implementPlanEntity.getTeams()){
                implementPlanMapper.insertImplementPlanTeamWidthId(implementPlanEntity.getId(), team);
                
                String ids=implementPlanEntity.getProjectOrderId()+","+team.getFzzstaffid()+","+team.getTeamLeaderId()+","+team.getTeamMembersIds();
                tblStaffMapper.updateStatus(ids);
                
                
            }
        }
        //处理关联附件表关系
        if(StringUtil.isNotEmpty(implementPlanEntity.getAttIds())){
            String[] ids = implementPlanEntity.getAttIds().split(",");
            for (String attId : ids){
                implementPlanMapper.insertAttachmentsWidthId(implementPlanEntity.getId(),attId);
            }
        }
        return ResponseFormat.retParam(1,200,implementPlanEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean saveEntity(String token,  ImplementPlanEntity implementPlanEntity) throws Exception{
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);  
        }
        if(implementPlanEntity.getAuditOrgName()!=null && ( implementPlanEntity.getAuditOrgId()==null || implementPlanEntity.getAuditOrgId().equals(""))) {
        	Long orgid = tblOrganizationMapper.findNameByname(implementPlanEntity.getAuditOrgName());
        	if(orgid!=null) {
        		implementPlanEntity.setAuditOrgId(orgid.toString());
        	}
        }
    	if(implementPlanEntity.getTempId()!=null){
            TblNbsjTempleteEntity templete = tblNbsjTempleteMapper.findbyid(implementPlanEntity.getTempId()+"");
            //复制审计模板
            TblNbsjTempleteEntity temp = new TblNbsjTempleteEntity();
            temp.setTempType("3");
            temp.setCreateDate(templete.getCreateDate());
            temp.setTempleteCode(templete.getTempleteCode());
            temp.setTempleteDesc(templete.getTempleteDesc());
            Set<TblOrganization> orgs = templete.getOrganizations();
            if (orgs != null && orgs.size() > 0) {
                for (TblOrganization org : orgs) {
                    temp.getOrganizations().add(org);
                }
            }
            temp.setStaffId(templete.getStaffId());
            temp.setTempleteType(templete.getTempleteType());
            temp.setUpdateStaffId(templete.getUpdateStaffId());
            temp.setUpdateDate(templete.getUpdateDate());
            temp.setStatus(templete.getStatus());
            temp.setTempleteName(templete.getTempleteName());
//				tblNbsjTempleteService.save(temp);
            tblNbsjTempleteMapper.insertEntity(temp);
            List<TblTargetTypeEntity> list = tblTargetTypeService.findByAllMB(implementPlanEntity.getTempId()+"");
			if (list != null && list.size() > 0) {
				for (TblTargetTypeEntity tblTargetType : list) {
					TblTargetTypeEntity target = new TblTargetTypeEntity();
					target.setCreateTime(tblTargetType.getCreateTime());
					target.setNbsjTemplete(temp);
					target.setParentId(tblTargetType.getParentId());
					target.setStatus(TblTargetTypeEntity.TEMP_NUMBER);
					target.setTargetDesc(tblTargetType.getTargetDesc());
					target.setTargetName(tblTargetType.getTargetName());
//					tblTargetTypeService.save(target);
					target.setTargetId(RandomUtil.uuBigDecimalId());
					tblTargetTypeMapper.insertEntity(target);
					List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
					if (grams != null && grams.size() > 0) {
						for (TblAduitProGramEntity gram : grams) {
							TblAduitProGramEntity prog = new TblAduitProGramEntity();
							prog.setBioData(gram.getBioData());
							prog.setTargetId(target.getTargetId());
							prog.setControl(gram.getControl());
							prog.setBusinessType(gram.getBusinessType());
							prog.setStatus(TblAduitProGramEntity.TEMP_NUMBER);
							prog.setCreateTime(gram.getCreateTime());
							prog.setTempId(temp.getTempleteId());
							prog.setRiskPoint(gram.getRiskPoint());
							prog.setRiskSource(gram.getRiskSource());
							prog.setSuditProcess(gram.getSuditProcess()); 
							prog.setUpdateTime(gram.getUpdateTime());
//							tblAduitProGramService.save(prog);
							prog.setProgramId(RandomUtil.uuBigDecimalId());
							tblAduitProGramMapper.insertEntity(prog);
						}
					}
					capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
				}
			}
            
            
			 implementPlanEntity.setTempId(temp.getTempleteId());
            
        }
        implementPlanEntity.setCreatestaffid(staff.getStaffid());
        implementPlanEntity.setCreatedate(new Date());
        implementPlanEntity.setStatus(0);
        implementPlanEntity.setFpStatus(0);//项目任务分配状态
        implementPlanEntity.setUpdateStatus(TblNbsjProject.UPDATEYES);
        implementPlanEntity.setCurrentStatre(TblNbsjProject.NO_SELECT);
        implementPlanEntity.setId(RandomUtil.uuBigDecimalId());
        implementPlanMapper.insertEntity(implementPlanEntity);
        
        //处理关联表关系 
        if(implementPlanEntity.getTeams()!= null && implementPlanEntity.getTeams().size() > 0){
            for (ImplementPlanTeamEntity team : implementPlanEntity.getTeams()){
                implementPlanMapper.insertImplementPlanTeamWidthId(implementPlanEntity.getId(), team);
                
                String ids=implementPlanEntity.getProjectOrderId()+","+team.getFzzstaffid()+","+team.getTeamLeaderId()+","+team.getTeamMembersIds();
                tblStaffMapper.updateStatus(ids);
            }
        }
        //处理关联附件表关系
        if(StringUtil.isNotEmpty(implementPlanEntity.getAttIds())){
            String[] ids = implementPlanEntity.getAttIds().split(",");
            for (String attId : ids){
                implementPlanMapper.insertAttachmentsWidthId(implementPlanEntity.getId(),attId);
            }
        }
        if(implementPlanEntity.getId()!=null && !implementPlanEntity.getZykstype().equals("经责")) {
        	List<TblYqnsOperate> list =tblYqnsOperateMapper.findbyByformname(staff.getStaffid().toString(), "1363",implementPlanEntity.getProjectName());
//        	List<TblYqnsOperate> list = tblYqnsOperateMapper.findbyformidStatusdb(staff.getStaffid().toString(), "1363", implementPlanEntity.getId().toString());
        	if(list!=null && list.size()>0) {
        		for (TblYqnsOperate oper : list) {
        			oper.setFormid(implementPlanEntity.getId());
        			tblYqnsOperateMapper.updateById(oper);
				}
        	}
        }else {
        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
                    .eq("DELETED", 0)
                    .eq("ID", implementPlanEntity.getXmapbid()));
//        	if(fund!=null && fund.getGljhxmlx().equals("23")) {
//        		TblYqnsOperate newoper= new TblYqnsOperate();
//				newoper.setSsmkid("1362");
//				newoper.setSsmk("任务分配");
//				newoper.setRwmc("项目主审分配任务");
//				newoper.setFormid(implementPlanEntity.getId());
//				newoper.setFormname(implementPlanEntity.getProjectName());
//				newoper.setOperid(RandomUtil.uuBigDecimalId());
//			    newoper.setStatus(0);
//			    newoper.setCreatestaffid(staff.getStaffid());
//			    newoper.setCreatename(staff.getRealname());
//			    newoper.setCreatedate(new Date());
//			    newoper.setRwuserid(implementPlanEntity.getZsstaffid().toString());
//			    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
//			    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
//			    tblYqnsOperateMapper.insert(newoper);
//        	}else {
        		List<TblYqnsOperate> list =tblYqnsOperateMapper.findbyByformname(staff.getStaffid().toString(), "1363",implementPlanEntity.getProjectName());
//        		List<TblYqnsOperate> list = tblYqnsOperateMapper.findbyformidStatusdb(staff.getStaffid().toString(), "1363", implementPlanEntity.getId().toString());
            	if(list!=null && list.size()>0) {
            		for (TblYqnsOperate oper : list) {
            			oper.setFormid(implementPlanEntity.getId());
            			tblYqnsOperateMapper.updateById(oper);
    				}
            	}
//        	}
        }
        return ResponseFormat.retParam(1,200,implementPlanEntity);

    }

    
    
    
    public void capyMb(String parentid, TblNbsjTempleteEntity temp, String newparentid) throws Exception {
		List<TblTargetTypeEntity> list = tblTargetTypeService.findByALLPatrnt(parentid);
		if (list != null && list.size() > 0) {
			for (TblTargetTypeEntity tblTargetType : list) {
				TblTargetTypeEntity target = new TblTargetTypeEntity();
				target.setCreateTime(tblTargetType.getCreateTime());
				target.setNbsjTemplete(temp);
				target.setParentId(new BigDecimal(newparentid));
				target.setStatus(TblTargetTypeEntity.ZY_NUMBER);
				target.setTargetDesc(tblTargetType.getTargetDesc());
				target.setTargetName(tblTargetType.getTargetName());
//				tblTargetTypeService.save(target);
//				tblTargetTypeMapper.insertEntity(target);
				target.setTargetId(RandomUtil.uuBigDecimalId());
				tblTargetTypeMapper.insertEntity(target);
				List<TblAduitProGramEntity> grams = tblAduitProGramService.findByALL(tblTargetType.getTargetId().toString());
				if (grams != null && grams.size() > 0) {
					for (TblAduitProGramEntity gram : grams) {
						TblAduitProGramEntity prog = new TblAduitProGramEntity();
						prog.setBioData(gram.getBioData());
						prog.setTargetId(target.getTargetId());
						prog.setBusinessType(gram.getBusinessType());
						prog.setStatus(TblAduitProGramEntity.ZY_NUMBER);
						prog.setCreateTime(gram.getCreateTime());
						prog.setControl(gram.getControl());
						//prog.setIsUseProgram(0);
						prog.setTempId(temp.getTempleteId());
						prog.setRiskPoint(gram.getRiskPoint());
						prog.setRiskSource(gram.getRiskSource());
						prog.setSuditProcess(gram.getSuditProcess());
						prog.setUpdateTime(gram.getUpdateTime());
//						tblAduitProGramService.save(prog);
//						tblAduitProGramMapper.insertEntity(prog);
						prog.setProgramId(RandomUtil.uuBigDecimalId());
						tblAduitProGramMapper.insertEntity(prog);
					}
				}
				capyMb(tblTargetType.getTargetId().toString(), temp, target.getTargetId().toString());
			}
		}
	}
    
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) throws Exception{

        implementPlanMapper.deleteEntity(ids);
        implementPlanMapper.deleteImplementPlanTeamByIds(ids);
        implementPlanMapper.deleteAttachmentByIds(ids);
        
        tblYqnsOperateMapper.deleteoneByfromid(ids);

    }
    
    //获取当前实施项目；
    @Override
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        } 
        ImplementPlanEntity plan = implementPlanMapper.selectById(projectId.toString());

			if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
	        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
	                    .eq("DELETED", 0)
	                    .eq("ID", plan.getXmapbid()));
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
		         	xmqd.setGroupLeader(fund.getGroupLeader());
		         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
		        	plan.setXmqd(xmqd);
	        	}
	        	
	        }
	        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
	        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
	                     .eq("DELETED", 0)
	                     .eq("ID", plan.getXmapbid()));
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
			         	xmqd.setGroupLeader(endin.getGroupLeader());
			         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
			         	plan.setXmqd(xmqd);
	        	 }
	        	 
	        }
			//构建预留字段返回
			reservePropertyService.buildReserveProperty(plan);
        	return plan; 
    }
    
    @Override
    public JsonBean findProjectDetail(String token, BigDecimal projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) { 
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);


        if (null == projectid) {
        	ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
            if (tnp == null) {
                return ResponseFormat.retParam(0, 30003, resultMap);
            }

            projectid = tnp.getId();
        }


        ImplementPlanEntity plan = implementPlanMapper.selectById(projectid.toString());

			if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
	        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
	                    .eq("DELETED", 0)
	                    .eq("ID", plan.getXmapbid()));
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
		         	xmqd.setGroupLeader(fund.getGroupLeader());
		         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
		        	plan.setXmqd(xmqd);
	        	}
	        	
	        }
	        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
	        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
	                     .eq("DELETED", 0)
	                     .eq("ID", plan.getXmapbid()));
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
			         	xmqd.setGroupLeader(endin.getGroupLeader());
			         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
			         	plan.setXmqd(xmqd);
	        	 }
	        	 
	        }
		
	
        //Integer aorgid = plan.getAuditOrgId();
        Integer astaffid = plan.getProjectOrderId().intValue();

        if (null == astaffid) {
            plan.setIsBmAudit(1);
        } else {
            plan.setIsBmAudit(0);
        }

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(plan);
        resultMap.put("pj", plan);
        return ResponseFormat.retParam(1, 200, resultMap);
    }
    
    
    @Override
    public JsonBean projectSS(String token, String projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        ImplementPlanEntity project = implementPlanMapper.selectById(projectid);
        

        //当前实施项目
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if (null != tnp && project!=null) {
            Integer curr_projectid = tnp.getId().intValue();
            if (projectid.equals(curr_projectid.toString())) {
                JsonBean json = new JsonBean(0, "当前项目已实施！", null);
                return json;
            }
        }
        if(project!=null && project.getIsgc().equals("1")) {
        	if (project.getStatus() != null && project.getStatus() == 0) {
                return ResponseFormat.retParam(0, "该项目负责人未启动，不能实施", null);
            }
        	 this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(loginStaff.getStaffid());
             this.tblNbsjStaffSelectMapper.insertEntity(loginStaff.getStaffid(), project.getId());
             this.implementPlanMapper.updatestatus(tnp.getId());
             this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
             return ResponseFormat.retParam(1, 200, null);
        }else {
        	if (null != project && null != project.getUpdateStatus() && project.getUpdateStatus().equals(ImplementPlanEntity.UPDATEYES)) {
                if (project.getStatus() != null && project.getStatus() == 0) {
                    return ResponseFormat.retParam(0, "该项目负责人未启动，不能实施", null);
                } else if (null != project.getStatus() && project.getStatus().equals(3) && project.getStatus().equals(4)) {
                    return ResponseFormat.retParam(0, 80002, null);
                } else {
                    if (null == project.getImplementtime()) {
                        project.setImplementtime(new Date());
                    }
                    this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(loginStaff.getStaffid());
                    this.tblNbsjStaffSelectMapper.insertEntity(loginStaff.getStaffid(), project.getId());
                    this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(project.getImplementtime(), "yyyy-MM-dd HH:mm:ss"));
                    return ResponseFormat.retParam(1, 200, null);
                }
            } else {
                return ResponseFormat.retParam(0, 80003, null);
            }
        } 

        


    }
    
    
    @Override
    public JsonBean pjStart(String token, BigDecimal projectid) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //修改；
        this.implementPlanMapper.updatePjStart(projectid);

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
//		resultMap.put("WorkReport",pj);
        return ResponseFormat.retParam(1, 200, resultMap);
    }
    
    
    
    @Override
    public JsonBean jsfpRoleManageSave(String token, String ids, BigDecimal teamId, BigDecimal projectId) throws Exception {
       
        ImplementPlanEntity project = implementPlanMapper.selectById(projectId.toString());
        
        List<TblNbsjAuthorizationEntity> list = new ArrayList<TblNbsjAuthorizationEntity>();
        List<TblNbsjAuthorizationEntity> updatelist = new ArrayList<TblNbsjAuthorizationEntity>();

        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblStaff tblStaff = new TblStaff();
        tblStaff.setStaffid(staff.getStaffid());

        if (null != project && null != teamId) {
        	Integer countrw = tblNbsjOperateMapper.getCountrw(ids);
        	if(countrw>0) {
        		 return ResponseFormat.retParam(0, "有的任务已完成，无法在进行分配", null);
        	}
            String[] str_ids = ids.split(",");
            for (String str : str_ids) {

//                TblAduitProGramEntity aduitProGram = this.tblAduitProGramService.get(new BigDecimal(str));
                TblAduitProGramEntity aduitProGram = this.tblAduitProGramMapper.selectById(str);
//                TblNbsjAuthorizationEntity authorization = this.tblNbsjAuthorizationService.get(new BigDecimal(projectId), aduitProGram.getProgramId());、
                TblNbsjAuthorizationEntity authorization =null;
                List<TblNbsjAuthorizationEntity> listauths = this.tblNbsjAuthorizationMapper.getByPjnewPg(projectId, aduitProGram.getProgramId());
                if(listauths!=null && listauths.size()>0) {
                	authorization=listauths.get(0);
                }
                if (null != authorization) {
                    authorization.setAduitProGram(aduitProGram);
                    authorization.setAuthTime(new Date());
//                    authorization.setProject(project);
                    authorization.setNewproject(project);
//                    authorization.setTeamStaff(teamStaff);
                    authorization.setTeamStaffId(teamId);
                    authorization.setAuthStaff(tblStaff);
                    updatelist.add(authorization);
                } else {
                    authorization = new TblNbsjAuthorizationEntity();
                    authorization.setAduitProGram(aduitProGram);
                    authorization.setAuthTime(new Date());
//                    authorization.setProject(project);
                    authorization.setNewproject(project);
//                    authorization.setTeamStaff(teamStaff);
                    authorization.setTeamStaffId(teamId);
                    authorization.setAuthStaff(tblStaff);
                    list.add(authorization);
                }
            }
            this.tblNbsjAuthorizationService.merge(updatelist);
            this.tblNbsjAuthorizationService.newsave(list);
//            this.tblNbsjProjectMapper.updateEntity(project);
//            tblnbsjProjectService.update(project);
            //全部分配完成
            BigDecimal tempId = project.getTempId();
            List<TblAduitProGramEntity> listTap = this.tblAduitProGramMapper.findByTMId(tempId);
            boolean is = false;
            if (listTap.size() >= 0) {
                List<TblNbsjAuthorizationEntity> byProjectId = tblNbsjAuthorizationMapper.getBynewProjectId(projectId);
                if (byProjectId.size() == listTap.size()) {
                    is = true;
                } else {
                    is = false;
                }
            }
            if (is == true) {
                //项目分配中
//                project.setFpStatus(2);//已分配
                this.implementPlanMapper.updateFpStatus(2, projectId);
            } else {
                //项目分配中
//                project.setFpStatus(1);//分配中
                this.implementPlanMapper.updateFpStatus(1, projectId);
            }

            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            return ResponseFormat.retParam(1, 200, resultMap);
        }
        return ResponseFormat.retParam(0, 30001, null);
    }

    
    

    @Override
    public JsonBean findByIdTeams(String token,BigDecimal id) throws Exception{
    	 TblStaffUtil staff = userProvider.get();
         if (staff == null) {
             return ResponseFormat.retParam(0, 20006, null);
         }
       List<ImplementPlanTeamEntity> list = implementPlanMapper.selectTeamsById(id);
       
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("data", list);
        return ResponseFormat.retParam(1,200,resultMap);
    }
    
    
    
    @Override
    public JsonBean findsqAll(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId, String projectName, String planStarttime, String planEndtime) throws Exception {
        TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         ImplementPlanEntity implementPlanEntity = new  ImplementPlanEntity();


        if(projectOrderId != null){
            implementPlanEntity.setProjectOrderId(projectOrderId);
        }

        if(StringUtil.isNotEmpty(projectName)){
            implementPlanEntity.setProjectName(projectName);
        }

        if(StringUtil.isNotEmpty(planStarttime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = simpleDateFormat.parse(planStarttime);
            implementPlanEntity.setPlanStarttime(new java.sql.Date(startTime.getTime()));
        }

        if(StringUtil.isNotEmpty(planEndtime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date endTime = simpleDateFormat.parse(planEndtime);
            implementPlanEntity.setPlanEndtime(new java.sql.Date(endTime.getTime()));
        }

        Page< ImplementPlanEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> implementPlanMapper.selectBysqEntity(implementPlanEntity,user.getStaffid()));
		PageInfo< ImplementPlanEntity> pageInfo = new PageInfoUtil< ImplementPlanEntity>().parsePageInfo(page);
		if(pageInfo.getTlist()!=null && pageInfo.getTlist().size()>0) {
			for (ImplementPlanEntity plan : pageInfo.getTlist()) {
				if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
		        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
		                    .eq("DELETED", 0)
		                    .eq("ID", plan.getXmapbid()));
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
			         	xmqd.setGroupLeader(fund.getGroupLeader());
			         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
			        	plan.setXmqd(xmqd);
		        	}
		        	
		        }
		        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
		        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
		                     .eq("DELETED", 0)
		                     .eq("ID", plan.getXmapbid()));
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
				         	xmqd.setGroupLeader(endin.getGroupLeader());
				         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
				         	plan.setXmqd(xmqd);
		        	 }
		        	 
		        }
			}
			
		}
		pageInfo.setCurrProjectId(tblNbsjStaffSelectMapper.selectProjectIdByStaffId(user.getStaffid()));
        return ResponseFormat.retParam(1,200,pageInfo);
    }
    
    
    @Override
    public JsonBean fpzyksry(String token, BigDecimal projectid,String zyksryids,String zyksryrwnames) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        //修改；
        this.implementPlanMapper.fpzyksry(zyksryids, zyksryrwnames, projectid);

//        Map<String, Object> resultMap = new HashMap<String, Object>(0);
//		resultMap.put("WorkReport",pj);
        return ResponseFormat.retParam(1, 200, null);
    }



	@Override
	public JsonBean getReviewStatusList(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId,
			String projectName, String planStarttime, String planEndtime) throws Exception {
		TblStaffUtil user = userProvider.get();
        if(user == null) {
            return ResponseFormat.retParam(0,20006,null);
        }

         ImplementPlanEntity implementPlanEntity = new  ImplementPlanEntity();


        if(projectOrderId != null){
            implementPlanEntity.setProjectOrderId(projectOrderId);
        }

        if(StringUtil.isNotEmpty(projectName)){
            implementPlanEntity.setProjectName(projectName);
        }

        if(StringUtil.isNotEmpty(planStarttime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = simpleDateFormat.parse(planStarttime);
            implementPlanEntity.setPlanStarttime(new java.sql.Date(startTime.getTime()));
        }

        if(StringUtil.isNotEmpty(planEndtime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date endTime = simpleDateFormat.parse(planEndtime);
            implementPlanEntity.setPlanEndtime(new java.sql.Date(endTime.getTime()));
        }

        Page<ImplementPlanEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> implementPlanMapper.selectReviewStatusList(implementPlanEntity));
		PageInfo<ImplementPlanEntity> pageInfo = new PageInfoUtil< ImplementPlanEntity>().parsePageInfo(page);
		List<FlowTaskInfo> infoList = null;
		FlowTaskInfo info = null;
		for (ImplementPlanEntity impl : pageInfo.getTlist()) {
			if(impl.getId() == null) {
				continue;
			}
			infoList = this.implementPlanMapper.selectApprovalInfoList(impl.getId(),ImplementPlanEntity.SHEETID,"专业科室人员%","%重要项目%");
			if(infoList != null && infoList.size() > 0) {
				impl.setZyksAppStaffName(infoList.get(0).getCurrentStaffName());
			}
			infoList = this.implementPlanMapper.selectApprovalInfoList(impl.getId(),ImplementPlanEntity.SHEETID,"业务分管副主任%","%重要项目%");
			if(infoList != null && infoList.size() > 0) {
				impl.setYwfgAppStaffName(infoList.get(0).getCurrentStaffName());
				impl.setCommont(infoList.get(0).getCommont());
			}
			info = this.implementPlanMapper.selectApprovalEndInfo(impl.getId(),ImplementPlanEntity.SHEETID,"end");
			if(info != null) {
				impl.setEndAppDate(info.getCreateTime());
			}
			info = this.implementPlanMapper.selectApprovalStartInfo(impl.getId(),ImplementPlanEntity.SHEETID,"提交");
			if(info != null) {
				impl.setStartAppDate(info.getCreateTime());
			}
			
		}
		pageInfo.setCurrProjectId(tblNbsjStaffSelectMapper.selectProjectIdByStaffId(user.getStaffid()));

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1,200,pageInfo);
	}
	
	
	
	  @Override
	  public JsonBean updateImplementPlanTeamWidthId(String token,ImplementPlanTeamEntity team) throws Exception {
	      TblStaffUtil loginStaff = userProvider.get();
	      if (loginStaff == null) {
	          return ResponseFormat.retParam(0, 20006, null);
	      }
	      if(team!=null && team.getId()!=null) {
	    	  ImplementPlanTeamEntity entity = implementPlanMapper.findbyteamid(team.getId());
	    	  String ids= team.getTeamMembersIds()+","+entity.getTeamMembersIds();
	    	  team.setTeamMembersIds(ids);
		      //修改；
		      this.implementPlanMapper.updateImplementPlanTeamWidthId(team.getId(), team);
	      }

	      return ResponseFormat.retParam(1, 200, null);
	  }

    @Override
    public JsonBean getListcCompleted(String token, Integer pageNumber, Integer pageSize, BigDecimal projectOrderId, String projectName, String planStarttime, String planEndtime, BigDecimal staffId, Integer xmnd) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        ImplementPlanEntity implementPlanEntity = new  ImplementPlanEntity();


        if(projectOrderId != null){
            implementPlanEntity.setProjectOrderId(projectOrderId);
        }

        if(StringUtil.isNotEmpty(projectName)){
            implementPlanEntity.setProjectName(projectName);
        }

        if(StringUtil.isNotEmpty(planStarttime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = simpleDateFormat.parse(planStarttime);
            implementPlanEntity.setPlanStarttime(new java.sql.Date(startTime.getTime()));
        }

        if(StringUtil.isNotEmpty(planEndtime)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date endTime = simpleDateFormat.parse(planEndtime);
            implementPlanEntity.setPlanEndtime(new java.sql.Date(endTime.getTime()));
        }

        Page<ImplementPlanEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> implementPlanMapper.selectByCompletedEntity(implementPlanEntity,staffId,xmnd,implementPlanEntity.COMPLETED));
        PageInfo<ImplementPlanEntity> pageInfo = new PageInfoUtil< ImplementPlanEntity>().parsePageInfo(page);

		//构建预留字段返回
		reservePropertyService.buildReserveProperty(pageInfo.getTlist());
        return ResponseFormat.retParam(1,200,pageInfo);
    }



	@Override
	public JsonBean yzmb(String token, String tempid) throws Exception {
		List<TblAduitProGramEntity> alls = tblAduitProGramMapper.findByALLbytempid(tempid);
		if(alls==null || alls.size()<=0) {
			return ResponseFormat.retParam(0,"选择模板无内容",null);
		 }
		return ResponseFormat.retParam(1,"成功",null);
	}


	@Override
	public JsonBean findBytjsj(String token,QualityParam param) throws Exception {
		  TblStaffUtil loginStaff = userProvider.get();
	        if (loginStaff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
		List<QualityParam> alls = tblAduitProGramMapper.findBytjsj(param);
		return ResponseFormat.retParam(1,200,alls);
	}
	
	
	 
	 @Override
	 public JsonBean findbyOrgidLast(String token,String orgid) throws Exception{
		 TblStaffUtil loginStaff = userProvider.get();
	        if (loginStaff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
	        ImplementPlanEntity plan = implementPlanMapper.findbyOrgidLast(orgid);

				if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
		        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
		                    .eq("DELETED", 0)
		                    .eq("ID", plan.getXmapbid()));
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
			         	xmqd.setGroupLeader(fund.getGroupLeader());
			         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
			        	plan.setXmqd(xmqd);
		        	}
		        	
		        }
		        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
		        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
		                     .eq("DELETED", 0)
		                     .eq("ID", plan.getXmapbid()));
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
				         	xmqd.setGroupLeader(endin.getGroupLeader());
				         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
				         	plan.setXmqd(xmqd);
		        	 }
		        	 
		        }
			
		
	        Map<String,Object> resultMap = new HashMap<>();
	        resultMap.put("data", plan);
	        return ResponseFormat.retParam(1,200,resultMap);
	    }



	@Override
	public JsonBean getImplPlanListForSjbgdg(Integer pageNumber, Integer pageSize, String projectName)
			throws Exception {
		 TblStaffUtil user = userProvider.get();
	        if(user == null) {
	            return ResponseFormat.retParam(0,20006,null);
	        }

	        ImplementPlanEntity implementPlanEntity = new  ImplementPlanEntity();
	        if(StringUtil.isNotEmpty(projectName)){
	            implementPlanEntity.setProjectName(projectName);
	        }

	        Page<ImplementPlanEntity> page = PageHelper.startPage(pageNumber, pageSize).doSelectPage(()-> implementPlanMapper.selectPageListForSjbgdg(implementPlanEntity,user));
	        
			PageInfo<ImplementPlanEntity> pageInfo = new PageInfoUtil< ImplementPlanEntity>().parsePageInfo(page);
			if(pageInfo.getTlist()!=null && pageInfo.getTlist().size()>0) {
				for (ImplementPlanEntity plan : pageInfo.getTlist()) {
					if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
			        	TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
			                    .eq("DELETED", 0)
			                    .eq("ID", plan.getXmapbid()));
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
				         	xmqd.setGroupLeader(fund.getGroupLeader());
				         	xmqd.setGroupLeaderId(fund.getGroupLeaderId());
				        	plan.setXmqd(xmqd);
			        	}
			        	
			        }
			        if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
			        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
			                     .eq("DELETED", 0)
			                     .eq("ID", plan.getXmapbid()));
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
					         	xmqd.setGroupLeader(endin.getGroupLeader());
					         	xmqd.setGroupLeaderId(endin.getGroupLeaderId());
					         	plan.setXmqd(xmqd);
			        	 }
			        	 
			        }
				}
				
			}
			
			pageInfo.setCurrProjectId(tblNbsjStaffSelectMapper.selectProjectIdByStaffId(user.getStaffid()));

			//构建预留字段返回
			reservePropertyService.buildReserveProperty(pageInfo.getTlist());
	        return ResponseFormat.retParam(1,200,pageInfo);
	}

}
