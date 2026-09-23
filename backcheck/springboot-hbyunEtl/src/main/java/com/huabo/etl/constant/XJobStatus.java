package com.huabo.etl.constant;

public enum XJobStatus {
    UNKNOWN("-1", "未知状态"),
    PENDING("10", "等待中"),
    RUNNING("20", "运行中"),
    HALTING("21", "终止中"),
    STOPPED("30", "已中断"),
    FINISHED("40", "已完成"),
    SUCCESS("88", "运行成功"),
    FAILED("99", "异常停止");

    private String status;
    private String description;

    private XJobStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String value() {
        return this.status;
    }

    public String description() {
        return this.description;
    }

    public static XJobStatus forName(Integer value) {
        XJobStatus[] statuses = values();
        XJobStatus[] var2 = statuses;
        int var3 = statuses.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            XJobStatus status = var2[var4];
            if (status.value().equals(value)) {
                return status;
            }
        }

        return UNKNOWN;
    }
}
