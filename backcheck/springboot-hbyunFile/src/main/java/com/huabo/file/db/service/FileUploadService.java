//package com.huabo.file.db.service;
//
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.text.StrBuilder;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.hbfk.entity.TblStaffUtil;
//import com.huabo.file.config.TokenInterceptor;
//import com.huabo.file.db.entity.FileUpload;
//import com.huabo.file.db.mapper.FileUploadMapper;
//import com.huabo.file.exception.FileException;
//import com.huabo.file.util.AESUtil;
//import com.huabo.file.util.ErrorCodeEnum;
//import com.huabo.file.util.SaveType;
//import com.huabo.file.vo.FileUploadRes;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.util.Base64Utils;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.math.BigDecimal;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//@Slf4j
//@Service
//public class FileUploadService extends ServiceImpl<FileUploadMapper, FileUpload> {
//
//    @Value("${file.upload.path}")
//    private String filePath;
//    @Value("${file.ca.secret}")
//    private String caSecret;
//    @Value("${file.previewUrl}")
//    private String previewUrl;
//    @Value("${file.downloadUrl}")
//    private String downloadUrl;
//
//
//    /**
//     * 文件上传
//     */
//    public List<FileUploadRes> fileUpload(MultipartFile[] file, Boolean isCa) {
//        List<FileUploadRes> fileUploads = new ArrayList<>();
//        String fileFullPath;
//        // 获取用户信息
//        TblStaffUtil tblStaffUtil = TokenInterceptor.getTblStaffUtil();
//        for (MultipartFile multipartFile : file) {
//            long id = IdUtil.getSnowflakeNextId();
//            FileUpload fileUpload = new FileUpload();
//            fileUpload.setFileId(id);
//            try {
//                String fileName = new String(Objects.requireNonNull(multipartFile.getOriginalFilename()).getBytes());
//                String fileLast = fileName.substring(fileName.lastIndexOf("."));
//                fileFullPath = filePath + "/" + DateUtil.today() + "/" + id;
//                File dest = new File(fileFullPath);
//                if (!dest.getParentFile().exists()) {
//                    if (!dest.getParentFile().mkdirs()) {
//                        log.info("创建目录:[{}]失败", fileFullPath);
//                        throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
//                    }
//                }
//
//                saveFileLocal(isCa, multipartFile, dest);
//
//                fileUpload.setFileName(fileName);
//                fileUpload.setFilePath(fileFullPath);
//                fileUpload.setSaveType(SaveType.LOCAL.getType());
//                fileUpload.setFileExtension(fileLast);
//                long size = multipartFile.getSize() / 1024;
//                fileUpload.setFileSize(size);
//                fileUpload.setUploadTime(new Date());
//                String realName = tblStaffUtil.getRealname();
//                String username = tblStaffUtil.getUsername();
//                BigDecimal staffId = tblStaffUtil.getStaffid();
//                fileUpload.setUploaderName(realName);
//                fileUpload.setUploaderAccount(username);
//                fileUpload.setUserId(staffId.longValue());
//                this.save(fileUpload);
//                String fileId = String.valueOf(id);
//                fileUploads.add(new FileUploadRes(fileId, fileName, size, realName,
//                        DateUtil.format(fileUpload.getUploadTime(), "yyyy-MM-dd HH:mm:ss"),
//                        getPreviewUrl(fileId,fileName), "1")
//                );
//            } catch (Exception e) {
//                log.error("文件上传失败", e);
//                throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
//            }
//        }
//        return fileUploads;
//    }
//
//    private void saveFileLocal(Boolean isCa, MultipartFile multipartFile, File dest) {
//        if (isCa) {
//            try (InputStream inputStream = multipartFile.getInputStream();
//                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
//                // 读取输入流到字节数组
//                byte[] buffer = new byte[1024];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    byteArrayOutputStream.write(buffer, 0, bytesRead);
//                }
//                // 获取字节数组
//                byte[] fileBytes = byteArrayOutputStream.toByteArray();
//                // 加密字节数组
//                String encrypt = AESUtil.encrypt(fileBytes, caSecret);
//                FileUtil.writeString(encrypt, dest, StandardCharsets.UTF_8);
//            } catch (Exception e) {
//                log.error("文件加密保存失败", e);
//                throw new FileException(ErrorCodeEnum.FILE_CA_SAVE_FAIL);
//            }
//            return;
//        }
//
//        try (InputStream inputStream = multipartFile.getInputStream()) {
//            FileUtil.writeFromStream(inputStream, dest);
//        } catch (Exception e) {
//            log.error("文件上传失败(写入本地目录:[{}]失败)", dest.getPath(), e);
//            throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
//        }
//    }
//
//    public void fileDownLoad(HttpServletResponse response, String fileId, Boolean isCa) {
//        FileUpload file = this.getById(fileId);
//        if (file == null) {
//            log.error("文件不存在");
//            throw new FileException(ErrorCodeEnum.FILE_NOT_EXIST);
//        }
//        File fileBase = new File(file.getFilePath());
//        if (!fileBase.exists()) {
//            throw new FileException(ErrorCodeEnum.FILE_NOT_EXIST_LOCAL);
//        }
//        String fileName = file.getFileName();
//        String encodedFileName = fileName;
//        try {
//            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
//            // 替换空格，防止编码后的 "+" 号变成空格
//            encodedFileName = encodedFileName.replaceAll("\\+", "%20");
//        } catch (Exception e) {
//            log.error("文件名字encode失败", e);
//        }
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", new StrBuilder("attachment; filename=")
//                .append("\"").append(encodedFileName).append("\"").toString());
//        if (isCa && file.getIsEncrypted()) {
//
//            try (BufferedReader reader = new BufferedReader(new FileReader(fileBase));
//                 ServletOutputStream outputStream = response.getOutputStream()) {
//
//                String base64Line;
//
//                while ((base64Line = reader.readLine()) != null) {
//                    // 解密每一行 Base64 数据
//                    byte[] decryptedData = AESUtil.decrypt(base64Line, caSecret);
//                    // 写入到响应流
//                    outputStream.write(decryptedData);
//                }
//
//                outputStream.flush();
//            } catch (Exception e) {
//                log.error("文件下载失败", e);
//                throw new FileException(ErrorCodeEnum.FILE_WRITE_RESP_FAIL);
//            }
//        } else {
//            try (InputStream inputStream = new BufferedInputStream(new FileInputStream(fileBase));
//                 ServletOutputStream outputStream = response.getOutputStream()) {
//
//                // 直接复制字节流，避免逐行读取和编码转换
//                byte[] buffer = new byte[8192];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//                outputStream.flush();
//            } catch (Exception e) {
//                log.error("文件下载失败", e);
//                throw new FileException(ErrorCodeEnum.FILE_WRITE_RESP_FAIL);
//            }
//        }
//
//    }
//
//
//    public void removeFile(Long fileId) {
//        FileUpload file = this.getById(fileId);
//        // 删除本地文件
//        if (file != null) {
//            File fileBase = new File(file.getFilePath());
//            if (fileBase.exists()) {
//                if (!fileBase.delete()) {
//                    log.error("文件删除失败");
//                    throw new FileException(ErrorCodeEnum.FILE_DELETE_FAIL_LOCAL);
//                }
//            }
//            this.removeById(fileId);
//        }
//    }
//
//    public List<FileUploadRes> listFileUpload(List<String> ids) {
//        List<FileUploadRes> res = new ArrayList<>();
//        if (CollUtil.isEmpty(ids)) {
//            return res;
//        }
//        for (FileUpload file : this.listByIds(ids)) {
//            String fileId = String.valueOf(file.getFileId());
//            res.add(new FileUploadRes(fileId, file.getFileName(), file.getFileSize(),
//                    file.getUploaderName(), DateUtil.format(file.getUploadTime(), "yyyy-MM-dd HH:mm:ss"),
//                    getPreviewUrl(fileId,file.getFileName()), file.getIsEncrypted() ? "1" : "0"));
//        }
//        return res;
//
//    }
//
//    /**
//     * 获取预览地址
//     *
//     * @param fileId 文件id
//     */
//    public String getPreviewUrl(String fileId,String fileName) {
//        // 空值检查
//        if (StrUtil.isEmpty(previewUrl) || StrUtil.isEmpty(downloadUrl) || StrUtil.isEmpty(fileId)) {
//            return "";
//        }
//        // 将 downloadUrl 和 fileId 拼接并编码
//        String encodedPart = Base64Utils.encodeToString((downloadUrl + fileId + "&fullfilename=" + fileName.replace(".enc","")).getBytes(StandardCharsets.UTF_8));
//        return previewUrl + encodedPart;
//    }
//
//}
