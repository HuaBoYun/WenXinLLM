package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IRiskcategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 *  风险识别 - 风险创建 - 风险类型创建
 * </p>
 * @version 1.0.1
 * @author LiHongXu
 * @since 2022-08-02
 */
@RestController
@RequestMapping(value = "/fxxt")
@Tag(name="风险识别 - 风险创建 - 风险类型",description="风险识别 - 风险创建 - 风险类型")
public class RiskcategoryController {
    @Autowired
    private IRiskcategoryService riskcategoryService;

    @Autowired
    private IOrganizationService organizationService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * <p>
     *  风险识别 - 风险创建 - 风险类型新建
     * </p>
     * @version 1.0.1
     * @author LiHongXu
     * @since 2022-08-02
     */
    @OperationLog(
            success = "风险类型新建处理成功",
            busType = "风险识别",
            fail = "风险类型新建处理失败",
            operationType = OperationType.ADD,
            subType = "风险创建"
    )
    @RequestMapping(value = "/fxsb/risk_type_toAdd",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险类型新建/fxxt/fxsb/risk_type_toAdd")
    public JsonBean risk_type_toAdd(@Parameter(name="fatherriskcatid",description="父风险类别ID，必填项",required=true) @RequestParam(required = true) String fatherriskcatid,
                                    @Parameter(name = "unit", description = "当前选中的组织ID，可忽略") @RequestParam(required = false) String unit,
                                    @Parameter(name="moduletype",description="用于标识不同的库，此请求默认为FXSJK，必填项",required=true) @RequestParam(required = true) String moduletype,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
		}
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Organization organization = organizationService.getById(selectOrg.getOrgid());
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("fatherriskcatid", fatherriskcatid);
        result.put("unit", unit);
        result.put("isUse", organization.getIsautonumber());
        result.put("moduletype",moduletype==null?"":moduletype);
        jsonBean = ResponseFormat.retParam(1, 200, null);
        return jsonBean;
    }

    /**
     * <p>
     *  风险识别 - 风险创建 - 风险类型修改
     * </p>
     * @version 1.0.1
     * @author LiHongXu
     * @since 2022-08-03
     */
    @OperationLog(
            success = "风险类型修改处理成功",
            busType = "风险识别",
            fail = "风险类型修改处理失败",
            operationType = OperationType.UPDATE,
            subType = "风险创建"
    )
    @RequestMapping(value = "/fxsb/risk_type_toModify",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险类型修改/fxxt/fxsb/risk_type_toModify")
    public JsonBean risk_type_toModify(@Parameter(name = "riskcatid", description = "风险类型主键", required = true) @RequestParam(required = false) String riskcatid,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
		}
        Riskcategory cat = riskcategoryService.getById(riskcatid);
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("cat", cat);
        jsonBean = ResponseFormat.retParam(1, 200, null);
        return jsonBean;
    }

    /**
     * <p>
     *  风险识别 - 风险创建 - 风险类型修改保存
     * </p>
     * @version 1.0.1
     * @author LiHongXu
     * @since 2022-08-03
     */
    @OperationLog(
            success = "风险类型修改保存处理成功",
            busType = "风险识别",
            fail = "风险类型修改保存处理失败",
            operationType = OperationType.UPDATE,
            subType = "风险创建"
    )
    @RequestMapping(value = "/fxsb/risk_type_modify",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险类型修改保存/fxxt/fxsb/risk_type_modify")
    public JsonBean risk_type_modify(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "riskcatnumber", description = "风险类型编号", required = true)@RequestParam(value = "riskcatnumber", required = true) String riskcatnumber,
    		@Parameter(name = "riskcatname", description = "风险类型名称", required = true)@RequestParam(value = "riskcatname", required = true) String riskcatname,
    		@Parameter(name = "riskcatid", description = "风险类型主键", required = true)@RequestParam(value = "riskcatid", required = true) BigDecimal riskcatid,
    		@Parameter(name = "riskcatdes", description = "风险描述", required = false)@RequestParam(value = "riskcatdes", required = false) String riskcatdes,
    		@Parameter(name = "riskstatus", description = "状态  0-正常，1-禁用", required = true)@RequestParam(value = "riskstatus", required = true) String riskstatus,
    		@Parameter(name = "fatherriskcatid", description = "上级风险类型主键", required = true)@RequestParam(value = "fatherriskcatid", required = true) BigDecimal fatherriskcatid,
    		@Parameter(name = "isleaf", description = "是否是子节点  1-是  0-否", required = false)@RequestParam(value = "isleaf", required = true) BigDecimal isleaf,
    		@Parameter(name = "moduletype", description = "所属模块，风险创建模块默认为 FXSJK", required = true)@RequestParam(value = "moduletype", required = true) String moduletype) throws Exception {
    	TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
		}

        Riskcategory riskcategory = new Riskcategory();
        riskcategory.setRiskcatnumber(riskcatnumber);
        riskcategory.setRiskcatname(riskcatname);
        riskcategory.setRiskcatid(riskcatid);
        riskcategory.setRiskcatdes(riskcatdes);
        riskcategory.setRiskstatus(riskstatus);
        riskcategory.setFatherriskcatid(fatherriskcatid);
        if(null == isleaf) {
        	isleaf = new BigDecimal(1);
        }
        riskcategory.setIsleaf(isleaf);
        riskcategory.setModuletype(moduletype);
        riskcategory.setUnit(staffUtil.getCurrentOrg().getOrgid().toString());
        riskcategoryService.updateById(riskcategory);
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("riskcategory", riskcategory);
        jsonBean = ResponseFormat.retParam(1, 200, null);
        return jsonBean;
    }

    /**
     * <p>
     *  风险识别 - 风险创建 - 风险类型删除
     * </p>
     * @version 1.0.1
     * @author LiHongXu
     * @since 2022-08-03
     */
    @OperationLog(
            success = "风险类型删除处理成功",
            busType = "风险识别",
            fail = "风险类型删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险创建"
    )
    @OperationLog(
			success = "风险类型【{{#riskcatid}}】删除",
			busType = "风险识别",
			fail = "风险类型【{{#riskcatid}}】删除",
			operationType = OperationType.DELETE,
			subType = "风险创建"
	)
    @RequestMapping(value = "/fxsb/risk_type_disp",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险类型删除/fxxt/fxsb/risk_type_disp")
    public JsonBean risk_type_disp(
    		@Parameter(name = "riskcatid", description = "风险类型主键", required = true)@RequestParam(value = "riskcatid", required = true) BigDecimal riskcatid,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
		}
        Riskcategory riskcategory = new Riskcategory();
        riskcategory.setRiskcatid(riskcatid);

        List<BigDecimal> list = riskcategoryService.findRiskcatidByChildNode(riskcategory);
        JsonBean jsonBean = null;

        if (list.size()>0 && list.size()==1){
            try {
                riskcategoryService.deleteRiskcategory(riskcategory);
                jsonBean = ResponseFormat.retParam(1, 200, null);
            } catch (Exception e) {
            }
        }else {
        	jsonBean = ResponseFormat.retParam(0, "该风险类型下有子集风险类型无法删除", null);
        }
        return jsonBean;
    }

    /**
     * <p>
     *  风险识别 - 风险创建 - 风险类型新建保存
     * </p>
     * @version 1.0.1
     * @author LiHongXu
     * @since 2022-08-05
     */
    @OperationLog(
            success = "风险类型【{{#riskcatname}}】新增",
            busType = "风险识别",
            fail = "风险类型【{{#riskcatname}}】新增",
            operationType = OperationType.ADD,
            subType = "风险创建"
    )
    @RequestMapping(value = "/fxsb/risk_type_add",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险类型新建保存/fxxt/fxsb/risk_type_add")
    public JsonBean risk_type_add(
    		@Parameter(name = "riskcatnumber", description = "风险类型编号", required = true)@RequestParam(value = "riskcatnumber", required = true) String riskcatnumber,
    		@Parameter(name = "riskcatname", description = "风险类型名称", required = true)@RequestParam(value = "riskcatname", required = true) String riskcatname,
    		@Parameter(name = "riskcatdes", description = "风险描述", required = false)@RequestParam(value = "riskcatdes", required = false) String riskcatdes,
    		@Parameter(name = "riskstatus", description = "状态  0-正常，1-禁用", required = true)@RequestParam(value = "riskstatus", required = true) String riskstatus,
    		@Parameter(name = "fatherriskcatid", description = "上级风险类型主键", required = true)@RequestParam(value = "fatherriskcatid", required = true) BigDecimal fatherriskcatid,
    		@Parameter(name = "isleaf", description = "是否是子节点  1-是  0-否", required = true)@RequestParam(value = "isleaf", required = true) BigDecimal isleaf,
    		@Parameter(name = "moduletype", description = "所属模块，风险创建模块默认为 FXSJK", required = true)@RequestParam(value = "moduletype", required = true) String moduletype,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
		}

        Riskcategory riskcategory = new Riskcategory();
        riskcategory.setRiskcatnumber(riskcatnumber);
        riskcategory.setRiskcatname(riskcatname);
        riskcategory.setRiskcatdes(riskcatdes);
        riskcategory.setRiskstatus(riskstatus);
        riskcategory.setFatherriskcatid(fatherriskcatid);
        riskcategory.setIsleaf(isleaf);
        riskcategory.setModuletype(moduletype);
        riskcategory.setUnit(staffUtil.getCurrentOrg().getOrgid().toString());

        riskcategoryService.save(riskcategory);
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("riskcategory", riskcategory);
        jsonBean = ResponseFormat.retParam(1, 200, null);
        return jsonBean;
    }

    /**
     * <p>
     *  风险识别 - 风险创建 - 判断风险类型是否已经存在
     * </p>
     * @version 1.0.1
     * @author LiHongXu
     * @since 2022-08-05
     */
    @OperationLog(
            success = "判断风险类型是否已经存在处理成功",
            busType = "风险识别",
            fail = "判断风险类型是否已经存在处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/fxsb/find_riskcate_bynum",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "判断风险类型是否已经存在 /fxxt/fxsb/find_riskcate_bynum")
    public JsonBean findriskcateByNum(@Parameter(name = "number", description = "风险类型编号", required = true)@RequestParam(value = "number", required = true) String number,
    		@Parameter(name = "name", description = "风险类型名称", required = true)@RequestParam(value = "name", required = true) String name,
    		@Parameter(name = "moduletype", description = "所属模块，风险创建模块默认为 FXSJK", required = true)@RequestParam(value = "moduletype", required = true) String moduletype,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
		}
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        JsonBean jsonBean = null;
        jsonBean = ResponseFormat.retParam(1, 200, null);
        if (riskcategoryService.findTblRiskOrganBynumber(number,selectOrg.getOrgid().toString())) {
            jsonBean = ResponseFormat.retParam(1, "该风险类型编号已存在", null);
        }
        if(riskcategoryService.findRiskOrganByName(name,selectOrg.getOrgid().toString(),moduletype)){
            jsonBean = ResponseFormat.retParam(1, "该风险类型名称已存在", null);
        }
        return jsonBean;
    }

    /**
     * <p>
     *  风险热图 左边树形下拉列表
     * </p>
     * @version 1.0.1
     * @author ZuoShun
     * @since 2022-08-05
     */
    @OperationLog(
            success = "风险热图 左边树形下拉列表处理成功",
            busType = "风险识别",
            fail = "风险热图 左边树形下拉列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value ="/fxsb/risk_left",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险热图 左边树形下拉列表 /fxxt/fxsb/risk_left")
    public JsonBean  riskQueryLeft(@Parameter(description="orgid") @RequestParam(required = false) String orgid,
                                   @Parameter(description="treeName") @RequestParam(required = false) String treeName,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        if (StringUtils.isEmpty(orgid)) {
            orgid = selectOrg.getOrgid().toString();
        }
        return  riskcategoryService.riskQueryLeft(orgid,selectOrg,treeName);
    }
}

