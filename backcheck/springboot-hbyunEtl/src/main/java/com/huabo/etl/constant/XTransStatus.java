package com.huabo.etl.constant;

public enum XTransStatus {
    UNKNOWN("-1", "未知状态"),
    WAITING("10", "等待中"),
    INITIALIZING("20", "转换初始化"),
    PREPARING("21", "转换准备执行"),
    STOPPED("30", "已终止"),
    FINISHED("40", "已结束"),
    RUNNING("50", "转换运行中"),
    PAUSED("60", "转换已被暂停"),
    HALTING("70", "转换被挂起"),
    SUCCESS("88", "转换运行成功"),
    FAILED("99", "异常停止");

    private String status;
    private String description;

    private XTransStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String value() {
        return this.status;
    }

    public String description() {
        return this.description;
    }

    public static XTransStatus forName(Integer value) {
        XTransStatus[] statuses = values();
        XTransStatus[] var2 = statuses;
        int var3 = statuses.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            XTransStatus status = var2[var4];
            if (status.value().equals(value)) {
                return status;
            }
        }

        return UNKNOWN;
    }
}
