package com.global.treasurer.dto.imports;

// import lombok.Data; // 已移除

import java.io.Serializable;

/**
 * 导入错误信息
 *
 * @author 华博云开发团队
 * @since 2026-01-29
 */
// @Data // 已移除,使用手动编写的getter/setter
public class ImportError implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 行号 */
    private int row;

    /** 错误信息 */
    private String message;

    public ImportError() {
    }

    public ImportError(int row, String message) {
        this.row = row;
        this.message = message;
    }

    // 以下方法由Lombok生成,手动添加以解决编译问题

    public int getRow() { return row; }
    public void setRow(int row) { this.row = row; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
