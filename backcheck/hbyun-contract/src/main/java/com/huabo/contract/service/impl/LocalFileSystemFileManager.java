package com.huabo.contract.service.impl;

import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.mapper.TblAttachmentMapper;
import com.huabo.contract.service.FileManager;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@ConfigurationProperties(prefix = "application.file-upload")
@Data
public class LocalFileSystemFileManager implements FileManager {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private String baseDir;
    private String baseUrl;

    @Resource
    public TblAttachmentMapper tblAttachmentMapper;
    
    @Resource
    private UserProvider userProvider;

    @Override
    public Map<String,Object>  upload(MultipartFile file, String token) {
        String originalFilename = file.getOriginalFilename();
        Assert.notNull(originalFilename, "original filename must not be null");
        String filenameExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        filenameExtension = filenameExtension.toLowerCase();
        String typeDir;
        switch (filenameExtension) {
            case "jpg": case "jpeg": case "png":
                typeDir = "image";
                break;
            case "txt": case "doc": case "docx": case "xlsx": case "xls": case "pdf":
                typeDir = "doc";
                break;
            case "rar": case "zip": case "7z":
                typeDir = "archive";
                break;
            case "mp4":
                typeDir = "video";
                break;
            case "mp3":
                typeDir = "audio";
                break;
            default:
                typeDir = "file";
                break;
        }

        String dateDirs = LocalDate.now().format(DATE_FORMATTER);
        String uuidDir = UUID.randomUUID().toString().replace("-", "");

        String pathSuffix = "/" + typeDir + "/" + dateDirs + "/" + uuidDir + "/" + originalFilename;

        File destination = new File(baseDir + pathSuffix);
        destination.getParentFile().mkdirs();
        Map<String,Object> resultMap = new HashMap<String, Object>(0);
        try {
            file.transferTo(new File(baseDir + pathSuffix));
            TblStaffUtil staff = userProvider.get();
            TblAttachment att = new TblAttachment();
            att.setAttsize(file.getSize()/1000);
            att.setAttpath(baseUrl + pathSuffix);
            att.setFileName(originalFilename);
            att.setAttname(file.getOriginalFilename());
            att.setUploader(staff.getRealname());
            att.setUploadtime(new Date());
            att.setAttid(RandomUtil.uuBigDecimalId());
            this.tblAttachmentMapper.insert(att);
            resultMap.put("code", "1");
            resultMap.put("msg", "附件保存成功！");
            resultMap.put("data", att);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return resultMap;
    }
}
