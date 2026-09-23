package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.entity.BugCriterion;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.MonitorExeinterval;
import com.huabo.cybermonitor.entity.MonitorModel;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.MonitorSolutionRule;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.entity.Worksheet;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBugCriterionService;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.huabo.cybermonitor.service.IMonitorExeintervalService;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorModelsolutionService;
import com.huabo.cybermonitor.service.IMonitorRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.IWorksheetService;
import com.huabo.cybermonitor.service.TreeService;
import com.huabo.cybermonitor.task.ZNJKGZYJTask;
import com.huabo.cybermonitor.task.ZNJKMXYJTask;
import com.huabo.cybermonitor.task.ZNJKZBYJTask;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.task.base.ScheduleJob;
import com.huabo.cybermonitor.util.ConstClass;
import com.huabo.cybermonitor.util.FxglUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;






/**
 * 监控执行
 *
 * @author kangjx
 * @createTime 2022/7/27
 */
@RestController
@Slf4j
@Tag(name="老版-监控执行",description="老版-监控执行")
@RequestMapping(value = "/cyber/SupervisionController")
public class SupervisionController {

//    private static final Logger logger = LoggerFactory.getLogger(IndicatorMonitoringController.class);

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorModelsolutionService iMonitorModelsolutionService;

    @Autowired
    IMonitorExeintervalService iMonitorExeintervalService;

    @Autowired
    IMonitorSolutionRuleService iMonitorSolutionRuleService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    IWorksheetService iWorksheetService;

    @Autowired
    IAttachmentService iAttachmentService;

    @Autowired
    public ZNJKZBYJTask znjkzbyjTask;

    @Autowired
    public ZNJKGZYJTask znjkgzyjTask;

    @Autowired
    public JobTaskService jobTaskService;

    @Autowired
    IStaffService iStaffService;

    @Autowired
    IBugCriterionService iBugCriterionService;

    @Autowired
    TreeService treeService;

    @Autowired
    IMonitorModelService iMonitorModelService;

    @Autowired
    IIndicatorService iIndicatorService;

    @Resource
    public ZNJKMXYJTask znjkmxyjTask;

    @Resource
    public IMonitorRuleService iMonitorRuleService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 监控执行：规则预警 --index
     *
     * @param tblStaff 原session对象只为获得orgid,感觉没有意义的方法 规则列改原则进行修改
     */
    @Operation(summary = "rulesolution_index")
    @PostMapping(value = "/jkzx/rulesolution_index")
    public JsonBean rulesolution_index(@Parameter(name = "tblStaff", description = "tblStaff") @RequestBody Staff tblStaff) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        return new JsonBean(200, "success", tblStaff.getOrgid());
    }

    /**
     * 监控执行：规则预警---左侧树形
     *
     * @param orgid 通过session获得orgid，分布式修改以后没有session，要么考虑前台传递要么考虑redis获取，此处直接前台传递
     * @return
     */
    @Operation(summary = "jkzx_solutionleft")
    @GetMapping(value = "/jkzx/solutionleft")
    public JsonBean jkzx_solutionleft(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid", required = false) String orgid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        return new JsonBean(200, "success", orgid);
    }

    /**
     * 监控执行：规则预警 --列表
     *
     * @param orgId      组织编号
     * @param pageNumber 当前页
     * @return
     * @date 2016年1月14日 下午2:56:54
     */
    @Operation(summary = "监控执行：规则预警 --列表")
    @GetMapping(value = "/jkzx/solutionmgmt")
    public JsonBean jkzx_solutionMgmt(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid", required = false) BigDecimal orgId,
                                      @Parameter(name = "orgid", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                      @Parameter(name = "tblStaff", description = "老系统session获取，现在从前台传递，如果不可以后期改成redis获取 ") @RequestParam(value = "tblStaff", required = false) @RequestBody Staff tblStaff) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        IPage ip = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
        orgId = Objects.isNull(orgId)?tblStaff.getOrgid() : orgId;
        Boolean isSelect = treeService.isSJByOrgId(tblStaff.getOrgid().toString(),
                orgId.toString());
        QueryWrapper<MonitorSolution> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", 2);
        queryWrapper.eq("SOLUTIONSTATUS", "启用");
        queryWrapper.orderByDesc("createdate");
        if (isSelect) {
            queryWrapper.eq("ORGID", orgId);
        } else {
            queryWrapper.eq("ORGID", tblStaff.getOrgid());
        }
        iMonitorSolutionService.page(ip, queryWrapper);
        Map<String, Object> map = new HashMap<>(3);
        map.put("orgId", orgId);
        map.put("pageBean", ip);
        map.put("isAdd", isSelect);
        return new JsonBean(200, "success", map);
    }

    /**
     * 指标管理-修改权限判断
     *
     * @return
     */
    @Operation(summary = "指标管理-修改权限判断")
    @Transactional
    @PostMapping(value = "/jkzx/saveuser", produces = "application/json; charset=utf-8")
    public JsonBean saveUser(@Parameter(name = "solutionid", description = "solutionid") @RequestParam(name = "solutionid", value = "solutionid") String solutionid
            , @Parameter(name = "userid", description = "userid") @RequestParam(name = "userid", value = "userid") String userid) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (userid != null && !userid.equals("") && solutionid != null && !solutionid.equals("")) {
            MonitorSolution solution = iMonitorSolutionService.getById(solutionid);

            QueryWrapper qw = new QueryWrapper();
            qw.eq("solutionid", solutionid);
            Staff user = iStaffService.getById(userid);
            List<Staff> staffs = iStaffService.list(qw);
            boolean falg = true;
            if (staffs != null && staffs.size() > 0) {
                for (Staff tblStaff : staffs) {
                    if (tblStaff.getStaffid().toString().equals(userid)) {
                        falg = false;
                    }
                }
            }
            if (falg) {
                iStaffService.save(user);
            }
            iMonitorSolutionService.updateById(solution);
            return new JsonBean(200, "success", true);
        } else {
            return new JsonBean(200, "操作失败", false);
        }
    }

    /**
     * 监控执行：规则tree
     *
     * @param nodeId
     * @param type
     * @param orgId
     * @return
     */
    @Operation(summary = "jkzx_gzjfindOrganizationByTree")
    @GetMapping(value = "/jkzx/findOrganizationByTreeAll")
    public JsonBean jkzx_gzjfindOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId") @RequestParam(name = "nodeId", value = "nodeId") BigDecimal nodeId,
                                                   @Parameter(name = "type", description = "type") @RequestParam(name = "type", value = "type") String type,
                                                   @Parameter(name = "orgId", description = "orgId") @RequestParam(name = "orgId", value = "orgId") BigDecimal orgId) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        String json = "";
        if (null == nodeId) {
            nodeId = orgId;
        }
        if (StringUtils.isNotBlank(type)) {
            List<Tree> list = this.treeService.getTree(nodeId);
            for (Tree tree : list) {
                if (!tree.getIsParent()) {
                    tree.setTarget("mainFramex");
                    tree.setUrl("/znjk/jkzx/solutionmgmt?orgId=" + tree.getId());
                }
            }
            json = JSONObject.toJSONString(list);
        } else {
            List<Tree> list = iOrganizationService.getNodeAll(nodeId);
            for (Tree tree : list) {
                setUrlByTree(tree, "/znjk/jkzx/solutionmgmt?orgId=");
            }
            json = JSONObject.toJSONString(list);

        }
        return new JsonBean(200, "success", json);
    }

    /**
     * 处理树
     *
     * @param tree
     * @param url
     */
    private void setUrlByTree(Tree tree, String url) {
        for (Tree tre : tree.getChildren()) {
            if (tre.getChildren().size() > 0) {
                setUrlByTree(tre, url);
            }
            tre.setTarget("mainFramex");
            tre.setUrl(url + tre.getId());
        }
    }


    /**
     * 监控执行：预警方案-选择粒度
     *
     * @param nodeId
     * @param type
     * @param orgId
     * @return
     */
    @Operation(summary = "预警方案 通过type选择预警方案")
    @PutMapping(value = "/jkzx/update_monitorSolution")
    public String update_monitorSolution(@Parameter(name = "nodeId", description = "nodeId") @RequestParam(name = "nodeId", value = "nodeId") BigDecimal nodeId,
                                         @Parameter(name = "type", description = "type") @RequestParam(name = "type", value = "type") String type,
                                         @Parameter(name = "staffid", description = "staffid") @RequestParam(name = "staffid", value = "staffid") String staffid,
                                         @Parameter(name = "exe", description = "exe") @RequestParam(name = "exe", value = "exe") String exe,
                                         @Parameter(name = "solutionid", description = "solutionid") @RequestParam(name = "solutionid", value = "solutionid") String solutionid,
                                         @Parameter(name = "orgId", description = "orgId") @RequestParam(name = "orgId", value = "orgId") BigDecimal orgId) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return JsonBean.error("用户已失效");
 		}
        if (solutionid != null && !"".equals(solutionid)) {
            MonitorSolution monitorSolution = iMonitorSolutionService.getById(solutionid);

            Staff tblStaff = iStaffService.getById(staffid);

            List<MonitorSolution> monitorSolutions = new ArrayList<MonitorSolution>();
            if (monitorSolution.getStaffid().equals(tblStaff.getStaffid())) {
                try {
                    QueryWrapper qw = new QueryWrapper();
                    qw.eq("SOLUTIONID", solutionid);
                    if (monitorSolution.getType().equals(1)) {
                        // 规则
//                        logger.info("添加规则预警");

                        List<MonitorSolutionRule> set = iMonitorSolutionRuleService.list(qw);

                        monitorSolutions.add(monitorSolution);

                        ZNJKGZYJTask znjkgzyjTask = this.znjkgzyjTask;
                        znjkgzyjTask.setMonitorSolutions(monitorSolutions);
                        znjkgzyjTask.setStaff(tblStaff);
                        znjkgzyjTask.setLabel("");
                        znjkgzyjTask.setSource(MonitorSolutionresult.ZKZX);
                        ScheduleJob job = new ScheduleJob();
                        job.setJobGroup(ScheduleJob.GROUP_TYPE);
                        job.setJobName(monitorSolution.getSolutionid().toString());
                        job.setJobStatus(ScheduleJob.STATUS_RUNNING);
                        job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
                        job.setTask(znjkgzyjTask);
                        jobTaskService.updateJob(job, exe);
                        monitorSolution.setExefrequncy(exe);
                        // tblMonitorSolutionService.modify(monitorSolution);
                    } else if (monitorSolution.getType().equals(2)) {
                        // 指标预警
                        List<Indicator> tblMonitorSolutionIndicators = iIndicatorService.list(qw);
                        if (tblMonitorSolutionIndicators.size() > 0) {
//                            logger.info("添加指标预警");
                            ZNJKZBYJTask task = this.znjkzbyjTask;
                            monitorSolutions.add(monitorSolution);
                            task.setMonitorSolutions(monitorSolutions);
                            task.setTblStaff(tblStaff);
                            task.setLabel("");
                            task.setSource(MonitorSolutionresult.ZKZX);
                            ScheduleJob job = new ScheduleJob();
                            job.setJobGroup(ScheduleJob.GROUP_TYPE);
                            job.setJobName(monitorSolution.getSolutionid().toString());
                            job.setJobStatus(ScheduleJob.STATUS_RUNNING);
                            job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
                            job.setTask(task);
                            jobTaskService.updateJob(job, exe);
                            monitorSolution.setExefrequncy(exe);
                            // tblMonitorSolutionService.modify(monitorSolution);
                        } else {
                            return JsonBean.error("指标不完整，不能设置粒度！");
                        }
                    } else if (monitorSolution.getType().equals(3)) {
//                        logger.info("添加模型预警");
                        List<MonitorModel> models = iMonitorModelService.list(qw);
                        if (models.size() > 0) {
                            ScheduleJob job = new ScheduleJob();
                            ZNJKMXYJTask task = this.znjkmxyjTask;
                            task.setTblStaff(tblStaff);
                            task.setLable("");
                            task.setSource(MonitorSolutionresult.ZKZX);
                            monitorSolutions.add(monitorSolution);
                            task.setMonitorSolutions(monitorSolutions);
                            job.setIsConcurrent(ScheduleJob.CONCURRENT_IS);
                            job.setJobName(monitorSolution.getSolutionid().toString());
                            job.setJobGroup(ScheduleJob.GROUP_TYPE);
                            job.setJobStatus(ScheduleJob.STATUS_RUNNING);
                            job.setTask(task);
                            jobTaskService.updateJob(job, exe);
                            monitorSolution.setExefrequncy(exe);
                        } else {
                            return JsonBean.error("模型不完整，不能设置粒度！");
                        }
                    }
                    return JsonBean.success();
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonBean.error("选择执行粒度失败");
                }
            } else {
                return JsonBean.error("权限不足");
            }
        } else {
            return JsonBean.error("选择执行粒度失败");
        }
    }


    /**
     * 监控执行：模型执行-tree
     *
     * @return
     */
    @Operation(summary = "监控执行：模型执行-tree")
    @GetMapping(value = "/jkzx/findOrganizationByTreeAlls", produces = "application/json; charset=utf-8")
    public String jkzxlfindOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId") @RequestParam(name = "nodeId", value = "nodeId") BigDecimal nodeId,
                                              @Parameter(name = "type", description = "type") @RequestParam(name = "type", value = "type") String type,
                                              @Parameter(name = "orgId", description = "orgId") @RequestParam(name = "orgId", value = "orgId") BigDecimal orgId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return JsonBean.error("用户已失效");
 		}
        String json = "";
        if (null == nodeId) {
            nodeId = orgId;
        }
        if (StringUtils.isNotBlank(type)) {
            List<Tree> list = this.treeService.getTree(nodeId);
            for (Tree tree : list) {
                if (!tree.getIsParent()) {
                    tree.setTarget("mainFramex");
                    tree.setUrl("/znjk/jkzx/modelssolutionmgmt?orgId=" + tree.getId());
                }
            }
            json = JSONObject.toJSONString(list);
        } else {
            List<Tree> list = this.iOrganizationService.getNodeAll(nodeId);
            for (Tree tree : list) {
                setUrlByTree(tree, "/znjk/jkzx/modelssolutionmgmt?orgId=");
            }
            json = JSONObject.toJSONString(list);
        }
        return json;
    }

    /**
     * 监控执行-模型执行
     *
     * @return
     */
    @Operation(summary = "监控执行-模型执行")
    @GetMapping(value = "/jkzx/modelssolutionmgmt")
    public JsonBean modelssolutionmgmt(@Parameter(description="用户点击的组织机构id") @RequestParam("userOrgid") String userOrgid,
                                       @Parameter(description="orgid") @RequestParam("orgid") String orgid,
                                       @Parameter(description="页码") @RequestParam("page") Integer page,
                                       @Parameter(description="每页多少条") @RequestParam("orgid") Integer limit) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Boolean isSelect = treeService.isSJByOrgId(userOrgid, orgid);
        IPage<MonitorSolution> ip = new Page<>(page, limit);
        QueryWrapper<MonitorSolution> qw = new QueryWrapper<>();
        qw.eq("type", 3);
        qw.eq("SOLUTIONSTATUS", "启用");
        qw.eq("ORGID", orgid);
        qw.orderByDesc("createdate");
        if (isSelect) {
            qw.eq("ORGID", orgid);
        } else {
            qw.eq("ORGID", userOrgid);
        }
        iMonitorSolutionService.page(ip, qw);
        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", ip);
        mv.put("orgId", orgid);
        mv.put("tblStaff", iStaffService.getById(userOrgid.equals("") || userOrgid == null ? orgid : userOrgid));
        return new JsonBean(200, "success", mv);

    }

    /**
     * 规则管理 --列表
     *
     * @return
     * @author SongXiangYing
     * @date 2016年1月15日 下午2:59:54
     */
    @Operation(summary = "rulessolutionmgmt")
    @GetMapping(value = "/jkzx/rulessolutionmgmt")
    public JsonBean rulessolutionmgmt(@Parameter(description="orgid") @RequestParam("orgid") String orgid,
                                      @Parameter(description="页码") @RequestParam("page") Integer page,
                                      @Parameter(description="每页多少条") @RequestParam("limit") Integer limit) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        IPage<MonitorSolution> ip = new Page<>(page, limit);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("orgid", orgid);
        iMonitorSolutionService.page(ip, qw);
        return new JsonBean(200, "success", ip);

    }

    /**
     * 返回全部MonitorSolution 列表里面的全部
     *
     * @param page  页码
     * @param limit 多少条
     * @return
     */
    @Operation(summary = "rulessolutionmgmt2")
    @GetMapping(value = "/jkzx/rulessolutionmgmt2")
    public JsonBean rulessolutionmgmt2(@Parameter(description="页码") @RequestParam("page") Integer page,
                                       @Parameter(description="每页多少条") @RequestParam("orgid") Integer limit) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        IPage ip = new Page(page, limit);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("solutionid", new BigDecimal("0"));
        return new JsonBean(200, "success", iMonitorSolutionService.page(ip, qw));
    }

    /**
     * 返回全部MonitorSolution 列表里面的全部
     *
     * @param page  页码
     * @param limit 多少条
     * @return
     */
    @Operation(summary = "modelssolutionmgmt2")
    @GetMapping(value = "/jkzx/modelssolutionmgmt2")
    public JsonBean modelssolutionmgmt2(@Parameter(description="页码") @RequestParam("page") Integer page,
                                        @Parameter(description="每页多少条") @RequestParam("orgid") Integer limit) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        IPage ip = new Page(page, limit);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("solutionid", new BigDecimal("0"));
        return new JsonBean(200, "success", iMonitorModelsolutionService.page(ip, qw));

    }

    /**
     * 监控执行保存 总控制方法 ，用来根据条件处理转方法
     *
     * @return
     */
    @Operation(summary = "监控执行保存")
    @PostMapping(value = "/jkzx/save")
    public JsonBean jkzxsave(@Parameter(description="目标") @RequestParam("dest") String dest,
                             @Parameter(description="路径") @RequestParam("path") String path,
                             @Parameter(description="id") @RequestParam(value = "worksheetid", required = false) String worksheetid,
                             @Parameter(description="orgid") @RequestParam(value = "orgid", required = false) String orgid
    ) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        String name = FxglUtil.getTimeString() + ".xls";
        String file = path + "/" + name;
        String rFile = "/data/excel/" + name;
        int attid = 91500;
        Attachment a = iAttachmentService.getById(attid);
        Map<String, Object> mv = new HashMap<>();
        if (dest != null) {
            if (dest.equals("dg")) {
                mv.put("url", "nbkz/nkgj/pmp_audit_project_03");
            } else if (dest.equals("dgfj")) {
                Worksheet work = iWorksheetService.getById("worksheetid");
                mv.put("url", "nbkz/nkgj/worksheet_view");
                mv.put("worksheet", work);
            } else if (dest.equals("yd")) {
                mv.put("url", "nbkz/nkgj/dp_new");
            } else if (dest.equals("qx")) {
                QueryWrapper qw = new QueryWrapper();
                qw.eq("orgid", orgid);
                List<BugCriterion> list1 = iBugCriterionService.list(qw);
                mv.put("url", "nbkz/qxwt/defect_add");
                mv.put("list", list1);
            } else if (dest.equals("wt")) {
                mv.put("url", "nbkz/qxwt/question_add");
            }
        }
        mv.put("a", a);
        return new JsonBean(200, "success", mv);
    }

    /**
     * 查找全部的worksheet
     * @param page 页码
     * @return
     */
    @Operation(summary = "监控执行xzdgh")
    @RequestMapping(value = "/jkzx/xzdgh")
    public JsonBean jkzxxzj(@Parameter(description="页码") @RequestParam("page") String page) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List<Worksheet> list = iWorksheetService.list();
        Map<String, Object> mv = new HashMap<>();
        mv.put("pager", list);
        mv.put("page", page);
        return new JsonBean(200, "success", mv);
    }


    /**
     * Modify the exe interval
     *
     * @return
     */
    @Operation(summary = "监控执行setexeinterval")
    @GetMapping(value = "/jkzx/setexeinterval")
    public JsonBean setexeinterval(@Parameter(description="编号") @RequestParam("selectedid") String id) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List<MonitorExeinterval> ins = null;
        if (id != null) {
            try {
                ins = iMonitorExeintervalService.listByIds(Arrays.asList(id));
            } catch (Exception e) {
            }
        }

        Map<String, Object> mv = new HashMap<>();
        mv.put("id", id);
        if (ins != null && ins.size() > 0) {
            String interval = ins.get(0).getExeinterval();
            mv.put("interval", interval);
        }
        return new JsonBean(200, "success", mv);
    }


    /**
     * 设置规则执行---粒度跳转页面
     *
     * @return
     */
    @Operation(summary = "setruleexeinterval")
    @GetMapping(value = "/jkzx/setruleexeinterval")
    public JsonBean setruleexeinterval(@Parameter(description="页码") @RequestParam("page") Integer page,
                                       @Parameter(description="每页多少条") @RequestParam("orgid") Integer limit,
                                       @Parameter(description="Solutionid") @RequestParam("selectedid") String selectedid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        IPage ip = new Page(page, limit);
        iMonitorRuleService.findRuleBySolut(ip, selectedid);
        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", ip);
        mv.put("selectedid", selectedid);
        return new JsonBean(200, "success", mv);
    }

    /**
     * 获取全部MonitorSolution 列表
     * @return
     */
    @Operation(summary = "获取全部MonitorSolution 列表")
    @GetMapping(value = "/jkzx/setmodelexeinterval")
    public JsonBean setmodelexeinterval() throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List list = iMonitorSolutionService.list();
        return new JsonBean(200, "success", list);
    }

    /**
     * modelsetexeinterval
     *
     * @param id solutionid
     * @return
     */
    @Operation(summary = "jkzx/modelsetexeinterval")
    @GetMapping(value = "/jkzx/modelsetexeinterval")
    public JsonBean modelsetexeinterval(@Parameter(description="solutionid") @RequestParam("selectedid") String id) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List<MonitorExeinterval> ins = null;
        if (id != null) {
            QueryWrapper qw = new QueryWrapper();
            qw.eq("solutionid", id);
            ins = iMonitorModelsolutionService.list(qw);
        }
        Map<String, Object> mv = new HashMap<>(4);
        mv.put("id", id);
        if (ins != null && ins.size() > 0) {
            String interval = ins.get(0).getExeinterval();
            mv.put("interval", interval);
        }
        return new JsonBean(200, "success", mv);
    }

    /**
     * 保存规则粒度
     *
     * @return
     */
    @Operation(summary = "保存规则粒度")
    @GetMapping(value = "/jkzx/ruleexeinterval_addorupdate")
    public JsonBean ruleexeinterval_add(@Parameter(description="solutionid") @RequestParam("selectedid") String id,
                                        @Parameter(description="exeinterval") @RequestParam("exeinterval") String interval) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List<MonitorExeinterval> ins = null;
        QueryWrapper qw = new QueryWrapper();
        if (id != null) {
            qw.eq("solutionid", id);
            ins = iMonitorExeintervalService.list(qw);
            if (ins == null || ins.size() < 1) {
                MonitorExeinterval o = new MonitorExeinterval();
                o.setModelsolutionid(new BigDecimal(id));
                o.setExeinterval(interval);
                iMonitorExeintervalService.save(o);
            } else {
                ins.get(0).setModelsolutionid(new BigDecimal(id));
                ins.get(0).setExeinterval(interval);
                iMonitorExeintervalService.updateById(ins.get(0));
            }
        }
        return new JsonBean(200, "success", Collections.emptyList());
    }


    /**
     * 保存模型粒度
     */
    @Operation(summary = "保存模型粒度")
    @PostMapping(value = "/jkzx/modelexeinterval_addorupdate")
    public JsonBean modelexeinterval_addorupdate(@Parameter(description="modelid") @RequestParam("modelid") String id,
                                                 @Parameter(description="exeinterval") @RequestParam("exeinterval") String interval
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        QueryWrapper qw = new QueryWrapper();
        if (id != null) {
            qw.eq("solutionid", id);
            List<MonitorExeinterval> ins = iMonitorExeintervalService.list(qw);
            if (ins == null || ins.size() < 1) {
                MonitorExeinterval o = new MonitorExeinterval();
                o.setModelsolutionid(new BigDecimal(id));
                o.setExeinterval(interval);
                iMonitorExeintervalService.save(o);
            } else {
                ins.get(0).setModelsolutionid(new BigDecimal(id));
                ins.get(0).setExeinterval(interval);
                iMonitorExeintervalService.updateById(ins.get(0));
            }

        }
        return new JsonBean(200, "success", Collections.emptyList());
    }

    /**
     * 删除规则操作
     *
     * @return
     */
    @Operation(summary = "删除规则操作")
    @DeleteMapping(value = "/jkzx/ruleexeinterval_del")
    public JsonBean ruleexeinterval_del(@Parameter(description="modelid") @RequestParam("selectedid") String id) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List<MonitorExeinterval> ins = null;
        if (id != null) {
            QueryWrapper qw = new QueryWrapper();
            qw.eq("solutionid", id);
            ins = iMonitorExeintervalService.list(qw);
            if (ins != null) {
                iMonitorExeintervalService.removeById(ins.get(0).getExeintervalid().toString());
            }
        }
        return new JsonBean(200, "success", Collections.emptyList());
    }

    /**
     * 删除模型操作
     *
     * @return
     */
    @Operation(summary = "删除模型操作")
    @DeleteMapping(value = "/jkzx/modelexeinterval_del")
    public JsonBean modelexeinterval_del(@Parameter(description="modelid") @RequestParam("selectedid") String id) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (id != null) {
            QueryWrapper qw = new QueryWrapper();
            qw.eq("solutionid", id);
            List<MonitorExeinterval> ins = iMonitorExeintervalService.list(qw);
            if (ins != null) {
                iMonitorExeintervalService.removeById(ins.get(0).getExeintervalid().toString());
            }
        }
        return new JsonBean(200, "success", Collections.emptyList());
    }

}
