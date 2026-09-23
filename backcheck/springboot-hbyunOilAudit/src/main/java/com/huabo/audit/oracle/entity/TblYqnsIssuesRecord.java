package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 央企模块-审计整改-问题整改
 * @TableName TBL_YQNS_SJZG_WTZG
 */
@TableName(value ="TBL_YQNS_ISSUESRECORD")
@Data
@Schema(name="问题清单整改记录维护表")
public class TblYqnsIssuesRecord implements Serializable {

	private static final long serialVersionUID = 1L;

    @Schema(name = "整改记录主键")
    @TableId(value = "RECORDID",type = IdType.INPUT)
    private BigDecimal recordId;

    @Schema(name = "问题清单主键")
    @TableField(value = "ISSUESID")
    private BigDecimal issuesId;
    
    @Schema(name = "问题清单主键")
    @TableField(value = "WTZGID")
    private BigDecimal wtzgId;

    @Schema(name = "整改人主键")
    @TableField(value = "RECTPERSON")
    private BigDecimal rectPerson;
    
    @TableField(value = "RECTPERSONNAME")
    @Schema(name = "整改人姓名")
    private String rectPersonName;
    
    @TableField(value = "CREATETIME")
    @Schema(name = "创建时间")
    private Date createTime;
    
    @Schema(name = "版本信息")
    @TableField(value = "VERSION")
    private Integer version;
   
}