package com.financial.sharing.service.impl;

import cn.hutool.core.util.StrUtil;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.oracle.entity.TblAttachment;
import com.financial.sharing.oracle.mapper.TblAttachmentMapper;
import com.financial.sharing.service.FileUploadService;
import com.financial.sharing.util.SnowflakeIdWorker;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;

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
	private TblAttachmentMapper tblAttachmentMapper;

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
			List<TblAttachment> fileIds = new ArrayList<>();
			for (MultipartFile multipartFile : file) {
				try {
					String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
					String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());

					InputStream inputStream = multipartFile.getInputStream();
					Long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称

					attPath = FtpUtil.uploadFilePath(imageName.toString() + name, inputStream);
					if (StrUtil.isEmpty(attPath)) {
						return new JsonBean(500, "文件上传失败！", null);
					}

					TblAttachment model = new TblAttachment();
					Long attId = RandomUtil.uuLongId();
					setFieldValue(model, "attid", attId);
					setFieldValue(model, "attpath", attPath + imageName + name);
					setFieldValue(model, "attsize", (double) (multipartFile.getSize() / 1024));
					setFieldValue(model, "uploadtime", new Date());
					setFieldValue(model, "uploader", realname);
					setFieldValue(model, "attname", fileName);

					tblAttachmentMapper.insertSelective(model);
					TblAttachment tblAttachment = tblAttachmentMapper.selectByPrimaryKey(attId);
					fileIds.add(tblAttachment);
				} catch (Exception e) {
					throw new ServiceException(400, "上传失败");
				}
			}
			map.put("fileIds", fileIds);
			return ResponseFormat.retParam(200, 200, map);
		}
		return ResponseFormat.retParam(200, 200, map);
	}

	private void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
		java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, value);
	}

	/**
	 * 下载文件
	 * @param response
	 * @param fileId
	 * @return
	 */
	@Override
	public JsonBean fileDownLoad(HttpServletResponse response, Long fileId) {
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblAttachment tblAttachmentModel = tblAttachmentMapper.selectByPrimaryKey(fileId);
			if (tblAttachmentModel == null) {
				throw new ServiceException(400, "附件不存在或已经删除");
			}
			com.hbfk.entity.TblAttachment tblAttachment = new com.hbfk.entity.TblAttachment();
			try {
				tblAttachment.setAttname((String) getFieldValue(tblAttachmentModel, "attname"));
				tblAttachment.setAttpath((String) getFieldValue(tblAttachmentModel, "attpath"));
				tblAttachment.setAttsize((double) getFieldValue(tblAttachmentModel, "attsize"));
			} catch (Exception e) {
				throw new ServiceException(400, "获取附件信息失败");
			}
			FtpUtil.downUploadFileNew(tblAttachment, response);
			return ResponseFormat.retParam(200, 200, null);
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	private Object getFieldValue(Object obj, String fieldName) throws Exception {
		java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(obj);
	}

	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	@Override
	public JsonBean fileRemove(Long id) {
		TblAttachment tblAttachment = tblAttachmentMapper.selectByPrimaryKey(id);
		if (tblAttachment == null) {
			return ResponseFormat.retParam(200, 200, null);
		}
		//文件删除
		try {
			String attpath = (String) getFieldValue(tblAttachment, "attpath");
			FtpUtil.removeFile(attpath);
		} catch (Exception e) {
			// 忽略异常
		}
		tblAttachmentMapper.deleteByPrimaryKey(id);
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
		TblAttachment byId = tblAttachmentMapper.selectByPrimaryKey(parseInt);
		if (byId != null) {
			try {
				String attpath = (String) getFieldValue(byId, "attpath");
				String attname = (String) getFieldValue(byId, "attname");
				if (StringUtils.isNotBlank(attpath)) {
					fileName = attname;
					if (fileName == null || "".equals(fileName)) {
						return ResponseFormat.retParam(0, 50006, null);
					}
					ftpUrl += attpath;
					resultMap.put("ftpUrl", "ftp://" + ftpUrl);
					resultMap.put("previewurl", FtpUtil.previewurl);
					resultMap.put("param", "url");
					return ResponseFormat.retParam(200, 200, resultMap);
				}
			} catch (Exception e) {
				return ResponseFormat.retParam(0, 50006, null);
			}
		}
		return ResponseFormat.retParam(0, 50006, null);
	}
}
