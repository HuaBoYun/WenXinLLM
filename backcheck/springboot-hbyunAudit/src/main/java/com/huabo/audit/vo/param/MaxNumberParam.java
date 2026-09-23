package com.huabo.audit.vo.param;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
 
@Data
public class MaxNumberParam{
   
	@Schema(name = "表名称")
    private String tblName; 
	
	@Schema(name = "查询字段")
    private String column; 
 
	@Schema(name = "父级字段")
    private String orgCol; 

 	@Schema(name = "组织id")
    private BigDecimal orgid; 

   	@Schema(name = "节点id")
    private Integer NoId; 

   	@Schema(name = "子表关联字段")
    private String chChoiceCol;//报告部门

	@Schema(name = "查询条件")
    private String choiceVal;

	@Schema(name = "查询条件")
    private String bjf;
	
	@Schema(name = "查询条件")
    private String noCode;
	
	@Schema(name = "查询条件")
    private String sep;
	
	@Schema(name = "查询条件:组织架构范围")
	private List<String> orgList;
	
	@Schema(name = "查询条件")
    private String jgf;
 
}
