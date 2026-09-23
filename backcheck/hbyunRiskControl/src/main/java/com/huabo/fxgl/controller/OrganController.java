package com.huabo.fxgl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.service.IOrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 组织机构选择控制器
 * <p>提供风险管控模块中组织机构的树形选择接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping(value = "/organ", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="组织机构选择",description="组织机构选择")
public class OrganController {


    @Autowired
    private IOrganizationService organizationService;

    /*status:ok pass
    * @author zuoshun
    * @version v1.0.1
    * @Description 获取组织树
    * @Date 2022/8/10
    * @param str
    * @param orgid
    * @param hbOrgEntity
    * @return com.hbfk.util.JsonBean
    * @url: http://localhost:8081/organ/radio_orgarea_window?str=1&orgid=111366
    **/
    @OperationLog(
            success = "风险报告编制——新建——报告部门 处理成功",
            busType = "组织机构选择",
            fail = "风险报告编制——新建——报告部门 处理失败",
            operationType = OperationType.SELECT,
            subType = "组织机构选择"
    )
    @Operation(summary = "风险报告编制——新建——报告部门 /organ/radio_orgarea_window")
    @RequestMapping(value = "/radio_orgarea_window")
    public JsonBean radioOrgareaWindow(@Parameter(description="str") @RequestParam(required = false) String str,
                                       @RequestParam(required = false) String orgid,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return organizationService.getOrganizationTree(str,orgid,token);
    }





}
