package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.ImplementPlanTeamEntity;
import com.huabo.audit.oracle.entity.LeaveAudit3LEntity;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsOperate;
import com.huabo.audit.oracle.entity.TblYqnsXmdq;
import com.huabo.audit.oracle.mapper.EnginAuditProjectMapper;
import com.huabo.audit.oracle.mapper.FundAuditProjectMapper;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.LeaveAudit3LMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.mapper.TblYqnsOperateMapper;
import com.huabo.audit.oracle.service.TblYqnsOperateService;
import com.huabo.audit.util.PageResult;


@Service
public class TblYqnsOperateServiceImpl implements TblYqnsOperateService {

	@Resource
	private TblYqnsOperateMapper tblYqnsOperateMapper;
	 @Autowired
	 private TblStaffMapper tblStaffMapper;
	 
	 @Resource
	 private EnginAuditProjectMapper enginAuditProjectMapper;
	 
	 @Resource
	 private FundAuditProjectMapper fundAuditProjectMapper;
	 
	 @Autowired
	 private  ImplementPlanMapper implementPlanMapper;
	 
	 @Resource
	 private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;
	 
	 @Resource
	    private UserProvider userProvider;
	 
	 @Resource
		private LeaveAudit3LMapper leaveAudit3LMapper;
	
	@Override
	public JsonBean saveOrupdate(String token, TblYqnsOperate jd) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        jd.setCreatedate(new Date());
        jd.setCreatestaffid(staff.getStaffid());
        jd.setCreatename(staff.getRealname());
        jd.setStatus(0); 
        if(jd!=null && jd.getOperid()!=null) {
        	tblYqnsOperateMapper.updateById(jd);
        }else {
        	jd.setOperid(RandomUtil.uuBigDecimalId());
        	tblYqnsOperateMapper.insert(jd);
        }
        resultMap.put("data", jd);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	
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
	        	return plan;
	    }
	 
	 
	@Override
	public JsonBean findByid(String token, BigDecimal jdid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsOperate tb = tblYqnsOperateMapper.selectById(jdid);
      
        if(tb.getSsmkid().equals("22222")) {//我的任务
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        
        if(tb.getSsmkid().equals("1349")) {//底稿管理
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        
        
        if(tb.getSsmkid().equals("1342")) {//交换意见稿
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        
        if(tb.getSsmkid().equals("1343")) {//审计报告定稿
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        
        
        if(tb.getSsmkid().equals("1498")) {//审计承诺书
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        
        if(tb.getSsmkid().equals("1353")) {//我的工程任务
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
            
        }
        
        if(tb.getSsmkid().equals("1351")) {//我的底稿
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        
        if(tb.getSsmkid().equals("1400")) {//审计结果确认单
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
        }
        
        
        if(tb.getSsmkid().equals("1399")) {//审计工作记录
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
         }
        
        
        if(tb.getSsmkid().equals("1525")) {//现场审查主要内容
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
          }
        
        if(tb.getSsmkid().equals("1402")) {//审计项目运行情况表
        	//当前实施项目
            ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(staff.getStaffid());
            if(tnp!=null) {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	this.tblNbsjStaffSelectMapper.deleteNbsjByStaff(staff.getStaffid());
                this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
                this.implementPlanMapper.updatestatus(tnp.getId());
                this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }else {
            	ImplementPlanEntity project = implementPlanMapper.selectById(tb.getFormid().toString());
            	 this.tblNbsjStaffSelectMapper.insertEntity(staff.getStaffid(), project.getId());
            	 this.implementPlanMapper.updateImplementTime(project.getId(), DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }   
        }
        
        
        
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap.put("data", tb);
		return ResponseFormat.retParam(1,200,resultMap);
	}



	@Override
	public JsonBean findhzAllList(String token, Integer pageNumber, Integer pageSize,String ssmkid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}



		com.github.pagehelper.PageInfo<TblYqnsOperate> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						tblYqnsOperateMapper.findbyhzlist(ssmkid);

					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsOperate> build = new PageResult<TblYqnsOperate>().build(info);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}


	@Override
	public JsonBean findAllList(String token, Integer pageNumber, Integer pageSize,Integer status) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        com.github.pagehelper.PageInfo<TblYqnsOperate> info =  PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> {
					try {
						if(status!=null) {
							tblYqnsOperateMapper.findbyStatus(staff.getStaffid().toString(), status);
						}else {
							tblYqnsOperateMapper.findbyStatusdb(staff.getStaffid().toString());
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		PageResult<TblYqnsOperate> build = new PageResult<TblYqnsOperate>().build(info);
		resultMap.put("pageInfo", build);
		return ResponseFormat.retParam(1,200,resultMap);
	}

	@Override
	public JsonBean deleteone(String token, BigDecimal jdid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        tblYqnsOperateMapper.deleteoneById(jdid);
        return ResponseFormat.retParam(1,200,null);
	}

	@Override
	public JsonBean complete(String token, BigDecimal operid) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblYqnsOperate tb = tblYqnsOperateMapper.selectById(operid);
        
        Integer info = setTypeInfo(tb,staff);//判断下一步模块内容
        if(info!=0) {
        	return ResponseFormat.retParam(0,"有未审批的内容，审批通过后才能点击完成！！",null);
        }
        //完成当前任务
        tb.setStatus(1);
        tblYqnsOperateMapper.updateById(tb);
        
        return ResponseFormat.retParam(1,200,null);
	} 
	
	
	
	private Integer setTypeInfo(TblYqnsOperate tb,TblStaffUtil staff) {
		TblYqnsOperate newoper=null;
		List<String> list=null;
		ImplementPlanEntity plan =null;
		Integer returnstatus=0;  
		switch (tb.getSsmkid()) {
		
		case "1511":
			Integer findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
			if(findrwcount<=1) {
				//根据当前任务查询下一步任务，并保存消息表
				 list = tblStaffMapper.getByroleIds("专项科");
				List<String> jclist = tblStaffMapper.getByroleIds("基建科");
				list.addAll(jclist);
				if(list!=null && list.size()>0) {
					for (String string : list) {
						List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
						if(list2!=null && list2.size()>0) {
							for (TblStaff user : list2) {
								newoper= new TblYqnsOperate(); 
								newoper.setSsmkid("661");
								newoper.setSsmk("立项建议专业评估");
								newoper.setRwmc("立项建议专业评估内容上报");
								newoper.setFormid(tb.getFormid());
								newoper.setFormname("立项建议专业评估");
								newoper.setOperid(RandomUtil.uuBigDecimalId());
							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid());
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(user.getStaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							}
						}
						
					}
				}
			}
			 
			
			break;
			
			
		case "661":
			 //根据当前任务查询下一步任务，并保存消息表
			 findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
			if(findrwcount<=1) {
				list = tblStaffMapper.getByroleIds("计划科科长");
				if(list!=null && list.size()>0) {
					for (String string : list) {
						List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
						if(list2!=null && list2.size()>0) {
							for (TblStaff user : list2) {
								newoper= new TblYqnsOperate();
								newoper.setSsmkid("792905");
								newoper.setSsmk("立项建议总体评估");
								newoper.setRwmc("立项建议总体评估汇总");
								newoper.setFormid(tb.getFormid());
								newoper.setFormname("立项建议总体评估汇总");
								newoper.setOperid(RandomUtil.uuBigDecimalId());
							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid());
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(user.getStaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							}
						}
						
					}
				}
			}
			break;
			
			
		case "792905":
			 //根据当前任务查询下一步任务，并保存消息表
			list = tblStaffMapper.getByroleIds("计划科科长");
			if(list!=null && list.size()>0) {
				for (String string : list) {
					List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
					if(list2!=null && list2.size()>0) {
						for (TblStaff user : list2) {
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1519");
							newoper.setSsmk("计划草稿");
							newoper.setRwmc("计划草稿编制");
							newoper.setFormid(tb.getFormid());
							newoper.setFormname("计划草稿编制");
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						}
					}
					
				}
			}
			break;
			
			
		case "1519":
			 //根据当前任务查询下一步任务，并保存消息表
			list = tblStaffMapper.getByroleIds("计划科科长");
			if(list!=null && list.size()>0) {
				for (String string : list) {
					List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
					if(list2!=null && list2.size()>0) {
						for (TblStaff user : list2) {
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1520");
							newoper.setSsmk("计划初稿");
							newoper.setRwmc("计划初稿编制");
							newoper.setFormid(tb.getFormid());
							newoper.setFormname("计划初稿编制");
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						}
					}
					
				}
			}
			break;
			
		case "1520":
			 //根据当前任务查询下一步任务，并保存消息表 
			list = tblStaffMapper.getByroleIds("计划科科长");
			if(list!=null && list.size()>0) {
				for (String string : list) {
					List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
					if(list2!=null && list2.size()>0) {
						for (TblStaff user : list2) {
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1521");
							newoper.setSsmk("计划定稿");
							newoper.setRwmc("计划定稿编制");
							newoper.setFormid(tb.getFormid());
							newoper.setFormname("计划定稿编制");
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						}
					}
					
				}
			}
			break;
			
		case "1521":
			 //根据当前任务查询下一步任务，并保存消息表
			list = tblStaffMapper.getByroleIds("计划科科长");
			if(list!=null && list.size()>0) {
				for (String string : list) {
					List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
					if(list2!=null && list2.size()>0) {
						for (TblStaff user : list2) {
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1465");
							newoper.setSsmk("工程审计项目安排");
							newoper.setRwmc("工程审计项目安排");
							newoper.setFormid(tb.getFormid());
							newoper.setFormname("工程审计项目安排");
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1466");
							newoper.setSsmk("财务审计项目安排");
							newoper.setRwmc("财务审计项目安排");
							newoper.setFormid(tb.getFormid());
							newoper.setFormname("财务审计项目安排");
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						}
					}
					
				}
			}
			break;
			
			case "1465":
				if(tb.getFormid()!=null) {
						List<TblYqnsEnginAuditProjectEntity> tbidall = enginAuditProjectMapper.findByTbidall(tb.getFormid().toString());
						if(tbidall!=null && tbidall.size()>0) {
							for (TblYqnsEnginAuditProjectEntity engin : tbidall) {
								if(engin.getGljhxmlx()!=null && engin.getGljhxmlx().equals("12")) {
									newoper= new TblYqnsOperate();
									newoper.setSsmkid("1497");
									newoper.setSsmk("工作方案");
									newoper.setRwmc("工作方案编制");
									newoper.setFormid(new BigDecimal(engin.getId()));
									newoper.setFormname(engin.getName());
									newoper.setOperid(RandomUtil.uuBigDecimalId());
								    newoper.setParentid(tb.getOperid());
								    newoper.setStatus(0);
								    newoper.setCreatestaffid(staff.getStaffid());
								    newoper.setCreatename(staff.getRealname());
								    newoper.setCreatedate(new Date());
								    newoper.setRwuserid(engin.getApproverId().toString());
								    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
								    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
								    tblYqnsOperateMapper.insert(newoper);
								}else {
									  newoper= new TblYqnsOperate();
									  newoper.setSsmkid("1363");
									  newoper.setSsmk("实施方案");
									  newoper.setRwmc("实施方案编制");
									  newoper.setFormid(new BigDecimal(engin.getId()));
									  newoper.setFormname(engin.getName());
									  newoper.setOperid(RandomUtil.uuBigDecimalId());
									  newoper.setParentid(tb.getOperid());
									  newoper.setStatus(0);
									  newoper.setCreatestaffid(staff.getStaffid());
									  newoper.setCreatename(staff.getRealname());
									  newoper.setCreatedate(new Date());
									  newoper.setRwuserid(engin.getApproverId().toString());
									  newoper.setOrgid(staff.getCurrentOrg().getOrgid());
									  newoper.setOrgname(staff.getCurrentOrg().getOrgname());
									  tblYqnsOperateMapper.insert(newoper);
									
								}
						}
					}
				}
		
				
			 
			break;
			
			
			case "1466":
				if(tb.getFormid()!=null) {
						List<TblYqnsFundAuditProjectEntity> tbidall = fundAuditProjectMapper.findByTbidall(tb.getFormid().toString());
						if(tbidall!=null && tbidall.size()>0) {
							for (TblYqnsFundAuditProjectEntity engin : tbidall) {
								if(engin.getGljhxmlx()!=null && engin.getGljhxmlx().equals("11")) {
									newoper= new TblYqnsOperate();
									newoper.setSsmkid("1497");
									newoper.setSsmk("工作方案");
									newoper.setRwmc("工作方案编制");
									newoper.setFormid(new BigDecimal(engin.getId()));
									newoper.setFormname(engin.getName());
									newoper.setOperid(RandomUtil.uuBigDecimalId());
								    newoper.setParentid(tb.getOperid());
								    newoper.setStatus(0);
								    newoper.setCreatestaffid(staff.getStaffid());
								    newoper.setCreatename(staff.getRealname());
								    newoper.setCreatedate(new Date());
								    newoper.setRwuserid(engin.getApproverId().toString());
								    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
								    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
								    tblYqnsOperateMapper.insert(newoper);
								}else if(engin.getGljhxmlx()!=null && engin.getGljhxmlx().equals("23")) { 
									List<LeaveAudit3LEntity> llist = leaveAudit3LMapper.getcwanbList(null,new BigDecimal((engin.getId())));
									if(llist!=null && llist.size()>0) {
										for (LeaveAudit3LEntity lls : llist) {
											newoper= new TblYqnsOperate();
											newoper.setSsmkid("1496"); 
											newoper.setSsmk("审前调查报告");
											newoper.setRwmc("审前调查报告编制");
											newoper.setFormid(lls.getId());
											newoper.setFormname(lls.getProjectname());
											newoper.setOperid(RandomUtil.uuBigDecimalId());
										    newoper.setParentid(tb.getOperid());
										    newoper.setStatus(0);
										    newoper.setCreatestaffid(staff.getStaffid()); 
										    newoper.setCreatename(staff.getRealname());
										    newoper.setCreatedate(new Date());
										    newoper.setRwuserid(lls.getZsstaffid().toString());
										    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
										    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
										    tblYqnsOperateMapper.insert(newoper);
										    
										    
										    newoper= new TblYqnsOperate();
											newoper.setSsmkid("1363");
											newoper.setSsmk("实施方案");
											newoper.setRwmc("实施方案编制");
											newoper.setFormid(lls.getId());
											newoper.setFormname(lls.getProjectname());
											newoper.setOperid(RandomUtil.uuBigDecimalId());
										    newoper.setParentid(tb.getOperid());
										    newoper.setStatus(0);
										    newoper.setCreatestaffid(staff.getStaffid()); 
										    newoper.setCreatename(staff.getRealname());
										    newoper.setCreatedate(new Date());
										    newoper.setRwuserid(lls.getZsstaffid().toString());
										    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
										    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
										    tblYqnsOperateMapper.insert(newoper);
										}
									}
								}else {
									newoper= new TblYqnsOperate();
									newoper.setSsmkid("1496");
									newoper.setSsmk("审前调查报告");
									newoper.setRwmc("审前调查报告编制");
									newoper.setFormid(new BigDecimal(engin.getId()));
									newoper.setFormname(engin.getName());
									newoper.setOperid(RandomUtil.uuBigDecimalId());
								    newoper.setParentid(tb.getOperid());
								    newoper.setStatus(0);
								    newoper.setCreatestaffid(staff.getStaffid()); 
								    newoper.setCreatename(staff.getRealname());
								    newoper.setCreatedate(new Date());
								    newoper.setRwuserid(engin.getApproverId().toString());
								    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
								    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
								    tblYqnsOperateMapper.insert(newoper);
								    
								    
								    newoper= new TblYqnsOperate();
									newoper.setSsmkid("1363");
									newoper.setSsmk("实施方案");
									newoper.setRwmc("实施方案编制");
									newoper.setFormid(new BigDecimal(engin.getId()));
									newoper.setFormname(engin.getName());
									newoper.setOperid(RandomUtil.uuBigDecimalId());
								    newoper.setParentid(tb.getOperid());
								    newoper.setStatus(0);
								    newoper.setCreatestaffid(staff.getStaffid());
								    newoper.setCreatename(staff.getRealname());
								    newoper.setCreatedate(new Date());
								    newoper.setRwuserid(engin.getApproverId().toString());
								    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
								    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
								    tblYqnsOperateMapper.insert(newoper);
								}
							}
					}
				}
			
			break;
			
			case "1497":
				if(tb.getFormid()!=null) {
					
					TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
			                .eq("DELETED", 0)
			                .eq("ID", tb.getFormid()));
						if(fund!=null ) {
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1496");
							newoper.setSsmk("审前调查报告");
							newoper.setRwmc("审前调查报告编制");
							newoper.setFormid(new BigDecimal(fund.getId()));
							newoper.setFormname(fund.getName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(fund.getApproverId().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1363");
							newoper.setSsmk("实施方案");
							newoper.setRwmc("实施方案编制");
							newoper.setFormid(new BigDecimal(fund.getId()));
							newoper.setFormname(fund.getName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(fund.getApproverId().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
					}
						
						TblYqnsEnginAuditProjectEntity engin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
				                .eq("DELETED", 0)
				                .eq("ID", tb.getFormid()));	
					
					if(engin!=null ) {
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1496");
							newoper.setSsmk("审前调查报告");
							newoper.setRwmc("审前调查报告编制");
							newoper.setFormid(new BigDecimal(engin.getId()));
							newoper.setFormname(engin.getName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid()); 
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(engin.getApproverId().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1363");
							newoper.setSsmk("实施方案");
							newoper.setRwmc("实施方案编制");
							newoper.setFormid(new BigDecimal(engin.getId()));
							newoper.setFormname(engin.getName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(engin.getApproverId().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
					}
				}
			
			break;
			
			case "1363":
			
			try {
				plan= implementPlanMapper.selectById(tb.getFormid().toString());
				if(plan.getSpzt()!=null && !plan.getSpzt().equals("6")) {
					returnstatus=1;
					break;	
				} 
				if(plan!=null && !plan.getZykstype().equals("基建")) {
					newoper= new TblYqnsOperate();
					newoper.setSsmkid("1362");
					newoper.setSsmk("任务分配");
					newoper.setRwmc("项目主审分配任务");
					newoper.setFormid(plan.getId());
					newoper.setFormname(tb.getFormname());
					newoper.setOperid(RandomUtil.uuBigDecimalId());
				    newoper.setParentid(tb.getOperid());
				    newoper.setStatus(0);
				    newoper.setCreatestaffid(staff.getStaffid());
				    newoper.setCreatename(staff.getRealname());
				    newoper.setCreatedate(new Date());
				    newoper.setRwuserid(plan.getZsstaffid().toString());
				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
				    tblYqnsOperateMapper.insert(newoper);
				}else {
					newoper= new TblYqnsOperate();
					newoper.setSsmkid("60095");
					newoper.setSsmk("审计通知审批");
					newoper.setRwmc("审计通知审批编制");
					newoper.setFormid(plan.getId());
					newoper.setFormname(tb.getFormname());
					newoper.setOperid(RandomUtil.uuBigDecimalId());
				    newoper.setParentid(tb.getOperid());
				    newoper.setStatus(0);
				    newoper.setCreatestaffid(staff.getStaffid());
				    newoper.setCreatename(staff.getRealname());
				    newoper.setCreatedate(new Date());
				    newoper.setRwuserid(tb.getRwuserid());
				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
				    tblYqnsOperateMapper.insert(newoper);
				    
				    
				    newoper= new TblYqnsOperate();
					newoper.setSsmkid("1417");
					newoper.setSsmk("印信使用单");
					newoper.setRwmc("印信使用申请");
					newoper.setFormid(plan.getId());
					newoper.setFormname(tb.getFormname());
					newoper.setOperid(RandomUtil.uuBigDecimalId());
				    newoper.setParentid(tb.getOperid());
				    newoper.setStatus(0);
				    newoper.setCreatestaffid(staff.getStaffid());
				    newoper.setCreatename(staff.getRealname());
				    newoper.setCreatedate(new Date());
				    newoper.setRwuserid(tb.getRwuserid());
				    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
				    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
				    tblYqnsOperateMapper.insert(newoper);
				}
			} catch (Exception e3) {
				e3.printStackTrace();
			}
				
			
			break;
			
			
			case "1362":
			try {
				plan = implementPlanMapper.selectById(tb.getFormid().toString());
				
				newoper= new TblYqnsOperate();
				newoper.setSsmkid("60095");
				newoper.setSsmk("审计通知审批");
				newoper.setRwmc("审计通知审批编制");
				newoper.setFormid(tb.getFormid());
				newoper.setFormname(tb.getFormname());
				newoper.setOperid(RandomUtil.uuBigDecimalId());
			    newoper.setParentid(tb.getOperid());
			    newoper.setStatus(0);
			    newoper.setCreatestaffid(staff.getStaffid());
			    newoper.setCreatename(staff.getRealname());
			    newoper.setCreatedate(new Date());
			    newoper.setRwuserid(plan.getZsstaffid().toString());
			    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
			    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
			    tblYqnsOperateMapper.insert(newoper);
			    
			    
			    newoper= new TblYqnsOperate();
				newoper.setSsmkid("1417");
				newoper.setSsmk("印信使用单");
				newoper.setRwmc("印信使用申请"); 
				newoper.setFormid(tb.getFormid());
				newoper.setFormname(tb.getFormname());
				newoper.setOperid(RandomUtil.uuBigDecimalId());
			    newoper.setParentid(tb.getOperid());
			    newoper.setStatus(0);
			    newoper.setCreatestaffid(staff.getStaffid());
			    newoper.setCreatename(staff.getRealname());
			    newoper.setCreatedate(new Date());
			    newoper.setRwuserid(tb.getRwuserid());
			    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
			    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
			    tblYqnsOperateMapper.insert(newoper);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			break;
			
			
		
			
		case "60095":
			
		  //根据当前任务查询下一步任务，并保存消息表
			list = tblStaffMapper.getByroleIds("计划科副科长");
			if(list!=null && list.size()>0) {
				for (String string : list) {
					List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
					if(list2!=null && list2.size()>0) {
						for (TblStaff user : list2) {
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1356");
							newoper.setSsmk("审计通知书");
							newoper.setRwmc("审计通知书上传");
							newoper.setFormid(tb.getFormid());
							newoper.setFormname(tb.getFormname());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						}
					}
					
				}
			}
			break;
			
			
		case "1356":
			
			  //根据当前任务查询下一步任务，并保存消息表
				list = tblStaffMapper.getByroleIds("计划科副科长");
				if(list!=null && list.size()>0) {
					for (String string : list) {
						List<TblStaff> list2 = tblStaffMapper.selectbyRoleids(string);
						if(list2!=null && list2.size()>0) {
							for (TblStaff user : list2) {
								newoper= new TblYqnsOperate();
								newoper.setSsmkid("662");
								newoper.setSsmk("项目启动");
								newoper.setRwmc("项目启动 ");
								newoper.setFormid(tb.getFormid());
								newoper.setFormname(tb.getFormname());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid());
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(user.getStaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							}
						}
						
					}
				}
				break;
			
				
		case "662":
			
			//根据当前任务查询下一步任务，并保存消息表
			String staffids="";
			List<TblStaff> list2=null;
			ImplementPlanEntity entity;
			try {
				entity = implementPlanMapper.selectById(tb.getFormid().toString());
				if(entity!=null && entity.getZykstype().equals("基建")) {
					
					if(entity.getZsstaffid()!=null) {
						newoper= new TblYqnsOperate();
						newoper.setSsmkid("1498"); 
						newoper.setSsmk("审 计 承 诺 书");
						newoper.setRwmc("审 计 承 诺 书 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
						
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1353");
						newoper.setSsmk("我的工程任务");
						newoper.setRwmc("我的工程任务 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1351");
						newoper.setSsmk("我的底稿");
						newoper.setRwmc("我的底稿 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1349");
						newoper.setSsmk("底稿管理");
						newoper.setRwmc("底稿管理 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1400");
						newoper.setSsmk("审计结果确认单");
						newoper.setRwmc("审计结果确认单");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					   
					    
					    
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1402");
						newoper.setSsmk("审计项目运行情况表");
						newoper.setRwmc("审计项目运行情况表");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					}
					
					
					List<ImplementPlanTeamEntity> teams = entity.getTeams();
					if(teams!=null && teams.size()>0) {
						ImplementPlanTeamEntity team=teams.get(0);
						staffids=team.getTeamMembersIds()+","+team.getFzzstaffid();
						list2=tblStaffMapper.selectTblStaffByIds(staffids);
						for (TblStaff user : list2) {
							
//						newoper= new TblYqnsOperate();
//						newoper.setSsmkid("1498");
//						newoper.setSsmk("审 计 承 诺 书");
//						newoper.setRwmc("审 计 承 诺 书 ");
//						newoper.setFormid(entity.getId());
//						newoper.setFormname(entity.getProjectName());
//						newoper.setOperid(RandomUtil.uuBigDecimalId());
//					    newoper.setParentid(tb.getOperid());
//					    newoper.setStatus(0);
//					    newoper.setCreatestaffid(staff.getStaffid());
//					    newoper.setCreatename(staff.getRealname());
//					    newoper.setCreatedate(new Date());
//					    newoper.setRwuserid(user.getStaffid().toString());
//					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
//					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
//					    tblYqnsOperateMapper.insert(newoper);
							
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1353");
							newoper.setSsmk("我的工程任务");
							newoper.setRwmc("我的工程任务 ");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
//					    newoper= new TblYqnsOperate();
//						newoper.setSsmkid("1399");
//						newoper.setSsmk("审计工作记录");
//						newoper.setRwmc("审计工作记录");
//						newoper.setFormid(entity.getId());
//						newoper.setFormname(entity.getProjectName());
//						newoper.setOperid(RandomUtil.uuBigDecimalId());
//					    newoper.setParentid(tb.getOperid());
//					    newoper.setStatus(0);
//					    newoper.setCreatestaffid(staff.getStaffid());
//					    newoper.setCreatename(staff.getRealname());
//					    newoper.setCreatedate(new Date());
//					    newoper.setRwuserid(user.getStaffid().toString());
//					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
//					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
//					    tblYqnsOperateMapper.insert(newoper);
						    
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1351");
							newoper.setSsmk("我的底稿");
							newoper.setRwmc("我的底稿 ");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1402");
							newoper.setSsmk("审计项目运行情况表");
							newoper.setRwmc("审计项目运行情况表");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    
						}
						
					}
					
				}else {
					
					if(entity.getZsstaffid()!=null) {
						newoper= new TblYqnsOperate();
						newoper.setSsmkid("1498"); 
						newoper.setSsmk("审 计 承 诺 书");
						newoper.setRwmc("审 计 承 诺 书 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
						
						newoper= new TblYqnsOperate();
						newoper.setSsmkid("22222");
						newoper.setSsmk("我的任务");
						newoper.setRwmc("我的任务 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1399");
						newoper.setSsmk("审计工作记录");
						newoper.setRwmc("审计工作记录");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1351");
						newoper.setSsmk("我的底稿");
						newoper.setRwmc("我的底稿 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1349");
						newoper.setSsmk("底稿管理");
						newoper.setRwmc("底稿管理 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1400");
						newoper.setSsmk("审计结果确认单");
						newoper.setRwmc("审计结果确认单");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					    
					   
					    
					    
					    
					    newoper= new TblYqnsOperate();
						newoper.setSsmkid("1402");
						newoper.setSsmk("审计项目运行情况表");
						newoper.setRwmc("审计项目运行情况表");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					}
					List<ImplementPlanTeamEntity> teams = entity.getTeams();
					if(teams!=null && teams.size()>0) {
						ImplementPlanTeamEntity team=teams.get(0);
						staffids=team.getTeamMembersIds()+","+team.getFzzstaffid();
						list2=tblStaffMapper.selectTblStaffByIds(staffids);
						for (TblStaff user : list2) {
//						newoper= new TblYqnsOperate();
//						newoper.setSsmkid("1498"); 
//						newoper.setSsmk("审 计 承 诺 书");
//						newoper.setRwmc("审 计 承 诺 书 ");
//						newoper.setFormid(entity.getId());
//						newoper.setFormname(entity.getProjectName());
//						newoper.setOperid(RandomUtil.uuBigDecimalId());
//					    newoper.setParentid(tb.getOperid());
//					    newoper.setStatus(0);
//					    newoper.setCreatestaffid(staff.getStaffid());
//					    newoper.setCreatename(staff.getRealname());
//					    newoper.setCreatedate(new Date());
//					    newoper.setRwuserid(user.getStaffid().toString());
//					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
//					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
//					    tblYqnsOperateMapper.insert(newoper);
							
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("22222");
							newoper.setSsmk("我的任务");
							newoper.setRwmc("我的任务 ");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1399");
							newoper.setSsmk("审计工作记录");
							newoper.setRwmc("审计工作记录");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1351");
							newoper.setSsmk("我的底稿");
							newoper.setRwmc("我的底稿 ");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1400");
							newoper.setSsmk("审计结果确认单");
							newoper.setRwmc("审计结果确认单");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						   
						    
						    
						    
						    newoper= new TblYqnsOperate();
							newoper.setSsmkid("1402");
							newoper.setSsmk("审计项目运行情况表");
							newoper.setRwmc("审计项目运行情况表");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(user.getStaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						    
						    
						}
					}
					
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
			
		break;	
				
				
		case "1498":
			
			//根据当前任务查询下一步任务，并保存消息表
			if(tb.getFormid()!=null) {
				findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
				if(findrwcount<=1) {
					try {
						entity = implementPlanMapper.selectById(tb.getFormid().toString());
						newoper= new TblYqnsOperate();
						newoper.setSsmkid("1342");
						newoper.setSsmk("交换意见稿");
						newoper.setRwmc("交换意见稿上传 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
				break;		
			
			case "22222":
			
			//根据当前任务查询下一步任务，并保存消息表
			if(tb.getFormid()!=null) {
				findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
				if(findrwcount<=1) {
					try {
						entity = implementPlanMapper.selectById(tb.getFormid().toString());
						newoper= new TblYqnsOperate();
						newoper.setSsmkid("1342");
						newoper.setSsmk("交换意见稿");
						newoper.setRwmc("交换意见稿上传 ");
						newoper.setFormid(entity.getId());
						newoper.setFormname(entity.getProjectName());
						newoper.setOperid(RandomUtil.uuBigDecimalId());
					    newoper.setParentid(tb.getOperid());
					    newoper.setStatus(0);
					    newoper.setCreatestaffid(staff.getStaffid());
					    newoper.setCreatename(staff.getRealname());
					    newoper.setCreatedate(new Date());
					    newoper.setRwuserid(entity.getZsstaffid().toString());
					    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
					    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
					    tblYqnsOperateMapper.insert(newoper);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
			
				break;		
				
			case "1400":
				
				//根据当前任务查询下一步任务，并保存消息表
				if(tb.getFormid()!=null) {
					try {
						entity = implementPlanMapper.selectById(tb.getFormid().toString());
						if(entity.getZsstaffid()!=null && entity.getZsstaffid().toString().equals(staff.getStaffid().toString())) {
							tblYqnsOperateMapper.updateBystatus(tb.getFormid().toString(), null);
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1342");
							newoper.setSsmk("交换意见稿");
							newoper.setRwmc("交换意见稿上传 ");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(entity.getZsstaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						}else {
							findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
							if(findrwcount<=1) {
									newoper= new TblYqnsOperate();
									newoper.setSsmkid("1342");
									newoper.setSsmk("交换意见稿");
									newoper.setRwmc("交换意见稿上传 ");
									newoper.setFormid(entity.getId());
									newoper.setFormname(entity.getProjectName());
									newoper.setOperid(RandomUtil.uuBigDecimalId());
								    newoper.setParentid(tb.getOperid());
								    newoper.setStatus(0);
								    newoper.setCreatestaffid(staff.getStaffid());
								    newoper.setCreatename(staff.getRealname());
								    newoper.setCreatedate(new Date());
								    newoper.setRwuserid(entity.getZsstaffid().toString());
								    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
								    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
								    tblYqnsOperateMapper.insert(newoper);
								}
							
						}
					
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				break;	
				
			case "1351":
				
				//根据当前任务查询下一步任务，并保存消息表
				if(tb.getFormid()!=null) {
					try {
						
						Integer dg = implementPlanMapper.selectCountBydgnei(tb.getFormid().toString());
						Integer wc = implementPlanMapper.selectCountBydgwc(tb.getFormid().toString());
						if(dg!=wc) {
							returnstatus=1;
							break;
						}
							entity = implementPlanMapper.selectById(tb.getFormid().toString());
							if(entity.getZsstaffid()!=null && entity.getZsstaffid().toString().equals(staff.getStaffid().toString())) {
								tblYqnsOperateMapper.updateBystatus(tb.getFormid().toString(), null);
								newoper= new TblYqnsOperate();
								newoper.setSsmkid("1342");
								newoper.setSsmk("交换意见稿");
								newoper.setRwmc("交换意见稿上传 ");
								newoper.setFormid(entity.getId());
								newoper.setFormname(entity.getProjectName());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid());
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(entity.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							}else {
								findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
								if(findrwcount<=1) {
										newoper= new TblYqnsOperate();
										newoper.setSsmkid("1342");
										newoper.setSsmk("交换意见稿");
										newoper.setRwmc("交换意见稿上传 ");
										newoper.setFormid(entity.getId());
										newoper.setFormname(entity.getProjectName());
										newoper.setOperid(RandomUtil.uuBigDecimalId());
									    newoper.setParentid(tb.getOperid());
									    newoper.setStatus(0);
									    newoper.setCreatestaffid(staff.getStaffid());
									    newoper.setCreatename(staff.getRealname());
									    newoper.setCreatedate(new Date());
									    newoper.setRwuserid(entity.getZsstaffid().toString());
									    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
									    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
									    tblYqnsOperateMapper.insert(newoper);
									}
								
							}
						
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				break;
					
				case "1399":
				
				//根据当前任务查询下一步任务，并保存消息表
				if(tb.getFormid()!=null) {
					findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
					if(findrwcount<=1) {
						try {
							entity = implementPlanMapper.selectById(tb.getFormid().toString());
							newoper= new TblYqnsOperate();
							newoper.setSsmkid("1342");
							newoper.setSsmk("交换意见稿");
							newoper.setRwmc("交换意见稿上传 ");
							newoper.setFormid(entity.getId());
							newoper.setFormname(entity.getProjectName());
							newoper.setOperid(RandomUtil.uuBigDecimalId());
						    newoper.setParentid(tb.getOperid());
						    newoper.setStatus(0);
						    newoper.setCreatestaffid(staff.getStaffid());
						    newoper.setCreatename(staff.getRealname());
						    newoper.setCreatedate(new Date());
						    newoper.setRwuserid(entity.getZsstaffid().toString());
						    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
						    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
						    tblYqnsOperateMapper.insert(newoper);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
				
					break;	
					
				case "1402":
					
					//根据当前任务查询下一步任务，并保存消息表
					if(tb.getFormid()!=null) {
						findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
						if(findrwcount<=1) {
							try {
								entity = implementPlanMapper.selectById(tb.getFormid().toString());
								newoper= new TblYqnsOperate();
								newoper.setSsmkid("1342");
								newoper.setSsmk("交换意见稿");
								newoper.setRwmc("交换意见稿上传 ");
								newoper.setFormid(entity.getId());
								newoper.setFormname(entity.getProjectName());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid());
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(entity.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					}
					
						break;	
						
				case "1353":
					
					//根据当前任务查询下一步任务，并保存消息表
					if(tb.getFormid()!=null) {
						findrwcount = tblYqnsOperateMapper.findrwcount(tb.getFormid().toString());
						if(findrwcount<=1) {
							try {
								entity = implementPlanMapper.selectById(tb.getFormid().toString());
								newoper= new TblYqnsOperate();
								newoper.setSsmkid("1342");
								newoper.setSsmk("交换意见稿");
								newoper.setRwmc("交换意见稿上传 ");
								newoper.setFormid(entity.getId());
								newoper.setFormname(entity.getProjectName());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid());
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(entity.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					}
					
						break;
						
				case "1342":
					
					try {
						Integer st = implementPlanMapper.selectCountBysjyjsSt(tb.getFormid().toString());
						if(st<=0) {
							returnstatus=1;
							break;
						}
						
						plan = implementPlanMapper.selectById(tb.getFormid().toString());
						if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
				        	 TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
				                    .eq("DELETED", 0)
				                    .eq("ID", plan.getXmapbid()));
				        	if(fund!=null) {
				        		newoper= new TblYqnsOperate();
								newoper.setSsmkid("1343");
								newoper.setSsmk("审计报告定稿");
								newoper.setRwmc("审计报告定稿上传 ");
								newoper.setFormid(tb.getFormid());
								newoper.setFormname(tb.getFormname());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
								newoper.setParentid(tb.getOperid());
								newoper.setStatus(0);
								newoper.setCreatestaffid(staff.getStaffid());
								newoper.setCreatename(staff.getRealname());
								newoper.setCreatedate(new Date());
								newoper.setRwuserid(fund.getFpslkryid());
								newoper.setOrgid(staff.getCurrentOrg().getOrgid());
								newoper.setOrgname(staff.getCurrentOrg().getOrgname());
								tblYqnsOperateMapper.insert(newoper);
				        		
				        	}
				        }
						
						if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
				        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
				                     .eq("DELETED", 0)
				                     .eq("ID", plan.getXmapbid()));
				        	 if(endin!=null) {
				        		 	newoper= new TblYqnsOperate();
									newoper.setSsmkid("1343");
									newoper.setSsmk("审计报告定稿");
									newoper.setRwmc("审计报告定稿上传 ");
									newoper.setFormid(tb.getFormid());
									newoper.setFormname(tb.getFormname());
									newoper.setOperid(RandomUtil.uuBigDecimalId());
									newoper.setParentid(tb.getOperid());
									newoper.setStatus(0);
									newoper.setCreatestaffid(staff.getStaffid());
									newoper.setCreatename(staff.getRealname());
									newoper.setCreatedate(new Date());
									newoper.setRwuserid(endin.getFpslkryid());
									newoper.setOrgid(staff.getCurrentOrg().getOrgid());
									newoper.setOrgname(staff.getCurrentOrg().getOrgname());
									tblYqnsOperateMapper.insert(newoper);
				        	}
				        }
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					break;
						
				case "1343":
					
					//根据当前任务查询下一步任务，并保存消息表
					if(tb.getFormid()!=null) {
						try {
							Integer st = implementPlanMapper.selectCountBysjbgdg(tb.getFormid().toString());
							if(st<=0) {
								returnstatus=1;
								break;
							}
							
							plan = implementPlanMapper.selectById(tb.getFormid().toString());
							if(plan!=null) {
								newoper= new TblYqnsOperate();
								newoper.setSsmkid("1490");
								newoper.setSsmk("问题清单");
								newoper.setRwmc("问题清单上传 ");
								newoper.setFormid(plan.getId());
								newoper.setFormname(plan.getProjectName());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid());
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(plan.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
							    
							    
								newoper= new TblYqnsOperate();
								newoper.setSsmkid("1494");
								newoper.setSsmk("审计建议");
								newoper.setRwmc("审计建议上传 ");
								newoper.setFormid(plan.getId());
								newoper.setFormname(plan.getProjectName());
								newoper.setOperid(RandomUtil.uuBigDecimalId());
							    newoper.setParentid(tb.getOperid());
							    newoper.setStatus(0);
							    newoper.setCreatestaffid(staff.getStaffid());
							    newoper.setCreatename(staff.getRealname());
							    newoper.setCreatedate(new Date());
							    newoper.setRwuserid(plan.getZsstaffid().toString());
							    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
							    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
							    tblYqnsOperateMapper.insert(newoper);
					        }
							
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
						break;
						
						
				case "1490":
					
						try {
							

							plan = implementPlanMapper.selectById(tb.getFormid().toString());
							if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
					        	 TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
					                    .eq("DELETED", 0)
					                    .eq("ID", plan.getXmapbid()));
					        	if(fund!=null) {
					        		newoper= new TblYqnsOperate();
									newoper.setSsmkid("1491");
									newoper.setSsmk("问题整改");
									newoper.setRwmc("问题整改 ");
									newoper.setFormid(tb.getFormid());
									newoper.setFormname(tb.getFormname());
									newoper.setOperid(RandomUtil.uuBigDecimalId());
								    newoper.setParentid(tb.getOperid());
								    newoper.setStatus(0);
								    newoper.setCreatestaffid(staff.getStaffid());
								    newoper.setCreatename(staff.getRealname());
								    newoper.setCreatedate(new Date());
								    newoper.setRwuserid(fund.getFpslkryid());
								    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
								    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
								    tblYqnsOperateMapper.insert(newoper);
					        		
					        	}
					        }
							
							if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
					        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
					                     .eq("DELETED", 0)
					                     .eq("ID", plan.getXmapbid()));
					        	 if(endin!=null) {
					        		 	newoper= new TblYqnsOperate();
										newoper.setSsmkid("1491");
										newoper.setSsmk("问题整改");
										newoper.setRwmc("问题整改 ");
										newoper.setFormid(tb.getFormid());
										newoper.setFormname(tb.getFormname());
										newoper.setOperid(RandomUtil.uuBigDecimalId());
									    newoper.setParentid(tb.getOperid());
									    newoper.setStatus(0);
									    newoper.setCreatestaffid(staff.getStaffid());
									    newoper.setCreatename(staff.getRealname());
									    newoper.setCreatedate(new Date());
									    newoper.setRwuserid(endin.getFpslkryid());
									    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
									    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
									    tblYqnsOperateMapper.insert(newoper);
					        	}
					        }
							
							
						    
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					
						break;
						
				case "1491":
					

						try {
							plan = implementPlanMapper.selectById(tb.getFormid().toString());
							if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
								TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
					                    .eq("DELETED", 0)
					                    .eq("ID", plan.getXmapbid()));
					        	if(fund!=null) {
					        		 newoper= new TblYqnsOperate();
										newoper.setSsmkid("1492");
										newoper.setSsmk("跟踪回访");
										newoper.setRwmc("跟踪回访 ");
										newoper.setFormid(tb.getFormid());
										newoper.setFormname(tb.getFormname());
										newoper.setOperid(RandomUtil.uuBigDecimalId());
									    newoper.setParentid(tb.getOperid());
									    newoper.setStatus(0);
									    newoper.setCreatestaffid(staff.getStaffid());
									    newoper.setCreatename(staff.getRealname());
									    newoper.setCreatedate(new Date());
									    newoper.setRwuserid(fund.getFpslkryid());
									    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
									    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
									    tblYqnsOperateMapper.insert(newoper);
					        		
					        	}
					        }
							
							if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
					        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
					                     .eq("DELETED", 0)
					                     .eq("ID", plan.getXmapbid()));
					        	 if(endin!=null) {
					        		 newoper= new TblYqnsOperate();
										newoper.setSsmkid("1492");
										newoper.setSsmk("跟踪回访");
										newoper.setRwmc("跟踪回访 ");
										newoper.setFormid(tb.getFormid());
										newoper.setFormname(tb.getFormname());
										newoper.setOperid(RandomUtil.uuBigDecimalId());
									    newoper.setParentid(tb.getOperid());
									    newoper.setStatus(0);
									    newoper.setCreatestaffid(staff.getStaffid());
									    newoper.setCreatename(staff.getRealname());
									    newoper.setCreatedate(new Date());
									    newoper.setRwuserid(endin.getFpslkryid());
									    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
									    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
									    tblYqnsOperateMapper.insert(newoper);
					        	}
					        }
							
							
						   
						    
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					
						break;
						
				case "1492":
						
						try {
							plan = implementPlanMapper.selectById(tb.getFormid().toString());
							if(plan!=null && plan.getZykstype()!=null && !plan.getZykstype().equals("基建")) {
								TblYqnsFundAuditProjectEntity fund = fundAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsFundAuditProjectEntity>()
					                    .eq("DELETED", 0)
					                    .eq("ID", plan.getXmapbid()));
					        	if(fund!=null) {
					        		newoper= new TblYqnsOperate();
									newoper.setSsmkid("1493");
									newoper.setSsmk("后续整改");
									newoper.setRwmc("后续整改 ");
									newoper.setFormid(tb.getFormid());
									newoper.setFormname(tb.getFormname());
									newoper.setOperid(RandomUtil.uuBigDecimalId());
								    newoper.setParentid(tb.getOperid());
								    newoper.setStatus(0);
								    newoper.setCreatestaffid(staff.getStaffid());
								    newoper.setCreatename(staff.getRealname());
								    newoper.setCreatedate(new Date());
								    newoper.setRwuserid(fund.getFpslkryid());
								    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
								    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
								    tblYqnsOperateMapper.insert(newoper);
					        		
					        	}
					        }
							
							if(plan!=null && plan.getZykstype()!=null && plan.getZykstype().equals("基建")) {
					        	 TblYqnsEnginAuditProjectEntity endin = enginAuditProjectMapper.selectOne(new QueryWrapper<TblYqnsEnginAuditProjectEntity>()
					                     .eq("DELETED", 0)
					                     .eq("ID", plan.getXmapbid()));
					        	 if(endin!=null) {
					        		 newoper= new TblYqnsOperate();
										newoper.setSsmkid("1493");
										newoper.setSsmk("后续整改");
										newoper.setRwmc("后续整改 ");
										newoper.setFormid(tb.getFormid());
										newoper.setFormname(tb.getFormname());
										newoper.setOperid(RandomUtil.uuBigDecimalId());
									    newoper.setParentid(tb.getOperid());
									    newoper.setStatus(0);
									    newoper.setCreatestaffid(staff.getStaffid());
									    newoper.setCreatename(staff.getRealname());
									    newoper.setCreatedate(new Date());
									    newoper.setRwuserid(endin.getFpslkryid());
									    newoper.setOrgid(staff.getCurrentOrg().getOrgid());
									    newoper.setOrgname(staff.getCurrentOrg().getOrgname());
									    tblYqnsOperateMapper.insert(newoper);
					        	}
					        }
							
						    
						    
						    
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					
						break;
						
		default:
			break;
		}
		return returnstatus;
	}
	
	
	
	
}
