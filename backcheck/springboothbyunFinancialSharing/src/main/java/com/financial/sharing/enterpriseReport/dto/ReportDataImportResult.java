package com.financial.sharing.enterpriseReport.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 报表数据导入结果DTO
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Data
public class ReportDataImportResult {

    /**
     * 总行数
     */
    private Integer totalRows;

    /**
     * 成功行数
     */
    private Integer successRows;

    /**
     * 失败行数
     */
    private Integer failRows;

    /**
     * 错误信息列表
     */
    private List<String> errorMessages;

    public ReportDataImportResult() {
        this.totalRows = 0;
        this.successRows = 0;
        this.failRows = 0;
        this.errorMessages = new ArrayList<>();
    }

    /**
     * 添加错误信息
     */
    public void addError(int rowNum, String message) {
        this.errorMessages.add("第" + rowNum + "行: " + message);
        this.failRows++;
    }

    /**
     * 增加成功行数
     */
    public void addSuccess() {
        this.successRows++;
    }

    /**
     * 是否有错误
     */
    public boolean hasError() {
        return this.failRows > 0;
    }
}

