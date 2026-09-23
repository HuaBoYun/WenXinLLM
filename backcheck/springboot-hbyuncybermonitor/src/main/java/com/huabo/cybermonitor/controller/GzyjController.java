package com.huabo.cybermonitor.controller;

import static org.apache.http.util.TextUtils.isEmpty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.MonitorPrewarning;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.MonitorSolutionRule;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.huabo.cybermonitor.entity.MonitorSolutionresultVo;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.mapper.YhrPageMapper;
import com.huabo.cybermonitor.service.IMonitorPrewarningService;
import com.huabo.cybermonitor.service.IMonitorRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.base.CJobTaskService;
import com.huabo.cybermonitor.service.impl.AutoIdService;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.task.base.ScheduleJob;
import com.huabo.cybermonitor.util.DateUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;




/**
 * 规则监控-规则预警api接口
 *
 * @createTime 2022/8/12
 */

@RestController
@Slf4j
@Tag(name="规则监控-规则预警",description="规则监控-规则预警")
@RequestMapping(value = "/cyber/GzyjController")
@SuppressWarnings("all")
public class GzyjController {

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    IMonitorRuleService iMonitorRuleService;

    @Autowired
    IMonitorSolutionRuleService iMonitorSolutionRuleService;

    @Autowired
    IOrganizationService iOrganizaService;

    @Autowired
    CJobTaskService cJobTaskService;

    @Autowired
    JobTaskService jobTaskService;

    @Autowired
    IStaffService iStaffService;

    @Autowired
    IMonitorPrewarningService iMonitorPrewarningService;


    @Autowired
    IMonitorSolutionresultService iMonitorSolutionresultService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 规则预警---左侧树形
     */
    @Operation(summary = "solutionleft")
    @GetMapping(value = "/gzjk/rule/solutionleft")
    public JsonBean solutionleft(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mv = new HashMap<>();
        Organization byId = iOrganizationService.getById(staff.getStaffid());
        mv.put("userOrgId", byId.getOrgid());
        mv.put("orgid", staff.getCurrentOrg().getOrgid());

        return new JsonBean(200, "成功", mv);
    }


    @Autowired
    YhrPageMapper yhrPageMapper;
    /**
     * 规则预警 --列表
     */
    @Operation(summary = "solutionMgmt")
    @GetMapping(value = "/gzjk/rule/solutionmgmt")
    public JsonBean solutionMgmt(@Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId",required = false) BigDecimal orgId,
                                 @Parameter(name = "solutioncode", description = "solutioncode") @RequestParam(value = "solutioncode") String solutioncode,
                                 @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                                 @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", name = "pageSize", defaultValue = "15") Integer pageSize,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        IPage<Map<String,Object>> a = new Page<>(pageNumber, pageSize);
        StringBuilder where  = new StringBuilder(" where ");
        Boolean isSelect = false;
        boolean isDU = false;
        if (staff.getCurrentOrg().getOrgid().equals(staff.getLinkOrg().getOrgid())) {
            if (orgId == null) {
                orgId = staff.getLinkDetp().getOrgid();
            }

            where.append(" a.type =  1");
            where.append(" and a.ORGID = "+orgId);
            if (StringUtils.isNotBlank(solutioncode)) {
                where.append(" and a.SOLUTIONCODE like "+solutioncode);
            }
            where.append(" order by CREATEDATE");

        } else {
            if (orgId == null) {
                orgId = staff.getCurrentOrg().getOrgid();
            }
            where.append(" a.type =  1");
            where.append(" and a.ORGID = "+orgId);
        }
        Map<String, Object> mv = new HashMap<>();
        mv.put("orgId", orgId);

        String sql = "select a.*,ts.realname from TBL_MONITOR_SOLUTION a left join TBL_STAFF TS on a.STAFFID = TS.STAFFID" +where;
        yhrPageMapper.getPage(a,sql);
        mv.put("pageBean", a);
        mv.put("isAdd", isSelect);
        mv.put("isDU", isDU);
        mv.put("solutioncode", solutioncode);
        return new JsonBean(200, "成功", mv);

    }

    /**
     * 规则预警--添加跳转
     */
    @Operation(summary = "toSolutionAdd")
    @GetMapping(value = "/gzjk/rule/to_solution_add")
    public JsonBean toSolutionAdd(@Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId") BigDecimal orgId,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mv = new HashMap<>();
        mv.put("orgId", orgId);
        mv.put("tblStaff", staff);
        return new JsonBean(200, "成功", mv);
    }

    /**
     * 规则预警--保存
     */
    @Operation(summary = "solutionAdd")
    @GetMapping(value = "/gzjk/rule/solution_add")
    @Transactional
    public JsonBean solutionAdd(@Parameter(name = "solutioncode", description = "solutioncode") @RequestParam(value = "solutioncode") String solutioncode,
                                @Parameter(name = "solutionname", description = "solutionname") @RequestParam(value = "solutionname") String solutionname,
                                @Parameter(name = "solutionstatus", description = "solutionstatus") @RequestParam(value = "solutionstatus") String solutionstatus,
                                @Parameter(name = "memo", description = "memo") @RequestParam(value = "memo") String memo,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution solution = new MonitorSolution();
        solution.setSolutioncode(solutioncode);
        solution.setSolutionname(solutionname);
        solution.setRunstatus(new BigDecimal(0));
        solution.setSolutionstatus(solutionstatus);
        solution.setType(new BigDecimal(1));
        solution.setCreatedate(LocalDateTime.now());
        solution.setMemo(memo);
        Organization organization = this.iOrganizaService.getById(staff.getLinkDetp().getOrgid());
        if (null != organization) {
            solution.setOrgid(organization.getOrgid());
        }
        solution.setStaffid(staff.getStaffid());
        //solution.setSolutionid(new BigDecimal("1559526045026635778"));
        iMonitorSolutionService.save(solution);
        return new JsonBean(200, "成功", 0);
    }


    /**
     * 规则预警--添加规则列表
     */
    @Operation(summary = "ruleslistSelector")
    @GetMapping(value = "/gzjk/rule/ruleslistSelector")
    public JsonBean ruleslistSelector(@Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId") BigDecimal orgId,
                                      @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "pageSize") @RequestParam("pageSize") Integer pageSize,
                                      @Parameter(name = "solutionid", description = "solutionid") @RequestParam(value = "solutionid",required = false) String solutionid,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        IPage<MonitorRule> ip = new Page<>(pageNumber, pageSize);
        QueryWrapper<MonitorRule> wrapper = new QueryWrapper<>();
        wrapper.eq("orgid", orgId);
        wrapper.eq("satus", "启用");
        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", iMonitorRuleService.page(ip, wrapper));
        mv.put("orgId", orgId);
        mv.put("solutionid", solutionid);
        return new JsonBean(200, "成功", mv);
    }


    /**
     * 规则预警-保存规则
     */
    @Operation(summary = "新增")
    @GetMapping(value = "/gzjk/add_rule")
    public JsonBean add_rule(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid") String ruleid,
                             @Parameter(name = "souceid", description = "souceid") @RequestParam(value = "souceid") String souceid,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (ruleid != null && souceid != null) {


            QueryWrapper qw = new QueryWrapper();
            qw.eq("SOLUTIONID", souceid);
            qw.eq("RULEID", ruleid);
            MonitorSolutionRule msr = iMonitorSolutionRuleService.getOne(qw);
            if (msr == null) {
                msr = new MonitorSolutionRule();
                msr.setRuleid(new BigDecimal(ruleid));
                msr.setSolutionid(new BigDecimal(souceid));
                iMonitorSolutionRuleService.save(msr);
            }
//            MonitorSolution solution = iMonitorSolutionService.getById(souceid);
//            MonitorRule r = iMonitorRuleService.getById(ruleid);
//            MonitorSolution monitorSolution = iMonitorSolutionService.getById(souceid);
//            MonitorSolutionRule sr = new MonitorSolutionRule();
//            sr.setRuleid(r.getRuleid());
//            sr.setSolruleid(solution.getSolutionid());
//            QueryWrapper qw = new QueryWrapper();
//            qw.eq("solutionid", solution.getSolutionid());
//            List<MonitorSolutionRule> list = iMonitorSolutionRuleService.list(qw);
//            boolean fal = true;
//            if (list != null && list.size() > 0) {
//                for (MonitorSolutionRule iMonitorSolutionRule : list) {
//                    if (iMonitorSolutionRule.getRuleid().toString().equals(ruleid)
//                            && iMonitorSolutionRule.getSolutionid().toString()
//                            .equals(souceid)) {
//                        fal = false;
//                    }
//                }
//            }
//            if (fal) {
//                iMonitorSolutionRuleService.save(sr);
//                iMonitorSolutionService.updateById(monitorSolution);
//            }
        }
        return new JsonBean(200, "成功", "");
    }

    /**
     * 规则预警--修改跳转
     */
    @Operation(summary = "toSolutionModify")
    @GetMapping(value = "/gzjk/rule/to_solution_modify")
    public JsonBean toSolutionModify(@Parameter(name = "selectedid", description = "selectedid") @RequestParam(value = "selectedid") String selectedid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution solution = iMonitorSolutionService.getById(selectedid);

        List<MonitorRule> list = iMonitorSolutionRuleService.queryRuleBySoluid(new BigDecimal(selectedid));

        StringBuffer buffer = new StringBuffer();
        if (list != null) {
            for (MonitorRule mr : list) {
                buffer.append(mr.getRuleid() + ",");
            }
        }

        Map<String, Object> mv = new HashMap<>();
        mv.put("solutionruleList", list);
        if (buffer.toString().length() > 0) {
            mv.put("solruleids", buffer.toString().substring(0, buffer.toString().length() - 1));
        }
        if (solution != null) {
            mv.put("solution", solution);
            mv.put("orgId", solution.getOrgid());
        }
        return new JsonBean(200, "成功", mv);
    }

    /**
     * 规则预警--判断当前方案是否可以删除或者修改
     *   if ("solution_del".equals(href)) {
     *                     return new JsonBean(200, "方案已执行，不能删除！", "方案已执行，不能删除！");
     *                 } else {
     *                     return new JsonBean(200, "方案已执行，不能修改！", "方案已执行，不能修改！");
     *                 }
     */
    @Operation(summary = "solutionchechUpate")
    @GetMapping(value = "/gzjk/rule/solutioncheckUpdate")
    public JsonBean solutionchechUpate(@Parameter(name = "selectedid", description = "方案编号") @RequestParam(value = "selectedid") String selectedid,
                                       @Parameter(name = "href", description = "修改以及删除时状态判断，solution_del为删除，其它为修改") @RequestParam(value = "href") String href,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution rule = this.iMonitorSolutionService.getById(selectedid);
        if(rule==null) return new JsonBean(200, "表中没有此条数据", selectedid);
        if (rule.getStaffid().equals(staff.getStaffid())) {
            if (!rule.getRunstatus().equals(0)) {
                if ("solution_del".equals(href)) {
                    return new JsonBean(200, "方案已执行，不能删除！", "方案已执行，不能删除！");
                } else {
                    return new JsonBean(200, "方案已执行，不能修改！", "方案已执行，不能修改！");
                }
            }
            return new JsonBean(1, "可以进行修改", "");
        } else {
            return new JsonBean(200, "权限不足", "权限不足");
        }
    }

    /**
     * 规则预警 -修改操作
     */
    @Operation(summary = "solutionModify")
    @GetMapping(value = "/gzjk/rule/solution_modify")
    public JsonBean solutionModify(@Parameter(name = "solutionid", description = "solutionid") @RequestParam(value = "solutionid") String solutionid,
                                   @Parameter(name = "solutioncode", description = "solutioncode") @RequestParam(value = "solutioncode") String solutioncode,
                                   @Parameter(name = "solutionname", description = "solutionname") @RequestParam(value = "solutionname") String solutionname,
                                   @Parameter(name = "solutionstatus", description = "solutionstatus") @RequestParam(value = "solutionstatus") String solutionstatus,
                                   @Parameter(name = "memo", description = "memo") @RequestParam(value = "memo") String memo,
                                   @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId") String orgId,
                                   @Parameter(name = "createdate", description = "2000-10-12") @RequestParam(value = "createdate") String createdate,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
            throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution solution = this.iMonitorSolutionService.getById(solutionid);
        solution.setSolutioncode(solutioncode);
        solution.setSolutionname(solutionname);
        solution.setSolutionstatus(solutionstatus);
        //Organization organization = this.iOrganizaService.getById(orgId);
        //solution.setOrgid(organization.getOrgid());
        solution.setOrgid(new BigDecimal(orgId));
        if (createdate != null && !"".equalsIgnoreCase(createdate)) {
            solution.setCreatedate(DateUtils.StringToLocalDateTime(createdate, "yyyy-MM-dd"));
        }
        solution.setMemo(memo);
        Map<String, Object> mv = new HashMap<>();
        iMonitorSolutionService.updateById(solution);
        return new JsonBean(200, "成功", mv);
    }

    /**
     * 规则预警 -删除操作
     */
    @Operation(summary = "solutionDel")
    @GetMapping(value = "/gzjk/rule/solution_del")
    public JsonBean solutionDel(@Parameter(name = "selectedid", description = "selectedid") @RequestParam(value = "selectedid") String selectedid,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mv = new HashMap<>();
        if (selectedid != null) {
            MonitorSolution monitorSolution = this.iMonitorSolutionService.getById(selectedid);
            mv.put("orgId", monitorSolution.getOrgid());
            // iMonitorSolutionService.removeById(selectedid);  删除操作
        }
        return new JsonBean(200, "成功", mv);
    }

    /**
     * 预警方案--状态修改
     */
    @Operation(summary = "solutionUpdateStatus")
    @GetMapping(value = "/gzjk/solution/solutionUpdateStatus")
    public JsonBean solutionUpdateStatus(@Parameter(name = "solutionid", description = "136494") @RequestParam(value = "solutionid") String solutionid,
                                         @Parameter(name = "pageNumber", description = "1") @RequestParam(value = "pageNumber") String pageNumber,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution solution = this.iMonitorSolutionService.getById(solutionid);
        if (solution.getStaffid().equals(staff.getStaffid())) {
            if (null != solution) {
                String status = solution.getSolutionstatus();
                if (status.equalsIgnoreCase("启用")) {
                    solution.setSolutionstatus("停用");
                } else if (status.equalsIgnoreCase("停用")) {
                    solution.setSolutionstatus("启用");
                }
                iMonitorSolutionService.updateById(solution);
                return new JsonBean(200, "成功", solution);
            }
        }
        return new JsonBean(200, "权限不足", "权限不足");
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

    /**
     * 规则预警 --结果页面
     */
    @Operation(summary = "resultmgmtlist")
    @GetMapping(value = "/result/resultmgmtlist")
    public JsonBean resultmgmtlist(@Parameter(name = "modelId", description = "modelId") @RequestParam(value = "modelId") BigDecimal modelId,
                                   @Parameter(name = "solutionResultId", description = "solutionResultId") @RequestParam(value = "solutionResultId") BigDecimal solutionResultId,
                                   @Parameter(name = "soultionId", description = "soultionId") @RequestParam(value = "soultionId") String soultionId,
                                   @Parameter(name = "source", description = "source") @RequestParam(value = "source", required = false) String source,
                                   @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId",required = true) String orgId,
                                   @Parameter(name = "fhtype", description = "fhtype") @RequestParam(value = "fhtype", required = false) String fhtype,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        List<MonitorPrewarning> resultBySignId = iMonitorPrewarningService.getResultBySignId(modelId, solutionResultId);
        Map<String, Object> mv = new HashMap<>();
        mv.put("solutionResultId", solutionResultId);
        mv.put("soultionId", soultionId);
        mv.put("modelId", modelId);
        mv.put("fhtype", fhtype);
        mv.put("orgId", orgId);
        mv.put("results", resultBySignId);
        mv.put("source", source);
        return new JsonBean(200, "成功", mv);
    }

    /**
     * 规则预警 --执行
     */
    @Operation(summary = "solutionExecute")
    @GetMapping(value = "/gzjk/rule/solutionExecute")
    public JsonBean solutionExecute(@Parameter(name = "selectedid", description = "selectedid") @RequestParam(value = "selectedid") String selectedid,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution solution = iMonitorSolutionService.getById(selectedid);
        if (null != solution && solution.getSolutionstatus().equals("启用")) {
            if (solution.getRunstatus().equals("1")) {
                return new JsonBean(200, "正在执行,请稍等！", "正在执行,请稍等！");
            } else {
                List<MonitorSolution> solutions = new ArrayList<MonitorSolution>();
                solutions.add(solution);
                ScheduleJob job = null;
                try {
                    Staff staff1 = iStaffService.getById(staff.getStaffid());
                    //执行类不匹配
                    job = this.cJobTaskService.gzzjTaskExec(solutions, staff1, "", MonitorSolutionresult.ZX);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                    try {
                        this.jobTaskService.changeStatus(job, "stop");
                    } catch (SchedulerException e1) {
                        e1.printStackTrace();
                    }
                }
                return new JsonBean(200, "成功", "");
            }
        }
        return new JsonBean(200, "请先启用规则", "请先启用规则");
    }

    @Autowired
    AutoIdService autoid;
    /**
     * 通用获取设置的编号
     *
     * @param
     */
    @Operation(summary = "查询数据")
    @GetMapping(value = "/code/findAutoNumber")
    public JsonBean findAutoNumber(HttpServletResponse response,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name="tblName",description="TBL_ASSESS") @RequestParam(value = "tblName") String tblName,
                                   @Parameter(name="column",description="ASSESSID") @RequestParam(value = "column") String column,
                                   @Parameter(name="orgCol",description="TBLCOMANY") @RequestParam(value = "orgCol") String orgCol,
                                   @Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        String flowNextId = null;
        try {
            flowNextId = autoid.findFlowNextId(tblName, column, orgCol, staff.getCurrentOrg().getOrgid(), noId,
                    null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.reset();
        return new JsonBean(200, "success", flowNextId);
    }

    /**
     * 获取组织是否使用自定义编号
     * @param orgid
     * @return
     * @throws Exception
     */
    public Integer getIsUseAutoNoInfo(BigDecimal orgid) throws Exception {
        String sql = "SELECT ISAUTONUMBER FROM TBL_ORGANIZATION WHERE ORGID = "+orgid;
        Organization o =  iOrganizaService.getById(orgid);
        return Integer.parseInt(o.getIsautonumber().toString());
    }



    /**
     * 规则预警-查看
     *
     * @param request
     * @return
     */
    @Operation(summary = "solution_disp_gz")
    @GetMapping(value = "/gzjk/rule/solution_disp_gz")

    public JsonBean solution_disp_gz(@Parameter(name = "selectedid", description = "selectedid") @RequestParam(value = "selectedid") String selectedid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}


        MonitorSolution solution=iMonitorSolutionService.queryOne(selectedid);

        List<MonitorRule> list= iMonitorSolutionService.queryMonitorRuleList(selectedid);

        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("solution", solution);
        mv.put("orgId", solution.getOrgid());
        mv.put("solutionruleList", list);



        return new JsonBean(200, "success", mv);
    }

}
