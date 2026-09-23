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
 * 科目对照
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCOUNTMAP")
@Schema(name="BdAccountmap对象", description="科目对照")
public class BdAccountmap implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "科目对照主键")
      @TableField("PK_ACCMAP")
    private String pkAccmap;

      @Schema(name = "新版本科目主键")
      @TableField("PK_NEWACCOUNT")
    private String pkNewaccount;

      @Schema(name = "旧版本科目主键")
      @TableField("PK_OLDACCOUNT")
    private String pkOldaccount;

      @Schema(name = "所属科目表")
      @TableField("PK_ACCCHART")
    private String pkAccchart;

      @Schema(name = "科目表旧版本")
      @TableField("PK_OLDCHART")
    private String pkOldchart;

      @Schema(name = "旧版本科目历史表主键")
      @TableField("PK_OLDACCOUNTHIS")
    private String pkOldaccounthis;

      @Schema(name = "对照状态 0=已对照;1=未对照; 默认1")
      @TableField("MAPSTATUS")
    private String mapstatus;


}
