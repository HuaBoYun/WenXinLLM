package com.huabo.financialdata.entity.enums;

import java.util.Objects;

/**
 * 枚举 - 财务 - 账套选中状态
 *
 * @author lee
 */
public enum AccBookStatusEnum {

    /**
     * 未选中
     */
    NULL(null, "未选中"),

    /**
     * 选中
     */
    SELECTED(0, "选中"),
    ;


    private Integer code;
    private String name;

    AccBookStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static AccBookStatusEnum getCorpTypeEnum(Integer code) {
        for (AccBookStatusEnum accBookStatusEnum : AccBookStatusEnum.values()) {
            if (accBookStatusEnum.getCode().equals(code)) {
                return accBookStatusEnum;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        return getCorpTypeEnum(code) == null ? "" : Objects.requireNonNull(getCorpTypeEnum(code)).getName();
    }

    public boolean equals(AccBookStatusEnum accBookStatusEnum) {
        return this.getCode().equals(accBookStatusEnum.getCode());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
