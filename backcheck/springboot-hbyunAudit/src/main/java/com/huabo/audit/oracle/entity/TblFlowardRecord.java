package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * 整改落实转发表
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_FORWARD_RECORD")
@Table(name = "TBL_FORWARD_RECORD")
@Schema(name="TblFlowardRecord对象")
public class TblFlowardRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId("RECORDID")
    @Column(name = "RECORDID")
    @Id
    private BigDecimal recordid;

    
    @Schema(name = "转发人id")
    @TableField("ZFSTAFFID")
    @Column(name = "ZFSTAFFID")
    private String zfstaffid;
    
    @Schema(name = "转发人名称")
    @TableField("ZFRENAME")
    @Column(name = "ZFRENAME")
    private String zfrename;
    
    
    @Schema(name = "接收人id")
    @TableField("JSSTAFFID")
    @Column(name = "JSSTAFFID")
    private String jsstaffid;
    
    @Schema(name = "接收人名称")
    @TableField("JSRENAME")
    @Column(name = "JSRENAME")
    private String jsrename;
    
    @Schema(name = "关联整改方案和整改问题中间表主键")
    @TableField("FORMID")
    @Column(name = "FORMID")
    private BigDecimal formid;
    
    @TableField("CREATEDATE")
    @Column(name = "CREATEDATE")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createdate;
 

}
