package com.huabo.audit.controller;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdviceAprEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdvicenoteChangeEntity;
import com.huabo.audit.oracle.entity.TblYqnsAdvicenoteEntity;
import com.huabo.audit.oracle.vo.DataProVo;
import com.huabo.audit.oracle.vo.TblNbsjAdvicenoteVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.service.ActivityPluginsService;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.ProcessService;
import com.huabo.audit.service.TblAduitProGramService;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblNbsjAdvicenoteService;
import com.huabo.audit.service.TblNbsjAdvicenotechangeService;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjFactbookService;
import com.huabo.audit.service.TblNbsjOperateService;
import com.huabo.audit.service.TblNbsjProjectDataProService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjSheetService;
import com.huabo.audit.util.FileUtil;
import com.huabo.audit.util.FreeMarkerUtil;
import com.huabo.audit.util.R;
import com.huabo.audit.util.SnowflakeIdWorker;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计准备阶段
 */
@RestController
@Slf4j
@Tag(name="审计准备阶段",description="审计准备阶段")
@RequestMapping(value = "/auditReady")
public class NbsjReadyController {

    @Resource
    public TblAduitProGramService tblAduitProGramService;

    @Resource
    public TblNbsjProjectService tblnbsjProjectService;

    @Resource
    public TblNbsjProjectDataProService dataProService;

    @Resource
    public AttachmentService attachmentService;

    @Resource
    public ActivityPluginsService activityPluginsService;

    @Resource
    public TblNbsjAdvicenoteService tblNbsjAdvicenoteService;

    @Resource
    public ProcessService processService;

    @Resource
    public TblNbsjSheetService tBlNbsjSheetService;

    @Resource
    public TblNbsjFactbookService tblNbsjFactbookService;

    @Resource
    public TblNbsjAuditplanService tblNbsjAuditplanService;

    @Resource
    public TblNbsjOperateService tblNbsjOperateService;

    @Resource
    private TblAttachmentService tblAttachmentService;

    @Resource
    public FreeMarkerConfig freeMarkerConfig;

    @Resource
    public TblNbsjAdvicenotechangeService  advicenotechangeService;
    
    @Resource
    private UserProvider userProvider;
    

    private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

    /**
     * 审计指引-左侧树菜单
     */
    @GetMapping("/sjgj/getTreeZy")
    @Operation(summary = "审计指引-左侧树菜单")
    public JsonBean getTreeZy(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name="nodeId",description="nodeId",required=false) @RequestParam(value = "nodeId", required = false) Integer nodeId,
                              @Parameter(name="tempId",description="tempId",required=false) @RequestParam(value = "tempId", required = false) Integer tempId,
                              @Parameter(name = "type", description = "mb,fp(审计指引传mb)", required = false) @RequestParam(value = "type", required = false) String type) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjOperateService.getTreeZy(token, tempId, nodeId, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 审计指引列表
     */
    @GetMapping("/sjyj/def_cat_list_zy")
    @Operation(summary = "审计指引列表")
    public JsonBean def_cat_list_zy(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                    @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                    @Parameter(name="tempId",description="tempId",required=false) @RequestParam(value = "tempId", required = false) Integer tempId,
                                    @Parameter(name="targetId",description="targetId",required=false) @RequestParam(value = "targetId", required = false) Integer targetId,
                                    @Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) Integer projectId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblAduitProGramService.defZyPageList(token, pageNumber, pageSize, tempId, targetId, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/getAuditPlanApprovalInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-办理审计计划流程页面数据获取页面")
    public JsonBean getAuditPlanApprovalInfo(HttpServletRequest request,
                                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                             @Parameter(name = "planId", description = "审计计划主键 ", required = true) @RequestParam(value = "planId", required = true) Integer planId,
                                             @Parameter(name = "taskId", description = "工作流任务主键 ", required = false) @RequestParam(value = "taskId", required = false) String taskId,
                                             @Parameter(name = "cyId", description = "审批记录主键 ", required = true) @RequestParam(value = "cyId", required = true) Integer cyId,
                                             @Parameter(name = "v", description = "v", required = false) @RequestParam(value = "v", required = false) String v) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAuditPlanApprovalInfo(token, planId, taskId, cyId, v);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/dealAuditPlanApporvalInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计计划管理-办理审批流程，通过或驳回")
    public JsonBean dealAuditPlanApporvalInfo(HttpServletRequest request, HttpServletResponse response,
                                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                              @Parameter(name = "cyId", description = "审批记录主键 ", required = true) @RequestParam(value = "cyId", required = true) Integer cyId,
                                              @Parameter(name = "taskId", description = "工作流任务主键 ", required = true) @RequestParam(value = "taskId", required = true) String taskId,
                                              @Parameter(name = "planId", description = "升级计划主键 ", required = true) @RequestParam(value = "planId", required = true) String planId,
                                              @Parameter(name = "transition", description = "办理结果 通过或驳回 同意或修改 ", required = true) @RequestParam(value = "transition", required = true) String transition,
                                              @Parameter(name = "optDesc", description = "审批意见", required = true) @RequestParam(value = "optDesc", required = true) String optDesc,
                                              @Parameter(name = "processDefinitionId", description = "processDefinitionId", required = true) @RequestParam(name = "processDefinitionId", required = true) String processDefinitionId,
                                              @Parameter(name = "processInstanceId", description = "processInstanceId", required = true) @RequestParam(name = "processInstanceId", required = true) String processInstanceId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.dealAuditPlanApporvalInfo(token, cyId, taskId, transition, optDesc, planId, processDefinitionId, processInstanceId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 审计指引-新增与修改
     */
    @RequestMapping(value = "/sjyj/def_cat_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计指引-新增与修改")
    public JsonBean def_cat_add(HttpServletRequest request, @Parameter(name = "apg", description = "实体", required = true) TblAduitProGramEntity apg,
                                @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblAduitProGramService.defCatAdd(apg, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 审计指引-删除
     */
    @GetMapping("/sjyj/def_cat_del")
    @Operation(summary = "审计指引-删除")
    public JsonBean def_cat_del(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "programId", description = "主键", required = true) @RequestParam(value = "programId", required = true) String programId) {

        try {
            return tblAduitProGramService.defCatDel(programId, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 审计指引 详情
     */
    @GetMapping("/sjss/def_cat_detail_zy")
    @Operation(summary = "审计指引--详情")
    public JsonBean def_cat_detail_zy(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "programid", description = "主键", required = true) @RequestParam(value = "programid", required = true) String programid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblAduitProGramService.findDefZyDetail(token, programid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 审计指引 左侧树
     */
    @GetMapping("/sjyj/def_left_tree_zy")
    @Operation(summary = "审计指引左侧树")
    public JsonBean def_left_tree_zy(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblAduitProGramService.defZyLeftTreeList(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 审计通知书列表
     */
    @GetMapping("/sjzb/notice_list")
    @Operation(summary = "审计通知书列表")
    public JsonBean notice_list(HttpServletRequest request, TblNbsjAdvicenoteVo tblNbsjAdvicenoteVo,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAdvicenoteService.noticePageList(token, pageNumber, pageSize, tblNbsjAdvicenoteVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 审计通知书更改列表
     */
    @GetMapping("/sjtj/noticechangeList")
    @Operation(summary = "审计通知书更改列表")
    public JsonBean noticechangeList(HttpServletRequest request, TblYqnsAdvicenoteChangeEntity tblYqnsAdvicenoteChange,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = advicenotechangeService.noticeChangePageList(token, pageNumber, pageSize, tblYqnsAdvicenoteChange);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /** 
     * 审计通知书 详情
     */
    @GetMapping("/sjzb/notice_disp")
    @Operation(summary = "审计通知书--详情")
    public JsonBean notice_disp(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "adviceid", description = "主键", required = true) @RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAdvicenoteService.findNoticeDetail(token, adviceid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean; 
    }

    /**
     * 审计通知书更改 详情
     */
    @GetMapping("/sjtj/noticechangeDisp")
    @Operation(summary = "审计通知书更改--详情")
    public JsonBean noticechangeDisp(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "changeid", description = "主键", required = true) @RequestParam(value = "changeid", required = true) Integer changeid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = advicenotechangeService.findDetail(token, changeid);
        } catch (Exception e) { 
            e.printStackTrace();
        }
        return jsonBean;
    }
 
    /**
     * 审计通知书-新增与修改
     */
    @RequestMapping(value = "/sjzb/notice_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计通知书-新增与修改") 
    public JsonBean nkgj_projectdp_add(HttpServletRequest request, @RequestBody TblYqnsAdvicenoteEntity notice,
                                       @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                       @Parameter(name = "attids", description = "附件id数组", required = false) String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAdvicenoteService.noticeAdd(notice, token, attids);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 审计通知书更改-新增与修改
     */
    @RequestMapping(value = "/sjtj/noticechangeAdd", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计通知书更改-新增与修改")
    public JsonBean noticechangeAdd(HttpServletRequest request, @Parameter(name = "noticeChange", description = "实体", required = true) TblYqnsAdvicenoteChangeEntity noticeChange,
                                       @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                       @Parameter(name = "attids", description = "附件id数组", required = false) String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.advicenotechangeService.noticeChangeAdd(noticeChange, token, attids);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 审计通知书-导出
     *
     * @param request
     * @param response
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/expOuterRuleFile", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计通知书-导出")
    public void expOuterRuleFile(HttpServletRequest request, HttpServletResponse response,
                                 @Parameter(name = "adviceid", description = "主键", required = true) @RequestHeader("adviceid") BigDecimal adviceid) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        TblYqnsAdvicenoteEntity info = tblNbsjAdvicenoteService.findById(adviceid);
        map.put("repdesc", info.getContent());
        String fileName = info.getAdvicename() + ".doc";
        String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/template/doc");
        Boolean flag = (Boolean) FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
        if (!flag) {//如何静态文件不存在，重新生成
            FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
        }
        String fileName1 = FREEMARKER_PATH + "/" + fileName;
        FileUtil.downLoad(fileName1, response, false, fileName);
        FileUtil.deleteFile(fileName);
    }

    /**
     * 审计通知书-附件列表
     */
    @GetMapping("/sjzb/notice_file_list")
    @Operation(summary = "工作日志-附件列表")
    public JsonBean notice_file_list(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "adviceid", description = "业务主键", required = true) @RequestParam(value = "adviceid", required = true) Integer adviceid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblAttachmentService.noticeFileList(token, adviceid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 审计通知书-附件删除
     */
    @GetMapping("/sjzb/notice_file_del")
    @Operation(summary = "审计通知书-附件删除")
    public R notice_file_del(HttpServletRequest request, HttpServletResponse response,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.tblNbsjAdvicenoteService.removeAttInfoByAttId(token, attId);
    }


    /**
     * 审计通知书-删除
     */
    @GetMapping("/sjzb/notice_del")
    @Operation(summary = "审计通知书-删除")
    public JsonBean notice_del(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "adviceid", description = "主键", required = true) @RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {

        try {
            return tblNbsjAdvicenoteService.noticeDelete(adviceid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    /**
     * 审计通知书更改-新增与修改
     */
    @RequestMapping(value = "/sjzb/getbglist", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计通知书-获取通知书关联的通知变更内容")
    public JsonBean getbglist(HttpServletRequest request, 
                                       @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                       @Parameter(name = "adviceid", description = "通知书ID", required = false)@RequestParam(value = "adviceid", required = false)  BigDecimal adviceid) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.advicenotechangeService.findbyadviceidAll(adviceid, token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }
    
    @GetMapping("/sjzb/noticeapr_splist")
    @Operation(summary = "审计通知书-选择审计通知审批已完成审批内容列表")
    public JsonBean noticeapr_splist(HttpServletRequest request, String advicename,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAdvicenoteService.noticespAprPageList(token, pageNumber, pageSize, advicename);
        } catch (Exception e) {
            e.printStackTrace(); 
        } 
        return jsonBean;
    } 
    
    
    
    
    /**
     * 审计通知书更改-删除
     */
    @GetMapping("/sjtj/noticechangeDel")
    @Operation(summary = "审计通知书更改-删除")
    public JsonBean noticechangeDel(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "changeid", description = "主键", required = true) @RequestParam(value = "changeid", required = true) Integer changeid) {

        try {
            return advicenotechangeService.noticeChangeDelete(changeid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 审计通知书-作废
     */
    @GetMapping("/sjzb/notice_cancel")
    @Operation(summary = "审计通知书-作废")
    public JsonBean notice_cancel(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "adviceid", description = "主键", required = true) @RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {

        try {
            return tblNbsjAdvicenoteService.noticeCancel(adviceid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 项目资料准备
     * /sjzl/data_list
     */
    @GetMapping("/sjzl/dataproject_list")
    @Operation(summary = "项目资料准备列表")
    public JsonBean dataproject_list(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, DataProVo dataProVo,
                                     @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                     @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                     @Parameter(name = "projectId", description = "projectId", required = false) @RequestParam(value = "projectId", required = false) Integer projectId,
                                     @Parameter(name = "orgid", description = "orgid", required = false) @RequestParam(value = "orgid", required = false) Integer orgid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = dataProService.dataproPageList(token, pageNumber, pageSize, dataProVo, orgid, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 项目资料准备 预览
     */
    @GetMapping("/sjzl/dataproject_view")
    @Operation(summary = "项目资料准备--预览")
    public JsonBean dataproject_view(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "dataId", description = "主键", required = true) @RequestParam(value = "dataId", required = true) Integer dataId) {
        JsonBean jsonBean = null;
        try {
            jsonBean = dataProService.findDataProDetail(token, dataId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 项目资料准备-新增与修改
     */
    @RequestMapping(value = "/sjzl/dataproject_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "项目资料准备-新增与修改")
    public JsonBean dataproject_save(HttpServletRequest request, TblNbsjProjectDataEntity pd,
                                     @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                     @Parameter(name = "mbids", description = "模板ID ", required = false) @RequestParam(value = "mbids", required = false) String mbids,
                                     @Parameter(name = "jykids", description = "审计经验库id ", required = false) @RequestParam(value = "jykids", required = false) String jykids,
                                     @Parameter(name = "attids", description = "附件id数组", required = false) String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.dataProService.dataproAdd(pd, token, attids, mbids, jykids);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 项目资料准备-附件列表
     */
    @GetMapping("/sjzl/dataproject_file_list")
    @Operation(summary = "项目资料准备-附件列表")
    public JsonBean dataproject_file_list(HttpServletRequest request,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                          @Parameter(name = "dataId", description = "业务主键", required = true) @RequestParam(value = "dataId", required = true) Integer dataId) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblAttachmentService.dataprojectFileList(token, dataId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 项目资料准备-附件删除
     */
    @GetMapping("/sjzl/dataproject_file_del")
    @Operation(summary = "项目资料准备-附件删除")
    public R report_file_del(HttpServletRequest request, HttpServletResponse response,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.dataProService.removeAttInfoByAttId(token, attId);
    }


    /**
     * 项目资料准备-下发附件给项目人员
     */
    @GetMapping("/sjzl/xfry")
    @Operation(summary = "项目资料准备-下发附件给项目人员")
    public JsonBean xfry(HttpServletRequest request, HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "staffids", description = "项目组人员ID字符串", required = true) @RequestParam("staffids") String staffids,
                         @Parameter(name = "attids", description = "附件主键ID字符串", required = true) @RequestParam("attids") String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblAttachmentService.xfry(token, staffids, attids);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, "下发失败", null);
        }
        return jsonBean;
    }


    /**
     * 项目资料准备-删除
     */
    @GetMapping("/sjzl/dataproject_del")
    @Operation(summary = "项目资料准备-删除")
    public JsonBean dataproject_del(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "dataId", description = "主键", required = true) @RequestParam(value = "dataId", required = true) Integer dataId) {

        try {
            return dataProService.dataproDelete(dataId, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/sjzl/dataproject_file_upload")
    @Operation(summary = "项目资料准备-附件列表--附件上传")
    public R fileUpload(HttpServletRequest request,
                        @Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile[] file,
                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                        @Parameter(name = "dataId", description = "业务主键", required = true) @RequestParam(value = "dataId", required = true) Integer dataId) throws Exception {
        String attPath = "";
        TblAttachment tblAttachmentEntity = new TblAttachment();
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return R.fail("用户已失效！");
        }
        if (dataId == null) {
            return R.fail("项目资料不存在！");
        }
        for (MultipartFile multipartFile : file) {
            try {
                InputStream inputStream = multipartFile.getInputStream();
                long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
                String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                attPath = FtpUtil.uploadFilePath(imageName + name, inputStream);
                if (StrUtil.isEmpty(attPath)) {
                    return R.fail("文件上传失败");
                }
                //tblAttachmentEntity.setAttid(new BigDecimal(snowflakeIdWorker.nextId()));
                tblAttachmentEntity.setAttpath(imageName + name);
                tblAttachmentEntity.setAttsize(multipartFile.getSize() / 1024);
                tblAttachmentEntity.setUploadtime(new Date());
                tblAttachmentEntity.setUploader(loginStaff.getRealname());
                tblAttachmentEntity.setAttname(fileName);
                tblAttachmentService.saveEntity(tblAttachmentEntity);
                //上传附件之后 保存到项目资料中间表
                this.dataProService.dataproAddFile(dataId, tblAttachmentEntity.getAttid());

            } catch (Exception e) {
                e.printStackTrace();
                return R.fail("上传失败");
            }
        }
        //返回当前添加的文件 前端回显
        return R.success(tblAttachmentEntity);
    }


    @PostMapping("/sjzl/tbxmfj")
    @Operation(summary = "项目资料准备-同步项目附件上传")
    public JsonBean tbxmfj(HttpServletRequest request,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "dataId", description = "业务主键", required = true) @RequestParam(value = "dataId", required = true) Integer dataId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = dataProService.savexmfj(token, dataId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, "同步失败", null);
        }
        return jsonBean;
    }


    @GetMapping("/sjzl/issueProject")
    @Operation(summary = "项目资料准备-下发")
    public JsonBean issueProject(HttpServletRequest request, HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "staffId", description = "下发人员ID", required = true) @RequestParam("staffId") String staffId,
                                 @Parameter(name = "dataId", description = "项目资料ID", required = true) @RequestParam("dataId") String dataId,
                                 @Parameter(name = "projectId", description = "项目资料ID", required = false) @RequestParam(value = "projectId", required = false) String projectId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = dataProService.issueProject(token, staffId, dataId, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 前期审计资料列表
     */
    @GetMapping("/sjzb/project_proposal_yw")
    @Operation(summary = "前期审计资料列表")
    public JsonBean project_proposal_yw(HttpServletRequest request, TblnbsjProjectVo tblnbsjProjectVo,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                        @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblnbsjProjectService.projectProposalPageList(token, pageNumber, pageSize, tblnbsjProjectVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @GetMapping("/sjzb/project_proposal_detail")
    @Operation(summary = "前期审计资料--明细")
    public JsonBean project_proposal_detail(HttpServletRequest request,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @Parameter(name = "projectId", description = "主键", required = true) @RequestParam(value = "projectId", required = true) Integer projectId) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblnbsjProjectService.findProjectProposalDetail(token, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 前期审计资料--导入资料列表
     */
    @GetMapping("/sjgd/audit_plan_list_planId_in")
    @Operation(summary = "前期审计资料--导入资料列表")
    public JsonBean audit_plan_list_planId_in(HttpServletRequest request,
                                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                              @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                              @Parameter(name = "projectname", description = "项目名称", required = false) @RequestParam(value = "projectname", required = false) String projectname) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblnbsjProjectService.auditPlanListPlanIdIn(token, pageNumber, pageSize, projectname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 前期审计资料--导入资料选择保存
     */
    @RequestMapping(value = "/sjgd/audit_plan_list_planId_in_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "前期审计资料--导入资料选择保存")
    public JsonBean audit_plan_list_planId_in_save(HttpServletRequest request,
                                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                                   @Parameter(name = "ids", description = "选择导入的项目ids(,分隔)", required = false) String ids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblnbsjProjectService.auditPlanInAdd(ids, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }
    
    
    @GetMapping("/sjzb/noticeapr_list")
    @Operation(summary = "审计通知审批-列表")
    public JsonBean noticeapr_list(HttpServletRequest request, String advicename,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "xctype", description = "筛选类型：sjtzs-审计通知书", required = false) @RequestParam(value = "xctype", required = false) String xctype,
                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAdvicenoteService.noticeAprPageList(token, pageNumber, pageSize, advicename,xctype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    @GetMapping("/sjzb/noticeapr_disp")
    @Operation(summary = "审计通知审批-详情")
    public JsonBean noticeapr_disp(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "adviceid", description = "主键", required = true) @RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAdvicenoteService.findNoticeAprDetail(token, adviceid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/sjzb/noticeapr_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计通知审批-新增与修改")
    public JsonBean noticeapr_add(HttpServletRequest request, @Parameter(name = "notice", description = "实体", required = true) TblYqnsAdviceAprEntity noticeapr,
                                       @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAdvicenoteService.noticeAprAdd(noticeapr, token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }
	
    @GetMapping("/sjzb/noticeapr_del")
    @Operation(summary = "审计通知审批-删除")
    public JsonBean noticeapr_del(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "adviceid", description = "主键", required = true) @RequestParam(value = "adviceid", required = true) BigDecimal adviceid) {

        try {
            return tblNbsjAdvicenoteService.noticeAprDelete(adviceid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
}
