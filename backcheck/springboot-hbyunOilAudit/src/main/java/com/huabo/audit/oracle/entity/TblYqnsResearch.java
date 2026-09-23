package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.huabo.audit.oracle.entity.base.BaseReservedProperty;
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
 * 理论研究上报-子表数据
 *
 * @TableName TBL_YQNS_RESEARCH
 */
@TableName("TBL_YQNS_RESEARCH")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsResearch extends BaseReservedProperty implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	
    @Schema(name = "主键")
    @TableId(value = "CHID")
//    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Id
    private BigDecimal chid;
    
    
    @Schema(name = "排序")
    @TableField(value = "CODE")
    private Integer code;
    
    

    @Schema(name = "研究方向")
    @TableField(value = "DIRECTION")
    private String direction;


    @Schema(name = "组长id")
    @TableField(value = "ZZSTAFFID")
    private BigDecimal zzstaffid;
    
    @Schema(name = "组长名称")
    @TableField(value = "ZZNAME")
    private String zzname;

    @Schema(name = "研究人员id集合")
    @TableField(value = "YJSTAFFID")
    private String yjstaffid;
    
    @Schema(name = "研究人员名称集合")
    @TableField(value = "YJNAME")
    private String yjname;
 
    @Schema(name = "撰写人id")
    @TableField(value = "ZXRSTAFFID")
    private BigDecimal zxrstaffid;
    
    @Schema(name = "撰写人名称")
    @TableField(value = "ZXRNAME")
    private String zxrname;
    
    

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
    
     
    
    @Schema(name = "备注")
    @TableField(value = "REMARKS")
    private String remarks;
    
    
    @TableField(exist = false)
    @Schema(name = "上报单位")
    private String tborgname;
    
    
    
}

