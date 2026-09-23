package com.huabo.fxgl.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.RiskAssessmentstd;
import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskAssplanAtt;
import com.huabo.fxgl.entity.RiskAssplanOrg;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.huabo.fxgl.entity.RiskRiskmarking;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.Task;
import com.huabo.fxgl.entity.TblNbkzRiskEntity;
import com.huabo.fxgl.entity.TblNbsjBugEntity;
import com.huabo.fxgl.entity.TblNbsjRisktolerability;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IRiskAssessmentstdService;
import com.huabo.fxgl.service.IRiskAssplanAttService;
import com.huabo.fxgl.service.IRiskAssplanOrgService;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.huabo.fxgl.service.IRiskAssplanService;
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
import com.huabo.fxgl.vo.TblFxgkBugVo;
import com.huabo.fxgl.vo.TblNbkzRiskVo;
import com.huabo.fxgl.vo.fieldActivationVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-03
 */
@Slf4j
@Tag(name="风险管控 - 风险评估 - 评估计划",description="风险管控 - 风险评估 - 评估计划")
@RestController
@RequestMapping(value = "/pggl")
public class RiskAssplanController {


    @Autowired
    private IRiskAssplanService riskAssplanService;

    @Autowired
    private IRiskAssessmentstdService iRiskAssessmentstdService;

    @Autowired
    private IRiskAssplanService iriskAssplanService;
    @Autowired
    private IRiskAssplanOrgService iRiskAssplanOrgService;
    @Autowired
    private IRiskAssplanAttService riskAssplanAttService;

    @Autowired
    private IOrganizationService iOrganizationService;

    @Autowired
    private IRiskAssplanRiskService iRiskAssplanRiskService;

    @Autowired
    private ITaskService iTaskService;

    @Autowired
    private IRiskeventService riskEventService;

    @Autowired
    private IRiskcategoryService riskCategoryService;
    @Autowired
    private IRiskService riskService;

    @Autowired
    private IRiskRiskmarkingService riskRiskmarkingService;

    @Autowired
    private IAttachmentService iAttachmentService;

    @Autowired
    private IRiskAssplanAttService iRiskAssplanAttService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IRiskAssplanRiskService riskAssplanRiskService;

 


    @Autowired
    private IStaffService staffService;

    @Autowired
    private IOrganizationService organizationService;

    @Resource
    public TblNbsjBugService tblNbsjBugService;

    @Resource
    public TblNbkzRiskService nbkzRiskService;

    @Resource
    public TblFxgkBugService tblFxgkBugService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 评估计划查询
     *
     * @param pageNo       当前页数
     * @param token        登录用户数据
     * @param riskAssplan
     * @param pageSize     每页条数
     * @param choiceSearch 查询框显示状态
     * @return data:2022.8.3 author: xujiajun
     * @throws Exception
     */
    @OperationLog(
            success = "获取评估计划列表",
            busType = "风险评估",
            fail = "获取评估计划列表",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @RequestMapping(value = "/pggl/riplanlist", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "评估计划查询 /pggl/riplanlist")
    public JsonBean riplanlist(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "plancode", description = "查询条件 -计划编号", required = false) @RequestParam(value = "plancode", required = false) String plancode,
            @Parameter(name = "planName", description = "查询条件 -计划名称", required = false) @RequestParam(value = "planName", required = false) String planName,
            @Parameter(name = "startDate", description = "查询条件 -计划时间开始时间", required = false) @RequestParam(value = "startDate", required = false) String startDate,
            @Parameter(name = "endDate", description = "查询条件 -计划时间结束时间", required = false) @RequestParam(value = "endDate", required = false) String endDate,
            @Parameter(name = "planType", description = "查询条件 -计划类型 1-年度计划  2-临时性计划", required = false) @RequestParam(value = "planType", required = false) String planType,
            @Parameter(name = "planStatus", description = "查询条件 -计划状态 1-未开始 2-评估中 3-已完成", required = false) @RequestParam(value = "planStatus", required = false) String planStatus,
			@Parameter(name = "aprstatus", description = "查询条件 -审批状态", required = false) @RequestParam(value = "aprstatus", required = false) String aprstatus
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        IPage page = new Page<>(pageNo, pageSize);//分页设置
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        BigDecimal orgid = selectOrg.getOrgid(); //当前用户选择的组织
        /*if (selectOrg != null && selectOrg.getOrderid() != null) {
            orgid = selectOrg.getOrgid();
        }*/
        RiskAssplan riskAssplan = new RiskAssplan();
        riskAssplan.setPlancode(plancode);
        riskAssplan.setPlanName(planName);
		riskAssplan.setAprstatus(aprstatus);
        riskAssplan.setStartDate(DateUtil.formatDate(startDate, "yyyy-MM-dd"));
        riskAssplan.setEndDate(DateUtil.formatDate(endDate, "yyyy-MM-dd"));
        riskAssplan.setPlanType(planType);
        riskAssplan.setPlanStatus(planStatus);

        PageInfo<RiskAssplan> pageBean = riskAssplanService.findAll(riskAssplan, orgid,pageNo,pageSize,staffUtil);
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("pageBean", pageBean);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    @OperationLog(
            success = "获取风险评估计划编号查询处理成功",
            busType = "风险管控",
            fail = "获取风险评估计划编号查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )

    @OperationLog(
			success = "获取风险评估计划编号",
			busType = "风险评估",
			fail = "获取风险评估计划编号",
			operationType = OperationType.SELECT,
			subType = "评估计划"
	)
    @RequestMapping(value = "/get_riskpgplan_no",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "获取风险评估计划编号")
    public JsonBean get_riskpgplan_no(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    		) throws Exception {
        JsonBean jsonBean = this.riskAssplanService.get_riskpgplan_no(token);

        return jsonBean;
    }

    @OperationLog(
            success = "获取评估人员列表",
            busType = "风险评估",
            fail = "获取评估人员列表",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @RequestMapping(value = "/setting_user_list", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "评估人员列表 /setting_user_list")
    public JsonBean settingUserList(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "orgid", description = "查询公司或部门主键  首次访问为空 默认查找全公司人员", required = false) @RequestParam(value = "orgid", required = false) BigDecimal orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        IPage page = new Page(pageNo, pageSize);
        Organization organization = null;
        if (null != orgid) {
            organization = organizationService.getById(orgid);
        } else {
            organization = organizationService.getById(selectOrg.getOrgid());
            ;
        }
        IPage pageBean = staffService.findAllPageBeanPid(page, organization);
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("orgid", orgid);
        result.put("pageBean", pageBean);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * @author wanghongtuo
     * @Param planid -- ASSPLANID
     * @Param riskid --
     * @Date 2022/8/10
     * @Des: 修改 - 删除风险按钮
     * @version 1.0.1
     */
    @OperationLog(
            success = "评估计划删除关联风险点【{{#riskid}}】成功",
            busType = "风险评估",
            fail = "评估计划删除关联风险点【{{#riskid}}】失败",
            operationType = OperationType.DELETE,
            subType = "评估计划"
    )
    @RequestMapping(value = "/pggl/delPgglByRisk", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "修改 - 删除风险按钮/pggl/delPgglByRisk")
    public JsonBean deleteRiskByRisk(@Parameter(name = "planid", description = "评估计划ID", required = true) @RequestParam(required = true) BigDecimal planid,
                                     @Parameter(name = "riskid", description = "风险基本信息ID，并非assriskid", required = true) @RequestParam(required = true) String riskid,//传参数对应的数据库中的RISKID（风险点编号）,要删除的是ASSRISKID
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        List<RiskAssplanRisk> listRiskAssPlanRisk = riskAssplanRiskService.getByPlanid(planid);//ASSPLANID

        for (RiskAssplanRisk riskAssPlanRisk : listRiskAssPlanRisk) {
            log.info(JSONObject.toJSONString(riskAssPlanRisk.getRisk()));
            if (riskAssPlanRisk.getRisk() != null && riskid.equals(riskAssPlanRisk.getRisk().getRiskid().toString())) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("ASSRISKID", riskAssPlanRisk.getAssriskid());
                riskRiskmarkingService.remove(queryWrapper);//删除所有对应的RiskMarking
                riskAssplanRiskService.removeById(riskAssPlanRisk.getAssriskid());
            }
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    /**
     * 修改 --- 选择风险 ---> 选定（按钮）
     *
     * @param selectList 风险点编号(ASSRISKID)组成的集合  --->  113337,113339,113391,113341,
     * @param assplanid  (ASSPLANID)
     * @return
     * @author wanghongtuo
     * @version 1.0.1
     * @Date 2022/8/15
     */
    @OperationLog(
            success = "评估计划关联风险点【{{#assplanid}}】成功",
            busType = "风险评估",
            fail = "评估计划关联风险点【{{#assplanid}}】失败",
            operationType = OperationType.ADD,
            subType = "评估计划"
    )
    @RequestMapping(value = "/pggl/riskadd", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "修改 --- 选择风险 ---> 选定（按钮）/pggl/riskadd")
    public JsonBean addFK(@Parameter(name="selectList",description="选中的风险风险id使用,分割",required=true) @RequestParam(required = true) String selectList,
                          @Parameter(name = "assplanid", description = "评估计划id，必须是真实存在的评估计划，且状态必须是 ‘未开始’", required = true) @RequestParam(required = true) BigDecimal assplanid,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if (StringUtils.isNotBlank(selectList) && null != assplanid) {
//            String[] ids = selectList.substring(0, selectList.length() - 1).split(",");
        	String[] ids = selectList.split(",");
            List<String> checkedIdList = new ArrayList<>(Arrays.asList(ids));

            RiskAssplan assplan = riskAssplanService.getById(assplanid);
            List<RiskAssplanRisk> have = new ArrayList<RiskAssplanRisk>();
            //原本已选中的风险列表
            List<RiskAssplanRisk> listRiskAssPlanRisk = riskAssplanRiskService.getByPlanid(assplanid);
            if (null != assplan) {
                for (RiskAssplanRisk riskAssPlanRisk : listRiskAssPlanRisk) {
                    if (riskAssPlanRisk.getRisk() != null && riskAssPlanRisk.getRisk().getRiskid() != null
                            && checkedIdList.contains(riskAssPlanRisk.getRisk().getRiskid().toString())) {
                        String riskid = riskAssPlanRisk.getRisk().getRiskid().toString();
                        have.add(riskAssPlanRisk);

                        checkedIdList.remove(riskid);//  riskid  --- assriskid 将原本就存在的assriskid从列表里移除，assriskid是风险点编号的id
                    }
//                    else {
//                        QueryWrapper queryWrapper = new QueryWrapper();
//                        queryWrapper.eq("ASSRISKID", riskAssPlanRisk.getAssriskid());
//                        riskRiskmarkingService.remove(queryWrapper);//删除所有对应的RiskMarking
//                        riskAssplanRiskService.removeById(riskAssPlanRisk.getAssriskid());
//                    }
                }
                log.info("----------------------------------------checkedIdList: " + checkedIdList);

                for (String otherid : checkedIdList) {
                    BigDecimal riskid = new BigDecimal(otherid).setScale(0, BigDecimal.ROUND_HALF_UP);

//                    Risk risk = riskService.getById(riskid);
//                    Risk risk = new Risk();
//                    risk.setRiskid(riskid);

                    log.info("----------------------------------------(assplanid, riskid): " + assplanid + "," + riskid);
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("ASSPLANID", assplanid);
                    queryWrapper.eq("RISKID", riskid);

                    if (riskAssplanRiskService.count(queryWrapper) == 0) {
                        RiskAssplanRisk riskAssPlanRisk = new RiskAssplanRisk();

//                        riskAssPlanRisk.setRisk(risk);
                        riskAssPlanRisk.setAssplan(assplan);
                        riskAssPlanRisk.setAssstatus("0");
                        riskAssPlanRisk.setRiskid(riskid);

                        riskAssplanRiskService.save(riskAssPlanRisk);
                    }
                }
            }
            return ResponseFormat.retParam(1, 200, null);
        }
        return ResponseFormat.retParam(0, "保存失败", null);
    }

    /**
     * 修改 - 设置评估人员
     *
     * @param users     STAFFID 用户ID
     * @param assplanid assplanid
     * @param assriskid ASSRISKID
     * @return
     * @author wanghongtuo
     * @Date 2022/8/15
     */
    @OperationLog(
            success = "设置评估人员成功",
            busType = "风险评估",
            fail = "设置评估人员失败",
            operationType = OperationType.ADD,
            subType = "评估计划"
    )
    @RequestMapping(value = "/pggl/saveUsers", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "修改 - 设置评估人员/pggl/saveUsers")
    public JsonBean saveUsers(@Parameter(name = "users", description = "选择的人员ID，多个用,分割", required = true) @RequestParam(required = true, value = "users") String users,
                              @Parameter(name = "assriskid", description = "新增修改页面选择分配的风险基本信息ID，多个用,分割", required = true) @RequestParam(required = true, value = "assriskid") String assriskid,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String[] ids = users.split(",");
        String[] pids = assriskid.split(",");
        for (String string : pids) {
            BigDecimal pd = new BigDecimal(string);
            QueryWrapper<RiskRiskmarking> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("assriskid", pd);
            riskRiskmarkingService.remove(queryWrapper);//将原来的评估人删除
        }
        for (String userid : ids) {
            if ("undefined".equals(userid)) {
                continue;
            }
            BigDecimal bd = new BigDecimal(userid);
            Staff staff = staffService.getById(bd);
            for (String pid : pids) {
                BigDecimal pd = new BigDecimal(pid);
                RiskAssplanRisk riskAssPlanRisk = riskAssplanRiskService.getById(pd);
                RiskRiskmarking marking = new RiskRiskmarking();
                marking.setStaff(staff);
                marking.setAsssatus(new BigDecimal(0));
                marking.setRiskAssplanRisk(riskAssPlanRisk);
                riskRiskmarkingService.save(marking);//加入新选的评估人

            }
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    /**
     * 修改 - 设置评估人员- 弹出窗口
     *
     * @param choiceSearch
     * @param token
     * @return
     * @author wanghongtuo
     * @version 1.0.1
     * @Date 2022/8/17
     */
    @OperationLog(
            success = "修改 - 设置评估人员- 弹出窗口处理成功",
            busType = "风险管控",
            fail = "修改 - 设置评估人员- 弹出窗口处理失败",
            operationType = OperationType.UPDATE,
            subType = "风险评估"
    )
    @RequestMapping(value = "/setting_user_main", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "iframe框架使用   vue弃用  修改 - 设置评估人员- 弹出窗口/setting_user_main ")
    public JsonBean settingUserMain(@Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }
        //为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        Map result = new HashMap();
        result.put("choiceSearch", choiceSearch);
        JsonBean jsonBean = new JsonBean();
        jsonBean.setData(result);
        jsonBean.setCode(200);
        jsonBean.setMsg("操作成功!");
        return jsonBean;
    }

    /**
     * 修改 - 设置评估人员- 弹出窗口左侧
     *
     * @param token
     * @return
     * @author wanghongtuo
     * @version 1.0.1
     * @Date 2022/8/17
     */
    @OperationLog(
            success = "修改 - 设置评估人员- 弹出窗口左侧处理成功",
            busType = "风险管控",
            fail = "修改 - 设置评估人员- 弹出窗口左侧处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @RequestMapping(value = "/setting_user_left", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "iframe框架使用   vue弃用  修改 - 设置评估人员- 弹出窗口左侧/setting_user_left")
    public JsonBean settingUserLeft(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }
        Map result = new HashMap();
        result.put("orgid", 1);
        JsonBean jsonBean = new JsonBean();
        jsonBean.setData(result);
        jsonBean.setCode(200);
        jsonBean.setMsg("操作成功!");
        return jsonBean;
    }


    /**
     * 进入新建页面
     *
     * @param choiceSearch
     * @param token
     * @return JsonBean
     * @throws Exception
     * @auther guanhongyi
     * @date 2022/8/13
     * @version 1.0.1
     */
    @OperationLog(
            success = "新建页面获取基础信息",
            busType = "风险评估",
            fail = "新建页面获取基础信息",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @Operation(summary = " 进入新建页面 /pggl/riplanaddpage")
    @RequestMapping(value = "/riplanaddpage", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean riplanAddPage(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // 获取当前用户选择的组织信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("orgByUser", selectOrg);
        result.put("Staff", staffUtil);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }


    /**
     * 弹层查询信息--获取评估标准
     *
     * @param pageNo
     * @param pageSize
     * @param assName
     * @param choiceSearch
     * @return JsonBean
     * @auther guanhongyi
     * @date 2022/8/13
     * @version 1.0.1
     */
    @OperationLog(
            success = "评估标准查询",
            busType = "风险评估",
            fail = "评估标准查询",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @Operation(summary = "评估计划新建、修改-评估标准查询： /pggl/risk_main")
    @RequestMapping(value = "/risk_main", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean riskMain(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
                             @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
                             @Parameter(name = "assName", description = "评估标准名称", required = false) @RequestParam(value = "assName", required = false) String assName,
                             @Parameter(name = "secrectLevelId", description = "密级级别", required = false) @RequestParam(value = "secrectLevelId", required = false) String secrectLevelId
                             ) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }
        //查询评估标准
        PageInfo<RiskAssessmentstd> pageBean = iRiskAssessmentstdService.selectEvaluateStandard(assName,staffUtil.getCurrentOrg().getOrgid(),pageNo, pageSize,staffUtil,secrectLevelId);
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("pageBean", pageBean);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }


    /**
     * 保存评估基本信息
     *
     * @param token
     * @param plancode
     * @param planName
     * @param starttime_max
     * @param starttime_min
     * @param planType
     * @param planDes
     * @param assId
     * @return JsonBean
     * @auther guanghongyi
     * @Date 2022/8/13
     * @version 1.0.1
     */
    @OperationLog(
            success = "新增/修改评估计划【{{#planName}}】成功",
            busType = "风险评估",
            fail = "新增/修改评估计划【{{#planName}}】失败",
            operationType = OperationType.ADD,
            subType = "评估计划"
    )
    @Operation(summary = "保存计划：/pggl/riplanadd")
    @RequestMapping(value = "/riplanadd", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean riplanAdd(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "plancode", description = "plancode 评估计划编号", required = true) @RequestParam(required = true) String plancode,
                              @Parameter(name = "planName", description = "planName 评估计划名称", required = true) @RequestParam(required = true) String planName,
                              @Parameter(name = "unit", description = "unit 组织id", required = true) @RequestParam(required = false) String unit,
                              @Parameter(name = "endDate", description = "starttime_max 计划结束时间，必须大于计划开始时间", required = false) @RequestParam(required = true, name = "endDate") String starttime_max,
                              @Parameter(name = "startDate", description = "starttime_min 计划开始时间，必须大于计划开始时间", required = false) @RequestParam(required = true, name = "startDate") String starttime_min,
                              @Parameter(name = "planType", description = "planType 计划类型  1-年度计划  2-临时性计划", required = false) @RequestParam(required = true) String planType,
                              @Parameter(name = "planDes", description = "planDes 评估计划描述 ", required = false) @RequestParam(required = false) String planDes,
                              @Parameter(name = "reporteds", description = "reporteds 上传附件ids", required = false) @RequestParam String reporteds,
                              @Parameter(name = "removeReporteds", description = "removeReporteds 上传附件ids", required = false) @RequestParam String removeReporteds,
                              @Parameter(name = "assId", description = "assId 评估标准id", required = true) @RequestParam(required = false) BigDecimal assId,
                              @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false)BigDecimal secrectLevelId,
                              @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false)String staffScopeNames,
                              @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false)String staffScopeIds,
                              @Parameter(name = "id", description = "评估计划id，修改计划必须提供，新增计划省略", required = false) @RequestParam(required = false) String id,
                             // @Parameter(name = "content", description = "富文本框", required = false) @RequestParam String content,
                              @ModelAttribute fieldActivationVo vo) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        //查询评估标准
        RiskAssessmentstd assessMentsTd = iRiskAssessmentstdService.getById(assId);
        log.info(assessMentsTd.toString());

        RiskAssplan tblRiskAssplan = new RiskAssplan();
        RiskAssplanOrg riskAssplanOrg = new RiskAssplanOrg();

        //时间验证    比较两个时间，当结束时间大于开始时间时，返回1；
        if (DateUtils.compare_date(starttime_max, starttime_min) == 1) {
            if (null != assessMentsTd) {
                if (StringUtils.isNotEmpty(id)) {
                    //查询评估计划
                    tblRiskAssplan = iriskAssplanService.getById(id);

                    tblRiskAssplan.setPlanName(planName);
                    tblRiskAssplan.setUnit(unit);
                    tblRiskAssplan.setPlancode(plancode);
                    tblRiskAssplan.setRecorddate(LocalDateTime.now());
                    tblRiskAssplan.setEndDate(DateUtil.formatDate(starttime_max, "yyyy-MM-dd"));
                    tblRiskAssplan.setStartDate(DateUtil.formatDate(starttime_min, "yyyy-MM-dd"));
                    tblRiskAssplan.setPlanStatus(RiskAssplan.PLANSTATUS_WKS);
                    tblRiskAssplan.setPlandes(planDes);
                    //修改评估标准  assTdId
                    tblRiskAssplan.setAssessmentstd(assessMentsTd);
                    tblRiskAssplan.setPlanType(planType);
                    tblRiskAssplan.setContent(vo.getContent());
                    tblRiskAssplan.setSecrectLevelId(secrectLevelId);
                    tblRiskAssplan.setStaffScopeNames(staffScopeNames);
                    tblRiskAssplan.setStaffScopeIds(staffScopeIds);
                    if(vo!=null){
                    	tblRiskAssplan.setFieldActivationCopy(vo);
                    }
                    //修改计划信息
                    UpdateWrapper<RiskAssplan> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("ASSPLANID", id);

                    iriskAssplanService.update(tblRiskAssplan, updateWrapper);
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("ASSPLANID", tblRiskAssplan.getAssplanid());
                    riskAssplanAttService.remove(queryWrapper);
                    if (StringUtils.isNotEmpty(removeReporteds)){
                        attachmentService.removeByIds(Arrays.asList(removeReporteds.split(",")));
                    }
                    if (StringUtils.isNotEmpty(reporteds)){
                        String[] reportedSplit = reporteds.split(",");
                        for (String reported:reportedSplit){
                            RiskAssplanAtt riskAssplanAtt = new RiskAssplanAtt(tblRiskAssplan.getAssplanid(), new BigDecimal(reported));
                            riskAssplanAttService.save(riskAssplanAtt);
                        }
                    }
                    //返回操作成功信息
                    return ResponseFormat.retParam(1, 200, null);

                } else {

                    //查询同个公司下风险评估计划的编号重复情况
                    Integer selectRiskAssplanNumber = iriskAssplanService.selectRiskAssplanNumber(plancode, selectOrg.getOrgid().toString());
                    if (selectRiskAssplanNumber != 0) {
                        //评估计划的编号重复，返回的失败信息
                        return ResponseFormat.retParam(0, "评估计划的编号重复", null);
                    }

                    tblRiskAssplan.setPlanName(planName);
                    tblRiskAssplan.setPlancode(plancode);
                    tblRiskAssplan.setRecorddate(LocalDateTime.now());
                    tblRiskAssplan.setUnit(unit);
                    tblRiskAssplan.setCreatestaffid(staffUtil.getStaffid());
                    tblRiskAssplan.setLinkdeptid(staffUtil.getLinkDetp().getOrgid());
                    tblRiskAssplan.setEndDate(DateUtil.formatDate(starttime_max, "yyyy-MM-dd"));
                    tblRiskAssplan.setStartDate(DateUtil.formatDate(starttime_min, "yyyy-MM-dd"));
                    tblRiskAssplan.setPlanStatus(RiskAssplan.PLANSTATUS_WKS);
                    tblRiskAssplan.setPlandes(planDes);
                    tblRiskAssplan.setAssessmentstd(assessMentsTd);
                    tblRiskAssplan.setPlanType(planType);
                    tblRiskAssplan.setRecorder(staffUtil.getRealname());
                    tblRiskAssplan.setCreatetime(new Date());
                    tblRiskAssplan.setSecrectLevelId(secrectLevelId);
                    tblRiskAssplan.setStaffScopeNames(staffScopeNames);
                    tblRiskAssplan.setStaffScopeIds(staffScopeIds);
                    //当前用户选择的组织
                    tblRiskAssplan.setOrganization(iOrganizationService.getById(selectOrg.getOrgid()));
                    tblRiskAssplan.setContent(vo.getContent());
                    if(vo!=null){
                    	tblRiskAssplan.setFieldActivationCopy(vo);
                    }
                    Serializable serializable = iriskAssplanService.save(tblRiskAssplan);


                    //向riskAssplanOrg中间表添加数据 评估编号与当前用户选择的组织的中间表
                    riskAssplanOrg.setAssplanid(tblRiskAssplan.getAssplanid());
                    riskAssplanOrg.setOrgid(selectOrg.getOrgid());
                    iRiskAssplanOrgService.save(riskAssplanOrg);
                    if (StringUtils.isNotEmpty(removeReporteds)){
                        attachmentService.removeByIds(Arrays.asList(removeReporteds.split(",")));
                    }
                    if (StringUtils.isNotEmpty(reporteds)){
                        String[] reportedSplit = reporteds.split(",");
                        for (String reported:reportedSplit){
                            RiskAssplanAtt riskAssplanAtt = new RiskAssplanAtt(tblRiskAssplan.getAssplanid(), new BigDecimal(reported));
                            riskAssplanAttService.save(riskAssplanAtt);
                        }
                    }


                    //操作成功返回相应信息 setCode("0")和serializable;
                    JsonBean jsonBean = null;
                    Map<String, Object> result = new HashMap<String, Object>(0);
                    result.put("assplanid", tblRiskAssplan.getAssplanid());
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


    /**
     * 新建-选择风险
     *
     * @param choiceSearch
     * @return JsonBean
     * @auther guanhongyi
     * @date 2022/8/13
     * @version 1.0.1
     */
    @OperationLog(
            success = "新增/修改评估计划【{{#planName}}】成功",
            busType = "风险评估",
            fail = "新增/修改评估计划【{{#planName}}】失败",
            operationType = OperationType.ADD,
            subType = "评估计划"
    )
    @Operation(summary = "评估计划新增、修改选择风险 /pggl/risktype vue 弃用")
    @RequestMapping(value = "/risktype", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean riskType(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }

        Map result = new HashMap();
        result.put("ViewName", "fxgl/fxpg/plan/risk_type");

        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(200);
        jsonBean.setData(result);

        return jsonBean;
    }


    /**
     * 选择风险页面-左侧风险数据树查询
     *
     * @param orgid
     * @param token
     * @param treeName
     * @return JsonBean
     * @throws Exception
     * @auther zuoshun
     * @date 2022/8/13
     * @version 1.0.2
     */
    @OperationLog(
            success = "评估计划新增、修改-选择左侧风险数据树查询处理成功",
            busType = "风险管控",
            fail = "评估计划新增、修改-选择左侧风险数据树查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @Operation(summary = "评估计划新增、修改-选择左侧风险数据树查询 /pggl/risk_left")
    @RequestMapping(value = "/risk_left", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean riskQueryLeft(
            @Parameter(name = "orgid", description = "公司主键", required = false) @RequestParam(value = "orgid", required = false) String orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "treeName", description = "公司名称", required = false) @RequestParam(value = "treeName", required = false) String treeName) throws Exception {

//        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
//        if (staffUtil == null) {
//            return new JsonBean(0, "用户已失效", null);
//        }
//
//        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
//
//        log.info("进入接口");
//        log.info("{}", orgid);
//        log.info("{}", selectOrg);
//        log.info("{}", treeName);


        return riskEventService.riskQueryLeft(orgid, token, treeName);
    }


    /**
     * 评估计划-新建-选择评估的风险信息
     * 可根据风险编号和风险名称查询记录
     *
     * @param
     * @param riskcatid
     * @param orgid
     * @param risk
     * @return JsonBean
     * @auther guanhongyi
     * @date 2022/8/12
     * @version 1.0.1
     */
    @OperationLog(
            success = "评估计划新增、修改选择风险-查询列表查询处理成功",
            busType = "风险管控",
            fail = "评估计划新增、修改选择风险-查询列表查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @Operation(summary = "评估计划新增、修改选择风险-查询列表 /pggl/risk_analysis_list")
    @RequestMapping(value = "/risk_analysis_list", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean risk_analysis_list(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "riskcatid", description = "风险类型主键Id", required = false) @RequestParam(value = "riskcatid", required = false) String riskcatid,
                                       @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
                                       @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
                                       @Parameter(name = "risknumber", description = "查询条件 -风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
                                       @Parameter(name = "riskname", description = "查询条件 -风险名称", required = false) @RequestParam(value = "riskname", required = false) String riskname) throws Exception {


        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        Risk risk = new Risk();
        risk.setRisknumber(risknumber);
        risk.setRiskname(riskname);

        //查询风险类型表记录 拿到 RISKCATNAME='企业风险'的记录
        if (StringUtils.isEmpty(riskcatid)) {
            Riskcategory cat = riskCategoryService.findQYFXByOrgid(selectOrg.getOrgid().toString(), Riskcategory.FXSJK);
            if (cat != null) {
                riskcatid = cat.getRiskcatid().toString();
            }
        }

        //查询所有风险子节点id
        List<BigDecimal> riskcatidAll = riskCategoryService.findRiskcatidsByChildNode1(riskcatid);

        //分页查询获取risk和organization表数据
        PageInfo<Risk> pageBean = riskService.findRiskByPGRisks(risk, 1,pageNo, pageSize,riskcatidAll,staffUtil);

        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("riskcatid", riskcatid);
        result.put("pageBean", pageBean);

        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }


    /**
     * 设置权重列表
     *
     * @param assriskid
     * @param choiceSearch
     * @return JsonBean
     * @auther guanhongyi
     * @date 2022/8/12
     * @version 1.0.1
     */
    @OperationLog(
            success = "查询设置权重列表成功",
            busType = "风险评估",
            fail = "查询设置权重列表失败",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @Operation(summary = "评估计划新增、修改-设置权重列表  /pggl/estimate_user")
    @RequestMapping(value = "/estimate_user", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean estimateUser(@Parameter(name = "assriskid", description = "评估风险信息主键", required = true) @RequestParam(value = "assriskid", required = true) BigDecimal assriskid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //查询中间表数据
        List<RiskRiskmarking> list = riskRiskmarkingService.findByRiskId(assriskid);

        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("list", list);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }


    /**
     * 保存修改权重
     *
     * @param ids
     * @param values
     * @return JsonBean
     * @auther guanhongyi
     * @date 2022/8/15
     * @version 1.0.1
     */
    @OperationLog(
            success = "修改设置权重成功",
            busType = "风险评估",
            fail = "修改设置权重失败",
            operationType = OperationType.ADD,
            subType = "评估计划"
    )
    @Operation(summary = "修改权重-保存  /pggl/save_estimate_user")
    @RequestMapping(value = "/save_estimate_user", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public @ResponseBody
    JsonBean saveEstimateUser(
            @Parameter(name = "ids", description = "评价项目 任务管理主键，多个用,分割", required = true) @RequestParam(value = "ids", required = true) String ids,
            @Parameter(name = "values", description = "权重比例，多个用,分割  权重比例只能输入数字，权重比例相加之和必须等于100", required = true) @RequestParam(value = "values", required = true) String values,
            @Parameter(name = "token", description = "登录用户token", required = true)
            @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        log.info("{}", ids);
        log.info("{}", values);
        String[] papmIds = ids.split(",");
        String[] papmValues = values.split(",");
        for (int i = 0; i < papmIds.length; i++) {
            BigDecimal bigDecimal = new BigDecimal(papmIds[i]);

            QueryWrapper<RiskRiskmarking> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("markingId", bigDecimal);
            List<RiskRiskmarking> list = riskRiskmarkingService.list(queryWrapper);
            log.info("{}", list);

            if (list.size() > 0) {
                RiskRiskmarking marking = list.get(0);
                marking.setAssweight(Double.valueOf(papmValues[i]));
                UpdateWrapper<RiskRiskmarking> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("MARKINGID", marking.getMarkingid());
                //修改RiskRiskmarking表权重
                riskRiskmarkingService.update(marking, updateWrapper);
            } else {
                return ResponseFormat.retParam(1, "评估任务中对应记录至少有一条不存在", null);
            }
        }
        return ResponseFormat.retParam(1, 200, null);

    }

    /**
     * 保存文件记录
     *
     * @param assplanid
     * @param tblAttachment
     * @param choiceSearch
     * @return JsonBean
     * @throws FileNotFoundException
     * @throws IOException
     * @auther guanhongyi
     * @date 2022/8/15
     * @version 1.0.2
     */
    @OperationLog(
            success = "保存文件处理成功",
            busType = "风险管控",
            fail = "保存文件处理失败",
            operationType = OperationType.ADD,
            subType = "风险评估"
    )
    @Operation(summary = "保存文件 ,   /pggl/saveFile")
    @RequestMapping(value = "/saveFile", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean saveFile(
            @Parameter(name = "assplanid", description = "assplanid 评估计划id", required = true) @RequestParam(required = true, value = "assplanid") BigDecimal assplanid,
            @Parameter(name = "attid", description = "attid 附件Id", required = true) @RequestParam(required = true, value = "attid") BigDecimal attid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        log.info("{}", assplanid);
        log.info("{}", attid);

        RiskAssplan assplan = iriskAssplanService.getById(assplanid);
        if (null != assplan) {
            //向中间表插入记录
            RiskAssplanAtt riskAssplanAtt = new RiskAssplanAtt();
            riskAssplanAtt.setAttid(attid);
            riskAssplanAtt.setAssplanid(assplanid);
            log.info("{}", riskAssplanAtt);
            iRiskAssplanAttService.save(riskAssplanAtt);
        }

        return ResponseFormat.retParam(1, 200, null);
    }


    /**
     * 删除上传的文件记录
     *
     * @param id
     * @return
     * @auther guanhongyi
     * @date 2022/8/19
     * @version 1.0.2
     */
    @OperationLog(
            success = "删除文件处理成功",
            busType = "风险管控",
            fail = "删除文件处理失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @Operation(summary = "删除文件  /pggl/deleteFile")
    @RequestMapping(value = "/deleteFile", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public @ResponseBody
    JsonBean deleteFile(
            @Parameter(name = "id", description = "id 附件Id", required = true) @RequestParam(required = true, value = "id") BigDecimal id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Attachment attachment = iAttachmentService.getById(id);

        if (null != attachment) {
            //删除中间表数据
            iRiskAssplanAttService.removeById(id);
            //删除附件表记录
            iAttachmentService.removeById(attachment);
            return ResponseFormat.retParam(1, 200, null);
        } else {
            return ResponseFormat.retParam(1, "操作失败", null);
        }
    }


    /**
     * 评估计划-修改  (Ajax)
     * updateAjax
     * 判断计划是否存在
     *
     * @param id
     * @return JsonBean
     * @auther guanhongyi
     * @date 2022/8/13
     * @version 1.0.1
     */
    @OperationLog(
            success = "修改计划处理成功",
            busType = "风险管控",
            fail = "修改计划处理失败",
            operationType = OperationType.UPDATE,
            subType = "风险评估"
    )
    @Operation(summary = "修改计划 /pggl/updateAjax")
    @RequestMapping(value = "/updateAjax", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean updateAjax(
            @Parameter(name = "id", description = "id 评估计划id", required = true) @RequestParam(required = true, value = "id") BigDecimal id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //查询计划是否存在
        RiskAssplan tblRiskAssplan = iriskAssplanService.getById(id);
        if (null != tblRiskAssplan && tblRiskAssplan.getPlanStatus().equals(RiskAssplan.PLANSTATUS_WKS)) {
            return ResponseFormat.retParam(1, "评估计划存在", null);
        } else {
            return ResponseFormat.retParam(1, "评估计划不存在！", null);
        }

    }


    /**
     * 评估计划-修改
     *
     * @param id
     * @param choiceSearch
     * @param token
     * @return JsonBean
     * @throws Exception
     * @auther guanhongyi
     * @date 2022/8/13
     * @version 1.0.1
     */
    @OperationLog(
            success = "评估计划修改 获取详情处理成功",
            busType = "风险管控",
            fail = "评估计划修改 获取详情处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @Operation(summary = "评估计划修改 获取详情 /pggl/riplanaddupdatepage")
    @RequestMapping(value = "/riplanaddupdatepage", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean riplanUpaatePage(@Parameter(name = "id", description = "id 评估计划id", required = true) @RequestParam(required = true, value = "id") BigDecimal id,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {


        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        RiskAssplan tblRiskAssplan = iriskAssplanService.getById(id);
        Map<String, Object> result = new HashMap<String, Object>(0);
        //RiskAssplan不能为空，且评估计划状态为 未开始
        JsonBean jsonBean = null;
        if (null != tblRiskAssplan && tblRiskAssplan.getPlanStatus().equals(RiskAssplan.PLANSTATUS_WKS)) {

            //查询RiskAssplanRisk中间表  返回评估的风险信息id
            List<RiskAssplanRisk> listRiskAssPlanRisk = iRiskAssplanRiskService.findRiskByRisk(id);

            result.put("orgByUser", tblRiskAssplan.getOrganization());
            result.put("Staff", staffUtil);
            result.put("RiskAssplan", tblRiskAssplan);
            result.put("listriskAssPlanRisk", listRiskAssPlanRisk);
            jsonBean = ResponseFormat.retParam(1, 200, result);
            return jsonBean;
        } else {
            return ResponseFormat.retParam(1, "操作失败", null);
        }

    }


    /**
     * 评估计划审批验证
     *
     * @param id
     * @return JsonBean
     * @auther guanhongyi
     * @date 2022/8/19
     * @version 1.0.2
     */
    @OperationLog(
            success = "审批验证信息处理成功",
            busType = "风险管控",
            fail = "审批验证信息处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @Operation(summary = "审批验证信息 /pggl/pong")
    @RequestMapping(value = "/pgjhspyz", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean pong(@Parameter(name = "id", description = "id 评估计划id", required = true) @RequestParam(required = true, value = "id") String id,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        log.info("{}", id);
        //查询计划
        if (StringUtils.isBlank(id)) {
            return new JsonBean(0, "请提供ASSPLANID评估计划Id，后重试", null);
        }
        RiskAssplan tblRiskAssplan = iriskAssplanService.getById(id);


        List<Task> list = new ArrayList<Task>();

        //评估计划不为空，且评估计划为未开始
        if (null != tblRiskAssplan && tblRiskAssplan.getPlanStatus().equals(RiskAssplan.PLANSTATUS_WKS)) {

            //查询RiskAssplanRisk中间表  返回评估的风险信息id
            List<RiskAssplanRisk> assPlanRisks = iRiskAssplanRiskService.findRiskByRisk(new BigDecimal(id));

            //需要评估的风险不能小于等于0
            if (assPlanRisks.size() > 0) {

                for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {

                    //获得RiskRiskmarking记录
                    List<RiskRiskmarking> markings = riskRiskmarkingService.findByRiskId(riskAssPlanRisk.getAssriskid());
                    //评估人、权重等风险标记信息不能为空
                    if (markings.size() > 0) {

                        double ewight = 0d;

                        for (RiskRiskmarking riskMarking : markings) {
                            Task task = new Task();
                            task.setStaffid(riskMarking.getStaff().getStaffid());
                            task.setStatus(Task.START);
                            task.setTasktime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                            task.setTaskcode(tblRiskAssplan.getPlancode());
                            task.setTaskname(tblRiskAssplan.getPlanName());
                            task.setUrl("/fxxt/fxpg/plan/riresultlist");
                            list.add(task);
                            if (null != riskMarking.getAssweight()) {
                                ewight = ewight + riskMarking.getAssweight();
                            }

                        }

                        if (ewight != 100d) {
                            return ResponseFormat.retParam(1, "权重比例设置不正确", null);
                        }

                    } else {
                        return ResponseFormat.retParam(1, "评估人、权重等风险标记信息不能为空", null);
                    }

                }

            } else {
                return ResponseFormat.retParam(1, "需要评估的风险不能小于等于0", null);

            }
        }
        return null;
    }


    /**
     * 下发评估计划
     *
     * @param id
     * @return JsonBean
     * @auther guanhongyi
     * @date 2022/8/19
     * @version 1.0.2
     */
    @OperationLog(
            success = "下发成功",
            busType = "风险评估",
            fail = "下发失败",
            operationType = OperationType.DISPATCH,
            subType = "评估计划"
    )
    @Operation(summary = "下发 /pggl/down")
    @RequestMapping(value = "/down", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean down(@Parameter(name = "id", description = "id 评估计划id", required = true) @RequestParam(required = true, value = "id") String id,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        log.info("{}", id);
        //查询计划
        if (StringUtils.isBlank(id)) {
            return new JsonBean(0, "请提供ASSPLANID评估计划Id，后重试", null);
        }
        RiskAssplan tblRiskAssplan = iriskAssplanService.getById(id);


        List<Task> list = new ArrayList<Task>();

        //评估计划不为空，且评估计划为未开始
        if (null != tblRiskAssplan && tblRiskAssplan.getPlanStatus().equals(RiskAssplan.PLANSTATUS_WKS)) {

            //查询RiskAssplanRisk中间表  返回评估的风险信息id
            List<RiskAssplanRisk> assPlanRisks = iRiskAssplanRiskService.findRiskByRisk(new BigDecimal(id));

            //需要评估的风险不能小于等于0
            if (assPlanRisks.size() > 0) {

                for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {

                    //获得RiskRiskmarking记录
                    List<RiskRiskmarking> markings = riskRiskmarkingService.findByRiskId(riskAssPlanRisk.getAssriskid());
                    //评估人、权重等风险标记信息不能为空
                    if (markings.size() > 0) {

                        double ewight = 0d;

                        for (RiskRiskmarking riskMarking : markings) {
                            Task task = new Task();
                            task.setStaffid(riskMarking.getStaff().getStaffid());
                            task.setStatus(Task.START);
                            task.setTasktime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                            task.setTaskcode(tblRiskAssplan.getPlancode());
                            task.setTaskname(tblRiskAssplan.getPlanName());
                            task.setUrl("/fxxt/fxpg/plan/riresultlist");
                            list.add(task);
                            if (null != riskMarking.getAssweight()) {
                                ewight = ewight + riskMarking.getAssweight();
                            }

                        }

                        if (ewight != 100d) {
                            return ResponseFormat.retParam(1, "权重比例设置不正确", null);
                        }

                    } else {
                        return ResponseFormat.retParam(1, "评估人、权重等风险标记信息不能为空", null);
                    }

                }

            } else {
                return ResponseFormat.retParam(1, "需要评估的风险不能小于等于0", null);

            }


            //将评估计划的状态改为：评估中
            tblRiskAssplan.setPlanStatus(RiskAssplan.PLANSTATUS_PGZ);

            //修改评估计划 updateWrapper 不能为null
            UpdateWrapper<RiskAssplan> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("assplanid", id);
            iriskAssplanService.update(tblRiskAssplan, updateWrapper);

            //向TBL_TASK添加数据
            iTaskService.addList(list);

            return ResponseFormat.retParam(1, 200, null);
        } else {
            return ResponseFormat.retParam(1, "评估计划为空或计划不是未开始状态", null);
            //计划不存在
        }

    }


    /**
     * 评估计划 - 删除
     *
     * @param id
     * @return
     */
    @OperationLog(
            success = "删除【{{#id}}】成功",
            busType = "风险评估",
            fail = "删除【{{#id}}】失败",
            operationType = OperationType.DISPATCH,
            subType = "评估计划"
    )
    @Operation(summary = "删除  /pggl/riplandel")
    @RequestMapping(value = "/riplandel", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public @ResponseBody
    JsonBean riplanDel(@Parameter(name = "id", description = "id 评估计划id", required = true) @RequestParam(required = true, value = "id") String id,
                       @Parameter(name = "token", description = "登录用户token", required = true)
                       @RequestHeader("token") String token) throws Exception {


        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        log.info("{}", id);
        //查询RiskAssplan表记录 id是Assplanid
        RiskAssplan assplan = iriskAssplanService.getById(id);

        //assplan不为空且评估计划状态为：未开始时才能删除评估计划 || 非集团下发的数据才能被删除
        if ((null != assplan &&!assplan.getPlanStatus().equals(RiskAssplan.PLANSTATUS_WKS))||(null != assplan &&assplan.getIsgroup()!=null&& assplan.getIsgroup().compareTo(new BigDecimal("1"))==0)) {
            return ResponseFormat.retParam(1, "计划不是未开始状态或是集团下发数据，不能进行删除!", null);
        }else{
            //根据Assplanid查询TBL_RISK_ASSPLAN_RISK中间表，
            List<RiskAssplanRisk> assPlanRisks = iRiskAssplanRiskService.findRiskByRisk(assplan.getAssplanid());
            for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {

                //删除TBL_RISK_ASSPLAN_RISK中间表记录
                iRiskAssplanRiskService.removeById(riskAssPlanRisk);

                Set<RiskRiskmarking> riskMarkings = riskAssPlanRisk.getTblRiskRiskMarking();
                for (RiskRiskmarking riskmarking : riskMarkings) {

                    //删除RiskRiskmarking表数据
                    riskRiskmarkingService.removeById(riskmarking);
                }

            }


            QueryWrapper<RiskAssplanAtt> queryWrapperA = new QueryWrapper<>();
//            queryWrapperA.eq("assplanid", assplan.getAssplanid());
            queryWrapperA.eq("assplanid", id);
            List<RiskAssplanAtt> riskAssplanAtts = iRiskAssplanAttService.list(queryWrapperA);
            for (RiskAssplanAtt riskAssplanAtt : riskAssplanAtts) {

                //删除TBL_RiskAssplanAtt表记录
                QueryWrapper<RiskAssplanAtt> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("assplanid", assplan.getAssplanid());
//                iRiskAssplanAttService.removeById(riskAssplanAtt);
                iRiskAssplanAttService.remove(queryWrapper2);

                //删除附件表记录
                iAttachmentService.removeById(riskAssplanAtt.getAttid());

            }


            //删除RiskAssplanOrg中间表数据
            QueryWrapper<RiskAssplanOrg> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("assplanid", assplan.getAssplanid());
            iRiskAssplanOrgService.remove(queryWrapper1);


            //删除评估计划
            iriskAssplanService.removeById(assplan);

            //操作成功，返回数据
            return ResponseFormat.retParam(1, 200, null);

        }
    }


    /**
     * 缺陷管理==
     */
    @OperationLog(
            success = "缺陷管理列表处理成功",
            busType = "风险管控",
            fail = "缺陷管理列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/defect_list")
    @Operation(summary = "缺陷管理列表")
    public JsonBean workReportList(HttpServletRequest request, TblFxgkBugVo tblFxgkBugVo,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                   @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                   @Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) Integer orgid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblFxgkBugService.bugPageList(token, pageNumber, pageSize, tblFxgkBugVo, orgid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-新增与修改处理成功",
            busType = "风险管控",
            fail = "缺陷管理-新增与修改处理失败",
            operationType = OperationType.ADD,
            subType = "风险评估"
    )
    @RequestMapping(value = "/qxgl/defect_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷管理-新增与修改")
    public JsonBean defect_add(HttpServletRequest request, @Parameter(name = "bug", description = "实体", required = true) TblNbsjBugEntity bug,
                               @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                               @Parameter(name = "attids", description = "附件id数组", required = false) String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.bugAdd(bug, token, attids);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-新增与修改-内规选择列表处理成功",
            busType = "风险管控",
            fail = "缺陷管理-新增与修改-内规选择列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @RequestMapping(value = "/qxgl/inner_common_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷管理-新增与修改-内规选择列表")
    public JsonBean inner_common_qxwt(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                      @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.innerCommonQxwtList(token, pageNumber, pageSize, bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-新增与修改-外规选择列表处理成功",
            busType = "风险管控",
            fail = "缺陷管理-新增与修改-外规选择列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @RequestMapping(value = "/qxgl/outer_common_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷管理-新增与修改-外规选择列表")
    public JsonBean outer_common_qxwt(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                      @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.outerCommonQxwtList(token, pageNumber, pageSize, bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-新增与修改-选择内规保存处理成功",
            busType = "风险管控",
            fail = "缺陷管理-新增与修改-选择内规保存处理失败",
            operationType = OperationType.ADD,
            subType = "风险评估"
    )
    @RequestMapping(value = "/qxgl/add_inner_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷管理-新增与修改-选择内规保存")
    public JsonBean add_inner_qxwt(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                   @Parameter(name = "bugid", description = "bugid", required = false) Integer bugid,
                                   @Parameter(name = "innrulids", description = "内规ids", required = false) String innrulids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.bugInnrulidsAdd(token, bugid, innrulids);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-新增与修改-选择外规保存处理成功",
            busType = "风险管控",
            fail = "缺陷管理-新增与修改-选择外规保存处理失败",
            operationType = OperationType.ADD,
            subType = "风险评估"
    )
    @RequestMapping(value = "/qxgl/add_outer_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷管理-新增与修改-选择外规保存")
    public JsonBean add_outer_qxwt(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                   @Parameter(name = "bugid", description = "bugid", required = false) Integer bugid,
                                   @Parameter(name = "outrulids", description = "外规ids", required = false) String outrulids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.bugOutrulidsAdd(token, bugid, outrulids);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-关联内规查询处理成功",
            busType = "风险管控",
            fail = "缺陷管理-关联内规查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/inner_common_link")
    @Operation(summary = "缺陷管理-关联内规查询")
    public JsonBean inner_common_link(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.innerCommonLinkList(token, 1, 99, bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-关联外规查询处理成功",
            busType = "风险管控",
            fail = "缺陷管理-关联外规查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/outer_common_link")
    @Operation(summary = "缺陷管理-关联外规查询")
    public JsonBean outer_common_link(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.outerCommonLinkList(token, 1, 99, bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-关联内规-删除处理成功",
            busType = "风险管控",
            fail = "缺陷管理-关联内规-删除失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/delete_qx_inner")
    @Operation(summary = "缺陷管理-关联内规-删除")
    public JsonBean delete_qx_inner(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "bugid", description = "主键", required = true) @RequestParam(value = "bugid", required = true) Integer bugid,
                                    @Parameter(name = "innrulids", description = "内规ids", required = false) String innrulids) {

        try {
            return this.tblNbsjBugService.bugInnrulidsDelete(token, bugid, innrulids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @OperationLog(
            success = "缺陷管理-关联外规-删除处理成功",
            busType = "风险管控",
            fail = "缺陷管理-关联外规-删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/delete_qx_outer")
    @Operation(summary = "缺陷管理-关联外规-删除")
    public JsonBean delete_qx_outer(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "bugid", description = "主键", required = true) @RequestParam(value = "bugid", required = true) Integer bugid,
                                    @Parameter(name = "outrulids", description = "外规ids", required = false) String outrulids) {

        try {
            return this.tblNbsjBugService.bugOutrulidsDelete(token, bugid, outrulids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @OperationLog(
            success = "关联缺陷-选择保存处理成功",
            busType = "风险管控",
            fail = "关联缺陷-选择保存处理失败",
            operationType = OperationType.ADD,
            subType = "风险评估"
    )
    @RequestMapping(value = "/qxgl/add_defect_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "关联缺陷-选择保存")
    public JsonBean add_defect_qxwt(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                    @Parameter(name = "bugid", description = "bugid", required = false) Integer bugid,
                                    @Parameter(name = "bugids", description = "关联bugids", required = false) String bugids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.defectQxwtAdd(token, bugid, bugids);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-关联缺陷查询处理成功",
            busType = "风险管控",
            fail = "缺陷管理-关联缺陷查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/defect_link")
    @Operation(summary = "缺陷管理-关联缺陷查询")
    public JsonBean defect_link(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.defectLinkList(token, 1, 99, bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-关联缺陷删除处理成功",
            busType = "风险管控",
            fail = "缺陷管理-关联缺陷删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/delete_qx_child")
    @Operation(summary = "缺陷管理-关联缺陷删除")
    public JsonBean delete_qx_child(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "bugid", description = "主键", required = true) @RequestParam(value = "bugid", required = true) Integer bugid,
                                    @Parameter(name = "bugids", description = "关联bugids", required = false) String bugids) {

        try {
            return this.tblNbsjBugService.bugFatherDelete(token, bugid, bugids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @OperationLog(
            success = "缺陷管理-删除处理成功",
            busType = "风险管控",
            fail = "缺陷管理-删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/defect_del")
    @Operation(summary = "缺陷管理-删除")
    public JsonBean workReportDelete(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "bugid", description = "主键", required = true) @RequestParam(value = "bugid", required = true) Integer bugid) {

        try {
            return tblNbsjBugService.bugDelete(bugid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @OperationLog(
            success = "缺陷管理-明细处理成功",
            busType = "风险管控",
            fail = "缺陷管理-明细处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/defect_detail")
    @Operation(summary = "缺陷管理-明细")
    public JsonBean workReportDetail(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "bugid", description = "主键", required = true) @RequestParam(value = "bugid", required = true) Integer bugid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.findBugDetail(token, bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-附件列表处理成功",
            busType = "风险管控",
            fail = "缺陷管理-附件列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/defect_file_list")
    @Operation(summary = "缺陷管理-附件列表")
    public JsonBean defect_file_list(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "bugId", description = "业务主键", required = true) @RequestParam(value = "bugId", required = true) Integer bugId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.defectFileList(token, bugId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-附件删除处理成功",
            busType = "风险管控",
            fail = "缺陷管理-附件删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/defect_file_del")
    @Operation(summary = "缺陷管理-附件删除")
    public void defect_file_del(HttpServletRequest request, HttpServletResponse response,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        this.tblNbsjBugService.removeAttInfoByAttId(token, attId);
    }

    @OperationLog(
            success = "缺陷管理-导出处理成功",
            busType = "风险管控",
            fail = "缺陷管理-导出处理失败",
            operationType = OperationType.EXPORT,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/defect_file_export")
    @Operation(summary = "缺陷管理-导出")
    public JsonBean defect_file_export(HttpServletRequest request, HttpServletResponse response,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "orgId", description = "组织编号", required = false) @RequestParam(value = "orgId", required = false) Integer orgId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.defect_file_export(token, orgId, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷管理-明细处理成功",
            busType = "风险管控",
            fail = "缺陷管理-导出明细处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/qxgl/bug_criterion_list")
    @Operation(summary = "缺陷管理-明细")
    public JsonBean bug_criterion_list(HttpServletRequest request,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.findBugCriterion(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 风险发现==
     */
    @OperationLog(
            success = "风险发现列表处理成功",
            busType = "风险管控",
            fail = "风险发现列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/fxfx/risk_list")
    @Operation(summary = "风险发现列表")
    public JsonBean risk_list(HttpServletRequest request, TblNbkzRiskVo tblNbkzRiskVo,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                              @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                              @Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) Integer orgid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.riskPageList(token, pageNumber, pageSize, tblNbkzRiskVo, orgid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "风险发现-新增与修改处理成功",
            busType = "风险管控",
            fail = "风险发现-新增与修改处理失败",
            operationType = OperationType.ADD,
            subType = "风险评估"
    )
    @RequestMapping(value = "/fxfx/risk_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "风险发现-新增与修改")
    public JsonBean risk_add(HttpServletRequest request, @Parameter(name = "risk", description = "实体", required = true) TblNbkzRiskEntity risk,
                             @Parameter(name = "attids", description = "附件id数组", required = false) String attids,
                             @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.riskAdd(risk, token, attids);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "风险发现-删除处理成功",
            busType = "风险管控",
            fail = "风险发现-删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @GetMapping("/fxfx/risk_del")
    @Operation(summary = "风险发现-删除")
    public JsonBean risk_del(HttpServletRequest request,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "riskid", description = "主键", required = true) @RequestParam(value = "riskid", required = true) Integer riskid) {

        try {
            return this.nbkzRiskService.riskDelete(riskid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @OperationLog(
            success = "风险发现-明细处理成功",
            busType = "风险管控",
            fail = "风险发现-明细处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/fxfx/risk_detail")
    @Operation(summary = "风险发现-明细")
    public JsonBean risk_detail(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "riskid", description = "主键", required = true) @RequestParam(value = "riskid", required = true) Integer riskid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.findRiskDetail(token, riskid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "风险发现-附件列表处理成功",
            busType = "风险管控",
            fail = "风险发现-附件列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/fxfx/risk_file_list")
    @Operation(summary = "风险发现-附件列表")
    public JsonBean risk_file_list(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "riskId", description = "业务主键", required = true) @RequestParam(value = "riskId", required = true) Integer riskId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.risk_file_list(token, riskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "风险发现-附件删除处理成功",
            busType = "风险管控",
            fail = "风险发现-附件删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @GetMapping("/fxfx/risk_file_del")
    @Operation(summary = "风险发现-附件删除")
    public void risk_file_del(HttpServletRequest request, HttpServletResponse response,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        this.nbkzRiskService.removeAttInfoByAttId(token, attId);
    }

    @OperationLog(
            success = "风险发现-风险容忍度保存处理成功",
            busType = "风险管控",
            fail = "风险发现-风险容忍度保存处理失败",
            operationType = OperationType.ADD,
            subType = "风险评估"
    )
    @RequestMapping(value = "/fxfx/fxrrd_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "风险发现-风险容忍度保存")
    public JsonBean fxrrd_save(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                               @Parameter(name="tnr",description="",required=false) TblNbsjRisktolerability tnr) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.fxrrdAdd(token, tnr);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "风险发现-风险容忍度删除处理成功",
            busType = "风险管控",
            fail = "风险发现-风险容忍度删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险评估"
    )
    @GetMapping("/fxfx/fxrrd_del")
    @Operation(summary = "风险发现-风险容忍度删除")
    public JsonBean fxrrd_del(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "toleid", description = "主键", required = true) @RequestParam(value = "toleid", required = true) Integer toleid) {

        try {
            return nbkzRiskService.fxrrdDelete(toleid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @OperationLog(
            success = "风险发现-风险容忍度查询处理成功",
            busType = "风险管控",
            fail = "风险发现-风险容忍度查询处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @GetMapping("/fxfx/fxrrd_list")
    @Operation(summary = "风险发现-风险容忍度查询")
    public JsonBean fxrrd_list(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "riskid", description = "riskid", required = false) @RequestParam(value = "riskid", required = false) Integer riskid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = nbkzRiskService.fxrrdLinkList(token, 1, 99, riskid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @OperationLog(
            success = "验证评估计划的风险分配情况处理成功",
            busType = "风险管控",
            fail = "验证评估计划的风险分配情况处理失败",
            operationType = OperationType.SELECT,
            subType = "风险评估"
    )
    @Operation(summary = "验证评估计划的风险分配情况")
    @RequestMapping(value = "/check_assplan", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean check_assplan(@Parameter(name = "id", description = "id 评估计划id", required = true) @RequestParam(required = true, value = "id") String id,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        log.info("{}", id);
        //查询计划
        if (StringUtils.isBlank(id)) {
            return new JsonBean(0, "请提供ASSPLANID评估计划Id，后重试", null);
        }
        RiskAssplan tblRiskAssplan = iriskAssplanService.getById(id);

        List<Task> list = new ArrayList<Task>();

        //评估计划不为空，且评估计划为未开始
        if (null != tblRiskAssplan && tblRiskAssplan.getPlanStatus().equals(RiskAssplan.PLANSTATUS_WKS)) {

            //查询RiskAssplanRisk中间表  返回评估的风险信息id
            List<RiskAssplanRisk> assPlanRisks = iRiskAssplanRiskService.findRiskByRisk(new BigDecimal(id));

            //需要评估的风险不能小于等于0
            if (assPlanRisks.size() > 0) {

                for (RiskAssplanRisk riskAssPlanRisk : assPlanRisks) {

                    //获得RiskRiskmarking记录
                    List<RiskRiskmarking> markings = riskRiskmarkingService.findByRiskId(riskAssPlanRisk.getAssriskid());
                    //评估人、权重等风险标记信息不能为空
                    if (markings.size() > 0) {

                        double ewight = 0d;

                        for (RiskRiskmarking riskMarking : markings) {
                            Task task = new Task();
                            task.setStaffid(riskMarking.getStaff().getStaffid());
                            task.setStatus(Task.START);
                            task.setTasktime(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                            task.setTaskcode(tblRiskAssplan.getPlancode());
                            task.setTaskname(tblRiskAssplan.getPlanName());
                            task.setUrl("/fxxt/fxpg/plan/riresultlist");
                            list.add(task);
                            if (null != riskMarking.getAssweight()) {
                                ewight = ewight + riskMarking.getAssweight();
                            }
                        }
                        if (ewight != 100d) {
                            return ResponseFormat.retParam(1, "权重比例设置不正确", null);
                        }
                    } else {
                        return ResponseFormat.retParam(1, "评估人、权重等风险标记信息不能为空", null);
                    }
                }
            } else {
                return ResponseFormat.retParam(1, "需要评估的风险不能小于等于0", null);
            }
        } else {
            return ResponseFormat.retParam(1, "评估计划为空或计划不是未开始状态", null);
        }

        return ResponseFormat.retParam(1, 200, null);

    }

    
    
    @OperationLog(
            success = "查询列表",
            busType = "风险评估",
            fail = "查询列表",
            operationType = OperationType.SELECT,
            subType = "风险评估跟踪"
    )
    @RequestMapping(value = "/pggl/riplanTrackList", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "评估计划查询 /pggl/riplanTrackList")
    public JsonBean riplanTrackList(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "plancode", description = "查询条件 -计划编号", required = false) @RequestParam(value = "plancode", required = false) String plancode,
            @Parameter(name = "planName", description = "查询条件 -计划名称", required = false) @RequestParam(value = "planName", required = false) String planName,
            @Parameter(name = "startDate", description = "查询条件 -计划时间开始时间", required = false) @RequestParam(value = "startDate", required = false) String startDate,
            @Parameter(name = "endDate", description = "查询条件 -计划时间结束时间", required = false) @RequestParam(value = "endDate", required = false) String endDate,
            @Parameter(name = "planType", description = "查询条件 -计划类型 1-年度计划  2-临时性计划", required = false) @RequestParam(value = "planType", required = false) String planType,
            @Parameter(name = "planStatus", description = "查询条件 -计划状态 1-未开始 2-评估中 3-已完成", required = false) @RequestParam(value = "planStatus", required = false) String planStatus,
			@Parameter(name = "aprstatus", description = "查询条件 -审批状态", required = false) @RequestParam(value = "aprstatus", required = false) String aprstatus,
			@Parameter(name = "orgid", description = "左侧树传递公司部门树", required = false) @RequestParam(value = "orgid", required = false) BigDecimal orgid,
			@Parameter(name = "orgName", description = "查询条件 -机构名称", required = false) @RequestParam(value = "orgName", required = false) String orgName

    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        IPage page = new Page<>(pageNo, pageSize);//分页设置
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        if(orgid==null){
          orgid = selectOrg.getOrgid(); //当前用户选择的组织
        }
        /*if (selectOrg != null && selectOrg.getOrderid() != null) {
            orgid = selectOrg.getOrgid();
        }*/
        RiskAssplan riskAssplan = new RiskAssplan();
        riskAssplan.setPlancode(plancode);
        riskAssplan.setPlanName(planName);
		riskAssplan.setAprstatus(aprstatus);
        riskAssplan.setStartDate(DateUtil.formatDate(startDate, "yyyy-MM-dd"));
        riskAssplan.setEndDate(DateUtil.formatDate(endDate, "yyyy-MM-dd"));
        riskAssplan.setPlanType(planType);
        riskAssplan.setPlanStatus(planStatus);
        riskAssplan.setOrgName(orgName);
        riskAssplan.setUnit(orgid.toString());

        PageInfo<RiskAssplan> pageBean = riskAssplanService.riplanTrackList(riskAssplan, orgid,pageNo,pageSize,staffUtil);
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("pageBean", pageBean);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    @OperationLog(
            success = "查询风险评估跟踪--评估人员信息列表成功",
            busType = "风险评估",
            fail = "查询风险评估跟踪评估人员信息列表失败",
            operationType = OperationType.SELECT,
            subType = "风险评估跟踪"
    )
    @RequestMapping(value = "/plan/trackItemList",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险评估跟踪--评估人员信息列表 /plan/trackItemList")
    public JsonBean trackItemList(@Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="planId",description="评估任务主键, 必填项",required=true) @RequestParam(required = true,value="planId") String planId) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息

        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        IPage page = new Page(pageNo, pageSize);//设置分页
        //IPage pageBean = riskAssplanRiskService.findRiskByAssplanid(new BigDecimal(planId), page);//通过计划ID进入当前计划marking设置
        IPage pageBean =  riskRiskmarkingService.getMarkingList(new BigDecimal(planId),page);
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("pageBean", pageBean);
        result.put("assplanid", planId);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }
}
