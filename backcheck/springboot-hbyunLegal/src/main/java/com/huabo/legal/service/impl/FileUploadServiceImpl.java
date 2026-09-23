package com.huabo.legal.service.impl;

import cn.hutool.core.util.StrUtil;
import com.hbfk.entity.TblAttachment;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglFileOracle;
import com.huabo.legal.oracle.service.TblFwglFileOracleService;
import com.huabo.legal.service.FileUploadService;
import com.huabo.legal.util.SnowflakeIdWorker;
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
	private TblFwglFileOracleService tblFwglFileOracleService;

	/**
	 * 文件上传
	 * @param file
	 * @param realname
	 */
	@Override
	public JsonBean fileUpload(MultipartFile[] file, String realname) {
		HashMap<String, Object> map = new HashMap<>();
		String attPath = "";
		List<TblFwglFileOracle> fileIds = new ArrayList<>();
		TblFwglFileOracle tblFwglFileOracleModel = new TblFwglFileOracle();
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
				tblFwglFileOracleModel.setFileId(RandomUtil.uuLongId());
				tblFwglFileOracleModel.setFilePath(imageName + name);
				tblFwglFileOracleModel.setFileSize((double) (multipartFile.getSize() / 1024));
				tblFwglFileOracleModel.setUploadTime(new Date());
				tblFwglFileOracleModel.setUploader(realname);
				tblFwglFileOracleModel.setFileName(fileName);
				TblFwglFileOracle tblFwglFileMySql = tblFwglFileOracleService.saveOrUpdate(tblFwglFileOracleModel);
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
	public JsonBean fileDownLoad(HttpServletResponse response, Long fileId) {
		TblFwglFileOracle tblFwglFileOracle = tblFwglFileOracleService.findById(fileId);
		if (tblFwglFileOracle == null) {
			throw new ServiceException(400, "附件不存在或已经删除");
		}
		TblAttachment tblAttachment = new TblAttachment();
		tblAttachment.setAttname(tblFwglFileOracle.getFileName());
		tblAttachment.setAttpath(tblFwglFileOracle.getFilePath());
		tblAttachment.setFileName(tblFwglFileOracle.getFileName());
		tblAttachment.setAttsize(tblFwglFileOracle.getFileSize());
		FtpUtil.downUploadFileNew(tblAttachment, response);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean fileRemove(Long id) {
		TblFwglFileOracle tblFwglFileOracle = tblFwglFileOracleService.findById(id);
		if (tblFwglFileOracle == null) {
			throw new ServiceException(400, "附件不存在或已经删除");
		}
		//文件删除
		try {
			FtpUtil.removeFile(tblFwglFileOracle.getFilePath());
		} catch (Exception e) {
			log.error("删除文件异常：", e);
		}
		tblFwglFileOracleService.delete(id);
		return ResponseFormat.retParam(200, 200, null);
	}

	/**
	 * 获取上传的url
	 * @param response
	 * @param parseInt
	 */
	@Override
	public JsonBean getPrivewAttInfo(HttpServletResponse response, Long parseInt) {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		String ftpUrl = FtpUtil.Ftpip;
		String fileName = null;
		TblFwglFileOracle byId = tblFwglFileOracleService.findById(parseInt);
		if (byId != null && StringUtils.isNotBlank(byId.getFilePath())) {
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
}
