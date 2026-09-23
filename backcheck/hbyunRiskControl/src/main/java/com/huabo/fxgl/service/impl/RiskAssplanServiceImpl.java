package com.huabo.fxgl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.mapper.OpenQueryMapperSqlConfig;
import com.huabo.fxgl.mapper.OrganizationMapper;
import com.huabo.fxgl.mapper.RiskAssplanMapper;
import com.huabo.fxgl.mapper.RiskAssplanRiskMapper;
import com.huabo.fxgl.mapper.RiskRiskmarkingMapper;
import com.huabo.fxgl.service.IRiskAssplanService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import cn.hutool.core.date.DateUtil;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblTransferWorkUtils;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.hbfk.util.user.UserProvider;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
@Service
public class RiskAssplanServiceImpl extends ServiceImpl<RiskAssplanMapper, RiskAssplan> implements IRiskAssplanService {
    @Autowired
    private RiskAssplanRiskServiceImpl assplanRiskService;

    @Autowired
    private RiskAssplanMapper riskAssplanMapper;
    
    @Autowired
    private RiskRiskmarkingMapper riskRiskmarkingMapper;

    @Autowired
    private OrganizationMapper organizationMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public PageInfo<RiskAssplan> findUserAndRiskLevelById(TblStaffUtil staffUtil, RiskRiskmarking riskmarking, Find find, BigDecimal orgid,Integer pageNo,Integer pageSize) throws Exception {
        //风险任务条件查询
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1", "1");
        // 如果提供了计划编号，添加计划编号的模糊匹配条件
        if (find != null && StringUtils.isNotEmpty(find.getCode())) {
            queryWrapper.like("r.plancode", find.getCode());
        }
        // 如果提供了计划名称，添加计划名称的模糊匹配条件
        if (find != null && StringUtils.isNotEmpty(find.getName())) {
            queryWrapper.like("r.planname", find.getName());
        }
        // 如果提供了开始日期和结束日期，添加日期范围条件
        if (find != null) {
            if (find.getStartDate() != null&&StringUtils.isNotEmpty(find.getStartDate())) {
                queryWrapper.ge("r.startdate", LocalDate.parse(find.getStartDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
            }
            if (find.getEndDate() != null&&StringUtils.isNotEmpty(find.getEndDate())) {
                queryWrapper.le("r.enddate", LocalDate.parse(find.getEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
            }
        }
        // 如果提供了评估状态，添加状态条件
        if (find != null && find.getState() != null&&StringUtils.isNotBlank(find.getState())) {
            queryWrapper.eq("rbs.asssatus", find.getState());
        }
        // 如果提供了组织ID，添加组织ID条件
        if (orgid != null) {
            queryWrapper.eq("unit", orgid);
        }
        queryWrapper.orderByDesc("r.assplanid");
/*
		String sqlCount="select count(*) from ("+sql+")";
		if("MySql".equals(SysConfig.get("databaseType"))){
			sqlCount+=" AS T2";
 */
        
    //    String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "r.UNIT", "r.LINKDEPTID", "r.CREATESTAFFID", "r.SECRECTLEVELID", "r.STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
        
        com.github.pagehelper.PageInfo<RiskAssplan> pageInfo = PageMethod.startPage(pageNo, pageSize)
				.doSelectPageInfo(() -> baseMapper.findRiskAssplanRiskfxgl(staffUtil.getStaffid(), null, queryWrapper));
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(pageInfo.getList())){
			pageInfo.getList().forEach(riskAssplan->{
				try {
					//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(riskAssplan,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,riskAssplan ); 
					item=null; // 处理并解除引用
					nameEntity=null; // 处理并解除引用
					
					
					 List<RiskAssplanRisk> assPlanRisks = riskAssplan.getRiskAssplanRiskList();
			            // 创建一个新的列表，用于存储风险标记
			            List<RiskRiskmarking> list = new ArrayList();
			            // 遍历风险评估计划的风险标记列表
			            for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {
			                // 获取当前风险评估计划风险对象关联的风险标记集合
			                Set<RiskRiskmarking> markings = riskAssPlanRisk.getTblRiskRiskMarking();
			                // 如果风险标记集合不为空且有元素
			                if (markings != null && markings.size() > 0)
			                    // 将风险标记集合添加到列表中
			                    list.addAll(markings);
			                Collections.sort(list, new Comparator<RiskRiskmarking>() {
			                    @Override
			                    public int compare(RiskRiskmarking o1, RiskRiskmarking o2) {
			                        return o1.getAsssatus().compareTo(o2.getAsssatus());
			                    }
			                });
			            }
			            if (list.size() > 0) {
			                for (RiskRiskmarking riskMarking : list) {
			                    // 如果当前的风险标记对象（riskMarking）不为空
			                    if (riskMarking != null && riskMarking.getStaff().getStaffid().equals(staffUtil.getStaffid())) {
			                        // 更新风险评估计划（riskAssplan）的状态
			                        // 如果风险标记的评估状态（riskMarking.getAsssatus()）为null，则状态设置为"0"
			                        riskAssplan.setStatus(null == riskMarking.getAsssatus() ? String.valueOf(0) : riskMarking.getAsssatus().toString());
			                    }
			                }
			            }
			            
			            
			            // 设置风险评估计划的单位名称和评估状态
//			          System.out.println(riskAssplan.getAssplanid() + " / " + riskAssplan.getStatus());
			          riskAssplan.setUnit(null != riskAssplan.getOrganization() ? riskAssplan.getOrganization().getOrgname() : null);
			          riskAssplan.setOrganization(null);
			          riskAssplan.setRiskAssplanRiskList(null);
			          if (null != riskAssplan.getStatus()) {
			              switch (riskAssplan.getStatus()) {
			                  case "0": riskAssplan.setStatus("未评估"); break;
			                  case "1": riskAssplan.setStatus("已保存"); break; 
			                  case "2": riskAssplan.setStatus("已评估"); break;
			                  default: riskAssplan.setStatus("未知");
			              }
			          } else {
			              riskAssplan.setStatus("未知");
//			              System.out.println(riskAssplan);
			          }
			          this.setPingguStatusByPlanId(riskAssplan);
					
					
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
			
		}
        return pageInfo;
    }



    @Override
    public PageInfo<RiskAssplan> findAll(RiskAssplan riskAssplan, BigDecimal orgid,Integer pageno,Integer pagesize,TblStaffUtil staffUtil) throws Exception{
/*
     @param plancode 计划编号
     * @param planName 计划名称
     * @param endDate  计划结束时间
     * @param starDate 计划开始时间
     * @param planType  计划类型
     * @param planStatus 计划状态
 */
        //风险计划条件查询
        QueryWrapper<RiskAssplan> queryWrapper = new QueryWrapper();
        // 如果提供了组织ID，添加单位（组织）的查询条件
        if (StringUtils.isNotBlank(String.valueOf(orgid)))
            queryWrapper.eq("unit", orgid);
        // 如果风险评估计划对象不为空，并且提供了计划编号，添加计划编号的模糊匹配条件
        if (riskAssplan != null && riskAssplan.getPlancode() != null && riskAssplan.getPlancode().length() > 0) {
            queryWrapper.like("plancode", riskAssplan.getPlancode());
        }
        // 如果风险评估计划对象不为空，并且提供了计划名称，添加计划名称的模糊匹配条件
        if (riskAssplan != null && riskAssplan.getPlanName() != null && riskAssplan.getPlanName().length() > 0) {
            queryWrapper.like("planname", riskAssplan.getPlanName());
        }
        // 如果风险评估计划对象不为空，并且提供了计划类型，添加计划类型的精确匹配条件
        if (riskAssplan != null && riskAssplan.getPlanType() != null && riskAssplan.getPlanType().length() > 0) {
            queryWrapper.eq("plantype", riskAssplan.getPlanType());
        }
        // 如果风险评估计划对象不为空，并且提供了计划状态，添加计划状态的精确匹配条件
        if (riskAssplan != null && riskAssplan.getPlanStatus() != null && riskAssplan.getPlanStatus().length() > 0) {
            queryWrapper.eq("planstatus", riskAssplan.getPlanStatus());
        }
		if (riskAssplan != null && riskAssplan.getAprstatus() != null && riskAssplan.getAprstatus().length() > 0) {
			queryWrapper.eq("aprstatus", riskAssplan.getAprstatus());
		}
        if (riskAssplan != null) {
            // 如果风险评估计划对象不为空，并且提供了开始日期，添加开始日期的大于等于条件
            if (riskAssplan.getStartDate() != null) {
                queryWrapper.ge("startdate", riskAssplan.getStartDate());
            }
            // 如果风险评估计划对象不为空，并且提供了结束日期，添加结束日期的小于等于条件
            if (riskAssplan.getEndDate() != null) {
                queryWrapper.le("enddate", riskAssplan.getEndDate());
            }
        }
//        queryWrapper.orderByDesc("plancode");
        // 按照风险评估计划ID降序排序
        queryWrapper.orderByDesc("ASSPLANID");
		 String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), true, "UNIT", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", 
				 "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
				 
        com.github.pagehelper.PageInfo<RiskAssplan> pageInfo = PageMethod.startPage(pageno, pagesize)
				.doSelectPageInfo(() -> riskAssplanMapper.selectAllList(queryWrapper,sql));
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
					StringBuffer buffer=new StringBuffer();
					buffer.append("[");
					List<String> list=riskRiskmarkingMapper.findList(entity.getAssplanid());
                    for(String str:list){
                    	buffer.append("{\"formId\":"+entity.getAssplanid()+",\"distributionTitle\":\""+entity.getPlanName()+"\",\"reciver\":"+str+",\"isread\":0,\"moduleType\":\"fxgk\"}");
                    }
                    entity.setJsonString(buffer.append("]").toString());
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			} );
			
		}
        return pageInfo;
    }

//    @Override
//    public List<RiskAssplanRisk> get(BigDecimal planid) {
//        return  baseMapper.selectById(planid);
//
//    }


    /**
     * 查找评估结果页面的分页详情信息
     *
     * @param page 分页
     * @param find 一个工具类
     * @return
     * @Date 2022/8/10
     * @author wanghongtuo
     * @version 1.0.1
     */
    @Override
    public PageInfo<RiskAssplan> findRiskAssPlanPageDetails(Find find,Integer pageno,Integer pagesize, TblStaffUtil staffUtil,Integer authorityType) {
        com.github.pagehelper.PageInfo<RiskAssplan> pageBean = PageMethod.startPage(pageno, pagesize)
				.doSelectPageInfo(() -> baseMapper.selectListByPageInfo(staffUtil,find,authorityType));
        List<RiskAssplan> recordList = pageBean.getList();
        // 遍历记录列表，为每个风险评估计划计算风险级别数量
        for (RiskAssplan riskAssplan : recordList) {
            // 创建新的查询条件包装器，用于查询与当前风险评估计划关联的风险评估计划风险
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("ASSPLANID", riskAssplan.getAssplanid());
            // 查询与当前风险评估计划关联的风险评估计划风险列表
            List<RiskAssplanRisk> assPlanRisks = assplanRiskService.list(queryWrapper1);
            // 遍历风险评估计划风险列表，根据风险级别更新风险评估计划的计数
            for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {
            	if(StringUtils.isNotBlank(riskAssPlanRisk.getRisklevel())){
                switch (Integer.parseInt(riskAssPlanRisk.getRisklevel())) {
                    case 1:
                        riskAssplan.setCount1(riskAssplan.getCount1() + 1);
                        break;
                    case 2:
                        riskAssplan.setCount2(riskAssplan.getCount2() + 1);
                        break;
                    case 3:
                        riskAssplan.setCount3(riskAssplan.getCount3() + 1);
                        break;
                    case 4:
                        riskAssplan.setCount4(riskAssplan.getCount4() + 1);
                        break;
                    case 5:
                        riskAssplan.setCount5(riskAssplan.getCount5() + 1);
                        break;
                    default:
                        break;
                }
            	}
            }
            riskAssplan.setCount(riskAssplan.getCount1() + riskAssplan.getCount2() + riskAssplan.getCount3()
                    + riskAssplan.getCount4() + riskAssplan.getCount5());
        }
        
        FiexibleNameAssignment ment=new FiexibleNameAssignment();
		if(CollectionUtils.isNotEmpty(recordList)){
			recordList.forEach(entity->{
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
        return pageBean;
    }

    /**
     * 查询同个公司下风险评估计划的编号重复情况
     * @param plancode
     * @param
     * @return
     */
    @Override
    public Integer selectRiskAssplanNumber(String plancode, String orgid) throws Exception {

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



        Integer  result= riskAssplanMapper.findBysqlObj(plancode,orgid,idsStr);
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
	public void setPingguStatusByPlanId(RiskAssplan riskAssplan) {
		List<RiskRiskmarking> riskmarkingList = this.riskRiskmarkingMapper.selectPgList(riskAssplan.getAssplanid());
		String ypgStr = "";// 已评估的风险标记
		String wpgStr = "";// 未评估的风险标记
        // 设置评估状态为已评估的默认值
		BigDecimal status = new BigDecimal("2");
		for (RiskRiskmarking riskRiskmarking : riskmarkingList) {
            // 如果风险标记的评估状态与已评估状态相同
			if(status.compareTo(riskRiskmarking.getAsssatus()) == 0) {
                // 添加已评估的风险标记到ypgStr
				ypgStr += riskRiskmarking.getRealname()+"，已评估数量：" + riskRiskmarking.getPgcount()+"\n";
			}else {
                // 添加未评估的风险标记到wpgStr
				wpgStr += riskRiskmarking.getRealname()+"，未评估数量：" + riskRiskmarking.getPgcount()+"\n";
			}
		}
		riskAssplan.setWpgStr(wpgStr);// 设置未评估的风险标记字符串
		riskAssplan.setYpgStr(ypgStr);// 设置已评估的风险标记字符串
	}
	
	@Override
	public JsonBean get_riskpgplan_no(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //拼接编号
        Organization org=organizationMapper.selectById(loginStaff.getCurrentOrg().getOrgid());
        String yearStr = String.valueOf(DateUtil.thisYear());
        String plancode = "风险评估计划-"+org.getOrgnumber()+"-"+yearStr+"-";
        Integer maxno = this.riskAssplanMapper.get_riskpgplan_no("'"+plancode+"%'");
        if(null == maxno) {
        	maxno = 0;
        }
        maxno = maxno+1;
        plancode = plancode+maxno;
        
		return ResponseFormat.retParam(1, 200, plancode);
	}
	

	@Override
	public Map<String, Object> getRiskPointTask(String token, String company) throws Exception {
		// TODO Auto-generated method stub
		 Map<String,Object> result=new HashMap<String,Object>(); 
			final TblStaffUtil tblStaffUtil = userProvider.get();
		try {
			if(StringUtils.isBlank(company)&&tblStaffUtil.getLinkOrg()!=null){
				company=tblStaffUtil.getLinkOrg().getOrgid().toString();
			}
			List<Map<String, Object>> list=riskAssplanMapper.getRiskPointTaskNew(company);
			result.put("data", list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	 @Override
	    public PageInfo<RiskAssplan> findGroupResultList(Find find,Integer pageno,Integer pagesize) {
	        QueryWrapper<RiskAssplan> queryWrapper = new QueryWrapper<>();
	        // 设置查询条件：计划状态为3
	        queryWrapper.eq("planStatus", '3');
	        // 如果提供了计划编号，添加计划编号的模糊匹配条件
	        if (StringUtils.isNotEmpty(find.getCode()))
	            queryWrapper.like("plancode", find.getCode());
	        // 如果提供了计划名称，添加计划名称的模糊匹配条件
	        if (StringUtils.isNotEmpty(find.getName()))
	            queryWrapper.like("planName", find.getName());
	        if (find != null && StringUtils.isNotBlank(find.getStr1()))
	            queryWrapper.eq("groupId", find.getStr1());
                queryWrapper.eq("isgroup", new BigDecimal("1"));
	        
	        
	        // 按照记录日期降序排序
	        queryWrapper.orderByDesc("recordDate");

	        com.github.pagehelper.PageInfo<RiskAssplan> pageBean = PageMethod.startPage(pageno, pagesize)
					.doSelectPageInfo(() -> baseMapper.selectList(queryWrapper));
	        List<RiskAssplan> recordList = pageBean.getList();
	        // 遍历记录列表，为每个风险评估计划计算风险级别数量
	        for (RiskAssplan riskAssplan : recordList) {
	            // 创建新的查询条件包装器，用于查询与当前风险评估计划关联的风险评估计划风险
	            QueryWrapper queryWrapper1 = new QueryWrapper();
	            queryWrapper1.eq("ASSPLANID", riskAssplan.getAssplanid());
	            // 查询与当前风险评估计划关联的风险评估计划风险列表
	            List<RiskAssplanRisk> assPlanRisks = assplanRiskService.list(queryWrapper1);
	            // 遍历风险评估计划风险列表，根据风险级别更新风险评估计划的计数
	            for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {
	                switch (Integer.parseInt(riskAssPlanRisk.getRisklevel())) {
	                    case 1:
	                        riskAssplan.setCount1(riskAssplan.getCount1() + 1);
	                        break;
	                    case 2:
	                        riskAssplan.setCount2(riskAssplan.getCount2() + 1);
	                        break;
	                    case 3:
	                        riskAssplan.setCount3(riskAssplan.getCount3() + 1);
	                        break;
	                    case 4:
	                        riskAssplan.setCount4(riskAssplan.getCount4() + 1);
	                        break;
	                    case 5:
	                        riskAssplan.setCount5(riskAssplan.getCount5() + 1);
	                        break;
	                    default:
	                        break;
	                }
	            }
	            riskAssplan.setCount(riskAssplan.getCount1() + riskAssplan.getCount2() + riskAssplan.getCount3()
	                    + riskAssplan.getCount4() + riskAssplan.getCount5());
	        }
	        FiexibleNameAssignment ment=new FiexibleNameAssignment();
			if(CollectionUtils.isNotEmpty(recordList)){
				recordList.forEach(entity->{
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
	        
	        
	        return pageBean;
	    }
	  @Override
	    public PageInfo<RiskAssplan> riplanTrackList(RiskAssplan riskAssplan, BigDecimal orgid,Integer pageno,Integer pagesize,TblStaffUtil staffUtil) throws Exception{
	        //风险计划条件查询
	        QueryWrapper<RiskAssplan> queryWrapper = new QueryWrapper();
	        queryWrapper.eq("1", 1);
			Organization org=organizationMapper.selectById(riskAssplan.getUnit());
      		if (StringUtils.isNotEmpty(riskAssplan.getUnit())&&org.getOrgtype().compareTo(new BigDecimal(0))!=0){
      			queryWrapper.eq("unit", riskAssplan.getUnit());
      		}else{
      			List<Organization> list=organizationMapper.getChildOrg(riskAssplan.getUnit());
      			List<BigDecimal> orgids=list.stream().map(Organization::getOrgid).collect(Collectors.toList());
      			queryWrapper.in("LINKDEPTID", orgids);
      		}
	        // 如果提供了组织ID，添加单位（组织）的查询条件
	        if (StringUtils.isNotBlank(riskAssplan.getOrgName()))
	            queryWrapper.like("o.orgname", riskAssplan.getOrgName());
	        // 如果风险评估计划对象不为空，并且提供了计划编号，添加计划编号的模糊匹配条件
	        if (riskAssplan != null && riskAssplan.getPlancode() != null && riskAssplan.getPlancode().length() > 0) {
	            queryWrapper.like("plancode", riskAssplan.getPlancode());
	        }
	        // 如果风险评估计划对象不为空，并且提供了计划名称，添加计划名称的模糊匹配条件
	        if (riskAssplan != null && riskAssplan.getPlanName() != null && riskAssplan.getPlanName().length() > 0) {
	            queryWrapper.like("planname", riskAssplan.getPlanName());
	        }
	        // 如果风险评估计划对象不为空，并且提供了计划类型，添加计划类型的精确匹配条件
	        if (riskAssplan != null && riskAssplan.getPlanType() != null && riskAssplan.getPlanType().length() > 0) {
	            queryWrapper.eq("plantype", riskAssplan.getPlanType());
	        }
	        // 如果风险评估计划对象不为空，并且提供了计划状态，添加计划状态的精确匹配条件
	        if (riskAssplan != null && riskAssplan.getPlanStatus() != null && riskAssplan.getPlanStatus().length() > 0) {
	            queryWrapper.eq("planstatus", riskAssplan.getPlanStatus());
	        }
			if (riskAssplan != null && riskAssplan.getAprstatus() != null && riskAssplan.getAprstatus().length() > 0) {
				if(riskAssplan.getAprstatus().equals("0")){
					queryWrapper.isNull("aprstatus");
				}else{
					queryWrapper.eq("aprstatus",riskAssplan.getAprstatus());
				}
			}
	        if (riskAssplan != null) {
	            // 如果风险评估计划对象不为空，并且提供了开始日期，添加开始日期的大于等于条件
	            if (riskAssplan.getStartDate() != null) {
	                queryWrapper.ge("startdate", riskAssplan.getStartDate());
	            }
	            // 如果风险评估计划对象不为空，并且提供了结束日期，添加结束日期的小于等于条件
	            if (riskAssplan.getEndDate() != null) {
	                queryWrapper.le("enddate", riskAssplan.getEndDate());
	            }
	        }
//	        queryWrapper.orderByDesc("plancode");
	        // 按照风险评估计划ID降序排序
	        queryWrapper.orderByDesc("ASSPLANID");
			 String sql = GeneralSQLConcatConfig.concatSecrectSql(staffUtil.getCurrentOrg().getUseSecrect(), false, "UNIT", "LINKDEPTID", "CREATESTAFFID", "SECRECTLEVELID", 
					 "STAFFSCOPEIDS", staffUtil.getStaffid(), staffUtil.getDeptIds(), staffUtil.getSecrectScopeIds());
					 
	        com.github.pagehelper.PageInfo<RiskAssplan> pageInfo=pageInfo = PageMethod.startPage(pageno, pagesize)
					.doSelectPageInfo(() -> riskAssplanMapper.riplanTrackList(queryWrapper,sql));
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
}
