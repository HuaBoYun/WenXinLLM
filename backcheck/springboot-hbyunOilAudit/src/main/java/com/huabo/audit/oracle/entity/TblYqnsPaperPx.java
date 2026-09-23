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
 * 论文排序-评委评分表
 *
 * @TableName TBL_YQNS_PAPERPX
 */
@TableName("TBL_YQNS_PAPERPX")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsPaperPx implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	
    @Schema(name = "主键")
    @TableId(value = "PXID")
//    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Id
    private BigDecimal pxid;
    

    @Schema(name = "评委人员id")
    @TableField(value = "PWSTAFFID")
    private BigDecimal pwstaffid;
    
    @Schema(name = "评委人员名称")
    @TableField(value = "PWNAME")
    private String pwname;



    @Schema(name = "创建时间")
    @TableField(value = "CREATEDATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdate;
    
    @Schema(name = "分数")
    @TableField(value = "FS")
    private Integer fs;
    
    
    @Schema(name = "关联论文上报id")
    @TableField(value = "PERID")
    private BigDecimal perid;
    
    @TableField(exist = false)
    @Schema(name = "合计")
    private Integer total;
    
    
    
}

