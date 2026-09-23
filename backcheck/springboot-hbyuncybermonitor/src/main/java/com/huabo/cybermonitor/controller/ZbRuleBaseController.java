package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.Indicatorthreshold;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.huabo.cybermonitor.service.IIndicatorthresholdService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name="指标监控-指标库",description="指标监控-指标库")
@RequestMapping(value = "/cyber/ZbRuleBaseController")
public class ZbRuleBaseController {

	private static final Logger log = LoggerFactory.getLogger(ZbRuleBaseController.class);

    @Autowired
    IIndicatorService iIndicatorService;

    @Autowired
    IIndicatorthresholdService indicatorthresholdService;
    
    @Resource
    private UserProvider userProvider;
    /**
     * 指标库 -- 新增
     *
     * @param indicator
     * @param date
     * @param token
     * @return
     * @throws Exception
     */
    @Operation(summary = "rule_base_add")
    @PostMapping(value = "/rule/rule_base_add")
    public JsonBean rule_base_add(@Parameter(name = "indicator", description = "indicator") @RequestBody Indicator indicator,
                                  @Parameter(name = "date", description = "date") @RequestParam(value = "date") String date,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        //判断是否有token,token是否是正确的
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (StringUtils.isNotEmpty(date)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                indicator.setCreatedate(LocalDateTime.parse(date));
            } catch (Exception e) {

            }
        }
        // 在token 取到当前用户
        indicator.setIndicatordb(Indicator.IS_HY1);
        iIndicatorService.save(indicator);
        return new JsonBean(200, "成功", "/znjk/gzjk/rule/rulesmgmt?orgId=" + indicator.getOrgid());
    }


    /**
     * 指标库 --列表
     *
     * @param pageNumber
     * @param pageSize
     * @param indicatorcode
     * @param indicatorname
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
            @Parameter(name = "indicatorcode", description = "indicatorcode") @RequestParam(value = "indicatorcode") String indicatorcode,
            @Parameter(name = "indicatorname", description = "indicatorname") @RequestParam(value = "indicatorname") String indicatorname,
            @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
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
            IPage<Indicator> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<Indicator> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("indicatordb", Indicator.IS_HY1);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(indicatorcode)) {
                queryWrapper.like("indicatorcode", "%" + indicatorcode + "%");
            }
            if (StringUtils.isNotBlank(indicatorname)) {
                queryWrapper.like("indicatorname", "%" + indicatorname + "%");
            }
            map.put("pageBean", iIndicatorService.page(iPage, queryWrapper));

        } else {
            if (null == orgId) {
                //如果orgid为空这为他赋值
                orgId = staff.getCurrentOrg().getOrgid();
            }
            IPage<Indicator> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<Indicator> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("indicatordb", Indicator.IS_HY1);
            queryWrapper.eq("ORGID", orgId);
            if (StringUtils.isNotBlank(indicatorcode)) {
                queryWrapper.like("indicatorcode", "%" + indicatorcode + "%");
            }
            if (StringUtils.isNotBlank(indicatorname)) {
                queryWrapper.like("indicatorname", "%" + indicatorname + "%");
            }
            queryWrapper.orderByDesc("ruleid");

            map.put("pageBean", iIndicatorService.page(iPage, queryWrapper));
        }
        map.put("orgId", orgId);
        map.put("isAdd", isSelect);
        map.put("isDU", isDU);
        map.put("modelcode", indicatorcode);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 规则库 ---跳转修改
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
        Indicator indicator = iIndicatorService.getById(selectedruleid);
        Map map = new HashMap();
        //Organization byId = iOrganizationService.getById(staff.getStaffid());
        //BigDecimal orgId = byId.getOrgid();
        BigDecimal orgId = staff.getLinkDetp().getOrgid();
        map.put("orgId", orgId);
        map.put("orgName", staff.getLinkDetp().getOrgname());
        if (indicator != null) {
            map.put("indicator", indicator);
        }
        return new JsonBean(200, "成功", map);
    }

    @Transactional
    @Operation(summary = "ruleModify")
    @PostMapping(value = "/rule/rule_modify")
    //solutionrules没有什么用
    public JsonBean ruleModify(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                               @Parameter(name = "indicatorid", description = "indicatorid") @RequestParam(value = "indicatorid") String indicatorid,
                               @Parameter(name = "indicatorcode", description = "indicatorcode") @RequestParam(value = "indicatorcode") String indicatorcode,
                               @Parameter(name = "indicatorname", description = "indicatorname") @RequestParam(value = "indicatorname") String indicatorname,
                               @Parameter(name = "indicatordes", description = "indicatordes") @RequestParam(value = "indicatordes") String indicatordes,
                               @Parameter(name = "indicatorstatus", description = "indicatorstatus") @RequestParam(value = "indicatorstatus") String indicatorstatus,
                               @Parameter(name = "unittype", description = "unittype") @RequestParam(value = "unittype") String unittype,
                               @Parameter(name = "staffid", description = "staffid") @RequestParam(value = "staffid") String staffid,
                               @Parameter(name = "connectionstrings", description = "connectionstrings") @RequestParam(value = "connectionstrings") String connectionstrings,
                               @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,
                               @Parameter(name = "creater", description = "creater") @RequestParam(value = "creater") String creater,
                               @Parameter(name = "data", description = "data") @RequestParam(value = "data") String data,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        /* 修改结束 */
        Indicator indicator = iIndicatorService.getById(indicatorid);
        indicator.setIndicatorcode(indicatorcode);
        indicator.setIndicatorname(indicatorname);
        indicator.setIndicatordes(indicatordes);
        indicator.setIndicatorstatus(indicatorstatus);
        indicator.setUnittype(unittype);
        indicator.setStaffid(staff.getStaffid());
        indicator.setConnectionstrings(connectionstrings);
        if (StringUtils.isNotEmpty(data)) {
            try {
                indicator.setCreatedate(LocalDateTime.parse(data));
            } catch (Exception e) {
            }
        }
        Map map = new HashMap();
        try {
            iIndicatorService.updateById(indicator);
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
            Indicator indicator = iIndicatorService.getById(selectedruleid);
            if (Objects.isNull(indicator)) {
                return new JsonBean(500, "通过" + selectedruleid + "没有找到规则！！", null);
            }
            try {
                iIndicatorService.removeById(indicator);
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonBean(500, e.getMessage(), "");
            }
            return new JsonBean(200, "成功", map);
        }
        return new JsonBean(500, "请选择规则id", null);
    }


    @Transactional
    @Operation(summary = "hy_zbk_jsgs_add")
    @PostMapping(value = "/rule/hy_zbk_jsgs_add")
    public JsonBean hy_zbk_jsgs_add(@Parameter(name = "indicator", description = "indicator") @RequestBody Indicator indicator,
                                    @Parameter(name = "indicatorid", description = "indicatorid") @RequestParam(value = "indicatorid") String indicatorid,
                                    @Parameter(name = "formula", description = "formula") @RequestParam(value = "formula") String formula,
                                    @Parameter(name = "expressiondesc", description = "expressiondesc") @RequestParam(value = "expressiondesc") String expressiondesc,
                                    @Parameter(name = "expression", description = "expression") @RequestParam(value = "expression") String expression,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Indicator indicators = null;
        if (indicatorid != null && !"".equals(indicatorid)) {
            indicators = iIndicatorService.getById(indicatorid);
            indicators.setFormula(formula);
            indicators.setForlumachs(expression);
            indicators.setFormulades(expressiondesc);
            iIndicatorService.updateById(indicator);
            return new JsonBean(200, "成功", "");
        }
        return new JsonBean(500, "请选择规则id", null);
    }

    @Transactional
    @Operation(summary = "hy_kri_info_range_mng")
    @PostMapping(value = "/rule/hy_kri_info_range_mng")
    public JsonBean hy_kri_info_range_mng(@Parameter(name = "indicatorid", description = "indicatorid") @RequestParam(value = "indicatorid") String indicatorid,
                                          @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                                          @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (indicatorid != null && !"".equals(indicatorid)){
            List<Indicatorthreshold> list = indicatorthresholdService.QueryByIndicatorId(indicatorid);
            Map map = new HashMap();
            map.put("list", list);
            map.put("indicatorid", indicatorid);
            map.put("orgid", orgid);
            map.put("tableHistoryRows", list.size());
            map.put("choiceSearch", choiceSearch);
            if(choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            return new JsonBean(200, "成功", map);
        }
        return new JsonBean(500, "请选择规则id", null);
    }

    @Transactional
    @Operation(summary = "hy_kri_info_range_mng_find")
    @GetMapping(value = "/rule/hy_kri_info_range_mng_find")
    public JsonBean hy_kri_info_range_mng_find(@Parameter(name = "indicatorid", description = "indicatorid") @RequestParam(value = "indicatorid") String indicatorid,
                                                   @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                                                   @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,
                                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String tokent) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List<Indicatorthreshold> list = indicatorthresholdService.QueryByIndicatorId(indicatorid);
        if (indicatorid != null && !"".equals(indicatorid)){
            Map map = new HashMap();
            map.put("list", list);
            map.put("indicatorid", indicatorid);
            map.put("orgid", orgid);
            if(choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            return new JsonBean(200, "成功", map);
        }
        return new JsonBean(500, "请选择规则id", null);
    }

    @Transactional
    @Operation(summary = "hy_range_mng_add")
    @PostMapping(value = "/rule/hy_range_mng_add")
    public JsonBean hy_range_mng_add(@Parameter(name = "indicator", description = "indicator") @RequestBody Indicatorthreshold indicatorthreshold,
                                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String tokent
                                                  ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        indicatorthresholdService.save(indicatorthreshold);
        return new JsonBean(200, "成功", "");
    }
    @Transactional
    @Operation(summary = "hy_range_mng_del")
    @GetMapping(value = "/rule/hy_range_mng_del")
    public JsonBean hy_range_mng_del(@Parameter(name = "indicatorid", description = "indicatorid") @RequestParam(value = "indicatorid") String indicatorid,
                                         @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                                         @Parameter(name = "indicator", description = "indicator") @RequestBody String[] selectIds,
                                         @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,

                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("tokent") String tokent) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        for (int i = 0; i < selectIds.length; i++) {
            Indicatorthreshold indicatorthreshold = new Indicatorthreshold();
            indicatorthreshold.setThresholdid(new BigDecimal(selectIds[i]));
            indicatorthresholdService.removeById(indicatorthreshold);
        }
        return new JsonBean(200, "成功", "");
    }
    @Transactional
    @Operation(summary = "")
    @PostMapping(value = "/rule/hy_kri_info_range_mng_find")
    public @ResponseBody String hy_zbk_findnumber(@Parameter(name = "indicator", description = "indicator") @RequestBody Indicator indicator,
                                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String tokent) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return JsonBean.error("用户已失效");
 		}
        Integer count = iIndicatorService.selectIndicatorNumber(indicator.getIndicatorcode());
        return count.toString();
    }
}
