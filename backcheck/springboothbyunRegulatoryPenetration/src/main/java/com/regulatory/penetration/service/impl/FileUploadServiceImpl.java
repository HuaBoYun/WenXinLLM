package com.regulatory.penetration.service.impl;

import cn.hutool.core.util.StrUtil;
import com.regulatory.penetration.config.DateBaseConfig;
import com.regulatory.penetration.exception.ServiceException;
import com.regulatory.penetration.oracle.entity.TblAttachment;
import com.regulatory.penetration.oracle.mapper.TblAttachmentMapper;
import com.regulatory.penetration.service.FileUploadService;
import com.regulatory.penetration.util.SnowflakeIdWorker;
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
			TblAttachment model = new TblAttachment();
			for (MultipartFile multipartFile : file) {
				try {
					String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
					String name = fileName.substring(fileName.lastIndexOf("."), fileName.length());

					InputStream inputStream = multipartFile.getInputStream();
					long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称

					attPath = FtpUtil.uploadFilePath(imageName + name + "", inputStream);
					if (StrUtil.isEmpty(attPath)) {
						return new JsonBean(500, "文件上传失败！", null);
					}
					model.setAttid(RandomUtil.uuLongId());
					model.setAttpath(attPath + imageName + name);
					model.setAttsize((double) (multipartFile.getSize() / 1024));
					model.setUploadtime(new Date());
					model.setUploader(realname);
					model.setAttname(fileName);
					tblAttachmentMapper.insertSelective(model);
					TblAttachment tblAttachment = tblAttachmentMapper.selectByPrimaryKey(model.getAttid());
					fileIds.add(tblAttachment);
				} catch (Exception e) {
					log.error("文件上传", e);
					throw new ServiceException(400, "上传失败");
				}
			}
			map.put("fileIds", fileIds);
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
	public JsonBean fileDownLoad(HttpServletResponse response, Long fileId) {
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblAttachment tblAttachmentModel = tblAttachmentMapper.selectByPrimaryKey(fileId);
			if (tblAttachmentModel == null) {
				throw new ServiceException(400, "附件不存在或已经删除");
			}
			com.hbfk.entity.TblAttachment tblAttachment = new com.hbfk.entity.TblAttachment();
			tblAttachment.setAttname(tblAttachmentModel.getAttname());
			tblAttachment.setAttpath(tblAttachmentModel.getAttpath());
			tblAttachment.setAttname(tblAttachmentModel.getAttname());
			tblAttachment.setAttsize(tblAttachmentModel.getAttsize());
			FtpUtil.downUploadFileNew(tblAttachment, response);
		}
		return ResponseFormat.retParam(200, 200, null);
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
			log.error("附件不存在或已经删除");
			return ResponseFormat.retParam(200, 200, null);
		}
		//文件删除
		try {
			FtpUtil.removeFile(tblAttachment.getAttpath());
		} catch (Exception e) {
			log.error("删除文件异常：", e);
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
		if (byId != null && StringUtils.isNotBlank(byId.getAttpath())) {
			fileName = byId.getAttname();
			if (fileName == null || "".equals(fileName)) {
				return ResponseFormat.retParam(0, 50006, null);
			}
			ftpUrl += byId.getAttpath();
			resultMap.put("ftpUrl", "ftp://" + ftpUrl);
			resultMap.put("previewurl", FtpUtil.previewurl);
			resultMap.put("param", "url");
			return ResponseFormat.retParam(200, 200, resultMap);
		}
		return ResponseFormat.retParam(0, 50006, null);
	}
}
