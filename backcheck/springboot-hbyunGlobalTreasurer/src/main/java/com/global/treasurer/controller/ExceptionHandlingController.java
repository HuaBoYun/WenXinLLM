package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.global.treasurer.entity.*;
import com.global.treasurer.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Claude
 * @date 2026-01-20
 * @description 异常处理管理Controller
 */
@RestController
@RequestMapping("/exception-handling")
@Api(tags = "异常处理管理")
public class ExceptionHandlingController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlingController.class);

    @Resource
    private TblExceptionHandlingService tblExceptionHandlingService;

    /**
     * 分页查询异常处理
     */
    @GetMapping("/page")
    @ApiOperation("分页查询异常处理")
    public String getExceptionPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("异常编号") @RequestParam(required = false) String exceptionNo,
            @ApiParam("异常类型") @RequestParam(required = false) String exceptionType,
            @ApiParam("异常级别") @RequestParam(required = false) String exceptionLevel,
            @ApiParam("异常状态") @RequestParam(required = false) String exceptionStatus,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblExceptionHandling> pageInfo = tblExceptionHandlingService.getExceptionPage(pageNo, pageSize, exceptionNo, exceptionType, exceptionLevel, exceptionStatus, startDate, endDate);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询异常处理分页数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询异常处理
     */
    @GetMapping("/{exceptionId}")
    @ApiOperation("根据ID查询异常处理")
    public String getExceptionById(
            @ApiParam("异常ID") @PathVariable String exceptionId,
            HttpServletResponse response) throws IOException {

        try {
            TblExceptionHandling exception = tblExceptionHandlingService.getExceptionById(exceptionId);
            return JsonBean.success(exception);
        } catch (Exception e) {
            log.error("查询异常处理详情失败, exceptionId={}", exceptionId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建异常记录
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("创建异常记录")
    public String createException(
            @ApiParam("异常信息") @FlexibleRequestBody TblExceptionHandling exception,
            HttpServletResponse response) throws IOException {

        try {
            // 打印接收到的数据
            log.info("========== 开始接收异常处理数据 ==========");
            log.info("接收到的异常对象: {}", exception);
            log.info("exceptionId: {}", exception.getExceptionId());
            log.info("exceptionNo: {}", exception.getExceptionNo());
            log.info("exceptionType: {}", exception.getExceptionType());
            log.info("exceptionLevel: {}", exception.getExceptionLevel());
            log.info("exceptionTitle: {}", exception.getExceptionTitle());
            log.info("exceptionDesc: {}", exception.getExceptionDesc());
            log.info("exceptionTime: {}", exception.getExceptionTime());
            log.info("handleBy: {}", exception.getHandleBy());
            log.info("handleResult: {}", exception.getHandleResult());
            log.info("exceptionStatus: {}", exception.getExceptionStatus());
            log.info("========== 数据接收完毕 ==========");

            TblExceptionHandling saved = tblExceptionHandlingService.saveException(exception);

            // 打印保存后的数据
            log.info("========== 保存后的数据 ==========");
            log.info("保存后 exceptionId: {}", saved.getExceptionId());
            log.info("保存后 exceptionTitle: {}", saved.getExceptionTitle());
            log.info("保存后 exceptionDesc: {}", saved.getExceptionDesc());
            log.info("========== 数据保存完毕 ==========");

            return JsonBean.success("创建成功", saved);
        } catch (Exception e) {
            log.error("创建异常记录失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 处理异常
     */
    @PostMapping("/{exceptionId}/handle")
    @ApiOperation("处理异常")
    public String handleException(
            @ApiParam("异常ID") @PathVariable String exceptionId,
            @ApiParam("处理方式") @RequestParam String handleMethod,
            @ApiParam("处理结果") @RequestParam String handleResult,
            HttpServletResponse response) throws IOException {

        try {
            tblExceptionHandlingService.handleException(exceptionId, handleMethod, handleResult);
            return JsonBean.success("处理成功");
        } catch (Exception e) {
            log.error("处理异常失败, exceptionId={}", exceptionId, e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 重试异常
     */
    @PostMapping("/{exceptionId}/retry")
    @ApiOperation("重试异常")
    public String retryException(
            @ApiParam("异常ID") @PathVariable String exceptionId,
            HttpServletResponse response) throws IOException {

        try {
            String exceptionNo = tblExceptionHandlingService.retryException(exceptionId);
            return JsonBean.success("重试成功", exceptionNo);
        } catch (Exception e) {
            log.error("重试异常失败, exceptionId={}", exceptionId, e);
            return new JsonBean(0, "重试失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 关闭异常
     */
    @PutMapping("/{exceptionId}/close")
    @ApiOperation("关闭异常")
    public String closeException(
            @ApiParam("异常ID") @PathVariable String exceptionId,
            @ApiParam("关闭备注") @RequestParam(required = false) String closeRemark,
            HttpServletResponse response) throws IOException {

        try {
            tblExceptionHandlingService.closeException(exceptionId, closeRemark);
            return JsonBean.success("关闭成功");
        } catch (Exception e) {
            log.error("关闭异常失败, exceptionId={}", exceptionId, e);
            return new JsonBean(0, "关闭失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取异常统计
     */
    @GetMapping("/statistics")
    @ApiOperation("获取异常统计")
    public String getExceptionStatistics(HttpServletResponse response) throws IOException {

        try {
            Map<String, Object> statistics = tblExceptionHandlingService.getExceptionStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("查询异常统计失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}

