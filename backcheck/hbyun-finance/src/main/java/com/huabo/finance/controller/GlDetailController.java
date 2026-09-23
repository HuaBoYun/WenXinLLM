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
import com.huabo.finance.service.IGlDetailService;
import com.huabo.finance.vo.GlDetailVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/detail")
@Tag(name="财务微服务",description="财务微服务")
public class GlDetailController {

	@Autowired
	private UserProvider userProvider;
	
	@Resource
	private IGlDetailService iGlDetailService;
	
	@GetMapping(value = "/getFinanceDataList",produces = "application/json; charset=utf-8")
	@Operation(summary = "数据采集-凭证明细信息列表")
	public JsonBean getFinanceDataList(HttpServletRequest request, HttpServletResponse response,GlDetailVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.iGlDetailService.getFinanceDataList(staff,vo);
	}
	
}
