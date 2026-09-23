package com.huabo.audit.oracle.vo;


import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Schema(name="项目启动列表查询入参")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmdqVo extends BaseVo{
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Schema(name = "项目名称")
	private String xmname;
	  
	
	@Schema(name = "标题")
	private String title;
	  
	 @Schema(name = "项目类型")
	 private String xmtype;
	 
	 @Schema(name = "项目年度")
	 private Integer xmnd;
	 
	 @Schema(name = "人员主键")
	 private BigDecimal staffId;
	 
	 @Schema(name = "填报年度")
	private String year;
	 
	 @Schema(name = "校验是否根据权限查询")
	 @TableField(exist=false)
	 private Integer checkRight;
	 
	 @Schema(name = "文号")
	 private String document;
	 
	 @Schema(name = "启动编号")
	private String qdcode;
}
