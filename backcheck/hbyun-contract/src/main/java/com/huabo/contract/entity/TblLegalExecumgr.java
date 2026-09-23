package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_LEGAL_EXECUMGR")
@Schema(name="执行管理实体类")
public class TblLegalExecumgr implements Serializable {
	
	private static final long serialVersionUID = -571717622320487282L;

	@TableId(value = "ID" ,type = IdType.INPUT)
    private BigDecimal id;
	
	@Schema(name = "纠纷名称")
	@TableField("DISPUTENAME")
	private String disputename;
	@Schema(name = "所属纠纷")
	@TableField("DISPUTEID")
	private BigDecimal disputeid;
	
	@Schema(name = "执行案号")
	@TableField("EXECUNO")
	private String execuno;
	
	@Schema(name = "执行法院")
	@TableField("EXECUCOURT")
	private String execucourt;
	
	@Schema(name = "执行方式")
	@TableField("EXECUTYPE")
	private String executype;
	
	@Schema(name = "执行总金额")
	@TableField("EXECUAMOUNT")
	private BigDecimal execuamount;
	
	@Schema(name = "已执行总金额")
	@TableField("EXECUEDAMOUNT")
	private BigDecimal execuedamount;
	
	@Schema(name = "未执行总金额")
	@TableField("NOEXECUAMOUNT")
	private BigDecimal noexecuamount;
	
	@Schema(name = "状态")
	@TableField("STATUS")
	private Integer status;
	
	//=====
	
	@Schema(name = "诉讼过程id")
	@TableField("LITIGATIONID")
	private BigDecimal litigationid;
	
	
	@Schema(name = "诉讼过程名称")
	@TableField("LITIGATIONNAME")
	private String litigationname;
	
	@Schema(name = "仲裁过程id")
	@TableField("ARBITRAID")
	private BigDecimal arbitraid;
	
	@Schema(name = "仲裁过程名称")
	@TableField("ARBITRANAME")
	private String arbitraname;
	
	@Schema(name = "创建时间")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("CREATETIME")
    private Date createtime;
	
	@Schema(name = "创建人")
	@TableField("CREATESTAFFID")
	private BigDecimal createstaffid;
	
	
	
	@Schema(name = "所属公司")
	@TableField("ORGID")
	private BigDecimal orgid;
	
	
	@Schema(name = "所属过程（诉讼或仲裁）")
	@TableField("SSLX")
	private String sslx;

	@Schema(name = "执行时间 格式：yyyy-MM-dd")
	@TableField("EXECUTIONTIME")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JSONField(format = "yyyy-MM-dd")
	private Date executiontime;
	
}