package com.management.accountant.service.impl;

import cn.hutool.core.util.StrUtil;
import com.management.accountant.config.DateBaseConfig;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.TblAttachment;
import com.management.accountant.oracle.mapper.TblAttachmentMapper;
import com.management.accountant.service.FileUploadService;
import com.management.accountant.util.SnowflakeIdWorker;
// import com.hbfk.util.FtpUtil; // 临时注释,FTP功能暂时禁用
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
// import com.hbfk.util.redis.Random.RandomUtil; // 临时注释
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
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


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

					// 临时禁用FTP上传功能
					// attPath = FtpUtil.uploadFilePath(imageName + name + "", inputStream);
					attPath = "/tmp/uploads/"; // 临时使用本地路径
					if (StrUtil.isEmpty(attPath)) {
						return new JsonBean(500, "文件上传失败！", null);
					}
					model.setAttid(System.currentTimeMillis()); // 使用时间戳替代RandomUtil
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
		// TODO: FTP功能暂时禁用,需要修复FtpUtil依赖后恢复
		if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
			TblAttachment tblAttachmentModel = tblAttachmentMapper.selectByPrimaryKey(fileId);
			if (tblAttachmentModel == null) {
				throw new ServiceException(400, "附件不存在或已经删除");
			}
			// FTP下载功能暂时禁用
			// FtpUtil.downUploadFileNew(tblAttachment, response);
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
		//文件删除 - FTP功能暂时禁用
		// try {
		// 	FtpUtil.removeFile(tblAttachment.getAttpath());
		// } catch (Exception e) {
		// 	log.error("删除文件异常：", e);
		// }
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
		// TODO: FTP功能暂时禁用
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		// String ftpUrl = FtpUtil.Ftpip;
		String ftpUrl = "ftp://localhost"; // 临时占位
		String fileName = null;
		TblAttachment byId = tblAttachmentMapper.selectByPrimaryKey(parseInt);
		if (byId != null && StringUtils.isNotBlank(byId.getAttpath())) {
			fileName = byId.getAttname();
			if (fileName == null || "".equals(fileName)) {
				return ResponseFormat.retParam(0, 50006, null);
			}
			ftpUrl += byId.getAttpath();
			resultMap.put("ftpUrl", ftpUrl);
			resultMap.put("previewurl", "/preview"); // 临时占位
			resultMap.put("param", "url");
			return ResponseFormat.retParam(200, 200, resultMap);
		}
		return ResponseFormat.retParam(0, 50006, null);
	}
}
