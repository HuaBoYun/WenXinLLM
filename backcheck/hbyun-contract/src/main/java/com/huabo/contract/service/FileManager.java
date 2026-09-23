package com.huabo.contract.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileManager {

    /**
     * 文件上传
     * @param file 文件
     * @return 文件访问url, 上传失败返回null
     */
    Map<String,Object>  upload(MultipartFile file, String token);
}
