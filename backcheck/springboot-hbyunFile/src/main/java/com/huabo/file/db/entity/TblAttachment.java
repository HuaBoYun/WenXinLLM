package com.huabo.file.db.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
@TableName("TBL_ATTACHMENT")
@Schema(name="TblAttachment对象")
public class TblAttachment  {


    @Schema(name = "主键Id 自增")
    @TableId(value = "ATTID")
    private Long attid;

    @Schema(name = "附件名称")
    @TableField("ATTNAME")
    private String attname;

    @Schema(name = "附件路径")
    @TableField("ATTPATH")
    private String attpath;

    @Schema(name = "附件大小")
    @TableField("ATTSIZE")
    private double attsize;

    @Schema(name = "备注")
    @TableField("MEMO")
    private String memo;

    @Schema(name = "上传时间")
    @TableField("UPLOADTIME")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date uploadtime;

    @Schema(name = "上传人")
    @TableField("UPLOADER")
    private String uploader;

    @Schema(name = "是否是python爬取文件 0是")
    @TableField("ISPYTHONFLAG")
    private String ispythonflag;

    @TableField("ISENCRYPTED")
    @Schema(name = "是否加密存储，1表示加密，0表示不加密，默认值为0")
    private Boolean isEncrypted = true;
    
    
    @Schema(name = "加密地址，用于预览")
    @TableField("JMURL")
    private String jmurl;
    
    @Schema(name="附件密级")
    @TableField("ATTACHMENTLEVEL")
    private BigDecimal attachmentlevel;

}
