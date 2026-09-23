package com.hbfk.entity;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "账簿信息实体")
public class FaAccbookinfoUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	  @Schema(description = "主键")
	  private String pkAccbookinfo;

	  @Schema(description = "账簿_账簿类型")
	  private String pkSetofbook;
	
	  @Schema(description = "外币折算日期   	0=卡片建卡日期;1=业务发生日期;")
	  private Integer convertDate;
	
	  @Schema(description = "本币原值来源")
	  private String localoriginvalue;
	
	  @Schema(description = "明细")
	  private String bodyvos;
	    
	  @Schema(description = "账簿名称")
	  private String bookName;
	
	  @Schema(description = "数据采集方案")
	  private String pkFinanplanid;
	
	  @Schema(description = "账簿类别编码")
	  private String accbooktypecode;
	
	  @Schema(description = "账簿类别名称")
	  private String accbooktypename;
	    
}
