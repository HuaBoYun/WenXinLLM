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
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 附件信息
 * @TableName TBL_ATTACHMENT
 */
@TableName(value ="TBL_ATTACHMENT")
@Data
public class TblAttachment implements Serializable{
    /**
     * 
     */
	@Id
    @TableId(value = "ATTID")
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
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
    
    
    
    @TableField("ISENCRYPTED")
    @Schema(name = "是否加密存储，1表示加密，0表示不加密，默认值为0")
    private Boolean isEncrypted = true;
    
   
    @Schema(name = "加密地址，用于预览")
    @TableField("JMURL")
    private String jmurl;
    
    @TableField(value = "ISPYTHONFLAG")
    @Schema(name="是否是python爬取文件 0是")
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

    @TableField("ATTACHMENTLEVEL")
    @Schema(name = "附件密级")
    private BigDecimal attachmentlevel;

    
    
    
    
    
    
    
    public TblAttachment(){}
    
    
	public TblAttachment(String attname, String attpath, double attsize, String memo, String uploader,
			String ispythonflag, String filename,String contentText, String staffids, BigDecimal attachmentlevel, Boolean isEncrypted, String jmurl) {
		super();
		this.attname = attname;
		this.attpath = attpath;
		this.attsize = attsize;
		this.memo = memo;
		this.uploader = uploader;
		this.ispythonflag = ispythonflag;
		this.filename = filename;
		this.contentText = contentText;
		this.staffids = staffids;
		this.attachmentlevel = attachmentlevel;
		this.isEncrypted=isEncrypted;
		this.jmurl=jmurl;
	}
    
    
    
    
}