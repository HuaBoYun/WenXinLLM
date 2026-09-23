package com.huabo.system.manager;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {

    /**
     * 文件上传
     * @param file 文件
     * @return 文件访问url, 上传失败返回null
     */
    String upload(MultipartFile file);
}
