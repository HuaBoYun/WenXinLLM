package com.huabo.fxgl.controller;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.service.TblMajorRiskCreationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 重大风险创建控制器
 * <p>提供重大风险的创建、列表查询、修改、删除等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/majorRisk")
@RestController
@Tag(name="重大风险创建",description="重大风险创建")
@Slf4j
public class TblMajorRiskCreationController {

    @Resource
    private TblMajorRiskCreationService tblMajorRiskCreationService;

    @Resource
    private UserProvider userProvider;


    @OperationLog(
            success = "重大风险创建获取季度列表数据",
            busType = "重大风险",
            fail = "重大风险创建获取季度列表数据",
            operationType = OperationType.ADD,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/getMajorRiskCreateList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建-外层季度列表")
    public JsonBean getjdList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "nd", description = "年度") @RequestParam(value = "nd", required = false) String nd,
                            @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd", required = false) String jd

    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblMajorRiskCreationService.getMajorRiskCreateList(token, pageNumber, pageSize, nd,jd);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }


    @OperationLog(
            success = "重大风险创建新增/修改",
            busType = "重大风险",
            fail = "重大风险创建新增/修改",
            operationType = OperationType.ADD,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/mjorSaveOrUpdate",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建新增/修改")
    public JsonBean mjorSaveOrUpdate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @RequestBody TblMajorRiskCreate tblMajorRiskCreate, String attIds) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblMajorRiskCreationService.mjorSaveOrUpdate(tblMajorRiskCreate, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }


    @OperationLog(
            success = "重大风险创建删除【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险创建删除【{{#id}}】",
            operationType = OperationType.DELETE,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/mjorDelete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建删除")
    public JsonBean issuedImplementDelete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblMajorRiskCreationService.mjorDelete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    @OperationLog(
            success = "重大风险创建详情【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险创建详情【{{#id}}】",
            operationType = OperationType.SELECT,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/majorDetails",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建详情")
    public JsonBean majorDetails(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "id", description = "表单主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblMajorRiskCreationService.mjorDetail(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    @OperationLog(
            success = "重大风险创建批量选择下发人员【{{#majorid}}】",
            busType = "重大风险",
            fail = "重大风险创建批量选择下发人员【{{#majorid}}】",
            operationType = OperationType.DISPATCH,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/majorIssued",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建下发")
    public JsonBean majorIssued(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "majorid", description = "下发表单主键", required = true) @RequestParam(value = "majorid", required = true) String majorid,
            @Parameter(name = "ids", description = "多选风险id", required = true) @RequestParam(value = "ids", required = true) String ids,
            @Parameter(name = "staffids", description = "多选下发人员id", required = true) @RequestParam(value = "staffids", required = true) String staffids,
            @Parameter(name = "staffnames", description = "多选下发人员name", required = true) @RequestParam(value = "staffnames", required = true) String staffnames
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblMajorRiskCreationService.majorIssued(majorid,ids,staffids,staffnames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }



    @OperationLog(
            success = "重大风险创建确认下发【{{#majorid}}】",
            busType = "重大风险",
            fail = "重大风险创建确认下发【{{#majorid}}】",
            operationType = OperationType.DISPATCH,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/majorIssuedValidate",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建确认下发")
    public JsonBean majorIssuedValidate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "majorid", description = "下发表单主键", required = true) @RequestParam(value = "majorid", required = true) String majorid,
            @Parameter(name = "ids", description = "多选风险id", required = false) @RequestParam(value = "ids", required = false) String ids) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblMajorRiskCreationService.majorIssuedValidate(majorid,ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }





    @OperationLog(
            success = "重大风险创建--内层风险信息新增/修改",
            busType = "重大风险",
            fail = "重大风险创建--内层风险信息新增/修改",
            operationType = OperationType.ADD,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/mjorRiskSaveOrUpdate",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建--内层风险信息新增/修改")
    public JsonBean mjorRiskSaveOrUpdate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @RequestBody TblRiskImplementGroupEntity tblRiskImplementGroupEntity) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblMajorRiskCreationService.mjorRiskSaveOrUpdate(tblRiskImplementGroupEntity, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    @OperationLog(
            success = "重大风险创建--内层风险信息导入",
            busType = "重大风险创建",
            fail = "重大风险创建--内层风险信息导入",
            operationType = OperationType.IMPORT,
            subType = "重大风险创建"
    )
    @Operation(summary = "重大风险创建--内层风险信息导入/importMjorRisk")
    @RequestMapping(value = "/importMjorRisk",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    public JsonBean importMjorRisk(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "majorid", description = "主键", required = true) @RequestParam(value = "majorid", required = true) BigDecimal majorid,
            MultipartFile file
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        JsonBean  jsonBean =null;
        try {
        	Map<String,Object> resultMap = this.tblMajorRiskCreationService.importMjorRisk(token,file,majorid);
        	jsonBean = ResponseFormat.retParam(1, 200, resultMap);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        return jsonBean;
    }
    @OperationLog(
            success = "重大风险创建--内层风险信息详情【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险创建--内层风险信息详情【{{#id}}】",
            operationType = OperationType.SELECT,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/majorRiskDetails",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建--内层风险信息详情")
    public JsonBean majorRiskDetails(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "id", description = "下发表单主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblMajorRiskCreationService.mjorRiskDetail(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }


    @OperationLog(
            success = "重大风险创建--内层风险信息删除【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险创建--内层风险信息删除【{{#id}}】",
            operationType = OperationType.DELETE,
            subType = "重大风险创建"
    )
    @RequestMapping(value = "/mjorRiskDelete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险创建--内层风险信息删除")
    public JsonBean mjorRiskDelete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblMajorRiskCreationService.mjorRiskDelete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }

}
