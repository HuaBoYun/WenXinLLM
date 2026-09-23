package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@Schema(name="TblAttachment对象")
public class TblAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name= "主键Id 自增")
        @TableId(value="ATTID",type = IdType.INPUT)
      private BigDecimal attid;

      @Schema(name= "附件名称")
      @TableField("ATTNAME")
    private String attname;

      @Schema(name= "附件路径")
      @TableField("ATTPATH")
    private String attpath;

      @Schema(name= "附件大小")
      @TableField("ATTSIZE")
    private BigDecimal attsize;

      @Schema(name= "备注")
      @TableField("MEMO")
    private String memo;


      @Schema(name= "上传人")
      @TableField("UPLOADER")
    private String uploader;

      @Schema(name= "是否是python爬取文件 0是")
      @TableField("ISPYTHONFLAG")
    private String ispythonflag;
      
      @Schema(name= "加密地址，用于预览")
      @TableField("JMURL")
      private String jmurl;

      
      @Schema(name= "上传时间")
      @TableField("UPLOADTIME")
      @JSONField(format = "yyyy-MM-dd HH:mm:ss")
      @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
      private Date uploadtime;

}
