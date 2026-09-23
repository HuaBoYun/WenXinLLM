package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
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
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblCurrentYearAuditIssuesEntity;
import com.huabo.audit.service.TblCurrentYearAuditIssuesService;
import com.huabo.audit.util.ExcelTemplateExportUtil;
import com.huabo.audit.util.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 本年度审计问题整改控制器
 * <p>提供成果汇总中本年度内部审计发现问题及整改情况的查询、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="成果汇总-本年度内部审计发现问题及整改情况",description="成果汇总-本年度内部审计发现问题及整改情况")
@RequestMapping(value = "/currentYearAuditIssues")
public class TblCurrentYearAuditIssuesController {

    @Autowired
    private TblCurrentYearAuditIssuesService tblCurrentYearAuditIssuesService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "本年度内部审计发现问题及整改情况列表信息",
            busType = "智能审计",
            fail = "本年度内部审计发现问题及整改情况列表信息",
            operationType = OperationType.SELECT,
            subType = "成果运用——本年度内部审计发现问题及整改情况列表信息"
    )
    @GetMapping("/getPage")
    @Operation(summary = "本年度内部审计发现问题及整改情况-列表")
    public JsonBean getPage(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                              @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                              TblCurrentYearAuditIssuesEntity entity) throws Exception {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, new HashMap<>());
        }

        JsonBean jsonBean = null;
        try {
            PageResult<TblCurrentYearAuditIssuesEntity> page = tblCurrentYearAuditIssuesService.page(pageNumber, pageSize, entity);
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
            success = "本年度内部审计发现问题及整改情况记录详细信息",
            busType = "智能审计",
            fail = "本年度内部审计发现问题及整改情况记录详细信息",
            operationType = OperationType.SELECT,
            subType = "成果运用——本年度内部审计发现问题及整改情况记录详细信息"
    )
    @GetMapping("/getDetail")
    @Operation(summary = "本年度内部审计发现问题及整改情况-详情")
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
            TblCurrentYearAuditIssuesEntity byId = tblCurrentYearAuditIssuesService.getById(id);
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
            success = "本年度内部审计发现问题及整改情况删除记录",
            busType = "智能审计",
            fail = "本年度内部审计发现问题及整改情况删除记录",
            operationType = OperationType.DELETE,
            subType = "成果运用——本年度内部审计发现问题及整改情况记录删除操作"
    )
    @GetMapping("/remove")
    @Operation(summary = "本年度内部审计发现问题及整改情况-删除")
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
            boolean b = tblCurrentYearAuditIssuesService.removeById(id);
            jsonBean= ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "本年度内部审计发现问题及整改情况新增修改记录",
            busType = "智能审计",
            fail = "本年度内部审计发现问题及整改情况新增修改记录",
            operationType = OperationType.UPDATE,
            subType = "成果运用——本年度内部审计发现问题及整改情况记录新增修改操作"
    )
    @PostMapping(value = "/mergeInfo")
    @Operation(summary = "本年度内部审计发现问题及整改情况-新增或修改")
    public JsonBean mergembInfo(HttpServletRequest request,
                                @Parameter(name = "currentYearAuditIssues", description = "本年度内部审计发现问题及整改情况", required = true) TblCurrentYearAuditIssuesEntity entity,
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
                tblCurrentYearAuditIssuesService.save(entity);
            } else {
                entity.setCreateTime(null);
                tblCurrentYearAuditIssuesService.updateById(entity);
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
            success = "本年度内部审计发现问题及整改情况记录导出",
            busType = "智能审计",
            fail = "本年度内部审计发现问题及整改情况记录导出",
            operationType = OperationType.EXPORT,
            subType = "成果运用——本年度内部审计发现问题及整改情况记录导出操作"
    )
    @Operation(summary = "本年度内部审计发现问题及整改情况-导出")
    @GetMapping("/export")
    public void exportByTemplate(HttpServletResponse response,TblCurrentYearAuditIssuesEntity entity,
                                 @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token) {
        try {
            //验证用户登录
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                throw new ServiceException("用户已失效！");
            }

            // 查询数据
            List<TblCurrentYearAuditIssuesEntity> list = tblCurrentYearAuditIssuesService.list(entity);

            // 设置响应头
            //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("本年度内部审计发现问题及整改情况.xlsx"));

            // 导出Excel（模板文件放在resources/templates目录下）
            ExcelTemplateExportUtil.exportByTemplate("template/TBL_CURRENT_YEAR_AUDIT_ISSUES.xlsx", list, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
