package com.hbfk.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class YMParam implements Serializable {
	private static final long serialVersionUID = -1464776945724389113L;
	
	public YMParam() {
		super();
	}
	
	public YMParam(String fatherId,String orgNumber,String orgName) {
		this.fatherId = fatherId;
		this.orgNumber = orgNumber;
		this.orgName = orgName;
	};
	
	public YMParam(BigDecimal rid){
		this.rid = rid;
	}
	
	public YMParam(String account) {
		this.account = account;
	}
	
	public YMParam(BigDecimal jobid, String pkymOrgId) {
		this.jobid = jobid;
		this.pkymOrgId = pkymOrgId;
	}
	
	private String eventType;//操作类型 ，提交-submit

	private String fatherId;//流程平台信息中的父级主键
	private String orgNumber;//公司编号
	private String orgName;//公司名称
	private BigDecimal rid;//角色主键  -- 映射流程平台中的角色编号 唯一
	private String account;//用户账号
	private BigDecimal jobid;//岗位主键  -- 映射 流程平台中的岗位编号  唯一
	private String pkymOrgId;//流程平台中的组织主键
	private String flowTaskOperatorId;//流程任务节点主键
	
	private String formId;//业务单据主键
	
	private String taskId;//流程任务主键
	private String flowEngienId;//流程引擎主键
	private String nodeCode;//流程节点编号
	private String candidateType;//1-流程分支，2-候选人，3-没有流程分支和候选人 流程节点类型
	private String candidateList;//候选人数组 candidateType = 2时候传入
	private String branchStrs;//candidateType=1 的时候传入，选择分支，可以多选 用逗号分割
	private String copyYmId;//抄送人主键多个用,分割
	
	private String rejectStep;//多节点返回的nodeCode
	private String rejectType;//驳回类型 1.重新审批 2.从当前节点审批
	
	private String handleStatus;//审批处理标识，0.拒绝  1.同意
	
	private String freeApproverYmUserId;//多人加签下一步办理人；
	
	private String handleOpinion;//审批意见
	private String signImg;//电子签名
	private String enCode;//流程编号
	
	private String currNodeId;//当前办理节点Id
	
	private String opType;//查看类型 0-查看发起,3-查看待办在办，4-查看已办
	
	
	
	private HashMap<String, Object> formDataMap;//表单数据变量
	private Map<String, String> headerMap;//请求头部header
	
}
