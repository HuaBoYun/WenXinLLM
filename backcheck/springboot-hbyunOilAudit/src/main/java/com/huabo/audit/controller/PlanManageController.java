package com.huabo.audit.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.vo.TblNbsjAuditPlanVo;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjPlanProjectService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计计划管理控制器
 * <p>提供审计计划的列表查询、新增、修改、删除、审批等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="审计计划管理",description="审计计划管理")
@RequestMapping(value = "/auditPlan")
public class PlanManageController {
    /**
     * @description 计划管理列表
     * @author lyz
     * @date 2022/4/15 9:36
     */
    @Autowired
    public TblNbsjAuditplanService tblNbsjAuditplanService;

    @Resource
    public TblNbsjPlanProjectService tblNbsjPlanProjectService;

    @Resource
    public AttachmentService attachmentService;

    @Resource
    public TblNbsjProjectService tblNbsjProjectService;

    @GetMapping("/getAuditPlanPageList")
    @Operation(summary = "审计计划管理-计划管理列表分页功能")
    public JsonBean getAuditPlanPageList(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                         @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                         @Parameter(name = "planStartDate", description = "计划开始时间 格式年-月-日", required = false) @RequestParam(value = "planStartDate", required = false) String planStartDate,
                                         @Parameter(name = "planEndDate", description = "计划结束时间 格式年-月-日", required = false) @RequestParam(value = "planEndDate", required = false) String planEndDate,
                                         TblNbsjAuditPlanVo TblNbsjAuditPlanVo) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAuditplanService.planManagePageList(token, pageNumber, pageSize, TblNbsjAuditPlanVo, planStartDate, planEndDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * @description 计划管理详情
     * @author lyz
     * @date 2022/4/15 9:37
     */
    @GetMapping("/getAuditPlanInfo")
    @Operation(summary = "审计计划管理-根据计划主键查找计划信息")
    public JsonBean getAuditPlanInfo(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "planid", description = "审计计划主键", required = true) @RequestParam(value = "planid", required = true) Integer planid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAuditplanService.findNbsjAuditPlanDetail(token, planid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * @description 删除计划管理
     * @author lyz
     * @date 2022/4/15 10:15
     */
    @GetMapping("/deleteAuditPlanByPlanId")
    @Operation(summary = "审计计划管理-根据计划主键删除计划信息")
    public JsonBean deleteAuditPlanByPlanId(HttpServletRequest request,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @Parameter(name = "planid", description = "审计计划主键", required = true) @RequestParam(value = "planid", required = true) Integer planId) {

        try {
            return tblNbsjAuditplanService.deletePlanManageByPlanId(planId, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/mergeAuditPlanInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计计划管理-修改或保存审计计划")
    public JsonBean mergeAuditPlanInfo(HttpServletRequest request, @Parameter(name = "plan", description = "审计计划实体", required = true) TblNbsjAuditplan plan,
                                       @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                       @Parameter(name = "planStartTime", description = "计划开始时间", required = false) @RequestParam(value = "planStartTime", required = false) String planStartTime,
                                       @Parameter(name = "planEndTime", description = "计划完成时间", required = false) @RequestParam(value = "planEndTime", required = false) String planEndTime,
                                       @Parameter(name = "attIds", description = "附件主键数组 示例1,2,3,4", required = false) @RequestParam(value = "attIds", required = false) String attIds) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.mergePlanManageInfo(plan, token, planStartTime, planEndTime, attIds);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/mergePlanProjectManageInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计计划管理-审计计划中计划项目新增或修改")
    public JsonBean mergePlanProjectManageInfo(HttpServletRequest request, @Parameter(name = "project", description = "审计计划项目实体", required = true) TblNbsjPlanProject project,
                                               @Parameter(name = "plancode", description = "审计计划编码", required = false) @RequestParam(value = "plancode", required = false) String plancode,
                                               @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjPlanProjectService.mergePlanProjectManageInfo(project, project.getPlanid(), plancode, token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/mergePlanProjectManageInfoList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计计划管理-审计计划中计划项目新增或修改")
    public JsonBean mergePlanProjectManageInfoList(HttpServletRequest request,
                                                   @Parameter(name = "planId", description = "审计计划主键 ，如果主键为空则根据编码新增审计计划信息", required = false) @RequestParam(value = "planId", required = false) String planId,
                                                   @Parameter(name = "plancode", description = "审计计划编码", required = true) @RequestParam(value = "plancode", required = true) String plancode,
                                                   @Parameter(name = "projectListJson", description = "审计计划编码", required = true) @RequestParam(value = "projectListJson", required = true) String projectListJson,
                                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            List<TblNbsjPlanProject> projectList = JSONArray.parseArray(projectListJson, TblNbsjPlanProject.class);
            jsonBean = this.tblNbsjPlanProjectService.mergePlanProjectManageInfoList(projectList, planId, plancode, token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/removePlanProjectInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计计划管理-审计计划中计划项目删除")
    public JsonBean removePlanProjectInfo(HttpServletRequest request,
                                          @RequestParam(value = "planprojectid", required = true) @Parameter(name = "planprojectid", description = "审计计划项目主键", required = true) Integer planprojectid,
                                          @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjPlanProjectService.removePlanProjectInfo(token, planprojectid);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/getPlanProjectListByPlanId", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理，通过计划主键获取计划项目集合")
    public JsonBean getPlanProjectListByPlanId(HttpServletRequest request,
                                               @RequestParam(value = "planId", required = true) @Parameter(name = "planId", description = "审计计划主键", required = true) Integer planId,
                                               @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjPlanProjectService.findPlanProjectListInfoByPlanId(token, planId);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/getAuditPlanAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计计划管理，根据审计计划主键获取审计计划的附件")
    public JsonBean getAuditPlanAttInfo(HttpServletRequest request,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "planId", description = "审计计划主键 ", required = true) @RequestParam(value = "planId", required = false) Integer planId) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAuditPlanAttInfo(token, planId);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * @description 根据attid删除附件
     * @author lyz
     * @date 2022/4/19 9:04
     */
    @GetMapping("/deleteFileById")
    @Operation(summary = "审计计划管理-根据附件主键删除附件接口")
    public R deleteFileById(HttpServletRequest request, HttpServletResponse response,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.tblNbsjAuditplanService.removeAttInfoByAttId(token, attId);
    }


    @RequestMapping(value = "/getAuditPlanViewInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-审计计划查看分页列表接口")
    public JsonBean getAuditPlanViewInfo(HttpServletRequest request, HttpServletResponse response,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "planName", description = "查询条件-计划名称", required = false) @RequestParam(value = "planName", required = false) String planName,
                                         @Parameter(name = "planYear", description = "查询条件-计划年度", required = false) @RequestParam(value = "planYear", required = false) String planYear,
                                         @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                         @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAuditPlanViewInfo(token, planName, planYear, pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/getAuditPlanViewDetail", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-审计计划查看获取审计计划详情")
    public JsonBean getAuditPlanViewDetail(HttpServletRequest request,
                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                           @Parameter(name = "planid", description = "审计计划主键", required = true) @RequestParam(value = "planid", required = true) Integer planid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAuditplanService.findNbsjAuditPlanViewDetail(token, planid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @RequestMapping(value = "/getAuditFileInfoList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-审计档案列表页面")
    public JsonBean getAuditFileInfoList(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                         @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                         @Parameter(name = "projectName", description = "项目名称", required = false) @RequestParam(value = "projectName", required = false) String projectName) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjProjectService.getAuditFileInfoList(token, projectName, pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @RequestMapping(value = "/getAuditFileDetailTreeInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-审计档案详情页面获取树形菜单信息")
    public JsonBean getAuditFileDetailTreeInfo(HttpServletRequest request,
                                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                               @Parameter(name = "projectId", description = "项目主键", required = true) @RequestParam(value = "projectId", required = true) Integer projectId) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjProjectService.getAuditFileDetailTreeInfo(token, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 查询计划关联的项目
     */
    @RequestMapping(value = "/getProjectListByWspJhw", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "查询计划关联的项目")
    public JsonBean getProjectListByWspJhw(HttpServletRequest request,
                                           @RequestParam(value = "planId", required = true) @Parameter(name = "planId", description = "审计计划主键", required = true) Integer planId,
                                           @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjPlanProjectService.findProjectListInfoByWspJhw(token, planId);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 新增自动计划（计划外）
     */
    @RequestMapping(value = "/addAutoPlan", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "新增自动计划（计划外）")
    public JsonBean addAutoPlan(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.addAutoPlan(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    //自动编号-审计通知
    @RequestMapping(value = "/getAutoCodeBySjtz", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-审计通知")
    public JsonBean getAutoCodeBySjtz(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeBySjtz(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-疑点管理
    @RequestMapping(value = "/getAutoCodeByYdgl", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-疑点管理")
    public JsonBean getAutoCodeByYdgl(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByYdgl(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-我的底稿
    @RequestMapping(value = "/getAutoCodeByWddg", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-我的底稿")
    public JsonBean getAutoCodeByWddg(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByWddg(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-审计建议书
    @RequestMapping(value = "/getAutoCodeBySjjys", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-审计建议书")
    public JsonBean getAutoCodeBySjjys(HttpServletRequest request,
                                       @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeBySjjys(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-缺陷管理
    @RequestMapping(value = "/getAutoCodeByQxgl", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-缺陷管理")
    public JsonBean getAutoCodeByQxgl(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByQxgl(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-风险发现
    @RequestMapping(value = "/getAutoCodeByFxfx", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-风险发现")
    public JsonBean getAutoCodeByFxfx(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByFxfx(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-整改方案
    @RequestMapping(value = "/getAutoCodeByZgfa", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-整改方案")
    public JsonBean getAutoCodeByZgfa(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByZgfa(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-拟实施审计指引
    @RequestMapping(value = "/getAutoCodeBySjmb", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-拟实施审计指引")
    public JsonBean getAutoCodeBySjmb(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeBySjmb(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-审计指引模板库
    @RequestMapping(value = "/getAutoCodeBySjzy", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-审计指引模板库")
    public JsonBean getAutoCodeBySjzy(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeBySjzy(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-管理制度
    @RequestMapping(value = "/getAutoCodeByGlzd", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-管理制度")
    public JsonBean getAutoCodeByGlzd(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByGlzd(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-项目资料
    @RequestMapping(value = "/getAutoCodeByXmzl", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-项目资料")
    public JsonBean getAutoCodeByXmzl(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByXmzl(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-审计模板
    @RequestMapping(value = "/getAutoCodeByNewSjmb", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-审计模板")
    public JsonBean getAutoCodeByNewSjmb(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByNewSjmb(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    //自动编号-审计经验库
    @RequestMapping(value = "/getAutoCodeByNewSjjyk", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自动编号-审计经验库")
    public JsonBean getAutoCodeByNewSjjyk(HttpServletRequest request,
                                          @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByNewSjjyk(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

}
