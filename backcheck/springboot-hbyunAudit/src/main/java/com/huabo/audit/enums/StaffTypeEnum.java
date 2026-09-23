package com.huabo.audit.enums;

/**
 *@ClassName StaffTypeEnum
 *@Description 员工类型枚举
 *@Author ZiYao
 *@Date 2022/4/15 14:14
 *@Version 1.0
 **/
public enum StaffTypeEnum {

    GROUP_LEADER( 0,"组长"),
    TEAM_MEMBER( 1,"组员");

    private int value;
    private String description;

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 构造方法
     * @param value
     * @param description
     */
    StaffTypeEnum(int value, String description) {
        this.description=description; this.value = value;
    }

    public static String getDescriptionByValue(Integer value){
        String result=null;
        if(value==null){ return result; }
        for(StaffTypeEnum item: StaffTypeEnum.values()) {
            if(item.getValue()==value) {
                return item.getDescription();
            }
        }
        return result;
    }
}
