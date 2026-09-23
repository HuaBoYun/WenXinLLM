package com.financial.sharing.enums;

/**
 * 数据源类型枚举
 */
public enum DataSourceType {

    DATABASE_TABLE("database_table", "数据库表"),
    API_INTERFACE("api_interface", "API接口"),
    EXCEL_FILE("excel_file", "Excel文件"),
    CSV_FILE("csv_file", "CSV文件"),
    FIXED_WIDTH_FILE("fixed_width_file", "定宽文件"),
    JSON_FILE("json_file", "JSON文件"),
    XML_FILE("xml_file", "XML文件"),
    WEB_SERVICE("web_service", "Web服务");

    private String code;
    private String description;

    DataSourceType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     */
    public static DataSourceType getByCode(String code) {
        for (DataSourceType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的数据源类型: " + code);
    }

    /**
     * 获取所有数据源类型
     */
    public static DataSourceType[] getAllTypes() {
        return values();
    }

    /**
     * 检查是否为文件类型
     */
    public boolean isFileType() {
        return this == EXCEL_FILE || this == CSV_FILE || this == FIXED_WIDTH_FILE ||
               this == JSON_FILE || this == XML_FILE;
    }

    /**
     * 检查是否为接口类型
     */
    public boolean isApiType() {
        return this == API_INTERFACE || this == WEB_SERVICE;
    }

    /**
     * 检查是否为数据库类型
     */
    public boolean isDatabaseType() {
        return this == DATABASE_TABLE;
    }
}