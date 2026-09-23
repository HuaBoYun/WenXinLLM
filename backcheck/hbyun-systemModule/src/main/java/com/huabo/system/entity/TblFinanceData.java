package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FINANCEDATA")
@Schema(name="TblFinanceData对象", description="")
public class TblFinanceData implements Serializable {
	private static final long serialVersionUID = 1509385990408224564L;

	@TableId(value="ORDERID",type = IdType.INPUT)
	@Schema(name="主键")
	private BigDecimal orderId;
	@TableField("OPERTYPE")
	@Schema(name="类型")
	private String operType;
	@TableField("SOURCEDBTYPE")
	@Schema(name="源端数据库类型")
	private String sourceDbType;
	@TableField("SOURCECONN")
	@Schema(name="源端数据库连接路径")
	private String sourceConn;
	@TableField("SOURCEUSERID")
	@Schema(name="源端数据库用户名")
	private String sourceUserId;
	@TableField("SOURCEPASSWORD")
	@Schema(name="源端数据库密码")
	private String sourcePassWord;
	@TableField("DESTDBTYPE")
	@Schema(name="目标端数据库类型")
	private String destDbType;
	@TableField("DESTCONN")
	@Schema(name="目标端连接方式")
	private String destConn;
	@TableField("DESTUSERID")
	@Schema(name="目标端数据库用户")
	private String destUserId;
	@TableField("DESTPASSWORD")
	@Schema(name="目标端数据库密码")
	private String destPassWord;
	@TableField("COMPANYID")
	@Schema(name="所属公司主键")
	private String companyId;
	@TableField("COMPANYNAME")
	@Schema(name="公司名称")
	private String companyName;
	@TableField("STATUS")
	@Schema(name="状态区别")
	private Integer status;
	@TableField("FID")
	private String fid;//版本
	@TableField("FVENDOR")
	private String fvendor;//版本名称
	@TableField("STARTDATE")
	private Integer startdate;//开始年份
	@TableField("ENDDATE")
	private Integer enddate;//结束年份
	@TableField("DESTCOMPANYID")
	@Schema(name="源端数据库公司主键")
	private String destcompanyid;
	@TableField("DESTBOOKID")
	@Schema(name="源端数据库模式名")
	private String destbookid;
	
	
	
	public TblFinanceData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public TblFinanceData(BigDecimal orderId, String operType, String sourceDbType, String sourceConn,
                          String sourceUserId, String sourcePassWord, String destDbType, String destConn, String destUserId,
                          String destPassWord, String companyId, String companyName, Integer status, String fid, String fvendor,
                          Integer startdate, Integer enddate, String destcompanyid, String destbookid) {
		super();
		this.orderId = orderId;
		this.operType = operType;
		this.sourceDbType = sourceDbType;
		this.sourceConn = sourceConn;
		this.sourceUserId = sourceUserId;
		this.sourcePassWord = sourcePassWord;
		this.destDbType = destDbType;
		this.destConn = destConn;
		this.destUserId = destUserId;
		this.destPassWord = destPassWord;
		this.companyId = companyId;
		this.companyName = companyName;
		this.status = status;
		this.fid = fid;
		this.fvendor = fvendor;
		this.startdate = startdate;
		this.enddate = enddate;
		this.destcompanyid = destcompanyid;
		this.destbookid = destbookid;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	
}
