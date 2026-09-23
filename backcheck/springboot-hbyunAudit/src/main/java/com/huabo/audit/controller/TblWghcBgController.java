package com.huabo.audit.controller;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblWgzzWghcBg;
import com.huabo.audit.service.TblWghcBgService;
import com.huabo.audit.service.TblWgzzWghcService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.controller
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/5/4
 * @Time:13:15
 */
@RestController
@Slf4j
@Tag(name="违规追责",description="违规追责")
@RequestMapping(value = "/wghcbg/projectTeam")
public class TblWghcBgController {
    @Autowired
    TblWghcBgService tblWghcBgService;

    @Autowired
    TblWgzzWghcService tblWgzzWghcService;
    @OperationLog(
            success = "违规核查报告列表",
            busType = "整改追责",
            fail = "违规核查报告列表",
            operationType = OperationType.SELECT,
            subType = "违规追责——违规核查-列表页相关信息"
    )
    @GetMapping("/wghcbgList")
    @Operation(summary = "违规核查报告列表")
    public JsonBean wghcbgList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="clueNaber",description="clueNaber",required=false)@RequestParam(value = "clueNaber",required=false) String clueNaber,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize
                             ){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWghcBgService.getByWghcBgList(token,pageNumber,pageSize,clueNaber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规核查报告记录详情",
            busType = "整改追责",
            fail = "违规核查报告记录详情",
            operationType = OperationType.SELECT,
            subType = "违规追责——违规核查指定记录的详细信息"
    )
    @GetMapping("/wghcbgDetail")
    @Operation(summary = "违规核查报详情")
    public JsonBean wghcbgDetail(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                               @Parameter(name="wghcid",required=false)@RequestParam("wghcid") BigDecimal wghcid){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWghcBgService.detail(token,wghcid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规核查报告新增修改记录",
            busType = "整改追责",
            fail = "违规核查报告新增修改记录",
            operationType = OperationType.ADD,
            subType = "违规追责——违规核查报告新增修改记录"
    )
    @RequestMapping(value = "/wghcbgSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "违规核查报告新增/修改")
    public JsonBean wghcbgSave(	@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                      @Parameter(name = "tblWgzzWghcBg", description = "实体", required = false) TblWgzzWghcBg tblWgzzWghcBg)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWghcBgService.addlist(token,tblWgzzWghcBg);
            jsonBean = tblWgzzWghcService.updateStatus(token,tblWgzzWghcBg.getWghcid(),2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规核查报告删除记录",
            busType = "整改追责",
            fail = "违规核查报告删除记录",
            operationType = OperationType.DELETE,
            subType = "违规追责——违规核查报告——删除记录"
    )
    @PostMapping("/wghsBgRemove")
    @Operation(summary = "违规核查报告删除")
    public JsonBean wghsBgRemove(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id
    )
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWghcBgService.removeList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
}
