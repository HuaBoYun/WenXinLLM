package com.huabo.compliance.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hbfk.entity.TblAttachment;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.compliance.config.DateBaseConfig;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceFileOracle;
import com.huabo.compliance.oracle.service.TblComplianceFileOracleService;
import com.huabo.compliance.service.FileUploadService;
import com.huabo.compliance.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

	private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);

	@Resource
	private TblComplianceFileOracleService tblComplianceFileOracleService;

	/**
	 * 文件上传
	 * @param file
	 * @param realname
	 */
	@Override
	public JsonBean fileUpload(MultipartFile[] file, String realname) {
		HashMap<String, Object> map = new HashMap<>();
		String attPath = "";
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			List<TblComplianceFileOracle> fileIds = new ArrayList<>();
			TblComplianceFileOracle tblComplianceFileOracleModel = new TblComplianceFileOracle();
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
					tblComplianceFileOracleModel.setFilePath(attPath + imageName + name);
					tblComplianceFileOracleModel.setFileSize((double) (multipartFile.getSize() / 1024));
					tblComplianceFileOracleModel.setUploadTime(new Date());
					tblComplianceFileOracleModel.setUploader(realname);
					tblComplianceFileOracleModel.setFileName(fileName);
					TblComplianceFileOracle tblComplianceFileMySql = tblComplianceFileOracleService.saveOrUpdate(tblComplianceFileOracleModel);
					fileIds.add(tblComplianceFileMySql);
				} catch (Exception e) {
					log.error("文件上传", e);
					throw new ServiceException(400, "上传失败");
				}
			}
			map.put("fileIds", fileIds);
		} else {

		}
		return ResponseFormat.retParam(200, 200, map);
	}

	/**
	 * 下载文件
	 * @param response
	 * @param fileId
	 * @return
	 */
	@Override
	public JsonBean fileDownLoad(HttpServletResponse response, Integer fileId) {
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceFileOracle tblComplianceFileOracle = tblComplianceFileOracleService.findById(fileId);
			if (tblComplianceFileOracle == null) {
				throw new ServiceException(400, "附件不存在或已经删除");
			}
			TblAttachment tblAttachment = new TblAttachment();
			tblAttachment.setAttname(tblComplianceFileOracle.getFileName());
			tblAttachment.setAttpath(tblComplianceFileOracle.getFilePath());
			tblAttachment.setFileName(tblComplianceFileOracle.getFileName());
			tblAttachment.setAttsize(tblComplianceFileOracle.getFileSize());
			FtpUtil.downUploadFileNew(tblAttachment, response);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean fileRemove(Integer id) {
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblComplianceFileOracle tblComplianceFileOracle = tblComplianceFileOracleService.findById(id);
			if (tblComplianceFileOracle == null) {
				throw new ServiceException(400, "附件不存在或已经删除");
			}
			//文件删除
			try {
				FtpUtil.removeFile(tblComplianceFileOracle.getFilePath());
			} catch (Exception e) {
				log.error("删除文件异常：", e);
			}
			tblComplianceFileOracleService.delete(id);
		} else {

		}
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 获取上传的url
	 * @param response
	 * @param parseInt
	 */
	@Override
	public JsonBean getPrivewAttInfo(HttpServletResponse response, int parseInt) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		String ftpUrl = FtpUtil.Ftpip;
		String fileName = null;
		TblComplianceFileOracle byId = tblComplianceFileOracleService.findById(parseInt);
		if (byId != null && StringUtils.isNotBlank(byId.getFilePath())) {
			//ftpUrl += FtpUtil.Uploadfilepath;
			fileName = byId.getFileName();
			if (fileName == null || "".equals(fileName)) {
				return ResponseFormat.retParam(0, 50006, null);
			}
			ftpUrl += byId.getFilePath();
			resultMap.put("ftpUrl", "ftp://" + ftpUrl);
			resultMap.put("previewurl", FtpUtil.previewurl);
			resultMap.put("param", "url");
			return ResponseFormat.retParam(200, 200, resultMap);
		}
		return ResponseFormat.retParam(0, 50006, null);
	}
}
