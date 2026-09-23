package com.huabo.system.entity;


import java.math.BigDecimal;

import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 权限修改名称中间表
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Table(name = "TBL_ORG_RIGHT_NEW")
@Schema(name="TblOrgRight")
public class TblOrgRightnew implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@TableField("RIGHTID")
	@Schema(name="权限表主键")
	private BigDecimal rightid;
	@TableField("ORGID")
	@Schema(name="公司主键")
	private BigDecimal orgid;
	@TableField("RIGHTNAME")
	@Schema(name="权限名称")
	private String rightname;
	@TableField("INDICATORSTATUS")
	@Schema(name="是否启用")
	private String indicatorstatus; // 是否启用
	
	
}