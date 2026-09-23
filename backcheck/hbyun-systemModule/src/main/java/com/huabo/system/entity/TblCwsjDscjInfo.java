package com.huabo.system.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//数据采集定时信息表
@Data
public class TblCwsjDscjInfo {
	@TableId(value="INFOID",type = IdType.INPUT)
	@Schema(name="主键")
	private BigDecimal infoId;
	@TableField("INFONAME")
	@Schema(name="采集策略名称")
	private String infoName;
	@TableField("ORGID")
	@Schema(name="公司主键")
	private BigDecimal orgId;
	@TableField("SETDATE")
	@Schema(name="设置日期")
	private String setDate;
	@TableField("ACSPACE")
	@Schema(name="采集时间")
	private Integer acSpace;
	@TableField("CREATETIME")
	@Schema(name="创建时间")
	private Date createTime;
	@TableField("ACCTID")
	@Schema(name="账套库")
	private String acctId;
	@TableField("STATUS")
	@Schema(name="启用弃用窗台")
	private Integer status;
	@TableField("ACWEEK")
	@Schema(name="星期记录数")
	private String acWeek;
	@TableField("STAFFID")
	@Schema(name="创建人")
	private BigDecimal staffId;
	@Transient
	@Schema(name="员工真实姓名")
	private String realName;
	@Transient
	@Schema(name="公司名称")
	private String orgName;
	
}
