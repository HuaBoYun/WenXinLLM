package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 语言翻译表
 * </p>
 *
 * @author lhp
 * @since 2025-05-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LANGUAGE_TRANSLATE")
@Schema(name="TblLanguageTranslate对象", description="语言翻译表")
public class TblLanguageTranslate implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="语义转换信息表主键")
      @TableField("INFOID")
    private String infoid;

      @Schema(name="基础语言配置表主键")
      @TableField("CONFIGID")
    private String configid;

      @Schema(name="转换文本")
      @TableField("TRANTEXT")
    private String trantext;

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
