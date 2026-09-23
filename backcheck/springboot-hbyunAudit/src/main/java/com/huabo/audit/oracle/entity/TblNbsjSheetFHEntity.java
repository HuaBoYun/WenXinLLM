package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_SHEET_FH")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjSheetFHEntity {
	
	@TableId(value = "fhid", type= IdType.INPUT)
	@Schema
	private BigDecimal fhid;
	
	@TableField(value = "fhOption")
	@Schema
	private String fhOption;
	
	@TableField(value = "fhDate")
	@Schema
	private Date fhDate;
	
	@TableField(value = "sheetid")
	@Schema
	private String sheetid;
	
	@TableField(value = "staffid")
	@Schema
	private BigDecimal staffid;
	
	@TableField(value = "staffid2")
	@Schema
	private BigDecimal staffid2;
	
    @TableField(value = "projectCode")
	@Schema
	private String projectCode;
    
    @TableField(value = "projectName")
	@Schema
	private String projectName;

}
