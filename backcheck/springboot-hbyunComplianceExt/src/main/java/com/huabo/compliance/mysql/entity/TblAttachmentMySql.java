package com.huabo.compliance.mysql.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ATTACHMENT")
@Schema(name="TblAttachmentMySql对象")
public class TblAttachmentMySql implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键Id 自增")
    @TableId("ATTID")
    private BigDecimal attid;

    @Schema(name = "附件名称")
    @TableField("ATTNAME")
    private String attname;

    @Schema(name = "附件路径")
    @TableField("ATTPATH")
    private String attpath;

    @Schema(name = "附件大小")
    @TableField("ATTSIZE")
    private BigDecimal attsize;

    @Schema(name = "备注")
    @TableField("MEMO")
    private String memo;

    @Schema(name = "上传时间")
    @TableField("UPLOADTIME")
    private LocalDateTime uploadtime;

    @Schema(name = "上传人")
    @TableField("UPLOADER")
    private String uploader;

    @Schema(name = "是否是python爬取文件 0是")
    @TableField("ISPYTHONFLAG")
    private String ispythonflag;


}
