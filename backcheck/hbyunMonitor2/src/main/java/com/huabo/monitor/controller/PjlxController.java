package com.huabo.monitor.controller;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.mibiao.SecretLabel;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessMark;
import com.huabo.monitor.entity.TblAssessMarkVo;
import com.huabo.monitor.entity.TblAssessPlan;
import com.huabo.monitor.entity.TblAssessStaffVo;
import com.huabo.monitor.entity.TblAssesstemple;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.entity.TblTask;
import com.huabo.monitor.entity.Tree;
import com.huabo.monitor.mapper.TblAssessPlanMapper;
import com.huabo.monitor.service.ITblAssessMarkService;
import com.huabo.monitor.service.ITblAssesstempleService;
import com.huabo.monitor.service.ITblAttachmentService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTaskService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.util.ConstClass;
import com.huabo.monitor.util.DateUtils;
import com.huabo.monitor.util.FiexibleNameAssignment;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.vo.param.fieldActivationVo;
import com.huabo.monitor.vo.param.fieldOrgStaffId;
import com.huabo.monitor.vo.param.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;



@RestController
@Slf4j
@Tag(name="评价管理-评价立项",description="评价管理-评价立项")
@RequestMapping(value = "/nbkz")
public class PjlxController {

    @Autowired
    TblAssessService tblAssessService;

    @Autowired
    ITblStaffService iTblStaffService;
    @Autowired
    ITblAssessMarkService  iTblAssessMarkService;

    @Autowired
    ITblTaskService iTblTaskService;
    @Autowired
    ITblAssesstempleService iTblAssesstempleService;
    
    @Resource
    private UserProvider userProvider;

	@Value("${application.administrators:}")
	private String administrators;

    @Resource
    private TblAssessPlanMapper tblAssessPlanMapper;

    @Resource
    ITblAttachmentService iTblAttachmentService;

    @Autowired
    SecretLabel secretLabel;

    @OperationLog(
            success = "评价立项-主页查询成功",
            busType = "内控设置",
            fail = "评价立项-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @GetMapping(value = "/pjgl/proj_initiate")
    @Operation(summary = "评价立项-主页查询")
    public JsonBean initiatePjgl(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                 @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                 @Parameter(name = "assNumnber", description = "评价编号") @RequestParam(value = "assNumnber", required = false) String assNumnber,
                                 @Parameter(name = "assName", description = "项目名称") @RequestParam(value = "assName", required = false) String assName,
                                 @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
                                 @Parameter(name = "startDates", description = "--开始时间") @RequestParam(value = "startDates", required = false) String startDates,
                                 @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
                                 @Parameter(name = "endDates", description = "--结束日期") @RequestParam(value = "endDates", required = false) String endDates,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, staff.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
        boolean isAudit = false;
        TblOrganization organization=this.tblAssessService.queryOrganizationById(staff.getLinkDetp().getOrgid());
        System.out.println(organization);
        if (organization.getAudittype() != null
                && organization.getAudittype().equals(new BigDecimal(TblOrganization.AUDITTYPE))) {
            isAudit = true;
        }
        System.out.println("isAudit---"+isAudit);
       // IPage<TblAssessVo> iPage=tblAssessService.initiatePjgl( isAudit,staff.getCurrentOrg().getOrgid(),staff.getStaffid(),staff.getRealname(),pageNumber, assNumnber, assName, startDate, startDates, endDate, endDates,authorityType);
        PageInfo<TblAssess> iPage=tblAssessService.initiatePjglPage( isAudit,staff.getCurrentOrg().getOrgid(),staff.getStaffid(),staff.getRealname(),pageNumber,pageSize, assNumnber, assName, startDate, startDates, endDate, endDates,authorityType,staff);
        IPageResult<TblAssess> pageInfo=new IPageResult<TblAssess>().buildIpage(iPage);

        Map<String, Object> mv = new HashMap<>();
        mv.put("assNumnber", assNumnber);
        mv.put("assName", assName);
        mv.put("startDate", startDate);
        mv.put("startDates", startDates);
        mv.put("endDate", endDate);
        mv.put("endDates", endDates);
        mv.put("pageBean", pageInfo);
        return new JsonBean(200, "success", mv);
    }


    /**
     * 通用获取设置的编号
     *
     * @param
     */
    @OperationLog(
            success = "评价立项-新建页面-编号生成(这是通用方法,通过改变传值生成各类编号)查询成功",
            busType = "内控设置",
            fail = "评价立项-新建页面-编号生成(这是通用方法,通过改变传值生成各类编号)查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @GetMapping(value = "/code/findAutoNumber")
    @Operation(summary = "评价立项-新建页面-编号生成(这是通用方法,通过改变传值生成各类编号)")
    public JsonBean findAutoNumber(HttpServletResponse response,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name="tblName",description="TBL_ASSESS") @RequestParam(value = "tblName") String tblName,
                                   @Parameter(name="column",description="ASSESSID") @RequestParam(value = "column") String column,
                                   @Parameter(name="orgCol",description="TBLCOMANY") @RequestParam(value = "orgCol") String orgCol,
                                   @Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String flowNextId = null;
        try {
            flowNextId = tblAssessService.findFlowNextIdNk(tblName, column, orgCol, staff.getCurrentOrg().getOrgid(), noId,
                    null, null, null); //findFlowNextId

           // flowNextId = tblAssessService.findFlowNextId(tblName, column, orgCol, staff.getCurrentOrg().getOrgid(), noId,
                  //  null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.reset();
        return new JsonBean(200, "success", flowNextId);
    }


    /**
     * 通用获取设置的编号
     *
     * @param
     */
    @OperationLog(
            success = "评价立项/测试模板/评价模板/要素维护  自动生成编号)查询成功",
            busType = "内控设置",
            fail = "评价立项/测试模板/评价模板/要素维护  自动生成编号)查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @GetMapping(value = "/code/findAutoNumberNew")
    @Operation(summary = "评价立项/测试模板/评价模板/要素维护  自动生成编号)")
    public JsonBean findAutoNumberNew(HttpServletResponse response,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name="tblName",description="TBL_ASSESS") @RequestParam(value = "tblName") String tblName,
                                   @Parameter(name="column",description="ASSESSID") @RequestParam(value = "column") String column,
                                   @Parameter(name="orgCol",description="TBLCOMANY") @RequestParam(value = "orgCol") String orgCol,
                                   @Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String flowNextId = null;
        try {
            flowNextId = tblAssessService.findAutoNextId(tblName, column, orgCol, staff.getCurrentOrg().getOrgid(), noId,
                    null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.reset();
        return new JsonBean(200, "success", flowNextId);
    }


    /*
       选择评价负责人
       点击树--查询用户信息
    */
    @OperationLog(
            success = "评价立项-新建/修改页面-选择评价负责人列表查询成功",
            busType = "内控设置",
            fail = "评价立项-新建/修改页面-选择评价负责人列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @GetMapping(value = "/pjlx/list")
    @Operation(summary = "评价立项-新建/修改页面-选择评价负责人列表")
    public JsonBean pjlxuserListss(@Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name="pid",description="左侧树节点id",required=false) @RequestParam(value = "pid", required = false) String pid,
                                   @Parameter(name="realname",description="用户真实名",required=false) @RequestParam(value = "realname", required = false) String realname
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        BigDecimal orgid = staff.getCurrentOrg().getOrgid();// 选则的机构
        TblOrganization tblOrganization;
        Integer orgtype;
        if (StringUtils.isNotBlank(pid)) {
            tblOrganization = this.tblAssessService.queryOrganizationById(new BigDecimal(pid));
            orgid = tblOrganization.getOrgid();
            orgtype = tblOrganization.getOrgtype().intValue();
        }else{
            tblOrganization = this.tblAssessService.queryOrganizationById(orgid);
            orgtype=tblOrganization.getOrgtype().intValue();
        }

        IPage<Map<String, Object>> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

        this.tblAssessService.queryAllPageBeanPid(iPage, orgid, orgtype, realname);

        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("pid", pid);
        mv.put("pageBean", iPage);

        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "评价立项-新建/修改页面-选择模板列表查询成功",
            busType = "内控设置",
            fail = "评价立项-新建/修改页面-选择模板列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @GetMapping(value = "/pjgl/proj_tmplLocation")
    @Operation(summary = "评价立项-新建/修改页面-选择模板列表")
    public JsonBean proj_tmplLocationPjgl(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="templeNumber",description="模板编号",required=false) @RequestParam(value = "templeNumber", required = false) String templeNumber,
            @Parameter(name="orgids",description="组织机构编号s",required=true) @RequestParam(value = "orgids", required = true) String orgids,
            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
            @Parameter(name="templename",description="模板名称",required=false) @RequestParam(value = "templename", required = false) String templename) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        IPage<Map<String, Object>> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

        if (StringUtils.isNotBlank(orgids)) {
            this.tblAssessService.findAll(iPage, staff.getCurrentOrg().getOrgid().toString(), templeNumber, templename, orgids,secrectLevelId,staff);

        }

        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", iPage);
        mv.put("templeNumber", templeNumber);
        mv.put("templename", templename);
        mv.put("orgids", orgids);
        return new JsonBean(200, "success", mv);

    }


    /**
     * 评价立项 --保存项目
     *
     * @param start
     * @param end
     * @param templatekey
     * @return assessid
     * assessname
     * start
     * end
     * staffname
     * staffid
     * projname
     * orgid
     * templatename
     */
    @OperationLog(
            success = "评价立项-新建页面-保存成功",
            busType = "内控设置",
            fail = "评价立项-新建页面-保存失败",
            operationType = OperationType.ADD,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/add", produces = "application/json; charset=utf-8")
    @Operation(summary = "评价立项-新建页面-保存")
    public JsonBean addPjgl(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="assessid",description="项目编号") @RequestParam(value = "assessid")String assessid,
            @Parameter(name="assessname",description="项目名称") @RequestParam(value = "assessname")String assessname,
            @Parameter(name="start",description="开始日期") @RequestParam(value = "start") String start,
            @Parameter(name="end",description="结束日期") @RequestParam(value = "end")String end,
            @Parameter(name="staffid",description="负责人id") @RequestParam(value = "staffid")String staffid,
            @Parameter(name="templatekey",description="模板key") @RequestParam(value = "templatekey") BigDecimal templatekey,
            @Parameter(name="orgid",description="评价对象orgids") @RequestParam(value = "orgid")String orgid,
            @Parameter(name="planid",description="评价计划ID") @RequestParam(value = "planid")BigDecimal planid,
            @Parameter(name="planname",description="评价计划名称") @RequestParam(value = "planname")String planname,
            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
            @Parameter(name = "attids", description = "附件ids-多值逗号分割") @RequestParam(value = "attids", required = false)String attids,
            @ModelAttribute fieldActivationVo content)throws Exception  {

        try {
        	TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }

			if (StringUtils.isNotBlank(assessid) && StringUtils.isNotBlank(orgid)) {
			    TblAssess tblAssess=new  TblAssess();
			    // List<TblAssess> assesses =
			    // this.tblAssessService.getAssessByNumber(tblAssess.getAssessid());
			    //----------------下面有组织架构查询
			    Long count = this.tblAssessService.selectTblassessNumber(assessid, staff.getCurrentOrg().getOrgid());
			    if (count > 0) {
			        return  new JsonBean(0,"编号不能重复",null);
			    } else {
			        if (DateUtils.compare_date(end, start) == 1) {
			            tblAssess.setAssessid(assessid);
			            tblAssess.setAssessname(assessname);
			            tblAssess.setStartdate(new SimpleDateFormat("yyyy-MM-dd").parse(start)); //DateUtils.StringToLocalDateTime(start,"yyyy-MM-dd")
			            tblAssess.setEnddate(new SimpleDateFormat("yyyy-MM-dd").parse(end));
			            tblAssess.setAsstemid(templatekey);

			            tblAssess.setAssstatus(1 + "");
			            tblAssess.setAsssponsor(staff.getRealname());
                        tblAssess.setCreatestaffid(staff.getStaffid());
			            tblAssess.setTblcomany(staff.getCurrentOrg().getOrgid().toString());
			            //---密级
			            tblAssess.setSecrectLevelId(secrectLevelId);
			            tblAssess.setStaffScopeNames(staffScopeNames);
			            tblAssess.setStaffScopeIds(staffScopeIds);
			            tblAssess.setLinkDeptId(staff.getLinkDetp().getOrgid());
			            tblAssess.setLinkOrgId(staff.getCurrentOrg().getOrgid());

			             //---------------------------------
			            if (staffid != null && staffid.trim().length() > 0) {
			                tblAssess.setLeaderid(new BigDecimal(staffid)); //负责人id
			            }

//			            tblAssess.setAssteamname(assteamname);
//			            tblAssess.setAssteamlead(assteamlead);

			            tblAssess.setPlanid(planid);
			            tblAssess.setPlanname(planname);
			            tblAssess.setCreatetime(new Date());
			            if (content != null) {
				            tblAssess.setFieldActivationCopy(content);
			            }
			            return   tblAssessService.saveAssEss(staff,tblAssess,orgid,attids);

//			            //评价小组组员
//			            if(null != assteammemberids&&tblAssess.getAssid()!=null) {
//			            	String[] assteammemberidArr = assteammemberids.split(",");
//			            	for (int i = 0; i < assteammemberidArr.length; i++) {
//			            		String assteammemberid = assteammemberidArr[i];
//			            		System.out.println("assteammemberid:"+assteammemberid+"Assid："+tblAssess.getAssid());
//			            		if(StringUtils.isNotBlank(assteammemberid)){
//			            		tblAssessService.insAssessTeamMember(assteammemberid,tblAssess.getAssid());
//			            		}
//							}
//			            }
			        } else {
			            return  new JsonBean(0,"结束时间不能大于开始时间",null);
			        }

			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    @OperationLog(
            success = "评价立项-列表页面-点击评价模板查询成功",
            busType = "内控设置",
            fail = "评价立项-列表页面-点击评价模板查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @GetMapping("/pjgl/proj_diap")
    @Operation(summary = "评价立项-列表页面-点击评价模板")
    public JsonBean proj_diapPjgl(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="tmplId",description="模板id") @RequestParam(value = "tmplId") BigDecimal  tmplId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return this.tblAssessService.queryPjmb(tmplId);
    }



    /**
     * 获取项目下面的被评价对象
     *
     * @param pageNumber
     * @param selectedPlans
     * @return
     */
    @OperationLog(
            success = "评价立项-列表页面-授权列表查询成功",
            busType = "内控设置",
            fail = "评价立项-列表页面-授权列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @GetMapping(value = "/pjgl/t08_proj_org")
    @Operation(summary = "评价立项-列表页面-授权列表")
    public JsonBean t08_proj_task(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="selectedPlans",description="assid") @RequestParam(value = "selectedPlans") BigDecimal selectedPlans,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mav = new HashMap<>();
        if (null != selectedPlans) {
           // IPage<Map<String,Object>> page = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
           // this.tblAssessService.getOrgByassId(page,selectedPlans);
             PageInfo<Map<String,Object>> pageInfo=tblAssessService.getOrgByassIdNew(selectedPlans,pageNumber, ConstClass.DEFAULT_SIZE);
            IPageResult<Map<String,Object>> build= new IPageResult<Map<String,Object>>().buildIpage(pageInfo);
            TblAssess assess = tblAssessService.getById(selectedPlans);
        	mav.put("pageBean", build);
            mav.put("project",assess);
        }
        return new JsonBean(200,"success", mav);
    }


    /**
     * 项目授权
     *
     * @return
     */
    @OperationLog(
            success = "评价立项-授权列表-授权查询成功",
            busType = "内控设置",
            fail = "评价立项-授权列表-授权查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @Operation(summary = "评价立项-授权列表-授权")
    @GetMapping(value = "/pjgl/t08_proj_modify")
    public JsonBean t08_proj_modify(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="pageSize",description="pageSize",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name="assId",description="assId") @RequestParam(value = "assId") BigDecimal assId,
            @Parameter(name="orgId",description="orgId") @RequestParam(value = "orgId")BigDecimal orgId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mav = new HashMap<>();
        if (null != assId && null != orgId) {

          //  IPage<TblAssessMarkVo> page = new Page(pageNumber, pageSize);
           // this.tblAssessService.findAssessMarkVoByPageBean(page,assId,orgId);
            PageInfo<TblAssessMarkVo> pageInfo=tblAssessService.findAssessMarkIpage(pageNumber, pageSize,assId,orgId);
            mav.put("pageBean", pageInfo);
            mav.put("assId", assId);
            mav.put("pageNumber", pageNumber);
            mav.put("orgId", orgId);
            mav.put("project", this.tblAssessService.getById(assId));

        }
        return new JsonBean(200,"success", mav);
    }

    /**
     * 设置参评人权重
     *
     * @return
     */
    @OperationLog(
            success = "评价立项-授权列表-授权-设置参评人权重查询成功",
            busType = "内控设置",
            fail = "评价立项-授权列表-授权-设置参评人权重查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @Operation(summary = "评价立项-授权列表-授权-设置参评人权重")
    @GetMapping(value = "/pjgl/t08_proj_assignor")
    public JsonBean t08_proj_assignor(
            @Parameter(name="assMarkId",description="assMarkId") @RequestParam(value = "assMarkId") BigDecimal assMarkId,
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mav = new HashMap<>();

        IPage<TblAssessStaffVo> page = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
        this.tblAssessService.getTblAssessStaffByMarkId(page,assMarkId);
        mav.put("pageBean", page);
        mav.put("pageNumber", pageNumber);
        mav.put("assMarkId", assMarkId);
        mav.put("assess", tblAssessService.getTblAssessByassmarkid(assMarkId));

        return new JsonBean(200,"success", mav);
    }


    /**
     * 保存参评人权重
     *
     * @param ids
     * @param values
     * @return
     */
    @OperationLog(
            success = "评价立项-授权列表-授权-保存参评人权重成功",
            busType = "内控设置",
            fail = "评价立项-授权列表-授权-保存参评人权重失败",
            operationType = OperationType.UPDATE,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/SaveAssignor")
    @Operation(summary = "评价立项-授权列表-授权-保存参评人权重")
    public JsonBean  SaveAssignor(
            @Parameter(name="ids",description="ids") @RequestParam(value = "ids") String ids,
            @Parameter(name="values",description="values") @RequestParam(value = "values")String values,
            @Parameter(name="assMarkId",description="assMarkId") @RequestParam(value = "assMarkId") BigDecimal assMarkId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(ids) && StringUtils.isNotBlank(values)) {
            String[] idArray = ids.split(",");
            String[] varleArray = values.split(",");
            Double number = 0d;
            for (int i = 0; i < varleArray.length; i++) {
                number += Double.parseDouble(varleArray[i]);
            }
            if (number > 100) {
                return new JsonBean(-1,"权重比例设置不正确",null);
            }

            TblAssessMark tblAssessMark = this.tblAssessService.updateTblAssessStaff(assMarkId, idArray, varleArray);

            map.put("assId", tblAssessMark.getAssid());
            map.put("orgId", tblAssessMark.getAssorgid());
            map.put("tblAssessMark", tblAssessMark);

        }
        return new JsonBean(200,"success", map);
    }


    /**
     * 获取组织架构下面的人员
     *
     * @param nodeId   部门id
     * @param
     * @param  z      markids
     * @return
     */
    @OperationLog(
            success = "评价立项-授权列表-授权-右边主评人/参评人管理列表查询成功",
            busType = "内控设置",
            fail = "评价立项-授权列表-授权-右边主评人/参评人管理列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @Operation(summary = "评价立项-授权列表-授权-右边主评人/参评人管理列表")
    @GetMapping(value = "/pjgl/setting_user_list")
    public JsonBean settingUserList(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="nodeId",description="部门id",required=true) @RequestParam(value = "nodeId", required = true)  BigDecimal nodeId,
            @Parameter(name="z",description="assmarkids",required=false) @RequestParam(value = "z", required = false)        String z,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
       // IPage<TblStaff> page = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
       //iTblStaffService.findStaffByOrgid(page,nodeId);
        TblStaff staff=new TblStaff();
        staff.setOrgid(nodeId);
        PageInfo<TblStaff> page=PageMethod.startPage(pageNumber, ConstClass.DEFAULT_SIZE).doSelectPageInfo(()->iTblStaffService.findStaffByOrgid(staff));
       IPageResult<TblStaff> build=new IPageResult<TblStaff>().buildIpage(page);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pageBean", page);
        map.put("z", z);
        map.put("nodeId", nodeId);
        map.put("pageNumber", pageNumber);

        return new JsonBean(200,"success", map);
    }


    /**
     * 保存主评人
     *
     * @param z
     * @param userid
     * @return
     */
    @OperationLog(
            success = "评价立项-主评人管理列表-点击选定保存主评人成功",
            busType = "内控设置",
            fail = "评价立项-主评人管理列表-点击选定保存主评人失败",
            operationType = OperationType.ADD,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/savezupingUsers")
    @Operation(summary = "评价立项-主评人管理列表-点击选定保存主评人")
    public JsonBean  savezupingUsers(
            @Parameter(name="z",description="assmarkids") @RequestParam(value = "z")        String z,
            @Parameter(name="userid",description="用户id") @RequestParam(value = "userid")  String userid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (StringUtils.isNotBlank(z) && StringUtils.isNotBlank(userid)) {
            String[] str = z.split(",");
            return this.tblAssessService.updateAssessMarkStaffid(userid,str);
        }
        return  new JsonBean(-1,"保存失败", null);
    }


    /**
     * 保存主评人
     *
     * @param z
     * @param users
     * @return
     */
    @OperationLog(
            success = "评价立项-参评人管理列表-点击选定保存参评人成功",
            busType = "内控设置",
            fail = "评价立项-参评人管理列表-点击选定保存参评人失败",
            operationType = OperationType.ADD,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/savecanpingUsers")
    @Operation(summary = "评价立项-参评人管理列表-点击选定保存参评人")
    public JsonBean  savecanpingUsers(
            @Parameter(name="z",description="assmarkids") @RequestParam(value = "z")        String z,
            @Parameter(name="users",description="用户ids") @RequestParam(value = "users")  String users,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (StringUtils.isNotBlank(z) && StringUtils.isNotBlank(users)) {

            return this.tblAssessService.updateCanPingRen(users,z);
        }
        return  new JsonBean(-1,"保存失败", null);
    }


    /**
     * 项目立项 --启动
     *
     * @param assId
     * @return
     */
    @OperationLog(
            success = "评价立项-启动成功",
            busType = "内控设置",
            fail = "评价立项-启动失败",
            operationType = OperationType.UPDATE,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/start")
    @Operation(summary = "评价立项-启动")
    public  String start(

              @Parameter(name="assId",description="assId") @RequestParam(value = "assId")  BigDecimal assId,
               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return "用户已失效";
        }

        TblAssess assess = this.tblAssessService.getById(assId);
        if (assess.getAssstatus().equals(TblAssess.CREATE)) {

            QueryWrapper<TblAssessMark> qw=new QueryWrapper();
            qw.eq("assid",assId);
            qw.eq("suitable",1);
            qw.eq("state",1);
            List<TblAssessMark> assessMarks = iTblAssessMarkService.list(qw);

            if (assessMarks.size() == 0) {

                List<BigDecimal> staff = this.iTblTaskService.getStaff(assId);
                for (BigDecimal bigDecimal : staff) {
                    TblTask task = new TblTask();
                    task.setStaffid(bigDecimal);

                    task.setStatus(TblTask.START);
                    task.setTaskcode(assess.getAssessid());
                    task.setTaskname(assess.getAssessname());
                    task.setUrl("/nbkz/pjgl/proj_task_gradelist");
                    task.setTasktime(LocalDateTime.now());
                    this.iTblTaskService.saveTask(task);
                }
                assess.setAssstatus(TblAssess.START);
               // assess.setAssstartday(LocalDateTime.now());
               assess.setAssstartday(new Date());
                this.tblAssessService.update(assess);
                return JsonBean.success();
            } else {
                return JsonBean.error("参评人设置不完整");
            }
        }
        return JsonBean.error("不能重复启动");
    }


    /**
     * 项目立项 --修改
     *
     * @param selectedPlans
     *
     */
    @OperationLog(
            success = "评价立项-修改成功",
            busType = "内控设置",
            fail = "评价立项-修改失败",
            operationType = OperationType.UPDATE,
            subType = "评价立项"
    )
    @GetMapping(value = "/pjgl/proj_modify")
    @Operation(summary = "评价立项-修改")
    public  JsonBean proj_modifyPjgl(
            @Parameter(name = "selectedPlans", description = "selectedPlans") @RequestParam(value = "selectedPlans")  BigDecimal selectedPlans,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> map = new HashMap<String, Object>();

        if (null != selectedPlans) {
            TblAssess assess = tblAssessService.getById(selectedPlans);
           TblAssessPlan plan= tblAssessService.selectOne(assess.getPlanid());
           assess.setPlanname(plan.getAssessname());
            List<TblOrganization> organizations=this.tblAssessService.getOrgByassId(assess.getAssid());
            String orgids = "";
            String orgName = "";
            for (TblOrganization tblOrganization : organizations) {
                orgids += tblOrganization.getOrgid() + ",";
                orgName += tblOrganization.getOrgname() + ",";
            }
            //字段变活赋值realname
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
        	//对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(assess,item); 
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,assess); 
            map.put("assess", assess);
            if (orgids.length() > 0) {
                map.put("orgids", orgids.substring(0, orgids.length() - 1));
                map.put("orgName", orgName.substring(0, orgName.length() - 1));
            }
            //查询负责人
            final TblStaff fuzeren = this.iTblStaffService.getById(assess.getLeaderid());
            //查询模板
            final TblAssesstemple moban = this.iTblAssesstempleService.getById(assess.getAsstemid());
            try {
				if(moban!=null){
					item=new fieldOrgStaffId();
					BeanUtils.copyProperties(moban,item); 
					  nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,moban); 
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
            List<TblAttachment> list=tblAssessService.getAssessAttListByAssid(selectedPlans);
            map.put("fuzeren", fuzeren);
            map.put("moban", moban);

            //查询小组组长、组员
       /*     String teamleadStaffid = assess.getAssteamlead();
            BigDecimal assId = assess.getAssid();
            if(null != teamleadStaffid) {
            	TblStaff zuzhang = this.iTblStaffService.getById(teamleadStaffid);
                assess.setAssteamleadname(zuzhang.getRealname());
            }

            List<TblStaff> listTeamMember = tblAssessService.gettAssteamMembers(assId);
            if(null != listTeamMember) {
            	String assteammemberids = "";
            	String assteammember = "";
            	TblStaff teamMember = null;
            	for (int i = 0; i < listTeamMember.size(); i++) {
            		teamMember = listTeamMember.get(i);
            		if(teamMember.getStaffid()!=null){
            			System.out.println("teamMember=================================="+teamMember);

                		BigDecimal staffid = teamMember.getStaffid();
                		String realname = teamMember.getRealname();

                		assteammemberids = assteammemberids+staffid+",";
                		assteammember = assteammember+realname+",";
            		}

    			}
            	assess.setAssteammemberids(assteammemberids);
            	assess.setAssteammember(assteammember);
            }
*/
            map.put("atts",list);
        }
        return new JsonBean(200,"success", map);
    }


    @OperationLog(
            success = "评价立项(附件单独操作直接对应数据,保存修改时不修改附件)-附件上传成功",
            busType = "内控设置",
            fail = "评价立项(附件单独操作直接对应数据,保存修改时不修改附件)-附件上传失败",
            operationType = OperationType.UPLOAD,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/updateupload")
    @Operation(summary = "评价立项(附件单独操作直接对应数据,保存修改时不修改附件)-附件上传")
    public  JsonBean updateupload(HttpServletRequest request,
            @Parameter(name="assId",description="assId") @RequestParam(value = "assId")  BigDecimal assId,
            @Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile file,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "formlevel", description = "表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel
    ) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblAttachment a = new TblAttachment();

        if(formlevel!=null && !"".equals(formlevel)) {
            BigDecimal attid = null;
            Integer result = secretLabel.test(file);
            System.out.println("是否是密标文件：" + result);
            if (result == 0) {
                log.info("文件上传失败，包含非密标文件");
                return  new JsonBean(500, "包含非密标文件！", null);
            } else {
                //判断文件名是否包含密级信息
                String str = file.getOriginalFilename();
                int index = file.getOriginalFilename().lastIndexOf(".");
                if(str.length()>=4){
                    String secretFlag = str.substring(index-4, index);
                    System.out.println(secretFlag);
                    String strs = "[非密][商密][秘密][机密][公开][内部][敏感][敏感信息][普通商密][核心商密]";
                    if(strs.indexOf(secretFlag)==-1){
                        log.info("文件上传失败，文件名不包含正确密级信息！");
                        return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                    }
                }else {
                    log.info("文件上传失败，文件名不包含正确密级信息！");
                    return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                }

                //获取密级级别
                String AttachmentLevel = secretLabel.secrectLabelInfo(file);
                //根据密级信息查询密级ID
                BigDecimal attachmentLevelId = iTblAttachmentService.selectSecretLabel(AttachmentLevel);
                attid = attachmentLevelId;
                System.out.println(attid);
                a.setAttachmentlevel(attid);
                //根据表单密级信息，查询附件密级是否合理
                Integer count = iTblAttachmentService.getAttachmentList(formlevel, attachmentLevelId.toString());
                if (count == 0) {
                    log.info("文件上传失败，附件密级大于表单密级！");
                    return  new JsonBean(500, "文件上传失败，附件密级大于表单密级！", null);
                }
            }
        }
        String fileName = file.getOriginalFilename();
        System.out.println("fileName---"+fileName);
        Map<String, Object> mv = new LinkedHashMap<>();
        if (fileName != null && fileName != "") {
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            String oldname = fileName.substring(0, fileName.lastIndexOf("."));
            String newname = fileName.replace(oldname, "00"+timeInMillis);
            System.out.println(file.getInputStream());
            long size = file.getSize();
            try {
                boolean flag = FtpUtil.uploadFile(newname, file.getInputStream());
                if (flag) {
                    log.info("上传成功");
                } else {
                    log.info("上传失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            a.setAttname(fileName);
            a.setAttpath(newname);
            a.setAttsize(new BigDecimal(size / 1024));
            a.setUploader(staff.getUsername());
            a.setUploadtime(LocalDateTime.now());
            //iTblAttachmentService.save(a);
            // 添加附件 和  中间表
            tblAssessService.saveRepAtt(a,assId.toString());
            mv.put("attid",a.getAttid());
            mv.put("assId",assId);
            mv.put("Attachment",a);

        }
        return new JsonBean(200, "上传成功并加入关系表", mv);

    }


    @OperationLog(
            success = "评价立项(附件单独操作直接对应数据,保存修改时不修改附件)-附件删除成功",
            busType = "内控设置",
            fail = "评价立项(附件单独操作直接对应数据,保存修改时不修改附件)-附件删除失败",
            operationType = OperationType.DELETE,
            subType = "评价立项"
    )
    @Operation(summary = "评价立项(附件单独操作直接对应数据,保存修改时不修改附件)-附件删除")
    @PostMapping(value = "/pjgl/del_fj")
    public JsonBean updatesp_del_fj(
            @Parameter(name = "attid", description = "单个附件id") @RequestParam(value = "attid") BigDecimal attid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        this.tblAssessService.delAssessAtt(attid,null);
        return new JsonBean(200, "success", "删除附件成功");
    }


    /**
     * 执行修改操作
     *
     * @param
     * @param
     * @return
     */
    @OperationLog(
            success = "评价立项-修改-保存修改成功",
            busType = "内控设置",
            fail = "评价立项-修改-保存修改失败",
            operationType = OperationType.UPDATE,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/proj_update")
    @Operation(summary = "评价立项-修改-保存修改")
    public JsonBean proj_update(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="assid",description="assid") @RequestParam(value = "assid")String assid,
            @Parameter(name = "attids", description = "附件ids-多值逗号分割") @RequestParam(value = "attids", required = false)String attids,
            @Parameter(name="assessname",description="项目名称") @RequestParam(value = "assessname")String assessname,
            @Parameter(name="start",description="开始日期") @RequestParam(value = "start") String start,
            @Parameter(name="end",description="结束日期") @RequestParam(value = "end")String end,
            @Parameter(name="staffid",description="负责人id") @RequestParam(value = "staffid")String staffid,
            @Parameter(name="templatekey",description="模板key") @RequestParam(value = "templatekey") BigDecimal templatekey,
            @Parameter(name="orgid",description="评价对象orgids") @RequestParam(value = "orgid")String orgid,
            @Parameter(name="planid",description="评价计划ID") @RequestParam(value = "planid")BigDecimal planid,
            @Parameter(name="planname",description="评价计划名称") @RequestParam(value = "planname")String planname,
            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
            @ModelAttribute fieldActivationVo content
            /* @Parameter(name="assteamname",description="评价小组名称") @RequestParam(value = "assteamname")String assteamname,
            @Parameter(name="assteamlead",description="评价小组组长ID") @RequestParam(value = "assteamlead")String assteamlead,
            @Parameter(name="assteammemberids",description="评价小组组员IDS（,分隔）") @RequestParam(value = "assteammemberids")String assteammemberids
            */)throws Exception  {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (null != assid && StringUtils.isNotBlank(orgid)) {

            return tblAssessService.updateAssEss(staff,assid,assessname,start,end,staffid,templatekey,orgid,null,null,null
            		, planid, planname, secrectLevelId, staffScopeNames, staffScopeIds,content,attids);

        }
        return null;
    }



    /**
     * 项目立项 --删除
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "评价立项-删除成功",
            busType = "内控设置",
            fail = "评价立项-删除失败",
            operationType = OperationType.DELETE,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/proj_delete")
    @Operation(summary = "评价立项-删除")
    public @ResponseBody String proj_deletePjgl(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="assid",description="assid") @RequestParam(value = "assid")BigDecimal assid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return "用户已失效";
        }
        return this.tblAssessService.deleteAssess(assid);

    }


    @OperationLog(
            success = "评价立项-主评人/参评人-左侧部门树查询成功",
            busType = "内控设置",
            fail = "评价立项-主评人/参评人-左侧部门树查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @Operation(summary = "评价立项-主评人/参评人-左侧部门树")
    @GetMapping(value = "/pjgl/tree")
    public JsonBean  zupingtree(
            @Parameter(name="z",description="assmarkid如果多值用逗号隔开") @RequestParam(value = "z") String z,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception  {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        String[] ids = z.split(",");
        if(ids.length>0){
        	for(String s:ids){
        	if(StringUtils.isNotBlank(s)){
            TblAssessMark assessMark=this.iTblAssessMarkService.getById(s);
            if(assessMark==null){
                return new JsonBean(0,"assmarkid传值不正确,数据库无此数据", null);
            }else{
             // List<Map<String,Object>> treeList= this.tblAssessService.getOrgTree(assessMark.getAssorgid());
              String str;
              List<Tree> list=null;
              try {
            	  HashMap<String, Object> fields = new HashMap<String, Object>();
                  fields.put("nodeId", assessMark.getAssorgid());
                  str = HttpClient.request(HttpClient.getDeptUrl, fields, null);
                  if (!StringUtils.isNotBlank(str)) {
                       list = this.tblAssessService.getNodeAllbm(assessMark.getAssorgid());
                  }
                  list=JSONArray.fromObject(str);

              } catch (Exception e) {
                   list = this.tblAssessService.getNodeAllbm(assessMark.getAssorgid());
              }
              map.put("tree", list);
            }
            break;
        	}
        	}
        }
        map.put("z", z);
        return new JsonBean(200,"成功", map);
    }

    @OperationLog(
            success = "修改要素适用性成功",
            busType = "内控设置",
            fail = "修改要素适用性失败",
            operationType = OperationType.UPDATE,
            subType = "评价立项"
    )
		  @Operation(summary = "修改要素适用性")
		    @GetMapping(value = "/pjgl/updateShiYongXing")
		    public JsonBean  zupingtree(
		            @Parameter(name="markId",description="markId") @RequestParam(value = "markId") BigDecimal markId,
		            @Parameter(name = "type", description = "1 为适用  0 为不适用", required = true) @RequestParam(value = "type")String type,
		            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
		    ) throws Exception  {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		if (null != markId && StringUtils.isNotBlank(type)) {
			QueryWrapper<TblAssessMark> qw = new QueryWrapper<TblAssessMark>();
			qw.eq("assmarkid", markId);
			TblAssessMark assessMark = this.iTblAssessMarkService.getOne(qw);
			assessMark.setSuitable(type.equals("1") ? "1" : "0");
			this.iTblAssessMarkService.updateById(assessMark);
			 return new JsonBean(200,"成功", assessMark);
		}
		return new JsonBean(0,"失败", null);
	}

    @OperationLog(
            success = "评价立项-参评人管理列表-删除参评人成功",
            busType = "内控设置",
            fail = "评价立项-参评人管理列表-删除参评人失败",
            operationType = OperationType.DELETE,
            subType = "评价立项"
    )
		  @DeleteMapping(value = "/pjgl/deletecanpingUsers")
		    @Operation(summary = "评价立项-参评人管理列表-删除参评人")
		    public JsonBean  deletecanpingUsers(
		            @Parameter(name = "z", description = "assmarkids")@RequestParam(value = "z")String z,
		            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		        if (StringUtils.isNotBlank(z)) {
		            return this.tblAssessService.deleteCanPingRen(z);
		        }
		        return  new JsonBean(0,"保存失败", null);
		    }



	//=================================================评价计划
    @OperationLog(
            success = "评价计划-列表查询成功",
            busType = "内控设置",
            fail = "评价计划-列表查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
	@GetMapping(value = "/pjgl/plan_initiate")
    @Operation(summary = "评价计划-列表")
    public JsonBean initiatePjjh(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
    		                     @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                 @Parameter(name = "assNumnber", description = "评价编号") @RequestParam(value = "assNumnber", required = false) String assNumnber,
                                 @Parameter(name = "assName", description = "项目名称") @RequestParam(value = "assName", required = false) String assName,
                                 @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
                                 @Parameter(name = "startDates", description = "--开始时间") @RequestParam(value = "startDates", required = false) String startDates,
                                 @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
                                 @Parameter(name = "endDates", description = "--结束日期") @RequestParam(value = "endDates", required = false) String endDates,
                                 @Parameter(name = "status", description = "审批状态") @RequestParam(value = "status", required = false) Integer status,
                                 @Parameter(name = "secrectLevelId", description = "密级级别") @RequestParam(value = "secrectLevelId", required = false) BigDecimal secrectLevelId,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, staff.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
        boolean isAudit = false;
        TblOrganization organization=this.tblAssessService.queryOrganizationById(staff.getLinkDetp().getOrgid());
        if (organization.getAudittype() != null
                && organization.getAudittype().equals(new BigDecimal(TblOrganization.AUDITTYPE))) {
            isAudit = true;
        }
       // IPage<TblAssessPlanVo> iPage=tblAssessService.initiatePjjh( isAudit,staff.getCurrentOrg().getOrgid(),staff.getStaffid(),staff.getRealname(),pageNumber, assNumnber, assName, startDate, startDates, endDate, endDates,status,authorityType);

        PageInfo<TblAssessPlan> iPage=tblAssessService.initiatePjjhPage( isAudit,staff.getCurrentOrg().getOrgid(),staff.getStaffid(),staff.getRealname(),pageNumber, assNumnber, assName, startDate, startDates, endDate, endDates,status,authorityType,pageSize,staff,secrectLevelId);
		IPageResult<TblAssessPlan> build = new IPageResult<TblAssessPlan>().buildIpage(iPage);
        Map<String, Object> mv = new HashMap<>();
        mv.put("assNumnber", assNumnber);
        mv.put("assName", assName);
        mv.put("startDate", startDate);
        mv.put("startDates", startDates);
        mv.put("endDate", endDate);
        mv.put("endDates", endDates);
        mv.put("pageBean", build);
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "评价计划-修改成功",
            busType = "内控设置",
            fail = "评价计划-修改失败",
            operationType = OperationType.UPDATE,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/plan_update")
    @Operation(summary = "评价计划-修改")
    public JsonBean plan_update(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="assid",description="assid") @RequestParam(value = "assid")String assid,
            @Parameter(name="assessid",description="assessid") @RequestParam(value = "assessid")String assessid,
            @Parameter(name="assessname",description="项目名称") @RequestParam(value = "assessname")String assessname,
            @Parameter(name="start",description="开始日期") @RequestParam(value = "start") String start,
            @Parameter(name="end",description="结束日期") @RequestParam(value = "end")String end,
            @Parameter(name="staffid",description="负责人id") @RequestParam(value = "staffid")String staffid,
            @Parameter(name="orgids",description="评价对象orgids") @RequestParam(value = "orgids")String orgids,
            @Parameter(name="orgnames",description="评价对象orgnames") @RequestParam(value = "orgnames")String orgnames,
            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,//@Parameter(name="staffScopeIds",description="staffScopeIds",required=false) @RequestParam(value = "assteamname")String assteamname,
//            @Parameter(name="assteamlead",description="评价小组组长ID") @RequestParam(value = "assteamlead")String assteamlead,
//            @Parameter(name="assteammemberids",description="评价小组组员IDS（,分隔）") @RequestParam(value = "assteammemberids")String assteammemberids
            @Parameter(name = "attids", description = "附件ids-多值逗号分割") @RequestParam(value = "attids", required = false)String attids,@RequestBody fieldActivationVo content)throws Exception  {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (null != assid && StringUtils.isNotBlank(orgids)) {
        	 TblAssessPlan assess = this.tblAssessPlanMapper.selectById(assid);
             assess.setAssessid(assessid);
                   assess.setAssessname(assessname);
                   assess.setAssobjids(orgids);
                   assess.setAssobjnames(orgnames);
                   //---------------------------------
                   if (staffid != null && staffid.trim().length() > 0) {
                       assess.setLeaderid(new BigDecimal(staffid)); //负责人id
                   }
//                   assess.setAssteamname(assteamname);
//                   assess.setAssteamlead(assteamlead);
                   assess.setContent(content.getContent());
                   assess.setSecrectLevelId(secrectLevelId);
                   assess.setStaffScopeNames(staffScopeNames);
                   assess.setStaffScopeIds(staffScopeIds);
                   assess.setFieldActivationCopy(content);
            return tblAssessService.updateAssPlan(staff,start,end,attids,assess);
        }
        return null;
    }

    @OperationLog(
            success = "评价计划-新增成功",
            busType = "内控设置",
            fail = "评价计划-新增失败",
            operationType = OperationType.ADD,
            subType = "评价立项"
    )
    @PostMapping(value = "/pjgl/plan_add", produces = "application/json; charset=utf-8")
    @Operation(summary = "评价计划-新增")
    public JsonBean plan_add(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="assessid",description="项目编号") @RequestParam(value = "assessid")String assessid,
            @Parameter(name="assessname",description="项目名称") @RequestParam(value = "assessname")String assessname,
            @Parameter(name="start",description="开始日期") @RequestParam(value = "start") String start,
            @Parameter(name="end",description="结束日期") @RequestParam(value = "end")String end,
            @Parameter(name="staffid",description="负责人id") @RequestParam(value = "staffid")String staffid,
            @Parameter(name="orgids",description="评价对象orgids") @RequestParam(value = "orgids")String orgids,
            @Parameter(name="orgnames",description="评价对象orgnames") @RequestParam(value = "orgnames")String orgnames,
            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
//            @Parameter(name = "assteamname", description = "评价小组名称") @RequestParam(value = "assteamname")String assteamname,
//            @Parameter(name = "assteamlead", description = "评价小组组长ID") @RequestParam(value = "assteamlead")String assteamlead,
//            @Parameter(name = "assteammemberids", description = "评价小组组员IDS（,分隔）") @RequestParam(value = "assteammemberids")String assteammemberids,
             @Parameter(name = "attids", description = "附件ids-多值逗号分割") @RequestParam(value = "attids", required = false)String attids,@RequestBody fieldActivationVo content)throws Exception  {
        try {
        	TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
			if (StringUtils.isNotBlank(assessid) && StringUtils.isNotBlank(orgids)) {
			    TblAssessPlan tblAssess=new  TblAssessPlan();
//			    Long count = this.tblAssessService.selectTblassessNumber(assessid, staff.getCurrentOrg().getOrgid());
//			    if (count > 0) {
//			        return  new JsonBean(0,"编号不能重复",null);
//			    } else {
			        if (DateUtils.compare_date(end, start) == 1) {
			            tblAssess.setAssessid(assessid);
			            tblAssess.setAssessname(assessname);
			           // tblAssess.setStartdate(DateUtils.StringToLocalDateTime(start,"yyyy-MM-dd"));
			           // tblAssess.setEnddate(DateUtils.StringToLocalDateTime(end,"yyyy-MM-dd"));
			            tblAssess.setStartdate(new SimpleDateFormat("yyyy-MM-dd").parse(start));
				        tblAssess.setEnddate(new SimpleDateFormat("yyyy-MM-dd").parse(end));
			            tblAssess.setAssstatus(1 + "");
			            tblAssess.setAssobjids(orgids);
			            tblAssess.setAssobjnames(orgnames);
			            tblAssess.setSecrectLevelId(secrectLevelId);
			            tblAssess.setStaffScopeNames(staffScopeNames);
			            tblAssess.setStaffScopeIds(staffScopeIds);
			             //---------------------------------
			            if (staffid != null && staffid.trim().length() > 0) {
			                tblAssess.setLeaderid(new BigDecimal(staffid)); //负责人id
			            }
			            tblAssess.setContent(content.getContent());
			            tblAssess.setAssteamname("");
			            tblAssess.setAssteamlead("");
                        /**
                         * 20230728
                         * 新增时把创建人和创建时间赋上值
                         */
                        tblAssess.setCreatestaffid(staff.getStaffid());
                       // tblAssess.setCreatetime(LocalDateTime.now());
                        tblAssess.setCreatetime(new Date());
                        tblAssess.setLinkorgid(staff.getLinkOrg().getOrgid());
                        tblAssess.setLinkdeptid(staff.getLinkDetp().getOrgid());
                        //
                        if(content!=null){
                        	tblAssess.setFieldActivationCopy(content);
                        }
                        
			            tblAssessService.saveAssPlan(staff,tblAssess,orgids,attids);

			            return  new JsonBean(1,"成功",null);
			        } else {
			            return  new JsonBean(0,"结束时间不能大于开始时间",null);
			        }
//			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    @OperationLog(
            success = "评价计划-明细查询成功",
            busType = "内控设置",
            fail = "评价计划-明细查询失败",
            operationType = OperationType.SELECT,
            subType = "评价立项"
    )
    @GetMapping(value = "/pjgl/plan_detail")
    @Operation(summary = "评价计划-明细")
    public  JsonBean plan_detail(
            @Parameter(name = "selectedPlans", description = "selectedPlans") @RequestParam(value = "selectedPlans")  BigDecimal selectedPlans,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> map = new HashMap<String, Object>();

        if (null != selectedPlans) {
            TblAssessPlan assess = tblAssessPlanMapper.selectById(selectedPlans);
            //字段变活赋值realname
            FiexibleNameAssignment ment=new FiexibleNameAssignment();
        	//对灵活字段中的姓名名称及机构名称赋值
			fieldOrgStaffId item=new fieldOrgStaffId();
			BeanUtils.copyProperties(assess,item); 
			fieldOrgStaffName nameEntity=ment.setOpenName(item);
			BeanUtils.copyProperties(nameEntity,assess ); 
            map.put("assess", assess);
            //查询负责人
            final TblStaff fuzeren = this.iTblStaffService.getById(assess.getLeaderid());
            List<TblAttachment> list=tblAssessService.getAssessAttListByAssid(selectedPlans);
           
            map.put("fuzeren", fuzeren);
             map.put("atts",list);
        }
        return new JsonBean(200,"success", map);
    }

    @OperationLog(
            success = "评价计划-删除成功",
            busType = "内控设置",
            fail = "评价计划-删除失败",
            operationType = OperationType.DELETE,
            subType = "评价立项"
    )
	@DeleteMapping("/pjgl/plan/delete/{id}")
	@Operation(summary = "评价计划-删除")
	public JsonBean deletePlanDetail(@PathVariable() BigDecimal id, @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		//删除
		tblAssessPlanMapper.deleteById(id);
		return ResponseFormat.retParam(200, 200, null);
	}
}
