package com.huabo.system.service.impl;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eetrust.label.LabelOperator;
import org.springframework.beans.factory.annotation.Autowired;
//import com.huabo.file.vo.FileUploadRes;
import com.huabo.system.oracle.vo.ByteMultipartFile;
import com.huabo.system.oracle.vo.FileUploadRes;
import com.huabo.system.util.FileDecryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hbfk.entity.TblAttachment;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.mapper.TblAttachmentMapper;
import com.huabo.system.mapper.TblYmFlowRecordAttMapper;
import com.huabo.system.service.TblAttachmentService;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Service
public class TblAttachmentServiceImpl implements TblAttachmentService {
	
	@Value("${file.upload.path}")
    private String filePath;
    @Value("${file.ca.secret}")
    private String caSecret;
    @Value("${file.previewUrl}")
    private String previewUrl;
    @Value("${file.downloadUrl}")
    private String downloadUrl;
	@Value("${fileUploadPath}")
	private String fileUploadPath;

    @Resource
    private TblAttachmentMapper tblAttachmentMapper;

    @Resource
	private TblYmFlowRecordAttMapper tblYmFlowRecordAttMapper;
    
    @Resource
    private UserProvider userProvider;

	@Resource
	TblAttachmentService tblAttachmentService;

	@Autowired(required = false)
	private LabelOperator labelOperator;


	//这些密钥需要与前端的密钥保持一致
	private static final String AES_KEY = "b8e9a1c7d4f265a830e7b1f4d8a9c6e2";
	private static final String AES_IV = "a1b2c3d4e5f6g7h8";

	private final FileDecryptor fileDecryptor = new FileDecryptor(AES_KEY, AES_IV);

    @Override
    public TblAttachment selectAtt(BigDecimal attid) {
    	return tblAttachmentMapper.selectAtt(attid);
    }

    @Override
    public List<TblAttachment> findAllByTblNBSJSheet(String id) {
         return tblAttachmentMapper.findAllByTblNBSJSheet(id);
    }

//	@Override
//	public JsonBean getPrivewAttInfo(String token, BigDecimal attId, Integer attType) throws Exception {
//		/*TblStaffUtil loginStaff = userProvider.get();
//		if (loginStaff == null) {
//			return ResponseFormat.retParam(0, 20006, null);
//		}*/
//		Map<String, Object> resultMap = new HashMap<String, Object>(0);
//		//1.获取FTP服务器 IP 和 端口；
//		String ftpUrl = FtpUtil.Ftpip+":"+FtpUtil.Port;
//		String fileName = null;
//		switch (attType) {
//			/**
//			 *  1. 签署附件
//			 *  3. 其他类型
//			 *  4. 合同附件
//			 *  5. 考试附件
//			 */
//		case 1:
//			//获取合同签署文件
//			ftpUrl += FtpUtil.constractfilepath;
//			fileName = this.tblAttachmentMapper.selectSignFileNameById(attId);
//			break;
//		case 3:
//			//审批流程附件
//			fileName = this.tblYmFlowRecordAttMapper.selectSignFileNameById(attId);
//			break;
//		case 4:
//			//合同文本
//			ftpUrl += FtpUtil.constractfilepath;
//			fileName = this.tblAttachmentMapper.findBycontentPdfId(attId);
//			break;
//		case 5:
//			//审核合同文本
//			ftpUrl += FtpUtil.constractfilepath;
//			fileName = this.tblAttachmentMapper.findBycontentExamId(attId);
//			break;
//			//其他附件类型，将ftpUrl拼接上FtpUtil类中定义的Uploadfilepath属性，
//			// 然后通过tblAttachmentMapper的selectUploadFileNameById方法查询附件的文件名。
//		default:
//			//正常上传文件
//			 com.huabo.system.entity.TblAttachment attachment = tblAttachmentMapper.selectEntityByIdalal(attId.toString());
//			 resultMap.put("zxtoken",attachment.getJmurl());
//			ftpUrl += FtpUtil.Uploadfilepath;
//			fileName = this.tblAttachmentMapper.selectUploadFileNameById(attId);
//			break;
//		}
//		//如果fileName为空，则返回错误提示
//		if(fileName == null || "".equals(fileName)) {
//			//未找到FTP文件
//			return ResponseFormat.retParam(0, 50006, null);
//		}
//		//拼接附件ftp地址
//		ftpUrl += fileName;	
//		
//		//返回 ftpUrl：附件ftp地址，previewurl:预览地址，param:预览传入参数名称
//		resultMap.put("ftpUrl", "ftp://"+ftpUrl);
//		resultMap.put("previewurl", FtpUtil.previewurl);
//		resultMap.put("param","url");
//		return ResponseFormat.retParam(1, 200, resultMap);
//	}
	
	
	@Override
	public JsonBean getPrivewAttInfo(String token, BigDecimal attId, Integer attType) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		//1.获取FTP服务器 IP 和 端口；
		String ftpUrl = FtpUtil.Ftpip+":"+FtpUtil.Port;
		String fileName = null;
		com.huabo.system.entity.TblAttachment attachment = new com.huabo.system.entity.TblAttachment();
		switch (attType) {
			/**
			 *  1. 签署附件
			 *  3. 其他类型
			 *  4. 合同附件
			 *  5. 考试附件
			 */
		case 1:
			//获取合同签署文件
			ftpUrl += FtpUtil.constractfilepath;
			fileName = this.tblAttachmentMapper.selectSignFileNameById(attId);
			break;
		case 3:
			//审批流程附件
			String name = tblYmFlowRecordAttMapper.selectEntityByIdalal(attId);
			attachment.setAttname(name);
			fileName = this.tblYmFlowRecordAttMapper.selectSignFileNameById(attId);
			String jmurl = this.tblYmFlowRecordAttMapper.selectJmurlFileNameById(attId);
			resultMap.put("zxtoken",jmurl);
			break;
		case 4:
			//合同文本
			ftpUrl += FtpUtil.constractfilepath;
			fileName = this.tblAttachmentMapper.findBycontentPdfId(attId);
			break;
		case 5:
			//审核合同文本
			ftpUrl += FtpUtil.constractfilepath;
			fileName = this.tblAttachmentMapper.findBycontentExamId(attId);
			break;
			//其他附件类型，将ftpUrl拼接上FtpUtil类中定义的Uploadfilepath属性，
			// 然后通过tblAttachmentMapper的selectUploadFileNameById方法查询附件的文件名。
		default:
			//正常上传文件
			 attachment = tblAttachmentMapper.selectEntityByIdalal(attId.toString());
//			 resultMap.put("zxtoken",attachment.getJmurl());
			attachment.setMemo("上传的文件");
			ftpUrl += FtpUtil.Uploadfilepath;
			fileName = this.tblAttachmentMapper.selectUploadFileNameById(attId);
			break;
		}
		//如果fileName为空，则返回错误提示
		if(fileName == null || "".equals(fileName)) {
			//未找到FTP文件
			return ResponseFormat.retParam(0, 50006, null);
		}
		//拼接附件ftp地址
		ftpUrl += fileName;

		//根据文件地址获取字节流
		byte[] encryptedFileBytes  = FtpUtil.readfile(fileName);
		//获取扩展名 
		String conteType = Files.probeContentType(Paths.get(attachment.getAttname()));
		//根据字节流生成对应的MultipartFile对象
		MultipartFile multipartFile = new ByteMultipartFile(encryptedFileBytes, "file", attachment.getAttname(), "application/octet-stream");
		BigDecimal attIds = null;
		attIds = tblAttachmentMapper.selectattachmentLevel("公开");
		//文件加密

		//文件流解密
		InputStream inputStream1 = fileDecryptor.decryptMultipartFile(multipartFile);
		encryptedFileBytes = inputStreamToByteArray(inputStream1);
		multipartFile = new ByteMultipartFile(encryptedFileBytes, "file", attachment.getAttname(), "application/octet-stream");
		//解密文件上传
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// 将 MultipartFile 内容写入到 outputStream
		try (InputStream inputStream = multipartFile.getInputStream()) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}
		// 现在您可以使用 outputStream.toByteArray() 获取字节数组
		byte[] bytes = outputStream.toByteArray();
		//文件上传
		String result = this.uploadFileWithMultipart(bytes, attachment.getAttname(), fileUploadPath,token);
		com.huabo.system.entity.TblAttachment newfiles = new com.huabo.system.entity.TblAttachment();
		JSONObject reJson = JSONObject.parseObject(result);
		String code = reJson.getString("code");
		if("200".equals(code)) {
			JSONArray attJsonArray = reJson.getJSONArray("data");
			JSONObject attJsonObj = attJsonArray.getJSONObject(0);
			newfiles.setAttid(attJsonObj.getBigDecimal("attid"));
			newfiles.setAttname(attJsonObj.getString("attname"));
			newfiles.setAttpath(attJsonObj.getString("attpath"));
			newfiles.setAttsize(BigDecimal.valueOf(attJsonObj.getDoubleValue("attsize")));
			newfiles.setUploader(attJsonObj.getString("uploader"));
			newfiles.setUploadtime(com.hbfk.util.DateUtil.formatDate(attJsonObj.getString("uploadTime"), com.hbfk.util.DateUtil.DATE_FULL_STR));
			newfiles.setJmurl(attJsonObj.getString("previewUrl"));
			newfiles.setIspythonflag("1".equals(attJsonObj.getString("isEncrypted"))?"1":"0");
		}else {
			return ResponseFormat.retParam(0, reJson.getString("msg"), null);
		}
		//文件流去除密标
		labelOperator.removeFileLabel("00020592",newfiles.getAttpath(),newfiles.getAttname());
		//文件流加密测试
		//获取去除密标之后的文件字节流 并生成对应的文件进行加密上传
		encryptedFileBytes  = FtpUtil.readfile(newfiles.getAttpath());
		multipartFile = new ByteMultipartFile(encryptedFileBytes, "file", newfiles.getAttname(), "application/octet-stream");
		InputStream inputStream = fileDecryptor.encryptMultipartFile(multipartFile);
		encryptedFileBytes = inputStreamToByteArray(inputStream);
		multipartFile = new ByteMultipartFile(encryptedFileBytes, "file", newfiles.getAttname(), "application/octet-stream");
		//将最终的临时文件进行上传,方便预览
		// 现在您可以使用 outputStream.toByteArray() 获取字节数组

		outputStream = new ByteArrayOutputStream();
		// 将 MultipartFile 内容写入到 outputStream
		try (InputStream inputStreams = multipartFile.getInputStream()) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = inputStreams.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}
		bytes = outputStream.toByteArray();

		//文件上传
		result = this.uploadFileWithMultipart(bytes, attachment.getAttname(), fileUploadPath,token);
		newfiles = new com.huabo.system.entity.TblAttachment();
		reJson = JSONObject.parseObject(result);
		code = reJson.getString("code");
		if("200".equals(code)) {
			JSONArray attJsonArray = reJson.getJSONArray("data");
			JSONObject attJsonObj = attJsonArray.getJSONObject(0);
			newfiles.setAttid(attJsonObj.getBigDecimal("attid"));
			newfiles.setAttname(attJsonObj.getString("attname"));
			newfiles.setAttpath(attJsonObj.getString("attpath"));
			newfiles.setAttsize(BigDecimal.valueOf(attJsonObj.getDoubleValue("attsize")));
			newfiles.setUploader(attJsonObj.getString("uploader"));
			newfiles.setUploadtime(com.hbfk.util.DateUtil.formatDate(attJsonObj.getString("uploadTime"), com.hbfk.util.DateUtil.DATE_FULL_STR));
			newfiles.setJmurl(attJsonObj.getString("previewUrl"));
			newfiles.setIspythonflag("1".equals(attJsonObj.getString("isEncrypted"))?"1":"0");
		}else {
			return ResponseFormat.retParam(0, reJson.getString("msg"), null);
		}
		String jmurl = newfiles.getJmurl();
		String zxtoken = null;
		if (jmurl != null) {
			int startIndex = jmurl.indexOf("url=");
			if (startIndex != -1) {
				zxtoken = jmurl.substring(startIndex + 4); // 提取"url="之后的所有内容
			}
		}
		resultMap.put("zxtoken", zxtoken);
		//返回 ftpUrl：附件ftp地址，previewurl:预览地址，param:预览传入参数名称
		resultMap.put("ftpUrl", "ftp://"+ftpUrl);
		resultMap.put("previewurl", FtpUtil.previewurl);
		resultMap.put("param","url");
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	 /**
     * 提取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名（如 .docx）
     */
    public static String extractExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        // 找到最后一个 '.' 的位置
        int lastDotIndex = fileName.lastIndexOf('.');

        // 如果没有找到 '.', 返回空字符串
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        // 截取从 '.' 到字符串末尾的部分
        return fileName.substring(lastDotIndex);
    }

	/**
	 * 字节数组转换为自定义MultipartFile（无spring-test依赖）
	 * @param bytes 待转换的字节数组
	 * @param fileName 文件名（带扩展名）
	 * @param paramName 表单参数名
	 * @return MultipartFile
	 */
	public MultipartFile bytesToCustomMultipartFile(byte[] bytes, String fileName, String paramName){
		try {
			String contentType = Files.probeContentType(Paths.get(fileName));
			return new ByteMultipartFile(bytes, paramName, fileName, contentType);
		} catch (Exception e) {
			throw new RuntimeException("字节流转MultipartFile失败", e);
		}
	}


	public byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, bytesRead);
			}
			return baos.toByteArray();
		}
	}



	@Override
	public JsonBean getPrivewAttInfoNew(String token, BigDecimal attId, Integer attType) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		//1.获取FTP服务器 IP 和 端口；
		String ftpUrl = FtpUtil.Ftpip+":"+FtpUtil.Port;
		String fileName = null;
		com.huabo.system.entity.TblAttachment attachment = new com.huabo.system.entity.TblAttachment();
		switch (attType) {
			/**
			 *  1. 签署附件
			 *  3. 其他类型
			 *  4. 合同附件
			 *  5. 考试附件
			 */
			case 1:
				//获取合同签署文件
				ftpUrl += FtpUtil.constractfilepath;
				fileName = this.tblAttachmentMapper.selectSignFileNameById(attId);
				break;
			case 3:
				//审批流程附件
				fileName = this.tblYmFlowRecordAttMapper.selectSignFileNameById(attId);
				String jmurl = this.tblYmFlowRecordAttMapper.selectJmurlFileNameById(attId);
				resultMap.put("zxtoken",jmurl);
				break;
			case 4:
				//合同文本
				ftpUrl += FtpUtil.constractfilepath;
				fileName = this.tblAttachmentMapper.findBycontentPdfId(attId);
				break;
			case 5:
				//审核合同文本
				ftpUrl += FtpUtil.constractfilepath;
				fileName = this.tblAttachmentMapper.findBycontentExamId(attId);
				break;
			//其他附件类型，将ftpUrl拼接上FtpUtil类中定义的Uploadfilepath属性，
			// 然后通过tblAttachmentMapper的selectUploadFileNameById方法查询附件的文件名。
			default:
				//正常上传文件
				attachment = tblAttachmentMapper.selectEntityByIdalal(attId.toString());
//			 resultMap.put("zxtoken",attachment.getJmurl());
				ftpUrl += FtpUtil.Uploadfilepath;
				fileName = this.tblAttachmentMapper.selectUploadFileNameById(attId);
				break;
		}
		//如果fileName为空，则返回错误提示
		if(fileName == null || "".equals(fileName)) {
			//未找到FTP文件
			return ResponseFormat.retParam(0, 50006, null);
		}
		//拼接附件ftp地址
		ftpUrl += fileName;

		//根据文件地址获取字节流
		byte[] encryptedFileBytes  = FtpUtil.readfile(fileName);
		//获取扩展名
		String conteType = Files.probeContentType(Paths.get(attachment.getAttname()));
		//根据字节流生成对应的MultipartFile对象
		MultipartFile multipartFile = new ByteMultipartFile(encryptedFileBytes, "file", attachment.getAttname(), "application/octet-stream");
		BigDecimal attIds = null;
		attIds = tblAttachmentMapper.selectattachmentLevel("公开");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		// 将 MultipartFile 内容写入到 outputStream
		try (InputStream inputStream = multipartFile.getInputStream()) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}
		// 现在您可以使用 outputStream.toByteArray() 获取字节数组
		byte[] bytes = outputStream.toByteArray();
		//文件上传
		String result = this.uploadFileWithMultipart(bytes, attachment.getAttname(), fileUploadPath,token);
		com.huabo.system.entity.TblAttachment newfiles = new com.huabo.system.entity.TblAttachment();
		JSONObject reJson = JSONObject.parseObject(result);
		String code = reJson.getString("code");
		if("200".equals(code)) {
			JSONArray attJsonArray = reJson.getJSONArray("data");
			JSONObject attJsonObj = attJsonArray.getJSONObject(0);
			newfiles.setAttid(attJsonObj.getBigDecimal("attid"));
			newfiles.setAttname(attJsonObj.getString("attname"));
			newfiles.setAttpath(attJsonObj.getString("attpath"));
			newfiles.setAttsize(BigDecimal.valueOf(attJsonObj.getDoubleValue("attsize")));
			newfiles.setUploader(attJsonObj.getString("uploader"));
			newfiles.setUploadtime(com.hbfk.util.DateUtil.formatDate(attJsonObj.getString("uploadTime"), com.hbfk.util.DateUtil.DATE_FULL_STR));
			newfiles.setJmurl(attJsonObj.getString("previewUrl"));
			newfiles.setIspythonflag("1".equals(attJsonObj.getString("isEncrypted"))?"1":"0");
		}else {
			return ResponseFormat.retParam(0, reJson.getString("msg"), null);
		}
		//文件地址去除密标
		if (labelOperator != null) {
			labelOperator.removeFileLabel("00020592",newfiles.getAttpath(),newfiles.getAttname());
		}

		System.out.println(attachment);
		tblAttachmentMapper.insert(newfiles);
		resultMap.put("zxtoken",newfiles.getJmurl());
		//返回 ftpUrl：附件ftp地址，previewurl:预览地址，param:预览传入参数名称
		resultMap.put("ftpUrl", "ftp://"+ftpUrl);
		resultMap.put("previewurl", FtpUtil.previewurl);
		resultMap.put("param","url");
		return ResponseFormat.retParam(1, 200, resultMap);
	}


	/**
	 * 使用 multipart/form-data 上传文件
	 */
	public String uploadFileWithMultipart(byte[] fileBytes, String fileName, String targetUrl,String token) {
		try {
			// 创建 ByteArrayResource
			ByteArrayResource resource = new ByteArrayResource(fileBytes) {
				@Override
				public String getFilename() {
					return fileName;
				}
			};

			// 创建 multipart 请求体
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", resource);
			body.add("fileName", fileName);

			// 设置请求头
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("token", token);

			// 创建请求实体
			HttpEntity<MultiValueMap<String, Object>> requestEntity =
					new HttpEntity<>(body, headers);

			// 发送请求
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.postForEntity(
					targetUrl, requestEntity, String.class);

			return response.getBody();

		} catch (Exception e) {
			throw new RuntimeException("文件上传失败", e);
		}
	}
}
