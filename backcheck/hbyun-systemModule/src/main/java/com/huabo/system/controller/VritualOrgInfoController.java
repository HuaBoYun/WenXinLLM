package com.huabo.system.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblVirtualOrgInfo;
import com.huabo.system.service.TblVirtualOrgInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 虚拟组织信息控制器
 * <p>提供虚拟组织的创建、查询、修改、删除等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "vritual", description = "虚拟组织信息接口调用")
@RequestMapping("/vritual")
public class VritualOrgInfoController {

	@Resource
	private TblVirtualOrgInfoService tblVirtualOrgInfoService;

	@PostMapping(value = "/adds",produces = "application/json; charset=utf-8")
	@Operation(summary="新增虚拟组织信息")
	public JsonBean TblVirtualOrgInfo_adds(HttpServletRequest request, HttpServletResponse response,
			@RequestBody List<TblVirtualOrgInfo> voiList) throws Exception {
		return this.tblVirtualOrgInfoService.adds(voiList);
	}

	@PostMapping(value = "/modify",produces = "application/json; charset=utf-8")
	@Operation(summary="修改虚拟组织信息")
	public JsonBean TblVirtualOrgInfo_modify(HttpServletRequest request, HttpServletResponse response,
			@RequestBody TblVirtualOrgInfo voi) throws Exception {
		return this.tblVirtualOrgInfoService.modify(voi);
	}

	@GetMapping(value = "/remove",produces = "application/json; charset=utf-8")
	@Operation(summary="删除虚拟组织信息")
	public JsonBean remove(HttpServletRequest request, HttpServletResponse response,
			@Parameter(description = "主键",required = true,name = "fid")@RequestParam(value = "fid",required = true)String fid) throws Exception {
		return this.tblVirtualOrgInfoService.remove(fid);
	}

	@GetMapping(value = "/list",produces = "application/json; charset=utf-8")
	@Operation(summary="获取当前组织的虚拟组织列表")
	public JsonBean remove(HttpServletRequest request, HttpServletResponse response,
			@Parameter(description = "组织主键",required = true,name = "orgid")@RequestParam(value = "orgid",required = true)BigDecimal orgid,
			@Parameter(description = "筛选条件-虚拟组织名称",required = false,name = "virtualname")@RequestParam(value = "virtualname",required = false)String virtualname,
			@Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	    	@Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) throws Exception {
		return this.tblVirtualOrgInfoService.list(orgid,virtualname,pageNumber,pageSize);
	}

}
