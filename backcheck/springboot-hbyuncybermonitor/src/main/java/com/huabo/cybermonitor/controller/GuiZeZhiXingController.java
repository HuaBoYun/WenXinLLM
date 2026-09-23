package com.huabo.cybermonitor.controller;

import static org.apache.http.util.TextUtils.isEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.MonitorModel;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.MonitorSolutionRule;
import com.huabo.cybermonitor.entity.MonitorSolutionStaff;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.huabo.cybermonitor.entity.MonitorSolutionresultVo;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.mapper.YhrPageMapper;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBugCriterionService;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.huabo.cybermonitor.service.IMonitorExeintervalService;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorModelsolutionService;
import com.huabo.cybermonitor.service.IMonitorRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionStaffService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;





@RestController
@Slf4j
@Tag(name="监控执行-规则执行",description="监控执行-规则执行")
@RequestMapping(value = "/cyber/GuiZeZhiXingController")
@CrossOrigin
/*
   预警结果没有 (规则执行列表 点击接口)
   用户列表没有 (规则执行列表 点击推送)
 */
public class GuiZeZhiXingController {

    private static final Logger logger = LoggerFactory.getLogger(GuiZeZhiXingController.class);

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
    YhrPageMapper  pageMapper;

    @Autowired
    IMonitorSolutionStaffService iMonitorSolutionStaffService;

    @Autowired
    IMonitorSolutionresultService iMonitorSolutionresultService;
    
    @Resource
    private UserProvider userProvider;
    
    /**
     * 监控执行：规则执行 --index
     *
     * @param
     */

    @Operation(summary = "rulesolution_index")
    @PostMapping(value = "/jkzx/rulesolution_index")
    public JsonBean rulesolution_index(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        //获取token
        TblStaffUtil staff = null;
        try {

        	staff = userProvider.get();
			if (staff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}

            logger.info(staff.toString());
            logger.info(staff.getLinkDetp().getOrgid()+"--"+staff.getCurrentOrg().getOrgid());
            //获取当前用户信息
            BigDecimal orgId= staff.getLinkDetp().getOrgid(); //当前用户id
            Map<String, Object> map = new HashMap<>(1);

            map.put("orgid", orgId);

            return new JsonBean(200, "success", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200,"failure","没有拿到token");
    }
//    /**
//     * 监控执行：规则预警---左侧树形
//     *
//     * @param orgid 通过session获得orgid，分布式修改以后没有session，要么考虑前台传递要么考虑redis获取，此处直接前台传递
//     * @return
//     */
    @Operation(summary = "jkzx_solutionleft")
    @GetMapping(value = "/jkzx/solutionleft")
    public JsonBean jkzx_solutionleft(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

        TblStaffUtil staff = null;
        try {
        	staff = userProvider.get();
			if (staff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            BigDecimal orgId= staff.getCurrentOrg().getOrgid(); //当前用户选择组织id
            Map<String, Object> map = new HashMap<>(1);

            map.put("orgid", orgId);
            map.put("userOrgId", orgId);
            return new JsonBean(200, "success", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200,"failure","没有拿到token");

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
                                                   @Parameter(name = "orgId", description = "orgId") @RequestParam(name = "orgId", value = "orgId") BigDecimal orgId,
                                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

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
     * 指标管理-修改权限判断
     *
     * @return
     */
    @Operation(summary = "监控执行：规则执行/指标执行/-推送-用户列表选定按钮")
    @Transactional
    @PostMapping(value = "/jkzx/saveuser", produces = "application/json; charset=utf-8")
    public JsonBean saveUser(@Parameter(name = "solutionid", description = "solutionid") @RequestParam(name = "solutionid", value = "solutionid") String solutionid
            , @Parameter(name = "userid", description = "userid") @RequestParam(name = "userid", value = "userid") String userid,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token                ) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (userid != null && !userid.equals("") && solutionid != null && !solutionid.equals("")) {
            //MonitorSolution solution = iMonitorSolutionService.getById(solutionid); //没用了

            QueryWrapper qw = new QueryWrapper();
            qw.eq("solutionid", solutionid);
            qw.eq("staffid",userid);



//            Staff user = iStaffService.getById(userid);
//            List<Staff> staffs = iStaffService.list(qw);
//            boolean falg = true;
//            if (staffs != null && staffs.size() > 0) {
//                for (Staff tblStaff : staffs) {
//                    if (tblStaff.getStaffid().toString().equals(userid)) {
//                        falg = false;
//                    }
//                }
//            }
//            if (falg) {
//                iStaffService.save(user);
//            }
            MonitorSolutionStaff solutionStaff = iMonitorSolutionStaffService.getOne(qw);
            if(solutionStaff==null){
                solutionStaff=new MonitorSolutionStaff();
                solutionStaff.setSolutionid(new BigDecimal(solutionid));
                solutionStaff.setStaffid(new BigDecimal(userid));
                iMonitorSolutionStaffService.save(solutionStaff);
            }
            //iMonitorSolutionService.updateById(solution); //没用了
            return new JsonBean(200, "success", true);
        } else {
            return new JsonBean(200, "操作失败", false);
        }
    }





    /**
     * 监控执行：预警方案-选择粒度
     *
     *
     */
    @Operation(summary = "执行列表-执行粒度下拉框选择 -通过type选择预警方案")
    @PutMapping(value = "/jkzx/update_monitorSolution")
    public String update_monitorSolution(
                                         @Parameter(name = "staffid", description = "staffid") @RequestParam(name = "staffid", value = "staffid") String staffid,
                                         @Parameter(name = "exe", description = "exe") @RequestParam(name = "exe", value = "exe") String exe,
                                         @Parameter(name = "solutionid", description = "solutionid") @RequestParam(name = "solutionid", value = "solutionid") String solutionid,
       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return JsonBean.error("用户已失效");
		}
        if (solutionid != null && !"".equals(solutionid)) {
            MonitorSolution monitorSolution = iMonitorSolutionService.getById(solutionid);

            Staff tblStaff = iStaffService.getById(staffid);

            /**
             * 修改粒度
             */
            UpdateWrapper<MonitorSolution> queryWrapper = new UpdateWrapper<>();
            queryWrapper.set("EXEFREQUNCY",exe);
            queryWrapper.eq("SOLUTIONID",solutionid);
            iMonitorSolutionService.update(queryWrapper);

            List<MonitorSolution> monitorSolutions = new ArrayList<MonitorSolution>();
            if (monitorSolution.getStaffid().equals(tblStaff.getStaffid())) {
                try {
                    QueryWrapper qw = new QueryWrapper();
                    qw.eq("SOLUTIONID", solutionid);
                    if (monitorSolution.getType().equals(1)) {
                        // 规则
                        logger.info("添加规则预警");

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
                            logger.info("添加指标预警");
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
                        logger.info("添加模型预警");
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
     * 监控执行-推送我的预警列表
     *
     * @param
     * @return
     */

    @Operation(summary = "监控执行-点击推送获取预警列表")
    @PostMapping(value = "/user/list")
    public JsonBean userListss(  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               //@Parameter(name = "type", description = "type") @RequestParam(value = "type", required = false) String type,
                               @Parameter(name = "solutionid", description = "solutionid") @RequestParam(value = "solutionid", required = false) String solutionid,
                                 @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid", required = false)String orgid,
                               @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber
                                 ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (pageNumber == null) {
            pageNumber = 1;
        }
        try {
            if(StringUtils.isBlank(orgid)){
                orgid=staff.getCurrentOrg().getOrgid().toString();
            }

            Organization organization=iOrganizationService.getById(orgid);
            // 分页查询
            IPage ip = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
            iStaffService.findAllPageBeanPid(ip,organization);
            //pageBean = userService.findAllPageBeanPid(Integer.parseInt(pageNumber), pageBean.getPageSize(), organization);

            Map<String, Object> map = new HashMap<>(4);

            map.put("orgid", orgid);
           // map.put("solutionid", solutionid);
            map.put("pageBean", ip);
            return new JsonBean(200, "success", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200,"failure","");
    }

    /**
     * 监控执行：规则预警 --列表
     *
     * @param orgId      组织编号
     * @param pageNumber 当前页
     * @return
     * @date 2016年1月14日 下午2:56:54
     */
    @Operation(summary = "监控执行：规则执行 --右侧执行列表分页查询")
    @GetMapping(value = "/jkzx/solutionmgmt")
    public JsonBean jkzx_solutionMgmt(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid", required = false) BigDecimal orgId,
                                      @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        IPage ip = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
        try {


            orgId = Objects.isNull(orgId)?staff.getLinkDetp().getOrgid() : orgId;
            logger.info(staff.getLinkDetp().getOrgid().toString()+"-------"+orgId);
            Boolean isSelect = treeService.isSJByOrgId(staff.getLinkDetp().getOrgid().toString(),
                    orgId.toString());

            StringBuilder sb=new  StringBuilder();

            sb.append("select t.*,REALNAME,USERNAME from TBL_MONITOR_SOLUTION  t left join TBL_STAFF on t.STAFFID = TBL_STAFF.STAFFID ")
                    .append(" where t.type=1 and t.SOLUTIONSTATUS='启用' ");
            if (isSelect) {
                sb.append(" and t.orgid="+orgId);

            } else {
                sb.append(" and t.orgid="+ staff.getLinkDetp().getOrgid());

            }
            sb.append(" order by t.createdate desc");

            IPage<Map<String,Object>>  page =new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
            page=pageMapper.getPage(page,sb.toString());

            Map<String, Object> map = new HashMap<>(3);
            map.put("orgId", orgId);
            map.put("pageBean", page);
            map.put("isAdd", isSelect);
            return new JsonBean(200, "success", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200,"failure","");
    }


    /**
     * 查看规则预警执行结果
     */
    @Operation(summary = "resultmgmtyj")
    @GetMapping(value = "/result/resultmgmtyj")
    public JsonBean resultmgmtyj(@Parameter(name = "pageNumber", description = "1") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                                 @Parameter(name = "pageSize", description = "20") @RequestParam(value = "pageSize", name = "pageSize", defaultValue = "15") Integer pageSize,
                                 @Parameter(name="source",description="source",required=false) @RequestParam(value = "source", required = false) String source,
                                 @Parameter(name="soultionId",description="soultionId") @RequestParam(value = "soultionId") String soultionId,
                                 @Parameter(name="orgId",description="orgId") @RequestParam(value = "orgId") String orgId,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

        IPage<MonitorSolutionresultVo> ip = new Page<>(pageNumber, pageSize);
        String sql="select tms.*,ts.REALNAME,ts.USERNAME from TBL_MONITOR_SOLUTIONRESULT tms inner join TBL_STAFF TS on tms.STAFFID = TS.STAFFID\n" +
                "where SOLUTIONID="+soultionId+" ";

        if (!isEmpty(soultionId)) {
            if (StringUtils.isNotBlank(source)) {
                if (source.equals("3")) {
                    sql+=" and source="+MonitorSolutionresult.ZKZX;

                } else {
                    sql+=" and rownum=1 and  source="+MonitorSolutionresult.YJYJ;

                }
            } else {
                sql+=" and  source="+MonitorSolutionresult.ZX;

            }
            sql+= " order by saveTime desc ";
        }
        this.iMonitorSolutionresultService.getSolutionresultVoPage(ip,sql);
        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("orgId", orgId);
        mv.put("soultionId", soultionId);
        mv.put("source", source);
        mv.put("pageBean",ip);


        return new JsonBean(200, "成功", mv);
    }

}
