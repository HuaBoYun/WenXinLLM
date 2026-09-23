package com.huabo.system.flow;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FlowTaskOperator {

	@Schema(name="自然主键")
	private String fid;
	
	@Schema(name="经办对象")
	private String handleType;
	
	@Schema(name="经办主键")
	private String handleId;
	
	@Schema(name="处理状态")
	private Integer handleStatus;
	
	@Schema(name="处理时间")
	private Date handleTime;
	
	@Schema(name="节点编号")
	private String nodeCode;
	
	@Schema(name="节点名称")
	private String nodeName;
	
	@Schema(name="是否完成")
	private Integer completion;
	
	@Schema(name="描述")
	private String description;
	
	@Schema(name="创建时间")
	private Date creatorTime;
	
	@Schema(name="节点主键")
	private String taskNodeId;
	
	@Schema(name="任务主键")
	private String taskId;
	
	@Schema(name="节点类型")
	private String type;
	
	@Schema(name="状态")
	private String state;
	
	@Schema(name="父节点id")
	private String parentId;
	
	@Schema(name="草稿数据")
	private String draftData;
	
	@Schema(name="自动审批")
	private String automation;
	
	@Schema(name="排序码")
	private Integer sortCode;
	
	@Schema(name="冻结审批人")
	private String reject;
	
}
