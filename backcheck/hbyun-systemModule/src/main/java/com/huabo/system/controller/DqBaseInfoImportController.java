package com.huabo.system.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblStaffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 大庆人员组织信息导入控制器
 * <p>提供公司信息、部门信息的Excel批量导入功能</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "大庆人员组织信息导入", description = "大庆人员组织信息导入")
@RequestMapping("/dqImport")
public class DqBaseInfoImportController {
	
	
	@Resource
    public TblOrganizaService tblOrganizaService;
	
	@Resource
	public TblStaffService tblStaffService;
	

	@PostMapping("/importCompanyInfo")
    @Operation(summary="列表导入")
    public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name="importType",description="1-公司，0-部门",required = false)@RequestParam(value = "importType", required = false, defaultValue = "1") Integer importType
    ) throws IOException {
        InputStream in = file.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(in);
        try{
            for (int i =0; i < workBook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workBook.getSheetAt(i);
                tblOrganizaService.importCompanyExcelData(sheet,token,importType);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //读取完毕则关闭流
            in.close();
            workBook.close();
        }
        return ResponseFormat.retParam(1,200);
    }
	
	
	@PostMapping("/checkImportCompany")
    @Operation(summary="组织架构导入检查")
    public JsonBean importStaffInfo(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws IOException {
        InputStream in = file.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(in);
        JsonBean jsonBean = null;
        String companyName = "";
        String allName = "";
        try{
            for (int i =0; i < workBook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workBook.getSheetAt(i);
                companyName = tblOrganizaService.checkImportCompany(sheet,token);
                allName += companyName + ",";
            }
            jsonBean = ResponseFormat.retParam(1,200,allName);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //读取完毕则关闭流
            in.close();
            workBook.close();
        }
        return jsonBean;
    }
	
	@PostMapping("/importStaffInfo")
    @Operation(summary="组织架构导入检查")
    public JsonBean checkImportCompany(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws IOException {
        InputStream in = file.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(in);
        JsonBean jsonBean = null;
        try{
            for (int i =0; i < workBook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workBook.getSheetAt(i);
                tblStaffService.importStaffInfo(sheet,token);
            }
            jsonBean = ResponseFormat.retParam(1,200);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //读取完毕则关闭流
            in.close();
            workBook.close();
        }
        return jsonBean;
    }
	
	
	
}
