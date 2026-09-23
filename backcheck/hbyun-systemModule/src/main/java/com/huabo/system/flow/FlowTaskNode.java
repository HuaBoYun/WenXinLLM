package com.huabo.system.flow;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FlowTaskNode {

	@Schema(name="自然主键")
	private String fid;
	
	@Schema(name="节点编号")
	private String nodeCode;
	
	@Schema(name="节点名称")
	private String nodeName;
	
	@Schema(name="节点类型")
	private String nodeType;
	
	@Schema(name="节点属性Json")
	private String nodePropertyJson;
	
	@Schema(name="上一节点")
	private String nodeup;
	
	@Schema(name="下一节点")
	private String nodeNext;
	
	@Schema(name="是否完成")
	private Integer completion;
	
	@Schema(name="描述")
	private String description;
	
	@Schema(name="排序码")
	private Integer sortCode;
	
	@Schema(name="创建时间")
	private Date creatorTime;
	
	@Schema(name="任务主键")
	private String taskId;
	
	@Schema(name="状态")
	private String state;
	
	@Schema(name="候选人")
	private String candidates;
	
	@Schema(name="依次审批")
	private String counterSign;
	
	@Schema(name="草稿数据")
	private String draftData;
	
}
