package com.huabo.file.db.service;


import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
// import com.eetrust.label.LabelOperator;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.ResponseFormat;
import com.huabo.file.util.FileDecryptor;
import com.huabo.file.vo.ByteMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.file.config.TokenInterceptor;
import com.huabo.file.db.entity.TblAttachment;
import com.huabo.file.db.entity.TblYmFlowRecordAtt;
import com.huabo.file.db.mapper.TblAttachmentMapper;
import com.huabo.file.db.mapper.TblYmFlowRecordAttMapper;
import com.huabo.file.exception.FileException;
import com.huabo.file.util.AESUtil;
import com.huabo.file.util.ErrorCodeEnum;
import com.huabo.file.vo.FileUploadRes;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TblAttachmentService extends ServiceImpl<TblAttachmentMapper, TblAttachment> {

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
    
    @Autowired
    private TblAttachmentMapper tblAttachmentMapper;
    
    @Autowired
    private TblYmFlowRecordAttMapper  tblYmFlowRecordAttMapper;

    // @Resource
    // LabelOperator labelOperator;



    //这些密钥需要与前端的密钥保持一致
    private static final String AES_KEY = "b8e9a1c7d4f265a830e7b1f4d8a9c6e2";
    private static final String AES_IV = "a1b2c3d4e5f6g7h8";

    private final FileDecryptor fileDecryptor = new FileDecryptor(AES_KEY, AES_IV);


    /**
     * 文件上传
     */
    public List<FileUploadRes> fileUpload(MultipartFile[] file, Boolean isCa
            ,@Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel,BigDecimal attachmentLevelId,InputStream inputStream,String toke) {
        List<FileUploadRes> fileUploads = new ArrayList<>();
        if(inputStream==null){
            System.out.println("fileUpload这里的inputstream就是空的");
        }
        String fileFullPath;
        // 获取用户信息
        TblStaffUtil tblStaffUtil = TokenInterceptor.getTblStaffUtil();
        for (MultipartFile multipartFile : file) {
            if (file == null) {
                throw new IllegalArgumentException("上传文件不能为空");
            }
            long id = RandomUtil.uuLongId();
            TblAttachment fileUpload = new TblAttachment();
            fileUpload.setAttid(id);


            //判断是否启用附件密级
            if(null!=attachmentLevelId) {
                fileUpload.setAttachmentlevel(attachmentLevelId);
            }

            try {
                String fileName = new String(Objects.requireNonNull(multipartFile.getOriginalFilename()).getBytes()).replace(".enc","");
                if (filePath == null){
                    fileFullPath = "/opt/" + DateUtil.today() + "/" + id;
                }else {
                    fileFullPath = filePath + "/" + DateUtil.today() + "/" + id;
                }

                File dest = new File(fileFullPath);
                if (!dest.getParentFile().exists()) {
                    if (!dest.getParentFile().mkdirs()) {
                        log.info("创建目录:[{}]失败", fileFullPath);
                        throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
                    }
                }
                String fileId = String.valueOf(id);
                String encodedPart = Base64Utils.encodeToString((downloadUrl + fileId + "&fullfilename=" + fileId+extractExtension(fileName)).getBytes(StandardCharsets.UTF_8));
                fileUpload.setJmurl(encodedPart);
                saveFileLocal(isCa, multipartFile, dest);

                fileUpload.setAttname(fileName);
                fileUpload.setAttpath(fileFullPath);
                double size = (double) multipartFile.getSize() / 1024;
                // 明确单位为 KiB（二进制千字节）
                fileUpload.setAttsize(size);
                fileUpload.setUploadtime(new Date());
                String realName = "星光";
                if(tblStaffUtil!=null){
                    realName = tblStaffUtil.getRealname();
                }

                fileUpload.setUploader(realName);
                if (tblStaffUtil == null){
                    fileUploads.add(new FileUploadRes(fileId, fileName, fileFullPath, size, realName,
                            DateUtil.format(fileUpload.getUploadtime(), "yyyy-MM-dd HH:mm:ss"),
                            fileUpload.getJmurl(), "1",encodedPart)
                    );
                }else {
                    this.save(fileUpload);
                    fileUploads.add(new FileUploadRes(fileId, fileName, fileFullPath, size, realName,
                            DateUtil.format(fileUpload.getUploadtime(), "yyyy-MM-dd HH:mm:ss"),
                            getPreviewUrlNew(multipartFile, fileName, toke), "1",encodedPart)
                    );
                }
            } catch (Exception e) {
                log.error("文件上传失败", e);
                throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
            }
        }
        return fileUploads;
    }


    /**
     * 文件预览时的上传
     */
    public List<FileUploadRes> fileUploadYuLan(MultipartFile[] file, Boolean isCa
    		,@Parameter(name="formlevel",description="表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel,BigDecimal attachmentLevelId) {
    	 List<FileUploadRes> fileUploads = new ArrayList<>();
         String fileFullPath;
         // 获取用户信息
         TblStaffUtil tblStaffUtil = TokenInterceptor.getTblStaffUtil();
         for (MultipartFile multipartFile : file) {
             long id = RandomUtil.uuLongId();
             TblAttachment fileUpload = new TblAttachment();
             fileUpload.setAttid(id);
             
             
             //判断是否启用附件密级
             if(null!=attachmentLevelId) {
             	fileUpload.setAttachmentlevel(attachmentLevelId);
             }
             
             try {
                 String fileName = new String(Objects.requireNonNull(multipartFile.getOriginalFilename()).getBytes()).replace(".enc","");
                 fileFullPath = filePath + "/" + DateUtil.today() + "/" + id;
                 File dest = new File(fileFullPath);
                 if (!dest.getParentFile().exists()) {
                     if (!dest.getParentFile().mkdirs()) {
                         log.info("创建目录:[{}]失败", fileFullPath);
                         throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
                     }
                 }
                 String fileId = String.valueOf(id);
                 String encodedPart = Base64Utils.encodeToString((downloadUrl + fileId + "&fullfilename=" + fileId+extractExtension(fileName)).getBytes(StandardCharsets.UTF_8));
                 fileUpload.setJmurl(encodedPart);
                 saveFileLocal(isCa, multipartFile, dest);

                 fileUpload.setAttname(fileName);
                 fileUpload.setAttpath(fileFullPath);
                 double size = (double) multipartFile.getSize() / 1024;
                 // 明确单位为 KiB（二进制千字节）
                 fileUpload.setAttsize(size);
                 fileUpload.setUploadtime(new Date());
                 String realName = tblStaffUtil.getRealname();
                 fileUpload.setUploader(realName);
                 this.save(fileUpload);
                 
                 fileUploads.add(new FileUploadRes(fileId, fileName, fileFullPath, size, realName,
                         DateUtil.format(fileUpload.getUploadtime(), "yyyy-MM-dd HH:mm:ss"),
                         getPreviewUrl(fileId, fileName), "1",encodedPart)
                 ); 
             } catch (Exception e) {
                 log.error("文件上传失败", e);
                 throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
             }
         }
         return fileUploads;
    }

    private void saveFileLocal(Boolean isCa, MultipartFile multipartFile, File dest) {
        if (isCa) {
            try (InputStream inputStream = multipartFile.getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                // 读取输入流到字节数组
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                // 获取字节数组
                byte[] fileBytes = byteArrayOutputStream.toByteArray();
                // 加密字节数组
                String encrypt = AESUtil.encrypt(fileBytes, caSecret);
                FileUtil.writeString(encrypt, dest, StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("文件加密保存失败", e);
                throw new FileException(ErrorCodeEnum.FILE_CA_SAVE_FAIL);
            }
            return;
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            FileUtil.writeFromStream(inputStream, dest);
        } catch (Exception e) {
            log.error("文件上传失败(写入本地目录:[{}]失败)", dest.getPath(), e);
            throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
        }
    }

    public void fileDownLoad(HttpServletResponse response, String fileId, Boolean isCa) {
    	TblYmFlowRecordAtt recordAtt = tblYmFlowRecordAttMapper.selectEntityById(fileId);
    	if(recordAtt!=null) {
    		File fileBase = new File(recordAtt.getAttpath());
            if (!fileBase.exists()) {
                throw new FileException(ErrorCodeEnum.FILE_NOT_EXIST_LOCAL);
            }
            String fileName = recordAtt.getAttname();
            String encodedFileName = fileName;
            try {
                encodedFileName = URLEncoder.encode(fileName, "UTF-8");
                // 替换空格，防止编码后的 "+" 号变成空格
                encodedFileName = encodedFileName.replaceAll("\\+", "%20");
            } catch (Exception e) {
                log.error("文件名字encode失败", e);
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", new StrBuilder("attachment; filename=")
                    .append("\"").append(encodedFileName).append("\"").toString());
            if (isCa) {

                try (BufferedReader reader = new BufferedReader(new FileReader(fileBase));
                     ServletOutputStream outputStream = response.getOutputStream()) {

                    String base64Line;

                    while ((base64Line = reader.readLine()) != null) {
                        // 解密每一行 Base64 数据
                        byte[] decryptedData = AESUtil.decrypt(base64Line, caSecret);
                        // 写入到响应流
                        outputStream.write(decryptedData);
                    }

                    outputStream.flush();
                } catch (Exception e) {
                    log.error("文件下载失败", e);
                    throw new FileException(ErrorCodeEnum.FILE_WRITE_RESP_FAIL);
                }
            }else {
                try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(fileBase.toPath()));
                        ServletOutputStream outputStream = response.getOutputStream()) {

                       // 直接复制字节流，避免逐行读取和编码转换
                       byte[] buffer = new byte[8192];
                       int bytesRead;
                       while ((bytesRead = inputStream.read(buffer)) != -1) {
                           outputStream.write(buffer, 0, bytesRead);
                       }
                       outputStream.flush();
                   } catch (Exception e) {
                       log.error("文件下载失败", e);
                       throw new FileException(ErrorCodeEnum.FILE_WRITE_RESP_FAIL);
                   }
               }
    	}else {
    		TblAttachment file = this.getById(fileId);
            if (file == null) {
                log.error("文件不存在");
                throw new FileException(ErrorCodeEnum.FILE_NOT_EXIST);
            }
            File fileBase = new File(file.getAttpath());
            if (!fileBase.exists()) {
                throw new FileException(ErrorCodeEnum.FILE_NOT_EXIST_LOCAL);
            }
            String fileName = file.getAttname();
            String encodedFileName = fileName;
            try {
                encodedFileName = URLEncoder.encode(fileName, "UTF-8");
                // 替换空格，防止编码后的 "+" 号变成空格
                encodedFileName = encodedFileName.replaceAll("\\+", "%20");
            } catch (Exception e) {
                log.error("文件名字encode失败", e);
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", new StrBuilder("attachment; filename=")
                    .append("\"").append(encodedFileName).append("\"").toString());
            if (isCa && file.getIsEncrypted()) {

                try (BufferedReader reader = new BufferedReader(new FileReader(fileBase));
                     ServletOutputStream outputStream = response.getOutputStream()) {

                    String base64Line;

                    while ((base64Line = reader.readLine()) != null) {
                        // 解密每一行 Base64 数据
                        byte[] decryptedData = AESUtil.decrypt(base64Line, caSecret);
                        // 写入到响应流
                        outputStream.write(decryptedData);
                    }

                    outputStream.flush();
                } catch (Exception e) {
                    log.error("文件下载失败", e);
                    throw new FileException(ErrorCodeEnum.FILE_WRITE_RESP_FAIL);
                }
            } else {
                try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(fileBase.toPath()));
                     ServletOutputStream outputStream = response.getOutputStream()) {

                    // 直接复制字节流，避免逐行读取和编码转换
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.flush();
                } catch (Exception e) {
                    log.error("文件下载失败", e);
                    throw new FileException(ErrorCodeEnum.FILE_WRITE_RESP_FAIL);
                }
            }
    	}

    }


    public void removeFile(Long fileId) {
        TblAttachment file = this.getById(fileId);
        // 删除本地文件
        if (file != null) {
            File fileBase = new File(file.getAttpath());
            if (fileBase.exists()) {
                if (!fileBase.delete()) {
                    log.error("文件删除失败");
                    throw new FileException(ErrorCodeEnum.FILE_DELETE_FAIL_LOCAL);
                }
            }
            this.removeById(fileId);
        }
    }

    public List<FileUploadRes> listFileUpload(List<String> ids) {
        List<FileUploadRes> res = new ArrayList<>();
        if (CollUtil.isEmpty(ids)) {
            return res;
        }
        for (TblAttachment file : this.listByIds(ids)) {
            String fileId = String.valueOf(file.getAttid());
            res.add(new FileUploadRes(fileId, file.getAttname(), file.getAttpath(), file.getAttsize(),
                    file.getUploader(), DateUtil.format(file.getUploadtime(), "yyyy-MM-dd HH:mm:ss"),
                    getPreviewUrl(fileId, file.getAttname()), file.getIsEncrypted() ? "1" : "0",file.getJmurl()));
        }
        return res;

    }

    /**
     * 获取预览地址
     *
     * @param fileId 文件id
     */
    public String getPreviewUrl(String fileId, String fileName) {
        // 空值检查
        if (StrUtil.isEmpty(previewUrl) || StrUtil.isEmpty(downloadUrl) || StrUtil.isEmpty(fileId)) {
            return "";
        }
        // 将 downloadUrl 和 fileId 拼接并编码
        String encodedPart = Base64Utils.encodeToString((downloadUrl + fileId + "&fullfilename=" + fileId+extractExtension(fileName)).getBytes(StandardCharsets.UTF_8));
        
        return previewUrl + encodedPart;
    }

//文件流、文件真实名称、文件地址、token
    public String getPreviewUrlNew(MultipartFile newmultipartFile,String filename,String token) throws Exception {

        if(newmultipartFile == null){
            return "文件上传时文件为空";
        }
        //对文件进行解密
        InputStream inputStream1 = fileDecryptor.decryptMultipartFile(newmultipartFile);
        //通过输入流获取对应的字节流数组并转换为对应的multipartFile文件
        byte[] encryptedFileBytes = inputStreamToByteArray(inputStream1);
        MultipartFile multipartFile = new ByteMultipartFile(encryptedFileBytes, "file", filename, "application/octet-stream");
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
        String result = this.uploadFileWithMultipart(bytes, filename, fileUploadPath,token);
        TblAttachment newfiles = new TblAttachment();
        JSONObject reJson = JSONObject.parseObject(result);
        String code = reJson.getString("code");
        if("200".equals(code)) {
            JSONArray attJsonArray = reJson.getJSONArray("data");
            JSONObject attJsonObj = attJsonArray.getJSONObject(0);
            newfiles.setAttid(attJsonObj.getLong("attid"));
            newfiles.setAttname(attJsonObj.getString("attname"));
            newfiles.setAttpath(attJsonObj.getString("attpath"));
            newfiles.setAttsize(attJsonObj.getDoubleValue("attsize"));
            newfiles.setUploader(attJsonObj.getString("uploader"));
            newfiles.setUploadtime(attJsonObj.getDate("uploadTime"));
            newfiles.setJmurl(attJsonObj.getString("previewUrl"));
            newfiles.setIspythonflag("1".equals(attJsonObj.getString("isEncrypted"))?"1":"0");
        }else {
            return "上传解密之后的文件失败！";
        }
        //文件流去除密标 - 暂时注释，缺少LabelOperator依赖
        // System.out.println("文件流去除密标");
        // System.out.println("文件地址为："+newfiles.getAttpath());
        // System.out.println("文件名称为："+newfiles.getAttname());
        // int i = labelOperator.removeFileLabel("00020592", newfiles.getAttpath(), newfiles.getAttname());
        // System.out.println("文件流去除密标结果为："+i);
        //文件流加密测试
        //获取去除密标之后的文件字节流 并生成对应的文件进行加密上传
        System.out.println("文件流加密测试");
        System.out.println("文件地址为："+newfiles.getAttpath());
        encryptedFileBytes  = FtpUtil.readfile(newfiles.getAttpath());
        if (encryptedFileBytes == null){
            return "文件字节流加密时文件为空";
        }
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
        result = this.uploadFileWithMultipart(bytes, filename, fileUploadPath,token);
        newfiles = new TblAttachment();
        reJson = JSONObject.parseObject(result);
        code = reJson.getString("code");
        if("200".equals(code)) {
            JSONArray attJsonArray = reJson.getJSONArray("data");
            JSONObject attJsonObj = attJsonArray.getJSONObject(0);
            newfiles.setAttid(attJsonObj.getLong("attid"));
            newfiles.setAttname(attJsonObj.getString("attname"));
            newfiles.setAttpath(attJsonObj.getString("attpath"));
            newfiles.setAttsize(attJsonObj.getDoubleValue("attsize"));
            newfiles.setUploader(attJsonObj.getString("uploader"));
            newfiles.setUploadtime(com.hbfk.util.DateUtil.formatDate(attJsonObj.getString("uploadTime"), com.hbfk.util.DateUtil.DATE_FULL_STR));
            newfiles.setJmurl(attJsonObj.getString("previewUrl"));
            newfiles.setIspythonflag("1".equals(attJsonObj.getString("isEncrypted"))?"1":"0");
        }else {
            return "上传加密文件失败";
        }
        String jmurl = newfiles.getJmurl();
        String zxtoken = null;
        if (jmurl != null) {
            int startIndex = jmurl.indexOf("url=");
            if (startIndex != -1) {
                zxtoken = jmurl.substring(startIndex + 4); // 提取"url="之后的所有内容
            }
        }
        return previewUrl + zxtoken;
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
    
    //
    public BigDecimal selectSecretLabel(String attachmentLevel) throws Exception {
        return tblAttachmentMapper.selectattachmentLevel(attachmentLevel) ;
    }
    
    //
    public Integer getAttachmentList(String formlevel, String attachmentLevelId) throws Exception {
        return tblAttachmentMapper.selectattachmentList(formlevel,attachmentLevelId) ;

    }
    
    //新增方法
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
