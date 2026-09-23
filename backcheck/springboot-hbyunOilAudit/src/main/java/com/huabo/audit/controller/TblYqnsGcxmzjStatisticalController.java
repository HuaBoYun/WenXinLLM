package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.service.TblYqnsGcxmzjStatisticalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangys
 * @description 计划管理_工程项目造价 统计表 控制器
 * @createDate 2023-09-07 16:46:40
 */
@Tag(name="计划管理_工程项目造价 统计表",description="计划管理_工程项目造价 统计表")
@RestController
@RequestMapping(value = "/gcxmzj/statistical")
public class TblYqnsGcxmzjStatisticalController {

    @Resource
    TblYqnsGcxmzjStatisticalService tblYqnsGcxmzjStatisticalService;

    @Operation(summary = "工程项目造价表-建设单位统计表-接口")
    @RequestMapping(value = "/selectTblYqnsGcxmzjJsdwStatisticalList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean selectTblYqnsGcxmzjJsdwStatisticalList(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                                           @Parameter(name = "queryYear", description = "queryYear", required = false) @RequestParam(value = "queryYear", required = false) Integer queryYear

    ) {
        try {
            return this.tblYqnsGcxmzjStatisticalService.selectTblYqnsGcxmzjJsdwStatisticalList(token, queryYear);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "工程项目造价表-建设单位统计表-详情-接口")
    @RequestMapping(value = "/selectTblYqnsGcxmzjJsdwStatisticalToOne", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean selectTblYqnsGcxmzjJsdwStatisticalList(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                                           @Parameter(name = "jsdw", description = "建设单位", required = true) @RequestParam(value = "jsdw", required = false) String jsdw,
                                                           @Parameter(name = "queryYear", description = "queryYear", required = false) @RequestParam(value = "queryYear", required = false) Integer queryYear

    ) {
        try {
            return this.tblYqnsGcxmzjStatisticalService.selectTblYqnsGcxmzjJsdwStatisticalToOne(token, jsdw, queryYear);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "工程项目造价表-施工单位统计表-接口")
    @RequestMapping(value = "/selectTblYqnsGcxmzjSgdwStatisticalList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean selectTblYqnsGcxmzjSgdwStatisticalList(HttpServletRequest request, HttpServletResponse response,
                                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                                           @Parameter(name = "queryYear", description = "queryYear", required = false) @RequestParam(value = "queryYear", required = false) Integer queryYear
    ) {
        try {
            return this.tblYqnsGcxmzjStatisticalService.selectTblYqnsGcxmzjSgdwStatisticalList(token, queryYear);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "工程项目造价表-内外部 统计表-接口")
    @RequestMapping(value = "/selectTblYqnsGcxmzjNwbStatisticalList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean selectTblYqnsGcxmzjNwbStatisticalList(HttpServletRequest request,
                                                          HttpServletResponse response,
                                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                                          @Parameter(name = "queryYear", description = "queryYear", required = false) @RequestParam(value = "queryYear", required = false) Integer queryYear
    ) {
        try {
            return this.tblYqnsGcxmzjStatisticalService.selectTblYqnsGcxmzjNwbStatisticalList(token, queryYear);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "工程项目造价表-抽审表(按施工单位及额度) 统计表-接口")
    @RequestMapping(value = "/selectTblYqnsGcxmzjSampleStatisticalList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean selectTblYqnsGcxmzjSampleStatisticalList(HttpServletRequest request,
                                                             HttpServletResponse response,
                                                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                                             @Parameter(name = "queryYear", description = "queryYear", required = false) @RequestParam(value = "queryYear", required = false) Integer queryYear
    ) {
        try {
            return this.tblYqnsGcxmzjStatisticalService.selectTblYqnsGcxmzjSampleStatisticalList(token, queryYear);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


}

