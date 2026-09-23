package com.financial.sharing.enums;

/**
 * 导出格式枚举
 */
public enum ExportFormat {

    EXCEL("excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"),
    PDF("pdf", "application/pdf", ".pdf"),
    CSV("csv", "text/csv", ".csv"),
    JSON("json", "application/json", ".json"),
    XML("xml", "application/xml", ".xml"),
    WORD("word", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");

    private String code;
    private String contentType;
    private String fileExtension;

    ExportFormat(String code, String contentType, String fileExtension) {
        this.code = code;
        this.contentType = contentType;
        this.fileExtension = fileExtension;
    }

    public String getCode() {
        return code;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    /**
     * 根据代码获取枚举
     */
    public static ExportFormat getByCode(String code) {
        for (ExportFormat format : values()) {
            if (format.getCode().equals(code)) {
                return format;
            }
        }
        throw new IllegalArgumentException("未知的导出格式: " + code);
    }

    /**
     * 获取所有导出格式
     */
    public static ExportFormat[] getAllFormats() {
        return values();
    }

    /**
     * 检查格式是否支持
     */
    public static boolean isSupported(String code) {
        try {
            getByCode(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}