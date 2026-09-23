package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.TblNbsjOperateService;
import com.huabo.audit.service.TblNbsjProjectPreviewService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjSheetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目资料汇总
 */
@RestController
@Slf4j
@Tag(name="项目资料汇总",description="项目资料汇总")
@RequestMapping(value = "/pjData")
public class NbsjProjectDataController {

    @Resource
    public TblNbsjOperateService tblNbsjOperateService;

    @Resource
    public AttachmentService attachmentService;

    @Resource
    public TblNbsjProjectService tblnbsjProjectService;

    @Resource
    public TblNbsjProjectPreviewService tblNbsjProjectPreviewService;

    @Resource
    public TblNbsjSheetService tBlNbsjSheetService;


    /**
     * 我的任务获取左侧树结构
     */
    @GetMapping("/myWork/getTree")
    @Operation(summary = "我的任务获取左侧树结构")
    public JsonBean myWork_getTree(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjOperateService.getMyWorkTree(token,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 任务管理-左侧树
     */
    @GetMapping("/sjss/getTree")
    @Operation(summary = "任务管理-左侧树")
    public JsonBean getTree(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name="nodeId",description="nodeId",required=false) @RequestParam(value = "nodeId", required = false) Integer nodeId,
                            @Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) Integer projectId,
                            @Parameter(name = "type", description = "all,my", required = false) @RequestParam(value = "type", required = false) String type) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjOperateService.getTree(token, projectId, nodeId, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 任务管理列表
     */
    @GetMapping("/sjss/check_list_my_all")
    @Operation(summary = "任务管理列表")
    public JsonBean check_list_my_all(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                      @Parameter(name="businessType",description="businessType",required=false) @RequestParam(value = "businessType", required = false) String businessType,
                                      @Parameter(name="targetId",description="targetId",required=false) @RequestParam(value = "targetId", required = false) Integer targetId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjOperateService.checkAllListMyPageList(token, pageNumber, pageSize, businessType, targetId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 任务管理-明细
     */
    @GetMapping("/sjss/check_all_disp")
    @Operation(summary = "任务管理-明细")
    public JsonBean check_all_disp(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "programid", description = "主键", required = true) @RequestParam(value = "programid", required = true) Integer programid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjOperateService.checkListMyDetail(token, programid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 附件列表
     */
    @GetMapping("/sjgl/dgfjlist_download")
    @Operation(summary = "附件列表")
    public JsonBean dgfjlist_download(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "attname", description = "附件名称", required = false) @RequestParam(value = "attname", required = false) String attname,
                                      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = attachmentService.dgfjlistPageList(token, pageNumber, pageSize, attname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 项目查看-项目查看
     */
    @GetMapping("/sjgl/project_look")
    @Operation(summary = "项目查看")
    public JsonBean project_look(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "projectId", description = "项目id", required = false) @RequestParam(value = "projectId", required = false) Integer projectId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblnbsjProjectService.findSSProjectDetail(token, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 项目查看-左侧菜单树
     */
    @GetMapping("/xmzl/getLiftMenu")
    @Operation(summary = "项目查看-左侧菜单树")
    public JsonBean getLiftMenu(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "projectId", description = "项目id", required = false) @RequestParam(value = "projectId", required = false) Integer projectId) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjProjectPreviewService.getLiftMenu(token, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 项目归档
     */
    @GetMapping("/sjss/project_archive_list")
    @Operation(summary = "项目归档列表")
    public JsonBean project_archive_list(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

        JsonBean jsonBean = null;
        try {

            jsonBean = tblNbsjOperateService.pjArchiveMyPageList(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 项目归档-保存
     */
    @RequestMapping(value = "/xmgd/save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "项目归档-保存")
    public JsonBean save(HttpServletRequest request,
                         @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                         @Parameter(name = "selectedData", description = "选择业务数据的数组id(用,分隔)", required = false) @RequestParam(value = "selectedData", required = false) String selectedData,
                         @Parameter(name = "numPrice", description = "实际费用", required = false) @RequestParam(value = "numPrice", required = false) Integer numPrice,
                         @Parameter(name = "dateEndTime", description = "项目实际结束时间", required = false) @RequestParam(value = "dateEndTime", required = false) String dateEndTime,
                         @Parameter(name = "filCode", description = "档案编号", required = false) @RequestParam(value = "filCode", required = false) String filCode,
                         @Parameter(name = "worktime", description = "工作量", required = false) @RequestParam(value = "worktime", required = false) Integer worktime,
                         @Parameter(name = "filName", description = "档案名称", required = false) @RequestParam(value = "filName", required = false) String filName) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjOperateService.xmgdSave(token, selectedData, numPrice, dateEndTime, filCode, filName, worktime);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 归档验证-底稿是否复核完成进行提示
     */
    @GetMapping("/sjss/dg_listall")
    @Operation(summary = "底稿未复核数量")
    public JsonBean dgAllPageList(HttpServletRequest request, TBlNbsjSheetVo tBlNbsjSheetVo,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tBlNbsjSheetService.dgAllPageList(token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


}
