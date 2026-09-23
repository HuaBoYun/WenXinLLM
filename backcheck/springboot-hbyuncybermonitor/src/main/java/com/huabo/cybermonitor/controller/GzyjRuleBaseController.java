package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.service.IAccbookService;
import com.huabo.cybermonitor.service.IMonitorRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;




@RestController
@Tag(name="规则监控-规则库",description="规则监控-规则库")
@RequestMapping(value = "/cyber/GzyjRuleBaseController")
public class GzyjRuleBaseController {

	private static final Logger log = LoggerFactory.getLogger(GzyjRuleBaseController.class);

    @Autowired
    IMonitorRuleService iMonitorRuleService;

    @Autowired
    IMonitorSolutionRuleService iMonitorSolutionRuleService;

    @Autowired
    IAccbookService iAccbookService;
    
    
    @Resource
    private UserProvider userProvider;
    
    /**
     * 规则库新增
     * @param monitorRule
     * @param token
     * @return
     * @throws Exception
     */
    @Operation(summary = "rule_base_add")
    @PostMapping(value = "/rule/rule_base_add")
    public JsonBean rule_base_add(@Parameter(name = "indicator", description = "indicator") @RequestBody() MonitorRule monitorRule,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        //判断是否有token,token是否是正确的
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        monitorRule.setInruledb(MonitorRule.IS_HY1.toString());
        //判断是否
        if (monitorRule.getRuleid() != null) {
            //iMonitorRuleService
        } else {
            iMonitorRuleService.save(monitorRule);
        }
        return new JsonBean(200, "成功", "/znjk/gzjk/rule/rulesmgmt?orgId=" + monitorRule.getOrgid());
    }
    /**
     * 规则管理 ---跳转修改
     *
     * @param
     * @return
     */
    @Operation(summary = "toRuleModify")
    @GetMapping(value = "/rule/to_rule_modify")
    public JsonBean toRuleModify(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam("selectedruleid") String selectedruleid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorRule monitorRule = iMonitorRuleService.getById(selectedruleid);
        Map map = new HashMap();
        BigDecimal orgId = staff.getLinkDetp().getOrgid();
        map.put("orgId", orgId);
        map.put("orgName", staff.getLinkDetp().getOrgname());
        if (monitorRule != null) {
            map.put("monitorRule", monitorRule);
        }
        return new JsonBean(200, "成功", map);
    }

    @Transactional
    @Operation(summary = "ruleModify")
    @PostMapping(value = "/rule/rule_modify")
    public JsonBean ruleModify(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                               @Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid") String ruleid,
                               @Parameter(name = "rulecode", description = "rulecode") @RequestParam(value = "rulecode") String rulecode,
                               @Parameter(name = "rulename", description = "rulename") @RequestParam(value = "rulename") String rulename,
                               @Parameter(name = "rulepriority", description = "rulepriority") @RequestParam(value = "rulepriority") String rulepriority,
                               @Parameter(name = "risklevel", description = "risklevel") @RequestParam(value = "risklevel") String risklevel,
                               @Parameter(name = "satus", description = "satus") @RequestParam(value = "satus") String satus,
                               @Parameter(name = "regexp", description = "regexp") @RequestParam(value = "regexp") String regexp,
                               @Parameter(name = "rulesql", description = "rulesql") @RequestParam(value = "rulesql") String rulesql,
                               @Parameter(name = "ruledescription", description = "ruledescription") @RequestParam(value = "ruledescription") String ruledescription,
                               @Parameter(name = "unit", description = "unit") @RequestParam(value = "unit") String unit,
                               @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,
                               @Parameter(name = "memo", description = "memo") @RequestParam(value = "memo") String memo,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        /* 修改结束 */
        MonitorRule rule = iMonitorRuleService.getById(ruleid);
        rule.setRulecode(rulecode);
        rule.setRulename(rulename);
        rule.setRulepriority(rulepriority);
        rule.setRisklevel(risklevel);
        rule.setSatus(satus);
        //rule.setExeinterval(exeinterval);缺少字段
        rule.setRegexp(regexp);
        rule.setRulesql(rulesql);
        rule.setRuledescription(ruledescription);
        rule.setMemo(memo);
        Map map = new HashMap();
        try {
            iMonitorRuleService.updateById(rule);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(500, "sql语句出错", "");
        }
        map.put("orgId", orgid);
        return new JsonBean(200, "成功", map);
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
    @GetMapping(value = "/rule/rule_del")
    @Transactional
    public JsonBean ruleDel(@Parameter(name = "selectedruleid", description = "selectedruleid") @RequestParam(value = "selectedruleid") String selectedruleid,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        if (selectedruleid != null) {
            MonitorRule monitorRule = iMonitorRuleService.getById(selectedruleid);
            if (Objects.isNull(monitorRule)) {
                return new JsonBean(500, "通过" + selectedruleid + "没有找到规则！！", null);
            }
            try {
                iMonitorRuleService.removeById(monitorRule);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonBean(500, e.getMessage(), "");
            }
            return new JsonBean(200, "成功", map);
        }
        return new JsonBean(500, "请选择规则id", null);
    }


    /**
     * 规则库 --列表
     * @param pageNumber
     * @param pageSize
     * @param rulecode
     * @param orgId
     * @param token
     * @return
     * @throws Exception
     */
    @Operation(summary = "hy_rulesmgmt")
    @GetMapping(value = "/rule/hy_rulesmgmt")
    public JsonBean hy_rulesmgmt(
            @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber") Integer pageNumber,
            @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize") Integer pageSize,
            @Parameter(name = "rulecode", description = "rulecode") @RequestParam(value = "rulecode") String rulecode,
            @Parameter(name = "rulename", description = "rulename") @RequestParam(value = "rulename") String rulename,
            @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId",required = false) BigDecimal orgId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        //判断是否有token,token是否是正确的
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

        Map map = new HashMap();
        Boolean isSelect = false;
        boolean isDU = false;
        //根据用户当前所在的公司的Orgid和用户隶属的公司的orgid相比较
        if (staff.getCurrentOrg().getOrgid().equals(staff.getLinkOrg().getOrgid())) {
            if (null == orgId) {
                //如果orgid为空这为他赋值
                //Organization byId = iOrganizationService.getById(staff.getOrgid());
                orgId = staff.getLinkDetp().getOrgid();
            }
            IPage<MonitorRule> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inruledb", MonitorRule.IS_HY1);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(rulecode)) {
                queryWrapper.like("rulecode", "%" + rulecode + "%");
            }
            if (StringUtils.isNotBlank(rulename)) {
                queryWrapper.like("rulename", "%" + rulename + "%");
            }
            map.put("pageBean", iMonitorRuleService.page(iPage, queryWrapper));

        } else {
            if (null == orgId) {
                orgId = staff.getCurrentOrg().getOrgid();
            }
            IPage<MonitorRule> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<MonitorRule> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("inruledb", MonitorRule.IS_HY1);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(rulecode)) {
                queryWrapper.like("rulecode", "%" + rulecode + "%");
            }
            if (StringUtils.isNotBlank(rulename)) {
                queryWrapper.like("rulename", "%" + rulename + "%");
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

}
