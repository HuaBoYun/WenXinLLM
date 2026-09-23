package com.huabo.financialdata.entity.enums;

import java.util.Objects;

/**
 * 枚举 - 财务 - 会计科目列表 - 方向枚举
 *
 * @author lee
 */
public enum AccountDcEnum {

    /**
     * 借
     */
    BORROW("D", "借"),

    /**
     * 贷
     */
    LOAN("C", "贷"),

    /**
     * 平
     */
    BALANCE("0", "平"),
    ;


    private String code;
    private String name;

    AccountDcEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static AccountDcEnum getCorpTypeEnum(String code) {
        for (AccountDcEnum accBookStatusEnum : AccountDcEnum.values()) {
            if (accBookStatusEnum.getCode().equals(code)) {
                return accBookStatusEnum;
            }
        }
        return null;
    }

    public static String getNameByCode(String code) {
        return getCorpTypeEnum(code) == null ? "" : Objects.requireNonNull(getCorpTypeEnum(code)).getName();
    }

    public static AccountDcEnum getCorpTypeEnumByName(String name) {
        for (AccountDcEnum accBookStatusEnum : AccountDcEnum.values()) {
            if (accBookStatusEnum.getName().equals(name)) {
                return accBookStatusEnum;
            }
        }
        return null;
    }

    public static String getCodeByName(String name) {
        return getCorpTypeEnumByName(name) == null ? "" : Objects.requireNonNull(getCorpTypeEnumByName(name)).getCode();
    }

    public boolean equals(AccountDcEnum accBookStatusEnum) {
        return this.getCode().equals(accBookStatusEnum.getCode());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
