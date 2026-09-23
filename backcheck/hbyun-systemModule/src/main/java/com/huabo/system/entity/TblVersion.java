package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_VERSION")
@Schema(name="TblVersion", description="")
public class TblVersion implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value="FID",type = IdType.INPUT)
	private BigDecimal fid;
	@TableField("FVENDOR")
	private String fvendor;//厂商:1.金蝶    2.用友
	@TableField("DATABASETYPE")
	private String databaseType;//Bathdata数据库类型
	@TableField("FATHERID")
	private BigDecimal fatherid;

	private List<TblVersion> chiVerList = new ArrayList<TblVersion>();//

}
