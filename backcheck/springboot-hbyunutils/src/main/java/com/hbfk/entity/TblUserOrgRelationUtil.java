package com.hbfk.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户多组织关系表")
public class TblUserOrgRelationUtil implements Serializable {
	private static final long serialVersionUID = -9026495367071601347L;
	
	@Schema(description = "新增用户主键")
	private BigDecimal staffid;
	@Schema(description = "公司Ids拼接字符串")
	private String orgstrs;
	@Schema(description = "部门主键")
	private BigDecimal deptId;
	@Schema(description = "公司主键")
	private BigDecimal orgId;
	@Schema(description = "创建时间")
	private Date createTime;
	@Schema(description = "创建用户")
	private BigDecimal createStaff;
	@Schema(description = "排序")
	private Integer numno;
	@Schema(description = "流程平台公司Ids拼接字符串")
	private String orgYmStrIds;
	@Schema(description = "公司名称拼部门名称")
	private String longName;
	@Schema(description = "公司名称")
	private String orgName;
	@Schema(description = "部门名称")
	private String detpName;
	
}
