package com.huabo.finance.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.entity.TblConfigTableInfo;
import com.huabo.finance.service.BdImportBatchsRecordService;
import com.huabo.finance.service.ExcelImportService;
import com.huabo.finance.service.GatherFinanceDateService;
import com.huabo.finance.service.TblConfigTableInfoService;
import com.huabo.finance.vo.BdImportBatchsRecordVo;
import com.huabo.finance.vo.BusinessDataVo;
import com.huabo.finance.vo.TblConfigTableInfoVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/budata")
@Tag(name="财务微服务",description="财务微服务")
public class BusinessDataController {
	
	// 允许的 Excel 文件扩展名
    private static final String[] EXCEL_EXTENSIONS = {".xls", ".xlsx"};
    // 允许的 Excel 内容类型
    private static final String[] EXCEL_CONTENT_TYPES = {
        "application/vnd.ms-excel",                      // .xls
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xlsx
    };
	

	@Resource
	private TblConfigTableInfoService tblConfigTableInfoService;
	
	@Resource
	private GatherFinanceDateService gatherFinanceDateService;
	
	@Resource
	private ExcelImportService excelImportService;
	
	@Resource
	private BdImportBatchsRecordService bdImportBatchsRecordService;
	
	
	@GetMapping(value = "/getTablInfoList",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-获取表列表信息")
	public JsonBean table_getFinanceDataList(HttpServletRequest request, HttpServletResponse response,TblConfigTableInfoVo vo) throws Exception {
		return this.tblConfigTableInfoService.getTablInfoList(vo);
	}
	
	@PostMapping(value = "/addTableInfo",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-新增表信息")
	public JsonBean table_addTableInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody TblConfigTableInfo tableInfo) throws Exception {
		return this.tblConfigTableInfoService.addTableInfo(tableInfo);
	}
	
	@PostMapping(value = "/modifyTableInfo",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-修改表信息")
	public JsonBean table_modifyTableInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestBody TblConfigTableInfo tableInfo) throws Exception {
		return this.tblConfigTableInfoService.modifyTableInfo(tableInfo);
	}
	
	@PostMapping(value = "/executeSql",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-sql语句执行结果查看")
	public JsonBean table_executeSql(HttpServletRequest request, HttpServletResponse response,
			@RequestBody TblConfigTableInfo tableInfo) throws Exception {
		return this.tblConfigTableInfoService.executeSql(tableInfo);
	}
	
	
	
	@GetMapping(value = "/removeTableInfo",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-删除表信息")
	public JsonBean table_removeTableInfo(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		return this.tblConfigTableInfoService.removeTableInfo(fid);
	}
	
	@GetMapping(value = "/getTableInfo",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-获取表信息")
	public JsonBean table_getTableInfo(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		return this.tblConfigTableInfoService.getTableInfo(fid);
	}
	
	@GetMapping(value = "/tableCreate",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-创建库表信息-发布")
	public JsonBean table_tableCreate(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		return this.tblConfigTableInfoService.tableCreate(fid);
	}
	
	@GetMapping(value = "/tableFstatus",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-库表启用弃用")
	public JsonBean table_tableFstatus(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="主键",required=true)@RequestParam(value = "fid",required = true)String fid,
			@Parameter(name="fstatus",description="表单状态 0-草稿  1-已启用  2-已弃用",required=true)@RequestParam(value = "fstatus",required = true)Integer fstatus) throws Exception {
		return this.tblConfigTableInfoService.tableFstatus(fid,fstatus);
	}
	
	@GetMapping(value = "/gatherBussinessData",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-单独采集业务数据信息")
	public JsonBean table_gatherBussinessData(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="表主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		return this.gatherFinanceDateService.gatherBussinessData(fid,request,response);
	}
	
	@GetMapping(value = "/stopGatherData",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-停止单独采集业务数据信息")
	public JsonBean stopGatherData(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="fid",description="表主键",required=true)@RequestParam(value = "fid",required = true)String fid) throws Exception {
		return this.gatherFinanceDateService.stopGatherData(fid);
	}
	
	
	@GetMapping(value = "/showTableList",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-账簿数据点击业务数据获取左侧业务表数据")
	public JsonBean table_cwzbshowTableList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return this.tblConfigTableInfoService.cwzbshowTableList();
	}

	@PostMapping(value = "/showBusinessDataList",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-账簿数据点击业务数据获取右侧业务表单列表数据")
	public JsonBean table_cwzbshowBusinessDataList(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BusinessDataVo vo
			) throws Exception {
		return this.tblConfigTableInfoService.cwzbshowBusinessDataList(vo);
	}
	
	@PostMapping(value = "/showBusinessDataDetail",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-账簿数据点击业务数据通过主键参数获取详情数据")
	public JsonBean table_cwzbshowBusinessDataDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody BusinessDataVo vo
			) throws Exception {
		return this.tblConfigTableInfoService.cwzbshowBusinessDataDetail(vo);
	}
	
	
	@PostMapping(value = "/getExportTemplate",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-下载导入模板")
	public JsonBean table_getExportTemplate(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="tableId",description="表主键",required=true)@RequestParam(value = "tableId",required = true)String tableId
			) throws Exception {
		return this.tblConfigTableInfoService.getExportTemplate(tableId,response);
	}
	
	
	@PostMapping(value = "/importTemplateData",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-导入模板数据")
	public JsonBean table_importTemplateData(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="tableId",description="表主键",required=true)@RequestParam(value = "tableId",required = true)String tableId,
			@RequestParam("file") MultipartFile file
			) throws Exception {
		if (file.isEmpty()) {
			return ResponseFormat.retParam(0, "请上传文件", null);
        }
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new IllegalArgumentException("无效的文件名");
        }
        
        // 获取小写的文件扩展名
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        
     // 2. 检查内容类型
        String contentType = file.getContentType();
		
        // 3. 双重验证
        boolean validExtension = false;
        boolean validContentType = false;
        
        // 检查扩展名
        for (String ext : EXCEL_EXTENSIONS) {
            if (ext.equals(fileExtension)) {
                validExtension = true;
                break;
            }
        }
        
        // 检查内容类型
        if (contentType != null) {
            for (String mime : EXCEL_CONTENT_TYPES) {
                if (contentType.equalsIgnoreCase(mime)) {
                    validContentType = true;
                    break;
                }
            }
        }
     // 4. 验证结果
        if (!validExtension) {
        	return ResponseFormat.retParam(0, "不支持的文件类型: " + fileExtension + ". 请上传 Excel 文件 (.xls, .xlsx)", null);
        }
        
        if (!validContentType) {
        	return ResponseFormat.retParam(0,  "不支持的内容类型: " + contentType + ". 请上传正确的 Excel 文件", null);
        }
		return this.excelImportService.importTemplateData(tableId,file,request);
	}
	
	
	@GetMapping(value = "/getImportRecordList",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-获取导入记录列表")
	public JsonBean table_getImportRecordList(HttpServletRequest request, HttpServletResponse response,BdImportBatchsRecordVo vo) throws Exception {
		return this.bdImportBatchsRecordService.getImportRecordList(vo);
	}
	
	@GetMapping(value = "/getImportRecordDetail",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-获取导入记录详情")
	public JsonBean table_getImportRecordDetail(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="importId",description="导入记录主键",required=true)@RequestParam(value = "importId",required = true)String importId) throws Exception {
		return this.bdImportBatchsRecordService.getImportRecordDetail(importId);
	}
	
	@GetMapping(value = "/removeImportData",produces = "application/json; charset=utf-8")
	@Operation(summary = "业务数据-清除本次导入数据")
	public JsonBean table_removeImportData(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="tableId",description="表主键",required=true)@RequestParam(value = "tableId",required = true)String tableId,
			@Parameter(name="importId",description="导入记录主键",required=true)@RequestParam(value = "importId",required = true)String importId) throws Exception {
		return this.bdImportBatchsRecordService.removeImportData(importId,tableId);
	}
	
}
