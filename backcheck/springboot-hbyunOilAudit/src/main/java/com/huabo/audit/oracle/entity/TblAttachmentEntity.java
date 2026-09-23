package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * 附件信息
 * @TableName TBL_ATTACHMENT
 */
@TableName(value ="TBL_ATTACHMENT")
@Data
public class TblAttachmentEntity implements Serializable {
    /**
     * 
     */
    @TableId(value = "ATTID")
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
    private BigDecimal attsize;

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
    
}