package com.huabo.legal.oracle.entity;




import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 附件信息
 * @TableName TBL_ATTACHMENT
 */
@TableName(value ="TBL_ATTACHMENT")
@Data
public class TblAttachment implements Serializable {
    /**
     * 
     */
	@Id
    @TableId(value = "ATTID")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    private BigDecimal attid;

    /**
     * 
     */
    @TableField(value = "ATTNAME")
    private String attname;

    /**
     * 
     */
    @TableField(value = "ATTPATH")
    private String attpath;

    /**
     * 
     */
    @TableField(value = "ATTSIZE")
    private double attsize;

    /**
     * 
     */
    @TableField(value = "MEMO")
    private String memo;

    /**
     * 
     */
    @TableField(value = "UPLOADTIME")
    private Date uploadtime;

    /**
     * 
     */
    @TableField(value = "UPLOADER")
    private String uploader;

    /**
     * 
     */
    @TableField(value = "ISPYTHONFLAG")
    private String ispythonflag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
    
    @Transient
    private String filename;
    @Transient
    private String contentText;
    
    @TableField("STAFFIDS")
    @Schema(name = "审计项目资料下发给组员字段")
    private String staffids;
    
}