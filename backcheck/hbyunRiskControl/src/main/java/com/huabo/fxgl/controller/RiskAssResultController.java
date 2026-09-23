package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.huabo.fxgl.service.IRiskAssplanService;
import com.huabo.fxgl.service.IRiskLevelmappingService;
import com.huabo.fxgl.service.IRiskcategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险评估结果控制器
 * <p>提供风险评估结果的列表查询、详情查看、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="风险评估 - 评估结果",description="风险评估 - 评估结果")
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
@Slf4j
public class RiskAssResultController {
    @Autowired
    private IRiskAssplanService riskAssplanService;
    @Autowired
    private IRiskcategoryService iRiskcategoryService;
    @Autowired
    private IRiskAssplanRiskService riskAssplanRiskService;
    @Autowired
    private IRiskLevelmappingService riskLevelmappingService;
    
    @Resource
    private UserProvider userProvider;
    
    @Value("${application.administrators:}")
	private String administrators;

    /**
     * 风险管控 - 风险评估 - 评估结果
     *
     * @param pageNo 页码
     * @param pageSize  每页显示几条
     * @param find   页面查找公共类
     * @return
     * @author wanghongtuo
     * @data 2022/8/13
     * @version 1.0.2
     * @Param pageSize 每页显示几条
     */
    @OperationLog(
            success = "查询评估结果列表成功",
            busType = "风险评估",
            fail = "查询评估结果列表失败",
            operationType = OperationType.SELECT,
            subType = "评估结果"
    )
    @RequestMapping(value = "/fxxt/fxcl/riresultquerylist",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "评估结果查询页/fxxt/fxcl/riresultquerylist")
    public JsonBean resultQueryList(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "code", description = "查询条件 -计划编号", required = false) @RequestParam(value = "code",required = false) String code,
            @Parameter(name = "name", description = "查询条件 -计划名称", required = false) @RequestParam(value = "name",required = false) String name,
            @Parameter(name = "riskname", description = "首页风险领域跳转条件", required = false) @RequestParam(value = "riskname",required = false) String riskname) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        Find find = new Find();
        find.setCode(code);
        find.setName(name);
        //赋值首页风险领域条件
        find.setType(riskname);
        //用于判断是否为风险管理员；该角色能看到本公司所有的风险创建信息；
      		Integer authorityType=0;
      		if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
      			find.setId(staffUtil.getCurrentOrg().getOrgid());
      			authorityType=1;
      		}
        result.put("pageBean", riskAssplanService.findRiskAssPlanPageDetails(find,pageNo,pageSize,staffUtil,authorityType));
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;

    }

    /**
     * 风险管控 - 风险评估 - 评估结果
     * 评估结果热图列表页
     *
     * @param riskcatid 风险类别ID --- RISKCATID
     * @param planId    计划ID --- ASSPLANID
     * @param pageSize  每页显示几条
     * @param pageNo    页码
     * @author wanghongtuo
     * @data 2022/8/13
     * @version 1.0.2
     */
    @OperationLog(
            success = "评估结果热图列表页处理成功",
            busType = "风险评估",
            fail = "评估结果热图列表页处理失败",
            operationType = OperationType.SELECT,
            subType = "评估结果"
    )


    @OperationLog(
 			success = "查询评估结果热图列表成功",
 			busType = "风险评估",
 			fail = "查询评估结果热图列表失败",
 			operationType = OperationType.SELECT,
 			subType = "评估结果"
 	)
    @RequestMapping(value = "/fxxt/fxcl/pgjg_rt_list",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "评估结果热图列表页/fxxt/fxcl/pgjg_rt_list")
    public JsonBean evaluationResultThermographyList(
    		@Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "riskcatid", description = "风险类型主键Id", required = false)@RequestParam(value="riskcatid",required=false) BigDecimal riskcatid,
            @Parameter(name = "planId", description = "评估计划主键", required = true) @RequestParam(value = "planId",required = true) BigDecimal planId,
            @Parameter(name = "catids", description = "catids") @RequestParam(required = false) String catids,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        if (riskcatid != null && !riskcatid.toString().equals("0")) {
            List<Riskcategory> list = iRiskcategoryService.findRiskCateByRoot(riskcatid);
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < list.size(); i++) {
                sb.append(list.get(i).getRiskcatid());
                if (i < list.size() - 1)
                    sb.append(",");
            }
            catids = sb.toString();
        } else {
            catids = iRiskcategoryService.findRiskCateParentByAssPanid(planId.toString())[1];
            return defaultDisplay(catids, planId, token);
        }
        List<RiskAssplanRisk> assPlanRisks = riskAssplanRiskService.findRiskByRiskType(catids, planId);
        String riskids = "";

        if (assPlanRisks.size() > 0) {
            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < assPlanRisks.size(); i++) {
                sb.append(assPlanRisks.get(i).getRisk().getRiskid());
                if (i < assPlanRisks.size() - 1)
                    sb.append(",");
            }
            riskids = sb.toString();
        }
        IPage pageBean = new Page(pageNo, pageSize);
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);
        if (StringUtils.isNotBlank(riskids) && null != planId) {
            pageBean = this.riskAssplanRiskService.findRiskInRiskIdAndAssId(riskids, planId, pageBean);
        }
        result.put("riskcatid", riskcatid);
//        try {
//            result.put("url", "/fxxt/fxcl/pgjg_rt_list?catids=" + URLEncoder.encode(catids, "utf-8") + "&planId=" + planId);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        result.put("pageBean", pageBean);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * @author wanghongtuo
     * @Date 2022/8/13
     * @Des:默认显示图
     * @version 1.0.2
     */
    @OperationLog(
            success = "查询热图列表成功",
            busType = "风险评估",
            fail = "查询热图列表失败",
            operationType = OperationType.SELECT,
            subType = "评估结果"
    )
    @RequestMapping(value = "/fxxt/fxcl/pgjg_list_rt",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
//    @Operation(summary = "风险管控 - 风险评估 - 评估结果 - 热图数据返回 /fxxt/fxcl/pgjg_list_rt")
    @Operation(summary = "默认显示图/fxxt/fxcl/pgjg_list_rt")
    public JsonBean defaultDisplay(
    		@Parameter(name = "riskcatid", description = "风险类型主键Id", required = false)@RequestParam(value="riskcatid",required=false) String riskcatid,
            @Parameter(name = "planId", description = "评估计划主键", required = true) @RequestParam(value = "planId",required = true) BigDecimal planId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestParam(required = false) @RequestHeader("token") String token) throws Exception {
        /*TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }*/
        JsonBean jsonBean = null;
        Map<String,Object> result = riskLevelmappingService.forCycle(planId);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    @OperationLog(
            success = "fxcl/risk_list_rt处理成功",
            busType = "风险评估",
            fail = "fxcl/risk_list_rt处理失败",
            operationType = OperationType.SELECT,
            subType = "评估结果"
    )
    @Operation(summary = "风险处理-查询评估结果风险列表")
    @RequestMapping(value = "/fxcl/risk_list_rt",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
	public JsonBean risk_list_rt(
			 @Parameter(name = "token", description = "登录用户token", required = true)@RequestHeader("token") String token,
			 @Parameter(name = "pageNumber", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNumber",required=false) Integer pageNumber,
	         @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
	         @Parameter(name = "riskIds", description = "风险类型主键Id", required = true)@RequestParam(value="riskIds",required=true) String riskIds,
	         @Parameter(name = "planId", description = "评估计划主键", required = true) @RequestParam(value = "planId",required = true) BigDecimal planId) throws Exception {
    	 JsonBean jsonBean = null;
    	 jsonBean = riskAssplanRiskService.findRiskInRiskIdAndAssId(token,pageNumber,pageSize,riskIds,planId);
         return jsonBean;
	}


    @OperationLog(
            success = "热力图左侧处理成功",
            busType = "风险评估",
            fail = "热力图左侧处理失败",
            operationType = OperationType.SELECT,
            subType = "评估结果"
    )
    @RequestMapping(value = "/fxcl/pgjg_rt_left",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "热力图左侧")
	public JsonBean pgjg_rt_left(@Parameter(name = "planId", description = "planId") @RequestParam(required = false) String planId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
//		TblRiskAssplan assplan = riskAssplanService.get(new BigDecimal(planId));
//		String findOrgId = assplan.getTblOrganization().iterator().next().getOrgid().toString();
//		String orgid = request.getParameter("orgid");
//		if (orgid != null && !"".equals(orgid)) {
//			findOrgId = orgid;
//		}
//		String tree = service.findRiskCateParentByAssPanid(planId)[0];
//		tree = tree.replace("-1_", "0_");
//		tree = "tree.nodes['-1_0']=\"text:"+assplan.getPlanName()+";method:check(0,0,0,0)\";"+tree;

		JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);

//        result.put("tree", tree);

        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;

	}

    
    
    @OperationLog(
            success = "查询集团跟进结果列表成功",
            busType = "风险评估",
            fail = "查询集团跟进结果列表失败",
            operationType = OperationType.SELECT,
            subType = "集团跟进结果"
    )
    @RequestMapping(value = "/fxxt/fxcl/groupQueryList",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "集团跟进结果查询页/fxxt/fxcl/groupQueryList")
    public JsonBean groupQueryList(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1",value="pageNo",required=false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20",value="pageSize",required=false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestParam(required = false) @RequestHeader("token") String token,
            @Parameter(name = "code", description = "查询条件 -计划编号", required = false) @RequestParam(value = "code",required = false) String code,
            @Parameter(name = "name", description = "查询条件 -计划名称", required = false) @RequestParam(value = "name",required = false) String name,
            @Parameter(name = "groupId", description = "外层集团评估计划id", required = false) @RequestParam(value = "groupId",required = false) String groupId) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = null;
        Map<String,Object> result = new HashMap<String,Object>(0);

        Find find = new Find();
        find.setCode(code);
        find.setName(name);
        find.setStr1(groupId);
        result.put("pageBean", riskAssplanService.findGroupResultList(find,pageNo,pageSize));
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;

    }
    
}
