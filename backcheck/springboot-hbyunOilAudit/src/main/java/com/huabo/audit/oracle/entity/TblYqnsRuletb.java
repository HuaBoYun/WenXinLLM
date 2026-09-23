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
import java.util.List;

import javax.persistence.Id;



/**
 * 论文评优-规则上班
 *
 * @TableName TBL_YQNS_RULETB
 */
@TableName("TBL_YQNS_RULETB")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsRuletb implements Serializable {

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

    @Schema(name = "年度")
    @TableField(value = "RULEYEAR")
    private String ruleyear;

    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;


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
    
    @TableField(exist = false)
    @Schema(name = "等级集合")
    private List<TblYqnsRule> list;

}

