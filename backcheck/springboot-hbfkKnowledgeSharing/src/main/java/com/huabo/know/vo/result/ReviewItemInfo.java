package com.huabo.know.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Schema(name="风险清单配置信息")
public class ReviewItemInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Schema(name = "主键ID")
	private String id;
	
	@Schema(name = "名称")
	private String name;
	
	@Schema(name = "编号")
	private String code;
	
	@Schema(name = "类型")
	private String type;
	
	@Schema(name = "排序")
	private Integer sort;

	@Schema(name = "主要内容")
	private String mainBody;

	@Schema(name = "上级id")
	private String pid;

	@Schema(name = "风险清单id")
	private String reviewId;
	
	@Schema(name = "创建公司")
	private String createCompany;

	@Schema(name = "创建部门")
	private String createDept;

	@Schema(name = "创建人")
	private String createBy;

	@Schema(name = "创建时间")
	private Date createTime;

	@Schema(name = "下级")
	private List<ReviewItemInfo> children;
	
}
