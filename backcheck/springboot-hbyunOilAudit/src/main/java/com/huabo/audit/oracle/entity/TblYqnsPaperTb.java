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
 * 论文排序
 *
 * @TableName TBL_YQNS_PAPERTB
 */
@TableName("TBL_YQNS_PAPERTB")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsPaperTb extends BaseReservedProperty implements Serializable {

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
    
    
    @Schema(name = "编号")
    @TableField(value = "TBCODE")
    private Integer tbcode;
    
    
    @Schema(name = "编号")
    @TableField(value = "NO")
    private String no;

    @Schema(name = "小组名称")
    @TableField(value = "XZNAME")
    private String xzname;


 
    @Schema(name = "填报单位id")
    @TableField(value = "TBRGID")
    private BigDecimal tbrgid;
 
    @Schema(name = "填报单位名称")
    @TableField(value = "TBRGNAME")
    private String tbrgname;
    
    
    
    @Schema(name = "评委人员id集合")
    @TableField(value = "PWSTAFFIDS")
    private String pwstaffids;
    
    
    @Schema(name = "评委人员名称集合")
    @TableField(value = "PWENAMES")
    private String pwenames;
    
    

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
    
 
    @Schema(name = "组长id")
    @TableField(value = "ZZSTAFFIDS")
    private BigDecimal zsstaffids;

    @Schema(name = "组长名称")
    @TableField(value = "ZZNAMES")
    private String zznames;
     
    
    
    @Schema(name = "副组长id")
    @TableField(value = "FZSTAFFIDS")
    private BigDecimal fsstaffids;

    @Schema(name = "副组长名称")
    @TableField(value = "FZNAMES")
    private String fznames;
    
    
}

