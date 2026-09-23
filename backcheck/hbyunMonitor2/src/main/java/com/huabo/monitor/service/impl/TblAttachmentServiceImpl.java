package com.huabo.monitor.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.mapper.TblAttachmentMapper;
import com.huabo.monitor.mapper.YhrPageMapper;
import com.huabo.monitor.service.ITblAttachmentService;
import com.huabo.monitor.util.AESUtil;
import com.huabo.monitor.util.ErrorCodeEnum;
import com.huabo.monitor.util.FileException;
import com.huabo.monitor.vo.FileUploadRes;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhr
 * @since 2022-08-29
 */
@Slf4j
@Service
public class TblAttachmentServiceImpl extends ServiceImpl<TblAttachmentMapper, TblAttachment> implements ITblAttachmentService {

	@Value("${file.upload.path}")
	private String filePath;
	@Value("${file.ca.secret}")
	private String caSecret;
	@Value("${file.previewUrl}")
	private String previewUrl;
	@Value("${file.downloadUrl}")
	private String downloadUrl;
	@Resource
	private UserProvider userProvider;
	@Resource
    TblAttachmentMapper  attachmentMapper;
    @Resource
    YhrPageMapper mapper;

    
    @Override
    public List<TblAttachment> findtTblAttachmentByTask(BigDecimal taskid) {
        String sql = "SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN ( SELECT ATTID FROM TBL_TESTTASK_ATT where TESTTASKID= "+taskid+")";
        return attachmentMapper.getListBySql(sql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttAndTaskAtt(BigDecimal attid) {
        String sql=" delete from TBL_TESTTASK_ATT where attid="+attid;
        mapper.delete(sql);
        attachmentMapper.deleteEntity(attid);
    }
    
	@Override
	public void saveEntity(TblAttachment tblAttachmentEntity) throws Exception {
		tblAttachmentEntity.setAttid(RandomUtil.uuBigDecimalId());
		this.attachmentMapper.insert(tblAttachmentEntity); //insertEntity
	}

    @Override
    public BigDecimal selectSecretLabel(String attachmentLevel) throws Exception {
        return attachmentMapper.selectattachmentLevel(attachmentLevel) ;
    }

    @Override
    public Integer getAttachmentList(String formlevel, String attachmentLevelId) throws Exception {
        return attachmentMapper.selectattachmentList(formlevel,attachmentLevelId) ;
    }
    
    
    @Override
    public List<FileUploadRes> fileUpload(MultipartFile[] file, Boolean isCa
    		,@Parameter(name = "formlevel", description = "表单密级信息 不为空则启动密标，为空则 不启动密标") String formlevel,BigDecimal attachmentLevelId) throws Exception{
        List<FileUploadRes> fileUploads = new ArrayList<>();
        String fileFullPath;
        // 获取用户信息
    	TblStaffUtil user = userProvider.get();
        for (MultipartFile multipartFile : file) {
            BigDecimal id = RandomUtil.uuBigDecimalId();
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
                String encodedPart = Base64Utils.encodeToString((downloadUrl + fileId + "&fullfilename=" +
                fileId+extractExtension(fileName)).getBytes(StandardCharsets.UTF_8));
                fileUpload.setJmurl(encodedPart);
                saveFileLocal(isCa, multipartFile, dest);

                fileUpload.setAttname(fileName);
                fileUpload.setAttpath(fileFullPath);
                double size = (double) multipartFile.getSize() / 1024;
                // 明确单位为 KiB（二进制千字节）
                fileUpload.setAttsize(new BigDecimal(size / 1024));
                fileUpload.setUploader(user.getUsername());
                fileUpload.setUploadtime(LocalDateTime.now());
                attachmentMapper.insert(fileUpload);
                
                fileUploads.add(new FileUploadRes(fileId, fileName, fileFullPath, size, user.getUsername(),
                        DateUtil.format(fileUpload.getUploadtime(), "yyyy-MM-dd HH:mm:ss"),
                        getPreviewUrl(fileId, fileName), "1")
                ); 
            } catch (Exception e) {
                log.error("文件上传失败", e);
                e.printStackTrace();
                //throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
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
                e.printStackTrace();
                //throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
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

    @Override
    public void fileDownLoad(HttpServletResponse response, String fileId, Boolean isCa)throws Exception {
        TblAttachment file = attachmentMapper.getOne(fileId);
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
            res.add(new FileUploadRes(fileId, file.getAttname(), file.getAttpath(), Double.parseDouble(file.getAttsize().toString()),
                    file.getUploader(), DateUtil.format(file.getUploadtime(), "yyyy-MM-dd HH:mm:ss"),
                    getPreviewUrl(fileId, file.getAttname()), file.getIsEncrypted() ? "1" : "0"));
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

	@Override
	public void delEntity(BigDecimal attid) throws Exception {
		// TODO Auto-generated method stub
		attachmentMapper.deleteEntity(attid);
	}
    
	@Override
	public TblAttachment getOne(BigDecimal attid) throws Exception {
		// TODO Auto-generated method stub
		return attachmentMapper.getOne(attid.toString());
	}
    
  

}
