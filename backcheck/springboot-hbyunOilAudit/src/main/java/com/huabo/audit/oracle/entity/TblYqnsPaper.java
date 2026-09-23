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
import java.util.List;

import javax.persistence.Id;



/**
 * 论文上报
 *
 * @TableName TBL_YQNS_PAPER
 */
@TableName("TBL_YQNS_PAPER")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsPaper extends BaseReservedProperty implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	
    @Schema(name = "主键")
    @TableId(value = "PERID")
//    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Id
    private BigDecimal perid;
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;
    
    @Schema(name = "序号")
    @TableField(value = "CODE")
    private Integer code;
    

    @Schema(name = "论文名称")
    @TableField(value = "PAPERNAME")
    private String papername;



    @Schema(name = "备注")
    @TableField(value = "REMARKS")
    private String remarks;

 
    @Schema(name = "填报单位id")
    @TableField(value = "TBRGID")
    private BigDecimal tbrgid;
 
    @Schema(name = "填报单位名称")
    @TableField(value = "TBRGNAME")
    private String tbrgname;
    
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
    
    @Schema(name = "状态：0未上报、1已上报")
    @TableField(value = "STATUS")
    private Integer status;
    
    
    @Schema(name = "名次")
    @TableField(value = "RANKING")
    private Integer ranking;
    
    @Schema(name = "合计")
    @TableField(value = "TOTAL")
    private Integer total;
    
    @TableField(exist = false)
    @Schema(name = "关联评委组打分")
    private List<TblYqnsPaperPx> px;
    
    
}

