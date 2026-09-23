package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 时区
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_TIMEZONE")
@Schema(name="BdTimezone对象", description="时区")
public class BdTimezone implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      @TableField("PK_TIMEZONE")
    private String pkTimezone;

      @Schema(name = "编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "偏移量")
      @TableField("OFFSETVAR")
    private String offsetvar;

      @Schema(name = "描述")
      @TableField("DESCRIPTION")
    private String description;


}
