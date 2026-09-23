package com.huabo.know.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
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

      @Schema(name = "主键Id 自增")
        @TableId("ATTID")
      private Long attid;

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
