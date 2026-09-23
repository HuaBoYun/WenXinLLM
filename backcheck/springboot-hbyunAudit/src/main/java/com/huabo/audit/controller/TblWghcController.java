package com.huabo.audit.controller;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
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
@RequestMapping(value = "/wghc/projectTeam")
public class TblWghcController {
    @Autowired
    TblWgzzWghcService tblWgzzWghcService;

    @OperationLog(
            success = "违规核查报告删除记录",
            busType = "整改追责",
            fail = "违规核查报告删除记录",
            operationType = OperationType.DELETE,
            subType = "违规追责——违规核查报告——删除记录"
    )
    @GetMapping("/wghcList")
    @Operation(summary = "违规核查列表")
    public JsonBean getByWghslist(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="creator",description="creator",required=false)@RequestParam(value = "creator",required=false) String creator,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize
                             ){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghcService.selectwghcBy(token,creator,pageNumber,pageSize);
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
            subType = "违规追责——违规核查报告——获取指定记录详情"
    )
    @GetMapping("/wghcXQList")
    @Operation(summary = "违规核查列表详情")
    public JsonBean getByWghsXQlist(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id") BigDecimal id

    ){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghcService.selectwghcXQBy(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "违规核查结束",
            busType = "整改追责",
            fail = "违规核查结束",
            operationType = OperationType.UPDATE,
            subType = "违规追责——违规核查结束"
    )
    @PostMapping("/wghcJs")
    @Operation(summary = "违规核查结束")
    public JsonBean getByWghsJs(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id
    ){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghcService.updateStatus(token,id,1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


}
