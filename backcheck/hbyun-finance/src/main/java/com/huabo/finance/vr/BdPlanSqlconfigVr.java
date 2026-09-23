package com.huabo.finance.vr;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.finance.entity.BdFinancedateRecord;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(name="BdPlanSqlconfig对象", description="sql配置信息返回参数")
public class BdPlanSqlconfigVr implements Serializable {

	private static final long serialVersionUID = 1L;

	 @Schema(name = "主键")
	 private String fid;
	
	 @Schema(name = "sql标题")
	 private String fname;
	
	 @Schema(name = "sql内容")
	 private String fsql;
	
	 @Schema(name = "关联方案")
	 private String fplanid;
	 
	 @Schema(name = "特定where条件，不创建在原生sql")
	 private String specificityCol;
	
	 @Schema(name = "初始化sql信息")
	 private String finitsqlid;
	
	 @Schema(name = "所属公司")
	 private BigDecimal linkorgid;
	 
	 @Schema(name = "增量标识列")
	 private String incrementcol;
	
	 @Schema(name = "所属部门")
	 private BigDecimal linkdetpid;
	
	 @Schema(name = "创建人")
	 private BigDecimal creator;
	
	 @Schema(name = "修改人")
	 private BigDecimal modifier;
	
	 @Schema(name = "创建时间")
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	 private Date creationtime;
	
	 @Schema(name = "修改时间")
	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	 private Date modifiedtime;
	   
	 @Schema(name = "sql初始化配置主键")
	 private String sqlconfigid;
	 
	 @Schema(name = "sql初始化配置名称")
	 private String sqlconfigname;
	 
	 @Schema(name = "初始配置sql")
	 private String initsql;
	 
	 @Schema(name = "所属财务版本")
	 private String configVersionId;
	 
	 @Schema(name = "关联方案")
	 private String finitPlanid;
	 
	 @Schema(name = "sql初始化配置增量标识列")
	 private String finitincrementcol;
	 
	 @Schema(name = "sql初始化配置特定where条件，不创建在原生sql")
	 private String configSpecificityCol;
	 
	 
	 @Schema(name = "采集信息主键")
	 private BdFinancedateRecordVr bfrv;
	 
	 @Schema(name = "采集信息主键")
	 private BdFinancedateRecord fr;
	 
	 @Schema(name = "初始化采集信息表名")
	 private String configTableName;

	 @Schema(name = "初始化配置财务数据表的主键列")
	 private String initPrimaryCol;
}
