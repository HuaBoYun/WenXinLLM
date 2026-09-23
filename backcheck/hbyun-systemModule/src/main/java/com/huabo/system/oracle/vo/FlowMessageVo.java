package com.huabo.system.oracle.vo;

import lombok.Data;
import lombok.val;

@Data
public class FlowMessageVo {
	private String muessage;  //发起人 提醒消息
	private Integer doType;   ////1 通过查看详情  0.拒绝需要调整
	private String flowUrl;   //访问路径
	
	private String flowId;
	private String id;
	private String processId;
	private String thisStepId;
	private String operatorId;
	
	
	
}
