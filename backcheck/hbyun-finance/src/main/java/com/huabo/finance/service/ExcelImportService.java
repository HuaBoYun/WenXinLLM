package com.huabo.finance.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;

/**
 * <p>
 * excel 导入
 * </p>
 *
 * @author L
 * @since 2025-06-24
 */
public interface ExcelImportService {

	JsonBean importTemplateData(String tableId, MultipartFile file, HttpServletRequest request) throws Exception;

}
