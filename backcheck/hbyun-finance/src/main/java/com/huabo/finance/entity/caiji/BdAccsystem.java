package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会计科目体系
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCSYSTEM")
@Schema(name="BdAccsystem对象", description="会计科目体系")
public class BdAccsystem implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
      @TableField("PK_ACCSYSTEM")
    private String pkAccsystem;

      @Schema(name = "科目体系编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "科目体系名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "科目编码规则")
      @TableField("ACCCODERULE")
    private String acccoderule;

      @Schema(name = "备注")
      @TableField("MEMO")
    private String memo;

      @Schema(name = "科目类型")
      @TableField("ACCTYPES")
    private String acctypes;

      @Schema(name = "创建组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "创建集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "数据来源 	0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "最后修改人")
      @TableField("MODIFIER")
    private String modifier;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
    private Date creationtime;

      @Schema(name = "最后修改时间")
      @TableField("MODIFIEDTIME")
    private Date modifiedtime;


}
