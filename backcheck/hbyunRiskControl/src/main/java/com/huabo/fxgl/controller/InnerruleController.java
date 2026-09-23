package com.huabo.fxgl.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.RiskInnerrule;
import com.huabo.fxgl.service.impl.InnerruleServiceImpl;
import com.huabo.fxgl.service.impl.RiskInnerruleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@RestController
@Tag(name="内规API",description="内规API")
@RequestMapping(value = "/innerrule", method = {RequestMethod.GET, RequestMethod.POST})
public class InnerruleController {
    @Autowired
    private InnerruleServiceImpl innerruleService;

    @Autowired
    private RiskInnerruleServiceImpl riskInnerruleService;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 缺陷库-关联内规
     * auther mamingxu
     * version 1.0.1
     * datatime 2022/8/13
     *
     * @param
     * @return
     */
    @OperationLog(
            success = "缺陷库-关联内规处理成功",
            busType = "内规",
            fail = "缺陷库-关联内规处理失败",
            operationType = OperationType.SELECT,
            subType = "内规"
    )
    @RequestMapping(value = "/inner_common_qxwt")
    @Operation(summary = "缺陷库-关联内规/inner_common_qxwt")
    public JsonBean inner_common_qxwt(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(required = false) String selectProjectid,
                                      @Parameter(name = "limit", description = "limit") @RequestParam(defaultValue = "20") Integer pageSize,
                                      @Parameter(name = "czurl", description = "czurl") @RequestParam(required = false) String czurl,
                                      @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
                                      @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(defaultValue = "1") Integer pageNo,
                                      @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "bugid", description = "bugid") @RequestParam(required = false) String bugid
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织*/
        if (orgid == null || orgid == "") {
            orgid = selectOrg.getOrgid().toString();
        }

        IPage page = new Page(pageNo, pageSize);//分页设置

        IPage<Innerrule> innerRuleByBugPageBean = innerruleService.findInnerRuleByBugPageBean(page, bugid, orgid);

        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        result.put("pageBean", innerRuleByBugPageBean);
        result.put("selectProjectid", selectProjectid);
        result.put("type", type);
        result.put("orgid", orgid);
        result.put("czurl", czurl);
        result.put("orgid", orgid);
        //为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        result.put("choiceSearch", choiceSearch);
        jsonBean.setMsg("success");
        jsonBean.setCode(200);
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * 风险创建---内外规--内规---删除
     * auther mamingxu
     * version 1.0.1
     * datatime 2022/8/15
     */
    @OperationLog(
            success = "内规---删除处理成功",
            busType = "风险创建",
            fail = "内规---删除处理失败",
            operationType = OperationType.DELETE,
            subType = "内外规"
    )
    @RequestMapping(value = "/delete_internal_regulations")
    @Operation(summary = "风险创建---内外规--内规---删除/delete_internal_regulations")

    public String delete_internal_regulations(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            RiskInnerrule riskInnerRule) {
        if (riskInnerRule != null && riskInnerRule.getInnrulid() != null) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("INNRULID", riskInnerRule.getInnrulid());
            queryWrapper.eq("RISKID", riskInnerRule.getRiskid());
            riskInnerruleService.remove(queryWrapper);

        }
        return "";
    }


}
