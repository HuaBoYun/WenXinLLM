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
 * 基础语言配置表
 * </p>
 *
 * @author lhp
 * @since 2025-05-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("TBL_LANGUAGE_BASIC_CONFIG")
@Schema(name="TblLanguageBasicConfig对象", description="基础语言配置表")
public class TblLanguageBasicConfig implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name="主键")
      @TableId("CONFIGID")
    private String configid;

      @Schema(name="菜单名称（翻译前的）")
      @TableField("MENUNAME")
    private String menuname;

      @Schema(name="创建人")
      @TableField("CREATESTAFF")
    private BigDecimal createstaff;

      @Schema(name="创建时间")
      @TableField("CREATETIME")
    private Date createtime;

      @Schema(name="修改人")
      @TableField("MODIFYSTAFF")
    private BigDecimal modifystaff;

      @Schema(name="修改时间")
      @TableField("MODIFYTIME")
    private Date modifytime;

      @Schema(name="数据来源 -2系统预制 ，1用户新增")
      @TableField("CONFIGTYPE")
    private Integer configtype;

      
      @Schema(name="翻译文本（翻译后的）")
      @TableField(exist=false)
    private String trantext;
      
      @Schema(name="语义转换信息表主键")
      @TableField(exist=false)
    private String infoid;

}
