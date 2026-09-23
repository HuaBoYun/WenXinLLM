package com.huabo.finance.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据采集请求VO
 * 用于接收前端的采集任务创建请求
 * 
 * @author 开发团队
 * @since 2025-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name = "CollectionRequest对象", description = "数据采集请求参数")
public class CollectionRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(name =  "采集方案ID")
	private String planId;

	@Schema(name =  "数据源ID")
	private String dataSourceId;

	@Schema(name =  "任务名称")
	private String taskName;

	@Schema(name =  "采集类型: FULL(全量) / INCREMENT(增量)")
	private String collectionType;

	@Schema(name =  "是否异步执行")
	private Boolean async;

	@Schema(name =  "超时时间(秒)")
	private Integer timeout;

	@Schema(name =  "备注")
	private String remark;
}

