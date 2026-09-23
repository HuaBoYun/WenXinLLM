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

    @TableField(exist = false)
    private Set<TblNbsjProject> tblAttprojects;

    @TableField(exist = false)
    private Set<TblProjectDataPreEntity> tblProjectDataPre;

    @TableField(exist = false)
    private Set<TblNbsjEntermeetingEntity> tblAttMets;

    @TableField(exist = false)
    private Set<TblNbsjLeavemeetingEntity> tblAttLeaves;
    
}