package com.huabo.file.vo;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ByteMultipartFile implements MultipartFile {
    private final byte[] content;       // 核心：文件字节数组
    private final String name;          // 表单参数名
    private final String originalFilename; // 原始文件名（带扩展名）
    private final String contentType;   // MIME类型

    // 构造方法：直接接收字节数组（推荐）
    public ByteMultipartFile(byte[] content, String name, String originalFilename, String contentType) {
        this.content = content;
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content; // 直接返回字节数组，无性能损耗
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content); // 字节数组转输入流
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Files.write(dest.toPath(), content); // 字节数组写入文件（如需落地）
    }
}