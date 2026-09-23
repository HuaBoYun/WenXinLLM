package com.huabo.bigmodel.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件解析服务：将上传的文档抽取为纯文本，供大模型分析。
 * 支持格式：md / txt / doc / docx / xls / xlsx / pdf
 */
public interface FileParseService {

    /**
     * 解析上传文件，返回抽取出的纯文本内容
     *
     * @param file 上传的文件（MultipartFile）
     * @return 抽取的纯文本
     * @throws Exception 解析失败时抛出
     */
    String parseToText(MultipartFile file) throws Exception;
}
