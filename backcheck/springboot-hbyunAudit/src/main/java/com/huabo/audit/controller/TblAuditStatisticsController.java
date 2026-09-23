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
import com.huabo.audit.oracle.entity.TblAuditStatisticsEntity;
import com.huabo.audit.service.TblAuditStatisticsService;
import com.huabo.audit.util.ExcelTemplateExportUtil;
import com.huabo.audit.util.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 审计情况统计控制器
 * <p>提供成果汇总中审计情况统计表的查询、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="成果汇总-审计情况统计表",description="成果汇总-审计情况统计表")
@RequestMapping(value = "/auditStatistics")
public class TblAuditStatisticsController {

    @Autowired
    private TblAuditStatisticsService tblAuditStatisticsService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "审计情况统计表列表信息",
            busType = "智能审计",
            fail = "审计情况统计表列表信息",
            operationType = OperationType.SELECT,
            subType = "成果运用——审计情况统计表列表查询操作"
    )
    @GetMapping("/getPage")
    @Operation(summary = "审计情况统计表-列表")
    public JsonBean getPage(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                              @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                              TblAuditStatisticsEntity entity) throws Exception {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, new HashMap<>());
        }

        JsonBean jsonBean = null;
        try {
            PageResult<TblAuditStatisticsEntity> page = tblAuditStatisticsService.page(pageNumber, pageSize, entity);
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
            success = "审计情况统计表详情信息",
            busType = "智能审计",
            fail = "审计情况统计表详情信息",
            operationType = OperationType.SELECT,
            subType = "成果运用——审计情况统计表列表查询指定记录详情信息"
    )
    @GetMapping("/getDetail")
    @Operation(summary = "审计情况统计表-详情")
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
            TblAuditStatisticsEntity byId = tblAuditStatisticsService.getById(id);
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
            success = "审计情况统计表删除",
            busType = "智能审计",
            fail = "审计情况统计表删除",
            operationType = OperationType.DELETE,
            subType = "成果运用——审计情况统计表删除指定记录"
    )
    @GetMapping("/remove")
    @Operation(summary = "审计情况统计表-删除")
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
            boolean b = tblAuditStatisticsService.removeById(id);
            jsonBean= ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计情况统计表新增修改",
            busType = "智能审计",
            fail = "审计情况统计表新增修改",
            operationType = OperationType.ADD,
            subType = "成果运用——新增修改审计情况统计表"
    )
    @PostMapping(value = "/mergeInfo")
    @Operation(summary = "审计情况统计表-新增或修改")
    public JsonBean mergembInfo(HttpServletRequest request,
                                @Parameter(name = "auditStatistics", description = "审计情况统计", required = true) TblAuditStatisticsEntity entity,
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
                tblAuditStatisticsService.save(entity);
            } else {
                entity.setCreateTime(null);
                tblAuditStatisticsService.updateById(entity);
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
            success = "审计情况统计表导出",
            busType = "智能审计",
            fail = "审计情况统计表导出",
            operationType = OperationType.EXPORT,
            subType = "成果运用——审计情况统计表导出列表信息"
    )
    @Operation(summary = "审计情况统计表-导出")
    @GetMapping("/export")
    public void exportByTemplate(HttpServletResponse response,TblAuditStatisticsEntity entity,
                                 @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token) {
        try {
            //验证用户登录
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                throw new ServletException("用户已失效！");
            }

            // 查询数据
            List<TblAuditStatisticsEntity> list = tblAuditStatisticsService.list(entity);

            // 设置响应头
            /*response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");*/
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("审计情况统计.xlsx"));
            // 导出Excel（模板文件放在resources/templates目录下）
            ExcelTemplateExportUtil.exportByTemplate("template/TBL_AUDIT_STATISTICS.xlsx", list, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
