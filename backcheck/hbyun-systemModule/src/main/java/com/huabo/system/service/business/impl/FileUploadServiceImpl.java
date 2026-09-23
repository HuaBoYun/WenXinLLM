package com.huabo.system.service.business.impl;

import cn.hutool.core.util.StrUtil;
import com.hbfk.entity.TblAttachment;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.entity.TblSystemFileOracle;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.oracle.service.TblSystemFileOracleService;
import com.huabo.system.service.business.FileUploadService;
import com.huabo.system.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

	private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

	@Resource
	private TblSystemFileOracleService tblSystemFileOracleService;

	@Value("${application.file-url:}")
	private String fileUrl;

	/**
	 * 文件上传
	 * @param file
	 * @param realname
	 */
	@Override
	public JsonBean fileUpload(MultipartFile[] file, String realname) {
		HashMap<String, Object> map = new HashMap<>();
		String attPath = "";
			List<TblSystemFileOracle> fileIds = new ArrayList<>();
			TblSystemFileOracle TblSystemFileOracleModel = new TblSystemFileOracle();
			for (MultipartFile multipartFile : file) {
				try {
					String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
					String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());

					InputStream inputStream = multipartFile.getInputStream();
					long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称

					attPath = FtpUtil.uploadFilePath(imageName + name + "", inputStream);
					if (StrUtil.isEmpty(attPath)) {
						throw new ServiceException(400, "文件上传失败");
					}
					TblSystemFileOracleModel.setFilePath(attPath + imageName + name);
					TblSystemFileOracleModel.setFileSize((double) (multipartFile.getSize() / 1024));
					TblSystemFileOracleModel.setUploadTime(new Date());
					TblSystemFileOracleModel.setUploader(realname);
					TblSystemFileOracleModel.setFileName(fileName);
					TblSystemFileOracle tblFwglFileMySql = tblSystemFileOracleService.saveOrUpdate(TblSystemFileOracleModel);
					fileIds.add(tblFwglFileMySql);
				} catch (Exception e) {
					log.error("文件上传", e);
					throw new ServiceException(400, "上传失败");
				}
			}
			map.put("fileIds", fileIds);
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 下载文件
	 * @param response
	 * @param fileId
	 * @return
	 */
	@Override
	public JsonBean fileDownLoad(HttpServletResponse response, String fileId) {
		TblSystemFileOracle TblSystemFileOracle = tblSystemFileOracleService.findById(new BigDecimal(fileId));
		if (TblSystemFileOracle == null) {
			throw new ServiceException(400, "附件不存在或已经删除");
		}
		TblAttachment tblAttachment = new TblAttachment();
		tblAttachment.setAttname(TblSystemFileOracle.getFileName());
		tblAttachment.setAttpath(TblSystemFileOracle.getFilePath());
		tblAttachment.setFileName(TblSystemFileOracle.getFileName());
		tblAttachment.setAttsize(TblSystemFileOracle.getFileSize());
		FtpUtil.downUploadFileNew(tblAttachment, response);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean fileRemove(BigDecimal id) {
		TblSystemFileOracle TblSystemFileOracle = tblSystemFileOracleService.findById(id);
		if (TblSystemFileOracle == null) {
			throw new ServiceException(400, "附件不存在或已经删除");
		}
		//文件删除
		try {
			FtpUtil.removeFile(TblSystemFileOracle.getFilePath());
		} catch (Exception e) {
			log.error("删除文件异常：", e);
		}
		tblSystemFileOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 获取上传的url
	 * @param response
	 * @param parseInt
	 */
	@Override
	public JsonBean getPrivewAttInfo(HttpServletResponse response, String fileId) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		String ftpUrl = FtpUtil.Ftpip;
		String fileName = null;
		TblSystemFileOracle byId = tblSystemFileOracleService.findById(new BigDecimal(fileId));
		if (StringUtils.isNotBlank(byId.getFilePath())) {
			//ftpUrl += FtpUtil.Uploadfilepath;
			fileName = byId.getFileName();
			if (fileName == null || "".equals(fileName)) {
				return ResponseFormat.retParam(0, 50006, null);
			}
			//ftpUrl+=byId.getFilePath()+fileName.substring(fileName.lastIndexOf("."), fileName.length());
			ftpUrl += byId.getFilePath();
			//ftpUrl= byId.getFileName();
			resultMap.put("ftpUrl", "ftp://" + ftpUrl);
			resultMap.put("previewurl", FtpUtil.previewurl);
			resultMap.put("param", "url");
			return ResponseFormat.retParam(200, 200, resultMap);
		}
		return ResponseFormat.retParam(0, 50006, null);
	}

	/**
	 * 本地图片上传-删除
	 * @param url
	 */
	@Override
	public void deletePicture(String url) {
		String imgUrl = url.substring(url.indexOf("imgs") - 1);
		String property = System.getProperty("user.dir");
		String path = property + "\\upload" + imgUrl.replaceAll("/", "\\\\");
		File directory = new File(path);
		if (directory.exists() && directory.isFile()) {
			log.info("图片上传-删除成功");
			directory.delete();
		}
	}
}
