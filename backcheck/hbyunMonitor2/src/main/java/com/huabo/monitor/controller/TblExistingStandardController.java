package com.huabo.monitor.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblExistingStandardVo;
import com.huabo.monitor.service.TblExistingStandardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name="内控测试-现行标准",description="内控测试-现行标准")
@RequestMapping(value = "/xxbz")
public class TblExistingStandardController {
    @Autowired
    private TblExistingStandardService tblExistingStandardService;

    @OperationLog(
            success = "现行标准-新建页面-保存成功",
            busType = "内控测试",
            fail = "现行标准-新建页面-保存失败",
            operationType = OperationType.ADD,
            subType = "现行标准"
    )
    @PostMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "现行标准-新建/修改功能")
    public JsonBean addPjgl(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TblExistingStandardVo entity
            ) throws Exception {
        return tblExistingStandardService.insertOrUpdate(entity);
    }

    @OperationLog(
            success = "现行标准-删除成功",
            busType = "内控测试",
            fail = "现行标准-删除失败",
            operationType = OperationType.DELETE,
            subType = "现行标准"
    )
    @PostMapping(value = "/remove")
    @Operation(summary = "现行标准-删除")
    public @ResponseBody JsonBean remove(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="id",description="id") @RequestParam(value = "id") BigDecimal id) throws Exception{
        return tblExistingStandardService.deleteById(id);
    }

    @OperationLog(
            success = "现行标准-列表查询成功",
            busType = "内控测试",
            fail = "现行标准-列表查询失败",
            operationType = OperationType.SELECT,
            subType = "现行标准"
    )
    @GetMapping(value = "/getHomepage_List")
    @Operation(summary = "现行标准-列表查询")
    public JsonBean getHomepage_List(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false,defaultValue = "1") Integer pageNumber,
                                     @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false,defaultValue = "20") Integer pageSize,
                                     @Parameter(name = "ruleNumber", description = "发文文号") @RequestParam(value = "ruleNumber", required = false) String ruleNumber,
                                     @Parameter(name = "ruleName", description = "文件名称") @RequestParam(value = "ruleName", required = false)String ruleName,
                                     @Parameter(name = "summaryInfo", description = "摘要内容") @RequestParam(value = "summaryInfo", required = false) String summaryInfo,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        return tblExistingStandardService.selectList(ruleNumber,ruleName,summaryInfo,pageNumber, pageSize);
    }

    @OperationLog(
            success = "现行标准-详情查询成功",
            busType = "内控测试",
            fail = "现行标准-详情查询失败",
            operationType = OperationType.SELECT,
            subType = "现行标准"
    )
    @GetMapping(value = "/getDetail")
    @Operation(summary = "现行标准-详情")
    public  JsonBean plan_detail(
            @Parameter(name = "id", description = "id") @RequestParam(value = "id")  BigDecimal id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        return tblExistingStandardService.selectById(id);
    }

    @OperationLog(
            success = "现行标准-预览成功",
            busType = "内控测试",
            fail = "现行标准-预览失败",
            operationType = OperationType.SELECT,
            subType = "现行标准"
    )
    @GetMapping(value = "/preview")
    @Operation(summary = "现行标准-预览")
    public  JsonBean preview(
            @Parameter(name = "id", description = "id") @RequestParam(value = "id")  BigDecimal id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        return tblExistingStandardService.previewById(id);
    }

    @OperationLog(
            success = "现行标准-删除附件成功",
            busType = "内控测试",
            fail = "现行标准-删除附件失败",
            operationType = OperationType.DELETE,
            subType = "现行标准"
    )
    @PostMapping(value = "/deleteAttachment")
    @Operation(summary = "现行标准-删除附件")
    public JsonBean deleteAttachment(
            @Parameter(name = "attid", description = "附件ID", required = true) @RequestParam(value = "attid") BigDecimal attid,
            @Parameter(name = "tesId", description = "现行标准ID", required = true) @RequestParam(value = "tesId") BigDecimal tesId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        return tblExistingStandardService.deleteAttachment(attid, tesId, token);
    }

}
