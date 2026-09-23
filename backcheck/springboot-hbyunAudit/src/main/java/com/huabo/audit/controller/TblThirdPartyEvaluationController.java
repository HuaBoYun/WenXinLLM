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
import com.huabo.audit.oracle.entity.TblThirdPartyEvaluationEntity;
import com.huabo.audit.service.TblThirdPartyEvaluationService;
import com.huabo.audit.util.ExcelTemplateExportUtil;
import com.huabo.audit.util.PageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 中介机构评价控制器
 * <p>提供成果汇总中中介机构评价表的查询、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="成果汇总-中介机构评价表",description="成果汇总-中介机构评价表")
@RequestMapping(value = "/thirdPartyEvaluation")
public class TblThirdPartyEvaluationController {

    @Autowired
    private TblThirdPartyEvaluationService tblThirdPartyEvaluationService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "中介机构评价表列表页",
            busType = "智能审计",
            fail = "中介机构评价表列表页",
            operationType = OperationType.SELECT,
            subType = "成果运用——中介机构评价表列表页——获取相关信息"
    )
    @GetMapping("/getPage")
    @Operation(summary = "中介机构评价表-列表")
    public JsonBean getPage(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                              @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                              TblThirdPartyEvaluationEntity entity) throws Exception {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, new HashMap<>());
        }

        JsonBean jsonBean = null;
        try {
            PageResult<TblThirdPartyEvaluationEntity> page = tblThirdPartyEvaluationService.page(pageNumber, pageSize, entity);
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
            success = "中介机构评价表记录详情",
            busType = "智能审计",
            fail = "中介机构评价表记录详情",
            operationType = OperationType.SELECT,
            subType = "成果运用——中介机构评价表列表页——获取指定记录详细信息"
    )
    @GetMapping("/getDetail")
    @Operation(summary = "中介机构评价表-详情")
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
            TblThirdPartyEvaluationEntity byId = tblThirdPartyEvaluationService.getById(id);
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
            success = "中介机构评价表删除",
            busType = "智能审计",
            fail = "中介机构评价表记录详情",
            operationType = OperationType.DELETE,
            subType = "成果运用——中介机构评价表列表页——获取指定记录详细信息"
    )
    @GetMapping("/remove")
    @Operation(summary = "中介机构评价表-删除")
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
            boolean b = tblThirdPartyEvaluationService.removeById(id);
            jsonBean= ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "中介机构评价表新增修改信息",
            busType = "智能审计",
            fail = "中介机构评价表新增修改信息",
            operationType = OperationType.ADD,
            subType = "成果运用——中介机构评价表列表页"
    )
    @PostMapping(value = "/mergeInfo")
    @Operation(summary = "中介机构评价表-新增或修改")
    public JsonBean mergembInfo(HttpServletRequest request,
                                @Parameter(name = "thirdPartyEvaluation", description = "中介机构评价", required = true) TblThirdPartyEvaluationEntity entity,
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
                tblThirdPartyEvaluationService.save(entity);
            } else {
                entity.setCreateTime(null);
                tblThirdPartyEvaluationService.updateById(entity);
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
            success = "中介机构评价表列表信息导出",
            busType = "智能审计",
            fail = "中介机构评价表列表信息导出",
            operationType = OperationType.EXPORT,
            subType = "成果运用——中介机构评价表——列表信息导出操作"
    )
    @Operation(summary = "中介机构评价表-导出")
    @GetMapping("/export")
    public void exportByTemplate(HttpServletResponse response,TblThirdPartyEvaluationEntity entity,
                                 @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token) {
        try {
            //验证用户登录
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null){
                throw new ServiceException("用户已失效！");
            }

            // 查询数据
            List<TblThirdPartyEvaluationEntity> list = tblThirdPartyEvaluationService.list(entity);

            // 设置响应头
            //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("中介机构评价表.xlsx"));

            // 导出Excel（模板文件放在resources/templates目录下）
            ExcelTemplateExportUtil.exportByTemplate("template/TBL_THIRD_PARTY_EVALUATION.xlsx", list, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
