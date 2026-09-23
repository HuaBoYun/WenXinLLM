package com.huabo.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 账簿信息表TBL_CUBE_YS202201
 * @author Lenovo
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_CUBE_YS202201")
@Schema(name="TBL_CUBE_YS202201", description="")
public class TblCubeYS202201 {

//	@TableId(value="TCYID",type = IdType.INPUT)
	@TableField("TCYID")
	@Schema(name= "主键")
	private BigDecimal tcyId;

	@TableField("CODEBMMEATYPE")
	@Schema(name= "介质类型")
	private String codebmmeatype;

	@TableField("VALUE")
	@Schema(name= "数值")
	private String value;

	@TableField("CODEMEASURE")
	@Schema(name= "")
	private String codemeasure;

	@TableField("CODEENTITY")
	@Schema(name= "实体代码")
	private String codeentity;

	@TableField("CODEVERSION")
	@Schema(name= "版本")
	private String codeversion;

	@TableField("CODEMVTYPE")
	@Schema(name= "")
	private String codemvtype;

}
