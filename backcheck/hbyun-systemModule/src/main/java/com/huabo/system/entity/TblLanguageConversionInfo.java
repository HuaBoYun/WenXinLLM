package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 语义转换信息表
 * </p>
 *
 * @author lhp
 * @since 2025-05-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LANGUAGE_CONVERSION_INFO")
@Schema(name="TblLanguageConversionInfo对象", description="语义转换信息表")
public class TblLanguageConversionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="主键")
      @TableId("INFOID")
    private String infoid;

      @Schema(name="标题")
      @TableField("INFONAME")
    private String infoname;

      @Schema(name="翻译语言")
      @TableField("TARGETLANGUAGE")
    private String targetlanguage;

      @Schema(name="创建人")
      @TableField("CREATESTAFF")
    private BigDecimal createstaff;

      @Schema(name="创建时间")
      @TableField("CREATEDATE")
    private Date createdate;

      @Schema(name="修改人")
      @TableField("MODIFYSTAFF")
    private BigDecimal modifystaff;

      @Schema(name="修改时间")
      @TableField("MODIFYDATE")
    private Date modifydate;


}
