package com.huabo.financialdata.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.financialdata.service.IAccBookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/generate")
@Tag(name="财务决策大模型问询",description="财务决策大模型问询")
public class GenerateFinController {
	
	@Resource
	private IAccBookService accBookService;
	
	@RequestMapping(value = "/generateFinance", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "流程设计-复制流程")
    public JsonBean generateFinance(HttpServletRequest reques,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "prompt", description = "提问的问题", required = false) @RequestParam(value = "prompt", required = true) String prompt) throws Exception {
        return this.accBookService.generateFinance(token,prompt);
    }
	

	@RequestMapping(value = "/getCwztDbInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "流程设计-复制流程")
    public JsonBean getCwztDbInfo(HttpServletRequest reques,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) throws Exception {
        return this.accBookService.getCwztDbInfo(token);
    }
}
