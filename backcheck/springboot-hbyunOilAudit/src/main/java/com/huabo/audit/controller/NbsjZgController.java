package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjRefopm;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjRefopmService;
import com.huabo.audit.service.TblNbsjReformSolutionService;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * 智能审计模块拆分(审计整改)
 *
 * @author Lenovo
 */
@RestController
@Tag(name="审计整改",description="审计整改")
@RequestMapping(value = "/nbsjzg")
public class NbsjZgController {

    @Resource
    public TblNbsjReformSolutionService solutionService;

    @Resource
    public TblNbsjRefopmService refopmservice;

    @Resource
    public TblNbsjProjectService tblnbsjProjectService;

    /**
     * 整改跟踪-项目管理列表
     */
    @GetMapping("/getZgproject_list")
    @Operation(summary = "整改跟踪-项目管理列表")
    public JsonBean project_list(HttpServletRequest request, TblnbsjProjectVo tblnbsjProjectVo, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblnbsjProjectService.getZgprojectPageList(token, pageNumber, pageSize, tblnbsjProjectVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @GetMapping("/getZgContentsList")
    @Operation(summary = "整改方案-添加整改内容页面分页功能")
    public JsonBean getzgContentsList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "问题编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "selectidIdsstr", description = "方案已关联的内容问题ID拼接串", required = false) @RequestParam(value = "selectidIdsstr", required = false) String selectidIdsstr, @Parameter(name = "projectid", description = "整改方案关联项目id", required = false) @RequestParam(value = "projectid", required = false) BigDecimal projectid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            re.setCode(code);
            jsonBean = refopmservice.getAllReformNbsjByProject(re, pageNumber, pageSize, projectid, selectidIdsstr);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getZgsolutionmgmtList")
    @Operation(summary = "整改方案列表分页功能")
    public JsonBean getAuditPlanPageList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "整改方案编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "name", description = "整改方案名称", required = false) @RequestParam(value = "name", required = false) String name, @Parameter(name = "createstaffid", description = "创建人", required = false) @RequestParam(value = "createstaffid", required = false) Integer createstaffid, @Parameter(name = "runstatus", description = "状态", required = false) @RequestParam(value = "runstatus", required = false) Integer runstatus) {
        JsonBean jsonBean = null;
        try {
            jsonBean = solutionService.findAllcxs(code, name, pageNumber, pageSize, token, "", createstaffid, runstatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @GetMapping("/getSolutionDetail")
    @Operation(summary = "整改方案查看详情信息")
    public JsonBean getAuditPlanInfo(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "solutionid", description = "整改方案主键", required = true) @RequestParam(value = "solutionid", required = true) String solutionid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = solutionService.findBySolutionid(solutionid);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/mergeSolutionInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改方案新增或修改")
    public JsonBean mergePlanProjectManageInfo(HttpServletRequest request, @Parameter(name = "solution", description = "整改方案实体", required = true) TblNbsjReformSolution solution, @Parameter(name = "solutionid", description = "整改方案主键 ，如果主键为空则根据编码新增信息", required = false) BigDecimal solutionid, @Parameter(name = "endDate", description = "截止时间", required = false) @RequestParam(value = "endDate", required = false) String endDate, @Parameter(name = "reformid", description = "根据项目选择后的问题拼接id", required = false) @RequestParam(value = "reformid", required = false) String reformid, @Parameter(name = "attids", description = "上传附件的ID", required = false) @RequestParam(value = "attids", required = false) String attids, @Parameter(name = "projectid", description = "选择的关联项目id", required = false) @RequestParam(value = "projectid", required = false) String projectid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            if (solutionid != null) {
                solution.setSolutionid(solutionid);
            }
            jsonBean = this.solutionService.saveOrUpdate(solution, token, reformid, attids, projectid, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @PostMapping("/delSolutionnr")
    @Operation(summary = "整改方案删除整改内容")
    public JsonBean delSolutionnr(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "reformid", description = "整改内容ID", required = true) @RequestParam(value = "reformid", required = true) BigDecimal reformid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = refopmservice.delete(reformid);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/getSolutionAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改方案获取所属的附件")
    public JsonBean getAuditPlanAttInfo(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "solutionid", description = "整改方案主键 ", required = true) @RequestParam(value = "solutionid", required = true) Integer solutionid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.solutionService.getAttListBySolutionId(token, solutionid);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/delSolutionAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改方案删除选择的附件")
    public JsonBean delSolutionAttInfo(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "attid", description = "附件ID ", required = true) @RequestParam(value = "attid", required = true) BigDecimal attid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.solutionService.delAttListBySolutionId(token, attid);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/issuePersonliable", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改方案下发整改责任人")
    public JsonBean issuePersonliable(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "solutionid", description = "整改方案ID ", required = true) @RequestParam(value = "solutionid", required = true) BigDecimal solutionid, @Parameter(name = "bsjdwzrrid", description = "被审计单位整改责任人用户id ", required = true) @RequestParam(value = "bsjdwzrrid", required = true) BigDecimal bsjdwzrrid, @Parameter(name = "bsjdwzfr", description = "被审计单位整改责任人用户名 ", required = true) @RequestParam(value = "bsjdwzfr", required = true) String bsjdwzfr, @Parameter(name = "reformuserid", description = "整改联络人id ", required = true) @RequestParam(value = "reformuserid", required = true) BigDecimal reformuserid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjReformSolution solution = new TblNbsjReformSolution();
            if (solutionid != null) {
                solution.setSolutionid(solutionid);
                solution.setReformuserid(reformuserid);
                solution.setBsjdwzfr(bsjdwzfr);
                solution.setBsjdwzrrid(bsjdwzrrid);
            }
            jsonBean = this.solutionService.xfry(solution, token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/startSolution", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改方案启动")
    public JsonBean startSolution(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "solutionid", description = "整改方案ID ", required = true) @RequestParam(value = "solutionid", required = true) BigDecimal solutionid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjReformSolution solution = new TblNbsjReformSolution();
            if (solutionid != null) {
                solution.setSolutionid(solutionid);
                solution.setRunstatus(1);
            }
            jsonBean = this.solutionService.saveOrUpdate(solution, token, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/deleteSolution", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改方案删除")
    public JsonBean deleteSolution(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "solutionid", description = "整改方案ID ", required = true) @RequestParam(value = "solutionid", required = true) BigDecimal solutionid) {
        JsonBean jsonBean = null;
        try {

            jsonBean = this.solutionService.delete(solutionid.toString(), token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/closeSolution", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改方案关闭")
    public JsonBean closeSolution(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "solutionid", description = "整改方案ID ", required = true) BigDecimal solutionid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjReformSolution solution = new TblNbsjReformSolution();
            if (solutionid != null) {
                solution.setSolutionid(solutionid);
                solution.setRunstatus(3);
            }
            jsonBean = this.solutionService.saveOrUpdate(solution, token, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getZgfpsolutionmgmtList")
    @Operation(summary = "整改分派列表分页功能")
    public JsonBean zgfpsolutionmgmtList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "整改方案编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "name", description = "整改方案名称", required = false) @RequestParam(value = "name", required = false) String name) {
        JsonBean jsonBean = null;
        try {
            jsonBean = solutionService.findAllcxs(code, name, pageNumber, pageSize, token, "1", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getfpContentsList")
    @Operation(summary = "整改分派-整改内容页面分页功能")
    public JsonBean getfpContentsList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "问题编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "solutionid", description = "方案id", required = false) @RequestParam(value = "solutionid", required = false) BigDecimal solutionid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            re.setCode(code);
            jsonBean = refopmservice.findTblReformBySheetIdAndSoultionid(code, solutionid, pageNumber, pageSize, re, "1", token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/fpSolutionRy", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改分派-分派保存人员")
    public JsonBean fpSolutionRy(@Parameter(name = "userid", description = "分派的整改执行人员id", required = false) BigDecimal userid, @Parameter(name = "reformid", description = "分派任务id", required = false) BigDecimal reformid, @Parameter(name = "solutionid", description = "方案id", required = false) BigDecimal solutionid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            if (reformid != null) {
                re.setReformid(reformid);
                re.setPeronincharge(userid);
            }
            jsonBean = refopmservice.saveOrupdate(re, token, solutionid);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getZggzsolutionmgmtList")
    @Operation(summary = "整改跟踪列表分页功能")
    public JsonBean getZggzsolutionmgmtList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "整改方案编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "name", description = "整改方案名称", required = false) @RequestParam(value = "name", required = false) String name) {
        JsonBean jsonBean = null;
        try {
            jsonBean = solutionService.findAllcxs(code, name, pageNumber, pageSize, token, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getgzContentsList")
    @Operation(summary = "整改跟踪-整改内容页面分页功能")
    public JsonBean getgzContentsList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "问题编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "solutionid", description = "方案id", required = false) @RequestParam(value = "solutionid", required = false) BigDecimal solutionid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            re.setCode(code);
            jsonBean = refopmservice.findTblReformBySheetIdAndSoultionid(code, solutionid, pageNumber, pageSize, re, null, token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getZglsSolutionmgmtList")
    @Operation(summary = "整改落实列表分页功能")
    public JsonBean getZglsSolutionmgmtList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "整改方案编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "name", description = "整改方案名称", required = false) @RequestParam(value = "name", required = false) String name) {
        JsonBean jsonBean = null;
        try {
            jsonBean = solutionService.findAllcxs(code, name, pageNumber, pageSize, token, "2", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @GetMapping("/getLsContentsList")
    @Operation(summary = "整改落实-整改内容页面分页功能")
    public JsonBean getLsContentsList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "问题编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "solutionid", description = "方案id", required = false) @RequestParam(value = "solutionid", required = false) BigDecimal solutionid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            re.setCode(code);
            jsonBean = refopmservice.findTblReformBySheetIdAndSoultionid(code, solutionid, pageNumber, pageSize, re, "2", token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/getReformByid", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改落实-获取整改落实信息")
    public JsonBean getReformByid(@Parameter(name = "reformid", description = "任务id", required = false) String reformid, @Parameter(name = "solutionid", description = "方案id", required = false) BigDecimal solutionid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = refopmservice.getByid(reformid);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/saveReformInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改落实-保存整改落实信息")
    public JsonBean saveReformInfo(@Parameter(name = "reformid", description = "任务id", required = false) BigDecimal reformid, @Parameter(name = "refopm", description = "整改任务实体", required = true) TblNbsjRefopm refopm, @Parameter(name = "solutionid", description = "方案id", required = false) BigDecimal solutionid, @Parameter(name = "attids", description = "上传附件id拼接串", required = false) String attids, @Parameter(name = "plancomdate", description = "计划完成时间", required = false) String plancomdate, @Parameter(name = "linedate", description = "整改截止时间", required = false) String linedate, @Parameter(name = "zgproblem", description = "整改问题金额跟非金额json", required = false) String zgproblem, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            if (reformid != null) {
                refopm.setReformid(reformid);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (plancomdate != null && plancomdate.trim().length() > 0) {
                refopm.setNextmplancomdate(sdf.parse(plancomdate));
            }
            if (linedate != null && linedate.trim().length() > 0) {
                refopm.setReformdeadline(sdf.parse(linedate));
            }
            jsonBean = refopmservice.update(refopm, token, solutionid, attids, null, zgproblem);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/tjReform", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改落实-提交整改落实信息")
    public JsonBean tjReform(@Parameter(name = "solutionid", description = "方案id", required = false) BigDecimal solutionid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = refopmservice.tjyz(solutionid, token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getZgcxSolutionmgmtList")
    @Operation(summary = "整改查询列表分页功能")
    public JsonBean getZgcxSolutionmgmtList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "整改方案编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "name", description = "整改方案名称", required = false) @RequestParam(value = "name", required = false) String name) {
        JsonBean jsonBean = null;
        try {
            jsonBean = solutionService.findAllcxs(code, name, pageNumber, pageSize, token, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @GetMapping("/getcxContentsList")
    @Operation(summary = "整改查询-整改内容页面分页功能")
    public JsonBean getcxContentsList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "问题编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "solutionid", description = "方案id", required = false) @RequestParam(value = "solutionid", required = false) BigDecimal solutionid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            re.setCode(code);
            jsonBean = refopmservice.findTblReformBySheetIdAndSoultionid(code, solutionid, pageNumber, pageSize, re, null, token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getZgjcSolutionmgmtList")
    @Operation(summary = "整改评价列表分页功能")
    public JsonBean getZgjcSolutionmgmtList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "整改方案编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "name", description = "整改方案名称", required = false) @RequestParam(value = "name", required = false) String name) {
        JsonBean jsonBean = null;
        try {
            jsonBean = solutionService.findAllcxs(code, name, pageNumber, pageSize, token, "3", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getpjContentsList")
    @Operation(summary = "整改评价-整改内容页面分页功能")
    public JsonBean getpjContentsList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "问题编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "solutionid", description = "方案id", required = false) @RequestParam(value = "solutionid", required = false) BigDecimal solutionid) {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            re.setCode(code);
            jsonBean = refopmservice.findTblReformBySheetIdAndSoultionid(code, solutionid, pageNumber, pageSize, re, null, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @RequestMapping(value = "/getpjReformByid", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改评价-获取整改评价信息")
    public JsonBean getpjReformByid(@Parameter(name = "reformid", description = "任务id", required = false) String reformid, @Parameter(name = "solutionid", description = "方案id", required = false) BigDecimal solutionid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = refopmservice.getByid(reformid);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/savepjReformInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改评价-保存评价信息")
    public JsonBean savepjReformInfo(@Parameter(name = "reformid", description = "任务id", required = false) BigDecimal reformid, @Parameter(name = "refopm", description = "整改评价实体", required = true) TblNbsjRefopm refopm, @Parameter(name = "solutionid", description = "方案id", required = false) BigDecimal solutionid, @Parameter(name = "attids", description = "上传附件id拼接串", required = false) String attids, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            if (reformid != null) {
                refopm.setReformid(reformid);
            }
            jsonBean = refopmservice.update(refopm, token, solutionid, attids, "1", null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/getReformAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改落实或整改评价获取所属的附件")
    public JsonBean getReformAttInfo(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "reformid", description = "整改方案主键 ", required = true) @RequestParam(value = "reformid", required = true) Integer reformid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.refopmservice.getAttListByReformId(token, reformid);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/delRefromAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改落实或评价删除选择的附件")
    public JsonBean delRefromAttInfo(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "attid", description = "附件ID ", required = true) @RequestParam(value = "attid", required = true) BigDecimal attid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.refopmservice.delAttListByReformId(token, attid);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/pjthReformInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改评价-退回信息")
    public JsonBean pjthReformInfo(@Parameter(name = "reformid", description = "任务id", required = false) BigDecimal reformid, @Parameter(name = "refopm", description = "整改评价实体", required = true) TblNbsjRefopm refopm, @Parameter(name = "solutionid", description = "方案id", required = false) BigDecimal solutionid, @Parameter(name = "attids", description = "上传附件id拼接串", required = false) String attids, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            if (reformid != null) {
                refopm.setReformid(reformid);
            }
            jsonBean = refopmservice.update(refopm, token, solutionid, attids, "2", null);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/tjpjReform", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改评价-提交评价信息")
    public JsonBean tjpjReform(@Parameter(name = "solutionid", description = "方案id", required = false) BigDecimal solutionid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = refopmservice.pjtjyz(solutionid, token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getwxhContentsList")
    @Operation(summary = "未销号问题列表分页功能")
    public JsonBean getwxhContentsList(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "code", description = "问题编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "audiorgid", description = "被审计单位id", required = false) @RequestParam(value = "audiorgid", required = false) String audiorgid, @Parameter(name = "projectname", description = "所属项目名称", required = false) @RequestParam(value = "projectname", required = false) String projectname) {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            re.setCode(code);
            re.setCompany(audiorgid);
            re.setProjectname(projectname);
            jsonBean = refopmservice.getAllReformNbsjBywxh(re, pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/wxhXfSolutionRy", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "未销号-再次下发")
    public JsonBean wxhXfSolutionRy(@Parameter(name = "reformid", description = "分派任务id", required = false) BigDecimal reformid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            TblNbsjRefopm re = new TblNbsjRefopm();
            if (reformid != null) {
                re.setReformid(reformid);
            }
            jsonBean = refopmservice.saveOrupdate(re, token, null);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping(value = "/getzgjgReformlist")
    @Operation(summary = "整改查询-根据整改内容查询结果集合")
    public JsonBean getzgjgReformlist(@Parameter(name = "problemid", description = "整改内容id", required = false) @RequestParam(value = "problemid", required = false) String problemid, @Parameter(name = "soultionid", description = "方案id", required = false) @RequestParam(value = "soultionid", required = false) BigDecimal soultionid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {

            jsonBean = refopmservice.getResultall(soultionid, token, problemid);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping(value = "/getzgjgReformByid")
    @Operation(summary = "获取问题整改结果信息")
    public JsonBean getzgjgReformByid(@Parameter(name = "reformid", description = "任务id", required = false) @RequestParam(value = "reformid", required = false) String reformid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = refopmservice.getByid(reformid);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 整改查询-导出
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/zgcx/exploredInfo", method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
    @Operation(summary = "整改查询导出")
    public void contract_contractLedgerList(HttpServletRequest request, HttpServletResponse response, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "solutionid", description = "方案id", required = false) @RequestParam(value = "soultionid", required = false) BigDecimal solutionid) {
        try {
            List<JSONObject> list = solutionService.findAlls2(solutionid);
            String[] cNames = {"审计类别", "项目名称", "被审计对象", "业务单元", "审计发现", "审计建议", "整改措施", "整改落实情况", "整改结论", "整改联络人", "责任部门", "整改期限", "整改状态"};
            List<Object[]> contractlist = new ArrayList<Object[]>(0);
            Object[] objs = null;
            if (list != null && list.size() > 0) {
                for (int j = 0; j < list.size(); j++) {
                    JSONObject obj = list.get(j);
                    objs = new Object[13];
                    objs[0] = obj.get("AUDITTYPE") != null ? obj.get("AUDITTYPE").toString() : "";
                    objs[1] = obj.get("PRJOECTNAME") != null ? obj.get("PRJOECTNAME").toString() : "";
                    objs[2] = obj.get("ORGNAME") != null ? obj.get("ORGNAME").toString() : "";
                    objs[3] = obj.get("BUSINESSTYPE") != null ? obj.get("BUSINESSTYPE").toString() : "";
                    objs[4] = obj.get("AUDITDISCOVERABLE") != null ? obj.get("AUDITDISCOVERABLE").toString() : "";
                    objs[5] = obj.get("AUDITCOURSE") != null ? obj.get("AUDITCOURSE").toString() : "";
                    objs[6] = obj.get("REFORMMEASURE") != null ? obj.get("REFORMMEASURE").toString() : "";
                    objs[7] = obj.get("REFORMCARRYOUT") != null ? obj.get("REFORMCARRYOUT").toString() : "";
                    objs[8] = obj.get("REFORMRESULT") != null ? obj.get("REFORMRESULT").toString() : "";
                    objs[9] = obj.get("REALNAME") != null ? obj.get("REALNAME").toString() : "";
                    objs[10] = obj.get("NAMEO") != null ? obj.get("NAMEO").toString() : "";
                    objs[11] = obj.get("REFORMDEADLINE") != null ? obj.get("REFORMDEADLINE").toString() : "";
                    objs[12] = obj.get("REFORMRESULTS") != null ? obj.get("REFORMRESULTS").toString() : "";
                    contractlist.add(objs);
//					
                }
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("整改查询结果".getBytes(), "UTF-8") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping(value = "/getzlAllReformlist")
    @Operation(summary = "整改台账-分页列表")
    public JsonBean getzlAllReformlist(@Parameter(name = "auditorg", description = "公司id(查询条件)", required = false) @RequestParam(value = "auditorg", required = false) BigDecimal auditorg, @Parameter(name = "code", description = "问题编号", required = false) @RequestParam(value = "code", required = false) String code, @Parameter(name = "projectname", description = "项目名称", required = false) @RequestParam(value = "projectname", required = false) String projectname, @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {

            jsonBean = refopmservice.selectNbsjReformByAll(auditorg, code, projectname, pageNumber, pageSize, token);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @GetMapping(value = "/getzgproblemlist")
    @Operation(summary = "整改落实-根据落实id获取整改金额或非金额信息")
    public JsonBean getzgproblemlistall(@Parameter(name = "datatype", description = "类型：金额类为1，非金额类为2", required = false) @RequestParam(value = "datatype", required = false) String datatype, @Parameter(name = "reformid", description = "整改落实id", required = false) @RequestParam(value = "reformid", required = false) BigDecimal reformid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {

            jsonBean = refopmservice.getzgproblemlist(token, reformid, datatype);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @GetMapping(value = "/deletezgproblem")
    @Operation(summary = "整改落实-删除整改金额或非金额信息")
    public JsonBean deletezgproblemlistak(@Parameter(name = "problemid", description = "整改金额或非金额id", required = false) @RequestParam(value = "problemid", required = false) BigDecimal problemid, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {

            jsonBean = refopmservice.deletezgproblemlist(token, problemid);
        } catch (Exception e) {
            e.printStackTrace();
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

}
