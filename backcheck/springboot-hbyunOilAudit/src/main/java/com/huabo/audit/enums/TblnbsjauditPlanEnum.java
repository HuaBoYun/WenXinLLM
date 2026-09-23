package com.huabo.audit.enums;

/**
* @description  流程状态枚举
* @author   lyz
* @date 2022/4/19 11:04
*/
public enum TblnbsjauditPlanEnum {
    NO_SHS(0, "未实施"),
    YE_SHS(1, "已实施"),
    SPNO(0, "未审批"),
    SPKA(1, "审批中"),
    SPTZ(2, "需调整"),
    SPWC(3, "3审批完"),
    SPZZ(4, ""), //不知道是啥
    YSCXM(5, "已生成项目");

    TblnbsjauditPlanEnum(long code, String message) {
        this.code = code;
        this.message = message;
    }

    private long code;
    private String message;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
