package com.huabo.audit.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjProposalEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.vo.TblNbsjProposalVo;
import com.huabo.audit.oracle.vo.TblReportVo;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblNbsjAdvicenoteService;
import com.huabo.audit.service.TblNbsjOperateService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjProposalService;
import com.huabo.audit.service.TblNbsjSheetReportService;
import com.huabo.audit.service.TblNbsjSheetService;
import com.huabo.audit.service.TblReportService;
import com.huabo.audit.service.ZhAssessService;
import com.huabo.audit.service.ZhContentService;
import com.huabo.audit.service.ZhFormService;
import com.huabo.audit.util.FileUtil;
import com.huabo.audit.util.FreeMarkerUtil;
import com.huabo.audit.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计报告阶段
 */
@RestController
@Slf4j
@Tag(name="审计报告阶段",description="审计报告阶段")
@RequestMapping(value = "/auditReport")
public class NbsjReportController {

    @Resource
    public TblNbsjProjectService tblnbsjProjectService;

    @Resource
    public TblNbsjProposalService tblNbsjProposalService;

    @Resource
    public AttachmentService attachmentService;

    @Resource
    public ZhFormService zhFormService;

    @Resource
    public ZhContentService zhContentService;

    @Resource
    public ZhAssessService zhAssessService;

    @Resource
    public TblNbsjSheetReportService tblNbsjSheetReportService;

    @Resource
    public TblNbsjSheetService tBlNbsjSheetService;

    @Resource
    public TblNbsjOperateService tblNbsjOperateService;

    @Resource
    public TblNbsjAdvicenoteService tblNbsjAdvicenoteService;

    @Resource
    public TblReportService tblReportService;

    @Autowired
    public FreeMarkerConfig freeMarkerConfig;

    @Resource
    private TblAttachmentService tblAttachmentService;

    /**
     * 审计报告编制列表
     */
    @GetMapping("/manage/report")
    @Operation(summary = "审计报告编制列表")
    public JsonBean in_meet_record_list(HttpServletRequest request, TblReportVo tblReportVo,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                        @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                        @Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) Integer projectId) {

        JsonBean jsonBean = null;
        try {
//			jsonBean = this.zhFormService.reportPageList(token, pageNumber, pageSize,zhFormVo);
            tblReportVo.setType("nbsj");

            jsonBean = this.tblReportService.zdyPageList(token, pageNumber, pageSize, tblReportVo, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;

    }

    /**
     * 审计报告编制-新增与修改
     */
    @RequestMapping(value = "/manage/report_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计报告编制-新增与修改")
    public JsonBean workReportSave(HttpServletRequest request, @Parameter(name = "zf", description = "实体", required = true) TblReportEntity report,
                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                   @Parameter(name = "attids", description = "附件id数组", required = false) String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
//			jsonBean = this.zhFormService.reportAdd(zf,token);
            report.setType("nbsj");
            jsonBean = this.tblReportService.zdyAdd(report, token, attids);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 审计报告编制-附件列表
     */
    @GetMapping("/manage/report_file_list")
    @Operation(summary = "审计报告编制-附件列表")
    public JsonBean report_file_list(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "reportid", description = "业务主键", required = true) @RequestParam(value = "reportid", required = true) Integer reportid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblAttachmentService.reportFileList(token, reportid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 审计报告编制-附件删除
     */
    @GetMapping("/manage/report_file_del")
    @Operation(summary = "审计报告编制-附件删除")
    public R report_file_del(HttpServletRequest request, HttpServletResponse response,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.tblReportService.removeAttInfoByAttId(token, attId);
    }

    /**
     * 审计报告编制-删除
     */
    @GetMapping("/manage/report_del")
    @Operation(summary = "审计报告编制-删除")
    public JsonBean workReportDelete(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "reportid", description = "主键", required = true) @RequestParam(value = "reportid", required = true) Integer reportid) {

        try {
//			return zhFormService.reportDelete(formid, token);
            return tblReportService.zdyDelete(reportid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 审计报告编制-明细
     */
    @GetMapping("/manage/report_detail")
    @Operation(summary = "审计报告编制-明细")
    public JsonBean workReportDetail(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "reportid", description = "主键", required = true) @RequestParam(value = "reportid", required = true) Integer reportid) {
        JsonBean jsonBean = null;
        try {
//			jsonBean = zhFormService.findNbsjWorkReportDetail(token,formid);
            jsonBean = tblReportService.findZdyReportDetail(token, reportid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 报告编制-导出
     */
    @GetMapping("/manage/report_export")
    @Operation(summary = "报告编制-导出")
    public void in_meet_record_export(HttpServletRequest request, HttpServletResponse response,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "reportid", description = "报告编制主键", required = true) @RequestParam(value = "reportid", required = true) String reportid) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        TblReportEntity info = tblReportService.findReportByreportId(reportid);
        map.put("repdesc", info.getRepdesc());
        String fileName = info.getReportname() + ".doc";
        String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/template/doc");
        Boolean flag = (Boolean) FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
        if (!flag) {//如何静态文件不存在，重新生成
            FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
        }
        String fileName1 = FREEMARKER_PATH + "/" + fileName;
        FileUtil.downLoad(fileName1, response, false, fileName);
        FileUtil.deleteFile(fileName);
    }

    @GetMapping("/manage/expReportDoc")
    @Operation(summary = "报告编制-导出初稿")
    public void expReportDoc(HttpServletRequest request, HttpServletResponse response, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        try {
            List<TblNbsjSheetEntity> objList = tBlNbsjSheetService.getReportSheet(token);
            if (objList != null) {
                XWPFDocument document = new XWPFDocument();
                XWPFParagraph paragraph = document.createParagraph();
                for (int i = 0; i < objList.size(); i++) {
                    TblNbsjSheetEntity ob = objList.get(i);
                    if (ob != null) {
                        int n = ob.getTsize();
                        XWPFRun r2 = paragraph.createRun();
                        r2.setText("业务单元：" + ob.getBusinessType());
                        r2.addCarriageReturn();
                        // System.out.println("外层循环"+ob.toString());
                        for (int j = 0; j < n; j++) {
                            ob = objList.get(i);
                            XWPFRun r3 = paragraph.createRun();
                            r3.setText("标题" + (j + 1) + ". " + ob.getQuesTitle());
                            r3.addCarriageReturn();
                            r3.setBold(true);
                            XWPFRun r4 = paragraph.createRun();
                            String sjfx = ob.getAuditDiscoverable() != null ? "审计发现: " + ob.getAuditDiscoverable() + "" : "审计发现: ";
                            r4.setText(sjfx);
                            r4.addCarriageReturn();
                            XWPFRun r5 = paragraph.createRun();
                            String sjjy = ob.getAuditCourse() != null ? "审计建议：" + ob.getAuditCourse() + "" : "审计建议：";
                            r5.setText(sjjy);
                            r5.addCarriageReturn();
                            //   System.out.println("内层循环"+ob.toString()+"--"+i);
                            if (j < n - 1) {
                                i++;
                            }
                        }
                    }
                }
                response.reset();
                response.setContentType("application/binary;charset=UTF-8");
                response.setHeader("Content-disposition",
                        "attachment;filename=user_world_" + System.currentTimeMillis() + ".docx");
                OutputStream os = response.getOutputStream();
                document.write(os);
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createTitle(XWPFDocument doc, String content) {
        XWPFParagraph title = doc.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r1 = title.createRun();
        r1.setBold(true);
        r1.setFontFamily("宋体");
        r1.setText(content);
        r1.setFontSize(22);
    }

    /**
     * 审计建议书列表
     */
    @GetMapping("/sjzj/audit_suggest_list")
    @Operation(summary = "审计建议书列表")
    public JsonBean audit_suggest_list(HttpServletRequest request, TblNbsjProposalVo tblNbsjProposalVo,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                       @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                       @Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) Integer projectId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjProposalService.suggestPageList(token, pageNumber, pageSize, tblNbsjProposalVo, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;

    }

    /**
     * 审计建议书-新增与修改
     */
    @RequestMapping(value = "/sjzj/audit_suggest_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计建议书-新增与修改")
    public JsonBean audit_suggest_add(HttpServletRequest request, @Parameter(name = "proposal", description = "实体", required = true) TblNbsjProposalEntity proposal,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                      @Parameter(name = "attids", description = "附件id数组", required = false) String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjProposalService.suggestAdd(proposal, token, attids);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 审计建议书-附件列表
     */
    @GetMapping("/sjzj/audit_suggest_file_list")
    @Operation(summary = "审计建议书-附件列表")
    public JsonBean audit_suggest_file_list(HttpServletRequest request,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @Parameter(name = "proid", description = "业务主键", required = true) @RequestParam(value = "proid", required = true) Integer proid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblAttachmentService.auditSuggestFileList(token, proid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 审计建议书-附件删除
     */
    @GetMapping("/sjzj/audit_suggest_file_del")
    @Operation(summary = "审计建议书-附件删除")
    public R audit_suggest_file_del(HttpServletRequest request, HttpServletResponse response,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.tblNbsjProposalService.removeAttInfoByAttId(token, attId);
    }


    /**
     * 审计建议书-作废
     */
    @GetMapping("/sjzj/audit_suggest_cancel")
    @Operation(summary = "审计建议书-作废")
    public JsonBean audit_suggest_cancel(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "proid", description = "主键", required = true) @RequestParam(value = "proid", required = true) Integer proid) {

        try {
            return tblNbsjProposalService.suggestCancel(proid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 审计建议书-删除
     */
    @GetMapping("/sjzj/audit_suggest_del")
    @Operation(summary = "审计建议书-删除")
    public JsonBean audit_suggest_del(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "proid", description = "主键", required = true) @RequestParam(value = "proid", required = true) Integer proid) {

        try {
            return tblNbsjProposalService.suggestDelete(proid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 审计建议书-明细
     */
    @GetMapping("/sjzj/audit_suggest_detail")
    @Operation(summary = "审计建议书-明细")
    public JsonBean audit_suggest_detail(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "proid", description = "主键", required = true) @RequestParam(value = "proid", required = true) Integer proid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjProposalService.findSuggestDetail(token, proid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 自定义报告
     */
    @GetMapping("/nkbg/zdy_list")
    @Operation(summary = "自定义报告列表")
    public JsonBean zdy_list(HttpServletRequest request, TblReportVo tblReportVo,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                             @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            tblReportVo.setType("nbsj_zdy");
            jsonBean = this.tblReportService.zdyPageList(token, pageNumber, pageSize, tblReportVo, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 自定义报告-新增与修改
     */
    @RequestMapping(value = "/nkbg/zdy_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "自定义报告-新增与修改")
    public JsonBean zdy_add(HttpServletRequest request, @Parameter(name = "zf", description = "实体", required = true) TblReportEntity report,
                            @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                            @Parameter(name = "attids", description = "附件id数组", required = false) String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
            report.setType("nbsj_zdy");
            jsonBean = this.tblReportService.zdyAdd(report, token, attids);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 自定义报告-删除
     */
    @GetMapping("/nkbg/zdy_del")
    @Operation(summary = "自定义报告-删除")
    public JsonBean zdy_del(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "reportid", description = "主键", required = true) @RequestParam(value = "reportid", required = true) Integer reportid) {

        try {
            return tblReportService.zdyDelete(reportid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 自定义报告-明细
     */
    @GetMapping("/nkbg/zdy_detail")
    @Operation(summary = "自定义报告-明细")
    public JsonBean zdy_detail(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "reportid", description = "主键", required = true) @RequestParam(value = "reportid", required = true) Integer reportid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblReportService.findZdyReportDetail(token, reportid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

}
