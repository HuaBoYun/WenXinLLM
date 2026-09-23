package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 合同类型维护表。
 *
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("TBL_CONTRACT_TYPEOF")
@Schema(name="TblContractTypeof")
public class TblContractTypeof implements Serializable{
	private static final long serialVersionUID = 6312252984564842416L;
	@TableId(value="TYPEID",type = IdType.INPUT)
	private BigDecimal typeId;
	@TableField("TYPENAME")
	private String typeName;
	@TableField("CREATESTAFF")
	private BigDecimal createStaff;
	@TableField("CREATETIME")
	private Date createTime;
	@TableField("ORGID")
	private BigDecimal orgId;
	@TableField("UPDATESTAFF")
	private BigDecimal updateStaff;
	@TableField("UPDATETIME")
	private Date updateTime;
	@TableField("PAGEURL")
	private String pageUrl;
	@TableField("PARENTID")
	private BigDecimal parentid;
	
	@TableField(exist=false)
	private boolean hasChildren;
	
	
	@TableField(exist = false)
	private TblContractTypeof fatherType;
	@TableField(exist = false)
	private List<TblContractTypeof> childrenList = new ArrayList<TblContractTypeof>(0);
	@TableField("SETTINGID")
	private String settingid;//流程id
	
	@TableField(exist = false)
	private List<TblContractTypeof> children = new ArrayList<TblContractTypeof>(0);
	
	public TblContractTypeof() {
		
	}
	
	public TblContractTypeof(BigDecimal typeId) {
		this.typeId = typeId;
	}


}
