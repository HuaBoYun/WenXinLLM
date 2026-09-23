package com.hbfk.enums;

public enum ProcessEnum {

	AUDITPALN("审计计划"),
	//SJ_XMGL("项目管理"),//审计项目列表中审批字段
	//expense("审计项目"),
	invest("投资管理"),
	extraWork("加班流程"),
	//流程标识；
	SJ_JHGL("SJ_JHGL") ,//审计---计划管理
	SJ_JYDA("SJ_JYDA") ,//审计---计划管理
	SJ_XMGL("SJ_XMGL") ,//审计---项目管理
	SJ_JHTZD("SJ_JHTZD"),//审计---计划通知单
	SJ_SJBG("SJ_SJBG"),//审计---审计报告
	SJ_SSQRS("SJ_SSQRS"),//审计---事实确认书
	SJ_DGFH("SJ_DGFH"),//审计---底稿复核
	SJ_SJBGFH("SJ_SJBGFH"),//审计---审计报告复核
	SJ_ZQYJ("SJ_ZQYJ"),//审计---征求意见
	
	SJYH("SJYH"),
	PJSH("PJSH");
	
	private ProcessEnum(String value) {
		this.value = value;
	}
	
	private String value;
	
	public String getValue() {
		return value;
	}
	
}
