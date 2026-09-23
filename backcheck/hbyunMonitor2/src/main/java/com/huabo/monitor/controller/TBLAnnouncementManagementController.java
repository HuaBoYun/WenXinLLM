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
import com.huabo.monitor.entity.TBLAnnouncementManagement;
import com.huabo.monitor.service.TBLAnnouncementManagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name="内控设置-公告维护",description="内控设置-公告维护")
@RequestMapping(value = "/ggwh")
public class TBLAnnouncementManagementController {
   @Autowired
   private TBLAnnouncementManagementService tblAnnouncementManagementService;

    @OperationLog(
            success = "公告维护-新建页面-保存成功",
            busType = "内控设置",
            fail = "公告维护-新建页面-保存失败",
            operationType = OperationType.ADD,
            subType = "公告维护"
    )
    @PostMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "公告维护-新建/修改功能")
    public JsonBean addPjgl(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody TBLAnnouncementManagement entity) throws Exception {
       return tblAnnouncementManagementService.saveOrUpdate(entity);
    }

    @OperationLog(
            success = "公告维护-列表查询成功",
            busType = "内控设置",
            fail = "公告维护-列表查询失败",
            operationType = OperationType.SELECT,
            subType = "公告维护"
    )
    @GetMapping(value = "/getHomepage_List")
    @Operation(summary = "公告维护-列表查询")
    public JsonBean getHomepage_List(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false,defaultValue = "1") Integer pageNumber,
                                     @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false,defaultValue = "20") Integer pageSize,
                                     @Parameter(name = "title", description = "标题") @RequestParam(value = "title", required = false) String title,
                                     @Parameter(name = "start", description = "失效时间段查询：开始时间") @RequestParam(value = "start", required = false)String start,
                                     @Parameter(name = "end", description = "失效时间段查询：结束时间") @RequestParam(value = "end", required = false) String end,
                                     @Parameter(name = "createTimeStart", description = "根据创建时间段查询：开始时间") @RequestParam(value = "createTimeStart",required = false) String createTimeStart,
                                     @Parameter(name = "createTimeEnd", description = "根据创建时间段查询：结束时间") @RequestParam(value = "createTimeEnd",required = false) String createTimeEnd,
                                     @Parameter(name = "type", description = "是否有效:1有效,其余无效") @RequestParam(value = "type",required = false) Integer type,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        return tblAnnouncementManagementService.getHomepage_List(pageNumber, pageSize, title, start, end,createTimeStart,createTimeEnd,type);
    }

    @OperationLog(
            success = "公告维护-删除成功",
            busType = "内控设置",
            fail = "公告维护-删除失败",
            operationType = OperationType.DELETE,
            subType = "公告维护"
    )
    @PostMapping(value = "/remove")
    @Operation(summary = "公告维护-删除")
    public @ResponseBody JsonBean remove(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="id",description="id") @RequestParam(value = "id") BigDecimal id) throws Exception{
        return tblAnnouncementManagementService.deleteById(id);
    }

    @OperationLog(
            success = "公告维护-详情查询成功",
            busType = "内控设置",
            fail = "公告维护-详情查询失败",
            operationType = OperationType.SELECT,
            subType = "公告维护"
    )
    @GetMapping(value = "/getDetail")
    @Operation(summary = "公告维护-详情")
    public  JsonBean plan_detail(
            @Parameter(name = "id", description = "id") @RequestParam(value = "id")  BigDecimal id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        return tblAnnouncementManagementService.getDetail(id);
    }

    @OperationLog(
            success = "公告维护-首页查询成功",
            busType = "内控设置",
            fail = "公告维护-首页查询失败",
            operationType = OperationType.SELECT,
            subType = "公告维护"
    )
    @GetMapping(value = "/getIndexList")
    @Operation(summary = "公告维护-首页查询")
    public JsonBean getIndexList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false,defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false,defaultValue = "20") Integer pageSize
            ) throws Exception {
        return tblAnnouncementManagementService.getIndexList(pageNumber,pageSize);
    }
}
