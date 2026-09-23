package com.huabo.system.flow;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FlowTaskOperatorRecord {

	@Schema(name="自然主键")
	private String fid;
	
	@Schema(name="节点编号")
	private String nodeCode;
	
	@Schema(name="节点名称")
	private String nodeName;
	
	@Schema(name="处理状态")
	private Integer handleStatus;
	
	@Schema(name="经办主键")
	private String handleId;
	
	@Schema(name="处理时间")
	private Date handleTime;
	
	@Schema(name="经办理由")
	private String handleOpinion;
	
	@Schema(name="经办主键")
	private String taskOperatorId;
	
	@Schema(name="节点主键")
	private String taskNodeId;
	
	@Schema(name="任务主键")
	private String taskId;
	
	@Schema(name="签名图片")
	private String signImg;
	
	@Schema(name="审批标识")
	private Integer status;
	
	@Schema(name="流转操作人")
	private String operatorId;
	
	@Schema(name="经办文件")
	private String fileList ;
	
}
