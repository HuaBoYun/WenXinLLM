package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Id;



/**
 * 项目评优-获奖通知
 */
@TableName("TBL_YQNS_XMHJTZ")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsXmHjtz implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @Schema(name = "主键")
    @TableId(value = "RYID")
    @Id
    private BigDecimal ryid;

    @Schema(name = "名称")
    @TableField(value = "TITLE")
    private String title;

    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @Schema(name = "描述")
    @TableField(value = "RYCONTENT")
    private String rycontent;

    @Schema(name = "创建人")
    @TableField(value = "CREATESTAFFID")
    private BigDecimal createstaffid;
    
    @Schema(name = "创建人名称")
    @TableField(value = "CREATENAME")
    private String createname;

    @Schema(name = "创建时间")
    @TableField(value = "CREATEDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;
    
    @Schema(name = "状态")
    @TableField(value = "STATUS")
    private Integer status;
    
    @Schema(name = "下发人员名称")
    @TableField(value = "XFRYNAMES")
    private String xfrynames;
    
    @Schema(name = "下发人员ids")
    @TableField(value = "XFRYIDS")
    private String xfryids;
    
}

