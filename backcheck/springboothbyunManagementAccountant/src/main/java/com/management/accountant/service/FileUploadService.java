package com.management.accountant.service;

import com.hbfk.util.JsonBean;
import org.springframework.web.multipart.MultipartFile;

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
	JsonBean fileDownLoad(HttpServletResponse response, Long fileId);

	/**
	 * 文件删除
	 * @param id
	 * @return
	 */
	JsonBean fileRemove(Long id);

	/**
	 * 获取上传的url
	 * @param response
	 * @param parseInt
	 */
	JsonBean getPrivewAttInfo(HttpServletResponse response, Long parseInt);
}
