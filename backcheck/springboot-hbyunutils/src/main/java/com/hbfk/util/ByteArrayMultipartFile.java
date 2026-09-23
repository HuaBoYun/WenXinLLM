package com.hbfk.util;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.Assert;
import java.io.*;

public class ByteArrayMultipartFile implements MultipartFile {
    
    private final byte[] content;
    private final String name;
    private final String originalFilename;
    private final String contentType;
    
    public ByteArrayMultipartFile(byte[] content, String name, String originalFilename, String contentType) {
        Assert.notNull(content, "Content must not be null");
        this.content = content;
        this.name = name;
        this.originalFilename = (originalFilename != null ? originalFilename : "");
        this.contentType = contentType;
    }
    
    public ByteArrayMultipartFile(byte[] content, String originalFilename, String contentType) {
        this(content, "", originalFilename, contentType);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getOriginalFilename() {
        return this.originalFilename;
    }
    
    @Override
    public String getContentType() {
        return this.contentType;
    }
    
    @Override
    public boolean isEmpty() {
        return this.content.length == 0;
    }
    
    @Override
    public long getSize() {
        return this.content.length;
    }
    
    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }
    
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(this.content);
        }
    }
}