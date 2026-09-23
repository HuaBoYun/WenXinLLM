package com.huabo.cybermonitor.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Accbook;
import com.huabo.cybermonitor.entity.MonitorPrewarning;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.MonitorSolutionRule;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.service.IAccbookService;
import com.huabo.cybermonitor.service.IMonitorPrewarningService;
import com.huabo.cybermonitor.service.IMonitorRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.IWarningResultService;
import com.huabo.cybermonitor.service.TreeService;
import com.huabo.cybermonitor.service.base.CJobTaskService;
import com.huabo.cybermonitor.service.base.DepartmentUtils;
import com.huabo.cybermonitor.task.TaskConstant;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.task.base.ScheduleJob;
import com.huabo.cybermonitor.util.DynamicDataSource;
import com.huabo.cybermonitor.util.ExcelUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
@Tag(name="规则监控-规则管理",description="规则监控-规则管理")
@RequestMapping(value = "/cyber/GzglController")
@SuppressWarnings("all")
public class GzglController {
    private static final Logger logger = LoggerFactory.getLogger(GzglController.class);

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorRuleService iMonitorRuleService;

    @Autowired
    IAccbookService iAccbookService;

     @Autowired
    IStaffService iStaffService;

    @Autowired
    IMonitorSolutionRuleService iMonitorSolutionRuleService;

    @Autowired
    IMonitorPrewarningService iMonitorPrewarningService;

    @Autowired
    IWarningResultService iWarningResultService;

    @Autowired
    CJobTaskService cJobTaskService;

    @Autowired
    JobTaskService jobTaskService;

    @Autowired
    TreeService treeService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 规则管理 --左侧菜单
     *
     * @param
     * @return
     */

    @Operation(summary = "userLeft")
    @GetMapping(value = "/gzjk/rule/left")
    public JsonBean userLeft(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap<>();
        Organization byId = iOrganizationService.getById(staff.getLinkDetp().getOrgid());
        map.put("userOrgId", byId.getOrgid());
        map.put("orgid", staff.getCurrentOrg().getOrgid());
        return new JsonBean(200, "成功", map);
    }


    /**
     * 规则管理 --列表
     *
     * @param
     * @return
     */
    @Operation(summary = "rulesMgmt")
    @GetMapping(value = "/gzjk/rule/rulesmgmt")
    public JsonBean rulesMgmt(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber") Integer pageNumber,
                              @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize") Integer pageSize,
                              @Parameter(name = "rulecode", description = "rulecode") @RequestParam(value = "rulecode",required = false) String rulecode,
                              @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId",required = false) BigDecimal orgId,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

        Map map = new HashMap();
        Boolean isSelect = false;
        boolean isDU = false;
        if (staff.getCurrentOrg().getOrgid().equals(staff.getLinkOrg().getOrgid())) {
            if (null == orgId) {
                //Organization byId = iOrganizationService.getById(staff.getOrgid());
                orgId = staff.getLinkDetp().getOrgid();
            }
            IPage<MonitorRule> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inruledb", MonitorRule.IS_HY0);
            queryWrapper.eq("ORGID", orgId);
//            if (StringUtils.isNotBlank(rulecode)) {
//                queryWrapper.like("rulecode", "%" + rulecode + "%");
//            }
            queryWrapper.orderByDesc("ruleid");

            map.put("pageBean", iMonitorRuleService.page(iPage, queryWrapper));

        } else {
            if (null == orgId) {
                orgId = staff.getCurrentOrg().getOrgid();
            }
            IPage<MonitorRule> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inruledb", MonitorRule.IS_HY0);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(rulecode)) {
                queryWrapper.like("rulecode", "%" + rulecode + "%");
            }
            queryWrapper.orderByDesc("ruleid");

            map.put("pageBean", iMonitorRuleService.page(iPage, queryWrapper));
        }
        map.put("orgId", orgId);
        map.put("isAdd", isSelect);
        map.put("isDU", isDU);
        map.put("rulecode", rulecode);
        return new JsonBean(200, "成功", map);
    }


    //用到了redis   http://www.huabao.example.com/redisorg/findOrganizationByTreeAllbm?nodeId=116821


    /**
     * 规则管理 --添加
     *
     * @param
     * @return
     */
    @Operation(summary = "toRuleAdd")
    @GetMapping(value = "/gzjk/rule/to_rule_add")
    public JsonBean toRuleAdd(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        //Organization byId = iOrganizationService.getById(staff.getLinkOrg().getOrgid());
        BigDecimal orgId = staff.getLinkDetp().getOrgid();
        map.put("orgId", orgId);

        List<Accbook> bookList = iAccbookService.findBookIdByUserAll(staff.getStaffid(), staff.getCurrentOrg().getOrgid().toString());
        List<Accbook> books = DepartmentUtils.getAllDepartmentss(bookList);

        map.put("books", books);
        map.put("bookList", bookList);
        Organization organization = iOrganizationService.getById(orgId);
        if (null != organization) {
            map.put("orgName", organization.getOrgname());
        }

        return new JsonBean(200, "成功", map);
    }


    /**
     * 规则监控 --保存操作
     *
     * @param
     * @return
     */
    @Operation(summary = "ruleAdd")
    @PostMapping(value = "/gzjk/rule/rule_add")
    public JsonBean ruleAdd(@Parameter(name = "rulename", description = "rulename") @RequestParam(value = "rulename",required = false) String rulename,
                            @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid",required = false) String orgid,
                            @Parameter(name = "ruledescription", description = "ruledescription") @RequestParam(value = "ruledescription",required = false) String ruledescription,
                            @Parameter(name = "rulecode", description = "rulecode") @RequestParam(value = "rulecode",required = false) String rulecode,
                            @Parameter(name = "risklevel", description = "risklevel") @RequestParam(value = "risklevel",required = false) String risklevel,
                            @Parameter(name = "rulepriority", description = "rulepriority") @RequestParam(value = "rulepriority",required = false) String rulepriority,
                            @Parameter(name = "satus", description = "satus") @RequestParam(value = "satus",required = false) String satus,
                            @Parameter(name = "unit", description = "unit") @RequestParam(value = "unit",required = false) String unit,

                            @Parameter(name = "rulesql", description = "rulesql") @RequestParam(value = "rulesql",required = false) String rulesql,
                            @Parameter(name = "reportsql", description = "reportsql") @RequestParam(value = "reportsql",required = false) String reportsql,

                            @Parameter(name = "orgname", description = "orgname") @RequestParam(value = "orgname",required = false) String orgname,
                            @Parameter(name = "dataname", description = "dataname") @RequestParam(value = "dataname",required = false) String dataname,
                            
                            
                            @Parameter(name = "connectionstrings", description = "connectionstrings") @RequestParam(value = "connectionstrings",required = false) String connectionstrings,
                            @Parameter(name = "memo", description = "memo") @RequestParam(value = "memo",required = false) String memo,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule rule = new MonitorRule();
        rule.setRuleid(RandomUtil.uuBigDecimalId());
        rule.setRulename(rulename);
        rule.setRulecode(rulecode);
        rule.setRuledescription(ruledescription);
        rule.setRisklevel(risklevel);
        rule.setRulepriority(rulepriority);
        rule.setSatus(satus);
        rule.setOrgname(orgname);
        rule.setDataname(dataname);
//        rule.setExeinterval(exeinterval); 新版数据库缺少字段
//        rule.setUnit(unit);
       // rule.setRegexp(regexp);
        rule.setRulesql(rulesql);
        rule.setReportsql(reportsql);
        rule.setInruledb(MonitorRule.IS_HY0.toString());
        //rule.setTips(tips);
        rule.setConnectionstrings(connectionstrings);
        rule.setMemo(memo);
        rule.setRunstatus(new BigDecimal(0));

        rule.setStaffid(staff.getStaffid());

//        Organization org = new Organization();
//        BigDecimal bgorgid = new BigDecimal(orgid);
//        org.setOrgid(bgorgid);
        rule.setOrgid(new BigDecimal(orgid));
//        iOrganizationService.save(org);
        try {
        	  Serializable serializable = iMonitorRuleService.save(rule);
//            Accbook acctbook = iAccbookService.getById(connectionstrings);
//            System.out.println(acctbook+"==>1");
//            if (Objects.isNull(acctbook) || Objects.isNull(acctbook.getAcctid())) {
//                return new JsonBean(500, connectionstrings + " 没有找到帐簿!!", null);
//            }
//            if (null != acctbook) {
//                //替换当前账套年份
//                System.out.println(acctbook+"==>2");
//                rulesql = rulesql.replace("@", acctbook.getBookyear());
//                rulesql = rulesql.replace("$", acctbook.getAcctid());
//                DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
//                List<String> list = dynamicDataSource.getTbable(rulesql);
//                Serializable serializable = iMonitorRuleService.save(rule);
//                dynamicDataSource.executeSql("ZNJK_GZ_" + rule.getRuleid().toString(), list);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(500, "失败", "SQL有误，请查看之后再保存！");
        }
        return new JsonBean(200, "成功", "/znjk/gzjk/rule/rulesmgmt?orgId=" + orgid);
    }

    /**
     * 验证规则 是否有权限修改和删除
     *
     * @param
     * @return
     * @author SongXiangYing
     * @date 2016年1月19日 下午4:15:20
     */
    @Operation(summary = "chechUpate")
    @GetMapping(value = "/gzjk/rule/checkUpdate")
    public JsonBean chechUpate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam("selectedruleid") String selectedruleid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule rule = iMonitorRuleService.getById(selectedruleid);
        //Staff byId = iStaffService.getById(rule.getStaffid());
        if (rule.getStaffid().equals(staff.getStaffid())) {
            QueryWrapper<MonitorSolutionRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ruleid", rule.getRuleid());
            List<MonitorSolutionRule> list = iMonitorSolutionRuleService.list(queryWrapper);
            if (list.size() == 0) {
                return new JsonBean(200, "成功", 0);
            } else {
                return new JsonBean(500, "规则正在使用,无法操作！", "规则正在使用,无法操作！");
            }
        } else {
            return new JsonBean(204, "权限不足", "权限不足");
        }
    }


    /**
     * 规则管理 ---跳转修改
     *
     * @param
     * @return
     */
    @Operation(summary = "toRuleModify")
    @GetMapping(value = "/gzjk/rule/to_rule_modify")
    public JsonBean toRuleModify(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam("selectedruleid") String selectedruleid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule rule = iMonitorRuleService.getById(selectedruleid);
        Map map = new HashMap();
        //Organization byId = iOrganizationService.getById(staff.getStaffid());
        //BigDecimal orgId = byId.getOrgid();
        BigDecimal orgId = staff.getLinkDetp().getOrgid();
        map.put("orgId", orgId);
        map.put("orgName", staff.getLinkDetp().getOrgname());
        List<Accbook> bookList = iAccbookService.findBookIdByUserAll(staff.getStaffid(), staff.getCurrentOrg().getOrgid().toString());
        List<Accbook> books = DepartmentUtils.getAllDepartmentss(bookList);

        map.put("books", books);
        if (rule != null) {
            map.put("rule", rule);
        }
        return new JsonBean(200, "成功", map);
    }


    /**
     * 执行规则--修改
     *
     * @param
     * @return
     */
    @Transactional
    @Operation(summary = "ruleModify")
    @PostMapping(value = "/gzjk/rule/rule_modify")
    public JsonBean ruleModify(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid") String ruleid,
                               @Parameter(name = "rulename", description = "rulename") @RequestParam(value = "rulename",required = false) String rulename,
                               @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid",required = false) String orgid,
                               @Parameter(name = "ruledescription", description = "ruledescription") @RequestParam(value = "ruledescription",required = false) String ruledescription,
                               @Parameter(name = "rulecode", description = "rulecode") @RequestParam(value = "rulecode",required = false) String rulecode,
                               @Parameter(name = "risklevel", description = "risklevel") @RequestParam(value = "risklevel",required = false) String risklevel,
                               @Parameter(name = "rulepriority", description = "rulepriority") @RequestParam(value = "rulepriority",required = false) String rulepriority,
                               @Parameter(name = "satus", description = "satus") @RequestParam(value = "satus",required = false) String satus,
                               @Parameter(name = "exeinterval", description = "exeinterval") @RequestParam(value = "exeinterval",required = false) String exeinterval,
                               @Parameter(name = "regexp", description = "regexp") @RequestParam(value = "regexp",required = false) String regexp,
                               @Parameter(name = "rulesql", description = "rulesql") @RequestParam(value = "rulesql",required = false) String rulesql,
                               @Parameter(name = "reportsql", description = "reportsql") @RequestParam(value = "reportsql",required = false) String reportsql,
                               @Parameter(name = "inruledb", description = "inruledb") @RequestParam(value = "inruledb",required = false) String inruledb,
                               @Parameter(name = "tips", description = "tips") @RequestParam(value = "tips") String tips,
                               @Parameter(name = "connectionstrings", description = "connectionstrings") @RequestParam(value = "connectionstrings",required = false) String connectionstrings,
                               @Parameter(name = "memo", description = "memo") @RequestParam(value = "memo",required = false) String memo,
                               @Parameter(name = "orgname", description = "orgname") @RequestParam(value = "orgname",required = false) String orgname,
                               @Parameter(name = "dataname", description = "dataname") @RequestParam(value = "dataname",required = false) String dataname,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        /*
         * tyb 修改开始 作用：每次规则点击修改就把以前生成的结果数据删掉
         */
//        QueryWrapper<MonitorPrewarning> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("signId", TaskConstant.ZX + "_%");
//        queryWrapper.eq("ruleid", new BigDecimal(ruleid));
//        queryWrapper.orderByDesc("saveDateTime");
//        List<MonitorPrewarning> mps = iMonitorPrewarningService.list(queryWrapper);
//        for (int i = 0; i < mps.size(); i++) {
//            MonitorPrewarning mp = mps.get(i);
//            iMonitorPrewarningService.removeById(mp.getPrewarningid().toString());
//        }
        /* 修改结束 */

        MonitorRule rule = iMonitorRuleService.getById(ruleid);
        rule.setRulename(rulename);
        rule.setRulecode(rulecode);
        rule.setRuledescription(ruledescription);
        rule.setRisklevel(risklevel);
        rule.setRulepriority(rulepriority);
        rule.setSatus(satus);
        //rule.setExeinterval(exeinterval);缺少字段
        rule.setRegexp(regexp);
        rule.setRulesql(rulesql);
        rule.setReportsql(reportsql);
        rule.setInruledb(MonitorRule.IS_HY0.toString());
        rule.setTips(tips);
        rule.setConnectionstrings(connectionstrings);
        rule.setMemo(memo);
//        rule.setOrgname(orgname);
        rule.setDataname(dataname);
        Map map = new HashMap();
        try {
        	 iMonitorRuleService.updateById(rule);
//            Accbook acctbook = iAccbookService.getById(connectionstrings);
//            if (Objects.isNull(acctbook) || Objects.isNull(acctbook.getAcctid())) {
//                return new JsonBean(500, connectionstrings + " 没有找到帐簿!!", null);
//            }
//            if (null != acctbook) {
//                DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
//                dynamicDataSource.deleteTable("ZNJK_GZ_" + ruleid);
//                rulesql = rulesql.replace("@", acctbook.getBookyear());
//
//                List<String> list = dynamicDataSource.getTbable(rulesql);
//                dynamicDataSource.executeSql("ZNJK_GZ_" + ruleid, list);
//                iMonitorRuleService.updateById(rule);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(500, "sql语句出错", "");
        }
        map.put("orgId", orgid);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 规则管理-启用和禁用修改状态
     *
     * @param
     * @return
     */
    @Operation(summary = "ruleUpdateStatus")
    @GetMapping(value = "/gzjk/rule/ruleUpdateStatus")
    public JsonBean ruleUpdateStatus(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam(value = "selectedruleid") String selectedruleid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule rule = iMonitorRuleService.getById(selectedruleid);
        if (rule.getStaffid().equals(staff.getStaffid())) {
            QueryWrapper<MonitorSolutionRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ruleid", rule.getRuleid());
            List<MonitorSolutionRule> list = iMonitorSolutionRuleService.list(queryWrapper);
            if (list.size() == 0) {
                String status = rule.getSatus();
                if (status.equalsIgnoreCase("启用"))
                    rule.setSatus("禁用");
                else if (status.equalsIgnoreCase("禁用"))
                    rule.setSatus("启用");
                iMonitorRuleService.updateById(rule);
                return new JsonBean(200, "成功", 0);
            } else {
                return new JsonBean(200, "成功", "规则正在使用,无法操作！");
            }
        } else {
            return new JsonBean(200, "成功", "权限不足");
        }
    }

    /**
     * 查看一条数据
     *
     * @param
     * @return
     */
    @Operation(summary = "torule_disp")
    @GetMapping(value = "/gzjk/rule/rule_disp")
    public JsonBean torule_disp(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam(value = "selectedruleid") String selectedruleid,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule rule = iMonitorRuleService.getById(selectedruleid);
        Map map = new HashMap();
        if (rule != null) {
            Organization byId = iOrganizationService.getById(rule.getOrgid());
            if (byId != null && byId.getOrgid() != null) {
                map.put("org", byId);
            }

            map.put("rule", rule);
            map.put("url", "znjk/gzjk/rule/rule_disp");
        } else
            map.put("url", "znjk/gzjk/rule/rulesmgmt");
        return new JsonBean(200, "成功", map);
    }


    /**
     * 查看规则执行结果
     *
     * @param
     * @return
     * @author SongXiangYing
     * @date 2016年1月21日 上午11:09:14
     */
    @Operation(summary = "resultmgmt")
    @GetMapping(value = "/result/resultmgmt")
    public JsonBean resultmgmt(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid") String ruleid,
                               @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                               @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", name = "pageSize", defaultValue = "15") Integer pageSize,
                               @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId") String orgId,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        if (null != ruleid) {
            IPage<MonitorPrewarning> a = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorPrewarning> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("ruleid", ruleid);
            queryWrapper.like("signId", "%" + TaskConstant.ZX + "_%");
            queryWrapper.orderByDesc("saveDateTime");
            map.put("pageBean", iMonitorPrewarningService.page(a, queryWrapper));
        }
        map.put("orgId", orgId);
        map.put("ruleid", ruleid);
        //为页面查找区域显隐藏赋值

        return new JsonBean(200, "成功", map);
    }


    /**
     * 规则详细结果 SongXiangYing 2016年3月9日 - 下午1:14:14
     * 先注释掉  --- 待解决
     *
     * @param pageNumber
     * @param
     * @return
     */

    @Operation(summary = "resultmgmtInfo")
    @GetMapping(value = "/result/resultmgmtinfo")
    public JsonBean resultmgmtInfo(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1",required = false) Integer pageNumber,
                                   @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", name = "pageSize", defaultValue = "15",required = false) Integer pageSize,
                                   @Parameter(name = "selectIds", description = "selectIds") @RequestParam(value = "selectIds",required = false) String selectIds,
                                   @Parameter(name = "ruleId", description = "ruleId") @RequestParam(value = "ruleId",required = false) String ruleId,
                                   @Parameter(name = "signId", description = "signId") @RequestParam(value = "signId",required = false) String signId,
                                   @Parameter(name = "seleNum", description = "seleNum") @RequestParam(value = "seleNum",required = false) String seleNum,
                                   @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId",required = false) String orgId,
                                   @Parameter(name = "seleText", description = "seleText") @RequestParam(value = "seleText",required = false) String seleText,
                                   @Parameter(name = "seleText1", description = "seleText1") @RequestParam(value = "seleText1",required = false) String seleText1,
                                   @Parameter(name = "seleText2", description = "seleText2") @RequestParam(value = "seleText2",required = false) String seleText2,
                                   @Parameter(name = "fhtype", description = "fhtype") @RequestParam(value = "fhtype",required = false) String fhtype,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        seleText = StringUtils.isNotBlank(seleText2) ? seleText2 : seleText;
        try {
            MonitorRule rule = iMonitorRuleService.getById(ruleId);
            String whe = "";
            if (StringUtils.isNotBlank(seleNum) && StringUtils.isNotBlank(seleText)) {
                whe += " and " + seleNum + "  like  '%" + seleText + "%'";
            }

            if (null == signId) {
                String lastId = iWarningResultService.getLast();
                QueryWrapper<MonitorPrewarning> queryWrapper = new QueryWrapper<>();
                queryWrapper.like("signId", lastId + "_%");
                queryWrapper.eq("ruleid", new BigDecimal(ruleId));
                queryWrapper.orderByDesc("saveDateTime");
                List<MonitorPrewarning> findResltList = iMonitorPrewarningService.list(queryWrapper);
                if (findResltList.size() > 0) {
                    signId = findResltList.get(0).getSignid();
                }
            }
            Accbook acctbook = iAccbookService.getById(rule.getConnectionstrings());
            if (Objects.isNull(acctbook) || Objects.isNull(acctbook.getAcctid())) {
                return new JsonBean(500, rule.getConnectionstrings() + " 没有找到帐簿!!", null);
            }
            DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
            Map<String, Object> map = dynamicDataSource.getData("ZNJK_GZ_" + ruleId, signId, pageNumber, pageSize, whe);
            Map map1 = new HashMap();
            map1.put("column", map.get("column"));
            map1.put("pageBean", map.get("data"));
            map1.put("ruleId", ruleId);
            map1.put("fhtype", fhtype);
            map1.put("signId", signId);
            map1.put("orgId", orgId);
            map1.put("seleNum", seleNum);
            map1.put("bookId", acctbook.getAcctid());
            return new JsonBean(200, "成功", map1);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(500, e.getMessage(), null);
        }
    }


    /**
     * 规则详细结果-导出 SongXiangYing 2016年3月9日 - 下午1:14:14
     *
     * @param
     * @param
     * @return
     */
    @Operation(summary = "导出")
    @GetMapping(value = "/result/resultmgmtinfo/export")
    public JsonBean export_YwlcKzjz(@Parameter(name = "ruleId", description = "ruleId") @RequestParam(value = "ruleId") String ruleId,
                                    @Parameter(name = "signId", description = "signId") @RequestParam(value = "signId") String signId,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule rule = iMonitorRuleService.getById(ruleId);
        if (null == signId) {
            String lastId = iWarningResultService.getLast();
            QueryWrapper<MonitorPrewarning> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("signId", lastId + "_%");
            queryWrapper.eq("ruleid", new BigDecimal(ruleId));
            queryWrapper.orderByDesc("saveDateTime");
            List<MonitorPrewarning> findResltList = iMonitorPrewarningService.list(queryWrapper);
            if (findResltList.size() > 0) {
                signId = findResltList.get(0).getSignid();
            }
        }
        Accbook acctbook = iAccbookService.getById(rule.getConnectionstrings());
        if (Objects.isNull(acctbook) || Objects.isNull(acctbook.getAcctid())) {
            return new JsonBean(500, rule.getConnectionstrings() + " 没有找到帐簿!!", null);
        }
        try {
            DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
            Map<String, Object> map = dynamicDataSource.getDataExport("ZNJK_GZ_" + ruleId, signId);
            List<String> columeName = (List<String>) map.get("column");
            List<List<Object>> list = (List<List<Object>>) map.get("data");
            List<Object[]> objs = new ArrayList<Object[]>();
            for (List<Object> list2 : list) {
                Object[] obj = new Object[list2.size()];
                for (int i = 0; i < list2.size(); i++) {
                    obj[i] = list2.get(i);
                }
                objs.add(obj);
            }
            String[] strs = new String[columeName.size() - 2];
            for (int i = 0; i < columeName.size(); i++) {
                if (i >= 2) {
                    strs[i - 2] = columeName.get(i);
                }
            }
            ExcelUtil exportUtil = new ExcelUtil("规则管理预警结果", strs);
            HSSFCell cell = null;
            HSSFCellStyle bodyStyle = exportUtil.getBodyStyle();

            for (int j = 0; j < objs.size(); j++) {
                HSSFRow bodyRow = exportUtil.getNextRow(j + 1);
                Object[] c = (Object[]) objs.get(j);
                for (int i = 2; i < c.length; i++) {
                    cell = bodyRow.createCell(i - 2);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(c[i].toString());
                }
            }
            return new JsonBean(200, rule.getRulename().trim() + "-规则导出.xls", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(500, e.getMessage(), "");
        }
    }


    /**
     * 规则管理--执行
     *
     * @param
     * @return
     */
    @Operation(summary = "ruleExecute")
    @GetMapping(value = "/gzjk/rule/ruleExecute")
    public JsonBean ruleExecute(@Parameter(name = "id", description = "id") @RequestParam(value = "id") String id,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule rule = iMonitorRuleService.getById(id);

        if (null != rule && rule.getSatus().equals("启用")) {
            if (new BigDecimal("1").compareTo(rule.getRunstatus())==0) {
                return new JsonBean(200, "成功", "执行中,请稍等！");
            }
            ScheduleJob job = null;
            try {
                ArrayList<MonitorRule> list = new ArrayList<MonitorRule>();
                list.add(rule);

                Staff staff1 = iStaffService.getById(staff.getStaffid());
                job = cJobTaskService.gzTaskExec(list, staff1, TaskConstant.ZX);
                rule.setRunstatus(new BigDecimal("1"));
            } catch (SchedulerException e) {
                e.printStackTrace();
                try {
                    jobTaskService.changeStatus(job, "stop");
                } catch (SchedulerException e1) {
                    e1.printStackTrace();
                }
                rule.setRunstatus(new BigDecimal("3"));
            }
            iMonitorRuleService.updateById(rule);
            return new JsonBean(200, "成功", 0);
        } else if (!rule.getSatus().equals("启用")) {
            return new JsonBean(200, "成功", "请先启用规则！");
        }
        return new JsonBean(200, "成功", "请提供正确的规则编号！");
    }


    /**
     * 规则管理 --删除
     *
     * @param
     * @return
     * @author SongXiangYing
     * @date 2016年1月19日 下午7:24:42
     */
    @Operation(summary = "ruleDel")
    @GetMapping(value = "/gzjk/rule/rule_del")
    @Transactional
    public JsonBean ruleDel(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam(value = "selectedruleid") String selectedruleid,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        BigDecimal orgId = null;
        //判断是否有id
        if (selectedruleid != null) {
            //获取当前管理信息
            MonitorRule monitorRule = iMonitorRuleService.getById(selectedruleid);
            if (Objects.isNull(monitorRule)) {
                return new JsonBean(500, "通过" + selectedruleid + "没有找到规则！！", null);
            }
            //获取ordId
            orgId = monitorRule.getOrgid();
            //获取账本信息
//            Accbook acctbook = iAccbookService.getById(monitorRule.getConnectionstrings());
//            if (Objects.isNull(acctbook) || Objects.isNull(acctbook.getAcctid())) {
//                return new JsonBean(500, "通过连库没有找到帐簿!!" + monitorRule.getConnectionstrings(), null);
//            }
            try {
//                DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
//                dynamicDataSource.deleteTable("ZNJK_GZ_" + selectedruleid);
                iMonitorRuleService.removeById(monitorRule);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonBean(500, e.getMessage(), "");
            }
            return new JsonBean(200, "成功", map);
        }
        return new JsonBean(500, "请选择规则id", null);
    }


    /*
    行业规则库---》左侧菜单
     */
    @Operation(summary = "hy_left")
    @GetMapping(value = "/gzjk/rule/hy_left")
    public JsonBean hy_left(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                            @Parameter(name = "type", description = "type") @RequestParam(value = "type") String type,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        logger.info("行业规则库---》左侧菜单");
        //树的问题  有遗留问题
        List<Organization> findOrgTree = treeService.findOrgTreeObjByHY();

        String tree = "";
        String treeName = "";
        if (StringUtils.isNotEmpty(orgid) && !orgid.equals(findOrgTree.get(0).getOrgid().toString())) {
            iOrganizationService.initOrgByOrgtype(Organization.TYPE_HY_GZK, "行业规则库", orgid);
            tree = iOrganizationService.findOrgTreeByOrgtypeAndOrgid(orgid, Organization.TYPE_HY_GZK, null);
            Organization o = iOrganizationService.getById(orgid);
            treeName = o.getOrgname();
        } else {
            treeName = findOrgTree.get(0).getOrgname();
        }
        int startNum = tree.indexOf("-1_");
        String defaultId = startNum > 0 ? tree.substring(startNum).substring(3, tree.substring(startNum).indexOf("'")) : "";
        Map map = new HashMap();
        map.put("icode", orgid);
        map.put("treeName", treeName.replace("┣", ""));
        map.put("orgTree", findOrgTree);
        map.put("defaultId", defaultId);
        map.put("selectorgtype", Organization.TYPE_HY_GZK);
        map.put("targetFrame", "mainFramex");
        map.put("type", type);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 行业规则库、规则管理,互相复制。
     *
     * @param id
     * @return
     */
    @Operation(summary = "CopyRule")
    @GetMapping(value = "/rule/copyExchange")
    public JsonBean CopyRule(@Parameter(name = "id", description = "id") @RequestParam(value = "id") String id,
                             @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                             @Parameter(name = "bookid", description = "bookid") @RequestParam(value = "bookid") String bookid,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule rule = iMonitorRuleService.getById(id);
        if (rule.getInruledb().equals("1")) {
            MonitorRule monitorRule = new MonitorRule();
            monitorRule.setRuleid(RandomUtil.uuBigDecimalId());
            monitorRule.setRulename(rule.getRulename());
            monitorRule.setRulecode(rule.getRulecode());
            monitorRule.setRuledescription(rule.getRuledescription());
            monitorRule.setRisklevel(rule.getRisklevel());
            monitorRule.setRulepriority(rule.getRulepriority());
            monitorRule.setSatus(rule.getSatus());
//            缺少字段
//            monitorRule.setExeinterval(rule.getExeinterval());
//            monitorRule.setUnit(rule.getUnit());
            monitorRule.setRegexp(rule.getRegexp());
            monitorRule.setRulesql(rule.getRulesql());
            monitorRule.setReportsql(rule.getReportsql());
            monitorRule.setTips(rule.getTips());
//            修改为当前用户的账套
            monitorRule.setConnectionstrings(bookid);
            monitorRule.setMemo(rule.getMemo());
            monitorRule.setStaffid(staff.getStaffid());
            monitorRule.setInruledb(MonitorRule.IS_HY0.toString());
            monitorRule.setRunstatus(new BigDecimal("0"));
            monitorRule.setOrgid(staff.getOrgid());
            iMonitorRuleService.save(monitorRule);
            return new JsonBean(200, "成功", "复制成功！");

        } else {
            MonitorRule monitorRule = new MonitorRule();
            monitorRule.setRuleid(RandomUtil.uuBigDecimalId());
            monitorRule.setRulename(rule.getRulename());
            monitorRule.setRulecode(rule.getRulecode());
            monitorRule.setRuledescription(rule.getRuledescription());
            monitorRule.setRisklevel(rule.getRisklevel());
            monitorRule.setRulepriority(rule.getRulepriority());
            monitorRule.setSatus(rule.getSatus());
            //缺少字段
//            monitorRule.setExeinterval(rule.getExeinterval());
//            monitorRule.setUnit(rule.getUnit());
            monitorRule.setRegexp(rule.getRegexp());
            monitorRule.setRulesql(rule.getRulesql());
            monitorRule.setReportsql(rule.getReportsql());
            monitorRule.setTips(rule.getTips());
            //修改为当前用户的账套
            //monitorRule.setConnectionstrings(rule.getConnectionstrings());
            monitorRule.setConnectionstrings(bookid);
            monitorRule.setMemo(rule.getMemo());
            /*monitorRule.setRunstatus(0);*/
            monitorRule.setInruledb(MonitorRule.IS_HY1.toString());
            monitorRule.setStaffid(staff.getStaffid());
//            Organization tblOrganization = new Organization();
//            tblOrganization.setOrgid(new BigDecimal(orgid));
//            iOrganizationService.save(tblOrganization);
            monitorRule.setOrgid(new BigDecimal(orgid));
            iMonitorRuleService.save(monitorRule);
            return new JsonBean(200, "成功", "复制成功！");
        }

    }

}
