package com.huabo.audit.enums;

public enum AuditPlanEnum {
	NO_SHS(0),//未实施
	YE_SHS(1),//已实施
	SPNO(0),//未审批
	SPKA(1),//审批中
	SPTZ(2),//需调整
	SPWC(3),//审批完
	SPZZ(4),
	YSCXM(5);//已生成项目
	private AuditPlanEnum(Integer value) {
		this.value = value;
	}
	
	private Integer value;
	
	public Integer getValue() {
		return value;
	}
	
}
