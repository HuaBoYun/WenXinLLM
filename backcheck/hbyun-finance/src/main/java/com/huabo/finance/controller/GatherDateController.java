package com.huabo.finance.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.IpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.service.BdFinancedateRecordService;
import com.huabo.finance.service.BdFinanceplanService;
import com.huabo.finance.service.GatherFinanceDateService;
import com.huabo.finance.vo.BdFinancedateRecordVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>
 * 财务数据版本 前端控制器
 * </p>
 *
 * @author L
 * @since 2025-03-10
 */
@RestController
@RequestMapping(value = "/gather")
@Tag(name="财务微服务",description="财务微服务")
public class GatherDateController {

	@Autowired
	private UserProvider userProvider;
	
	@Resource
	private GatherFinanceDateService gatherFinanceDateService;
	
	@Resource
	private BdFinancedateRecordService bdFinancedateRecordService;
	
	@Resource
	private BdFinanceplanService bdFinanceplanService;
	
	
	@GetMapping(value = "/exeUnique",produces = "application/json; charset=utf-8")
	@Operation(summary = "开始sql配置采集")
	public JsonBean gather_exeUnique(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="sqlConfigId主键",description="fid",required=false)@RequestParam(value = "fid",required = false)String fid,
			@Parameter(name="sqlInit主键",description="finitsqlid",required=true)@RequestParam(value = "finitsqlid",required = true)String finitsqlid,
			@Parameter(name="planid方案主键",description="finitPlanid",required=true)@RequestParam(value = "finitPlanid",required = true)String finitPlanid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.gatherFinanceDateService.exeUnique(fid,request,staff,finitsqlid,finitPlanid);
	}
	
	@GetMapping(value = "/stopGather",produces = "application/json; charset=utf-8")
	@Operation(summary = "停止sql配置采集")
	public JsonBean gather_stopGather(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="sqlConfigId主键",description="fid",required=false)@RequestParam(value = "fid",required = false)String fid,
			@Parameter(name="采集记录里面关联的sqlid",description="recordSqlid",required=false)@RequestParam(value = "recordSqlid",required = false)String recordSqlid,
			@Parameter(name="sqlInit主键",description="finitsqlid",required=true)@RequestParam(value = "finitsqlid",required = true)String finitsqlid,
			@Parameter(name="planid方案主键",description="finitPlanid",required=true)@RequestParam(value = "finitPlanid",required = true)String finitPlanid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.gatherFinanceDateService.stopGather(fid,finitsqlid,finitPlanid,recordSqlid);
	}
	
	@GetMapping(value = "/getSqlGatherInfo",produces = "application/json; charset=utf-8")
	@Operation(summary = "获取单一采集记录信息")
	public JsonBean gather_getSqlGatherInfo(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="采集记录主键",description="recordid",required=true)@RequestParam(value = "recordid",required = true)String recordid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.gatherFinanceDateService.getSqlGatherInfo(recordid);
	}
	
	@GetMapping(value = "/getGoonAcInfoList",produces = "application/json; charset=utf-8")
	@Operation(summary = "获取正在进行的采集记录信息")
	public JsonBean gather_getGoonAcInfoList(HttpServletRequest request, HttpServletResponse response,BdFinancedateRecordVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateRecordService.getGoonAcInfoList(vo);
	}
	
	@GetMapping(value = "/getAllAcInfoList",produces = "application/json; charset=utf-8")
	@Operation(summary = "获取所有采集记录信息")
	public JsonBean gather_getAllAcInfoList(HttpServletRequest request, HttpServletResponse response,BdFinancedateRecordVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateRecordService.getAllAcInfoList(vo);
	}
	
	/**
	 * 获得当前组织或集团下所有的采集方案
	 */
	@GetMapping(value = "/getFianacePlanList",produces = "application/json; charset=utf-8")
	@Operation(summary = "获得当前组织或集团下所有的采集方案")
	public JsonBean gather_getFianacePlanList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false , name = "fname")@Parameter(name = "fname", description = "采集方案名称", required = false)String fname) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinanceplanService.getFianacePlanList(staff,fname);
	}
	
	/**
	 * 采集方案开始采集
	 */
	
	@GetMapping(value = "/getFinancePlanTreeList",produces = "application/json; charset=utf-8")
	@Operation(summary = "获取采集方案树结构，并返回其采集状态")
	public JsonBean gather_getFinancePlanTreeList(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false , name = "fname")@Parameter(name = "fname", description = "整改方案名称", required = false)String fname) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.gatherFinanceDateService.getFinancePlanTreeList(staff,fname,1);
	}
	
	
	
	@GetMapping(value = "/getFinancePlanStatus",produces = "application/json; charset=utf-8")
	@Operation(summary = "点击采集方案获取方案及子单元采集状态")
	public JsonBean gather_getFinancePlanStatus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true , name = "fid")@Parameter(name = "fid", description = "采集方案主键", required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.gatherFinanceDateService.getFinancePlanStatus(staff,fid);
	}
	
	
	@GetMapping(value = "/beginFinancePlan",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集方案开始采集")
	public JsonBean gather_beginFinancePlan(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true , name = "fid")@Parameter(name = "fid", description = "采集方案主键", required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		String ip = IpUtil.getIpAddr(request);
		return this.gatherFinanceDateService.beginFinancePlan(staff,ip,fid);
	}
	
	
	@GetMapping(value = "/stopFinanceUniqueProcess",produces = "application/json; charset=utf-8")
	@Operation(summary = "根据采集记录停止当前进程")
	public JsonBean gather_stopFinanceProcess(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true , name = "recordid")@Parameter(name = "recordid", description = "采集记录信息主键", required = true)String recordid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.gatherFinanceDateService.stopFinanceProcess(staff,recordid);
	}
	
	@GetMapping(value = "/stopFinancePlanProcess",produces = "application/json; charset=utf-8")
	@Operation(summary = "根据采集方案主键停止所有进程")
	public JsonBean gather_stopFinancePlanProcess(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true , name = "planid")@Parameter(name = "planid", description = "采集方案信息主键", required = true)String planid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.gatherFinanceDateService.stopFinancePlanProcess(staff,planid);
	}
	
	
	@GetMapping(value = "/getFinanceRecordPageList",produces = "application/json; charset=utf-8")
	@Operation(summary = "获取当前采集信息所有的采集记录信息")
	public JsonBean gather_getFinanceRecordPageList(HttpServletRequest request, HttpServletResponse response,BdFinancedateRecordVo vo,
			@RequestParam(required = false , name = "tableId")@Parameter(name = "tableId", description = "业务数据表主键", required = false)String tableId,
			@RequestParam(required = false , name = "sqlconfigid")@Parameter(name = "sqlconfigid", description = "财务数据初始化sql主键", required = false)String sqlconfigid,
			@RequestParam(required = false , name = "planid")@Parameter(name = "planid", description = "方案主键", required = false)String planid,
			@RequestParam(required = false , name = "sqlfinid")@Parameter(name = "sqlfinid", description = "财务数据采集sql主键", required = false)String sqlfinid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateRecordService.getFinanceRecordPageList(staff,vo,tableId,sqlconfigid,planid,sqlfinid);
	}
	
	@GetMapping(value = "/getRecordDetail",produces = "application/json; charset=utf-8")
	@Operation(summary = "获取当前采集信息所有的采集记录信息")
	public JsonBean gather_getRecordDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true , name = "recordid")@Parameter(name = "recordid", description = "采集记录主键", required = true)String recordid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateRecordService.getRecordDetail(staff,recordid);
	}
	
}

