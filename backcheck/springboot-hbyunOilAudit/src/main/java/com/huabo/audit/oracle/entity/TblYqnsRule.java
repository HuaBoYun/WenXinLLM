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
 * 论文评优-规则表
 *
 * @TableName TBL_YQNS_RULE
 */
@TableName("TBL_YQNS_RULE")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsRule implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	
    @Schema(name = "主键")
    @TableId(value = "RULEID")
//    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Id
    private BigDecimal ruleid;

    @Schema(name = "等级类别")
    @TableField(value = "HJTYPE")
    private String hjtype;

  
    
    @Schema(name = "等级比例")
    @TableField(value = "HJBL")
    private Integer hjbl;
    
    @Schema(name = "关联填报主表id")
    @TableId(value = "TBID")
    private BigDecimal tbid;
    

}

