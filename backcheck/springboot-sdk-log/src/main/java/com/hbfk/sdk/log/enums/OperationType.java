package com.hbfk.sdk.log.enums;

import lombok.Getter;

/**
 * 操作类型
 */
public enum OperationType {
    SELECT(0, "查询"),
    ADD(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    DEFAULT(4, "默认"),
    IMPORT(5, "导入"),
    EXPORT(6, "导出"),
    DISPATCH(7, "下发"),
    UPLOAD(8, "上传"),
    DOWNLOAD(9, "下载"),
    PREVIEW(10, "预览"),
    APPROVE(11, "审核通过"),
    REJECTION(12, "审核拒绝"),
    FORWARDED(13, "转审"),
    WITHDRAW(14, "撤回"),
    SUBMIT_APPROVE(15, "提交审批"),
    REPORT(16,"上报")
    ;
    @Getter
    int code;
    @Getter
    String name;

    OperationType(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
