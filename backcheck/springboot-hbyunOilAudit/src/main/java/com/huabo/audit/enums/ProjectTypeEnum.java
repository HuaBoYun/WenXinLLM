package com.huabo.audit.enums;

public enum ProjectTypeEnum {
	NO_SELECT(0),//未切换
	YE_SELECT(1),//已切换
	UPDATENO(0),//不完整
	UPDATEYES(1),//完整
	GD_STATUS(4),//审计项目归档状态
	EXAMINETYPE1(1),//未审批
	EXAMINETYPE2(2),//审批中
	EXAMINETYPE3(3),//审批驳回
	EXAMINETYPE4(4),//审批通过
	EXAMINETYPE5(5),//需调整
	EXAMINETYPE6(6);//中断
	private ProjectTypeEnum(Integer value) {
		this.value = value;
	}
	
	private Integer value;
	
	public Integer getValue() {
		return value;
	}
	
}
