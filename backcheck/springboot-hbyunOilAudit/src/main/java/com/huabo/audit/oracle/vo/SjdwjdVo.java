package com.huabo.audit.oracle.vo;


import com.huabo.audit.util.BaseVo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Schema(name="列表查询入参")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SjdwjdVo extends BaseVo{
	
	  /**
	 *  
	 */
	private static final long serialVersionUID = 1L;


	@Schema(name = "季度名称")
	private String jdname;
	
	@Schema(name = "填报年度")
	private String queryYear;
	  
	
	@Schema(name = "填报年度")
	private String year;
	  
	  
	 @Schema(name = "填报单位id")
	 private String tbrgid;
	 
	 
	@Schema(name = "名称")
	private String tbname;
	
	@Schema(name = "名称")
	private String name;
}
