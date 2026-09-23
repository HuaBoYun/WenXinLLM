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
 * 立项建议专项评估填报主表
 *
 * @TableName TBL_YQNS_NDTB
 */
@TableName("TBL_YQNS_NDTB")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsLxjytb implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	
    @Schema(name = "主键")
    @TableId(value = "TBID")
//    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Id
    private BigDecimal tbid;

    @Schema(name = "名称")
    @TableField(value = "TBNAME")
    private String tbname;

 


    @Schema(name = "填报单位id")
    @TableField(value = "TBRGID")
    private BigDecimal tbrgid;

    @Schema(name = "填报单位名称")
    @TableField(value = "TBRGNAME")
    private String tbrgname;

    
 
 
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
    
    
    

}

