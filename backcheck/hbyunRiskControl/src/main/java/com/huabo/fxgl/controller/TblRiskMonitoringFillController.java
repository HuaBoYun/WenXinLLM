package com.huabo.fxgl.controller;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.TblRiskMonitoringFill;
import com.huabo.fxgl.service.TblRiskMonitoringFillService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险监测指标填报控制器
 * <p>提供风险监测指标填报的列表查询、数据填报、提交等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/MonitoringFill")
@RestController
@Tag(name="风险监测指标填报",description="风险监测指标填报")
@Slf4j
public class TblRiskMonitoringFillController {

    @Resource
    private TblRiskMonitoringFillService tblRiskMonitoringFillService;

    @Value("${application.administrators:}")
	private String administrators;

    @Value("${application.auditlegaldepartment:}")
  	private String auditlegaldepartment;

    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "风险监测指标填报新增/修改 ",
            busType = "重大风险",
            fail = "风险监测指标填报新增/修改",
            operationType = OperationType.ADD,
            subType = "风险监测指标填报"
    )
    @RequestMapping(value = "/saveOrUpdate",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监测指标填报新增/修改")
    public JsonBean issuedSaveOrUpdate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @RequestBody TblRiskMonitoringFill tblRiskMonitoringFill, String attIds) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskMonitoringFillService.saveOrUpdate(tblRiskMonitoringFill, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }


  @OperationLog(
  success = "风险监测指标填报删除【{{#id}}】 ",
  busType = "重大风险",
  fail = "风险监测指标填报删除【{{#id}}】",
  operationType = OperationType.DELETE,
  subType = "风险监测指标填报"
)
@RequestMapping(value = "/delete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
@Operation(summary = "风险监测指标填报删除")
public JsonBean delete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "id", description = "相关事件主键", required = true) @RequestParam(value = "id", required = true) String id
) throws Exception {
Map<String, Object> hashMap = new HashMap<>();
try {
  TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
  if (staffUtil == null){
      return  ResponseFormat.retParam(0, 20006, hashMap);
  }
  tblRiskMonitoringFillService.delete(id);
} catch (Exception e) {
  e.printStackTrace();
}
return ResponseFormat.retParam(1, 200, null);
}

/**
* 下发表单列表  更名为重大风险事件填报列表
* @param token
* @param pageNumber
* @param pageSize
* @return
* @throws Exception
*/

@OperationLog(
  success = "获取风险监测指标填报列表 ",
  busType = "重大风险",
  fail = "获取风险监测指标填报列表",
  operationType = OperationType.SELECT,
  subType = "风险监测指标填报"
)
@RequestMapping(value = "/getList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
@Operation(summary = "获取风险监测指标填报列表")
public JsonBean getList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                             @ModelAttribute TblRiskMonitoringFill fill
	) throws Exception {
JsonBean jsonBean = null;
try {
  jsonBean = tblRiskMonitoringFillService.getList(token, pageNumber, pageSize,fill);
} catch (Exception e) {
  e.printStackTrace();
}
return jsonBean;
}


@OperationLog(
success = "详情【{{#id}}】",
busType = "重大风险",
fail = "详情【{{#id}}】",
operationType = OperationType.DISPATCH,
subType = "风险监测指标填报"
)
@RequestMapping(value = "/details",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
@Operation(summary = "风险监测指标填报详情")
public JsonBean details(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "id", description = "表单主键", required = true) @RequestParam(value = "id", required = true) String id
) throws Exception {
JsonBean bean = null;
try {
bean = tblRiskMonitoringFillService.details(id, token);
} catch (Exception e) {
e.printStackTrace();
}
return bean;
}

@OperationLog(
success = "风险监测指标填报--上报【{{#id}}】",
busType = "重大风险",
fail = "风险监测指标填报--上报【{{#id}}】",
operationType = OperationType.SELECT,
subType = "风险监测指标填报"
)
@Operation(summary = "风险监测指标填报--上报/implement/reportToLeader")
@ResponseBody
@RequestMapping(value = "/reportToLeader")
public JsonBean reportToLeader(
@Parameter(name = "id", description = "主键id", required = false) @RequestParam(value = "id", required = false) String id,
                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                      ) throws Exception {
return tblRiskMonitoringFillService.reportToLeader(token,id);
}


@OperationLog(
        success = "读取配置的填报列表",
        busType = "重大风险",
        fail = "读取配置的填报列表",
        operationType = OperationType.SELECT,
        subType = "风险监测指标填报"
)
@RequestMapping(value = "/getRiskMonDeptList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
@Operation(summary = "读取配置的填报列表")
public JsonBean getRiskMonDeptList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
		@Parameter(name = "id", description = "填报表单主键", required = true) @RequestParam(value = "id", required = true) String id
) throws Exception {
    JsonBean bean = null;
    try {
        bean = tblRiskMonitoringFillService.getRiskMonDeptList(token,id);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return bean;
}

}
