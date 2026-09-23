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
import com.huabo.audit.oracle.entity.TblAuditPersonnelInfoEntity;
import com.huabo.audit.service.TblAuditPersonnelInfoService;
import com.huabo.audit.util.ExcelTemplateExportUtil;
import com.huabo.audit.util.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 审计人员情况报表控制器
 * <p>提供成果汇总中审计人员情况报表的查询、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="成果汇总-审计人员情况报表",description="成果汇总-审计人员情况报表")
@RequestMapping(value = "/auditPersonnelInfo")
public class TblAuditPersonnelInfoController {

    @Autowired
    private TblAuditPersonnelInfoService tblAuditPersonnelInfoService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "审计人员情况报表列表信息",
            busType = "智能审计",
            fail = "审计人员情况报表列表信息",
            operationType = OperationType.SELECT,
            subType = "成果运用——审计人员情况报表"
    )
    @GetMapping("/getPage")
    @Operation(summary = "审计人员情况报表-列表")
    public JsonBean getPage(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                              @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                              TblAuditPersonnelInfoEntity entity) throws Exception {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, new HashMap<>());
        }

        JsonBean jsonBean = null;
        try {
            PageResult<TblAuditPersonnelInfoEntity> page = tblAuditPersonnelInfoService.page(pageNumber, pageSize, entity);
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
            success = "审计人员情况报表详情",
            busType = "智能审计",
            fail = "审计人员情况报表详情",
            operationType = OperationType.SELECT,
            subType = "成果运用——审计人员情况报表指定记录详情内容"
    )
    @GetMapping("/getDetail")
    @Operation(summary = "审计人员情况报表-详情")
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
            TblAuditPersonnelInfoEntity byId = tblAuditPersonnelInfoService.getById(id);
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
            success = "审计人员情况报表删除",
            busType = "智能审计",
            fail = "审计人员情况报表删除",
            operationType = OperationType.DELETE,
            subType = "成果运用——审计人员情况报表删除指定记录"
    )
    @GetMapping("/remove")
    @Operation(summary = "审计人员情况报表-删除")
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
            boolean b = tblAuditPersonnelInfoService.removeById(id);
            jsonBean= ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计人员情况报表新增/修改",
            busType = "智能审计",
            fail = "审计人员情况报表新增/修改",
            operationType = OperationType.ADD,
            subType = "成果运用——审计人员情况报表新增修改记录"
    )
    @PostMapping(value = "/mergeInfo")
    @Operation(summary = "审计人员情况报表-新增或修改")
    public JsonBean mergembInfo(HttpServletRequest request,
                                @Parameter(name = "auditPersonnelInfo", description = "审计人员情况", required = true) TblAuditPersonnelInfoEntity entity,
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
                tblAuditPersonnelInfoService.save(entity);
            } else {
                entity.setCreateTime(null);
                tblAuditPersonnelInfoService.updateById(entity);
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
            success = "审计人员情况报表导出",
            busType = "智能审计",
            fail = "审计人员情况报表导出",
            operationType = OperationType.EXPORT,
            subType = "成果运用——审计人员情况报表导出"
    )
    @Operation(summary = "审计人员情况报表-导出")
    @GetMapping("/export")
    public void exportByTemplate(HttpServletResponse response,TblAuditPersonnelInfoEntity entity,
                                 @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token) {

        try {
            //验证用户登录
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                throw new ServiceException("用户已失效！");
            }

            // 查询数据
            List<TblAuditPersonnelInfoEntity> list = tblAuditPersonnelInfoService.list(entity);

            // 设置响应头
            //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("审计情况统计.xlsx"));

            // 导出Excel（模板文件放在resources/templates目录下）
            ExcelTemplateExportUtil.exportByTemplate("template/TBL_AUDIT_PERSONNEL_INFO.xlsx", list, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
