package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.BugOuterrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.service.impl.BugOuterruleServiceImpl;
import com.huabo.fxgl.service.impl.OuterruleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-05
 */
@RestController
@RequestMapping(value = "/outerrule", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="外规API",description="外规API")

public class OuterruleController {
    @Autowired
    private OuterruleServiceImpl outerruleService;

    @Autowired
    private BugOuterruleServiceImpl bugOuterruleService;

    @Resource
    private UserProvider userProvider;

    /**
     * 相关问题汇总 - 缺陷管理 -新建-关联外规
     * @auther mamingxu
     * @since 2022/8/15
     * @version 1.0.1
     * @return
     */
    @OperationLog(
            success = "相关问题汇总 - 缺陷管理 -新建-关联外规处理成功",
            busType = "外规",
            fail = "相关问题汇总 - 缺陷管理 -新建-关联外规处理失败",
            operationType = OperationType.SELECT,
            subType = "外规"
    )
    @RequestMapping(value = "/outer_common_qxwt")
    @Operation(summary = "相关问题汇总 - 缺陷管理 -新建-关联外规/outer_common_qxwt")
    public JsonBean outer_common_qxwt(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(required = false) String selectProjectid,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                                      @Parameter(name = "czurl", description = "czurl")@RequestParam(required = false) String czurl,
                                      @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(name = "choiceSearch", required = false) String choiceSearch,
                                      @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(defaultValue = "1") Integer pageNo,
                                      @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织*/
        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        IPage page = new Page(pageNo,pageSize);//分页设置
        IPage<Outerrule> innerRuleByBugPageBean = outerruleService.findOuterRuleByBugPageBean(page, selectProjectid, selectOrg.getOrgid().toString());
        result.put("pageBean", innerRuleByBugPageBean);
        result.put("czurl", czurl);
        result.put("type", type);
        result.put("selectProjectid", selectProjectid);
        result.put("orgid",orgid);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        result.put("choiceSearch", choiceSearch );
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * 保存缺陷关联外规
     * @auther mamingxu
     * @since 2022/8/15
     * @version 1.0.1
     * @return
     */
    @OperationLog(
            success = "保存缺陷关联外规处理成功",
            busType = "外规",
            fail = "保存缺陷关联外规处理失败",
            operationType = OperationType.ADD,
            subType = "外规"
    )
    @RequestMapping(value = "/qxwt/add_outer_qxwt")
    @Operation(summary = "保存缺陷关联外规/qxwt/add_outer_qxwt")

    public String add_outer_qxwt(@Parameter(name = "conid", description = "conid") @RequestParam(name = "conid", required = false) String conid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "innrulid", description = "innrulid") @RequestParam(name = "innrulid", required = false) String outerid
                                                                     ) {
        if (conid != null && outerid != null) {
            List<BugOuterrule> list = bugOuterruleService.isIfFlowInner(conid,outerid);
            if (list == null || list.size() == 0) {
                BugOuterrule ou = new BugOuterrule();
                ou.setBugid(new BigDecimal(conid));
                ou.setOuterid(Integer.parseInt(outerid));
                bugOuterruleService.saveOrUpdate(ou);
                //tblBugOuterRuleService.saveTblBugOuterRule(ou);
            }
        }
        return "";
    }

}
