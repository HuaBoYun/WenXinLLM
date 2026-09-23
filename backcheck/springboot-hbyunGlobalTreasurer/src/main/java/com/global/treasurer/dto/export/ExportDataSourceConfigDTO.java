package com.global.treasurer.dto.export;

import com.global.treasurer.util.excel.annotation.ExcelField;
// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.Date;

/**
 * 数据源配置导出DTO
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ExportDataSourceConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(title = "数据源编码", sort = 1, words = 15)
    private String sourceCode;

    @ExcelField(title = "数据源名称", sort = 2, words = 20)
    private String sourceName;

    @ExcelField(title = "数据源类型", sort = 3, words = 12)
    private String sourceType;

    @ExcelField(title = "数据类型", sort = 4, words = 12)
    private String dataType;

    @ExcelField(title = "同步频率", sort = 5, words = 12)
    private String syncFrequency;

    @ExcelField(title = "同步状态", sort = 6, words = 10)
    private String syncStatus;

    @ExcelField(title = "状态", sort = 7, words = 10)
    private String status;

    @ExcelField(title = "备注", sort = 8, words = 30)
    private String remark;

    @ExcelField(title = "创建时间", sort = 9, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelField(title = "最后同步时间", sort = 10, words = 20, dataFormat = "yyyy-MM-dd HH:mm:ss")
    private Date lastSyncTime;

    /**
     * 从实体转换为导出DTO
     */
    public static ExportDataSourceConfigDTO fromEntity(com.global.treasurer.entity.DataSourceConfig entity) {
        if (entity == null) {
            return null;
        }
        ExportDataSourceConfigDTO dto = new ExportDataSourceConfigDTO();
        dto.setSourceCode(entity.getSourceCode());
        dto.setSourceName(entity.getSourceName());
        dto.setSourceType(convertSourceType(entity.getSourceType()));
        dto.setDataType(entity.getDataType());
        dto.setSyncFrequency(convertSyncFrequency(entity.getSyncFrequency()));
        dto.setSyncStatus(convertSyncStatus(entity.getSyncStatus()));
        dto.setStatus("1".equals(entity.getStatus()) ? "启用" : "停用");
        dto.setRemark(entity.getRemark());
        dto.setCreateTime(entity.getCreateTime());
        dto.setLastSyncTime(entity.getLastSyncTime());
        return dto;
    }

    private static String convertSourceType(String type) {
        if (type == null) return "";
        switch (type) {
            case "DATABASE": return "数据库";
            case "API": return "API接口";
            case "FILE": return "文件";
            case "MQ": return "消息队列";
            default: return type;
        }
    }

    private static String convertSyncFrequency(String freq) {
        if (freq == null) return "";
        switch (freq) {
            case "REALTIME": return "实时";
            case "HOURLY": return "每小时";
            case "DAILY": return "每天";
            case "WEEKLY": return "每周";
            case "MONTHLY": return "每月";
            default: return freq;
        }
    }

    private static String convertSyncStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "SUCCESS": return "成功";
            case "FAILED": return "失败";
            case "RUNNING": return "运行中";
            case "PENDING": return "待执行";
            default: return status;
        }
    }


    // 以下方法由Lombok生成,手动添加以解决编译问题


    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public String getSyncFrequency() { return syncFrequency; }
    public void setSyncFrequency(String syncFrequency) { this.syncFrequency = syncFrequency; }
    public String getSyncStatus() { return syncStatus; }
    public void setSyncStatus(String syncStatus) { this.syncStatus = syncStatus; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getLastSyncTime() { return lastSyncTime; }
    public void setLastSyncTime(Date lastSyncTime) { this.lastSyncTime = lastSyncTime; }

}
