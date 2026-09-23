package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

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
    
    @TableField(value = "tblAttprojects")
    private Set<TblNbsjProject> tblAttprojects;
    
    @TableField(value = "tblProjectDataPre")
    private Set<TblProjectDataPreEntity> tblProjectDataPre;
    
    @TableField(value = "tblAttMets")
    private Set<TblNbsjEntermeetingEntity> tblAttMets;
    
    @TableField(value = "tblAttLeaves")
    private Set<TblNbsjLeavemeetingEntity> tblAttLeaves;
    
    @Transient
    private String filename;
    @Transient
    private String contentText;
    
    @TableField("STAFFIDS")
    @Schema(name = "审计项目资料下发给组员字段")
    private String staffids;

    @TableField(exist = false)
    @Schema(name = "附件类型：1-原始上传文件 2-科长文件 3-副主任文件")
    private String type;
    
}