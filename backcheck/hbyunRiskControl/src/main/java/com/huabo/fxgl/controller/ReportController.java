package com.huabo.fxgl.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.enums.ProcessEnum;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.NbsjProject;
import com.huabo.fxgl.entity.Report;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.service.ActivityPluginsService;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.INbsjProjectService;
import com.huabo.fxgl.service.IProcessSettingService;
import com.huabo.fxgl.service.IReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-09
 */
@Slf4j
@Controller
@RequestMapping(value = "/report", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险报告",description="风险报告")
public class ReportController {
    /**
     * <p>
     * 风险报告 - 自定义报告编制
     * </p>
     *
     * @version 1.0.1
     * @author ZhangYP
     * @since 2022-08-09
     */

    @Autowired
    private IReportService reportService;

    @Autowired
    private IProcessSettingService processSettingService;

    @Autowired
    private INbsjProjectService nbsjProjectService;

    @Autowired
    private IAttachmentService attachmentService;
    @Autowired
    private ActivityPluginsService activityPluginsService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "风险报告--->风险报告编制&自定义报告编制--->点击新建按钮处理成功",
            busType = "风险报告",
            fail = "风险报告--->风险报告编制&自定义报告编制--->点击新建按钮处理失败",
            operationType = OperationType.ADD,
            subType = "风险报告"
    )
    @ResponseBody
    @Operation(summary = "风险报告--->风险报告编制&自定义报告编制--->点击新建按钮  /nkbg/add")
    @RequestMapping(value = "/nkbg/add")
    public JsonBean addnkbg(
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String identifier = "";
        if (StringUtils.isNotEmpty(type)) {
            if ("fx".equals(type)) {
                log.info("风险管理---上-风险报告---风险报告编制---列表页---跳转添加页 ");
            } else if ("nk".equals(type)) {
                log.info("内部控制---上-内控报告---评价报告编制---列表页---跳转添加页 ");
            } else if ("fx_zdy".equals(type)) {
                log.info("风险管理---上-风险报告---自定义报告编制---列表页---跳转添加页 ");
            } else if ("nk_zdy".equals(type)) {
                log.info("内部控制---上-内控报告---自定义报告编制---列表页---跳转添加页 ");
            } else if ("znjk".equals(type.trim())) {
                log.info("智能监控---上-监控报告---监控报告编制---列表页---跳转添加页 ");
            } else if ("znjk_zdy".equals(type.trim())) {
                log.info("智能监控---上-监控报告---自定义报告编制---列表页---跳转添加页 ");
            } else if ("nbsj".equals(type.trim())) {
                try {
                    identifier = activityPluginsService.getoNState(ProcessEnum.SJ_SJBG.name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("内部审计---上-审计报告---审计报告编制---列表页---跳转添加页 ");
            } else if ("nbsj_zdy".equals(type.trim())) {
                log.info("内部审计---上-审计报告---自定义报告编制---列表页---跳转添加页 ");
            }
        }
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(200);
        Map result = new HashMap();
//        result.put("UETempType", type);
        result.put("choiceSearch", choiceSearch);
        result.put("type", type);
        result.put("identifier", identifier);
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * 如果报告名称有重复的返回-2, 不保存重复报告的信息; 报告名称不重复则保存该报告
     *
     * @param reportname
     * @param startdate
     * @param report
     * @param type
     * @param staff
     * @param attids
     * @param describe
     * @param token
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "风险报告--->风险报告编制&自定义报告编制--->新建--->保存新建  处理成功",
            busType = "风险报告",
            fail = "风险报告--->风险报告编制&自定义报告编制--->新建--->保存新建  处理失败",
            operationType = OperationType.ADD,
            subType = "风险报告"
    )
    @Operation(summary = "风险报告--->风险报告编制&自定义报告编制--->新建--->保存新建  /report/isExist")
    @RequestMapping(value = "/isExist", produces = "application/json;charset=utf-8")
    @ResponseBody
    public JsonBean isExistByname(@Parameter(name = "reportname", description = "reportname") @RequestParam(required = false) String reportname,
                                  @Parameter(name = "startdate", description = "startdate") @RequestParam(required = false) String startdate,
                                  @Parameter(name = "report", description = "report") Report report,
                                  @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                                  @Parameter(name = "staff", description = "staff") Staff staff,
                                  @Parameter(name = "reporteds", description = "reporteds") @RequestParam(required = false) String reporteds,
//                                  @RequestBody CatVo describe,
                                  @Parameter(name = "removeReporteds", description = "removeReporteds 上传附件ids", required = true) @RequestParam String removeReporteds,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
//        List list = reportService.search(reportname, startdate);
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(reportname)) {
            queryWrapper.eq("reportname", reportname);
        }
        if (StringUtils.isNotBlank(startdate)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            queryWrapper.eq("reporttime", formatter.parse(startdate));
        }
//        return list(queryWrapper);
        Integer existByNum = 0;
        if (reportService.count(queryWrapper) > 0) {
            existByNum = -2;
        } else {
            if (StringUtils.isNotEmpty(type)) {
                if ("fx".equals(type)) {
                    log.info("风险管理---上-风险报告---风险报告编制---保存 ");
                } else if ("nk".equals(type)) {
                    log.info("内部控制---上-内控报告---评价报告编制---保存");
                } else if ("fx_zdy".equals(type)) {
                    log.info("风险管理---上-风险报告---自定义报告编制---保存 ");
                } else if ("nk_zdy".equals(type)) {
                    log.info("内部控制---上-内控报告---自定义报告编制---保存 ");
                } else if ("znjk".equals(type.trim())) {
                    log.info("智能监控---上-监控报告---监控报告编制---保存");
                } else if ("znjk_zdy".equals(type.trim())) {
                    log.info("智能监控---上-监控报告---自定义报告编制---保存");
                } else if ("nbsj".equals(type.trim())) {
                    log.info("内部审计---上-审计报告---审计报告编制---保存");
                    NbsjProject nbsjProject = nbsjProjectService.getSelectProject(staff.getStaffid());
                } else if ("nbsj_zdy".equals(type.trim())) {
                    log.info("内部审计---上-审计报告---自定义报告编制---保存");
                    NbsjProject nbsjProject = nbsjProjectService.getSelectProject(staff.getStaffid());
                    report.setProjectId(nbsjProject.getProjectid());
                }
            }
        }
        try {
            report.setOrgid(selectOrg.getOrgid());
            if (StringUtils.isNotEmpty(removeReporteds)){
                attachmentService.removeByIds(Arrays.asList(removeReporteds.split(",")));
            }

            report.setLinkdeptid(staffUtil.getLinkDetp().getOrgid());
            report.setCreatetime(new Date());
            reportService.add(report, reporteds);
            existByNum = Integer.parseInt(report.getReportid().toString());
        } catch (Exception e) {
            e.printStackTrace();
            existByNum = -1;
        }
        Map result = new HashMap();
        result.put("newReportId", existByNum.toString());
        return new JsonBean(200, "添加成功", result);
    }


    @OperationLog(
            success = "风险报告--->风险报告&自定义报告编制--->点击修改按钮 处理成功",
            busType = "风险报告",
            fail = "风险报告--->风险报告&自定义报告编制--->点击修改按钮 处理失败",
            operationType = OperationType.SELECT,
            subType = "风险报告"
    )
    @Operation(summary = "风险报告--->风险报告&自定义报告编制--->点击修改按钮  /nkbg/modify")
    @ResponseBody
    @RequestMapping(value = "/nkbg/modify")
    public JsonBean modifynkbg(@Parameter(name = "type", description = "风险报告类型传fx, 自定义风险报告传fx_zdy", required = true) @RequestParam(required = true) String type,
                               @Parameter(name="id",description="风险报告ID",required=true) @RequestParam(required = true) String id,
                               @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String identifier = "";

        Report report = null;
        if (StringUtils.isEmpty(id)) {
            return new JsonBean(0, "未提供风险报告ID, 该操作无法继续", null);
        }
        report = reportService.getById(id);
        if (report == null) {
            return new JsonBean(0, "该风险报告ID在系统中不存在", null);
        }
        if (StringUtils.isNotEmpty(type)) {
            if ("fx".equals(type)) {
                log.info("风险管理---风险报告---风险报告编制---列表页---修改页");
            } else if ("nk".equals(type)) {
                log.info("内部控制---内控报告---评价报告编制---列表页---修改页");
            } else if ("fx_zdy".equals(type)) {
                log.info("风险管理---风险报告---自定义报告编制---列表页---修改页");
            } else if ("nk_zdy".equals(type)) {
                log.info("内部控制---内控报告---自定义报告编制---列表页---修改页");
            } else if ("znjk".equals(type.trim())) {
                log.info("智能监控---监控报告---监控报告编制---列表页---修改页");
            } else if ("znjk_zdy".equals(type.trim())) {
                log.info("智能监控---监控报告---自定义报告编制---列表页---修改页");
            } else if ("nbsj".equals(type.trim())) {
                log.info("内部审计---上-审计报告---审计报告编制---列表页---修改页");
                try {
                    identifier = activityPluginsService.getoNState(ProcessEnum.SJ_SJBG.name());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ("nbsj_zdy".equals(type.trim())) {
                log.info("内部审计---上-审计报告---自定义报告编制---列表页---修改页");
            }
        }


        // 查询框代码
        Map result = new HashMap();
        String mbtype = "";
        if (report.getReportfile() != null && !"".equals(report.getReportfile())) {
            mbtype = "can_update";
            result.put("attid", report.getReportfile());
        }
        result.put("mbtype", mbtype);
        result.put("report", report);

        result.put("identifier", identifier);
        result.put("type", type);
        result.put("choiceSearch", choiceSearch);
        return new JsonBean(1, "success", result);
    }


    @OperationLog(
            success = "风险报告--->风险报告编制&自定义报告编制--->保存修改处理成功",
            busType = "风险报告",
            fail = "风险报告--->风险报告编制&自定义报告编制--->保存修改处理失败",
            operationType = OperationType.ADD,
            subType = "风险报告"
    )
    @Operation(summary = "风险报告--->风险报告编制&自定义报告编制--->保存修改  /nkbg/modify_save")
    @ResponseBody
    @RequestMapping(value = "/nkbg/modify_save")
    public String savemodinkbg(@Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
//                               @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(required = false) String selectProjectid,
                               @Parameter(name = "reporteds", description = "reporteds") @RequestParam(required = false) String reporteds,
                               @Parameter(name = "report", description = "report") Report report,
                               @Parameter(name = "removeReporteds", description = "removeReporteds 上传附件ids", required = true) @RequestParam String removeReporteds,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        if (StringUtils.isNotEmpty(type)) {
            switch (type) {
                case "fx":
                    log.info("风险管理---上-风险报告---风险报告编制---修改 ");
                case "nk":
                    log.info("内部控制---上-内控报告---评价报告编制---修改 ");
                case "fx_zdy":
                    log.info("风险管理---上-风险报告---自定义报告编制---修改 ");
            }
        }
//        report.setReportid(new BigDecimal(selectProjectid));
        reportService.updateReport(report, reporteds);
        if (StringUtils.isNotEmpty(removeReporteds)){
            attachmentService.removeByIds(Arrays.asList(removeReporteds.split(",")));
        }
        return JsonBean.success();
    }

    @OperationLog(
            success = "export处理成功",
            busType = "风险报告",
            fail = "export处理失败",
            operationType = OperationType.EXPORT,
            subType = "风险报告"
    )
    @Operation(summary = "export处理")
    @RequestMapping(value = "/export")
    public void expReportFile(HttpServletRequest request,
                              HttpServletResponse response,
                              @Parameter(name="id",description="风险报告ID",required=true) @RequestParam(required = true) String reportid,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Map<String, String> map = new HashMap<String,String>();

        /*TblnbsjProject project = this.tblnbsjProjectService.getSelectProject();
        TblReportService service = (TblReportService) SpringContextHolder.getBean("TblReportService");
        TblReport report = service.findByid(reportid);
        map.put("repdesc", report.getRepdesc()==null?"":report.getRepdesc());
        String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/WEB-INF/doc");
        //String fileName =report.getReportname()+".doc";
        String str = MD5Encrypt.encrypByMd5(report.getReportname());
        //加密文件
        String fileName =str+ DateUtils.dateToUnixTimestamp(DateUtils.getNowTime())+".doc";
        //中文文件
        String filen = report.getReportname()+".doc";
        Boolean flag =(Boolean)FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
        fileName=new String(fileName.getBytes("utf-8"),"iso-8859-1");
        if(!flag){//如何静态文件不存在，重新生成
            FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName,report,project);//根据模板生成静态页面
        }
        String fileName1 = FREEMARKER_PATH +"/"+ fileName;
        fileName1=new String(fileName1.getBytes("utf-8"),"iso-8859-1");
        logger.info("导出地址："+fileName1);
        FileUtil.downLoad(fileName1, response, false, filen);
        FileUtil.deleteFile(fileName1);*/
    }
}

