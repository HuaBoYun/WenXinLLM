package com.huabo.contract.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @author huabo
 * @since 2021-10-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("TBL_ATTACHMENT")
@Schema(name="TblAttachment对象")
public class TblAttachment extends com.hbfk.entity.TblAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键Id 自增")
      @TableId(value = "ATTID",type = IdType.INPUT)
      private BigDecimal attid;

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

      @TableField(exist=false,fill=FieldFill.DEFAULT)
      private String filename;
      
      @TableField(exist=false,fill=FieldFill.DEFAULT)
      private String contentText;
}
