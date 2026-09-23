package com.huabo.finance.controller;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.entity.BdFinanceplan;
import com.huabo.finance.entity.BdFinversion;
import com.huabo.finance.entity.BdFinversionApi;
import com.huabo.finance.entity.BdPlanSqlconfig;
import com.huabo.finance.entity.VersionFieldMapping;
import com.huabo.finance.service.BdFinancedateService;
import com.huabo.finance.service.BdFinanceplanService;
import com.huabo.finance.service.BdFinversionService;
import com.huabo.finance.service.BdFinversionApiService;
import com.huabo.finance.service.BdInitSqlconfigService;
import com.huabo.finance.service.BdPlanSqlconfigService;
import com.huabo.finance.service.IVersionFieldMappingService;
import com.huabo.finance.vo.BdFinancedateVo;
import com.huabo.finance.vo.BdFinanceplanVo;
import com.huabo.finance.vo.BdFinversionVo;
import com.huabo.finance.vo.BdFinversionApiVo;
import com.huabo.finance.vo.BdPlanSqlconfigVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 财务数据版本 前端控制器
 * </p>
 *
 * @author L
 * @since 2025-03-10
 */
@Slf4j
@RestController
@RequestMapping(value = "/config")
@Tag(name="财务微服务",description="财务微服务")
public class BdConfigController {

	@Autowired
	private UserProvider userProvider;

	@Value("${offline.tool.template.path:docs/hbyun}")
	private String offlineToolTemplatePath;
	
	@Resource
	private BdFinversionService bdFinversionService;
	
	@Resource
	private BdFinancedateService bdFinancedateService;

	@Resource
	private IVersionFieldMappingService versionFieldMappingService;
	
	@Resource
	private BdFinanceplanService bdFinanceplanService;
	
	@Resource
	private BdPlanSqlconfigService bdPlanSqlconfigService;
	
	@Resource
	private BdInitSqlconfigService bdInitSqlconfigService;

	@Resource
	private BdFinversionApiService bdFinversionApiService;
	
	/**
	 * D 方案配置信息采集sql语句开始
	 */
	
	@GetMapping(value = "/fconfigSql/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集sql编辑时通过configId，获取初始采集sql配置")
	public JsonBean fconfigSql_detail(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdInitSqlconfigService.findAllList(staff,fid);
	}
	
	@GetMapping(value = "/fplanSql/getList",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集sql配置列表接口")
	public JsonBean fplanSql_getList(HttpServletRequest request, HttpServletResponse response,BdPlanSqlconfigVo pc) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdPlanSqlconfigService.findAllList(staff,pc);
	}
	
	
	
	@PostMapping(value = "/fplanSql/save",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集sql保存")
	public JsonBean fplanSql_save(HttpServletRequest request, HttpServletResponse response,BdPlanSqlconfig ps) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdPlanSqlconfigService.save(staff,ps);
	}
	
	@PostMapping(value = "/fplanSql/testSql",produces = "application/json; charset=utf-8")
	@Operation(summary = "sql测试")
	public JsonBean fplanSql_testSql(HttpServletRequest request, HttpServletResponse response,BdPlanSqlconfig ps) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdPlanSqlconfigService.testSql(staff,ps);
	}
	
	
	@PostMapping(value = "/fplanSql/excuteSql",produces = "application/json; charset=utf-8")
	@Operation(summary = "sql执行")
	public JsonBean fplanSql_excuteSql(HttpServletRequest request, HttpServletResponse response,BdPlanSqlconfig ps) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdPlanSqlconfigService.excuteSql(staff,ps);
	}
	
	@GetMapping(value = "/fplanSql/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集sql详情")
	public JsonBean fplanSql_detail(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdPlanSqlconfigService.detail(fid);
	}
	
	@GetMapping(value = "/fplanSql/remove",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集sql删除")
	public JsonBean fplanSql_remove(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=false)@RequestParam(value = "fid",required = false)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdPlanSqlconfigService.remove(fid);
	}
	
	/**
	 * D 方案配置版本信息采集sql语句 结束
	 */
	
	
	
	/**
	 * C.采集方案配置信息--开始
	 */
	
	@GetMapping(value = "/fplan/getList",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集方案列表接口")
	public JsonBean fplan_getList(HttpServletRequest request, HttpServletResponse response,BdFinanceplanVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinanceplanService.findAllList(staff,vo);
	}
	
	@PostMapping(value = "/fplan/save",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集方案保存")
	public JsonBean fplan_save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BdFinanceplan fd) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinanceplanService.save(staff,fd);
	}
	
	@GetMapping(value = "/fplan/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集方案详情")
	public JsonBean fplan_detail(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinanceplanService.detail(fid);
	}
	
	@GetMapping(value = "/fplan/remove",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集方案删除")
	public JsonBean fplan_remove(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinanceplanService.remove(fid);
	}
	
	
	@GetMapping(value = "/fplan/modifyStatus",produces = "application/json; charset=utf-8")
	@Operation(summary = "采集方案启用弃用")
	public JsonBean fplan_modifyStatus(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid,
			@Parameter(name="fstatus",description="采集方案状态  0-未启用 1-启用 2-弃用",required=true)@RequestParam(value = "fstatus",required = true)Integer fstatus) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinanceplanService.modifyStatus(fid,fstatus);
	}
	
	
	/**
	 * C.采集方案配置信息--结束 
	 */
	
	
	/**
	 * 
	 */
	
	/**
	 * B.数据源配置信息--开始
	 */
	@GetMapping(value = "/fdataSource/getList",produces = "application/json; charset=utf-8")
	@Operation(summary = "数据源列表接口")
	public JsonBean fdataSource_getList(HttpServletRequest request, HttpServletResponse response,BdFinancedateVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateService.findAllList(staff,vo);
	}
	
	@PostMapping(value = "/fdataSource/save",produces = "application/json; charset=utf-8")
	@Operation(summary = "数据源保存")
	public JsonBean fdataSource_save(HttpServletRequest request, HttpServletResponse response,BdFinancedate fd) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateService.save(staff,fd);
	}
	
	@GetMapping(value = "/fdataSource/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "数据源详情")
	public JsonBean fdataSource_getOne(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateService.detail(fid);
	}
	
	@GetMapping(value = "/fdataSource/remove",produces = "application/json; charset=utf-8")
	@Operation(summary = "数据源删除")
	public JsonBean fdataSource_remove(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateService.remove(fid);
	}
	
	@PostMapping(value = "/fdataSource/testCon",produces = "application/json; charset=utf-8")
	@Operation(summary = "数据源连接测试")
	public JsonBean fdataSource_testCon(HttpServletRequest request, HttpServletResponse response,BdFinancedate fd) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinancedateService.testCon(fd);
	}

	/**
	 * B.数据源配置信息--结束
	 */
	
	
	/**
	 *  A.财务版本信息控制--开始
	 */
	@GetMapping(value = "/fversion/getList",produces = "application/json; charset=utf-8")
	@Operation(summary = "版本信息获取接口")
	public JsonBean fversion_getList(HttpServletRequest request, HttpServletResponse response,BdFinversionVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionService.findAllList(staff,vo);
	}
	
	@GetMapping(value = "/fversion/getParentList",produces = "application/json; charset=utf-8")
	@Operation(summary = "版本信息获取父级信息")
	public JsonBean fversion_getList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionService.getParentList(staff);
	}
	
	
	@PostMapping(value = "/fversion/save",produces = "application/json; charset=utf-8")
	@Operation(summary = "版本信息保存")
	public JsonBean fversion_save(HttpServletRequest request, HttpServletResponse response,BdFinversion fv) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionService.save(staff,fv);
	}
	
	
	@GetMapping(value = "/fversion/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "版本信息详情")
	public JsonBean fversion_getOne(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionService.getOne(fid);
	}
	
	
	@GetMapping(value = "/fversion/remove",produces = "application/json; charset=utf-8")
	@Operation(summary = "版本信息删除")
	public JsonBean fversion_remove(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionService.remove(fid);
	}
	
	/**
	 *  A.财务版本信息控制--结束
	 */

	/**
	 *  E.系统API配置信息控制--开始
	 */
	@GetMapping(value = "/fversionApi/getList",produces = "application/json; charset=utf-8")
	@Operation(summary = "系统API配置列表接口")
	public JsonBean fversionApi_getList(HttpServletRequest request, HttpServletResponse response, BdFinversionApiVo vo) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionApiService.findAllList(staff,vo);
	}

	@GetMapping(value = "/fversionApi/getParentList",produces = "application/json; charset=utf-8")
	@Operation(summary = "系统API配置获取父级信息")
	public JsonBean fversionApi_getParentList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionApiService.getParentList(staff);
	}

	@PostMapping(value = "/fversionApi/save",produces = "application/json; charset=utf-8")
	@Operation(summary = "系统API配置保存")
	public JsonBean fversionApi_save(HttpServletRequest request, HttpServletResponse response, @RequestBody BdFinversionApi fv) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionApiService.save(staff,fv);
	}

	@GetMapping(value = "/fversionApi/detail",produces = "application/json; charset=utf-8")
	@Operation(summary = "系统API配置详情")
	public JsonBean fversionApi_getOne(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionApiService.getOne(fid);
	}

	@GetMapping(value = "/fversionApi/remove",produces = "application/json; charset=utf-8")
	@Operation(summary = "系统API配置删除")
	public JsonBean fversionApi_remove(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionApiService.remove(fid);
	}

	@PostMapping(value = "/fversionApi/testConnection",produces = "application/json; charset=utf-8")
	@Operation(summary = "系统API连接测试")
	public JsonBean fversionApi_testConnection(HttpServletRequest request, HttpServletResponse response, @RequestBody BdFinversionApi api) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return this.bdFinversionApiService.testApiConnection(staff, api);
	}

	/**
	 *  E.系统API配置信息控制--结束
	 */

	/**
	 * 下载离线采集工具
	 * 功能说明:
	 * 1. 从云端同步最新的版本信息和数据源配置
	 * 2. 更新离线工具的settings.config.json配置文件
	 * 3. 打包成ZIP文件供用户下载
	 */
	@GetMapping(value = "/offline/download", produces = "application/octet-stream")
	@Operation(summary = "下载离线采集工具")
	public void downloadOfflineTool(HttpServletRequest request, HttpServletResponse response) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("未授权访问");
			return;
		}

		FileInputStream fis = null;
		OutputStream os = null;
		ZipOutputStream zos = null;

		try {
			log.info("开始准备离线采集工具下载,用户:{}", staff.getRealname());

			// 1. 查询最新版本信息
			BdFinversionVo versionVo = new BdFinversionVo();
			JsonBean versionResult = bdFinversionService.findAllList(staff, versionVo);
			List<BdFinversion> versionList = null;
			if (versionResult.getCode() == 1 && versionResult.getData() != null) {
				versionList = (List<BdFinversion>) versionResult.getData();
			}

			// 2. 查询数据源配置
			BdFinancedateVo dataSourceVo = new BdFinancedateVo();
			dataSourceVo.setPageNumber(1);
			dataSourceVo.setPageSize(1000); // 设置足够大的分页大小,获取所有数据源
			JsonBean dataSourceResult = bdFinancedateService.findAllList(staff, dataSourceVo);
			List<BdFinancedate> dataSourceList = null;
			if (dataSourceResult.getCode() == 1 && dataSourceResult.getData() != null) {
				// 从Page对象中提取records列表
				com.baomidou.mybatisplus.extension.plugins.pagination.Page<BdFinancedate> page =
					(com.baomidou.mybatisplus.extension.plugins.pagination.Page<BdFinancedate>) dataSourceResult.getData();
				dataSourceList = page.getRecords();
			}

			// 3. 构建配置JSON对象
			JSONObject configJson = new JSONObject();
			configJson.put("Language", "zh-CN");
			configJson.put("Theme", "Light");
			configJson.put("FontSize", 12);
			configJson.put("CollectionTimeout", 3600);
			configJson.put("BatchSize", 1000);
			configJson.put("ThreadCount", 4);
			configJson.put("EnableDetailedLogging", true);
			configJson.put("OnlineSystemUrl", "http://localhost:8099");
			configJson.put("ConnectionTimeout", 30);
			configJson.put("EnableProxy", false);

			// 添加版本信息
			if (versionList != null && !versionList.isEmpty()) {
				configJson.put("Versions", versionList);

				// 为每个版本添加字段映射配置模板
				JSONArray versionsWithMappings = new JSONArray();
				for (BdFinversion version : versionList) {
					JSONObject versionObj = (JSONObject) JSON.toJSON(version);

					// 查询该版本的字段映射配置
					if (version.getFid() != null) {
						List<VersionFieldMapping> mappings = versionFieldMappingService.getByVersionFid(version.getFid());
						if (mappings != null && !mappings.isEmpty()) {
							versionObj.put("FieldMappings", mappings);
						}
					}

					// 递归处理子版本
					if (version.getChildrenList() != null && !version.getChildrenList().isEmpty()) {
						JSONArray childrenWithMappings = new JSONArray();
						for (BdFinversion child : version.getChildrenList()) {
							JSONObject childObj = (JSONObject) JSON.toJSON(child);

							// 查询子版本的字段映射配置
							if (child.getFid() != null) {
								List<VersionFieldMapping> childMappings = versionFieldMappingService.getByVersionFid(child.getFid());
								if (childMappings != null && !childMappings.isEmpty()) {
									childObj.put("FieldMappings", childMappings);
								}
							}
							childrenWithMappings.add(childObj);
						}
						versionObj.put("childrenList", childrenWithMappings);
					}

					versionsWithMappings.add(versionObj);
				}
				configJson.put("Versions", versionsWithMappings);
			}

			// 添加数据源信息
			if (dataSourceList != null && !dataSourceList.isEmpty()) {
				configJson.put("DataSources", dataSourceList);
			}

			// 4. 创建临时目录
			String tempDir = System.getProperty("java.io.tmpdir") + File.separator + "offline_tool_" + System.currentTimeMillis();
			File tempDirFile = new File(tempDir);
			if (!tempDirFile.exists()) {
				tempDirFile.mkdirs();
			}

			// 5. 复制离线工具模板文件(先复制模板文件)
			File templateDir = new File(offlineToolTemplatePath);
			if (!templateDir.exists() || !templateDir.isDirectory()) {
				throw new Exception("离线工具模板目录不存在: " + offlineToolTemplatePath);
			}
			copyDirectory(templateDir, tempDirFile);

			// 6. 写入配置文件(覆盖模板中的配置文件)
			File configFile = new File(tempDir + File.separator + "settings.config.json");
			try (FileOutputStream fos = new FileOutputStream(configFile)) {
				fos.write(configJson.toJSONString().getBytes(StandardCharsets.UTF_8));
			}

			// 7. 打包成ZIP文件
			String zipFileName = "华博云数据采集工具.zip";
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" +
				new String(zipFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));

			os = new BufferedOutputStream(response.getOutputStream());
			zos = new ZipOutputStream(os);

			// 压缩临时目录
			zipDirectory(tempDirFile, tempDirFile.getName(), zos);

			zos.finish();
			zos.flush();

			log.info("离线采集工具下载成功,用户:{}", staff.getRealname());

			// 8. 清理临时文件
			deleteDirectory(tempDirFile);

		} catch (Exception e) {
			log.error("下载离线采集工具失败", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("下载失败: " + e.getMessage());
		} finally {
			if (fis != null) {
				try { fis.close(); } catch (Exception e) { }
			}
			if (zos != null) {
				try { zos.close(); } catch (Exception e) { }
			}
			if (os != null) {
				try { os.close(); } catch (Exception e) { }
			}
		}
	}

	/**
	 * 复制目录
	 */
	private void copyDirectory(File sourceDir, File targetDir) throws Exception {
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}

		File[] files = sourceDir.listFiles();
		if (files != null) {
			for (File file : files) {
				File targetFile = new File(targetDir, file.getName());
				if (file.isDirectory()) {
					copyDirectory(file, targetFile);
				} else {
					copyFile(file, targetFile);
				}
			}
		}
	}

	/**
	 * 复制文件
	 */
	private void copyFile(File source, File target) throws Exception {
		try (FileInputStream fis = new FileInputStream(source);
			 FileOutputStream fos = new FileOutputStream(target)) {
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
		}
	}

	/**
	 * 压缩目录
	 */
	private void zipDirectory(File dir, String baseName, ZipOutputStream zos) throws Exception {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					zipDirectory(file, baseName + File.separator + file.getName(), zos);
				} else {
					try (FileInputStream fis = new FileInputStream(file)) {
						ZipEntry zipEntry = new ZipEntry(baseName + File.separator + file.getName());
						zos.putNextEntry(zipEntry);

						byte[] buffer = new byte[8192];
						int length;
						while ((length = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, length);
						}

						zos.closeEntry();
					}
				}
			}
		}
	}

	/**
	 * 删除目录
	 */
	private void deleteDirectory(File dir) {
		if (dir.exists()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						deleteDirectory(file);
					} else {
						file.delete();
					}
				}
			}
			dir.delete();
		}
	}

}

