package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

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
import com.huabo.fxgl.entity.Flow;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.service.impl.FlowServiceImpl;
import com.huabo.fxgl.service.impl.RiskcategoryServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


/**
 * 风险管控控制器
 * <p>提供风险管控的核心业务接口</p>
 *
 * @author hbyun
 */
@Slf4j
@RestController
@RequestMapping(value = "/fxxt")
@Tag(name="风险管控",description="风险管控")
public class FxglController {
    @Autowired
    private FlowServiceImpl flowService;
    @Autowired
    private RiskcategoryServiceImpl riskcategoryService;
    
    @Resource
    private UserProvider userProvider;
    //从属性文件中获取所需服务的 URL
    private static final String activitiModelerUrl = ResourceBundle.getBundle("setting/jdbc").getString("activitiModelerUrl").toString();

    /**
     * 风险识别-风险创建- 添加
     * @author YangZeGuo
     * @since 2022-08-04
     * @param catid
//     * @param orgid
     * @param riskcatName
     * @param token
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "风险创建处理成功",
            busType = "风险管控",
            fail = "风险创建处理失败",
            operationType = OperationType.ADD,
            subType = "风险管控"
    )
    @Operation(summary = "风险识别-风险创建- 点击新建按钮 /fxxt/fxsb/processAnalysis/risk_analysis_toAdd")
    @RequestMapping(value = "/fxsb/processAnalysis/risk_analysis_toAdd",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    public JsonBean risk_analysis_toAdd(
            @Parameter(name = "catid", description = "左侧选中的风险类别ID，必填项", required = true) @RequestParam(required = true,value="catid") String catid,
//            @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) Integer orgid,
            @Parameter(name = "riskcatName", description = "风险类型名称") @RequestParam(required = false) String riskcatName,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
		}
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        result.put("riskcatName", riskcatName);
        result.put("riskcatid", catid);
        Riskcategory riskcatselef = riskcategoryService.getById(new BigDecimal(catid));
        List<Flow> flows = flowService.findList(selectOrg.getOrgid());//TBL_FLOW

        result.put("flows", flows);
        result.put("orgid", selectOrg.getOrgid());
        result.put("catName", riskcatselef.getRiskcatname());
        result.put("type", "jbxx");
        result.put("activitiModelerUrl", activitiModelerUrl);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }


}
