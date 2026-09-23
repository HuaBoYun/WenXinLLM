package com.huabo.file.util;

/**
 * 文件保存类型
 */
public enum SaveType {
    LOCAL("LOCAL"),
    NFS("NFS"),
    OSS("OSS");

    private String type;

    SaveType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
