package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据转换请求VO
 * 用于接收前端的转换任务创建请求
 * 
 * @author 开发团队
 * @since 2025-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "TransformRequest对象", description = "数据转换请求参数")
public class TransformRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name = "采集任务ID")
	private String collectionTaskId;

	@Schema(name = "任务名称")
	private String taskName;

	@Schema(name = "转换规则")
	private String transformRule;

	@Schema(name = "目标表名")
	private String targetTable;

	@Schema(name = "转换类型: INSERT / UPDATE / DELETE")
	private String transformType;

	@Schema(name = "是否异步执行")
	private Boolean async;

	@Schema(name = "超时时间(秒)")
	private Integer timeout;

	@Schema(name = "备注")
	private String remark;
}

