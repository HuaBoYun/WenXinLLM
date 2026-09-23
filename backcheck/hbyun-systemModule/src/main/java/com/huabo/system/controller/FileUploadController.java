package com.huabo.system.controller;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
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

import com.google.gson.Gson;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.service.business.FileUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 附件上传管理控制器
 * <p>提供文件上传、下载、删除等附件管理接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/fileManage")
@Tag(name = "附件上传接口")
@Slf4j
public class FileUploadController {

	@Resource
	private FileUploadService fileUploadService;
	
	@Resource
    private UserProvider userProvider;

	@Value("${application.file-url:}")
	private String fileUrl;
	
	@Value("${local-path}")
	private String localPath;
	
	@Value("${access-prefix}")
    private String accessPrefix;
	
	

    // 允许的图片后缀
    private static final String[] ALLOW_SUFFIX = {".jpg", ".jpeg", ".png", ".gif"};
    // 最大10MB
    private static final long MAX_SIZE = 10 * 1024 * 1024;

    @PostMapping("/upload/images")
    public String uploadImg(@RequestParam("file") MultipartFile file) throws Exception {
        // 1. 参数校验
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        // 文件大小校验
        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("图片不能超过10MB");
        }
        // 获取后缀
        String originalName = file.getOriginalFilename();
        String suffix = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(ALLOW_SUFFIX).contains(suffix)) {
            throw new RuntimeException("仅支持jpg/png/gif图片");
        }

        // 2. 创建存储文件夹，不存在自动创建
        File dir = new File(localPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 3. 生成唯一文件名（UUID防止重名覆盖）
        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        File saveFile = new File(localPath + fileName);

        // 4. 写入本地磁盘
        file.transferTo(saveFile);

        // 5. 返回前端直接可用的图片地址
        String imgUrl = accessPrefix + fileName;
        return imgUrl;
    }
	
	

	@PostMapping("/upload")
	@Operation(summary="文件上传接口")
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
	@Operation(summary="获取上传的url")
	public JsonBean getPrivewAttInfo(HttpServletResponse response,
			@Parameter(name = "fileId", description = "文件主键ID", required = true) @RequestParam("fileId") String fileId,
			@RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			jsonBean = fileUploadService.getPrivewAttInfo(response, fileId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("获取上传的url ...接口 异常", e);
		}
		return jsonBean;
	}

	@GetMapping("/download")
	@Operation(summary="文件下载接口")
	public JsonBean fileDownLoad(HttpServletResponse response,
			@Parameter(name = "fileId", description = "文件主键ID", required = true) @RequestParam("fileId") String fileId,
			@RequestHeader("token") String token) {
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			fileUploadService.fileDownLoad(response, fileId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("文件下载接口 ...接口 异常", e);
		}
		return null;
	}

	@Operation(summary="附件删除接口，不删除中间表关系")
	@DeleteMapping("/{id}")
	public JsonBean fileRemove(@PathVariable("id") BigDecimal id, @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			jsonBean = fileUploadService.fileRemove(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("文件下载接口 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary="本地图片上传")
	@PostMapping("/upload/image")
	public void uploadPicture(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) {
		JsonBean jsonBean = new JsonBean();
		String url = "";//返回存储路径
		System.out.println(file);
		String fileName = file.getOriginalFilename();//获取文件名加后缀
		if (fileName != null && fileName != "") {
			//获取项目路径
			String path = System.getProperty("user.dir") + "/upload/imgs"; //文件存储位置
			log.info("本地图片上传 获取项目路径:{}", path);
			String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());//文件后缀
			fileName = new Date().getTime() + "_" + new Random().nextInt(1000) + fileF;//新的文件名
			//先判断文件是否存在
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String fileAdd = sdf.format(new Date());
			//获取文件夹路径
			File file1 = new File(path + "/" + fileAdd);
			//如果文件夹不存在则创建
			if (!file1.exists() && !file1.isDirectory()) {
				file1.getParentFile().mkdirs();
			}
			//将图片存入文件夹
			File targetFile = new File(file1, fileName);
			try {
				//将上传的文件写到服务器上指定的文件。
				FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
				url = fileUrl + "/localUpload/imgs/" + fileAdd + "/" + fileName;
				jsonBean.setCode(200);
				jsonBean.setMsg("图片上传成功");
				jsonBean.setData(url);
			} catch (Exception e) {
				log.error("系统异常，图片上传失败", e);
				jsonBean.setCode(400);
				jsonBean.setMsg("系统异常，图片上传失败");
			}
		}
		writeJson(response, jsonBean);
	}

	@Operation(summary="本地图片上传-删除")
	@DeleteMapping("/upload/image")
	public void deletePicture(String url) {
		fileUploadService.deletePicture(url);
	}

	/**
	 * 输出JSON数据
	 *
	 * @param response
	 * @param obj
	 */
	public void writeJson(HttpServletResponse response, Object obj) {
		response.setContentType("text/json;charset=utf-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter pw = null;
		Gson gson = new Gson();
		try {
			pw = response.getWriter();
			pw.write(gson.toJson(obj));

			pw.flush();
		} catch (Exception e) {
			log.info("输出JSON数据异常", e);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
}
