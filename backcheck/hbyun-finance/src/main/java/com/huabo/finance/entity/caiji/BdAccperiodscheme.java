package com.huabo.finance.entity.caiji;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 会计期间方案
 * </p>
 *
 * @author L
 * @since 2025-03-28
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_ACCPERIODSCHEME")
@Schema(name="BdAccperiodscheme对象", description="会计期间方案")
public class BdAccperiodscheme implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "期间方案主键")
        @TableId("PK_ACCPERIODSCHEME")
      private String pkAccperiodscheme;

      @Schema(name = "主组织")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "期间方案编码")
      @TableField("CODE")
    private String code;

      @Schema(name = "期间方案名称")
      @TableField("NAME")
    private String name;

      @Schema(name = "期间方案描述")
      @TableField("MEMO")
    private String memo;

      @Schema(name = "分布式字段 0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
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

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;


}
