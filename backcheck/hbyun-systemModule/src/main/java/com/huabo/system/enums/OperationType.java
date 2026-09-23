package com.huabo.system.enums;

import lombok.Getter;

/**
 * 操作类型
 */
public enum OperationType {
	MODULE(1,"功能密级"),
	FORM(2,"表单密级"),
	STAFF(3,"人员密级"),
	FILE(4,"附件密级"),
	
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
