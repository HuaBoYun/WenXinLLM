package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@Schema(name="OA待办已办流程实体", description="")
@TableName(value = "TBL_OAFLOW_MESSAGE")
public class TblOaFlowMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@TableId("ID")
	@Schema(name="主键ID")
	private String id;
	
	@TableField("TITLE")
	@Schema(name="流程标题")
	private String title;
	
	@TableField("URL")
	@Schema(name="web端访问路径")
	private String url;
	
	
	@TableField("RECEIVETIME")
	@Schema(name="当前人接收时间")
	private String receiveTime;
	
	@TableField("H5URL")
	@Schema(name="移动端路径")
	private String h5Url;
	
	
	@TableField("READFLAG")
	@Schema(name="是否已读")
	private String readFlag;
	
	@TableField("FLOWTYPE")
	@Schema(name="所属类型")
	private String flowType;
	
	@TableField("FLOWMODULE")
	@Schema(name="待办or已办")
	private String flowModule;
	
	@TableField("STAFFID")
	@Schema(name="主键ID,自动增长")
	private BigDecimal staffid;//主键ID,自动增长
	
	@TableField("REALNAME")
	@Schema(name="真实名字")
	private String realname;//真实名字
	
	@TableField("ORGID")
	@Schema(name="所属部门ID")
	private BigDecimal orgid;

	@TableField("ORGNAME")
	@Schema(name="所属部门名称")
	private String orgname;
	
	@TableField("SSFL")
	@Schema(name="所属分类（经营事项审核/制度审核）")
	private String ssfl;
	
}
