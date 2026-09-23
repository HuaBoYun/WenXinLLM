package com.huabo.system.service.business;

import com.hbfk.util.JsonBean;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;

public interface FileUploadService {

	/**
	 * 文件上传
	 * @param file
	 * @param realname
	 * @return
	 */
	JsonBean fileUpload(MultipartFile[] file, String realname);

	/**
	 * 下载文件
	 * @param response
	 * @param fileId
	 * @return
	 */
	JsonBean fileDownLoad(HttpServletResponse response, String fileId);

	/**
	 * 文件删除
	 * @param id
	 * @return
	 */
	JsonBean fileRemove(BigDecimal id);

	/**
	 * 获取上传的url
	 * @param response
	 * @param fileId
	 */
	JsonBean getPrivewAttInfo(HttpServletResponse response, String fileId);

	/**
	 * 本地图片上传-删除
	 * @param url
	 */
	void deletePicture(String url);
}
