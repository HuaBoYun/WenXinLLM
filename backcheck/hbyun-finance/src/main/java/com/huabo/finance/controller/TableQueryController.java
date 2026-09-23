package com.huabo.finance.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.service.TableQueryService;
import com.huabo.finance.vo.TableDataQueryVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数据库表查询工具 Controller
 * 
 * @author system
 * @since 2025-11-22
 */
@RestController
@RequestMapping(value = "/table")
@Tag(name="财务微服务-表查询工具",description="数据库表查询工具")
public class TableQueryController {

	@Autowired
	private UserProvider userProvider;
	
	@Resource
	private TableQueryService tableQueryService;
	
	/**
	 * 检查表是否存在
	 * 
	 * @param request
	 * @param response
	 * @param tableName 表名
	 * @return JsonBean
	 * @throws Exception
	 */
	@GetMapping(value = "/check", produces = "application/json; charset=utf-8")
	@Operation(summary = "检查表是否存在")
	public JsonBean checkTableExists(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("tableName") String tableName) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.tableQueryService.checkTableExists(staff, tableName);
	}
	
	/**
	 * 获取表结构信息(包含表备注、字段信息)
	 * 
	 * @param request
	 * @param response
	 * @param tableName 表名
	 * @return JsonBean
	 * @throws Exception
	 */
	@GetMapping(value = "/structure", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取表结构信息")
	public JsonBean getTableStructure(
			HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("tableName") String tableName) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.tableQueryService.getTableStructure(staff, tableName);
	}
	
	/**
	 * 查询表数据
	 * 
	 * @param request
	 * @param response
	 * @param vo 查询参数
	 * @return JsonBean
	 * @throws Exception
	 */
	@PostMapping(value = "/data", produces = "application/json; charset=utf-8")
	@Operation(summary = "查询表数据")
	public JsonBean getTableData(
			HttpServletRequest request,
			HttpServletResponse response,
			TableDataQueryVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.tableQueryService.getTableData(staff, vo);
	}
	
	/**
	 * 获取已查询表列表
	 *
	 * @param request
	 * @param response
	 * @return JsonBean
	 * @throws Exception
	 */
	@GetMapping(value = "/history", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取已查询表列表")
	public JsonBean getQueriedTableList(
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.tableQueryService.getQueriedTableList(staff);
	}

	/**
	 * 更新表分类
	 *
	 * @param request
	 * @param response
	 * @param tableName 表名
	 * @param category 分类名称
	 * @param sortOrder 排序序号
	 * @return JsonBean
	 * @throws Exception
	 */
	@PostMapping(value = "/category", produces = "application/json; charset=utf-8")
	@Operation(summary = "更新表分类")
	public JsonBean updateTableCategory(
			HttpServletRequest request,
			HttpServletResponse response,
			String tableName,
			String category,
			Integer sortOrder) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}

		if (sortOrder == null) {
			sortOrder = 0;
		}

		return this.tableQueryService.updateTableCategory(staff, tableName, category, sortOrder);
	}

}

