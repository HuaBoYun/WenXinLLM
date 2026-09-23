package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import com.financial.sharing.service.TrialBalanceService;
import com.financial.sharing.vo.param.TrialBalanceParam;
import com.financial.sharing.vo.result.TrialBalanceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 试算平衡表控制器
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/trial-balance")
@CrossOrigin
@Api(tags = "试算平衡表")
public class TrialBalanceController {

    @Resource
    private TrialBalanceService trialBalanceService;

    @ApiOperation("生成试算平衡表")
    @PostMapping("/generate")
    public String generateTrialBalance(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody TrialBalanceParam param) {
        try {
            log.info("生成试算平衡表，参数：{}", param);
            TrialBalanceResult result = trialBalanceService.generateTrialBalance(param);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("试算平衡");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("生成试算平衡表失败", e);
            return createErrorResponse("生成试算平衡表失败: " + e.getMessage());
        }
    }

    @ApiOperation("试算平衡校验")
    @PostMapping("/check")
    public String checkBalance(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestBody TrialBalanceParam param) {
        try {
            log.info("试算平衡校验，参数：{}", param);
            TrialBalanceResult result = trialBalanceService.checkBalance(param);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("校验通过");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("试算平衡校验失败", e);
            return createErrorResponse("校验失败: " + e.getMessage());
        }
    }

    @ApiOperation("异步生成试算平衡表")
    @PostMapping("/async")
    public String asyncGenerateTrialBalance(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody TrialBalanceParam param) {
        try {
            log.info("异步生成试算平衡表，参数：{}", param);
            String taskId = trialBalanceService.asyncGenerateTrialBalance(param);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("任务已创建");
            json.setData(taskId);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("创建异步试算平衡任务失败", e);
            return createErrorResponse("创建任务失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询试算平衡进度")
    @GetMapping("/progress/{taskId}")
    public String getTrialBalanceProgress(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @PathVariable String taskId) {
        try {
            log.info("查询试算平衡进度，任务ID：{}", taskId);
            Object progress = trialBalanceService.getTrialBalanceProgress(taskId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(progress);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询试算平衡进度失败", e);
            return createErrorResponse("查询进度失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取历史试算平衡记录")
    @GetMapping("/history")
    public String getTrialBalanceHistory(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @ApiParam(value = "账簿ID", required = true) @RequestParam Long bookId,
                                       @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId,
                                       @ApiParam(value = "会计期间") @RequestParam(required = false) String period) {
        try {
            log.info("查询历史试算平衡记录，bookId：{}，tenantId：{}，period：{}", bookId, tenantId, period);
            List<TrialBalanceResult> history = trialBalanceService.getTrialBalanceHistory(bookId, tenantId, period);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(history);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询历史试算平衡记录失败", e);
            return createErrorResponse("查询历史记录失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取可用的会计期间列表")
    @GetMapping("/periods")
    public String getAvailablePeriods(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @ApiParam(value = "账簿ID", required = true) @RequestParam Long bookId,
                                     @ApiParam(value = "租户ID", required = true) @RequestParam Long tenantId) {
        try {
            log.info("查询可用会计期间，bookId：{}，tenantId：{}", bookId, tenantId);
            List<String> periods = trialBalanceService.getAvailablePeriods(bookId, tenantId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(periods);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询可用会计期间失败", e);
            return createErrorResponse("查询期间失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出试算平衡表")
    @PostMapping("/export")
    public void exportTrialBalance(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestBody TrialBalanceParam param) {
        try {
            log.info("导出试算平衡表，参数：{}", param);
            trialBalanceService.exportTrialBalance(param, response);
        } catch (Exception e) {
            log.error("导出试算平衡表失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    @ApiOperation("获取试算平衡配置")
    @GetMapping("/config")
    public String getTrialBalanceConfig(HttpServletRequest request,
                                       HttpServletResponse response) {
        try {
            log.info("获取试算平衡配置");

            // 返回试算平衡配置信息
            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(new java.util.HashMap<String, Object>() {{
                put("subjectTypes", new Object[]{
                    new java.util.HashMap<String, Object>() {{
                        put("value", 1);
                        put("label", "资产类");
                    }},
                    new java.util.HashMap<String, Object>() {{
                        put("value", 2);
                        put("label", "负债类");
                    }},
                    new java.util.HashMap<String, Object>() {{
                        put("value", 3);
                        put("label", "权益类");
                    }},
                    new java.util.HashMap<String, Object>() {{
                        put("value", 4);
                        put("label", "损益类");
                    }},
                    new java.util.HashMap<String, Object>() {{
                        put("value", 5);
                        put("label", "成本类");
                    }}
                });
                put("subjectLevels", new Object[]{
                    new java.util.HashMap<String, Object>() {{
                        put("value", 0);
                        put("label", "全部级次");
                    }},
                    new java.util.HashMap<String, Object>() {{
                        put("value", 1);
                        put("label", "一级科目");
                    }},
                    new java.util.HashMap<String, Object>() {{
                        put("value", 2);
                        put("label", "二级科目");
                    }},
                    new java.util.HashMap<String, Object>() {{
                        put("value", 3);
                        put("label", "三级科目");
                    }},
                    new java.util.HashMap<String, Object>() {{
                        put("value", 4);
                        put("label", "明细科目");
                    }}
                });
                put("defaultTolerance", 0.01);
                put("reportFormats", new String[]{"HTML", "Excel", "PDF"});
            }});
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("获取试算平衡配置失败", e);
            return createErrorResponse("获取配置失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除试算平衡记录")
    @DeleteMapping("/{taskId}")
    public String deleteTrialBalance(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @PathVariable String taskId) {
        try {
            log.info("删除试算平衡记录，任务ID：{}", taskId);
            boolean result = trialBalanceService.deleteTrialBalance(taskId);

            JsonBean json = new JsonBean();
            json.setCode(result ? 1 : 0);
            json.setMsg(result ? "删除成功" : "删除失败");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("删除试算平衡记录失败", e);
            return createErrorResponse("删除失败: " + e.getMessage());
        }
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.nonNullMapper().toJson(json);
    }
}