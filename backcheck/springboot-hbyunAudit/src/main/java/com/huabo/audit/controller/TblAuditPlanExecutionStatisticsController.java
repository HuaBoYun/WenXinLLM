package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.IpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAuditPlanExecutionStatisticsEntity;
import com.huabo.audit.service.TblAuditPlanExecutionStatisticsService;
import com.huabo.audit.util.ExcelTemplateExportUtil;
import com.huabo.audit.util.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 审计计划执行统计控制器
 * <p>提供成果汇总中审计计划执行及要点完成情况表的查询、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="成果汇总-审计计划执行及要点完成情况表",description="成果汇总-审计计划执行及要点完成情况表")
@RequestMapping(value = "/auditPlanExecutionStatistics")
public class TblAuditPlanExecutionStatisticsController {

    @Autowired
    private TblAuditPlanExecutionStatisticsService tblAuditPlanExecutionStatisticsService;

    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "审计计划执行列表信息",
            busType = "智能审计",
            fail = "审计计划执行列表信息",
            operationType = OperationType.SELECT,
            subType = "成果运用——审计计划执行及要点完成情况表列表信息"
    )
    @GetMapping("/getPage")
    @Operation(summary = "审计计划执行及要点完成情况表-列表")
    public JsonBean getPage(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                              @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                              TblAuditPlanExecutionStatisticsEntity entity) throws Exception {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, new HashMap<>());
        }

        JsonBean jsonBean = null;
        try {
            PageResult<TblAuditPlanExecutionStatisticsEntity> page = tblAuditPlanExecutionStatisticsService.page(pageNumber, pageSize, entity);
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("pageInfo", page);
            jsonBean= ResponseFormat.retParam(1, 200, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计计划执行详情信息",
            busType = "智能审计",
            fail = "审计计划执行详情信息",
            operationType = OperationType.SELECT,
            subType = "成果运用——审计计划执行及要点完成情况表列表指定记录详情信息"
    )
    @GetMapping("/getDetail")
    @Operation(summary = "审计计划执行及要点完成情况表-详情")
    public JsonBean getDetail(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                              @Parameter(name="id",description="id")@RequestParam(value = "id") BigDecimal id) throws Exception {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, new HashMap<>());
        }

        JsonBean jsonBean = null;
        try {
            TblAuditPlanExecutionStatisticsEntity byId = tblAuditPlanExecutionStatisticsService.getById(id);
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("data", byId);
            jsonBean= ResponseFormat.retParam(1, 200, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计计划执行及要点完成情况删除",
            busType = "智能审计",
            fail = "审计计划执行及要点完成情况删除",
            operationType = OperationType.DELETE,
            subType = "成果运用——审计计划执行及要点完成情况表列表指定记录的删除操作【{{id}}】"
    )
    @GetMapping("/remove")
    @Operation(summary = "审计计划执行及要点完成情况表-删除")
    public JsonBean remove(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                              @Parameter(name="id",description="id")@RequestParam(value = "id") BigDecimal id) throws Exception {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, new HashMap<>());
        }

        JsonBean jsonBean = null;
        try {
            boolean b = tblAuditPlanExecutionStatisticsService.removeById(id);
            jsonBean= ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计计划执行及要点完成情况新增或修改",
            busType = "智能审计",
            fail = "审计计划执行及要点完成情况新增或修改",
            operationType = OperationType.ADD,
            subType = "成果运用——审计计划执行及要点完成情况表-新增或修改操作"
    )
    @PostMapping(value = "/mergeInfo")
    @Operation(summary = "审计计划执行及要点完成情况表-新增或修改")
    public JsonBean mergembInfo(HttpServletRequest request,
                                @Parameter(name = "auditPlanExecutionStatistics", description = "审计计划执行及要点完成情况", required = true)TblAuditPlanExecutionStatisticsEntity entity,
                                @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token)throws Exception{
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, new HashMap<>());
        }

        JsonBean jsonBean = null;
        try {
            if (Objects.isNull(entity.getId())) {
                entity.setId(RandomUtil.uuBigDecimalId());
                entity.setCreateTime(new Date());
                tblAuditPlanExecutionStatisticsService.save(entity);
            } else {
                entity.setCreateTime(null);
                tblAuditPlanExecutionStatisticsService.updateById(entity);
            }
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("data",entity);
            jsonBean= ResponseFormat.retParam(1, 200, resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean=ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计计划执行及要点完成情况表导出操作",
            busType = "智能审计",
            fail = "审计计划执行及要点完成情况表导出操作",
            operationType = OperationType.EXPORT,
            subType = "成果运用——审计计划执行及要点完成情况表导出操作"
    )
    @Operation(summary = "审计计划执行及要点完成情况表-导出")
    @GetMapping("/export")
    public void exportByTemplate(HttpServletResponse response,TblAuditPlanExecutionStatisticsEntity entity,
                                 @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token) {
        try {

            //验证用户登录
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                throw new ServletException("用户已失效！");
            }

            // 查询数据
            List<TblAuditPlanExecutionStatisticsEntity> list = tblAuditPlanExecutionStatisticsService.list(entity);

            // 设置响应头
            //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("审计计划执行及要点完成情况表.xlsx"));

            // 导出Excel（模板文件放在resources/templates目录下）
            ExcelTemplateExportUtil.exportByTemplate("template/TBL_AUDIT_PLAN_EXECUTION_STATISTICS.xlsx", list, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
