package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.mibiao.SecretLabel;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblExistingPlanLink;
import com.huabo.monitor.entity.TblExistingPlanLinkVo;
import com.huabo.monitor.entity.TblExistingStandard;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblTestelement;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTesttask;
import com.huabo.monitor.entity.TblTesttaskAtt;
import com.huabo.monitor.entity.TblTesttaskProblemFind;
import com.huabo.monitor.entity.Tree;
import com.huabo.monitor.mapper.TblExistingPlanLinkMapper;
import com.huabo.monitor.mapper.TblExistingStandardMapper;
import com.huabo.monitor.service.CsfaService;
import com.huabo.monitor.service.CsrwService;
import com.huabo.monitor.service.ITblAttachmentService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTestplanService;
import com.huabo.monitor.service.ITblTesttaskAttService;
import com.huabo.monitor.service.ITblTesttaskProblemFindService;
import com.huabo.monitor.service.ITblTesttaskService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.service.TblTestElementService;
import com.huabo.monitor.util.DateUtils;
import com.huabo.monitor.util.IPageResult;
import com.huabo.monitor.vo.param.ControlTestImplSaveParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author：yhr
 * @date:2022-09-13 13:42
 * @description:
 */
@RestController
@Slf4j
@Tag(name="内控测试-测试任务",description="内控测试-测试任务")
@RequestMapping(value = "/nbkz")
public class CsrwController {
	
	  @Value("${role1}")
	  private String role1;


    @Resource
    ITblTestplanService testplanService;
    @Autowired
    ITblStaffService iTblStaffService;


    @Autowired
    TblAssessService tblAssessService;

    @Resource
    CsfaService csfaService;

    @Resource
    TblTestElementService tblTestelementService;
    @Resource
    CsrwService csrwService;

    @Resource
    ITblTesttaskService testtaskService;

    @Resource
    ITblTesttaskProblemFindService iTblTesttaskProblemFindService;

    @Resource
    ITblAttachmentService attachmentService;
    @Resource
    ITblTesttaskAttService testtaskAttService;

    @Resource
    TblOrganizaService tblOrganizaService;


    @Resource
    private UserProvider userProvider;

    @Value("${application.administrators:}")
    private String administrators;

    @Autowired
    SecretLabel secretLabel;

    @Autowired
    private TblExistingPlanLinkMapper tblExistingPlanLinkMapper;

    @Autowired
    private TblExistingStandardMapper tblExistingStandardMapper;

    @Resource
    ITblAttachmentService iTblAttachmentService;

    /**
     * 测试任务
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "测试任务-主页查询成功",
            busType = "内控测试",
            fail = "测试任务-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/nkcs/impl/control_test_impl_list")
    @ResponseBody
    @Operation(summary = "测试任务-主页")
    public JsonBean nkcs_control_test_impl_list(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
        //TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
        // pageBean = service.findAllRw(null, pageNumber,
        // pageBean.getPageSize(), "", "",user.getStaffid().toString());
        // 查看测试任务
        // IPage<TblTestplan> iPage;
        //  iPage = csrwService.findAllRwToOrg(pageNumber,
        //   user.getStaffid().toString(), user.getCurrentOrg().getOrgid().toString());
        PageInfo<TblTestplan> iPage;
        iPage = csrwService.findAllRwToOrgNew(pageNumber,
                user.getStaffid().toString(), user.getCurrentOrg().getOrgid().toString(), user);
        IPageResult<TblTestplan> page = new IPageResult<TblTestplan>().buildIpage(iPage);
        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("pageBean", page);

        return new JsonBean(200, "success", mv);

    }


    @OperationLog(
            success = "测试任务-左侧树查询成功",
            busType = "内控测试",
            fail = "测试任务-左侧树查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/csrw/gettree")
    @ResponseBody
    @Operation(summary = "测试任务-左侧树")
    public JsonBean csrw_getTree(
            @Parameter(name = "planid", description = "planid") @RequestParam(value = "planid", required = false) String planid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(planid)) {
            TblTestplan plan = this.testplanService.getById(new BigDecimal(planid));

            if (plan != null) {
                mv.put("panid", planid);
                mv.put("templId", plan.getTesttemid());
                if (StringUtils.isNotBlank(planid)) {
                    TblTestplan testplan = this.testplanService.getById(new BigDecimal(planid));
                    if (testplan != null && testplan.getReturnstatus() != null && testplan.getReturnstatus().toString().equals("1")) {
                        //TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
                        List<Tree> tree = this.csrwService.getTreeCSRWByChildsReturn(plan.getTesttemid().toString(), user.getStaffid().toString(), planid);
                        if (tree != null && tree.size() > 0) {
                            mv.put("tree", tree);
                            return new JsonBean(200, "success", mv);
                        } else {
                            tree = this.csrwService.getTreeCSRWByChilds(plan.getTesttemid().toString(), user.getStaffid().toString(), planid);
                            mv.put("tree", tree);
                            return new JsonBean(200, "success", mv);
                        }
                    } else {

                        List<Tree> tree = this.csrwService.getTreeCSRWByChilds(plan.getTesttemid().toString(), user.getStaffid().toString(), planid);
                        mv.put("tree", tree);
                        return new JsonBean(200, "success", mv);
                    }
                }


            }
        }
        return new JsonBean(-1, "planid不存在", planid);
    }


    @OperationLog(
            success = "测试任务-右侧列表查询成功",
            busType = "内控测试",
            fail = "测试任务-右侧列表查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/csrw/def_list")
    @ResponseBody
    @Operation(summary = "测试任务-右侧列表")
    public JsonBean csrw_gzdg_def_list(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name="node",description="node",required=false) @RequestParam(value = "node", required = false) String node,
            @Parameter(name="templId",description="templId",required=false) @RequestParam(value = "templId", required = false) String templId,
            @Parameter(name="planid",description="planid",required=false) @RequestParam(value = "planid", required = false) String planid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        PageInfo<Map<String, Object>> pageBean = null;

        if (StringUtils.isNotBlank(planid)) {
            TblTestplan testplan = this.testplanService.getById(new BigDecimal(planid));
            if (testplan != null && testplan.getReturnstatus() != null && testplan.getReturnstatus().toString().equals("1")) {

                pageBean = this.csrwService.fingByTreeCSRWByUserReturn(node, templId, planid, user.getStaffid(), pageNumber);

                mv.put("pageBean", pageBean);
                mv.put("str", "1");
            } else {

                pageBean = this.csrwService.fingByTreeCSRWByUser(node, templId, planid, user.getStaffid(), pageNumber);
                mv.put("pageBean", pageBean);
                //mv.addObject("str", "1");
            }
            mv.put("plan", testplan);
        }

        mv.put("node", node);
        mv.put("templId", templId);
        mv.put("planid", planid);

        return new JsonBean(200, "success", mv);

    }


    @OperationLog(
            success = "测试跟踪-跟踪右侧列表查询成功",
            busType = "内控测试",
            fail = "测试跟踪-跟踪右侧列表查询失败",
            operationType = OperationType.SELECT,
            subType = "测试跟踪"
    )
    @GetMapping(value = "/csrw/defgz_list")
    @ResponseBody
    @Operation(summary = "测试跟踪-右侧列表")
    public JsonBean defgz_list(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name="node",description="node",required=false) @RequestParam(value = "node", required = false) String node,
            @Parameter(name="templId",description="templId",required=false) @RequestParam(value = "templId", required = false) String templId,
            @Parameter(name="planid",description="planid",required=false) @RequestParam(value = "planid", required = false) String planid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        PageInfo<Map<String, Object>> pageBean = null;

        if (StringUtils.isNotBlank(planid)) {
            TblTestplan testplan = this.testplanService.getById(new BigDecimal(planid));
            if (testplan != null && testplan.getReturnstatus() != null && testplan.getReturnstatus().toString().equals("1")) {

                pageBean = this.csrwService.fingByTreeCSRWByUserReturngz(node, templId, planid, user.getStaffid(), pageNumber);

                mv.put("pageBean", pageBean);
                mv.put("str", "1");
            } else {
                pageBean = this.csrwService.fingByTreeCSRWByUsergz(node, templId, planid, user.getStaffid(), pageNumber);
                mv.put("pageBean", pageBean);
                //mv.addObject("str", "1");
            }
            mv.put("plan", testplan);
        }

        mv.put("node", node);
        mv.put("templId", templId);
        mv.put("planid", planid);

        return new JsonBean(200, "success", mv);

    }


    @OperationLog(
            success = "测试任务-右侧列表-点击编号查看详情查询成功",
            busType = "内控测试",
            fail = "测试任务-右侧列表-点击编号查看详情查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/csmb/elemendatail")
    @ResponseBody
    @Operation(summary = "测试任务-右侧列表-点击编号查看详情")
    public JsonBean elemendatail(
            @Parameter(name="elementId",description="elementId",required=false) @RequestParam(value = "elementId", required = false) BigDecimal elementId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        if (null != elementId) {
            TblTestelement element = this.tblTestelementService.getById(elementId);

            mv.put("element", element);
        }

        return new JsonBean(200, "success", mv);
    }


    @OperationLog(
            success = "测试任务-右侧列表-点击修改/测试查询查询成功",
            busType = "内控测试",
            fail = "测试任务-右侧列表-点击修改/测试查询查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/csrw/addtask")
    @ResponseBody
    @Operation(summary = "测试任务-右侧列表-点击修改/测试查询")
    public JsonBean addtask(
            @Parameter(name="ementid",description="ementid") @RequestParam(value = "ementid") BigDecimal ementid,
            @Parameter(name="planid",description="planid") @RequestParam(value = "planid") BigDecimal planid,
            @Parameter(name="node",description="node",required=false) @RequestParam(value = "node", required = false) String node,
            @Parameter(name="templId",description="templId",required=false) @RequestParam(value = "templId", required = false) String templId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        if (ementid != null && planid != null) {
            QueryWrapper<TblTesttask> qw = new QueryWrapper<>();
            qw.eq("elementid", ementid).eq("planid", planid);
            TblTesttask task = this.testtaskService.getOne(qw);
            if (StringUtils.isNotBlank(task.getDutyorg())) {
                if (task.getDutyorg().matches("\\d+")) {
                    TblOrganization org = tblOrganizaService.findById(task.getDutyorg());
                    task.setDutyorgName(org != null ? org.getOrgname() : task.getDutyorg());
                } else {
                    task.setDutyorgName(task.getDutyorg());
                }
            }
            TblTestelement element = this.tblTestelementService.getById(ementid);
            List<TblAttachment> atts = attachmentService.findtTblAttachmentByTask(task.getTesttaskid());
            mv.put("element", element);
            mv.put("task", task);
            mv.put("atts", atts);
        }

        mv.put("node", node);
        mv.put("templId", templId);
        mv.put("planid", planid);


        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "测试任务-修改/测试页面-附件保存成功",
            busType = "内控测试",
            fail = "测试任务-修改/测试页面-附件保存失败",
            operationType = OperationType.UPLOAD,
            subType = "测试任务"
    )
    @Operation(summary = "测试任务-修改/测试页面-附件保存")
    @PostMapping(value = "/csrw/control_test_impl_upload")
    public JsonBean control_test_impl_upload(@Parameter(name="projectid",description="testtaskid") @RequestParam(value = "projectid") String projectid,
                                             @Parameter(name = "file", description = "附件上传entity", required = true) MultipartFile file,
                                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @Parameter(name = "formlevel", description = "表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel
    ) throws Exception {

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (formlevel != null && !"".equals(formlevel)) {
            Integer result = secretLabel.test(file);
//            Integer result = 0;
            System.out.println("是否是密标文件：" + result);
            if (result == 0) {
                log.info("文件上传失败，包含非密标文件");
                return new JsonBean(500, "包含非密标文件！", null);
            } else {
                //判断文件名是否包含密级信息
                String str = file.getOriginalFilename();
                if (str.length() >= 4) {
                    int index1 = str.indexOf("[");
                    int index2 = str.indexOf("]");
                    if (index1 != -1 && index2 != -1) {
                        String secretFlag = str.substring(index1 + 1, index2);
                        System.out.println(secretFlag);
                        String AttachmentLevel = secretLabel.secrectLabelInfo(file);
//                        String AttachmentLevel = "机密";
                        if (AttachmentLevel != null && !AttachmentLevel.equals("")) {
                            System.out.println("密级级别：" + AttachmentLevel);
                            System.out.println("密级级别：" + str.substring(index1 + 1, index2));
                            if (!AttachmentLevel.equals(str.substring(index1 + 1, index2))) {
                                return new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                            }
                        } else {
                            log.info("文件上传失败，文件名不包含正确密级信息！");
                            return new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                        }
                    } else {
                        log.info("文件上传失败，文件名不包含正确密级信息！");
                        return new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                    }
                   /* String strs = "[非密][商密][秘密][机密][公开][内部][敏感][敏感信息][普通商密][核心商密]";
                    if(strs.indexOf(secretFlag)==-1){
                        log.info("文件上传失败，文件名不包含正确密级信息！");
                        return  new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                    }*/
                } else {
                    log.info("文件上传失败，文件名不包含正确密级信息！");
                    return new JsonBean(500, "文件上传失败，附件名不包含正确密级信息！", null);
                }
                /*//获取密级级别
                String AttachmentLevel = secretLabel.secrectLabelInfo(file);
//                String AttachmentLevel = "机密";
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
                }*/
            }
        }

        String fileName = file.getOriginalFilename();
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        String oldname = fileName.substring(0, fileName.lastIndexOf("."));
        String newname = fileName.replace(oldname, "00" + timeInMillis + "csrw");
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
        TblAttachment a = new TblAttachment();
        a.setAttname(fileName);
//        a.set
//        TblTesttask task = new TblTesttask();
//        task.setTesttaskid(new BigDecimal(projectid));
//        a.getTblTesttasks().add(task);
        a.setAttpath(newname);
        a.setAttsize(new BigDecimal(size / 1024));
        a.setUploader(user.getUsername());
        a.setUploadtime(LocalDateTime.now());
        attachmentService.saveEntity(a);
        testtaskAttService.saveEntity(a.getAttid(), new BigDecimal(projectid));
        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("att", a);
        return new JsonBean(200, "上传成功", mv);
    }

    @OperationLog(
            success = "测试任务-修改/测试页面-附件删除成功",
            busType = "内控测试",
            fail = "测试任务-修改/测试页面-附件删除失败",
            operationType = OperationType.DELETE,
            subType = "测试任务"
    )
    @Operation(summary = "测试任务-修改/测试页面-附件删除")
    @PostMapping(value = "/csrw/control_test_impl_upload_del")
    public JsonBean control_test_impl_upload_del(
            @Parameter(name = "attid", description = "attid") @RequestParam(value = "attid") BigDecimal attid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        if (attid != null) {
            attachmentService.deleteAttAndTaskAtt(attid);
        }
        return new JsonBean(200, "success", null);
    }


    /**
     * 测试任务-保存测试信息
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "测试任务-修改/测试页面-保存测试成功",
            busType = "内控测试",
            fail = "测试任务-修改/测试页面-保存测试失败",
            operationType = OperationType.ADD,
            subType = "测试任务"
    )
    @Operation(summary = "测试任务-修改/测试页面-保存测试")
    @PostMapping(value = "/csrw/control_test_impl_save", produces = "application/json; charset=utf-8")
    public JsonBean nkcs_control_test_impl_save(@RequestBody ControlTestImplSaveParam param,
                                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        Integer result = 0;
        TblTesttask newtask = new TblTesttask();
        if (param.getTesttaskid() != null) {
            newtask = this.testtaskService.getById(param.getTesttaskid());
            newtask.setTestpointvalidity(param.getTestpointvalidity());
            newtask.setMemo(param.getMemo());
            newtask.setProcedures(param.getProcedures());
            newtask.setTestresult(param.getTestresult());
            newtask.setTeststatus("已完成");
            newtask.setOneprocess(param.getOneprocess());
            newtask.setTwoprocess(param.getTwoprocess());
            newtask.setThreeprocess(param.getThreeprocess());
            newtask.setRisktype(param.getRisktype());
            newtask.setEvidence(param.getEvidence());
            newtask.setDutyorg(param.getDutyorg());
            newtask.setDutystation(param.getDutystation());
            newtask.setInsystemname(param.getInsystemname());
            newtask.setEvaluationpoint(param.getEvaluationpoint());
            newtask.setEvaluationpro(param.getEvaluationpro());
            newtask.setEvaluationnode(param.getEvaluationnode());
            newtask.setQuabasis(param.getQuabasis());
            newtask.setDefecttype(param.getDefecttype());
            newtask.setDefectlevel(param.getDefectlevel());
            newtask.setDefectmemo(param.getDefectmemo());
            newtask.setDefectdetail(param.getDefectdetail());
            newtask.setFieldActivationCopy(param);
            newtask.setDesignpointvalidity(param.getDesignpointvalidity());
            newtask.setExecutepointvalidity(param.getExecutepointvalidity());
            newtask.setKnowbase(param.getKnowbase());
            testtaskService.updateById(newtask);
            mv.put("testtask", newtask);
            result = 1;
        } else {
            testtaskService.saveTesttsak(newtask);
        }
        if (param.getPlanid() != null) {
            TblTestplan plan = testplanService.getById(param.getPlanid());
            plan.setPlanstatus("执行中");
            testplanService.update(plan);
            mv.put("plan", plan);
        }
        if (StringUtils.isNotBlank(param.getAttid())) {
            String[] ids = param.getAttid().split(",");
            for (String s : ids) {
                TblTesttaskAtt att = testtaskAttService.getOne(new BigDecimal(s));
                if (att == null) {
                    testtaskAttService.saveEntity(new BigDecimal(s), param.getTesttaskid());
                }
            }
        }
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "测试任务-全部提交成功",
            busType = "内控测试",
            fail = "测试任务-全部提交失败",
            operationType = OperationType.ADD,
            subType = "测试任务"
    )
    @Operation(summary = "测试任务-全部提交")
    @PostMapping(value = "/csrw/saveall", produces = "application/json; charset=utf-8")
    public @ResponseBody
    String saveall(
            @Parameter(name = "planid", description = "planid") @RequestParam(value = "planid", required = false) String planid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return JsonBean.error("用户已失效");
        }
        if (StringUtils.isNotBlank(planid)) {
            return this.csrwService.saveAll(new BigDecimal(planid), user.getStaffid());
        }
        return JsonBean.error("保存失败");
    }


    @OperationLog(
            success = "测试任务-问题发现-新增修改成功",
            busType = "内控测试",
            fail = "测试任务-问题发现-新增修改失败",
            operationType = OperationType.UPDATE,
            subType = "测试任务"
    )
    //测试任务-问题发现
    @Operation(summary = "测试任务-问题发现-新增修改")
    @PostMapping(value = "/csrw/testtask_profind_save")
    public JsonBean testtask_profind_save(
//            @Parameter(name = "findid", description = "主键") @RequestParam(value = "findid", required = false) BigDecimal findid,
//            @Parameter(name = "testtaskid", description = "测试任务ID") @RequestParam(value = "testtaskid", required = false) BigDecimal testtaskid,
//            @Parameter(name = "oneprocess", description = "一级流程") @RequestParam(value = "oneprocess", required = false) String oneprocess,
//            @Parameter(name = "problemmemo", description = "问题概述") @RequestParam(value = "problemmemo", required = false) String problemmemo,
//            @Parameter(name = "defectmemo", description = "缺陷具体描述") @RequestParam(value = "defectmemo", required = false) String defectmemo,
//            @Parameter(name = "problemtype", description = "问题类别") @RequestParam(value = "problemtype", required = false) String problemtype,
//            @Parameter(name = "defectlevel", description = "缺陷等级") @RequestParam(value = "defectlevel", required = false) String defectlevel,
//            @Parameter(name = "quabasis", description = "定性依据") @RequestParam(value = "quabasis", required = false) String quabasis,
//            @Parameter(name = "mainorg", description = "主责部门") @RequestParam(value = "mainorg", required = false) BigDecimal mainorg,
//            @Parameter(name = "feedback", description = "反馈意见") @RequestParam(value = "feedback", required = false) String feedback,
//            @Parameter(name = "reformplan", description = "整改计划") @RequestParam(value = "reformplan", required = false) String reformplan,
//            @Parameter(name = "estfinishdate", description = "预计完成时间") @RequestParam(value = "estfinishdate", required = false) String estfinishdate,
//            @Parameter(name = "reformstaffid", description = "落实整改人") @RequestParam(value = "reformstaffid", required = false) BigDecimal reformstaffid,
//            @Parameter(name = "defecttype", description = "缺陷类型") @RequestParam(value = "defecttype", required = false) String defecttype,
//            @Parameter(name = "testyear", description = "内控评价年度") @RequestParam(value = "testyear", required = false) String testyear,
//            @Parameter(name = "risknumberid", description = "风险编号") @RequestParam(value = "risknumberid", required = false) String risknumberid,
//            @Parameter(name = "secrectLevelId", description = "密级主键") @RequestParam(value = "secrectLevelId", required = false) BigDecimal secrectLevelId,
//            @Parameter(name = "staffScopeNames", description = "知悉范围名称") @RequestParam(value = "staffScopeNames", required = false) String staffScopeNames,
//            @Parameter(name = "staffScopeIds", description = "知悉范围id") @RequestParam(value = "staffScopeIds", required = false) String staffScopeIds,
            @Parameter(name = "attids", description = "附件id") @RequestParam(value = "attids", required = false) String attids,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @RequestBody TblTesttaskProblemFind profind
    ) throws Exception {

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        Integer result = 0;
        if (profind.getFindid()!=null&&profind.getFindid().compareTo(new BigDecimal("0"))!=0) {
            iTblTesttaskProblemFindService.updateTesttsak(profind, attids);
            mv.put("profind", profind);
            result = 1;
        } else {
            profind.setFindid(RandomUtil.uuBigDecimalId());
            //在做问题台账的时候发现没有创建人，不晓得是不是有啥业务，先在此加上
            profind.setCreatestaffid(user.getStaffid());
            //关联的公司
            profind.setLinkOrg(user.getLinkOrg().getOrgid());
            profind.setCreatetime(new Date());
            iTblTesttaskProblemFindService.saveTesttsak(profind, attids);
            mv.put("profind", profind);
        }
        return new JsonBean(200, "success", mv);
    }

    
    
    
    @OperationLog(
            success = "问题台账维护整改内容",
            busType = "内控测试",
            fail = "问题台账维护整改内容",
            operationType = OperationType.UPDATE,
            subType = "问题台账"
    )
    //测试任务-问题发现
    @Operation(summary = "问题台账维护整改内容")
    @PostMapping(value = "/csrw/wttz_update")
    public JsonBean wttz_update(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, @RequestBody TblTesttaskProblemFind profind
    ) throws Exception {

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        if (profind.getFindid()!=null&&profind.getFindid().compareTo(new BigDecimal("0"))!=0) {
        	TblTesttaskProblemFind find=iTblTesttaskProblemFindService.getById(profind.getFindid());
        	find.setMeasures(profind.getMeasures());
        	find.setDeadlineStart(profind.getDeadlineStart());
        	find.setDeadlineEnd(profind.getDeadlineEnd());
        	find.setRectification(profind.getRectification());
        	find.setRectificationStatus(profind.getRectificationStatus());
            iTblTesttaskProblemFindService.updateTesttsak(profind, null);
            mv.put("profind", profind);
        } 
        return new JsonBean(200, "success", mv);
    }
    
    @OperationLog(
            success = "测试任务-问题发现-查看明查询成功",
            busType = "内控测试",
            fail = "测试任务-问题发现-查看明查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/csrw/testtask_profind_detail")
    @ResponseBody
    @Operation(summary = "测试任务-问题发现-查看明细")
    public JsonBean testtask_profind_detail(
            @Parameter(name="findid",description="findid",required=false) @RequestParam(value = "findid", required = false) BigDecimal findid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        if (null != findid) {
            TblTesttaskProblemFind profind = this.iTblTesttaskProblemFindService.getById(findid);

            mv.put("profind", profind);
        }
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "测试任务-问题发现-审批人修改成功",
            busType = "内控测试",
            fail = "测试任务-问题发现-审批人修改失败",
            operationType = OperationType.UPDATE,
            subType = "测试任务"
    )
    //测试任务-问题发现
    @Operation(summary = "测试任务-问题发现-审批人修改")
    @PostMapping(value = "/csrw/testtask_profind_update")
    public JsonBean testtask_profind_update(
            @Parameter(name = "findid", description = "主键") @RequestParam(value = "findid", required = false) BigDecimal findid,
            @Parameter(name = "feedback", description = "反馈意见") @RequestParam(value = "feedback", required = false) String feedback,
            @Parameter(name = "reformplan", description = "整改计划") @RequestParam(value = "reformplan", required = false) String reformplan,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        TblTesttaskProblemFind profind = new TblTesttaskProblemFind();
        if (null != findid) {
            profind.setFindid(findid);
            profind.setFeedback(feedback);
            profind.setReformplan(reformplan);
            iTblTesttaskProblemFindService.updateProfind(profind);
            mv.put("profind", profind);
        }
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "测试任务-问题发现-删除成功",
            busType = "内控测试",
            fail = "测试任务-问题发现-删除失败",
            operationType = OperationType.DELETE,
            subType = "测试任务"
    )
    @GetMapping(value = "/csrw/testtask_profind_del")
    @ResponseBody
    @Operation(summary = "测试任务-问题发现-删除")
    public JsonBean testtask_profind_del(
            @Parameter(name="findid",description="findid",required=false) @RequestParam(value = "findid", required = false) BigDecimal findid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        this.iTblTesttaskProblemFindService.delTesttsak(findid);

        return new JsonBean(200, "success", null);
    }

    @OperationLog(
            success = "测试任务-问题发现-通过测试任务ID查询查询成功",
            busType = "内控测试",
            fail = "测试任务-问题发现-通过测试任务ID查询查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/csrw/testtask_profind_testtaskid")
    @ResponseBody
    @Operation(summary = "测试任务-问题发现-通过测试任务ID查询")
    public JsonBean testtask_profind_testtaskid(
            @Parameter(name="testtaskid",description="testtaskid",required=false) @RequestParam(value = "testtaskid", required = false) BigDecimal testtaskid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        if (null != testtaskid) {
            List<TblTesttaskProblemFind> profind = this.iTblTesttaskProblemFindService.getByTesttaskid(testtaskid);

            mv.put("profind", profind);
        }
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "问题发现-列表查询成功",
            busType = "内控测试",
            fail = "问题发现-列表查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/csrw/testtask_profind_list")
    @ResponseBody
    @Operation(summary = "问题发现-列表")
    public JsonBean testtask_profind_list(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        IPage<TblTesttaskProblemFind> iPage;
        iPage = iTblTesttaskProblemFindService.findAllRwToOrg(pageNumber,
                user.getStaffid().toString(), user.getCurrentOrg().getOrgid().toString());

        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("pageBean", iPage);

        return new JsonBean(200, "success", mv);

    }


    @OperationLog(
            success = "发现问题台账汇总查询成功",
            busType = "内控测试",
            fail = "发现问题台账汇总查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @GetMapping(value = "/csrw/getProblemLedgerList")
    @ResponseBody
    @Operation(summary = "发现问题台账汇总")
    public JsonBean getProblemLedgerList(
            @Parameter(name = "oneprocess", description = "一级流程") @RequestParam(value = "oneprocess", required = false) String oneprocess,
            @Parameter(name = "problemtype", description = "问题类别") @RequestParam(value = "problemtype", required = false) String problemtype,
            @Parameter(name = "defectlevel", description = "缺陷等级") @RequestParam(value = "defectlevel", required = false) String defectlevel,
            @Parameter(name = "testYear", description = "内控评价年度") @RequestParam(value = "testYear", required = false) Integer testYear,
            @Parameter(name = "zgstatus", description = "整改状态") @RequestParam(value = "zgstatus", required = false) Integer zgstatus,
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name="pageSize",description="pageSize",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Integer authorityType;
       //列表权限:内控管理人员查询全部，下发人员自己
        if (JudgeRoleRight.judgeRoleRight(role1, user.getRoleNames())) {
            authorityType = 1;
        } else {
            authorityType = 0;
        }
        // IPage<TblTesttaskProblemFind> iPage;
        PageInfo<TblTesttaskProblemFind> iPage;
        TblTesttaskProblemFind tblTesttaskProblemFind = new TblTesttaskProblemFind();
        tblTesttaskProblemFind.setOneprocess(oneprocess);
        tblTesttaskProblemFind.setProblemtype(problemtype);
        tblTesttaskProblemFind.setDefectlevel(defectlevel);
        tblTesttaskProblemFind.setTestYear(testYear == null ? 0 : testYear);
        tblTesttaskProblemFind.setZgstatus(zgstatus);
        // iPage = iTblTesttaskProblemFindService.findALLProblemLedgerList(pageNumber,pageSize,user.getStaffid().toString(), user.getCurrentOrg().getOrgid().toString(),tblTesttaskProblemFind,authorityType);
        iPage = iTblTesttaskProblemFindService.findALLProblemLedgerListNew(pageNumber, pageSize, user.getStaffid().toString(), user.getCurrentOrg().getOrgid().toString(), tblTesttaskProblemFind, authorityType, user);
        IPageResult<TblTesttaskProblemFind> pageInfo = new IPageResult<TblTesttaskProblemFind>().buildIpage(iPage);
        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("pageBean", pageInfo);
        return new JsonBean(200, "success", mv);

    }

    @OperationLog(
            success = "问题台账-发起整改成功",
            busType = "内控测试",
            fail = "问题台账-发起整改失败",
            operationType = OperationType.UPDATE,
            subType = "测试任务"
    )
    @Operation(summary = "问题台账-发起整改")
    @PostMapping(value = "/csrw/sendreform")
    public JsonBean sendreform(
            @Parameter(name = "findid", description = "问题发现id") @RequestParam(value = "findid") BigDecimal findid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        try {
            iTblTesttaskProblemFindService.sendreform(findid);
        } catch (Exception e) {
            // TODO: handle exception
            return new JsonBean(201, "fail", "发起整改失败");
        }
        return new JsonBean(200, "success", "发起整改成功");
    }

    @OperationLog(
            success = "发现问题台账导出成功",
            busType = "内控测试",
            fail = "发现问题台账导出失败",
            operationType = OperationType.EXPORT,
            subType = "测试任务"
    )
    @RequestMapping(value = "/csrw/exportProblemLedger", method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
    @ResponseBody
    @Operation(summary = "发现问题台账导出")
    public void exportProblemLedger(HttpServletRequest request, HttpServletResponse response, @Parameter(name = "oneprocess", description = "一级流程") @RequestParam(value = "oneprocess", required = false) String oneprocess,
                                     @Parameter(name = "ids", description = "主键ids") @RequestParam(value = "ids", required = false) String ids,
    		                         @Parameter(name = "problemtype", description = "问题类别") @RequestParam(value = "problemtype", required = false) String problemtype,
                                    @Parameter(name = "defectlevel", description = "缺陷等级") @RequestParam(value = "defectlevel", required = false) String defectlevel,
                                    @Parameter(name = "testYear", description = "内控评价年度") @RequestParam(value = "testYear", required = false) Integer testYear,
                                    @Parameter(name = "zgstatus", description = "整改状态") @RequestParam(value = "zgstatus", required = false) Integer zgstatus,
                                    @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                    @Parameter(name="pageSize",description="pageSize",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) {
                return;
            }

            log.info("内控--问题台账-导出Excel");
            TblTesttaskProblemFind t = new TblTesttaskProblemFind();
            t.setOneprocess(oneprocess);
            t.setProblemtype(problemtype);
            t.setDefectlevel(defectlevel);
            t.setTestYear(testYear == null ? 0 : testYear);
            t.setZgstatus(zgstatus);
            response.setContentType("application/binary;charset=UTF-8");
            List<TblTesttaskProblemFind> unitList = iTblTesttaskProblemFindService.getExportProblemLedgerList(user.getStaffid().toString(), user.getCurrentOrg().getOrgid().toString(), t,ids);
            String[] cNames = {"内控评价年度", "一级流程", "问题概述", "缺陷具体描述", "问题类别", "缺陷等级", "定性依据", "主责部门", "反馈意见", "整改计划", "预计完成时间", "整改落实人", "审批状态","整改措施","整改时限","整改进展情况","完成状态"};
            List<Object[]> contractlist = new ArrayList<Object[]>(0);
            Object[] objs = null;
            for (TblTesttaskProblemFind task : unitList) {
                objs = new Object[17];
                objs[0] = task.getTestYear();
                objs[1] = task.getOneprocess();
                objs[2] = task.getProblemmemo();
                objs[3] = task.getDefectmemo();
                objs[4] = task.getProblemtype();
                objs[5] = task.getDefectlevel();
                objs[6] = task.getQuabasis();
                objs[7] = task.getOrgname();
                objs[8] = task.getFeedback();
                objs[9] = task.getReformplan();
                objs[10] = task.getEstfinishdate() == null ? "" : DateUtils.parseDate(task.getEstfinishdate(), "yyyy-MM-dd");
                objs[11] = task.getRealname();
                String status = "";
                if (task.getStatus().equals("1")) {
                    status = "审批中";
                } else if (task.getStatus().equals("2")) {
                    status = "已退回";
                } else if (task.getStatus().equals("3")) {
                    status = "已撤销";
                } else if (task.getStatus().equals("4")) {
                    status = "已终止";
                } else if (task.getStatus().equals("5")) {
                    status = "已跟踪";
                } else if (task.getStatus().equals("6")) {
                    status = "已完成";
                } else {
                    status = "未审批";
                }
                objs[12] = status;
                objs[13] = task.getMeasures();
                objs[14] = task.getDeadlineStart() == null ? "" : DateUtils.parseDate(task.getDeadlineStart(), "yyyy-MM-dd")+"-"+task.getDeadlineEnd() == null ? "" : DateUtils.parseDate(task.getDeadlineEnd(), "yyyy-MM-dd");
                objs[15] = task.getRectification();
                objs[16] = task.getRectificationStatus()==0?"未完成":"已完成";
                contractlist.add(objs);
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("问题台账".getBytes(), "UTF-8") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OperationLog(
            success = "测试任务-问题发现-新增修改-由测试任务处发送成功",
            busType = "内控测试",
            fail = "测试任务-问题发现-新增修改-由测试任务处发送失败",
            operationType = OperationType.ADD,
            subType = "测试任务"
    )
    //测试任务-问题发现
    @Operation(summary = "测试任务-问题发现-新增修改-由测试任务处发送")
    @PostMapping(value = "/csrw/testtask_profind_save_byCs")
    public JsonBean testtask_profind_save_byCs(
            @Parameter(name="ementid",description="ementid") @RequestParam(value = "ementid") BigDecimal ementid,
            @Parameter(name="planid",description="planid") @RequestParam(value = "planid") BigDecimal planid,
            @Parameter(name = "findid", description = "findid") @RequestParam(value = "findid", required = false) String findid,
            @Parameter(name="templId",description="templId",required=false) @RequestParam(value = "templId", required = false) String templId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new LinkedHashMap<>();
        try {
            Integer result = 0;
            if (ementid != null && planid != null) {
                QueryWrapper<TblTesttask> qw = new QueryWrapper<>();
                qw.eq("elementid", ementid).eq("planid", planid);
                TblTesttask task = this.testtaskService.getOne(qw);
                String id = iTblTesttaskProblemFindService.saveProblem(token, task, findid);
                mv.put("findid", id);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return new JsonBean(200, "success", mv);

    }

    @OperationLog(
            success = "问题发现附件删除成功",
            busType = "内控测试",
            fail = "问题发现附件删除失败",
            operationType = OperationType.DELETE,
            subType = "测试任务"
    )
    @Operation(summary = "问题发现附件删除")
    @PostMapping(value = "/delFjByTypeAndId")
    public JsonBean delFjByTypeAndId(
            @Parameter(name = "type", description = "类型 wtfx:问题发现") @RequestParam(value = "type") String type,
            @Parameter(name = "attid", description = "单个附件id") @RequestParam(value = "attid") String attid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        try {
            iTblTesttaskProblemFindService.delFjByTypeAndId(type, attid);
        } catch (Exception e) {
            // TODO: handle exception
            return new JsonBean(201, "fail", "删除附件失败");
        }
        return new JsonBean(200, "success", "删除附件成功");
    }


    @OperationLog(
            success = "问题发现附件列表查询成功",
            busType = "内控测试",
            fail = "问题发现附件列表查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @Operation(summary = "问题发现附件列表")
    @PostMapping(value = "/getFjListByFind")
    public JsonBean getFjListByFind(
            @Parameter(name = "findId", description = "问题发现id") @RequestParam(value = "findId") String findId
    ) throws Exception {

        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        Map<String, Object> mv = new LinkedHashMap<>();
        try {
            List<TblAttachment> fjlist = iTblTesttaskProblemFindService.getRepAttById(findId);
            mv.put("fjlist", fjlist);
        } catch (Exception e) {
            // TODO: handle exception
            return new JsonBean(201, "fail", "操作失败");
        }
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "现行标准新增成功",
            busType = "内控测试",
            fail = "现行标准新增失败",
            operationType = OperationType.DELETE,
            subType = "测试任务"
    )
    @Operation(summary = "现行标准新增")
    @PostMapping(value = "/addXXBZ")
    public JsonBean addXXBZ(@RequestBody TblExistingPlanLinkVo tblExistingPlanLinkvo,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
//        TblStaffUtil user = userProvider.get();
//        if (user == null) {
//            return ResponseFormat.retParam(0, 20006, null);
//        }
        if (tblExistingPlanLinkvo == null || tblExistingPlanLinkvo.getTaskId() == null || tblExistingPlanLinkvo.getTaskId().equals("") || tblExistingPlanLinkvo.getTestId() == null || tblExistingPlanLinkvo.getTestId().equals("")) {
            return new JsonBean(201, "fail", "操作失败");
        }
        TblTesttask tblTesttask = testtaskService.getById(tblExistingPlanLinkvo.getTaskId());
        String[] testId = tblExistingPlanLinkvo.getTestId().split(",");
        Integer flag = 0;
        for (String tid : testId) {
            TblExistingPlanLink tblExistingPlanLink = new TblExistingPlanLink();
            tblExistingPlanLink.setTestId(new BigDecimal(tid));
            tblExistingPlanLink.setTaskId(tblExistingPlanLinkvo.getTaskId());
            TblExistingStandard tblExistingStandard = tblExistingStandardMapper.selectById(tid);
            if (tblTesttask != null && tblExistingStandard != null) {
                QueryWrapper<TblExistingPlanLink> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("TASKID", tblExistingPlanLink.getTaskId()).eq("TESTID", tblExistingPlanLink.getTestId());
                List<TblExistingPlanLink> tblExistingPlanLinkList = tblExistingPlanLinkMapper.selectList(queryWrapper);
                if (tblExistingPlanLinkList == null || tblExistingPlanLinkList.size() == 0) {
                    int result = tblExistingPlanLinkMapper.insert(tblExistingPlanLink);
                    if (result > 0) {
                        flag += 1;
                    }
                }
            }
        }
        if (flag > 0){
            return new JsonBean(200, "success",flag);
        }else
            return new JsonBean(201, "fail", "操作失败");
    }

    @OperationLog(
            success = "现行标准查询成功",
            busType = "内控测试",
            fail = "现行标准查询失败",
            operationType = OperationType.SELECT,
            subType = "测试任务"
    )
    @Operation(summary = "现行标准查询")
    @GetMapping(value = "/getXXBZ")
    public JsonBean getXXBZ(@Parameter(name = "taskId", description = "任务id") @RequestParam(value = "taskId") BigDecimal taskId,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
//        TblStaffUtil user = userProvider.get();
//        if (user == null) {
//            return ResponseFormat.retParam(0, 20006, null);
//        }
        if (taskId == null || taskId.compareTo(BigDecimal.ZERO) <= 0) {
            return new JsonBean(201, "fail", "任务ID不能为空或无效");
        }
        QueryWrapper<TblExistingPlanLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("TASKID", taskId);
        List<TblExistingPlanLink> tblExistingPlanLinkList = tblExistingPlanLinkMapper.selectList(queryWrapper);
        if (tblExistingPlanLinkList != null && tblExistingPlanLinkList.size() > 0) {
            QueryWrapper<TblExistingStandard> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.in("ID", tblExistingPlanLinkList.stream().map(TblExistingPlanLink::getTestId).collect(Collectors.toList()));
            queryWrapper1.eq("IS_DELETE", 0);
            List<TblExistingStandard> tblExistingStandardList = tblExistingStandardMapper.selectList(queryWrapper1);
            return new JsonBean(200, "success", tblExistingStandardList);
        }
        return new JsonBean(200, "success", new ArrayList<>());
    }

    @OperationLog(
            success = "现行标准删除成功",
            busType = "内控测试",
            fail = "现行标准删除失败",
            operationType = OperationType.DELETE,
            subType = "测试任务"
    )
    @Operation(summary = "现行标准删除")
    @PostMapping(value = "/removeXXBZ")
    public JsonBean removeXXBZ(@RequestBody TblExistingPlanLink tblExistingPlanLink,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
//        TblStaffUtil user = userProvider.get();
//        if (user == null) {
//            return ResponseFormat.retParam(0, 20006, null);
//        }
        if (tblExistingPlanLink == null || tblExistingPlanLink.getTaskId() == null || tblExistingPlanLink.getTaskId().compareTo(BigDecimal.ZERO) <= 0 || tblExistingPlanLink.getTestId() == null || tblExistingPlanLink.getTestId().compareTo(BigDecimal.ZERO) <= 0) {
            return new JsonBean(201, "fail", "参数不能为空或无效");
        }
        QueryWrapper<TblExistingPlanLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("TASKID", tblExistingPlanLink.getTaskId());
        queryWrapper.eq("TESTID", tblExistingPlanLink.getTestId());
        int result = tblExistingPlanLinkMapper.delete(queryWrapper);
        if (result > 0) {
            return new JsonBean(200, "success", "操作成功！");
        }
        return new JsonBean(201, "fail", "操作失败");
    }
    
    @OperationLog(
            success = "问题台账-转发人员",
            busType = "内控测试",
            fail = "问题台账-转发人员",
            operationType = OperationType.DISPATCH,
            subType = "问题台账"
    )
    @Operation(summary = "问题台账-转发")
    @PostMapping(value = "/csrw/toIssueLedger")
    public JsonBean toIssueLedger(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "ids", description = "主键ids,逗号相隔", required = true) @RequestParam(value = "ids", required = true) String  ids,
    		@Parameter(name = "staffId", description = "被转发人员id", required = true) @RequestParam(value = "staffId", required = true) BigDecimal staffId
    ) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return   iTblTesttaskProblemFindService.forwardPersonnel(token,ids,staffId);
    }

    
    
    @OperationLog(
            success = "查看转发记录",
            busType = "内控测试",
            fail = "查看转发记录",
            operationType = OperationType.SELECT,
            subType = "问题台账"
    )
    
    @Operation(summary = "问题台账-转发记录列表/getProblemTransferList")
    @ResponseBody
    @RequestMapping(value = "/getProblemTransferList")
    public JsonBean getProblemTransferList(
    		@Parameter(name = "id", description = "主键id", required = false) @RequestParam(value = "id", required = false) BigDecimal id,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                                    ) throws Exception {
        return iTblTesttaskProblemFindService.getProblemTransferList(token,id);
    }
}
