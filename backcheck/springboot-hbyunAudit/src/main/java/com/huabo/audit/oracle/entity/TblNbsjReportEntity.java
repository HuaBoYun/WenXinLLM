package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("TBL_NBSJ_REPORT")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjReportEntity {

	public final Integer STATUS_1= 1;//作废
	public final Integer STATUS_0= 0;//正常
	
	@TableId(value = "projectid", type= IdType.INPUT)
    @Schema
    private BigDecimal reportid;

    @TableField(exist = false)
    @Schema
    private TblStaff tblStaff;

    @TableField(exist = false)
    @Schema
    private TblNbsjProject tblNbsjProject;
    
    @TableField(value = "reportcode")
    @Schema
    private String reportcode;
    
    @TableField(value = "reportname")
    @Schema
    private String reportname;
    
    @TableField(value = "createtime")
    @Schema
    private Date createTime;

    @TableField(value = "updatetime")
    @Schema
    private Date updateTime;
    
	@TableField(value = "qualityadvice")
    @Schema
    private String qualityadvice;
	
	@TableField(value = "dempadvice")
    @Schema
    private String dempadvice;
	
	@TableField(value = "examineadice")
    @Schema
    private String examineadice;
	
	@TableField(value = "docurl")
    @Schema
    private String docurl;
	
	@TableField(value = "status")
    @Schema
    private BigDecimal status;

    @TableField(exist = false)
    @Schema
    private Set<TblAttachment> tblNbsjReportatts;
}
