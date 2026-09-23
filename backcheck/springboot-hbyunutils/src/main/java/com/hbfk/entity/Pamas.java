package com.hbfk.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "操作按钮传入参数")
public class Pamas {
	
	@Schema(description = "操作选中记录的主键，多选用逗号分割，示例：1,2,3,4")
	private String selectIds;
	private String signId;
	private String bookId;
	private String ruleId;
	@Schema(description = "返回页面的路径")
	private String url;
//	private String index;
	private String modelid;
//	private String source;
	@Schema(description = "模块类型，智能审计-nbsj")
	private String type;
	private String id;
	private String mid;
	private String source;
	private String execId;
	private String orgId;
	private String fhtype;
	private String soluId;
	private String assid;
	private String nodeId;
	private String classfin;
	private String node;
	private String templId;
	private String planid;
	

}
