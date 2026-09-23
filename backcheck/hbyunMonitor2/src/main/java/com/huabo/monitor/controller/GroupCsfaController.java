package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblGroupTestplan;
import com.huabo.monitor.entity.TblGroupTestplanAtt;
import com.huabo.monitor.entity.TblTestelement;
import com.huabo.monitor.mapper.TblAttachmentMapper;
import com.huabo.monitor.service.GroupCsfaService;
import com.huabo.monitor.service.ITblGroupTestplanService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.service.TblTestElementService;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.vo.param.fieldActivationVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name="内控测试-集团测试计划",description="内控测试-集团测试计划")
public class GroupCsfaController {

	 @Resource
	 ITblGroupTestplanService groupTestplanService;
	
    @Autowired
    ITblStaffService iTblStaffService;


    @Autowired
    TblAssessService tblAssessService;

    @Resource
    GroupCsfaService csfaService;

    @Resource 
    TblAttachmentMapper tblAttachmentMapper;

 

    @Resource
    TblTestElementService tblTestelementService;
    
    @Resource
    private UserProvider userProvider;

	@Value("${application.administrators:}")
	private String administrators;

    @OperationLog(
            success = "集团测试计划-主页查询成功",
            busType = "内控测试",
            fail = "集团测试计划-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "集团测试计划"
    )
    @GetMapping(value = "/groupPlan/ctrltest_plan_list")
    @Operation(summary = "集团测试计划-主页")
    public JsonBean nkcs_ctrltest_plan_list(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "plannumber", description = "plannumber") @RequestParam(value = "plannumber", required = false) String plannumber,
            @Parameter(name = "planname", description = "planname") @RequestParam(value = "planname", required = false) String planname,
            @Parameter(name = "starttime_min", description = "starttime_min") @RequestParam(value = "starttime_min", required = false) String starttime_min,
            @Parameter(name = "starttime_max", description = "starttime_max") @RequestParam(value = "starttime_max", required = false) String starttime_max,
            @Parameter(name = "status", description = "status") @RequestParam(value = "status", required = false) String status,
            @Parameter(name = "year", description = "year") @RequestParam(value = "year", required = false) String year,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        TblGroupTestplan plan = new TblGroupTestplan();
        if (planname != null && planname.length() > 0) {
            plan.setPlanname(planname);
        }
        if (plannumber != null && plannumber.length() > 0) {
            plan.setPlannumber(plannumber);
        }
        if (status != null && status.length() > 0) {
            plan.setStatus(status);
        }
        if (year != null && year.length() > 0) {
            plan.setPlanyear(year);
        }

		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, user.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
        boolean bool = csfaService.isSJByOrgId(user.getLinkDetp().getOrgid().toString());
        PageInfo<TblGroupTestplan> iPage;
        if (bool) {
            iPage = csfaService.findAllNew(plan, pageNumber, starttime_min, starttime_max, user.getCurrentOrg().getOrgid(),user.getStaffid(),authorityType,user);
        } else {
        	iPage = csfaService.findAllnoSjNew(plan, pageNumber, starttime_min, starttime_max,
                       user.getStaffid().toString(),authorityType,user);
        }
        IPageResult<TblGroupTestplan> pageInfo=new IPageResult<TblGroupTestplan>().buildIpage(iPage);
        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", pageInfo);
        mv.put("starttime_min", starttime_min);
        mv.put("starttime_max", starttime_max);
        mv.put("plan", plan);
        return ResponseFormat.retParam(1, 200, mv);
 
    }


    @OperationLog(
            success = "集团测试计划-详情页面成功",
            busType = "内控测试",
            fail = "集团测试计划-详情页面失败",
            operationType = OperationType.SELECT,
            subType = "集团测试计划"
    )
    @GetMapping(value = "/groupPlan/detail")
    @Operation(summary = "集团测试计划-详情页面")
    public JsonBean nkcs_ctrltest_plan_detail(
            @Parameter(name = "selectProjectid", description = "testplanid") @RequestParam(value = "selectProjectid") BigDecimal selectProjectid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new HashMap<>();
        mv=csfaService.queryOneTestPlan(selectProjectid);
        return ResponseFormat.retParam(1, 200, mv);
    }


    @OperationLog(
            success = "集团测试计划-附件删除成功",
            busType = "内控测试",
            fail = "集团测试计划-附件删除失败",
            operationType = OperationType.DELETE,
            subType = "集团测试计划"
    )
    @Operation(summary = "集团测试计划-附件删除")
    @PostMapping(value = "/delGroupTestPlanAtt")
    public JsonBean delGroupTestPlanAtt(
            @Parameter(name = "attid", description = "attid") @RequestParam(value = "attid")BigDecimal attid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        if (attid != null ) {
        	csfaService.deleteAtt(attid);
        }
        return new JsonBean(200, "success", null);
    }
    
   
    /**
     * 通用获取设置的编号
     *
     * @param
     */
    @OperationLog(
            success = "集团测试计划-新建-编号生成查询成功",
            busType = "内控测试",
            fail = "集团测试计划-新建-编号生成查询失败",
            operationType = OperationType.SELECT,
            subType = "集团测试计划"
    )
    @GetMapping(value = "/groupCode/findAutoNumber")
    @Operation(summary = "集团测试计划-新建-编号生成")
    public JsonBean findAutoNumber(HttpServletResponse response,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name="tblName",description="tblName") @RequestParam(value = "tblName") String tblName,
                                   @Parameter(name="column",description="column") @RequestParam(value = "column") String column,
                                   @Parameter(name="orgCol",description="orgCol") @RequestParam(value = "orgCol") String orgCol,
                                   @Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        String flowNextId = null;
        try {
            flowNextId = tblAssessService.findFlowNextIdNk(tblName, column, orgCol, staff.getCurrentOrg().getOrgid(), noId,
                    null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.reset();
        return ResponseFormat.retParam(1, 200, flowNextId);
    }

    

    @OperationLog(
            success = "集团测试计划-添加页面-保存成功",
            busType = "内控测试",
            fail = "集团测试计划-添加页面-保存失败",
            operationType = OperationType.ADD,
            subType = "集团测试计划"
    )
    @Operation(summary = "集团测试计划-添加页面-保存")
    @PostMapping(value = "/groupPlan/saveOrUpdate")
    public JsonBean saveOrUpdate(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TblGroupTestplan plan 
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
       // TblGroupTestplan plan = new TblGroupTestplan();
        plan.setCreatid(staff.getStaffid());
        plan.setLinkdeptid(staff.getLinkDetp().getOrgid());
        plan.setCreatetime(new Date());
        plan.setOrgid(staff.getLinkOrg().getOrgid());
        if(plan.getTesttemid()!=null&&plan.getTesttemid().compareTo(new BigDecimal("0"))!=0){
        	   QueryWrapper<TblTestelement> wrapper = new QueryWrapper<>();
               wrapper.eq("templid", plan.getTesttemid());
               final long count = tblTestelementService.count(wrapper);
               if (count <= 0) {
                   return ResponseFormat.retParam(0, 90001, null);
               }else{
                   plan.setTesttemid(plan.getTesttemid());
               }
        }
        if(plan.getId()==null||plan.getId().compareTo(new BigDecimal("0"))==0){
        	 this.groupTestplanService.savePlan(plan);
        }else{
        	 this.groupTestplanService.update(plan);
        }
        if (StringUtils.isNotBlank(plan.getAttids())) {
            String[] ids = plan.getAttids().split(",");
            TblGroupTestplanAtt  att=new TblGroupTestplanAtt();
            for (int i = 0; i < ids.length; i++) {
           	 if(StringUtils.isNotBlank(ids[i])){
                this.tblAttachmentMapper.insertGroupTestPlanAtt(new BigDecimal(ids[i].toString()),plan.getId());
            }
            }
         }
        return ResponseFormat.retParam(1, 200, plan);
    }

    @OperationLog(
            success = "集团测试计划-修改页面-修改保存成功",
            busType = "内控测试",
            fail = "集团测试计划-修改页面-修改保存失败",
            operationType = OperationType.UPDATE,
            subType = "集团测试计划"
    )
    @Operation(summary = "集团测试计划-修改页面-修改保存")
    @PostMapping(value = "/groupPlan/modify_save")
    public JsonBean nkcs_ctrltest_plan_modify_save(
            @Parameter(name = "id", description = "id") @RequestParam(value = "id") BigDecimal id,
            @Parameter(name = "plannumber", description = "计划编号", required = true) @RequestParam(value = "plannumber", required = true) String plannumber,
            @Parameter(name = "planname", description = "计划名称", required = true) @RequestParam(value = "planname", required = true) String planname,
            @Parameter(name = "planyear", description = "计划年度", required = true) @RequestParam(value = "planyear", required = true) String planyear,

            @Parameter(name = "testtype", description = "测试类型") @RequestParam(value = "testtype", required = false) String testtype,

            @Parameter(name = "starttime_min", description = "计划时间min") @RequestParam(value = "starttime_min", required = false) String starttime_min,
            @Parameter(name = "starttime_max", description = "计划时间max") @RequestParam(value = "starttime_max", required = false) String starttime_max,
            @Parameter(name = "staffid", description = "负责人id", required = true) @RequestParam(value = "staffid", required = true) String staffid,
            @Parameter(name = "staffname", description = "负责人名", required = true) @RequestParam(value = "staffname", required = true) String staffname,

            @Parameter(name = "cost", description = "开展费用") @RequestParam(value = "cost", required = false) String cost,
            @Parameter(name = "people", description = "投入人力") @RequestParam(value = "people", required = false) String people,
            @Parameter(name = "org3", description = "被测试机构名称") @RequestParam(value = "org3", required = false) String org3,
            @Parameter(name = "testtemid", description = "测试模板id", required = true) @RequestParam(value = "testtemid", required = true) String testtemid,
            @Parameter(name = "attids", description = "附件id") @RequestParam(value = "attids",required = false) String attids,
            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @ModelAttribute fieldActivationVo content) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblGroupTestplan plan = this.groupTestplanService.getById(id);
        if (plan == null) {
            return ResponseFormat.retParam(0, 50001, null);
        }
        plan.setCreatid(staff.getStaffid());
        plan.setPlanname(planname);
        plan.setPlanyear(planyear);
        plan.setTesttype(testtype);
        plan.setTestedorgs(org3);
            QueryWrapper<TblTestelement> wrapper = new QueryWrapper<>();
            wrapper.eq("templid", new BigDecimal(testtemid));
            final long count = tblTestelementService.count(wrapper);
            if (count <= 0) {
                return ResponseFormat.retParam(0, 90001, null);
            }else{
            	  plan.setTesttemid(new BigDecimal(testtemid));
            }
  
        if (StringUtils.isNotBlank(cost)) {
            plan.setPlanfee(new BigDecimal(cost));
        }

        if (StringUtils.isNotBlank(staffid)) {
            plan.setStaffid(new BigDecimal(staffid));
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//
//            if (StringUtils.isNotBlank(starttime_min)) {
//                plan.setStarttime(DateUtils.dateToLocalDateTime(formatter.parse(starttime_min)));
//            }
//
//            if (StringUtils.isNotBlank(starttime_max)) {
//                plan.setEndtime(DateUtils.dateToLocalDateTime(formatter.parse(starttime_max)));
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return ResponseFormat.retParam(0, 90002, null);
//        }
        plan.setPlanleader(staffname);
        plan.setNumberofpeople(people);
        plan.setPlannumber(plannumber);
        plan.setSecrectLevelId(secrectLevelId);
        plan.setStaffScopeIds(staffScopeIds);
        plan.setStaffScopeNames(staffScopeNames);
        if (content != null) {
        	plan.setFieldActivationCopy(content);
        }
        this.groupTestplanService.update(plan);
        if (StringUtils.isNotBlank(attids)) {
            String[] ids = attids.split(",");
            TblGroupTestplanAtt  att=new TblGroupTestplanAtt();
            for (int i = 0; i < ids.length; i++) {
           	 if(StringUtils.isNotBlank(ids[i])){
                this.tblAttachmentMapper.insertGroupTestPlanAtt(new BigDecimal(ids[i].toString()),plan.getId());
            }
            }
         }
        return ResponseFormat.retParam(1, 200, plan);
    }

    @OperationLog(
            success = "集团测试计划-删除成功",
            busType = "内控测试",
            fail = "集团测试计划-删除失败",
            operationType = OperationType.DELETE,
            subType = "集团测试计划"
    )
    @Operation(summary = "集团测试计划-删除")
    @PostMapping(value = "/groupPlan/delete")
    public JsonBean nkcs_ctrltest_plan_delete(
            @Parameter(name = "selectProjectid", description = "testplanid") @RequestParam(value = "selectProjectid") BigDecimal selectProjectid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblGroupTestplan plan = this.groupTestplanService.getById(selectProjectid);
 
        if (selectProjectid != null) {
            this.csfaService.deleteTestPlan(selectProjectid);
        }
        return ResponseFormat.retParam(1, 200, null);
    }



    
    @OperationLog(
            success = "集团测试计划-下发分公司【{{#id}}】",
            busType = "内控测试",
            fail = "集团测试计划-下发分公司【{{#id}}】",
            operationType = OperationType.DISPATCH,
            subType = "集团测试计划"
    ) 
    @RequestMapping(value = "/toIssued",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "集团测试计划-下发分公司")
    public JsonBean toIssued(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "id", description = "下发表单主键", required = true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name = "staffids", description = "多选下发人员id", required = true) @RequestParam(value = "staffids", required = true) String staffids,
          @Parameter(name = "staffnames", description = "多选下发人员name", required = true) @RequestParam(value = "staffnames", required = true) String staffnames) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
    	
    	JsonBean bean = null;
        try {
            bean = groupTestplanService.toIssued(id,staffids,staffnames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }
    
    
    @OperationLog(
            success = "集团测试计划-启动【{{#id}}】",
            busType = "内控测试",
            fail = "集团测试计划-启动【{{#id}}】",
            operationType = OperationType.DISPATCH,
            subType = "集团测试计划"
    ) 
    @RequestMapping(value = "/startIssued",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "集团测试计划-启动")
    public JsonBean startIssued(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "id", description = "下发表单主键", required = true) @RequestParam(value = "id", required = true) String id) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
    	
    	JsonBean bean = null;
        try {
            bean = groupTestplanService.startIssued(id,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }
    
    
    @OperationLog(
            success = "集团跟进结果-外层查询成功",
            busType = "内控测试",
            fail = "集团跟进结果-外层查询失败",
            operationType = OperationType.SELECT,
            subType = "集团跟进结果"
    )
    @GetMapping(value = "/groupPlan/resultPlanList")
    @Operation(summary = "集团跟进结果-外层")
    public JsonBean resultPlanList(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "plannumber", description = "plannumber") @RequestParam(value = "plannumber", required = false) String plannumber,
            @Parameter(name = "planname", description = "planname") @RequestParam(value = "planname", required = false) String planname,
            @Parameter(name = "starttime_min", description = "starttime_min") @RequestParam(value = "starttime_min", required = false) String starttime_min,
            @Parameter(name = "starttime_max", description = "starttime_max") @RequestParam(value = "starttime_max", required = false) String starttime_max,
            @Parameter(name = "year", description = "year") @RequestParam(value = "year", required = false) String year,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }

        TblGroupTestplan plan = new TblGroupTestplan();
        if (planname != null && planname.length() > 0) {
            plan.setPlanname(planname);
        }
        if (plannumber != null && plannumber.length() > 0) {
            plan.setPlannumber(plannumber);
        }
         plan.setStatus("6");
        if (year != null && year.length() > 0) {
            plan.setPlanyear(year);
        }
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, user.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
		PageInfo<TblGroupTestplan>   iPage = csfaService.findAll(plan, pageNumber, starttime_min, starttime_max, user.getCurrentOrg().getOrgid(),user.getStaffid(),authorityType,user);
        IPageResult<TblGroupTestplan> pageInfo=new IPageResult<TblGroupTestplan>().buildIpage(iPage);
        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", pageInfo);
        mv.put("starttime_min", starttime_min);
        mv.put("starttime_max", starttime_max);
        mv.put("plan", plan);
        return ResponseFormat.retParam(1, 200, mv);

    }
     
}
