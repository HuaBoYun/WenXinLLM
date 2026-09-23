package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="用户多组织关系表", description="")
@TableName(value = "TBL_USER_ORGRELATION")
public class TblUserOrgRelation implements Serializable {
	private static final long serialVersionUID = -9026495367071601347L;
	
	@TableField(value="STAFFID")
	@Schema(name="新增用户主键")
	private BigDecimal staffid;
	@TableField("ORGSTRS")
	@Schema(name="公司Ids拼接字符串")
	private String orgstrs;
	@TableField("DEPTID")
	@Schema(name="部门主键")
	private BigDecimal deptId;
	@TableField("ORGID")
	@Schema(name="公司主键")
	private BigDecimal orgId;
	@TableField("CREATETIME")
	@Schema(name="创建时间")
	private Date createTime;
	@TableField("CREATESTAFF")
	@Schema(name="创建用户")
	private BigDecimal createStaff;
	
	@TableField("NUMNO")
	@Schema(name="排序")
	private Integer numno;
	
	@TableField("ORGYMSTRIDS")
	@Schema(name="流程平台公司Ids拼接字符串")
	private String orgYmStrIds;
	
	@Transient
	@Schema(name="公司名称拼部门名称")
	@TableField(exist = false)
	private String longName;
	
	@Transient
	@Schema(name="公司名称")
	@TableField(exist = false)
	private String orgName;
	
	@Transient
	@Schema(name="部门名称")
	@TableField(exist = false)
	private String detpName;
	
}
