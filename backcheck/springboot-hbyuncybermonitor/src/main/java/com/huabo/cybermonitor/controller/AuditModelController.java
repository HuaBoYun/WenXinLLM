package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.TblAuditModelDataSourceOracle;
import com.huabo.cybermonitor.exception.ServiceException;
import com.huabo.cybermonitor.service.AuditModelService;
import com.huabo.cybermonitor.service.IMonitorRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.huabo.cybermonitor.service.TblAuditModelDataSourceOracleService;
import com.huabo.cybermonitor.util.DatabaseTableFieldParam;
import com.huabo.cybermonitor.util.DatabaseTableParam;
import com.huabo.cybermonitor.util.JDBCProperties;
import com.huabo.cybermonitor.util.TokenUtil;
import com.huabo.cybermonitor.vo.CheckConnectionParam;
import com.huabo.cybermonitor.vo.TblAuditModelDataSourceQueryParam;
import com.huabo.cybermonitor.vo.UserInfoParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;






@RestController
@Tag(name="智能监控-规则管理数据源及验证SQL接口",description="智能监控-规则管理数据源及验证SQL接口")
public class AuditModelController {

	private static final Logger log = LoggerFactory.getLogger(AuditModelController.class);

	@Resource
	private AuditModelService auditModelService;
	 
	@Resource
	private TblAuditModelDataSourceOracleService tblAuditModelExcelOracleService;
	
	
	@Autowired
    IMonitorSolutionRuleService iMonitorSolutionRuleService;
	
	 @Autowired
	 IMonitorRuleService iMonitorRuleService;
	 
	 
	 @Resource
	 private UserProvider userProvider;
	


	@Operation(summary = "数据源管理 列表查询")
	@PostMapping("/model/data/source/getList")
	public JsonBean getTblAuditModelDataSourceList(@RequestHeader("token") String token,
			@RequestBody @Validated TblAuditModelDataSourceQueryParam param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			
			UserInfoParam userInfo = TokenUtil.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = auditModelService.getTblAuditModelDataSourceList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数据源管理 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "数据源管理 新增/更新")
	@PostMapping("/model/data/source/saveOrUpdate")
	public JsonBean saveOrUpdateTblAuditModelDataSource(@RequestHeader("token") String token,
			@RequestBody @Validated TblAuditModelDataSourceOracle param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			
			UserInfoParam userInfo = TokenUtil.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = auditModelService.saveOrUpdateTblAuditModelDataSource(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数据源管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "数据源管理 刪除")
	@DeleteMapping("/model/data/source/{id}")
	public JsonBean deleteTblAuditModelDataSource(@RequestHeader("token") String token, @PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			
			jsonBean = auditModelService.deleteTblAuditModelDataSource(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数据源管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "数据源管理 查询")
	@GetMapping("/model/data/source/{id}")
	public JsonBean getTblAuditModelDataSource(@RequestHeader("token") String token, @PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = auditModelService.getTblAuditModelDataSource(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数据源管理 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	
	
	 @Operation(summary = "校验")
	 @PostMapping("/sjmx/check/connection")
	    public JsonBean checkConnection(@RequestBody @Validated CheckConnectionParam param) throws Exception {
	        boolean flag = false;
	        TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
	        String dataBaseConnectionAddress = param.getDataBaseConnectionAddress();
	        String dataBaseUsers = param.getDataBaseUsers();
	        String dataBasePassWord = param.getDataBasePassWord();
	        if (Objects.equals(param.getDataBaseType(), "Oracle")) {
	            flag = JDBCProperties.checkConnection(dataBaseConnectionAddress, dataBaseUsers, dataBasePassWord);
	        }
	        if (Objects.equals(param.getDataBaseType(), "Mysql")) {
	            flag = JDBCProperties.checkConnection(dataBaseConnectionAddress, dataBaseUsers, dataBasePassWord);
	        }
	        if (Objects.equals(param.getDataBaseType(), "SqlServer")) {
	            flag = JDBCProperties.checkConnection(dataBaseConnectionAddress, dataBaseUsers, dataBasePassWord);
	        }
			if (Objects.equals(param.getDataBaseType(), "Dm")) {
	            flag = JDBCProperties.checkConnection(dataBaseConnectionAddress, dataBaseUsers, dataBasePassWord);
	        }
	        if (flag) {
	            return ResponseFormat.retParam(1, "连接测试成功", null);
	        } else {
	            return ResponseFormat.retParam(0, "连接测试异常", null);
	        }
	    }
	
	
	 
	 
	 @Operation(summary = "规则管理-数据库列表")
	    @PostMapping("/sjmx/database/table/get-list")
	    public JsonBean getDatabaseTableList(@RequestHeader("token") String token, @RequestBody @Validated DatabaseTableParam param) throws Exception {
	        JsonBean jsonBean = null;
	        TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
	        try {
	            if (param.getBookid() != null) {
	                TblAuditModelDataSourceOracle data = tblAuditModelExcelOracleService.findById(Integer.parseInt(param.getBookid()));
	                if (data == null) {
	                    return ResponseFormat.retParam(0, "选择的数据源不存在", null);
	                }else if (data != null && data.getDataBaseType().equals("Oracle")) {
	                    String sql = "select TABLE_NAME from all_tables where owner='" + data.getDataBaseUsers() + "'";
	                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
	                    jsonBean = JDBCProperties.GetGatherListoracle(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
	                }else if (data != null && data.getDataBaseType().equals("Mysql")) {
	                    String cname = data.getDataBaseConnectionAddress().substring((data.getDataBaseConnectionAddress().lastIndexOf("/") + 1), data.getDataBaseConnectionAddress().length());
	                    String sql = "select table_name from information_schema.tables where table_schema='" + cname + "'";
	                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
	                    jsonBean = JDBCProperties.GetGatherListmysql(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
	                }else if (data != null && data.getDataBaseType().equals("SqlServer")) {
	                    String sql = "select name as TABLE_NAME from sysobjects where xtype='u' order by name";
	                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
	                    jsonBean = JDBCProperties.GetGatherListSqlServer(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
	                }else if (data != null && data.getDataBaseType().equals("Dm")) {
						String cname = getSchemaName(data.getDataBaseConnectionAddress());

						String sql = "SELECT TABLE_NAME FROM DBA_TABLES WHERE OWNER ='" + cname + "'";
						jsonBean = JDBCProperties.GetGatherListmysql(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
					}else {
						return ResponseFormat.retParam(0, "暂不支持的数据源类型", null);
					}

	            }


	        } catch (Exception e) {
	            log.error("规则管理-数据库列表 异常：", e);
	            return ResponseFormat.retParam(0, "规则管理-数据库列表 异常", null);
	        }
	        return jsonBean;
	    }
	 
	 
	 
	 
	 @Operation(summary = "规则管理-数据库-表字段列表")
	    @PostMapping("/sjmx/database/table/field/get-list")
	    public JsonBean getDatabaseTableFieldList(@RequestHeader("token") String token, @RequestBody @Validated DatabaseTableFieldParam param) throws Exception {
	        JsonBean jsonBean = null;
	        TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
	        try {
	            if (param.getBookid() != null) {
	                TblAuditModelDataSourceOracle data = tblAuditModelExcelOracleService.findById(Integer.parseInt(param.getBookid()));
	                if (data == null) {
	                    return ResponseFormat.retParam(0, "选择的数据源不存在", null);
	                }
	                if (data != null && data.getDataBaseType().equals("Oracle")) {
	                    String sql = "select COLUMN_NAME from user_tab_columns where table_name = '" + param.getTable() + "'";
	                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
	                    jsonBean = JDBCProperties.GetGatherListoracle(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
	                }else if (data != null && data.getDataBaseType().equals("Mysql")) {
	                    String cname = data.getDataBaseConnectionAddress().substring((data.getDataBaseConnectionAddress().lastIndexOf("/") + 1), data.getDataBaseConnectionAddress().length());
	                    String sql = "select COLUMN_NAME from information_schema.COLUMNS where TABLE_NAME='" + param.getTable() + "'";
	                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
	                    jsonBean = JDBCProperties.GetGatherListmysql(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
	                }else if (data != null && data.getDataBaseType().equals("SQLServer")) {
	                    String sql = "select COLUMN_NAME from information_schema.COLUMNS where TABLE_NAME='" + param.getTable() + "'";
	                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
	                    jsonBean = JDBCProperties.GetGatherListSqlServer(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
	                }else if (data != null && data.getDataBaseType().equals("Dm")) {
						String cname = getSchemaName(data.getDataBaseConnectionAddress());
						String sql = "SELECT COLUMN_NAME FROM DBA_TAB_COLUMNS WHERE TABLE_NAME = '"+param.getTable()+"' AND OWNER = '" + cname + "'";
						jsonBean = JDBCProperties.GetGatherListmysql(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
					}else{
						return ResponseFormat.retParam(0, "暂不支持的数据源类型", null);
					}

	            }


	        } catch (Exception e) {
	            log.error("规则管理-数据库列表 异常：", e);
	            return ResponseFormat.retParam(0, "规则管理-数据库列表 异常", null);
	        }
	        return jsonBean;
	    }
	 
	 /**
	     * 规则管理证SQL
	     */
	    @RequestMapping(value = "/sjmx/sqlyz", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	    @Operation(summary = "规则管理验证SQL")
	    public JsonBean sjmxgetList(HttpServletRequest request,
	                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	                                @Parameter(name = "sql", description = "SQL", required = false) @RequestParam(value = "sql", required = false) String sql,
	                                @Parameter(name = "bookid", description = "bookid", required = false) @RequestParam(value = "bookid", required = false) String bookid,
	                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
	        JsonBean jsonBean = null;
	        try {
	        	TblStaffUtil loginStaff = userProvider.get();
				if (loginStaff == null) {
					return ResponseFormat.retParam(0, 20006, null);
				}
	            if (bookid != null && bookid.trim().length() > 0) {
	                //TblAccBook accBook = accbookservice.findByBookIdOne(bookid);
	                PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>();
	                pageInfo.setPageSize(pageSize);
	                pageInfo.setCurrentPage(pageNumber);
	                //jsonBean = JDBCProperties.GetGather(accBook.getAcctid(), sql, pageInfo);

	                TblAuditModelDataSourceOracle data = tblAuditModelExcelOracleService.findById(Integer.parseInt(bookid));
	                if (data == null) {
	                    return ResponseFormat.retParam(0, "选择的数据源不存在", null);

	                } else if (data != null && data.getDataBaseType().equals("Oracle")) {
	                    jsonBean = JDBCProperties.GetGatheroracle(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, pageInfo);

	                } else if (data != null && data.getDataBaseType().equals("Mysql")) {
	                    jsonBean = JDBCProperties.GetGathermysql(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, pageInfo);

	                } else if (data != null && data.getDataBaseType().equals("SqlServer")) {
						jsonBean = JDBCProperties.GetGatherSqlServer(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, pageInfo);

					}else if (data != null && data.getDataBaseType().equals("Dm")) {
						jsonBean = JDBCProperties.GetGatherSqlServer(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, pageInfo);

					}else {
						return ResponseFormat.retParam(0, "暂不支持的数据源类型", null);
					}


	            } else {
	                jsonBean = iMonitorSolutionRuleService.getList(token, pageNumber, pageSize, sql);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(0, "SQL有误，请修改", null);
	        }
	        return jsonBean;
	    }
	    
	    
	    /**
	     * 规则管理sql结果导出
	     */
	    @RequestMapping(value = "/sjmx/sqljgexport", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	    @Operation(summary = "规则管理sql结果导出")
	    public void sqljgexport(HttpServletRequest request, HttpServletResponse response,
	                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                            @Parameter(name = "sql", description = "SQL", required = false) @RequestParam(value = "sql", required = false) String sql,
	                            @Parameter(name = "bookid", description = "bookid", required = false) @RequestParam(value = "bookid", required = false) String bookid) {
	        try {
	        	TblStaffUtil loginStaff = userProvider.get();
				if (loginStaff == null) {
					return ;
				}
	            List<Object[]> contractlist = null;
	            List<String> cNames = null;
	            if (bookid != null && bookid.trim().length() > 0) {

	                TblAuditModelDataSourceOracle data = tblAuditModelExcelOracleService.findById(Integer.parseInt(bookid));

	                if (data != null && data.getDataBaseType().equals("Oracle")) {
	                    cNames = JDBCProperties.getTbableall(sql, null, data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
	                    contractlist = JDBCProperties.GetGatheroracleall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, cNames.size());
	                }
	                if (data != null && data.getDataBaseType().equals("Mysql")) {
	                    cNames = JDBCProperties.getTbableall(sql, "1", data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
	                    contractlist = JDBCProperties.GetGathermysqlall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, cNames.size());
	                }
	                if (data != null && data.getDataBaseType().equals("SqlServer")) {
	                    cNames = JDBCProperties.getTbableall(sql, "2", data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
	                    contractlist = JDBCProperties.GetGatherSqlserverall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, cNames.size());
	                }
					if (data != null && data.getDataBaseType().equals("Dm")) {
						cNames = JDBCProperties.getTbableall(sql, "3", data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
						contractlist = JDBCProperties.GetGathermysqlall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, cNames.size());
					}

	            }
	            response.setHeader("Content-Disposition", "attachment;filename=" + new String("规则管理SQL查询结果".getBytes(), "UTF-8") + ".xlsx");
	            ServletOutputStream outputStream = response.getOutputStream();
	            ImportOrExportExcelUtil.exportExcelsj(cNames, contractlist, outputStream, null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /**
	     * 规则管理-sql执行
	     */
	    @Operation(summary = "zxsql")
	    @GetMapping("/sjmx/zxsql")
	    public JsonBean zxsql(HttpServletRequest request,
	                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                          @Parameter(name = "ruleid", description = "主键ruleid", required = true) @RequestParam(value = "ruleid", required = true) BigDecimal ruleid) {

	        JsonBean jsonBean = null;
	        try {
	        	TblStaffUtil loginStaff = userProvider.get();
				if (loginStaff == null) {
					return ResponseFormat.retParam(0, 20006, null);
				}
	        	 MonitorRule rule = iMonitorRuleService.getById(ruleid);
				if (Objects.isNull(rule)) {
					throw new ServiceException("规则信息不存在！");
				}
	            jsonBean = tblAuditModelExcelOracleService.zxsql(token, rule);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(1, 1000, e.getMessage());
	        }
	        return jsonBean;
	    }
	    
	    

	    /**
	     * 规则管理-sql执行过程查询
	     */
	    @Operation(summary = "zxReslut")
	    @GetMapping("/sjmx/zxReslut")
	    public JsonBean zxReslut(HttpServletRequest request,
	                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                             @Parameter(name = "ruleid", description = "规则主键ruleid", required = true) @RequestParam(value = "ruleid", required = true) BigDecimal ruleid) {

	        JsonBean jsonBean = null;
	        try {
	            jsonBean = tblAuditModelExcelOracleService.getxjjgList(token, ruleid);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(1, 1000, e.getMessage());
	        }
	        return jsonBean;
	    }
	    
	    
	    
	    /**
	     * 规则管理-sql执行结果查询
	     */
	    @Operation(summary = "查询列表")
	    @GetMapping("/sjmx/getDatelist")
	    public JsonBean getDatelist(HttpServletRequest request,
	                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                                @Parameter(name = "resultid", description = "过程记录主键resultid", required = true) @RequestParam(value = "resultid", required = true) BigDecimal resultid,
	                                @Parameter(name = "ruleid", description = "规则主键ruleid", required = true) @RequestParam(value = "ruleid", required = true) BigDecimal ruleid,
	                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

	        JsonBean jsonBean = null;
	        try {
				MonitorRule rule = iMonitorRuleService.getById(ruleid);
				if (Objects.isNull(rule)) {
					throw new ServiceException("规则信息不存在！");
				}

				jsonBean = tblAuditModelExcelOracleService.getDatelistt(token, resultid, rule, pageNumber, pageSize);
			} catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(1, 1000, e.getMessage());
	        }
	        return jsonBean;
	    }

	public String getSchemaName(String url) {
		if (StringUtils.isBlank(url)) {
			throw new ServiceException("未获取到数据库连接！");
		}
		int lastSlashIndex = url.lastIndexOf('/');
		if (lastSlashIndex == -1) {
			throw new ServiceException("未获取到数据库名称！");
		}

		int questionMarkIndex = url.indexOf('?');
		if (questionMarkIndex == -1) {
			return url.substring(lastSlashIndex + 1);
		} else {
			return url.substring(lastSlashIndex + 1, questionMarkIndex);
		}
	}

}
