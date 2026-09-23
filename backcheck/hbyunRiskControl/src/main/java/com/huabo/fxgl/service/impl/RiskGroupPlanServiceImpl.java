package com.huabo.fxgl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.mapper.AttachmentMapper;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.RiskAssessmentstdMapper;
import com.huabo.fxgl.mapper.RiskAssplanAttMapper;
import com.huabo.fxgl.mapper.RiskAssplanMapper;
import com.huabo.fxgl.mapper.RiskAssplanOrgMapper;
import com.huabo.fxgl.mapper.RiskAssplanRiskMapper;
import com.huabo.fxgl.mapper.RiskGroupPlanAttMapper;
import com.huabo.fxgl.mapper.RiskInfludegreeMapper;
import com.huabo.fxgl.mapper.RiskLevelmappingMapper;
import com.huabo.fxgl.mapper.RiskPossibilityMapper;
import com.huabo.fxgl.mapper.RiskRiskmarkingMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.mapper.TblRiskGroupPlanMapper;
import com.huabo.fxgl.service.IRiskAssplanOrgService;
import com.huabo.fxgl.service.IRiskAssplanService;
import com.huabo.fxgl.service.IRiskGroupPlanService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import cn.hutool.core.date.DateUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@Service
public class RiskGroupPlanServiceImpl extends ServiceImpl<TblRiskGroupPlanMapper, TblRiskGroupplan> implements IRiskGroupPlanService {
    @Autowired
    private RiskAssessmentstdServiceImpl riskAssessmentstdServiceImpl;

    @Autowired
    private TblRiskGroupPlanMapper tblRiskGroupPlanMapper ;
    
    
    @Autowired
    private  RiskPossibilityMapper riskPossibilityMapper ;
    
    @Autowired
    private  RiskInfludegreeMapper riskInfludegreeMapper;
    
    @Autowired
    private  RiskAssplanMapper riskAssplanMapper;
    
    @Autowired
    private  RiskLevelmappingMapper riskLevelmappingMapper;
    @Autowired
    private RiskAssessmentstdMapper riskAssessmentstdMapper;
    
    
    @Autowired
    private RiskRiskmarkingMapper riskRiskmarkingMapper;

    @Autowired
    private OrganizationMapper organizationMapper;
    
    @Resource
    private StaffMapper staffMapper;
    
    @Resource
    private  RiskAssplanOrgMapper riskAssplanOrgMapper;
    
    @Resource
    private UserProvider userProvider;
    
    
    @Resource
    private   AttachmentMapper attachmentMapper;

    @Resource
    private  RiskAssplanAttMapper riskAssplanAttMapper;
    
    @Override
    public PageInfo<TblRiskGroupplan> findAll(TblStaffUtil staffUtil,TblRiskGroupplan tblRiskGroupplan, BigDecimal orgid,Integer pageno,Integer pagesize) throws Exception {
/*
     @param plancode 计划编号
     * @param planName 计划名称
     * @param endDate  计划结束时间
     * @param starDate 计划开始时间
     * @param planType  计划类型
     * @param planStatus 计划状态
 */
        //风险计划条件查询
        QueryWrapper<TblRiskGroupplan> queryWrapper = new QueryWrapper();
        // 如果提供了组织ID，添加单位（组织）的查询条件
        if (StringUtils.isNotBlank(String.valueOf(orgid)))
            queryWrapper.eq("unit", orgid);
        // 如果风险评估计划对象不为空，并且提供了计划编号，添加计划编号的模糊匹配条件
        if (tblRiskGroupplan != null && tblRiskGroupplan.getPlancode() != null && tblRiskGroupplan.getPlancode().length() > 0) {
            queryWrapper.like("plancode", tblRiskGroupplan.getPlancode());
        }
        // 如果风险评估计划对象不为空，并且提供了计划名称，添加计划名称的模糊匹配条件
        if (tblRiskGroupplan != null && tblRiskGroupplan.getPlanName() != null && tblRiskGroupplan.getPlanName().length() > 0) {
            queryWrapper.like("planname", tblRiskGroupplan.getPlanName());
        }
        // 如果风险评估计划对象不为空，并且提供了计划类型，添加计划类型的精确匹配条件
        if (tblRiskGroupplan != null && tblRiskGroupplan.getPlanType() != null && tblRiskGroupplan.getPlanType().length() > 0) {
            queryWrapper.eq("plantype", tblRiskGroupplan.getPlanType());
        }
        // 如果风险评估计划对象不为空，并且提供了计划状态，添加计划状态的精确匹配条件
        if (tblRiskGroupplan != null && tblRiskGroupplan.getPlanStatus() != null && tblRiskGroupplan.getPlanStatus().length() > 0) {
            queryWrapper.eq("planstatus", tblRiskGroupplan.getPlanStatus());
        }
		if (tblRiskGroupplan != null && tblRiskGroupplan.getStatus() != null && tblRiskGroupplan.getStatus().length() > 0) {
			queryWrapper.eq("status", tblRiskGroupplan.getStatus());
		}
		
		if (tblRiskGroupplan != null && tblRiskGroupplan.getToIssued() != null && tblRiskGroupplan.getToIssued().compareTo(new BigDecimal(1))==0) {
			queryWrapper.eq("toIssued", tblRiskGroupplan.getToIssued());
		}
        if (tblRiskGroupplan != null) {
            // 如果风险评估计划对象不为空，并且提供了开始日期，添加开始日期的大于等于条件
            if (tblRiskGroupplan.getStartDate() != null) {
                queryWrapper.ge("startdate", tblRiskGroupplan.getStartDate());
            }
            // 如果风险评估计划对象不为空，并且提供了结束日期，添加结束日期的小于等于条件
            if (tblRiskGroupplan.getEndDate() != null) {
                queryWrapper.le("enddate", tblRiskGroupplan.getEndDate());
            }
        }
//        queryWrapper.orderByDesc("plancode");
        // 按照风险评估计划ID降序排序
        queryWrapper.orderByDesc("id");
        String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "UNIT", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
        com.github.pagehelper.PageInfo<TblRiskGroupplan> pageInfo=pageInfo = PageMethod.startPage(pageno, pagesize)
				.doSelectPageInfo(() -> tblRiskGroupPlanMapper.findAll(sql,queryWrapper));
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(pageInfo.getList())){
			pageInfo.getList().forEach(entity->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(entity,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,entity ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
			
		}
        return pageInfo;
    }
  

    /**
     * 查询同个公司下风险评估计划的编号重复情况
     * @param plancode
     * @param
     * @return
     */
    @Override
    public Integer selectTblRiskGroupplanNumber(String plancode, String orgid) throws Exception {

       /* SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID= ${orgid}  " +
        " AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID*/
        // 改为java递归处理（多数据融合）start
        /*List<Organization> list = organizationMapper.selectList(new QueryWrapper<>());
        List<Organization> ids = list.stream().filter(o -> new BigDecimal(orgid).equals(o.getFatherOrgid())).collect(Collectors.toList());
        String idsStr = "";
        if (ids.size() > 0 ){
            List<BigDecimal> idList = new ArrayList<>();
            for (Organization id : ids) {
                if(id.getOrgtype().equals(0)){
                    idList.add(id.getFatherorgid());
                }

            }
            if (!idList.isEmpty()){
                List<Organization> subDepartments = new ArrayList<>();
                List<BigDecimal> bigDecimals = getSubDepartments(new BigDecimal(orgid), list,subDepartments).stream().map(Organization::getFatherorgid).collect(Collectors.toList());
                idList.addAll(bigDecimals);
            }
            if(null!=idList && idList.size()>0) {
                for (BigDecimal id : idList) {
                    idsStr += id + ",";
                }
                idsStr =  idsStr.substring(0,idsStr.length()-1);
            }
        }*/

        String idsStr = selectChidrenIdStrsByFatherOrgId(orgid);



        Integer  result=tblRiskGroupPlanMapper.findBysqlObj(plancode,orgid,idsStr);
        //Integer  result= riskAssplanMapper.findBysqlObj(plancode,orgid);

        return result ;
    }

    public String selectChidrenIdStrsByFatherOrgId(String orgId) throws Exception {

        List<String> orgIdList = this.organizationMapper.selectChildrenIdListByFatherOrgId(orgId);
        if(orgIdList == null || orgIdList.size() == 0){
            return orgId;
        }
        String orgIdIdStrs = String.join(",", orgIdList);
        return this.getChidrenIdStrsByFatherOrgIds(orgIdIdStrs,orgId+","+orgIdIdStrs);
    }


    private String getChidrenIdStrsByFatherOrgIds(String orgIdIdStrs, String totalIds) throws Exception {
        List<String> orgIdList = this.organizationMapper.selectChildrenIdListByFatherOrgId(orgIdIdStrs);
        if(orgIdList == null || orgIdList.size() == 0){
            return totalIds;
        }
        orgIdIdStrs = String.join(",", orgIdList);
        return this.getChidrenIdStrsByFatherOrgIds(orgIdIdStrs,totalIds+","+orgIdIdStrs);
    }

    public List<Organization> getSubDepartments(BigDecimal orgid, List<Organization> allList, List<Organization> subDepartments ) {

        for (Organization risk : allList) {
            if (risk.getFatherorgid().equals(orgid)) {
                subDepartments.add(risk);
                // 递归查询子部门的子部门
                subDepartments.addAll(getSubDepartments(risk.getOrgid(), allList,subDepartments));
            }
        }
        return subDepartments;
    }

 
	@Override
	public JsonBean get_riskpgplan_no(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Organization org=organizationMapper.selectById(loginStaff.getLinkOrg().getOrgid());
        //拼接编号
        String yearStr = String.valueOf(DateUtil.thisYear());
        String plancode = "集团评估计划-"+org.getOrgnumber()+"-"+yearStr+"-";
        Integer maxno = this.tblRiskGroupPlanMapper.get_plan_no("'"+plancode+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        plancode = plancode+maxno;
        
		return ResponseFormat.retParam(1, 200, plancode);
	}
	
 
	@Override
	public TblRiskGroupplan saveEntity(TblRiskGroupplan plan) throws Exception {
		// TODO Auto-generated method stub
		tblRiskGroupPlanMapper.insert(plan);
		return plan;
	}
	
	@Override
	public TblRiskGroupplan updateEntity(TblRiskGroupplan plan) throws Exception {
		// TODO Auto-generated method stub
		tblRiskGroupPlanMapper.updateById(plan);
		return plan;
	}

	@Override
	public JsonBean toIssued(String id, String staffids, String staffnames) throws Exception {
		try {
			     TblRiskGroupplan plan=tblRiskGroupPlanMapper.selectById(id);
            	  //修改集团评估计划下发状态、时间、下发人员
			      TblRiskGroupplan entity=tblRiskGroupPlanMapper.selectById(id);
            	  entity.setIssuedStaffid(staffids);
            	  entity.setIssuedStaffName(staffnames);
            	  Map<String, String> map=setUnitName(staffids);
            	  entity.setLssuedUnit(map.get("id"));
            	  entity.setIssuedUnitName(map.get("name"));
            	  entity.setToIssued(new BigDecimal(1));//确认下发状态
            	  entity.setIssueddate(new Date()); //下发时间
            	  tblRiskGroupPlanMapper.updateById(entity);
            	//将下发人员信息填充到评估计划中，通过人员信息循环新建子公司评估计划信息
            	  if(StringUtils.isNotBlank(staffids)){
            		  for(String s:staffids.split(",")){
            			  Staff  staff=staffMapper.selectById(s);
            			  BigDecimal orgid=getDeptLinkCompanyNameByDeptId(staff.getOrgid());
            			  String orgname=organizationMapper.selectById(orgid).getOrgname();
            			  //已下发过的数据不能再新建  子公司季度数据
            			  System.out.println("****"+riskAssplanMapper.getCountById(entity.getId(), s)+"%%%");
                          if(riskAssplanMapper.getCountById(entity.getId(), s)==0){
                        	  RiskAssplan tblRiskAssplan=new RiskAssplan();
                              tblRiskAssplan.setPlanName(entity.getPlanName());
                              tblRiskAssplan.setPlancode(get_planCode(orgid.toString()));
                              tblRiskAssplan.setRecorddate(LocalDateTime.now());
                              tblRiskAssplan.setUnit(orgid.toString());
                              tblRiskAssplan.setCreatestaffid(staff.getStaffid());
                              tblRiskAssplan.setLinkdeptid(staff.getOrgid());
                              tblRiskAssplan.setEndDate(entity.getEndDate());
                              tblRiskAssplan.setStartDate(entity.getStartDate());
                              tblRiskAssplan.setPlanStatus(RiskAssplan.PLANSTATUS_WKS);
                              tblRiskAssplan.setPlandes(entity.getPlandes());
                              tblRiskAssplan.setAssessmentstd(copyRiskAssessmentstd(entity.getAssstdid().toString(),orgid));
                              tblRiskAssplan.setPlanType(entity.getPlanType());
                              tblRiskAssplan.setRecorder(staff.getRealname());
                              tblRiskAssplan.setSecrectLevelId(entity.getSecrectLevelId());//下发密级级别
                              //当前用户选择的组织
                              tblRiskAssplan.setOrganization(organizationMapper.selectById(orgid));
                              tblRiskAssplan.setContent(entity.getContent());
                              tblRiskAssplan.setGroupId(entity.getId());
                              tblRiskAssplan.setCreatetime(new Date());
                              tblRiskAssplan.setIsgroup(new BigDecimal("1"));//集团下发数据的标记
                              riskAssplanMapper.insert(tblRiskAssplan);
                              RiskAssplanOrg riskAssplanOrg = new RiskAssplanOrg();
                              //向riskAssplanOrg中间表添加数据 评估编号与当前用户选择的组织的中间表
                              riskAssplanOrg.setAssplanid(tblRiskAssplan.getAssplanid());
                              riskAssplanOrg.setOrgid(orgid);
                              riskAssplanOrgMapper.insert(riskAssplanOrg);
                              //附件处理-1.查询集团附件信息，新增附件信息到子公司
                              List<Attachment> tblAttachments =attachmentMapper.getAttList("TBL_RISK_GROUPPLAN_ATT", "id", entity.getId());
                              for(Attachment a:tblAttachments){
                            	  Attachment att=new Attachment(a);
                            	 att.setUploadtime(LocalDateTime.now());
                            	 att.setUploader(staff.getRealname());
                            	 attachmentMapper.insert(att); //新增附件信息
                            	 //插入到评估计划附件表中
                            	 RiskAssplanAtt riskAssplanAtt = new RiskAssplanAtt(tblRiskAssplan.getAssplanid(), att.getAttid());
                            	  riskAssplanAttMapper.insert(riskAssplanAtt);
                              }
                          }
                    	 
            		  }
            	  }
             // }
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  return ResponseFormat.retParam(1, 200, null);
	}
	
	//编号赋值
	public String get_planCode(String orgid) throws Exception {
        //拼接编号
		  Organization org=organizationMapper.selectById(orgid);
	        //拼接编号
	        String yearStr = String.valueOf(DateUtil.thisYear());
	        String plancode = "风险评估计划-"+org.getOrgnumber()+"-"+yearStr+"-";
	        Integer maxno = this.riskAssplanMapper.get_riskpgplan_no("'"+plancode+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        plancode = plancode+maxno;
        
		return plancode;
	}
	
	
	//赋值评估标准
	public RiskAssessmentstd copyRiskAssessmentstd(String id,BigDecimal orgid) throws Exception {
		RiskAssessmentstd   ass=null;
		try {
                RiskAssessmentstd   assessMentsTd=riskAssessmentstdMapper.selectById(id);
                //根据评估标准名称 描述  公司id判断，已存在不再新增
                ass = riskAssessmentstdMapper.selectNumberByInfo(assessMentsTd.getAssname(),assessMentsTd.getAssdes(),orgid);
     			if(ass==null){
                ass=new RiskAssessmentstd(assessMentsTd);
                ass.setAssnumber(riskAssessmentstdServiceImpl.get_NewCode(orgid)); 
                ass.setCompanyid(orgid);
                riskAssessmentstdMapper.insert(ass);
                List<RiskPossibility> rList=assessMentsTd.getPossibilities();
                List<RiskInfludegree> dList=assessMentsTd.getRiskInfludegrees();
                for(RiskPossibility r:rList){
                	RiskPossibility entity=new RiskPossibility(r);
                	entity.setAssstdid(ass.getAssstdid());
                	riskPossibilityMapper.insert(entity);
                }
                for(RiskInfludegree r:dList){
                	RiskInfludegree entity=new RiskInfludegree(r);
                	entity.setAssstdid(ass.getAssstdid());
                	riskInfludegreeMapper.insert(entity);
                	
                	RiskLevelmapping level=riskLevelmappingMapper.getByInfluId(r.getDegreeid().toString());
                	RiskLevelmapping l=new RiskLevelmapping(level);
                	l.setAssstdid(ass.getAssstdid());
                	l.setInfludegree(entity.getDegreeid().toString());
                	riskLevelmappingMapper.insert(l);
                }
                
     	} 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return ass;
		
	}
	
	public Map<String, String> setUnitName(String staffids)throws Exception{
		Map<String, String> map=new HashMap<String, String>();
		try {
		StringBuffer buffId=new StringBuffer();
		StringBuffer buffName=new StringBuffer();
		String[] stList=staffids.split(",");
		for(String s :stList){
			Staff staff=staffMapper.selectById(s);
			BigDecimal id=getDeptLinkCompanyNameByDeptId(staff.getOrgid());
			String name=organizationMapper.selectById(id).getOrgname();
			buffId.append(id+",");
			buffName.append(name+",");
		}
		int index = buffId.lastIndexOf(",");
		int index2 = buffName.lastIndexOf(",");
		 if (index != -1) {
			 buffId.deleteCharAt(index);
	        }
		 if (index2 != -1) {
			 buffName.deleteCharAt(index2);
	        }
		 map.put("id", buffId.toString());
		 map.put("name", buffName.toString());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}
	
	public BigDecimal getDeptLinkCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		Organization fatherOrg = this.organizationMapper.selectById(deptId);
		if(fatherOrg.getOrgtype().compareTo(new BigDecimal(0))==0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgid();
	}
	
	public BigDecimal getCompanyNameByDeptId(BigDecimal deptId) throws Exception {
		TblOrganization fatherOrg = this.organizationMapper.selectFatherOrgIdInfoByOrgId(deptId);
		if(fatherOrg.getOrgtype() == 0) {
			return this.getCompanyNameByDeptId(fatherOrg.getOrgid());
		}
		return fatherOrg.getOrgid();
	}

	@Override
	public TblRiskGroupplan getOneDetail(String planid) throws Exception{
		// TODO Auto-generated method stub
		TblRiskGroupplan plan=null;
		try {
			plan=tblRiskGroupPlanMapper.getOneDetail(planid);
			  FiexibleNameAssignment ment=new FiexibleNameAssignment();
	        	//对灵活字段中的姓名名称及机构名称赋值
				fieldOrgStaffId item=new fieldOrgStaffId();
				BeanUtils.copyProperties(plan,item); 
				fieldOrgStaffName nameEntity=ment.setOpenName(item);
				BeanUtils.copyProperties(nameEntity,plan); 
				item=null;
				nameEntity=null;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return plan;
	}
}
