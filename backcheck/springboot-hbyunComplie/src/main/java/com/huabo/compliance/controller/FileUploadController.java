package com.huabo.compliance.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.service.FileUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api-auth/fileManage")
@Tag(name="合规附件上传接口",description="合规附件上传接口")
@Slf4j
public class FileUploadController {

	@Resource
	private FileUploadService fileUploadService;
	
	@Resource
	private UserProvider userProvider;

	@PostMapping("/upload")
	@Operation(summary = "文件上传接口")
	public JsonBean fileUpload(@Parameter(name = "file", description = "附件上传", required = true) @RequestPart("file") MultipartFile[] file,
			@RequestHeader("token") String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		JsonBean jsonBean = null;
		try {
			jsonBean = fileUploadService.fileUpload(file, loginStaff.getRealname());
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("通用文件上传 ...接口 异常", e);
		}
		return jsonBean;
	}

	@GetMapping("/getPrivewAttInfo")
	@Operation(summary = "获取上传的url")
	public JsonBean getPrivewAttInfo(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "fileId", description = "文件主键ID", required = true) @RequestParam("fileId") String fileId) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		
		JsonBean jsonBean = null;
		try {
			jsonBean = fileUploadService.getPrivewAttInfo(response, Integer.parseInt(fileId));
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("获取上传的url ...接口 异常", e);
		}
		return jsonBean;
	}

	@GetMapping("/download")
	@Operation(summary = "文件下载接口")
	public JsonBean fileDownLoad(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name = "fileId", description = "文件主键ID", required = true) @RequestParam("fileId") String fileId) {
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
			
			fileUploadService.fileDownLoad(response, Integer.parseInt(fileId));
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("文件下载接口 ...接口 异常", e);
		}
		return null;
	}

	@Operation(summary = "附件删除接口，不删除中间表关系")
	@DeleteMapping("/{id}")
	public JsonBean fileRemove(@PathVariable("id") Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
			
			jsonBean = fileUploadService.fileRemove(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("文件下载接口 ...接口 异常", e);
		}
		return jsonBean;
	}
}
