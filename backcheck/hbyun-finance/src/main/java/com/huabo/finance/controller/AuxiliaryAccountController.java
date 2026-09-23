package com.huabo.finance.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.service.BdFinanceAccassService;
import com.huabo.finance.service.GlAssBalaneService;
import com.huabo.finance.service.IBdAccassitemService;
import com.huabo.finance.vo.BdFinanceAccassVo;
import com.huabo.finance.vo.GlAssBalaneVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/auxiliary")
@Tag(name="财务微服务",description="财务微服务")
public class AuxiliaryAccountController {

	@Autowired
	private UserProvider userProvider;
	
	@Resource
	private IBdAccassitemService iBdAccassitemService;
	
	@Resource
	private BdFinanceAccassService bdFinanceAccassService;
	
	@Resource
	private GlAssBalaneService glAssBalaneService;
	
	@GetMapping(value = "/getAccAssTreeList",produces = "application/json; charset=utf-8")
	@Operation(summary = "辅助账-获取辅助类型列表")
	public JsonBean getAccAssTreeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.iBdAccassitemService.getAccAssTreeList(staff);
	}
	
	
	@GetMapping(value = "/getFinanceAccAssInfoList",produces = "application/json; charset=utf-8")
	@Operation(summary = "辅助账-获取辅助信息列表（分页）")
	public JsonBean getFinanceAccAssInfoList(HttpServletRequest request, HttpServletResponse response,BdFinanceAccassVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinanceAccassService.getFinanceAccAssInfoList(staff,vo);
	}
	
	@GetMapping(value = "/getFinanceAccAssBalanceList",produces = "application/json; charset=utf-8")
	@Operation(summary = "辅助账-获取辅助余额列表（分页）")
	public JsonBean getFinanceAccAssBalanceList(HttpServletRequest request, HttpServletResponse response,GlAssBalaneVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.glAssBalaneService.getFinanceAccAssBalanceList(staff,vo);
	}
	
	@GetMapping(value = "/getFinanceAccAssGeneralLedgerList",produces = "application/json; charset=utf-8")
	@Operation(summary = "辅助账-获取辅助总账列表（分页）")
	public JsonBean getFinanceAccAssGeneralLedgerList(HttpServletRequest request, HttpServletResponse response,GlAssBalaneVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.glAssBalaneService.getFinanceAccAssGeneralLedgerList(staff,vo);
	}
	
}
