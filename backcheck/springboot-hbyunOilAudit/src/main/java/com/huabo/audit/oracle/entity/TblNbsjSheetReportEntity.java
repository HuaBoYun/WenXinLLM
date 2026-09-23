package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_SHEET_REPORT")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjSheetReportEntity {
	
	@TableId(value = "reportid", type= IdType.AUTO)
	@Schema
	private Integer reportid;
	
	@TableField(value = "tblNbsjSheet")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private TblNbsjSheetEntity tblNbsjSheet;
	
	@TableField(value = "reportcontent")
	@Schema
	private String reportConcent;
	
	@TableField(value = "reportorgids")
	@Schema
	private String sjdeptIds;
	
	@TableField(value = "reportorgname")
	@Schema(hidden=true)
	private String sjdeptNames;
	
	@TableField(value = "SHEETID")
	@Schema
	private Integer sheetId;
	

}
