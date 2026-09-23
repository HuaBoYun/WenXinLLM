package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_BI_USER_PAGE")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="TBL_BI_USER_PAGE", description="用户主题关系表")
public class TblBiUserPage implements Serializable {
	private static final long serialVersionUID = 5703236299273528243L;

	@TableId(value="PAGEID",type = IdType.INPUT)
	@Schema(name= "报表逐渐")
	private BigDecimal pageId;
	
	@TableField("STAFFID")
	@Schema(name= "员工主键")
	private BigDecimal staffId;
	
	@TableField("SORT")
	@Schema(name= "排序字段")
	private Integer sort;
	
	@TableField("PAGETYPE")
	@Schema(name= "报表类型")
	private Integer pageType;
}
