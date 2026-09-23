package com.huabo.cybermonitor.enmus;

/**
 * 穿透式监管领域类型枚举
 *
 * @author system
 * @since 2026-03-26
 */
public enum DomainType {

    INVESTMENT("INVESTMENT", "投资穿透式监管"),
    PROPERTY("PROPERTY", "产权穿透式监管"),
    FINANCIAL("FINANCIAL", "财务穿透式监管"),
    FINANCIAL_RISK("FINANCIAL_RISK", "金融风险穿透式监管"),
    ACCOUNTING("ACCOUNTING", "会计穿透式监管"),
    SALARY("SALARY", "薪酬分配穿透式监管"),
    MILITARY("MILITARY", "军品业务穿透式监管"),
    PROCUREMENT("PROCUREMENT", "采购供应链穿透式监管"),
    OVERSEAS("OVERSEAS", "境外单位穿透式监管"),
    CONTRACT("CONTRACT", "合同穿透式监管");

    private final String code;
    private final String desc;

    DomainType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static DomainType fromCode(String code) {
        for (DomainType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}

