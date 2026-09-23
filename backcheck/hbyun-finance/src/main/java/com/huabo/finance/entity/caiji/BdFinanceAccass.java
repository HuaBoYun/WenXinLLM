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
 * 会计辅助信息
 * </p>
 *
 * @author L
 * @since 2025-04-20
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    @TableName("BD_FINANCE_ACCASS")
@Schema(name="BdFinanceAccass对象", description="会计辅助信息")
public class BdFinanceAccass implements Serializable {

    private static final long serialVersionUID = 1L;

      @Schema(name = "主键")
        @TableId("PK_ACCASS")
      private String pkAccass;

      @Schema(name = "辅助类型")
      @TableField("ASSTYPE")
    private String asstype;

      @Schema(name = "辅助信息主键")
      @TableField("PK_BUNESSIES")
    private String pkBunessies;

      @Schema(name = "辅助名称")
      @TableField("ASSNAME")
    private String assname;

      @Schema(name = "描述")
      @TableField("ASSDES")
    private String assdes;

    @TableField("ASSLEVEL")
    private String asslevel;

      @Schema(name = "辅助核算项主键")
      @TableField("PK_ACCASSITEM")
    private String pkAccassitem;

      @Schema(name = "描述")
      @TableField("ASSDD")
    private String assdd;

      @Schema(name = "数据来源 0=本级产生;1=上级下发;2=下级上报;3=本级产生已上报下发;-1=系统预置;")
      @TableField("DATAORIGINFLAG")
    private String dataoriginflag;

      @Schema(name = "所属采集方案")
      @TableField("FPLANID")
    private String fplanid;

      @Schema(name = "所属公司")
      @TableField("PK_ORG")
    private String pkOrg;

      @Schema(name = "所属集团")
      @TableField("PK_GROUP")
    private String pkGroup;

      @Schema(name = "创建时间")
      @TableField("CREATIONTIME")
    private Date creationtime;

      @Schema(name = "创建人")
      @TableField("CREATOR")
    private String creator;

      @Schema(name = "修改时间")
      @TableField("MODIFIEDTIME")
    private Date modifiedtime;

      @Schema(name = "修改人")
      @TableField("MODIFIER")
    private String modifier;


}
