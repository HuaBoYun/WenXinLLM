package com.huabo.fxgl.controller;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.hbfk.util.ExportUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.TblMajorRiskbranchCreateDto;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.service.TblRiskImplementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 重大风险填报控制器
 * <p>提供重大风险填报的列表查询、新增、修改、提交等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/implement")
@RestController
@Tag(name="重大风险填报",description="重大风险填报")
@Slf4j
public class TblRiskImplementController {

    @Resource
    private TblRiskImplementService tblRiskImplementService;
    @Value("${application.administrators:}")
	private String administrators;

    @Value("${application.auditlegaldepartment:}")
  	private String auditlegaldepartment;

    @Resource
    private UserProvider userProvider;

    /**
     * 分公司判断是否可新建
     * @param token
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "分公司判断是否可新建 ",
            busType = "重大风险",
            fail = "分公司判断是否可新建 1",
            operationType = OperationType.SELECT,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/isCreateSubmit",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "分公司判断是否可新建 1 ：可新建 ，0 ：不可新建 ")
    public JsonBean isCreateSubmit(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskImplementService.isCreateSubmit(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }

    /**
     * 下发表单-下发
     * @param token
     * @param tblRiskImplementEntity
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "下发表单 ",
            busType = "重大风险",
            fail = "下发表单",
            operationType = OperationType.DISPATCH,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/issuedAndSubmit",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "下发表单-下发")
    public JsonBean issuedAndSubmit(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @RequestBody TblRiskImplementEntity tblRiskImplementEntity, String attIds) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskImplementService.issuedAndSubmit(tblRiskImplementEntity, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }
    /**
     * 重大风险填报新增/修改
     * @param token
     * @param tblRiskImplementEntity
     * @return
     * @throws Exception
     */


    @OperationLog(
            success = "重大风险填报新增/修改 ",
            busType = "重大风险",
            fail = "重大风险填报新增/修改",
            operationType = OperationType.ADD,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/issuedSaveOrUpdate",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险填报新增/修改")
    public JsonBean issuedSaveOrUpdate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @RequestBody TblRiskImplementEntity tblRiskImplementEntity, String attIds) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskImplementService.issuedSaveOrUpdate(tblRiskImplementEntity, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 相关风险事件填报删除
     * @param token
     * @param id
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "重大风险填报删除【{{#id}}】 ",
            busType = "重大风险",
            fail = "重大风险填报删除【{{#id}}】",
            operationType = OperationType.DELETE,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/issuedImplementDelete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险填报删除")
    public JsonBean issuedImplementDelete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "id", description = "相关风险事件主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblRiskImplementService.issuedImplementDelete(id);
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
            success = "获取重大风险事件填报列表 ",
            busType = "重大风险",
            fail = "获取重大风险事件填报列表",
            operationType = OperationType.SELECT,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/issuedImplementList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "获取重大风险事件填报列表")
    public JsonBean issuedImplementList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                        @Parameter(name = "impRiskName", description = "风险名称") @RequestParam(value = "impRiskName", required = false) String impRiskName,
                                        @Parameter(name = "majorid", description = "季度主键", required = false) @RequestParam(value = "majorid", required = false) String majorid,
                                        @Parameter(name = "id", description = "事件主键", required = false) @RequestParam(value = "id", required = false) String id
    		) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskImplementService.issuedImplementList(token, pageNumber, pageSize,impRiskName,majorid,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }


    @OperationLog(
            success = "获取重大风险事件填报汇总 ",
            busType = "重大风险",
            fail = "获取重大风险事件填报汇总",
            operationType = OperationType.SELECT,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/issuedImplementSummary",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "获取重大风险事件填报汇总")
    public JsonBean issuedImplementSummary(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                        @Parameter(name = "impRiskName", description = "风险名称") @RequestParam(value = "impRiskName", required = false) String impRiskName,
                                        @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd", required = false) String jd,
                                        @Parameter(name = "orgName", description = "上报单位") @RequestParam(value = "orgName", required = false) String orgName,
                                        @Parameter(name = "id", description = "事件主键", required = false) @RequestParam(value = "id", required = false) String id
    		) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskImplementService.issuedImplementSummary(token, pageNumber, pageSize,impRiskName,id,orgName,jd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }



    @OperationLog(
            success = "获取重大风险台账按照风险名称排序汇总 ",
            busType = "重大风险",
            fail = "获取重大风险台账按照风险名称排序汇总",
            operationType = OperationType.SELECT,
            subType = "重大风险台账"
    )
    @RequestMapping(value = "/issuedImplementSummaryOrder",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "获取重大风险台账--按照风险名称排序")
    public JsonBean issuedImplementSummaryOrder(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                        @Parameter(name = "impRiskName", description = "风险名称") @RequestParam(value = "impRiskName", required = false) String impRiskName,
                                        @Parameter(name = "orgName", description = "上报单位") @RequestParam(value = "orgName", required = false) String orgName,
                                        @Parameter(name = "id", description = "事件主键", required = false) @RequestParam(value = "id", required = false) String id,
                                        @Parameter(name = "impWayStaffName", description = "牵头领导") @RequestParam(value = "impWayStaffName", required = false) String impWayStaffName,
                                        @Parameter(name = "impWayDeptName", description = "牵头责任部门") @RequestParam(value = "impWayDeptName", required = false) String impWayDeptName,
                                        @Parameter(name = "impRiskDetails", description = "风险描述") @RequestParam(value = "impRiskDetails", required = false) String impRiskDetails,
                                        @Parameter(name = "impThisControl", description = "本季度风险防控qing'kk") @RequestParam(value = "impThisControl", required = false) String impThisControl,
                                        @Parameter(name = "nd", description = "上报年度") @RequestParam(value = "nd", required = false) String nd,
                                        @Parameter(name = "jd", description = "上报季度") @RequestParam(value = "jd", required = false) String jd
    		) throws Exception {
        JsonBean jsonBean = null;
        try {
        	     TblRiskImplementEntity entity=new TblRiskImplementEntity();
	          entity.setImpRiskName(impRiskName);
	          entity.setImpLssuedUnitName(orgName);
	          entity.setImpWayStaffName(impWayStaffName);
	          entity.setImpWayDeptName(impWayDeptName);
	          entity.setImpRiskDetails(impRiskDetails);
	          entity.setImpThisControl(impThisControl);
	          entity.setNd(nd);
	          entity.setJd(jd);
            jsonBean = tblRiskImplementService.issuedImplementSummaryOrder(token, pageNumber, pageSize,id,entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }




    /**
     * 下发表单详情
     * @param token
     * @param id
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "下发表单详情【{{#id}}】",
            busType = "重大风险",
            fail = "下发表单详情【{{#id}}】",
            operationType = OperationType.DISPATCH,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/issuedImplementDetails",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "下发表单详情")
    public JsonBean issuedImplementDetails(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "id", description = "下发表单主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskImplementService.issuedImplementDetails(id, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }



    @OperationLog(
            success = "重大风险下发风险导出【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险下发风险导出【{{#id}}】",
            operationType = OperationType.EXPORT,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/exportIssuedImplement",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@ResponseBody
    @Operation(summary = "重大风险下发风险导出")
	public void  exportIssuedImplement(HttpServletRequest request, HttpServletResponse response,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
											 @Parameter(name = "impRiskName", description = "风险名称") @RequestParam(value = "impRiskName", required = false) String impRiskName,
											 @Parameter(name = "id", description = "事件主键", required = false) @RequestParam(value = "id", required = false) String id,
		                                        Object task) {
		try {
			log.info("下发风险-导出Excel");
			response.setContentType("application/binary;charset=UTF-8");
		    TblStaffUtil user = userProvider.get();
		   List<TblRiskImplementEntity> entityList =tblRiskImplementService.exportIssuedImplement(token,id,impRiskName);
			String[] cNames = {"风险名称","牵头领导","牵头责任部门","公司相关责任单位","风险描述",
					"本季度风险防控情况","已发生的风险事件及应对处置情况", "需要提示的问题和风险", "本季度主要风险研判及相应防控措施", "其他需要说明的情况"};

			List<Object[]> contractlist = new ArrayList<Object[]>(0);
			Object[] objs = null;
			 for (TblRiskImplementEntity entity : entityList) {
				objs = new Object[10];
				objs[0] = entity.getImpRiskName();
				objs[1] = entity.getImpWayStaffName();
				objs[2] = entity.getImpWayDeptName();
				objs[3] = entity.getImpDutyUnitName();
				objs[4] =entity.getImpRiskDetails();
				objs[5] = entity.getImpThisControl();//entity.getLossentitycategory().equals("1")?"一般事件":"重大事件";
				objs[6] = entity.getImpSolutions();
				objs[7] = entity.getImpTips();
				objs[8] =entity.getImpTextControl();
				objs[9] =entity.getImpOther();
				contractlist.add(objs);
			 }
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("下发风险".getBytes(),"UTF-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
			ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    @OperationLog(
            success = "重大风险填报汇总导出【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险填报汇总导出【{{#id}}】",
            operationType = OperationType.EXPORT,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/exportIssuedImplementSummary",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
  	@ResponseBody
      @Operation(summary = "重大风险填报汇总导出")
  	public void  exportIssuedImplementSummary(HttpServletRequest request, HttpServletResponse response,
  											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
  											 @Parameter(name = "impRiskName", description = "风险名称") @RequestParam(value = "impRiskName", required = false) String impRiskName,
  	                                        @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd", required = false) String jd,
  											 @Parameter(name = "orgName", description = "上报单位") @RequestParam(value = "orgName", required = false) String orgName,
  											 @Parameter(name = "id", description = "事件主键", required = false) @RequestParam(value = "id", required = false) String id,
  		                                        Object task) {
  		try {
  			log.info("下发风险-导出Excel");
  			response.setContentType("application/binary;charset=UTF-8");
  		    TblStaffUtil user = userProvider.get();
  		   List<TblRiskImplementEntity> entityList =tblRiskImplementService.exportIssuedImplementSummary(token,id,impRiskName,orgName,jd);
  			String[] cNames = {"上报单位","季度","风险名称","上报日期","牵头领导","牵头责任部门", "公司相关责任单位","重点事项","风险描述",
  					"本季度风险防控情况","已发生的风险事件及应对处置情况", "需要提示的问题和风险", "下季度主要风险研判及相应防控措施", "其他需要说明的情况"};
  			String[] names = new String[]{"重大风险汇总"};
  			int[] chidths=new int[]{3500,3500,3500,3500,3500,3500,3600,12500,12500,3700,3700,3700,3700,3700};
  			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
  		   List<List<Object>> objLists = new ArrayList<List<Object>>(0);
  	        List<Object> objList = null;
  			 for (TblRiskImplementEntity entity : entityList) {
  				int i = 0;
  				objList = new ArrayList<Object>(0);
  				objList.add(entity.getImpLssuedUnitName());
  				objList.add(entity.getJd());
  				objList.add(entity.getImpRiskName());
  				objList.add(format.format(entity.getToreportdate()));
  				objList.add(entity.getImpWayStaffName());
  				objList.add(entity.getImpWayDeptName());
  				objList.add(entity.getImpDutyUnitName());
  				objList.add(entity.getImpKeyIssues());
  				objList.add(entity.getImpRiskDetails());
  				objList.add(entity.getImpThisControl());//entity.getLossentitycategory().equals("1")?"一般事件":"重大事件";
  				objList.add(entity.getImpSolutions());
  				objList.add(entity.getImpTips());
  				objList.add(entity.getImpTextControl());
  				objList.add(entity.getImpOther());
  					objList.add(0);
  				objLists.add(objList);
  			 }
  			response.setHeader("Content-Disposition", "attachment;filename=" + new String("下发风险".getBytes(),"UTF-8") + ".xlsx");
  			ServletOutputStream outputStream = response.getOutputStream();
  		    ImportOrExportExcelUtil.exportExcelIssuesLedgetList(cNames, objLists, response.getOutputStream(),chidths, null);
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}


    @OperationLog(
            success = "重大风险填报--上报【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险填报--上报【{{#id}}】",
            operationType = OperationType.SELECT,
            subType = "重大风险填报"
    )

    @Operation(summary = "重大风险填报--上报/implement/reportToLeader")
    @ResponseBody
    @RequestMapping(value = "/reportToLeader")
    public JsonBean reportToLeader(
    		@Parameter(name = "id", description = "主键id", required = false) @RequestParam(value = "id", required = false) BigDecimal id,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                                    ) throws Exception {
        return tblRiskImplementService.reportToLeader(token,id);
    }



    @OperationLog(
            success = "重大风险填报获取季度列表数据",
            busType = "重大风险",
            fail = "重大风险填报获取季度列表数据",
            operationType = OperationType.SELECT,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/getMajorRiskCreateList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险填报-外层季度列表")
    public JsonBean getjdList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @ModelAttribute TblMajorRiskbranchCreateDto dto

    ){
        JsonBean jsonBean = null;
        try{
		    TblStaffUtil staffUtil = userProvider.get();
        	  //用于判断是否为风险管理员；该角色能看到本公司所有的风险创建信息；
    		Integer authorityType;
    		if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())||JudgeRoleRight.judgeRoleRight(auditlegaldepartment, staffUtil.getRoleNames())) {
    			authorityType = 1;
    		} else {
    			authorityType = 0;
    		}
            jsonBean = tblRiskImplementService.getMajorRiskCreateList(token, pageNumber, pageSize, dto,authorityType);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @OperationLog(
            success = "重大风险填报详情【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险创建填报【{{#id}}】",
            operationType = OperationType.SELECT,
            subType = "重大风险填报"
    )
    @RequestMapping(value = "/majorDetails",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险填报详情")
    public JsonBean majorDetails(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "id", description = "表单主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        JsonBean bean = null;
        try {
            bean = tblRiskImplementService.mjorDetail(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    @OperationLog(
            success = "重大风险台账导出【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险台账导出【{{#id}}】",
            operationType = OperationType.EXPORT,
            subType = "重大风险台账"
    )
    @RequestMapping(value = "/exportIssuedImplementSummaryOrder",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
  	@ResponseBody
      @Operation(summary = "重大风险台账导出-按照风险名称分组")
  	public void  exportIssuedImplementSummaryOrder(HttpServletRequest request, HttpServletResponse response,
  											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
  											 @Parameter(name = "impRiskName", description = "风险名称") @RequestParam(value = "impRiskName", required = false) String impRiskName,
  	                                        @Parameter(name = "orgName", description = "上报单位") @RequestParam(value = "orgName", required = false) String orgName,
  											 @Parameter(name = "id", description = "事件主键", required = false) @RequestParam(value = "id", required = false) String id,
  											 @Parameter(name = "impWayStaffName", description = "牵头领导") @RequestParam(value = "impWayStaffName", required = false) String impWayStaffName,
  	                                        @Parameter(name = "impWayDeptName", description = "牵头责任部门") @RequestParam(value = "impWayDeptName", required = false) String impWayDeptName,
  	                                        @Parameter(name = "impRiskDetails", description = "风险描述") @RequestParam(value = "impRiskDetails", required = false) String impRiskDetails,
  	                                        @Parameter(name = "impThisControl", description = "本季度风险防控qing'kk") @RequestParam(value = "impThisControl", required = false) String impThisControl,
  	                                        @Parameter(name = "nd", description = "上报年度") @RequestParam(value = "nd", required = false) String nd,
  	                                        @Parameter(name = "jd", description = "上报季度") @RequestParam(value = "jd", required = false) String jd,
  											 Object task) {
  		try {
  			log.info("下发风险-导出Excel");
  			response.setContentType("application/binary;charset=UTF-8");
  		    TblStaffUtil user = userProvider.get();
  		  TblRiskImplementEntity entity=new TblRiskImplementEntity();
          entity.setImpRiskName(impRiskName);
          entity.setImpLssuedUnitName(orgName);
          entity.setImpWayStaffName(impWayStaffName);
          entity.setImpWayDeptName(impWayDeptName);
          entity.setImpRiskDetails(impRiskDetails);
          entity.setImpThisControl(impThisControl);
          entity.setNd(nd);
          entity.setJd(jd);
			int[] cWidths=new int[]{3500,3500,3500,3500,3500,3600,12500,12500,3700,3700,3700,3700,3700};
  		   List<TblRiskImplementEntity> entityList =tblRiskImplementService.exportIssuedImplementSummaryOrder(token,id,entity);
  	        Map<String, List<TblRiskImplementEntity>> groupedData = new TreeMap();
  	        for (TblRiskImplementEntity row: entityList) {
  	            String className =row.getImpRiskName();
  	            groupedData.computeIfAbsent(className, k -> new ArrayList<>()).add(row);
  	        }
  	      XSSFWorkbook workbook = new XSSFWorkbook();
  	      XSSFSheet sheet = workbook.createSheet();
  	      ExportUtil exportUtil = new ExportUtil(workbook, sheet);
			XSSFCellStyle titleStyle =null;
			XSSFFont titleFont = workbook.createFont();
			titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			titleStyle = workbook.createCellStyle();
			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
			titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			titleStyle.setBorderTop(CellStyle.BORDER_THIN); // 设置边框
			titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
			titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
			titleStyle.setBorderRight(CellStyle.BORDER_THIN);
			titleStyle.setTopBorderColor(HSSFColor.BLACK.index);
			titleStyle.setBottomBorderColor(HSSFColor.BLACK.index);
			titleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
			titleStyle.setRightBorderColor(HSSFColor.BLACK.index);
			titleStyle.setFont(titleFont);
			XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
  	      String[] cNames = {"风险名称","上报单位","上报日期","牵头领导","牵头责任部门", "公司相关责任单位","重点事项","风险描述",
					"本季度风险防控情况","已发生的风险事件及应对处置情况", "需要提示的问题和风险", "下季度主要风险研判及相应防控措施", "其他需要说明的情况"};
   	    Row headerRow = sheet.createRow(0);
     	 headerRow.setHeightInPoints(60);
   	    SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < cNames.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(cNames[i]);
            cell.setCellStyle(titleStyle); // 应用标题样式
            sheet.setColumnWidth(i, cWidths[i]);
        }
  	        int rowIndex = 1; // 从第 2 行开始写入数据
  	        for (Map.Entry<String, List<TblRiskImplementEntity>> entry : groupedData.entrySet()) {
  	            String className = entry.getKey();
  	          List<TblRiskImplementEntity> rows = entry.getValue();
  	            if (rows.size() > 1) {
  	                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + rows.size() - 1, 0, 0));
  	            }
  	            for (TblRiskImplementEntity row : rows) {
  	                Row dataRow = sheet.createRow(rowIndex++);
					Cell cell = dataRow.createCell(0);
					cell.setCellValue(className);
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(1);
					cell.setCellValue(row.getImpLssuedUnitName());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(2);
					cell.setCellValue(format.format(row.getToreportdate()));
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(3);
					cell.setCellValue(row.getImpWayStaffName());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(4);
					cell.setCellValue(row.getImpWayDeptName());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(5);
					cell.setCellValue(row.getImpDutyUnitName());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(6);
					cell.setCellValue(row.getImpRiskDetails());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(7);
					cell.setCellValue(row.getImpKeyIssues());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(8);
					cell.setCellValue(row.getImpThisControl());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(9);
					cell.setCellValue(row.getImpSolutions());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(10);
					cell.setCellValue(row.getImpTips());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(11);
					cell.setCellValue(row.getImpTextControl());
					cell.setCellStyle(bodyStyle);
					cell = dataRow.createCell(12);
					cell.setCellValue(row.getImpOther());
					cell.setCellStyle(bodyStyle);
  	            }
  	        }
  	        response.setHeader("Content-Disposition", "attachment;filename=" + new String("下发风险".getBytes(),"UTF-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
  	        workbook.write(outputStream);
  	        workbook.close();
  	        outputStream.close();
  	        System.out.println("Excel 文件导出成功！");
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}


    @OperationLog(
            success = "重大风险填报--转派【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险填报--转派【{{#id}}】",
            operationType = OperationType.ADD,
            subType = "重大风险填报"
    )

    @Operation(summary = "重大风险填报--转派/implement/majorTransfer")
    @ResponseBody
    @RequestMapping(value = "/majorTransfer")
    public JsonBean majorTransfer(
    		@Parameter(name = "ids", description = "主键ids,逗号相隔", required = true) @RequestParam(value = "ids", required = true) String  ids,
    		@Parameter(name = "staffId", description = "被转派人员id", required = true) @RequestParam(value = "staffId", required = true) BigDecimal staffId,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                                    ) throws Exception {
        return tblRiskImplementService.majorTransfer(token,ids,staffId);
    }


    @OperationLog(
            success = "重大风险填报--转派记录",
            busType = "重大风险",
            fail = "重大风险填报--转派记录",
            operationType = OperationType.SELECT,
            subType = "重大风险填报"
    )

    @Operation(summary = "重大风险填报--转派记录列表/implement/getMajorTransferList")
    @ResponseBody
    @RequestMapping(value = "/getMajorTransferList")
    public JsonBean getMajorTransferList(
    		@Parameter(name = "id", description = "主键id", required = false) @RequestParam(value = "id", required = false) BigDecimal id,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
                                    ) throws Exception {
        return tblRiskImplementService.getMajorTransferList(token,id);
    }



    @OperationLog(
            success = "列表查询",
            busType = "重大风险",
            fail = "列表查询",
            operationType = OperationType.SELECT,
            subType = "重大风险跟踪"
    )
    @RequestMapping(value = "/getMajorRiskTrack",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "重大风险跟踪-外层季度列表")
    public JsonBean getMajorRiskTrack(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "nd", description = "年度") @RequestParam(value = "nd", required = false) String nd,
                            @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd", required = false) String jd,
                            @Parameter(name = "orgName", description = "公司查询条件") @RequestParam(value = "orgName", required = false) String orgName

    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblRiskImplementService.getMajorRiskTrack(token, pageNumber, pageSize, nd,jd,orgName);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

}
