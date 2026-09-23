package com.huabo.system.manager;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@ConfigurationProperties(prefix = "application.file-upload")
@Data
public class LocalFileSystemFileManager implements FileManager {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private String baseDir;
    private String baseUrl;

    @Override
    public String upload(MultipartFile file) {
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
        try {
            file.transferTo(new File(baseDir + pathSuffix));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return baseUrl + pathSuffix;
    }
}
