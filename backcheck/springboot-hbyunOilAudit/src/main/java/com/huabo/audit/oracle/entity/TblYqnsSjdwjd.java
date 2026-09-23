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
import javax.persistence.Transient;



/**
 * 三级单位离任审计季度表
 *
 * @TableName TBL_YQNS_XMQD
 */
@TableName("TBL_YQNS_LEAVE_AUDIT_JD3L")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblYqnsSjdwjd extends BaseReservedProperty implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	
    @Schema(name = "主键")
    @TableId(value = "JDID")
//    @KeySql(sql = "select HIBERNATE_SEQUENCE.nextval from dual", order= ORDER.DEFAULT)
//    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Id
    private BigDecimal jdid;

    @Schema(name = "季度名称")
    @TableField(value = "JDNAME")
    private String jdname;

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
    
    
    
    @Schema(name = "下发人员名称")
    @TableField(value = "XFRYNAMES")
    private String xfrynames;
     
    
    @Schema(name = "下发人员ids")
    @TableField(value = "XFRYIDS")
    private String xfryids;
    
    @Schema(name = "关联内容")
    @TableField(exist = false)
    @Transient
    private List<LeaveAudit3LEntity>  list;

}

