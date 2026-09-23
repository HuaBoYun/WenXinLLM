package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Accbook;
import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.entity.BugCriterion;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.MonitorIndicatorresult;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.MonitorSolutionIndicator;
import com.huabo.cybermonitor.entity.Worksheet;
import com.huabo.cybermonitor.service.IAccbookService;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBugCriterionService;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.huabo.cybermonitor.service.IMonitorExeintervalService;
import com.huabo.cybermonitor.service.IMonitorSolutionIndicatorService;
import com.huabo.cybermonitor.service.IMonitorSolutionModelService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionStaffService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.IWorksheetService;
import com.huabo.cybermonitor.service.base.CJobTaskService;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.task.base.ScheduleJob;
import com.huabo.cybermonitor.util.ConstClass;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;




@RestController
@Slf4j
@Tag(name="指标监控-指标预警",description="指标监控-指标预警")
@RequestMapping(value = "/cyber/ZbyjController")
@SuppressWarnings("all")
public class ZbyjController {

	private static final Logger log = LoggerFactory.getLogger(ZbyjController.class);

    @Autowired
    IMonitorSolutionStaffService iMonitorSolutionStaffService;

    @Autowired
    IMonitorSolutionModelService iMonitorSolutionModelService;

    @Autowired
    IIndicatorService iIndicatorService;

    @Autowired
    IMonitorExeintervalService iMonitorExeintervalService;

    @Autowired
    IMonitorSolutionRuleService iMonitorSolutionRuleService;

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    IStaffService iStaffService;

    @Autowired
    IAttachmentService iAttachmentService;

    @Autowired
    IWorksheetService iWorksheetService;

    @Autowired
    IBugCriterionService iBugCriterionService;

    @Autowired
    IMonitorSolutionIndicatorService iMonitorSolutionIndicatorService;

    @Resource
    private UserProvider userProvider;
    
    /**
     * 指标预警-左边tree
     *
     * @param
     * @return
     */
    @Operation(summary = "monitor_left")
    @GetMapping(value = "/zbjk/monitor_left")
    public JsonBean monitor_left(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            String Orgid = staff.getCurrentOrg().getOrgid().toString(); //当前用户选择的组织hbOrgEntityOrgid
            Map map = new HashMap(2);
            map.put("targetFrame", "mainFramex");
            map.put("orgid", Orgid);
            return new JsonBean(200, "成功", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "failure", "没有拿到token");
    }


    /**
     * 指标预警-列表
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_monitor_infoZbjk")
    @GetMapping(value = "/zbjk/kri_monitor_info")
    public JsonBean kri_monitor_infoZbjk(@Parameter(name = "solutioncode", description = "solutioncode") @RequestParam(value = "solutioncode", required = false) String solutioncode,
                                         @Parameter(name = "solutionname", description = "solutionname") @RequestParam(value = "solutionname", required = false) String solutionname,
                                         @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
                                         @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        System.out.println("orgid----" + orgId);
        TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        TblOrganizationUtil attribute = null;
        TblOrganizationUtil attribute1 = null;
            //选则的机构
            attribute = staff.getCurrentOrg();
            //当前用户的机构
            attribute1 = staff.getLinkOrg();

        Map map = new HashMap();
        Boolean isSelect = false;
        if (attribute.getOrgid().equals(attribute1.getOrgid())) {
            if (null == orgId) {
                //Organization byId = iOrganizationService.getById(attribute1.getOrgid());
                orgId = staff.getLinkDetp().getOrgid();
            }
            IPage<MonitorSolution> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
            QueryWrapper<MonitorSolution> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", 2);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(solutioncode)) {
                queryWrapper.like("SOLUTIONCODE", solutioncode);
            }
            if (StringUtils.isNotBlank(solutionname)) {
                queryWrapper.like("SOLUTIONNAME", solutionname);
            }
            queryWrapper.orderByDesc("createdate");
            map.put("pageBean", iMonitorSolutionService.page(iPage, queryWrapper));
        } else {
            if (null == orgId) {
                orgId = attribute.getOrgid();
            }
            IPage<MonitorSolution> iPage = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
            QueryWrapper<MonitorSolution> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", 2);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(solutioncode)) {
                queryWrapper.like("SOLUTIONCODE", solutioncode);
            }
            if (StringUtils.isNotBlank(solutionname)) {
                queryWrapper.like("SOLUTIONNAME", solutionname);
            }
            queryWrapper.orderByDesc("createdate");
            map.put("pageBean", iMonitorSolutionService.page(iPage, queryWrapper));

        }
        map.put("orgId", orgId);

        map.put("tblStaff", staff);
        map.put("isSelect", isSelect);
        map.put("solutioncode", solutioncode);
        map.put("solutionname", solutionname);

        return new JsonBean(200, "成功", map);
    }

    /**
     * 指标预警-添加跳转
     *
     * @param orgId
     * @param
     * @return
     */
    @Operation(summary = "to_solution_add")
    @GetMapping(value = "/zbjk/to_solution_add")
    public JsonBean to_solution_add(@Parameter(name = "Orgid", description = "Orgid") @RequestParam("Orgid") BigDecimal orgId,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {


    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        Map<String, Object> map = new HashMap<>(2);
            map.put("orgId", orgId);
            map.put("tblStaff", staff);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 指标预警-保存方案
     *
     * @param orgId
     * @param
     * @param
     * @return
     */
    @Operation(summary = "solution_save")
    @GetMapping(value = "/zbjk/solution_save")
    public JsonBean solution_save(@Parameter(name = "Orgid", description = "没有用到，添加时直接取的登陆登陆用户信息") @RequestParam(value = "Orgid", required = false) BigDecimal Orgid,
                                  @Parameter(name = "solutionid", description = "增加时不传,修改时传") @RequestParam(value = "solutionid", required = false) String solutionid,
                                  @Parameter(name = "solutioncode", description = "solutioncode") @RequestParam(value = "solutioncode", required = false) String solutioncode,
                                  @Parameter(name = "solutionname", description = "solutionname") @RequestParam(value = "solutionname", required = false) String solutionname,
                                  @Parameter(name = "solutionstatus", description = "solutionstatus") @RequestParam(value = "solutionstatus", required = false) String solutionstatus,
                                  @Parameter(name = "memo", description = "memo") @RequestParam(value = "memo", required = false) String memo,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorSolution so = new MonitorSolution();
        if (StringUtil.isNotBlank(solutionid)) {
            so.setSolutionid(new BigDecimal(solutionid));
        }
        so.setSolutionstatus(solutionstatus);
        so.setSolutioncode(solutioncode);
        so.setSolutionname(solutionname);
        Map<String, Object> map = new HashMap<>();
        LocalDateTime ldt = LocalDateTime.now();
        so.setCreatedate(ldt);
        so.setRunstatus(new BigDecimal("0"));
        so.setType(new BigDecimal("2"));

        so.setOrgid(staff.getLinkDetp().getOrgid());
        so.setStaffid(staff.getStaffid());

        boolean num = iMonitorSolutionService.saveOrUpdate(so);
        System.out.println(num);
        map.put("orgid", Orgid);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 指标预警-添加指标
     *
     * @param orgId
     * @param
     * @return
     */
    @Operation(summary = "ruleslistSelector_zb")
    @GetMapping(value = "/zbjk/ruleslistSelector")
    public JsonBean ruleslistSelector_zb(@Parameter(name="orgId",description="orgId",required=false) @RequestParam(value = "orgId", required = false) BigDecimal orgId,
                                         @Parameter(name="solutionid",description="solutionid",required=false) @RequestParam(value = "solutionid", required = false) String solutionid,
                                         @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                         @Parameter(name="pageSize",description="pageSize",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap(2);
        IPage<Indicator> a = new Page<Indicator>(pageNumber, pageSize);
        QueryWrapper<Indicator> wrapper = new QueryWrapper<>();
        wrapper.eq("ORGID", orgId);
        map.put("orgid", orgId);
        map.put("tblStaff", staff);
        map.put("solutionid", solutionid);
        map.put("pageBean", iIndicatorService.page(a, wrapper));
        return new JsonBean(200, "成功", map);
    }

    /**
     * 指标预警-保存指标
     *
     * @param
     * @return
     */
    @Operation(summary = "新增")
    @GetMapping(value = "/zbjk/add_indicator")
    public JsonBean add_indicator_so(@Parameter(name = "indicatorid", description = "indicatorid") @RequestParam("indicatorid") String indicatorid,
                                     @Parameter(name = "souceid", description = "souceid") @RequestParam("souceid") String souceid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        if (indicatorid != null && souceid != null) {
            QueryWrapper qw = new QueryWrapper();
            qw.eq("indicatorid", indicatorid);
            qw.eq("solutionid", souceid);

            MonitorSolutionIndicator msi = iMonitorSolutionIndicatorService.getOne(qw);
            if (msi == null) {
                msi = new MonitorSolutionIndicator();
                msi.setIndicatorid(new BigDecimal(indicatorid));
                msi.setSolutionid(new BigDecimal(souceid));
                iMonitorSolutionIndicatorService.save(msi);
            }
        }
//            Indicator tblIndicator = iIndicatorService.getById(indicatorid);
//           MonitorSolution monitorSolution = iMonitorSolutionService.getById(souceid);
//            List<Indicator> list = iMonitorSolutionService.getMonitorSolutionIndicators();
//            boolean fal = true;
//            if (list != null && list.size() > 0) {
//                for (Indicator indicator : list) {
//                    if (indicator.getIndicatorid().toString().equals(tblIndicator.getIndicatorid().toString())) {
//                        fal = false;
//                    }
//                }
//            }
//            if (fal) {
//                iIndicatorService.save(tblIndicator);
//                iMonitorSolutionService.updateById(monitorSolution);
//            }


        return new JsonBean(200, "添加成功", "");

    }

    /**
     * 指标预警-判断修改权限
     *
     * @param
     * @return
     */
    @Operation(summary = "solutionchechUpate_zb")
    @GetMapping(value = "/zbjk/solutioncheckUpdate")
    public JsonBean solutionchechUpate_zb(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                          @Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid") String selectedid) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorSolution rule = this.iMonitorSolutionService.getById(selectedid);
        if (rule.getStaffid().equals(staff.getStaffid())) {
            if (rule.getRunstatus().toString().equals("0")) {
                return new JsonBean(200, "成功", "0");
            } else {
                return new JsonBean(200, "该预警方案已执行，不能删除和修改", "1");
            }
        } else {
            return new JsonBean(200, "权限不足", "0");
        }

    }

    /**
     * 指标预警-删除
     *
     * @param
     * @return
     */
    @Operation(summary = "solutionDel_zn")
    @GetMapping(value = "/zbjk/solution_del")
    @Transactional(rollbackFor = Exception.class)
    public JsonBean solutionDel_zn(@Parameter(name = "selectedid", description = "方案编号") @RequestParam("selectedid") String selectedid,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (selectedid != null) {
            MonitorSolution monitorSolution = this.iMonitorSolutionService.getById(selectedid);
            map.put("orgId", monitorSolution.getOrgid());
            QueryWrapper qw = new QueryWrapper();
            qw.eq("RULESOLUTIONID", selectedid);
            iMonitorExeintervalService.remove(qw);
            QueryWrapper qw1 = new QueryWrapper();
            qw1.eq("SOLUTIONID", selectedid);
            iMonitorSolutionRuleService.remove(qw1);

            QueryWrapper qw2 = new QueryWrapper();
            qw2.eq("SOLUTIONID", selectedid);
            iMonitorSolutionStaffService.remove(qw2);
            iMonitorSolutionModelService.remove(qw2);
            iMonitorSolutionIndicatorService.remove(qw2);
            iMonitorSolutionService.removeById(selectedid);


        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 指标预警-删除指标
     *
     * @param
     * @return
     */
    @Operation(summary = "删除")
    @DeleteMapping(value = "/zbjk/del_indicator")
    public JsonBean del_indicator_so(@Parameter(name = "indicatorid", description = "indicatorid") @RequestParam("indicatorid") String indicatorid,
                                     @Parameter(name = "souceid", description = "souceid") @RequestParam("souceid") String souceid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (indicatorid != null && souceid != null) {
            Indicator tblIndicator = iIndicatorService.getById(indicatorid);
            MonitorSolution monitorSolution = iMonitorSolutionService.getById(souceid);
            iIndicatorService.removeById(indicatorid);
            iMonitorSolutionService.updateById(monitorSolution);
        }
        return new JsonBean(200, "成功", "");
    }


    /**
     * 指标预警-跳转修改
     *
     * @param
     * @return
     */
    @Operation(summary = "toSolutionModify_zb")
    @GetMapping(value = "/zbjk/to_solution_modify")
    public JsonBean toSolutionModify_zb(@Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid") String selectedid,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorSolution solution = iMonitorSolutionService.getById(selectedid);
        QueryWrapper qw = new QueryWrapper();
        qw.eq("solutionid", selectedid);
        List<Indicator> set = iMonitorSolutionIndicatorService.list(qw);
        StringBuffer buffer = new StringBuffer();
        Map map = new HashMap();
        if (buffer.toString().length() > 0) {
            map.put("solruleids", buffer.toString().substring(0, buffer.toString().length() - 1));
        }
        if (solution != null) {
            map.put("solution", solution);
            //传递当前是solution的详细信息
            map.put("solutionDetail", set);
            map.put("orgId", solution.getOrgid());
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 指标预警-查看
     *
     * @param
     * @return
     */
    @Operation(summary = "to_solution_disp")
    @GetMapping(value = "/zbjk/to_solution_disp")
    public JsonBean to_solution_disp(@Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid") String selectedid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorSolution solution = iMonitorSolutionService.getById(selectedid);
        if (Objects.isNull(solution)) {
            return new JsonBean(500, selectedid + "没有找到对应方案", null);
        }
        QueryWrapper qw = new QueryWrapper();
        qw.eq("solutionid", selectedid);
        //
        if (solution.getStaffid() != null) {
            solution.setRealname(this.iStaffService.getById(solution.getStaffid()).getRealname());
        }

        List<Indicator> set = iMonitorSolutionIndicatorService.list(qw);
        iStaffService.setRealNameIndicator(set);

        Map map = new HashMap();
        StringBuffer buffer = new StringBuffer();
        if (buffer.toString().length() > 0) {
            map.put("solruleids", buffer.toString().substring(0, buffer.toString().length() - 1));
        }
        if (solution != null) {
            map.put("solution", solution);
            map.put("solutionDetail", set);
            map.put("orgId", solution.getOrgid());
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 指标保存 功能
     *
     * @param worksheetid
     * @param Orgid
     * @param dest
     * @param path
     * @return
     */
    @Operation(summary = "fxsbsave")
    @PostMapping(value = "/zbjk/save")
    public JsonBean fxsbsave(@Parameter(name = "worksheetid", description = "worksheetid") @RequestParam(value = "worksheetid", required = false) String worksheetid,
                             @Parameter(name = "Orgid", description = "Orgid") @RequestParam(value = "Orgid", required = false) String Orgid,
                             @Parameter(name = "dest", description = "dest") @RequestParam(value = "dest", required = false) String dest,
                             @Parameter(name = "path", description = "path") @RequestParam(value = "path", required = false) String path,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        String name = LocalDateTime.now().toString() + ".xls";
        String file = path + "/" + name;
        String rFile = "/data/excel/" + name;
        int attid = 91500;
        Attachment a = iAttachmentService.getById("" + attid);
        Map map = new HashMap();
        if (dest != null) {
            if (dest.equals("dg")) {
            } else if (dest.equals("dgfj")) {
                Worksheet work = iWorksheetService.getById(worksheetid);
                map.put("worksheet", work);
            } else if (dest.equals("yd")) {
            } else if (dest.equals("qx")) {
                //选则的机构
                QueryWrapper qw = new QueryWrapper();
                qw.eq("Orgid", Orgid);
                List<BugCriterion> list1 = iBugCriterionService.list(qw);
                map.put("list", list1);
            } else if (dest.equals("wt")) {
            }
        }
        map.put("a", a);
        return new JsonBean(200, "成功", map);
    }

    @Autowired
    IAccbookService iAccbookService;

    @Autowired
    IIndicatorService indicatorService;

    @Autowired
    CJobTaskService cJobTaskService;

    @Autowired
    JobTaskService jobTaskService;

    /**
     * 指标预警 --执行
     *
     * @return
     */
    @Operation(summary = "monitorYJExecute")
    @GetMapping(value = "/gzjk/monitor/monitoryjExecute")
    public JsonBean monitorYJExecute(
            @Parameter(name = "id", description = "指标主键") @RequestParam(value = "id") String id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) {


        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}

            System.out.println(staff);
            BigDecimal staffid = staff.getStaffid();
            BigDecimal orgId = staff.getCurrentOrg().getOrgid();
            System.out.println(staffid);
            System.out.println(orgId);
            Accbook accBook = iAccbookService.findUserById(staffid, orgId);
            if (accBook == null) {
                return new JsonBean(500, id + "没有在accbook表里找到相关项", "0");
            }

            String book = accBook.getAcctid();
            String bookid = accBook.getBookid();
            String bookyear = accBook.getBookyear();

            MonitorSolution monitorSolution = iMonitorSolutionService.getById(id);
            if (monitorSolution == null) {
                return new JsonBean(500, id + "没有在MonitorSolution表里找到相关项", "0");
            }


            //查询指标表
            List<Indicator> tblMonitorSolutionIndicators = indicatorService.showAllWithYj(id);
            if (tblMonitorSolutionIndicators == null) {
                return new JsonBean(500, id + "没有在Indicator表里找到相关项", "0");
            }
            if (tblMonitorSolutionIndicators.size() > 0) {
                if (monitorSolution.getSolutionstatus().equals("启用")) {
                    if (monitorSolution.getRunstatus().toString().equals("1")) {
                        return new JsonBean(200, "执行中，请稍等！", null);
                    } else {
                        List<MonitorSolution> list = new ArrayList<>();
                        Set<Indicator> m = new HashSet<>();
                        for (Indicator tblIndicator : tblMonitorSolutionIndicators) {
                            tblIndicator.setConnectionstrings(accBook == null ? null : accBook.getBookid());
                            m.add(tblIndicator);
                        }
                        monitorSolution.setTblMonitorSolutionIndicators(m);
                        list.add(monitorSolution);
                        ScheduleJob job = null;
                        try {
                            job = cJobTaskService.zbyjTaskExec(list, iStaffService.getById(staffid), "", MonitorIndicatorresult.ZJ);
                        } catch (SchedulerException e) {
                            e.printStackTrace();
                            try {
                                jobTaskService.changeStatus(job, "stop");
                            } catch (SchedulerException e1) {
                                e1.printStackTrace();
                            }
                        }
                        return new JsonBean(200, "成功", null);
                    }
                }
                return new JsonBean(500, "请先启用", null);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(500, "无效的令牌", null);
    }
}