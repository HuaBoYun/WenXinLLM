package com.huabo.fxgl.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.DateUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.RiskAssessmentstd;
import com.huabo.fxgl.entity.RiskGroupPlanAtt;
import com.huabo.fxgl.entity.RiskGroupPlanOrg;
import com.huabo.fxgl.entity.TblRiskGroupplan;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IRiskAssessmentstdService;
import com.huabo.fxgl.service.IRiskAssplanAttService;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.huabo.fxgl.service.IRiskGroupPlanAttService;
import com.huabo.fxgl.service.IRiskGroupPlanOrgService;
import com.huabo.fxgl.service.IRiskGroupPlanService;
import com.huabo.fxgl.service.IRiskRiskmarkingService;
import com.huabo.fxgl.service.IRiskService;
import com.huabo.fxgl.service.IRiskcategoryService;
import com.huabo.fxgl.service.IRiskeventService;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.ITaskService;
import com.huabo.fxgl.service.TblFxgkBugService;
import com.huabo.fxgl.service.TblNbkzRiskService;
import com.huabo.fxgl.service.TblNbsjBugService;
import com.huabo.fxgl.util.DateUtils;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


/**
 * 集团评估计划控制器
 * <p>提供风险评估中集团评估计划的列表查询、新增、修改、删除、下发等接口</p>
 *
 * @author hbyun
 */
@Slf4j
@Tag(name="风险管控 - 风险评估 - 集团评估计划",description="风险管控 - 风险评估 - 集团评估计划")
@RestController
@RequestMapping(value = "/groupPlan")
public class RiskGroupAssplanController {

 
    @Autowired
    private IRiskGroupPlanService riskGroupPlanService;

    @Autowired
    private IRiskAssessmentstdService iRiskAssessmentstdService;

    @Autowired
    private IRiskGroupPlanService iRiskGroupPlanService;
    @Autowired
    private IRiskGroupPlanOrgService iRiskGroupPlanOrgService;
    @Autowired
    private IRiskGroupPlanAttService iRiskGroupPlanAttService;

    @Autowired
    private IOrganizationService iOrganizationService;


    @Autowired
    private ITaskService iTaskService;

    @Autowired
    private IRiskeventService riskEventService;

    @Autowired
    private IRiskcategoryService riskCategoryService;
    @Autowired
    private IRiskService riskService;

    @Autowired
    private IRiskRiskmarkingService iRiskRiskmarkingService;

    @Autowired
    private IAttachmentService iAttachmentService;

    @Autowired
    private IRiskAssplanAttService iRiskAssplanAttService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IRiskAssplanRiskService riskAssplanRiskService;

    @Autowired
    private IRiskRiskmarkingService riskRiskmarkingService;


    @Autowired
    private IStaffService staffService;

    @Autowired
    private IOrganizationService organizationService;

    @Resource
    public TblNbsjBugService tblNbsjBugService;

    @Resource
    public TblNbkzRiskService nbkzRiskService;
    
    @Resource
    private UserProvider userProvider;

    @Resource
    public TblFxgkBugService tblFxgkBugService;
//-----------------------------------------------------------start
    @OperationLog(
            success = "新增/修改评估计划【{{#planName}}】成功",
            busType = "风险评估",
            fail = "新增/修改评估计划【{{#planName}}】失败",
            operationType = OperationType.ADD,
            subType = "集团评估计划"
    )
    @Operation(summary = "保存计划：/groupPlan/riplanadd")
    @RequestMapping(value = "/riplanadd", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean riplanAdd(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "endDate", description = "starttime_max 计划结束时间，必须大于计划开始时间", required = false) @RequestParam(required = true, name = "endDate") String starttime_max,
                              @Parameter(name = "startDate", description = "starttime_min 计划开始时间，必须大于计划开始时间", required = false) @RequestParam(required = true, name = "startDate") String starttime_min,
                              @Parameter(name = "reporteds", description = "reporteds 上传附件ids", required = false) @RequestParam String reporteds,
                              @Parameter(name = "removeReporteds", description = "removeReporteds 上传附件ids", required = false) @RequestParam String removeReporteds ,
                               @Parameter(name = "assId", description = "assId 评估标准id", required = true) @RequestParam(required = false) BigDecimal assId,
                               @RequestBody TblRiskGroupplan tblRiskGroupplan
                              ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getLinkOrg(); //当前用户选择的组织
        //查询评估标准
        RiskAssessmentstd assessMentsTd = iRiskAssessmentstdService.getById(assId);
        log.info(assessMentsTd.toString());
        RiskGroupPlanOrg   riskGroupPlanOrg = new RiskGroupPlanOrg();
        Map<String, Object> result = new HashMap<String, Object>(0);
        //时间验证    比较两个时间，当结束时间大于开始时间时，返回1；
        if (DateUtils.compare_date(starttime_max, starttime_min) == 1) {
            if (null != assessMentsTd) {
                if (tblRiskGroupplan.getId().compareTo(BigDecimal.ZERO)!=0) {
                    //查询评估计划
                    tblRiskGroupplan.setEndDate(DateUtil.formatDate(starttime_max, "yyyy-MM-dd"));
                    tblRiskGroupplan.setStartDate(DateUtil.formatDate(starttime_min, "yyyy-MM-dd"));
                   tblRiskGroupplan.setStatus("0");
                    //修改评估标准  assTdId
                   tblRiskGroupplan.setAssessmentstd(assessMentsTd);
                   tblRiskGroupplan.setAssstdid(assessMentsTd.getAssstdid());
                    tblRiskGroupplan.setAssessmentstd(assessMentsTd);
                    Serializable serializable =riskGroupPlanService.updateEntity(tblRiskGroupplan);
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("ID", tblRiskGroupplan.getId());
                    iRiskGroupPlanAttService.remove(queryWrapper);
                    if (StringUtils.isNotEmpty(removeReporteds)){
                        attachmentService.removeByIds(Arrays.asList(removeReporteds.split(",")));
                    }
                    if (StringUtils.isNotEmpty(reporteds)){
                        String[] reportedSplit = reporteds.split(",");
                        for (String reported:reportedSplit){
                            RiskGroupPlanAtt riskGroupPlanAtt = new RiskGroupPlanAtt(tblRiskGroupplan.getId(), new BigDecimal(reported));
                            iRiskGroupPlanAttService.save(riskGroupPlanAtt);
                        }
                    }
                    //返回操作成功信息
                    result.put("serializable", serializable.toString());
                    return ResponseFormat.retParam(1, 200, result);
                } else {
                    //查询同个公司下风险评估计划的编号重复情况
                    Integer selectRiskAssplanNumber = riskGroupPlanService.selectTblRiskGroupplanNumber(tblRiskGroupplan.getPlancode(), selectOrg.getOrgid().toString());
                    if (selectRiskAssplanNumber != 0) {
                        //评估计划的编号重复，返回的失败信息
                        return ResponseFormat.retParam(0, "集团评估计划的编号重复", null);
                    }

                    tblRiskGroupplan.setRecorddate(new Date());
                    tblRiskGroupplan.setEndDate(DateUtil.formatDate(starttime_max, "yyyy-MM-dd"));
                    tblRiskGroupplan.setStartDate(DateUtil.formatDate(starttime_min, "yyyy-MM-dd"));
                   tblRiskGroupplan.setStatus("0");
                    tblRiskGroupplan.setAssessmentstd(assessMentsTd);
                    tblRiskGroupplan.setAssstdid(assessMentsTd.getAssstdid());
                    tblRiskGroupplan.setRecorder(staffUtil.getRealname());
                    tblRiskGroupplan.setCreatestaffid(staffUtil.getStaffid());
                    tblRiskGroupplan.setLinkdeptid(staffUtil.getLinkDetp().getOrgid());
                    tblRiskGroupplan.setUnit(selectOrg.getOrgid().toString());
                    tblRiskGroupplan.setId(RandomUtil.uuBigDecimalId());
                    tblRiskGroupplan.setCreatetime(new Date());
                    //当前用户选择的组织
                    tblRiskGroupplan.setOrganization(iOrganizationService.getById(selectOrg.getOrgid()));
                    Serializable serializable = riskGroupPlanService.saveEntity(tblRiskGroupplan);
                    //向riskAssplanOrg中间表添加数据 评估编号与当前用户选择的组织的中间表
                    riskGroupPlanOrg.setId(tblRiskGroupplan.getId());
                    riskGroupPlanOrg.setOrgid(selectOrg.getOrgid());
                   iRiskGroupPlanOrgService.save(riskGroupPlanOrg);
                    if (StringUtils.isNotEmpty(removeReporteds)){
                        attachmentService.removeByIds(Arrays.asList(removeReporteds.split(",")));
                    }
                    if (StringUtils.isNotEmpty(reporteds)){
                        String[] reportedSplit = reporteds.split(",");
                        for (String reported:reportedSplit){
                        	RiskGroupPlanAtt riskGroupPlanAtt = new RiskGroupPlanAtt(tblRiskGroupplan.getId(), new BigDecimal(reported));
                            iRiskGroupPlanAttService.save(riskGroupPlanAtt);
                        }
                    }
                    JsonBean jsonBean = null;
                  
                    result.put("id", tblRiskGroupplan.getId());
                    result.put("serializable", serializable.toString());
                    jsonBean = ResponseFormat.retParam(1, 200, result);
                    return jsonBean;
                }
            } else {
                //评估标准不存在，返回的失败信息
                return ResponseFormat.retParam(0, "保存失败", null);
            }
        } else {
            //计划开始时间和结束时间不正确，返回的失败信息
            return ResponseFormat.retParam(0, "计划开始时间与结束时间不正确", null);
        }

    }
    
    @OperationLog(
          success = "删除【{{#id}}】成功",
          busType = "风险评估",
          fail = "删除【{{#id}}】失败",
          operationType = OperationType.DISPATCH,
          subType = "集团评估计划"
  )
  @Operation(summary = "删除/groupPlan/riplandel")
  @RequestMapping(value = "/riplandel", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
  public @ResponseBody
  JsonBean riplanDel(@Parameter(name = "id", description = "id评估计划id", required = true) @RequestParam(required = true, value = "id") String id,
                     @Parameter(name = "token", description = "登录用户token", required = true)
                     @RequestHeader("token") String token) throws Exception {


      TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
      if (staffUtil == null) {
          return ResponseFormat.retParam(0, 20006, null);
      }

      log.info("{}", id);
      //查询RiskAssplan表记录 id是Assplanid
      TblRiskGroupplan plan =  riskGroupPlanService.getById(id);
      //plan不为空且未进行审批的才能删除
      if (null != plan && plan.getStatus().equals("0")) {
//          //根据Assplanid查询TBL_RISK_ASSPLAN_RISK中间表，
//          List<RiskAssplanRisk> assPlanRisks = iRiskAssplanRiskService.findRiskByRisk(assplan.getAssplanid());
//
//
//          for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {
//
//              //删除TBL_RISK_ASSPLAN_RISK中间表记录
//              iRiskAssplanRiskService.removeById(riskAssPlanRisk);
//
//              Set<RiskRiskmarking> riskMarkings = riskAssPlanRisk.getTblRiskRiskMarking();
//              for (RiskRiskmarking riskmarking : riskMarkings) {
//
//                  //删除RiskRiskmarking表数据
//                  iRiskRiskmarkingService.removeById(riskmarking);
//              }
//
//          }


          QueryWrapper<RiskGroupPlanAtt> queryWrapperA = new QueryWrapper<>();
//          queryWrapperA.eq("assplanid", assplan.getAssplanid());
          queryWrapperA.eq("id", id);
          List<RiskGroupPlanAtt> riskAssplanAtts = iRiskGroupPlanAttService.list(queryWrapperA);
          for (RiskGroupPlanAtt riskAssplanAtt : riskAssplanAtts) {
              //删除TBL_RiskAssplanAtt表记录
              QueryWrapper<RiskGroupPlanAtt> queryWrapper2 = new QueryWrapper<>();
              queryWrapper2.eq("id", plan.getId());
              iRiskGroupPlanAttService.remove(queryWrapper2);
              //删除附件表记录
              iAttachmentService.removeById(riskAssplanAtt.getAttid());
          }
          //删除RiskAssplanOrg中间表数据
          QueryWrapper<RiskGroupPlanOrg> queryWrapper1 = new QueryWrapper<>();
          queryWrapper1.eq("id", plan.getId());
          iRiskGroupPlanOrgService.remove(queryWrapper1);
          //删除评估计划
          iRiskGroupPlanService.removeById(plan);
          //操作成功，返回数据
          return ResponseFormat.retParam(1, 200, null);

      }
      return ResponseFormat.retParam(1, "集团评估计划为空", null);
  }


    /**
     * 查看详情
     * @param planId
     * @param token
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "查看评估计划详情【{{#planId}}】成功",
            busType = "风险评估",
            fail = "查看评估计划详情【{{#planId}}】失败",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @RequestMapping(value = "/riplaninfo",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "查看详情 /groupPlan/riplaninfo")
    public JsonBean riplanInfo(@Parameter(name="id",description="评估主键, 必填项",required=true) @RequestParam(required = true) String id,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        TblRiskGroupplan tblRiskGroupplan = riskGroupPlanService.getOneDetail(id);
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        if (null != tblRiskGroupplan) {
        	Set<Attachment> tblAttachments =iAttachmentService.getRiskGroupPlanAttList(id);
        	tblRiskGroupplan.setTblAttachments(tblAttachments);

           // List<Organization> orgByUser = organizationService.findOrgByUser(staffUtil.getUsername());
            //集团评估计划需求暂时不关联风险
           // List<RiskAssplanRisk> listriskAssPlanRisk = riskAssplanRiskService.findRiskByRiskid(riskAssplan.getAssplanid());
//            result.put("orgByUser", orgByUser.size() == 1 ? orgByUser.get(0) : "");
//            result.put("staff", staffUtil);
        	  FiexibleNameAssignment ment=new FiexibleNameAssignment();
          	//对灵活字段中的姓名名称及机构名称赋值
  			fieldOrgStaffId item=new fieldOrgStaffId();
  			BeanUtils.copyProperties(tblRiskGroupplan,item); 
  			fieldOrgStaffName nameEntity=ment.setOpenName(item);
  			BeanUtils.copyProperties(nameEntity,tblRiskGroupplan ); 
            result.put("plan", tblRiskGroupplan);
//            result.put("listriskAssPlanRisk", listriskAssPlanRisk);
        }
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }
    
 
  @OperationLog(
          success = "获取集团评估计划列表",
          busType = "风险评估",
          fail = "获取集团评估计划列表",
          operationType = OperationType.SELECT,
          subType = "集团评估计划"
  )
  @RequestMapping(value = "/getGroupPlanlist", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
  @Operation(summary = "评估计划查询/groupPlan/getGroupPlanlist")
  public JsonBean getGroupPlanlist(
          @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
          @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
          @Parameter(name = "plancode", description = "查询条件 -计划编号", required = false) @RequestParam(value = "plancode", required = false) String plancode,
          @Parameter(name = "planName", description = "查询条件 -计划名称", required = false) @RequestParam(value = "planName", required = false) String planName,
          @Parameter(name = "startDate", description = "查询条件 -计划时间开始时间", required = false) @RequestParam(value = "startDate", required = false) String startDate,
          @Parameter(name = "endDate", description = "查询条件 -计划时间结束时间", required = false) @RequestParam(value = "endDate", required = false) String endDate,
          @Parameter(name = "planType", description = "查询条件 -计划类型 1-年度计划  2-临时性计划", required = false) @RequestParam(value = "planType", required = false) String planType,
          //@Parameter(name = "planStatus", description = "查询条件 -计划状态 1-未开始 2-评估中 3-已完成", required = false) @RequestParam(value = "planStatus", required = false) String planStatus,
			@Parameter(name = "status", description = "查询条件 -审批状态", required = false) @RequestParam(value = "status", required = false) String status
  ) throws Exception {
      TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
      if (staffUtil == null) {
          return ResponseFormat.retParam(0, 20006, null);
      }
      IPage page = new Page<>(pageNo, pageSize);//分页设置
      TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg();//当前用户选择的组织

      BigDecimal orgid = selectOrg.getOrgid(); //当前用户选择的组织
      /*if (selectOrg != null && selectOrg.getOrderid() != null) {
          orgid = selectOrg.getOrgid();
      }*/
      TblRiskGroupplan tblRiskGroupplan = new TblRiskGroupplan();
      tblRiskGroupplan.setPlancode(plancode);
      tblRiskGroupplan.setPlanName(planName);
      tblRiskGroupplan.setStatus(status);
      tblRiskGroupplan.setStartDate(DateUtil.formatDate(startDate, "yyyy-MM-dd"));
      tblRiskGroupplan.setEndDate(DateUtil.formatDate(endDate, "yyyy-MM-dd"));
      tblRiskGroupplan.setPlanType(planType);
     // tblRiskGroupplan.setPlanStatus(planStatus);

      PageInfo<TblRiskGroupplan> pageBean = riskGroupPlanService.findAll(staffUtil,tblRiskGroupplan, orgid,pageNo,pageSize);
      JsonBean jsonBean = null;
      Map<String, Object> result = new HashMap<String, Object>(0);
      result.put("pageBean", pageBean);
      jsonBean = ResponseFormat.retParam(1, 200, result);
      return jsonBean;
  }
  
  
  @OperationLog(
          success = "集团评估计划下发分公司【{{#id}}】",
          busType = "风险评估",
          fail = "集团评估计划下发分公司【{{#id}}】",
          operationType = OperationType.DISPATCH,
          subType = "集团评估计划"
  ) 
  @RequestMapping(value = "/toIssued",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
  @Operation(summary = "集团评估计划下发分公司")
  public JsonBean toIssued(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
  		@Parameter(name = "id", description = "下发表单主键", required = true) @RequestParam(value = "id", required = true) String id,
  		@Parameter(name = "staffids", description = "多选下发人员id", required = true) @RequestParam(value = "staffids", required = true) String staffids,
        @Parameter(name = "staffnames", description = "多选下发人员name", required = true) @RequestParam(value = "staffnames", required = true) String staffnames) throws Exception {
      JsonBean bean = null;
      try {
          bean = riskGroupPlanService.toIssued(id,staffids,staffnames);
      } catch (Exception e) {
          e.printStackTrace();
      }
      return ResponseFormat.retParam(1, 200, bean);
  }
  
    //-----------------------------------------------------------------end
//
  @OperationLog(
          success = "获取集团跟进结果列表",
          busType = "风险评估",
          fail = "获取集团跟进结果列表",
          operationType = OperationType.SELECT,
          subType = "集团跟进结果"
  )
  @RequestMapping(value = "/getGroupPlanResultList", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
  @Operation(summary = "获取集团跟进结果列表/groupPlan/getGroupPlanResultList")
  public JsonBean getGroupPlanResultList(
          @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
          @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
          @Parameter(name = "plancode", description = "查询条件 -计划编号", required = false) @RequestParam(value = "plancode", required = false) String plancode,
          @Parameter(name = "planName", description = "查询条件 -计划名称", required = false) @RequestParam(value = "planName", required = false) String planName,
          @Parameter(name = "startDate", description = "查询条件 -计划时间开始时间", required = false) @RequestParam(value = "startDate", required = false) String startDate,
          @Parameter(name = "endDate", description = "查询条件 -计划时间结束时间", required = false) @RequestParam(value = "endDate", required = false) String endDate,
          @Parameter(name = "planType", description = "查询条件 -计划类型 1-年度计划  2-临时性计划", required = false) @RequestParam(value = "planType", required = false) String planType
          
  ) throws Exception {
      TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
      if (staffUtil == null) {
          return ResponseFormat.retParam(0, 20006, null);
      }
      IPage page = new Page<>(pageNo, pageSize);//分页设置
      TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg();//当前用户选择的组织

      BigDecimal orgid = selectOrg.getOrgid(); //当前用户选择的组织
      /*if (selectOrg != null && selectOrg.getOrderid() != null) {
          orgid = selectOrg.getOrgid();
      }*/
      TblRiskGroupplan tblRiskGroupplan = new TblRiskGroupplan();
      tblRiskGroupplan.setPlancode(plancode);
      tblRiskGroupplan.setPlanName(planName);
      tblRiskGroupplan.setStatus("6");
      tblRiskGroupplan.setToIssued(new BigDecimal(1));
      tblRiskGroupplan.setStartDate(DateUtil.formatDate(startDate, "yyyy-MM-dd"));
      tblRiskGroupplan.setEndDate(DateUtil.formatDate(endDate, "yyyy-MM-dd"));
      tblRiskGroupplan.setPlanType(planType);
      PageInfo<TblRiskGroupplan> pageBean = riskGroupPlanService.findAll(staffUtil,tblRiskGroupplan, orgid,pageNo,pageSize);
      JsonBean jsonBean = null;
      Map<String, Object> result = new HashMap<String, Object>(0);
      result.put("pageBean", pageBean);
      jsonBean = ResponseFormat.retParam(1, 200, result);
      return jsonBean;
  }
  

  @OperationLog(
			success = "获取集团评估计划编号",
			busType = "风险评估",
			fail = "获取集团评估计划编号",
			operationType = OperationType.SELECT,
			subType = "集团评估计划"
	)
  @RequestMapping(value = "/get_riskpgplan_no",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
  @Operation(summary = "获取集团评估计划编号 ")
  public JsonBean get_riskpgplan_no(
          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
  		) throws Exception {
      JsonBean jsonBean = this.riskGroupPlanService.get_riskpgplan_no(token);

      return jsonBean;
  }
}
