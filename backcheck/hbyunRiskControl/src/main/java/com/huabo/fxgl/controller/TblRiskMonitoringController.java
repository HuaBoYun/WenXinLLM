package com.huabo.fxgl.controller;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.TblRiskMonitoringCreationDto;
import com.huabo.fxgl.entity.TblFillIssued;
import com.huabo.fxgl.entity.TblRiskMonitoringCreation;
import com.huabo.fxgl.entity.Tree;
import com.huabo.fxgl.service.TblRiskMonitoringService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险监测指标创建控制器
 * <p>提供风险监测指标的创建、列表查询、修改、删除等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/Monitoring")
@RestController
@Tag(name="风险监测指标创建",description="风险监测指标创建")
@Slf4j
public class TblRiskMonitoringController {
//
    @Resource
    private TblRiskMonitoringService tblRiskMonitoringService;

    @Resource
    private UserProvider userProvider;


    @OperationLog(
            success = "风险监测指标创建列表数据",
            busType = "重大风险",
            fail = "风险监测指标创建列表数据",
            operationType = OperationType.SELECT,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/getMajorRiskCreateList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监测指标创建列表")
    public JsonBean getjdList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                        @ModelAttribute TblRiskMonitoringCreationDto dto
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblRiskMonitoringService.getList(token, pageNumber, pageSize, dto);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }


    @OperationLog(
            success = "风险监测指标创建新增/修改",
            busType = "重大风险",
            fail = "风险监测指标创建新增/修改",
            operationType = OperationType.ADD,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/saveOrUpdate",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监测指标创建新增/修改")
    public JsonBean saveOrUpdate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @RequestBody TblRiskMonitoringCreation tblRiskMonitoringCreation, String attIds) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringService.saveOrUpdate(tblRiskMonitoringCreation, attIds, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }


    @OperationLog(
            success = "风险监测指标创建删除【{{#id}}】",
            busType = "重大风险",
            fail = "风险监测指标创建删除【{{#id}}】",
            operationType = OperationType.DELETE,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/delete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监测指标创建删除")
    public JsonBean delete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "id", description = "主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblRiskMonitoringService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    @OperationLog(
            success = "风险监测指标创建详情【{{#id}}】",
            busType = "重大风险",
            fail = "风险监测指标创建详情【{{#id}}】",
            operationType = OperationType.SELECT,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/details",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监测指标创建详情")
    public JsonBean details(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "id", description = "表单主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringService.detail(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    @OperationLog(
            success = "批量选择下发人员",
            busType = "重大风险",
            fail = "批量选择下发人员",
            operationType = OperationType.DISPATCH,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/monitoringIssued",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监测指标创建下发")
    public JsonBean monitoringIssued(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "ids", description = "下发表单主键", required = true) @RequestParam(value = "ids", required = true) String ids,
           // @Parameter(name = "ids", description = "多选风险id", required = true) @RequestParam(value = "ids", required = true) String ids,
    		@Parameter(name = "staffids", description = "多选下发人员id", required = true) @RequestParam(value = "staffids", required = true) String staffids,
            @Parameter(name = "staffnames", description = "多选下发人员name", required = true) @RequestParam(value = "staffnames", required = true) String staffnames
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringService.monitoringIssued(ids,staffids,staffnames);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }


    @OperationLog(
            success = "批量选择下发人员NEW",
            busType = "重大风险",
            fail = "批量选择下发人员NEW",
            operationType = OperationType.DISPATCH,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/monitoringIssuedNew",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监测指标创建下发人员")
    public JsonBean monitoringIssuedNew(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@RequestBody List<TblFillIssued> issueds,@Parameter(name = "ids", description = "表单主键,多个英文逗号分隔", required = true) @RequestParam(value = "ids", required = true) String ids
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringService.monitoringIssuedNew(issueds,ids,token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  bean;
    }

    @OperationLog(
            success = "获取监测填报字典信息",
            busType = "重大风险",
            fail = "获取监测填报字典信息",
            operationType = OperationType.SELECT,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/getMonDictonaryInfo",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "获取风险监测填报字典信息")
    public JsonBean getMonDictonaryInfo(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringService.getMonDictonaryInfo(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    @OperationLog(
            success = "保存字典与部门关联信息",
            busType = "重大风险",
            fail = "保存字典与部门关联信息",
            operationType = OperationType.ADD,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/saveMonDictonaryDept",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "保存字典与部门关联信息")
    public JsonBean saveMonDictonaryDept(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@RequestBody JSONArray arr
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringService.saveMonDictonaryDept(token,arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }


    @OperationLog(
            success = "风险监测创建下发获取配置的部门信息",
            busType = "重大风险",
            fail = "风险监测创建下发获取配置的部门信息",
            operationType = OperationType.SELECT,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/getMonDictonaryDeptList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监测创建下发获取配置的部门信息")
    public JsonBean getMonDictonaryDeptList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        JsonBean bean = null;
        Map<String, Object> result=null;
        try {
        	result = tblRiskMonitoringService.getMonDictonaryDeptList(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, result);
    }


    @OperationLog(
            success = "查看风险监测创建下发记录",
            busType = "重大风险",
            fail = "查看风险监测创建下发记录",
            operationType = OperationType.SELECT,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/getIssuedList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "查看风险监测创建下发记录")
    public JsonBean getIssuedList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
    		@Parameter(name = "id", description = "表单主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringService.getIssuedList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    @OperationLog(
            success = "读取配置的填报列表",
            busType = "重大风险",
            fail = "读取配置的填报列表",
            operationType = OperationType.SELECT,
            subType = "风险监测指标创建"
    )
    @RequestMapping(value = "/getRiskMonDeptList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "读取配置的填报列表")
    public JsonBean getRiskMonDeptList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringService.getRiskMonDeptList(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    @OperationLog(
            success = "左侧部门树查询成功",
            busType = "重大风险",
            fail = "左侧部门树查询失败",
            operationType = OperationType.SELECT,
            subType = "风险监测指标创建"
    )
    @Operation(summary = "风险监测指标创建")
    @GetMapping(value = "/getOrgTree")
    public JsonBean  getTree(
            @Parameter(name="z",description="assmarkid如果多值用逗号隔开") @RequestParam(value = "z") BigDecimal z,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception  {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		String str;
		List<Tree> list = null;
		try {
			HashMap<String, Object> fields = new HashMap<String, Object>();
			fields.put("nodeId", z);
			str = HttpClient.request(HttpClient.getDeptUrl, fields, null);
			if (!StringUtils.isNotBlank(str)) {
				list = this.tblRiskMonitoringService.getNodeAllbm(z);
			}
			list = net.sf.json.JSONArray.fromObject(str);

		} catch (Exception e) {
			list = this.tblRiskMonitoringService.getNodeAllbm(z);
		}
		map.put("tree", list);
		map.put("z", z);
		return new JsonBean(200, "成功", map);
    }


}
