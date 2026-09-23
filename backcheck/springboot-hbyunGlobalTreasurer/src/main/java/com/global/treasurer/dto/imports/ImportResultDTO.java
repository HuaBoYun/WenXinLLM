package com.global.treasurer.dto.imports;

// import lombok.Data; // 已移除

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务系统导入结果DTO
 * 用于返回批量导入的执行结果
 *
 * @author 华博云开发团队
 * @since 2026-01-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ImportResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 成功数量
     */
    private int successCount;

    /**
     * 失败数量
     */
    private int failCount;

    /**
     * 错误信息列表
     */
    private List<ImportError> errors;

    public ImportResultDTO() {
        this.errors = new ArrayList<>();
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }
    public int getFailCount() { return failCount; }
    public void setFailCount(int failCount) { this.failCount = failCount; }
    public List<ImportError> getErrors() { return errors; }
    public void setErrors(List<ImportError> errors) { this.errors = errors; }

    /**
     * 添加错误信息
     */
    public void addError(int row, String message) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ImportError(row, message));
    }
}
