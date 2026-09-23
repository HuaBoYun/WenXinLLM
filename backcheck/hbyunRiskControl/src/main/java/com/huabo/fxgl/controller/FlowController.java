package com.huabo.fxgl.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Flow;
import com.huabo.fxgl.service.IFlowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@RestController
@RequestMapping(value = "/flow", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="业务流程API",description="业务流程API")
public class FlowController {
    @Autowired
    private IFlowService flowService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "业务流程列表查询处理成功",
            busType = "业务流程",
            fail = "业务流程列表查询处理失败",
            operationType = OperationType.SELECT,
            subType = "业务流程"
    )
    @RequestMapping(value = "/list", produces = "application/json; charset=utf-8")
    @Operation(summary = "业务流程列表 /flow/list")
    public List<Flow> to_ywlc_copy(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        return flowService.findList(selectOrg.getOrgid());
    }
}
